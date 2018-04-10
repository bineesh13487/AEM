package com.mcd.rwd.wifi.core.sightly;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Option;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.mcd.rwd.global.core.widget.colorpicker.ColorPicker;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Krupa on 18/07/17.
 */

@Model(adaptables = { Resource.class, SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class JoinUsTile {

    @DialogField(
            fieldLabel = "Tile Text",
            name = "./defaultTitle",
            fieldDescription = "Enter text for the tile",
            additionalProperties = @Property(name = "key", value = "defaultTitle")
    )
    @TextField
    @Inject @Named("defaultTitle")
    @Default(values = StringUtils.EMPTY)
    private String imagetitle;

    @DialogField(name = "./defaultColorpicker", fieldLabel = "Text Color",
        fieldDescription = "Choose the color code for title. Note: Default colour is '#FFFFFF; (White).",
        additionalProperties = {@Property(name = "key", value = "defaultColorpicker")})
    @ColorPicker
    @Inject @Named("defaultColorpicker")
    @Default(values = StringUtils.EMPTY)
    private String colorpicker;

    @DialogField(name = "./defaultTextAlign",
            fieldLabel = "Text Align",
            fieldDescription = "Choose an option for text alignment of title. Note : Default alignment is 'Top'",
            additionalProperties = {@Property(name = "key", value = "defaultTextAlign")})
    @Selection(type = Selection.SELECT,
            options = {
                    @Option(text="Top" , value="top"),
                    @Option(text="Top Left" , value="topLeft"),
                    @Option(text="Top Right", value="topRight"),
                    @Option(text="Middle", value="middle"),
                    @Option(text="Middle Left", value="middleLeft"),
                    @Option(text="Middle Right", value="middleRight"),
                    @Option(text="Bottom", value="bottom"),
                    @Option(text="Bottom Left", value="bottomLeft"),
                    @Option(text="Bottom Right", value="bottomRight"),
            })
    @Inject @Named("defaultTextAlign")
    @Default(values = StringUtils.EMPTY)
    private String textalign;

    @DialogField(
            fieldLabel = "Image Path",
            name = "./defaultImagePath", required = true,
            fieldDescription = "Select the image path from DAM",
            additionalProperties = {@Property(name = "key", value = "defaultImagePath"),
                @Property(name = "regex", value = "new RegExp('content/(.*)?(?:jpe?g|gif|png)$')"),
                @Property(name = "regexText", value = "Please provide an image with a valid extension i.e : jpeg|gif|png")}
    )
    @PathField(showTitleInTree = true, rootPath = "/content/dam", rootTitle = "Image")
    @Inject @Named("defaultImagePath")
    @Default(values = StringUtils.EMPTY)
    private String imagepath;

    @DialogField(
            fieldLabel = "Image Alt Text",
            name = "./defaultAltText", required = true,
            fieldDescription = "Enter image alt text",
            additionalProperties = @Property(name = "key", value = "defaultAltText")
    )
    @TextField
    @Inject @Named("defaultAltText")
    @Default(values = StringUtils.EMPTY)
    private String imagealttext;

    @DialogField(
            fieldLabel = "Default URL",
            name = "./defaultURL",
            fieldDescription = "Enter default link for redirect if no user location is shared.",
            additionalProperties = @Property(name = "key", value = "defaultURL")
    )
    @TextField
    @Inject @Named("defaultURL")
    @Default(values = StringUtils.EMPTY)
    private String defaulturl;

    @DialogField(name = "./defaultTarget",
            fieldLabel = "Target",
            fieldDescription = "Choose a target to open image link. Note: Default target is 'Modal'.",
            additionalProperties = {@Property(name = "key", value = "defaultTarget")})
    @Selection(type = Selection.SELECT,
            options = {
                    @Option(text="Modal" , value="modal"),
                    @Option(text="New Window" , value="new"),
                    @Option(text="Same Window", value="same")
            })
    @Inject @Named("defaultTarget")
    @Default(values = StringUtils.EMPTY)
    private String target;

    public String getImagetitle() {
        return imagetitle;
    }

    public String getTextalign() {
        return textalign;
    }

    public String getImagepath() {
        return imagepath;
    }

    public String getImagealttext() {
        return imagealttext;
    }

    public String getDefaulturl() {
        return defaulturl;
    }

    public String getTarget() {
        return target;
    }

    public String getColorpicker() {
        return colorpicker;
    }
}
