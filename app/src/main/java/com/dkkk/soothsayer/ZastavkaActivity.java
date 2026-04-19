package com.dkkk.soothsayer;

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

public class ZastavkaActivity extends AppCompatActivity {

    private ImageView star1, star2, star3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.zastavka);

        // системные отступы
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.LogoV), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // подключаем элементы
        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);

        // стартовое состояние (на всякий случай)
        star2.setAlpha(0f);
        star3.setAlpha(0f);

        Handler handler = new Handler();

        // ⭐ star2 через 1 секунду (плавно)
        handler.postDelayed(() -> {
            star1.setVisibility(View.VISIBLE);
            star1.animate()
                    .alpha(1f)
                    .setDuration(600)
                    .start();
        }, 1000);

        // ⭐ star2 через 1 секунду (плавно)
        handler.postDelayed(() -> {
            star2.setVisibility(View.VISIBLE);
            star2.animate()
                    .alpha(1f)
                    .setDuration(600)
                    .start();
        }, 2000);

        // ⭐ star3 через 2 секунды (плавно)
        handler.postDelayed(() -> {
            star3.setVisibility(View.VISIBLE);
            star3.animate()
                    .alpha(1f)
                    .setDuration(600)
                    .start();
        }, 3000);

        // 🚀 переход на Login через 3 секунды
        handler.postDelayed(() -> {
            Intent i = new Intent(ZastavkaActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }, 4000);
    }
}