/**
 * 
 */
package com.mcd.rwd.us.core.sightly.callouts;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.widgets.DialogFieldSet;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.models.annotations.DesignAnnotation;
import com.mcd.rwd.global.core.utils.BumperUtil;
import com.mcd.rwd.global.core.utils.ImageUtil;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.global.core.utils.ResourceUtil;
import com.mcd.rwd.us.core.bean.FeatureCallout;
import com.mcd.rwd.us.core.constants.PromoConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

/**
 * @author mc41861
 *
 */

@Component(
        name = "productcallouts",
        value = "Product Feature Callout",
        description = "Feature Callouts used on the product page.",
        resourceSuperType = "mcd/components/content/featurecallout",
        group = ".hidden",
        tabs = {
                @Tab(title = "General Settings", touchUINodeName = "general"),
                @Tab(title = "Callout 1", touchUINodeName = "static1", listeners = {
                        @Listener(name = "render", value = "function() { " +
                                "this.findParentByType('tabpanel').hideTabStripItem(1); }")
                }),
                @Tab(title = "Callout 2", touchUINodeName = "static2", listeners = {
                        @Listener(name = "render", value = "function() { " +
                                "this.findParentByType('tabpanel').hideTabStripItem(2); }")
                }),
                @Tab(title = "Callout 3", touchUINodeName = "static3", listeners = {
                        @Listener(name = "render", value = "function() { " +
                                "this.findParentByType('tabpanel').hideTabStripItem(3); }")
                }),
                @Tab(title = "Callout 4", touchUINodeName = "static4", listeners = {
                        @Listener(name = "render", value = "function() { " +
                                "this.findParentByType('tabpanel').hideTabStripItem(4); }")
                }),
        }/*,
        listeners = {
                @Listener(name = "hideAllTabs", value = "function(){var tabs=['static1','static2','static3','static4'];" +
                        "for(var i=0;i&lt;tabs.length;i++){this.hideTabStripItem(i+1);}}"),
                @Listener(name = "manageTabs", value = "function(tab,noSwitch){var tabs=['static1','static2','static3','static4'];" +
                        "for(var j=0; j&lt;tab.length; j++) {var index=tabs.indexOf(tab[j]);if(index==-1) return;for(var i=0;i&lt;tabs.length;i++){if(index==i){this.unhideTabStripItem(i+1);}}}this.doLayout();}")
        }*/
)
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductCalloutHandler {//extends McDUse {

	private static final Logger LOG = LoggerFactory.getLogger(ProductCalloutHandler.class);

    private List<FeatureCallout> callouts;
	private boolean isEmpty;
	private static final String DEFAULT_PRODUCT_CALLOUT_RESOURCE = "cat-default/item-default";
	private String pageTitle;

	@DialogField(fieldLabel = "No. of Callouts",fieldDescription = "Number of Callouts to be displayed " +
            "on the page.", additionalProperties = {@Property(name = "class", value = "product-callout-number")},
            listeners = {
	            @Listener(name = "loadcontent", value = "function(){var tabPanel = this.findParentByType('tabpanel');var number = this.getValue();tabPanel.hideAllTabs();var tabType = 'static';var tabs = [];for(var i=1; i&lt;=number; i++) {tabs.push(tabType+i);}tabPanel.manageTabs(tabs,true);}"),
                @Listener(name = "selectionchanged", value = "function(box, value) {var tabPanel = box.findParentByType('tabpanel');tabPanel.hideAllTabs();var tabType = 'static';var tabs = [];for(var i=1; i&lt;=value; i++) {tabs.push(tabType+i);}tabPanel.manageTabs(tabs,true);}")
    })
    @Selection(type = Selection.SELECT, options = {
            @Option(text = "3", value = "3"), @Option(text = "4", value = "4")
    })
    @Inject @Default(intValues = 1)
    private int number;

    @DialogField(tab = 2)
    @DialogFieldSet(namePrefix = "callout1/")
    @ChildResource(name = "callout1")
    CalloutStaticRes calloutRes1;

    @DialogField(tab = 3)
    @DialogFieldSet(namePrefix = "callout2/")
    @ChildResource(name = "callout2")
    CalloutStaticRes calloutRes2;

    @DialogField(tab = 4)
    @DialogFieldSet(namePrefix = "callout3/")
    @ChildResource(name = "callout3")
    CalloutStaticRes calloutRes3;

    @DialogField(tab = 5)
    @DialogFieldSet(namePrefix = "callout4/")
    @ChildResource(name = "callout4")
    CalloutStaticRes calloutRes4;

    @Self
    SlingHttpServletRequest request;

    @Inject
    PageManager pageManager;

    @Inject
    Page currentPage;

    @Inject
    ResourceResolver resourceResolver;

    @Inject
    @Via("request")
    Resource currentResource;

    @DesignAnnotation(value = ApplicationConstants.RES_BUMPER)
    Resource bumperRes;
    
	/* (non-Javadoc)
     * @see com.adobe.cq.sightly.WCMUse#activate()
     */
    @PostConstruct
    public void activate() {
        LOG.debug("activate() method called.");

        Page scaffPage;
        this.pageTitle = PageUtil.getPageNameForAnalytics(currentPage, request);
        if(checkEmpty(number, currentResource)) {
        	currentResource = getDefaultCalloutResource(currentPage);
        	if(null != currentResource) {
    			//Check whether the default callout is configured or not.
    	        if(checkEmpty(number, currentResource)) {
    	        	this.isEmpty = true;
    	        	LOG.debug("Returning as default config not present");
    	        	return;
    	        }
    		} else {
    			this.isEmpty = true;
    			LOG.debug("Default node not present");
    			return;
    		}
        }
        
        this.callouts = new LinkedList<FeatureCallout>();

        for (int calloutIndex = 1; calloutIndex <= number; calloutIndex++) {
            ValueMap calloutProperties = ResourceUtil
                    .getValueMap(currentResource, PromoConstants.TYPE_STATIC + calloutIndex);
            String scaffPagePath = calloutProperties.get(PromoConstants.PROPERTY_STATIC_LINK, String.class);
            String width = calloutProperties.get(ApplicationConstants.PN_WIDTH, number == 4 ? "3" : "4");
            LOG.debug("get Scaff  Page Ref Path: " + scaffPagePath);
            scaffPage = pageManager.getPage(scaffPagePath);
            if (null != scaffPage) {
                populateCalloutList(scaffPage, width);
            }
        }
    }
    
    /**
     * Retrieves and populates the promo values
     * 
     * @param page
     * @param width
     */
    private void populateCalloutList(Page page, String width) {
        if (null != page && null != page.getContentResource("promo")) {
            Resource promoResource = page.getContentResource("promo");
            ValueMap properties = promoResource.getValueMap();

            FeatureCallout callout = new FeatureCallout();
            callout.setImagePath(ImageUtil.getImagePath(promoResource));
            callout.setMobileImagePath(
                    ImageUtil.getImagePath(promoResource, PromoConstants.PROPERTY_MOBILE_IMAGE));
            callout.setMcdlogo(ImageUtil.getImagePath(promoResource, PromoConstants.PROPERTY_MCD_LOGO));
            callout.setLogoAlt(properties.get(PromoConstants.PROPERTY_LOGO_ALT, String.class));
            callout.setEyetitle(properties.get(PromoConstants.EYETITLE, String.class));
            callout.setEyetitleFontStyle(null != properties.get(PromoConstants.PN_FONTSTYLE_EYETITLE, String.class) ? properties.get(PromoConstants.PN_FONTSTYLE_EYETITLE, String.class) : PromoConstants.DEFAULT_FONT_STYLE);
            callout.setTitle(properties.get(ApplicationConstants.PN_TITLE, String.class));
            callout.setTitleFontStyle(null != properties.get(PromoConstants.PN_FONTSTYLE_TITLE, String.class) ? properties.get(PromoConstants.PN_FONTSTYLE_TITLE, String.class) : PromoConstants.DEFAULT_TITLE_FONT_STYLE);
            callout.setSubtitle(properties.get(PromoConstants.PN_SUBTITLE, String.class));
            callout.setSubtitleFontStyle(null != properties.get(PromoConstants.PN_FONTSTYLE_SUBTITLE, String.class) ? properties.get(PromoConstants.PN_FONTSTYLE_SUBTITLE, String.class) : PromoConstants.DEFAULT_FONT_STYLE);
            callout.setDescription(properties.get(ApplicationConstants.PN_DESC, String.class));
            callout.setDescriptionFontStyle(null != properties.get(PromoConstants.PN_FONTSTYLE_DESCR, String.class) ? properties.get(PromoConstants.PN_FONTSTYLE_DESCR, String.class) : PromoConstants.DEFAULT_FONT_STYLE);
            callout.setDisclaimer(properties.get(PromoConstants.PN_DISCLAIMER, String.class));
            String link = properties.get(ApplicationConstants.PN_LINK, String.class);
            callout.setLink(new BumperUtil(bumperRes).getLink(link, properties.get(ApplicationConstants.PN_CTA, String.class), false));
            callout.setAlignment(properties.get(ApplicationConstants.STYLE_POSITION, String.class));
            callout.setImageAlt(properties.get(ApplicationConstants.PN_ALT, String.class));
            callout.setTitleColor(properties.get(ApplicationConstants.PN_COLOR, String.class));
            callout.setCtaAlt(properties.get(PromoConstants.STYLE_CTA_ALT, String.class));
            callout.setAriaLabel(properties.get("ariaLabel", String.class));
            callout.setAnalytics("Promotion:"+ this.pageTitle + ":" + callout.getLink().getText());
            callout.setWidth(width);

            callout.setBottomAligned(properties.get(PromoConstants.BOTTOM_ALIGNED, false));
            callout.setRetainAlignment(properties.get(PromoConstants.RETAIN_ALIGNMENT, false));

            this.callouts.add(callout);
        }
    }
    
    /**
     * Checks whether all the callouts are empty or not.
     * 
     * @param limit
     * @param currentResource
     * @return
     */
    private boolean checkEmpty(int limit, Resource currentResource) {
		boolean empty = true;
		if (null != currentResource) {
            for (int calloutIndex = 1; calloutIndex <= limit; calloutIndex++) {
                String calloutLink = ResourceUtil.getChildResourceProperty(
                		currentResource, PromoConstants.TYPE_STATIC + calloutIndex, 
                		PromoConstants.PROPERTY_STATIC_LINK);
                empty = StringUtils.isBlank(calloutLink) ? empty : false;
            }
            LOG.debug("Is config empty for " + currentResource.getName() + "? " + empty);
        }
		return empty;
	}
    
    /**
	 * Retrieves the default callout resource for product page.
	 * 
	 * @param currentPage
	 * @return
	 */
	public static Resource getDefaultCalloutResource(Page currentPage) {
		return ResourceUtil.getChildResource(
				currentPage.getContentResource(), DEFAULT_PRODUCT_CALLOUT_RESOURCE);
	}
	
    /**
	 * @return the callouts
	 */
	public List<FeatureCallout> getCallouts() {
		return callouts;
	}

	/**
	 * @return the isEmpty
	 */
	public boolean isEmpty() {
		return isEmpty;
	}

	public String getPageTitle() {
		return pageTitle;
	}
}
