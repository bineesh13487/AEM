package com.mcd.rwd.global.core.bean;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Option;
import com.citytechinc.cq.component.annotations.widgets.NumberField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.mcd.rwd.global.core.widget.colorpicker.ColorPicker;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

/**
 * Created by apple on 18/07/17.
 */
@Model(adaptables = { Resource.class ,SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public interface PublicationBorderItems {
    @DialogField(  fieldLabel="Paragraph Title", ranking = 1D)
    @TextField
    @Inject
    String getParagraphTitle();

    @DialogField(value = "left", fieldDescription="Choose how the border title should be horizontally aligned",
            fieldLabel="Border Title Alignment", ranking = 2D)
    @Selection(type = Selection.RADIO,
            options = {
                    @Option(text="Left" , value="left"),
                    @Option(text="Center" , value="center"),
                    @Option(text="Right" , value="right")
            })
    String getBorderTitleAlign();

    @DialogField( fieldDescription="Choose the border title color",fieldLabel="Border Title Color",
        ranking = 3D)
    @ColorPicker
    String getBorderTitleColor();

    @DialogField(  fieldDescription="Choose the border color",fieldLabel="Border Color", ranking = 4D)
    @ColorPicker
    String getBorderColor();

    @DialogField( fieldDescription="Enter the border size. Default border size is '0'",
            fieldLabel="Border Size", ranking = 5D)
    @NumberField
    String getBorderSize();
}
