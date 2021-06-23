package com.franciscoreina.consumerIoT.constants;

public enum ProductNames {

    CYCLE_PLUS_TRACKER("CyclePlusTracker"),
    GENERAL_TRACKER("GeneralTracker"),
    UNKNOWN_TRACKER("Unknown");

    private String value;

    ProductNames(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
