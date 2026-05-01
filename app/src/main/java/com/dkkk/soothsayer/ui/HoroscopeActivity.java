package com.dkkk.soothsayer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.dkkk.soothsayer.R;
import com.dkkk.soothsayer.model.HoroscopeItem;
import com.dkkk.soothsayer.ui.library.LibraryActivity;
import com.dkkk.soothsayer.viewmodel.HoroscopeViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Экран гороскопа.
 *
 * Отвечает за:
 * - выбор знака зодиака через Spinner
 * - получение данных гороскопа через ViewModel
 * - отображение изображения знака
 * - отображение описания и совета дня
 * - навигацию по приложению через BottomNavigationView
 */
public class HoroscopeActivity extends AppCompatActivity {

    /** Выпадающий список выбора знака зодиака */
    private Spinner spinnerZZ;

    /** Изображение знака зодиака */
    private ImageView imgZZ;

    /** Текст описания гороскопа */
    private TextView txtDescription;

    /** Текст совета на день */
    private TextView txtAdvice;

    /** ViewModel для загрузки данных гороскопа */
    private HoroscopeViewModel vm;

    /** Нижняя навигационная панель */
    private BottomNavigationView nav;

    /**
     * Вызывается при создании Activity.
     *
     * Инициализирует интерфейс, Spinner, ViewModel,
     * наблюдателей LiveData и нижнюю навигацию.
     *
     * @param savedInstanceState сохранённое состояние Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horoscope);

        initViews();
        setupSpinner();
        setupViewModel();
        setupObservers();
        setupBottomNavigation();
    }

    /**
     * Инициализация всех UI элементов экрана.
     */
    private void initViews() {
        spinnerZZ = findViewById(R.id.spinnerZZ);
        imgZZ = findViewById(R.id.imgzz);
        txtDescription = findViewById(R.id.txtDescription);
        txtAdvice = findViewById(R.id.txtAdvice);
        nav = findViewById(R.id.bottom_navigation);
    }

    /**
     * Флаг, чтобы игнорировать первое автоматическое срабатывание Spinner.
     */
    private boolean isFirstSelection = true;

    /**
     * Настройка Spinner со знаками зодиака.
     *
     * При выборе знака запрашивает данные через ViewModel.
     */
    private void setupSpinner() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.ZZ,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerZZ.setAdapter(adapter);

        spinnerZZ.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent,
                                       android.view.View view,
                                       int position,
                                       long id) {

                if (isFirstSelection) {
                    isFirstSelection = false;
                    return;
                }

                String sign = spinnerZZ.getSelectedItem().toString();
                vm.loadHoroscope(sign);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    /**
     * Инициализация ViewModel.
     */
    private void setupViewModel() {
        vm = new ViewModelProvider(this).get(HoroscopeViewModel.class);
    }

    /**
     * Подписка на LiveData из ViewModel.
     *
     * При изменении данных обновляет UI.
     */
    private void setupObservers() {
        vm.horoscope.observe(this, this::updateUI);
    }

    /**
     * Обновление интерфейса данными гороскопа.
     *
     * @param h объект гороскопа
     */
    private void updateUI(HoroscopeItem h) {

        if (h == null) return;

        int resId = getResources().getIdentifier(
                h.getImageName(),
                "drawable",
                getPackageName()
        );

        imgZZ.setImageResource(resId);
        txtDescription.setText(h.getDescription());
        txtAdvice.setText(h.getAdvice());
    }

    /**
     * Настройка нижней навигационной панели.
     *
     * Обеспечивает переход между основными экранами:
     * - Home
     * - Library
     * - Profile
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