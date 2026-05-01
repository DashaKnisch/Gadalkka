package com.dkkk.soothsayer.model;

/**
 * Модель данных для гороскопа.
 *
 * Используется для хранения информации о конкретном знаке зодиака,
 * включая текстовое описание и рекомендации на день.
 *
 * Содержит:
 * - название знака зодиака
 * - имя изображения (ресурс)
 * - описание дня
 * - совет/рекомендацию
 */
public class HoroscopeItem {

    /** Название знака зодиака (например: Овен, Телец) */
    private String sign;

    /** Имя изображения, связанного со знаком */
    private String imageName;

    /** Описание прогноза на день */
    private String description;

    /** Совет или рекомендация на день */
    private String advice;

    /**
     * Конструктор модели гороскопа.
     *
     * @param sign название знака зодиака
     * @param imageName имя изображения
     * @param description описание прогноза
     * @param advice совет на день
     */
    public HoroscopeItem(String sign, String imageName, String description, String advice) {
        this.sign = sign;
        this.imageName = imageName;
        this.description = description;
        this.advice = advice;
    }

    /**
     * Получить название знака зодиака.
     *
     * @return название знака
     */
    public String getSign() {
        return sign;
    }

    /**
     * Получить имя изображения знака.
     *
     * @return имя изображения
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * Получить описание прогноза.
     *
     * @return текст описания
     */
    public String getDescription() {
        return description;
    }

    /**
     * Получить совет на день.
     *
     * @return текст совета
     */
    public String getAdvice() {
        return advice;
    }
}