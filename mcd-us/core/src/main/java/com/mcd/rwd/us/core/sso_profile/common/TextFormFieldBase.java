package com.mcd.rwd.us.core.sso_profile.common;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.widgets.Hidden;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.mcd.rwd.us.core.constants.SSOProfileConstants;
import org.apache.sling.jcr.resource.JcrResourceConstants;
import org.apache.sling.models.annotations.Default;

import javax.inject.Inject;

public class TextFormFieldBase {

    @DialogField(
            fieldLabel = "Label",
            name = "./label",
            fieldDescription = "Label shown next to the field.",
            ranking = SSOProfileConstants.RANKING_ONE
    )
    @TextField
    @Inject @Default(values = {""})
    private String label;

    @DialogField(
            fieldLabel = "In-Field Text",
            name = "./placeholder",
            fieldDescription = "In-Field Text to display within the text input field.",
            ranking = SSOProfileConstants.RANKING_TWO
    )
    @TextField
    @Inject @Default(values = {""})
    private String placeholder;

    @DialogField(
            fieldLabel = "Validation RegEx",
            name = "./validationRegEx",
            fieldDescription = "Regular expression for front end input field value validation.",
            ranking = SSOProfileConstants.RANKING_FIVE
    )
    @TextField
    @Inject @Default(values = "")
    private String validationRegEx;

    @DialogField(
            fieldLabel = "Validation Error Message",
            name = "./validationRegExError",
            fieldDescription = "Error message to show when RegEx field validation fails. Will only be used if RegEx is provided.",
            additionalProperties = @Property(name = "emptyText", value = SSOProfileConstants.FIELD_NOT_VALID_DEFAULT_ERROR),
            ranking = SSOProfileConstants.RANKING_SIX
    )
    @TextField
    @Inject @Default(values = SSOProfileConstants.FIELD_NOT_VALID_DEFAULT_ERROR)
    private String validationRegExError;

    @DialogField(
            fieldLabel = "Description",
            name = "./description",
            fieldDescription = "Description to show below the form field. This could include explanation of the expected format.",
            ranking = SSOProfileConstants.RANKING_SEVEN
    )
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

    public String getLabel() {
        return label;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public String getValidationRegEx() {
        return validationRegEx;
    }

    public String getValidationRegExError() {
        return validationRegExError;
    }

    public String getDescription() {
        return description;
    }

    public String getNote() {
        return note;
    }
}
