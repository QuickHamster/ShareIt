package ru.practicum.shareit.booking.model;

public enum BookingState {
    ALL("ALL"),             // все
    CURRENT("CURRENT"),     // текущие
    PAST("PAST"),           // завершённые
    FUTURE("FUTURE"),       // будущие
    WAITING("WAITING"),     // ожидающие подтверждения
    REJECTED("REJECTED");   // отклонённые
    private final String title;

    BookingState(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
