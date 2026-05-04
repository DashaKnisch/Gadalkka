package com.dkkk.soothsayer.ui.tests;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.dkkk.soothsayer.R;
import com.dkkk.soothsayer.data.TestDatabaseHelper;
import com.dkkk.soothsayer.model.test.Answer;
import com.dkkk.soothsayer.ui.HomeActivity;
import com.dkkk.soothsayer.ui.ProfileActivity;
import com.dkkk.soothsayer.ui.library.LibraryActivity;
import com.dkkk.soothsayer.viewmodel.TestViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Активность для прохождения тестов.
 *
 * Отвечает за:
 * - отображение вопросов и вариантов ответов
 * - навигацию между вопросами (вперёд/назад)
 * - отображение результата после завершения теста
 * - перезапуск теста
 * - нижнюю навигацию для перехода к другим разделам
 *
 * Используется для:
 * - теста "Какая ты ведьма"
 * - теста "Какой камень тебе подходит"
 *
 * @author Soothsayer Team
 * @version 1.0
 */
public class TestRunActivity extends AppCompatActivity {

    /** Текст текущего вопроса */
    private TextView txtQuestion;

    /** Текст результата (описание) */
    private TextView txtResult;

    /** Заголовок результата (название ведьмы или камня) */
    private TextView tvTitle;

    /** Заголовок активности (название теста) */
    private TextView tvHeader;

    /** Группа радиокнопок для вариантов ответов */
    private RadioGroup radioGroup;

    /** Кнопка "Далее" */
    private Button btnNext;

    /** Кнопка "Назад" */
    private Button btnPrev;

    /** Кнопка "Пройти заново" */
    private Button btnRestart;

    /** Изображение результата */
    private ImageView imgResult;

    /** Кнопка "Назад" в тулбаре */
    private ImageView btnBack;

    /** Контейнер с вопросами теста */
    private LinearLayout testContainer;

    /** Контейнер с результатом */
    private LinearLayout resultContainer;

    /** Нижняя навигация */
    private BottomNavigationView nav;

    /** ViewModel для логики тестирования */
    private TestViewModel vm;

    /** Название текущего теста ("witch" или "stone") */
    private String testName;

    /**
     * Вызывается при создании активности.
     * Инициализирует UI, ViewModel и подписывается на LiveData.
     *
     * @param savedInstanceState сохранённое состояние (не используется)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_run);

        init();

        String title = getIntent().getStringExtra("test_title");
        tvHeader.setText(title);

        TestDatabaseHelper.copyDatabase(this);

        testName = getIntent().getStringExtra("test_name");

        vm = new ViewModelProvider(this).get(TestViewModel.class);
        vm.init(testName);

        observe();

        btnBack.setOnClickListener(v -> finish());

        btnNext.setOnClickListener(v -> vm.next());
        btnPrev.setOnClickListener(v -> vm.prev());

        btnRestart.setOnClickListener(v -> {
            vm.restart();

            testContainer.setVisibility(View.VISIBLE);
            resultContainer.setVisibility(View.GONE);
            btnRestart.setVisibility(View.GONE);
        });

        setupBottomNavigation();
    }

    /**
     * Инициализация UI элементов.
     * Находит все View по ID и устанавливает начальные состояния.
     */
    private void init() {
        txtQuestion = findViewById(R.id.txtQuestion);
        radioGroup = findViewById(R.id.radioGroupAnswers);

        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        btnRestart = findViewById(R.id.btnRestart);

        tvHeader = findViewById(R.id.tvHeader);
        tvTitle = findViewById(R.id.tvTitle);
        txtResult = findViewById(R.id.txtResult);
        imgResult = findViewById(R.id.imgResult);

        btnBack = findViewById(R.id.btnBack);

        testContainer = findViewById(R.id.testContainer);
        resultContainer = findViewById(R.id.resultContainer);

        nav = findViewById(R.id.bottom_navigation);

        resultContainer.setVisibility(View.GONE);
        btnRestart.setVisibility(View.GONE);
    }

    /**
     * Подписка на LiveData из ViewModel.
     * Обновляет UI при изменении данных:
     * - текущий вопрос
     * - список ответов
     * - возможность перехода к следующему вопросу
     * - отображение результата
     */
    private void observe() {

        vm.currentQuestion.observe(this, q -> {
            txtQuestion.setText(q.text);
        });

        vm.answers.observe(this, list -> {

            radioGroup.removeAllViews();

            Answer selected = vm.getSelectedAnswer();

            for (Answer a : list) {

                RadioButton rb = new RadioButton(this);
                rb.setText(a.text);

                rb.setTextColor(ContextCompat.getColor(this, R.color.darkblue));
                rb.setTextSize(18f);
                rb.setPadding(20, 20, 20, 20);
                rb.setButtonTintList(ContextCompat.getColorStateList(this, R.color.darkblue));

                if (selected != null && selected.id == a.id) {
                    rb.setChecked(true);
                }

                rb.setOnClickListener(v -> vm.selectAnswer(a));

                radioGroup.addView(rb);
            }

            btnPrev.setVisibility(vm.isFirst() ? View.GONE : View.VISIBLE);
        });

        vm.canGoNext.observe(this, can -> {
            btnNext.setEnabled(can);
            btnNext.setAlpha(can ? 1f : 0.5f);
        });

        vm.showResult.observe(this, show -> {
            if (show) {
                testContainer.setVisibility(View.GONE);
                resultContainer.setVisibility(View.VISIBLE);
                btnRestart.setVisibility(View.VISIBLE);
            }
        });

        vm.result.observe(this, r -> {

            if (r == null) {
                txtResult.setText("Результат не найден");
                tvTitle.setText("Ошибка");
                return;
            }

            tvTitle.setText(r.title);
            txtResult.setText(r.text);

            // Загрузка изображения по имени ресурса
            int resId = getResources().getIdentifier(
                    r.image,
                    "drawable",
                    getPackageName()
            );

            imgResult.setImageResource(resId);
        });
    }

    /**
     * Настройка нижней navigation-панели.
     *
     * Отключает подсветку текущего пункта (т.к. эта активность не является главным разделом)
     * и обрабатывает переходы на другие экраны:
     * - Главный экран (Home)
     * - Библиотека (Library)
     * - Профиль (Profile)
     */
    private void setupBottomNavigation() {

        nav.getMenu().setGroupCheckable(0, true, false);

        for (int i = 0; i < nav.getMenu().size(); i++) {
            nav.getMenu().getItem(i).setChecked(false);
        }

        nav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            }

            if (id == R.id.library) {
                startActivity(new Intent(this, LibraryActivity.class));
                finish();
                return true;
            }

            if (id == R.id.profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }

            return false;
        });
    }
}