package ru.yandex.practicum.filmorate.model;

/**
 * Класс описывает рейтинг Ассоциации кинокомпаний (англ. Motion Picture Association, сокращённо МРА).
 * Эта оценка определяет возрастное ограничение для фильма.
 */
public enum Rating {
    G("У фильма нет возрастных ограничений"),
    PG("Детям рекомендуется смотреть фильм с родителями"),
    PG_13("Детям до 13 лет просмотр не желателен"),
    R("Лицам до 17 лет просматривать фильм можно только в присутствии взрослого"),
    NC_17("Лицам до 18 лет просмотр запрещён");

    /**
     * Описание рейтинга
     */
    private final String description;
    Rating(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
