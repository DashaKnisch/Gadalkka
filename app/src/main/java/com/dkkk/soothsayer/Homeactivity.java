package com.dkkk.soothsayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Homeactivity extends AppCompatActivity {

    private Button btnTaro, btnHoroscope, btnMatrix, btnTest, btnBall, btnCompatibility, btnSolar;
    private BottomNavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeactivity);

        btnTaro = findViewById(R.id.buttonTaro);
        btnHoroscope = findViewById(R.id.buttonHoroscope);
        btnMatrix = findViewById(R.id.buttonMatrix);
        btnTest = findViewById(R.id.buttonTest);
        btnBall = findViewById(R.id.buttonBall);
        btnCompatibility = findViewById(R.id.buttonCompatibility);
        btnSolar = findViewById(R.id.buttonSolar);
        nav = findViewById(R.id.bottom_navigation);

        btnTaro.setOnClickListener(v -> {
            Intent intent = new Intent(Homeactivity.this, TaroActivity.class);
            startActivity(intent);
        });

        btnHoroscope.setOnClickListener(v -> {
            Intent intent = new Intent(Homeactivity.this, Goroscope.class);
            startActivity(intent);
        });

        btnMatrix.setOnClickListener(v -> {
            Intent intent = new Intent(Homeactivity.this, Matrix.class);
            startActivity(intent);
        });

        btnTest.setOnClickListener(v -> {
            Intent intent = new Intent(Homeactivity.this, Test.class);
            startActivity(intent);
        });

        btnBall.setOnClickListener(v -> {
            Intent intent = new Intent(Homeactivity.this, BallActivity.class);
            startActivity(intent);
        });

        btnCompatibility.setOnClickListener(v -> {
            Intent intent = new Intent(Homeactivity.this, CompatibilityActivity.class);
            startActivity(intent);
        });

        btnSolar.setOnClickListener(v -> {
            Intent intent = new Intent(Homeactivity.this, SolarActivity.class);
            startActivity(intent);
        });

        nav.setSelectedItemId(R.id.home);

        updateBottomNavIcons(R.id.home);

        nav.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            updateBottomNavIcons(itemId);

            if (itemId == R.id.home) {
                return true;
            }

            if (itemId == R.id.library) {
                startActivity(new Intent(Homeactivity.this, LibraryActivity.class));
                return true;
            }

            if (itemId == R.id.profile) {
                startActivity(new Intent(Homeactivity.this, ProfileActivity.class));
                return true;
            }

            return false;
        });
    }

    private void updateBottomNavIcons(int selectedId) {
        View menuView = nav.getChildAt(0);

        if (menuView instanceof android.view.ViewGroup) {
            android.view.ViewGroup group = (android.view.ViewGroup) menuView;

            for (int i = 0; i < group.getChildCount(); i++) {
                View itemView = group.getChildAt(i);

                int currentId = nav.getMenu().getItem(i).getItemId();

                if (currentId == selectedId) {
                    itemView.setScaleX(1.2f);
                    itemView.setScaleY(1.2f);
                } else {
                    itemView.setScaleX(1f);
                    itemView.setScaleY(1f);
                }
            }
        }
    }
}