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

public interface PublicationPaddingItems {

    @DialogField( fieldDescription="Enter top padding. Default value 0px",
            fieldLabel="Top Padding", ranking = 1D)
    @NumberField
    Double getPaddingTop();

    @DialogField(fieldDescription="Enter bottom padding. Default value 0px",
            fieldLabel="Bottom Padding", ranking = 2D)
    @NumberField
    Double getPaddingBottom();

    @DialogField( fieldDescription="Enter left padding. Default value 0px",
            fieldLabel="Left Padding", ranking = 3D)
    @NumberField
    Double getPaddingLeft();


    @DialogField( fieldDescription="Enter right padding. Default value 0px",
            fieldLabel="Right Padding", ranking = 4D)
    @NumberField
    Double getPaddingRight();
}
