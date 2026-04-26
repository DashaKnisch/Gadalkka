package com.dkkk.soothsayer.viewmodel.library;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.dkkk.soothsayer.repository.ArticleRepository;
import com.dkkk.soothsayer.repository.FavoriteRepository;

/**
 * Фабрика для создания экземпляра ArticleViewModel.
 *
 * Используется для передачи зависимостей (репозиториев) во ViewModel,
 * так как стандартный ViewModelProvider не поддерживает конструкторы с параметрами.
 */
public class ArticleDetailViewModelFactory implements ViewModelProvider.Factory {

    /** Репозиторий статей */
    private final ArticleRepository articleRepository;

    /** Репозиторий избранного */
    private final FavoriteRepository favoriteRepository;

    /**
     * Конструктор фабрики ViewModel.
     *
     * @param articleRepository репозиторий статей
     * @param favoriteRepository репозиторий избранного
     */
    public ArticleDetailViewModelFactory(ArticleRepository articleRepository,
                                         FavoriteRepository favoriteRepository) {
        this.articleRepository = articleRepository;
        this.favoriteRepository = favoriteRepository;
    }

    /**
     * Создаёт ViewModel нужного типа.
     *
     * @param modelClass класс запрашиваемой ViewModel
     * @param <T> тип ViewModel
     * @return экземпляр ArticleViewModel
     * @throws IllegalArgumentException если передан неизвестный класс ViewModel
     */
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(ArticleViewModel.class)) {
            return (T) new ArticleViewModel(articleRepository, favoriteRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}