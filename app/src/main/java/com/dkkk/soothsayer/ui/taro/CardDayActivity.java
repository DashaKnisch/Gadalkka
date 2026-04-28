package com.dkkk.soothsayer.ui.taro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.dkkk.soothsayer.R;
import com.dkkk.soothsayer.data.TarotDatabaseHelper;
import com.dkkk.soothsayer.ui.HomeActivity;
import com.dkkk.soothsayer.ui.ProfileActivity;
import com.dkkk.soothsayer.ui.library.LibraryActivity;
import com.dkkk.soothsayer.viewmodel.taro.CardDayViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Activity "Карта дня" в разделе Таро.
 *
 * Отвечает за:
 * - отображение случайной карты дня
 * - загрузку данных карты через ViewModel (MVVM)
 * - отображение изображения и текстовой информации карты
 * - работу нижней навигации приложения
 *
 * Логика:
 * - при первом входе выбирается случайная карта из базы данных
 * - карта фиксируется на один день
 * - данные отображаются на UI через LiveData
 */
public class CardDayActivity extends AppCompatActivity {

    /** Изображение карты */
    private ImageView imgCard;

    /** Кнопка возврата назад */
    private ImageView btnBack;

    /** Название карты (заголовок) */
    private TextView title;

    /** Описание карты */
    private TextView desc;

    /** Совет карты */
    private TextView advice;

    /** Важная информация карты */
    private TextView important;

    /**
     * Метод жизненного цикла Activity.
     *
     * Выполняет:
     * - инициализацию базы данных
     * - инициализацию UI элементов
     * - подключение ViewModel (MVVM)
     * - наблюдение за LiveData
     * - настройку навигации
     *
     * @param savedInstanceState сохранённое состояние Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_day);

        // Копирование базы данных из assets (если ещё не создана)
        TarotDatabaseHelper.copyDatabase(this);

        initViews();

        // Инициализация ViewModel (слой MVVM)
        CardDayViewModel vm =
                new ViewModelProvider(this).get(CardDayViewModel.class);

        // Подписка на изменения данных карты
        vm.cardLiveData.observe(this, card -> {

            // Отображение данных карты на UI
            title.setText("Карта дня: " + card.getName());
            desc.setText(card.getDescription());
            advice.setText(card.getAdvice());
            important.setText(card.getImportant());

            // Получение изображения из drawable по имени ресурса
            int resId = getResources().getIdentifier(
                    card.getImageName(),
                    "drawable",
                    getPackageName()
            );

            imgCard.setImageResource(resId);
        });

        // Загрузка карты дня
        vm.loadCard();

        // Кнопка назад
        btnBack.setOnClickListener(v -> finish());

        setupNav();
    }

    /**
     * Инициализация UI элементов экрана.
     *
     * Связывает Java-поля с элементами XML разметки.
     */
    private void initViews() {

        btnBack = findViewById(R.id.btnBack);
        imgCard = findViewById(R.id.imgCard);

        title = findViewById(R.id.cardTitle);
        desc = findViewById(R.id.txtDescription);
        advice = findViewById(R.id.txtAdvice);
        important = findViewById(R.id.txtImportant);
    }

    /**
     * Настройка нижней навигационной панели приложения.
     *
     * Обрабатывает переходы между основными экранами:
     * - Home (главный экран)
     * - Library (библиотека)
     * - Profile (профиль)
     *
     * При переходе текущая Activity закрывается.
     */
    private void setupNav() {

        BottomNavigationView nav =
                findViewById(R.id.bottom_navigation);

        // Сброс выделения элемента меню
        nav.setSelectedItemId(0);

        // Обработка кликов по навигации
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