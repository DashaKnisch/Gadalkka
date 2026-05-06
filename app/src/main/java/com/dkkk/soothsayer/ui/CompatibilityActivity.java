package com.dkkk.soothsayer.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.dkkk.soothsayer.R;
import com.dkkk.soothsayer.viewmodel.CompatibilityViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

/**
 * Активность для расчёта совместимости (CompatibilityActivity).
 *
 * Отвечает за:
 * - отображение интерфейса выбора двух дат рождения
 * - вывод процента и описания совместимости
 * - навигацию между экранами через BottomNavigationView
 *
 * Использует CompatibilityViewModel для расчётов.
 *
 */
public class CompatibilityActivity extends AppCompatActivity {

    /** Спиннер для выбора дня рождения первого человека */
    private Spinner day, month, year;

    /** Спиннер для выбора дня рождения второго человека */
    private Spinner dayP, monthP, yearP;

    /** TextView для отображения процента совместимости */
    private TextView procent, txtCom;

    /** ViewModel для расчёта совместимости */
    private CompatibilityViewModel vm;

    /** Нижняя навигационная панель */
    private BottomNavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compatibility);

        vm = new ViewModelProvider(this).get(CompatibilityViewModel.class);

        initViews();
        setupSpinners();
        setupObservers();
        setupBottomNavigation();

        findViewById(R.id.btnCompatibility).setOnClickListener(v -> calculate());
    }

    /**
     * Инициализирует все View элементы.
     * Находит компоненты по ID из layout-файла.
     */
    private void initViews() {
        day = findViewById(R.id.spinnerDay);
        month = findViewById(R.id.spinnerMonth);
        year = findViewById(R.id.spinnerYear);

        dayP = findViewById(R.id.spinnerDayP);
        monthP = findViewById(R.id.spinnerMonthP);
        yearP = findViewById(R.id.spinnerYearP);

        procent = findViewById(R.id.procent);
        txtCom = findViewById(R.id.txtCom);

        nav = findViewById(R.id.bottom_navigation);
    }

    /**
     * Настраивает спиннеры для выбора дат.
     * День: 1-31
     * Месяц: 1-12
     * Год: 1950-2025
     */
    private void setupSpinners() {

        setSpinner(day, 1, 31);
        setSpinner(month, 1, 12);
        setSpinner(year, 1950, 2025);

        setSpinner(dayP, 1, 31);
        setSpinner(monthP, 1, 12);
        setSpinner(yearP, 1950, 2025);
    }

    /**
     * Заполняет спиннер значениями от from до to.
     *
     * @param spinner целевой спиннер
     * @param from начальное значение диапазона
     * @param to конечное значение диапазона
     */
    private void setSpinner(Spinner spinner, int from, int to) {
        List<Integer> list = new ArrayList<>();

        for (int i = from; i <= to; i++) {
            list.add(i);
        }

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                list
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    /**
     * Настраивает наблюдателя за LiveData из ViewModel.
     * При получении результата обновляет UI:
     * - процент совместимости
     * - текстовое описание
     */
    private void setupObservers() {

        vm.result.observe(this, r -> {

            if (r == null) return;

            procent.setText(r.percentage + "%");
            txtCom.setText(r.text);
        });
    }

    /**
     * Выполняет расчёт совместимости.
     * Считывает выбранные значения из спиннеров
     * и передаёт их в ViewModel.
     */
    private void calculate() {

        int d1 = (int) day.getSelectedItem();
        int m1 = (int) month.getSelectedItem();
        int y1 = (int) year.getSelectedItem();

        int d2 = (int) dayP.getSelectedItem();
        int m2 = (int) monthP.getSelectedItem();
        int y2 = (int) yearP.getSelectedItem();

        vm.calculate(d1, m1, y1, d2, m2, y2);
    }

    /**
     * Настраивает нижнюю навигационную панель.
     * Сбрасывает выделение всех пунктов меню.
     * При выборе пункта закрывает текущую активность.
     */
    private void setupBottomNavigation() {

        nav.getMenu().setGroupCheckable(0, true, false);

        for (int i = 0; i < nav.getMenu().size(); i++) {
            nav.getMenu().getItem(i).setChecked(false);
        }

        nav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.home) {
                finish();
                return true;
            }

            if (id == R.id.library) {
                finish();
                return true;
            }

            if (id == R.id.profile) {
                finish();
                return true;
            }

            return false;
        });
    }
}