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

/**
 * Activity экрана "Расклад Таро".
 *
 * Отвечает за:
 * - выбор категории расклада
 * - генерацию случайных карт
 * - отображение результата расклада
 * - показ/скрытие блока результата
 * - отображение подсказки пользователя
 *
 * Использует:
 * - SpreadViewModel для бизнес-логики
 * - LiveData для обновления UI
 */
public class SpreadActivity extends AppCompatActivity {

    /** Spinner выбора категории расклада */
    private Spinner spinnerCategory;

    /** Кнопка запуска расклада */
    private Button btnSpread;

    /** Текст результата расклада */
    private TextView txtResult;

    /** Карты расклада */
    private ImageView card1, card2, card3;

    /** Кнопка назад */
    private ImageView btnBack;

    /** Кнопка информации (подсказка) */
    private ImageView btnInfo;

    /** Блок подсказки */
    private TextView txtTooltip;

    /** Контейнер блока результата расклада */
    private View spreadContainer;

    /** Нижняя навигация */
    private BottomNavigationView nav;

    /** ViewModel расклада */
    private SpreadViewModel vm;

    /** Флаг видимости подсказки */
    private boolean isTooltipVisible = false;

    /**
     * Инициализация Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spread);

        initViews();
        setupSpinner();
        setupViewModel();
        setupObservers();
        setupClicks();
        setupBottomNav();
        setupTooltip();
    }

    /**
     * Инициализация UI элементов
     */
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

    /**
     * Настройка Spinner категорий расклада
     */
    private void setupSpinner() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.categories,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    /**
     * Инициализация ViewModel
     */
    private void setupViewModel() {
        vm = new ViewModelProvider(this).get(SpreadViewModel.class);
    }

    /**
     * Подписка на LiveData из ViewModel
     */
    private void setupObservers() {

        vm.cards.observe(this, this::setCards);

        vm.resultText.observe(this, result -> txtResult.setText(result));
    }

    /**
     * Установка карт в UI
     *
     * @param cards список карт расклада
     */
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

    /**
     * Установка изображения карты
     *
     * @param view ImageView карты
     * @param imageName имя drawable ресурса
     */
    private void setImage(ImageView view, String imageName) {

        int resId = getResources().getIdentifier(
                imageName,
                "drawable",
                getPackageName()
        );

        view.setImageResource(resId);
    }

    /**
     * Обработка кликов UI
     */
    private void setupClicks() {

        btnSpread.setOnClickListener(v -> {

            String category = spinnerCategory.getSelectedItem().toString();

            spreadContainer.setVisibility(View.GONE);

            vm.loadSpread(category);
        });
    }

    /**
     * Настройка подсказки пользователя
     */
    private void setupTooltip() {

        txtTooltip.setVisibility(View.GONE);

        btnInfo.setOnClickListener(v -> {

            if (isTooltipVisible) {
                txtTooltip.setVisibility(View.GONE);
                isTooltipVisible = false;
            } else {
                txtTooltip.setVisibility(View.VISIBLE);
                isTooltipVisible = true;
            }
        });
    }

    /**
     * Настройка нижней навигации
     */
    private void setupBottomNav() {

        nav.setSelectedItemId(0);

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