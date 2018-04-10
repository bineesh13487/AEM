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
        value = "Last Name *", name = "lname",
        path = "/content/arabiacontactus",
        disableTargeting = true,
        editConfig = false,
        group = ComponentConstants.GROUP_HIDDEN)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class LastName {

    @DialogField(name = "./lastName", fieldLabel = "Last name text", value = ArabiaContactUsFormConstant.LAST_NAME,
            fieldDescription = "Text for Last Name field. Default set to <b>Last Name </b>")
    @TextField
    @Inject
    @Named("lastName")
    @Default(values = ArabiaContactUsFormConstant.LAST_NAME)
    private String lastNameText;

    @DialogField(name = "./lastNameMessage", fieldLabel = "Last Name Validation Message",
            fieldDescription = "Validation message for Last Name field. Default set to <b>Please" +
                    " enter Last Name </b>")
    @TextField @Named("lastNameMessage")
    @Inject @Default(values = ArabiaContactUsFormConstant.LNAME_VALIDATION_MSG)
    private String lastNameValidationMessage;

    public String getLastNameText() {
        return lastNameText;
    }

    public String getLastNameValidationMessage() {
        return lastNameValidationMessage;
    }
}
