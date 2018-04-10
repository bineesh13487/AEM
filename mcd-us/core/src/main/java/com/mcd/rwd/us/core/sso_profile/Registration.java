package com.mcd.rwd.us.core.sso_profile;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Listener;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.Tab;
import com.citytechinc.cq.component.annotations.widgets.DialogFieldSet;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextArea;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.day.cq.i18n.I18n;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.icfolson.aem.library.api.link.Link;
import com.icfolson.aem.library.api.node.ComponentNode;
import com.icfolson.aem.library.api.page.PageDecorator;
import com.icfolson.aem.library.core.constants.ComponentConstants;
import com.icfolson.aem.library.models.annotations.LinkInject;
import com.mcd.rwd.us.core.constants.SSOProfileConstants;
import com.mcd.rwd.us.core.service.McdFactoryConfigConsumer;
import com.mcd.rwd.us.core.sso_profile.common.Address;
import com.mcd.rwd.us.core.sso_profile.common.AddressWithCheckboxes;
import com.mcd.rwd.us.core.sso_profile.common.AgeVerificationField;
import com.mcd.rwd.us.core.sso_profile.common.CheckBoxFormField;
import com.mcd.rwd.us.core.sso_profile.common.PasswordConfirmField;
import com.mcd.rwd.us.core.sso_profile.common.PrivacyAndTC;
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
        value = Registration.COMPONENT_TITLE,
        path = SSOProfileConstants.SSO_COMPONENTS_PATH,
        actions = {
                ComponentConstants.EDIT_BAR_TEXT + Registration.COMPONENT_TITLE,
                ComponentConstants.EDIT_BAR_SPACER,
                ComponentConstants.EDIT_BAR_EDIT + ComponentConstants.EDIT_BAR_ANNOTATE,
                ComponentConstants.EDIT_BAR_COPY_MOVE,
                ComponentConstants.EDIT_BAR_DELETE,
                ComponentConstants.EDIT_BAR_SPACER,
                ComponentConstants.EDIT_BAR_INSERT },
        disableTargeting = true,
        tabs = {
                @Tab(title = "Config"),
                @Tab(title = "Form Fields"),
                @Tab(title = "Form Errors")
        },
        listeners = @Listener(name = ComponentConstants.EVENT_AFTER_EDIT, value = ComponentConstants.REFRESH_PAGE),
        dialogWidth = SSOProfileConstants.DIALOG_DEFAULT_WIDTH,
        dialogHeight = SSOProfileConstants.DIALOG_DEFAULT_HEIGHT
)
@Model(adaptables = { SlingHttpServletRequest.class })
public final class Registration implements Serializable {

    public static final String COMPONENT_TITLE = "Registration";
    private static final Logger LOGGER = LoggerFactory.getLogger(Registration.class);

    private static final int MAX_YEARS = 90;
    private static final int NO_OF_MONTHS = 12;
    private static final int NO_OF_MONTHS_WITH_LEAD_ZERO = 9;

    @DialogField(
            fieldLabel = "Page to redirect on success",
            name = "./redirectPage",
            fieldDescription = "Path to the page (or fully qualified URL) where user should be redirected after successful registration.",
            required = true,
            tab = SSOProfileConstants.TAB_ONE)
    @PathField(rootPath = SSOProfileConstants.CONTENT_ROOT)
    @LinkInject @Optional
    private Link redirectPage;

    //--------------------------

    @DialogField(xtype = "displayfield", fieldLabel = "Registration form - initial view",
            fieldDescription = "Configurations related to the initial view of the registration form.",
            tab = SSOProfileConstants.TAB_ONE)
    private String note1;

    @DialogField(
            fieldLabel = "Title - initial view",
            name = "./titleView1",
            fieldDescription = "Title to include on the initial view of the registration form.",
            additionalProperties = @Property(name = "emptyText", value = "Register"),
            tab = SSOProfileConstants.TAB_ONE
    )
    @TextField
    @Inject @Default(values = "Register")
    private String titleView1;

    @DialogField(
            fieldLabel = "Include Carousel",
            name = "./includeCarousel",
            fieldDescription = "Check this checkbox if Carousel should be included within the initial view of the Registration component.",
            tab = SSOProfileConstants.TAB_ONE
    )
    @Selection(type = "checkbox")
    @Inject @Default(booleanValues = false)
    private boolean includeCarousel;

