package com.mcd.rwd.global.core.widget.colorpicker;

import com.citytechinc.cq.component.dialog.exception.InvalidComponentFieldException;
import com.citytechinc.cq.component.touchuidialog.TouchUIDialogElement;
import com.citytechinc.cq.component.touchuidialog.exceptions.TouchUIDialogGenerationException;
import com.citytechinc.cq.component.touchuidialog.widget.maker.AbstractTouchUIWidgetMaker;
import com.citytechinc.cq.component.touchuidialog.widget.maker.TouchUIWidgetMakerParameters;

public class ColorPickerWidgetMaker extends AbstractTouchUIWidgetMaker<ColorPickerWidgetParameters> {

    public ColorPickerWidgetMaker(TouchUIWidgetMakerParameters parameters) {
        super(parameters);
    }

    @Override
    protected TouchUIDialogElement make(ColorPickerWidgetParameters parameters) throws ClassNotFoundException,
            InvalidComponentFieldException, TouchUIDialogGenerationException, IllegalAccessException,
            InstantiationException {

        final ColorPicker annotation = getAnnotation(ColorPicker.class);

        parameters.setVariant(annotation.variant());
        parameters.setAutoGenerateColors(annotation.autoGenerateColors());
        parameters.setShowDefaultColors(annotation.showDefaultColors());
        parameters.setShowSwatchesTab(annotation.showSwatchesTab());
        parameters.setShowColorPropertiesTab(annotation.showColorPropertiesTab());

        return new ColorPickerWidget(parameters);
    }
}
