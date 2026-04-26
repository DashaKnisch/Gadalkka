package com.dkkk.soothsayer.repository;

import com.dkkk.soothsayer.data.FavoriteDBHelper;
import com.dkkk.soothsayer.model.Article;

import java.util.List;

/**
 * Репозиторий для работы с избранными статьями.
 *
 * Является прослойкой между ViewModel и локальной базой данных (FavoriteDBHelper).
 * Инкапсулирует всю логику работы с избранным:
 * добавление, удаление, проверка и получение списка избранных статей.
 */
public class FavoriteRepository {

    /** Доступ к базе данных избранных статей */
    private final FavoriteDBHelper db;

    /**
     * Конструктор репозитория.
     *
     * @param db объект для работы с SQLite базой избранного
     */
    public FavoriteRepository(FavoriteDBHelper db) {
        this.db = db;
    }

    /**
     * Добавляет статью в избранное.
     *
     * Если статья уже существует, выполняется замена (REPLACE).
     *
     * @param article статья для добавления
     * @return ID вставленной записи или результат операции базы данных
     */
    public long addFavorite(Article article) {
        return db.addFavorite(article);
    }

    /**
     * Удаляет статью из избранного по её ID.
     *
     * @param articleId ID статьи
     * @return true если удаление прошло успешно, иначе false
     */
    public boolean removeFavorite(int articleId) {
        return db.removeFavorite(articleId);
    }

    /**
     * Проверяет, находится ли статья в избранном.
     *
     * @param articleId ID статьи
     * @return true если статья добавлена в избранное
     */
    public boolean isFavorite(int articleId) {
        return db.isFavorite(articleId);
    }

    /**
     * Получает список всех избранных статей.
     *
     * @return список объектов Article из таблицы favorites
     */
    public List<Article> getAllFavorites() {
        return db.getAllFavorites();
    }
}