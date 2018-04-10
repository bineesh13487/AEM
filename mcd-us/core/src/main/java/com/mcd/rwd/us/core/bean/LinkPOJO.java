package com.mcd.rwd.us.core.bean;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.widgets.CheckBox;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

@Model(adaptables = { Resource.class ,SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public interface LinkPOJO {

    @DialogField(fieldLabel = "Form Text", fieldDescription = "Enter form text", ranking = 1D)
    @TextField
    @Inject
    String getLinkText();

    @DialogField(fieldLabel = "Form URL", fieldDescription = "Enter form url", ranking = 2D)
    @PathField
    @Inject
    String getLinkUrl();

    @DialogField(fieldDescription = "Check the box to open in a new window", ranking = 3D, value = "true")
    @CheckBox(text = "Open in a New Window")
    @Inject
    String getTargetVal();


}
