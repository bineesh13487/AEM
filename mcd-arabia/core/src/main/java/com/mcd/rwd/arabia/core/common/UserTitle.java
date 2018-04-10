package com.mcd.rwd.arabia.core.common;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Option;
import com.citytechinc.cq.component.annotations.widgets.MultiField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.mcd.rwd.arabia.core.constants.ArabiaContactUsFormConstant;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Krupa on 06/11/17.
 */

@Model(adaptables = { Resource.class, SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class UserTitle {

    @DialogField(name = "./titleText", fieldLabel = "Title Text",
            fieldDescription = "Text for title field. Default set to <b>Title</b>.")
    @TextField @Named("titleText")
    @Inject @Default(values = ArabiaContactUsFormConstant.TITLE)
    private String titleText;

    @DialogField(name = "./title", fieldLabel = "Title", fieldDescription = "Please provide title by clicking + button." +
            " First provided value will be considered as a Representative Value")
    @MultiField
    @TextField
    @Inject @Named("title")
    private String[] title;

    @DialogField(name = "./titleReq", fieldLabel = "Title Required", defaultValue = ArabiaContactUsFormConstant.REQUIRED_NO_VALUE,
            fieldDescription = "Please select title is required or not. Default set to <b>no</b>")
    @Selection(type = Selection.SELECT,
            options = {
                    @Option(text="No" , value="no"),
                    @Option(text="Yes" , value="yes")
            })
    @Named("titleReq")
    @Inject @Default(values = ArabiaContactUsFormConstant.REQUIRED_NO_VALUE)
    private String titleValidation;

    @DialogField(name = "./titleMessage" , fieldLabel = "Form Action *",
            fieldDescription = "Form action url where user response will be submitted.")
    @TextField @Named("titleMessage")
    @Inject @Default(values = ArabiaContactUsFormConstant.TITLE_VALIDATION_MSG)
    private String titleValidationMessage;

    public String getTitleValidationMessage() {
        return titleValidationMessage;
    }

    public String getTitleValidation() {
        return titleValidation;
    }

    public String[] getTitle() {
        return title;
    }

    public String getTitleText() {
        return titleText;
    }
}
