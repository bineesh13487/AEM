package com.mcd.rwd.arabia.core.common;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Option;
import com.citytechinc.cq.component.annotations.widgets.MultiField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.icfolson.aem.library.core.constants.ComponentConstants;
import com.mcd.rwd.arabia.core.constants.ArabiaContactUsFormConstant;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Krupa on 06/11/17.
 */
@Component(
        value = "Gender", name = "cgender",
        path = "/content/arabiacontactus",
        disableTargeting = true,
        editConfig = false,
        group = ComponentConstants.GROUP_HIDDEN)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class Gender {

    @DialogField(name = "./genderText", fieldLabel = "Gender Text",
            fieldDescription = "Text for Gender field. Default set to <b>Gender</b>.")
    @TextField
    @Named("genderText")
    @Inject
    @Default(values = ArabiaContactUsFormConstant.GENDER)
    private String genderText;

    @DialogField(name = "./gender", fieldLabel = "Gender", fieldDescription = "Please provide Gender by " +
            "clicking + button. First provided value will be considered as a Representative Value")
    @MultiField
    @TextField
    @Inject @Named("gender")
    private String[] gender;

    @DialogField(name = "./genderReq", fieldLabel = "Gender Required",
            fieldDescription = "Please select Gender is required or not. Default set to <b>No</b>")
    @Selection(type = Selection.SELECT,
            options = {
                    @Option(text="No" , value="no"),
                    @Option(text="Yes" , value="yes")
            })
    @Named("genderReq")
    @Inject @Default(values = ArabiaContactUsFormConstant.REQUIRED_NO_VALUE)
    private String genderValidation;

    @DialogField(name = "./genderMessage" , fieldLabel = "Gender Validation Message",
            fieldDescription = "Validation message for Gender field. Default " +
                    "set to <b>Please select Gender</b>")
    @TextField @Named("genderMessage")
    @Inject @Default(values = ArabiaContactUsFormConstant.GENDER_VALIDATION_MSG)
    private String genderValidationMessage;

    public String getGenderText() {
        return genderText;
    }

    public String[] getGender() {
        return gender;
    }

    public String getGenderValidation() {
        return genderValidation;
    }

    public String getGenderValidationMessage() {
        return genderValidationMessage;
    }
}
