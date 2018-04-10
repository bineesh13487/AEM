package com.mcd.rwd.us.core.sso_profile;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Listener;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.Tab;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.TextArea;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.icfolson.aem.library.api.link.Link;
import com.icfolson.aem.library.core.constants.ComponentConstants;
import com.icfolson.aem.library.models.annotations.LinkInject;
import com.mcd.rwd.us.core.constants.SSOProfileConstants;
import com.mcd.rwd.us.core.sso_profile.model.Directive;
import com.mcd.rwd.us.core.sso_profile.model.DirectiveInput;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import javax.inject.Inject;

@Component(
        value = ResetPassword.COMPONENT_TITLE,
        path = SSOProfileConstants.SSO_COMPONENTS_PATH,
        actions = {
                ComponentConstants.EDIT_BAR_TEXT + ResetPassword.COMPONENT_TITLE,
                ComponentConstants.EDIT_BAR_SPACER,
                ComponentConstants.EDIT_BAR_EDIT + ComponentConstants.EDIT_BAR_ANNOTATE,
                ComponentConstants.EDIT_BAR_COPY_MOVE,
                ComponentConstants.EDIT_BAR_DELETE,
                ComponentConstants.EDIT_BAR_SPACER,
                ComponentConstants.EDIT_BAR_INSERT },
        disableTargeting = true,
        tabs = {
                @Tab(title = "Reset Password"),
                @Tab(title = "Form Errors")
        },
        listeners = @Listener(name = ComponentConstants.EVENT_AFTER_EDIT, value = ComponentConstants.REFRESH_PAGE),
        dialogWidth = SSOProfileConstants.DIALOG_DEFAULT_WIDTH,
        dialogHeight = SSOProfileConstants.DIALOG_DEFAULT_HEIGHT)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class ResetPassword implements Serializable {
    public static final String COMPONENT_TITLE = "Reset Password";

    @DialogField(
            fieldLabel = "Reset Password form title",
            name = "./resetPasswordFormTitle",
            fieldDescription = "Reset Password title to show above the form.",
            additionalProperties = @Property(name = "emptyText", value = "Password reset"),
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "Password reset")
    private String resetPasswordFormTitle;

    @DialogField(
            fieldLabel = "Reset Password form instructions",
            name = "./resetPasswordFormInstructions",
            fieldDescription = "Text to show above the Reset Password form fields.",
            tab = SSOProfileConstants.TAB_ONE)
    @TextArea
    @Inject @Default(values = "")
    private String resetPasswordFormInstructions;

    @DialogField(
            fieldLabel = "Submit button text",
            name = "./submitButtonText",
            fieldDescription = "The submit button text.",
            additionalProperties = @Property(name = "emptyText", value = "Submit"),
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "Submit")
    private String submitButtonText;

    @DialogField(
            fieldLabel = "Password In-Field Text",
            name = "./passwordPlaceholder",
            fieldDescription = "The In-Field Text for the password field.",
            additionalProperties = @Property(name = "emptyText", value = "Password"),
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = {"Password"})
    private String passwordPlaceholder;

    @DialogField(
            fieldLabel = "Confirm Password In-Field Text",
            name = "./confirmPasswordPlaceholder",
            fieldDescription = "The In-Field Text for the Confirm Password field.",
            additionalProperties = @Property(name = "emptyText", value = "Confirm Password"),
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = {"Confirm Password"})
    private String confirmPasswordPlaceholder;

    @DialogField(
            fieldLabel = "Password field description",
            name = "./passwordDescription",
            fieldDescription = "Description to show under the Confirm Password field. "
                    + "The description should inform the user what is the format of a valid password.<br/>"
                    + "E.g.: <i>Your password must include at least 8 characters and one uppercase character.</i>",
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "")
    private String passwordDescription;

    @DialogField(
            fieldLabel = "Password required error",
            name = "./passwordRequiredError",
            fieldDescription = "Error message to show when required field validation fails.",
            additionalProperties = @Property(name = "emptyText", value = SSOProfileConstants.FIELD_REQUIRED_DEFAULT_ERROR),
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = SSOProfileConstants.FIELD_REQUIRED_DEFAULT_ERROR)
    private String passwordRequiredError;

    @DialogField(
            fieldLabel = "Password validation RegEx",
            name = "./passwordRegEx",
            fieldDescription = "Regular expression for front end password validation.",
            required = true,
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "")
    private String passwordRegEx;

    @DialogField(
            fieldLabel = "Password validation error",
            name = "./passwordRegExError",
            fieldDescription = "Error message to show when regular expression validation fails.",
            additionalProperties = @Property(name = "emptyText", value = SSOProfileConstants.FIELD_NOT_VALID_DEFAULT_ERROR),
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = SSOProfileConstants.FIELD_NOT_VALID_DEFAULT_ERROR)
    private String passwordRegExError;

    @DialogField(
            fieldLabel = "Confirm Password required error",
            name = "./confirmPasswordRequiredError",
            fieldDescription = "Error message to show when required field validation fails.",
            additionalProperties = @Property(name = "emptyText", value = SSOProfileConstants.FIELD_REQUIRED_DEFAULT_ERROR),
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = SSOProfileConstants.FIELD_REQUIRED_DEFAULT_ERROR)
    private String confirmPasswordRequiredError;

    @DialogField(
            fieldLabel = "Passwords don't match error",
            name = "./passwordMatchError",
            fieldDescription = "Error message to show when the Confirm Password does not match Password.",
            required = true,
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "")
    private String passwordMatchError;

    @DialogField(
            fieldLabel = "Required Field Text",
            name = "./requiredFieldsText",
            fieldDescription = "Required field indicator text. This text will explain to the user that fields marked with asterisk are required.<br/>"
                    + "E.g.: <i>- required field</i>",
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "")
    private String requiredFieldsText;

    @DialogField(
            fieldLabel = "Page to redirect on success",
            name = "./redirectPage",
            fieldDescription = "Path to the page (or fully qualified URL) where user should be redirected after successful password reset.",
            required = true,
            tab = SSOProfileConstants.TAB_ONE)
    @PathField(rootPath = SSOProfileConstants.CONTENT_ROOT)
    @LinkInject @Optional
    private Link redirectPage;

    @DialogField(
            fieldLabel = "Error 11935",
            name = "./error11935",
            fieldDescription = "The account has already reset the password.",
            tab = SSOProfileConstants.TAB_TWO,
            required = true)
    @TextField
    @Inject @Default(values = "")
    @JsonProperty
    private String error11935;


    public String getDirectiveInputsJson() {
        return new Directive()
                .addInput(new DirectiveInput("password", "", "password")
                    .setRequired()
                    .addPlaceholder(passwordPlaceholder)
                    .addRequireMsg(passwordRequiredError)
                    .addValidation(passwordRegEx)
                    .addErrorMsg(passwordRegExError))
                .addInput(new DirectiveInput("confirmPassword", "", "password")
                    .setRequired()
                        .addPlaceholder(confirmPasswordPlaceholder)
                        .addRequireMsg(confirmPasswordRequiredError)
                        .addErrorMsg(passwordMatchError)
                        .addDescription(passwordDescription))
                .getJson();
    }


    public String getResetPasswordFormTitle() {
        return resetPasswordFormTitle;
    }

    public String getResetPasswordFormInstructions() {
        return resetPasswordFormInstructions;
    }

    public String getPasswordDescription() {
        return passwordDescription;
    }

    public String getSubmitButtonText() {
        return submitButtonText;
    }

    public String getPasswordPlaceholder() {
        return passwordPlaceholder;
    }

    public String getConfirmPasswordPlaceholder() {
        return confirmPasswordPlaceholder;
    }

    public String getPasswordRegEx() {
        return passwordRegEx;
    }

    public String getPasswordRegExError() {
        return passwordRegExError;
    }

    public String getPasswordRequiredError() {
        return passwordRequiredError;
    }

    public String getPasswordMatchError() {
        return passwordMatchError;
    }

    public String getRedirectPage() {
        return redirectPage != null ? redirectPage.getHref() : "";
    }

    public String getRequiredFieldsText() {
        return requiredFieldsText;
    }
}
