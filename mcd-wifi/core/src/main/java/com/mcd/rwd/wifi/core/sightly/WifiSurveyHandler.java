package com.mcd.rwd.wifi.core.sightly;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Krupa on 19/07/17.
 */

@Component(name = "wifi-survey", value = "Wifi Survey Component A/B",
        description = "This is a simple survey component contains two options to select one.",
        tabs = {
                @Tab(touchUINodeName = "tab1" , title = "Basic Survey Configuration"),
                @Tab(touchUINodeName = "tab2", title = "Accessibility Text")
        },
        resourceSuperType = "foundation/components/parbase",
        actions = { "text: Survey Component", "-", "editannotate", "copymove", "delete" },
        allowedParents = "[*/parsys]",
        group = "McD-Wifi", path = "content",
        listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE")})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class WifiSurveyHandler {

    @DialogField(
            fieldLabel = "Survey Question", required = true,
            fieldDescription = "Please enter your question for survey."
    )
    @TextField
    @Inject @Default(values = StringUtils.EMPTY)
    private String question;

    @DialogField(
            fieldLabel = "Option 1", required = true,
            name = "./option1",
            fieldDescription = "Please enter 1st option for survey question."
    )
    @TextField
    @Inject @Named("option1")
    @Default(values = StringUtils.EMPTY)
    private String option_1;

    @DialogField(
            fieldLabel = "Option One Image",
            name = "./optionOneImage", required = true,
            fieldDescription = "Please select image for option 1.M"
    )
    @PathField(showTitleInTree = true, rootPath = "/content/dam", rootTitle = "Image")
    @Inject @Named("optionOneImage")
    @Default(values = StringUtils.EMPTY)
    private String option_1_image;

    @DialogField(
            fieldLabel = "Option One Alt Text", required = true,
            name = "./optionOneAlt",
            fieldDescription = "Please enter alt text for option 1 image. (Not Selected)"
    )
    @TextField
    @Inject @Named("optionOneAlt")
    @Default(values = StringUtils.EMPTY)
    private String option_1_imageAlt;

    @DialogField(
            fieldLabel = "Option One Image Selected", required = true,
            name = "./optionOneImageSelected",
            fieldDescription = "Please select image after select for Option 1."
    )
    @PathField(showTitleInTree = true, rootPath = "/content/dam", rootTitle = "Image")
    @Inject @Named("optionOneImageSelected")
    @Default(values = StringUtils.EMPTY)
    private String option_1_selected_image;

    @DialogField(
            fieldLabel = "Option One Selected Alt Text", required = true,
            name = "./optionOneAltSelected",
            fieldDescription = "Please enter alt text for option 1 selected image. (Selected)"
    )
    @TextField
    @Inject @Named("optionOneAltSelected")
    @Default(values = StringUtils.EMPTY)
    private String option_1_selectedImageAlt;

    @DialogField(
            fieldLabel = "Option 2", required = true,
            name = "./option2",
            fieldDescription = "Please enter 2nd option for survey question."
    )
    @TextField
    @Inject @Named("option2")
    @Default(values = StringUtils.EMPTY)
    private String option_2;

    @DialogField(
            fieldLabel = "Option Two Image",
            name = "./optionTwoImage", required = true,
            fieldDescription = "Please select image for option 2."
    )
    @PathField(showTitleInTree = true, rootPath = "/content/dam", rootTitle = "Image")
    @Inject @Named("optionTwoImage")
    @Default(values = StringUtils.EMPTY)
    private String option_2_image;

    @DialogField(
            fieldLabel = "Option Two Alt Text", required = true,
            name = "./optionTwoAlt",
            fieldDescription = "Please enter alt text for option 2 image. (Not Selected)"
    )
    @TextField
    @Inject @Named("optionTwoAlt")
    @Default(values = StringUtils.EMPTY)
    private String option_2_imageAlt;

    @DialogField(
            fieldLabel = "Option Two Image Selected", required = true,
            name = "./optionTwoImageSelected",
            fieldDescription = "Please select image after select for Option 2."
    )
    @PathField(showTitleInTree = true, rootPath = "/content/dam", rootTitle = "Image")
    @Inject @Named("optionTwoImageSelected")
    @Default(values = StringUtils.EMPTY)
    private String option_2_selected_image;

    @DialogField(
            fieldLabel = "Option Two Selected Alt Text", required = true,
            name = "./optionTwoAltSelected",
            fieldDescription = "Please enter alt text for option 2 selected image. (Selected)"
    )
    @TextField
    @Inject @Named("optionTwoAltSelected")
    @Default(values = StringUtils.EMPTY)
    private String option_2_selectedImageAlt;

    @DialogField(
            fieldLabel = "Button Text", required = true,
            fieldDescription = "Please enter text for the button."
    )
    @TextField
    @Inject @Default(values = StringUtils.EMPTY)
    private String buttonText;

    @DialogField(
            fieldLabel = "Thank You Text", required = true,
            fieldDescription = "Please enter the text to display after submit the answer. "
    )
    @TextField
    @Inject @Default(values = StringUtils.EMPTY)
    private String thankyouText;

    @DialogField(
            fieldLabel = "Database URL", required = true,
            name = "./databaseUrl",
            fieldDescription = "Please enter your survey database URL."
    )
    @TextField
    @Inject @Named("databaseUrl")
    @Default(values = StringUtils.EMPTY)
    private String database_url;

    @DialogField(
            fieldLabel = "Error Message", required = true,
            name = "./errorMsg",
            fieldDescription = "Please enter error message text."
    )
    @TextField
    @Inject @Named("errorMsg")
    @Default(values = StringUtils.EMPTY)
    private String errormessage;

    @DialogField(
            fieldLabel = "Question Description", tab = 2, required = true,
            fieldDescription = "Please enter description to the question for accessibility. The text you enter " +
                    "will not be shown on the page but it can be readable through screen readers. "
    )
    @TextField
    @Inject @Default(values = StringUtils.EMPTY)
    private String questionDescription;

    private String option1 = StringUtils.EMPTY;
    private String optionOneImage = StringUtils.EMPTY;
    private String optionOneAlt = StringUtils.EMPTY;
    private String optionOneImageSelected = StringUtils.EMPTY;
    private String optionOneAltSelected = StringUtils.EMPTY;
    private String databaseUrl = StringUtils.EMPTY;
    private String errorMsg = StringUtils.EMPTY;
    private String option2 = StringUtils.EMPTY;
    private String optionTwoImage = StringUtils.EMPTY;
    private String optionTwoAlt = StringUtils.EMPTY;
    private String optionTwoImageSelected = StringUtils.EMPTY;
    private String optionTwoAltSelected = StringUtils.EMPTY;

    public String getQuestion() {
        return question;
    }

    public String getOption1() {
        return option1;
    }

    public String getOptionOneAlt() {
        return optionOneAlt;
    }

    public String getOptionOneImageSelected() {
        return optionOneImageSelected;
    }

    public String getOptionOneAltSelected() {
        return optionOneAltSelected;
    }

    public String getThankyouText() {
        return thankyouText;
    }

    public String getButtonText() {
        return buttonText;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public String getOptionOneImage() {
        return optionOneImage;
    }

    public String getOption2() {
        return option2;
    }

    public String getOptionTwoImage() {
        return optionTwoImage;
    }

    public String getOptionTwoAlt() {
        return optionTwoAlt;
    }

    public String getOptionTwoImageSelected() {
        return optionTwoImageSelected;
    }

    public String getOptionTwoAltSelected() {
        return optionTwoAltSelected;
    }

    public String getQuestionDescription() {
        return questionDescription;
    }

    @PostConstruct
    public void init(){
        option1 = this.option_1;
        optionOneImage = this.option_1_image;
        optionOneAlt = this.option_1_imageAlt;
        optionOneImageSelected = this.option_1_selected_image;
        optionOneAltSelected = this.option_1_selectedImageAlt;
        option2 = this.option_2;
        optionTwoImage = this.option_2_image;
        optionTwoAlt = this.option_2_imageAlt;
        optionTwoImageSelected = this.option_2_selected_image;
        optionTwoAltSelected = this.option_2_selectedImageAlt;
        databaseUrl = this.database_url;
        errorMsg = this.errormessage;
    }

}
