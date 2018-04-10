package com.mcd.rwd.us.core.sso_profile;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Listener;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.Tab;
import com.citytechinc.cq.component.annotations.widgets.DialogFieldSet;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.RichTextEditor;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.day.cq.i18n.I18n;
import com.icfolson.aem.library.api.link.Link;
import com.icfolson.aem.library.api.node.ComponentNode;
import com.icfolson.aem.library.api.page.PageDecorator;
import com.icfolson.aem.library.core.constants.ComponentConstants;
import com.icfolson.aem.library.models.annotations.LinkInject;
import com.mcd.rwd.us.core.constants.SSOProfileConstants;
import com.mcd.rwd.us.core.service.McdFactoryConfigConsumer;
import com.mcd.rwd.us.core.sso_profile.common.Address;
import com.mcd.rwd.us.core.sso_profile.common.AddressWithCheckboxes;
import com.mcd.rwd.us.core.sso_profile.common.CheckBoxFormField;
import com.mcd.rwd.us.core.sso_profile.common.CheckBoxFormFieldWithDesc;
import com.mcd.rwd.us.core.sso_profile.common.PasswordConfirmField;
import com.mcd.rwd.us.core.sso_profile.common.SelectFormFieldWithDefaultOption;
import com.mcd.rwd.us.core.sso_profile.common.SelectFormFieldWithOptions;
import com.mcd.rwd.us.core.sso_profile.common.TextFormField;
import com.mcd.rwd.us.core.sso_profile.model.Directive;
import com.mcd.rwd.us.core.sso_profile.model.DirectiveInput;
import com.mcd.rwd.us.core.utils.I18nUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@Component(
        value = Profile.COMPONENT_TITLE,
        path = SSOProfileConstants.SSO_COMPONENTS_PATH,
        actions = {
                ComponentConstants.EDIT_BAR_TEXT + Profile.COMPONENT_TITLE,
                ComponentConstants.EDIT_BAR_SPACER,
                ComponentConstants.EDIT_BAR_EDIT + ComponentConstants.EDIT_BAR_ANNOTATE,
                ComponentConstants.EDIT_BAR_COPY_MOVE,
                ComponentConstants.EDIT_BAR_DELETE,
                ComponentConstants.EDIT_BAR_SPACER,
                ComponentConstants.EDIT_BAR_INSERT },
        disableTargeting = true,
        tabs = {
                @Tab(title = "Profile"),
                @Tab(title = "Personal Details"),
                @Tab(title = "Settings"),
                @Tab(title = "Communications")
        },
        listeners = @Listener(name = ComponentConstants.EVENT_AFTER_EDIT, value = ComponentConstants.REFRESH_PAGE),
        dialogWidth = SSOProfileConstants.DIALOG_DEFAULT_WIDTH,
        dialogHeight = SSOProfileConstants.DIALOG_DEFAULT_HEIGHT)
@Model(adaptables = SlingHttpServletRequest.class)
public class Profile {

    public static final String COMPONENT_TITLE = "Profile";
    private static final Logger LOGGER = LoggerFactory.getLogger(Profile.class);

    private static final int MAX_YEARS = 90;
    private static final int NO_OF_MONTHS = 12;
    private static final int NO_OF_MONTHS_WITH_LEAD_ZERO = 9;

    @DialogField(
            fieldLabel = "Profile section title",
            name = "./profileSectionTitle",
            fieldDescription = "Title to show within the initial view of the Profile component.",
            additionalProperties = @Property(name = "emptyText", value = "Profile"),
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "Profile")
    private String profileSectionTitle;

    @DialogField(
            fieldLabel = "Personal Details link title",
            name = "./linkTitlePersonalDetails",
            fieldDescription = "Title of the Personal Details link.",
            additionalProperties = @Property(name = "emptyText", value = "Personal Details"),
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "Personal Details")
    private String linkTitlePersonalDetails;

    @DialogField(
            fieldLabel = "Settings link title",
            name = "./linkTitleSettings",
            fieldDescription = "Title of the Settings link.",
            additionalProperties = @Property(name = "emptyText", value = "Settings"),
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "Settings")
    private String linkTitleSettings;

