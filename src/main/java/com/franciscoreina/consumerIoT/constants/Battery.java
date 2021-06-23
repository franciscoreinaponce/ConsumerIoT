package com.franciscoreina.consumerIoT.constants;

public enum Battery {

    BATTERY_FULL("Full"),
    BATTERY_HIGH("High"),
    BATTERY_MEDIUM("Medium"),
    BATTERY_LOW("Low"),
    BATTERY_CRITICAL("Critical");

    private String value;

    Battery(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
