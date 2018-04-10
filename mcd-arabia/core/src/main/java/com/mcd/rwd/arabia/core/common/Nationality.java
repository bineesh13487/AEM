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
        value = "Nationality", name = "cnationality",
        path = "/content/arabiacontactus",
        disableTargeting = true,
        editConfig = false,
        group = ComponentConstants.GROUP_HIDDEN)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class Nationality {

    @DialogField(name = "./nationalityText", fieldLabel = "Nationality Text",
            fieldDescription = "Text for Nationality field. Default set to <b>Nationality</b>.")
    @TextField
    @Named("nationalityText")
    @Inject
    @Default(values = ArabiaContactUsFormConstant.NATIONALITY)
    private String nationalityText;

    @DialogField(name = "./nationality", fieldLabel = "Nationality", fieldDescription = "Please provide Nationality by " +
            "clicking + button. First provided value will be considered as a Representative Value")
    @MultiField
    @TextField
    @Inject @Named("nationality")
    private String[] nationality;

    @DialogField(name = "./nationalityReq", fieldLabel = "Nationality Required",
            fieldDescription = "Please select Nationality is required or not. Default set to <b>No</b>")
    @Selection(type = Selection.SELECT,
            options = {
                    @Option(text="No" , value="no"),
                    @Option(text="Yes" , value="yes")
            })
    @Named("nationalityReq")
    @Inject @Default(values = ArabiaContactUsFormConstant.REQUIRED_NO_VALUE)
    private String nationalityValidation;

    @DialogField(name = "./nationalityMessage" , fieldLabel = "Nationality Validation Message",
            fieldDescription = "Validation message for Nationality field. Default " +
                    "set to <b>Please select Nationality</b>")
    @TextField @Named("nationalityMessage")
    @Inject @Default(values = ArabiaContactUsFormConstant.NATIONALITY_VALIDATION_MSG)
    private String nationalityValidationMessage;

    public String[] getNationality() {
        return nationality;
    }

    public String getNationalityText() {
        return nationalityText;
    }

    public String getNationalityValidation() {
        return nationalityValidation;
    }

    public String getNationalityValidationMessage() {
        return nationalityValidationMessage;
    }
}
