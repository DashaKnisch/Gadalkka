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

public class LoginActivity extends AppCompatActivity {

    private EditText etLogin, etPassword;
    private Button btnLogin, btnRegister;

    private LoginViewModel viewModel;

    private final int errorColor = Color.parseColor("#4A1212");
    private final int defaultTextColor = Color.WHITE;

    private ColorStateList defaultTint;
    private int defaultStrokeColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLogin = findViewById(R.id.VLogin);
        etPassword = findViewById(R.id.VPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        defaultTint = etLogin.getBackgroundTintList();
        defaultStrokeColor = ((com.google.android.material.button.MaterialButton) btnLogin)
                .getStrokeColor()
                .getDefaultColor();

        // ================= SUCCESS =================
        viewModel.getLoginSuccess().observe(this, success -> {
            if (success) {
                startActivity(new Intent(this, Homeactivity.class));
                finish();
            }
        });

        // ================= ERROR TEXT =================
        viewModel.getErrorMessage().observe(this, msg -> {
            if (msg != null) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        // ================= FORM ERROR =================
        viewModel.getFormError().observe(this, error -> {
            setField(etLogin, error);
            setField(etPassword, error);
            setButtonStroke(error);
        });

        // ================= LOGIN =================
        btnLogin.setOnClickListener(v -> {
            viewModel.login(
                    etLogin.getText().toString().trim(),
                    etPassword.getText().toString().trim()
            );
        });

        // ================= REGISTER =================
        btnRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );

        // ================= RESET =================
        etLogin.setOnFocusChangeListener((v, f) -> {
            if (f) reset(etLogin);
        });

        etPassword.setOnFocusChangeListener((v, f) -> {
            if (f) reset(etPassword);
        });
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

        com.google.android.material.button.MaterialButton btn =
                (com.google.android.material.button.MaterialButton) btnLogin;

        if (error) {
            btn.setStrokeColor(ColorStateList.valueOf(errorColor));
        } else {
            btn.setStrokeColor(ColorStateList.valueOf(defaultStrokeColor));
        }
    }

    private void reset(EditText et) {
        et.setBackgroundTintList(defaultTint);
        et.setTextColor(defaultTextColor);
        et.setHintTextColor(defaultTextColor);
    }
}