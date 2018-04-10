package com.mcd.rwd.arabia.core.sightly;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.widgets.MultiField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.mcd.rwd.arabia.core.constants.ArabiaContactUsFormConstant;
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
 * Created by deepti_b on 11-05-2017.
 */
@Component(name = "celebrationform",value = "Celebration  Form",
        description = "Celebration form component",
        disableTargeting = true, group = "MCD ARABIA" , path="/content/arabiacontactus",
        actions = { "text: Celebrations  section", "-", "editannotate", "copymove", "delete", "insert" },
        listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
                @Listener(name = "afterinsert", value = "REFRESH_PAGE")},
        tabs = {
                @Tab( touchUINodeName = "celebrationForm" , title = "Celebrations Component" )})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CelebrationFormHandler {

    @DialogField(name = "./childLabelName", fieldLabel = "Name of Child Text",
            fieldDescription = "Text for Name of Child field. If no text Provided " +
                    "field will not be visible in form.")
    @TextField @Default(values = ArabiaContactUsFormConstant.EMPTY_STRING)
    @Inject @Named("childLabelName")
    private String nameofchild;

    @DialogField(name = "./childNameReq", fieldLabel = "Name of Child Required",
            fieldDescription = "Please select Name of Child" +
            " is required or not. Default set to 'No'")
    @Selection(type = Selection.SELECT,
            options = {
                    @Option(text="Yes" , value="yes"),
                    @Option(text="No" , value="no")
            })
    @Named("childNameReq")
    @Inject @Default(values = ArabiaContactUsFormConstant.REQUIRED_NO_VALUE)
    private String namOfChildValidation;

    @DialogField(name = "./childNameMessage", fieldLabel = "Name of Child  Validation Message",
            fieldDescription = "Validation message for Name of Child field." +
                    " Default set to 'Please enter Child Name. '")
    @TextField @Named("childNameMessage")
    @Inject @Default(values = ArabiaContactUsFormConstant.CHILD_VALIDATION_MSG)
    private String childNameValidationMessage;

    @DialogField(name = "./numKids", fieldLabel = "Number of Kids Text",
            fieldDescription = "Text for Number of Kids field. If no text Provided field " +
                    "will not be visible in form.")
    @TextField @Named("numKids")
    @Inject @Default(values = ArabiaContactUsFormConstant.CHILD_VALIDATION_MSG)
    private String noofkids;

    @DialogField(name = "./numKidsReq", fieldLabel = "Number of Kids Required",
            fieldDescription = "Please select Number of Kids" +
            " is required or not. Default set to 'No'")
    @Selection(type = Selection.SELECT,
            options = {
                    @Option(text="Yes" , value="yes"),
                    @Option(text="No" , value="no")
            })
    @Named("numKidsReq")
    @Inject @Default(values = ArabiaContactUsFormConstant.REQUIRED_NO_VALUE)
    private String noOfKidsValidation;

    @DialogField(name = "./numKidsMessage", fieldLabel = "No of Kids  Validation Message",
            fieldDescription = "Validation message for Number of Kids  field. Default set " +
                    "to 'Please enter Number of Kids. '")
    @TextField @Named("numKidsMessage")
    @Inject @Default(values = ArabiaContactUsFormConstant.KIDS_VALIDATION_MSG)
    private String noofKidsValidationMessage;

    @DialogField(name = "./dateParty", fieldLabel = "Date of Party Text",
            fieldDescription = "Text for Date of Party field. If no text" +
                    " Provided field will not be visible in form.")
    @TextField @Named("dateParty")
    @Inject @Default(values = ArabiaContactUsFormConstant.EMPTY_STRING)
    private String dateOfParty;

    @DialogField(name = "./datePartyReq", fieldLabel = "Date of Party Required",
            fieldDescription = "Please select Date of Party is required" +
            " or not. Default set to 'No'")
    @Selection(type = Selection.SELECT,
            options = {
                    @Option(text="Yes" , value="yes"),
                    @Option(text="No" , value="no")
            })
    @Named("datePartyReq")
    @Inject @Default(values = ArabiaContactUsFormConstant.REQUIRED_NO_VALUE)
    private String dateOfPartyValidation;

    @DialogField(fieldLabel = "Date Text",
            fieldDescription = "Text for Date field. Default set to 'Date'.This " +
                    "value will be considered as a Representative value of DropDown.")
    @TextField
    @Inject @Default(values = ArabiaContactUsFormConstant.DATE)
    private String dateText;

    @DialogField(fieldLabel = "Month Text",
            fieldDescription = "Text for Month field. Default set to 'Month'.This value " +
                    "will be considered as a Representative value of DropDown.")
    @TextField
    @Inject @Default(values = ArabiaContactUsFormConstant.MONTH)
    private String monthText;

    @DialogField(fieldLabel = "Month", fieldDescription = "Please provide Month by clicking + button.First " +
                    "provided value will be first Month of Year")
    @MultiField
    @TextField
    @Inject
    private String[] monthSet;

    @DialogField(fieldLabel = "Year Text",
            fieldDescription = "Text for Year field. Default set to 'Year'." +
                    "This value will be considered as a Representative value of DropDown.")
    @TextField
    @Inject @Default(values = ArabiaContactUsFormConstant.YEAR)
    private String yearText;

    @DialogField(fieldLabel = "Year Range",
            fieldDescription = "Provide Future Year Range. Drop down values would " +
                    "contain years from current year to year provided in the field.")
    @TextField
    @Inject @Default(values = StringUtils.EMPTY)
    private String yearRange;

    @DialogField(name = "./datePartyMessage", fieldLabel = "Date of Party Validation Message",
            fieldDescription = "Validation message for Date Of Party field. Default set to 'Please" +
                    " select an appropriate Date of Party '")
    @TextField @Named("datePartyMessage")
    @Inject @Default(values = ArabiaContactUsFormConstant.PARTY_VALIDATION_MSG)
    private String dateOfPartyValidationMessage;

    private static final Logger log = LoggerFactory.getLogger(CareersFormHandler.class);

    private String childName="";
    private String childNameReq="";
    private String childNameMessage="";

    private String date="";
    private List<String> dateOptions;
    private String month="";
    private Map<String,String> monthOptions;
    private String year="";
    private List<Integer> yearOptions;

    private String dateOfPartyReq="";
    private String dateOfPartyMessage="";


    private String noOfKids="";
    private String noOfKidsReq="";
    private String noOfKidsMessage="";


    private String coverLetter="";
    private String coverLetterReq="";
    private String coverLetterMessage="";

    private String uploadCv="";
    private String uploadCvReq="";
    private String uplaodCvMessage="";

    private String deptName = "";
    private List<String> deptNameOptions;
    private String deptNameReq = "";
    private String deptNameMessage="";

    private StringBuffer formsFieldsBuffr=new StringBuffer();

    private String formFieldList="";

    @PostConstruct
    public void activate() throws Exception {
        childName = this.nameofchild;
        childNameReq = this.namOfChildValidation;
        childNameMessage = this.childNameValidationMessage;
        populateFieldList(childName,ArabiaContactUsFormConstant.CELEBRATION_CHILDNAME);

        date=this.dateText;
        dateOptions=getDropdownDateList();
        month=this.monthText;
        monthOptions=getDropdownValueSet(monthSet);
        year=this.yearText;
        yearOptions =getDropdownYearList(yearRange);
        dateOfPartyReq = this.dateOfPartyValidation;
        dateOfPartyMessage = this.dateOfPartyValidationMessage;
        populateFieldList(dateOfParty,ArabiaContactUsFormConstant.CELEBRATION_PARTYDATE);

        noOfKids = this.noofkids;
        noOfKidsReq = this.noOfKidsValidation;
        noOfKidsMessage = this.noofKidsValidationMessage;
        populateFieldList(noOfKids,ArabiaContactUsFormConstant.CELEBRATION_NOOFKIDS);

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
            int nextYear = 0;
            try {
                nextYear = Integer.parseInt(propValue);
            } catch(Exception exception){
                nextYear = (year+5);
            }
            for (int i = year; i <= nextYear; i++) {
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

    public String getChildName() {
        return childName;
    }

    public String getChildNameReq() {
        return childNameReq;
    }

    public String getChildNameMessage() {
        return childNameMessage;
    }

    public String getDateOfParty() {
        return dateOfParty;
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
    public String getMonth() {
        return month;
    }
    public String getYear() {
        return year;
    }


    public String getDateOfPartyReq() {
        return dateOfPartyReq;
    }

    public String getDateOfPartyMessage() {
        return dateOfPartyMessage;
    }

    public String getNoOfKids() {
        return noOfKids;
    }

    public String getNoOfKidsReq() {
        return noOfKidsReq;
    }

    public String getNoOfKidsMessage() {
        return noOfKidsMessage;
    }




    public String getDate() {
        return date;
    }
}
