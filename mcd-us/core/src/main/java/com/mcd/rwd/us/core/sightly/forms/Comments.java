package com.mcd.rwd.us.core.sightly.forms;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.widgets.CheckBox;
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
import java.text.NumberFormat;
import java.util.Locale;

@Component(name = "comments", value = "Tell us about it",
		tabs = {
				@Tab( touchUINodeName = "section" , title = "Tell us about it" )
		},
		allowedChildren = "[/apps/mcd-us/components/form/comments/mobile]",
		actions = { "text: Tell Us About It", "-", "editannotate", "copymove", "delete" },
		group = " GWS-Global", path = "content/form",
		listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
				@Listener(name = "afterinsert", value = "REFRESH_PAGE")})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Comments {

	@DialogField(name = "./sectionTopHeading", fieldLabel = "Section Top Heading",
			fieldDescription = "Heading to be displayed on top of section.")
	@TextField
	@Inject
	@Named("sectionTopHeading")
	@Default(values = StringUtils.EMPTY)
	private String topHeading;

	@DialogField(name = "./sectionSubHeading", fieldLabel = "Section Sub Heading",
			fieldDescription = "Sub heading to be displayed on top of section.")
	@TextField
	@Inject @Named("sectionSubHeading")
	@Default(values = StringUtils.EMPTY)
	private String subHeading;

	@DialogField(fieldLabel = "Comment Text",
			fieldDescription = "Heading text to be displayed for comment field. Default set to 'Enter your comments: *'")
	@TextField
	@Inject
	@Default(values = FormConstants.COMMENT_TEXT)
	private String mobAppComment;

	@DialogField(name = "./commentReq", value = UKContactUsFormConstant.REQUIRED_YES_VALUE,
			fieldLabel = "Comment Required", fieldDescription = "Please select comment is required or not." +
			" Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("commentReq")
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String commentValidation;

	@DialogField(fieldLabel = "Comment Count",
			fieldDescription = "Number of characters including spaces for comment field. Default set to '1200 characters'.")
	@TextField
	@Inject
	@Default(values = FormConstants.COMMENT_COUNT)
	private String commentCount;

	@DialogField(fieldLabel = "Comment Validation Text",
			fieldDescription = "Text to be displayed for comment validation. Default set to 'characters'.")
	@TextField
	@Inject
	@Default(values = FormConstants.VALIDATION_TEXT)
	private String mobAppCommentValidation;

	@DialogField(name = "./sectionBGColor", fieldLabel = "Background Color",
		additionalProperties = {@Property(name = "showHexValue", value = "{Boolean}true")},
			fieldDescription = "This will change the background color of section on selection of this category."
		)
	@ColorPicker
	@Inject @Named("sectionBGColor")
	@Default(values = StringUtils.EMPTY)
	private String bgcolor;

	@DialogField(name = "./showMobileFields", fieldLabel = "Show Mobile Fields", fieldDescription = "This will display mobile fields for this section.",
		additionalProperties = {@Property(name = "inputValue", value = "true"),
				@Property(name = "value", value = "true")})
	@CheckBox(text = "Show Mobile Fields")
	@Inject @Default(values = "false") @Named("showMobileFields")
	private String showmobilefields;

	@DialogField(name = "./mobAppCommentMessage", fieldLabel = "Comment Validation Message",
			fieldDescription = "Validation message to be displayed for comment field." +
					" Default set to 'Please enter the 'Comments regarding your visit' '")
	@TextField
	@Inject @Named("mobAppCommentMessage")
	@Default(values = FormConstants.COMMENT_VALIDATION_MESSAGE)
	private String CommentValidationMessage;

	private static final Logger log = LoggerFactory.getLogger(Comments.class);

	private String sectionTopHeading = StringUtils.EMPTY;

	private String sectionSubHeading = StringUtils.EMPTY;

	private String mobileAppComment = StringUtils.EMPTY;

	private String commentCountString = StringUtils.EMPTY;

	private String mobileAppCommentReq = StringUtils.EMPTY;

	private String mobAppCommentMessage = StringUtils.EMPTY;

	private String mobileAppCommentValidation = StringUtils.EMPTY;

	private String sectionBackgroundColor = StringUtils.EMPTY;

	private String showMobileFields = StringUtils.EMPTY;

	@PostConstruct
	public void init() throws Exception {
		log.debug("inside activate method");
		sectionTopHeading = this.topHeading;
		sectionSubHeading = this.subHeading;
		mobileAppComment = this.mobAppComment;
		mobileAppCommentReq = this.commentValidation;
		mobAppCommentMessage = this.CommentValidationMessage;
		commentCountString = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(commentCount));
		mobileAppCommentValidation = this.mobAppCommentValidation;
		sectionBackgroundColor = this.bgcolor;
		showMobileFields = this.showmobilefields;
/*		if (!StringUtils.EMPTY.equals(sectionBackgroundColor)) {
			sectionBackgroundColor = "#" + sectionBackgroundColor;
		}*/
		log.debug("set value from dialog properties");
	}

	public String getSectionTopHeading() {
		return sectionTopHeading.trim();
	}

	public String getSectionSubHeading() {
		return sectionSubHeading.trim();
	}

	public String getMobileAppComment() {
		return mobileAppComment.trim();
	}

	public String getMobileAppCommentReq() {
		return mobileAppCommentReq;
	}

	public String getMobAppCommentMessage() {
		return mobAppCommentMessage.trim();
	}

	public String getCommentCount() {
		return commentCount.trim();
	}

	public String getCommentCountString() {
		return commentCountString;
	}

	public String getMobileAppCommentValidation() {
		return mobileAppCommentValidation.trim();
	}

	public String getSectionBackgroundColor() {
		return sectionBackgroundColor;
	}

	public String getShowmobilefields() {
		return showmobilefields;
	}

	public String getShowMobileFields() {
		return showMobileFields;
	}
}
