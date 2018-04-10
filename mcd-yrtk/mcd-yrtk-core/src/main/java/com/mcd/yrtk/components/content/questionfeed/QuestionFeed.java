package com.mcd.yrtk.components.content.questionfeed;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.widgets.NumberField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.icfolson.aem.library.api.page.PageDecorator;
import com.icfolson.aem.library.core.constants.ComponentConstants;
import com.mcd.yrtk.YRTKConstants;
import com.mcd.yrtk.components.content.Serializable;
import com.mcd.yrtk.components.content.YRTKServiceEnabled;
import com.mcd.yrtk.components.content.questionfeed.model.QueryResult;
import com.mcd.yrtk.components.content.questionfeed.model.QuestionDetails;
import com.mcd.yrtk.service.McdHttpConnectionMgrService;
import com.mcd.yrtk.service.YRTKWebServicesConfig;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;

@Component(value = QuestionFeed.COMPONENT_TITLE,
        actions = {
                ComponentConstants.EDIT_BAR_TEXT + QuestionFeed.COMPONENT_TITLE,
                ComponentConstants.EDIT_BAR_SPACER,
                ComponentConstants.EDIT_BAR_EDIT + ComponentConstants.EDIT_BAR_ANNOTATE,
                ComponentConstants.EDIT_BAR_COPY_MOVE,
                ComponentConstants.EDIT_BAR_DELETE},
        disableTargeting = true, group = "YRTK",
        tabs = {@Tab(title = "Question Listing"), @Tab(title = "Filters"),
                @Tab(title = "Question Details Modal"), @Tab(title = "Social Sharing")},
        listeners = @Listener(name = ComponentConstants.EVENT_AFTER_EDIT, value = ComponentConstants.REFRESH_PAGE),
        dialogWidth = YRTKConstants.DIALOG_DEFAULT_WIDTH,
        dialogHeight = YRTKConstants.DIALOG_DEFAULT_HEIGHT)
@Model(adaptables = SlingHttpServletRequest.class)
public final class QuestionFeed implements Serializable, YRTKServiceEnabled {

    public static final String COMPONENT_TITLE = "Question Listing";

