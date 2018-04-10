package com.mcd.rwd.us.core.sightly.forms;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.widgets.CheckBox;
import com.citytechinc.cq.component.annotations.widgets.MultiField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.mcd.rwd.global.core.widget.colorpicker.ColorPicker;
import com.mcd.rwd.us.core.bean.IssueTypeBean;
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

@Component(name = "issuetype", value = "What is this about?",
		tabs = {
			@Tab( touchUINodeName = "section" , title = "What is this about?" )
		},
		disableTargeting = true,
		path = "content/form",
		group = " GWS-Global",
		listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
			@Listener(name = "afterinsert", value = "REFRESH_PAGE"),
			@Listener(name = "beforeSubmit", value = "function(dialog) {\n" +
					"\tif (dialog.getField('./whatIsThisAbout').getValue() == '') {\n" +
					"\t\tCQ.Ext.Msg.show({\n" +
					"\t\t\t'title': CQ.I18n.getMessage('What Is This About Dialog Validation'),\n" +
					"\t\t\t'msg': CQ.I18n.getMessage('Please provide section Dropdown Options.'),\n" +
					"\t\t\t'buttons': CQ.Ext.Msg.OK,\n" +
					"\t\t\t'icon': CQ.Ext.MessageBox.INFO,\n" +
					"\t\t\t'scope': this\n" +
					"\t\t});\n" +
					"\t\treturn false;\n" +
					"\t}\n" +
					"\telse {\n" +
					"\t\tvar item = dialog.getField('./whatIsThisAbout').getValue();\n" +
					"\t\tfor (i = 0; i < item.length; i++) {\n" +
					"\t\t\tvar option = item[i];\n" +
					"\t\t\tif (option == '') {\n" +
					"\t\t\t\tCQ.Ext.Msg.show({\n" +
					"\t\t\t\t\t'title': CQ.I18n.getMessage('What Is This About Dialog Validation'),\n" +
					"\t\t\t\t\t'msg': CQ.I18n.getMessage('Please provide section Dropdown Option for each item.'),\n" +
					"\t\t\t\t\t'buttons': CQ.Ext.Msg.OK,\n" +
					"\t\t\t\t\t'icon': CQ.Ext.MessageBox.INFO,\n" +
					"\t\t\t\t\t'scope': this\n" +
					"\t\t\t\t});\n" +
					"\t\t\t\treturn false;\n" +
					"\t\t\t}\n" +
					"\t\t}\n" +
					"\t}\n" +
					"}")
		})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class IssueType {

	@DialogField(name = "./sectionTopHeading", fieldLabel = "Section Top Heading",
			fieldDescription = "Heading to be displayed on top of section.")
	@TextField
	@Inject
	@Named("sectionTopHeading")
	@Default(values = StringUtils.EMPTY)
	private String topHeading;

	@DialogField(name = "./sectionSubHeading", fieldLabel = "Section Sub Heading",
			fieldDescription = "Sub heading to be displayed on top of section. " +
					"Default set to 'Choose one of the following:'")
	@TextField
	@Inject
	@Named("sectionSubHeading")
	@Default(values = FormConstants.WTA_SUBHEADING_MSG)
	private String subHeading;

	@DialogField(name = "./whatIsThisAbout", fieldLabel = "Dropdown Options *",
		fieldDescription = "Please provide options for what is this about dropdown.", required = true)
	@MultiField
	@Inject @Named("whatIsThisAbout")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text= "Menu Items & Nutrition", value = "foodmatters_Menu Items & Nutrition"),
					@Option(text= "Marketing, Games & Promotions", value = "marketing_Marketing, Games & Promotions"),
					@Option(text= "Social Responsibility & Contributions", value = "socialresp_Social Responsibility & Contributions"),
					@Option(text = "Other Topics", value = "mcbiz_Other Topics")})
	@Default(values = "")
	private String[] wta;

	@DialogField(name = "./wtaReq", value = UKContactUsFormConstant.REQUIRED_NO_VALUE,
			fieldLabel = "What is this about Required",
			fieldDescription = "Please select what is this about reason required o" +
					" not. Default set to 'No'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("wtaReq")
	@Default(values = FormConstants.REQUIRED_NO_VALUE)
	private String wtaRequired;

	@DialogField(fieldLabel = "Mobile App Validation Message",
			fieldDescription = "Validation message for what is this about field. Default set to 'Please choose" +
					" what is this about reason'")
	@TextField
	@Inject
	@Default(values = FormConstants.WTA_REQUIRED_MSG)
	private String wtaReqMessage;

	@DialogField(fieldLabel = "Label for What is this about", fieldDescription = "default value is 'Select what this inquiry is about.'")
	@TextField
	@Inject
	@Default(values = "Select what this inquiry is about.")
	private String wtaLabel;

	@DialogField(fieldLabel = "Title for What is this about", fieldDescription = "default value is 'Choose Subject'")
	@TextField
	@Inject
	@Default(values = "Choose Subject")
	private String wtaTitle;

	@DialogField(name = "./sectionBGColor", fieldLabel = "Background Color",
			additionalProperties = {@Property(name = "showHexValue", value = "{Boolean}true")},
			fieldDescription = "This will change the background color of section on selection of this category."
	)
	@ColorPicker
	@Inject @Named("sectionBGColor")
	@Default(values = StringUtils.EMPTY)
	private String bgcolor;

	@DialogField(name = "./showLocation", fieldDescription = "This will display location fields for this section.",
			additionalProperties = {@Property(name = "inputValue", value = "true"),
				@Property(name = "value", value = "true")})
	@CheckBox(text = "Show Location Fields")
	@Inject @Named("showLocation")
	@Default(values = "false")
	private Boolean showlocation;

    @DialogField(fieldLabel = "Default value for what is this about fiel", name = "./wtaDefaultOption",
		fieldDescription = "Default value for what is this about field. Default set to 'Please select any one option'")
    @TextField
    @Inject
    @Default(values = FormConstants.WTA_DEFAULT_OPTION)
    private String wtaDropdownDefaultValue = "";

	private static final Logger log = LoggerFactory.getLogger(IssueType.class);

	private String sectionTopHeading = "";

	private String sectionSubHeading = "";

	private List<IssueTypeBean> aboutOptions;



	private String wtaRequiredMessage = "";

	private String sectionBackgroundColor = "";

	private String selectText = FormConstants.SELECT_TEXT;

	private boolean showLocation = false;

	@PostConstruct
	public void init() throws Exception {

		log.debug("Getting Issue Type for contact form::");
		sectionTopHeading = this.topHeading;
		sectionSubHeading = this.subHeading + ("yes".equals(wtaRequired) ? "*" : "");
		String[] aboutOptionsData = null!=wta ? wta : null;

		if (aboutOptionsData.length > 1) {
			aboutOptions = new ArrayList<IssueTypeBean>();
			for (int i = 0; i < aboutOptionsData.length; i++) {
				IssueTypeBean issueTypeBean = new IssueTypeBean();
				issueTypeBean.setName(aboutOptionsData[i].split("_")[1]);
				issueTypeBean.setValue(aboutOptionsData[i].split("_")[0]);
				aboutOptions.add(issueTypeBean);
			}
		}
		sectionBackgroundColor = this.bgcolor;
		showLocation = this.showlocation;
		wtaRequiredMessage = this.wtaReqMessage;
	}

	public String getSectionTopHeading() {
		return sectionTopHeading.trim();
	}

	public String getSectionSubHeading() {
		return sectionSubHeading.trim();
	}

	public List<IssueTypeBean> getAboutOptions() {
		return aboutOptions;
	}

	public String getWtaRequired() {
		return wtaRequired;
	}
	
	 public String getWtaLabel() {
	        return wtaLabel;
	    }

	public String getSectionBackgroundColor() {
		return sectionBackgroundColor;
	}

	public Boolean getShowLocation() {
		return showLocation;
	}

	public String getSelectText() {
		return selectText;
	}

	public boolean isShowLocation() {
		return showLocation;
	}

	public String getWtaRequiredMessage() {
		return wtaRequiredMessage;
	}

	public String getWtaDropdownDefaultValue() {
		return wtaDropdownDefaultValue;
	}

	public String getWtaTitle() {
		return wtaTitle;
	}
}
