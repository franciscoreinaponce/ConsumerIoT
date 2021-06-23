package com.franciscoreina.consumerIoT.constants;

public enum StatusDevice {

    STATUS_ACTIVE("Active"),
    STATUS_INACTIVE("Inactive"),
    STATUS_NA("N/A");

    private String value;

    StatusDevice(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
