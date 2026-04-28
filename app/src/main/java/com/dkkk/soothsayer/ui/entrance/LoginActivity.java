package com.dkkk.soothsayer.ui.entrance;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.dkkk.soothsayer.R;
import com.dkkk.soothsayer.ui.HomeActivity;
import com.dkkk.soothsayer.viewmodel.entrance.LoginViewModel;

/**
 * Экран авторизации пользователя.
 *
 * Отвечает за:
 * - ввод логина и пароля
 * - обработку авторизации через LoginViewModel
 * - отображение ошибок ввода и сервера
 * - навигацию на экран регистрации и главный экран
 *
 * Реализует реактивную модель через LiveData.
 */
public class LoginActivity extends AppCompatActivity {

    /** Поле ввода логина */
    private EditText etLogin;

    /** Поле ввода пароля */
    private EditText etPassword;

    /** Кнопка входа в систему */
    private Button btnLogin;

    /** Кнопка перехода на регистрацию */
    private Button btnRegister;

    /** ViewModel, содержащий бизнес-логику авторизации */
    private LoginViewModel viewModel;

    /** Цвет ошибки (используется для подсветки полей) */
    private final int errorColor = Color.parseColor("#4A1212");

    /** Стандартный цвет текста */
    private final int defaultTextColor = Color.WHITE;

    /** Исходная заливка поля ввода (для восстановления состояния) */
    private ColorStateList defaultTint;

    /** Исходный цвет обводки кнопки входа */
    private int defaultStrokeColor;

    /**
     * Инициализация активности.
     *
     * Создаёт UI, подключает ViewModel и подписывается на LiveData:
     * - успешный вход
     * - ошибки сервера
     * - ошибки формы
     *
     * @param savedInstanceState сохранённое состояние активности
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        initViews();
        initViewModel();
        initUiState();
        setupObservers();
        setupClickListeners();
        setupFocusReset();
    }

    /**
     * Инициализация UI компонентов.
     */
    private void initViews() {

        etLogin = findViewById(R.id.VLogin);
        etPassword = findViewById(R.id.VPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        defaultTint = etLogin.getBackgroundTintList();

        defaultStrokeColor =
                ((com.google.android.material.button.MaterialButton) btnLogin)
                        .getStrokeColor()
                        .getDefaultColor();
    }

    /**
     * Инициализация ViewModel.
     */
    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }

    /**
     * Начальная настройка UI состояния (цвета, стили).
     */
    private void initUiState() {
        // резерв под расширение (например, темная тема / автозаполнение)
    }

    /**
     * Подписка на LiveData из ViewModel.
     *
     * Обрабатывает:
     * - успешную авторизацию
     * - ошибки сервера
     * - ошибки формы
     */
    private void setupObservers() {

        // Успешный вход
        viewModel.getLoginSuccess().observe(this, success -> {
            if (success) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            }
        });

        // Ошибки сервера или логики
        viewModel.getErrorMessage().observe(this, msg -> {
            if (msg != null) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        // Ошибки валидации формы
        viewModel.getFormError().observe(this, error -> {
            setFieldState(etLogin, error);
            setFieldState(etPassword, error);
            setButtonStroke(error);
        });
    }

    /**
     * Настройка обработчиков кнопок.
     */
    private void setupClickListeners() {

        // Авторизация пользователя
        btnLogin.setOnClickListener(v -> viewModel.login(
                etLogin.getText().toString().trim(),
                etPassword.getText().toString().trim()
        ));

        // Переход на регистрацию
        btnRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }

    /**
     * Сброс визуального состояния полей при фокусе.
     */
    private void setupFocusReset() {

        etLogin.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) resetField(etLogin);
        });

        etPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) resetField(etPassword);
        });
    }

    /**
     * Изменяет визуальное состояние поля ввода в зависимости от ошибки.
     *
     * @param et поле ввода
     * @param error true — ошибка, false — нормальное состояние
     */
    private void setFieldState(EditText et, Boolean error) {

        if (error == null) return;

        if (error) {
            et.setBackgroundTintList(ColorStateList.valueOf(errorColor));
            et.setTextColor(errorColor);
            et.setHintTextColor(errorColor);
        } else {
            et.setBackgroundTintList(defaultTint);
            et.setTextColor(defaultTextColor);
            et.setHintTextColor(defaultTextColor);
        }
    }

    /**
     * Изменяет обводку кнопки входа при ошибке формы.
     *
     * @param error true — ошибка, false — нормальное состояние
     */
    private void setButtonStroke(Boolean error) {

        if (error == null) return;

        com.google.android.material.button.MaterialButton btn =
                (com.google.android.material.button.MaterialButton) btnLogin;

        if (error) {
            btn.setStrokeColor(ColorStateList.valueOf(errorColor));
        } else {
            btn.setStrokeColor(ColorStateList.valueOf(defaultStrokeColor));
        }
    }

    /**
     * Сброс визуального состояния поля ввода.
     *
     * @param et поле ввода
     */
    private void resetField(EditText et) {
        et.setBackgroundTintList(defaultTint);
        et.setTextColor(defaultTextColor);
        et.setHintTextColor(defaultTextColor);
    }
}