package com.mcd.rwd.global.core.components.common;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.widgets.CheckBox;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Created by Krupa on 13/06/17.
 */
@Component(value = "Button", group = "GWS-Global", disableTargeting = true, path="/common")
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Button {

    @DialogField(fieldLabel = "Button Text", fieldDescription = "Enter button text", ranking = 1D)
    @TextField
    @Inject
    String buttonText;

    @DialogField(fieldLabel = "Button Url", fieldDescription = "Enter button Url", ranking = 2D)
    @PathField
    @Inject
    String buttonUrl;

    @DialogField(fieldDescription = "Check the box to open in a new tab", ranking = 3D, value = "on")
    @CheckBox(text = "Target")
    @Inject
    String targetData;

    public String getTargetText() {
        return targetText;
    }

    private String targetText;

    public String getButtonUrl() {
        return buttonUrl;
    }

    public String getTargetData() {
        return targetData;
    }

    public String getButtonText() {
        return buttonText;
    }

    @PostConstruct
    private void buttonConstruct(){
        if(StringUtils.isNotEmpty(targetData) && targetData.equals("on")){
            this.targetText="_blank";
        }else{
            this.targetText="_self";
        }
    }

}
