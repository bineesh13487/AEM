package com.mcd.rwd.us.core.sightly.forms;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.widgets.MultiField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.mcd.rwd.us.core.constants.UKContactUsFormConstant;
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

@Component(name = "ukContactactusRestaurantInformation", value = "ukcontactactus_restaurantInformation",
		description = "ukcontactactus_restaurantInformation",
		tabs = {
				@Tab( touchUINodeName = "section" , title = "Contact us Restaurant Information Tab" )
		},
		actions = { "text: Contactus UK Restaurant Information", "-", "editannotate"},
		group = ".hidden", path = "content/ukForm",
		listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
				@Listener(name = "afterinsert", value = "REFRESH_PAGE")})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class UKcontactus_restaurantInfo{

	@DialogField(fieldLabel = "Please provide button's section heading text",
			fieldDescription = "Button's heading section. Default set to 'Which of our restaurants did you visit?'")
	@TextField
	@Inject
	@Default(values = "Which of our restaurants did you visit?")
	private String restaurantButtonLabelSection;

	@DialogField(name = "./menuItem", value = "Menu Item ", fieldLabel = "Menu Item Text", fieldDescription = "Text for Menu Item field. " +
			"Default set to 'Menu Item '")
	@TextField
	@Inject @Named("menuItem")
	@Default(values = UKContactUsFormConstant.MENUITEM)
	private String menuItemText;

	@DialogField(name = "./menuItemReq", value = UKContactUsFormConstant.REQUIRED_YES_VALUE, fieldLabel = "Menu Item Required", fieldDescription = "Please select Menu Item is" +
			" required or not. Default set to 'No'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("menuItemReq")
	@Default(values = UKContactUsFormConstant.REQUIRED_YES_VALUE)
	private String menuItemValidation;

	@DialogField(name = "./menuItemMessage", fieldLabel = "Menu Item Validation Message",
			fieldDescription = "Validation message for Menu Item field. Default set to 'Please Type Menu Item name '")
	@TextField
	@Inject @Named("menuItemMessage")
	@Default(values = UKContactUsFormConstant.MENUITEM_VALIDATION_MESSAGE)
	private String menuItemValidationMessage;

	@DialogField(fieldLabel = "Restaurant visit text.", value = "Regarding a specific restaurant visit?",
			fieldDescription = "Restaurant visit text. Default set to 'Regarding a specific restaurant visit? '")
	@TextField
	@Inject
	@Default(values = "Regarding a specific restaurant visit?")
	private String restaurantVisitText;

	@DialogField(fieldLabel = "Table Service Text.", fieldDescription = "Table Service Text. Default set to" +
			" 'Did you make use of Table service?'", value = "Did you make use of Table service?")
	@TextField
	@Inject
	@Default(values = "Did you make use of Table service?")
	private String tableServiceText;

	@DialogField(fieldLabel = "Restaurant Locator Button Text:", fieldDescription = "Text to be displayed for restaurant locator button." +
			" 'Default set to Restaurant Locator'")
	@TextField
	@Inject
	@Default(values = UKContactUsFormConstant.RESTAURANT_BUTTON_TEXT)
	private String restaurantButtonText;

	@DialogField(fieldLabel = "Locate Me Button Text:", fieldDescription = "Text to be displayed for locate me button." +
			" 'Default set to Locate Me'")
	@TextField
	@Inject
	@Default(values = UKContactUsFormConstant.LOCATEMEBUTTON)
	private String locateMeButtonText;

	@DialogField(fieldLabel = "Restaurant Visited Text", fieldDescription = "Text for restaurant name field. " +
			"Default set to 'Restaurant Visited: '")
	@TextField
	@Inject
	@Default(values = UKContactUsFormConstant.RESTAURANT_NAME_TEXT)
	private String restaurantName;

	@DialogField(fieldLabel = "Day Options",
			fieldDescription = "Please provide options for time. First provided value will be " +
					"considered as a Representative Value")
	@MultiField
	@TextField
	@Inject @Default(values = "")
	private String[] daysOptions;

	@DialogField(name = "./monthOptions", fieldLabel = "Month Options",
			fieldDescription = "Please provide options for time. First provided value will be considered " +
					"as a Representative Value")
	@MultiField
	@TextField @Named("monthOptions")
	@Inject @Default(values = "")
	private String[] monthsOptions;

	@DialogField(name = "./yearOptions", fieldLabel = "Year Options",
			fieldDescription = "Please provide options for time. First provided value will be " +
					"considered as a Representative Value")
	@MultiField
	@TextField
	@Inject @Named("yearOptions") @Default(values = "")
	private String[] yearsOptions;

	@DialogField(fieldLabel = "Hour Options",
			fieldDescription = "Please provide options for time. First provided value will be " +
					"considered as a Representative Value")
	@MultiField
	@TextField
	@Inject @Default(values = "")
	private String[] hoursOptions;

	@DialogField(name = "./minuteOptions", fieldLabel = "Minute Options",
			fieldDescription = "Please provide options for time. First provided value will be " +
					"considered as a Representative Value")
	@MultiField
	@TextField
	@Inject @Named("minuteOptions") @Default(values = "")
	private String[] minutesOptions;

	@DialogField(fieldLabel = "Drive Through", fieldDescription = "Text to be displayed for drive through " +
			"radio button. 'Default set to Drive Thru Lane'")
	@TextField
	@Inject
	@Default(values = UKContactUsFormConstant.RESTAURANT_DRIVE_THRU_VALUE)
	private String driveThru;

	@DialogField(fieldLabel = "front counter", fieldDescription = "Text to be displayed for front counter radio button. 'Default set to Inside the Restaurant at front counter'")
	@TextField
	@Inject
	@Default(values = UKContactUsFormConstant.RESTAURANT_FRONT_COUNTER)
	private String frontCounter;

	@DialogField(fieldLabel = "kiosk Ordering", fieldDescription = "Text to be displayed for kiosk Ordering radio button." +
			" 'Default set to Inside the restaurant at a Kiosk'")
	@TextField
	@Inject
	@Default(values = UKContactUsFormConstant.RESTAURANT_KIOSK_ORDERING)
	private String kioskOrdering;

	@DialogField(fieldLabel = "Mobile Ordering", fieldDescription = "Text to be displayed for mobile ordering radio button." +
			" 'Default set to Via the Mobile ordering App'")
	@TextField
	@Inject
	@Default(values = UKContactUsFormConstant.RESTAURANT_MOBILE_ORDERING)
	private String mobileOrdering;

	@DialogField(fieldLabel = "Visit Type Required", fieldDescription = "Please select Visit Type is " +
			"required or not. Default set to 'Yes'", value = UKContactUsFormConstant.REQUIRED_YES_VALUE)
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Default(values = UKContactUsFormConstant.REQUIRED_YES_VALUE)
	private String visitTypeReq;

	@DialogField(name = "./visitTypeValidationMessage", fieldLabel = "Visit Type Validation message",
			fieldDescription = "Validation message for Menu Item field. " +
					"Default set to 'Please select a radio button '")
	@TextField
	@Inject @Named("visitTypeValidationMessage")
	@Default(values = "Visit Type Validation message")
	private String visitTypeValidationMessage;

	@DialogField(fieldLabel = "Table Service Required", fieldDescription = "Please select table service" +
			" is required or not. Default set to 'Yes'", value = UKContactUsFormConstant.REQUIRED_YES_VALUE)
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Default(values = UKContactUsFormConstant.REQUIRED_YES_VALUE)
	private String tableServiceTypeReq;

	@DialogField(fieldLabel = "Please Select Restaurant button selection message", fieldDescription = "Default set to " +
			"'Please select an appropriate option for restaurant search '")
	@TextField
	@Inject
	@Default(values = "Please select an appropriate option for restaurant search")
	private String restaurantSelectionWay;

	@DialogField(fieldLabel = "Radio Button Text for Yes", fieldDescription = "Text to be displayed for" +
			" radio button. 'Default set to table service Yes'")
	@TextField
	@Inject
	@Default(values = UKContactUsFormConstant.Radio_Button_YES)
	private String yesButtonText;

	@DialogField(fieldLabel = "Radio Button Text for No", fieldDescription = "Text to be displayed for " +
			"radio button. 'Default set to table service No'")
	@TextField
	@Inject
	@Default(values = UKContactUsFormConstant.Radio_Button_NO)
	private String noButtonText;

	@DialogField(fieldLabel = "Product Purchase Text", fieldDescription = "Product purchase text " +
			"'Where did you make your purchase?'")
	@TextField
	@Inject
	@Default(values = "Where did you make your purchase?")
	private String productPurchase;

	@DialogField(fieldLabel = "Please provide date of visit text",
			fieldDescription = "Date of visit text 'Date of visit'")
	@TextField
	@Inject
	@Default(values = "Date of Visit")
	private String dateOfVisit;

	@DialogField(fieldLabel = "Date of Visit Required", fieldDescription = "Please select date of visit required or not." +
			" Default set to 'Yes'", value = UKContactUsFormConstant.REQUIRED_YES_VALUE)
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Default(values = UKContactUsFormConstant.REQUIRED_YES_VALUE)
	private String dateOfVisitReq;

	@DialogField(fieldLabel = "Date error message",
			fieldDescription = "Validation message for Date of Visit field." +
					" Default set to 'Please select appropriate date '")
	@TextField
	@Inject
	@Default(values = "Please select appropriate date")
	private String dateOfVisitValidationMessage;

	@DialogField(fieldLabel = "Please provide time of visit text",
			fieldDescription = "Date of visit text 'Approximate time of visit'")
	@TextField
	@Inject
	@Default(values = "Approximate time of visit")
	private String timeOfVisit;

	@DialogField(fieldLabel = "Time of Visit Required", fieldDescription = "Please select time of visit required or not." +
			" Default set to 'Yes'", value = UKContactUsFormConstant.REQUIRED_YES_VALUE)
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Default(values = UKContactUsFormConstant.REQUIRED_YES_VALUE)
	private String timeOfVisitReq;

	@DialogField(name = "./dropDownValidationMessage", fieldLabel = "Drop-Down Error Message",
			fieldDescription = "Validation message for drop down. Default set to" +
					" 'Please select appropriate value'")
	@TextField
	@Inject @Named("dropDownValidationMessage")
	@Default(values = "Please select an appropriate option for restaurant search")
	private String dropDownButtonSelectionMessage;

	@DialogField(name = "./radioValidationMessage", fieldLabel = "Radio Button Error Message",
			fieldDescription = "Validation message for radio button. Default set to '" +
					"Please select a radio option'")
	@TextField
	@Inject @Named("radioValidationMessage")
	@Default(values = "Please select a radio option")
	private String radioButtonValidationMessage;

	@DialogField(fieldLabel = "Placeholder Text for Menu Item",
			fieldDescription = "Enter text for Menu item pleace holder. Default set to 'Please Select'")
	@TextField
	@Inject @Named("menuItemPlaceholder")
	@Default(values = "Please Select")
	private String menuItemPlaceholder = "";

	@DialogField(fieldLabel = "Time error message", fieldDescription = "Validation message for Time of Visit field. " +
			"Default set to 'Please select appropriate time '")
	@TextField
	@Inject @Named("timeOfVisitValidationMessage")
	@Default(values = "Please select appropriate time")
	private String timeOfVisitValidationMessage = "";

	private static final Logger log = LoggerFactory.getLogger(UKcontactuspersonalInfo.class);

	private List<String> dayOptions;
	private List<String> monthOptions;
	private List<String> yearOptions;
	private List<String> hourOptions;
	private List<String> minuteOptions;
	private String buttonTextYes="";
	private String buttonTextNo="";


	private String dropDownValidationMessage = "";

	@PostConstruct
	public void init() throws Exception {

		log.debug("set value from dialog properties...");

		String[] daysOption = null!=daysOptions ? this.daysOptions : null;

		if (null != daysOption) {
			dayOptions = getListOfOptionData(daysOption);
		}

		String[] monthOption = null!=monthsOptions ? this.monthsOptions : null;

		if (null != monthOption) {
			monthOptions = getListOfOptionData(monthOption);
		}

		String[] yearOption = null!=yearsOptions ? this.yearsOptions : null;

		if (null != yearOption) {
			yearOptions = getListOfOptionData(yearOption);
		}

		String[] hoursOption = null!=hoursOptions ? this.hoursOptions : null;

		if (null != hoursOption) {
			hourOptions = getListOfOptionData(hoursOption);
		}

		String[] minuteOption = null!=minutesOptions ? this.minutesOptions : null;

		if (null != minuteOption) {
			minuteOptions = getListOfOptionData(minuteOption);
		}

		dropDownValidationMessage = this.dropDownButtonSelectionMessage;

		buttonTextYes=this.yesButtonText;
		buttonTextNo=this.noButtonText;

	}

	private List<String> getListOfOptionData(String[] optionsDataArray) {
		List<String> options = new ArrayList<String>();
		for (String anOptionsDataArray : optionsDataArray) {
			options.add(anOptionsDataArray);
		}
		return options;
	}
	
	public String getRestaurantSelectionWay() {
		return restaurantSelectionWay;
	}

	public String getDateOfVisitReq() {
		return dateOfVisitReq;
	}

	public String getDateOfVisitValidationMessage() {
		return dateOfVisitValidationMessage;
	}

	public String getTimeOfVisitReq() {
		return timeOfVisitReq;
	}

	public String getDropDownValidationMessage() {
		return dropDownValidationMessage;
	}

	public String getTimeOfVisit() {
		return timeOfVisit;
	}

	public String getDateOfVisit() {
		return dateOfVisit;
	}

	public String getVisitTypeReq() {
		return visitTypeReq;
	}

	public String getDriveThru() {
		return driveThru;
	}

	public String getFrontCounter() {
		return frontCounter;
	}

	public String getKioskOrdering() {
		return kioskOrdering;
	}

	public String getMobileOrdering() {
		return mobileOrdering;
	}

	public String getTableServiceTypeReq() {
		return tableServiceTypeReq;
	}

	public String getButtonTextYes() {
		return buttonTextYes;
	}

	public String getButtonTextNo() {
		return buttonTextNo;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public String getDateText() {
		return dateOfVisit;
	}

	public String getDateValidation() {
		return dateOfVisitReq;
	}

	public String getDateValidationMessage() {
		return dateOfVisitValidationMessage;
	}

	public String getRestaurantButtonLabelSection() {
		return restaurantButtonLabelSection;
	}

	public String getMenuItem() {
		return menuItemText;
	}

	public String getRestaurantButtonText() {
		return restaurantButtonText;
	}

	public String getLocateMeButtonText() {
		return locateMeButtonText;
	}

	public String getLocatorSearchText() {
		return locateMeButtonText;
	}

	public String getMenuItemReq() {
		return menuItemValidation;
	}

	public String getMenuItemMessage() {
		return menuItemValidationMessage;
	}

	public String getRestaurantVisitText() {
		return restaurantVisitText;
	}

	public String getTableServiceText() {
		return tableServiceText;
	}

	public List<String> getDayOptions() {
		return dayOptions;
	}

	public List<String> getMonthOptions() {
		return monthOptions;
	}

	public List<String> getYearOptions() {
		return yearOptions;
	}

	public List<String> getHourOptions() {
		return hourOptions;
	}

	public List<String> getMinuteOptions() {
		return minuteOptions;
	}

	public String getProductPurchaseText() {
		return productPurchase;
	}

	public String getMenuItemPlaceholder() {
		return menuItemPlaceholder;
	}

	public String getRadioValidationMessage() {
		return radioButtonValidationMessage;
	}

	public String getTimeOfVisitValidationMessage() {
		return timeOfVisitValidationMessage;
	}
}