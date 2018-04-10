package com.mcd.rwd.us.core.sightly.forms;

import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mcd.rwd.global.core.sightly.McDUse;
import com.mcd.rwd.us.core.constants.PeopleStoriesFormConstants;
import com.mcd.rwd.us.core.service.McdWebServicesConfig;
import org.apache.sling.api.scripting.SlingScriptHelper;

/**
 * 
 * @author Sapient - MCD-US-Support
 * This is for People Stories Form Component Handler. Reads dialog properties.
 */
public class PeopleStoriesFormHandler extends McDUse {
	private static final Logger LOG = LoggerFactory.getLogger(PeopleStoriesFormHandler.class);

	//People Stories Widget Params
	private String firstName;
	private String firstNameReq;

	private String lastName;
	private String lastNameReq;

	private String email;
	private String emailValidationMessage;
	private String emailReq;

	private String phone;
	private String phoneReq;
	private String phoneValidationMessage;

	private String city;
	private String cityReq;

	private String[] states;
	private String stateReq;
	private String statesLabel;

	private String[] positions;
	private String positionReq;
	private String positionLabel;

	private String employeeFirstJob;
	private String firstJobReq;
	private String firstJobYes;
	private String firstJobNo;


	private String archewayOpportunityProgram;
	private String archewayReq;
	private String archewayYes;
	private String archewayNo;
	private String archewayNotSure;

	private String advisingServices;
	private String currentText;
	private String pastText;

	private String englishUnderArches;
	private String enrolledText;
	private String graduateText;

	private String careerOnline;
	private String enrolledOnlineText;
	private String graduateOnlineText;

	private String collegeTuition;
	private String enrolledTuition;
	private String graduateTuitionText;

	private String additionalComment;
	private String additionalCommentReq;

	private String isMcdGreatPlace;
	private String isMcdGreatPlaceReq;

	private String specificSkillInfo;
	private String specificSkillInfoReq;

	private String futureIntent;
	private String futureIntentReq;
	private String futureIntentValidation;

	private String[] genders;
	private String genderLabel;
	private String genderReq;

	private String etnicity;
	private String ethnicityReq;

	private String asian;
	private String blackAfricanAmerican;
	private String hawaiianPacificIslander;
	private String hispanicLatino;
	private String americanAlaskan;
	private String whiteCaucasian;
	private String otherEthnicity;

	private String uploadPhoto;
	private String photoValidationMessage;
	private String photoReq;
	private String browseInfo;
	private String browseBtn;

	private String videoLink;
	private String videoLinkReq;
	private String videoLinkFmtValidationMessage;

	private String nonEmployee;
	private String nonEmpFirstName;
	private String nonEmpFirstNameReq;

	private String nonEmpLastName;
	private String nonEmpLastNameReq;

	private String nonEmpEmail;
	private String nonEmpEmailReq;
	private String nonEmpEmailValidation;

	private String nonEmpAddComments;
	private String nonEmpAddCommentsReq;

	private String submitBtn;

	private String globalErrorMessage;
	private String formDescription;
	private String formInfo;
	private String formType;
	private String formAdminEmail;
	
	private String peopleStoriesFormSubmitUrl;
	
