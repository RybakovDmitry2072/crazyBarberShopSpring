package org.example.springcrazybarbershop.models;

public enum AppointmentStatus {
    BOOKED,          // Запись создана (ожидает подтверждения или визита)
    CONFIRMED,       // Подтверждена (клиент подтвердил запись)
    ARRIVED,         // Клиент пришел в барбершоп
    IN_PROGRESS,     // Услуга выполняется (например, стрижка началась)
    COMPLETED,       // Услуга успешно завершена
    CANCELLED,       // Отменена (клиент или барбер отменили запись)
    NO_SHOW,         // Клиент не пришел (без предупреждения)
    RESCHEDULED,     // Перенесена на другое время
    PAYMENT_PENDING, // Ожидает оплаты
    PAID,            // Оплачено
    REFUNDED         // Деньги возвращены (если была отмена)
}
