package com.dkkk.soothsayer.viewmodel.taro;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.dkkk.soothsayer.model.TarotCard;
import com.dkkk.soothsayer.repository.TarotRepository;

/**
 * ViewModel для экрана "Карта дня".
 *
 * Отвечает за:
 * - получение данных карты дня из Repository
 * - хранение состояния UI через LiveData
 * - передачу данных из слоя Model в View (Activity)
 *
 * Является частью архитектуры MVVM.
 */
public class CardDayViewModel extends AndroidViewModel {

    /**
     * LiveData, содержащая текущую карту дня.
     *
     * Используется для автоматического обновления UI
     * при изменении данных карты.
     */
    public MutableLiveData<TarotCard> cardLiveData = new MutableLiveData<>();

    /**
     * Конструктор ViewModel.
     *
     * @param application контекст приложения
     */
    public CardDayViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * Загружает карту дня из репозитория.
     *
     * Алгоритм:
     * - создаётся экземпляр TarotRepository
     * - запрашивается карта дня (getCardOfDay)
     * - результат передаётся в LiveData
     *
     * После вызова метода UI автоматически обновляется,
     * если Activity подписана на cardLiveData.
     */
    public void loadCard() {

        // Создание репозитория для доступа к данным
        TarotRepository repository =
                new TarotRepository(getApplication());

        // Получение карты дня из базы данных
        TarotCard card = repository.getCardOfDay();

        // Обновление LiveData (уведомляет UI)
        cardLiveData.setValue(card);
    }
}