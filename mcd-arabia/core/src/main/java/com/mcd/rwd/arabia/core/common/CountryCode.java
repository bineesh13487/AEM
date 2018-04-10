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
        value = "Mobile Phone Country Code", name = "ccntrycode",
        path = "/content/arabiacontactus",
        disableTargeting = true,
        editConfig = false,
        group = ComponentConstants.GROUP_HIDDEN)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class CountryCode {

    @DialogField(name = "./countryCode", fieldLabel = "Country Code Text", fieldDescription = "Text for Country Code " +
            "field. Default set to <b>Country Code</b>.")
    @TextField
    @Inject
    @Named("countryCode") @Default(values = ArabiaContactUsFormConstant.COUNTRY_CODE)
    private String countrycodeText;

    @DialogField(name = "./countryCodeReq", fieldLabel = "Country Code Required",
            value = ArabiaContactUsFormConstant.REQUIRED_NO_VALUE,
            fieldDescription = "Please select Country Code is required or not. Default set to <b>No</b>")
    @Selection(type = Selection.SELECT,
            options = {
                    @Option(text="No" , value="no"),
                    @Option(text="Yes" , value="yes")
            })
    @Named("countryCodeReq")
    @Inject @Default(values = ArabiaContactUsFormConstant.REQUIRED_NO_VALUE)
    private String countrycodeValidation;

    @DialogField(name = "./countryCodeMessage" , fieldLabel = "Country Code Validation Message",
            fieldDescription = "Validation message for Country Code field. Default set to " +
                    "<b>Please enter Country Code </b>")
    @TextField @Named("countryCodeMessage")
    @Inject @Default(values = ArabiaContactUsFormConstant.COUNTY_CODE_VALIDATION_MSG)
    private String countrycodeValidationMessage;

    public String getCountrycodeText() {
        return countrycodeText;
    }

    public String getCountrycodeValidation() {
        return countrycodeValidation;
    }

    public String getCountrycodeValidationMessage() {
        return countrycodeValidationMessage;
    }
}
