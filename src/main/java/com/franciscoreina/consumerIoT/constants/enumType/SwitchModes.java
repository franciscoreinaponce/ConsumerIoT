package com.franciscoreina.consumerIoT.constants.enumType;

public enum SwitchModes {

    ON("ON"),
    OFF("OFF"),
    NA("N/A");

    private final String value;

    SwitchModes(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
