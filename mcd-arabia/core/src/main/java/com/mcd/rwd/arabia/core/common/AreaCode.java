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
        value = "Mobile Phone Service  Provider Code", name = "careacode",
        path = "/content/arabiacontactus",
        disableTargeting = true,
        editConfig = false,
        group = ComponentConstants.GROUP_HIDDEN)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class AreaCode {

    @DialogField(name = "./areaCode", fieldLabel = "Area Code Text", fieldDescription = "Text for Area Code field." +
            " Default set to <b>Area Code</b>.")
    @TextField
    @Inject
    @Named("areaCode") @Default(values = ArabiaContactUsFormConstant.AREA_CODE)
    private String areacodeText;

    @DialogField(name = "./areaCodeReq", fieldLabel = "Area Code Required",
            defaultValue = ArabiaContactUsFormConstant.REQUIRED_NO_VALUE,
            fieldDescription = "Please select Area Code is required or not. Default set to <b>No</b>")
    @Selection(type = Selection.SELECT,
            options = {
                    @Option(text="No" , value="no"),
                    @Option(text="Yes" , value="yes")

            })
    @Named("areaCodeReq")
    @Inject @Default(values = ArabiaContactUsFormConstant.REQUIRED_NO_VALUE)
    private String areacodeValidation;

    @DialogField(name = "./areaCodeMessage" , fieldLabel = "Area Code Validation Message",
            fieldDescription = "Validation message for Area Code field. Default " +
                    "set to <b>Please enter Area Code </b>")
    @TextField @Named("areaCodeMessage")
    @Inject @Default(values = ArabiaContactUsFormConstant.AREACODE_VALIDATION_MSG)
    private String areacodeValidationMessage;

    public String getAreacodeText() {
        return areacodeText;
    }

    public String getAreacodeValidation() {
        return areacodeValidation;
    }

    public String getAreacodeValidationMessage() {
        return areacodeValidationMessage;
    }
}
