package com.dkkk.soothsayer.viewmodel.library;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dkkk.soothsayer.model.Article;
import com.dkkk.soothsayer.repository.ArticleRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ViewModel для экрана библиотеки статей.
 *
 * Отвечает за:
 * - загрузку всех статей
 * - поиск по статьям
 * - группировку статей по категориям
 * - предоставление данных UI через LiveData
 */
public class LibraryViewModel extends ViewModel {

    /** Репозиторий статей */
    private ArticleRepository repo;

    /** LiveData сгруппированных статей по категориям */
    private final MutableLiveData<Map<String, List<Article>>> articlesLive =
            new MutableLiveData<>();

    /**
     * Инициализация ViewModel.
     *
     * Устанавливает репозиторий и загружает данные.
     *
     * @param repo репозиторий статей
     */
    public void init(ArticleRepository repo) {
        this.repo = repo;
        loadAll();
    }

    /**
     * Возвращает LiveData списка статей, сгруппированных по категориям.
     *
     * @return карта: категория -> список статей
     */
    public LiveData<Map<String, List<Article>>> getArticles() {
        return articlesLive;
    }

    /**
     * Загружает все статьи из репозитория и группирует их.
     */
    public void loadAll() {
        if (repo == null) return;
        group(repo.getAllArticles());
    }

    /**
     * Выполняет поиск статей по тексту запроса.
     *
     * Если строка пустая — загружаются все статьи.
     *
     * @param query текст поиска
     */
    public void search(String query) {

        if (repo == null) return;

        if (query == null || query.trim().isEmpty()) {
            loadAll();
            return;
        }

        group(repo.searchArticles(query));
    }

    /**
     * Группирует список статей по категориям.
     *
     * Используется для отображения данных в UI с разделением по категориям.
     *
     * @param list список статей
     */
    private void group(List<Article> list) {

        Map<String, List<Article>> map = new LinkedHashMap<>();

        for (Article a : list) {

            if (!map.containsKey(a.category)) {
                map.put(a.category, new ArrayList<Article>());
            }

            map.get(a.category).add(a);
        }

        articlesLive.setValue(map);
    }
}