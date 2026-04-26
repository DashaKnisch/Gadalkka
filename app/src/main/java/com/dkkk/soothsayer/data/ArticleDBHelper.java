package com.dkkk.soothsayer.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dkkk.soothsayer.model.Article;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы с базой данных статей.
 *
 * Использует готовую SQLite базу данных, хранящуюся в assets.
 *
 * Отвечает за:
 * - копирование базы данных при первом запуске
 * - открытие базы в режиме read-only
 * - получение статей из таблицы articles
 * - преобразование Cursor -> Article
 *
 * Является низкоуровневым Data Access Layer.
 */
public class ArticleDBHelper {

    /** Имя базы данных в assets и в системе */
    private static final String DB_NAME = "database.db";

    /** Контекст приложения */
    private final Context context;

    /** Полный путь к базе данных в устройстве */
    private final String DB_PATH;

    /**
     * Конструктор класса доступа к статьям.
     *
     * При создании проверяет наличие базы данных
     * и копирует её из assets при необходимости.
     *
     * @param context контекст приложения
     */
    public ArticleDBHelper(Context context) {
        this.context = context;
        DB_PATH = context.getDatabasePath(DB_NAME).getPath();
        copyDatabaseIfNeeded();
    }

    /**
     * Копирует базу данных из assets в память устройства,
     * если она ещё не существует.
     *
     * Используется при первом запуске приложения.
     */
    private void copyDatabaseIfNeeded() {

        File dbFile = new File(DB_PATH);
        if (dbFile.exists()) return;

        File parent = dbFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (InputStream is = context.getAssets().open(DB_NAME);
             OutputStream os = new FileOutputStream(dbFile)) {

            byte[] buffer = new byte[4096];
            int length;

            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }

            os.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Открывает базу данных в режиме только для чтения.
     *
     * @return объект SQLiteDatabase
     */
    private SQLiteDatabase getDB() {
        return SQLiteDatabase.openDatabase(
                DB_PATH,
                null,
                SQLiteDatabase.OPEN_READONLY
        );
    }

    /**
     * Преобразует Cursor в объект Article.
     *
     * Используется для унификации маппинга данных из базы.
     *
     * @param c курсор SQLite
     * @return объект Article
     */
    private Article mapArticle(Cursor c) {

        return new Article(
                c.getInt(c.getColumnIndexOrThrow("id")),
                c.getString(c.getColumnIndexOrThrow("title")),
                c.getString(c.getColumnIndexOrThrow("content")),
                c.getString(c.getColumnIndexOrThrow("category")),
                c.getString(c.getColumnIndexOrThrow("author")),
                c.getString(c.getColumnIndexOrThrow("date"))
        );
    }

    /**
     * Получает все статьи из базы данных.
     *
     * Сортировка:
     * - по категории
     * - затем по id
     *
     * @return список всех статей
     */
    public List<Article> getAllArticles() {

        List<Article> list = new ArrayList<>();
        SQLiteDatabase db = getDB();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM articles ORDER BY category, id",
                null
        );

        try {
            while (cursor.moveToNext()) {
                list.add(mapArticle(cursor));
            }
        } finally {
            cursor.close();
            db.close();
        }

        return list;
    }

    /**
     * Получает статью по её ID.
     *
     * @param id идентификатор статьи
     * @return Article или null, если не найдено
     */
    public Article getArticleById(int id) {

        SQLiteDatabase db = getDB();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM articles WHERE id=? LIMIT 1",
                new String[]{String.valueOf(id)}
        );

        try {
            if (cursor.moveToFirst()) {
                return mapArticle(cursor);
            }
            return null;
        } finally {
            cursor.close();
            db.close();
        }
    }

    /**
     * Получает статьи по категории.
     *
     * @param category категория статьи
     * @return список статей данной категории
     */
    public List<Article> getArticlesByCategory(String category) {

        List<Article> list = new ArrayList<>();
        SQLiteDatabase db = getDB();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM articles WHERE category=? ORDER BY id",
                new String[]{category}
        );

        try {
            while (cursor.moveToNext()) {
                list.add(mapArticle(cursor));
            }
        } finally {
            cursor.close();
            db.close();
        }

        return list;
    }

    /**
     * Поиск статей по тексту.
     *
     * Поиск выполняется по:
     * - заголовку
     * - категории
     * - содержимому статьи
     *
     * @param text строка поиска
     * @return список найденных статей
     */
    public List<Article> searchArticles(String text) {

        List<Article> list = new ArrayList<>();
        SQLiteDatabase db = getDB();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM articles WHERE title LIKE ? OR category LIKE ? OR content LIKE ? ORDER BY category, id",
                new String[]{
                        "%" + text + "%",
                        "%" + text + "%",
                        "%" + text + "%"
                }
        );

        try {
            while (cursor.moveToNext()) {
                list.add(mapArticle(cursor));
            }
        } finally {
            cursor.close();
            db.close();
        }

        return list;
    }
}