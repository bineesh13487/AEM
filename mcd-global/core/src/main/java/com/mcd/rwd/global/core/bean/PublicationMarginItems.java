package com.mcd.rwd.global.core.bean;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.widgets.NumberField;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;


/**
 * Created by apple on 18/07/17.
 */

@Model(adaptables = { Resource.class ,SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public interface PublicationMarginItems {

    @DialogField(  fieldDescription="Enter top margin. Default value 0px",
            fieldLabel="Top Margin", ranking = 1D)
    @NumberField
    Double getMarginTop();

    @DialogField(fieldDescription="Enter bottom margin. Default value 0px",
            fieldLabel="Bottom Margin", ranking = 2D)
    @NumberField
    Double getMarginBottom();

    @DialogField( fieldDescription="Enter left margin. Default value 0px",
            fieldLabel="Left Margin", ranking = 3D)
    @NumberField
    Double getMarginLeft();


    @DialogField(fieldDescription="Enter right margin. Default value 0px",
            fieldLabel="Right Margin", ranking = 4D)
    @NumberField
    Double getMarginRight();

}