    @DialogField(
            fieldLabel = "Communications link title",
            name = "./linkTitleCommunications",
            fieldDescription = "Title of the Communications link.",
            additionalProperties = @Property(name = "emptyText", value = "Communications"),
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "Communications")
    private String linkTitleCommunications;

    @DialogField(
            fieldLabel = "Login page path",
            name = "./loginPagePath",
            fieldDescription = "Path to the Login page.",
            tab = SSOProfileConstants.TAB_ONE,
            required = true)
    @PathField(rootPath = SSOProfileConstants.CONTENT_ROOT)
    @LinkInject(titleProperty = "loginPagePath") @Optional
    private Link loginPagePath;

    @DialogField(
            fieldLabel = "Logout link title",
            name = "./logoutLinkTitle",
            fieldDescription = "Title of the Logout link.",
            additionalProperties = @Property(name = "emptyText", value = "Logout"),
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "Logout")
    private String logoutLinkTitle;

    @DialogField(
            fieldLabel = "Required Field Text",
            name = "./requiredFieldsText",
            fieldDescription = "Required field indicator text. This text will explain to the user that fields marked with asterisk are required."
                    + "<br/>The required field indicator text will appear at the bottom of every form that contains required fields."
                    + "<br/>E.g.: <i>- required field</i>",
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "")
    private String requiredFieldsText;

    //tab 2 - Personal Details

    @DialogField(
            fieldLabel = "Personal Details section title",
            name = "./personalDetailsSectionTitle",
            fieldDescription = "Title of the Personal Details section within the Profile component.",
            additionalProperties = @Property(name = "emptyText", value = "Personal Details"),
            tab = SSOProfileConstants.TAB_TWO
    )
    @TextField
    @Inject @Default(values = "Personal Details")
    private String personalDetailsSectionTitle;

    @DialogField (fieldLabel = "Email", tab = SSOProfileConstants.TAB_TWO)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "email/", title = "Email related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private TextFormField email;

    @DialogField (fieldLabel = "Prefix", tab = SSOProfileConstants.TAB_TWO)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "prefix/", title = "Prefix related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private SelectFormFieldWithOptions prefix;

    @DialogField (fieldLabel = "First Name", tab = SSOProfileConstants.TAB_TWO)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "firstName/", title = "First Name related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private TextFormField firstName;

    @DialogField (fieldLabel = "Last Name", tab = SSOProfileConstants.TAB_TWO)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "lastName/", title = "Last Name related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private TextFormField lastName;

    @DialogField (fieldLabel = "Suffix", tab = SSOProfileConstants.TAB_TWO)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "suffix/", title = "Suffix related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private SelectFormFieldWithOptions suffix;

    @DialogField (fieldLabel = "Age Range", tab = SSOProfileConstants.TAB_TWO)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "ageRange/", title = "Age Range related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private SelectFormFieldWithOptions ageRange;

    @DialogField (fieldLabel = "Birth Month", tab = SSOProfileConstants.TAB_TWO)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "birthMonth/", title = "Birth Month related properties")
    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private SelectFormFieldWithDefaultOption birthMonth;

    @DialogField (fieldLabel = "Birth Year", tab = SSOProfileConstants.TAB_TWO)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "birthYear/", title = "Birth Year related properties")
    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private SelectFormFieldWithDefaultOption birthYear;

    @DialogField (fieldLabel = "Phone Number", tab = SSOProfileConstants.TAB_TWO)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "phoneNumber/", title = "Phone Number related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private TextFormField phoneNumber;

    @DialogField (fieldLabel = "Mobile Number", tab = SSOProfileConstants.TAB_TWO)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "mobileNumber/", title = "Mobile Number related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private TextFormField mobileNumber;

    @DialogField (fieldLabel = "Gender", tab = SSOProfileConstants.TAB_TWO)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "gender/", title = "Gender related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private SelectFormFieldWithOptions gender;

    @DialogField (fieldLabel = "Mailing Address", tab = SSOProfileConstants.TAB_TWO, name = "./mailingAddress")
