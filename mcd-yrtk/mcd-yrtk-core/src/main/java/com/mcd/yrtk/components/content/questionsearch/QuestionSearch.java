package com.mcd.yrtk.components.content.questionsearch;


import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.Tab;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextArea;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.icfolson.aem.library.api.link.Link;
import com.icfolson.aem.library.api.node.ComponentNode;
import com.icfolson.aem.library.api.page.PageDecorator;
import com.icfolson.aem.library.core.constants.ComponentConstants;
import com.icfolson.aem.library.models.annotations.LinkInject;
import com.mcd.yrtk.YRTKConstants;
import com.mcd.yrtk.components.content.Serializable;
import com.mcd.yrtk.components.content.YRTKServiceEnabled;
import com.mcd.yrtk.service.YRTKWebServicesConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

@Component(value = QuestionSearch.COMPONENT_TITLE,
        actions = {
                ComponentConstants.EDIT_BAR_TEXT + QuestionSearch.COMPONENT_TITLE,
                ComponentConstants.EDIT_BAR_SPACER,
                ComponentConstants.EDIT_BAR_EDIT + ComponentConstants.EDIT_BAR_ANNOTATE,
                ComponentConstants.EDIT_BAR_COPY_MOVE,
                ComponentConstants.EDIT_BAR_DELETE},
        disableTargeting = true,
        group = "YRTK",
        tabs = {@Tab(title = "Question Search"), @Tab(title = "New Question Modal"),
                @Tab(title = "Social Login"), @Tab(title = "Success Modal"), @Tab(title = "Errors")},
        dialogWidth = YRTKConstants.DIALOG_DEFAULT_WIDTH,
        dialogHeight = YRTKConstants.DIALOG_DEFAULT_HEIGHT)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class },
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public final class QuestionSearch implements Serializable, YRTKServiceEnabled {

    public static final String COMPONENT_TITLE = "Question Search";

    private static final String LINK_FORMAT =
            "<a href=\"{{href}}\" title=\"{{title}}\" target=\"_blank\">{{title}}</a>";
    private static final String PROP_TERMS_CONDITIONS_PAGE_PATH = "termsAndConditionsPage";

    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionSearch.class);

    private String countryCode;
    private String languageCode;

    @Inject
    private YRTKWebServicesConfig yrtkWebServicesConfig;
    @Inject
    private ComponentNode componentNode;
    @Inject
    private PageDecorator currentPage;


    //tab 1 properties - Question Search

    @DialogField(fieldLabel = "Question Search Title",
            fieldDescription = "Question Search title. This text is rendered as a H1 tag.",
            additionalProperties = @Property(name = "emptyText", value = "Got a question? Get an answer."))
    @TextField
    @Inject @Default(values = "Got a question? Get an answer.")
    private String title;

    @DialogField(fieldLabel = "Question Search Subtitle",
            fieldDescription = "Question Search subtitle. This text is displayed right below the title.<br/>"
                    + "E.g.: <i>Curious about McDonald's in the GCC? Ask us anything about our brand and food, "
                    + "and get factual responses.</i>")
    @TextField
    @Inject @Default(values = "")
    private String subtitle;

    @DialogField(fieldLabel = "Search Box Placeholder", fieldDescription = "Placeholder for the search box",
            additionalProperties = @Property(name = "emptyText", value = "Search for a question"))
    @TextField
    @Inject @Default(values = "Search for a question")
    @JsonProperty
    private String searchBoxPlaceholder;

    @DialogField(fieldLabel = "Search button text",
            fieldDescription = "Text on the button to search for questions.",
            additionalProperties = @Property(name = "emptyText", value = "Go"))
    @TextField
    @Inject @Default(values = "Go")
    @JsonProperty
    private String searchBtnText;

    @DialogField(fieldLabel = "Ask question button text",
            fieldDescription = "Text on the button to ask a new question.",
            additionalProperties = @Property(name = "emptyText", value = "Ask a question"))
    @TextField
    @Inject @Default(values = "Ask a question")
    @JsonProperty
    private String questionBtnText;

    @DialogField(fieldLabel = "Question Suggestions text",
            fieldDescription = "Text that is displayed on top of type-ahead suggestions (Top Questions).",
            additionalProperties = @Property(name = "emptyText", value = "TOP QUESTIONS"))
    @TextField
    @Inject @Default(values = "TOP QUESTIONS")
    @JsonProperty
    private String suggestionBtn1;

    @DialogField(fieldLabel = "Question Suggestions button text",
            fieldDescription = "Button to view all the suggestions.",
            additionalProperties = @Property(name = "emptyText", value = "View all questions"))
    @TextField
    @Inject @Default(values = "View all questions")
    @JsonProperty
    private String suggestionBtn2;

    @DialogField(fieldLabel = "Ask question help text",
            fieldDescription = "Text next to 'Ask question' button in search suggestion dropdown.",
            additionalProperties = @Property(name = "emptyText", value = "Can't find what you're looking for?"))
    @TextField
    @Inject @Default(values = "Can't find what you're looking for?")
    private String questionHelpText;


    //tab 2 properties - New Question Modal

    @DialogField(fieldLabel = "Question input box title", fieldDescription = "Title for input question box",
            additionalProperties = @Property(name = "emptyText", value = "What's your question?"),
            tab = YRTKConstants.TAB_TWO)
    @TextField
    @Inject @Default(values = "What's your question?")
    private String questionTitle;

    @DialogField(fieldLabel = "Question input box placeholder",
            fieldDescription = "Placeholder text for Question box",
            additionalProperties = @Property(name = "emptyText",
                    value = "How do you make your chicken nuggets so crispy?"), tab = YRTKConstants.TAB_TWO)
    @TextField
    @Inject @Default(values = "How do you make your chicken nuggets so crispy?") //TODO is the default value accurate?
    private String questionPlaceholder;

    @DialogField(fieldLabel = "Question input - required message",
            fieldDescription = "Error message (required field) for the question input field.",
            additionalProperties = @Property(name = "emptyText", value = "Please ask your question."),
            tab = YRTKConstants.TAB_TWO)
    @TextField
    @Inject @Default(values = "Please ask your question.")
    @JsonProperty
    private String questionErrorRequired;

