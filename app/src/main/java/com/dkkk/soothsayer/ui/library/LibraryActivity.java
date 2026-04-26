package com.dkkk.soothsayer.ui.library;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dkkk.soothsayer.data.ArticleDBHelper;
import com.dkkk.soothsayer.repository.ArticleRepository;
import com.dkkk.soothsayer.R;
import com.dkkk.soothsayer.ui.HomeActivity;
import com.dkkk.soothsayer.ui.ProfileActivity;
import com.dkkk.soothsayer.viewmodel.library.LibraryViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Экран библиотеки статей.
 *
 * <p>
 * Отвечает за отображение списка статей, поиск и навигацию по разделам приложения.
 * Реализует MVVM архитектуру:
 * View → LibraryActivity
 * ViewModel → LibraryViewModel
 * Repository → ArticleRepository
 * </p>
 *
 * <p>
 * Основные функции:
 * <ul>
 *     <li>Отображение списка статей через RecyclerView</li>
 *     <li>Поиск статей по тексту</li>
 *     <li>Переход в экран статьи</li>
 *     <li>Переход в избранное</li>
 *     <li>Навигация через BottomNavigationView</li>
 * </ul>
 * </p>
 */
public class LibraryActivity extends AppCompatActivity {

    /** Список отображаемых элементов (категории + статьи) */
    private RecyclerView recyclerView;

    /** Нижняя навигационная панель */
    private BottomNavigationView nav;

    /** Поле ввода поиска статей */
    private EditText searchInput;

    /** Кнопка перехода в избранное */
    private ImageView btnFavorite;

    /** Адаптер списка статей */
    private LibraryAdapter adapter;

    /** ViewModel библиотеки */
    private LibraryViewModel vm;

    /**
     * Инициализация Activity и всех UI компонентов.
     *
     * @param savedInstanceState сохранённое состояние Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        recyclerView = findViewById(R.id.recyclerView);
        nav = findViewById(R.id.bottom_navigation);
        searchInput = findViewById(R.id.searchInput);
        btnFavorite = findViewById(R.id.btnFavorite);

        vm = new ViewModelProvider(this).get(LibraryViewModel.class);

        ArticleDBHelper db = new ArticleDBHelper(this);
        ArticleRepository repo = new ArticleRepository(db);

        vm.init(repo);

        /**
         * Инициализация адаптера списка статей.
         * При клике открывается ArticleActivity с передачей ID статьи.
         */
        adapter = new LibraryAdapter(article -> {
            Intent i = new Intent(this, ArticleActivity.class);
            i.putExtra("id", article.id);
            startActivity(i);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        /**
         * Подписка на изменения списка статей.
         * Обновляет RecyclerView при изменении данных.
         */
        vm.getArticles().observe(this, grouped -> {
            adapter.setData(grouped);
        });

        /**
         * Поиск статей по введённому тексту.
         */
        searchInput.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                vm.search(s.toString());
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(android.text.Editable s) {}
        });

        /**
         * Переход в экран избранных статей.
         */
        btnFavorite.setOnClickListener(v -> {
            startActivity(new Intent(this, FavoriteActivity.class));
        });

        nav.setSelectedItemId(R.id.library);

        /**
         * Обработка нижней навигации.
         * Переключает между основными экранами приложения.
         */
        nav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            }

            if (id == R.id.library) return true;

            if (id == R.id.profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }

            return false;
        });
    }
}