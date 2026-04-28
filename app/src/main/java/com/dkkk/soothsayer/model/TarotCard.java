package com.dkkk.soothsayer.model;

/**
 * Модель карты Таро.
 *
 * Содержит основную информацию о карте:
 * - название
 * - изображение
 * - описание
 * - совет
 * - предупреждение
 * - текст для расклада
 *
 * Используется в:
 * - отображении карты дня
 * - раскладах Таро
 * - отображении UI карточек
 */
public class TarotCard {

    /** Уникальный идентификатор карты */
    private int id;

    /** Название карты */
    private String name;

    /** Имя изображения (drawable resource name) */
    private String imageName;

    /** Общее описание карты */
    private String description;

    /** Совет карты */
    private String advice;

    /** Важное предупреждение или значение */
    private String important;

    /** Текст, используемый в раскладе */
    private String spreadText;

    /**
     * Конструктор карты Таро.
     *
     * @param id уникальный идентификатор
     * @param name название карты
     * @param imageName имя изображения
     * @param description описание карты
     * @param advice совет карты
     * @param important важная информация / предупреждение
     * @param spreadText текст для расклада
     */
    public TarotCard(int id,
                     String name,
                     String imageName,
                     String description,
                     String advice,
                     String important,
                     String spreadText) {

        this.id = id;
        this.name = name;
        this.imageName = imageName;
        this.description = description;
        this.advice = advice;
        this.important = important;
        this.spreadText = spreadText;
    }

    /** @return ID карты */
    public int getId() {
        return id;
    }

    /** @return название карты */
    public String getName() {
        return name;
    }

    /** @return имя изображения */
    public String getImageName() {
        return imageName;
    }

    /** @return описание карты */
    public String getDescription() {
        return description;
    }

    /** @return совет карты */
    public String getAdvice() {
        return advice;
    }

    /** @return важная информация */
    public String getImportant() {
        return important;
    }

    /** @return текст расклада */
    public String getSpreadText() {
        return spreadText;
    }
}