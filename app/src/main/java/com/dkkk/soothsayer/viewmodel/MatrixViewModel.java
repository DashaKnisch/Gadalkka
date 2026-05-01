package com.dkkk.soothsayer.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.dkkk.soothsayer.model.MatrixResult;
import com.dkkk.soothsayer.repository.MatrixRepository;

/**
 * ViewModel для расчёта "Матрицы судьбы".
 *
 * Отвечает за:
 * - обработку даты рождения
 * - вычисление всех числовых значений матрицы
 * - применение правила приведения чисел (до диапазона 1–22)
 * - получение текстовых значений из базы данных через репозиторий
 * - передачу результата в UI через LiveData
 *
 * Является центральным слоем бизнес-логики между UI и MatrixRepository.
 */
public class MatrixViewModel extends AndroidViewModel {

    /** Результат расчёта матрицы (наблюдаемый объект для UI) */
    public MutableLiveData<MatrixResult> result = new MutableLiveData<>();

    /** Репозиторий для получения текстов из базы данных */
    private final MatrixRepository repo;

    /**
     * Создание ViewModel.
     *
     * Инициализирует репозиторий для работы с БД.
     *
     * @param app контекст приложения
     */
    public MatrixViewModel(@NonNull Application app) {
        super(app);
        repo = new MatrixRepository(app);
    }

    /**
     * Приведение числа к диапазону 1–22.
     *
     * Если число больше 22 — выполняется сложение цифр,
     * пока результат не станет ≤ 22.
     *
     * @param num исходное число
     * @return приведённое число (1–22)
     */
    private int reduce(int num) {

        while (num > 22) {
            int sum = 0;

            while (num > 0) {
                sum += num % 10;
                num /= 10;
            }

            num = sum;
        }

        return num;
    }

    /**
     * Основной метод расчёта матрицы.
     *
     * 1. Проверяет корректность даты
     * 2. Вычисляет все позиции матрицы
     * 3. Применяет формулу приведения (reduce)
     * 4. Получает текстовые значения из базы
     * 5. Возвращает результат в LiveData
     *
     * @param date дата рождения в формате dd.MM.yyyy
     */
    public void calculate(String date) {

        if (date == null || date.trim().isEmpty()) return;
        if (!date.contains(".")) return;

        String[] parts = date.split("\\.");
        if (parts.length != 3) return;

        int day, month, year;

        try {
            day = Integer.parseInt(parts[0]);
            month = Integer.parseInt(parts[1]);
            year = Integer.parseInt(parts[2]);
        } catch (Exception e) {
            return;
        }

        if (day < 1 || day > 31) return;
        if (month < 1 || month > 12) return;
        if (year < 1900) return;

        MatrixResult r = new MatrixResult();

        r.p2 = reduce(day);
        r.p9 = month;
        r.p7 = reduce(year);

        r.p4 = reduce(r.p2 + r.p9 + r.p7);
        r.p10 = reduce(r.p2 + r.p9 + r.p7 + r.p4);

        r.p1 = reduce(r.p2 + r.p9);
        r.p3 = reduce(r.p2 + r.p4);
        r.p6 = reduce(r.p4 + r.p7);
        r.p8 = reduce(r.p7 + r.p9);

        r.p22 = reduce(r.p2 + r.p10);
        r.p42 = reduce(r.p4 + r.p10);
        r.p72 = reduce(r.p7 + r.p10);
        r.p92 = reduce(r.p9 + r.p10);

        r.p21 = reduce(r.p2 + r.p22);
        r.p41 = reduce(r.p4 + r.p42);
        r.p71 = reduce(r.p7 + r.p72);
        r.p91 = reduce(r.p9 + r.p92);

        r.p52 = reduce(r.p42 + r.p72);
        r.p51 = reduce(r.p52 + r.p42);
        r.p53 = reduce(r.p52 + r.p72);

        r.character = repo.getCharacter(r.p10);
        r.parents = repo.getParents(r.p2);
        r.talent = repo.getTalent(r.p9);
        r.finance = repo.getFinance(r.p7);
        r.earnings = repo.getEarnings(r.p53);
        r.partner = repo.getPartner(r.p51);
        r.tail = repo.getTail(r.p4);

        r.spirit = repo.getSpirit(r.p1, r.p8);
        r.money = repo.getMoney(r.p6, r.p3);

        result.setValue(r);
    }
}