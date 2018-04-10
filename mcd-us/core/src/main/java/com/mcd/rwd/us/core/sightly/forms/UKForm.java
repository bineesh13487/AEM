package com.mcd.rwd.us.core.sightly.forms;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Listener;
import com.citytechinc.cq.component.annotations.Tab;
import com.citytechinc.cq.component.annotations.widgets.DialogFieldSet;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.day.cq.wcm.api.Page;
import com.mcd.rwd.global.core.components.common.Text;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.us.core.constants.FormConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

@Component(name = "ukForm", value = "ukcontactactus_form",
        description = "ukcontactactus_form",
        tabs = {
                @Tab( touchUINodeName = "contactus" , title = "Contact Us Main Form Tab" ),
                @Tab( touchUINodeName = "submitPopUpWindow", title = "Contact Us Pop Up Window Tab"),
                @Tab( touchUINodeName = "restaurantsearch", title = "Restaurant Search Pop-up Tab")
        },
        actions = { "text: Contactus UK Form", "-", "editannotate", "copymove", "delete" },
        group = "GWS-Global", path = "content",
        allowedChildren = "[/apps/mcd-us/components/content/ukForm/comments,/apps/mcd-us/components/content/ukForm/ukContactactusRestaurantInformation," +
                "/apps/mcd-us/components/content/ukForm/ukContactuspersonalInformation]",
        listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
                @Listener(name = "afterinsert", value = "REFRESH_PAGE"),
            @Listener(name = "beforeSubmit", value = "function(dialog) {\n" +
                    "\tif(dialog.getField('./formAction').getValue().trim() == '') {\n" +
                    "\t\tCQ.Ext.Msg.show({\n" +
                    "\t\t\t'title': CQ.I18n.getMessage('ContactUs Dialog Validation'),\n" +
                    "\t\t\t'msg': CQ.I18n.getMessage('Please provide form action url.'),\n" +
                    "\t\t\t'buttons': CQ.Ext.Msg.OK,\n" +
                    "\t\t\t'icon': CQ.Ext.MessageBox.INFO,\n" +
                    "\t\t\t'scope': this\n" +
                    "\t\t});\n" +
                    "\t\treturn false;\n" +
                    "\t}\n" +
                    "\tif(dialog.getField('./custServiceEmail').getValue().trim() == '') {\n" +
                    "\t\tCQ.Ext.Msg.show({\n" +
                    "\t\t\t'title': CQ.I18n.getMessage('ContactUs Dialog Validation'),\n" +
                    "\t\t\t'msg': CQ.I18n.getMessage('Please provide customer service email.'),\n" +
                    "\t\t\t'buttons': CQ.Ext.Msg.OK,\n" +
                    "\t\t\t'icon': CQ.Ext.MessageBox.INFO,\n" +
                    "\t\t\t'scope': this\n" +
                    "\t\t});\n" +
                    "\t\treturn false;\n" +
                    "\t}\n" +
                    "}")})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class UKForm {

    @DialogField(fieldLabel = "Form Action *", required = true,
            fieldDescription = "Form action url where user response will be submitted.")
    @TextField
    @Inject
    @Default(values = FormConstants.VALIDATION_TEXT)
    private String formAction;

    @DialogField(fieldLabel = "Form Name",
            fieldDescription = "Form name to save into DB. Leave empty to use page title.")
    @TextField
    @Inject
    @Default(values = StringUtils.EMPTY)
    private String formName;

    @DialogField(name = "./yesButtonText", fieldLabel = "Radio Button text for 'Yes' option",
            fieldDescription = "Radio button 'Yes' Text  'Yes'")
    @TextField
    @Inject @Named("yesButtonText")
    @Default(values = "Yes")
    private String yesButton;

    @DialogField(name = "./noButtonText", fieldLabel = "Radio Button text for 'No' option",
            fieldDescription = "Radio button 'No' Text 'No'")
    @TextField
    @Inject @Named("noButtonText")
    @Default(values = "No")
    private String noButton;

    @DialogField(fieldLabel = "Thank You Page",
            fieldDescription = "Please provide path of the page where user should" +
                    " be redirected after submitting the form.")
    @PathField
    @Inject
    @Default(values = StringUtils.EMPTY)
    private String thankYouPage;

    @DialogField(name = "./errorPage", fieldLabel = "Error Page",
            fieldDescription = "Please provide path of the page where user should be " +
                    "redirected in case of error in form submission.")
    @PathField
    @Inject @Named("errorPage")
    @Default(values = StringUtils.EMPTY)
    private String failurePage;

    @DialogField(name = "./indicateAge", fieldDescription="Default value is: 'Please indicate " +
            "if you are 16 years old or older.'")
    @DialogFieldSet(title = "Please provide text for Age Indicator" , namePrefix = "indicateAge/")
    @ChildResource(name = "indicateAge")
    @Inject @Named("indicateAge")
    private Text indicateAgeText;

    @DialogField(fieldDescription="Default value is: 'We are unable to deal with your query or complaint online.'")
    @DialogFieldSet(title = "Please provide text for Age Limit Message" , namePrefix = "ageLimitText/")
    @ChildResource(name = "ageLimitText")
    @Inject
    private Text ageLimitData;

    @DialogField(name = "./sectionContent", hideLabel = false , fieldDescription="Provided Text will appear on top of form." +
            " Please put '##' placeholder for 'click here' text. Default value is: 'We’re here to help you.'.")
    @DialogFieldSet(title = "Section Content" , namePrefix = "sectionContent/")
    @ChildResource(name = "sectionContent")
    @Inject @Named("sectionContent")
    private Text sectionContentTextData;

    @DialogField(name = "./ageAriaLabel", fieldDescription = "Default value is: '* required.'",
            fieldLabel = "Please provide aria label for Age Indicator")
    @TextField
    @Inject @Named("ageAriaLabel")
    @Default(values = " * required.")
    private String indicateAgeAriaLabel;

    @DialogField(fieldLabel = "'click here' text")
    @TextField
    @Inject
    @Default(values = StringUtils.EMPTY)
    private String clickHere;

    @DialogField(fieldLabel = "Link for Click here Link")
    @TextField
    @Inject
    @Default(values = StringUtils.EMPTY)
    private String clickHereLink;

    @DialogField(name = "./clickHereText", fieldLabel = " Click here Descriptive Text")
    @TextField
    @Inject @Named("clickHereText")
    @Default(values = StringUtils.EMPTY)
    private String clickHereDescribe;

    @DialogField(fieldLabel = "No Text Error Message",
            fieldDescription = "Text to be displayed as error message when location is not entered" +
                    " Default set to 'Please provide search location'")
    @TextField
    @Inject @Named("noTextErrorMessage")
    @Default(values = "Please provide search location")
    private String noTextErrorMessage = StringUtils.EMPTY;

    @DialogField(fieldLabel = "Search Results Text",
            fieldDescription = "Text to be displayed with results. Default set to 'results found'")
    @TextField
    @Inject @Named("searchResultText")
    @Default(values = "results found")
    private String searchResultText = StringUtils.EMPTY;

    @DialogField(fieldLabel = "Restaurant Number Text (Desktop)",
            fieldDescription = "Text for 'Restaurant Number' on desktop. Default set to 'Restaurant Number'")
    @TextField
    @Inject @Named("restaurantNumnerTextDesktop")
    @Default(values = "Restaurant Number")
    private String restaurantNumnerTextDesktop = StringUtils.EMPTY;

    @DialogField(fieldLabel = "Restaurant Number Text (Mobile)",
            fieldDescription = "Text for 'Restaurant Number' on mobile. Default set to 'number'")
    @TextField
    @Inject @Named("restaurantNumnerTextMobile")
    @Default(values = "number")
    private String restaurantNumnerTextMobile = StringUtils.EMPTY;

    @DialogField(tab = 2, fieldLabel = "First Line Text",
            fieldDescription = "Text to be displayed on Continue button dialog. Default set " +
                    "to 'Are you sure you want to'")
    @TextField
    @Inject
    @Default(values = FormConstants.FIRST_LINE_TEXT)
    private String firstLine;

    @DialogField(tab = 2, fieldLabel = "Second Line Text",
            fieldDescription = "Text to be displayed on Continue button " +
                    "dialog. Default set to 'SUBMIT'")
    @TextField
    @Inject
    @Default(values = FormConstants.SECOND_LINE_TEXT)
    private String secLine;

    @DialogField(tab = 2, fieldLabel = "Third Line Text",
            fieldDescription = "Text to be displayed on Continue button dialog. " +
                    "Default set to 'this form?'")
    @TextField
    @Inject
    @Default(values = FormConstants.THIRD_LINE_TEXT)
    private String thirdLine;

    @DialogField(tab = 2, fieldLabel = "Information Text",
            fieldDescription = "Information text to be displayed on Continue button dialog. Default set to " +
                    "'Click 'Cancel' to continue editing, or click 'Submit' to continue.'")
    @TextField
    @Inject
    @Default(values = FormConstants.INFORMATION_TEXT)
    private String informationText;

    @DialogField(tab = 2, name = "./submitText", fieldLabel = "Submit Button Text",
            fieldDescription = "Text to be displayed on submit button dialog for submit" +
                    " button. Default set to 'SUBMIT'")
    @TextField
    @Inject
    @Named("submitText") @Default(values = FormConstants.SUBMIT_BUTTON_TEXT)
    private String submit;

    @DialogField(tab = 2, name = "./cancelText", fieldLabel = "Cancel Button Text",
            fieldDescription = "Text to be displayed on submit button dialog for" +
                    " cancel button. Default set to 'CANCEL'")
    @TextField
    @Inject @Named("cancelText") @Default(values = FormConstants.CANCEL_BUTTON_TEXT)
    private String cancel;

    @DialogField(tab = 3, name = "./resSearchHeading", fieldLabel = "Heading",
            fieldDescription = "Text to be displayed as heading on restaurant search popup window." +
                    " Default set to 'Enter your location:'")
    @TextField
    @Inject @Named("resSearchHeading")
    @Default(values = FormConstants.RESTAURANT_SEARCH_HEADING_TEXT)
    private String topHeading;

    @DialogField(tab = 3, name = "./resSearchPlaceholder", fieldLabel = "Search Placeholder Text",
            fieldDescription = "Text to be displayed as placeholder for restaurant search" +
                    " text box. Default set to 'Zip, City, State'")
    @TextField
    @Inject @Named("resSearchPlaceholder")
    @Default(values = FormConstants.RESTAURANT_SEARCH_PLACEHOLDER_TEXT)
    private String searchPlaceholder;

    @DialogField(tab = 3, name = "./resSearchButtonText", fieldLabel = "Search Button Text",
            fieldDescription = "Text to be displayed for restaurant search button. Default set to 'GO'")
    @TextField
    @Inject @Named("resSearchButtonText") @Default(values = FormConstants.RESTAURANT_SEARCH_BUTTON_TEXT)
    private String searchButtonText;

    @DialogField(tab = 3, fieldLabel = "Restaurant Result Select Button", fieldDescription = "Text to be " +
            "displayed for restaurant result select button.Default set to 'Select Location'")
    @TextField
    @Inject @Default(values = FormConstants.RESTAURANT_SEARCH_RESULT_BUTTON_TEXT)
    private String resSelectButtonText;

    @Inject @Default(values = StringUtils.EMPTY)
    Page getCurrentPage;

    @Inject
    ValueMap valueMap;

    private static final Logger log = LoggerFactory.getLogger(UKForm.class);
    private static final String CONTENT = "/content";
    private static final String HTML_EXTENTION = ".html";

    private String sectionContentText = StringUtils.EMPTY;

    private String errorPage = StringUtils.EMPTY;
    
    private String resSearchHeading = StringUtils.EMPTY;

    private String resSearchPlaceholder = StringUtils.EMPTY;

    private String resNoLocation = StringUtils.EMPTY;

    private String resSearchButtonText = StringUtils.EMPTY;

    private String pageNameForAnalytics = StringUtils.EMPTY;
    
    private String ageIndicatorText = StringUtils.EMPTY;
    
    private String ageLimitText = StringUtils.EMPTY;
    
    private String yesButtonText = StringUtils.EMPTY;
    
    private String noButtonText = StringUtils.EMPTY;
    
    private String ageAriaLabel = StringUtils.EMPTY;
    
    private String sectionContentText1 = StringUtils.EMPTY;

    @PostConstruct
    public void activate() throws Exception {
        log.debug("inside activate method...");

        log.debug("set value from dialog properties..");
        formName = this.formName!=null ? this.formName : getCurrentPage.getTitle();
        if(sectionContentTextData != null) {
            sectionContentText = sectionContentTextData.getText();
        }else{
            sectionContentText ="We’re here to help you";
        }
        if (sectionContentText.contains("##")) {
			String [] arr = sectionContentText.split("##");
			sectionContentText = arr[0];
			if (arr.length>1) {
				sectionContentText1 = arr[1];
			}
		}

        if(null!=indicateAgeText) {
            ageIndicatorText = tagRemoval(indicateAgeText.getText());
        }else{
            ageIndicatorText = "<p>Please indicate if you are 16 years old or older </p>";
        }
        ageAriaLabel =  this.indicateAgeAriaLabel;

        if(null!=ageLimitData) {
            ageLimitText = tagRemoval(this.ageLimitData.getText());
        }else{
            ageLimitText = "<p>We are unable to deal with your query or complaint online.</p>";
        }

        yesButtonText = this.yesButton;
        noButtonText = this.noButton;
        thankYouPage = thankYouPage.trim().startsWith(CONTENT) ? thankYouPage + HTML_EXTENTION : thankYouPage;
        log.debug("thankYouPage path: " + thankYouPage);

        errorPage = this.failurePage;
        errorPage = errorPage.trim().startsWith(CONTENT) ? errorPage + HTML_EXTENTION : errorPage;
        log.debug("errorPage path: " + errorPage);

        resSearchHeading = this.topHeading;
        resSearchPlaceholder = this.searchPlaceholder;
        resNoLocation = valueMap.get("resNoLocation", FormConstants.RESTAURANT_LOCATOR_SEARCH_TEXT);
        resSearchButtonText = this.searchButtonText;
        pageNameForAnalytics = PageUtil.getPageNameForAnalytics(getCurrentPage);
    }
    
    

	public String tagRemoval(String paraString) {
		String[] paraStringArr = paraString.split("");
		String convertedString = "";
		for (int counter = 0; counter < paraStringArr.length; counter++) {
			if (counter > 2 && (paraStringArr.length - (counter + 4)) > 1) {
				convertedString = convertedString + paraStringArr[counter];
			}
		}
		return convertedString.toString();
	}

	public String getRestaurantNumnerTextDesktop() {
		return restaurantNumnerTextDesktop;
	}

	public String getRestaurantNumnerTextMobile() {
		return restaurantNumnerTextMobile;
	}

	public String getSearchResultText() {
		return searchResultText;
	}

	public String getNoTextErrorMessage() {
		return noTextErrorMessage;
	}

	public String getAgeIndicatorText() {
		return ageIndicatorText;
	}

	public String getAgeLimitText() {
		return ageLimitText;
	}
	
	public String getAgeAriaLabel() {
		return ageAriaLabel;
	}
	
    public String getFormAction() {
        return formAction;
    }

    public String getYesButtonText() {
		return yesButtonText;
	}

    public String getClickHere() {
		return clickHere;
	}
    
    public String getClickHereText() {
		return clickHereDescribe;
	}
    
    public String getClickHereLink() {
		return clickHereLink;
	}
    
    public String getSectionContentText1() {
		return sectionContentText1;
	}
    
	public String getNoButtonText() {
		return noButtonText;
	}

	public String getFormName() {
        return formName;
    }

    public String getSectionContentText() {
        return sectionContentText;
    }
    
    public String getThankYouPage() {
        return thankYouPage;
    }

    public String getErrorPage() {
        return errorPage;
    }

    public String getResSearchHeading() {
        return resSearchHeading;
    }

    public String getResSearchPlaceholder() {
        return resSearchPlaceholder;
    }

    public String getResNoLocation() {
        return resNoLocation;
    }

    public String getResSearchButtonText() {
        return resSearchButtonText;
    }

    public String getResSelectButtonText() {
        return resSelectButtonText;
    }

    public String getPageNameForAnalytics() {
        return pageNameForAnalytics;
    }
}
