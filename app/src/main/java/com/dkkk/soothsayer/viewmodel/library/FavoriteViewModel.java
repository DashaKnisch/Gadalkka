package com.dkkk.soothsayer.viewmodel.library;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dkkk.soothsayer.model.Article;
import com.dkkk.soothsayer.repository.FavoriteRepository;

import java.util.List;

/**
 * ViewModel для работы с избранными статьями.
 *
 * Отвечает за:
 * - загрузку списка избранных статей
 * - удаление статьи из избранного
 * - предоставление данных UI через LiveData
 */
public class FavoriteViewModel extends ViewModel {

    /** Репозиторий избранного */
    private FavoriteRepository repository;

    /** LiveData списка избранных статей */
    private final MutableLiveData<List<Article>> favoritesLive =
            new MutableLiveData<>();

    /**
     * Инициализация ViewModel.
     *
     * Передаёт репозиторий и загружает начальные данные.
     *
     * @param repository репозиторий избранного
     */
    public void init(FavoriteRepository repository) {
        this.repository = repository;
        loadFavorites();
    }

    /**
     * Возвращает LiveData списка избранных статей.
     *
     * @return список избранных статей
     */
    public LiveData<List<Article>> getFavorites() {
        return favoritesLive;
    }

    /**
     * Загружает все избранные статьи из репозитория
     * и обновляет LiveData.
     */
    public void loadFavorites() {
        if (repository == null) return;

        favoritesLive.setValue(repository.getAllFavorites());
    }

    /**
     * Удаляет статью из избранного и обновляет список.
     *
     * @param articleId идентификатор статьи
     */
    public void removeFavorite(int articleId) {
        repository.removeFavorite(articleId);
        loadFavorites();
    }
}