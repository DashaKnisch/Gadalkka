package com.dkkk.soothsayer.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.dkkk.soothsayer.R;
import com.dkkk.soothsayer.viewmodel.TaroViewModel;

/**
 * Активность гадания на картах Таро.
 */
public class TaroActivity extends AppCompatActivity {

    private ImageView generationImage1;
    private TextView imageDescription;
    private TaroViewModel taroViewModel;
    private Button situation, day;
    private ImageView imageViewSituation, imageSituation2, imageSituation3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taro);

        situation = findViewById(R.id.situation);
        day = findViewById(R.id.day);
        ImageButton resetButton = findViewById(R.id.resetButton);
        imageViewSituation = findViewById(R.id.sms3);
        imageSituation2 = findViewById(R.id.imageSituation2);
        imageSituation3 = findViewById(R.id.Situation3);
        generationImage1 = findViewById(R.id.generationImage1);
        imageDescription = findViewById(R.id.imageDescription);

        taroViewModel = new ViewModelProvider(this).get(TaroViewModel.class);

        taroViewModel.getSelectedCard().observe(this, card -> {
            if (card != null) {
                showAnimationAndCard(card);
            }
        });

        resetButton.setOnClickListener(v -> resetUI());
        situation.setOnClickListener(v -> taroViewModel.generateSituation());
        day.setOnClickListener(v -> taroViewModel.generateDay());
    }

    private void resetUI() {
        situation.setVisibility(View.VISIBLE);
        day.setVisibility(View.VISIBLE);
        imageViewSituation.setVisibility(View.GONE);
        imageSituation3.setVisibility(View.GONE);
        imageSituation2.setVisibility(View.GONE);
        generationImage1.setVisibility(View.GONE);
        imageDescription.setVisibility(View.GONE);
    }

    private void showAnimationAndCard(TaroViewModel.CardResult card) {
        situation.setVisibility(View.GONE);
        day.setVisibility(View.GONE);

        if (card.type == 1) {
            imageSituation2.setVisibility(View.VISIBLE);
            imageSituation2.setImageResource(R.drawable.otvet1);
        } else {
            imageSituation3.setVisibility(View.VISIBLE);
            imageSituation3.setImageResource(R.drawable.otvet2);
        }

        new Handler().postDelayed(() -> {
            imageViewSituation.setVisibility(View.VISIBLE);
            imageViewSituation.setImageResource(R.drawable.sms3);
        }, 1000);

        new Handler().postDelayed(() -> {
            generationImage1.setImageResource(card.imageRes);
            generationImage1.setVisibility(View.VISIBLE);
            imageDescription.setText(card.text);
            imageDescription.setVisibility(View.VISIBLE);
        }, 2500);
    }

    public void GoBack(View v) {
        finish();
    }
}