//    @DialogField(fieldLabel = "Name input box label", fieldDescription = "Name input box label",
//            additionalProperties = @Property(name = "emptyText", value = "What's your name?"),
//            tab = YRTKConstants.TAB_TWO)
//    @TextField @Default(values = "What's your name?")
//    @Inject
//    private String nameTitle;

    @DialogField(fieldLabel = "Name input box placeholder text",
            fieldDescription = "Name input box placeholder text",
            additionalProperties = @Property(name = "emptyText", value = "What's your name?"),
            tab = YRTKConstants.TAB_TWO)
    @TextField
    @Inject @Default(values = "What's your name?")
    private String namePlaceholder;

    @DialogField(fieldLabel = "Name input - required message",
            fieldDescription = "Error message (required field) for the name input field.",
            additionalProperties = @Property(name = "emptyText", value = "Please provide your name."),
            tab = YRTKConstants.TAB_TWO)
    @TextField
    @Inject @Default(values = "Please provide your name.")
    @JsonProperty
    private String nameErrorRequired;

    @DialogField(fieldLabel = "Name input - format regex",
            fieldDescription = "Regular expression (regex) to check validity of the name input field. If no value is "
                    + "provided, format of the filed value will not be checked.",
            tab = YRTKConstants.TAB_TWO)
    @TextField
    @Inject @Default(values = "")
    @JsonProperty
    private String nameRegEx;

    @DialogField(fieldLabel = "Name input - invalid message",
            fieldDescription = "Error message (invalid field) for the name input field.",
            additionalProperties = @Property(name = "emptyText", value = "The name entered is not valid."),
            tab = YRTKConstants.TAB_TWO)
    @TextField
    @Inject @Default(values = "The name entered is not valid.")
    @JsonProperty
    private String nameErrorInvalid;

