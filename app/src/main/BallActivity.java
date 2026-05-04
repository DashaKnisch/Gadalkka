package com.dkkk.soothsayer.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.dkkk.soothsayer.R;
import com.dkkk.soothsayer.viewmodel.BallViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Активность магического шара предсказаний.
 *
 * Отвечает за:
 * - отображение интерфейса магического шара
 * - обработку нажатия на шар для получения предсказания
 * - отображение подсказки для пользователя
 * - нижнюю навигацию для перехода к другим разделам
 *
 * Пользователь задаёт вопрос вслух, нажимает на шар,
 * и получает случайное предсказание из базы данных.
 *
 * Архитектура: MVVM (Model-View-ViewModel)
 *
 * @author Soothsayer Team
 * @version 1.0
 */
public class BallActivity extends AppCompatActivity {

    // ============================================
    // UI ЭЛЕМЕНТЫ
    // ============================================

    /** Текст предсказания, отображаемый пользователю */
    private TextView txtResult;

    /** Текст подсказки (как пользоваться шаром) */
    private TextView txtTooltip;

    /** Кнопка-иконка "Информация" для отображения/скрытия подсказки */
    private ImageView btnInfo;

    /** Кнопка магического шара (MaterialButton) для получения предсказания */
    private MaterialButton btnBall;

    /** Нижняя навигационная панель */
    private BottomNavigationView nav;

    /** ViewModel для логики получения предсказаний */
    private BallViewModel vm;

    /**
     * Вызывается при создании активности.
     * Инициализирует UI, ViewModel и подписывается на LiveData.
     *
     * @param savedInstanceState сохранённое состояние (не используется)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ball);

        // Инициализация UI элементов
        init();

        // Инициализация ViewModel
        vm = new ViewModelProvider(this).get(BallViewModel.class);

        // Подписка на изменения предсказания в LiveData
        // При получении нового предсказания обновляем TextView
        vm.prediction.observe(this, text -> {
            txtResult.setText(text);
        });

        // Обработчик нажатия на кнопку магического шара
        // При нажатии запрашиваем новое предсказание у ViewModel
        btnBall.setOnClickListener(v -> {
            vm.getPrediction();
        });

        // Обработчик нажатия на иконку подсказки
        // Показывает или скрывает текст с инструкцией
        btnInfo.setOnClickListener(v -> {
            if (txtTooltip.getVisibility() == View.GONE) {
                txtTooltip.setVisibility(View.VISIBLE);
            } else {
                txtTooltip.setVisibility(View.GONE);
            }
        });

        // Настройка нижней навигации
        setupBottomNavigation();
    }

    /**
     * Инициализация UI элементов.
     * Находит все View по ID из layout-файла.
     */
    private void init() {
        txtResult = findViewById(R.id.txtResult);
        txtTooltip = findViewById(R.id.txtTooltip);
        btnInfo = findViewById(R.id.btnInfo);
        btnBall = findViewById(R.id.btnBall);
        nav = findViewById(R.id.bottom_navigation);
    }

    /**
     * Настройка нижней navigation-панели.
     *
     * Отключает подсветку текущего пункта (т.к. эта активность не является главным разделом)
     * и обрабатывает переходы на другие экраны:
     * - Главный экран (Home)
     * - Библиотека (Library)
     * - Профиль (Profile)
     *
     * При нажатии на любой пункт текущая активность закрывается (finish()).
     */
    private void setupBottomNavigation() {

        // Отключаем групповую подсветку пунктов меню
        nav.getMenu().setGroupCheckable(0, true, false);

        // Снимаем выделение со всех пунктов
        for (int i = 0; i < nav.getMenu().size(); i++) {
            nav.getMenu().getItem(i).setChecked(false);
        }

        // Обработчик выбора пунктов меню
        nav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.home) {
                finish();  // Закрываем текущую активность и возвращаемся к главному экрану
                return true;
            }

            if (id == R.id.library) {
                finish();  // Закрываем текущую активность и переходим в библиотеку
                return true;
            }

            if (id == R.id.profile) {
                finish();  // Закрываем текущую активность и переходим в профиль
                return true;
            }

            return false;
        });
    }
}