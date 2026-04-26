package com.dkkk.soothsayer.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dkkk.soothsayer.model.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы с базой данных избранных статей.
 *
 * Отвечает за:
 * - создание таблицы favorites
 * - добавление статьи в избранное
 * - удаление статьи из избранного
 * - проверку наличия статьи в избранном
 * - получение всех избранных статей
 *
 * Выполняет преобразование данных SQLite -> объект Article.
 */
public class FavoriteDBHelper extends SQLiteOpenHelper {

    /** Название базы данных избранного */
    private static final String DB_NAME = "favorites.db";

    /** Версия базы данных */
    private static final int DB_VERSION = 1;

    /** Название таблицы избранного */
    public static final String TABLE_FAVORITES = "favorites";

    /**
     * Конструктор класса базы данных избранного.
     *
     * @param context контекст приложения
     */
    public FavoriteDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Создание таблицы избранных статей.
     *
     * Таблица содержит:
     * - id: внутренний ID записи
     * - article_id: ID статьи (уникальный)
     * - title: заголовок
     * - content: текст статьи
     * - category: категория
     * - author: автор
     * - date: дата публикации
     *
     * @param db база данных SQLite
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_FAVORITES + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "article_id INTEGER NOT NULL UNIQUE, " +
                        "title TEXT NOT NULL, " +
                        "content TEXT NOT NULL, " +
                        "category TEXT, " +
                        "author TEXT, " +
                        "date TEXT)"
        );
    }

    /**
     * Обновление структуры базы данных.
     *
     * При изменении версии таблица пересоздаётся.
     *
     * @param db база данных
     * @param oldVersion старая версия
     * @param newVersion новая версия
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }

    /**
     * Добавляет статью в избранное.
     *
     * Если статья уже существует — выполняется замена (CONFLICT_REPLACE).
     *
     * @param article статья для добавления
     * @return ID вставленной записи или -1 при ошибке
     */
    public long addFavorite(Article article) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("article_id", article.id);
        values.put("title", article.title);
        values.put("content", article.content);
        values.put("category", article.category);
        values.put("author", article.author);
        values.put("date", article.date);

        long result = db.insertWithOnConflict(
                TABLE_FAVORITES,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );

        db.close();
        return result;
    }

    /**
     * Удаляет статью из избранного по ID.
     *
     * @param articleId ID статьи
     * @return true если запись была удалена
     */
    public boolean removeFavorite(int articleId) {

        SQLiteDatabase db = getWritableDatabase();

        int rows = db.delete(
                TABLE_FAVORITES,
                "article_id=?",
                new String[]{String.valueOf(articleId)}
        );

        db.close();
        return rows > 0;
    }

    /**
     * Проверяет, находится ли статья в избранном.
     *
     * @param articleId ID статьи
     * @return true если статья уже добавлена в избранное
     */
    public boolean isFavorite(int articleId) {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT 1 FROM " + TABLE_FAVORITES +
                        " WHERE article_id=? LIMIT 1",
                new String[]{String.valueOf(articleId)}
        );

        try {
            return cursor.moveToFirst();
        } finally {
            cursor.close();
            db.close();
        }
    }

    /**
     * Получает список всех избранных статей.
     *
     * Выполняется преобразование Cursor -> List<Article>.
     *
     * @return список избранных статей
     */
    public List<Article> getAllFavorites() {

        List<Article> list = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT article_id, title, content, category, author, date " +
                        "FROM " + TABLE_FAVORITES + " ORDER BY id DESC",
                null
        );

        try {
            while (cursor.moveToNext()) {

                list.add(new Article(
                        cursor.getInt(cursor.getColumnIndexOrThrow("article_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("title")),
                        cursor.getString(cursor.getColumnIndexOrThrow("content")),
                        cursor.getString(cursor.getColumnIndexOrThrow("category")),
                        cursor.getString(cursor.getColumnIndexOrThrow("author")),
                        cursor.getString(cursor.getColumnIndexOrThrow("date"))
                ));
            }
        } finally {
            cursor.close();
            db.close();
        }

        return list;
    }
}