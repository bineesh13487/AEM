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
        value = "Hear About Us", name = "chearus",
        path = "/content/arabiacontactus",
        disableTargeting = true,
        editConfig = false,
        group = ComponentConstants.GROUP_HIDDEN)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class HearAboutUs {

    @DialogField(name = "./hearAboutText", fieldLabel = "Hear About Us Text",
            fieldDescription = "Text for Hear about us field. Default set to <b>Hear About Us</b>.")
    @TextField
    @Named("hearAboutText")
    @Inject
    @Default(values = ArabiaContactUsFormConstant.HEAR_ABOUT_US)
    private String hearAboutUsText;

    @DialogField(name = "./hearAbout", fieldLabel = "Hear About Us ", fieldDescription = "Please provide Hear " +
            "about Us by clicking + button. First provided value will be considered as a Representative Value")
    @MultiField
    @TextField
    @Inject @Named("hearAbout")
    private String[] heearAboutUs;

    @DialogField(name = "./hearAboutReq", fieldLabel = "Hear About Us Required",
            fieldDescription = "Please select Hear About Us is required or not. Default set to <b>No</b>")
    @Selection(type = Selection.SELECT,
            options = {
                    @Option(text="No" , value="no"),
                    @Option(text="Yes" , value="yes")
            })
    @Named("hearAboutReq")
    @Inject @Default(values = ArabiaContactUsFormConstant.REQUIRED_NO_VALUE)
    private String hearAboutUsValidation;

    @DialogField(name = "./hearAboutMessage" , fieldLabel = "Hear About Us Validation Message",
            fieldDescription = "Validation message for Hear About Us field. Default set to <b>Please" +
                    " select How You Hear About Us</b>")
    @TextField @Named("hearAboutMessage")
    @Inject @Default(values = ArabiaContactUsFormConstant.HEAR_VALIDATION_MSG)
    private String hearAboutUsValidationMessage;

    public String getHearAboutUsText() {
        return hearAboutUsText;
    }

    public String[] getHeearAboutUs() {
        return heearAboutUs;
    }

    public String getHearAboutUsValidation() {
        return hearAboutUsValidation;
    }

    public String getHearAboutUsValidationMessage() {
        return hearAboutUsValidationMessage;
    }
}
