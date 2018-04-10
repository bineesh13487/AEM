package com.mcd.rwd.global.core.sightly;

import com.citytechinc.cq.component.annotations.DialogField;
import com.mcd.rwd.global.core.widget.colorpicker.ColorPicker;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Krupa on 25/07/17.
 */

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TextDecoration {

    @DialogField(name = "./textColor", fieldLabel = "Text Color",
            fieldDescription = "Enter the Title and Description text color only if applicable.")
    @ColorPicker
    @Inject @Named("textColor")
    @Default(values = StringUtils.EMPTY)
    private String textColor;

    @DialogField(name = "./bgColor", fieldLabel = "Text Background Color",
            fieldDescription = "Enter the background color for Title and Description text only if applicable.")
    @ColorPicker
    @Inject @Named("bgColor")
    @Default(values = StringUtils.EMPTY)
    private String bgColor;

    public String getTextColor() {
        return textColor;
    }

    public String getBgColor() {
        return bgColor;
    }
}
