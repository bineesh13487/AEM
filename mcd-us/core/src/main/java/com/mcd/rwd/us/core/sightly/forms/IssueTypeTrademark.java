package com.mcd.rwd.us.core.sightly.forms;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Listener;
import com.citytechinc.cq.component.annotations.Tab;
import com.citytechinc.cq.component.annotations.widgets.MultiField;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.mcd.rwd.global.core.widget.colorpicker.ColorPicker;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import javax.inject.Named;

@Component(
        name = "issuetypeTrademark",
        value = "Trademark Reason Request",
        path = "content/form",
        group = " GWS-Global",
        actions = {"text: Reason for you request","-","edit","delete","copymove","insert"},
        disableTargeting = true,
        tabs = @Tab(title = "What is this about?"),
        listeners = {
                @Listener(name = "afteredit", value = "REFRESH_PAGE")
        }
)
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class IssueTypeTrademark {


    @DialogField(name = "./sectionTopHeadingTrademark", fieldLabel = "Section Top Heading",
            fieldDescription = "Heading to be displayed on top of section.")
    @TextField
    @Inject @Named("sectionTopHeadingTrademark") @Default(values = StringUtils.EMPTY)
    private String sectionTopHeading;

    @DialogField(name = "./sectionSubHeadingTrademark", fieldLabel = "Section Sub Heading",
            fieldDescription = "Sub heading to be displayed on top of section.")
    @TextField
    @Inject @Named("sectionSubHeadingTrademark") @Default(values = StringUtils.EMPTY)
    private String sectionSubHeading;

    @DialogField(name = "./whatIsThisAboutTrademark", fieldLabel = "Dropdown Options *",
            fieldDescription = "Please provide options for what is this about dropdown.")
    @MultiField
    @TextField
    @Inject @Named("whatIsThisAboutTrademark")
    private String[] aboutOptions;

    @DialogField(name = "./wtaAriaLabel", fieldLabel = "Aria Label for What is this about")
    @TextField
    @Inject @Named("wtaAriaLabel") @Default(values = StringUtils.EMPTY)
    private String wtaAriaLabel;

    @DialogField(name = "./sectionBGColorTrademark", fieldLabel = "Background Color",
            fieldDescription = "This will change the background color of section on selection of this category.")
    @ColorPicker
    @Inject @Named("sectionBGColorTrademark") @Default(values = StringUtils.EMPTY)
    private String sectionBackgroundColor;


    public String getSectionTopHeading() {
        return sectionTopHeading.trim();
    }

    public String getSectionSubHeading() {
        return sectionSubHeading.trim();
    }

    public String[] getAboutOptions() {
        return aboutOptions;
    }
    
    public String getWtaAriaLabel() {
        return wtaAriaLabel;
    }

    public String getSectionBackgroundColor() {
        return sectionBackgroundColor;
    }
}
