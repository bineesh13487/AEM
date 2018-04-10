package com.mcd.rwd.us.core.sightly;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.editconfig.DropTarget;
import com.citytechinc.cq.component.annotations.widgets.*;
import com.day.cq.wcm.api.designer.Design;
import com.icfolson.aem.library.api.page.PageDecorator;
import com.mcd.rwd.global.core.bean.Link;
import com.mcd.rwd.global.core.components.common.Image;
import com.mcd.rwd.global.core.components.common.Text;
import com.mcd.rwd.global.core.utils.BumperUtil;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.us.core.constants.ApplicationConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by deepti_b on 5/26/2016.
 */
@Component(name = "herocomponent",value = "Hero Component",
        dropTargets = {
                @DropTarget(propertyName = "./image/imagePath", accept = {"image/.*"}, groups = {"media"},
                        nodeName = "image")
        },
        isContainer = true,
        disableTargeting = true, group = "GWS-Global" , path="content" ,tabs = {
        @Tab(touchUINodeName = "advanced", title = "Advanced"),
        @Tab(touchUINodeName = "image", title = "Desktop Image"),
        @Tab(touchUINodeName = "mobileImage", title = "Mobile Image"),
        @Tab(touchUINodeName = "cornerlogo", title = "Corner Logo Image")
})
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeroComponentHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeroComponentHandler.class);

    @Inject
    @Via("request")
    private Resource resource;

    @Inject
    private PageDecorator currentPage;

    @Inject
    Design currentDesign;

    private static final String DEFAULT_DESIGN = "/etc/designs/default";

    @DialogField(fieldLabel = "Section Title", fieldDescription = "Enter the Section Title to be displayed." )
    @TextField
    @Inject
    private String sectionTitle;

    @DialogField(fieldLabel = "Title", required = true , fieldDescription = "Enter the title for Hero Component." )
    @TextField
    @Inject
    private String title;

    @DialogField(fieldLabel = "Image Alt Text", fieldDescription = "Enter the Alt Text for Hero Image")
    @TextField
    @Inject
    private String imageAltText;

    @DialogField(name = "./coopName", fieldDescription = "Check this to display Coop Name if Coop Information is visible else title will be displayed.", fieldLabel = "Show Coop Name",
            additionalProperties = {@Property(name = "inputValue", value = "true"),
                    @Property(name = "value", value = "true")})
    @CheckBox(text = "Show Coop Name")
    @Named("coopName") @Default(values = "false")
    @Inject
    private boolean showcoopname;

    @DialogField(name = "./align", fieldLabel = "Hero Text Alignment", fieldDescription = "Please select the alignment for Text")
    @Selection(options = {
            @Option(text = "Left", value = "heroTextLeft"),
            @Option(text = "Center", value = "heroTextCenter"),
            @Option(text = "Right", value = "heroTextRight")

    }, type = Selection.SELECT)
    @Named("align")
    @Inject @Default(values = "heroTextCenter")
    private String style;

    @DialogField(fieldLabel = "CTA Text", fieldDescription = "Enter the text for CTA")
    @TextField
    @Inject
    private String ctaText;

    @DialogField(name = "./ctaLink", fieldLabel = "CTA Link", fieldDescription = "Please choose or enter any URL for the CTA Link. Path must " +
            "start with '/content' or 'http://w' or 'https://w'")
    @PathField
    @Inject @Named("ctaLink")
    private String ctalink;

    @DialogField(name = "./linkTarget", fieldLabel = "Open link(New Tab)",
            fieldDescription = "Check to open CTA link on new tab. Default it will open in same tab",
            additionalProperties = {@Property(name = "inputValue", value = "true"),
                    @Property(name = "value", value = "true")})
    @CheckBox(text = "Open link(New Tab)")
    @Inject @Named("linkTarget")
    private boolean linktarget;

    @DialogField(name = "./ctaAriaLabel", fieldLabel = "CTA Aria Label", fieldDescription = "Enter the text for CTA aria Label")
    @TextField @Named("ctaAriaLabel")
    @Inject
    private String ctaarialabel;

    @DialogField(fieldLabel = "Description", fieldDescription = "Use the in place text editor to edit this field.")
    @DialogFieldSet(title = "Description" , namePrefix = "text/")
    @ChildResource(name = "text")
    @Inject
     Text description;

    @DialogField(name = "./showAppBadge", fieldDescription = "Check this to show App Badge",
            fieldLabel = "Show App Badge",additionalProperties = {@Property(name = "inputValue", value = "true"),
            @Property(name = "value", value = "true")})
    @CheckBox(text = "Check this to show App Badge")
    @Named("showAppBadge")
    @Inject @Default(values = "false")
    private boolean showappbadge;

    @DialogField(fieldLabel = "Legal Disclaimer Text", fieldDescription = "Please provide the legal disclaimer text.",
    additionalProperties = @Property(name = "id", value = "disclaimer"))
    @TextField
    @Inject
    String disclaimerText;

    @DialogField(fieldLabel = "Corner Logo Alt Text", fieldDescription = "Please provide alternate text for Corner logo.",
    additionalProperties = @Property(name = "id", value = "cornerLogoAlt"))
    @TextField
    @Inject
    String cornerLogoAlt;

    @DialogField(fieldLabel = "Desktop Image", tab = 2)
    @DialogFieldSet(title = "Desktop Image", namePrefix = "image/")
    @ChildResource(name = "image")
    @Inject
    private Image desktopImage;

    @DialogField(fieldLabel = "Mobile Image", tab = 3)
    @DialogFieldSet(title = "Mobile Image" , namePrefix = "mobileImage/")
    @ChildResource(name = "mobileImage")
    @Inject
    private Image mobileImage;

    @DialogField(fieldLabel = "Corner Logo Image", tab = 4)
    @DialogFieldSet(title = "Corner Logo Image" ,namePrefix = "cornerLogo/")
    @ChildResource(name = "cornerLogo")
    @Inject
    private Image cornerLogo;

    private Link bttnLink;
    private String textAlignClass = StringUtils.EMPTY;
    private Boolean isLocalUrl =false;
    private String ctaLink = StringUtils.EMPTY;
    private String pageNameForAnalytics = StringUtils.EMPTY;
    private boolean linkTarget = false;
    private boolean showCoopName = false;
    private String ctaAriaLabel = StringUtils.EMPTY;
    private boolean showAppBadge = false;
    /**
     * Method to perform Post Initialization Tasks.
     */
    @PostConstruct
    public final void activate() {
        LOGGER.debug("In Hero Component Handler");
        ValueMap topNavProperties = getSiteConfig("top-navigation");
        isLocalUrl = topNavProperties!=null && topNavProperties.get("localurl",String.class)!=null && ctalink!=null ? topNavProperties.get("localurl",String.class).equals(ctalink) : false;
        if(StringUtils.isNotBlank(ctalink)){
            ctalink=ctalink+ApplicationConstants.URL_EXT_HTML;
        }
        this.textAlignClass = this.style;
        this.ctaAriaLabel = this.ctaarialabel;
        this.ctaLink = this.ctalink;
        this.linkTarget = this.linktarget;
        this.showCoopName = this.showcoopname;
        this.pageNameForAnalytics = PageUtil.getPageNameForAnalytics(currentPage);
        this.showAppBadge = this.showappbadge;
        BumperUtil bumperUtil = new BumperUtil(resource);
        if (StringUtils.isNotBlank(ctalink)) {
            bttnLink = bumperUtil.getLink(ctalink, ctaText, linkTarget);
        }
    }

    public ValueMap getSiteConfig(String name) {
        if (getSiteConfigResource(name) != null) {
            return getSiteConfigResource(name).adaptTo(ValueMap.class);
        }
        return null;
    }

    /**
     * Returns Site Configuration Resource.
     *
     * @param name
     * @return Resource
     */
    public Resource getSiteConfigResource(String name) {
        if (StringUtils.isNotBlank(name)) {
            if (DEFAULT_DESIGN.equals(currentDesign.getPath())) {
                LOGGER.info("No design configured for the site. Please configure it in the home page.");
            }
            Resource resource = currentDesign.getContentResource();
            if (resource != null && null != resource.getChild(name)) {
                return resource.getChild(name);

            }
        }
        return null;
    }


    public String getImageHref() {
        return desktopImage != null ? desktopImage.getImagePath() : null;
    }

    public String getMobileImageHref() {
        return mobileImage != null ? mobileImage.getImagePath() : null;
    }

    public String getCornerLogoImageHref() {
        return cornerLogo != null ? cornerLogo.getImagePath() : null;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        if(null != description){
            return description.getText();
        }
        else{
            return "Description";
        }

    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public String getCtaText() {
        return ctaText;
    }

    public String getCtaLink() {
        return ctaLink;
    }

    public String getCtaAriaLabel() {
		return ctaAriaLabel;
	}


	public void setCtaAriaLabel(String ctaAriaLabel) {
		this.ctaAriaLabel = ctaAriaLabel;
	}

	public Boolean getIsLocalUrl() {
        return isLocalUrl;
    }

    public Boolean getShowCoopName() {
        return showCoopName;
    }

    public String getImageAltText() {
        return imageAltText;
    }

    public String getTextAlignClass() {
        return textAlignClass;
    }

    public Boolean getShowAppBadge() {
        return showAppBadge;
    }

	public String getDisclaimerText() {
		return disclaimerText;
	}

	public String getCornerLogoAlt() {
		return cornerLogoAlt;
	}

	public String getPageNameForAnalytics() {
		return pageNameForAnalytics;
	}

	public Link getBttnLink() {
		return bttnLink;
	}


}
