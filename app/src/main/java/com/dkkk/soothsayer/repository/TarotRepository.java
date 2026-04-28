package com.dkkk.soothsayer.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dkkk.soothsayer.model.TarotCard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Репозиторий для работы с картами Таро.
 *
 * Отвечает за доступ к данным SQLite базы taro.db и бизнес-логику:
 * - получение "Карты дня"
 * - генерация случайного расклада (3 карты)
 * - выбор карт из базы данных
 *
 * Является промежуточным слоем между ViewModel и SQLite.
 */
public class TarotRepository {

    /** Контекст приложения для доступа к базе данных */
    private final Context context;

    /**
     * Конструктор репозитория Таро.
     *
     * @param context контекст приложения
     */
    public TarotRepository(Context context) {
        this.context = context;
    }

    // =========================
    // КАРТА ДНЯ
    // =========================

    /**
     * Возвращает "Карту дня".
     *
     * Логика:
     * - каждый день генерируется новая карта
     * - если карта уже была выбрана сегодня — используется сохранённая
     *
     * @return объект TarotCard
     */
    public TarotCard getCardOfDay() {

        String today = new SimpleDateFormat("yyyyMMdd", Locale.getDefault())
                .format(new Date());

        var prefs = context.getSharedPreferences("card_day", Context.MODE_PRIVATE);

        String savedDate = prefs.getString("date", "");
        int savedId = prefs.getInt("card_id", -1);

        int cardId;

        if (today.equals(savedDate) && savedId != -1) {
            cardId = savedId;
        } else {
            cardId = new Random().nextInt(22) + 1;

            prefs.edit()
                    .putString("date", today)
                    .putInt("card_id", cardId)
                    .apply();
        }

        return getCardById(cardId);
    }

    // =========================
    // ПОЛУЧЕНИЕ КАРТЫ ПО ID
    // =========================

    /**
     * Получает карту Таро по её ID из базы данных.
     *
     * @param id идентификатор карты
     * @return объект TarotCard или null, если не найден
     */
    private TarotCard getCardById(int id) {

        SQLiteDatabase db =
                context.openOrCreateDatabase("taro.db", Context.MODE_PRIVATE, null);

        Cursor cursor = db.rawQuery(
                "SELECT * FROM tarot_cards WHERE id=?",
                new String[]{String.valueOf(id)}
        );

        TarotCard card = null;

        if (cursor.moveToFirst()) {

            card = new TarotCard(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(7)
            );
        }

        cursor.close();
        db.close();

        return card;
    }

    // =========================
    // РАСКЛАД 3 КАРТЫ
    // =========================

    /**
     * Возвращает случайный расклад из 3 карт по категории.
     *
     * Логика:
     * - выбираются случайные карты из базы
     * - используется SQL ORDER BY RANDOM()
     *
     * @param category категория расклада
     * @return список из 3 карт TarotCard
     */
    public List<TarotCard> getRandomSpread(String category) {

        SQLiteDatabase db =
                context.openOrCreateDatabase("taro.db", Context.MODE_PRIVATE, null);

        List<TarotCard> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM tarot_cards WHERE category=? ORDER BY RANDOM() LIMIT 3",
                new String[]{category}
        );

        try {

            if (cursor.moveToFirst()) {

                do {

                    TarotCard card = new TarotCard(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(7)
                    );

                    list.add(card);

                } while (cursor.moveToNext());
            }

        } finally {
            cursor.close();
            db.close();
        }

        return list;
    }
}