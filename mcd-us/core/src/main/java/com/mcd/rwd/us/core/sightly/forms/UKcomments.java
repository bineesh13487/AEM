package com.mcd.rwd.us.core.sightly.forms;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.widgets.RichTextEditor;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.citytechinc.cq.component.annotations.widgets.rte.*;
import com.mcd.rwd.us.core.constants.FormConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

@Component(name = "comments", value = "UK comment section",
		description = "ukcontactactus_commentsection",
		tabs = {
				@Tab( touchUINodeName = "section" , title = "Contact us comment Tab" )
		},
		actions = { "text: UK comment section", "-", "editannotate", "copymove", "delete" },
		group = ".hidden", path = "content/ukForm",
		listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
				@Listener(name = "afterinsert", value = "REFRESH_PAGE"),
				@Listener(name = "beforeSubmit", value = "function(dialog) {\n" +
						"\tvar defaultSection = dialog.getField('./showDefaultSection').getValue();\n" +
						"\tif(defaultSection == ''){\n" +
						"\t\tCQ.Ext.Msg.show({\n" +
						"\t\t\t'title': CQ.I18n.getMessage('Tell Us About It Dialog Validation'),\n" +
						"\t\t\t'msg': CQ.I18n.getMessage('Please provide Is This Default Section information.'),\n" +
						"\t\t\t'buttons': CQ.Ext.Msg.OK,\n" +
						"\t\t\t'icon': CQ.Ext.MessageBox.INFO,\n" +
						"\t\t\t'scope': this\n" +
						"\t\t});\n" +
						"\t\treturn false;\n" +
						"\t}\n" +
						"\telse{\n" +
						"\t\tif (dialog.getField('./secFormCategory').getValue() == '') {\n" +
						"\t\t\tCQ.Ext.Msg.show({\n" +
						"\t\t\t\t'title': CQ.I18n.getMessage('Tell Us About It Dialog Validation'),\n" +
						"\t\t\t\t'msg': CQ.I18n.getMessage('Please provide section Form Categories.'),\n" +
						"\t\t\t\t'buttons': CQ.Ext.Msg.OK,\n" +
						"\t\t\t\t'icon': CQ.Ext.MessageBox.INFO,\n" +
						"\t\t\t\t'scope': this\n" +
						"\t\t\t});\n" +
						"\t\t\treturn false;\n" +
						"\t\t\t}\n" +
						"\t\t\telse {\n" +
						"\t\t\t\tvar item = dialog.getField('./secFormCategory').getValue();\n" +
						"\t\t\t\tfor (i = 0; i < item.length; i++) {\n" +
						"\t\t\t\t\tvar category = item[i];\n" +
						"\t\t\t\t\tif (category == '') {\n" +
						"\t\t\t\t\t\tCQ.Ext.Msg.show({\n" +
						"\t\t\t\t\t\t\t'title': CQ.I18n.getMessage('Tell Us About It Dialog Validation'),\n" +
						"\t\t\t\t\t\t\t'msg': CQ.I18n.getMessage('Please provide section Form Category for each item.'),\n" +
						"\t\t\t\t\t\t\t'buttons': CQ.Ext.Msg.OK,\n" +
						"\t\t\t\t\t\t\t'icon': CQ.Ext.MessageBox.INFO,\n" +
						"\t\t\t\t\t\t\t'scope': this\n" +
						"\t\t\t\t\t\t});\n" +
						"\t\t\t\t\t\treturn false;\n" +
						"\t\t\t\t\t}\n" +
						"\t\t\t\t}\n" +
						"\t\t\t}\n" +
						"\t}\n" +
						"}")})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class UKcomments{

	@DialogField(fieldLabel = "Comment Text", fieldDescription = "Heading text to be displayed for comment field. Default set to 'Enter your comments: '")
	@TextField
	@Inject
	@Default(values = FormConstants.COMMENT_TEXT)
	private String commentMessage;

	@DialogField(name = "./commentReq", fieldLabel = "Comment Required", fieldDescription = "Please select comment is required or not. Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	@Inject @Named("commentReq")
	private String commentValidation;

	@DialogField(name = "./charactersText", fieldLabel = "Character Text", fieldDescription = "Text to be displayed for comment validation. Default set to 'characters'.")
	@TextField
	@Inject @Named("charactersText") @Default(values = FormConstants.VALIDATION_TEXT)
	private String mobAppCommentValidation;

	@DialogField(fieldLabel = "Comment Validation Message", fieldDescription = "Validation message to be displayed for comment" +
			" field. Default set to 'Please enter the 'Comments regarding your visit' '", value = FormConstants.COMMENT_VALIDATION_MESSAGE)
	@TextField
	@Inject @Default(values = FormConstants.COMMENT_VALIDATION_MESSAGE)
	private String commentValidationMessage;

	@DialogField(name = "./commentCount", fieldLabel = "Character Count Value", fieldDescription = "Please provide the character maximum characrter count. Default set to '1200'")
	@TextField @Named("commentCount")
	@Inject @Default(values = FormConstants.COMMENT_COUNT)
	private String CharactersCounter;

	@DialogField(fieldLabel = "Send Button Text", fieldDescription = "Text to be displayed for submit button. Default set to 'Send'")
	@TextField
	@Inject @Default(values = "Send")
	private String submitButtonText;

	@DialogField(fieldLabel = "Reset Button Texte", fieldDescription = "Text to be displayed for reset button. Default set to 'Reset'")
	@TextField
	@Inject @Default(values = "Reset")
	private String resetButtonText;

	@DialogField(fieldLabel = "Please provide legal text",
			fieldDescription = "Legal text for bottom of form. Default set to 'By submitting your comments and details, you are agreeing to our Terms and Conditions'")
	@RichTextEditor(
			edit = @Edit,
			subsuperscript = @SubSuperscript,
			lists = @Lists,
			justify = @Justify,
			misctools = @MiscTools(specialchars = false, sourceedit = true),
			spellcheck = @SpellCheck,
			findreplace = @FindReplace,
			paraformat = {
					@ParaFormat(tag = "h1", description = "Heading 1"),
					@ParaFormat(tag = "h2", description = "Heading 2"),
					@ParaFormat(tag = "h3", description = "Heading 3"),
					@ParaFormat(tag = "h4", description = "Heading 4"),
					@ParaFormat(tag = "h5", description = "Heading 5"),
					@ParaFormat(tag = "p", description = "Paragraph")
			})
	@Inject @Default(values = "<p>By submitting your comments and details, you are agreeing to our Terms and Conditions</p>")
	private String legalSection;

	@Inject
	ValueMap valueMap;

	private static final Logger LOGGER = LoggerFactory.getLogger(UKcomments.class);

	private String commentCount = StringUtils.EMPTY;

	private String commentReq = StringUtils.EMPTY;

	private String charactersText = StringUtils.EMPTY;
	
	private String legalText = StringUtils.EMPTY;

	private String honeyPotReq = FormConstants.REQUIRED_YES_VALUE;

	@PostConstruct
	public void init() throws Exception {
		LOGGER.error("inside init method");
		this.commentReq = this.commentValidation;
		this.charactersText = this.mobAppCommentValidation;
		if(null!=legalSection)
		commentCount = this.CharactersCounter;
		if(null!=legalSection)
		tagRemoval(legalSection);
		if(null!=legalSection){
			tagRemoval(legalSection);
		}

		LOGGER.error("set value from dialog properties");
	}
	
	private void tagRemoval(String oldHyperlink) {
		String[] hyper = oldHyperlink.split("");
		for (int counter = 0; counter < hyper.length; counter++) {
			if (counter > 2 && (hyper.length - (counter + 3)) > 1) {
				legalText = legalText + hyper[counter];
			}
		}
	}

	public String getHoneyPotReq() {
		return honeyPotReq;
	}

	public String getLegalText() {
		return legalText;
	}
	
	public String getCommentMessage() {
		return commentMessage.trim();
	}

	public String getCommentReq() {
		return commentReq;
	}

	public String getCommentValidationMessage() {
		return commentValidationMessage.trim();
	}

	public String getCommentCount() {
		return commentCount.trim();
	}

	public String getCharactersText() {
		return charactersText.trim();
	}

	public String getSubmitButtonText() {
		return submitButtonText;
	}

	public String getResetButtonText() {
		return resetButtonText;
	}
	
}
