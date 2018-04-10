package com.mcd.rwd.arabia.core.common;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
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
        value = "First Name *", name = "fname",
        path = "/content/arabiacontactus",
        disableTargeting = true,
        editConfig = false,
        group = ComponentConstants.GROUP_HIDDEN)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class FirstName {

    @DialogField(name = "./firstName", fieldLabel = "First name Text", value = ArabiaContactUsFormConstant.FIRST_NAME,
            fieldDescription = "Text for First Name field. Default set to <b>First Name </b>")
    @TextField
    @Inject @Named("firstName")
    @Default(values = ArabiaContactUsFormConstant.FIRST_NAME)
    private String firstNameText;

    @DialogField(name = "./firstNameValidationMessage", fieldLabel = "First Name Validation Message",
            fieldDescription = "Validation message for First Name field. Default set to <b>Please" +
                    " enter First Name </b>")
    @TextField @Named("firstNameValidationMessage")
    @Inject @Default(values = ArabiaContactUsFormConstant.FNAME_VALIDATION_MSG)
    private String firstNameValidationMessage;

    public String getFirstNameText() {
        return firstNameText;
    }

    public String getFirstNameValidationMessage() {
        return firstNameValidationMessage;
    }
}
