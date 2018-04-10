package com.mcd.rwd.arabia.core.common;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Option;
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
        value = "Mobile Phone Number", name = "cnumber",
        path = "/content/arabiacontactus",
        disableTargeting = true,
        editConfig = false,
        group = ComponentConstants.GROUP_HIDDEN)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class PhoneNumber {

    @DialogField(name = "./number", fieldLabel = "Number Text",
            fieldDescription = "Text for Mobile Number field. Default set to <b>Number</b>.")
    @TextField
    @Named("number")
    @Inject
    @Default(values = ArabiaContactUsFormConstant.NUMBER)
    private String numberText;

    @DialogField(name = "./numberReq", fieldLabel = "Number Required",
            fieldDescription = "Please select Number is required or not. Default set to <b>No</b>")
    @Selection(type = Selection.SELECT,
            options = {
                    @Option(text="No" , value="no"),
                    @Option(text="Yes" , value="yes")
            })
    @Named("numberReq")
    @Inject @Default(values = ArabiaContactUsFormConstant.REQUIRED_NO_VALUE)
    private String numberValidation;

    @DialogField(name = "./numberMessage" , fieldLabel = "Number Validation Message",
            fieldDescription = "Validation message for Number field. Default set to <b>Please enter Number </b>")
    @TextField @Named("numberMessage")
    @Inject @Default(values = ArabiaContactUsFormConstant.NUMBER_VALIDATION_MSG)
    private String numberValidationMessage;

    public String getNumberText() {
        return numberText;
    }

    public String getNumberValidation() {
        return numberValidation;
    }

    public String getNumberValidationMessage() {
        return numberValidationMessage;
    }
}
