package com.dkkk.soothsayer.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dkkk.soothsayer.model.HoroscopeItem;

import java.util.Random;

/**
 * Репозиторий гороскопа.
 *
 * Отвечает за работу с локальной SQLite базой данных
 * и получение данных для гороскопа.
 *
 * Основные задачи:
 * - получение изображения знака зодиака
 * - получение случайного описания и совета
 * - формирование объекта HoroscopeItem для UI слоя
 */
public class HoroscopeRepository {

    /** Контекст приложения для доступа к базе данных */
    private final Context context;

    /**
     * Конструктор репозитория.
     *
     * @param context контекст приложения
     */
    public HoroscopeRepository(Context context) {
        this.context = context;
    }

    /**
     * Получает гороскоп для указанного знака зодиака.
     *
     * Логика работы:
     * - извлекает изображение знака из таблицы zodiac_signs
     * - выбирает случайный текст из таблицы horoscope_texts
     * - формирует и возвращает объект HoroscopeItem
     *
     * @param sign название знака зодиака
     * @return объект HoroscopeItem с данными гороскопа
     */
    public HoroscopeItem getHoroscope(String sign) {

        SQLiteDatabase db =
                context.openOrCreateDatabase("gadalka.db", Context.MODE_PRIVATE, null);

        String imageName = "";
        String description = "";
        String advice = "";

        /**
         * Получение изображения знака зодиака
         */
        Cursor signCursor = db.rawQuery(
                "SELECT image_name FROM zodiac_signs WHERE name=?",
                new String[]{sign}
        );

        if (signCursor.moveToFirst()) {
            imageName = signCursor.getString(0);
        }
        signCursor.close();

        /**
         * Получение случайного текста гороскопа
         * (описание + совет)
         */
        Cursor textCursor = db.rawQuery(
                "SELECT description, advice FROM horoscope_texts ORDER BY RANDOM() LIMIT 1",
                null
        );

        if (textCursor.moveToFirst()) {
            description = textCursor.getString(0);
            advice = textCursor.getString(1);
        }
        textCursor.close();

        db.close();

        /**
         * Формирование итогового объекта данных
         */
        return new HoroscopeItem(sign, imageName, description, advice);
    }
}