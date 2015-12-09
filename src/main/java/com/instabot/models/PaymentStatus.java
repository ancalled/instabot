package com.instabot.models;

public enum PaymentStatus {
    CREATED,
    AUTHORIZED,
    FUNDED,
    INTRANSIT,
    SETTLED,
    REFUNDED,
    PARTIALLY_REFUNDED,
    CANCELED;
}
