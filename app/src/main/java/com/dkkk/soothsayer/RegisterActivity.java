package com.dkkk.soothsayer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Активность регистрации нового пользователя.
 * Позволяет пользователю ввести имя и пароль и зарегистрироваться в базе данных.
 */
public class RegisterActivity extends AppCompatActivity {

    /** Поле для ввода имени пользователя */
    EditText etName;

    /** Поле для ввода пароля пользователя */
    EditText etPassword;

    /** Кнопка регистрации */
    Button btnRegister;

    /** Объект для работы с базой данных */
    DBHelper dbHelper;

    /**
     * Метод жизненного цикла onCreate.
     * Инициализирует элементы интерфейса и задаёт логику нажатия кнопки регистрации.
     * @param savedInstanceState сохранённое состояние активности (если есть)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        dbHelper = new DBHelper(this);

        // Обработчик нажатия кнопки регистрации
        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String password = etPassword.getText().toString();

            // Проверка, что оба поля заполнены
            if (!name.isEmpty() && !password.isEmpty()) {
                // Попытка вставить нового пользователя в базу данных
                boolean inserted = dbHelper.insertUser(name, password);
                if (inserted) {
                    Toast.makeText(this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                    finish();  // Закрываем активность регистрации
                } else {
                    Toast.makeText(this, "Пользователь уже существует", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Введите данные", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Обработчик нажатия кнопки "Назад ко входу".
     * Возвращает пользователя на экран входа (LoginActivity).
     */
    public void goToLogin(android.view.View view) {
        finish();
    }

}
