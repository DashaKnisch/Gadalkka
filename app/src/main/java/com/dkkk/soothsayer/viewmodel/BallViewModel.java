package com.dkkk.soothsayer.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.dkkk.soothsayer.model.BallPrediction;
import com.dkkk.soothsayer.repository.BallRepository;

/**
 * ViewModel для магического шара предсказаний.
 *
 * Управляет логикой получения предсказаний и предоставляет данные для UI.
 *
 * Архитектура: MVVM (Model-View-ViewModel)
 * Жизненный цикл: привязан к BallActivity, переживает повороты экрана
 *
 * @author Soothsayer Team
 * @version 1.0
 */
public class BallViewModel extends AndroidViewModel {

    /**
     * LiveData с текстом предсказания.
     * UI подписывается на это поле и автоматически обновляется при изменении значения.
     *
     * Используется в:
     * - BallActivity (отображение нового предсказания при нажатии на шар)
     */
    public MutableLiveData<String> prediction = new MutableLiveData<>();

    /**
     * Репозиторий для работы с базой данных предсказаний.
     * Используется для получения случайного предсказания.
     */
    private BallRepository repo;

    /**
     * Конструктор ViewModel.
     *
     * Инициализирует репозиторий для работы с базой данных предсказаний.
     *
     * @param app экземпляр приложения (используется для доступа к контексту и репозиторию)
     */
    public BallViewModel(@NonNull Application app) {
        super(app);
        repo = new BallRepository(app);
    }

    /**
     * Получение случайного предсказания.
     *
     * Алгоритм работы:
     * 1. Запрашивает у репозитория случайное предсказание
     * 2. Если предсказание найдено (не null), обновляет LiveData prediction
     * 3. UI автоматически получает новое значение через observe()
     *
     * Вызывается при нажатии на кнопку магического шара.
     */
    public void getPrediction() {

        BallPrediction p = repo.getRandomPrediction();

        if (p != null) {
            prediction.setValue(p.text);
        }
    }
}