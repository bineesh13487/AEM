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
        value = "Preferred Method Of Contact", name = "cprefcontact",
        path = "/content/arabiacontactus",
        disableTargeting = true,
        editConfig = false,
        group = ComponentConstants.GROUP_HIDDEN)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class PreferredContact {

    @DialogField(name = "./prefContactText", fieldLabel = "Preferred Contact Text",
            fieldDescription = "Text for Preferred Contact  field. Default set to <b>Preferred Contact</b>.")
    @TextField
    @Named("prefContactText")
    @Inject
    @Default(values = ArabiaContactUsFormConstant.PREF_CONTACT)
    private String prefContactText;

    @DialogField(name = "./preferredCont", fieldLabel = "Preferred Contact", fieldDescription = "Please provide " +
            "Preferred Contact by clicking + button. First provided value will be considered as a Representative Value")
    @MultiField
    @TextField
    @Inject @Named("preferredCont")
    private String[] prefContact;

    @DialogField(name = "./preferredContReq", fieldLabel = "Preferred Contact Required",
            fieldDescription = "Please select Preferred Contact is required or not. Default set to <b>No</b>")
    @Selection(type = Selection.SELECT,
            options = {
                    @Option(text="No" , value="no"),
                    @Option(text="Yes" , value="yes")
            })
    @Named("preferredContReq")
    @Inject @Default(values = ArabiaContactUsFormConstant.REQUIRED_NO_VALUE)
    private String prefContactValidation;

    @DialogField(name = "./prefContMessage" , fieldLabel = "Preferred Contact Validation Message",
            fieldDescription = "Validation message for Preferred Contact field. Default " +
                    "set to <b>Please select Preferred Contact</b>")
    @TextField @Named("prefContMessage")
    @Inject @Default(values = ArabiaContactUsFormConstant.PERFCONT_VALIDATION_MSG)
    private String prefContactValidationMessage;

    public String getPrefContactText() {
        return prefContactText;
    }

    public String[] getPrefContact() {
        return prefContact;
    }

    public String getPrefContactValidationMessage() {
        return prefContactValidationMessage;
    }

    public String getPrefContactValidation() {
        return prefContactValidation;
    }
}
