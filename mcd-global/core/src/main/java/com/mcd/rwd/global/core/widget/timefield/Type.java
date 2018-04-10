package com.mcd.rwd.global.core.widget.timefield;

public enum Type {
    DATE,
    TIME,
    DATETIME;

    public String getValue() {
        return name().toLowerCase();
    }
}
