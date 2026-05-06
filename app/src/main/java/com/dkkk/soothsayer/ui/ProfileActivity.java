package com.dkkk.soothsayer.ui;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.dkkk.soothsayer.R;
import com.dkkk.soothsayer.repository.UserRepository;
import com.dkkk.soothsayer.ui.entrance.LoginActivity;
import com.dkkk.soothsayer.ui.library.FavoriteActivity;
import com.dkkk.soothsayer.ui.library.LibraryActivity;
import com.dkkk.soothsayer.viewmodel.ProfileViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

/**
 * Активность профиля пользователя.
 *
 * Отображает информацию о пользователе:
 * - логин
 * - email
 * - знак зодиака
 *
 * Предоставляет функционал:
 * - редактирование профиля
 * - переход в избранное
 * - просмотр информации о приложении
 * - выход из аккаунта
 * - навигацию по приложению через BottomNavigationView
 *
 */
public class ProfileActivity extends AppCompatActivity {

    /**
     * ViewModel для управления данными профиля.
     */
    private ProfileViewModel viewModel;

    /**
     * Поля ввода для данных пользователя.
     */
    private TextInputEditText etLogin, etEmail, etZodiac;

    /**
     * Кнопки действий профиля.
     */
    private LinearLayout btnEdit, btnFavorites, btnAbout, btnLogout;

    /**
     * Контейнеры для переключения между меню и информацией о приложении.
     */
    private LinearLayout containerButtons, containerAbout;

    /**
     * Кнопка возврата из раздела "О приложении".
     */
    private View btnBack;

    /**
     * Нижняя навигационная панель.
     */
    private BottomNavigationView nav;

    /**
     * Флаг режима редактирования профиля.
     * true - поля доступны для редактирования, false - поля заблокированы.
     */
    private boolean isEditMode = false;

    /**
     * Цвет текста при ошибке валидации (красный).
     */
    private final int errorColor = Color.parseColor("#4A1212");

    /**
     * Цвет текста по умолчанию (белый).
     */
    private final int defaultColor = Color.WHITE;

    /**
     * Вызывается при создании активности.
     * Инициализирует ViewModel, интерфейс, наблюдателей и обработчики событий.
     *
     * @param savedInstanceState сохраненное состояние активности
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        initViews();
        setupObservers();
        setupButtons();
        setupBottomNavigation();

        viewModel.loadUser();
    }

    /**
     * Инициализирует все View компоненты активности.
     * Находит элементы по ID из layout-файла.
     */
    private void initViews() {
        etLogin = findViewById(R.id.et_login);
        etEmail = findViewById(R.id.et_email);
        etZodiac = findViewById(R.id.et_zodiac);

        btnEdit = findViewById(R.id.btn_edit_profile);
        btnFavorites = findViewById(R.id.btn_favorites);
        btnAbout = findViewById(R.id.btn_about);
        btnLogout = findViewById(R.id.btn_logout);

        containerButtons = findViewById(R.id.containerButtons);
        containerAbout = findViewById(R.id.containerAbout);

        btnBack = findViewById(R.id.btnBack);

        nav = findViewById(R.id.bottom_navigation);
    }

    /**
     * Настраивает наблюдателей (LiveData) за изменениями данных в ViewModel.
     * Обрабатывает обновление полей ввода, сообщений об ошибках и состояния валидации.
     */
    private void setupObservers() {

        viewModel.username.observe(this, etLogin::setText);
        viewModel.email.observe(this, etEmail::setText);
        viewModel.zodiac.observe(this, etZodiac::setText);

        viewModel.errorMessage.observe(this, msg -> {
            if (msg != null) {
                android.widget.Toast.makeText(this, msg, android.widget.Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.formError.observe(this, error -> {
            setFieldState(etLogin, error);
            setFieldState(etEmail, error);
        });
    }

    /**
     * Настраивает обработчики нажатий для всех кнопок активности.
     * - Редактирование/сохранение профиля
     * - Переход в избранное
     * - Открытие информации о приложении
     * - Возврат из информации о приложении
     * - Выход из аккаунта
     */
    private void setupButtons() {

        btnEdit.setOnClickListener(v -> {

            TextView txt = (TextView) btnEdit.getChildAt(0);

            if (!isEditMode) {
                etLogin.setEnabled(true);
                etEmail.setEnabled(true);
                etZodiac.setEnabled(true);

                txt.setText("Сохранить");
                isEditMode = true;

            } else {

                boolean success = viewModel.updateUser(
                        etLogin.getText().toString().trim(),
                        etEmail.getText().toString().trim(),
                        etZodiac.getText().toString().trim()
                );

                if (!success) return;

                etLogin.setEnabled(false);
                etEmail.setEnabled(false);
                etZodiac.setEnabled(false);

                txt.setText(getString(R.string.prof_btn_1));
                isEditMode = false;
            }
        });

        btnFavorites.setOnClickListener(v ->
                startActivity(new Intent(this, FavoriteActivity.class))
        );

        btnAbout.setOnClickListener(v -> {
            containerButtons.setVisibility(View.GONE);
            containerAbout.setVisibility(View.VISIBLE);
        });

        btnBack.setOnClickListener(v -> {
            containerAbout.setVisibility(View.GONE);
            containerButtons.setVisibility(View.VISIBLE);
        });

        btnLogout.setOnClickListener(v -> {
            new UserRepository(this).logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    /**
     * Настраивает нижнюю навигационную панель.
     * Обрабатывает переходы между:
     * - домашним экраном
     * - библиотекой
     * - профилем
     */
    private void setupBottomNavigation() {

        nav.setSelectedItemId(R.id.profile);

        nav.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            }

            if (item.getItemId() == R.id.library) {
                startActivity(new Intent(this, LibraryActivity.class));
                finish();
                return true;
            }

            return item.getItemId() == R.id.profile;
        });
    }

    /**
     * Устанавливает цвет текста и подсказки для поля ввода.
     * Используется для отображения ошибок валидации.
     *
     * @param et поле ввода
     * @param error true - красный цвет (ошибка), false - белый цвет (норма)
     */
    private void setFieldState(TextInputEditText et, Boolean error) {

        if (error == null) return;

        if (error) {
            et.setTextColor(errorColor);
            et.setHintTextColor(errorColor);
        } else {
            et.setTextColor(defaultColor);
            et.setHintTextColor(defaultColor);
        }
    }
}