    private static final int DEFAULT_CHUNK_SIZE = 20;
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionFeed.class);
    private static final String RESPONSE_QUESTION_NOT_FOUND =
            "{\"result\":{\"rows\":\"Question Not Found\"},\"status\":\"success\"}";

    @Self
    private SlingHttpServletRequest request;
    @Inject
    private YRTKWebServicesConfig yrtkWebServicesConfig;
    @Inject
    private McdHttpConnectionMgrService mcdHttpConnectionMgrService;
    @Inject
    private PageDecorator currentPage;

    private String questionID;
    private QuestionDetails questionDetails;
    private String countryCode;
    private String languageCode;


    //tab 1 - properties related to question listing

    @DialogField(fieldLabel = "No results text",
            fieldDescription = "Text to display when there are no questions that match search criteria. "
                    + "If left empty, no text will be displayed.")
    @TextField
    @Inject @Default(values = "")
    private String noResultsText;

    @DialogField(fieldLabel = "Infinite scroll chunk size",
            fieldDescription = "Number of question cards to display at a time. (max 200)", defaultValue = "20",
            additionalProperties = @Property(name = "emptyText", value = "20"))
    @NumberField(allowDecimals = false, allowNegative = false, min = "3", max = "200")
    @Inject @Default(intValues = DEFAULT_CHUNK_SIZE)
    @JsonProperty
    private int chunkSize;

    @DialogField(fieldLabel = "Pinned Question ID", fieldDescription = "ID of the question that should be pinned at the"
            + " top in the default view of the question listing.<br/>Only a single ID (numeric string) is allowed.")
    @TextField
    @Inject @Default(values = "")
    @JsonProperty
    private String pinnedQuestionID;

    @DialogField(fieldLabel = "'Load more' label", fieldDescription = "Label for the 'Load more' button.",
            additionalProperties = @Property(name = "emptyText", value = "Load more"))
    @TextField
    @Inject @Default(values = "Load more")
    private String loadMoreLabel;


    //tab 2 - properties related to filters

    @DialogField(fieldLabel = "Quick Filter 'All'", fieldDescription = "Label for the quick filter 'All'",
            tab = YRTKConstants.TAB_TWO,
            additionalProperties = @Property(name = "emptyText", value = "All"))
    @TextField
    @Inject @Default(values = "All")
    private String quickFiltersAll;

    @DialogField(fieldLabel = "Category Filter label", fieldDescription = "Label for the category filters.",
            tab = YRTKConstants.TAB_TWO,
            additionalProperties = @Property(name = "emptyText", value = "Filter by category"))
    @TextField
    @Inject @Default(values = "Filter by category")
    private String categoryFilterLabel;

    @DialogField(fieldLabel = "Apply Filter button", fieldDescription = "Label for 'Apply Filter' button.",
            tab = YRTKConstants.TAB_TWO,
            additionalProperties = @Property(name = "emptyText", value = "Apply Filter"))
    @TextField
    @Inject @Default(values = "Apply Filter")
    private String applyFilterBtn;

    @DialogField(fieldLabel = "Reset button", fieldDescription = "Label for 'Reset' button.",
            tab = YRTKConstants.TAB_TWO,
            additionalProperties = @Property(name = "emptyText", value = "Reset"))
    @TextField
    @Inject @Default(values = "Reset")
    private String resetBtn;


    //tab 3 - properties related to modal with question details
	
	@DialogField(fieldLabel = "Hide Social Share", fieldDescription = "Select the checkbox to disable Social Share",
            tab = YRTKConstants.TAB_FOUR,
            additionalProperties = @Property(name = "emptyText", value = "false"))
    @Selection (type = "checkbox")
    @Inject @Default(values = "false")
    private String enableSocialShare;

    @DialogField(fieldLabel = "Category label", fieldDescription = "'Category' label",
            additionalProperties = @Property(name = "emptyText", value = "Category:"),
            tab = YRTKConstants.TAB_THREE)
    @TextField
    @Inject @Default(values = "Category:")
    private String categoryLabel;

    @DialogField(fieldLabel = "Close label", fieldDescription = "'Close' label",
            additionalProperties = @Property(name = "emptyText", value = "Close"),
            tab = YRTKConstants.TAB_THREE)
    @TextField
    @Inject @Default(values = "Close")
    private String closeLabel;


    //tab 4 - properties related to sharing

    @DialogField(fieldLabel = "Twitter username", fieldDescription = "The Twitter username to associate with the tweet "
            + " (without the <code>@</code> character).<br/>E.g.: <i>McDonaldsArabia</i>",
            tab = YRTKConstants.TAB_FOUR)
    @TextField
    @Inject @Default(values = "")
    @JsonProperty
    private String twitterUsername;

    @DialogField(fieldLabel = "Twitter hashtags", fieldDescription = "Comma-separated list of hashtag values without "
            + "the preceding <code>#</code> character.<br/>E.g.: <i>Your-Right-To-Know,Qatar</i>",
            tab = YRTKConstants.TAB_FOUR)
    @TextField
    @Inject @Default(values = "")
    @JsonProperty
    private String twitterHashtags;

    @PostConstruct
    public void init() {

        //if pinnedQuestionID is not a number, replace with empty string
        if (!StringUtils.isNumeric(pinnedQuestionID)) {
            pinnedQuestionID = "";
        }

        try {
            LOGGER.debug("request is null? {}", request == null);
            final String[] selectors = request != null ? request.getRequestPathInfo().getSelectors() : null;

            // first selector is the questionID - it must be a numeric string
            if (selectors != null && selectors.length > 0 && StringUtils.isNumeric(selectors[0])) {

                questionID = selectors[0];
                LOGGER.debug("Selector (questionID): {}", questionID);

                final HttpClient httpClient = new HttpClient(mcdHttpConnectionMgrService.getMultiThreadedConf());

                final String yrtkServiceURL = yrtkWebServicesConfig.getYrtkServicesBaseURL() + "/ofyq/questions/"
                        + getCountryCode() + "/" + getLanguageCode() + "/0/" + questionID + "/jsonp/JSON_CALLBACK";
                final GetMethod method = new GetMethod(yrtkServiceURL);

                final int statusCode = httpClient.executeMethod(method);
                if (statusCode == HttpStatus.SC_OK) {
                    String responseBodyAsString = method.getResponseBodyAsString();
                    responseBodyAsString = StringUtils.substring(responseBodyAsString, 5,
                            responseBodyAsString.length() - 1);
                    LOGGER.debug("response: {}", responseBodyAsString);
                    if (!RESPONSE_QUESTION_NOT_FOUND.equals(responseBodyAsString)) {
                        try {
                            final QueryResult queryResult = MAPPER.readValue(responseBodyAsString, QueryResult.class);
                            if ("success".equals(queryResult.getStatus())) {
                                queryResult.getResult().getRows();
                                LOGGER.debug("number of rows within results: {}",
                                        queryResult.getResult().getRows().size());

                                for (QuestionDetails questionDetails : queryResult.getResult().getRows()) {
                                    if (String.valueOf(questionDetails.getQuestionId()).equals(questionID)) {
                                        this.questionDetails = questionDetails;
                                        break;
                                    }
                                }
                            }
                        } catch (JsonParseException e) {
                            LOGGER.error("Error when parsing JSON from YRTK web service. ", e);
                        } catch (JsonMappingException e) {
                            LOGGER.error("Error when mapping JSON from YRTK web service. ", e);
                        }
                    }
                }
                LOGGER.debug("question found? {}", questionDetails != null);
            }
        } catch (IOException ioE) {
            LOGGER.error("IOException when making web service call or unmarshalling response.", ioE);
        }
    }

    @JsonProperty
    public String getNoResultsText() {
        return noResultsText;
    }

    public String getLoadMoreLabel() {
        return loadMoreLabel;
    }

    public boolean isQuestionDetailsPresent() {
        return questionDetails != null;
    }

    public QuestionDetails getQuestionDetails() {
        return questionDetails;
    }

    @JsonProperty
    public Long getSelectedQuestionID() {
        return questionID != null ? Long.parseLong(questionID) : null;
    }

    public String getQuickFiltersAll() {
        return quickFiltersAll;
    }

    public String getCategoryFilterLabel() {
        return categoryFilterLabel;
    }

    public String getApplyFilterBtn() {
        return applyFilterBtn;
    }

    public String getResetBtn() {
        return resetBtn;
    }

    public String getCategoryLabel() {
        return categoryLabel;
    }

    public String getCloseLabel() {
        return closeLabel;
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