    @DialogField(
            fieldLabel = "Enable Email Registration",
            name = "./emailRegistrationEnabled",
            fieldDescription = "Check this checkbox if Email Registration should be included on the form.",
            tab = SSOProfileConstants.TAB_ONE,
            listeners = {@Listener(value = "function(comp, value, isChecked) { var dialog = comp.findParentByType(\'dialog\'); "
                    + "var textfield = dialog.getField(\'./emailButtonTitle\');isChecked ? textfield.enable() : textfield.disable();}",
                    name = "selectionchanged")}
    )
    @Selection(type = "checkbox")
    @Inject @Default(booleanValues = false)
    private boolean emailRegistrationEnabled;

    @DialogField(
            fieldLabel = "Email button title",
            name = "./emailButtonTitle",
            fieldDescription = "Email button title for the Registration.",
            additionalProperties = @Property(name = "emptyText", value = "Use Email"),
            tab = SSOProfileConstants.TAB_ONE,
            disabled = true
    )
    @TextField
    @Inject @Default(values = "Use Email")
    private String emailButtonTitle;

    @DialogField(
            fieldLabel = "Enable Facebook Registration",
            name = "./facebookRegistrationEnabled",
            fieldDescription = "Check this checkbox if Facebook Registration should be included on the form.",
            tab = SSOProfileConstants.TAB_ONE,
            listeners = {@Listener(value = "function(comp, value, isChecked) { var dialog = comp.findParentByType(\'dialog\'); "
                    + "var textfield = dialog.getField(\'./socialFacebookButtonTitle\');isChecked ? textfield.enable() : textfield.disable();}",
                    name = "selectionchanged")}
    )
    @Selection(type = "checkbox")
    @Inject @Default(booleanValues = false)
    private boolean facebookRegistrationEnabled;

    @DialogField(
            fieldLabel = "Facebook button title",
            name = "./socialFacebookButtonTitle",
            fieldDescription = "Facebook button title for the Registration.",
            additionalProperties = @Property(name = "emptyText", value = "Use Facebook"),
            tab = SSOProfileConstants.TAB_ONE,
            disabled = true
    )
    @TextField
    @Inject @Default(values = "Use Facebook")
    private String socialFacebookButtonTitle;

    @DialogField(
            fieldLabel = "Enable Google Registration",
            name = "./googleRegistrationEnabled",
            fieldDescription = "Check this checkbox if Google registration should be included on the form.",
            tab = SSOProfileConstants.TAB_ONE,
            listeners = {@Listener(value = "function(comp, value, isChecked) { var dialog = comp.findParentByType(\'dialog\'); "
                    + "var textfield = dialog.getField(\'./socialGoogleButtonTitle\');isChecked ? textfield.enable() : textfield.disable();}",
                    name = "selectionchanged")}
    )
    @Selection(type = "checkbox")
    @Inject @Default(booleanValues = false)
    private boolean googleRegistrationEnabled;

    @DialogField(
            fieldLabel = "Google button title",
            name = "./socialGoogleButtonTitle",
            fieldDescription = "Google button title for the Social registration.",
            additionalProperties = @Property(name = "emptyText", value = "Use Google"),
            tab = SSOProfileConstants.TAB_ONE,
            disabled = true
    )
    @TextField
    @Inject @Default(values = "Use Google")
    private String socialGoogleButtonTitle;

    @DialogField(
            fieldLabel = "Login page path",
            name = "./loginPagePath",
            fieldDescription = "Path to the Login page.",
            tab = SSOProfileConstants.TAB_ONE,
//            ranking = SSOProfileConstants.RANKING_ZERO,
            required = true)
    @PathField(rootPath = SSOProfileConstants.CONTENT_ROOT)
    @LinkInject @Optional
    private Link loginPagePath;

    @DialogField(
            fieldLabel = "Login link title",
            name = "./loginLinkTitle",
            tab = SSOProfileConstants.TAB_ONE,
            additionalProperties = @Property(name = "emptyText", value = "Login")
//            ranking = SSOProfileConstants.RANKING_ZERO,
    )
    @TextField
    @Inject @Default(values = "Login")
    private String loginLinkTitle;

    //--------------------------------

    @DialogField(xtype = "displayfield", fieldLabel = "Required fields view",
            fieldDescription = "Configurations related to the required fields view of the registration form.",
            tab = SSOProfileConstants.TAB_ONE)
    private String note2;

    @DialogField(
            fieldLabel = "Title - required fields view",
            name = "./titleView2",
            fieldDescription = "Title to include on the required fields view of the registration form.",
            additionalProperties = @Property(name = "emptyText", value = "Register"),
            tab = SSOProfileConstants.TAB_ONE
    )
    @TextField
    @Inject @Default(values = "Register")
    private String titleView2;

