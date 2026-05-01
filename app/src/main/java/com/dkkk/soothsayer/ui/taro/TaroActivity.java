package com.dkkk.soothsayer.ui.taro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.dkkk.soothsayer.R;
import com.dkkk.soothsayer.ui.HomeActivity;
import com.dkkk.soothsayer.ui.ProfileActivity;
import com.dkkk.soothsayer.ui.library.LibraryActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Activity раздела "Таро".
 *
 * Содержит:
 * 1. Блок "Карта дня"
 * 2. Блок "Расклад"
 *
 * Каждый блок состоит из текста и кнопки.
 *
 * Нижняя навигация присутствует, но не подсвечивает текущий экран,
 * так как он не является основным разделом.
 */
public class TaroActivity extends AppCompatActivity {

    /** Кнопка "Назад" */
    private ImageView btnBack;

    /** Кнопка перехода к "Карте дня" */
    private Button btnCardDay;

    /** Кнопка перехода к "Раскладу" */
    private Button btnSpread;

    /** Нижняя навигация */
    private BottomNavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taro);

        initViews();
        setupClicks();
        setupBottomNavigation();
    }

    /**
     * Инициализация UI элементов
     */
    private void initViews() {
        btnCardDay = findViewById(R.id.btnCardDay);
        btnSpread = findViewById(R.id.btnSpread);
        nav = findViewById(R.id.bottom_navigation);
    }

    /**
     * Обработка кликов
     */
    private void setupClicks() {

        // Карта дня
        btnCardDay.setOnClickListener(v -> {
            Intent intent = new Intent(this, CardDayActivity.class);
            startActivity(intent);
        });

        // Расклад
        btnSpread.setOnClickListener(v -> {
            Intent intent = new Intent(this, SpreadActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Настройка нижней навигации
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