package com.mcd.rwd.us.core.sso_profile;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Listener;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.Tab;
import com.citytechinc.cq.component.annotations.widgets.DialogFieldSet;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.icfolson.aem.library.api.link.Link;
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
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;

import javax.inject.Inject;

@Component(
        value = NewsletterUnsubscribe.COMPONENT_TITLE,
        path = SSOProfileConstants.SSO_COMPONENTS_PATH,
        actions = {
                ComponentConstants.EDIT_BAR_TEXT + NewsletterUnsubscribe.COMPONENT_TITLE,
                ComponentConstants.EDIT_BAR_SPACER,
                ComponentConstants.EDIT_BAR_EDIT + ComponentConstants.EDIT_BAR_ANNOTATE,
                ComponentConstants.EDIT_BAR_COPY_MOVE,
                ComponentConstants.EDIT_BAR_DELETE,
                ComponentConstants.EDIT_BAR_SPACER,
                ComponentConstants.EDIT_BAR_INSERT },
        disableTargeting = true,
        tabs = {
                @Tab(title = "Newsletter Unsubscribe")
        },
        listeners = @Listener(name = ComponentConstants.EVENT_AFTER_EDIT, value = ComponentConstants.REFRESH_PAGE),
        dialogWidth = SSOProfileConstants.DIALOG_DEFAULT_WIDTH,
        dialogHeight = SSOProfileConstants.DIALOG_DEFAULT_HEIGHT)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class NewsletterUnsubscribe {
    public static final String COMPONENT_TITLE = "Newsletter Unsubscribe";

    @DialogField(
            fieldLabel = "Newsletter Unsubscribe form title",
            name = "./newsletterUnsubscribeFormTitle",
            fieldDescription = "Newsletter Unsubscribe title to show above the form.",
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "")
    private String newsletterUnsubscribeFormTitle;

    @DialogField(
            fieldLabel = "Newsletter Unsubscribe form subtitle",
            name = "./newsletterUnsubscribeFormSubtitle",
            fieldDescription = "Text to show below the Newsletter Unsubscribe form title.",
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "")
    private String newsletterUnsubscribeFormSubtitle;

    @DialogField (fieldLabel = "Email", tab = SSOProfileConstants.TAB_ONE)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "email/", title = "Email related properties")
    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private TextFormFieldNoIncl email;

    @DialogField(
            fieldLabel = "Disclaimer Text",
            name = "./disclaimerText",
            fieldDescription = "Disclaimer text that appears below form fields.<br>"
                    + "E.g.: <i>By unsubscribing you will stop receiving emails from McDonald's.</i>",
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "")
    private String disclaimerText;

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
            fieldLabel = "Unsubscribe button text",
            name = "./unsubscribeButtonText",
            fieldDescription = "The Unsubscribe button text.",
            additionalProperties = @Property(name = "emptyText", value = "Unsubscribe"),
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "Unsubscribe")
    private String unsubscribeButtonText;

    @DialogField(
            fieldLabel = "Page to redirect on success",
            name = "./redirectOnSuccessPage",
            fieldDescription = "Path to the page (or fully qualified URL) where user should be redirected after successful unsubscribing.",
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
                    + "If field is not configured, Manage Subscription link will not show up.",
            tab = SSOProfileConstants.TAB_ONE)
    @PathField(rootPath = SSOProfileConstants.CONTENT_ROOT)
    @LinkInject @Optional
    private Link manageSubscriptionPage;

    private boolean directiveContainsRequiredFields;


    public String getNewsletterUnsubscribeFormTitle() {
        return newsletterUnsubscribeFormTitle;
    }

    public String getNewsletterUnsubscribeFormSubtitle() {
        return newsletterUnsubscribeFormSubtitle;
    }

    public String getUnsubscribeButtonText() {
        return unsubscribeButtonText;
    }

    public String getRedirectOnSuccessHref() {
        return redirectOnSuccessPage != null ? redirectOnSuccessPage.getHref() : "'";
    }

    public String getManageSubscriptionLinkTitle() {
        return manageSubscriptionLinkTitle;
    }

    public String getManageSubscriptionHref() {
        return manageSubscriptionPage != null ? manageSubscriptionPage.getHref() : "";
    }

    public String getDirectiveInputsJson() {
        Directive directiveUnsubscribe = new Directive();
        if (email != null) {
            directiveContainsRequiredFields = email.isRequired();
            directiveUnsubscribe.addInput(
                    new DirectiveInput("email", email.getLabel(), "text")
                        .setRequired(email.isRequired())
                        .addPlaceholder(email.getPlaceholder())
                        .addRequireMsg(email.getRequiredError())
                        .addValidation(email.getValidationRegEx())
                        .addErrorMsg(email.getValidationRegExError())
                        .addDescription(email.getDescription())
                        .addNote(email.getNote())
                    );
        }
        if (StringUtils.isNotBlank(disclaimerText)) {
            directiveUnsubscribe.addInput(new DirectiveInput("disclaimer", disclaimerText, "disclaimer"));
        }

        return directiveUnsubscribe.getJson();
    }

    public String getRequiredFieldsText() {
        return directiveContainsRequiredFields ? requiredFieldsText : "";
    }
}
