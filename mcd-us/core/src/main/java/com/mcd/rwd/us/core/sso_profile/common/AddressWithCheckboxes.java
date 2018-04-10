package com.mcd.rwd.us.core.sso_profile.common;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.icfolson.aem.library.core.constants.ComponentConstants;
import com.mcd.rwd.us.core.constants.SSOProfileConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

@Component(
        value = "Mailing Address",
        path = SSOProfileConstants.SSO_COMPONENTS_PATH + "/common",
        disableTargeting = true,
        editConfig = false,
        group = ComponentConstants.GROUP_HIDDEN)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class AddressWithCheckboxes extends Address {

    @DialogField(xtype = "displayfield", hideLabel = true,
            fieldDescription = "There is an option to include two checkboxes below the Mailing Address form fields: 'Use as Billing Address' and "
                    + "'Use as Delivery Address'. When user checks the checkbox, values from Mailing Address fields will be copied to Billing Address "
                    + "and Delivery Address respectively.<br/>"
                    + "These checkboxes will only be included, if a Mailing Address is also included and if their labels are populated below.",
            ranking = SSOProfileConstants.RANKING_SEVEN)
    private String note;

    @DialogField(
            fieldLabel = "'Use as Billing Address' checkbox label",
            name = "./labelUseAsBilling",
            fieldDescription = "Label to show next to the 'Use as Billing Address' checkbox. <br/>"
                    + "If not populated, the checkbox will not be included on the form.",
            ranking = SSOProfileConstants.RANKING_EIGHT)
    @TextField
    @Inject @Default(values = "")
    private String labelUseAsBilling;

    @DialogField(
            fieldLabel = "'Use as Delivery Address' checkbox label",
            name = "./labelUseAsDelivery",
            fieldDescription = "Label to show next to the 'Use as Delivery Address' checkbox. <br/>"
                    + "If not populated, the checkbox will not be included on the form.",
            ranking = SSOProfileConstants.RANKING_NINE)
    @TextField
    @Inject @Default(values = "")
    private String labelUseAsDelivery;


    public String getLabelUseAsBilling() {
        return labelUseAsBilling;
    }

    public String getLabelUseAsDelivery() {
        return labelUseAsDelivery;
    }
}
