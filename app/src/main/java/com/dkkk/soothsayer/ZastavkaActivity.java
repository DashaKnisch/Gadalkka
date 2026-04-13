package com.dkkk.soothsayer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Активность заставки приложения (Splash screen).
 * <p>
 * При запуске отображает экран заставки с учетом системных отступов,
 * а через 3 секунды автоматически переходит на экран входа (LoginActivity).
 */
public class ZastavkaActivity extends AppCompatActivity {

    /**
     * Метод жизненного цикла активности, вызывается при создании.
     * Устанавливает полноэкранный режим с учетом системных панелей,
     * задает отступы для ImageView под системные бары,
     * и запускает задержку с переходом на следующий экран.
     *
     * @param savedInstanceState сохранённое состояние активности
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Включение режима edge-to-edge для полноэкранного отображения
        EdgeToEdge.enable(this);

        // Установка макета заставки
        setContentView(R.layout.zastavka);

        // Установка слушателя для применения отступов системных панелей к ImageView
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.imageView), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Установка паддингов слева, сверху, справа и снизу под системные панели
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Запуск задержки на 3 секунды, после чего открывается экран входа
        new Handler().postDelayed(() -> {
            Intent i = new Intent(ZastavkaActivity.this, LoginActivity.class);
            startActivity(i);
            finish(); // Завершение текущей активности, чтобы нельзя было вернуться назад
        }, 3 * 1000);
    }
}