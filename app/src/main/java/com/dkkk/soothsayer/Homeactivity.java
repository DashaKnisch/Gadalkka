package com.dkkk.soothsayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Главная активность приложения, которая служит точкой входа после запуска.
 * Здесь находятся кнопки для перехода к разным разделам приложения.
 */
public class Homeactivity extends AppCompatActivity {

    /**
     * Вызывается при создании активности.
     * Инициализирует интерфейс и настраивает обработчик нажатия на кнопку настроек.
     * @param savedInstanceState Состояние активности, сохранённое при предыдущем запуске.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeactivity);

        ImageButton settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Обрабатывает нажатие на кнопку настроек и открывает экран профиля.
             * @param v Вид, на который нажали.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homeactivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Переход к активности с таро раскладом.
     */
    public void GotoTaro(View v) {
        startActivity(new Intent(this, TaroActivity.class));
    }

    /**
     * Переход к активности с гороскопом.
     */
    public void GoToGoroscope(View v) {
        startActivity(new Intent(this, Goroscope.class));
    }

    /**
     * Переход к активности с матрицей.
     */
    public void GoToMatrix(View v) {
        startActivity(new Intent(this, Matrix.class));
    }

    /**
     * Переход к тестовой активности.
     */
    public void GoToTest(View v) {
        startActivity(new Intent(this, Test.class));
    }
}
