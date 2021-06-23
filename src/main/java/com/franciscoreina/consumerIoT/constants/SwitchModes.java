package com.franciscoreina.consumerIoT.constants;

public enum SwitchModes {

    ON("ON"),
    OFF("OFF"),
    NA("N/A");

    private String value;

    SwitchModes(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
