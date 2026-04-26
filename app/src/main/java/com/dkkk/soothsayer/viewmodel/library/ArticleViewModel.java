package com.dkkk.soothsayer.viewmodel.library;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dkkk.soothsayer.model.Article;
import com.dkkk.soothsayer.repository.ArticleRepository;
import com.dkkk.soothsayer.repository.FavoriteRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ViewModel для работы со статьями и избранным.
 *
 * Отвечает за:
 * - загрузку статей
 * - получение статьи по id
 * - группировку статей по категориям
 * - управление состоянием "избранное"
 */
public class ArticleViewModel extends ViewModel {

    /** Репозиторий статей */
    private ArticleRepository repository;

    /** Репозиторий избранного */
    private FavoriteRepository favoriteRepository;

    /** LiveData текущей статьи */
    private final MutableLiveData<Article> articleLiveData =
            new MutableLiveData<>();

    /** LiveData состояния избранного */
    private final MutableLiveData<Boolean> favoriteLiveData =
            new MutableLiveData<>(false);

    /** Текущая открытая статья */
    private Article currentArticle;

    /**
     * Конструктор ViewModel.
     *
     * @param repository репозиторий статей
     * @param favoriteRepository репозиторий избранного
     */
    public ArticleViewModel(ArticleRepository repository,
                            FavoriteRepository favoriteRepository) {
        this.repository = repository;
        this.favoriteRepository = favoriteRepository;
    }

    /**
     * Возвращает статьи, сгруппированные по категориям.
     *
     * Используется для отображения списка в UI с разделением по категориям.
     *
     * @return карта: категория -> список статей
     */
    public Map<String, List<Article>> getArticlesGrouped() {

        List<Article> all = repository.getAllArticles();

        Map<String, List<Article>> map = new HashMap<>();

        for (Article a : all) {

            if (!map.containsKey(a.category)) {
                map.put(a.category, new ArrayList<>());
            }

            map.get(a.category).add(a);
        }

        return map;
    }

    /**
     * Возвращает LiveData текущей статьи.
     *
     * @return LiveData<Article>
     */
    public LiveData<Article> getArticle() {
        return articleLiveData;
    }

    /**
     * Возвращает LiveData состояния "избранное".
     *
     * @return true если статья в избранном
     */
    public LiveData<Boolean> isFavorite() {
        return favoriteLiveData;
    }

    /**
     * Загружает статью по ID и обновляет LiveData.
     *
     * Также проверяет, находится ли статья в избранном.
     *
     * @param articleId идентификатор статьи
     */
    public void loadArticle(int articleId) {

        currentArticle = repository.getArticleById(articleId);

        articleLiveData.setValue(currentArticle);

        if (currentArticle != null) {
            favoriteLiveData.setValue(
                    favoriteRepository.isFavorite(currentArticle.id)
            );
        } else {
            favoriteLiveData.setValue(false);
        }
    }

    /**
     * Добавляет или удаляет статью из избранного.
     *
     * Если статья уже в избранном — удаляет её,
     * иначе добавляет.
     */
    public void toggleFavorite() {

        if (currentArticle == null) return;

        boolean alreadyFavorite =
                favoriteRepository.isFavorite(currentArticle.id);

        if (alreadyFavorite) {

            favoriteRepository.removeFavorite(currentArticle.id);
            favoriteLiveData.setValue(false);

        } else {

            favoriteRepository.addFavorite(currentArticle);
            favoriteLiveData.setValue(true);
        }
    }
}