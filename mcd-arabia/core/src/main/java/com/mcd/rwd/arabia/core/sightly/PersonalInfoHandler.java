package com.mcd.rwd.arabia.core.sightly;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.widgets.DialogFieldSet;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.mcd.rwd.arabia.core.common.*;
import com.mcd.rwd.arabia.core.constants.ArabiaContactUsFormConstant;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

/**
 * Created by deepti_b on 09-05-2017.
 */
@Component(name = "personalinfo",value = "Personal Information", disableTargeting = true, group = "MCD ARABIA",
        path="/content/arabiacontactus", extraClientlibs = "mcd.arabia.author",
        actions = { "text: Personal Info", "-", "editannotate", "copymove", "delete", "insert" },
        tabs = {
                @Tab( touchUINodeName = "fieldslist" , title = "Fields List" ),
                @Tab( touchUINodeName = "fieldsdesc" , title = "Fields Text")})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PersonalInfoHandler {

    @DialogField(tab = 1, cssClass = "personalinfoarabia", value = "ctitle", defaultValue = "abc",
            name = "./fieldsList", fieldLabel = "FieldList",
            additionalProperties = @Property(name = "personal-info-select", value = ""),
            fieldDescription = "Note: First Name,Last Name and Email Address is Mandatory(*) field.")
    @Selection(type = Selection.CHECKBOX, multiple = true,
            options = {
                    @Option(text="User Title" , value="ctitle"),
                    @Option(text = "Middle Name" , value = "mname"),
                    @Option(text = "Mobile Phone Country Code" , value = "ccntrycode"),
                    @Option(text = "Mobile Phone Service Provider Code" , value = "careacode"),
                    @Option(text = "Mobile Phone Number" , value = "cnumber"),
                    @Option(text = "Gender" , value = "cgender"),
                    @Option(text = "Nationality" , value = "cnationality"),
                    @Option(text = "Marital Status" , value = "cmarstatus"),
                    @Option(text = "Preferred Method of Contact" , value = "cprefcontact"),
                    @Option(text = "Hear About Us" , value = "chearus"),
                    @Option(text = "Date of Birth" , value = "cdob")
            })
    @Named("fieldsList")
    @Inject
    private String[] checkboxlist;

    @DialogField(tab = 2, fieldLabel = "User Title",
            additionalProperties = @Property(name = "itemId", value = "ctitle"))
    @DialogFieldSet(title = "User Title", collapsible = true, collapsed = true, namePrefix = "titledesc/")
    @ChildResource(name =  "titledesc")
    @Inject
    UserTitle titledesc;

    @DialogField(tab = 2, fieldLabel = "First Name *",
            additionalProperties = @Property(name = "itemId", value = "fname"))
    @DialogFieldSet(title = "First Name *", collapsible = true, collapsed = true, namePrefix = "firstnamedesc/")
    @ChildResource(name = "firstnamedesc")
    @Inject
    FirstName firstnamedesc;

    @DialogField(tab = 2, fieldLabel = "Middle Name",
            additionalProperties = @Property(name = "itemId", value = "mname"))
    @DialogFieldSet(title = "Middle Name", collapsible = true, collapsed = true, namePrefix = "middlenamedesc/")
    @ChildResource(name = "middlenamedesc")
    @Inject
    MiddleName middlenamedesc;

    @DialogField(tab = 2, fieldLabel = "Last Name *",
            additionalProperties = @Property(name = "itemId", value = "lname"))
    @DialogFieldSet(title = "Last Name *", collapsible = true, collapsed = true, namePrefix = "lastnamedesc/")
    @ChildResource(name = "lastnamedesc")
    @Inject
    LastName lastnamedesc;

    @DialogField(tab = 2, fieldLabel = "Email Address *",
            additionalProperties = @Property(name = "itemId", value = "cemail"))
    @DialogFieldSet(title = "Email Address *", collapsible = true, collapsed = true, namePrefix = "emaildesc/")
    @ChildResource(name = "emaildesc")
    @Inject
    EmailId emaildesc;

    @DialogField(tab = 2, fieldLabel = "Mobile Phone Country Code",
            additionalProperties = @Property(name = "itemId", value = "ccntrycode"))
    @DialogFieldSet(title = "Mobile Phone Country Code", collapsible = true, collapsed = true, namePrefix = "countrycodedesc/")
    @ChildResource(name = "countrycodedesc")
    @Inject
    CountryCode countrycodedesc;

    @DialogField(tab = 2, fieldLabel = "Mobile Phone Service  Provider Code",
            additionalProperties = @Property(name = "itemId", value = "careacode"))
    @DialogFieldSet(title = "Mobile Phone Service  Provider Code", collapsible = true, collapsed = true, namePrefix = "areacodedesc/")
    @ChildResource(name = "areacodedesc")
    @Inject
    AreaCode areacodedesc;

    @DialogField(tab = 2, fieldLabel = "Mobile Phone Number",
            additionalProperties = @Property(name = "itemId", value = "cnumber"))
    @DialogFieldSet(title = "Mobile Phone Number", collapsible = true, collapsed = true, namePrefix = "numberdesc/")
    @ChildResource(name = "numberdesc")
    @Inject
    PhoneNumber numberdesc;

    @DialogField(tab = 2, fieldLabel = "Gender",
            additionalProperties = @Property(name = "itemId", value = "cgender"))
    @DialogFieldSet(title = "Gender", collapsible = true, collapsed = true, namePrefix = "genderdesc/")
    @ChildResource(name = "genderdesc")
    @Inject
    Gender genderdesc;

    @DialogField(tab = 2, fieldLabel = "Nationality",
            additionalProperties = @Property(name = "itemId", value = "cnationality"))
    @DialogFieldSet(title = "Nationality", collapsible = true, collapsed = true, namePrefix = "nationalitydesc/")
    @ChildResource(name = "nationalitydesc")
    @Inject
    Nationality nationalitydesc;

    @DialogField(tab = 2, fieldLabel = "Marital Status",
            additionalProperties = @Property(name = "itemId", value = "cmarstatus"))
    @DialogFieldSet(title = "Marital Status", collapsible = true, collapsed = true, namePrefix = "maritalstatusdesc/")
    @ChildResource(name = "maritalstatusdesc")
    @Inject
    MartialStatus maritalstatusdesc;

    @DialogField(tab = 2, fieldLabel = "Preferred Method Of Contact",
            additionalProperties = @Property(name = "itemId", value = "cprefcontact"))
    @DialogFieldSet(title = "Preferred Method Of Contact", collapsible = true, collapsed = true, namePrefix = "prefcontactdesc/")
    @ChildResource(name = "prefcontactdesc")
    @Inject
    PreferredContact prefcontactdesc;

    @DialogField(tab = 2, fieldLabel = "Hear About Us",
            additionalProperties = @Property(name = "itemId", value = "chearus"))
    @DialogFieldSet(title = "Hear About Us", collapsible = true, collapsed = true, namePrefix = "hearabtusdesc/")
    @ChildResource(name = "hearabtusdesc")
    @Inject
    HearAboutUs hearabtusdesc;

    @DialogField(tab = 2, fieldLabel = "Date of Birth",
            additionalProperties = @Property(name = "itemId", value = "cdob"))
    @DialogFieldSet(title = "Date of Birth", collapsible = true, collapsed = true, namePrefix = "dobdesc/")
    @ChildResource(name = "dobdesc")
    @Inject
    DateOfBirth dobdesc;

    private static final Logger log = LoggerFactory.getLogger(PersonalInfoHandler.class);

    public static final String REQUIRED_YES_VALUE = "yes";
    public static final String REQUIRED_NO_VALUE = "no";
    public static final String EMPTY_STRING = "";

    private String titleText = "";
    private List<String> titleOptions;
    private String titleReq = "";
    private String titleMessage="";
    private Boolean showTitle=false;

    private String firstName="";
    private String firstNameReq="";
    private String firstNameMessage="";


    private String middleName="";
    private String middleNameReq="";
    private String middleNameMessage="";
    private Boolean showMiddleName=false;

    private String lastName="";
    private String lastNameReq="";
    private String lastNameMessage="";

    private String email="";
    private String emailReq="";
    private String emailMessage="";

    private String gender = "";
    private List<String> genderOptions;
    private String genderReq = "";
    private String genderMessage="";
    private Boolean showGender=false;

    private String nationality = "";
    private List<String> nationalityOptions;
    private String nationalityReq = "";
    private String nationalityMessage="";
    private Boolean showNationality=false;

    private String maritalstatus = "";
    private List<String> maritalstatusOptions;
    private String maritalstatusReq = "";
    private String maritalstatusMessage="";
    private Boolean showMaritalStatus=false;


    private String prefContact = "";
    private List<String> prefContactOptions;
    private String prefContactReq = "";
    private String prefContactMessage="";
    private Boolean showPrefContact=false;

    private String hearFromUs = "";
    private List<String> hearFromUsOptions;
    private String hearFromUsReq = "";
    private String hearFromUsMessage="";
    private Boolean showHearFromUs=false;

    private String dob="";
    private String date="";
    private List<String> dateOptions;
    private String month="";
    private Map<String,String> monthOptions;
    private String year="";
    private List<Integer> yearOptions;
    private String dobReq="";



    private String dobMessage="";
    private Boolean showDOB=false;

    private String countryCode="";
    private String countryCodeReq="";
    private String countryCodeMessage="";
    private Boolean showCountryCode=false;

    private String areaCode="";
    private String areaCodeReq="";
    private String areaCodeMessage="";
    private Boolean showAreaCode=false;

    private String number="";
    private String numberReq="";
    private String numberMessage="";
    private Boolean showNumber=false;

    private StringBuffer formsFieldsBuffr=new StringBuffer();
    private String formFieldList="";

    private String[] fieldDisplayedArr;

    private List<String> fieldDisplayList=new ArrayList<String>();

    @PostConstruct
    public void activate() throws Exception {
        fieldDisplayedArr= checkboxlist;
        if(fieldDisplayedArr !=null && fieldDisplayedArr.length >0){
            fieldDisplayList = Arrays.asList(fieldDisplayedArr);
        }

        if(null != titledesc) {
            titleText = titledesc.getTitleText();
            titleOptions = getDropdownOptions(titledesc.getTitle());
            titleReq = titledesc.getTitleValidation();
            titleMessage = titledesc.getTitleValidationMessage();
        }
        showTitle=fieldDisplayList.contains("ctitle");
        populateFieldList(showTitle,ArabiaContactUsFormConstant.PERSONAL_INFO_TITLE);

        if(null!=firstnamedesc) {
            firstName = firstnamedesc.getFirstNameText();
            firstNameReq = REQUIRED_YES_VALUE;
            firstNameMessage = firstnamedesc.getFirstNameValidationMessage();
        }else{
            firstName = ArabiaContactUsFormConstant.FIRST_NAME;
        }
        populateFieldList(true,ArabiaContactUsFormConstant.PERSONAL_INFO_FIRST_NAME);

        if(null!=middlenamedesc) {
            middleName = middlenamedesc.getMiddleNameText();
            middleNameReq = middlenamedesc.getMiddleNameValidation();
            middleNameMessage = middlenamedesc.getMiddleNameValidationMessage();
        }
        showMiddleName=fieldDisplayList.contains("mname");
        populateFieldList(showMiddleName,ArabiaContactUsFormConstant.PERSONAL_INFO_MIDDLE_NAME);

        if(null!=lastnamedesc) {
            lastName = lastnamedesc.getLastNameText();
            lastNameReq = REQUIRED_YES_VALUE;
            lastNameMessage = lastnamedesc.getLastNameValidationMessage();
        }else{
            lastName = ArabiaContactUsFormConstant.LAST_NAME;
        }
        populateFieldList(true,ArabiaContactUsFormConstant.PERSONAL_INFO_LAST_NAME);

        if(null!=emaildesc) {
            email = emaildesc.getEmailText();
            emailReq = REQUIRED_YES_VALUE;
            emailMessage = emaildesc.getEmailValidationMessage();
        }else{
            email = ArabiaContactUsFormConstant.EMAIL;
        }
        populateFieldList(true,ArabiaContactUsFormConstant.PERSONAL_INFO_EMAIL);

        if(null!=genderdesc) {
            gender = genderdesc.getGenderText();
            genderOptions = getDropdownOptions(genderdesc.getGender());
            genderReq = genderdesc.getGenderValidation();
            genderMessage = genderdesc.getGenderValidationMessage();
        }
        showGender=fieldDisplayList.contains("cgender");
        populateFieldList(showGender,ArabiaContactUsFormConstant.PERSONAL_INFO_GENDER);

        if(null!=nationalitydesc) {
            nationality = nationalitydesc.getNationalityText();
            nationalityOptions=getDropdownOptions(nationalitydesc.getNationality());
            nationalityReq = nationalitydesc.getNationalityValidation();
            nationalityMessage = nationalitydesc.getNationalityValidationMessage();
        }
        showNationality=fieldDisplayList.contains("cnationality");
        populateFieldList(showNationality,ArabiaContactUsFormConstant.PERSONAL_INFO_NATIONALITY);

        if(null!=maritalstatusdesc) {
            maritalstatus = maritalstatusdesc.getMaritalStatusText();
            maritalstatusOptions=getDropdownOptions(maritalstatusdesc.getMaritalStatus());
            maritalstatusReq = maritalstatusdesc.getMaritalStatusValidation();
            maritalstatusMessage = maritalstatusdesc.getMaritalValidationMessage();
        }
        showMaritalStatus=fieldDisplayList.contains("cmarstatus");
        populateFieldList(showMaritalStatus,ArabiaContactUsFormConstant.PERSONAL_INFO_MARITALSTATUS);

        if(null!=prefcontactdesc) {
            prefContact = prefcontactdesc.getPrefContactText();
            prefContactOptions=getDropdownOptions(prefcontactdesc.getPrefContact());
            prefContactReq = prefcontactdesc.getPrefContactValidation();
            prefContactMessage = prefcontactdesc.getPrefContactValidationMessage();
        }
        showPrefContact=fieldDisplayList.contains("cprefcontact");
        populateFieldList(showPrefContact,ArabiaContactUsFormConstant.PERSONAL_INFO_PREFCOTACT);

        if(null!=hearabtusdesc) {
            hearFromUs = hearabtusdesc.getHearAboutUsText();
            hearFromUsOptions=getDropdownOptions(hearabtusdesc.getHeearAboutUs());
            hearFromUsReq = hearabtusdesc.getHearAboutUsValidation();
            hearFromUsMessage = hearabtusdesc.getHearAboutUsValidationMessage();
        }
        showHearFromUs=fieldDisplayList.contains("chearus");
        populateFieldList(showHearFromUs,ArabiaContactUsFormConstant.PERSONAL_INFO_HEARFROM);

        if(null!=dobdesc) {
            dob = dobdesc.getDobText();
            date = dobdesc.getDateText();
            dateOptions = getDropdownDateList();
            month = dobdesc.getMonthText();
            monthOptions = getDropdownValueSet(dobdesc.getMonth());
            year = dobdesc.getYearText();
            yearOptions = getDropdownYearList(dobdesc.getYearRange());
            dobReq = dobdesc.getDobValidation();
            dobMessage = dobdesc.getDateOfBirthValidationMessage();
        }
        showDOB=fieldDisplayList.contains("cdob");
        populateFieldList(showDOB,ArabiaContactUsFormConstant.PERSONAL_INFO_DATEOFBIRTH);

        if(null!=countrycodedesc) {
            countryCode = countrycodedesc.getCountrycodeText();
            countryCodeReq = countrycodedesc.getCountrycodeValidation();
            countryCodeMessage = countrycodedesc.getCountrycodeValidationMessage();
        }
        showCountryCode=fieldDisplayList.contains("ccntrycode");
        populateFieldList(showCountryCode,ArabiaContactUsFormConstant.PERSONAL_INFO_COUNRYCODE);

        if(null!=areacodedesc) {
            areaCode = areacodedesc.getAreacodeText();
            areaCodeReq = areacodedesc.getAreacodeValidation();
            areaCodeMessage = areacodedesc.getAreacodeValidationMessage();
        }
        showAreaCode=fieldDisplayList.contains("careacode");
        populateFieldList(showAreaCode,ArabiaContactUsFormConstant.PERSONAL_INFO_AREACODE);

        if(null!=numberdesc) {
            number = numberdesc.getNumberText();
            numberReq = numberdesc.getNumberValidation();
            numberMessage = numberdesc.getNumberValidationMessage();
        }
        showNumber=fieldDisplayList.contains("cnumber");
        populateFieldList(showNumber,ArabiaContactUsFormConstant.PERSONAL_INFO_NUMBER);
        formFieldList=formsFieldsBuffr.toString();
    }


    public List<String>  getDropdownOptions(String[] propValue){
        List<String> optionList= new ArrayList<String>();;
        if (null != propValue) {
            for (int i = 0; i < propValue.length; i++) {
                String options = propValue[i];
                optionList.add(options);
            }
        }
        return optionList;
    }

    //get year range and set list of year from range year to current year
    public List<Integer>  getDropdownYearList(String propValue){

        List<Integer> optionList= new ArrayList<Integer>();
        if (null != propValue) {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            int prevYear = 0;
            try {
                prevYear = Integer.parseInt(propValue);
            } catch(Exception exception){
                prevYear = 1950;
            }
            for (int i = prevYear; i <= year; i++) {
                optionList.add(i);
            }
        }
        return optionList;
    }

    //get dropdown list for month and set values
    public Map<String, String>  getDropdownValueSet(String[] propValue){

        Map<String,String> optionList= new TreeMap<String,String>();
        if (null != propValue) {
            for (int i = 1; i <= propValue.length; i++) {
                String val = i+"";
                if(i<10){
                    val = "0"+i;
                }
                optionList.put(val, propValue[i-1]);
            }
        }
        return optionList;
    }

    //set dropdown list for Date
    public List<String>  getDropdownDateList(){
        List<String>  optionList= new ArrayList<String>();
        for (int i = 1; i <= 31; i++) {
            String val = i+"";
            if(i<10){
                val = "0"+i;
            }
            optionList.add(val);
        }
        return optionList;
    }

    public void populateFieldList(Boolean displayFlag,String str){
        if(displayFlag){
            formsFieldsBuffr.append(str);
            formsFieldsBuffr.append(',');
        }
    }



    public String getTitleText() {
        return titleText;
    }

    public List<String> getTitleOptions() {
        return titleOptions;
    }

    public String getTitleReq() {
        return titleReq;
    }

    public String getTitleMessage() {
        return titleMessage;
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

    public String getMiddleName() {
        return middleName;
    }

    public String getMiddleNameReq() {
        return middleNameReq;
    }

    public String getMiddleNameMessage() {
        return middleNameMessage;
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

    public String getEmail() {
        return email;
    }

    public String getEmailReq() {
        return emailReq;
    }

    public String getEmailMessage() {
        return emailMessage;
    }

    public String getGender() {
        return gender;
    }

    public List<String> getGenderOptions() {
        return genderOptions;
    }

    public String getGenderReq() {
        return genderReq;
    }

    public String getGenderMessage() {
        return genderMessage;
    }

    public String getNationality() {
        return nationality;
    }

    public List<String> getNationalityOptions() {
        return nationalityOptions;
    }

    public String getNationalityReq() {
        return nationalityReq;
    }

    public String getNationalityMessage() {
        return nationalityMessage;
    }

    public String getMaritalstatus() {
        return maritalstatus;
    }

    public List<String> getMaritalstatusOptions() {
        return maritalstatusOptions;
    }

    public String getMaritalstatusReq() {
        return maritalstatusReq;
    }

    public String getMaritalstatusMessage() {
        return maritalstatusMessage;
    }

    public String getPrefContact() {
        return prefContact;
    }

    public List<String> getPrefContactOptions() {
        return prefContactOptions;
    }

    public String getPrefContactReq() {
        return prefContactReq;
    }

    public String getPrefContactMessage() {
        return prefContactMessage;
    }

    public String getHearFromUs() {
        return hearFromUs;
    }

    public List<String> getHearFromUsOptions() {
        return hearFromUsOptions;
    }

    public String getHearFromUsReq() {
        return hearFromUsReq;
    }

    public String getHearFromUsMessage() {
        return hearFromUsMessage;
    }

    public String getDob() {
        return dob;
    }

    public String getDobReq() {
        return dobReq;
    }

    public String getDobMessage() {
        return dobMessage;
    }

    public static Logger getLog() {
        return log;
    }

    public List<String> getDateOptions() {
        return dateOptions;
    }

    public Map<String,String> getMonthOptions() {

        return monthOptions;
    }

    public List<Integer> getYearOptions() {
        return yearOptions;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getCountryCodeReq() {
        return countryCodeReq;
    }

    public String getCountryCodeMessage() {
        return countryCodeMessage;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public String getAreaCodeReq() {
        return areaCodeReq;
    }

    public String getAreaCodeMessage() {
        return areaCodeMessage;
    }

    public String getNumber() {
        return number;
    }

    public String getNumberReq() {
        return numberReq;
    }

    public String getNumberMessage() {
        return numberMessage;
    }

    public Boolean getShowMiddleName() {
        return showMiddleName;
    }

    public Boolean getShowGender() {
        return showGender;
    }

    public Boolean getShowNationality() {
        return showNationality;
    }

    public Boolean getShowMaritalStatus() {
        return showMaritalStatus;
    }

    public Boolean getShowPrefContact() {
        return showPrefContact;
    }

    public Boolean getShowHearFromUs() {
        return showHearFromUs;
    }

    public Boolean getShowDOB() {
        return showDOB;
    }

    public Boolean getShowCountryCode() {
        return showCountryCode;
    }

    public Boolean getShowAreaCode() {
        return showAreaCode;
    }

    public Boolean getShowNumber() {
        return showNumber;
    }

    public Boolean getShowTitle() {
        return showTitle;
    }


    public String getFormFieldList() {
        return formFieldList;
    }
    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDate() {
        return date;
    }

}