	@Override
	public void activate() throws Exception {
		ValueMap properties = getProperties();
		if (LOG.isDebugEnabled()) {
			LOG.debug(String.format("In PeopleStoriesFormHandler Activate Method %n"), properties.toString());
		}
		
		this.firstName = properties.get(PeopleStoriesFormConstants.PROP_FIRST_NAME_LBL, PeopleStoriesFormConstants.VAL_FIRST_NAME_LBL);
		this.firstNameReq = properties.get(PeopleStoriesFormConstants.PROP_FIRST_NAME_REQ, PeopleStoriesFormConstants.VAL_FIRST_NAME_REQ);

		this.lastName = properties.get(PeopleStoriesFormConstants.PROP_LAST_NAME_LBL, PeopleStoriesFormConstants.VAL_LAST_NAME_LBL);
		this.lastNameReq = properties.get(PeopleStoriesFormConstants.PROP_LAST_NAME_REQ, PeopleStoriesFormConstants.VAL_LAST_NAME_REQ);

		this.email = properties.get(PeopleStoriesFormConstants.PROP_EMAIL_LBL, PeopleStoriesFormConstants.VAL_EMAIL_LBL);
		this.emailValidationMessage = properties.get(PeopleStoriesFormConstants.PROP_EMAIL_VALIDATION_MSG, PeopleStoriesFormConstants.VAL_EMAIL_VALIDATION_MSG);
		this.emailReq = properties.get(PeopleStoriesFormConstants.PROP_EMAIL_REQ, PeopleStoriesFormConstants.VAL_EMAIL_REQ);

		this.phone = properties.get(PeopleStoriesFormConstants.PROP_PHONE_LBL, PeopleStoriesFormConstants.VAL_PHONE_LBL);
		this.phoneReq = properties.get(PeopleStoriesFormConstants.PROP_PHONE_REQ, PeopleStoriesFormConstants.VAL_PHONE_REQ);
		this.phoneValidationMessage = properties.get(PeopleStoriesFormConstants.PROP_PHONE_VALIDATION_MSG, PeopleStoriesFormConstants.VAL_PHONE_VALIDATION_MSG);

		this.city = properties.get(PeopleStoriesFormConstants.PROP_CITY_LBL, PeopleStoriesFormConstants.VAL_CITY_LBL);
		this.cityReq = properties.get(PeopleStoriesFormConstants.PROP_CITY_REQ, PeopleStoriesFormConstants.VAL_CITY_REQ);

		this.states = properties.get(PeopleStoriesFormConstants.PROP_STATE_LIST, String[].class);
		this.stateReq = properties.get(PeopleStoriesFormConstants.PROP_STATE_REQ, PeopleStoriesFormConstants.VAL_STATE_REQ);
		this.statesLabel = properties.get(PeopleStoriesFormConstants.PROP_STATE_LBL, PeopleStoriesFormConstants.VAL_STATE_LBL);

		this.positions = properties.get(PeopleStoriesFormConstants.PROP_POSITION_LIST, String[].class);
		this.positionReq = properties.get(PeopleStoriesFormConstants.PROP_POSITION_REQ, PeopleStoriesFormConstants.VAL_POSITION_REQ);
		this.positionLabel = properties.get(PeopleStoriesFormConstants.PROP_POSITION_LBL, PeopleStoriesFormConstants.VAL_POSITION_LBL);

		this.employeeFirstJob = properties.get(PeopleStoriesFormConstants.PROP_EMPLOYEE_FIRST_JOB_LBL, PeopleStoriesFormConstants.VAL_EMPLOYEE_FIRST_JOB_LBL);
		this.firstJobReq = properties.get(PeopleStoriesFormConstants.PROP_FIRST_JOB_REQ, PeopleStoriesFormConstants.VAL_FIRST_JOB_REQ);
		this.firstJobYes = properties.get(PeopleStoriesFormConstants.PROP_FIRST_JOB_YES, PeopleStoriesFormConstants.VAL_FIRST_JOB_YES);
		this.firstJobNo = properties.get(PeopleStoriesFormConstants.PROP_FIRST_JOB_NO, PeopleStoriesFormConstants.VAL_FIRST_JOB_NO);

		this.archewayOpportunityProgram = properties.get(PeopleStoriesFormConstants.PROP_ARCHEWAY_OPPORTUNITY_PROGRAM_LBL, PeopleStoriesFormConstants.VAL_ARCHEWAY_OPPORTUNITY_PROGRAM_LBL);
		this.archewayReq = properties.get(PeopleStoriesFormConstants.PROP_ARCHEWAY_REQ, PeopleStoriesFormConstants.VAL_ARCHEWAY_REQ);
		this.archewayYes = properties.get(PeopleStoriesFormConstants.PROP_ARCHEWAY_YES, PeopleStoriesFormConstants.VAL_ARCHEWAY_YES);
		this.archewayNo = properties.get(PeopleStoriesFormConstants.PROP_ARCHEWAY_NO, PeopleStoriesFormConstants.VAL_ARCHEWAY_NO);
		this.archewayNotSure = properties.get(PeopleStoriesFormConstants.PROP_ARCHEWAY_NOT_SURE, PeopleStoriesFormConstants.VAL_ARCHEWAY_NOT_SURE);

		this.advisingServices = properties.get(PeopleStoriesFormConstants.PROP_ADVISING_SERVICES_LBL, PeopleStoriesFormConstants.VAL_ADVISING_SERVICES_LBL);
		this.currentText = properties.get(PeopleStoriesFormConstants.PROP_CURRENT_TEXT, PeopleStoriesFormConstants.VAL_CURRENT_TEXT);
		this.pastText = properties.get(PeopleStoriesFormConstants.PROP_PAST_TEXT, PeopleStoriesFormConstants.VAL_PAST_TEXT);

		this.englishUnderArches = properties.get(PeopleStoriesFormConstants.PROP_ENGLISH_UNDER_ARCHES_LBL, PeopleStoriesFormConstants.VAL_ENGLISH_UNDER_ARCHES_LBL);
		this.enrolledText = properties.get(PeopleStoriesFormConstants.PROP_ENROLLED_TEXT, PeopleStoriesFormConstants.VAL_ENROLLED_TEXT);
		this.graduateText = properties.get(PeopleStoriesFormConstants.PROP_GRADUATE_TEXT, PeopleStoriesFormConstants.VAL_GRADUATE_TEXT);

		this.careerOnline = properties.get(PeopleStoriesFormConstants.PROP_CAREER_ONLINE_LBL, PeopleStoriesFormConstants.VAL_CAREER_ONLINE_LBL);
		this.enrolledOnlineText = properties.get(PeopleStoriesFormConstants.PROP_ENROLLED_ONLINE_TEXT, PeopleStoriesFormConstants.VAL_ENROLLED_ONLINE_TEXT);
		this.graduateOnlineText = properties.get(PeopleStoriesFormConstants.PROP_GRADUATE_ONLINE_TEXT, PeopleStoriesFormConstants.VAL_GRADUATE_ONLINE_TEXT);

		this.collegeTuition = properties.get(PeopleStoriesFormConstants.PROP_COLLEGE_TUITION_LBL, PeopleStoriesFormConstants.VAL_COLLEGE_TUITION_LBL);
		this.enrolledTuition = properties.get(PeopleStoriesFormConstants.PROP_ENROLLED_TUITION, PeopleStoriesFormConstants.VAL_ENROLLED_TUITION);
		this.graduateTuitionText = properties.get(PeopleStoriesFormConstants.PROP_GRADUATE_TUITION_TEXT, PeopleStoriesFormConstants.VAL_GRADUATE_TUITION_TEXT);

		this.additionalComment = properties.get(PeopleStoriesFormConstants.PROP_ADDITIONAL_COMMENT_LBL, PeopleStoriesFormConstants.VAL_ADDITIONAL_COMMENT_LBL);
		this.additionalCommentReq = properties.get(PeopleStoriesFormConstants.PROP_ADDITIONAL_COMMENT_REQ, PeopleStoriesFormConstants.VAL_ADDITIONAL_COMMENT_REQ);

		this.isMcdGreatPlace = properties.get(PeopleStoriesFormConstants.PROP_IS_MCD_GREAT_PLACE_LBL, PeopleStoriesFormConstants.VAL_IS_MCD_GREAT_PLACE_LBL);
		this.isMcdGreatPlaceReq = properties.get(PeopleStoriesFormConstants.PROP_IS_MCD_GREAT_PLACE_REQ, PeopleStoriesFormConstants.VAL_IS_MCD_GREAT_PLACE_REQ);

		this.specificSkillInfo = properties.get(PeopleStoriesFormConstants.PROP_SPECIFIC_SKILL_INFO_LBL, PeopleStoriesFormConstants.VAL_SPECIFIC_SKILL_INFO_LBL);
		this.specificSkillInfoReq = properties.get(PeopleStoriesFormConstants.PROP_SPECIFIC_SKILL_INFO_REQ, PeopleStoriesFormConstants.VAL_SPECIFIC_SKILL_INFO_REQ);

		this.futureIntent = properties.get(PeopleStoriesFormConstants.PROP_FUTURE_INTENT_LBL, PeopleStoriesFormConstants.VAL_FUTURE_INTENT_LBL);
		this.futureIntentReq = properties.get(PeopleStoriesFormConstants.PROP_FUTURE_INTENT_REQ, PeopleStoriesFormConstants.VAL_FUTURE_INTENT_REQ);
		this.futureIntentValidation = properties.get(PeopleStoriesFormConstants.PROP_FUTURE_INTENT_VALIDATION, PeopleStoriesFormConstants.VAL_FUTURE_INTENT_VALIDATION);

		this.genders = properties.get(PeopleStoriesFormConstants.PROP_GENDER_LIST, PeopleStoriesFormConstants.VAL_GENDER_LIST);
		this.genderLabel = properties.get(PeopleStoriesFormConstants.PROP_GENDER_LBL, PeopleStoriesFormConstants.VAL_GENDER_LBL);
		this.genderReq = properties.get(PeopleStoriesFormConstants.PROP_GENDER_REQ, PeopleStoriesFormConstants.VAL_GENDER_REQ);

		this.etnicity = properties.get(PeopleStoriesFormConstants.PROP_ETHNICITY_LBL, PeopleStoriesFormConstants.VAL_ETHNICITY_LBL);
		this.ethnicityReq = properties.get(PeopleStoriesFormConstants.PROP_ETHNICITY_REQ, PeopleStoriesFormConstants.VAL_ETHNICITY_REQ);

		this.asian = properties.get(PeopleStoriesFormConstants.PROP_ASIAN_LBL, PeopleStoriesFormConstants.VAL_ASIAN_LBL);
		this.blackAfricanAmerican = properties.get(PeopleStoriesFormConstants.PROP_BLACK_AFRICAN_AMERICAN_LBL, PeopleStoriesFormConstants.VAL_BLACK_AFRICAN_AMERICAN_LBL);
		this.hawaiianPacificIslander = properties.get(PeopleStoriesFormConstants.PROP_HAWAIIAN_PACIFIC_ISLANDER_LBL, PeopleStoriesFormConstants.VAL_HAWAIIAN_PACIFIC_ISLANDER_LBL);
		this.hispanicLatino = properties.get(PeopleStoriesFormConstants.PROP_HISPANIC_LATINO_LBL, PeopleStoriesFormConstants.VAL_HISPANIC_LATINO_LBL);
		this.americanAlaskan = properties.get(PeopleStoriesFormConstants.PROP_AMERICAN_ALASKAN_LBL, PeopleStoriesFormConstants.VAL_AMERICAN_ALASKAN_LBL);
		this.whiteCaucasian = properties.get(PeopleStoriesFormConstants.PROP_WHITE_CAUCASIAN_LBL, PeopleStoriesFormConstants.VAL_WHITE_CAUCASIAN_LBL);
		this.otherEthnicity = properties.get(PeopleStoriesFormConstants.PROP_OTHER_ETHNICITY_LBL, PeopleStoriesFormConstants.VAL_OTHER_ETHNICITY_LBL);

		this.uploadPhoto = properties.get(PeopleStoriesFormConstants.PROP_UPLOAD_PHOTO_LBL, PeopleStoriesFormConstants.VAL_UPLOAD_PHOTO_LBL);
		this.photoValidationMessage = properties.get(PeopleStoriesFormConstants.PROP_PHOTO_VALIDATION_MSG, PeopleStoriesFormConstants.VAL_PHOTO_VALIDATION_MSG);
		this.photoReq = properties.get(PeopleStoriesFormConstants.PROP_PHOTO_REQ, PeopleStoriesFormConstants.VAL_PHOTO_REQ);
		this.browseInfo = properties.get(PeopleStoriesFormConstants.PROP_BROWSE_INFO, PeopleStoriesFormConstants.VAL_BROWSE_INFO);
		this.browseBtn = properties.get(PeopleStoriesFormConstants.PROP_BROWSE_BTN, PeopleStoriesFormConstants.VAL_BROWSE_BTN);

		this.videoLink = properties.get(PeopleStoriesFormConstants.PROP_VIDEO_LINK_LBL, PeopleStoriesFormConstants.VAL_VIDEO_LINK_LBL);
		this.videoLinkReq = properties.get(PeopleStoriesFormConstants.PROP_VIDEO_LINK_REQ, PeopleStoriesFormConstants.VAL_VIDEO_LINK_REQ);
		this.videoLinkFmtValidationMessage = properties.get(PeopleStoriesFormConstants.PROP_VIDEO_LINK_FMT_VALIDATION_MSG, PeopleStoriesFormConstants.VAL_VIDEO_LINK_FMT_VALIDATION_MSG);

		this.nonEmployee = properties.get(PeopleStoriesFormConstants.PROP_NON_EMPLOYEE_LBL, PeopleStoriesFormConstants.VAL_NON_EMPLOYEE_LBL);
		this.nonEmpFirstName = properties.get(PeopleStoriesFormConstants.PROP_NON_EMP_FIRST_NAME, PeopleStoriesFormConstants.VAL_NON_EMP_FIRST_NAME);
		this.nonEmpFirstNameReq = properties.get(PeopleStoriesFormConstants.PROP_NON_EMP_FIRST_NAME_REQ, PeopleStoriesFormConstants.VAL_NON_EMP_FIRST_NAME_REQ);

		this.nonEmpLastName = properties.get(PeopleStoriesFormConstants.PROP_NON_EMP_LAST_NAME_LBL, PeopleStoriesFormConstants.VAL_NON_EMP_LAST_NAME_LBL);
		this.nonEmpLastNameReq = properties.get(PeopleStoriesFormConstants.PROP_NON_EMP_LAST_NAME_REQ, PeopleStoriesFormConstants.VAL_NON_EMP_LAST_NAME_REQ);

		this.nonEmpEmail = properties.get(PeopleStoriesFormConstants.PROP_NON_EMP_EMAIL_LBL, PeopleStoriesFormConstants.VAL_NON_EMP_EMAIL_LBL);
		this.nonEmpEmailReq = properties.get(PeopleStoriesFormConstants.PROP_NON_EMP_EMAIL_REQ, PeopleStoriesFormConstants.VAL_NON_EMP_EMAIL_REQ);
		this.nonEmpEmailValidation = properties.get(PeopleStoriesFormConstants.PROP_NON_EMP_EMAIL_VALIDATION, PeopleStoriesFormConstants.VAL_NON_EMP_EMAIL_VALIDATION);

		this.nonEmpAddComments = properties.get(PeopleStoriesFormConstants.PROP_NON_EMP_ADD_COMMENTS_LBL, PeopleStoriesFormConstants.VAL_NON_EMP_ADD_COMMENTS_LBL);
		this.nonEmpAddCommentsReq = properties.get(PeopleStoriesFormConstants.PROP_NON_EMP_ADD_COMMENTS_REQ, PeopleStoriesFormConstants.VAL_NON_EMP_ADD_COMMENTS_REQ);

		this.submitBtn = properties.get(PeopleStoriesFormConstants.PROP_SUBMIT_LBL, PeopleStoriesFormConstants.VAL_SUBMIT_LBL);

		this.globalErrorMessage = properties.get(PeopleStoriesFormConstants.PROP_GLOBAL_ERROR_MSG, PeopleStoriesFormConstants.VAL_GLOBAL_ERROR_MSG);
		this.formDescription = properties.get(PeopleStoriesFormConstants.PROP_FORM_DESCRIPTION, PeopleStoriesFormConstants.VAL_FORM_DESCRIPTION);
		this.formInfo = properties.get(PeopleStoriesFormConstants.PROP_FORM_INFO, PeopleStoriesFormConstants.VAL_FORM_INFO);
		this.formType = properties.get(PeopleStoriesFormConstants.PROP_FORM_TYPE, PeopleStoriesFormConstants.VAL_FORM_TYPE);
		this.formAdminEmail = properties.get(PeopleStoriesFormConstants.PROP_FORM_ADMIN_EMAIL, PeopleStoriesFormConstants.VAL_FORM_ADMIN_EMAIL);
		this.peopleStoriesFormSubmitUrl = getPSFormUrl();
	}

