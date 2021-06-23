package com.franciscoreina.consumerIoT.constants.enumType;

public enum ProductNames {

    CYCLE_PLUS_TRACKER("CyclePlusTracker"),
    GENERAL_TRACKER("GeneralTracker"),
    UNKNOWN_TRACKER("Unknown");

    private final String value;

    ProductNames(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
