package com.novruz.account.enums;

public enum CurrencyType {
    EUR("EUR"),
    SEK("SEK"),
    GBP("GBP"),
    USD("USD");

    private final String value;

    CurrencyType(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
