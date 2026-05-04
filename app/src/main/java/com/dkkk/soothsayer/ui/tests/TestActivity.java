package com.dkkk.soothsayer.ui.tests;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.dkkk.soothsayer.R;
import com.dkkk.soothsayer.ui.HomeActivity;
import com.dkkk.soothsayer.ui.ProfileActivity;
import com.dkkk.soothsayer.ui.library.LibraryActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Activity раздела "Тесты".
 *
 * Содержит:
 * 1. Блок "Какая ты ведьма"
 * 2. Блок "Какой камень тебе подходит"
 *
 * Каждый блок состоит из текста и кнопки.
 *
 * Нижняя навигация присутствует, но не подсвечивает текущий экран,
 * так как он не является основным разделом.
 */
public class TestActivity extends AppCompatActivity {


    /** Кнопка перехода к тесту "Какая ты ведьма" */
    private Button btnTestWitch;

    /** Кнопка перехода к тесту "Какой камень тебе подходит" */
    private Button btnTestStone;

    /** Нижняя навигация */
    private BottomNavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initViews();
        setupClicks();
        setupBottomNavigation();
    }

    /**
     * Инициализация UI элементов
     */
    private void initViews() {
        btnTestWitch = findViewById(R.id.btnTestWitch);
        btnTestStone = findViewById(R.id.btnTestStone);
        nav = findViewById(R.id.bottom_navigation);
    }

    /**
     * Обработка кликов
     */
    private void setupClicks() {

        btnTestWitch.setOnClickListener(v -> {
            Intent intent = new Intent(this, TestRunActivity.class);
            intent.putExtra("test_name", "witch");
            intent.putExtra("test_title", "Какая ты ведьма?");
            startActivity(intent);
        });

        btnTestStone.setOnClickListener(v -> {
            Intent intent = new Intent(this, TestRunActivity.class);
            intent.putExtra("test_name", "stone");
            intent.putExtra("test_title", "Какой камень тебе подходит?");
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