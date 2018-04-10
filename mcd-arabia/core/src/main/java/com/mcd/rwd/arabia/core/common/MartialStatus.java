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
        value = "Marital Status", name = "cmarstatus",
        path = "/content/arabiacontactus",
        disableTargeting = true,
        editConfig = false,
        group = ComponentConstants.GROUP_HIDDEN)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class MartialStatus {

    @DialogField(name = "./maritalText", fieldLabel = "MaritalStatus Text",
            fieldDescription = "Text for Marital Status field. Default set to <b>Marital Status</b>.")
    @TextField
    @Named("maritalText")
    @Inject
    @Default(values = ArabiaContactUsFormConstant.MARITAL_STATUS)
    private String maritalStatusText;

    @DialogField(name = "./maritalStat", fieldLabel = "Marital Status", fieldDescription = "Please provide Marital " +
            "Status by clicking + button. First provided value will be considered as a Representative Value")
    @MultiField
    @TextField
    @Inject @Named("maritalStat")
    private String[] maritalStatus;

    @DialogField(name = "./maritalstatusReq", fieldLabel = "Marital Status Required",
            fieldDescription = "Please select Marital Status is required or not. Default set to <b>No</b>")
    @Selection(type = Selection.SELECT,
            options = {
                    @Option(text="No" , value="no"),
                    @Option(text="Yes" , value="yes")
            })
    @Named("maritalstatusReq")
    @Inject @Default(values = ArabiaContactUsFormConstant.REQUIRED_NO_VALUE)
    private String maritalStatusValidation;

    @DialogField(name = "./maritalMessage" , fieldLabel = "Marital Status Validation Message",
            fieldDescription = "Validation message for Marital Status field." +
                    " Default set to <b>Please select Marital Status</b>")
    @TextField @Named("maritalMessage")
    @Inject @Default(values = ArabiaContactUsFormConstant.MARITAL_VALIDATION_MSG)
    private String maritalValidationMessage;

    public String getMaritalStatusText() {
        return maritalStatusText;
    }

    public String[] getMaritalStatus() {
        return maritalStatus;
    }

    public String getMaritalStatusValidation() {
        return maritalStatusValidation;
    }

    public String getMaritalValidationMessage() {
        return maritalValidationMessage;
    }
}
