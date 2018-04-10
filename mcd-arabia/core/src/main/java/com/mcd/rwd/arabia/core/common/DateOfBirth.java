package com.mcd.rwd.arabia.core.common;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Option;
import com.citytechinc.cq.component.annotations.widgets.MultiField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.icfolson.aem.library.core.constants.ComponentConstants;
import com.mcd.rwd.arabia.core.constants.ArabiaContactUsFormConstant;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Krupa on 06/11/17.
 */
@Component(
        value = "Date of Birth", name = "cdob",
        path = "/content/arabiacontactus",
        disableTargeting = true,
        editConfig = false,
        group = ComponentConstants.GROUP_HIDDEN)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class DateOfBirth {

    @DialogField(name = "./dob", fieldLabel = "Date of Birth Text",
            fieldDescription = "Text for Date of Birth field. Default set to <b>Date of Birth</b>.")
    @TextField
    @Named("dob")
    @Inject
    @Default(values = ArabiaContactUsFormConstant.DOB)
    private String dobText;

    @DialogField(name = "./dobReq", fieldLabel = "Date Of Birth Required",
            fieldDescription = "Please select Date of Birth is required or not. Default set to <b>No</b>")
    @Selection(type = Selection.SELECT,
            options = {
                    @Option(text="No" , value="no"),
                    @Option(text="Yes" , value="yes")
            })
    @Named("dobReq")
    @Inject @Default(values = ArabiaContactUsFormConstant.REQUIRED_NO_VALUE)
    private String dobValidation;

    @DialogField(name = "./dateText", fieldLabel = "Date Text",
            fieldDescription = "Text for Date  field. Default set to <b>Date</b>.This value will be " +
                    "considered as a Representative value of DropDown.")
    @TextField
    @Named("dateText")
    @Inject
    @Default(values = ArabiaContactUsFormConstant.DATE)
    private String dateText;

    @DialogField(name = "./monthText", fieldLabel = "Month Text",
            fieldDescription = "Text for Month field. Default set to <b>Month</b>.This value will be considered" +
                    " as a Representative value of DropDown.")
    @TextField
    @Named("monthText")
    @Inject
    @Default(values = ArabiaContactUsFormConstant.MONTH)
    private String monthText;

    @DialogField(name = "./month", fieldLabel = "Month", fieldDescription = "Please provide Month by " +
            "clicking + button.First provided value will be first Month of Year")
    @MultiField
    @TextField
    @Inject @Named("month")
    private String[] month;

    @DialogField(name = "./yearText" , fieldLabel = "Year Text",
            fieldDescription = "Text for Year  field. Default set to <b>Year</b>." +
                    "This value will be considered as a Representative value of DropDown.")
    @TextField @Named("yearText")
    @Inject @Default(values = ArabiaContactUsFormConstant.YEAR)
    private String yearText;

    @DialogField(name = "./yearRange", fieldLabel = "Year Range",
            fieldDescription = "Provide Year Range. Drop down values would contain years from " +
                    "provided year in the field to current year.")
    @TextField
    @Named("yearRange")
    @Inject
    private String yearRange;

    @DialogField(name = "./dobMessage", fieldLabel = "Date of Birth Validation Message",
            fieldDescription = "Validation message for Date Of Birth field. Default set to " +
                    "<b>Please select an appropriate Date of Birth </b>")
    @TextField
    @Named("dobMessage")
    @Inject
    @Default(values = ArabiaContactUsFormConstant.DOB_VALIDATION_MSG)
    private String dateOfBirthValidationMessage;

    public String getDobText() {
        return dobText;
    }

    public String getDobValidation() {
        return dobValidation;
    }

    public String getDateText() {
        return dateText;
    }

    public String getMonthText() {
        return monthText;
    }

    public String[] getMonth() {
        return month;
    }

    public String getYearText() {
        return yearText;
    }

    public String getDateOfBirthValidationMessage() {
        return dateOfBirthValidationMessage;
    }

    public String getYearRange() {
        return yearRange;
    }
}
