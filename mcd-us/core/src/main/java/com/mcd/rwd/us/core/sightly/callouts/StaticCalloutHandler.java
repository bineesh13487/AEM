package com.mcd.rwd.us.core.sightly.callouts;

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
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

/**
 * @author HCL
 */

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class StaticCalloutHandler {

    private static final Logger LOG = LoggerFactory.getLogger(StaticCalloutHandler.class);

    private List<FeatureCallout> callouts;

    @Inject
    SlingHttpServletRequest request;

    @Inject
    @Via("request")
    Resource resource;

    @Inject
    Page currentPage;

    @Inject
    PageManager pageManager;

    @DesignAnnotation(ApplicationConstants.RES_BUMPER)
    Resource bumperRes;

    /* (non-Javadoc)
     * @see com.adobe.cq.sightly.WCMUse#activate()
     */
    @PostConstruct
    public void activate() {
        LOG.debug("activate() method called.");

        Page scaffPage;
        ValueMap properties = resource.getValueMap();
        int limit = properties.get(PromoConstants.PROPERTY_NUMBER, 1);
        this.callouts = new LinkedList<FeatureCallout>();
        String pageTitle = PageUtil.getPageNameForAnalytics(currentPage, request);

        for (int calloutIndex = 1; calloutIndex <= limit; calloutIndex++) {
            ValueMap calloutProperties = ResourceUtil
                    .getValueMap(resource, PromoConstants.TYPE_STATIC + calloutIndex);
            String scaffPagePath = calloutProperties.get(PromoConstants.PROPERTY_STATIC_LINK, String.class);
            String width = calloutProperties.get(ApplicationConstants.PN_WIDTH, limit == 4 ? "3" : "4");
            LOG.debug("get Scaff  Page Ref Path: " + scaffPagePath);
            scaffPage = pageManager.getPage(scaffPagePath);
            if (null != scaffPage) {
                populateCalloutList(scaffPage, width, pageTitle);
            }
        }
    }

    private void populateCalloutList(Page page, String width, String pageTitle) {
        if (null != page && null != page.getContentResource("promo")) {
            Resource promoResource = page.getContentResource("promo");
            ValueMap properties = promoResource.getValueMap();

            FeatureCallout callout = new FeatureCallout();
            callout.setImagePath(ImageUtil.getImagePath(promoResource));
            callout.setMobileImagePath(
                    ImageUtil.getImagePath(promoResource, PromoConstants.PROPERTY_MOBILE_IMAGE));
            callout.setMcdlogo(ImageUtil.getImagePath(promoResource, PromoConstants.PROPERTY_MCD_LOGO));
            callout.setLogoAlt(properties.get(PromoConstants.PROPERTY_LOGO_ALT, String.class));
            callout.setCornerlogo(ImageUtil.getImagePath(promoResource, PromoConstants.PN_CORNER_LOGO));
            callout.setCornerLogoAlt(properties.get(PromoConstants.PN_CORNER_LOGO_ALT, String.class));
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
            callout.setLink(new BumperUtil(bumperRes)
                            .getLink(link, properties.get(ApplicationConstants.PN_CTA, String.class), false));
            callout.setAlignment(properties.get(ApplicationConstants.STYLE_POSITION, String.class));
            callout.setImageAlt(properties.get(ApplicationConstants.PN_ALT, String.class));
            callout.setTitleColor(properties.get(ApplicationConstants.PN_COLOR, String.class));
            callout.setCtaAlt(properties.get(PromoConstants.STYLE_CTA_ALT, String.class));
            callout.setWidth(width);
            callout.setAnalytics("Promotion:"+ pageTitle + ":" + callout.getLink().getText());
            callout.setAriaLabel(properties.get(ApplicationConstants.PN_ARIA_LABEL, String.class));

            callout.setBottomAligned(properties.get(PromoConstants.BOTTOM_ALIGNED, false));
            callout.setRetainAlignment(properties.get(PromoConstants.RETAIN_ALIGNMENT, false));

            this.callouts.add(callout);
        }
    }

    /**
     * @return the calloutList
     */
    public List<FeatureCallout> getCallouts() {
        return callouts;
    }

}
