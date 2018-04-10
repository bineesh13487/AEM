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
@Component(name = "careersform",value = "Careers Component",
        description = "careers form component",
        disableTargeting = true, group = "MCD ARABIA" , path="/content/arabiacontactus",
        actions = { "text: careers section", "-", "editannotate", "copymove", "delete", "insert" },
        listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
                @Listener(name = "afterinsert", value = "REFRESH_PAGE")},
        tabs = {
                @Tab( touchUINodeName = "CareersForm" , title = "Careers Form" )})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CareersFormHandler {

    @DialogField(tab = 1, name = "./coverLetter", defaultValue = ArabiaContactUsFormConstant.EMPTY_STRING,
            fieldLabel = "Cover Letter Text",
            fieldDescription = "Text for Cover Letter field. If no text Provided " +
                    "field will not be visible in form.")
    @TextField
    @Inject @Named("coverLetter")
    @Default(values = StringUtils.EMPTY)
    private String coverletter;

    @DialogField(name = "./coverLetterReq", defaultValue = ArabiaContactUsFormConstant.REQUIRED_NO_VALUE,
            fieldLabel = "Cover Letter Required", fieldDescription = "Please select " +
            "Cover Letter is required or not. Default set to 'No'")
    @Selection(type = Selection.SELECT,
            options = {
                    @Option(text="Yes" , value="yes"),
                    @Option(text="No" , value="no")
            })
    @Named("coverLetterReq")
    @Inject
    private String coverletterValidation;

    @DialogField(name = "./coverLetterMessage", fieldLabel = "Cover Letter Validation Message",
            fieldDescription = "Validation message for Cover Letter field. Default " +
                    "set to 'Please enter Cover Letter.'", defaultValue =
            ArabiaContactUsFormConstant.COVERLETTER_VALIDATION_MSG)
    @TextField @Named("coverLetterMessage")
    @Inject
    private String coverLetterValidationMessage;

    @DialogField(fieldLabel = "Upload CV Text", defaultValue = ArabiaContactUsFormConstant.EMPTY_STRING,
            fieldDescription = "Text for Upload CV field. If no text Provided field will not be visible in form.")
    @TextField
    @Inject
    private String uploadcv;

    @DialogField(name = "./uploadCVReq", fieldLabel = "Upload CV Required", fieldDescription = "Please select " +
            "Upload CV is required or not. Default set to 'Yes'" ,
            defaultValue = ArabiaContactUsFormConstant.REQUIRED_NO_VALUE)
    @Selection(type = Selection.SELECT,
            options = {
                    @Option(text="Yes" , value="yes"),
                    @Option(text="No" , value="no")
            })
    @Named("uploadCVReq")
    @Inject
    private String uploadcvValidation;

    @DialogField(name = "./uploadCvMessage", fieldLabel = "Upload CV Validation Message",
            fieldDescription = "Validation message for Upload CV field. Default set to " +
                    "'Please provide CV in one of the following format: .doc, .docx or " +
                    ".pdf and cannot be more than 1Mb'", defaultValue = ArabiaContactUsFormConstant.CV_VALIDATION_MSG)
    @Inject
    @TextField @Named("uploadCvMessage")
    private String uploadcvValidationMessage;

    @DialogField(name = "./uploadText", defaultValue = ArabiaContactUsFormConstant.UPLOAD_BUTTON_TEXT,
            fieldLabel = "Upload Button Text",
            fieldDescription = "Text for Upload Button .Default set to ' Browse '")
    @TextField @Named("uploadText")
    @Inject
    private String uploadButtonText;

    @DialogField(name = "./deptName", fieldLabel = "Choose Department",
            defaultValue = ArabiaContactUsFormConstant.EMPTY_STRING,
            fieldDescription = "Text for choose Department field. If no text Provided field will not be visible in form.")
    @TextField @Named("deptName")
    @Inject
    private String departmentText;

    @DialogField(fieldLabel = "Department",
            fieldDescription = "Please provide Department by clicking + button. First provided value " +
                    "will be considered as a Representative Value")
    @MultiField
    @TextField
    @Inject
    private String[] department;

    @DialogField(name = "./departmentReq", fieldLabel = "Department Required", fieldDescription = "Please select " +
            "Choose Department is required or not. Default set to 'No'" ,
            defaultValue = ArabiaContactUsFormConstant.REQUIRED_NO_VALUE)
    @Selection(type = Selection.SELECT,
            options = {
                    @Option(text="Yes" , value="yes"),
                    @Option(text="No" , value="no")
            })
    @Named("departmentReq")
    @Inject
    private String departmentValidation;

    @DialogField(fieldLabel = "Department Validation Message",
            defaultValue = ArabiaContactUsFormConstant.DEPT_VALIDATION_MSG,
            fieldDescription = "TValidation message for Choose Department field." +
                    " Default set to 'Please select Department'")
    @TextField
    @Inject
    private String deptValidationMessage;

    private static final Logger log = LoggerFactory.getLogger(CareersFormHandler.class);

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

        coverLetter = this.coverletter;
        coverLetterReq = this.coverletterValidation;
        coverLetterMessage = this.coverLetterValidationMessage;
        populateFieldList(coverLetter,ArabiaContactUsFormConstant.APPLICATION_COVERLETTER);

        uploadCv = this.uploadcv;
        uploadCvReq = this.uploadcvValidation;
        uplaodCvMessage = this.uploadcvValidationMessage;
        populateFieldList(uploadCv,ArabiaContactUsFormConstant.APPLICATION_CVNAME);

        deptName = this.departmentText;
        deptNameOptions=getDropdownOptions(department);
        deptNameReq = this.departmentValidation;
        deptNameMessage=this.deptValidationMessage;
        populateFieldList(deptName,ArabiaContactUsFormConstant.APPLICATION_DEPTNAME);
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
    public void populateFieldList(String fieldText,String str){
        if(StringUtils.isNotBlank(fieldText)){
            formsFieldsBuffr.append(str);
            formsFieldsBuffr.append(',');
        }
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public String getCoverLetterReq() {
        return coverLetterReq;
    }

    public String getCoverLetterMessage() {
        return coverLetterMessage;
    }

    public String getUploadCv() {
        return uploadCv;
    }

    public String getUploadCvReq() {
        return uploadCvReq;
    }

    public String getUplaodCvMessage() {
        return uplaodCvMessage;
    }

    public String getDeptName() {
        return deptName;
    }

    public List<String> getDeptNameOptions() {
        return deptNameOptions;
    }

    public String getDeptNameReq() {
        return deptNameReq;
    }

    public String getDeptNameMessage() {
        return deptNameMessage;
    }

    public String getUploadButtonText() {
        return uploadButtonText;
    }

    public String getFormFieldList() {
        return formFieldList;
    }

}
