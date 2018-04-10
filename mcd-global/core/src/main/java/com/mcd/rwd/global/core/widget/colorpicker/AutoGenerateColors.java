package com.mcd.rwd.global.core.widget.colorpicker;

public enum AutoGenerateColors {
    OFF,
    SHADES,
    TINTS;

    public String getValue() {
        return name().toLowerCase();
    }
}
