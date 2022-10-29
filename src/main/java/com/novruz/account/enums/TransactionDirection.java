package com.novruz.account.enums;

public enum TransactionDirection {
    IN("IN"),
    OUT("OUT");

    private final String value;

    TransactionDirection(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
