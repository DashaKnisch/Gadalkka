package com.dkkk.soothsayer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.dkkk.soothsayer.R;
import com.dkkk.soothsayer.ui.library.LibraryActivity;
import com.dkkk.soothsayer.ui.taro.TaroActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Главная активность приложения.
 *
 * Отвечает за:
 * - навигацию по основным разделам приложения (Home, Library, Profile)
 * - запуск функциональных модулей (таро, гороскоп, матрица и т.д.)
 * - управление состоянием нижней навигационной панели
 */
public class HomeActivity extends AppCompatActivity {

    /** Кнопка перехода к разделу Таро */
    private Button btnTaro;

    /** Кнопка перехода к гороскопу */
    private Button btnHoroscope;

    /** Кнопка перехода к матрице */
    private Button btnMatrix;

    /** Кнопка прохождения теста */
    private Button btnTest;

    /** Кнопка работы с шаром предсказаний */
    private Button btnBall;

    /** Кнопка проверки совместимости */
    private Button btnCompatibility;

    /** Кнопка солнечного прогноза */
    private Button btnSolar;

    /** Нижняя навигационная панель приложения */
    private BottomNavigationView nav;

    /**
     * Вызывается при создании Activity.
     *
     * Инициализирует UI элементы, назначает обработчики событий
     * и настраивает нижнюю навигацию.
     *
     * @param savedInstanceState сохранённое состояние активности (если есть)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_homeactivity);

        initViews();
        setupFeatureButtons();
        setupBottomNavigation();
    }

    /**
     * Инициализация всех UI элементов экрана.
     */
    private void initViews() {

        btnTaro = findViewById(R.id.buttonTaro);
        btnHoroscope = findViewById(R.id.buttonHoroscope);
        btnMatrix = findViewById(R.id.buttonMatrix);
        btnTest = findViewById(R.id.buttonTest);
        btnBall = findViewById(R.id.buttonBall);
        btnCompatibility = findViewById(R.id.buttonCompatibility);
        btnSolar = findViewById(R.id.buttonSolar);

        nav = findViewById(R.id.bottom_navigation);
    }

    /**
     * Настройка кнопок функциональных разделов приложения.
     *
     * Каждая кнопка запускает соответствующую Activity.
     */
    private void setupFeatureButtons() {

        btnTaro.setOnClickListener(v ->
                startActivity(new Intent(this, TaroActivity.class)));

        btnHoroscope.setOnClickListener(v ->
                startActivity(new Intent(this, HoroscopeActivity.class)));

        btnMatrix.setOnClickListener(v ->
                startActivity(new Intent(this, MatrixActivity.class)));

        btnTest.setOnClickListener(v ->
                startActivity(new Intent(this, Test.class)));

        btnBall.setOnClickListener(v ->
                startActivity(new Intent(this, BallActivity.class)));

        btnCompatibility.setOnClickListener(v ->
                startActivity(new Intent(this, CompatibilityActivity.class)));

        btnSolar.setOnClickListener(v ->
                startActivity(new Intent(this, SolarActivity.class)));
    }

    /**
     * Настройка нижней навигационной панели.
     *
     * Обрабатывает переходы между:
     * - Home (текущий экран)
     * - Library
     * - Profile
     *
     * Также обновляет визуальное состояние иконок.
     */
    private void setupBottomNavigation() {

        nav.setSelectedItemId(R.id.home);

        updateBottomNavIcons(R.id.home);

        nav.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            updateBottomNavIcons(itemId);

            if (itemId == R.id.home) {
                return true;
            }

            if (itemId == R.id.library) {
                startActivity(new Intent(this, LibraryActivity.class));
                return true;
            }

            if (itemId == R.id.profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }

            return false;
        });
    }

    /**
     * Обновляет визуальное состояние иконок нижней навигации.
     *
     * Увеличивает выбранную иконку и возвращает остальные к нормальному размеру.
     *
     * @param selectedId ID выбранного пункта меню
     */
    private void updateBottomNavIcons(int selectedId) {

        View menuView = nav.getChildAt(0);

        if (menuView instanceof android.view.ViewGroup) {

            android.view.ViewGroup group = (android.view.ViewGroup) menuView;

            for (int i = 0; i < group.getChildCount(); i++) {

                View itemView = group.getChildAt(i);

                int currentId = nav.getMenu().getItem(i).getItemId();

                if (currentId == selectedId) {
                    itemView.setScaleX(1.2f);
                    itemView.setScaleY(1.2f);
                } else {
                    itemView.setScaleX(1f);
                    itemView.setScaleY(1f);
                }
            }
        }
    }
}