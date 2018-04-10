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
        value = "Email Address *", name = "cemail",
        path = "/content/arabiacontactus",
        disableTargeting = true,
        editConfig = false,
        group = ComponentConstants.GROUP_HIDDEN)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class EmailId {

    @DialogField(name = "./email", fieldLabel = "Email Text", fieldDescription = "Text for email field." +
            " Default set to <b>Email </b>", value = ArabiaContactUsFormConstant.EMAIL)
    @TextField
    @Inject
    @Named("email") @Default(values = ArabiaContactUsFormConstant.EMAIL)
    private String emailText;

    @DialogField(name = "./emailMessage", fieldLabel = "Email Validation Message", fieldDescription = "Validation message " +
            "for email field. Default set to <b>Please enter Email </b>")
    @TextField
    @Inject
    @Named("emailMessage") @Default(values = ArabiaContactUsFormConstant.EMAIL_VALIDATION_MSG)
    private String emailValidationMessage;

    public String getEmailText() {
        return emailText;
    }

    public String getEmailValidationMessage() {
        return emailValidationMessage;
    }
}
