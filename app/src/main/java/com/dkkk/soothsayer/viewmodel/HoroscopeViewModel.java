package com.dkkk.soothsayer.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.dkkk.soothsayer.model.HoroscopeItem;
import com.dkkk.soothsayer.repository.HoroscopeRepository;

/**
 * ViewModel для экрана гороскопа.
 *
 * Отвечает за:
 * - получение данных гороскопа из репозитория
 * - хранение текущего состояния гороскопа
 * - передачу данных в UI через LiveData
 *
 * Является связующим слоем между HoroscopeActivity и HoroscopeRepository.
 */
public class HoroscopeViewModel extends AndroidViewModel {

    /** Текущий гороскоп (наблюдаемый объект для UI) */
    public MutableLiveData<HoroscopeItem> horoscope = new MutableLiveData<>();

    /** Репозиторий, отвечающий за работу с базой данных */
    private final HoroscopeRepository repo;

    /**
     * Создание ViewModel.
     *
     * Инициализирует репозиторий.
     *
     * @param application контекст приложения
     */
    public HoroscopeViewModel(@NonNull Application application) {
        super(application);
        repo = new HoroscopeRepository(application);
    }

    /**
     * Загружает гороскоп по выбранному знаку зодиака.
     *
     * Выполняет запрос в репозиторий и обновляет LiveData,
     * что автоматически обновляет UI.
     *
     * @param sign знак зодиака (например: Aries, Leo и т.д.)
     */
    public void loadHoroscope(String sign) {
        HoroscopeItem h = repo.getHoroscope(sign);
        horoscope.setValue(h);
    }
}