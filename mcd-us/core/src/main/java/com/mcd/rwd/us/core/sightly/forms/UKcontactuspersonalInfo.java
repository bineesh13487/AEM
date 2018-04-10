package com.mcd.rwd.us.core.sightly.forms;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.widgets.MultiField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.mcd.rwd.us.core.constants.UKContactUsFormConstant;
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
import java.util.ArrayList;
import java.util.List;

@Component(name = "ukContactusPersonalInformation", value = "UK-contactus-personalinformation",
		description = "UK-contactus-personalinformation",
		tabs = {
				@Tab( touchUINodeName = "section" , title = "Contact us Personal Information Tab" )
		},
		actions = { "text: Contactus UK personal information", "-", "editannotate"},
		group = ".hidden", path = "content/ukForm",
		listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
				@Listener(name = "afterinsert", value = "REFRESH_PAGE")})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class UKcontactuspersonalInfo {

	@DialogField(value = "Title ", fieldLabel = "Title Text", fieldDescription = "Text for title field." +
			" Default set to 'Title '")
	@TextField
	@Inject
	@Default(values = "")
	private String titleText;

	@DialogField(fieldLabel = "Title",
			fieldDescription = "Please provide title by clicking + button. First provided " +
					"value will be considered as a Representative Value")
	@MultiField
	@TextField
	@Inject
	private String[] title;

	@DialogField(name = "./titleReq", value = UKContactUsFormConstant.REQUIRED_YES_VALUE,
			fieldLabel = "Title Required", fieldDescription = "Please select title is required or not. " +
			"Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("titleReq")
	@Default(values = UKContactUsFormConstant.REQUIRED_YES_VALUE)
	private String titleValidation;

	@DialogField(name = "./titleMessage", fieldLabel = "Title Validation Message",
			fieldDescription = "Validation message for Title field. Default set to 'Please select your title '")
	@TextField
	@Inject @Named("titleMessage")
	@Default(values = UKContactUsFormConstant.TITLE_VALIDATION_MESSAGE)
	private String titleValidationMessage;

	@DialogField(name = "./firstName", value = "First Name ", fieldLabel = "first name Text",
			fieldDescription = "Text for first name field. Default set to 'First Name '")
	@TextField
	@Inject @Named("firstName")
	@Default(values = UKContactUsFormConstant.FIRSTNAME)
	private String firstNameText;

	@DialogField(name = "./firstNameReq", value = UKContactUsFormConstant.REQUIRED_YES_VALUE,
			fieldLabel = "First Name Required", fieldDescription = "Please select First Name is " +
			"required or not. Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("firstNameReq")
	@Default(values = UKContactUsFormConstant.REQUIRED_YES_VALUE)
	private String firstNameValidation;

	@DialogField(name = "./firstNameMessage", fieldLabel = "First Name Validation Message",
			fieldDescription = "Validation message for First Name field." +
					" Default set to 'Please enter your First Name '")
	@TextField
	@Inject @Named("firstNameMessage")
	@Default(values = UKContactUsFormConstant.FIRSTNAME_VALIDATION_MESSAGE)
	private String firstNameValidationMessage;

	@DialogField(value = "Last Name ", name = "./lastName", fieldLabel = "last name Text",
			fieldDescription = "Text for last name field. Default set to 'Last Name '")
	@TextField
	@Inject @Named("lastName")
	@Default(values = UKContactUsFormConstant.LASTNAME)
	private String lastNameText;

	@DialogField(name = "./lastnameReq", value = UKContactUsFormConstant.REQUIRED_YES_VALUE,
			fieldLabel = "Last Name Required", fieldDescription = "Please select Last Name is required" +
			" or not. Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("lastnameReq")
	@Default(values = UKContactUsFormConstant.REQUIRED_YES_VALUE)
	private String lastNameValidation;

	@DialogField(name = "./lastNameMessage", fieldLabel = "Last Name Validation Message",
			fieldDescription = "Validation message for Last Name field. Default set to 'Please enter valid Last Name'")
	@TextField
	@Inject @Named("lastNameMessage")
	@Default(values = UKContactUsFormConstant.LASTNAME_VALIDATION_MESSAGE)
	private String lastNameValidationMessage;

	@DialogField(value = "Address ", name = "./address", fieldLabel = "Address",
			fieldDescription = "Text for Address field. Default set to 'Addesss '")
	@TextField
	@Inject @Named("address")
	@Default(values = UKContactUsFormConstant.ADDRESS)
	private String addressText;

	@DialogField(name = "./addressReq", value = UKContactUsFormConstant.REQUIRED_YES_VALUE,
			fieldLabel = "Address Required", fieldDescription = "Please select Address is required " +
			"or not. Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("addressReq")
	@Default(values = UKContactUsFormConstant.REQUIRED_YES_VALUE)
	private String addressValidation;

	@DialogField(name = "./addressMessage", fieldLabel = "Addess Validation Message",
			fieldDescription = "Validation message for Addess field. Default set to" +
					" 'Please enter your First Line of Addess'")
	@TextField
	@Inject @Named("addressMessage")
	@Default(values = UKContactUsFormConstant.ADDRESS_VALIDATION_MESSAGE)
	private String addressValidationMessage;

	@DialogField(value = "Postcode ", name = "./postcode", fieldLabel = "postcode Text",
			fieldDescription = "Text for postcode field. Default set to 'Postcode '")
	@TextField
	@Inject @Named("postcode")
	@Default(values = UKContactUsFormConstant.POSTCODE)
	private String postcodeText;

	@DialogField(name = "./postcodeReq", value = UKContactUsFormConstant.REQUIRED_YES_VALUE,
			fieldLabel = "postcode Required", fieldDescription = "Please select postcode is required " +
			"or not. Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("postcodeReq")
	@Default(values = UKContactUsFormConstant.REQUIRED_YES_VALUE)
	private String postcodeValidation;

	@DialogField(name = "./postcodeMessage", fieldLabel = "Post Code Validation Message",
			fieldDescription = "Validation message for postcode field. Default set to " +
					"'Please Enter Valid Postcode with a space e.g. N2 8AW. '")
	@TextField
	@Inject @Named("postcodeMessage")
	@Default(values = UKContactUsFormConstant.POSTCODE_VALIDATION_MESSAGE)
	private String postcodeValidationMessage;

	@DialogField(value = "Phone ", name = "./phone", fieldLabel = "Phone Number Text",
			fieldDescription = "Text for phone number field. Default set to 'Phone '")
	@TextField
	@Inject @Named("phone")
	@Default(values = UKContactUsFormConstant.PHONE)
	private String phoneText;

	@DialogField(name = "./phoneReq", value = UKContactUsFormConstant.REQUIRED_YES_VALUE,
			fieldLabel = "phone Required", fieldDescription = "Please select phone is required or not. " +
			"Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("phoneReq")
	@Default(values = UKContactUsFormConstant.REQUIRED_YES_VALUE)
	private String phoneValidation;

	@DialogField(name = "./phoneMessage", fieldLabel = "phone Validation Message",
			fieldDescription = "Validation message for phone field. Default set to" +
					" 'Please enter a valid Phone Number '")
	@TextField
	@Inject @Named("phoneMessage")
	@Default(values = UKContactUsFormConstant.PHONE_VALIDATION_MESSAGE)
	private String phoneValidationMessage;

	@DialogField(value = "Email ", name = "./email", fieldLabel = "email Text",
			fieldDescription = "Text for email field. Default set to 'Email '")
	@TextField
	@Inject @Named("email")
	@Default(values = UKContactUsFormConstant.EMAIL)
	private String emailText;

	@DialogField(name = "./emailReq", value = UKContactUsFormConstant.REQUIRED_YES_VALUE,
			fieldLabel = "email Required", fieldDescription = "Please select email is required or not. " +
			"Default set to 'Yes'")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Yes" , value="yes"),
					@Option(text="No" , value="no")
			})
	@Inject @Named("emailReq")
	@Default(values = UKContactUsFormConstant.REQUIRED_YES_VALUE)
	private String emailValidation;

	@DialogField(value = "email ", name = "./emailMessage", fieldLabel = "email Validation Message",
			fieldDescription = "Validation message for email field. " +
					"Default set to 'Please enter a valid email '")
	@TextField
	@Inject @Named("emailMessage")
	@Default(values = UKContactUsFormConstant.EMAIL_VALIDATION_MESSAGE)
	private String emailValidationMessage;

	@Inject
	ValueMap valueMap;

	private static final Logger log = LoggerFactory.getLogger(UKcontactuspersonalInfo.class);

	private List<String> titleOptions;
	private String titleReq = "";
	private String titleMessage="";

	private String firstName="";
	private String firstNameReq="";
	private String firstNameMessage="";

	private String lastName="";
	private String lastNameReq="";
	private String lastNameMessage="";

	private String address="";
	private String addressReq="";
	private String addressMessage="";

	private String postcode="";
	private String postcodeReq="";
	private String postcodeMessage="";

	private String phone="";
	private String phoneReq="";
	private String phoneMessage="";

	private String email="";
	private String emailReq="";
	private String emailMessage="";

	@PostConstruct
	public void init() throws Exception {
		log.debug("set value from dialog properties...");
		String[] titleOptionsData = null!=title ? this.title : null;
		if (null != titleOptionsData) {
			titleOptions = new ArrayList<String>();
			for (int i = 0; i < titleOptionsData.length; i++) {
				String options = titleOptionsData[i];
				log.debug("stateOptions: "+ options);
				titleOptions.add(options);
			}
		}
		
		titleReq = this.titleValidation;
		titleMessage=this.titleValidationMessage;

		firstName=this.firstNameText;
		firstNameReq=this.firstNameValidation;
		firstNameMessage=this.firstNameValidationMessage;

		lastName=this.lastNameText;
		lastNameReq=this.lastNameValidation;
		lastNameMessage=this.lastNameValidationMessage;

		address=this.addressText;
		addressReq=this.addressValidation;
		addressMessage=this.addressValidationMessage;

		postcode=this.postcodeText;
		postcodeReq=this.postcodeValidation;
		postcodeMessage=this.postcodeValidationMessage;

		phone=this.phoneText;
		phoneReq=this.phoneValidation;
		phoneMessage=this.phoneValidationMessage;

		email=this.emailText;
		emailReq=this.emailValidation;
		emailMessage=this.emailValidationMessage;
	}

	public String getFirstName() {
		return firstName;
	}
	public String getFirstNameReq() {
		return firstNameReq;
	}
	public String getFirstNameMessage() {
		return firstNameMessage;
	}

	public String getLastName() {
		return lastName;
	}
	public String getLastNameReq() {
		return lastNameReq;
	}
	public String getLastNameMessage() {
		return lastNameMessage;
	}

	public String getAddress() {
		return address;
	}
	public String getAddressReq() {
		return addressReq;
	}
	public String getAddressMessage() {
		return addressMessage;
	}

	public String getPostcode() {
		return postcode;
	}
	public String getPostcodeReq() {
		return postcodeReq;
	}
	public String getPostcodeMessage() {
		return postcodeMessage;
	}

	public String getPhone() {
		return phone;
	}
	public String getPhoneReq() {
		return phoneReq;
	}
	public String getPhoneMessage() {
		return phoneMessage;
	}

	public String getEmail() {
		return email;
	}
	public String getEmailReq() {
		return emailReq;
	}
	public String getEmailMessage() {
		return emailMessage;
	}

	public String getTitleMessage() {
		return titleMessage;
	}
	public String getTitleReq() {
		return titleReq;
	}
	public String getTitleText() {
		return titleText;
	}

	public List<String> getTitleOptions() {
		return titleOptions;
	}

}