    @DialogField(
            fieldLabel = "Subtitle",
            name = "./subtitleView2",
            fieldDescription = "Subtitle to include on the required fields view of the registration form when registering with social media account."
                    + "<br/>The required field indicator text will appear at the bottom of the form if it contains required fields."
                    + "<br/>E.g.: <i>We just need a little more...</i>",
            tab = SSOProfileConstants.TAB_ONE
    )
    @TextField
    @Inject @Default(values = "")
    private String subtitleView2;

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
            fieldLabel = "Submit button - required fields",
            name = "./submitButtonView2",
            fieldDescription = "Submit button title for the registration form - required fields view.",
            additionalProperties = @Property(name = "emptyText", value = "Register"),
            tab = SSOProfileConstants.TAB_ONE
//            ranking = SSOProfileConstants.RANKING_ZERO,
    )
    @TextField
    @Inject @Default(values = "Register")
    private String submitButtonView2;

    //---------------------------------

    @DialogField(xtype = "displayfield", fieldLabel = "Optional fields view",
            fieldDescription = "Configurations related to the optional fields view of the registration form.",
            tab = SSOProfileConstants.TAB_ONE)
    private String note3;

    @DialogField(
            fieldLabel = "Title - optional fields view",
            name = "./titleView3",
            fieldDescription = "Title to include on the optional fields view of the registration form.",
            additionalProperties = @Property(name = "emptyText", value = "If you have the time..."),
            tab = SSOProfileConstants.TAB_ONE
    )
    @TextField
    @Inject @Default(values = "If you have the time...")
    private String titleView3;

    @DialogField(
            fieldLabel = "Submit button - optional fields",
            name = "./submitButtonView3",
            fieldDescription = "Submit button title for the optional fields view of the registration form.",
            additionalProperties = @Property(name = "emptyText", value = "Submit"),
            tab = SSOProfileConstants.TAB_ONE)
    @TextField
    @Inject @Default(values = "Submit")
    private String submitButtonView3;

    @DialogField(
            fieldLabel = "Skip link title",
            name = "./skipLinkTitle",
            fieldDescription = "Title of the 'skip' link on the optional fields view of the registration form.",
            tab = SSOProfileConstants.TAB_ONE,
            additionalProperties = @Property(name = "emptyText", value = "Skip")
    )
    @TextField
    @Inject @Default(values = "Skip")
    private String skipLinkTitle;


    //------------------------------------------ form fields -----------------------------------------------------------

    @DialogField (fieldLabel = "Age verification", tab = SSOProfileConstants.TAB_TWO)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "ageVerification/", title = "Age Verification related properties")
    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private AgeVerificationField ageVerification;

    @DialogField (fieldLabel = "Email", tab = SSOProfileConstants.TAB_TWO)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "email/", title = "Email related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private TextFormField email;

    @DialogField (fieldLabel = "Postal Code", tab = SSOProfileConstants.TAB_TWO)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "postalCode/", title = "Postal Code related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private TextFormField postalCode;

    @DialogField (fieldLabel = "Password", tab = SSOProfileConstants.TAB_TWO)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "password/", title = "Password related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private TextFormField password;

    @DialogField (fieldLabel = "Password Confirm", tab = SSOProfileConstants.TAB_TWO)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "passwordConfirm/", title = "Password Confirm related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private PasswordConfirmField passwordConfirm;

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

    @DialogField(xtype = "displayfield", fieldLabel = "Address note",
            fieldDescription = "The <code>required</code> checkbox of the Address Line 1 field determines whether the whole address is placed "
                    + "on the form with required fields or on the form with optional fields.",
            tab = SSOProfileConstants.TAB_TWO)
    private String noteAddress;

    @DialogField (fieldLabel = "Mailing Address", tab = SSOProfileConstants.TAB_TWO, name = "./mailingAddress")
//    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "mailingAddress/", title = "Mailing Address related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private AddressWithCheckboxes mailingAddress;

    @DialogField (fieldLabel = "Mailing Address 2", tab = SSOProfileConstants.TAB_TWO, name = "./mailingAddress2")
//    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "mailingAddress2/", title = "Mailing Address 2 related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private Address mailingAddress2;

    @DialogField (fieldLabel = "Billing Address", tab = SSOProfileConstants.TAB_TWO, name = "./billingAddress")
//    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "billingAddress/", title = "Billing Address related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private Address billingAddress;

    @DialogField (fieldLabel = "Delivery Address", tab = SSOProfileConstants.TAB_TWO, name = "./deliveryAddress")
