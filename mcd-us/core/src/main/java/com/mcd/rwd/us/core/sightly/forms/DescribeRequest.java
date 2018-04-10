package com.mcd.rwd.us.core.sightly.forms;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.widgets.MultiField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.mcd.rwd.global.core.widget.colorpicker.ColorPicker;
import com.mcd.rwd.us.core.constants.FormConstants;
import com.mcd.rwd.us.core.constants.UKContactUsFormConstant;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Component(name = "describerequest", value = "Tell us about your request",
		tabs = {
			@Tab( touchUINodeName = "section" , title = "Tell us about your request" )
		},
		disableTargeting = true,
		actions = { "text: Tell Us About Your Request", "-", "editannotate", "copymove", "delete" },
		group = " GWS-Global", path = "content/form",
		listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
			@Listener(name = "afterinsert", value = "REFRESH_PAGE")
		})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DescribeRequest {

	private static final Logger log = LoggerFactory.getLogger(DescribeRequest.class);

	@DialogField(name = "./sectionTopHeading", fieldLabel = "Section Top Heading",
			fieldDescription = "Heading to be displayed on top of section.")
	@TextField
	@Inject
	@Named("sectionTopHeading")
	@Default(values = StringUtils.EMPTY)
	private String topHeading;

	@DialogField(fieldLabel = "Comment Text",fieldDescription = "Heading text to be displayed for comment field." +
					" Default set to 'Enter your comments: *'")
	@TextField
	@Inject
	@Default(values = StringUtils.EMPTY)
	private String comment;

	@DialogField(name = "./commentReq", value = UKContactUsFormConstant.REQUIRED_YES_VALUE,
			fieldLabel = "Comment Required", fieldDescription = "Please select comment is required or not. Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("commentReq")
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String commentReqValidation;

	@DialogField(fieldLabel = "Comment Count",
			fieldDescription = "Number of characters including spaces for comment field." +
					" Default set to '1200 characters'.")
	@TextField
	@Inject
	@Default(values = FormConstants.COMMENT_COUNT)
	private String commentCount;

	@DialogField(name = "./commentValidation", fieldLabel = "Comment Validation Text",
			fieldDescription = "Text to be displayed for comment validation. Default set to 'characters'.")
	@TextField
	@Inject
	@Named("commentValidation")
	@Default(values = FormConstants.VALIDATION_TEXT)
	private String commentValidationText;

	@DialogField(name = "./commentMessage", fieldLabel = "Comment Validation Message",
			fieldDescription = "Validation message to be displayed for comment field. Default set to 'Please enter " +
					"the 'Comments regarding your visit' '")
	@TextField
	@Inject
	@Named("commentMessage")
	@Default(values = FormConstants.COMMENT_VALIDATION_MESSAGE)
	private String CommentValidationMessage;

	@DialogField(fieldLabel = "How will it be used Text",
			fieldDescription = "Text for How will it be used?* field. Default" +
					" set to 'How will it be used?*'")
	@TextField
	@Inject
	@Default(values = "")
	private String howUse;

	@DialogField(name = "./howUseReq", value = UKContactUsFormConstant.REQUIRED_YES_VALUE,
			fieldLabel = "How will it be used Required", fieldDescription = "Please select How will it be used required or " +
			"not. Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("howUseReq")
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String howUseRequired;

	@DialogField(name = "./howUseMessage", fieldLabel = "How will it be used Validation Message",
			fieldDescription = "Validation message for How will it be used?* field. Default set to 'Please enter information for the " +
					"field &quot;How will it be used?&quot;.'")
	@TextField
	@Inject
	@Named("howUseMessage")
	@Default(values = FormConstants.HOW_USE_VALIDATION_MESSAGE)
	private String howUseValidationMessage;

	@DialogField(fieldLabel = "Intended audience/distribution Text",
			fieldDescription = "Text for What is the intended audience/distribution?* field. Default set to '" +
					"What is the intended audience/distribution?*'")
	@TextField
	@Inject
	@Default(values = FormConstants.INT_AUD)
	private String intAud;

	@DialogField(name = "./intAudReq", value = UKContactUsFormConstant.REQUIRED_YES_VALUE,
			fieldLabel = "Intended audience/distribution Required",
			fieldDescription = "Please select  intended audience/distribution " +
					"required or not. Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("intAudReq")
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String intAudRequired;

	@DialogField(name = "./intAudMessage", fieldLabel = "Intended audience/distribution Validation Message",
			fieldDescription = "Validation message for What is the intended audience/distribution?* field. Default set to 'Please enter description " +
					"for 'What is the intended audience/distribution''")
	@TextField
	@Inject
	@Named("intAudMessage")
	@Default(values = FormConstants.INT_AUD_VALIDATION_MESSAGE)
	private String intAudValidationMessage;

	@DialogField(fieldLabel = "How long will it be used Text",
			fieldDescription = "Text for How long will it be used?* field. Default" +
					" set to 'How long will it be used?*'")
	@TextField
	@Inject
	@Default(values = FormConstants.HOW_LONG_USE)
	private String hlUse;

	@DialogField(name = "./hlUseReq", value = UKContactUsFormConstant.REQUIRED_YES_VALUE,
			fieldLabel = "How long will it be used Required", fieldDescription = "Please select How long will it be used required or not." +
			" Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("hlUseReq")
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String hlUseRequired;

	@DialogField(name = "./hlUseMessage", fieldLabel = "How long will it be used Validation Message",
			fieldDescription = "Validation message for How long will it be used?* field. Default set to 'Please enter information " +
					"for the field 'How long will it be used?'.'")
	@TextField
	@Inject
	@Named("hlUseMessage")
	@Default(values = FormConstants.HOW_LONG_USE_VALIDATION_MESSAGE)
	private String hlUseValidationMessage;

	@DialogField(fieldLabel = "Permission Request Text",
			fieldDescription = "Text for Have you previously requested permission for this use?* field. Default set to 'Have you previously" +
					" requested permission for this use?*'")
	@TextField
	@Inject
	@Default(values = FormConstants.PER_REQ)
	private String perReq;

	@DialogField(fieldLabel = "Yes Text",
			fieldDescription = "Radio button yes text to be displayed for specific question." +
					" Default set to 'Yes'")
	@TextField
	@Inject
	@Default(values = FormConstants.LOCATION_YES_TEXT)
	private String yesText;

	@DialogField(fieldLabel = "No Text",
			fieldDescription = "Radio button no text to be displayed for specific question." +
					" Default set to 'No'")
	@TextField
	@Inject
	@Default(values = FormConstants.LOCATION_NO_TEXT)
	private String noText;

	@DialogField(name = "./perReqReq", value = UKContactUsFormConstant.REQUIRED_YES_VALUE,
			fieldLabel = "Have you previously requested permission for this use Required",
			fieldDescription = "Please select Have you previously requested permission for this use " +
					"required or not. Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("perReqReq")
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String perReqRequired;

	@DialogField(name = "./perReqMessage", fieldLabel = "Permission Request Validation Message",
			fieldDescription = "Validation message for Have you previously requested permission for this use?* field." +
					" Default set to 'Validation Message'")
	@TextField
	@Inject
	@Named("perReqMessage")
	@Default(values = FormConstants.PRE_REQUEST_VALIDATION_MESSAGE)
	private String perReqValidationMessage;

	@DialogField(fieldLabel = "Submitted Request Text",
			fieldDescription = "Text for When was the request submitted?. Default set to 'When " +
					"was the request submitted? *'")
	@TextField
	@Inject
	@Default(values = FormConstants.REQ_SUBMIT)
	private String reqSubmit;

	@DialogField(name = "./reqSubmitReq", value = FormConstants.REQUIRED_YES_VALUE,
			fieldLabel = "When was the request submitted Required", fieldDescription = "Please select When was the request submitted " +
			"required or not. Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("reqSubmitReq")
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String reqSubmitRequired;

	@DialogField(name = "./reqSubmitMessage", fieldLabel = "Submitted Request Validation Message",
			fieldDescription = "Validation message for When was the request submitted?. Default set to 'Please pick the date of " +
					"'If yes, when was the request submitted'.'")
	@TextField
	@Inject
	@Named("reqSubmitMessage")
	@Default(values = "")
	private String reqSubmitValidationMessage;

	@DialogField(fieldLabel = "Outcome Text",
			fieldDescription = "Text for What was the outcome?. Default set " +
					"to 'What was the outcome? *'")
	@TextField
	@Inject
	@Default(values = FormConstants.OUTCOME_TEXT)
	private String outcome;

	@DialogField(fieldLabel = "Outcome Options")
	@MultiField
	@TextField
	@Inject @Default(values = "")
	private String[] outcomeOption;

	@DialogField(name = "./outcomeReq", value = UKContactUsFormConstant.REQUIRED_YES_VALUE,
			fieldLabel = "What was the outcome Required", fieldDescription = "Please select What was the outcome required or" +
			" not. Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("outcomeReq")
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String outcomeRequired;

	@DialogField(name = "./outcomeMessage", fieldLabel = "Outcome Validation Message",
			fieldDescription = "Validation message for What was the outcome?. Default set to 'Please pick the date of" +
					" 'If yes, when was the request submitted'.'")
	@TextField
	@Inject
	@Named("outcomeMessage")
	@Default(values = FormConstants.OUTCOME_VALIDATION_MESSAGE)
	private String outcomeValidationMessage;

	@DialogField(name = "./bookPubTopHeading", fieldLabel = "Book Publication Top Heading",
			fieldDescription = "Heading to be displayed on top of Book Publication section." +
					" Default set to 'Book Publications: '")
	@TextField
	@Inject
	@Named("bookPubTopHeading")
	@Default(values = FormConstants.BOOK_PUBLICATION_TOP_HEADING)
	private String bpTopHeading;

	@DialogField(name = "./bookPubSubHeading", fieldLabel = "Book Publication Sub Heading",
			fieldDescription = "Sub heading to be displayed on top of Book Publication section. Default set to 'Please " +
					"fill in all fields marked with * if applicable '")
	@TextField
	@Inject
	@Named("bookPubSubHeading")
	@Default(values = FormConstants.BOOK_PUBLICATION_SUB_HEADING)
	private String bpSubHeading;

	@DialogField(fieldLabel = "Author Text",
			fieldDescription = "Text for book publication author field.Default set to 'Author *</b")
	@TextField
	@Inject
	@Default(values = FormConstants.AUTHOR_TEXT)
	private String author;

	@DialogField(name = "./authorReq", value = UKContactUsFormConstant.REQUIRED_YES_VALUE,
			fieldLabel = "Author Required", fieldDescription = "Please select book publication author is required or" +
			" not. Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("authorReq")
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String auhtorRequired;

	@DialogField(name = "./authorMessage", fieldLabel = "Author Validation Message",
			fieldDescription = "Validation message for book publication author field.Default " +
					"set to 'Please enter Author'")
	@TextField
	@Inject
	@Named("authorMessage")
	@Default(values = FormConstants.AUTHOR_VALIDATION_MESSAGE)
	private String authorValidationMessage;

	@DialogField(fieldLabel = "Publisher Text",
			fieldDescription = "Text for Publisher field. Default set to 'Publisher*'")
	@TextField
	@Inject
	@Default(values = FormConstants.PUBLISHER_TEXT)
	private String publisher;

	@DialogField(name = "./publisherReq", value = UKContactUsFormConstant.REQUIRED_YES_VALUE,
			fieldLabel = "Publisher Required", fieldDescription = "Please select publisher is required or not." +
			" Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("publisherReq")
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String publisherRequired;

	@DialogField(name = "./publisherMessage", fieldLabel = "Publisher Validation Message",
			fieldDescription = "Validation message for Publisher field. " +
					"Default set to 'Please enter Publisher'")
	@TextField
	@Inject
	@Named("publisherMessage")
	@Default(values = FormConstants.PUBLISHER_VALIDATION_MESSAGE)
	private String publisherValidationMessage;

	@DialogField(fieldLabel = "Publication Title & Edition Text",
			fieldDescription = "Text for Publication Title & Edition field.Default " +
					"set to 'Publication Title & Edition*'")
	@TextField
	@Inject
	@Default(values = FormConstants.PUBLICATION_TITLE_EDITION)
	private String pubTitleEdition;

	@DialogField(name = "./pubTitleEditionReq", value = UKContactUsFormConstant.REQUIRED_YES_VALUE,
			fieldLabel = "Publication Title & Edition Required", fieldDescription = "Please select publication " +
			"title & edition is required or not. Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("pubTitleEditionReq")
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String pubTitleEditionRequired;

	@DialogField(name = "./pubTitleEditionMessage", fieldLabel = "Publication Title & Edition Validation Message",
			fieldDescription = "Validation message for Publication Title & Edition field.Default set to '" +
					"Please enter Publication Title & Edition'")
	@TextField
	@Inject
	@Named("pubTitleEditionMessage")
	@Default(values = FormConstants.PUBLICATION_TITLE_EDITION_VALIDATION_MESSAGE)
	private String pubTitleEditionValidationMessage;

	@DialogField(name = "./supAttachTopHeading", fieldLabel = "Supporting Attachments Heading",
			fieldDescription = "Heading to be displayed on top of Supporting Attachments section. " +
					"Default set to 'Supporting Attachments:'")
	@TextField
	@Inject
	@Named("supAttachTopHeading")
	@Default(values = FormConstants.SUPPORTING_ATTACH_TOP_HEADING)
	private String saTopHeading;

	@DialogField(name = "./supAttachSubHeading", fieldLabel = "Supporting Attachments Sub Heading",
			fieldDescription = "Sub heading to be displayed on top of Supporting Attachments section. Default set to 'Please upload documents or images which" +
					" will illustrate and support your request.'")
	@TextField
	@Inject
	@Named("supAttachSubHeading")
	@Default(values = FormConstants.SUPPORTING_ATTACH_SUB_HEADING)
	private String saSubHeading;

	@DialogField(name = "./supAttachNoteText", fieldLabel = "Supporting Attachments Note Text",
			fieldDescription = "Note to be displayed on top of Supporting Attachments section. Default set to '" +
					"Large documents may take several minutes to upload and the size of each attachment must be less than 10MB.'")
	@TextField
	@Inject
	@Named("supAttachNoteText")
	@Default(values = FormConstants.SUPPORTING_ATTACH_NOTE_TEXT)
	private String saNoteHeading;

	@DialogField(name = "./firstFile", fieldLabel = "File 1 Text",
			fieldDescription = "Text for supporting attachments file 1 field.Default set to 'File 1'")
	@TextField
	@Inject
	@Named("firstFile")
	@Default(values = FormConstants.FIRST_FILE)
	private String file1;

	@DialogField(name = "./secFile", fieldLabel = "File 2 Text",
			fieldDescription = "Text for supporting attachments file 2 field.Default set to 'File 2'")
	@TextField
	@Inject
	@Named("secFile")
	@Default(values = FormConstants.SECOND_FILE)
	private String file2;

	@DialogField(name = "./thirdFile", fieldLabel = "File 3 Text",
			fieldDescription = "Text for supporting attachments file 3 field.Default set to 'File 3'")
	@TextField
	@Inject
	@Named("thirdFile")
	@Default(values = FormConstants.THIRD_FILE)
	private String file3;

	@DialogField(fieldLabel = "Browse Text", fieldDescription = "File Upload button text to be displayed on" +
			" button. Default set to 'Browse'")
	@TextField
	@Inject
	@Default(values = FormConstants.BROWSE_TEXT)
	private String browseText;

	@DialogField(name = "./sectionBGColor", fieldLabel = "Background Color",
			additionalProperties = {@Property(name = "showHexValue", value = "{Boolean}true")},
			fieldDescription = "This will change the background color of section on selection of this category."
	)
	@ColorPicker
	@Inject @Named("sectionBGColor")
	@Default(values = StringUtils.EMPTY)
	private String bgcolor;

	@DialogField(name = "./reqSubmitReq", fieldLabel = "Date of Visit Required",
			fieldDescription = "Please select date of visit required or not. Default set to 'Yes'")
	@Selection(type = Selection.SELECT, options = {
			@Option(value = FormConstants.REQUIRED_YES_VALUE, text = "Yes"),
			@Option(value = FormConstants.REQUIRED_NO_VALUE, text = "No")
	})
	@Inject @Named("reqSubmitReq")
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String dateOfVisitReq;

	@DialogField(name = "./reqSubmitMessage", fieldLabel = "Date error message",
			fieldDescription = "Validation message for Date of Visit field. Default set to 'Please select an " +
					"appropriate date '")
	@TextField
	@Inject @Named("reqSubmitMessage")
	@Default(values = "Please select an appropriate date")
	private String dateOfVisitValidationMessage;


	@DialogField(name = "./adaTextForDayFieldTrade", fieldLabel = "Ada Text for day field",
			fieldDescription = "Ada Text for day field. Default set to 'Day' " +
					"Value will be considered as a Representative Value for field")
	@TextField
	@Inject
	@Default(values = "Day")
	private String adaTextForDayFieldTrade;

	@DialogField(fieldLabel = "Ada Text for month field",
			fieldDescription = "Ada Text for month field. Default set to 'Month' " +
					"Value will be considered as a Representative Value for field")
	@TextField
	@Inject
	@Default(values = "Month")
	private String adaTextForMonthFieldTrade;

	@DialogField(fieldLabel = "Ada Text for year field",
			fieldDescription = "Ada Text for year field. Default set to 'Year' " +
					"Value will be considered as a Representative Value for field")
	@TextField
	@Inject
	@Default(values = "Year")
	private String adaTextForYearFieldTrade;

	@DialogField(fieldLabel = "Ada Text for 1st Browser Button field",
			fieldDescription = "Ada Text for 1st Browser Button field. Default set to 'for file 1'")
	@TextField
	@Inject
	@Default(values = "for file 1")
	private String adaTextForForFileOne;

	@DialogField(fieldLabel = "Ada Text for 2nd Browser Button field",
			fieldDescription = "Ada Text for 2nd Browser Button field. Default set to 'for file 2'")
	@TextField
	@Inject
	@Default(values = "for file 2")
	private String adaTextForForFileTwo;

	@DialogField(fieldLabel = "Ada Text for 3rd Browser Button field",
			fieldDescription = "Ada Text for 3rd Browser Button field. Default set to 'for file 3'")
	@TextField
	@Inject
	@Default(values = "for file 3")
	private String adaTextForForFileThird;

	@DialogField(fieldLabel = "Generalise drop-down validation message",
			fieldDescription = "Please provide generalise drop-down validation message. Default set to " +
					"'Please select an appropriate value from drop-down'")
	@TextField
	@Inject
	@Default(values = "Please select an appropriate value from drop-down")
	private String dropDownValidationTradeText;

	private String sectionTopHeading = StringUtils.EMPTY;

	private String commentReq = StringUtils.EMPTY;

	private String commentValidation = StringUtils.EMPTY;

	private String commentMessage = StringUtils.EMPTY;

	private String howUseReq = StringUtils.EMPTY;

	private String howUseMessage = StringUtils.EMPTY;

	private String intAudReq = StringUtils.EMPTY;

	private String intAudMessage = StringUtils.EMPTY;

	private String howLongUse = StringUtils.EMPTY;

	private String howLongUseReq = StringUtils.EMPTY;

	private String howLongUseMessage = StringUtils.EMPTY;

	private String perReqReq = StringUtils.EMPTY;

	private String perReqMessage = StringUtils.EMPTY;

	private List<String> outcomeOptions;

	private String outcomeReq = StringUtils.EMPTY;

	private String outcomeMessage = StringUtils.EMPTY;

	private String bookPubTopHeading = StringUtils.EMPTY;

	private String bookPubSubHeading = StringUtils.EMPTY;

	private String authorReq = StringUtils.EMPTY;

	private String authorMessage = StringUtils.EMPTY;

	private String publisherReq = StringUtils.EMPTY;

	private String publisherMessage = StringUtils.EMPTY;

	private String pubTitleEditionReq = StringUtils.EMPTY;

	private String pubTitleEditionMessage = StringUtils.EMPTY;

	private String supAttachTopHeading = StringUtils.EMPTY;

	private String supAttachSubHeading = StringUtils.EMPTY;

	private String supAttachNoteText = StringUtils.EMPTY;

	private String firstFile = StringUtils.EMPTY;

	private String secFile = StringUtils.EMPTY;

	private String thirdFile = StringUtils.EMPTY;

	private String sectionBackgroundColor = StringUtils.EMPTY;


    private List<String> dayOptionsTrade;

	@PostConstruct
	public void init() throws Exception {
		log.debug("set Value from dialog properties..");
		sectionTopHeading = this.topHeading;
		commentReq = this.commentReqValidation;
		commentValidation = this.commentValidationText;
		commentMessage = this.CommentValidationMessage;
		howUseReq = this.howUseRequired;
		howUseMessage = this.howUseValidationMessage;
		intAudReq = this.intAudRequired;
		intAudMessage = this.intAudValidationMessage;
		howLongUse = this.hlUse;
		howLongUseReq = this.hlUseRequired;
		howLongUseMessage = this.hlUseValidationMessage;
		perReqReq = this.perReqRequired;
		perReqMessage = this.perReqValidationMessage;
		String[] outcomeOptionsData = null!=outcomeOption ? outcomeOption : null;
		if (null != outcomeOptionsData) {
			outcomeOptions = new ArrayList<String>();
			for (int i = 0; i < outcomeOptionsData.length; i++) {
				String options = outcomeOptionsData[i];
				log.debug("outcomeOptions "+options);
				outcomeOptions.add(options);
			}
		}
		outcomeReq = this.outcomeRequired;
		outcomeMessage = this.outcomeValidationMessage;
		bookPubTopHeading = this.bpTopHeading;
		bookPubSubHeading = this.bpSubHeading;
		authorReq = this.auhtorRequired;
		authorMessage = this.authorValidationMessage;
		publisherReq = this.publisherRequired;
		publisherMessage = this.publisherValidationMessage;
		pubTitleEditionReq = this.pubTitleEditionRequired;
		pubTitleEditionMessage = this.pubTitleEditionValidationMessage;
		supAttachTopHeading = this.saTopHeading;
		supAttachSubHeading = this.saSubHeading;
		supAttachNoteText = this.saNoteHeading;
		firstFile = this.file1;
		secFile = this.file2;
		thirdFile = this.file3;
		sectionBackgroundColor = this.bgcolor;
		/*if (!StringUtils.EMPTY.equals(sectionBackgroundColor)) {
			sectionBackgroundColor = "#" + sectionBackgroundColor;
		}*/

		String[] daysOptionValue = new String[] {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
		if (null != daysOptionValue) {
			dayOptionsTrade = getListOfOptionData(daysOptionValue);
		}

	}

	private List<String> getListOfOptionData(String[] optionsDataArray) {
		List<String> options = new ArrayList<String>();
		for (String anOptionsDataArray : optionsDataArray) {
			options.add(anOptionsDataArray);
		}
		return options;
	}

	public String getAdaTextForDayFieldTrade() {
		return adaTextForDayFieldTrade;
	}

	public String getAdaTextForMonthFieldTrade() {
		return adaTextForMonthFieldTrade;
	}

	public String getAdaTextForYearFieldTrade() {
		return adaTextForYearFieldTrade;
	}

	public String getSectionTopHeading() {
		return sectionTopHeading.trim();
	}

	public String getComment() {
		return comment.trim();
	}

	public String getCommentReq() {
		return commentReq;
	}

	public String getCommentCount() {
		return commentCount.trim();
	}

	public String getCommentValidation() {
		return commentValidation.trim();
	}

	public String getCommentMessage() {
		return commentMessage.trim();
	}

	public String getHowUse() {
		return howUse.trim();
	}

	public String getHowUseReq() {
		return howUseReq;
	}

	public String getHowUseMessage() {
		return howUseMessage.trim();
	}

	public String getIntAud() {
		return intAud.trim();
	}

	public String getIntAudReq() {
		return intAudReq;
	}

	public String getIntAudMessage() {
		return intAudMessage.trim();
	}

	public String getHowLongUse() {
		return howLongUse.trim();
	}

	public String getHowLongUseReq() {
		return howLongUseReq;
	}

	public String getHowLongUseMessage() {
		return howLongUseMessage.trim();
	}

	public String getPerReq() {
		return perReq.trim();
	}

	public String getPerReqReq() {
		return perReqReq;
	}

	public String getPerReqMessage() {
		return perReqMessage.trim();
	}

	public String getYesText() {
		return yesText.trim();
	}

	public String getNoText() {
		return noText.trim();
	}

	public String getReqSubmit() {
		return reqSubmit.trim();
	}

	public String getOutcome() {
		return outcome.trim();
	}

	public String getOutcomeReq() {
		return outcomeReq;
	}

	public List<String> getOutcomeOptions() {
		return outcomeOptions;
	}

	public String getOutcomeMessage() {
		return outcomeMessage.trim();
	}

	public String getBookPubTopHeading() {
		return bookPubTopHeading.trim();
	}

	public String getBookPubSubHeading() {
		return bookPubSubHeading.trim();
	}

	public String getAuthor() {
		return author.trim();
	}

	public String getAuthorReq() {
		return authorReq;
	}

	public String getAuthorMessage() {
		return authorMessage.trim();
	}

	public String getPublisher() {
		return publisher.trim();
	}

	public String getPublisherReq() {
		return publisherReq;
	}

	public String getPublisherMessage() {
		return publisherMessage.trim();
	}

	public String getPubTitleEdition() {
		return pubTitleEdition.trim();
	}

	public String getPubTitleEditionReq() {
		return pubTitleEditionReq;
	}

	public String getPubTitleEditionMessage() {
		return pubTitleEditionMessage.trim();
	}

	public String getSupAttachTopHeading() {
		return supAttachTopHeading.trim();
	}

	public String getSupAttachSubHeading() {
		return supAttachSubHeading.trim();
	}

	public String getSupAttachNoteText() {
		return supAttachNoteText.trim();
	}

	public String getFirstFile() {
		return firstFile.trim();
	}

	public String getSecFile() {
		return secFile.trim();
	}

	public String getThirdFile() {
		return thirdFile.trim();
	}

	public String getBrowseText() {
		return browseText.trim();
	}

	public String getSectionBackgroundColor() {
		return sectionBackgroundColor;
	}

	public List<String> getDayOptionsTrade() {
		return dayOptionsTrade;
	}

	public String getDateOfVisitReq() {
		return dateOfVisitReq;
	}

	public String getDateOfVisitValidationMessage() {
		return dateOfVisitValidationMessage;
	}

	public String getDropDownValidationTradeText() {
		return dropDownValidationTradeText;
	}

	public String getAdaTextForForFileOne() {
		return adaTextForForFileOne;
	}

	public String getAdaTextForForFileTwo() {
		return adaTextForForFileTwo;
	}

	public String getAdaTextForForFileThird() {
		return adaTextForForFileThird;
	}

	public String getReqSubmitRequired() {
		return reqSubmitRequired;
	}

	public String getReqSubmitValidationMessage() {
		return reqSubmitValidationMessage;
	}
}
