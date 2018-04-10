package com.mcd.rwd.global.core.widget.colorpicker;

public enum Variant {
    DEFAULT,
    SWATCH;

    public String getValue() {
        return name().toLowerCase();
    }
}
