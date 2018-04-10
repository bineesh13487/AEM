package com.mcd.rwd.us.core.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.tagging.Tag;
import com.day.cq.wcm.api.Page;
import com.google.gson.Gson;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.utils.ImageUtil;
import com.mcd.rwd.global.core.utils.LinkUtil;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.global.core.utils.ResourceUtil;
import com.mcd.rwd.us.core.bean.Carousel;
import com.mcd.rwd.us.core.bean.ImageGallery;
import com.mcd.rwd.us.core.constants.PromoConstants;

/**
 * Created by prahlad.d on 6/13/2016.
 */

@SlingServlet(
        paths = {"/services/mcd/localGallerySlide"},
        methods = {"GET"},extensions = "json"
)
@Properties(value = {
        @Property(name = "service.pid", value = "com.mcd.rwd.us.core.servlets.LocalGallerySlideServlet",
                propertyPrivate = false),
        @Property(name = "service.description", value = "Provides local gallery slide  in json format (key-value)",
                propertyPrivate = false),
        @Property(name = "service.vendor", value = "HCL Technologies", propertyPrivate = false)})
public class LocalGallerySlideServlet extends SlingSafeMethodsServlet {

	private static final String PN_CAROUSEL = "carousel";

    private static final String TRANSITION_SPEED = "5000";

    private static final String AUTO_PLAY_FALSE = "false";

    private static final String PN_ANIMATION = "animation";

    private static final String ANIMATION_TYPE = "slide";

    private static final String PN_SPEED = "speed";

    private static final String PN_AUTO_PLAY = "autoPlay";

    private static final String PN_VIDEO_IDS = "videoIds";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalGallerySlideServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        try {
        response.setContentType(ApplicationConstants.CONTENT_TYPE_JSON);

        String coopID = request.getParameter("coopID");
        String carouselResourcePath = request.getParameter("carouselPath");
        Resource resource = request.getResource().getResourceResolver().getResource(carouselResourcePath);
        ValueMap properties = ResourceUtil.getValueMap(resource, PN_CAROUSEL);
        if(null != properties){
            String animation = properties.get(PN_ANIMATION, ANIMATION_TYPE);
            String transitionSpeed = properties.get(PN_SPEED, TRANSITION_SPEED);
            String autoPlay = properties.get(PN_AUTO_PLAY, AUTO_PLAY_FALSE);
            String[] paths = properties.get(PromoConstants.PN_PATHS, String[].class);
            Carousel gallery = new Carousel();
            gallery.setAnimation(animation);
            gallery.setSpeed(transitionSpeed);
            gallery.setSlideshow(autoPlay);

            String pageTitle = PageUtil.getPageNameForAnalytics(ResourceUtil.getContainerPage(resource), request);

            Map<String, Carousel> galleryMap = new HashMap<String, Carousel>();
            List<ImageGallery> slideShowItems = new ArrayList<ImageGallery>();
            if (null != paths) {
                for (String path : paths) {
                    String target;
                    LOGGER.debug("gallery/carousel Paths Configured {}", path);
                    Page page = getContainerPage(request.getResource().getResourceResolver().getResource(path));
                    Resource carouselResource = null;

                    if(null != page)
                        carouselResource = page.getContentResource(PromoConstants.PN_PROMO);

                    if (null != carouselResource &&  compareTags(coopID, page.getTags())) {
                        ValueMap pageProperties = carouselResource.getValueMap();

                        ImageGallery carousel = new ImageGallery();
                        carousel.setImagePath(ImageUtil.getImagePath(carouselResource));
                        carousel.setMobileImagePath(
                                ImageUtil.getImagePath(carouselResource, PromoConstants.PROPERTY_MOBILE_IMAGE));
                        carousel.setCornerlogo(ImageUtil.getImagePath(carouselResource, PromoConstants.PN_CORNER_LOGO));
                        carousel.setCornerLogoAlt(pageProperties.get(PromoConstants.PN_CORNER_LOGO_ALT, String.class));
                        carousel.setEyetitle(pageProperties.get(PromoConstants.EYETITLE, String.class));
                        carousel.setTitle(pageProperties.get(ApplicationConstants.PN_TITLE, String.class));
                        carousel.setDescription(pageProperties.get(ApplicationConstants.PN_DESC, String.class));
                        carousel.setDisclaimer(pageProperties.get(PromoConstants.PN_DISCLAIMER, String.class));
                        carousel.setHref(LinkUtil.getHref(pageProperties.get(ApplicationConstants.PN_LINK, String.class)));
                        carousel.setLinkText(pageProperties.get(ApplicationConstants.PN_CTA, String.class));
                        target = Boolean.valueOf(pageProperties.get(ApplicationConstants.PN_TARGET, false)) ? "_blank" : "_self";
                        carousel.setTarget(target);

                        carousel.setCtaPosition(pageProperties.get(ApplicationConstants.STYLE_CTA_POSITION, String.class));
                        carousel.setAriaLabel(pageProperties.get(PromoConstants.PN_ARIA_LABEL, String.class));
                        carousel.setTitlePosition(pageProperties.get(ApplicationConstants.STYLE_POSITION, String.class));
                        carousel.setImageAlt(pageProperties.get(ApplicationConstants.PN_ALT, String.class));
                        carousel.setTitleColor(pageProperties.get(ApplicationConstants.PN_COLOR, String.class));
                        carousel.setCtaAlt(pageProperties.get(PromoConstants.STYLE_CTA_ALT, String.class));
                        carousel.setAnalytics(new StringBuilder("Carousal:").append(pageTitle).append(":").append(carousel.getLinkText()).toString());

                        String[] videoIds = pageProperties.get(PN_VIDEO_IDS, String[].class);
                        carousel.setVideoIds((null != videoIds) ?  getYouTubeId(videoIds) : StringUtils.EMPTY);

                        slideShowItems.add(carousel);
                    }
                }
            }
            gallery.setImagegallery(slideShowItems);
            galleryMap.put("slides", gallery);
            String localSlideResponse = new Gson().toJson(galleryMap);
            response.getWriter().print(localSlideResponse);

        }

        } catch (IOException ioe) {
            LOGGER.error("Exception in local gallery slide while writing response", ioe);
        }
    }

    /**
     * Function for comparing coop with slide tagid
     * @param coop
     * @param tags
     * @return
     */
    private boolean compareTags(String coop, Tag[] tags) {
        boolean isMatch = false;
        if(tags.length > 0) {
            for (int k = 0; k < tags.length; k++) {
                if(tags[k].getName().equals(coop) && !coop.equalsIgnoreCase(StringUtils.EMPTY)) {
                	isMatch = true;
                	LOGGER.debug("####### coopid: "+ tags[k].getName() +" matched #######");
                	break;
                }
            }
            return isMatch;
        }else{
            return true;
        }
    }

    /**
     * Funtion for return youtube video ids.
     * @param videoIds
     * @return
     */
    private String getYouTubeId(String[] videoIds) {
        StringBuilder youtubeId = new StringBuilder();
        for (String videoId : videoIds) {
            youtubeId.append(videoId).append(",");
        }
        LOGGER.debug("Adding videoIds :: {}", youtubeId.substring(0, youtubeId.length() - 1));
        return youtubeId.substring(0, youtubeId.length() - 1);
    }

    /**
     * Function for getting the current page.
     * @param resource
     * @return
     */
    private Page getContainerPage(Resource resource) {
        if (resource != null) {
            return resource.adaptTo(Page.class) != null ? resource.adaptTo(Page.class) : getContainerPage(resource.getParent());
        }
        return null;
    }
}
