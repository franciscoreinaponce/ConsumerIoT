package com.franciscoreina.consumerIoT.constants;

public enum ProductId {

    CPT_START_WITH("WG"),
    GT_START_WITH("69");

    private String value;

    ProductId(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
