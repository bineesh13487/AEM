package com.mcd.rwd.us.core.sightly.forms;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Listener;
import com.citytechinc.cq.component.annotations.Option;
import com.citytechinc.cq.component.annotations.widgets.MultiField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.mcd.rwd.global.core.widget.colorpicker.ColorPicker;
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
import java.util.*;

@Component(
		name = "restaurantinfo",
		value = "Restaurant Information",
		actions = { "text: Restaurant Information", "-", "editannotate", "copymove", "delete" },
		disableTargeting = true,
		listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
				@Listener(name = "afterinsert", value = "REFRESH_PAGE")},
		path = "content/form/",
		group = " GWS-Global")
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RestaurantInfo {

	private static final Logger log = LoggerFactory.getLogger(RestaurantInfo.class);

	@DialogField(fieldLabel = "Section Top Heading", fieldDescription = "Heading to be displayed on top of section.")
	@TextField
	@Inject
	private String sectionTopHeading;


	@DialogField(fieldLabel = "Section Sub Heading", fieldDescription = "Sub heading to be displayed on top of section.")
	@TextField
	@Inject
	private String sectionSubHeading;

	@DialogField(fieldLabel = "Restaurant Locator Button Text:", fieldDescription = "Text to be displayed for restaurant locator button. 'Default set to Restaurant Locator'")
	@TextField
	@Inject @Default(values = FormConstants.RESTAURANT_LOCATOR_TEXT)
	private String buttonText;


	@DialogField(fieldLabel = "Restaurant Locator Search Text", fieldDescription = "Text for restaurant location search. Default set to 'Can't find the location you're looking for?'")
	@TextField
	@Inject @Default(values =  FormConstants.RESTAURANT_LOCATOR_SEARCH_TEXT)
	private String locatorSearchText;


	@DialogField(fieldLabel = "Restaurant Address Text", fieldDescription = "Text for restaurant address field. Default set to 'Restaurant Address: *'")
	@TextField
	@Inject @Default(values = FormConstants.RESTAURANT_ADDRESS_TEXT)
	private String resAddress;

	@DialogField(fieldLabel="Restaurant Address Required", fieldDescription="Please select restaurant address is required or not. Default set to 'Yes'", value="tableView", defaultValue="tableView")
	@Selection(options = {
			@Option(text = "Yes", value = "yes"),
			@Option(text = "No", value = "no")
	}, type = Selection.SELECT)
	@Inject @Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String resAddressReq;

	@DialogField(fieldDescription="Validation message for restaurant address field. Default set to 'Please enter Restaurant Address'",fieldLabel="Restaurant Address Validation Message")
	@TextField
	@Inject @Default(values = FormConstants.RESTAURANT_ADDRESS_VALIDATION_MESSAGE)
	private String resAddressMessage;

	@DialogField( fieldDescription="Text for city field. Default set to 'City *'", fieldLabel="City Text")
	@TextField
	@Inject @Default(values = FormConstants.RESTAURANT_CITY_TEXT)
	private String resCity;

	@DialogField(defaultValue="yes", fieldDescription="Please select restaurant city is required or not. Default set to 'Yes'",fieldLabel="Restaurant City Required")
	@Selection(options = {
			@Option(text = "Yes", value = "yes"),
			@Option(text = "No", value = "no")
	}, type = Selection.SELECT)
	@Inject @Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String resCityReq;

	@DialogField( fieldDescription="Validation message for city field. Default set to 'Please enter Restaurant's City'", fieldLabel="City Validation Message")
	@TextField
	@Inject @Default(values = FormConstants.RESTAURANT_CITY_VALIDATION_MESSAGE)
	private String resCityMessage;

	@DialogField( fieldDescription="Text for city field. Default set to 'State *'", fieldLabel="State Text")
	@TextField
	@Inject @Default(values = FormConstants.STATE)
	private String resStateText;


	@DialogField(fieldLabel = "State", fieldDescription = "Please provide state name by clicking + button.")
	@MultiField
	@TextField
	@Inject @Named("state")
	private String[] resStateOptionsData;

	@DialogField(defaultValue="yes",fieldDescription="Please select state is required or not. Default set to 'Yes'", fieldLabel="State Required")
	@Selection(options = {
			@Option(text = "Yes", value = "yes"),
			@Option(text = "No", value = "no")
	}, type = Selection.SELECT)
	@Inject @Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String resStateReq;

	@DialogField( fieldDescription="Validation message for state field. Default set to 'Please select Restaurant's State'",fieldLabel="State Validation Message")
	@TextField
	@Inject @Default(values = FormConstants.RESTAURANT_STATE_VALIDATION_MESSAGE)
	private String resStateMessage;

	@DialogField( fieldDescription="Text for Landmark field. Default set to 'Landmark: (describe location) *'",fieldLabel="Landmark Text")
	@TextField
	@Inject @Default(values = FormConstants.RESTAURANT_LANDMARK_TEXT)
	private String landmark;

	@DialogField(defaultValue="yes",fieldDescription="Please select landmark is required or not. Default set to 'Yes'",fieldLabel="Landmark Required")
	@Selection(options = {
			@Option(text = "Yes", value = "yes"),
			@Option(text = "No", value = "no")
	}, type = Selection.SELECT)
	@Inject @Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String landmarkReq;

	@DialogField( fieldDescription="Validation Message for Landmark field. Default set to 'Please enter Restaurant's Landmark'",fieldLabel="Landmark Validation Message")
	@TextField
	@Inject @Default(values = FormConstants.RESTAURANT_LANDMARK_VALIDATION_MESSAGE)
	private String landmarkMessage;

	@DialogField(name = "./landmarkPopover", fieldDescription="Title for Landmark popover.",fieldLabel="Landmark Popover Title")
	@TextField
	@Inject @Named("landmarkPopover")
	private String landmarkPopoverTitle;

	@DialogField( name = "./landmarkPopupContent", fieldDescription="Text for Landmark popover content.",fieldLabel="Landmark Popover Content")
	@TextField
	@Inject @Named("landmarkPopupContent")
	private String landmarkPopoverContent;

	@DialogField(  fieldDescription="Text for date field.Default set to 'Date *'",fieldLabel="Date Text")
	@TextField
	@Inject @Named("date") @Default(values = FormConstants.RESTAURANT_DATE_TEXT)
	private String dateText;

	@DialogField(fieldLabel = "Time Options", fieldDescription = "Please provide options for time.")
	@MultiField
	@TextField
	@Inject
	private String[] timeOptions;

	@DialogField(fieldLabel = "AM/PM Options", fieldDescription = "Please provide options for AM/PM.")
	@MultiField
	@TextField
	@Inject
	private String[] ampmOptions;

	@DialogField(fieldDescription="Text for Drive-thru field.Default set to 'Drive-thru *'",fieldLabel="Drive-thru Text")
	@TextField
	@Inject	@Default(values = FormConstants.RESTAURANT_DRIVE_THRU_VALUE)
	private String driveThru;

	@DialogField(fieldDescription="Text for In-Store field.Default set to 'In-Store *'",fieldLabel="In-Store Text")
	@TextField
	@Inject @Default(values = FormConstants.RESTAURANT_IN_STORE_VALUE)
	private String inStore;

	@DialogField(defaultValue="yes",fieldDescription="Please select visit type is required or not. Default set to 'Yes'",fieldLabel="Visit Type Required")
	@Selection(options = {
			@Option(text = "Yes", value = "yes"),
			@Option(text = "No", value = "no")
	}, type = Selection.SELECT)
	@Inject @Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String visitTypeReq;

	@DialogField(fieldDescription="Validation message for visit type field.Default set to 'Please select Visit Type'",fieldLabel="Visit type Validation Message")
	@TextField
	@Inject @Default(values =  FormConstants.RESTAURANT_VISITTYPE_VALIDATION_MESSAGE)
	private String visitTypeValidationMessage;


	@DialogField( fieldDescription="This will change the background color of section on selection of this category.",fieldLabel="Background Color")
	@ColorPicker
	@Inject @Named(value = "sectionBGColor")
	String sectionBackgroundColor;

	@DialogField(name = "./resDateReq", defaultValue="yes",fieldDescription="Please select date is required or not. Default set to 'Yes'",fieldLabel="Date Visit Required")
	@Selection(options = {
			@Option(text = "Yes", value = "yes"),
			@Option(text = "No", value = "no")
	}, type = Selection.SELECT)
	@Inject @Named("resDateReq") @Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String dateOfVisitReq;

	@DialogField(name = "./dateMessage",  fieldDescription="Validation message for Date of Visit field. Default set to 'Please select appropriate date'",
			fieldLabel = "Date error message")
	@TextField @Named("dateMessage")
	@Inject @Default(values = FormConstants.RESTAURANT_DATE_VALIDATION_MESSAGE)
	private String dateOfVisitValidationMessage;

	@DialogField(fieldDescription="Ada Text for day field. Default set to 'Day' " +
			"Value will be considered as a Representative Value for field", fieldLabel="Ada Text for day field")
	@TextField
	@Inject @Default(values = "Day")
	private String adaTextForDayField;

	@DialogField(fieldDescription="Ada Text for month field. Default set to 'Month' " +
			"Value will be considered as a Representative Value for field", fieldLabel="Ada Text for month field")
	@TextField
	@Inject @Default(values = "Month")
	private String adaTextForMonthField;

	@DialogField(fieldDescription="Ada Text for year field. Default set to 'Year' " +
			"Value will be considered as a Representative Value for field", fieldLabel="Ada Text for year field")
	@TextField
	@Inject @Default(values = "Year")
	private String adaTextForYearField;

	@DialogField(fieldDescription="Please provide generalise drop-down validation message. Default set to " +
			"'Please select an appropriate value from drop-down'",
			fieldLabel="Generalise drop-down validation message")
	@TextField
	@Inject @Default(values = "Please select an appropriate value from drop-down")
	private String dropDownValidationText;

	@Inject
	ValueMap valueMap;

	private Map<String, String> resStateOptions;

	private String landmarkPopupTitle = StringUtils.EMPTY;

	private String driveThruValue = StringUtils.EMPTY;

	private String inStoreValue = StringUtils.EMPTY;

	private String landmarkPopupContent = StringUtils.EMPTY;

	private String locateMeButtonText= "Locate Me";

	private String dateReq = StringUtils.EMPTY;

	private String dateMessage = StringUtils.EMPTY;

	private List<String> dayOptions;

	private String[] periodOptions;

	@PostConstruct
	public void activate() throws Exception {
		log.debug("inside activate method...");
		log.debug("set the values from dialog properties");
		if (null != resStateOptionsData) {
			resStateOptions = new HashMap<String, String>();
			StateInfo stateInfo = new StateInfo();
			for (int i = 0; i < resStateOptionsData.length; i++) {
				String options = resStateOptionsData[i];
				String stateName = stateInfo.getStateName(options);
				log.debug("resStateOptions: "+ options + stateName);
				resStateOptions.put(options, stateName);
				this.landmarkPopupTitle = this.landmarkPopoverTitle;
				this.landmarkPopupContent = this.landmarkPopoverContent;
				this.periodOptions = this.ampmOptions;
				this.dateReq = this.dateOfVisitReq;
				this.dateMessage = this.dateOfVisitValidationMessage;
			}
		}

		driveThru =  driveThru + ("yes".equals(visitTypeReq) ? "*" : "");
		inStore = inStore + ("yes".equals(visitTypeReq) ? "*" : "");
		driveThruValue = driveThru;
		inStoreValue = inStore;

/*		if (!StringUtils.EMPTY.equals(sectionBackgroundColor)) {
			sectionBackgroundColor = "#" + sectionBackgroundColor;
		}*/

		String[] daysOptionValue = new String[] {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
		dayOptions = getListOfOptionData(daysOptionValue);

		log.debug("activate method end...");
	}

	private List<String> getListOfOptionData(String[] optionsDataArray) {
		List<String> options = new ArrayList<String>();
		for (String anOptionsDataArray : optionsDataArray) {
			options.add(anOptionsDataArray);
		}
		return options;
	}

	public String getDropDownValidationText() {
		return dropDownValidationText;
	}

	public String getAdaTextForDayField() {
		return adaTextForDayField;
	}

	public String getAdaTextForMonthField() {
		return adaTextForMonthField;
	}

	public String getAdaTextForYearField() {
		return adaTextForYearField;
	}

	public String getSectionTopHeading() {
		return sectionTopHeading;
	}

	public String getSectionSubHeading() {
		return sectionSubHeading;
	}

	public String getButtonText() {
		return buttonText;
	}

	public String getLocatorSearchText() {
		return locatorSearchText;
	}

	public String getResAddress() {
		return resAddress;
	}

	public String getResAddressReq() {
		return resAddressReq;
	}

	public String getResAddressMessage() {
		return resAddressMessage;
	}

	public String getResCity() {
		return resCity;
	}

	public String getResCityReq() {
		return resCityReq;
	}

	public String getResCityMessage() {
		return resCityMessage;
	}

	public String getResStateText() {
		return resStateText.trim();
	}

	public Map<String, String> getResStateOptions() {
		return resStateOptions;
	}

	public String getResStateReq() {
		return resStateReq;
	}

	public String getResStateMessage() {
		return resStateMessage.trim();
	}

	public String getLandmark() {
		return landmark;
	}

	public String getLandmarkReq() {
		return landmarkReq;
	}

	public String getLandmarkMessage() {
		return landmarkMessage;
	}

	public String getLandmarkPopupTitle() {
		return landmarkPopupTitle;
	}

	public String getLandmarkPopupContent() {
		return landmarkPopupContent;
	}

	public String getDateText() {
		return dateText;
	}

	public List<String> getTimeOptions() {
		return Arrays.asList(timeOptions);
	}

	public List<String> getPeriodOptions() {
		return Arrays.asList(periodOptions);
	}

	public String getDriveThru() {
		return driveThru;
	}

	public String getInStore() {
		return inStore;
	}

	public String getVisitTypeReq() {
		return visitTypeReq;
	}

	public String getVisitTypeValidationMessage() {
		return visitTypeValidationMessage;
	}

	public String getSectionBackgroundColor() {
		return sectionBackgroundColor;
	}

	public String getDriveThruValue() {
		return driveThruValue;
	}

	public String getInStoreValue() {
		return inStoreValue;
	}

	public String getLocateMeButtonText() {
		return locateMeButtonText;
	}

	public List<String> getDayOptions() {
		return dayOptions;
	}

	public String getDateOfVisitReq() {
		return dateReq;
	}

	public String getDateOfVisitValidationMessage() {
		return dateMessage;
	}

}
