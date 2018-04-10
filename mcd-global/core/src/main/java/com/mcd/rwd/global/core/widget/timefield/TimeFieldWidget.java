package com.mcd.rwd.global.core.widget.timefield;

import com.citytechinc.cq.component.annotations.config.TouchUIWidget;
import com.citytechinc.cq.component.touchuidialog.widget.AbstractTouchUIWidget;

@TouchUIWidget(annotationClass = TimeField.class, makerClass = TimeFieldWidgetMaker.class,
        resourceType = TimeFieldWidget.RESOURCE_TYPE)
public class TimeFieldWidget extends AbstractTouchUIWidget {

    public static final String RESOURCE_TYPE = "mcd-rwd-global/components/dialog/timepicker";

    private final String type;
    private final String displayedFormat;
    private final String valueFormat;
    private final String minDate;
    private final String maxDate;
    private final boolean displayTimezoneMessage;
    private final String beforeSelector;
    private final String afterSelector;

    public TimeFieldWidget(TimeFieldWidgetParameters parameters) {
        super(parameters);
        type = parameters.getType().getValue();
        displayedFormat = parameters.getDisplayedFormat();
        valueFormat = parameters.getValueFormat();
        minDate = parameters.getMinDate();
        maxDate = parameters.getMaxDate();
        displayTimezoneMessage = parameters.isDisplayTimezoneMessage();
        beforeSelector = parameters.getBeforeSelector();
        afterSelector = parameters.getAfterSelector();
    }

    public String getType() {
        return type;
    }

    public String getDisplayedFormat() {
        return displayedFormat;
    }

    public String getValueFormat() {
        return valueFormat;
    }

    public String getMinDate() {
        return minDate;
    }

    public String getMaxDate() {
        return maxDate;
    }

    public boolean isDisplayTimezoneMessage() {
        return displayTimezoneMessage;
    }

    public String getBeforeSelector() {
        return beforeSelector;
    }

    public String getAfterSelector() {
        return afterSelector;
    }
}
