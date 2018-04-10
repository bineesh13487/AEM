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
        value = "Middle Name", name = "mname",
        path = "/content/arabiacontactus",
        disableTargeting = true,
        editConfig = false,
        group = ComponentConstants.GROUP_HIDDEN)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class MiddleName {

    @DialogField(name = "./middleName", fieldLabel = "Middle Name Text", fieldDescription = "Text for Middle Name field." +
            " Default set to <b>Middle Name</b>.")
    @TextField
    @Inject
    @Named("middleName") @Default(values = ArabiaContactUsFormConstant.MIDDLE_NAME)
    private String middleNameText;

    @DialogField(name = "./middleNameReq", fieldLabel = "Middle Name Required",
            fieldDescription = "Please select Middle Name is required or not. Default set to <b>No</b>")
    @Selection(type = Selection.SELECT,
            options = {
                    @Option(text="No" , value="no"),
                    @Option(text="Yes" , value="yes")
            })
    @Named("middleNameReq")
    @Inject @Default(values = ArabiaContactUsFormConstant.REQUIRED_NO_VALUE)
    private String middleNameValidation;

    @DialogField(name = "./middleNameMessage" , fieldLabel = "Middle Name Validation Message",
            fieldDescription = "Validation message for Middle Name field. Default set to <b>Please enter Middle Name. </b>")
    @TextField @Named("middleNameMessage")
    @Inject @Default(values = ArabiaContactUsFormConstant.MNANE_VALIDATION_MSG)
    private String middleNameValidationMessage;

    public String getMiddleNameText() {
        return middleNameText;
    }

    public String getMiddleNameValidation() {
        return middleNameValidation;
    }

    public String getMiddleNameValidationMessage() {
        return middleNameValidationMessage;
    }
}
