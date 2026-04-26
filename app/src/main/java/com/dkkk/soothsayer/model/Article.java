package com.dkkk.soothsayer.model;

import java.io.Serializable;

/**
 * Модель статьи (Article).
 *
 * Используется как основная сущность в приложении.
 * Представляет запись из базы данных и используется в:
 * - списке статей (Library)
 * - избранном (Favorites)
 * - отображении детальной статьи
 *
 * Реализует Serializable для передачи между Activity через Intent.
 */
public class Article implements Serializable {

    /** Уникальный идентификатор статьи */
    public int id;

    /** Заголовок статьи */
    public String title;

    /** Основной текст статьи */
    public String content;

    /** Категория статьи (например: Таро, Гороскоп и т.д.) */
    public String category;

    /** Автор статьи */
    public String author;

    /** Дата публикации статьи */
    public String date;

    /**
     * Конструктор модели статьи.
     *
     * @param id уникальный идентификатор
     * @param title заголовок статьи
     * @param content текст статьи
     * @param category категория статьи
     * @param author автор статьи
     * @param date дата публикации
     */
    public Article(int id,
                   String title,
                   String content,
                   String category,
                   String author,
                   String date) {

        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.author = author;
        this.date = date;
    }
}