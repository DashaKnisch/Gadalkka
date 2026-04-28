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
import com.dkkk.soothsayer.viewmodel.entrance.RegisterViewModel;
import com.google.android.material.button.MaterialButton;

/**
 * Экран регистрации нового пользователя.
 *
 * Отвечает за:
 * - ввод регистрационных данных (логин, email, пароль)
 * - отправку данных через RegisterViewModel
 * - отображение ошибок формы и сервера
 * - навигацию обратно на экран авторизации
 *
 * Использует реактивную модель LiveData.
 */
public class RegisterActivity extends AppCompatActivity {

    /** Поле ввода логина */
    private EditText etLogin;

    /** Поле ввода email */
    private EditText etEmail;

    /** Поле ввода пароля */
    private EditText etPassword;

    /** Кнопка регистрации пользователя */
    private MaterialButton btnRegister;

    /** Кнопка возврата на экран входа */
    private Button btnBack;

    /** ViewModel, содержащий бизнес-логику регистрации */
    private RegisterViewModel viewModel;

    /** Цвет ошибки для UI-валидации */
    private final int errorColor = Color.parseColor("#4A1212");

    /** Стандартный цвет текста */
    private final int defaultTextColor = Color.WHITE;

    /** Исходная заливка полей ввода */
    private ColorStateList defaultTint;

    /** Исходный цвет обводки кнопки */
    private int defaultStrokeColor;

    /**
     * Инициализация активности регистрации.
     *
     * Выполняет:
     * - связывание UI элементов
     * - инициализацию ViewModel
     * - подписку на LiveData состояния регистрации
     * - настройку обработчиков событий
     *
     * @param savedInstanceState сохранённое состояние активности
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        initViews();
        initViewModel();
        setupObservers();
        setupClickListeners();
        setupFocusReset();
    }

    /**
     * Инициализация UI элементов и сохранение дефолтных стилей.
     */
    private void initViews() {

        etLogin = findViewById(R.id.RLogin);
        etEmail = findViewById(R.id.RMail);
        etPassword = findViewById(R.id.RPassword);

        btnRegister = findViewById(R.id.btnLogin);
        btnBack = findViewById(R.id.btnBack);

        defaultTint = etLogin.getBackgroundTintList();

        defaultStrokeColor = btnRegister.getStrokeColor().getDefaultColor();
    }

    /**
     * Инициализация ViewModel.
     */
    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
    }

    /**
     * Подписка на LiveData из ViewModel.
     *
     * Обрабатывает:
     * - успешную регистрацию
     * - ошибки сервера
     * - ошибки валидации формы
     */
    private void setupObservers() {

        // Успешная регистрация
        viewModel.getRegisterSuccess().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Ошибки сервера / логики
        viewModel.getErrorMessage().observe(this, msg -> {
            if (msg != null) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        // Ошибки формы
        viewModel.getFormError().observe(this, error -> {
            setFieldState(etLogin, error);
            setFieldState(etEmail, error);
            setFieldState(etPassword, error);
            setButtonStroke(error);
        });
    }

    /**
     * Настройка кнопок управления экраном.
     */
    private void setupClickListeners() {

        // Регистрация пользователя
        btnRegister.setOnClickListener(v -> viewModel.register(
                etLogin.getText().toString().trim(),
                etEmail.getText().toString().trim(),
                etPassword.getText().toString().trim()
        ));

        // Возврат на экран логина
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    /**
     * Сброс визуального состояния полей при получении фокуса.
     */
    private void setupFocusReset() {

        etLogin.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) resetField(etLogin);
        });

        etEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) resetField(etEmail);
        });

        etPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) resetField(etPassword);
        });
    }

    /**
     * Устанавливает визуальное состояние поля ввода в зависимости от ошибки.
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
     * Обновляет визуальное состояние кнопки регистрации при ошибке формы.
     *
     * @param error true — ошибка, false — нормальное состояние
     */
    private void setButtonStroke(Boolean error) {

        if (error == null) return;

        if (error) {
            btnRegister.setStrokeColor(ColorStateList.valueOf(errorColor));
        } else {
            btnRegister.setStrokeColor(ColorStateList.valueOf(defaultStrokeColor));
        }
    }

    /**
     * Сбрасывает визуальное состояние поля ввода.
     *
     * @param et поле ввода
     */
    private void resetField(EditText et) {
        et.setBackgroundTintList(defaultTint);
        et.setTextColor(defaultTextColor);
        et.setHintTextColor(defaultTextColor);
    }
}