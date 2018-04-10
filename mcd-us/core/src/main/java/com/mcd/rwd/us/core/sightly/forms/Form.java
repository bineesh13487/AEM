package com.mcd.rwd.us.core.sightly.forms;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.widgets.CheckBox;
import com.citytechinc.cq.component.annotations.widgets.DialogFieldSet;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.icfolson.aem.library.api.page.PageDecorator;
import com.icfolson.aem.multicompositeaddon.widget.MultiCompositeField;
import com.mcd.rwd.global.core.components.common.Image;
import com.mcd.rwd.global.core.components.common.Text;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.us.core.bean.ContactUsBean;
import com.mcd.rwd.us.core.bean.LinkPOJO;
import com.mcd.rwd.us.core.constants.FormConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
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
import java.util.Iterator;
import java.util.List;

@Component(
        name = "form",
        value = "Contact Us",
        actions = { "text: ContactUs", "-", "editannotate", "copymove", "delete" },
        disableTargeting = true,
        path = "content/",
        tabs = {
                @Tab(title = "Contact Us Form Options" , touchUINodeName = "contactus"), @Tab(title = "Section Content" , touchUINodeName = "sectioncontent"),@Tab(title = "Hero" , touchUINodeName = "hero") , @Tab(title = "Mobile Img" , touchUINodeName = "mobileImg"), @Tab(title = "Section Desktop Image" , touchUINodeName = "sectiondesktopimage") , @Tab(title = "Section Mobile Image" , touchUINodeName = "sectionmobileimage") , @Tab(title = "Restaurant Search" , touchUINodeName = "restaurantsearch") , @Tab(touchUINodeName = "cornerlogo" ,title = "Corner Logo")
        },
        allowedChildren = {"/apps/mcd-us/components/content/form/issuetype","/apps/mcd-us/components/content/form/issuetype/location","/apps/mcd-us/components/content/form/comments","/apps/mcd-us/components/content/form/personalinfo","/apps/mcd-us/components/content/form/restaurantinfo","/apps/mcd-us/components/content/form/describerequest"},
        group = " GWS-Global",
        listeners = {@Listener(name = "beforesubmit", value = "function(dialog) {\n" +
                "\tif (dialog.getField('./formAction').getValue().trim() == '') {\n" +
                "\t \tCQ.Ext.Msg.show({\n" +
                "\t\t \t\t'title': CQ.I18n.getMessage('ContactUs Dialog Validation'),\n" +
                "\t\t \t\t'msg': CQ.I18n.getMessage('Please provide form action url.'),\n" +
                "\t\t \t\t'buttons': CQ.Ext.Msg.OK,\n" +
                "\t\t \t\t'icon': CQ.Ext.MessageBox.INFO,\n" +
                "\t\t \t\t'scope': this\n" +
                "\t \t\t});\n" +
                "\t\treturn false;\n" +
                "\t}\n" +
                "\tif (dialog.getField('./custServiceEmail').getValue().trim() == '') {\n" +
                "\t\tCQ.Ext.Msg.show({\n" +
                "\t\t\t 'title': CQ.I18n.getMessage('ContactUs Dialog Validation'),\n" +
                "\t\t\t 'msg': CQ.I18n.getMessage('Please provide customer service email.'),\n" +
                "\t\t\t 'buttons': CQ.Ext.Msg.OK,\n" +
                "\t\t\t 'icon': CQ.Ext.MessageBox.INFO,\n" +
                "\t\t\t 'scope': this\n" +
                "\t\t});\n" +
                "\t\treturn false;\n" +
                "\t}\n" +
                "}")})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Form{

    private static final Logger log = LoggerFactory.getLogger(Form.class);
    private static final String CONTENT = "/content";
    private static final String HTML_EXTENTION = ".html";
    private static final String IMAGE_COMPONENT = "foundation/components/image";

    @DialogField(  tab = 1 , fieldLabel="Default Text for form categories", fieldDescription = "Default Text for form " +
            "categories field. Default set to 'Pick a different topic' Value will be " +
            "considered as a Representative Value for field")
    @TextField
    @Inject @Default(values = FormConstants.FORM_CONFIGURATION_TEXT)
    private String defaultTextForformConfiguration;

    @DialogField(fieldLabel="Form Categories" , name = "./formConfiguration", fieldDescription="Please enter form categories for different user feedback with their url.")
    @MultiCompositeField
    @Inject
    @Named("formConfiguration")
    private List<LinkPOJO> contactLinksData;

    @DialogField( fieldDescription="Form action url where user response will be submitted.",fieldLabel="Form Action *", required = true)
    @TextField
    @Inject
    private String formAction;

    @DialogField(  fieldDescription="Form name to save into DB. Leave empty to use page title.",fieldLabel="Form Name")
    @TextField
    @Inject
    private String formName;

    @DialogField( fieldDescription="Aria Label for the Form dropdown.",fieldLabel="Aria Label")
    @TextField
    @Inject
    private String formAriaLabel;

    @DialogField( fieldDescription="Email ids where user response has to send on form submission. 'Multiple Email Ids should be comma separated.'",fieldLabel="Customer Service Email *", required = true)
    @TextField
    @Inject
    private String custServiceEmail;

    @DialogField( name = "./firstLine", fieldDescription="Text to be displayed on Continue button dialog. Default set to 'Are you sure you want to'",fieldLabel="First Line Text")
    @TextField @Named("firstLine")
    @Inject @Default(values = FormConstants.FIRST_LINE_TEXT)
    private String firstLiine;

    @DialogField( fieldDescription="Text to be displayed on Continue button dialog. Default set to 'SUBMIT'",fieldLabel="Second Line Text")
    @TextField
    @Inject @Default(values = FormConstants.SECOND_LINE_TEXT)
    private String secLine;

    @DialogField(  fieldDescription="Text to be displayed on Continue button dialog. Default set to 'this form?'",fieldLabel="Third Line Text")
    @TextField
    @Inject @Default(values = FormConstants.THIRD_LINE_TEXT)
    private String thirdLine;

    @DialogField(  fieldDescription="Information text to be displayed on Continue button dialog. Default set to 'Click 'Cancel' to continue editing, or click 'Submit' to continue.'",fieldLabel="Information Text")
    @TextField
    @Inject @Default(values = FormConstants.INFORMATION_TEXT)
    private String informationText;

    @DialogField(name = "./cancelText", fieldDescription="Text to be displayed on Continue button dialog for cancel button. Default set to 'CANCEL'",fieldLabel="Cancel Button Text")
    @TextField @Named("cancelText")
    @Inject @Default(values = FormConstants.CANCEL_BUTTON_TEXT)
    private String cancel;

    @DialogField(name = "./submitText", fieldDescription="Text to be displayed on Continue button dialog for submit button. Default set to 'SUBMIT'",fieldLabel="Submit Button Text")
    @TextField @Named("submitText")
    @Inject @Default(values = FormConstants.SUBMIT_BUTTON_TEXT)
    private String submit;

    @DialogField(fieldDescription="Please provide path of the page where user should be redirected after submitting the form.",fieldLabel="Thank You Page")
    @PathField
    @Inject
    private String thankYouPage;

    @DialogField(name = "./errorPage", fieldDescription="Please provide path of the page where user should be redirected in case of error in form submission.",fieldLabel="Error Page")
    @PathField
    @Inject @Named("errorPage")
    private String failurePage;

    @DialogField(fieldDescription="This will hide section content completely from the page.",fieldLabel="Hide Section Content",
            additionalProperties = {@Property(name = "inputValue", value = "true")})
    @CheckBox(text = "Hide Section Content")
    @Inject @Default(values = "false")
    private boolean hideSectionContent;

    /*@DialogField(
            name = "./hero" + JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY,
            defaultValue = IMAGE_COMPONENT)
    @Hidden(value = IMAGE_COMPONENT)
    private String hero;*/

   /* @DialogField(
            name = "./mobile" + JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY,
            defaultValue = IMAGE_COMPONENT)
    @Hidden(value = IMAGE_COMPONENT)
    private String mobile;

    @DialogField(
            name = "./sectiondesktopimage" + JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY,
            defaultValue = Form.IMAGE_COMPONENT)
    @Hidden(value = IMAGE_COMPONENT)
    private String sectiondesktopimage;

    @DialogField(
            name = "./sectionmobileimage" + JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY,
            defaultValue = IMAGE_COMPONENT)
    @Hidden(value = IMAGE_COMPONENT)
    private String sectionmobileimage;

    @DialogField(
            name = "./cornerlogo" + JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY,
            defaultValue = IMAGE_COMPONENT)
    @Hidden(value = IMAGE_COMPONENT)
    private String cornerlogo;*/

    @DialogField(name = "./disclaimer", cssClass="disclaimer" , fieldDescription="Please provide the legal disclaimer text.",fieldLabel="Legal Disclaimer Text")
    @TextField
    @Inject @Named("disclaimer")
    private String disclaimer;

    @DialogField(cssClass="cornerLogoAlt" , fieldDescription="Please provide alternate text for Corner logo.",fieldLabel="Corner Logo Alt Text")
    @TextField
    @Inject
    private String cornerLogoAlt;

   /* @DialogField(tab = 2,
            name = "./textIsRich" + JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY)
    @Hidden(value = "true")
    private String textIsRich;*/

    @DialogField(tab = 2 , fieldDescription="Text to be displayed on top of background image of the page.",fieldLabel="Image Text")
    @DialogFieldSet(title = "Image Text" , namePrefix = "imageText/")
    @ChildResource(name = "imageText")
    @Inject
    Text imageTextData;

   /* @DialogField(tab = 2,
            name = "./flag" + JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY,
            defaultValue = "1")
    @Hidden(value = "true")
    private String flag;*/

    @DialogField(tab = 2 , fieldDescription="Text to be displayed above the form.",fieldLabel="Section Content Text")
    @DialogFieldSet(title = "Section Content Text" , namePrefix = "sectionContent/")
    @ChildResource(name = "sectionContent")
    @Inject @Named("sectionContent")
    Text sectiontitledesc;

    @DialogField(fieldLabel = "Desktop Background Image", tab = 3)
    @DialogFieldSet(title = "Desktop Background Image", namePrefix = "hero/")
    @ChildResource(name = "hero")
    @Inject @Named("hero")
    private Image hero;

    @DialogField(fieldLabel = "Mobile Background Image", tab = 4)
    @DialogFieldSet(title = "Mobile Background Image", namePrefix = "mobile/")
    @ChildResource(name = "mobile")
    @Inject @Named("mobile")
    private Image mobile;

    @DialogField(fieldLabel = "Section Content Desktop Image", tab = 5)
    @DialogFieldSet(title = "Section Content Desktop Image", namePrefix = "sectiondesktopimage/")
    @ChildResource(name = "sectiondesktopimage")
    @Inject @Named("sectiondesktopimage")
    private Image sectionContentImg;

    @DialogField(fieldLabel = "Section Content Mobile Image", tab = 6)
    @DialogFieldSet(title = "Section Content Mobile Image", namePrefix = "sectionmobileimage/")
    @ChildResource(name = "sectionmobileimage")
    @Inject @Named("sectionmobileimage")
    private Image sectionmobileimage;

    @DialogField(name = "./resSearchHeading", tab = 7 , fieldDescription="Text to be displayed as heading on restaurant search popup window. Default set to 'Enter your location:'",fieldLabel="Heading")
    @TextField @Named("resSearchHeading")
    @Inject @Default(values = FormConstants.RESTAURANT_SEARCH_HEADING_TEXT)
    private String topHeading;

    @DialogField(name = "./resSearchPlaceholder", tab = 7 , fieldDescription="Text to be displayed as placeholder for restaurant search text box. Default set to 'Zip, City, State'",fieldLabel="Search Placeholder Text")
    @TextField @Named("resSearchPlaceholder")
    @Inject @Default(values = FormConstants.RESTAURANT_SEARCH_PLACEHOLDER_TEXT)
    private String searchPlaceholder;

    @DialogField(name = "./resNoLocation", tab = 7 , fieldDescription="Text to be displayed for not finding restaurant location. Default set to 'Can't find the location you're looking for?'",fieldLabel="Location Not Find Text")
    @TextField @Named("resNoLocation")
    @Inject @Default(values = FormConstants.RESTAURANT_LOCATOR_TEXT)
    private String noLocationText;

    @DialogField(name = "./resSearchButtonText", tab = 7 , fieldDescription="Text to be displayed for restaurant search button. Default set to 'GO'",fieldLabel="Search Button Text")
    @TextField @Named("resSearchButtonText")
    @Inject @Default(values = FormConstants.RESTAURANT_SEARCH_BUTTON_TEXT)
    private String searchButtonText;

    @DialogField(  tab = 7 , fieldDescription="Text to be displayed for restaurant result select button. Default set to 'Select Location'",fieldLabel="Restaurant Result Select Button")
    @TextField
    @Inject @Default(values = FormConstants.RESTAURANT_SEARCH_RESULT_BUTTON_TEXT)
    private String resSelectButtonText;

    @DialogField(fieldLabel = "Corner Logo Image", tab = 8)
    @DialogFieldSet(title = "Corner Logo Image", namePrefix = "cornerlogo/")
    @ChildResource(name = "cornerlogo")
    @Inject @Named("cornerlogo")
    private Image cornerlogo;

    @Inject
    ValueMap valueMap;

    @Inject
    PageDecorator currentPage;

    @Inject
    SlingHttpServletRequest request;

    private List<ContactUsBean> contactLinks;

    private String pageNameForAnalytics = StringUtils.EMPTY;

    private String errorPage = StringUtils.EMPTY;
    private String firstLine = StringUtils.EMPTY;
    private String cancelText = StringUtils.EMPTY;
    private String disclaimerText = StringUtils.EMPTY;
    private String submitText = StringUtils.EMPTY;
    private String sectionContentText = StringUtils.EMPTY;
    private String resSearchHeading = StringUtils.EMPTY;
    private String resSearchPlaceholder = StringUtils.EMPTY;
    private String resNoLocation = StringUtils.EMPTY;
    private String resSearchButtonText = StringUtils.EMPTY;
    private String cornerlogoImagePath = StringUtils.EMPTY;
    private String heroImagePath = StringUtils.EMPTY;
    private String sectionContentDesktopImagePath = StringUtils.EMPTY;
    private String mobileImagePath = StringUtils.EMPTY;
    private String sectionContentMobileImagePath = StringUtils.EMPTY;
    private String imageText = StringUtils.EMPTY;

    @PostConstruct
    public void activate() throws Exception {
        this.firstLine = this.firstLiine;
        this.cancelText = this.cancel;
        this.disclaimerText = this.disclaimer;
        this.submitText = this.submit;
        this.imageText = null!=imageTextData ? imageTextData.getText() : StringUtils.EMPTY;
        this.sectionContentText = null!=sectiontitledesc ? sectiontitledesc.getText() : StringUtils.EMPTY;
        this.resSearchHeading = this.topHeading;
        this.resSearchPlaceholder = this.searchPlaceholder;
        this.resNoLocation = this.noLocationText;
        this.resSearchButtonText = this.searchButtonText;
        log.debug("inside activate method...");
        List<LinkPOJO> contactLinksDataList = this.contactLinksData;
        if (null != contactLinksDataList)
            addContactLinks(contactLinksDataList);
        log.debug("set value from dialog properties..");
        if(null != formName){
            formName =  currentPage.getTitle();
        }

        if(null!=hero)
        heroImagePath = hero.getImagePath();

        if(null!=mobile)
        mobileImagePath = mobile.getImagePath();

        if(null!=cornerlogo)
        cornerlogoImagePath = cornerlogo.getImagePath();

        if(null!=sectionContentImg)
        sectionContentDesktopImagePath = sectionContentImg.getImagePath();

        if(null!=sectionmobileimage)
        sectionContentMobileImagePath = sectionmobileimage.getImagePath();

        if(null != thankYouPage){
            thankYouPage = thankYouPage.trim().startsWith(CONTENT) ? thankYouPage + HTML_EXTENTION : thankYouPage;
            log.debug("thankYouPage path: " + thankYouPage);
        }
        if(null != failurePage){
            errorPage = failurePage.trim().startsWith(CONTENT) ? failurePage + HTML_EXTENTION : failurePage;
            log.debug("errorPage path: " + failurePage);
        }

        if(null!=currentPage)
        pageNameForAnalytics = PageUtil.getPageNameForAnalytics(currentPage);
    }

    private void addContactLinks(List<LinkPOJO> contactLinksData) {
        contactLinks = new ArrayList<ContactUsBean>();
        for (int i = 0; i < contactLinksData.size(); i++) {
            ContactUsBean contactBean = new ContactUsBean();
            if(null != contactBean) {
                LinkPOJO contactUsBean = contactLinksData.get(i);//split the item data
                if (null != contactUsBean) {
                    contactBean.setFormName(contactUsBean.getLinkText());
                    String suffix = request.getRequestPathInfo().getSuffix();
                    if (!StringUtils.EMPTY.equals(contactUsBean.getLinkUrl())) {
                        String contactURL = contactUsBean.getLinkUrl();
                        contactURL = contactURL.startsWith(CONTENT) ? contactURL + HTML_EXTENTION : contactURL;
                        contactURL = (null != suffix) ? contactURL + suffix : contactURL;
                        log.debug("contactURL: " + contactURL);
                        contactBean.setFormURL(contactURL);
                    }
                    contactBean.setNewWindow((!StringUtils.EMPTY.equals(contactUsBean.getTargetVal())) ? contactUsBean.getTargetVal() : "false");
                    log.debug("contactBean: " + contactBean);
                    contactLinks.add(contactBean);
                }
            }
        }
    }

    private static List<ContactUsBean> getChildMultiCompositeFieldValues(final Resource resource, final String name ) {
        List<ContactUsBean>results = new ArrayList();
        ContactUsBean contactUsBean = null;
        if(null != resource){
        Resource multiCompositeres = resource.getChild(name);
        if(null != multiCompositeres && multiCompositeres.hasChildren()){
            Iterator<Resource> multiCompositeResourceIterator = multiCompositeres.listChildren();
            while (multiCompositeResourceIterator.hasNext()){
                Resource itemRes = multiCompositeResourceIterator.next();
                contactUsBean = new ContactUsBean();
                if(null != itemRes){
                    ValueMap valueMap = itemRes.getValueMap();
                    String linkPath = valueMap.get("linkText").toString();
                    String linkUrl = valueMap.get("linkUrl").toString();
                    String targetVal = valueMap.get("targetVal").toString();
                    contactUsBean.setFormName(linkPath);
                    contactUsBean.setFormURL(linkUrl);
                    contactUsBean.setFormURL(targetVal);
                }
            }
        }
        }
        results.add(contactUsBean);
        return results;
    }


	public String getDefaultTextForformConfiguration() {
		return defaultTextForformConfiguration;
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

    public Boolean getHideSectionContent() {
        return hideSectionContent;
    }

    public String getDisclaimerText() {
		return disclaimerText;
	}

	public String getCornerLogoAlt() {
		return cornerLogoAlt;
	}

	public String getHeroImagePath() {
        return heroImagePath;
    }

    public String getMobileImagePath() {
        return mobileImagePath;
    }

    public String getCornerlogoImagePath() {
		return cornerlogoImagePath;
	}

	public String getImageText() {
        return imageText;
    }

    public String getSectionContentText() {
        return sectionContentText;
    }

    public String getSectionContentDesktopImagePath() {
        return sectionContentDesktopImagePath;
    }

    public String getSectionContentMobileImagePath() {
        return sectionContentMobileImagePath;
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
}
