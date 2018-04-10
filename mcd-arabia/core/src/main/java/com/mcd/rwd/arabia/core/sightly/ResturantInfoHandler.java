package com.mcd.rwd.arabia.core.sightly;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.widgets.MultiField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.mcd.rwd.arabia.core.constants.ArabiaContactUsFormConstant;
import com.mcd.rwd.us.core.constants.FormConstants;
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
import java.util.*;

/**
 * Created by deepti_b on 12-05-2017.
 */
@Component(name = "resturantinfo",value = "Restaurant Information",
        disableTargeting = true, group = "MCD ARABIA" , path="/content/arabiacontactus",
        actions = { "text: Arabia Restaurant Info", "-", "editannotate", "copymove", "delete", "insert" },
        listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
                @Listener(name = "afterinsert", value = "REFRESH_PAGE")},
        tabs = {
                @Tab( touchUINodeName = "section" , title = "Restaurant Information" )})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ResturantInfoHandler {

    @DialogField(name = "./sectionTopHeading", fieldLabel = "Section Top Heading",
            fieldDescription = "Heading to be displayed on top of section.")
    @TextField
    @Inject @Named("sectionTopHeading")
    @Default(values = StringUtils.EMPTY)
    private String topHeading;

    @DialogField(name = "./sectionSubHeading", fieldLabel = "Section Sub Heading",
            fieldDescription = "Sub heading to be displayed on top of section.")
    @TextField
    @Inject @Named("sectionSubHeading")
    @Default(values = StringUtils.EMPTY)
    private String subHeading;

    @DialogField(fieldLabel = "Restaurant Locator Button Text:",
            fieldDescription = "Text to be displayed for restaurant locator button." +
                    "Default set to ' Restaurant Locator'")
    @TextField
    @Inject @Default(values = FormConstants.RESTAURANT_LOCATOR_TEXT)
    private String buttonText;

    @DialogField(name = "./locatorSearchText", fieldLabel = "Restaurant Locator Search Text",
            fieldDescription = "Text for restaurant location search. Default set to 'Can't" +
                    " find the location you're looking for?'")
    @TextField @Default(values = FormConstants.RESTAURANT_LOCATOR_SEARCH_TEXT)
    @Inject @Named("locatorSearchText")
    private String locationSearchText;

    @DialogField(fieldLabel = "Locate Me Button Text:",
            fieldDescription = "Text to be displayed for locate me button." +
                    "Default set to ' Locate Me'")
    @TextField
    @Inject @Default(values = FormConstants.RESTAURANT_LOCATEME_TEXT)
    private String locateMeButtonText;

    @DialogField(name = "./seperatorText", fieldLabel = "Seperator PlaceHolder Text:",
            fieldDescription = "Text to be displayed between Restaurant Locator and Locate " +
                    "Me Button. Default set to ' OR'")
    @TextField @Default(values = "Or")
    @Inject @Named("seperatorText")
    private String placeholdertext;

    @DialogField(name = "./resAddress", fieldLabel = "Restaurant Address Text",
            fieldDescription = "Text for restaurant address field. Default set" +
                    " to 'Restaurant Address: '")
    @TextField
    @Inject @Named("resAddress")
    @Default(values = ArabiaContactUsFormConstant.RESTAURANT_ADDRESS_TEXT)
    private String address;

    @DialogField(name = "./resAddressReq", fieldLabel = "Restaurant Address Required",
            fieldDescription = "Please select restaurant address is " +
            "required or not. Default set to 'Yes'")
    @Selection(type = Selection.SELECT,
            options = {
                    @Option(text="Yes" , value="yes"),
                    @Option(text="No" , value="no")
            })
    @Named("resAddressReq")
    @Inject @Default(values = ArabiaContactUsFormConstant.REQUIRED_NO_VALUE)
    private String addressValidation;

    @DialogField(name = "./resAddressMessage", fieldLabel = "Restaurant Address Validation Message",
            fieldDescription = "Validation message for restaurant address field. Default set " +
                    "to 'Please enter Restaurant Address'")
    @TextField @Default(values = ArabiaContactUsFormConstant.RESTAURANT_ADDRESS_VALIDATION_MESSAGE)
    @Inject @Named("resAddressMessage")
    private String addressValidationMessage;

    @DialogField(name = "./resStateText", fieldLabel = "City Text",
            fieldDescription = "Text for city field. Default set to 'City '")
    @TextField @Default(values = ArabiaContactUsFormConstant.CITY)
    @Inject @Named("resStateText")
    private String stateText;

    @DialogField(fieldLabel = "City", fieldDescription = "Please provide city name by clicking + button.")
    @MultiField
    @TextField
    @Inject
    private String[] state;

    @DialogField(name = "./resStateReq", fieldLabel = "City Required",
            fieldDescription = "Please select city is required or " +
            "not. Default set to 'Yes'")
    @Selection(type = Selection.SELECT,
            options = {
                    @Option(text="Yes" , value="yes"),
                    @Option(text="No" , value="no")
            })
    @Named("resStateReq")
    @Inject @Default(values = ArabiaContactUsFormConstant.REQUIRED_YES_VALUE)
    private String stateValidation;

    @DialogField(name = "./resStateMessage", fieldLabel = "City Validation Message",
            fieldDescription = "Validation message for City field. Default set to 'Please " +
                    "select Restaurant's City'")
    @TextField
    @Inject @Named("resStateMessage")
    @Default(values = ArabiaContactUsFormConstant.RESTAURANT_CITY_VALIDATION_MESSAGE)
    private String stateValidationMessage;

    @DialogField(name = "./date", fieldLabel = "Date of Visit Text",
            fieldDescription = "Text for Date of Visit field. If no text Provided field will not be visible in form.")
    @TextField @Default(values = ArabiaContactUsFormConstant.EMPTY_STRING)
    @Inject @Named("date")
    private String dateVal;

    @DialogField(name = "./resDateReq", fieldLabel = "Date Required",
            fieldDescription = "Please select yes if visit date" +
            " is required. Default set to 'No'")
    @Selection(type = Selection.SELECT,
            options = {
                    @Option(text="Yes" , value="yes"),
                    @Option(text="No" , value="no")
            })
    @Inject @Named("resDateReq") @Default(values = FormConstants.REQUIRED_NO_VALUE)
    private String dateValidation;

    @DialogField(name = "./dateText", fieldLabel = "Date Text",
            fieldDescription = "Text for Date  field. Default set to 'Date'." +
                    "This value will be considered as a Representative value of DropDown.")
    @TextField @Default(values = ArabiaContactUsFormConstant.DATE)
    @Inject @Named("dateText")
    private String dateTextVal;

    @DialogField(fieldLabel = "Month Text",
            fieldDescription = "Text for Month field. Default set to 'Month'." +
                    "This value will be considered as a Representative value of DropDown.")
    @TextField
    @Inject @Default(values = ArabiaContactUsFormConstant.MONTH)
    private String monthText;

    @DialogField(name = "./month", fieldLabel = "Month", fieldDescription = "Please provide Month by clicking + " +
            "button. First provided value will be considered as a first month of Year")
    @MultiField
    @TextField
    @Inject @Named("month")
    @Default(values = StringUtils.EMPTY)
    private String[] monthVal;

    @DialogField(fieldLabel = "Year Text",
            fieldDescription = "Text for Year field. Default set to 'Year'." +
                    "This value will be considered as a Representative value of DropDown.")
    @TextField
    @Inject @Default(values = ArabiaContactUsFormConstant.YEAR)
    private String yearText;

    @DialogField(fieldLabel = "Year Range",
            fieldDescription = "Provide Year Range. Drop down values would contain years " +
                    "from provided Past year in the field to Current Year.")
    @TextField
    @Inject
    @Default(values = StringUtils.EMPTY)
    private String yearRange;

    @DialogField(name = "./dateMessage", fieldLabel = "Date Validation Message",
            fieldDescription = "Validation message for Date Of Visit field. Default set to" +
                    " 'Please select an appropriate Date of Visit '")
    @TextField @Default(values = ArabiaContactUsFormConstant.RESTAURANT_DATE_VALIDATION_MESSAGE)
    @Inject @Named("dateMessage")
    private String dateOfPartyValidationMessage;

    private static final Logger log = LoggerFactory.getLogger(ResturantInfoHandler.class);

    private String sectionTopHeading = StringUtils.EMPTY;

    private String sectionSubHeading = StringUtils.EMPTY;

    private String locatorSearchText = StringUtils.EMPTY;

    private String resAddress = StringUtils.EMPTY;

    private String resAddressReq = StringUtils.EMPTY;

    private String resAddressMessage = StringUtils.EMPTY;

    private String resStateText = StringUtils.EMPTY;

    private List<String> resStateOptions;

    private String resStateReq = StringUtils.EMPTY;

    private String resStateMessage = StringUtils.EMPTY;

    private String dateText = StringUtils.EMPTY;

    private String date = StringUtils.EMPTY;

    private String month = StringUtils.EMPTY;

    private String year = StringUtils.EMPTY;

    private List<String> dateOptions;

    private Map<String,String> monthOptions;
   
    private List<Integer> yearOptions;

    private String dateReq = StringUtils.EMPTY;

    private String dateMessage = StringUtils.EMPTY;

    private String placeHolderText= StringUtils.EMPTY;

    private StringBuffer formsFieldsBuffr=new StringBuffer();

    private String formFieldList="";

    public String getDate() {
        return date;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
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

    @PostConstruct
    public void activate() throws Exception {
        log.debug("inside activate method...");
        log.debug("set the values from dialog properties");
        sectionTopHeading = this.topHeading;
        sectionSubHeading = this.subHeading;
        locatorSearchText = this.locationSearchText;

        resAddress = this.address;
        resAddressReq = this.addressValidation;
        resAddressMessage = this.addressValidationMessage;
        populateFieldList(resAddress,ArabiaContactUsFormConstant.CELEBRATION_STOREADRRESS);
        populateFieldList(resAddress,ArabiaContactUsFormConstant.CONTACTUS_STOREADRRESS);
        resStateText = this.stateText;
        String[] resStateOptionsData = null!=state ? this.state : null;
        if (null != resStateOptionsData) {
            resStateOptions = getListOfOptionData(resStateOptionsData);
        }
        resStateReq = this.stateValidation;
        resStateMessage = this.stateValidationMessage;
        populateFieldList(resStateText,ArabiaContactUsFormConstant.CELEBRATION_STORECITY);
        populateFieldList(resStateText,ArabiaContactUsFormConstant.CONTACTUS_STORECITY);
        dateText = this.dateVal;
        date=this.dateTextVal;
        dateOptions=getDropdownDateList();
        month=this.monthText;
        monthOptions=getDropdownValueSet(monthVal);
        year=this.yearText;
        yearOptions =getDropdownYearList(yearRange);
        dateReq = this.dateValidation;
        dateMessage = this.dateOfPartyValidationMessage;
        populateFieldList(dateText,ArabiaContactUsFormConstant.CONTACTUS_VISITDATE);
        placeHolderText = this.placeholdertext;
        formFieldList=formsFieldsBuffr.toString();
        log.debug("activate method end...");
    }

    private List<String> getListOfOptionData(String[] optionsDataArray) {
        List<String> options = new ArrayList<String>();
        for (String anOptionsDataArray : optionsDataArray) {
            options.add(anOptionsDataArray);
        }
        return options;
    }

    
    public void populateFieldList(String fieldText,String str){
        if(StringUtils.isNotBlank(fieldText)){
            formsFieldsBuffr.append(str);
            formsFieldsBuffr.append(',');
        }

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


    public String getDateReq() {
        return dateReq;
    }

    public String getDateMessage() {
        return dateMessage;
    }

    public String getLocateMeButtonText() {
        return locateMeButtonText;
    }


    public String getDateText() {
        return dateText;
    }
    public String getPlaceHolderText() {
        return placeHolderText;
    }

    public String getFormFieldList() {
        return formFieldList;
    }


}
