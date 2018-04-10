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
        value = "Select Form Field",
        path = SSOProfileConstants.SSO_COMPONENTS_PATH + "/common",
        disableTargeting = true,
        editConfig = false,
        group = ComponentConstants.GROUP_HIDDEN)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class SelectFormField {

    @DialogField(
            fieldLabel = "Include this drop down",
            name = "./included",
            fieldDescription = "Check this checkbox if this drop down should be included on the form.",
            ranking = SSOProfileConstants.RANKING_ZERO
    )
    @Selection(type = "checkbox")
    @Inject @Default(values = "false")
    private boolean included;

    @DialogField(
            fieldLabel = "Label",
            name = "./label",
            fieldDescription = "Label to show next to the drop down.",
            ranking = SSOProfileConstants.RANKING_ONE)
    @TextField
    @Inject @Default(values = "")
    private String label;

    @DialogField(
            fieldLabel = "Make this drop down required",
            name = "./required",
            fieldDescription = "Check this checkbox if this drop down should be required.",
            ranking = SSOProfileConstants.RANKING_TWO
    )
    @Selection(type = "checkbox")
    @Inject @Default(values = "false")
    private boolean required;

    @DialogField(
            fieldLabel = "Required Error Message",
            name = "./requiredError",
            fieldDescription = "Error message to show when required drop down validation fails.",
            additionalProperties = @Property(name = "emptyText", value = SSOProfileConstants.FIELD_REQUIRED_DEFAULT_ERROR),
            ranking = SSOProfileConstants.RANKING_THREE
    )
    @TextField
    @Inject @Default(values = SSOProfileConstants.FIELD_REQUIRED_DEFAULT_ERROR)
    private String requiredError;

    @DialogField(
            fieldLabel = "Description",
            name = "./description",
            fieldDescription = "Description to show below the form field.",
            ranking = SSOProfileConstants.RANKING_FIVE
    )
    @TextField
    @Inject @Default(values = "")
    private String description;

    @DialogField(
            fieldLabel = "'Why' text",
            name = "./note",
            fieldDescription = "Text to show when user clicks the 'Why?' link. If text is provided, the 'Why?' link will be displayed.",
            ranking = SSOProfileConstants.RANKING_SIX
    )
    @TextField
    @Inject @Default(values = "")
    private String note;

    @DialogField(
            name = "./" + JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY,
            defaultValue = SSOProfileConstants.RESOURCE_SELECT_FORM_FIELD_COMPONENT)
    @Hidden(value = SSOProfileConstants.RESOURCE_SELECT_FORM_FIELD_COMPONENT)
    private String resourceType;

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

    public String getDescription() {
        return description;
    }

    public String getNote() {
        return note;
    }
}
