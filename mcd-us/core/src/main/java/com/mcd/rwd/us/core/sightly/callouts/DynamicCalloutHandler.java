package com.mcd.rwd.us.core.sightly.callouts;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.gson.Gson;
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
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles the processing of the component properties
 * for random and time-based callout types.
 *
 * @author HCL
 */

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DynamicCalloutHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicCalloutHandler.class);

    private Map<String, String> calloutMap;

    private List<String> calloutWidths;

    @Inject @Default(values = StringUtils.EMPTY)
    private String type;

    @Inject @Default(intValues = 1)
    private int number;

    private String pageTitle;

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
        this.calloutWidths = new ArrayList<String>();
        this.pageTitle = PageUtil.getPageNameForAnalytics(currentPage, request);
        if (PromoConstants.TYPE_RANDOM.equals(type)) {
            LOG.debug("Generating Random Callout Map");
            generateRandomCalloutMap(number);
        } else {
            LOG.debug("Generating Time Based Callout Map");
            generateTimeBasedCalloutMap(number);
        }
    }

    private void generateRandomCalloutMap(int limit) {
        this.calloutMap = new LinkedHashMap<String, String>();
        Gson gson = new Gson();

        for (int i = 1; i <= limit; i++) {
            ValueMap properties = ResourceUtil.getValueMap(resource, PromoConstants.TYPE_RANDOM + i);
            if (null != properties) {
                this.calloutWidths.add(properties.get(ApplicationConstants.PN_WIDTH, limit == 4 ? "3" : "4"));
                List<FeatureCallout> calloutList = new ArrayList<FeatureCallout>();
                String[] paths = properties.get(PromoConstants.PN_PATHS, String[].class);
                if (null != paths) {
                    for (String path : paths) {
                        LOG.debug("Random Callout Paths Configured {}", path);
                        calloutList = populateCalloutList(calloutList, pageManager.getPage(path), null, null,
                                false);
                    }
                }
                this.calloutMap.put(PromoConstants.TYPE_RANDOM + i, gson.toJson(calloutList));
            }
        }
    }

    private Map<String, String> generateTimeBasedCalloutMap(int limit) {
        this.calloutMap = new LinkedHashMap<String, String>();
        Gson gson = new Gson();

        for (int i = 1; i <= limit; i++) {
            Resource childResource = ResourceUtil
                    .getChildResource(resource, PromoConstants.TYPE_TIMEBASED + i);
            ValueMap properties = ResourceUtil
                    .getValueMap(resource, PromoConstants.TYPE_TIMEBASED + i);
            /*List<Map<String, String>> data = MultiFieldPanelUtil
                    .getMultiFieldPanelValues(childResource, PromoConstants.PROPERTY_DATA);*/
            if (null != childResource.getChild("promo")) {
                List<Map<String, String>> data = getMultiCompositeFieldValues(childResource.getChild("promo"));
                this.calloutWidths.add(properties.get(ApplicationConstants.PN_WIDTH, limit == 4 ? "3" : "4"));
                if (null != data && !data.isEmpty()) {
                    Iterator<Map<String, String>> itr = data.iterator();
                    List<FeatureCallout> calloutList = new ArrayList<FeatureCallout>();
                    while (itr.hasNext()) {
                        Map<String, String> map = itr.next();
                        LOG.debug("TimeBased Callout Paths Configured {}", map.get(ApplicationConstants.PN_LINK));
                        calloutList = populateCalloutList(calloutList,
                                pageManager.getPage(map.get(ApplicationConstants.PN_LINK)),
                                map.get(PromoConstants.PROPERTY_DYNAMIC_STARTTIME),
                                map.get(PromoConstants.PROPERTY_DYNAMIC_ENDTIME),
                                map.get(PromoConstants.PROPERTY_DEFAULT).contains(ApplicationConstants.FLAG_TRUE));
                    }

                    this.calloutMap.put(PromoConstants.TYPE_TIMEBASED + i, gson.toJson(calloutList));
                }
            }
        }

        return null;
    }

    private List<FeatureCallout> populateCalloutList(List<FeatureCallout> list, Page page, String start,
                                                     String end, boolean isDefault) {
        if (null != page && null != page.getContentResource(PromoConstants.PN_PROMO)) {
            Resource promoResource = page.getContentResource(PromoConstants.PN_PROMO);
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
            callout.setAnalytics("Promotion:"+ this.pageTitle + ":" + callout.getLink().getText());
            callout.setAriaLabel(properties.get(ApplicationConstants.PN_ARIA_LABEL, String.class));
            callout.setBottomAligned(properties.get(PromoConstants.BOTTOM_ALIGNED, false));
            callout.setRetainAlignment(properties.get(PromoConstants.RETAIN_ALIGNMENT, false));

            //Set time if callout type is not random
            if (!PromoConstants.TYPE_RANDOM.equals(this.type)) {
                callout.setStartTime(start);
                callout.setEndTime(end);
                callout.setDefaultCallout(isDefault);
            }
            list.add(callout);
        }
        return list;
    }

    private List<Map<String, String>> getMultiCompositeFieldValues(Resource resource) {
        List<Map<String, String>> results = new ArrayList<Map<String, String>>();
        Iterable<Resource> iterator = resource.getChildren();
        for (Resource promoResource : iterator) {
            Map<String, String> map = new HashMap<>();
            ValueMap valueMap = promoResource.getValueMap();
            map.put(ApplicationConstants.PN_LINK, valueMap.get(ApplicationConstants.PN_LINK, String.class));
            map.put(PromoConstants.PROPERTY_DYNAMIC_STARTTIME, valueMap.get(PromoConstants.PROPERTY_DYNAMIC_STARTTIME, String.class));
            map.put(PromoConstants.PROPERTY_DYNAMIC_ENDTIME, valueMap.get(PromoConstants.PROPERTY_DYNAMIC_ENDTIME, String.class));
            map.put("default", valueMap.get("defualt", "false"));
            results.add(map);
        }
        return results;
    }

    /**
     * @return the calloutMap
     */
    public Map<String, String> getCalloutMap() {
        return calloutMap;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    public List<String> getCalloutWidths() {
        return this.calloutWidths;
    }

    public String getPageTitle() {
        return pageTitle;
    }
}