//    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "mailingAddress11/", title = "Mailing Address 11 related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private AddressWithCheckboxes mailingAddress;

    @DialogField (fieldLabel = "Mailing Address 2", tab = SSOProfileConstants.TAB_TWO, name = "./mailingAddress2")
//    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "mailingAddress12/", title = "Mailing Address 12 related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private Address mailingAddress2;

    @DialogField (fieldLabel = "Delivery Address", tab = SSOProfileConstants.TAB_TWO, name = "./deliveryAddress")
//    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "deliveryAddress1/", title = "Delivery Address 1 related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private Address deliveryAddress;

    @DialogField (fieldLabel = "Billing Address", tab = SSOProfileConstants.TAB_TWO, name = "./billingAddress")
//    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "billingAddress1/", title = "Billing Address 1 related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private Address billingAddress;

    @DialogField (fieldLabel = "My CokeRewards Number", tab = SSOProfileConstants.TAB_TWO)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "myCokeRewardsNumber/", title = "My CokeRewards Number related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private TextFormField myCokeRewardsNumber;

    @DialogField(
            fieldLabel = "Update button text",
            name = "./profileDetailsUpdateButtonText",
            fieldDescription = "The update button text in Profile Details form.",
            additionalProperties = @Property(name = "emptyText", value = "Update"),
            tab = SSOProfileConstants.TAB_TWO)
    @TextField
    @Inject @Default(values = "Update")
    private String profileDetailsUpdateButtonText;

    //tab 3 - Settings

    @DialogField(
            fieldLabel = "Settings section title",
            name = "./settingsSectionTitle",
            fieldDescription = "Title of the Settings section within the Profile component.",
            additionalProperties = @Property(name = "emptyText", value = "Settings"),
            tab = SSOProfileConstants.TAB_THREE
    )
    @TextField
    @Inject @Default(values = "Settings")
    private String settingsSectionTitle;

    @DialogField(
            fieldLabel = "Account Verification message",
            name = "./accountVerificationMessage",
            fieldDescription = "Message to show to the user that their email address was updated but it needs verification.",
            additionalProperties = @Property(name = "emptyText", value = ""),
            tab = SSOProfileConstants.TAB_THREE)
    @RichTextEditor
    @Inject @Default(values = "")
    private String accountVerificationMessage;

    @DialogField(
            fieldLabel = "Send Verification Email button text",
            name = "./sendVerificationEmailButtonText",
            fieldDescription = "Send Verification Email button text within the Settings section of the Profile.",
            additionalProperties = @Property(name = "emptyText", value = "Resend Email Verification Link"),
            tab = SSOProfileConstants.TAB_THREE)
    @TextField
    @Inject @Default(values = "Resend Email Verification Link")
    private String sendVerificationEmailButtonText;

    @DialogField(
            fieldLabel = "Update Password link title",
            name = "./updatePasswordLinkTitle",
            additionalProperties = @Property(name = "emptyText", value = "Update Password"),
            tab = SSOProfileConstants.TAB_THREE)
    @TextField
    @Inject @Default(values = "Update Password")
    private String updatePasswordLinkTitle;

    @DialogField(
            fieldLabel = "Enable Deactivate Account",
            name = "./deleteAccountEnabled",
            fieldDescription = "Check this checkbox if Deactivate Account option should be included in the Settings section of Profile.",
            tab = SSOProfileConstants.TAB_THREE)
    @Selection(type = "checkbox")
    @Inject @Default(booleanValues = false)
    private boolean deleteAccountEnabled;

    @DialogField(
            fieldLabel = "Deactivate Account link title",
            name = "./deleteAccountLinkTitle",
            additionalProperties = @Property(name = "emptyText", value = "Delete Account"),
            tab = SSOProfileConstants.TAB_THREE)
    @TextField
    @Inject @Default(values = "Delete Account")
    private String deleteAccountLinkTitle;

    //tab3 Update Password form

    @DialogField(xtype = "displayfield", hideLabel = true,
            fieldDescription = "<hr/>Configurations related to the Update Password form.<hr/>",
            tab = SSOProfileConstants.TAB_THREE)
    private String noteUpdatePassword;

    @DialogField(
            fieldLabel = "Update Password form title",
            name = "./updatePasswordFormTitle",
            fieldDescription = "Update Password title to show above the form.",
            additionalProperties = @Property(name = "emptyText", value = "Update Password"),
            tab = SSOProfileConstants.TAB_THREE)
    @TextField
    @Inject @Default(values = "Update Password")
    private String updatePasswordFormTitle;

    @DialogField(
            fieldLabel = "Submit button text",
            name = "./updatePasswordSubmitButtonText",
            fieldDescription = "The submit button text in Update Password form.",
            additionalProperties = @Property(name = "emptyText", value = "Submit"),
            tab = SSOProfileConstants.TAB_THREE)
    @TextField
    @Inject @Default(values = "Submit")
    private String updatePasswordSubmitButtonText;

    @DialogField(
            fieldLabel = "Redirect Page after password update",
            name = "./redirectAfterUpdatePassword",
            fieldDescription = "Path for the page (or fully qualified URL) to redirect to after updating password.",
            tab = SSOProfileConstants.TAB_THREE,
            required = true)
    @PathField(rootPath = SSOProfileConstants.CONTENT_ROOT)
    @LinkInject @Optional
    private Link redirectAfterUpdatePassword;

    @DialogField (fieldLabel = "Current Password", tab = SSOProfileConstants.TAB_THREE)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "currentPassword/", title = "Current Password related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private TextFormField currentPassword;

    @DialogField (fieldLabel = "Password", tab = SSOProfileConstants.TAB_THREE)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "password/", title = "Password related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private TextFormField password;

    @DialogField (fieldLabel = "Confirm Password", tab = SSOProfileConstants.TAB_THREE)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "confirmPassword/", title = "Confirm Password related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