//    @DialogField(fieldLabel = "Email input box title", fieldDescription = "Email input box title",
//            additionalProperties = @Property(name = "emptyText", value = "What's your email?"),
//            tab = YRTKConstants.TAB_TWO)
//    @TextField @Default(values = "What's your email?")
//    @Inject
//    private String emailTitle;

    @DialogField(fieldLabel = "Email input box placeholder",
            fieldDescription = "Email input box placeholder",
            additionalProperties = @Property(name = "emptyText", value = "What's your email?"),
            tab = YRTKConstants.TAB_TWO)
    @TextField
    @Inject @Default(values = "What's your email?")
    private String emailPlaceholder;

    @DialogField(fieldLabel = "Email input - required message",
            fieldDescription = "Error message (required field) for the email input field.",
            additionalProperties = @Property(name = "emptyText", value = "Please provide your email."),
            tab = YRTKConstants.TAB_TWO)
    @TextField
    @Inject @Default(values = "Please provide your email.")
    @JsonProperty
    private String emailErrorRequired;

    @DialogField(fieldLabel = "Email input - format regex",
            fieldDescription = "Regular expression (regex) to check validity of the email input field. If no value is "
                    + "provided, format of the filed value will not be checked.",
            tab = YRTKConstants.TAB_TWO)
    @TextField
    @Inject @Default(values = "")
    @JsonProperty
    private String emailRegEx;

    @DialogField(fieldLabel = "Email input - invalid message",
            fieldDescription = "Error message (invalid field) for the email input field.",
            additionalProperties = @Property(name = "emptyText", value = "The email entered is not valid."),
            tab = YRTKConstants.TAB_TWO)
    @TextField
    @Inject @Default(values = "The email entered is not valid.")
    @JsonProperty
    private String emailErrorInvalid;

    @DialogField(
            fieldLabel = "Agreement Text",
            fieldDescription = "Text to show by the age verification and Terms & Conditions checkbox."
                    + "<br>Use <code>{{terms}}</code> as a placeholder for Terms & Conditions link.<br/>"
                    + "E.g.: <i>I am at least 18 years old and I have read, understood and agree to the {{terms}}.</i>",
            tab = YRTKConstants.TAB_TWO, required = true)
    @TextField
    @Inject @Default(values = "")
    private String agreementText;

    @DialogField(
            fieldLabel = "Terms & Conditions page path",
            name = "./" + PROP_TERMS_CONDITIONS_PAGE_PATH,
            fieldDescription = "Path to the Terms & Conditions page.",
            tab = YRTKConstants.TAB_TWO,
            required = true)
    @PathField(rootPath = YRTKConstants.CONTENT_ROOT)
    @LinkInject(titleProperty = "termsAndConditionsLinkTitle") @Optional
    @Named(PROP_TERMS_CONDITIONS_PAGE_PATH)
    private Link termsAndConditionsLink;

    @DialogField(
            fieldLabel = "Terms & Conditions link title",
            name = "./termsAndConditionsLinkTitle",
            tab = YRTKConstants.TAB_TWO,
            required = true)
    @TextField
    @Inject @Default(values = "Terms & Conditions")
    private String termsAndConditionsLinkTitle;


    @DialogField(fieldLabel = "Security message text", fieldDescription = "Configure the text for Google captcha",
            additionalProperties = @Property(name = "emptyText",
                    value = "For security, we need to validate that you're human."), tab = YRTKConstants.TAB_TWO)
    @TextField
    @Inject @Default(values = "For security, we need to validate that you're human.")
    private String securityText;

    @DialogField(fieldLabel = "Submit button text", fieldDescription = "Question submit button text",
            additionalProperties = @Property(name = "emptyText", value = "Submit question"),
            tab = YRTKConstants.TAB_TWO)
    @TextField
    @Inject @Default(values = "Submit question")
    private String submitBtnText;


    @DialogField(fieldLabel = "Captcha error",
            fieldDescription = "Error message to display when the captcha is not valid",
            additionalProperties = @Property(name = "emptyText", value = "Captcha Error. Try Reloading!"),
            tab = YRTKConstants.TAB_TWO)
    @TextField
    @Inject @Default(values = "Captcha Error. Try Reloading!")
    private String captchaError;

    //tab 3 properties - social login

    @DialogField(
            fieldLabel = "Facebook Login enabled",
            fieldDescription = "Check this checkbox if Facebook login should be included on the form.",
            tab = YRTKConstants.TAB_THREE)
    @Selection(type = "checkbox")
    @Inject @Default(booleanValues = false)
    private boolean facebookLoginEnabled;

    @DialogField(
            fieldLabel = "Facebook API key",
            fieldDescription = "Facebook API key for social login. This value must be provided for the Facebook login "
                    + "to be enabled.",
            tab = YRTKConstants.TAB_THREE)
    @TextField
    @Inject @Default(values = "")
    @JsonProperty
    private String apiKeyFacebook;

    @DialogField(
            fieldLabel = "Facebook button title",
            fieldDescription = "Facebook button title for the Social Login.",
            additionalProperties = @Property(name = "emptyText", value = "Log In with Facebook"),
            tab = YRTKConstants.TAB_THREE)
    @TextField
    @Inject @Default(values = "Log In with Facebook")
    private String socialFacebookButtonTitle;

    @DialogField(
            fieldLabel = "Twitter Login enabled",
            fieldDescription = "Check this checkbox if Twitter login should be included on the form.",
            tab = YRTKConstants.TAB_THREE)
    @Selection(type = "checkbox")
    @Inject @Default(booleanValues = false)
    private boolean twitterLoginEnabled;

    @DialogField(
            fieldLabel = "Twitter button title",
            fieldDescription = "Twitter button title for the Social Login.",
            additionalProperties = @Property(name = "emptyText", value = "Log In with Twitter"),
            tab = YRTKConstants.TAB_THREE)
    @TextField
    @Inject @Default(values = "Log In with Twitter")
    private String socialTwitterButtonTitle;

    @DialogField(fieldLabel = "Social Login Message 1",
            fieldDescription = "Message to appear before the social login buttons.",
            additionalProperties = @Property(name = "emptyText", value = "Make it quick, sign in via"),
            tab = YRTKConstants.TAB_THREE)
    @TextField
    @Inject @Default(values = "Make it quick, sign in via")
    private String socialLoginMsg1;

    @DialogField(fieldLabel = "Social Login Message 2",
            fieldDescription = "Message to appear after the social login buttons.",
            additionalProperties = @Property(name = "emptyText", value = "or the good old fashioned way"),
            tab = YRTKConstants.TAB_THREE)
    @TextField
    @Inject @Default(values = "or the good old fashioned way")
    private String socialLoginMsg2;

    //tab 4 properties - Success Modal

    @DialogField(fieldLabel = "Title 1", fieldDescription = "Success Modal title text 1. "
            + "This part of the title text appears in red.",
            additionalProperties = @Property(name = "emptyText", value = "Thank you"),
            tab = YRTKConstants.TAB_FOUR)
    @TextField
    @Inject @Default(values = "Thank you")
    private String successTitle1;

    @DialogField(fieldLabel = "Title 2", fieldDescription = "Success Modal title text 2. "
            + "This part of the title text appears in black.",
            additionalProperties = @Property(name = "emptyText", value = "for your question!"),
            tab = YRTKConstants.TAB_FOUR)
    @TextField
    @Inject @Default(values = "for your question!")
    private String successTitle2;

    @DialogField(fieldLabel = "Message", fieldDescription = "Message text to be displayed below title.<br/>E.g.: <i>"
            + "You will receive an email from the McDonald's Arabia team with the answer to your question within "
            + "48 hours.</i>", tab = YRTKConstants.TAB_FOUR)
    @TextArea
    @Inject
    private String successMsg;

    @DialogField(fieldLabel = "Button text", fieldDescription = "Button text for success modal. Clicking on the button"
            + " will close the modal.",
            additionalProperties = @Property(name = "emptyText", value = "OK, GOT IT"), tab = YRTKConstants.TAB_FOUR)
    @TextField
    @Inject @Default(values = "OK, GOT IT")
    private String successBtnText;


    //tab 5 - Errors


    @DialogField(fieldLabel = "Generic Error Message", fieldDescription = "Message text to be displayed when there are "
            + "errors with YRTK backend web service calls.",
            additionalProperties = @Property(name = "emptyText", value = "We are currently experiencing some "
                    + "connectivity issues. Please check again in few minutes."),
            tab = YRTKConstants.TAB_FIVE)
    @TextArea
    @Inject
    @Default(values = "We are currently experiencing some connectivity issues. Please check again in a few minutes.")
    @JsonProperty
    private String genericErrorMsg;


    @PostConstruct
    public void init() {

        String termsLink = StringUtils.replace(LINK_FORMAT, "{{href}}",
                termsAndConditionsLink != null ? termsAndConditionsLink.getHref() : "");
        termsLink = StringUtils.replace(termsLink, "{{title}}", termsAndConditionsLinkTitle);

        agreementText = StringUtils.replace(agreementText, "{{terms}}", termsLink);
    }


    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getSearchBoxPlaceholder() {
        return searchBoxPlaceholder;
    }

    public String getSearchBtnText() {
        return searchBtnText;
    }

    public String getQuestionBtnText() {
        return questionBtnText;
    }

    public String getSuggestionBtn1() {
        return suggestionBtn1;
    }

    public String getSuggestionBtn2() {
        return suggestionBtn2;
    }

    public String getQuestionHelpText() {
        return questionHelpText;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public String getQuestionPlaceholder() {
        return questionPlaceholder;
    }

//    public String getNameTitle() {
//        return nameTitle;
//    }

    public String getNamePlaceholder() {
        return namePlaceholder;
    }

//    public String getEmailTitle() {
//        return emailTitle;
//    }

    public String getEmailPlaceholder() {
        return emailPlaceholder;
    }

    public String getAgreementText() {
        return agreementText;
    }

    public String getSecurityText() {
        return securityText;
    }

    public String getSubmitBtnText() {
        return submitBtnText;
    }

    public String getSuccessTitle1() {
        return successTitle1;
    }

    public String getSuccessTitle2() {
        return successTitle2;
    }

    public String getSuccessMsg() {
        return successMsg;
    }

    public String getSuccessBtnText() {
        return successBtnText;
    }

    public String getQuestionErrorRequired() {
        return questionErrorRequired;
    }

    public String getNameErrorRequired() {
        return nameErrorRequired;
    }

    public String getEmailErrorRequired() {
        return emailErrorRequired;
    }

    public String getSocialFacebookButtonTitle() {
        return socialFacebookButtonTitle;
    }

    public String getSocialTwitterButtonTitle() {
        return socialTwitterButtonTitle;
    }

    public String getSocialLoginMsg1() {
        return socialLoginMsg1;
    }

    public String getSocialLoginMsg2() {
        return socialLoginMsg2;
    }

    public String getCaptchaError() {
        return captchaError;
    }

    @JsonProperty
    public boolean isTwitterLoginEnabled() {
        return twitterLoginEnabled;
    }
    @JsonProperty
    public boolean isFacebookLoginEnabled() {
        return facebookLoginEnabled && StringUtils.isNotEmpty(apiKeyFacebook);
    }

    public boolean isSocialLoginEnabled() {
        return isTwitterLoginEnabled() || isFacebookLoginEnabled();
    }

    @JsonProperty
    public String getWebServicesBaseUrl() {
        return yrtkWebServicesConfig.isYrtkServicesReverseProxyImplemented()
                ? "" : yrtkWebServicesConfig.getYrtkServicesBaseURL();
    }

    @JsonProperty
    public String getCaptchaSiteKey() {
        return yrtkWebServicesConfig.getYrtkRecaptchaSiteKey();
    }

    @JsonProperty
    public String getCountryCode() {
        if (StringUtils.isBlank(countryCode)) {
            countryCode = getYRTKCountryCode(currentPage);
        }
        return countryCode;
    }
    @JsonProperty
    public String getLanguageCode() {
        if (StringUtils.isBlank(languageCode)) {
            languageCode = getYRTKLanguageCode(currentPage, getCountryCode());
        }
        return languageCode;
    }
}
