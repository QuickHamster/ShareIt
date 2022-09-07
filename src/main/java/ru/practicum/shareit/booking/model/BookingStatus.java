package ru.practicum.shareit.booking.model;

public enum BookingStatus {
    WAITING("WAITING"),    // новое бронирование, ожидает одобрения
    APPROVED("APPROVED"),  // бронирование подтверждено владельцем
    REJECTED("REJECTED"),  // бронирование отклонено владельцем
    CANCELED("CANCELED");  // бронирование отменено создателем
    private final String title;

    BookingStatus(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
