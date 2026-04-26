package com.dkkk.soothsayer.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.dkkk.soothsayer.R;
import com.dkkk.soothsayer.viewmodel.GoroscopeViewModel;

/**
 * Активность для отображения гороскопа.
 */
public class Goroscope extends AppCompatActivity {

    private EditText user_field;
    private Button main_btn;
    private TextView result_info;
    private ImageView zodiacImage;

    private final Handler handler = new Handler();
    private int index = 0;
    private String currentHoroscopeText = "";
    private GoroscopeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_goroscope);

        user_field = findViewById(R.id.user_field);
        main_btn = findViewById(R.id.main_btn);
        result_info = findViewById(R.id.result_info);
        zodiacImage = findViewById(R.id.zodiacImage);

        viewModel = new ViewModelProvider(this).get(GoroscopeViewModel.class);

        viewModel.getGoroscopeResult().observe(this, result -> {
            if (result == null) {
                Toast.makeText(this, "Пожалуйста, введите свой знак зодиака.", Toast.LENGTH_SHORT).show();
            } else {
                startTypingAnimation(result.text, result.imageRes);
            }
        });

        main_btn.setOnClickListener(v -> {
            String zodiac = user_field.getText().toString();
            viewModel.fetchHoroscope(zodiac);
        });
    }

    private void startTypingAnimation(String text, int imageRes) {
        result_info.setText("");
        currentHoroscopeText = text;
        index = 0;
        zodiacImage.setImageResource(imageRes);
        handler.removeCallbacks(typingRunnable);
        handler.postDelayed(typingRunnable, 50);
    }

    private final Runnable typingRunnable = new Runnable() {
        @Override
        public void run() {
            if (index < currentHoroscopeText.length()) {
                result_info.append(String.valueOf(currentHoroscopeText.charAt(index)));
                index++;
                handler.postDelayed(this, 50);
            }
        }
    };

    public void GoBack2(View v) {
        finish();
    }
}
