package com.dkkk.soothsayer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ZastavkaActivity extends AppCompatActivity {

    private ImageView star1, star2, star3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.zastavka);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ZLogo), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        star1 = findViewById(R.id.Zstar1);
        star2 = findViewById(R.id.Zstar2);
        star3 = findViewById(R.id.Zstar3);

        star2.setAlpha(0f);
        star3.setAlpha(0f);

        Handler handler = new Handler();

        handler.postDelayed(() -> {
            star1.setVisibility(View.VISIBLE);
            star1.animate()
                    .alpha(1f)
                    .setDuration(600)
                    .start();
        }, 1000);

        handler.postDelayed(() -> {
            star2.setVisibility(View.VISIBLE);
            star2.animate()
                    .alpha(1f)
                    .setDuration(600)
                    .start();
        }, 2000);

        handler.postDelayed(() -> {
            star3.setVisibility(View.VISIBLE);
            star3.animate()
                    .alpha(1f)
                    .setDuration(600)
                    .start();
        }, 3000);

        handler.postDelayed(() -> {
            Intent i = new Intent(ZastavkaActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }, 4000);
    }
}