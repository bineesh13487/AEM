package com.mcd.rwd.global.core.widget.colorpicker;

public @interface ColorPicker {

    Variant variant() default Variant.DEFAULT;

    AutoGenerateColors autoGenerateColors() default AutoGenerateColors.OFF;

    boolean showDefaultColors() default true;

    boolean showSwatchesTab() default true;

    boolean showColorPropertiesTab() default true;

}