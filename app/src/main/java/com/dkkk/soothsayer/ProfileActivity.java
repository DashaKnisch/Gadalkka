package com.dkkk.soothsayer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Активность профиля пользователя.
 */
public class ProfileActivity extends AppCompatActivity {

    /** TextView для отображения имени пользователя */
    TextView nameText;

    /**
     * Метод жизненного цикла onCreate.
     * Инициализирует интерфейс и загружает имя пользователя из SharedPreferences.
     * @param savedInstanceState сохранённое состояние активности (если есть)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameText = findViewById(R.id.userNameText);

        // Загружаем имя пользователя из SharedPreferences
        SharedPreferences preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        String username = preferences.getString("username", "Неизвестно");
        nameText.setText("Имя: " + username);
    }

    /**
     * Обработчик нажатия кнопки выхода из аккаунта.
     * Очищает данные пользователя из SharedPreferences и возвращает на экран входа.
     * @param view View, вызвавший этот метод (кнопка logout)
     */
    public void logout(View view) {
        SharedPreferences preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();  // Очищаем все сохранённые данные
        editor.apply();

        // Переход на экран LoginActivity, очищая стек активностей
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * Обработчик нажатия кнопки перехода на главный экран.
     * Запускает HomeActivity.
     * @param view View, вызвавший метод (кнопка перехода)
     */
    public void goHome(View view) {
        Intent intent = new Intent(ProfileActivity.this, Homeactivity.class);
        startActivity(intent);
    }
}
