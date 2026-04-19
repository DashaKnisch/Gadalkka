package com.dkkk.soothsayer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

/**
 * Активность регистрации нового пользователя.
 * Использует RegisterViewModel для обработки логики.
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etPassword;
    private Button btnRegister;
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        // Инициализация ViewModel
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        // Подписка на результат регистрации
        registerViewModel.getRegisterSuccess().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Подписка на сообщения об ошибках
        registerViewModel.getErrorMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });

        // Обработчик нажатия кнопки регистрации
        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String password = etPassword.getText().toString();
            registerViewModel.register(name, password);
        });
    }

    /**
     * Обработчик нажатия кнопки "Назад ко входу".
     */
    public void goToLogin(android.view.View view) {
        finish();
    }
}
