package com.mcd.rwd.arabia.core.sightly;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.widgets.*;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Design;
import com.icfolson.aem.multicompositeaddon.widget.MultiCompositeField;
import com.mcd.rwd.global.core.components.common.Image;
import com.mcd.rwd.global.core.components.common.Text;
import com.mcd.rwd.global.core.utils.ImageUtil;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.us.core.bean.ContactUsBean;
import com.mcd.rwd.us.core.bean.LinkPOJO;
import com.mcd.rwd.us.core.constants.FormConstants;
import com.mcd.rwd.us.core.service.McdFactoryConfig;
import com.mcd.rwd.us.core.service.McdFactoryConfigConsumer;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepti_b on 09-05-2017.
 */
@Component(name = "arabiacontactus",value = "Arabia Contact US",
        disableTargeting = true, group = "MCD ARABIA" , path="/content",
        actions = { "text: ContactUs Arabia", "-", "editannotate", "copymove", "delete", "insert" },
        listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
            @Listener(name = "afterinsert", value = "REFRESH_PAGE")},
        tabs = {
                @Tab( touchUINodeName = "mainform" , title = "Contact Us Main Form Tab" ),
                @Tab( touchUINodeName = "sectioncontent", title = "Section Content Tab"),
                @Tab( touchUINodeName = "popup", title = "Contact UsPop Up Window Tab"),
                @Tab( touchUINodeName = "hero", title = "Desktop Background Image"),
                @Tab( touchUINodeName = "mobileImg", title = "Mobile Background Image"),
                @Tab( touchUINodeName = "restaurantsearch", title = "Restaurant Search Popup"),
                @Tab( touchUINodeName = "contactusEmail", title = "ContactUs Email Image")})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArabiaContactUsHandler{

    @DialogField(tab = 1, fieldLabel = "Form Categories", fieldDescription = "Please enter form categories " +
            "for different user feedback with their url.")
    @MultiCompositeField
    @Inject
    private List<LinkPOJO> formConfiguration;

    @DialogField(tab = 1, fieldLabel = "Form Action *",
            fieldDescription = "Form action url where user response will be submitted.")
    @TextField
    @Inject @Default(values = StringUtils.EMPTY)
    private String formAction;

    @DialogField(tab = 1, fieldLabel = "Form Name",
            fieldDescription = "Form name to save into DB. Leave empty to use page title.")
    @TextField
    @Inject @Default(values = StringUtils.EMPTY)
    private String formName;

    @DialogField(tab = 1, fieldLabel = "Aria Label",
            fieldDescription = "Aria Label for the Form dropdown.")
    @TextField
    @Inject @Default(values = StringUtils.EMPTY)
    private String formAriaLabel;

    @DialogField(tab = 1, name = "./custServiceEmail", fieldLabel = "Customer Service Email *",
            fieldDescription = "Email ids where user response has to send on form submission. " +
                    "Multiple Email Ids should be comma separated.")
    @TextField @Named("custServiceEmail")
    @Inject @Default(values = StringUtils.EMPTY)
    private String serviceEmail;

    @DialogField(fieldLabel = "Thank You Page", required = true,
            fieldDescription = "Please provide path of the page where user should be " +
                    "redirected after submitting the form.")
    @PathField
    @Inject @Default(values = StringUtils.EMPTY)
    private String thankYouPage;

    @DialogField(name = "./errorPage", fieldLabel = "Error Pag", required = true,
            fieldDescription = "Please provide path of the page where user should be " +
                    "redirected in case of error in form submission.")
    @PathField @Named("errorPage")
    @Inject @Default(values = StringUtils.EMPTY)
    private String failurePage;

    @DialogField(fieldLabel = "Article Description Text", fieldDescription = "Text to be displayed as Article Description.")
    @DialogFieldSet(title = "Article Description Text" , namePrefix = "imagetext/")
    @ChildResource(name = "imagetext")
    @Inject @Named("imageText")
    Text imagetext;

    @DialogField(name = "./textIsRich", value = "true")
    @Hidden @Named("textIsRich")
    private String richFlag;

    @DialogField(name = "./falg", defaultValue = "1")
    @Hidden @Named("flag")
    private String flag;

    @DialogField(tab = 2, fieldDescription = "This will hide section content completely from the page.",
            additionalProperties = {@Property(name = "inputValue", value = "true"),
                    @Property(name = "value", value = "true")})
    @CheckBox(text = "Hide Section Content")
    @Inject
    private boolean hideSectionContent;

    @DialogField(tab = 2, name = "./sectionContent", fieldLabel = "Section Content", fieldDescription = "Text to be displayed above the form.")
    @DialogFieldSet(title = "Section Content" , namePrefix = "sectiontitledesc/")
    @ChildResource(name = "sectiontitledesc")
    @Inject @Named("sectionContent")
    Text sectiontitledesc;

    @DialogField(tab = 3, name = "./firstLine", fieldLabel = "First Line Text",
            fieldDescription = "Text to be displayed on Continue button dialog." +
                    " Default set to Are you sure you want to")
    @TextField @Named("firstLine")
    @Inject @Default(values = FormConstants.FIRST_LINE_TEXT)
    private String firstLiine;

    @DialogField(tab = 3, fieldLabel = "Second Line Text",
            fieldDescription = "Text to be displayed on Continue button dialog. Default set to SUBMIT")
    @TextField
    @Inject @Default(values = FormConstants.SECOND_LINE_TEXT)
    private String secLine;

    @DialogField(tab = 3, fieldLabel = "Third Line Text",
            fieldDescription = "Text to be displayed on Continue button dialog. Default set to this form?>")
    @TextField
    @Inject @Default(values = FormConstants.THIRD_LINE_TEXT)
    private String thirdLine;

    @DialogField(tab = 3, fieldLabel = "Information Text",
            fieldDescription = "Information text to be displayed on Continue button dialog. Default set to " +
                    "Click 'Cancel' to continue editing, or click 'Submit' to continue.")
    @TextField
    @Inject @Default(values = FormConstants.INFORMATION_TEXT)
    private String informationText;

    @DialogField(tab = 3, name = "./cancelText", fieldLabel = "Cancel Button Text",
            fieldDescription = "Text to be displayed on Continue button dialog for cancel" +
                    " button. Default set to CANCEL")
    @TextField @Named("cancelText")
    @Inject @Default(values = FormConstants.CANCEL_BUTTON_TEXT)
    private String cancel;

    @DialogField(tab = 3, name = "./submitText", fieldLabel = "Submit Button Text",
            fieldDescription = "Text to be displayed on Continue button dialog for " +
                    "submit button. Default set to SUBMIT")
    @TextField @Named("submitText")
    @Inject @Default(values = FormConstants.SUBMIT_BUTTON_TEXT)
    private String submit;

    @DialogField(tab = 4, fieldLabel = "Desktop Background Image")
    @DialogFieldSet(title = "Desktop Background Image" , namePrefix = "hero/")
    @ChildResource(name = "hero")
    @Inject
    Image hero;

    @DialogField(tab = 5, fieldLabel = "Mobile Background Image")
    @DialogFieldSet(title = "Mobile Background Image" , namePrefix = "mobile/")
    @ChildResource(name = "mobile")
    @Inject
    Image mobileImg;

    @DialogField(tab = 6, name = "./resSearchHeading", fieldLabel = "Heading",
            fieldDescription = "Text to be displayed as heading on restaurant search popup window. Default set " +
                    "to Enter your location:")
    @TextField @Named("resSearchHeading")
    @Inject @Default(values = FormConstants.RESTAURANT_SEARCH_HEADING_TEXT)
    private String topHeading;

    @DialogField(tab = 6, name = "./resSearchPlaceholder", fieldLabel = "Search Placeholder Text",
            fieldDescription = "Text to be displayed as placeholder for restaurant search text box." +
                    " Default set to City, Area")
    @TextField @Named("resSearchPlaceholder")
    @Inject @Default(values = FormConstants.RESTAURANT_SEARCH_PLACEHOLDER_TEXT)
    private String searchPlaceholder;

    @DialogField(tab = 6, name = "./resNoLocation", fieldLabel = "Location Not Find Text",
            fieldDescription = "Text to be displayed for not finding restaurant location. Default set to " +
                    "Can't find the location you're looking for?")
    @TextField @Named("resNoLocation")
    @Inject @Default(values = FormConstants.RESTAURANT_LOCATOR_SEARCH_TEXT)
    private String noLocationText;

    @DialogField(tab = 6, name = "./resSearchButtonText", fieldLabel = "Search Button Text",
            fieldDescription = "Text to be displayed for restaurant search button." +
                    " Default set to GO")
    @TextField @Named("resSearchButtonText")
    @Inject @Default(values = FormConstants.RESTAURANT_SEARCH_BUTTON_TEXT)
    private String searchButtonText;

    @DialogField(tab = 6, fieldLabel = "Restaurant Result Select Button",
            fieldDescription = "Text to be displayed for restaurant result select button." +
                    " Default set to Select Location")
    @TextField
    @Inject @Default(values = FormConstants.RESTAURANT_SEARCH_RESULT_BUTTON_TEXT)
    private String resSelectButtonText;

    @DialogField(tab = 7, fieldLabel = "Mobile Background Image")
    @DialogFieldSet(title = "Mobile Background Image" , namePrefix = "heroEmail/")
    @ChildResource(name = "heroEmail")
    @Inject
    Image ContactUsEmailHero;

    @Inject
    Page currentPage;

    @Inject
    Design currentDesign;

    @Inject
    SlingScriptHelper slingScriptHelper;

    @Inject
    SlingHttpServletRequest request;

    private static final Logger log = LoggerFactory.getLogger(ArabiaContactUsHandler.class);
    private static final String CONTENT = "/content";
    private static final String HTML_EXTENTION = ".html";

    private List<ContactUsBean> contactLinks;

    private McdFactoryConfig mcdFactoryConfig;

    private String custServiceEmail = StringUtils.EMPTY;

    private String firstLine = StringUtils.EMPTY;

    private String cancelText = StringUtils.EMPTY;

    private String submitText = StringUtils.EMPTY;

    private String heroImagePath = StringUtils.EMPTY;

    private String mobileImagePath = StringUtils.EMPTY;

    private String errorPage = StringUtils.EMPTY;

    private String resSearchHeading = StringUtils.EMPTY;

    private String resSearchPlaceholder = StringUtils.EMPTY;

    private String resNoLocation = StringUtils.EMPTY;

    private String resSearchButtonText = StringUtils.EMPTY;

    private String pageNameForAnalytics = StringUtils.EMPTY;

    private String imageText = StringUtils.EMPTY;

    private String sectionContentText = StringUtils.EMPTY;

    private String countryName = StringUtils.EMPTY;

    private String lang = StringUtils.EMPTY;

    private String logoEmailImage = StringUtils.EMPTY;

    private String heroEmailImagePath = StringUtils.EMPTY;



    @PostConstruct
    public void activate() {
        log.debug("inside activate method...");
        //Page currentPage = getCurrentPage();
        String country = PageUtil.getCountry(currentPage);
        String language = PageUtil.getLanguage(currentPage);
        if (country != null && language != null) {
            McdFactoryConfigConsumer mcdFactoryConfigConsumer = slingScriptHelper.getService(McdFactoryConfigConsumer.class);
            if (mcdFactoryConfigConsumer != null){
                mcdFactoryConfig = mcdFactoryConfigConsumer.getMcdFactoryConfig(country, language);
                if (mcdFactoryConfig != null) {
                    lang=mcdFactoryConfig.getDnaLanguageCode();
                    countryName=mcdFactoryConfig.getCountry_short_name();
                    // countryName="uae";  //currently hardcoding it but will make it dynamic when xml work done
                }
            }
        }
        List<LinkPOJO> contactLinksData = this.formConfiguration;
        log.debug("contactLinksData: " + contactLinksData);
        if (null != contactLinksData)
            addContactLinks(contactLinksData);
        log.debug("set value from dialog properties..");
        formName = StringUtils.isEmpty(formName)? currentPage.getTitle() : this.formName;
        custServiceEmail = this.serviceEmail;
        firstLine = this.firstLiine;
        cancelText = this.cancel;
        submitText = this.submit;
        heroImagePath = hero!=null? hero.getImagePath() : StringUtils.EMPTY;
        mobileImagePath = mobileImg!=null? mobileImg.getImagePath() : StringUtils.EMPTY;
        thankYouPage = thankYouPage.trim().startsWith(CONTENT) ? thankYouPage + HTML_EXTENTION : thankYouPage;
        imageText = imagetext!=null ? imagetext.getText() : StringUtils.EMPTY;
        log.debug("thankYouPage path: " + thankYouPage);

        errorPage = this.failurePage;
        errorPage = errorPage.trim().startsWith(CONTENT) ? errorPage + HTML_EXTENTION : errorPage;
        log.debug("errorPage path: " + errorPage);

        resSearchHeading = this.topHeading;
        resSearchPlaceholder = this.searchPlaceholder;
        resNoLocation = this.noLocationText;
        resSearchButtonText = this.searchButtonText;
        pageNameForAnalytics = PageUtil.getPageNameForAnalytics(currentPage);
        sectionContentText = sectiontitledesc!=null? sectiontitledesc.getText() : StringUtils.EMPTY;
        mobileImagePath = mobileImg!=null? mobileImg.getImagePath() : StringUtils.EMPTY;
        heroEmailImagePath = ContactUsEmailHero!=null? ContactUsEmailHero.getImagePath() : StringUtils.EMPTY;

        if(null!=currentDesign) {
            Resource contentResource = currentDesign.getContentResource();
            Resource logo = null != contentResource.getChild("logo") ?
                    contentResource.getChild("logo") : null;
            logoEmailImage = ImageUtil.getImagePath(logo, "image");
        }

    }

    private void addContactLinks(List<LinkPOJO> contactLinksData) {
        contactLinks = new ArrayList<ContactUsBean>();
        for (LinkPOJO linkData : contactLinksData) {
            ContactUsBean contactBean = new ContactUsBean();
            //String[] contactLinkItem = contactLinksData[i].split("\\^");//split the item data
            if (null!=linkData) {
                contactBean.setFormName(linkData.getLinkText());
                String suffix = request.getRequestPathInfo().getSuffix();
                String contactURL = linkData.getLinkUrl();
                contactURL = contactURL.startsWith(CONTENT) ? contactURL + HTML_EXTENTION : contactURL;
                contactURL = (null != suffix) ? contactURL + suffix : contactURL;
                log.debug("contactURL: " + contactURL);
                contactBean.setFormURL(contactURL);
                contactBean.setNewWindow(linkData.getTargetVal()=="true" ? "true" : "false");
            }
            log.debug("contactBean: " + contactBean);
            contactLinks.add(contactBean);
        }
    }

    public List<ContactUsBean> getContactLinks() {
        return contactLinks;
    }

    public String getFormAction() {
        return formAction;
    }

    public String getFormName() {
        return formName;
    }

    public String getFormAriaLabel() {
        return formAriaLabel;
    }

    public String getCustServiceEmail() {
        return custServiceEmail;
    }

    public String getFirstLine() {
        return firstLine.trim();
    }

    public String getSecLine() {
        return secLine.trim();
    }

    public String getThirdLine() {
        return thirdLine.trim();
    }

    public String getInformationText() {
        return informationText.trim();
    }

    public String getCancelText() {
        return cancelText.trim();
    }

    public String getSubmitText() {
        return submitText.trim();
    }

    public String getHeroImagePath() {
        return heroImagePath;
    }

    public String getMobileImagePath() {
        return mobileImagePath;
    }

    public String getThankYouPage() {
        return thankYouPage;
    }

    public String getErrorPage() {
        return errorPage;
    }

    public String getResSearchHeading() {
        return resSearchHeading;
    }

    public String getResSearchPlaceholder() {
        return resSearchPlaceholder;
    }

    public String getResNoLocation() {
        return resNoLocation;
    }

    public String getResSearchButtonText() {
        return resSearchButtonText;
    }

    public String getResSelectButtonText() {
        return resSelectButtonText;
    }

    public String getPageNameForAnalytics() {
        return pageNameForAnalytics;
    }
    public String getImageText() {
        return imageText;
    }

    public boolean isHideSectionContent() {
        return hideSectionContent;
    }

    public String getSectionContentText() {
        return sectionContentText;
    }


    public String getCountryName() {
        return countryName;
    }


    public String getLang() {
        return lang;
    }

    public String getHeroEmailImagePath() {
        return heroEmailImagePath;
    }

    public String getLogoEmailImage() {
        return logoEmailImage;
    }
}


