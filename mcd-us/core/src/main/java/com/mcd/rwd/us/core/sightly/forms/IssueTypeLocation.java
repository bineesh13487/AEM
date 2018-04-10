package com.mcd.rwd.us.core.sightly.forms;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.widgets.MultiField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
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

@Component(name = "location", value = "What Is This About Location",
		tabs = {
				@Tab( touchUINodeName = "section" , title = "What is this about Location?" )
		},
		disableTargeting = true,
		path = "content/form/issuetype",
		listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
				@Listener(name = "afterinsert", value = "REFRESH_PAGE")
		})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class IssueTypeLocation {

	@DialogField(fieldLabel = "Location Question Text",
			fieldDescription = "Location text to be displayed for location specific question. Default set to " +
					"'Is this regarding a specific location and/or visit?'")
	@TextField
	@Inject
	@Default(values = FormConstants.LOCATION_SPECIFIC_TEXT)
	private String locationText;

	@DialogField(name = "./locationYesText", fieldLabel = "Location Question Yes Text",
		fieldDescription = "Location yes text to be displayed for location specific question. Default set to 'Yes'")
	@TextField
	@Inject
	@Named("locationYesText")
	@Default(values = FormConstants.LOCATION_YES_TEXT)
	private String yesText;

	@DialogField(name = "./locationNoText", fieldLabel = "Location Question No Text",
			fieldDescription = "Location no text to be displayed for location specific question. Default set to 'No'")
	@TextField
	@Inject
	@Named("locationNoText")
	@Default(values = FormConstants.LOCATION_NO_TEXT)
	private String noText;

	@DialogField(fieldLabel = "Restaurant Search Text",
			fieldDescription = "Text to be displayed for restaurant search. Default set" +
					" to 'Search for a restaurant:'")
	@TextField
	@Inject
	@Default(values = FormConstants.RESTAURANT_SEARCH_TEXT)
	private String searchText;

	@DialogField(fieldLabel = "Restaurant Locator Button Text",
			fieldDescription = "Text to be displayed for restaurant locator button." +
					" 'Default set to Restaurant Locator'")
	@TextField
	@Inject
	@Default(values = FormConstants.RESTAURANT_LOCATOR_TEXT)
	private String buttonText;

	@DialogField(name = "./locatorSearchText", fieldLabel = "Restaurant Locator Search Text",
			fieldDescription = "Text for restaurant location search. Default set to 'Can't" +
					" find the location you're looking for?'")
	@TextField
	@Inject
	@Named("locatorSearchText")
	@Default(values = FormConstants.RESTAURANT_LOCATOR_SEARCH_TEXT)
	private String locationSearchText;

	@DialogField(name = "./resAddress", fieldLabel = "Restaurant Address Text",
			fieldDescription = "Text for restaurant address field. Default set to 'Restaurant Address: '")
	@TextField
	@Inject
	@Named("resAddress")
	@Default(values = FormConstants.RESTAURANT_ADDRESS_TEXT)
	private String address;

	@DialogField(name = "./resAddressReq", value = UKContactUsFormConstant.REQUIRED_NO_VALUE,
			fieldLabel = "Restaurant Address Required",
			fieldDescription = "Please select restaurant address is required or not." +
					" Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("resAddressReq")
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String addressValidation;

	@DialogField(name = "./resAddressMessage", fieldLabel = "Restaurant Address Validation Message",
			fieldDescription = "Validation message for restaurant address field. Default set " +
					"to 'Please enter Restaurant Address'")
	@TextField
	@Inject
	@Named("resAddressMessage")
	@Default(values = FormConstants.RESTAURANT_ADDRESS_VALIDATION_MESSAGE)
	private String addressValidationMessage;

	@DialogField(name = "./resCity", fieldLabel = "City Text",
			fieldDescription = "Text for city field. Default set to 'City *'")
	@TextField
	@Inject
	@Named("resCity")
	@Default(values = FormConstants.RESTAURANT_CITY_TEXT)
	private String city;

	@DialogField(name = "./resCityReq", value = UKContactUsFormConstant.REQUIRED_NO_VALUE,
			fieldLabel = "Restaurant City Required",
			fieldDescription = "Please select restaurant city is required or not." +
					" Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("resCityReq")
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String cityValidation;

	@DialogField(name = "./resCityMessage", fieldLabel = "City Validation Message",
			fieldDescription = "Validation message for city field. Default set to" +
					" 'Please enter Restaurant's City'")
	@TextField
	@Inject
	@Named("resCityMessage")
	@Default(values = FormConstants.RESTAURANT_CITY_VALIDATION_MESSAGE)
	private String cityValidationMessage;

	@DialogField(name = "./resStateText", fieldLabel = "State Text",
			fieldDescription = "Text for city field. Default set to 'State *'")
	@TextField
	@Inject
	@Named("resStateText")
	@Default(values = FormConstants.STATE)
	private String stateText;

	@DialogField(fieldLabel = "State",
			fieldDescription = "Please provide state name by clicking + button.")
	@MultiField
	@TextField
	@Inject
	private String[] state;

	@DialogField(name = "./resStateReq", value = FormConstants.REQUIRED_NO_VALUE,
			fieldLabel = "State Required",
			fieldDescription = "Please select state is required or not. Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("resStateReq")
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String stateValidation;

	@DialogField(name = "./resStateMessage", fieldLabel = "State Validation Message",
			fieldDescription = "Validation message for state field. Default set " +
					"to 'Please select Restaurant's State'")
	@TextField
	@Inject
	@Named("resStateMessage")
	@Default(values = FormConstants.RESTAURANT_STATE_VALIDATION_MESSAGE)
	private String stateValidationMessage;

	@DialogField(fieldLabel = "Landmark Text",
			fieldDescription = "Text for Landmark field. Default set to 'Landmark: (describe location) *'")
	@TextField
	@Inject
	@Default(values = FormConstants.RESTAURANT_LANDMARK_TEXT)
	private String landmark;

	@DialogField(name = "./landmarkReq", value = FormConstants.REQUIRED_NO_VALUE,
			fieldLabel = "Landmark Requiredd",
			fieldDescription = "Please select landmark is required or not. Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("landmarkReq")
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String landmarkValidation;

	@DialogField(name = "./landmarkMessage", fieldLabel = "Landmark Validation Message",
			fieldDescription = "Validation Message for Landmark field. Default set " +
					"to 'Please enter Restaurant's Landmark'")
	@TextField
	@Inject
	@Named("landmarkMessage")
	@Default(values = FormConstants.RESTAURANT_LANDMARK_VALIDATION_MESSAGE)
	private String landmarkValidationMessage;

	@DialogField(name = "./landmarkPopover", fieldDescription = "Title for Landmark popover.",
			fieldLabel = "Landmark Popover Title")
	@TextField
	@Inject
	@Named("landmarkPopover")
	@Default(values = StringUtils.EMPTY)
	private String landmarkPopoverTitle;

	@DialogField(name = "./landmarkPopupContent", fieldLabel = "Landmark Popover Content",
			fieldDescription = "Text for Landmark popover content.")
	@TextField
	@Inject
	@Named("landmarkPopupContent")
	@Default(values = StringUtils.EMPTY)
	private String landmarkPopoverContent;

	@DialogField(fieldLabel = "Date Text",
			fieldDescription = "Text for date field.Default set to 'Date *'")
	@TextField
	@Inject
	@Default(values = FormConstants.RESTAURANT_DATE_TEXT)
	private String date;

	@DialogField(name = "./timeOptions", fieldLabel = "Time Options",
			fieldDescription = "Please provide options for time.")
	@MultiField
	@TextField
	@Inject
	@Named("timeOptions")
	private String[] timesOptions;

	@DialogField(fieldLabel = "AM/PM Options",
			fieldDescription = "Please provide options for AM/PM.")
	@MultiField
	@TextField
	@Inject
	private String[] ampmOptions;

	@DialogField(name = "./resDateReq", value = FormConstants.REQUIRED_NO_VALUE,
			fieldLabel = "Date Required",
			fieldDescription = "Please select date is required or not. Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("resDateReq")
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String dateValidation;

	@DialogField(name = "./dateMessage", fieldLabel = "Date Validation Message",
			fieldDescription = "Validation message for date field.Default set to 'Please select Date'")
	@TextField
	@Inject
	@Named("dateMessage")
	@Default(values = FormConstants.RESTAURANT_DATE_VALIDATION_MESSAGE)
	private String dateValidationMessage;

	@DialogField(name = "./driveThru", fieldLabel = "Drive-thru Text",
			fieldDescription = "Text for Drive-thru field.Default set to 'Drive-thru *'")
	@TextField
	@Inject @Named("driveThru")
	@Default(values = FormConstants.RESTAURANT_DRIVE_THRU_TEXT)
	private String drivethru;

	@DialogField(fieldLabel = "In-Store Text",
			fieldDescription = "Text for In-Store field.Default set to 'In-Store *'")
	@TextField
	@Inject
	@Default(values = FormConstants.RESTAURANT_IN_STORE_TEXT)
	private String inStore;

	@DialogField(name = "./visitTypeReq", value = FormConstants.REQUIRED_NO_VALUE,
			fieldLabel = "Visit Type Required",
			fieldDescription = "Please select visit type is required or not. Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("visitTypeReq")
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String visitTypeValidation;

	@DialogField(fieldLabel = "Visittype Validation Message",
			fieldDescription = "Validation message for visit type field." +
					"Default set to 'Please select Visit Type'")
	@TextField
	@Inject
	@Default(values = FormConstants.RESTAURANT_VISITTYPE_VALIDATION_MESSAGE)
	private String visittypeValidationMessage;

	private static final Logger log = LoggerFactory.getLogger(IssueTypeLocation.class);

	private String locationYesText = StringUtils.EMPTY;

	private String locationNoText = StringUtils.EMPTY;

	private String locatorSearchText = StringUtils.EMPTY;

	private String resAddress = StringUtils.EMPTY;

	private String resAddressReq = StringUtils.EMPTY;

	private String resAddressMessage = StringUtils.EMPTY;

	private String resCity = StringUtils.EMPTY;

	private String resCityReq = StringUtils.EMPTY;

	private String resCityMessage = StringUtils.EMPTY;

	private String resStateText = StringUtils.EMPTY;

	private List<String> resStateOptions;

	private String resStateReq = StringUtils.EMPTY;

	private String resStateMessage = StringUtils.EMPTY;

	private String landmarkReq = StringUtils.EMPTY;

	private String landmarkMessage = StringUtils.EMPTY;

	private String landmarkPopupTitle = StringUtils.EMPTY;

	private String landmarkPopupContent = StringUtils.EMPTY;

	private String dateText = StringUtils.EMPTY;

	private String driveThru = StringUtils.EMPTY;

	private List<String> timeOptions;

	private List<String> periodOptions;

	private String dateReq = StringUtils.EMPTY;

	private String dateMessage = StringUtils.EMPTY;

	private String driveThruValue = StringUtils.EMPTY;

	private String inStoreValue = StringUtils.EMPTY;

	private String visitTypeReq = StringUtils.EMPTY;

	private String visitTypeValidationMessage = StringUtils.EMPTY;

	@PostConstruct
	public void init() throws Exception {
		log.debug("inside activate method, set value from dialog properties...");
		locationYesText = this.yesText;
		locationNoText = this.noText;
		locatorSearchText = this.locationSearchText;
		resAddress = this.address;
		resAddressReq = this.addressValidation;
		resAddressMessage = this.addressValidationMessage;
		resCity = this.city;
		resCityReq = this.cityValidation;
		resCityMessage = this.cityValidationMessage;
		resStateText = this.stateText;
		String[] resStateOptionsData = null!=state ? this.state : null;
		log.debug("resStateOptionsData: " + resStateOptionsData);
		if (null != resStateOptionsData) {
			resStateOptions = getListOfOptionData(resStateOptionsData);
		}
		resStateReq = this.stateValidation;
		resStateMessage = this.stateValidationMessage;
		landmarkReq = this.landmarkValidation;
		landmarkMessage = this.landmarkValidationMessage;
		landmarkPopupTitle = this.landmarkPopoverTitle;
		landmarkPopupContent = this.landmarkPopoverContent;
		dateText = this.date;
		dateMessage = this.dateValidationMessage;

		String[] timeOptionsData = null!=timesOptions ? this.timesOptions : null;
		log.debug("timeOptionsData: " + timeOptionsData);
		if (null != timeOptionsData) {
			timeOptions = getListOfOptionData(timeOptionsData);
		}

		String[] periodOptionsData = null!=ampmOptions ? this.ampmOptions : null;
		log.debug("periodOptionsData: " + periodOptionsData);
		if (null != periodOptionsData) {
			periodOptions = getListOfOptionData(periodOptionsData);
		}
		dateReq = this.dateValidation;
		driveThru = this.drivethru;
		driveThruValue = StringUtils.isNotEmpty(drivethru) ? this.drivethru : FormConstants.RESTAURANT_DRIVE_THRU_VALUE;
		inStoreValue = this.inStore;
		visitTypeReq = this.visitTypeValidation;
	}

	private List<String> getListOfOptionData(String[] optionsDataArray) {
		List<String> options = new ArrayList<String>();
		for (int i = 0; i < optionsDataArray.length; i++) {
			options.add(optionsDataArray[i]);
		}
		return options;
	}

	public String getLocationText() {
		return locationText.trim();
	}

	public String getLocationYesText() {
		return locationYesText.trim();
	}

	public String getLocationNoText() {
		return locationNoText.trim();
	}

	public String getSearchText() {
		return searchText.trim();
	}

	public String getButtonText() {
		return buttonText.trim();
	}

	public String getLocatorSearchText() {
		return locatorSearchText.trim();
	}

	public String getResAddress() {
		return resAddress.trim();
	}

	public String getResAddressReq() {
		return resAddressReq;
	}

	public String getResAddressMessage() {
		return resAddressMessage;
	}

	public String getResCity() {
		return resCity.trim();
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

	public List<String> getResStateOptions() {
		return resStateOptions;
	}

	public String getResStateReq() {
		return resStateReq;
	}

	public String getResStateMessage() {
		return resStateMessage.trim();
	}

	public String getLandmark() {
		return landmark.trim();
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
		return dateText.trim();
	}

	public List<String> getTimeOptions() {
		return timeOptions;
	}

	public List<String> getPeriodOptions() {
		return periodOptions;
	}

	public String getDateReq() {
		return dateReq;
	}

	public String getDateMessage() {
		return dateMessage;
	}

	public String getDriveThru() {
		return driveThru.trim();
	}

	public String getInStore() {
		return inStore.trim();
	}

	public String getVisitTypeReq() {
		return visitTypeReq;
	}

	public String getVisitTypeValidationMessage() {
		return visitTypeValidationMessage;
	}


	public String getDriveThruValue() {
		return driveThruValue;
	}

	public void setDriveThruValue(String driveThruValue) {
		this.driveThruValue = driveThruValue;
	}

	public String getInStoreValue() {
		return inStoreValue;
	}

	public void setInStoreValue(String inStoreValue) {
		this.inStoreValue = inStoreValue;
	}
}  
