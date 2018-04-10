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
 * Created by Krupa on 12/06/17.
 */

@Component(value = "Link", group = "GWS-Global", disableTargeting = true, path="/common")

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Link {

    @DialogField(fieldLabel = "Link Text", fieldDescription = "Enter link text", ranking = 1D)
    @TextField
    @Inject
    String linkText;

    @DialogField(fieldLabel = "Link Url", fieldDescription = "Enter link Url", ranking = 2D)
    @PathField
    @Inject
    String linkUrl;

    @DialogField(fieldDescription = "Check the box to open in a new tab", ranking = 3D, value = "on")
    @CheckBox(text = "Target")
    @Inject
    String targetVal;

    private String targetText;

    @PostConstruct
    private void linkConstruct(){
        if(StringUtils.isNotEmpty(targetVal) && targetVal.equals("on")){
            this.targetText="_blank";
        }else{
            this.targetText="_self";
        }
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public String getTargetVal() {
        return targetVal;
    }

    public String getLinkText() {
        return linkText;
    }

    public String getTargetText() {
        return targetText;
    }
}
