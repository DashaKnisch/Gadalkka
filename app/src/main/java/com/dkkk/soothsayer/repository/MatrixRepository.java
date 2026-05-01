package com.dkkk.soothsayer.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dkkk.soothsayer.data.MatrixDBHelper;

/**
 * Репозиторий Матрицы судьбы.
 *
 * Отвечает за:
 * - получение числовых значений матрицы из SQLite
 * - получение текстовых расшифровок из базы данных
 * - работу с таблицами характеристик, кармы и сфер жизни
 *
 * Использует локальную SQLite базу (matrix.db),
 * подключаемую через MatrixDBHelper.
 */
public class MatrixRepository {

    /** Хелпер базы данных */
    private final MatrixDBHelper dbHelper;

    /**
     * Создание репозитория.
     *
     * @param context контекст приложения
     */
    public MatrixRepository(Context context) {
        this.dbHelper = new MatrixDBHelper(context);
    }

    /**
     * Получение подключения к базе данных.
     *
     * @return readable SQLiteDatabase
     */
    private SQLiteDatabase getDb() {
        return dbHelper.getReadableDatabase();
    }

    /**
     * Получение текстового описания из таблицы по id.
     *
     * Используется для таблиц:
     * - character_matrix
     * - parents_matrix
     * - talent_matrix
     * - finance_matrix
     * - ideal_partner
     * - karmic_tail_matrix
     *
     * @param table имя таблицы
     * @param id идентификатор записи
     * @return текстовое описание или пустая строка
     */
    private String getDescription(String table, int id) {

        SQLiteDatabase db = getDb();

        Cursor c = db.rawQuery(
                "SELECT description FROM " + table + " WHERE id=?",
                new String[]{String.valueOf(id)}
        );

        String result = "";

        if (c.moveToFirst()) {
            result = c.getString(0);
        }

        c.close();

        return result;
    }

    /** Характер человека */
    public String getCharacter(int id) {
        return getDescription("character_matrix", id);
    }

    /** Родительские программы */
    public String getParents(int id) {
        return getDescription("parents_matrix", id);
    }

    /** Таланты */
    public String getTalent(int id) {
        return getDescription("talent_matrix", id);
    }

    /** Финансовая сфера */
    public String getFinance(int id) {
        return getDescription("finance_matrix", id);
    }

    /** Сфера профессии / заработка */
    public String getEarnings(int id) {

        SQLiteDatabase db = getDb();

        Cursor c = db.rawQuery(
                "SELECT profession FROM earning_sphere WHERE id=?",
                new String[]{String.valueOf(id)}
        );

        String result = "";

        if (c.moveToFirst()) {
            result = c.getString(0);
        }

        c.close();

        return result;
    }

    /** Идеальный партнёр */
    public String getPartner(int id) {
        return getDescription("ideal_partner", id);
    }

    /** Кармический хвост */
    public String getTail(int id) {
        return getDescription("karmic_tail_matrix", id);
    }

    /**
     * Духовная карма родителей.
     *
     * Объединяет:
     * - father_text
     * - mother_text
     *
     * @param fatherId id отцовской линии
     * @param motherId id материнской линии
     * @return объединённое текстовое описание
     */
    public String getSpirit(int fatherId, int motherId) {

        SQLiteDatabase db = getDb();

        String result = "";

        Cursor f = db.rawQuery(
                "SELECT father_text FROM spiritual_karma_parents WHERE id=?",
                new String[]{String.valueOf(fatherId)}
        );

        if (f.moveToFirst()) {
            result += f.getString(0) + "\n\n";
        }
        f.close();

        Cursor m = db.rawQuery(
                "SELECT mother_text FROM spiritual_karma_parents WHERE id=?",
                new String[]{String.valueOf(motherId)}
        );

        if (m.moveToFirst()) {
            result += m.getString(0);
        }
        m.close();

        return result;
    }

    /**
     * Материальная карма родителей.
     *
     * Объединяет:
     * - father_text
     * - mother_text
     *
     * @param fatherId id отцовской линии
     * @param motherId id материнской линии
     * @return текстовое описание денежной кармы
     */
    public String getMoney(int fatherId, int motherId) {

        SQLiteDatabase db = getDb();

        String result = "";

        Cursor f = db.rawQuery(
                "SELECT father_text FROM material_karma_parents WHERE id=?",
                new String[]{String.valueOf(fatherId)}
        );

        if (f.moveToFirst()) {
            result += f.getString(0) + "\n\n";
        }
        f.close();

        Cursor m = db.rawQuery(
                "SELECT mother_text FROM material_karma_parents WHERE id=?",
                new String[]{String.valueOf(motherId)}
        );

        if (m.moveToFirst()) {
            result += m.getString(0);
        }
        m.close();

        return result;
    }
}