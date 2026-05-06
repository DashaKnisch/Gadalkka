package com.dkkk.soothsayer.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.dkkk.soothsayer.model.CompatibilityResult;
import com.dkkk.soothsayer.repository.CompatibilityRepository;

/**
 * ViewModel для экрана совместимости (CompatibilityViewModel).
 *
 * Отвечает за:
 * - расчёт чисел судьбы из дат рождения
 * - получение результата совместимости из репозитория
 * - хранение LiveData с результатом для UI
 *
 * Используется в CompatibilityFragment.
 *
 */
public class CompatibilityViewModel extends AndroidViewModel {

    /** LiveData с результатом совместимости для наблюдения из UI */
    public MutableLiveData<CompatibilityResult> result = new MutableLiveData<>();

    /** Репозиторий для работы с БД совместимости */
    private final CompatibilityRepository repo;

    /**
     * Конструктор ViewModel.
     *
     * @param app экземпляр приложения (используется для доступа к контексту)
     */
    public CompatibilityViewModel(@NonNull Application app) {
        super(app);
        repo = new CompatibilityRepository(app);
    }

    /**
     * Рассчитывает совместимость по двум датам рождения.
     *
     * Алгоритм:
     * 1. Вычисляет число судьбы для каждой даты
     * 2. Запрашивает результат из репозитория
     * 3. Обновляет LiveData для отображения в UI
     *
     * @param d1 день рождения первого человека
     * @param m1 месяц рождения первого человека
     * @param y1 год рождения первого человека
     * @param d2 день рождения второго человека
     * @param m2 месяц рождения второго человека
     * @param y2 год рождения второго человека
     */
    public void calculate(int d1, int m1, int y1, int d2, int m2, int y2) {

        int n1 = reduceToOneDigit(d1, m1, y1);
        int n2 = reduceToOneDigit(d2, m2, y2);

        CompatibilityResult r = repo.getResult(n1, n2);

        result.setValue(r);
    }

    /**
     * Сворачивает дату рождения до числа судьбы (1-9).
     *
     * Алгоритм:
     * 1. Суммирует все цифры дня, месяца и года
     * 2. Повторяет суммирование пока результат не станет однозначным
     *
     * @param d день рождения
     * @param m месяц рождения
     * @param y год рождения
     * @return число судьбы (от 1 до 9)
     */
    private int reduceToOneDigit(int d, int m, int y) {

        int sum = sumDigits(d) + sumDigits(m) + sumDigits(y);

        while (sum > 9) {
            sum = sumDigits(sum);
        }

        return sum;
    }

    /**
     * Суммирует все цифры числа.
     *
     * Пример: sumDigits(123) = 1+2+3 = 6
     *
     * @param num исходное число
     * @return сумма цифр числа
     */
    private int sumDigits(int num) {
        int s = 0;
        while (num > 0) {
            s += num % 10;
            num /= 10;
        }
        return s;
    }
}