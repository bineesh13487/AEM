package com.mcd.rwd.global.core.widget.colorpicker;

import com.citytechinc.cq.component.annotations.config.TouchUIWidget;
import com.citytechinc.cq.component.touchuidialog.widget.AbstractTouchUIWidget;

@TouchUIWidget(annotationClass = ColorPicker.class, makerClass = ColorPickerWidgetMaker.class,
        resourceType = ColorPickerWidget.RESOURCE_TYPE)
public class ColorPickerWidget extends AbstractTouchUIWidget {

    public static final String RESOURCE_TYPE = "mcd-rwd-global/components/dialog/colorpicker";

    private final Variant variant;
    private final AutoGenerateColors autoGenerateColors;
    private final boolean showDefaultColors;
    private final boolean showSwatchesTab;
    private final boolean showColorPropertiesTab;

    public ColorPickerWidget(ColorPickerWidgetParameters parameters) {
        super(parameters);
        variant = parameters.getVariant();
        autoGenerateColors = parameters.getAutoGenerateColors();
        showDefaultColors = parameters.isShowDefaultColors();
        showSwatchesTab = parameters.isShowSwatchesTab();
        showColorPropertiesTab = parameters.isShowColorPropertiesTab();
    }

    public Variant getVariant() {
        return variant;
    }

    public AutoGenerateColors getAutoGenerateColors() {
        return autoGenerateColors;
    }

    public boolean isShowDefaultColors() {
        return showDefaultColors;
    }

    public boolean isShowSwatchesTab() {
        return showSwatchesTab;
    }

    public boolean isShowColorPropertiesTab() {
        return showColorPropertiesTab;
    }
}
