package com.demo.ecommerce.data.enums;

public enum DataSourceType {
    MASTER("master"),
    SLAVE("slave");

    private final String type;

    DataSourceType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}