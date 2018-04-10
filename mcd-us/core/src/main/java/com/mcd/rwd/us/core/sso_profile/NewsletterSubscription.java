package com.mcd.rwd.us.core.sso_profile;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Listener;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.Tab;
import com.citytechinc.cq.component.annotations.widgets.DialogFieldSet;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.icfolson.aem.library.api.link.Link;
import com.icfolson.aem.library.api.node.ComponentNode;
import com.icfolson.aem.library.core.constants.ComponentConstants;
import com.icfolson.aem.library.models.annotations.LinkInject;
import com.mcd.rwd.us.core.constants.SSOProfileConstants;
import com.mcd.rwd.us.core.sso_profile.common.TextFormFieldNoIncl;
import com.mcd.rwd.us.core.sso_profile.model.Directive;
import com.mcd.rwd.us.core.sso_profile.model.DirectiveInput;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import javax.inject.Inject;

@Component(
        value = NewsletterSubscription.COMPONENT_TITLE,
        path = SSOProfileConstants.SSO_COMPONENTS_PATH,
        actions = {
                ComponentConstants.EDIT_BAR_TEXT + NewsletterSubscription.COMPONENT_TITLE,
                ComponentConstants.EDIT_BAR_SPACER,
                ComponentConstants.EDIT_BAR_EDIT + ComponentConstants.EDIT_BAR_ANNOTATE,
                ComponentConstants.EDIT_BAR_COPY_MOVE,
                ComponentConstants.EDIT_BAR_DELETE,
                ComponentConstants.EDIT_BAR_SPACER,
                ComponentConstants.EDIT_BAR_INSERT },
        disableTargeting = true,
        tabs = {
                @Tab(title = "Newsletter Subscription"),
                @Tab(title = "Form Errors")
        },
        listeners = @Listener(name = ComponentConstants.EVENT_AFTER_EDIT, value = ComponentConstants.REFRESH_PAGE),
        dialogWidth = SSOProfileConstants.DIALOG_DEFAULT_WIDTH,
        dialogHeight = SSOProfileConstants.DIALOG_DEFAULT_HEIGHT)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class NewsletterSubscription implements Serializable {
    public static final String COMPONENT_TITLE = "Newsletter Subscription";

    @DialogField(
            fieldLabel = "Newsletter Subscription form title",
            name = "./newsletterSubscriptionFormTitle",
            fieldDescription = "Newsletter Subscription title to show above the form.<br>"
                    + "E.g.: <i>DELICIOUSNESS STRAIGHT TO YOUR INBOX.</i>",
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "")
    private String newsletterSubscriptionFormTitle;

    @DialogField(
            fieldLabel = "Newsletter Subscription form subtitle",
            name = "./newsletterSubscriptionFormSubtitle",
            fieldDescription = "Text to show below the Newsletter Subscription form title.<br>"
                    + "E.g.: <i>Find out about deals and events first.</i>",
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "")
    private String newsletterSubscriptionFormSubtitle;

    @DialogField (fieldLabel = "Email", tab = SSOProfileConstants.TAB_ONE)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "email/", title = "Email related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private TextFormFieldNoIncl email;

    @DialogField (fieldLabel = "Postal Code", tab = SSOProfileConstants.TAB_ONE)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "postalCode/", title = "Postal Code related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private TextFormFieldNoIncl postalCode;

    @DialogField(
            fieldLabel = "Disclaimer Text",
            name = "./disclaimerText",
            fieldDescription = "Disclaimer text that appears below form fields.<br>"
                    + "E.g.: <i>By subscribing you agree to receive emails from McDonald's.</i>",
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "")
    private String disclaimerText;

    @DialogField(xtype = "displayfield", fieldLabel = "Alternative Language",
            fieldDescription = "It is possible to include a checkbox on the Newsletter Subscription form that will allow a user "
                    + "to opt for emails in a language other than the default language for the website.<br/>"
                    + "To enable this checkbox, both the checkbox label and the locale need to be populated.<br/>"
                    + "Please note that there needs to be a valid market configured within DCS for the specified locale.",
            tab = SSOProfileConstants.TAB_ONE)
    private String altLanguageNote;

    @DialogField(
            fieldLabel = "Alternative Language checkbox label",
            name = "./altLanguageCheckboxLabel",
            fieldDescription = "Text to show next the a checkbox that user can check to receive emails in alternative language.<br>"
                    + "E.g.: <i>Send me emails in Spanish</i>",
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "")
    private String altLanguageCheckboxLabel;

    @DialogField(
            fieldLabel = "Alternative Language locale",
            name = "./altLanguageLocale",
            fieldDescription = "Locale to use for the web service call when user selects to receive emails in alternative language "
                    + "(other than the default for the website).<br>"
                    + "E.g.: <i>es-US</i>",
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "")
    private String altLanguageLocale;

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
            fieldLabel = "Subscribe button text",
            name = "./subscribeButtonText",
            fieldDescription = "The Subscribe button text.",
            additionalProperties = @Property(name = "emptyText", value = "Subscribe"),
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "Subscribe")
    private String subscribeButtonText;

    @DialogField(
            fieldLabel = "Page to redirect on success",
            name = "./redirectOnSuccessPage",
            fieldDescription = "Path to the page (or fully qualified URL) where user should be redirected after successful subscription.",
            required = true,
            tab = SSOProfileConstants.TAB_ONE)
    @PathField(rootPath = SSOProfileConstants.CONTENT_ROOT)
    @LinkInject @Optional
    private Link redirectOnSuccessPage;

    @DialogField(
            fieldLabel = "Manage Subscription link title",
            name = "./manageSubscriptionLinkTitle",
            tab = SSOProfileConstants.TAB_ONE,
            additionalProperties = @Property(name = "emptyText", value = "Manage Subscription"))
    @TextField
    @Inject @Default(values = "Manage Subscription")
    private String manageSubscriptionLinkTitle;

    @DialogField(
            fieldLabel = "Profile page path",
            name = "./manageSubscriptionPage",
            fieldDescription = "Path to the profile page (or fully qualified URL) where user should be taken upon clicking the Manage Subscription link.<br/>"
                    + "If field is not configured, Manage Subscription link will not show up.<br/>"
                    + "This field needs to be configured in order to redirect a logged in user to the Profile page.",
            tab = SSOProfileConstants.TAB_ONE)
    @PathField(rootPath = SSOProfileConstants.CONTENT_ROOT)
    @LinkInject @Optional
    private Link manageSubscriptionPage;

    @DialogField(
            fieldLabel = "Unsubscribe link title",
            name = "./unsubscribeLinkTitle",
            tab = SSOProfileConstants.TAB_ONE,
            additionalProperties = @Property(name = "emptyText", value = "Unsubscribe"))
    @TextField
    @Inject @Default(values = "Unsubscribe")
    private String unsubscribeLinkTitle;

    @DialogField(
            fieldLabel = "Unsubscribe page path",
            name = "./unsubscribePage",
            fieldDescription = "Path to the page (or fully qualified URL) where user should be taken upon clicking the Unsubscribe link.<br/>"
                    + "If field is not configured, Unsubscribe link will not show up.",
            tab = SSOProfileConstants.TAB_ONE)
    @PathField(rootPath = SSOProfileConstants.CONTENT_ROOT)
    @LinkInject @Optional
    private Link unsubscribePage;

    @DialogField(
            fieldLabel = "Error 11928",
            name = "./error11928",
            fieldDescription = "Reset password has been initiated for this account and the action has not been completed.",
            tab = SSOProfileConstants.TAB_TWO,
            required = true)
    @TextField
    @Inject @Default(values = "")
    @JsonProperty
    private String error11928;

    @Inject
    private ComponentNode componentNode;

    private boolean directiveContainsRequiredFields;


    public String getNewsletterSubscriptionFormTitle() {
        return newsletterSubscriptionFormTitle;
    }

    public String getNewsletterSubscriptionFormSubtitle() {
        return newsletterSubscriptionFormSubtitle;
    }

    public String getDisclaimerText() {
        return disclaimerText;
    }

    public String getSubscribeButtonText() {
        return subscribeButtonText;
    }

    public String getRedirectOnSuccessHref() {
        return redirectOnSuccessPage != null ? redirectOnSuccessPage.getHref() : "'";
    }

    public String getManageSubscriptionLinkTitle() {
        return manageSubscriptionLinkTitle;
    }

    @JsonProperty
    public String getManageSubscriptionHref() {
        return manageSubscriptionPage != null ? manageSubscriptionPage.getHref() : "";
    }

    public String getUnsubscribeLinkTitle() {
        return unsubscribeLinkTitle;
    }

    public String getUnsubscribeHref() {
        return unsubscribePage != null ? unsubscribePage.getHref() : "";
    }

    public String getDirectiveInputsJson() {
        Directive directiveSubscription = new Directive();
        for (final ComponentNode childNode : componentNode.getComponentNodes()) {

            //we have to have each form field as a separate component - so that it is included when we loop over child nodes
            final Resource childResource = childNode.getResource();
            final String childName = childResource.getName();
            final String childResourceType = childResource.getResourceType();
            final DirectiveInput directiveInput = new DirectiveInput(childNode);

            if (childResourceType.startsWith(SSOProfileConstants.RESOURCE_TEXT_FORM_FIELD_COMPONENT)) {
                final String type;
                switch (childName) {
                    case "email":
                        type = "email";
                        break;
                    default:
                        type = "text";
                }
                directiveInput.setType(type);
            }
            if (directiveInput.getType() != null) {
                directiveSubscription.addInput(directiveInput);
                if (!directiveContainsRequiredFields && directiveInput.isRequired()) {
                    directiveContainsRequiredFields = true;
                }
            }
        }

        //add the disclaimer text and checkbox (if configured)
        if (StringUtils.isNotBlank(disclaimerText)) {
            directiveSubscription.addInput(new DirectiveInput("disclaimer", disclaimerText, "disclaimer"));
        }
        if (StringUtils.isNotBlank(altLanguageLocale) && StringUtils.isNotBlank(altLanguageCheckboxLabel)) {
            directiveSubscription.addInput(
                    new DirectiveInput("localeHeader", altLanguageCheckboxLabel, "checkbox").addValue(altLanguageLocale));
        }

        return directiveSubscription.getJson();
    }

    public String getRequiredFieldsText() {
        return directiveContainsRequiredFields ? requiredFieldsText : "";
    }
}
