package com.dkkk.soothsayer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.dkkk.soothsayer.R;
import com.dkkk.soothsayer.model.MatrixResult;
import com.dkkk.soothsayer.ui.library.LibraryActivity;
import com.dkkk.soothsayer.viewmodel.MatrixViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

/**
 * Экран "Матрица судьбы".
 *
 * Отвечает за:
 * - ввод даты рождения через Spinner (день, месяц, год)
 * - запуск расчёта матрицы через ViewModel
 * - отображение всех вычисленных значений матрицы
 * - вывод текстовых значений из базы данных
 * - управление подсказкой (tooltip)
 * - навигацию по приложению через BottomNavigationView
 */
public class MatrixActivity extends AppCompatActivity {

    /** Выбор дня рождения */
    private Spinner spinnerDay;

    /** Выбор месяца рождения */
    private Spinner spinnerMonth;

    /** Выбор года рождения */
    private Spinner spinnerYear;

    /** Кнопка запуска расчёта */
    private Button btn;

    /** Кнопка открытия подсказки */
    private ImageView btnInfo;

    /** Текст подсказки */
    private TextView txtTooltip;

    /** Все позиции матрицы */
    private TextView tv1, tv2, tv21, tv22, tv3, tv4, tv41, tv42,
            tv51, tv52, tv53, tv6, tv7, tv71, tv72,
            tv8, tv9, tv91, tv92, tv10;

    /** Текстовые значения из базы данных */
    private TextView txtCharacter, txtParents, txtTalent, txtFinance,
            txtEarnings, txtPartner, txtTail, txtSpirit, txtMoney;

    /** ViewModel расчёта матрицы */
    private MatrixViewModel vm;

    /** Нижняя навигационная панель */
    private BottomNavigationView nav;

    /** Флаг отображения подсказки */
    private boolean isTooltipVisible = false;

    /**
     * Вызывается при создании Activity.
     *
     * Инициализирует UI, Spinner'ы, ViewModel,
     * подписки на данные и навигацию.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix);

        init();
        setupSpinners();

        vm = new ViewModelProvider(this).get(MatrixViewModel.class);

        setupTooltip();
        setupBottomNavigation();

        btn.setOnClickListener(v -> vm.calculate(getSelectedDate()));

        vm.result.observe(this, this::updateUI);
    }

    /**
     * Инициализация всех UI элементов экрана.
     */
    private void init() {

        spinnerDay = findViewById(R.id.spinnerDay);
        spinnerMonth = findViewById(R.id.spinnerMonth);
        spinnerYear = findViewById(R.id.spinnerYear);

        btn = findViewById(R.id.btnMatrix);

        btnInfo = findViewById(R.id.btnInfo);
        txtTooltip = findViewById(R.id.txtTooltip);

        tv1 = findViewById(R.id.tvPosition1);
        tv2 = findViewById(R.id.tvPosition2);
        tv21 = findViewById(R.id.tvPosition21);
        tv22 = findViewById(R.id.tvPosition22);
        tv3 = findViewById(R.id.tvPosition3);
        tv4 = findViewById(R.id.tvPosition4);
        tv41 = findViewById(R.id.tvPosition41);
        tv42 = findViewById(R.id.tvPosition42);
        tv51 = findViewById(R.id.tvPosition51);
        tv52 = findViewById(R.id.tvPosition52);
        tv53 = findViewById(R.id.tvPosition53);
        tv6 = findViewById(R.id.tvPosition6);
        tv7 = findViewById(R.id.tvPosition7);
        tv71 = findViewById(R.id.tvPosition71);
        tv72 = findViewById(R.id.tvPosition72);
        tv8 = findViewById(R.id.tvPosition8);
        tv9 = findViewById(R.id.tvPosition9);
        tv91 = findViewById(R.id.tvPosition91);
        tv92 = findViewById(R.id.tvPosition92);
        tv10 = findViewById(R.id.tvPosition10);

        txtCharacter = findViewById(R.id.txtcharacter);
        txtParents = findViewById(R.id.txtparents);
        txtTalent = findViewById(R.id.txttalent);
        txtFinance = findViewById(R.id.txtfinance);
        txtEarnings = findViewById(R.id.txtearnings);
        txtPartner = findViewById(R.id.txtpartner);
        txtTail = findViewById(R.id.txttail);
        txtSpirit = findViewById(R.id.txtspirit);
        txtMoney = findViewById(R.id.txtmoney);

        nav = findViewById(R.id.bottom_navigation);
    }

    /**
     * Настройка Spinner'ов (день, месяц, год).
     */
    private void setupSpinners() {
        Integer[] days = new Integer[31];
        for (int i = 0; i < 31; i++) days[i] = i + 1;

        ArrayAdapter<Integer> dayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                days
        );
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(dayAdapter);

        Integer[] months = new Integer[12];
        for (int i = 0; i < 12; i++) months[i] = i + 1;

        ArrayAdapter<Integer> monthAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                months
        );
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(monthAdapter);

        Integer[] years = new Integer[80];
        int startYear = 1945;

        for (int i = 0; i < years.length; i++) {
            years[i] = startYear + i;
        }

        ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                years
        );
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(yearAdapter);
    }

    /**
     * Получение выбранной даты рождения.
     *
     * @return дата в формате dd.MM.yyyy
     */
    private String getSelectedDate() {

        int day = (int) spinnerDay.getSelectedItem();
        int month = (int) spinnerMonth.getSelectedItem();
        int year = (int) spinnerYear.getSelectedItem();

        return String.format(Locale.getDefault(),
                "%02d.%02d.%04d",
                day, month, year);
    }

    /**
     * Настройка отображения подсказки (tooltip).
     */
    private void setupTooltip() {

        txtTooltip.setVisibility(android.view.View.GONE);

        btnInfo.setOnClickListener(v -> {

            isTooltipVisible = !isTooltipVisible;

            txtTooltip.setVisibility(
                    isTooltipVisible ? android.view.View.VISIBLE : android.view.View.GONE
            );
        });
    }

    /**
     * Обновление UI после получения результата расчёта.
     *
     * @param r результат вычисления матрицы
     */
    private void updateUI(MatrixResult r) {

        tv2.setText(String.valueOf(r.p2));
        tv9.setText(String.valueOf(r.p9));
        tv7.setText(String.valueOf(r.p7));
        tv4.setText(String.valueOf(r.p4));
        tv10.setText(String.valueOf(r.p10));

        tv1.setText(String.valueOf(r.p1));
        tv3.setText(String.valueOf(r.p3));
        tv6.setText(String.valueOf(r.p6));
        tv8.setText(String.valueOf(r.p8));

        tv21.setText(String.valueOf(r.p21));
        tv22.setText(String.valueOf(r.p22));
        tv41.setText(String.valueOf(r.p41));
        tv42.setText(String.valueOf(r.p42));

        tv71.setText(String.valueOf(r.p71));
        tv72.setText(String.valueOf(r.p72));
        tv91.setText(String.valueOf(r.p91));
        tv92.setText(String.valueOf(r.p92));

        tv51.setText(String.valueOf(r.p51));
        tv52.setText(String.valueOf(r.p52));
        tv53.setText(String.valueOf(r.p53));

        txtCharacter.setText(r.character);
        txtParents.setText(r.parents);
        txtTalent.setText(r.talent);
        txtFinance.setText(r.finance);
        txtEarnings.setText(r.earnings);
        txtPartner.setText(r.partner);
        txtTail.setText(r.tail);
        txtSpirit.setText(r.spirit);
        txtMoney.setText(r.money);
    }

    /**
     * Настройка нижней навигации приложения.
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