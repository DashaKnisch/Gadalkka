package com.dkkk.soothsayer.ui.library;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.dkkk.soothsayer.R;
import com.dkkk.soothsayer.data.ArticleDBHelper;
import com.dkkk.soothsayer.data.FavoriteDBHelper;
import com.dkkk.soothsayer.repository.ArticleRepository;
import com.dkkk.soothsayer.repository.FavoriteRepository;
import com.dkkk.soothsayer.ui.HomeActivity;
import com.dkkk.soothsayer.ui.ProfileActivity;
import com.dkkk.soothsayer.viewmodel.library.ArticleViewModel;
import com.dkkk.soothsayer.viewmodel.library.ArticleDetailViewModelFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Экран детального просмотра статьи.
 *
 * <p>
 * Отображает полную информацию о выбранной статье:
 * <ul>
 *     <li>Категория</li>
 *     <li>Заголовок</li>
 *     <li>Текст статьи</li>
 *     <li>Автор и дата публикации</li>
 * </ul>
 * </p>
 *
 * <p>
 * Также позволяет:
 * <ul>
 *     <li>Добавлять / удалять статью из избранного</li>
 *     <li>Навигироваться по приложению через BottomNavigationView</li>
 *     <li>Возвращаться в библиотеку</li>
 * </ul>
 * </p>
 *
 * <p>
 * Архитектура: MVVM
 * <ul>
 *     <li>View: ArticleActivity</li>
 *     <li>ViewModel: ArticleViewModel (detail variant)</li>
 *     <li>Repository: ArticleRepository + FavoriteRepository</li>
 *     <li>Database: ArticleDBHelper + FavoriteDBHelper</li>
 * </ul>
 * </p>
 */
public class ArticleActivity extends AppCompatActivity {

    /** Кнопка возврата назад в библиотеку */
    private ImageView btnBack;

    /** Поле отображения категории статьи */
    private TextView category;

    /** Поле отображения заголовка статьи */
    private TextView title;

    /** Поле отображения полного текста статьи */
    private TextView content;

    /** Поле отображения автора и даты публикации */
    private TextView meta;

    /** Кнопка добавления/удаления из избранного */
    private Button btnFavorite;

    /** ViewModel, управляющая логикой загрузки статьи и избранного */
    private ArticleViewModel vm;

    /** Нижняя навигация приложения */
    private BottomNavigationView nav;

    /**
     * Инициализация экрана и загрузка статьи.
     *
     * <p>Последовательность:
     * <ol>
     *     <li>Инициализация UI компонентов</li>
     *     <li>Получение ID статьи из Intent</li>
     *     <li>Создание Repository слоёв</li>
     *     <li>Инициализация ViewModel через Factory</li>
     *     <li>Подписка на LiveData</li>
     *     <li>Настройка кнопок и навигации</li>
     * </ol>
     * </p>
     *
     * @param savedInstanceState сохранённое состояние Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        // ---------------- NAVIGATION ----------------

        nav = findViewById(R.id.bottom_navigation);
        nav.setSelectedItemId(R.id.library);

        // ---------------- UI INIT ----------------

        btnBack = findViewById(R.id.btnBack);

        category = findViewById(R.id.articleCategory);
        title = findViewById(R.id.articleTitle);
        content = findViewById(R.id.articleContent);
        meta = findViewById(R.id.articleMeta);

        btnFavorite = findViewById(R.id.btnFavorite);

        /**
         * Возврат назад в библиотеку.
         */
        btnBack.setOnClickListener(v -> finish());

        // ---------------- ARTICLE ID ----------------

        /**
         * ID статьи передаётся через Intent.
         * Если ID отсутствует — экран закрывается.
         */
        int articleId = getIntent().getIntExtra("id", -1);

        if (articleId == -1) {
            Toast.makeText(this, "Статья не найдена", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // ---------------- REPOSITORY ----------------

        ArticleDBHelper articleDb = new ArticleDBHelper(this);
        FavoriteDBHelper favoriteDb = new FavoriteDBHelper(this);

        ArticleRepository articleRepository =
                new ArticleRepository(articleDb);

        FavoriteRepository favoriteRepository =
                new FavoriteRepository(favoriteDb);

        // ---------------- VIEWMODEL FACTORY ----------------

        /**
         * Factory используется, так как ViewModel требует параметры.
         */
        ArticleDetailViewModelFactory factory =
                new ArticleDetailViewModelFactory(
                        articleRepository,
                        favoriteRepository
                );

        vm = new ViewModelProvider(this, factory)
                .get(ArticleViewModel.class);

        btnFavorite.setEnabled(false);

        // ---------------- OBSERVE ARTICLE ----------------

        /**
         * Подписка на загрузку статьи.
         * UI обновляется при изменении данных.
         */
        vm.getArticle().observe(this, article -> {

            if (article == null) {
                Toast.makeText(
                        this,
                        "Не удалось загрузить статью",
                        Toast.LENGTH_SHORT
                ).show();

                finish();
                return;
            }

            category.setText("Категория: " + article.category);
            title.setText(article.title);
            content.setText(article.content);
            meta.setText(
                    "Автор: " + article.author +
                            "\nДата публикации: " + article.date
            );

            btnFavorite.setEnabled(true);
        });

        // ---------------- OBSERVE FAVORITE ----------------

        /**
         * Обновление состояния кнопки избранного.
         */
        vm.isFavorite().observe(this, isFavorite -> {

            if (isFavorite == null) return;

            if (isFavorite) {
                btnFavorite.setText("Убрать из избранного");
            } else {
                btnFavorite.setText("Добавить в избранное");
            }
        });

        /**
         * Переключение состояния избранного.
         */
        btnFavorite.setOnClickListener(v -> vm.toggleFavorite());

        // ---------------- LOAD DATA ----------------

        vm.loadArticle(articleId);

        // ---------------- BOTTOM NAVIGATION ----------------

        nav.setSelectedItemId(R.id.library);

        nav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            }

            if (id == R.id.library) {
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