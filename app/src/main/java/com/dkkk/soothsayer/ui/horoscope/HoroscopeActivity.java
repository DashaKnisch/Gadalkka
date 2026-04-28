package com.dkkk.soothsayer.ui.horoscope;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.dkkk.soothsayer.R;
import com.dkkk.soothsayer.ui.HomeActivity;
import com.dkkk.soothsayer.ui.ProfileActivity;
import com.dkkk.soothsayer.ui.library.LibraryActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Activity раздела "Гороскоп".
 *
 * Содержит два основных блока:
 * 1. Карта дня
 * 2. Расклад
 *
 * Также содержит нижнюю навигационную панель.
 * Данная страница не относится к основным разделам,
 * поэтому иконка BottomNavigation не подсвечивается.
 */
public class HoroscopeActivity extends AppCompatActivity {

    /** Кнопка возврата назад */
    private ImageView btnBack;

    /** Карточка перехода к странице "Карта дня" */
    private MaterialCardView cardDay;

    /** Карточка перехода к странице "Расклад" */
    private MaterialCardView cardSpread;

    /** Нижняя навигация */
    private BottomNavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horoscope);

        initViews();
        setupBottomNavigation();
    }

    /**
     * Инициализация элементов интерфейса.
     */
    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        cardDay = findViewById(R.id.cardDay);
        cardSpread = findViewById(R.id.cardSpread);
        nav = findViewById(R.id.bottom_navigation);
    }

    /**
     * Настройка нажатий на элементы экрана.
     */

    /**
     * Настройка нижней панели навигации.
     */
    private void setupBottomNavigation() {

        nav.getMenu().setGroupCheckable(0, true, true);
        nav.setSelectedItemId(0);

        nav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.home) {
                startActivity(
                        new Intent(
                                HoroscopeActivity.this,
                                HomeActivity.class
                        )
                );
                finish();
                return true;
            }

            if (id == R.id.library) {
                startActivity(
                        new Intent(
                                HoroscopeActivity.this,
                                LibraryActivity.class
                        )
                );
                finish();
                return true;
            }

            if (id == R.id.profile) {
                startActivity(
                        new Intent(
                                HoroscopeActivity.this,
                                ProfileActivity.class
                        )
                );
                finish();
                return true;
            }

            return false;
        });
    }
}