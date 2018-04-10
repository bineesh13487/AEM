package com.mcd.rwd.global.core.sightly;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.widgets.*;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Krupa on 26/07/17.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ShowcaseGallery {

    @DialogField(fieldLabel = "Showcase Image*")
    @Html5SmartImage(tab = false)
    @Inject
    private String mainImage;

    @DialogField(name = "./title", fieldDescription = "Provide title text for showcase image.",
            fieldLabel = "Showcase Title")
    @TextField
    @Named("title")
    @Inject @Default(values = StringUtils.EMPTY)
    private String titleField;

    @DialogField(name = "./description", fieldLabel = "Showcase Description", fieldDescription = "Provide the description text")
    @TextArea
    @Named("description")
    @Inject @Default(values = StringUtils.EMPTY)
    private String showcaseDescField;

    @DialogField(name = "./ctaPath", fieldDescription = "eg: /content, http://w, https://w", fieldLabel = "Showcase Link")
    @PathField
    @Named("ctaPath")
    @Inject @Default(values = StringUtils.EMPTY)
    private String ctaPathField;

    @DialogField(fieldLabel = "Hide description for Mobile?", name = "./hideDesc", value = "on")
    @CheckBox(text = "Hide description for Mobile?")
    @Inject @Named("./hideDesc") @Default(values = "false")
    String hideDescField;

    @Inject
    Resource resource;

    @PostConstruct
    public void init(){
        if(null!=resource){
            ValueMap mainImg = resource.getChild("mainImage").adaptTo(ValueMap.class);
            this.mainImage = mainImg.get("fileReference", String.class);
        }
    }

    public String getMainImage() {
        return mainImage;
    }

    public String getTitleField() {
        return titleField;
    }

    public String getShowcaseDescField() {
        return showcaseDescField;
    }

    public String getCtaPathField() {
        return ctaPathField;
    }

    public String getHideDescField() {
        return hideDescField;
    }
}
