package com.dkkk.soothsayer.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.dkkk.soothsayer.R;
import com.dkkk.soothsayer.viewmodel.MatrixViewModel;

/**
 * Активность для вычисления и отображения "матрицы".
 */
public class Matrix extends AppCompatActivity {

    private EditText vvod;
    private Button knopka;
    private ImageButton reset;
    private TextView result;
    private ImageView matrixImage;
    private TextView zonaTalent, zonaPortret, zonaMat, zonaKarma, centralArcan;
    private TextView zonaM1, zonaM2, zonaG1, zonaG2;
    private TextView Talant1, Talant2, Portret1, Portret2, Mat1, Mat2, Karma1, Karma2;

    private MatrixViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_matrix);

        initViews();

        viewModel = new ViewModelProvider(this).get(MatrixViewModel.class);

        viewModel.getMatrixResult().observe(this, res -> {
            if (res == null) {
                Toast.makeText(this, "Введена неправильная дата", Toast.LENGTH_SHORT).show();
            } else {
                updateUI(res);
            }
        });

        reset.setOnClickListener(v -> resetUI());

        knopka.setOnClickListener(v -> {
            String input = vvod.getText().toString();
            viewModel.calculateMatrix(input);
        });
    }

    private void initViews() {
        vvod = findViewById(R.id.vvod);
        knopka = findViewById(R.id.knopka);
        reset = findViewById(R.id.reset);
        result = findViewById(R.id.result);
        matrixImage = findViewById(R.id.matrixImage);
        zonaTalent = findViewById(R.id.zonaTalant);
        zonaPortret = findViewById(R.id.zonaPortret);
        zonaMat = findViewById(R.id.zonaMat);
        zonaKarma = findViewById(R.id.zonaKarma);
        centralArcan = findViewById(R.id.centralArcan);
        zonaM1 = findViewById(R.id.zonaM1);
        zonaM2 = findViewById(R.id.zonaM2);
        zonaG1 = findViewById(R.id.zonaG1);
        zonaG2 = findViewById(R.id.zonaG2);
        Talant1 = findViewById(R.id.Talant1);
        Talant2 = findViewById(R.id.Talant2);
        Portret1 = findViewById(R.id.Portret1);
        Portret2 = findViewById(R.id.Portret2);
        Mat1 = findViewById(R.id.Mat1);
        Mat2 = findViewById(R.id.Mat2);
        Karma1 = findViewById(R.id.Karma1);
        Karma2 = findViewById(R.id.Karma2);
    }

    private void resetUI() {
        vvod.setVisibility(View.VISIBLE);
        knopka.setVisibility(View.VISIBLE);
        setResultVisibility(View.GONE);
    }

    private void updateUI(MatrixViewModel.MatrixResult res) {
        vvod.setVisibility(View.GONE);
        knopka.setVisibility(View.GONE);
        setResultVisibility(View.VISIBLE);

        result.setText(res.description);
        centralArcan.setText(res.central);
        zonaM1.setText(res.m1);
        zonaM2.setText(res.m2);
        zonaG1.setText(res.g1);
        zonaG2.setText(res.g2);
    }

    private void setResultVisibility(int visibility) {
        matrixImage.setVisibility(visibility);
        result.setVisibility(visibility);
        zonaTalent.setVisibility(visibility);
        zonaPortret.setVisibility(visibility);
        zonaMat.setVisibility(visibility);
        zonaKarma.setVisibility(visibility);
        centralArcan.setVisibility(visibility);
        zonaM1.setVisibility(visibility);
        zonaM2.setVisibility(visibility);
        zonaG1.setVisibility(visibility);
        zonaG2.setVisibility(visibility);
        Talant1.setVisibility(visibility);
        Talant2.setVisibility(visibility);
        Portret1.setVisibility(visibility);
        Portret2.setVisibility(visibility);
        Mat1.setVisibility(visibility);
        Mat2.setVisibility(visibility);
        Karma1.setVisibility(visibility);
        Karma2.setVisibility(visibility);
    }
}