	public String getPSFormUrl() {
		SlingScriptHelper helper = getSlingScriptHelper();
		String psFormUrl = "";
		if(helper!=null){
		McdWebServicesConfig mcdConfig=helper.getService(McdWebServicesConfig.class);
		if(null != mcdConfig)
			psFormUrl = mcdConfig.getPeopleStoriesFormSubmitUrl();
		}
		return psFormUrl;	
	}
	
	public String getPeopleStoriesFormSubmitUrl() {
		return peopleStoriesFormSubmitUrl;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public String getFirstNameReq() {
		return firstNameReq;
	}

	public String getLastName() {
		return lastName;
	}

	public String getLastNameReq() {
		return lastNameReq;
	}

	public String getEmail() {
		return email;
	}

	public String getEmailValidationMessage() {
		return emailValidationMessage;
	}

	public String getEmailReq() {
		return emailReq;
	}

	public String getPhone() {
		return phone;
	}

	public String getPhoneReq() {
		return phoneReq;
	}

	public String getPhoneValidationMessage() {
		return phoneValidationMessage;
	}

	public String getCity() {
		return city;
	}

	public String getCityReq() {
		return cityReq;
	}

	public String[] getStates() {
		return states;
	}

	public String getStateReq() {
		return stateReq;
	}

	public String getStatesLabel() {
		return statesLabel;
	}

	public String[] getPositions() {
		return positions;
	}

	public String getPositionReq() {
		return positionReq;
	}

	public String getPositionLabel() {
		return positionLabel;
	}

	public String getEmployeeFirstJob() {
		return employeeFirstJob;
	}

	public String getFirstJobReq() {
		return firstJobReq;
	}

	public String getFirstJobYes() {
		return firstJobYes;
	}

	public String getFirstJobNo() {
		return firstJobNo;
	}

	public String getArchewayOpportunityProgram() {
		return archewayOpportunityProgram;
	}

	public String getArchewayReq() {
		return archewayReq;
	}

	public String getArchewayYes() {
		return archewayYes;
	}

	public String getArchewayNo() {
		return archewayNo;
	}

	public String getArchewayNotSure() {
		return archewayNotSure;
	}

	public String getAdvisingServices() {
		return advisingServices;
	}

	public String getCurrentText() {
		return currentText;
	}

	public String getPastText() {
		return pastText;
	}

	public String getEnglishUnderArches() {
		return englishUnderArches;
	}

	public String getEnrolledText() {
		return enrolledText;
	}

	public String getGraduateText() {
		return graduateText;
	}

	public String getCareerOnline() {
		return careerOnline;
	}

	public String getEnrolledOnlineText() {
		return enrolledOnlineText;
	}

	public String getGraduateOnlineText() {
		return graduateOnlineText;
	}

	public String getCollegeTuition() {
		return collegeTuition;
	}

	public String getEnrolledTuition() {
		return enrolledTuition;
	}

	public String getGraduateTuitionText() {
		return graduateTuitionText;
	}

	public String getAdditionalComment() {
		return additionalComment;
	}

	public String getAdditionalCommentReq() {
		return additionalCommentReq;
	}

	public String getIsMcdGreatPlace() {
		return isMcdGreatPlace;
	}

	public String getIsMcdGreatPlaceReq() {
		return isMcdGreatPlaceReq;
	}

	public String getSpecificSkillInfo() {
		return specificSkillInfo;
	}

	public String getSpecificSkillInfoReq() {
		return specificSkillInfoReq;
	}

	public String getFutureIntent() {
		return futureIntent;
	}

	public String getFutureIntentReq() {
		return futureIntentReq;
	}

	public String getFutureIntentValidation() {
		return futureIntentValidation;
	}

	public String[] getGenders() {
		return genders;
	}
	
	public String getGenderLabel() {
		return genderLabel;
	}

	public String getGenderReq() {
		return genderReq;
	}

	public String getEtnicity() {
		return etnicity;
	}

	public String getEthnicityReq() {
		return ethnicityReq;
	}

	public String getAsian() {
		return asian;
	}

	public String getBlackAfricanAmerican() {
		return blackAfricanAmerican;
	}

	public String getHawaiianPacificIslander() {
		return hawaiianPacificIslander;
	}

	public String getHispanicLatino() {
		return hispanicLatino;
	}

	public String getAmericanAlaskan() {
		return americanAlaskan;
	}

	public String getWhiteCaucasian() {
		return whiteCaucasian;
	}

	public String getOtherEthnicity() {
		return otherEthnicity;
	}

	public String getUploadPhoto() {
		return uploadPhoto;
	}

	public String getPhotoValidationMessage() {
		return photoValidationMessage;
	}

	public String getPhotoReq() {
		return photoReq;
	}

	public String getBrowseInfo() {
		return browseInfo;
	}

	public String getBrowseBtn() {
		return browseBtn;
	}

	public String getVideoLink() {
		return videoLink;
	}

	public String getVideoLinkReq() {
		return videoLinkReq;
	}

	public String getVideoLinkFmtValidationMessage() {
		return videoLinkFmtValidationMessage;
	}

	public String getNonEmployee() {
		return nonEmployee;
	}

	public String getNonEmpFirstName() {
		return nonEmpFirstName;
	}

	public String getNonEmpFirstNameReq() {
		return nonEmpFirstNameReq;
	}

	public String getNonEmpLastName() {
		return nonEmpLastName;
	}

	public String getNonEmpLastNameReq() {
		return nonEmpLastNameReq;
	}

	public String getNonEmpEmail() {
		return nonEmpEmail;
	}

	public String getNonEmpEmailReq() {
		return nonEmpEmailReq;
	}

	public String getNonEmpEmailValidation() {
		return nonEmpEmailValidation;
	}

	public String getNonEmpAddComments() {
		return nonEmpAddComments;
	}

	public String getNonEmpAddCommentsReq() {
		return nonEmpAddCommentsReq;
	}

	public String getSubmitBtn() {
		return submitBtn;
	}

	public String getGlobalErrorMessage() {
		return globalErrorMessage;
	}

	public String getFormDescription() {
		return formDescription;
	}

	public String getFormInfo() {
		return formInfo;
	}

	public String getFormType() {
		return formType;
	}

	public String getFormAdminEmail() {
		return formAdminEmail;
	}
}
