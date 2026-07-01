package ru.yandex.practicum.filmorate.model;

/**
 * Класс описывает возможные статусы дружбы между пользователями
 */
public enum FriendshipStatus {
    UNCONFIRMED("Пользователь отправил запрос на добавление другого пользователя в друзья. " +
            "Другой пока не подтвердил."),
    CONFIRMED("Второй пользователь согласился на добавление");

    /**
     * Описание статуса
     */
    private final String description;

    FriendshipStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
