package com.mcd.rwd.global.core.widget.timefield;

import com.citytechinc.cq.component.touchuidialog.widget.DefaultTouchUIWidgetParameters;
import com.mcd.rwd.global.core.widget.colorpicker.ColorPickerWidget;

public class TimeFieldWidgetParameters extends DefaultTouchUIWidgetParameters {

    private Type type;
    private String displayedFormat;
    private String valueFormat;
    private String minDate;
    private String maxDate;
    private boolean displayTimezoneMessage;
    private String beforeSelector;
    private String afterSelector;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDisplayedFormat() {
        return displayedFormat;
    }

    public void setDisplayedFormat(String displayedFormat) {
        this.displayedFormat = displayedFormat;
    }

    public String getValueFormat() {
        return valueFormat;
    }

    public void setValueFormat(String valueFormat) {
        this.valueFormat = valueFormat;
    }

    public String getMinDate() {
        return minDate;
    }

    public void setMinDate(String minDate) {
        this.minDate = minDate;
    }

    public String getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(String maxDate) {
        this.maxDate = maxDate;
    }

    public boolean isDisplayTimezoneMessage() {
        return displayTimezoneMessage;
    }

    public void setDisplayTimezoneMessage(boolean displayTimezoneMessage) {
        this.displayTimezoneMessage = displayTimezoneMessage;
    }

    public String getBeforeSelector() {
        return beforeSelector;
    }

    public void setBeforeSelector(String beforeSelector) {
        this.beforeSelector = beforeSelector;
    }

    public String getAfterSelector() {
        return afterSelector;
    }

    public void setAfterSelector(String afterSelector) {
        this.afterSelector = afterSelector;
    }

    @Override
    public String getResourceType() {
        return TimeFieldWidget.RESOURCE_TYPE;
    }

    @Override
    public void setResourceType(String resourceType) {
        throw new UnsupportedOperationException("resourceType is Static for TimeFieldWidget");
    }
}
