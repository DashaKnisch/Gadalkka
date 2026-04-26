package com.dkkk.soothsayer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dkkk.soothsayer.R;

/**
 * Экран-заставка приложения.
 *
 * Отображает анимацию появления звезд и через заданное время
 * перенаправляет пользователя на экран входа (LoginActivity).
 *
 * Используется как стартовый экран приложения.
 */
public class ZastavkaActivity extends AppCompatActivity {

    /** Первая звезда анимации */
    private ImageView star1;

    /** Вторая звезда анимации */
    private ImageView star2;

    /** Третья звезда анимации */
    private ImageView star3;

    /**
     * Инициализация заставки и запуск последовательной анимации.
     *
     * @param savedInstanceState сохранённое состояние активности
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.zastavka);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ZLogo), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        star1 = findViewById(R.id.Zstar1);
        star2 = findViewById(R.id.Zstar2);
        star3 = findViewById(R.id.Zstar3);

        star2.setAlpha(0f);
        star3.setAlpha(0f);

        Handler handler = new Handler();

        /**
         * Появление первой звезды
         */
        handler.postDelayed(() -> {
            star1.setVisibility(View.VISIBLE);
            star1.animate()
                    .alpha(1f)
                    .setDuration(600)
                    .start();
        }, 1000);

        /**
         * Появление второй звезды
         */
        handler.postDelayed(() -> {
            star2.setVisibility(View.VISIBLE);
            star2.animate()
                    .alpha(1f)
                    .setDuration(600)
                    .start();
        }, 2000);

        /**
         * Появление третьей звезды
         */
        handler.postDelayed(() -> {
            star3.setVisibility(View.VISIBLE);
            star3.animate()
                    .alpha(1f)
                    .setDuration(600)
                    .start();
        }, 3000);

        /**
         * Переход на экран логина после завершения анимации
         */
        handler.postDelayed(() -> {
            Intent i = new Intent(ZastavkaActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }, 4000);
    }
}