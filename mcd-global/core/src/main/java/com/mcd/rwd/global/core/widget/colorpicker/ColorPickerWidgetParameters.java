package com.mcd.rwd.global.core.widget.colorpicker;

import com.citytechinc.cq.component.touchuidialog.widget.DefaultTouchUIWidgetParameters;

public class ColorPickerWidgetParameters extends DefaultTouchUIWidgetParameters {

    private Variant variant;
    private AutoGenerateColors autoGenerateColors;
    private boolean showDefaultColors;
    private boolean showSwatchesTab;
    private boolean showColorPropertiesTab;

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }

    public AutoGenerateColors getAutoGenerateColors() {
        return autoGenerateColors;
    }

    public void setAutoGenerateColors(AutoGenerateColors autoGenerateColors) {
        this.autoGenerateColors = autoGenerateColors;
    }

    public boolean isShowDefaultColors() {
        return showDefaultColors;
    }

    public void setShowDefaultColors(boolean showDefaultColors) {
        this.showDefaultColors = showDefaultColors;
    }

    public boolean isShowSwatchesTab() {
        return showSwatchesTab;
    }

    public void setShowSwatchesTab(boolean showSwatchesTab) {
        this.showSwatchesTab = showSwatchesTab;
    }

    public boolean isShowColorPropertiesTab() {
        return showColorPropertiesTab;
    }

    public void setShowColorPropertiesTab(boolean showColorPropertiesTab) {
        this.showColorPropertiesTab = showColorPropertiesTab;
    }

    @Override
    public String getResourceType() {
        return ColorPickerWidget.RESOURCE_TYPE;
    }

    @Override
    public void setResourceType(String resourceType) {
        throw new UnsupportedOperationException("resourceType is Static for ColorPickerWidget");
    }

}
