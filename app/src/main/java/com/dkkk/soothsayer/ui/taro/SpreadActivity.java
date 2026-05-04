package com.dkkk.soothsayer.ui.taro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.dkkk.soothsayer.R;
import com.dkkk.soothsayer.model.TarotCard;
import com.dkkk.soothsayer.ui.HomeActivity;
import com.dkkk.soothsayer.ui.ProfileActivity;
import com.dkkk.soothsayer.ui.library.LibraryActivity;
import com.dkkk.soothsayer.viewmodel.taro.SpreadViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class SpreadActivity extends AppCompatActivity {

    private Spinner spinnerCategory;
    private Button btnSpread;
    private TextView txtResult;

    private ImageView card1, card2, card3;
    private ImageView btnBack, btnInfo;

    private TextView txtTooltip;
    private View spreadContainer;

    private BottomNavigationView nav;

    private SpreadViewModel vm;

    private boolean isTooltipVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spread);

        initViews();
        setupSpinner();
        setupViewModel();
        setupObservers();
        setupClicks();
        setupTooltip();
        setupBottomNavigation();
    }

    // ---------------- UI ----------------

    private void initViews() {

        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSpread = findViewById(R.id.btnSpread);
        txtResult = findViewById(R.id.txtSpreadResult);

        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);

        btnBack = findViewById(R.id.btnBack);
        btnInfo = findViewById(R.id.btnInfo);

        txtTooltip = findViewById(R.id.txtTooltip);
        spreadContainer = findViewById(R.id.spreadContainer);

        nav = findViewById(R.id.bottom_navigation);

        spreadContainer.setVisibility(View.GONE);

        btnBack.setOnClickListener(v -> finish());
    }

    // ---------------- Spinner ----------------

    private void setupSpinner() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.categories,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    // ---------------- ViewModel ----------------

    private void setupViewModel() {
        vm = new ViewModelProvider(this).get(SpreadViewModel.class);
    }

    private void setupObservers() {

        vm.cards.observe(this, this::setCards);

        vm.resultText.observe(this, result -> {
            txtResult.setText(result);
        });
    }

    // ---------------- Logic ----------------

    private void setupClicks() {

        btnSpread.setOnClickListener(v -> {

            String category = spinnerCategory.getSelectedItem().toString();

            spreadContainer.setVisibility(View.GONE);
            vm.loadSpread(category);
        });
    }

    private void setCards(List<TarotCard> cards) {

        if (cards == null || cards.isEmpty()) return;

        spreadContainer.setVisibility(View.VISIBLE);

        card1.setImageDrawable(null);
        card2.setImageDrawable(null);
        card3.setImageDrawable(null);

        if (cards.size() > 0) setImage(card1, cards.get(0).getImageName());
        if (cards.size() > 1) setImage(card2, cards.get(1).getImageName());
        if (cards.size() > 2) setImage(card3, cards.get(2).getImageName());
    }

    private void setImage(ImageView view, String imageName) {

        int resId = getResources().getIdentifier(
                imageName,
                "drawable",
                getPackageName()
        );

        view.setImageResource(resId);
    }

    // ---------------- Tooltip ----------------

    private void setupTooltip() {

        txtTooltip.setVisibility(View.GONE);

        btnInfo.setOnClickListener(v -> {

            isTooltipVisible = !isTooltipVisible;
            txtTooltip.setVisibility(isTooltipVisible ? View.VISIBLE : View.GONE);
        });
    }

    // ---------------- Bottom Navigation ----------------

    private void setupBottomNavigation() {

        nav.getMenu().setGroupCheckable(0, true, false);

        for (int i = 0; i < nav.getMenu().size(); i++) {
            nav.getMenu().getItem(i).setChecked(false);
        }

        nav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            }

            if (id == R.id.library) {
                startActivity(new Intent(this, LibraryActivity.class));
                finish();
                return true;
            }

            if (id == R.id.profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }

            return false;
        });
    }
}