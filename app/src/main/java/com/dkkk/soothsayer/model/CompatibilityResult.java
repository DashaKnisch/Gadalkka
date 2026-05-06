package com.dkkk.soothsayer.model;

/**
 * Модель результата совместимости (CompatibilityResult).
 *
 * Представляет результат расчёта совместимости между двумя людьми
 * на основе их чисел судьбы (1-9), полученных из дат рождения.
 *
 * Используется в:
 * - расчёте совместимости по чакрам/числам судьбы
 * - отображении процента совместимости в UI
 * - выводе текстового описания результата
 *
 * Данные получаются из таблицы compatibility базы данных.
 *
 */
public class CompatibilityResult {

    /** Процент совместимости (0-100). Берётся из колонки percentage таблицы compatibility */
    public int percentage;

    /** Текстовое описание результата. Берётся из колонки text таблицы compatibility */
    public String text;

    /**
     * Конструктор модели результата совместимости.
     *
     * @param percentage процент совместимости (целое число от 0 до 100)
     * @param text текстовое описание результата
     */
    public CompatibilityResult(int percentage, String text) {
        this.percentage = percentage;
        this.text = text;
    }
}