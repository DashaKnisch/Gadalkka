package com.dkkk.soothsayer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Активность для входа пользователя в приложение.
 * Позволяет ввести логин и пароль, проверить их корректность
 * и перейти на главный экран приложения.
 */
public class LoginActivity extends AppCompatActivity {

    /** Поле для ввода логина пользователя */
    EditText etLogin;

    /** Поле для ввода пароля пользователя */
    EditText etPassword;

    /** Кнопка для выполнения входа */
    Button btnLogin;

    /** Кнопка для перехода к регистрации нового пользователя */
    Button btnRegister;

    /** Помощник для работы с базой данных пользователей */
    DBHelper dbHelper;

    /**
     * Метод жизненного цикла Activity, вызывается при создании.
     * Инициализирует элементы интерфейса и задает обработчики событий.
     *
     * @param savedInstanceState сохранённое состояние активности (если есть)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        dbHelper = new DBHelper(this);

        // Обработчик нажатия на кнопку входа
        btnLogin.setOnClickListener(v -> {
            String login = etLogin.getText().toString();
            String password = etPassword.getText().toString();

            // Проверка валидности логина и пароля через DBHelper
            if (dbHelper.isValidUser(login, password)) {
                // Сохранение логина в SharedPreferences для последующего использования
                SharedPreferences preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("username", login);
                editor.apply();

                // Переход на главный экран и завершение текущей активности
                startActivity(new Intent(this, Homeactivity.class));
                finish();
            } else {
                // Сообщение об ошибке при неверных данных
                Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
            }
        });

        // Обработчик нажатия на кнопку регистрации - переход на экран регистрации
        btnRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }
}