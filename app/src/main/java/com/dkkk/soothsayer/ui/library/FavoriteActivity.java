package com.dkkk.soothsayer.ui.library;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dkkk.soothsayer.R;
import com.dkkk.soothsayer.data.FavoriteDBHelper;
import com.dkkk.soothsayer.repository.FavoriteRepository;
import com.dkkk.soothsayer.ui.HomeActivity;
import com.dkkk.soothsayer.ui.ProfileActivity;
import com.dkkk.soothsayer.viewmodel.library.FavoriteViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Экран "Избранное".
 *
 * <p>
 * Отвечает за отображение списка избранных статей пользователя,
 * а также позволяет:
 * <ul>
 *     <li>Открывать статью из избранного</li>
 *     <li>Удалять статью из избранного</li>
 *     <li>Навигироваться по приложению через BottomNavigationView</li>
 * </ul>
 * </p>
 *
 * <p>
 * Архитектура: MVVM
 * <ul>
 *     <li>View: FavoriteActivity</li>
 *     <li>ViewModel: FavoriteViewModel</li>
 *     <li>Repository: FavoriteRepository</li>
 *     <li>DB: FavoriteDBHelper</li>
 * </ul>
 * </p>
 */
public class FavoriteActivity extends AppCompatActivity {

    /** Список UI-элементов для отображения избранных статей */
    private RecyclerView recyclerView;

    /** Кнопка возврата назад */
    private ImageView btnBack;

    /** Нижняя навигация приложения */
    private BottomNavigationView nav;

    /** Адаптер списка избранных статей */
    private FavoriteAdapter adapter;

    /** ViewModel, управляющая логикой избранного */
    private FavoriteViewModel vm;

    /**
     * Инициализация Activity и привязка UI.
     *
     * <p>Последовательность:
     * <ol>
     *     <li>Инициализация View</li>
     *     <li>Создание ViewModel через factory</li>
     *     <li>Инициализация Repository</li>
     *     <li>Подключение RecyclerView</li>
     *     <li>Подписка на LiveData</li>
     *     <li>Настройка навигации</li>
     * </ol>
     * </p>
     *
     * @param savedInstanceState сохранённое состояние Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        // ---------------- UI INIT ----------------

        recyclerView = findViewById(R.id.recyclerView);
        btnBack = findViewById(R.id.btnBack);
        nav = findViewById(R.id.bottom_navigation);

        // ---------------- VIEWMODEL ----------------

        vm = new ViewModelProvider(this).get(FavoriteViewModel.class);

        FavoriteDBHelper db = new FavoriteDBHelper(this);
        FavoriteRepository repo = new FavoriteRepository(db);

        vm.init(repo);

        // ---------------- ADAPTER ----------------

        /**
         * Адаптер отвечает за:
         * - отображение статей
         * - открытие статьи
         * - удаление из избранного
         */
        adapter = new FavoriteAdapter(new FavoriteAdapter.Listener() {

            /**
             * Открытие статьи из избранного.
             *
             * @param article выбранная статья
             */
            @Override
            public void onOpen(com.dkkk.soothsayer.model.Article article) {
                Intent i = new Intent(FavoriteActivity.this,
                        com.dkkk.soothsayer.ui.library.ArticleActivity.class);

                i.putExtra("id", article.id);
                startActivity(i);
            }

            /**
             * Удаление статьи из избранного.
             *
             * @param article статья для удаления
             */
            @Override
            public void onDelete(com.dkkk.soothsayer.model.Article article) {
                vm.removeFavorite(article.id);
            }
        });

        // ---------------- RECYCLER ----------------

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        /**
         * Подписка на изменения списка избранного.
         * ViewModel обновляет UI автоматически при изменении данных.
         */
        vm.getFavorites().observe(this, articles -> {
            adapter.setData(articles);
        });

        // ---------------- BACK BUTTON ----------------

        /**
         * Возврат назад в предыдущий экран (LibraryActivity).
         */
        btnBack.setOnClickListener(v -> finish());

        // ---------------- BOTTOM NAV ----------------

        /**
         * Навигация нижнего меню.
         *
         * Важно: текущий экран — Library context,
         * поэтому library item считается активным.
         */
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