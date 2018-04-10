package com.mcd.rwd.us.core.sightly.forms;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import junitx.util.PrivateAccessor;

import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.junit.Before;
import org.junit.Test;

import com.mcd.rwd.us.core.constants.PeopleStoriesFormConstants;
import org.apache.sling.api.scripting.SlingScriptHelper;


public class PeopleStoriesFormHandlerTest {

	private PeopleStoriesFormHandler peopleStoriesFormHandler;
	private SlingScriptHelper SlingScriptHelper;
	private ValueMap properties;

	@Before
	public void setup() throws Exception {
		peopleStoriesFormHandler = mock(PeopleStoriesFormHandler.class);
		properties = new ValueMapDecorator(new HashMap());

		properties.put(PeopleStoriesFormConstants.PROP_FIRST_NAME_LBL, PeopleStoriesFormConstants.VAL_FIRST_NAME_LBL);
		properties.put(PeopleStoriesFormConstants.PROP_FIRST_NAME_REQ , PeopleStoriesFormConstants.VAL_FIRST_NAME_REQ); 

		properties.put(PeopleStoriesFormConstants.PROP_LAST_NAME_LBL , PeopleStoriesFormConstants.VAL_LAST_NAME_LBL);
		properties.put(PeopleStoriesFormConstants.PROP_LAST_NAME_REQ , PeopleStoriesFormConstants.VAL_LAST_NAME_REQ);

		properties.put(PeopleStoriesFormConstants.PROP_EMAIL_LBL , PeopleStoriesFormConstants.VAL_EMAIL_LBL);
		properties.put(PeopleStoriesFormConstants.PROP_EMAIL_VALIDATION_MSG , PeopleStoriesFormConstants.VAL_EMAIL_VALIDATION_MSG); 
		properties.put(PeopleStoriesFormConstants.PROP_EMAIL_REQ , PeopleStoriesFormConstants.VAL_EMAIL_REQ);

		properties.put(PeopleStoriesFormConstants.PROP_PHONE_LBL , PeopleStoriesFormConstants.VAL_PHONE_LBL);
		properties.put(PeopleStoriesFormConstants.PROP_PHONE_REQ , PeopleStoriesFormConstants.VAL_PHONE_REQ);
		properties.put(PeopleStoriesFormConstants.PROP_PHONE_VALIDATION_MSG , PeopleStoriesFormConstants.VAL_PHONE_VALIDATION_MSG); 

		properties.put(PeopleStoriesFormConstants.PROP_CITY_LBL , PeopleStoriesFormConstants.VAL_CITY_LBL);
		properties.put(PeopleStoriesFormConstants.PROP_CITY_REQ , PeopleStoriesFormConstants.VAL_CITY_REQ);

		properties.put(PeopleStoriesFormConstants.PROP_STATE_LIST , PeopleStoriesFormConstants.VAL_STATE_LIST); 
		properties.put(PeopleStoriesFormConstants.PROP_STATE_REQ , PeopleStoriesFormConstants.VAL_STATE_REQ);
		properties.put(PeopleStoriesFormConstants.PROP_STATE_LBL , PeopleStoriesFormConstants.VAL_STATE_LBL); 

		properties.put(PeopleStoriesFormConstants.PROP_POSITION_LIST , PeopleStoriesFormConstants.VAL_POSITION_LIST);
		properties.put(PeopleStoriesFormConstants.PROP_POSITION_REQ , PeopleStoriesFormConstants.VAL_POSITION_REQ);
		properties.put(PeopleStoriesFormConstants.PROP_POSITION_LBL , PeopleStoriesFormConstants.VAL_POSITION_LBL); 

		properties.put(PeopleStoriesFormConstants.PROP_EMPLOYEE_FIRST_JOB_LBL , PeopleStoriesFormConstants.VAL_EMPLOYEE_FIRST_JOB_LBL);
		properties.put(PeopleStoriesFormConstants.PROP_FIRST_JOB_REQ , PeopleStoriesFormConstants.VAL_FIRST_JOB_REQ);
		properties.put(PeopleStoriesFormConstants.PROP_FIRST_JOB_YES , PeopleStoriesFormConstants.VAL_FIRST_JOB_YES);
		properties.put(PeopleStoriesFormConstants.PROP_FIRST_JOB_NO , PeopleStoriesFormConstants.VAL_FIRST_JOB_NO);


		properties.put(PeopleStoriesFormConstants.PROP_ARCHEWAY_OPPORTUNITY_PROGRAM_LBL , PeopleStoriesFormConstants.VAL_ARCHEWAY_OPPORTUNITY_PROGRAM_LBL); 
		properties.put(PeopleStoriesFormConstants.PROP_ARCHEWAY_REQ , PeopleStoriesFormConstants.VAL_ARCHEWAY_REQ);
		properties.put(PeopleStoriesFormConstants.PROP_ARCHEWAY_YES , PeopleStoriesFormConstants.VAL_ARCHEWAY_YES);
		properties.put(PeopleStoriesFormConstants.PROP_ARCHEWAY_NO , PeopleStoriesFormConstants.VAL_ARCHEWAY_NO);
		properties.put(PeopleStoriesFormConstants.PROP_ARCHEWAY_NOT_SURE , PeopleStoriesFormConstants.VAL_ARCHEWAY_NOT_SURE); 

		properties.put(PeopleStoriesFormConstants.PROP_ADVISING_SERVICES_LBL , PeopleStoriesFormConstants.VAL_ADVISING_SERVICES_LBL); 
		properties.put(PeopleStoriesFormConstants.PROP_CURRENT_TEXT , PeopleStoriesFormConstants.VAL_CURRENT_TEXT);
		properties.put(PeopleStoriesFormConstants.PROP_PAST_TEXT , PeopleStoriesFormConstants.VAL_PAST_TEXT);

		properties.put(PeopleStoriesFormConstants.PROP_ENGLISH_UNDER_ARCHES_LBL , PeopleStoriesFormConstants.VAL_ENGLISH_UNDER_ARCHES_LBL); 
		properties.put(PeopleStoriesFormConstants.PROP_ENROLLED_TEXT , PeopleStoriesFormConstants.VAL_ENROLLED_TEXT);
		properties.put(PeopleStoriesFormConstants.PROP_GRADUATE_TEXT , PeopleStoriesFormConstants.VAL_GRADUATE_TEXT);

		properties.put(PeopleStoriesFormConstants.PROP_CAREER_ONLINE_LBL , PeopleStoriesFormConstants.VAL_CAREER_ONLINE_LBL); 
		properties.put(PeopleStoriesFormConstants.PROP_ENROLLED_ONLINE_TEXT , PeopleStoriesFormConstants.VAL_ENROLLED_ONLINE_TEXT); 
		properties.put(PeopleStoriesFormConstants.PROP_GRADUATE_ONLINE_TEXT , PeopleStoriesFormConstants.VAL_GRADUATE_ONLINE_TEXT);

		properties.put(PeopleStoriesFormConstants.PROP_COLLEGE_TUITION_LBL , PeopleStoriesFormConstants.VAL_COLLEGE_TUITION_LBL);
		properties.put(PeopleStoriesFormConstants.PROP_ENROLLED_TUITION , PeopleStoriesFormConstants.VAL_ENROLLED_TUITION);
		properties.put(PeopleStoriesFormConstants.PROP_GRADUATE_TUITION_TEXT , PeopleStoriesFormConstants.VAL_GRADUATE_TUITION_TEXT);

		properties.put(PeopleStoriesFormConstants.PROP_ADDITIONAL_COMMENT_LBL , PeopleStoriesFormConstants.VAL_ADDITIONAL_COMMENT_LBL); 
		properties.put(PeopleStoriesFormConstants.PROP_ADDITIONAL_COMMENT_REQ , PeopleStoriesFormConstants.VAL_ADDITIONAL_COMMENT_REQ);

		properties.put(PeopleStoriesFormConstants.PROP_IS_MCD_GREAT_PLACE_LBL , PeopleStoriesFormConstants.VAL_IS_MCD_GREAT_PLACE_LBL);
		properties.put(PeopleStoriesFormConstants.PROP_IS_MCD_GREAT_PLACE_REQ , PeopleStoriesFormConstants.VAL_IS_MCD_GREAT_PLACE_REQ);

		properties.put(PeopleStoriesFormConstants.PROP_SPECIFIC_SKILL_INFO_LBL , PeopleStoriesFormConstants.VAL_SPECIFIC_SKILL_INFO_LBL); 
		properties.put(PeopleStoriesFormConstants.PROP_SPECIFIC_SKILL_INFO_REQ , PeopleStoriesFormConstants.VAL_SPECIFIC_SKILL_INFO_REQ );

		properties.put(PeopleStoriesFormConstants.PROP_FUTURE_INTENT_LBL , PeopleStoriesFormConstants.VAL_FUTURE_INTENT_LBL);
		properties.put(PeopleStoriesFormConstants.PROP_FUTURE_INTENT_REQ , PeopleStoriesFormConstants.VAL_FUTURE_INTENT_REQ);
		properties.put(PeopleStoriesFormConstants.PROP_FUTURE_INTENT_VALIDATION , PeopleStoriesFormConstants.VAL_FUTURE_INTENT_VALIDATION); 

		properties.put(PeopleStoriesFormConstants.PROP_GENDER_LIST , PeopleStoriesFormConstants.VAL_GENDER_LIST);
		properties.put(PeopleStoriesFormConstants.PROP_GENDER_LBL , PeopleStoriesFormConstants.VAL_GENDER_LBL);
		properties.put(PeopleStoriesFormConstants.PROP_GENDER_REQ , PeopleStoriesFormConstants.VAL_GENDER_REQ);

		properties.put(PeopleStoriesFormConstants.PROP_ETHNICITY_LBL , PeopleStoriesFormConstants.VAL_ETHNICITY_LBL); 
		properties.put(PeopleStoriesFormConstants.PROP_ETHNICITY_REQ , PeopleStoriesFormConstants.VAL_ETHNICITY_REQ);

		properties.put(PeopleStoriesFormConstants.PROP_ASIAN_LBL , PeopleStoriesFormConstants.VAL_ASIAN_LBL);
		properties.put(PeopleStoriesFormConstants.PROP_AMERICAN_ALASKAN_LBL , PeopleStoriesFormConstants.VAL_AMERICAN_ALASKAN_LBL);
		properties.put(PeopleStoriesFormConstants.PROP_BLACK_AFRICAN_AMERICAN_LBL , PeopleStoriesFormConstants.VAL_BLACK_AFRICAN_AMERICAN_LBL); 
		properties.put(PeopleStoriesFormConstants.PROP_HAWAIIAN_PACIFIC_ISLANDER_LBL , PeopleStoriesFormConstants.VAL_HAWAIIAN_PACIFIC_ISLANDER_LBL); 
		properties.put(PeopleStoriesFormConstants.PROP_HISPANIC_LATINO_LBL , PeopleStoriesFormConstants.VAL_HISPANIC_LATINO_LBL);
		properties.put(PeopleStoriesFormConstants.PROP_WHITE_CAUCASIAN_LBL , PeopleStoriesFormConstants.VAL_WHITE_CAUCASIAN_LBL);
		properties.put(PeopleStoriesFormConstants.PROP_OTHER_ETHNICITY_LBL , PeopleStoriesFormConstants.VAL_OTHER_ETHNICITY_LBL);

		properties.put(PeopleStoriesFormConstants.PROP_UPLOAD_PHOTO_LBL , PeopleStoriesFormConstants.VAL_UPLOAD_PHOTO_LBL);
		properties.put(PeopleStoriesFormConstants.PROP_PHOTO_VALIDATION_MSG , PeopleStoriesFormConstants.VAL_PHOTO_VALIDATION_MSG); 
		properties.put(PeopleStoriesFormConstants.PROP_PHOTO_REQ , PeopleStoriesFormConstants.VAL_PHOTO_REQ);
		properties.put(PeopleStoriesFormConstants.PROP_BROWSE_INFO , PeopleStoriesFormConstants.VAL_BROWSE_INFO); 
		properties.put(PeopleStoriesFormConstants.PROP_BROWSE_BTN , PeopleStoriesFormConstants.VAL_BROWSE_BTN);

		properties.put(PeopleStoriesFormConstants.PROP_VIDEO_LINK_LBL , PeopleStoriesFormConstants.VAL_VIDEO_LINK_LBL); 
		properties.put(PeopleStoriesFormConstants.PROP_VIDEO_LINK_REQ , PeopleStoriesFormConstants.VAL_VIDEO_LINK_REQ);
		properties.put(PeopleStoriesFormConstants.PROP_VIDEO_LINK_FMT_VALIDATION_MSG , PeopleStoriesFormConstants.VAL_VIDEO_LINK_FMT_VALIDATION_MSG); 

		properties.put(PeopleStoriesFormConstants.PROP_NON_EMPLOYEE_LBL , PeopleStoriesFormConstants.VAL_NON_EMPLOYEE_LBL);
		properties.put(PeopleStoriesFormConstants.PROP_NON_EMP_FIRST_NAME , PeopleStoriesFormConstants.VAL_NON_EMP_FIRST_NAME); 
		properties.put(PeopleStoriesFormConstants.PROP_NON_EMP_FIRST_NAME_REQ , PeopleStoriesFormConstants.VAL_NON_EMP_FIRST_NAME_REQ); 

		properties.put(PeopleStoriesFormConstants.PROP_NON_EMP_LAST_NAME_LBL , PeopleStoriesFormConstants.VAL_NON_EMP_LAST_NAME_LBL);
		properties.put(PeopleStoriesFormConstants.PROP_NON_EMP_LAST_NAME_REQ , PeopleStoriesFormConstants.VAL_NON_EMP_LAST_NAME_REQ);

		properties.put(PeopleStoriesFormConstants.PROP_NON_EMP_EMAIL_LBL , PeopleStoriesFormConstants.VAL_NON_EMP_EMAIL_LBL);
		properties.put(PeopleStoriesFormConstants.PROP_NON_EMP_EMAIL_REQ , PeopleStoriesFormConstants.VAL_NON_EMP_EMAIL_REQ);
		properties.put(PeopleStoriesFormConstants.PROP_NON_EMP_EMAIL_VALIDATION , PeopleStoriesFormConstants.VAL_NON_EMP_EMAIL_VALIDATION); 

		properties.put(PeopleStoriesFormConstants.PROP_NON_EMP_ADD_COMMENTS_LBL , PeopleStoriesFormConstants.VAL_NON_EMP_ADD_COMMENTS_LBL);
		properties.put(PeopleStoriesFormConstants.PROP_NON_EMP_ADD_COMMENTS_REQ , PeopleStoriesFormConstants.VAL_NON_EMP_ADD_COMMENTS_REQ);

		properties.put(PeopleStoriesFormConstants.PROP_SUBMIT_LBL , PeopleStoriesFormConstants.VAL_SUBMIT_LBL);

		properties.put(PeopleStoriesFormConstants.PROP_GLOBAL_ERROR_MSG , PeopleStoriesFormConstants.VAL_GLOBAL_ERROR_MSG); 
		properties.put(PeopleStoriesFormConstants.PROP_FORM_DESCRIPTION , PeopleStoriesFormConstants.VAL_FORM_DESCRIPTION);
		properties.put(PeopleStoriesFormConstants.PROP_FORM_INFO , PeopleStoriesFormConstants.VAL_FORM_INFO);
		properties.put(PeopleStoriesFormConstants.PROP_FORM_TYPE , PeopleStoriesFormConstants.VAL_FORM_TYPE);
		properties.put(PeopleStoriesFormConstants.PROP_FORM_ADMIN_EMAIL , PeopleStoriesFormConstants.VAL_FORM_ADMIN_EMAIL); 

		when(peopleStoriesFormHandler.getProperties()).thenReturn(properties);

		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_FIRST_NAME_LBL, PeopleStoriesFormConstants.VAL_FIRST_NAME_LBL);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_FIRST_NAME_REQ , PeopleStoriesFormConstants.VAL_FIRST_NAME_REQ); 

		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_LAST_NAME_LBL , PeopleStoriesFormConstants.VAL_LAST_NAME_LBL);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_LAST_NAME_REQ , PeopleStoriesFormConstants.VAL_LAST_NAME_REQ);

		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_EMAIL_LBL , PeopleStoriesFormConstants.VAL_EMAIL_LBL);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_EMAIL_VALIDATION_MSG , PeopleStoriesFormConstants.VAL_EMAIL_VALIDATION_MSG); 
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_EMAIL_REQ , PeopleStoriesFormConstants.VAL_EMAIL_REQ);

		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_PHONE_LBL , PeopleStoriesFormConstants.VAL_PHONE_LBL);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_PHONE_REQ , PeopleStoriesFormConstants.VAL_PHONE_REQ);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_PHONE_VALIDATION_MSG , PeopleStoriesFormConstants.VAL_PHONE_VALIDATION_MSG); 

		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_CITY_LBL , PeopleStoriesFormConstants.VAL_CITY_LBL);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_CITY_REQ , PeopleStoriesFormConstants.VAL_CITY_REQ);

		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_STATE_LIST , PeopleStoriesFormConstants.VAL_STATE_LIST); 
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_STATE_REQ , PeopleStoriesFormConstants.VAL_STATE_REQ);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_STATE_LBL , PeopleStoriesFormConstants.VAL_STATE_LBL); 

		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_POSITION_LIST , PeopleStoriesFormConstants.VAL_POSITION_LIST);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_POSITION_REQ , PeopleStoriesFormConstants.VAL_POSITION_REQ);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_POSITION_LBL , PeopleStoriesFormConstants.VAL_POSITION_LBL); 

		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_EMPLOYEE_FIRST_JOB_LBL , PeopleStoriesFormConstants.VAL_EMPLOYEE_FIRST_JOB_LBL);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_FIRST_JOB_REQ , PeopleStoriesFormConstants.VAL_FIRST_JOB_REQ);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_FIRST_JOB_YES , PeopleStoriesFormConstants.VAL_FIRST_JOB_YES);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_FIRST_JOB_NO , PeopleStoriesFormConstants.VAL_FIRST_JOB_NO);


		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_ARCHEWAY_OPPORTUNITY_PROGRAM_LBL , PeopleStoriesFormConstants.VAL_ARCHEWAY_OPPORTUNITY_PROGRAM_LBL); 
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_ARCHEWAY_REQ , PeopleStoriesFormConstants.VAL_ARCHEWAY_REQ);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_ARCHEWAY_YES , PeopleStoriesFormConstants.VAL_ARCHEWAY_YES);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_ARCHEWAY_NO , PeopleStoriesFormConstants.VAL_ARCHEWAY_NO);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_ARCHEWAY_NOT_SURE , PeopleStoriesFormConstants.VAL_ARCHEWAY_NOT_SURE); 

		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_ADVISING_SERVICES_LBL , PeopleStoriesFormConstants.VAL_ADVISING_SERVICES_LBL); 
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_CURRENT_TEXT , PeopleStoriesFormConstants.VAL_CURRENT_TEXT);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_PAST_TEXT , PeopleStoriesFormConstants.VAL_PAST_TEXT);

		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_ENGLISH_UNDER_ARCHES_LBL , PeopleStoriesFormConstants.VAL_ENGLISH_UNDER_ARCHES_LBL); 
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_ENROLLED_TEXT , PeopleStoriesFormConstants.VAL_ENROLLED_TEXT);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_GRADUATE_TEXT , PeopleStoriesFormConstants.VAL_GRADUATE_TEXT);

		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_CAREER_ONLINE_LBL , PeopleStoriesFormConstants.VAL_CAREER_ONLINE_LBL); 
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_ENROLLED_ONLINE_TEXT , PeopleStoriesFormConstants.VAL_ENROLLED_ONLINE_TEXT); 
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_GRADUATE_ONLINE_TEXT , PeopleStoriesFormConstants.VAL_GRADUATE_ONLINE_TEXT);

		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_COLLEGE_TUITION_LBL , PeopleStoriesFormConstants.VAL_COLLEGE_TUITION_LBL);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_ENROLLED_TUITION , PeopleStoriesFormConstants.VAL_ENROLLED_TUITION);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_GRADUATE_TUITION_TEXT , PeopleStoriesFormConstants.VAL_GRADUATE_TUITION_TEXT);

		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_ADDITIONAL_COMMENT_LBL , PeopleStoriesFormConstants.VAL_ADDITIONAL_COMMENT_LBL); 
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_ADDITIONAL_COMMENT_REQ , PeopleStoriesFormConstants.VAL_ADDITIONAL_COMMENT_REQ);

		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_IS_MCD_GREAT_PLACE_LBL , PeopleStoriesFormConstants.VAL_IS_MCD_GREAT_PLACE_LBL);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_IS_MCD_GREAT_PLACE_REQ , PeopleStoriesFormConstants.VAL_IS_MCD_GREAT_PLACE_REQ);

		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_SPECIFIC_SKILL_INFO_LBL , PeopleStoriesFormConstants.VAL_SPECIFIC_SKILL_INFO_LBL); 
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_SPECIFIC_SKILL_INFO_REQ , PeopleStoriesFormConstants.VAL_SPECIFIC_SKILL_INFO_REQ );

		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_FUTURE_INTENT_LBL , PeopleStoriesFormConstants.VAL_FUTURE_INTENT_LBL);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_FUTURE_INTENT_REQ , PeopleStoriesFormConstants.VAL_FUTURE_INTENT_REQ);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_FUTURE_INTENT_VALIDATION , PeopleStoriesFormConstants.VAL_FUTURE_INTENT_VALIDATION); 

		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_GENDER_LIST , PeopleStoriesFormConstants.VAL_GENDER_LIST);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_GENDER_LBL , PeopleStoriesFormConstants.VAL_GENDER_LBL);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_GENDER_REQ , PeopleStoriesFormConstants.VAL_GENDER_REQ);

		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_ETHNICITY_LBL , PeopleStoriesFormConstants.VAL_ETHNICITY_LBL); 
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_ETHNICITY_REQ , PeopleStoriesFormConstants.VAL_ETHNICITY_REQ);

		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_ASIAN_LBL , PeopleStoriesFormConstants.VAL_ASIAN_LBL);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_AMERICAN_ALASKAN_LBL , PeopleStoriesFormConstants.VAL_AMERICAN_ALASKAN_LBL);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_BLACK_AFRICAN_AMERICAN_LBL , PeopleStoriesFormConstants.VAL_BLACK_AFRICAN_AMERICAN_LBL); 
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_HAWAIIAN_PACIFIC_ISLANDER_LBL , PeopleStoriesFormConstants.VAL_HAWAIIAN_PACIFIC_ISLANDER_LBL); 
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_HISPANIC_LATINO_LBL , PeopleStoriesFormConstants.VAL_HISPANIC_LATINO_LBL);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_WHITE_CAUCASIAN_LBL , PeopleStoriesFormConstants.VAL_WHITE_CAUCASIAN_LBL);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_OTHER_ETHNICITY_LBL , PeopleStoriesFormConstants.VAL_OTHER_ETHNICITY_LBL);

		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_UPLOAD_PHOTO_LBL , PeopleStoriesFormConstants.VAL_UPLOAD_PHOTO_LBL);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_PHOTO_VALIDATION_MSG , PeopleStoriesFormConstants.VAL_PHOTO_VALIDATION_MSG); 
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_PHOTO_REQ , PeopleStoriesFormConstants.VAL_PHOTO_REQ);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_BROWSE_INFO , PeopleStoriesFormConstants.VAL_BROWSE_INFO); 
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_BROWSE_BTN , PeopleStoriesFormConstants.VAL_BROWSE_BTN);

		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_VIDEO_LINK_LBL , PeopleStoriesFormConstants.VAL_VIDEO_LINK_LBL); 
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_VIDEO_LINK_REQ , PeopleStoriesFormConstants.VAL_VIDEO_LINK_REQ);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_VIDEO_LINK_FMT_VALIDATION_MSG , PeopleStoriesFormConstants.VAL_VIDEO_LINK_FMT_VALIDATION_MSG); 

		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_NON_EMPLOYEE_LBL , PeopleStoriesFormConstants.VAL_NON_EMPLOYEE_LBL);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_NON_EMP_FIRST_NAME , PeopleStoriesFormConstants.VAL_NON_EMP_FIRST_NAME); 
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_NON_EMP_FIRST_NAME_REQ , PeopleStoriesFormConstants.VAL_NON_EMP_FIRST_NAME_REQ); 

		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_NON_EMP_LAST_NAME_LBL , PeopleStoriesFormConstants.VAL_NON_EMP_LAST_NAME_LBL);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_NON_EMP_LAST_NAME_REQ , PeopleStoriesFormConstants.VAL_NON_EMP_LAST_NAME_REQ);

		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_NON_EMP_EMAIL_LBL , PeopleStoriesFormConstants.VAL_NON_EMP_EMAIL_LBL);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_NON_EMP_EMAIL_REQ , PeopleStoriesFormConstants.VAL_NON_EMP_EMAIL_REQ);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_NON_EMP_EMAIL_VALIDATION , PeopleStoriesFormConstants.VAL_NON_EMP_EMAIL_VALIDATION); 

		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_NON_EMP_ADD_COMMENTS_LBL , PeopleStoriesFormConstants.VAL_NON_EMP_ADD_COMMENTS_LBL);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_NON_EMP_ADD_COMMENTS_REQ , PeopleStoriesFormConstants.VAL_NON_EMP_ADD_COMMENTS_REQ);

		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_SUBMIT_LBL , PeopleStoriesFormConstants.VAL_SUBMIT_LBL);

		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_GLOBAL_ERROR_MSG , PeopleStoriesFormConstants.VAL_GLOBAL_ERROR_MSG); 
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_FORM_DESCRIPTION , PeopleStoriesFormConstants.VAL_FORM_DESCRIPTION);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_FORM_INFO , PeopleStoriesFormConstants.VAL_FORM_INFO);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_FORM_TYPE , PeopleStoriesFormConstants.VAL_FORM_TYPE);
		PrivateAccessor.setField(peopleStoriesFormHandler, PeopleStoriesFormConstants.PROP_FORM_ADMIN_EMAIL , PeopleStoriesFormConstants.VAL_FORM_ADMIN_EMAIL);

		doCallRealMethod().when(peopleStoriesFormHandler).activate();
		when(peopleStoriesFormHandler.getFirstName()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getFirstNameReq()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getLastName()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getLastNameReq()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getEmail()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getEmailValidationMessage()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getEmailReq()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getPhone()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getPhoneReq()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getPhoneValidationMessage()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getCity()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getCityReq()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getStates()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getStateReq()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getStatesLabel()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getPositions()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getPositionReq()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getPositionLabel()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getEmployeeFirstJob()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getFirstJobReq()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getFirstJobYes()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getFirstJobNo()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getArchewayOpportunityProgram()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getArchewayReq()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getArchewayYes()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getArchewayNo()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getArchewayNotSure()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getAdvisingServices()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getCurrentText()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getPastText()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getEnglishUnderArches()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getEnrolledText()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getGraduateText()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getCareerOnline()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getEnrolledOnlineText()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getGraduateOnlineText()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getCollegeTuition()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getEnrolledTuition()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getGraduateTuitionText()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getAdditionalComment()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getAdditionalCommentReq()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getIsMcdGreatPlace()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getIsMcdGreatPlaceReq()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getSpecificSkillInfo()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getSpecificSkillInfoReq()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getFutureIntent()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getFutureIntentReq()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getFutureIntentValidation()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getGenders()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getGenderReq()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getGenderLabel()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getEtnicity()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getEthnicityReq()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getAsian()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getAmericanAlaskan()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getBlackAfricanAmerican()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getHawaiianPacificIslander()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getHispanicLatino()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getWhiteCaucasian()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getOtherEthnicity()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getUploadPhoto()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getPhotoValidationMessage()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getPhotoReq()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getBrowseInfo()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getBrowseBtn()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getVideoLink()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getVideoLinkReq()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getVideoLinkFmtValidationMessage()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getNonEmployee()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getNonEmpFirstName()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getNonEmpFirstNameReq()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getNonEmpLastName()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getNonEmpLastNameReq()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getNonEmpEmail()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getNonEmpEmailReq()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getNonEmpEmailValidation()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getNonEmpAddComments()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getNonEmpAddCommentsReq()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getSubmitBtn()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getGlobalErrorMessage()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getFormDescription()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getFormInfo()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getFormType()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getFormAdminEmail()).thenCallRealMethod();
		when(peopleStoriesFormHandler.getPeopleStoriesFormSubmitUrl()).thenCallRealMethod();		
		
	}
	
	@Test public void getPeopleStoriesFormSubmitUrl() throws Exception {
		//SlingScriptHelper helper = getSlingScriptHelper();
		//assertNotNull(helper);
		//assertTrue(msg.length()>0);
	}
	

	@Test public void getFirstName() throws Exception {
		String msg = peopleStoriesFormHandler.getFirstName();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getFirstNameReq() throws Exception {
		String msg = peopleStoriesFormHandler.getFirstNameReq();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getLastName() throws Exception {
		String msg = peopleStoriesFormHandler.getLastName();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getLastNameReq() throws Exception {
		String msg = peopleStoriesFormHandler.getLastNameReq();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getEmail() throws Exception {
		String msg = peopleStoriesFormHandler.getEmail();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getEmailValidationMessage() throws Exception {
		String msg = peopleStoriesFormHandler.getEmailValidationMessage();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getEmailReq() throws Exception {
		String msg = peopleStoriesFormHandler.getEmailReq();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getPhone() throws Exception {
		String msg = peopleStoriesFormHandler.getPhone();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getPhoneReq() throws Exception {
		String msg = peopleStoriesFormHandler.getPhoneReq();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getPhoneValidationMessage() throws Exception {
		String msg = peopleStoriesFormHandler.getPhoneValidationMessage();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getCity() throws Exception {
		String msg = peopleStoriesFormHandler.getCity();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getCityReq() throws Exception {
		String msg = peopleStoriesFormHandler.getCityReq();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getStates() throws Exception {
		String[] msg = peopleStoriesFormHandler.getStates();
		assertNotNull(msg);
		assertTrue(msg.length>0);
	}

	@Test public void getStateReq() throws Exception {
		String msg = peopleStoriesFormHandler.getStateReq();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getStateDropDownDefaultText() throws Exception {
		String msg = peopleStoriesFormHandler.getStatesLabel();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getPositions() throws Exception {
		String[] msg = peopleStoriesFormHandler.getPositions();
		assertNotNull(msg);
		assertTrue(msg.length>0);
	}

	@Test public void getPositionReq() throws Exception {
		String msg = peopleStoriesFormHandler.getPositionReq();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getPositionLabel() throws Exception {
		String msg = peopleStoriesFormHandler.getPositionLabel();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getEmployeeFirstJob() throws Exception {
		String msg = peopleStoriesFormHandler.getEmployeeFirstJob();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getFirstJobReq() throws Exception {
		String msg = peopleStoriesFormHandler.getFirstJobReq();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getFirstJobYes() throws Exception {
		String msg = peopleStoriesFormHandler.getFirstJobYes();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getFirstJobNo() throws Exception {
		String msg = peopleStoriesFormHandler.getFirstJobNo();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getArchewayOpportunityProgram() throws Exception {
		String msg = peopleStoriesFormHandler.getArchewayOpportunityProgram();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getArchewayReq() throws Exception {
		String msg = peopleStoriesFormHandler.getArchewayReq();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getArchewayYes() throws Exception {
		String msg = peopleStoriesFormHandler.getArchewayYes();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getArchewayNo() throws Exception {
		String msg = peopleStoriesFormHandler.getArchewayNo();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getArchewayNotSure() throws Exception {
		String msg = peopleStoriesFormHandler.getArchewayNotSure();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getAdvisingServices() throws Exception {
		String msg = peopleStoriesFormHandler.getAdvisingServices();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getCurrentText() throws Exception {
		String msg = peopleStoriesFormHandler.getCurrentText();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getPastText() throws Exception {
		String msg = peopleStoriesFormHandler.getPastText();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getEnglishUnderArches() throws Exception {
		String msg = peopleStoriesFormHandler.getEnglishUnderArches();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getEnrolledText() throws Exception {
		String msg = peopleStoriesFormHandler.getEnrolledText();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getGraduateText() throws Exception {
		String msg = peopleStoriesFormHandler.getGraduateText();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getCareerOnline() throws Exception {
		String msg = peopleStoriesFormHandler.getCareerOnline();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getEnrolledOnlineText() throws Exception {
		String msg = peopleStoriesFormHandler.getEnrolledOnlineText();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getGraduateOnlineText() throws Exception {
		String msg = peopleStoriesFormHandler.getGraduateOnlineText();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getCollegeTuition() throws Exception {
		String msg = peopleStoriesFormHandler.getCollegeTuition();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getEnrolledTuition() throws Exception {
		String msg = peopleStoriesFormHandler.getEnrolledTuition();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getGraduateTuitionText() throws Exception {
		String msg = peopleStoriesFormHandler.getGraduateTuitionText();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getAdditionalComment() throws Exception {
		String msg = peopleStoriesFormHandler.getAdditionalComment();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getAdditionalCommentReq() throws Exception {
		String msg = peopleStoriesFormHandler.getAdditionalCommentReq();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getIsMcdGreatPlace() throws Exception {
		String msg = peopleStoriesFormHandler.getIsMcdGreatPlace();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getIsMcdGreatPlaceReq() throws Exception {
		String msg = peopleStoriesFormHandler.getIsMcdGreatPlaceReq();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getSpecificSkillInfo() throws Exception {
		String msg = peopleStoriesFormHandler.getSpecificSkillInfo();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getSpecificSkillInfoReq() throws Exception {
		String msg = peopleStoriesFormHandler.getSpecificSkillInfoReq();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getFutureIntent() throws Exception {
		String msg = peopleStoriesFormHandler.getFutureIntent();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getFutureIntentReq() throws Exception {
		String msg = peopleStoriesFormHandler.getFutureIntentReq();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getFutureIntentValidation() throws Exception {
		String msg = peopleStoriesFormHandler.getFutureIntentValidation();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getGenders() throws Exception {
		String[] msg = peopleStoriesFormHandler.getGenders();
		assertNotNull(msg);
		assertTrue(msg.length>0);
	}

	@Test public void getGenderReq() throws Exception {
		String msg = peopleStoriesFormHandler.getGenderReq();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}
	
	@Test public void getGenderLabel() throws Exception {
		String msg = peopleStoriesFormHandler.getGenderLabel();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getEtnicity() throws Exception {
		String msg = peopleStoriesFormHandler.getEtnicity();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getEthnicityReq() throws Exception {
		String msg = peopleStoriesFormHandler.getEthnicityReq();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getAsian() throws Exception {
		String msg = peopleStoriesFormHandler.getAsian();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}
	
	@Test public void getAmericanAlaskan() throws Exception {
		String msg = peopleStoriesFormHandler.getAmericanAlaskan();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getBlackAfricanAmerican() throws Exception {
		String msg = peopleStoriesFormHandler.getBlackAfricanAmerican();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getHawaiianPacificIslander() throws Exception {
		String msg = peopleStoriesFormHandler.getHawaiianPacificIslander();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getHispanicLatino() throws Exception {
		String msg = peopleStoriesFormHandler.getHispanicLatino();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getWhiteCaucasian() throws Exception {
		String msg = peopleStoriesFormHandler.getWhiteCaucasian();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getOtherEthnicity() throws Exception {
		String msg = peopleStoriesFormHandler.getOtherEthnicity();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getUploadPhoto() throws Exception {
		String msg = peopleStoriesFormHandler.getUploadPhoto();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getPhotoValidationMessage() throws Exception {
		String msg = peopleStoriesFormHandler.getPhotoValidationMessage();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getPhotoReq() throws Exception {
		String msg = peopleStoriesFormHandler.getPhotoReq();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getBrowseInfo() throws Exception {
		String msg = peopleStoriesFormHandler.getBrowseInfo();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getBrowseBtn() throws Exception {
		String msg = peopleStoriesFormHandler.getBrowseBtn();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getVideoLink() throws Exception {
		String msg = peopleStoriesFormHandler.getVideoLink();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getVideoLinkReq() throws Exception {
		String msg = peopleStoriesFormHandler.getVideoLinkReq();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getVideoLinkFmtValidationMessage() throws Exception {
		String msg = peopleStoriesFormHandler.getVideoLinkFmtValidationMessage();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getNonEmployee() throws Exception {
		String msg = peopleStoriesFormHandler.getNonEmployee();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getNonEmpFirstName() throws Exception {
		String msg = peopleStoriesFormHandler.getNonEmpFirstName();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getNonEmpFirstNameReq() throws Exception {
		String msg = peopleStoriesFormHandler.getNonEmpFirstNameReq();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getNonEmpLastName() throws Exception {
		String msg = peopleStoriesFormHandler.getNonEmpLastName();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getNonEmpLastNameReq() throws Exception {
		String msg = peopleStoriesFormHandler.getNonEmpLastNameReq();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getNonEmpEmail() throws Exception {
		String msg = peopleStoriesFormHandler.getNonEmpEmail();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getNonEmpEmailReq() throws Exception {
		String msg = peopleStoriesFormHandler.getNonEmpEmailReq();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getNonEmpEmailValidation() throws Exception {
		String msg = peopleStoriesFormHandler.getNonEmpEmailValidation();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getNonEmpAddComments() throws Exception {
		String msg = peopleStoriesFormHandler.getNonEmpAddComments();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getNonEmpAddCommentsReq() throws Exception {
		String msg = peopleStoriesFormHandler.getNonEmpAddCommentsReq();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getSubmitBtn() throws Exception {
		String msg = peopleStoriesFormHandler.getSubmitBtn();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getGlobalErrorMessage() throws Exception {
		String msg = peopleStoriesFormHandler.getGlobalErrorMessage();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getFormDescription() throws Exception {
		String msg = peopleStoriesFormHandler.getFormDescription();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getFormInfo() throws Exception {
		String msg = peopleStoriesFormHandler.getFormInfo();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getFormType() throws Exception {
		String msg = peopleStoriesFormHandler.getFormType();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}

	@Test public void getFormAdminEmail() throws Exception {
		String msg = peopleStoriesFormHandler.getFormAdminEmail();
		assertNotNull(msg);
		assertTrue(msg.length()>0);
	}
	
	@Test public void activate() throws Exception {
		peopleStoriesFormHandler.activate();
	}
}
