package com.dkkk.soothsayer.viewmodel.taro;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.dkkk.soothsayer.model.TarotCard;
import com.dkkk.soothsayer.repository.TarotRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel для экрана "Расклад Таро".
 *
 * Отвечает за:
 * - загрузку случайного расклада карт
 * - формирование текстового результата расклада
 * - передачу данных в UI через LiveData
 *
 * Является связующим звеном между Repository и Activity.
 */
public class SpreadViewModel extends AndroidViewModel {

    /** Список карт расклада */
    public MutableLiveData<List<TarotCard>> cards = new MutableLiveData<>();

    /** Текстовый результат расклада */
    public MutableLiveData<String> resultText = new MutableLiveData<>();

    /** Репозиторий для получения данных Таро */
    private final TarotRepository repo;

    /**
     * Конструктор ViewModel.
     *
     * @param application контекст приложения
     */
    public SpreadViewModel(@NonNull Application application) {
        super(application);
        repo = new TarotRepository(application);
    }

    /**
     * Загружает расклад карт по выбранной категории.
     *
     * Логика:
     * - получает 3 случайные карты из Repository
     * - если список пуст → возвращает сообщение об ошибке
     * - иначе формирует текст расклада
     *
     * @param category категория расклада
     */
    public void loadSpread(String category) {

        List<TarotCard> selected = repo.getRandomSpread(category);

        if (selected == null || selected.isEmpty()) {
            cards.setValue(new ArrayList<>());
            resultText.setValue("Нет карт для этой категории");
            return;
        }

        cards.setValue(selected);
        resultText.setValue(buildText(selected));
    }

    /**
     * Формирует текстовое описание расклада из списка карт.
     *
     * Структура:
     * - 1 карта: основное значение
     * - 2 карта: влияние ситуации
     * - 3 карта: предупреждение
     *
     * @param cards список карт
     * @return готовый текст расклада
     */
    private String buildText(List<TarotCard> cards) {

        StringBuilder sb = new StringBuilder();

        if (cards.size() > 0) {
            TarotCard first = cards.get(0);
            sb.append("Основная карта:\n")
                    .append(first.getSpreadText() != null ? first.getSpreadText() : first.getDescription())
                    .append("\n\n");
        }

        if (cards.size() > 1) {
            TarotCard second = cards.get(1);
            sb.append("Влияние ситуации:\n")
                    .append(second.getSpreadText() != null ? second.getSpreadText() : second.getAdvice())
                    .append("\n\n");
        }

        if (cards.size() > 2) {
            TarotCard third = cards.get(2);
            sb.append("Предостережение:\n")
                    .append(third.getSpreadText() != null ? third.getSpreadText() : third.getImportant());
        }

        return sb.toString();
    }
}