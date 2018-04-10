package com.mcd.rwd.global.core.widget.timefield;

import com.citytechinc.cq.component.dialog.exception.InvalidComponentFieldException;
import com.citytechinc.cq.component.touchuidialog.TouchUIDialogElement;
import com.citytechinc.cq.component.touchuidialog.exceptions.TouchUIDialogGenerationException;
import com.citytechinc.cq.component.touchuidialog.widget.maker.AbstractTouchUIWidgetMaker;
import com.citytechinc.cq.component.touchuidialog.widget.maker.TouchUIWidgetMakerParameters;

public class TimeFieldWidgetMaker extends AbstractTouchUIWidgetMaker<TimeFieldWidgetParameters> {

    public TimeFieldWidgetMaker(TouchUIWidgetMakerParameters parameters) {
        super(parameters);
    }

    @Override
    protected TouchUIDialogElement make(TimeFieldWidgetParameters parameters) throws ClassNotFoundException,
            InvalidComponentFieldException, TouchUIDialogGenerationException, IllegalAccessException,
            InstantiationException {

        final TimeField annotation = getAnnotation(TimeField.class);

        parameters.setType(annotation.type());
        parameters.setDisplayedFormat(annotation.displayedFormat());
        parameters.setValueFormat(annotation.valueFormat());
        parameters.setMinDate(annotation.minDate());
        parameters.setMaxDate(annotation.maxDate());
        parameters.setDisplayTimezoneMessage(annotation.displayTimezoneMessage());
        parameters.setBeforeSelector(annotation.beforeSelector());
        parameters.setAfterSelector(annotation.afterSelector());

        return new TimeFieldWidget(parameters);
    }
}
