package com.mcd.rwd.us.core.sightly.forms;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Tab;
import com.citytechinc.cq.component.annotations.widgets.MultiField;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.mcd.rwd.us.core.constants.FormConstants;
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

@Component(
        name = "mobile",
        value = "Tell Us About It Mobile",
        path = "content/form/comments/",
        tabs = {
                @Tab(title = "Tell Us About It Mobile")
        })
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CommentsMobile {

    private static final Logger log = LoggerFactory.getLogger(CommentsMobile.class);

    @DialogField(fieldLabel = "Mobile Device Type Text", fieldDescription = "Heading text to be displayed for Mobile Device Type. Default set to 'Mobile Device Type:'")
    @TextField
    @Inject @Named("mobDeviceType") @Default(values = "Mobile Device Type:")
    private String deviceTypeText;


    @DialogField(fieldLabel = "Mobile Device Type Text", fieldDescription = "Please provide options for Mobile Device Type.")
    @MultiField
    @TextField
    @Inject @Named("mdtOptions")
    String[] deviceOptionsData;

    @DialogField(fieldLabel = "Mobile Device Type Validation Message *", fieldDescription = "Validation message for mobile device type field. Default set to 'Please select mobile device type'")
    @TextField()
    @Inject @Named("mdtMessage") @Default(values = "Please select mobile device type")
    private String deviceOptionsMessage;

    @DialogField(fieldLabel = "Aria Label for the mobile device type", fieldDescription = "Aria Label for the mobile device type.")
    @TextField
    @Inject
    private String mdtAriaLabel;

    @DialogField(fieldLabel = "Mobile Operating System Text", fieldDescription = "Heading text to be displayed for Mobile operating System:. Default set to 'Mobile operating System:'")
    @TextField
    @Inject @Named("mobOperatingSystem") @Default(values = "Mobile Operating System:")
    private String operatingSystemText;

    @DialogField(fieldLabel = "Mobile Operating System Dropdown Options *", fieldDescription = "Please provide options for Mobile operating System.")
    @MultiField
    @TextField
    @Inject @Named("mosOptions")
    private String[] operatingSystemOptionsData;

    @DialogField(fieldLabel = "Mobile Operating System Validation Message", fieldDescription = "Validation message for mobile operating system field. Default set to 'Please select mobile operating system'")
    @TextField
    @Inject @Named("mosMessage") @Default(values = "Please select mobile operating system")
    private String operatingSystemMessage;

    @DialogField(fieldLabel = "Aria Label for the mobile operating syste", fieldDescription = "Aria Label for the mobile operating system")
    @TextField
    @Inject
    private String mosAriaLabel;

    @DialogField(fieldLabel = "Mobile App Version Text", fieldDescription = "Heading text to be displayed for Mobile App Version:. Default set to 'Mobile App Version:'")
    @TextField
    @Inject @Named("mobAppVersion") @Default(values = "Mobile App Version:")
    private String appVersionText;

    @DialogField(fieldLabel = "Mobile Operating System Validation Message", fieldDescription = "Validation message for mobile operating system field. Default set to 'Please select mobile operating system'")
    @TextField
    @Inject @Named("mavOptions")
    private String appVersion;

    @DialogField(fieldLabel = "Mobile Operating System Validation Message", fieldDescription = "Validation message for Mobile app version field. Default set to 'Please enter mobile app version'")
    @TextField
    @Inject @Named("mavMessage") @Default(values = "Please enter mobile app version")
    private String appVersionMessage;

    @Inject
    ValueMap valueMap;

    private List<String> deviceOptions;

    private String mavAriaLabel = "";

    private List<String> operatingSystemOptions;

    private String selectText = FormConstants.SELECT_TEXT;

    @PostConstruct
    public void activate() throws Exception {
        log.debug("inside activate method...");
        log.debug("set values from dialog properties");
        String[] deviceOptionsData = valueMap.get("deviceOptionsData", String[].class);
        log.debug("deviceOptionsData: " + deviceOptionsData);
        if (null != deviceOptionsData) {
            deviceOptions = getListOfOptionData(deviceOptionsData);
        }
        String[] operatingSystemOptionsData = valueMap.get("operatingSystemOptionsData", String[].class);
        log.debug("operatingSystemOptionsData: " + operatingSystemOptionsData);
        if (null != operatingSystemOptionsData) {
            operatingSystemOptions = getListOfOptionData(operatingSystemOptionsData);
        }
    }

    private List<String> getListOfOptionData(String[] optionsDataArray) {
        List<String> options = new ArrayList<String>();
        for (int i = 0; i < optionsDataArray.length; i++) {
            options.add(optionsDataArray[i]);
        }
        return options;
    }

    public String getDeviceTypeText() {
        return deviceTypeText.trim();
    }

    public List<String> getDeviceOptions() {
        return deviceOptions;
    }

    public String getOperatingSystemText() {
        return operatingSystemText.trim();
    }

    public List<String> getOperatingSystemOptions() {
        return operatingSystemOptions;
    }

    public String getAppVersionText() {
        return appVersionText.trim();
    }

    public String getMdtAriaLabel() {
        return mdtAriaLabel;
    }

    public String getMosAriaLabel() {
        return mosAriaLabel;
    }

    public String getMavAriaLabel() {
        return mavAriaLabel;
    }

    public String getAppVersion() {

        return appVersion;
    }

    public String getSelectText() {
        return selectText;
    }

    public String getDeviceOptionsMessage() {
        return deviceOptionsMessage;
    }

    public String getOperatingSystemMessage() {
        return operatingSystemMessage;
    }

    public String getAppVersionMessage() {
        return appVersionMessage;
    }
}
