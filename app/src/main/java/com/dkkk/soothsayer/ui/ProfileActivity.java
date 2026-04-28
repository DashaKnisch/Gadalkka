package com.dkkk.soothsayer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.dkkk.soothsayer.R;
import com.dkkk.soothsayer.ui.entrance.LoginActivity;
import com.dkkk.soothsayer.viewmodel.ProfileViewModel;

/**
 * Активность профиля пользователя.
 */
public class ProfileActivity extends AppCompatActivity {

    private TextView nameText;
    private ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameText = findViewById(R.id.userNameText);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Подписка на имя пользователя
        profileViewModel.getUsername().observe(this, name -> {
            nameText.setText("Имя: " + name);
        });

        // Подписка на навигацию при выходе
        profileViewModel.getNavigateToLogin().observe(this, navigate -> {
            if (navigate != null && navigate) {
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    /**
     * Обработчик нажатия кнопки выхода.
     */
    public void logout(View view) {
        profileViewModel.logout();
    }

    /**
     * Переход на главный экран.
     */
    public void goHome(View view) {
        finish(); // Просто закрываем текущую активность
    }
}