//    private TextFormField confirmPassword;
    private PasswordConfirmField confirmPassword;

//    @DialogField(
//            fieldLabel = "Error XXXXX",
//            name = "./errorXXXXX",
//            fieldDescription = "Current password was supplied, but the current password is wrong.",
//            tab = SSOProfileConstants.TAB_THREE,
//            required = true
////            ranking = SSOProfileConstants.RANKING_ZERO,
//    )
//    @TextArea
//    @Inject @Default(values = "")
//    @JsonProperty
//    private String errorXXXXX;

    //tab3 Delete Account form

    @DialogField(xtype = "displayfield", hideLabel = true,
            fieldDescription = "<hr/>Configurations related to the Deactivate Account form.<hr/>",
            tab = SSOProfileConstants.TAB_THREE)
    private String noteDeleteAccount;

    @DialogField(
            fieldLabel = "Deactivate Account form title",
            name = "./deleteAccountFormTitle",
            fieldDescription = "Deactivate Account title to show above the form.",
            additionalProperties = @Property(name = "emptyText", value = "Delete Account"),
            tab = SSOProfileConstants.TAB_THREE)
    @TextField
    @Inject @Default(values = "Delete Account")
    private String deleteAccountFormTitle;

    @DialogField(
            fieldLabel = "Deactivate Account subtitle",
            name = "./deleteAccountSubtitle",
            fieldDescription = "Subtitle to show below the form title.<br/>"
                    + "E.g.: <i>Are you sure?</i>",
            tab = SSOProfileConstants.TAB_THREE)
    @TextField
    @Inject @Default(values = "")
    private String deleteAccountSubtitle;

    @DialogField(
            fieldLabel = "Deactivate Account Message",
            name = "./deleteAccountMessage",
            fieldDescription = "Message to show below the deactivate form subtitle.<br/>"
                    + "E.g.: <i>Be careful, this cannot be undone!</i>",
            tab = SSOProfileConstants.TAB_THREE)
    @TextField
    @Inject @Default(values = "")
    private String deleteAccountMessage;

    @DialogField (fieldLabel = "Deactivate Account Data checkbox", tab = SSOProfileConstants.TAB_THREE)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "deleteAccountData/",
            title = "Deactivate Account Data checkbox related properties")
    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private CheckBoxFormField deleteAccountData;

    @DialogField(
            fieldLabel = "Deactivate Account button text",
            name = "./deleteAccountButtonText",
            fieldDescription = "The deactivate account button text in deactivate account form.",
            additionalProperties = @Property(name = "emptyText", value = "Delete Account"),
            tab = SSOProfileConstants.TAB_THREE)
    @TextField
    @Inject @Default(values = "Delete Account")
    private String deleteAccountButtonText;

    @DialogField(
            fieldLabel = "Cancel link title",
            name = "./cancelLinkTitle",
            fieldDescription = "Title of the Cancel link in Delete Account form.",
            additionalProperties = @Property(name = "emptyText", value = "Cancel"),
            tab = SSOProfileConstants.TAB_THREE)
    @TextField
    @Inject @Default(values = "Cancel")
    private String cancelLinkTitle;

    @DialogField(
            fieldLabel = "Redirect Page after account delete",
            name = "./redirectAfterDeletePath",
            fieldDescription = "Path for the page (or fully qualified URL) to redirect to after deleting the account.",
            tab = SSOProfileConstants.TAB_THREE,
            required = true)
    @PathField(rootPath = SSOProfileConstants.CONTENT_ROOT)
    @LinkInject @Optional
    private Link redirectAfterDeletePath;


    //tab 4 - Communications

    @DialogField(
            fieldLabel = "Communications section title",
            name = "./communicationSectionTitle",
            fieldDescription = "Title of the Communications section within the Profile component.",
            additionalProperties = @Property(name = "emptyText", value = "Communications"),
            tab = SSOProfileConstants.TAB_FOUR)
    @TextField
    @Inject @Default(values = "Communications")
    private String communicationSectionTitle;


    @DialogField (fieldLabel = "Send SMS checkbox", tab = SSOProfileConstants.TAB_FOUR)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "sendSMS/", title = "Send SMS checkbox related properties")
    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private CheckBoxFormFieldWithDesc sendSMS;

    @DialogField (fieldLabel = "Email Promotions checkbox", tab = SSOProfileConstants.TAB_FOUR)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "emailPromotions/",
            title = "Email Promotions checkbox related properties")
    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private CheckBoxFormFieldWithDesc emailPromotions;

    @DialogField (fieldLabel = "Push Notifications checkbox", tab = SSOProfileConstants.TAB_FOUR)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "pushNotifications/",
            title = "Push notifications checkboxrelated properties")
    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private CheckBoxFormFieldWithDesc pushNotifications;

    @DialogField(
            fieldLabel = "Submit button title",
            name = "./communicationsFormSubmitButtonTitle",
            fieldDescription = "Submit button title for the communications form.",
            additionalProperties = @Property(name = "emptyText", value = "Submit"),
            tab = SSOProfileConstants.TAB_FOUR)
    @TextField
    @Inject @Default(values = "Submit")
    private String communicationsFormSubmitButtonTitle;


    @Inject
    private PageDecorator currentPage;
    @Inject
    private ComponentNode componentNode;
    @Inject
    private SlingHttpServletRequest request;
    @Inject
    private McdFactoryConfigConsumer mcdFactoryConfigConsumer;

    private I18n i18n;
    private Directive directiveUpdatePassword = new Directive();
    private Directive directiveProfile = new Directive();
    private Directive directiveCommunications = new Directive();

    @PostConstruct
    public void init() {

        try {
            LOGGER.debug("init() start");

            Locale locale = I18nUtil.getLocaleFromConfigLanguageForPage(currentPage, mcdFactoryConfigConsumer);
            LOGGER.debug("locale for current page from configuration: " + locale.getLanguage());
            i18n = new I18n((request.getResourceBundle(locale)));

            populateDirectives();

            LOGGER.debug("init() end");
        } catch (Exception e) {
            LOGGER.error("exception in init {}", e);
        }
    }

    private void populateDirectives() {

        LOGGER.debug("populateDirectives() start");

        for (final ComponentNode childNode : componentNode.getComponentNodes()) {

            final Resource childResource = childNode.getResource();
            final String childName = childResource.getName();
            final String childResourceType = childResource.getResourceType();

            final boolean included = childNode.get("included", false);
            if (included) {
                final DirectiveInput directiveInput = new DirectiveInput(childNode);
                if (childResourceType.startsWith(SSOProfileConstants.RESOURCE_TEXT_FORM_FIELD_COMPONENT)) {
                    String type;
                    switch (childName) {
                        case "email":
                            type = "email";
                            break;
                        case "phoneNumber":
                        case "mobileNumber":
                            type = "tel";
                            break;
                        case "currentPassword":
                        case "password":
                        case "confirmPassword":
                            type = "password";
                            break;
                        default:
                            type = "text";
                            break;
                    }
                    directiveInput.setType(type);

                } else if (childResourceType.startsWith(SSOProfileConstants.RESOURCE_SELECT_FORM_FIELD_COMPONENT)) {
                    directiveInput.setType("select");
                    if (childName.equals("birthYear")) {
                        populateYears(directiveInput);
                    } else if (childName.equals("birthMonth")) {
                        populateMonths(directiveInput);
                    } else {
                        List<String> optionValues = childNode.getAsList("options", String.class);
                        for (final String option : optionValues) {
                            LOGGER.debug("option: " + option);
                            directiveInput.addOption(StringUtils.substringAfter(option, "::"), StringUtils.substringBefore(option, "::"));
                        }
                    }

                } else if (childResourceType.startsWith(SSOProfileConstants.RESOURCE_CHECK_BOX_FORM_FIELD_COMPONENT)) {
                    directiveInput.setType("checkbox");

                } else if (childResourceType.equals(SSOProfileConstants.RESOURCE_ADDRESS_COMPONENT)) {
                    final Address address = childResource.adaptTo(Address.class);

                    for (final DirectiveInput dirInput : address.getDirectiveInputs()) {
                        directiveProfile.addInput(dirInput);
                    }

                    //mailing address checkboxes
                    if (StringUtils.isNotEmpty(childNode.get("labelUseAsBilling", ""))) {
                        directiveProfile.addInput(new DirectiveInput("useAsBillingAddress", childNode.get("labelUseAsBilling", ""), "checkbox"));
                    }
                    if (StringUtils.isNotEmpty(childNode.get("labelUseAsDelivery", ""))) {
                        directiveProfile.addInput(new DirectiveInput("useAsDeliveryAddress", childNode.get("labelUseAsDelivery", ""), "checkbox"));
                    }
                }

                if (directiveInput.getType() != null) {
                    if (directiveInput.getType().equals("password")) {
                        directiveUpdatePassword.addInput(directiveInput);
                    } else if (directiveInput.getName().equals("sendSMS")
                            || directiveInput.getName().equals("emailPromotions")
                            || directiveInput.getName().equals("pushNotifications")) {
                        directiveCommunications.addInput(directiveInput);
                    } else if (!directiveInput.getName().equals("deleteAccountData")) {
                        directiveProfile.addInput(directiveInput);
                    }
                }
            }
        }
    }

    private void populateYears(final DirectiveInput directiveInput) {
        directiveInput.addOption(birthYear != null ? birthYear.getDefaultOptionLabel() : "Select", "");
        final int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        for (int i = 0; i < MAX_YEARS; i++) {
            directiveInput.addOption(String.valueOf(currentYear - i), String.valueOf(currentYear - i));
        }
    }

    private void populateMonths(final DirectiveInput directiveInput) {
        directiveInput.addOption(birthMonth != null ? birthMonth.getDefaultOptionLabel() : "Select", "");
        String key;
        for (int i = 1; i <= NO_OF_MONTHS; i++) {
            if (i <= NO_OF_MONTHS_WITH_LEAD_ZERO) {
                key = "month0" + i;
            } else {
                key = "month" + i;
            }
            directiveInput.addOption(i18n.get(key), String.format("%02d", i));
        }
    }

    public String getDirectiveUpdatePassword() {
        return directiveUpdatePassword != null ? directiveUpdatePassword.getJson() : "";
    }

    public String getDirectiveProfile() {
        return directiveProfile != null ? directiveProfile.getJson() : "";
    }

    public String getDirectiveCommunications() {
        return directiveCommunications != null ? directiveCommunications.getJson() : "";
    }

    public String getProfileSectionTitle() {
        return profileSectionTitle;
    }

    public String getLinkTitlePersonalDetails() {
        return linkTitlePersonalDetails;
    }

    public String getLinkTitleSettings() {
        return linkTitleSettings;
    }

    public String getLinkTitleCommunications() {
        return linkTitleCommunications;
    }

    public String getLoginLinkHref() {
        return loginPagePath != null ? loginPagePath.getHref() : "";
    }

    public String getLogoutLinkTitle() {
        return logoutLinkTitle;
    }

    public String getPersonalDetailsSectionTitle() {
        return personalDetailsSectionTitle;
    }

    public String getProfileDetailsUpdateButtonText() {
        return profileDetailsUpdateButtonText;
    }

    public String getSettingsSectionTitle() {
        return settingsSectionTitle;
    }

    public String getAccountVerificationMessage() {
        return accountVerificationMessage;
    }

    public String getSendVerificationEmailButtonText() {
        return sendVerificationEmailButtonText;
    }

    public String getUpdatePasswordLinkTitle() {
        return updatePasswordLinkTitle;
    }

    public boolean isDeleteAccountEnabled() {
        return deleteAccountEnabled;
    }

    public String getDeleteAccountLinkTitle() {
        return deleteAccountLinkTitle;
    }

    public String getUpdatePasswordFormTitle() {
        return updatePasswordFormTitle;
    }

    public String getUpdatePasswordSubmitButtonText() {
        return updatePasswordSubmitButtonText;
    }

    public String getRedirectAfterUpdatePasswordHref() {
        return redirectAfterUpdatePassword != null ? redirectAfterUpdatePassword.getHref() : "";
    }

    public String getDeleteAccountFormTitle() {
        return deleteAccountFormTitle;
    }

    public String getDeleteAccountSubtitle() {
        return deleteAccountSubtitle;
    }

    public String getDeleteAccountMessage() {
        return deleteAccountMessage;
    }

    public CheckBoxFormField getDeleteAccountData() {
        return deleteAccountData;
    }

    public String getRedirectAfterDeleteHref() {
        return redirectAfterDeletePath != null ? redirectAfterDeletePath.getHref() : "";
    }

    public String getDeleteAccountButtonText() {
        return deleteAccountButtonText;
    }

    public String getCancelLinkTitle() {
        return cancelLinkTitle;
    }

    public String getCommunicationSectionTitle() {
        return communicationSectionTitle;
    }

    public String getCommunicationsFormSubmitButtonTitle() {
        return communicationsFormSubmitButtonTitle;
    }

    public String getRequiredFieldsText() {
        return requiredFieldsText;
    }

    public boolean isDirectiveProfileHasRequiredFields() {
        return directiveProfile.includesRequiredFields();
    }

    public boolean isDirectiveUpdatePasswordHasRequiredFields() {
        return directiveUpdatePassword.includesRequiredFields();
    }

    public boolean isSendSMSIncluded() {
        return sendSMS != null && sendSMS.isIncluded();
    }

    public CheckBoxFormFieldWithDesc getSendSMS() {
        return sendSMS;
    }

    public boolean isEmailPromotionsIncluded() {
        return emailPromotions != null && emailPromotions.isIncluded();
    }

    public CheckBoxFormFieldWithDesc getEmailPromotions() {
        return emailPromotions;
    }

    public boolean isPushNotificationsIncluded() {
        return pushNotifications != null && pushNotifications.isIncluded();
    }

    public CheckBoxFormFieldWithDesc getPushNotifications() {
        return pushNotifications;
    }
}
