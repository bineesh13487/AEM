package com.mcd.rwd.us.core.sso_profile.common;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.widgets.Hidden;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.icfolson.aem.library.core.constants.ComponentConstants;
import com.mcd.rwd.us.core.constants.SSOProfileConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.jcr.resource.JcrResourceConstants;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

@Component(
        value = "Password Confirm Field",
        path = SSOProfileConstants.SSO_COMPONENTS_PATH + "/common",
        disableTargeting = true,
        editConfig = false,
        group = ComponentConstants.GROUP_HIDDEN)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class PasswordConfirmField {

    @DialogField(
            fieldLabel = "Include this field",
            name = "./included",
            fieldDescription = "Check this checkbox if this field should be included on the form.",
            ranking = SSOProfileConstants.RANKING_ZERO
    )
    @Selection(type = "checkbox")
    @Inject @Default(values = "false")
    private boolean included;

    @DialogField(
            fieldLabel = "Label",
            name = "./label",
            fieldDescription = "Label shown next to the field.",
            ranking = SSOProfileConstants.RANKING_ONE)
    @TextField
    @Inject @Default(values = {""})
    private String label;

    @DialogField(
            fieldLabel = "In-Field Text",
            name = "./placeholder",
            fieldDescription = "In-Field Text to display within the text input field.",
            ranking = SSOProfileConstants.RANKING_TWO)
    @TextField
    @Inject @Default(values = {""})
    private String placeholder;

    @DialogField(
            fieldLabel = "Make this field required",
            name = "./required",
            fieldDescription = "Check this checkbox if this field should be required.",
            ranking = SSOProfileConstants.RANKING_THREE)
    @Selection(type = "checkbox")
    @Inject
    @Default(values = "false")
    private boolean required;

    @DialogField(
            fieldLabel = "Required Error Message",
            name = "./requiredError",
            fieldDescription = "Error message to show when required field validation fails.",
            additionalProperties = @Property(name = "emptyText", value = SSOProfileConstants.FIELD_REQUIRED_DEFAULT_ERROR),
            ranking = SSOProfileConstants.RANKING_FOUR)
    @TextField
    @Inject @Default(values = SSOProfileConstants.FIELD_REQUIRED_DEFAULT_ERROR)
    private String requiredError;

    @DialogField(
            fieldLabel = "Passwords don't match Message",
            name = "./validationRegExError",
            fieldDescription = "Error message to show when passwords don't match.",
//            additionalProperties = @Property(name = "emptyText", value = SSOProfileConstants.FIELD_NOT_VALID_DEFAULT_ERROR),
            required = true,
            ranking = SSOProfileConstants.RANKING_SIX
    )
    @TextField
    @Inject @Default(values = "")
    private String validationRegExError;

    @DialogField(
            fieldLabel = "Description",
            name = "./description",
            fieldDescription = "Description to show below the form field. This could include explanation of the expected format.",
            ranking = SSOProfileConstants.RANKING_SEVEN)
    @TextField
    @Inject @Default(values = "")
    private String description;

    @DialogField(
            fieldLabel = "'Why' text",
            name = "./note",
            fieldDescription = "Text to show when user clicks the 'Why?' link. If text is provided, the 'Why?' link will be displayed.",
            ranking = SSOProfileConstants.RANKING_EIGHT
    )
    @TextField
    @Inject @Default(values = "")
    private String note;

    @DialogField(
            name = "./" + JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY,
            defaultValue = SSOProfileConstants.RESOURCE_TEXT_FORM_FIELD_COMPONENT)
    @Hidden(value = SSOProfileConstants.RESOURCE_TEXT_FORM_FIELD_COMPONENT)
    private String resourceType;

    public boolean isIncluded() {
        return included;
    }

    public String getLabel() {
        return label;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public String getDescription() {
        return description;
    }

    public String getNote() {
        return note;
    }

    public boolean isRequired() {
        return required;
    }

    public String getRequiredError() {
        return requiredError;
    }

    public String getValidationRegExError() {
        return validationRegExError;
    }
}
