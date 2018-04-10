package com.mcd.rwd.global.core.sightly;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Option;
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
public class SlideShowGallery {

    @DialogField(fieldLabel = "Title Image")
    @Html5SmartImage(tab = false)
    @Inject
    private String titleImage;

    @DialogField(name = "./titleImageAlign", fieldLabel = "Title Image Alignment",
            fieldDescription = "Choose the horizontal alignment of title image", defaultValue = "Select")
    @Selection(type = Selection.SELECT,
            options = {
                    @Option(text="Select" , value="Select"),
                    @Option(text="Left" , value="left", qtip = "Align the Title Image Left"),
                    @Option(text="Right", value="right", qtip = "Align the Title Image Right"),
                    @Option(text="Center", value="center", qtip = "Align the Title Image Center")
            })
    @Named("titleImageAlign") @Default(values = StringUtils.EMPTY)
    @Inject
    private String titleImageAlignField;

    @DialogField(fieldLabel = "Slide Image")
    @Html5SmartImage(tab = false)
    @Inject
    private String mainImage;

    @DialogField(name = "./mainImageAlign", fieldLabel = "Slide Image Alignment",
            fieldDescription = "Choose the horizontal alignment of Slide image", defaultValue = "Select")
    @Selection(type = Selection.SELECT,
            options = {
                    @Option(text="Select" , value="Select"),
                    @Option(text="Left" , value="left", qtip = "Align the Main slide Image Left"),
                    @Option(text="Right", value="right", qtip = "Align the Main Slide Image Right"),
                    @Option(text="Center", value="center", qtip = "Align Main Slide Image Center")
            })
    @Named("mainImageAlign") @Default(values = StringUtils.EMPTY)
    @Inject
    private String mainImageAlignField;

    @DialogField(fieldLabel = "Background Image")
    @Html5SmartImage(tab = false)
    @Inject
    private String bgImage;

    @DialogField(fieldLabel = "Call To Action Image")
    @Html5SmartImage(tab = false)
    @Inject
    private String ctaImage;

    @DialogField(name = "./ctaImageAlign", fieldLabel = "Call To Action Image Alignment", defaultValue = "Select",
            fieldDescription = "Choose the alignment of CTA image. Default value is Bottom (centered)")
    @Selection(type = Selection.SELECT,
            options = {
                    @Option(text="Select" , value="Select"),
                    @Option(text="Bottom (centered)" , value="bottom-center"),
                    @Option(text="Bottom-Left", value="bottom-left"),
                    @Option(text="Bottom-Right", value="bottom-right")
            })
    @Named("ctaImageAlign") @Default(values = StringUtils.EMPTY)
    @Inject
    private String ctaImageAlignField;

    @DialogField(name = "./ctaPath", fieldDescription = "If CTA Image is not provided, entire slide" +
            " will be clickable.", fieldLabel = "Link")
    @PathField
    @Named("ctaPath")
    @Inject @Default(values = StringUtils.EMPTY)
    private String ctaPathField;

    @DialogField(name = "./description", fieldLabel = "Slide Description", fieldDescription = "Provide slide description")
    @TextArea @Named("description")
    @Inject @Default(values = StringUtils.EMPTY)
    private String descriptionField;

    @DialogField(fieldLabel = "Hide description for Mobile?", name = "./hideDesc", value = "on")
    @CheckBox(text = "Hide description for Mobile?")
    @Inject @Named("./hideDesc") @Default(values = "false")
    String hideDescField;

    @Inject
    Resource resource;

    public String getHideDescField() {
        return hideDescField;
    }

    public String getTitleImage() {
        return titleImage;
    }

    public String getTitleImageAlignField() {
        return titleImageAlignField;
    }

    public String getMainImage() {
        return mainImage;
    }

    public String getMainImageAlignField() {
        return mainImageAlignField;
    }

    public String getBgImage() {
        return bgImage;
    }

    public String getCtaImage() {
        return ctaImage;
    }

    public String getCtaImageAlignField() {
        return ctaImageAlignField;
    }

    public String getCtaPathField() {
        return ctaPathField;
    }

    public String getDescriptionField() {
        return descriptionField;
    }

    @PostConstruct
    public void init(){
        if(null!=resource){
            ValueMap titleImg = resource.getChild("titleImage").adaptTo(ValueMap.class);
            this.titleImage = titleImg.get("fileReference", String.class);
            ValueMap mainImg = resource.getChild("mainImage").adaptTo(ValueMap.class);
            this.mainImage = mainImg.get("fileReference", String.class);
            ValueMap bgImg = resource.getChild("bgImage").adaptTo(ValueMap.class);
            this.bgImage = bgImg.get("fileReference", String.class);
            ValueMap ctaImg = resource.getChild("ctaImage").adaptTo(ValueMap.class);
            this.ctaImage = ctaImg.get("fileReference", String.class);
        }
    }

}