//    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "deliveryAddress/", title = "Delivery Address related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private Address deliveryAddress;

    @DialogField (fieldLabel = "My CokeRewards Number", tab = SSOProfileConstants.TAB_TWO)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "myCokeRewardsNumber/", title = "My CokeRewards Number related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private TextFormField myCokeRewardsNumber;

    @DialogField (fieldLabel = "Send SMS checkbox", tab = SSOProfileConstants.TAB_TWO)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "sendSMS/", title = "Send SMS checkbox related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private CheckBoxFormField sendSMS;

    @DialogField (fieldLabel = "Email Promotions checkbox", tab = SSOProfileConstants.TAB_TWO)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "emailPromotions/",
            title = "Email Promotions checkbox related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private CheckBoxFormField emailPromotions;

    @DialogField (fieldLabel = "Push Notifications checkbox", tab = SSOProfileConstants.TAB_TWO)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "pushNotifications/",
            title = "Push Notifications checkbox related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private CheckBoxFormField pushNotifications;

    @DialogField(fieldLabel = "Privacy and T&C", tab = SSOProfileConstants.TAB_TWO)
//    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "privacyAndTC/", title = "Privacy and T&C related properties")
//    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private PrivacyAndTC privacyAndTC;


    //tab 3 - error messages

    @DialogField(
            fieldLabel = "Error 11380",
            name = "./error11380",
            fieldDescription = "You are attempting to register a new user, but a user already exists with that email address.",
            tab = SSOProfileConstants.TAB_THREE,
            required = true)
    @TextArea
    @Inject @Default(values = "")
    @JsonProperty
    private String error11380;

    @DialogField(
            fieldLabel = "Error 11820",
            name = "./error11820",
            fieldDescription = "The email address provided is invalid.",
            tab = SSOProfileConstants.TAB_THREE,
            required = true)
    @TextArea
    @Inject @Default(values = "")
    @JsonProperty
    private String error11820;

    @DialogField(
            fieldLabel = "Error 11931",
            name = "./error11931",
            fieldDescription = "The account already exists.",
            tab = SSOProfileConstants.TAB_THREE,
            required = true)
    @TextArea
    @Inject @Default(values = "")
    @JsonProperty
    private String error11931;

    @Inject
    private ComponentNode componentNode;
    @Inject
    private PageDecorator currentPage;
    @Inject
    private SlingHttpServletRequest request;
    @Inject
    private McdFactoryConfigConsumer mcdFactoryConfigConsumer;

    private I18n i18n;
    private Directive directiveRequired = new Directive();
    private Directive directiveOptional = new Directive();


    @PostConstruct
    private void init() {
        try {
            LOGGER.debug("init() start");
            LOGGER.debug("directiveRequired: " + directiveRequired);

            Locale locale = I18nUtil.getLocaleFromConfigLanguageForPage(currentPage, mcdFactoryConfigConsumer);
            LOGGER.debug("locale for current page from configuration: " + locale.getLanguage());

            i18n = new I18n((request.getResourceBundle(locale)));

            populateDirectives();

            LOGGER.debug("init() end");
            LOGGER.debug("directiveRequired: " + directiveRequired);
        } catch (Exception e) {
            LOGGER.error("exception in init {}", e);
        }
    }

    private void populateDirectives() {
        LOGGER.debug("populateDirectives() start");
        LOGGER.debug("directiveRequired: " + directiveRequired);

        //go over child component nodes
        for (final ComponentNode childNode : componentNode.getComponentNodes()) {

            //we have to have each form field as a separate component - so that it is included when we loop over child nodes
            final Resource childResource = childNode.getResource();
            final String childName = childResource.getName();
            final String childResourceType = childResource.getResourceType();

            final boolean included = childNode.get("included", false);
            if (included) {
                final DirectiveInput directiveInput = new DirectiveInput(childNode);

                if (childResourceType.startsWith(SSOProfileConstants.RESOURCE_TEXT_FORM_FIELD_COMPONENT)) {
                    final String type;
                    switch (childName) {
                        case "email":
                            type = "email";
                            break;
                        case "phoneNumber":
                        case "mobileNumber":
                            type = "tel";
                            break;
                        case "password":
                        case "passwordConfirm":
                            type = "password";
                            break;
                        default:
                            type = "text";
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

                    final boolean addressRequired = childNode.getComponentNode("addressLine1").isPresent()
                            && childNode.getComponentNode("addressLine1").get().get("required", false);

                    final Address address = childResource.adaptTo(Address.class);

                    for (final DirectiveInput dirInput : address.getDirectiveInputs()) {
                        if (addressRequired) {
                            directiveRequired.addInput(dirInput);
                        } else {
                            directiveOptional.addInput(dirInput);
                        }
                    }

                    //mailing address checkboxes
                    if (StringUtils.isNotEmpty(childNode.get("labelUseAsBilling", ""))) {
                        if (addressRequired) {
                            directiveRequired.addInput(new DirectiveInput("useAsBillingAddress", childNode.get("labelUseAsBilling", ""), "checkbox"));
                        } else {
                            directiveOptional.addInput(new DirectiveInput("useAsBillingAddress", childNode.get("labelUseAsBilling", ""), "checkbox"));
                        }
                    }
                    if (StringUtils.isNotEmpty(childNode.get("labelUseAsDelivery", ""))) {
                        if (addressRequired) {
                            directiveRequired.addInput(new DirectiveInput("useAsDeliveryAddress", childNode.get("labelUseAsDelivery", ""), "checkbox"));
                        } else {
                            directiveOptional.addInput(new DirectiveInput("useAsDeliveryAddress", childNode.get("labelUseAsDelivery", ""), "checkbox"));
                        }
                    }

                }

                if (directiveInput.getType() != null) {
                    if (childNode.get("required", false)) {
                        //add to the required directive
                        directiveRequired.addInput(directiveInput);
                    } else {
                        //add to the optional directive
                        directiveOptional.addInput(directiveInput);
                    }
                }
            }
            //process Age Verification
            if (childResourceType.equals(SSOProfileConstants.RESOURCE_AGEVERIFICATION_COMPONENT)) {
                final DirectiveInput ageVerificationDirectiveInput = ageVerification.getDirectiveInput();
                if (ageVerificationDirectiveInput != null) {
                    if (ageVerification.isRequired()) {
                        directiveRequired.addInput(ageVerificationDirectiveInput);
                    } else {
                        directiveOptional.addInput(ageVerificationDirectiveInput);
                    }
                }
            }

            //process Privacy and Terms & Conditions
            if (childResourceType.equals(SSOProfileConstants.RESOURCE_PRIVACYANDTC_COMPONENT)) {
                final PrivacyAndTC privacyAndTerms = childResource.adaptTo(PrivacyAndTC.class);
                for (final DirectiveInput dirInput : privacyAndTerms.getDirectiveInputs()) {
                    if (dirInput.isRequired()) {
                        directiveRequired.addInput(dirInput);
                    } else {
                        directiveOptional.addInput(dirInput);
                    }
                }
            }
        }

        LOGGER.debug("populateDirectives() end");
        LOGGER.debug("directiveRequired: " + directiveRequired);
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

    public String getDirectiveRequiredJson() {
        return directiveRequired != null ? directiveRequired.getJson() : "";
    }

    public String getDirectiveOptionalJson() {
        return directiveOptional != null ? directiveOptional.getJson() : "";
    }

    public boolean isIncludeCarousel() {
        return includeCarousel;
    }

    public boolean isEmailRegistrationEnabled() {
        return emailRegistrationEnabled;
    }

    public String getEmailButtonTitle() {
        return emailButtonTitle;
    }

    public boolean isFacebookRegistrationEnabled() {
        return facebookRegistrationEnabled;
    }

    public String getSocialFacebookButtonTitle() {
        return socialFacebookButtonTitle;
    }

    public boolean isGoogleRegistrationEnabled() {
        return googleRegistrationEnabled;
    }

    public String getSocialGoogleButtonTitle() {
        return socialGoogleButtonTitle;
    }

    public String getLoginLinkHref() {
        return loginPagePath != null ? loginPagePath.getHref() : "";
    }

    public String getLoginLinkTitle() {
        return loginLinkTitle;
    }


    public String getTitleView1() {
        return titleView1;
    }

    @JsonProperty
    public String getTitleView2() {
        return titleView2;
    }

    @JsonProperty
    public String getSubtitleView2() {
        return subtitleView2;
    }

    public String getSubmitButtonView2() {
        return submitButtonView2;
    }

    @JsonProperty
    public String getTitleView3() {
        return titleView3;
    }

    public String getSubmitButtonView3() {
        return submitButtonView3;
    }

    @JsonProperty
    public String getAgeVerificationDialogMessage() {
        return ageVerification != null ? ageVerification.getAgeVerificationDialogMessage() : "";
    }

    public String getSkipLinkTitle() {
        return skipLinkTitle;
    }

    @JsonProperty
    public String getRedirectPage() {
        return redirectPage != null ? redirectPage.getHref() : "";
    }

    public String getRequiredFieldsText() {
        return requiredFieldsText;
    }
}
