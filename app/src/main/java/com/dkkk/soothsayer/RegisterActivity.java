package com.dkkk.soothsayer;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;

public class RegisterActivity extends AppCompatActivity {

    private EditText etLogin, etEmail, etPassword;
    private MaterialButton btnRegister;
    private Button btnBack;

    private RegisterViewModel viewModel;

    private final int errorColor = Color.parseColor("#4A1212");
    private final int defaultTextColor = Color.WHITE;

    private ColorStateList defaultTint;
    private int defaultStrokeColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etLogin = findViewById(R.id.RLogin);
        etEmail = findViewById(R.id.RMail);
        etPassword = findViewById(R.id.RPassword);

        btnRegister = findViewById(R.id.btnLogin);
        btnBack = findViewById(R.id.btnBack);

        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        defaultTint = etLogin.getBackgroundTintList();

        defaultStrokeColor = btnRegister.getStrokeColor().getDefaultColor();

        viewModel.getRegisterSuccess().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        viewModel.getErrorMessage().observe(this, msg -> {
            if (msg != null) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getFormError().observe(this, error -> {
            setField(etLogin, error);
            setField(etEmail, error);
            setField(etPassword, error);
            setButtonStroke(error);
        });

        btnRegister.setOnClickListener(v -> {
            viewModel.register(
                    etLogin.getText().toString().trim(),
                    etEmail.getText().toString().trim(),
                    etPassword.getText().toString().trim()
            );
        });

        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        etLogin.setOnFocusChangeListener((v, f) -> { if (f) reset(etLogin); });
        etEmail.setOnFocusChangeListener((v, f) -> { if (f) reset(etEmail); });
        etPassword.setOnFocusChangeListener((v, f) -> { if (f) reset(etPassword); });
    }

    private void setField(EditText et, Boolean error) {
        if (error == null) return;

        if (error) {
            et.setBackgroundTintList(ColorStateList.valueOf(errorColor));
            et.setTextColor(errorColor);
            et.setHintTextColor(errorColor);
        } else {
            et.setBackgroundTintList(defaultTint);
            et.setTextColor(defaultTextColor);
            et.setHintTextColor(defaultTextColor);
        }
    }

    private void setButtonStroke(Boolean error) {
        if (error == null) return;

        if (error) {
            btnRegister.setStrokeColor(ColorStateList.valueOf(errorColor));
        } else {
            btnRegister.setStrokeColor(ColorStateList.valueOf(defaultStrokeColor));
        }
    }

    private void reset(EditText et) {
        et.setBackgroundTintList(defaultTint);
        et.setTextColor(defaultTextColor);
        et.setHintTextColor(defaultTextColor);
    }
}