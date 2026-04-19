package com.dkkk.soothsayer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

/**
 * Активность для входа пользователя в приложение.
 * Использует LoginViewModel для обработки логики входа.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText etLogin;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // Инициализация ViewModel
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Подписка на результат входа
        loginViewModel.getLoginSuccess().observe(this, success -> {
            if (success) {
                startActivity(new Intent(this, Homeactivity.class));
                finish();
            }
        });

        // Подписка на сообщения об ошибках
        loginViewModel.getErrorMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });

        // Обработчик нажатия на кнопку входа
        btnLogin.setOnClickListener(v -> {
            String login = etLogin.getText().toString();
            String password = etPassword.getText().toString();
            loginViewModel.login(login, password);
        });

        // Обработчик нажатия на кнопку регистрации
        btnRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }
}
