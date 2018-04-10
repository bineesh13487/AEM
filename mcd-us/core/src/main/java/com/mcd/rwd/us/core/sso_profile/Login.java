package com.mcd.rwd.us.core.sso_profile;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Listener;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.Tab;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.RichTextEditor;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.icfolson.aem.library.api.link.Link;
import com.icfolson.aem.library.core.constants.ComponentConstants;
import com.icfolson.aem.library.models.annotations.LinkInject;
import com.mcd.rwd.us.core.constants.SSOProfileConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

@Component(
        value = Login.COMPONENT_TITLE,
        path = SSOProfileConstants.SSO_COMPONENTS_PATH,
        actions = {
                ComponentConstants.EDIT_BAR_TEXT + Login.COMPONENT_TITLE,
                ComponentConstants.EDIT_BAR_SPACER,
                ComponentConstants.EDIT_BAR_EDIT + ComponentConstants.EDIT_BAR_ANNOTATE,
                ComponentConstants.EDIT_BAR_COPY_MOVE,
                ComponentConstants.EDIT_BAR_DELETE,
                ComponentConstants.EDIT_BAR_SPACER,
                ComponentConstants.EDIT_BAR_INSERT },
        disableTargeting = true,
        tabs = {
                @Tab(title = "Traditional Login"),
                @Tab(title = "Social Login"),
                @Tab(title = "Forgot Password"),
                @Tab(title = "Form Errors") },
        listeners = @Listener(name = ComponentConstants.EVENT_AFTER_EDIT, value = ComponentConstants.REFRESH_PAGE),
        dialogWidth = SSOProfileConstants.DIALOG_DEFAULT_WIDTH,
        dialogHeight = SSOProfileConstants.DIALOG_DEFAULT_HEIGHT)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public final class Login implements Serializable {

    public static final String COMPONENT_TITLE = "Login";

    private static final String LINK_FORMAT = "<a href=\"{{href}}\" title=\"{{title}}\" target=\"_blank\">{{title}}</a>";

    private static final String PROP_PRIVACY_PAGE_PATH = "privacyPage";
    private static final String PROP_TERMS_CONDITIONS_PAGE_PATH = "termsAndConditionsPage";


    // tab 1 - traditional login

    @DialogField(
            fieldLabel = "Login form title",
            name = "./loginFormTitle",
            fieldDescription = "Title to show on the Login form view.",
            additionalProperties = @Property(name = "emptyText", value = "Login"),
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "Login")
    private String loginFormTitle;

    @DialogField(
            fieldLabel = "Email In-Field Text",
            name = "./emailPlaceholder",
            fieldDescription = "In-Field Text to display within the Email input field.",
            additionalProperties = @Property(name = "emptyText", value = "Email"),
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "Email")
    private String emailPlaceholder;

    @DialogField(
            fieldLabel = "Email required error",
            name = "./emailRequiredError",
            fieldDescription = "Error message to show when required field validation fails.",
            additionalProperties =
                @Property(name = "emptyText", value = SSOProfileConstants.FIELD_REQUIRED_DEFAULT_ERROR),
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = SSOProfileConstants.FIELD_REQUIRED_DEFAULT_ERROR)
    private String emailRequiredError;

    @DialogField(
            fieldLabel = "Email validation RegEx",
            name = "./emailRegEx",
            fieldDescription = "Regular expression for front end email validation.",
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "")
    private String emailRegEx;

    @DialogField(
            fieldLabel = "Email validation error",
            name = "./emailRegExError",
            fieldDescription = "Error message to show when regular expression validation fails.",
            additionalProperties =
                @Property(name = "emptyText", value = SSOProfileConstants.FIELD_NOT_VALID_DEFAULT_ERROR),
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = SSOProfileConstants.FIELD_NOT_VALID_DEFAULT_ERROR)
    private String emailRegExError;

    @DialogField(
            fieldLabel = "Password In-Field Text",
            name = "./passwordPlaceholder",
            fieldDescription = "In-Field Text to display within the Password input field.",
            additionalProperties = @Property(name = "emptyText", value = "Password"),
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "Password")
    private String passwordPlaceholder;

    @DialogField(
            fieldLabel = "Password required error",
            name = "./passwordRequiredError",
            fieldDescription = "Error message to show when required field validation fails.",
            additionalProperties =
                @Property(name = "emptyText", value = SSOProfileConstants.FIELD_REQUIRED_DEFAULT_ERROR),
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = SSOProfileConstants.FIELD_REQUIRED_DEFAULT_ERROR)
    private String passwordRequiredError;

    @DialogField(
            fieldLabel = "Remember me label",
            name = "./rememberMeLabel",
            fieldDescription = "Label to show next to 'Remember me' checkbox.",
            additionalProperties = @Property(name = "emptyText", value = "Remember me"),
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "Remember me")
    private String rememberMeLabel;

    @DialogField(
            fieldLabel = "T&C and Privacy Policy Text",
            name = "./termsAndPrivacyLabel",
            fieldDescription = "Privacy Policy and Terms & Conditions text to show below the form fields. "
                    + "<br>Use <code>{{terms}}</code> and <code>{{privacy}}</code> as placeholders for Terms & Conditions and Privacy Policy links.<br/>"
                    + "E.g.: <i>By logging in you agree to our {{terms}} and {{privacy}}.</i>",
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "")
    private String termsAndPrivacyLabel;

    @DialogField(
            fieldLabel = "Privacy Policy page path",
            name = "./" + PROP_PRIVACY_PAGE_PATH,
            fieldDescription = "Path to the privacy policy page.",
            tab = SSOProfileConstants.TAB_ONE,
//            ranking = SSOProfileConstants.RANKING_ZERO,
            required = true)
    @PathField(rootPath = SSOProfileConstants.CONTENT_ROOT)
    @LinkInject(titleProperty = "privacyPolicyLinkTitle") @Optional
    @Named(PROP_PRIVACY_PAGE_PATH)
    private Link privacyPolicyLink;

    @DialogField(
            fieldLabel = "Privacy Policy link title",
            name = "./privacyPolicyLinkTitle",
            tab = SSOProfileConstants.TAB_ONE,
//            ranking = SSOProfileConstants.RANKING_ZERO,
            required = true
    )
    @TextField
    @Inject @Default(values = "")
    private String privacyPolicyLinkTitle;

    @DialogField(
            fieldLabel = "Terms & Conditions page path",
            name = "./" + PROP_TERMS_CONDITIONS_PAGE_PATH,
            fieldDescription = "Path to the terms and conditions page.",
            tab = SSOProfileConstants.TAB_ONE,
//            ranking = SSOProfileConstants.RANKING_ZERO,
            required = true)
    @PathField(rootPath = SSOProfileConstants.CONTENT_ROOT)
    @LinkInject(titleProperty = "termsAndConditionsLinkTitle") @Optional
    @Named(PROP_TERMS_CONDITIONS_PAGE_PATH)
    private Link termsAndConditionsLink;

    @DialogField(
            fieldLabel = "Terms & Conditions link title",
            name = "./termsAndConditionsLinkTitle",
            tab = SSOProfileConstants.TAB_ONE,
//            ranking = SSOProfileConstants.RANKING_ZERO,
            required = true
    )
    @TextField
    @Inject @Default(values = "")
    private String termsAndConditionsLinkTitle;

    @DialogField(
            fieldLabel = "Required Field Text",
            name = "./requiredFieldsText",
            fieldDescription = "Required field indicator text. This text will explain to the user that fields marked with asterisk are required."
                    + "<br/>The required field indicator text will appear at the bottom of the form if it contains required fields."
                    + "<br/>E.g.: <i>- required field</i>",
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "")
    private String requiredFieldsText;

    @DialogField(
            fieldLabel = "Submit button title",
            name = "./loginFormSubmitButtonTitle",
            fieldDescription = "Submit button title for the traditional login form.",
            additionalProperties = @Property(name = "emptyText", value = "Log In"),
            tab = SSOProfileConstants.TAB_ONE
//            ranking = SSOProfileConstants.RANKING_ZERO,
    )
    @TextField
    @Inject @Default(values = "Log In")
    private String loginFormSubmitButtonTitle;

    private String termsAndPrivacyLabelWithLinks;

    @DialogField(
            fieldLabel = "Page to show after successful login",
            name = "./redirectAfterLoginPath",
            fieldDescription = "Path to the page where user should be redirected after successfully logging in.",
            tab = SSOProfileConstants.TAB_ONE,
            required = true)
    @PathField(rootPath = SSOProfileConstants.CONTENT_ROOT)
    @LinkInject @Optional
    private Link redirectAfterLoginPath;

    @DialogField(
            fieldLabel = "Registration page path",
            name = "./registrationPagePath",
            fieldDescription = "Path to the registration page.",
            tab = SSOProfileConstants.TAB_ONE,
            required = true)
    @PathField(rootPath = SSOProfileConstants.CONTENT_ROOT)
    @LinkInject @Optional
    private Link registrationPagePath;

    @DialogField(
            fieldLabel = "Registration link title",
            name = "./registrationLinkTitle",
            tab = SSOProfileConstants.TAB_ONE,
            required = true)
    @TextField
    @Inject @Default(values = "")
    private String registrationLinkTitle;


    //tab 2 - social login

    @DialogField(
            fieldLabel = "Enable Facebook Login",
            name = "./facebookLoginEnabled",
            fieldDescription = "Check this checkbox if Facebook login should be included on the form.",
            tab = SSOProfileConstants.TAB_TWO,
            listeners = {@Listener(value = "function(comp, value, isChecked) { var dialog = comp.findParentByType(\'dialog\'); "
                    + "var textfield = dialog.getField(\'./socialFacebookButtonTitle\');isChecked ? textfield.enable() : textfield.disable();}",
                    name = "selectionchanged")})
    @Selection(type = "checkbox")
    @Inject @Default(booleanValues = false)
    private boolean facebookLoginEnabled;

    @DialogField(
            fieldLabel = "Facebook button title",
            name = "./socialFacebookButtonTitle",
            fieldDescription = "Facebook button title for the Social Login.",
            additionalProperties = @Property(name = "emptyText", value = "Log In with Facebook"),
            tab = SSOProfileConstants.TAB_TWO,
            disabled = true)
    @TextField
    @Inject @Default(values = "Log In with Facebook")
    private String socialFacebookButtonTitle;

    @DialogField(
            fieldLabel = "Enable Google Login",
            name = "./googleLoginEnabled",
            fieldDescription = "Check this checkbox if Google login should be included on the form.",
            tab = SSOProfileConstants.TAB_TWO,
            listeners = {@Listener(value = "function(comp, value, isChecked) { var dialog = comp.findParentByType(\'dialog\'); "
                    + "var textfield = dialog.getField(\'./socialGoogleButtonTitle\');isChecked ? textfield.enable() : textfield.disable();}",
                    name = "selectionchanged")})
    @Selection(type = "checkbox")
    @Inject @Default(booleanValues = false)
    private boolean googleLoginEnabled;

    @DialogField(
            fieldLabel = "Google button title",
            name = "./socialGoogleButtonTitle",
            fieldDescription = "Google button title for the Social Login.",
            additionalProperties = @Property(name = "emptyText", value = "Log In with Google"),
            tab = SSOProfileConstants.TAB_TWO,
            disabled = true)
    @TextField
    @Inject @Default(values = "Log In with Google")
    private String socialGoogleButtonTitle;

    @DialogField(
            fieldLabel = "'Or' label",
            name = "./labelOr",
            fieldDescription = "'Or' text to show above the social login buttons.",
            additionalProperties = @Property(name = "emptyText", value = "Or"),
            tab = SSOProfileConstants.TAB_TWO)
    @TextField
    @Inject @Default(values = "Or")
    private String labelOr;

    // tab 3 - Forgot Password

    @DialogField(
            fieldLabel = "Forgot Password Link Title",
            name = "./forgotPasswordLinkTitle",
            fieldDescription = "Forgot Password link title (included on the traditional login form).",
            additionalProperties = @Property(name = "emptyText", value = "Forgot Password?"),
            tab = SSOProfileConstants.TAB_THREE)
    @TextField
    @Inject @Default(values = "Forgot Password?")
    private String forgotPasswordLinkTitle;

    @DialogField(
            fieldLabel = "Forgot Password form title",
            name = "./forgotPasswordFormTitle",
            fieldDescription = "Title to show on the Forgot Password form view.",
            additionalProperties = @Property(name = "emptyText", value = "Forgot password"),
            tab = SSOProfileConstants.TAB_THREE)
    @TextField
    @Inject @Default(values = "Forgot password")
    private String forgotPasswordFormTitle;

    @DialogField(
            fieldLabel = "Forgot Password form instructions",
            name = "./forgotPasswordFormInstructions",
            fieldDescription = "Instructions for filling out the Forgot Password form.",
            additionalProperties = @Property(name = "emptyText", value = ""),
            tab = SSOProfileConstants.TAB_THREE)
    @TextField
    @Inject @Default(values = "")
    private String forgotPasswordFormInstructions;

    @DialogField(
            fieldLabel = "Submit button title",
            name = "./forgotPasswordFormSubmitButtonTitle",
            fieldDescription = "Submit button title for the forgot password form.",
            additionalProperties = @Property(name = "emptyText", value = "Submit"),
            tab = SSOProfileConstants.TAB_THREE)
    @TextField
    @Inject @Default(values = "Submit")
    private String forgotPasswordFormSubmitButtonTitle;

    @DialogField(
            fieldLabel = "Forgot Password message",
            name = "./forgotPasswordMessage",
            fieldDescription = "Message to show to the user after submitting the Forgot Password form. "
                    + "<br>You can use the <code>{{email}}</code> placeholder in the text. That placeholder will be replaced with the"
                    + " email address user entered on the form.",
            additionalProperties = @Property(name = "emptyText", value = ""),
            required = true,
            tab = SSOProfileConstants.TAB_THREE)
    @RichTextEditor
    @Inject @Default(values = "")
    private String forgotPasswordMessage;


    //tab 4 - error messages

    @DialogField(
            fieldLabel = "Account Locked Out Title",
            name = "./accountLockedOutTitle",
            fieldDescription = "Title to show when account is locked out.",
            additionalProperties = @Property(name = "emptyText", value = "Well that didn't work."),
            tab = SSOProfileConstants.TAB_FOUR)
    @TextField
    @Inject @Default(values = "Well that didn't work.")
    private String accountLockedOutTitle;

    @DialogField(
            fieldLabel = "Account Locked Out Message",
            name = "./accountLockedOutMessage",
            fieldDescription = "Message to show when account is locked out.",
            tab = SSOProfileConstants.TAB_FOUR,
            required = true)
    @RichTextEditor
    @Inject @Default(values = "")
    private String accountLockedOutMessage;

    @DialogField(
            fieldLabel = "Error 11210",
            name = "./error11210",
            fieldDescription = "The username/password combination supplied was incorrect.",
            tab = SSOProfileConstants.TAB_FOUR,
            required = true)
    @TextField
    @Inject @Default(values = "")
    @JsonProperty
    private String error11210;

    //commenting out the following error codes: 11211, 11212 & 11213, as part of ticket SPC-389
//    @DialogField(
//            fieldLabel = "Error 11211",
//            name = "./error11211",
//            fieldDescription = "An email/password combination was supplied, but the account is social sign-in only.",
//            tab = SSOProfileConstants.TAB_FOUR,
//            required = true)
//    @TextField
//    @Inject @Default(values = "")
//    @JsonProperty
//    private String error11211;
//
//    @DialogField(
//            fieldLabel = "Error 11212",
//            name = "./error11212",
//            fieldDescription = "An email/password combination was supplied, but the email address doesn’t exist.",
//            tab = SSOProfileConstants.TAB_FOUR,
//            required = true)
//    @TextField
//    @Inject @Default(values = "")
//    @JsonProperty
//    private String error11212;
//
//    @DialogField(
//            fieldLabel = "Error 11213",
//            name = "./error11213",
//            fieldDescription = "An email/password combination was supplied, and the email is valid, but the password is wrong.",
//            tab = SSOProfileConstants.TAB_FOUR,
//            required = true)
//    @TextField
//    @Inject @Default(values = "")
//    @JsonProperty
//    private String error11213;

    @DialogField(
            fieldLabel = "Error 11928",
            name = "./error11928",
            fieldDescription = "Reset password has been initiated for this account and the action has not been completed.",
            tab = SSOProfileConstants.TAB_FOUR,
            required = true)
    @TextField
    @Inject @Default(values = "")
    @JsonProperty
    private String error11928;

    @DialogField(
            fieldLabel = "Error 11930",
            name = "./error11930",
            fieldDescription = "The account has only subscribed and not registered.",
            tab = SSOProfileConstants.TAB_FOUR,
            required = true)
    @TextField
    @Inject @Default(values = "")
    @JsonProperty
    private String error11930;

    @DialogField(
            fieldLabel = "Error 11540",
            name = "./error11540",
            fieldDescription = "Cannot reset password for social account.",
            tab = SSOProfileConstants.TAB_FOUR,
            required = true)
    @TextField
    @Inject @Default(values = "")
    @JsonProperty
    private String error11540;



    @PostConstruct
    private void init() {

        String privacyLink = StringUtils.replace(LINK_FORMAT, "{{href}}", privacyPolicyLink.getHref());
        privacyLink = StringUtils.replace(privacyLink, "{{title}}", privacyPolicyLinkTitle);

        String termsLink = StringUtils.replace(LINK_FORMAT, "{{href}}", termsAndConditionsLink.getHref());
        termsLink = StringUtils.replace(termsLink, "{{title}}", termsAndConditionsLinkTitle);

        termsAndPrivacyLabelWithLinks = StringUtils.replace(termsAndPrivacyLabel, "{{terms}}", termsLink);
        termsAndPrivacyLabelWithLinks = StringUtils.replace(termsAndPrivacyLabelWithLinks, "{{privacy}}", privacyLink);
    }

    @JsonProperty
    public String getRedirectAfterLoginHref() {
        return redirectAfterLoginPath != null ? redirectAfterLoginPath.getHref() : "";
    }

    public String getLoginFormTitle() {
        return loginFormTitle;
    }

    public String getEmailPlaceholder() {
        return emailPlaceholder;
    }

    public String getEmailRequiredError() {
        return emailRequiredError;
    }

    public String getEmailRegEx() {
        return emailRegEx;
    }

    public String getEmailRegExError() {
        return emailRegExError;
    }

    public String getPasswordPlaceholder() {
        return passwordPlaceholder;
    }

    public String getPasswordRequiredError() {
        return passwordRequiredError;
    }

    public String getRememberMeLabel() {
        return rememberMeLabel;
    }

    public String getLoginFormSubmitButtonTitle() {
        return loginFormSubmitButtonTitle;
    }

    public String getTermsAndPrivacyLabelWithLinks() {
        return termsAndPrivacyLabelWithLinks;
    }

    public String getForgotPasswordLinkTitle() {
        return forgotPasswordLinkTitle;
    }

    public String getForgotPasswordFormTitle() {
        return forgotPasswordFormTitle;
    }

    public String getForgotPasswordFormInstructions() {
        return forgotPasswordFormInstructions;
    }

    public String getForgotPasswordFormSubmitButtonTitle() {
        return forgotPasswordFormSubmitButtonTitle;
    }

    public String getForgotPasswordMessage() {
        return forgotPasswordMessage;
    }

    public boolean isFacebookLoginEnabled() {
        return facebookLoginEnabled;
    }

    public String getSocialFacebookButtonTitle() {
        return socialFacebookButtonTitle;
    }

    public boolean isGoogleLoginEnabled() {
        return googleLoginEnabled;
    }

    public String getSocialGoogleButtonTitle() {
        return socialGoogleButtonTitle;
    }

    public String getLabelOr() {
        return labelOr;
    }

    public String getAccountLockedOutTitle() {
        return accountLockedOutTitle;
    }

    public String getAccountLockedOutMessage() {
        return accountLockedOutMessage;
    }

    public String getRequiredFieldsText() {
        return requiredFieldsText;
    }

    public String getRegistrationLinkHref() {
        return registrationPagePath != null ? registrationPagePath.getHref() : "";
    }

    public String getRegistrationLinkTitle() {
        return registrationLinkTitle;
    }
}
