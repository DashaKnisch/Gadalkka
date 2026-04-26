package com.dkkk.soothsayer.repository;

import com.dkkk.soothsayer.data.ArticleDBHelper;
import com.dkkk.soothsayer.model.Article;

import java.util.List;

/**
 * Репозиторий статей.
 *
 * <p>
 * Является промежуточным слоем между источником данных (SQLite через ArticleDBHelper)
 * и слоем ViewModel.
 * </p>
 *
 * <p>
 * Отвечает за предоставление данных о статьях и инкапсулирует работу с базой данных.
 * UI слой не обращается к базе напрямую — только через данный репозиторий.
 * </p>
 *
 * <p>
 * Используется в архитектуре MVVM как Data Access Layer.
 * </p>
 */
public class ArticleRepository {

    /** Ссылка на помощник работы с SQLite базой данных */
    private final ArticleDBHelper db;

    /**
     * Создаёт репозиторий статей.
     *
     * @param db объект доступа к базе данных статей
     */
    public ArticleRepository(ArticleDBHelper db) {
        this.db = db;
    }

    /**
     * Получает список всех статей из базы данных.
     *
     * @return список всех статей
     */
    public List<Article> getAllArticles() {
        return db.getAllArticles();
    }

    /**
     * Получает статью по её уникальному идентификатору.
     *
     * @param id идентификатор статьи
     * @return объект статьи или null, если не найдено
     */
    public Article getArticleById(int id) {
        return db.getArticleById(id);
    }

    /**
     * Получает список статей по категории.
     *
     * @param category название категории
     * @return список статей, относящихся к категории
     */
    public List<Article> getArticlesByCategory(String category) {
        return db.getArticlesByCategory(category);
    }

    /**
     * Поиск статей по текстовому запросу.
     *
     * <p>
     * Поиск выполняется по заголовку и категории.
     * </p>
     *
     * @param text строка поиска
     * @return список найденных статей
     */
    public List<Article> searchArticles(String text) {
        return db.searchArticles(text);
    }
}