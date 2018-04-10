package com.mcd.rwd.us.core.sightly.forms;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.widgets.DialogFieldSet;
import com.citytechinc.cq.component.annotations.widgets.MultiField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.mcd.rwd.global.core.components.common.Text;
import com.mcd.rwd.global.core.widget.colorpicker.ColorPicker;
import com.mcd.rwd.us.core.constants.FormConstants;
import com.mcd.rwd.us.core.constants.UKContactUsFormConstant;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

@Component(name = "personalinfo", value = "Tell us about yourself",
		tabs = {
				@Tab( touchUINodeName = "section" , title = "Tell us about yourself" )
		},
		disableTargeting = true,
		actions = { "text: Tell Us About Yourself", "-", "editannotate", "copymove", "delete" },
		group = " GWS-Global", path = "content/form",
		listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
				@Listener(name = "afterinsert", value = "REFRESH_PAGE")
		})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PersonalInfo {

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
	@Inject
	@Named("sectionSubHeading")
	@Default(values = StringUtils.EMPTY)
	private String subHeading;

	@DialogField(fieldLabel = "First Name Text", fieldDescription = "Text for first name field. Default set to 'First Name *'")
	@TextField
	@Inject
	@Default(values = FormConstants.FIRST_NAME)
	private String firstName;

	@DialogField(name = "./fnameAriaLabel", fieldLabel = "Aria Label for first name")
	@TextField
	@Inject
	@Named("fnameAriaLabel")
	@Default(values = FormConstants.FIRST_NAME)
	private String firstNameAriaLabel;

	@DialogField(name = "./firstNameReq", value = FormConstants.REQUIRED_YES_VALUE,
			fieldLabel = "First Name Required",
			fieldDescription = "Please select first name is required or not. " +
					"Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("firstNameReq")
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String firstNameValidation;

	@DialogField(name = "./firstNameMessage", fieldLabel = "First Name Validation Message",
			fieldDescription = "Validation message for first name field. " +
					"Default set to 'Please enter your First Name'")
	@TextField
	@Inject
	@Named("firstNameMessage")
	@Default(values = FormConstants.FIRST_NAME_VALIDATION_MESSAGE)
	private String firstNameValidationMessage;

	@DialogField(fieldLabel = "Last Name Text",
			fieldDescription = "Text for last name field. Default set to 'Last Name *'")
	@TextField
	@Inject
	@Default(values = FormConstants.LAST_NAME)
	private String lastName;

	@DialogField(name = "./lnameAriaLabel", fieldLabel = "Aria Label for last name")
	@TextField
	@Inject
	@Named("lnameAriaLabel")
	@Default(values = FormConstants.LAST_NAME)
	private String lastNameAriaLabel;

	@DialogField(name = "./lastNameReq", value = UKContactUsFormConstant.REQUIRED_NO_VALUE,
			fieldLabel = "Last Name Required",
			fieldDescription = "Please select last name is required or not. " +
					"Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("lastNameReq")
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String lastNameValidation;

	@DialogField(name = "./lastNameMessage", fieldLabel = "Last Name Validation Message",
			fieldDescription = "Validation message for last name field. " +
					"Default set to 'Please enter your Last Name'")
	@TextField
	@Inject
	@Named("lastNameMessage")
	@Default(values = FormConstants.LAST_NAME_VALIDATION_MESSAGE)
	private String lastNameValidationMessage;

	@DialogField(fieldLabel = "Address Text", fieldDescription = "Text for address field. " +
			"Default set to 'Address (US Only) *'")
	@TextField
	@Inject
	@Default(values = FormConstants.ADDRESS)
	private String address;

	@DialogField(fieldLabel = "Aria Label for address")
	@TextField
	@Inject
	@Default(values = FormConstants.ADDRESS)
	private String addressAriaLabel;

	@DialogField(name = "./addressReq", value = UKContactUsFormConstant.REQUIRED_NO_VALUE,
			fieldLabel = "Address Required",
			fieldDescription = "Please select address (us only) is required or not." +
					" Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("addressReq")
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String addressValidation;

	@DialogField(name = "./addressMessage", fieldLabel = "Address Validation Message",
			fieldDescription = "Validation message for address field. Default set " +
					"to 'Please enter your Address (U.S. Only)'")
	@TextField
	@Inject
	@Named("addressMessage")
	@Default(values = FormConstants.ADDRESS_VALIDATION_MESSAGE)
	private String addressValidationMessage;

	@DialogField(fieldLabel = "Apt/Suite Text", fieldDescription = "Text for apt/suite field. " +
			"Default set to 'Apt/Suite'")
	@TextField
	@Inject
	@Default(values = FormConstants.APT)
	private String apt;

	@DialogField(fieldLabel = "Aria Label for apt/suite")
	@TextField
	@Inject
	@Default(values = FormConstants.APT)
	private String aptAriaLabel;

	@DialogField(fieldLabel = "City Text",
			fieldDescription = "Text for city field. Default set to 'City *'")
	@TextField
	@Inject
	@Default(values = FormConstants.CITY)
	private String city;

	@DialogField(fieldLabel = "Aria Label for city")
	@TextField
	@Inject
	@Default(values = FormConstants.CITY)
	private String cityAriaLabel;

	@DialogField(name = "./cityReq", value = UKContactUsFormConstant.REQUIRED_NO_VALUE,
			fieldLabel = "City Required",
			fieldDescription = "Please select city is required or not." +
					" Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("cityReq")
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String cityValidation;

	@DialogField(name = "./cityMessage", fieldLabel = "City Validation Message",
			fieldDescription = "Validation message for city field." +
					" Default set to 'Please enter your City'")
	@TextField
	@Inject
	@Named("cityMessage")
	@Default(values = FormConstants.CITY_VALIDATION_MESSAGE)
	private String cityValidationMessage;

	@DialogField(fieldLabel = "State Text", fieldDescription = "Text for city field." +
			" Default set to 'State *'")
	@TextField
	@Inject
	@Default(values = FormConstants.STATE)
	private String stateText;

	@DialogField(fieldLabel = "Aria Label for state")
	@TextField
	@Inject
	@Default(values = FormConstants.STATE)
	private String stateAriaLabel;

	@DialogField(fieldLabel = "State", fieldDescription = "Please provide state name by clicking + button.")
	@MultiField @TextField
	@Inject
	private String[] state;

	@DialogField(name = "./stateReq", value = UKContactUsFormConstant.REQUIRED_NO_VALUE,
			fieldLabel = "State Required",
			fieldDescription = "Please select state is required or not." +
					" Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("stateReq")
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String stateValidation;

	@DialogField(name = "./stateMessage", fieldLabel = "State Validation Message",
			fieldDescription = "Validation message for state field. " +
					"Default set to 'Please select your State'")
	@TextField
	@Inject
	@Named("stateMessage")
	@Default(values = FormConstants.STATE_VALIDATION_MESSAGE)
	private String stateValidationMessage;

	@DialogField(fieldLabel = "Zip Text", fieldDescription = "Text for zip field." +
			"Default set to 'Zip *'")
	@TextField
	@Inject
	@Default(values = FormConstants.ZIP)
	private String zip;

	@DialogField(fieldLabel = "Aria Label for zip code")
	@TextField
	@Inject
	@Default(values = FormConstants.ZIP)
	private String zipAriaLabel;

	@DialogField(name = "./zipReq", value = UKContactUsFormConstant.REQUIRED_NO_VALUE,
			fieldLabel = "Zip Required & Number Validation",
			fieldDescription = "Please select zip required and number validation should be " +
					"applied or not. Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("zipReq")
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String zipValidation;

	@DialogField(name = "./zipMessage", fieldLabel = "Zip Validation Message",
			fieldDescription = "Validation message for zip field.Default " +
					"set to 'Please enter a valid Zip code'")
	@TextField
	@Inject
	@Named("zipMessage")
	@Default(values = FormConstants.ZIP_VALIDATION_MESSAGE)
	private String zipValidationMessage;

	@DialogField(fieldLabel = "Hear Back Yes Text",
			fieldDescription = "Text for hear back yes field. Default set to 'Please indicate whether" +
					" you would like to hear back from the Owner’s organization.'")
	@TextField
	@Inject
	@Default(values = FormConstants.HEAR_BACK_YES)
	private String hearBackYes;

	@DialogField(fieldLabel = "Yes Text",
			fieldDescription = "Radio button yes text to be displayed for specific question. Default set to 'Yes'")
	@TextField
	@Inject
	@Default(values = FormConstants.LOCATION_YES_TEXT)
	private String yesText;

	@DialogField(fieldLabel = "Hear Back No Text",
			fieldDescription = "Text for hear back no field.Default set to 'No (Please know there may be instances where we need to contact" +
					" you for additional information regarding this matter)'")
	@TextField
	@Inject
	@Default(values = FormConstants.HEAR_BACK_NO)
	private String hearBackNo;

	@DialogField(fieldLabel = "No Text", fieldDescription = "Radio button no text to be displayed for specific " +
			"question. Default set to 'No'")
	@TextField
	@Inject
	@Default(values = FormConstants.LOCATION_NO_TEXT)
	private String noText;

	@DialogField(name = "./hearBackReq", value = UKContactUsFormConstant.REQUIRED_NO_VALUE,
			fieldLabel = "Hear Back Required",
			fieldDescription = "Please select hear back ( follow up) is required " +
					"or not. Default set to 'No'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("hearBackReq")
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String hearbackValidation;

	@DialogField(name = "./hearBackMessage", fieldLabel = "Hear Back Validation Message",
			fieldDescription = "Validation message for hear back field. Default set to" +
					" 'Please select Request Followup option'")
	@TextField
	@Inject
	@Named("hearBackMessage")
	@Default(values = FormConstants.HEAR_BACK_VALIDATION_MESSAGE)
	private String hearBackValidationMessage;

	@DialogField(fieldLabel = "Email Text",
			fieldDescription = "Text for email field.Default set to 'Email *'")
	@TextField
	@Inject
	@Default(values = FormConstants.EMAIL)
	private String email;

	@DialogField(fieldLabel = "Aria Label for email")
	@TextField
	@Inject
	@Default(values = FormConstants.EMAIL)
	private String emailAriaLabel;

	@DialogField(name = "./emailReq", value = UKContactUsFormConstant.REQUIRED_NO_VALUE,
			fieldLabel = "Email Required & Format Validation",
			fieldDescription = "Please select email required and formatvalidation should be " +
					"applied or not. Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("emailReq")
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String emailValidation;

	@DialogField(name = "./emailMessage", fieldLabel = "Email Validation Message",
			fieldDescription = "Validation message for email field.Default set to 'Please enter" +
					" (valid) 'Your e-mail address' Information'")
	@TextField
	@Inject
	@Named("emailMessage")
	@Default(values = FormConstants.EMAIL_VALIDATION_MESSAGE)
	private String emailValidationMessage;


	@DialogField(fieldLabel = "Confirm Email Text", fieldDescription = "Text for confirm email field." +
			"Default set to 'Confirm Email *'")
	@TextField
	@Inject
	@Default(values = FormConstants.CONFIRM_EMAIL)
	private String confirmEmail;

	@DialogField(fieldLabel = "Aria Label for Confirm Email")
	@TextField
	@Inject
	@Default(values = FormConstants.CONFIRM_EMAIL)
	private String confirmAriaLabel;

	@DialogField(name = "./confirmEmailReq", value = UKContactUsFormConstant.REQUIRED_NO_VALUE,
			fieldLabel = "Confirm Email Required",
			fieldDescription = "Please select confirm email is required " +
					"or not. Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("confirmEmailReq")
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String confirmEmailValidation;

	@DialogField(name = "./confirmEmailMessage", fieldLabel = "Confirm Email Validation Message",
			fieldDescription = "Validation message for confirm email field." +
					"Default set to 'Please confirm that both E-mail Addresses are the same'")
	@TextField
	@Inject
	@Named("confirmEmailMessage")
	@Default(values = FormConstants.CONFIRM_EMAIL_VALIDATION_MESSAGE)
	private String confirmEmailValidationMessage;

	@DialogField(fieldLabel = "Phone Number Text", fieldDescription = "Text for phone number field. " +
			"Default set to 'Phone Number *'")
	@TextField
	@Inject
	@Default(values = FormConstants.PHONE_NUMBER)
	private String phoneNumber;

	@DialogField(fieldLabel = "Aria Label for phone")
	@TextField
	@Inject
	@Default(values = FormConstants.PHONE_NUMBER)
	private String phoneAriaLabel;

	@DialogField(name = "./phoneReq", value = UKContactUsFormConstant.REQUIRED_NO_VALUE,
			fieldLabel = "Phone Required & Number Validation",
			fieldDescription = "Please select phone required and number validation should be" +
					" applied or not. Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("phoneReq")
	@Default(values = FormConstants.REQUIRED_YES_VALUE)
	private String phoneValidation;

	@DialogField(name = "./phoneNumberMessage", fieldLabel = "Phone Number Validation Message",
			fieldDescription = "Validation message for phone number field." +
					" Default set to 'Please enter valid Phone Number'")
	@TextField
	@Inject
	@Named("phoneNumberMessage")
	@Default(values = FormConstants.PHONE_NUMBER_VALIDATION_MESSAGE)
	private String phoneNumberValidationMessage;

	@DialogField(fieldLabel = "Disclaimer Text", fieldDescription = "Disclaimer text to be displayed at end of form.")
	@DialogFieldSet(title = "Disclaimer Text", namePrefix = "disclaimerText/")
	@ChildResource(name = "disclaimerText")
	@Inject @Named("disclaimerText")
	Text disclaimerData;

	@DialogField(name = "./captchaOptionReq", value = UKContactUsFormConstant.REQUIRED_NO_VALUE,
			fieldLabel = "Captcha Option",
			fieldDescription = "Please select captcha option is required or" +
					" not. Default set to 'reCaptcha'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="reCaptcha" , value="reCaptcha"),
					@Option(text="Honeypot" , value="Honeypot"),
					@Option(text="None", value="None")
			})
	@Inject @Named("captchaOptionReq")
	@Default(values = FormConstants.CAPTCHA_OPTION_REQ)
	private String captchaOption;

	@DialogField(fieldLabel = "Captcha Validation Text", fieldDescription = "Text for captcha validation. " +
			"Default set to 'Please select captcha.'")
	@TextField
	@Inject
	@Default(values = FormConstants.CAPTCHA_VALIDATION_TEXT)
	private String captchaText;

	@DialogField(name = "./sectionBGColor", fieldLabel = "Background Color",
			fieldDescription = "This will change the background color of section on selection of this category.")
	@ColorPicker
	@Inject @Named("sectionBGColor")
	@Default(values = StringUtils.EMPTY)
	private String bgcolor;

	@DialogField(name = "./contText", fieldLabel = "Continue Button Text",
			fieldDescription = "Text to be displayed for continue button. Default set to 'Continue'")
	@TextField
	@Inject
	@Named("contText")
	@Default(values = FormConstants.CONTINUE_BUTTON_TEXT)
	private String continuee;

    @DialogField(fieldLabel = "ADA \"I would like to hear back\" Text",
            fieldDescription = "ADA text for radio. Default set to 'I would like to hear back from the owner's organization'")
    @TextField
    @Inject
    @Default(values = "I would like to hear back from the owner's organization")
    private String adaTextForOwnerHearbackYesRadioButton;


    @DialogField( fieldLabel = "ADA \"I would not like to hear back\" Text",
            fieldDescription = "ADA text for radio. Default set to 'I would not like to hear back from the owner's organization. Please know there may be instances where we need to contact you for additional information regarding this matter/b>")
    @TextField
    @Inject
    @Default(values = "I would not like to hear back from the owner's organization. Please know there may be instances where we need to contact you for additional information regarding this matter")
    private String adaTextForOwnerHearbackNoRadioButton;

	private static final Logger log = LoggerFactory.getLogger(PersonalInfo.class);

	private String sectionTopHeading = "";

	private String sectionSubHeading = "";

	private String firstNameReq = "";

	private String firstNameMessage = "";

	private String lastNameReq = "";

	private String lastNameMessage = "";

	private String addressReq = "";

	private String addressMessage = "";

	private String apartment = "";

	private String cityReq = "";

	private String cityMessage = "";

	private Map<String, String> stateOptions;
	private String stateReq = "";

	private String stateMessage = "";

	private String zipReq = "";

	private String zipMessage = "";

	private String hearBackReq = "";

	private String hearBackMessage = "";

	private String emailReq = "";

	private String emailMessage = "";

	private String confirmEmailReq = "";

	private String confirmEmailMessage = "";

	private String phoneNumberReq = "";

	private String phoneNumberMessage = "";

	// backlog requirement
	private String captchaOptionReq = "";

	private String sectionBackgroundColor = "";

	private String continueText = "";

	//Aria Label Requirement
	private String fNameAriaLabel = "";

	private String lNameAriaLabel = "";

	private String disclaimerText = "";

	@PostConstruct
	public void init() throws Exception {

		StateInfo stateInfo = new StateInfo();
		log.debug("set value from dialog properties...");
		sectionTopHeading = this.topHeading;
		sectionSubHeading = this.subHeading;
		disclaimerText = null!=disclaimerData ? disclaimerData.getText() : StringUtils.EMPTY;

		firstNameReq = this.firstNameValidation;
		firstNameMessage = this.firstNameValidationMessage;
		fNameAriaLabel =  this.firstNameAriaLabel;

		lastNameReq = this.lastNameValidation;
		lastNameMessage = this.lastNameValidationMessage;
		lNameAriaLabel = this.lastNameAriaLabel;

		addressReq = this.addressValidation;
		addressMessage = this.addressValidationMessage;
		apartment = this.apt;

		cityReq = this.cityValidation;
		cityMessage = this.cityValidationMessage;

		String[] stateOptionsData = null!=state ? this.state : null;
		if (null != stateOptionsData) {
			stateOptions = new HashMap<String, String>();

			for (int i = 0; i < stateOptionsData.length; i++) {
				String options = stateOptionsData[i];
				String stateName = stateInfo.getStateName(options);
				log.debug("stateOptions: "+ options + stateName);
				stateOptions.put(options, stateName);
			}
		}

		stateReq = this.stateValidation;
		stateMessage = this.stateValidationMessage;

		zipReq = this.zipValidation;
		zipMessage =this.zipValidationMessage;

		hearBackReq = this.hearbackValidation;
		hearBackMessage = this.hearBackValidationMessage;

		emailReq = this.emailValidation;
		emailMessage = this.emailValidationMessage;

		confirmEmailReq = this.confirmEmailValidation;
		confirmEmailMessage = this.confirmEmailValidationMessage;

		phoneNumberReq = this.phoneValidation;
		phoneNumberMessage = this.phoneNumberValidationMessage;

		// Backlog Requirement
		captchaOptionReq= this.captchaOption;

		sectionBackgroundColor = this.bgcolor;
		/*if (!"".equals(sectionBackgroundColor)) {
			sectionBackgroundColor = "#" + sectionBackgroundColor;
		}*/
		continueText = this.continuee;

	}

	public String getSectionTopHeading() {
		return sectionTopHeading.trim();
	}

	public String getSectionSubHeading() {
		return sectionSubHeading.trim();
	}

	public String getFirstName() {
		return firstName.trim();
	}

	public String getFirstNameReq() {
		return firstNameReq;
	}

	public String getFirstNameMessage() {
		return firstNameMessage.trim();
	}

	public String getLastName() {
		return lastName.trim();
	}

	public String getLastNameReq() {
		return lastNameReq;
	}

	public String getLastNameMessage() {
		return lastNameMessage.trim();
	}

	public String getAddress() {
		return address.trim();
	}

	public String getAddressReq() {
		return addressReq;
	}

	public String getAddressMessage() {
		return addressMessage.trim();
	}

	public String getApartment() {
		return apartment.trim();
	}

	public String getCity() {
		return city.trim();
	}

	public String getCityReq() {
		return cityReq;
	}

	public String getCityMessage() {
		return cityMessage.trim();
	}

	public String getStateText() {
		return stateText.trim();
	}

	public Map<String, String> getStateOptions() {
		return stateOptions;
	}

	public String getStateReq() {
		return stateReq;
	}

	public String getStateMessage() {
		return stateMessage.trim();
	}

	public String getZip() {
		return zip.trim();
	}

	public String getZipReq() {
		return zipReq;
	}

	public String getZipMessage() {
		return zipMessage.trim();
	}

	public String getHearBackYes() {
		return hearBackYes.trim();
	}

	public String getHearBackNo() {
		return hearBackNo.trim();
	}

	public String getYesText() {
		return yesText.trim();
	}

	public String getNoText() {
		return noText.trim();
	}

	public String getHearBackReq() {
		return hearBackReq;
	}

	public String getHearBackMessage() {
		return hearBackMessage.trim();
	}

	public String getEmail() {
		return email.trim();
	}

	public String getEmailReq() {
		return emailReq;
	}

	public String getEmailMessage() {
		return emailMessage.trim();
	}

	public String getConfirmEmail() {
		return confirmEmail.trim();
	}

	public String getConfirmEmailReq() {
		return confirmEmailReq;
	}

	public String getConfirmEmailMessage() {
		return confirmEmailMessage.trim();
	}

	public String getPhoneNumber() {
		return phoneNumber.trim();
	}

	public String getPhoneNumberReq() {
		return phoneNumberReq;
	}

	public String getPhoneNumberMessage() {
		return phoneNumberMessage.trim();
	}

	//Backlog Requirement
	public String getCaptchaOptionReq() {
		return captchaOptionReq.trim();
	}

	public String getCaptchaText() {
		return captchaText.trim();
	}

	public String getSectionBackgroundColor() {
		return sectionBackgroundColor;
	}

	public String getContinueText() {
		return continueText.trim();
	}

	public String getDisclaimerText() {
		return disclaimerText;
	}

	//For Aria Label

	public String getFNameAriaLabel() {
		return fNameAriaLabel;
	}

	public String getLNameAriaLabel() {
		return lNameAriaLabel;
	}

	public String getPhoneAriaLabel() {
		return phoneAriaLabel;
	}

	public String getConfirmAriaLabel() {
		return confirmAriaLabel;
	}

	public String getEmailAriaLabel() {
		return emailAriaLabel;
	}

	public String getZipAriaLabel() {
		return zipAriaLabel;
	}

	public String getStateAriaLabel() {
		return stateAriaLabel;
	}

	public String getCityAriaLabel() {
		return cityAriaLabel;
	}

	public String getAddressAriaLabel() {
		return addressAriaLabel;
	}

	public String getAptAriaLabel() {
		return aptAriaLabel;
	}	
	
	public String getAdaTextForOwnerHearbackYesRadioButton() {
		return adaTextForOwnerHearbackYesRadioButton;
	}

	public String getAdaTextForOwnerHearbackNoRadioButton() {
		return adaTextForOwnerHearbackNoRadioButton;
	}

}
