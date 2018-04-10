package com.mcd.rwd.us.core.sso_profile.common;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.widgets.Hidden;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextArea;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.icfolson.aem.library.core.constants.ComponentConstants;
import com.mcd.rwd.us.core.constants.SSOProfileConstants;
import com.mcd.rwd.us.core.sso_profile.model.DirectiveInput;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.jcr.resource.JcrResourceConstants;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

@Component(
        value = "Age Verification Field",
        path = SSOProfileConstants.SSO_COMPONENTS_PATH + "/common",
        disableTargeting = true,
        editConfig = false,
        group = ComponentConstants.GROUP_HIDDEN)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class AgeVerificationField {

    @DialogField(
            fieldLabel = "Include this field",
            name = "./included",
            fieldDescription = "Indicates whether this field should be included on the form.",
            ranking = SSOProfileConstants.RANKING_ZERO)
    @Selection(type = "checkbox")
    @Inject @Default(values = "false")
    private boolean included;

    @DialogField(
            fieldLabel = "Label",
            name = "./label",
            fieldDescription = "Label to show next to the age verification field.",
            ranking = SSOProfileConstants.RANKING_ONE)
    @TextField
    @Inject @Default(values = "")
    private String label;

    @DialogField(
            fieldLabel = "Make this field required",
            name = "./required",
            fieldDescription = "Indicates whether this form field should be required.",
            ranking = SSOProfileConstants.RANKING_TWO)
    @Selection(type = "checkbox")
    @Inject @Default(values = "false")
    private boolean required;

    @DialogField(
            fieldLabel = "Required Error Message",
            name = "./requiredError",
            fieldDescription = "Error message to show when required checkbox validation fails.",
            additionalProperties = @Property(name = "emptyText", value = SSOProfileConstants.FIELD_REQUIRED_DEFAULT_ERROR),
            ranking = SSOProfileConstants.RANKING_THREE)
    @TextField
    @Inject @Default(values = SSOProfileConstants.FIELD_REQUIRED_DEFAULT_ERROR)
    private String requiredError;

    @DialogField(
            fieldLabel = "Yes Option Label",
            name = "./labelYes",
            fieldDescription = "Label for the 'Yes' option.",
            additionalProperties = @Property(name = "emptyText", value = "Yes"),
            ranking = SSOProfileConstants.RANKING_FOUR)
    @TextField
    @Inject @Default(values = "Yes")
    private String labelYes;

    @DialogField(
            fieldLabel = "No Option Label",
            name = "./labelNo",
            fieldDescription = "Label for the 'No' option.",
            additionalProperties = @Property(name = "emptyText", value = "No"),
            ranking = SSOProfileConstants.RANKING_FIVE)
    @TextField
    @Inject @Default(values = "No")
    private String labelNo;

    @DialogField(
            fieldLabel = "Age Verification popup message",
            name = "./ageVerificationDialogMessage",
            fieldDescription = "Message to display in a popup when user clicks on 'No' option of the age verification form field.<br/>"
                    + "When no value is provided, the popup will not be displayed, but user will not be able to fill out the form until 'Yes' is selected.",
            ranking =  SSOProfileConstants.RANKING_SIX)
    @TextArea
    @Inject @Default(values = "")
    private String ageVerificationDialogMessage;

    @DialogField(
            fieldLabel = "Combine with Privacy Policy and T&C",
            name = "./combineWithPrivacyAndTC",
            fieldDescription = "When combining with Privacy Policy and Terms & Conditions, the profile will include flag that user verified "
                    + "their age, even if the field is not found on the form.<br/>"
                    + "This checkbox will only be looked at, if the field is not included on the form on its own.",
            ranking = SSOProfileConstants.RANKING_SEVEN)
    @Selection(type = "checkbox")
    @Inject @Default(values = "false")
    private boolean combineWithPrivacyAndTC;

    @DialogField(
            name = "./" + JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY,
            defaultValue = SSOProfileConstants.RESOURCE_AGEVERIFICATION_COMPONENT)
    @Hidden(value = SSOProfileConstants.RESOURCE_AGEVERIFICATION_COMPONENT)
    private String resourceType;

    @Inject
    private Resource resource;

    public boolean isIncluded() {
        return included;
    }

    public boolean isRequired() {
        return required;
    }

    public String getLabel() {
        return label;
    }

    public String getRequiredError() {
        return requiredError;
    }

    public String getLabelYes() {
        return labelYes;
    }

    public String getLabelNo() {
        return labelNo;
    }

    public String getAgeVerificationDialogMessage() {
        return ageVerificationDialogMessage;
    }

    public boolean isCombineWithPrivacyAndTC() {
        return combineWithPrivacyAndTC;
    }

    public DirectiveInput getDirectiveInput() {
        final DirectiveInput directiveInput = new DirectiveInput(resource.getName(), label, "radio");
        if (included) {
            directiveInput.addOption(labelYes, "Y").addOption(labelNo, "N");
        } else if (combineWithPrivacyAndTC) {
            directiveInput.setType("hidden").setRequired().addValue("Y");
        } else {
            return null;
        }
        return directiveInput;
    }
}
