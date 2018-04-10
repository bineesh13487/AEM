package com.mcd.rwd.us.core.sightly;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.widgets.*;
import com.day.cq.tagging.Tag;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.utils.ImageUtil;
import com.mcd.rwd.global.core.utils.LinkUtil;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.global.core.utils.ResourceUtil;
import com.mcd.rwd.us.core.bean.ImageGallery;
import com.mcd.rwd.us.core.constants.PromoConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;

/**
 * Handler class for configuring the image gallery / carousel component.
 * Created by prahlad.d on 5/19/2016.
 */
@Component(name = "imagegallery", value = "Gallery / Carousel",
        description = "Displays multiple images in slide show.",
        actions = { "text: Gallery / Carousel", "-", "editannotate", "copymove", "delete" },
        disableTargeting = true,
        resourceSuperType = "foundation/components/parbase",
        tabs = {@Tab( touchUINodeName = "carousel" , title = "Gallery / Carousel" )},
        group = " GWS-Global", path = "content",
        listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
                @Listener(name = "afterinsert", value = "REFRESH_PAGE")})

@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ImageGalleryHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageGalleryHandler.class);

    @DialogField(name = "./carousel/paths", fieldLabel = "Carousel Paths")
    @MultiField
    @PathField
    @Inject @Default(values = "")
    private String[] multi;

    @DialogField(defaultValue = "slide", name = "./carousel/animation", fieldLabel = "Animation type", fieldDescription = "Please select either Slide or Fade as an animation type.")
    @Selection(type = Selection.SELECT,
            options = {
                    @Option(text="Slide" , value="slide"),
                    @Option(text="Fade" , value="fade")
            })
    @Inject
    private String animation;

    @Inject
    Page getCurrentPage;

    @Inject
    PageManager pageManager;

    @Inject
    SlingHttpServletRequest request;

    @DialogField(name = "./carousel/speed", defaultValue = "5000", fieldLabel = "Speed", fieldDescription = "Speed is in milliseconds (ms)")
    @NumberField
    @Inject
    private String speed;

    @DialogField(name = "./carousel/autoPlay", fieldDescription = "Please check the checkbox to enable AutoPlay", value = "true")
    @CheckBox(text = "Auto Play")
    @Inject
    private String autoplay;

    private static final String PN_CAROUSEL = "carousel";

    private static final String TRANSITION_SPEED = "5000";

    private static final String AUTO_PLAY_FALSE = "false";

    private static final String PN_ANIMATION = "animation";

    private static final String ANIMATION_TYPE = "slide";

    private static final String PN_SPEED = "speed";

    private static final String PN_AUTO_PLAY = "autoPlay";

    private static final String DATA_ANIMATION_TYPE = "data-animation";

    private static final String DATA_TRANSITION_SPEED = "data-slide-speed";

    private static final String DATA_AUTO_PLAY = "data-slideshow";

    private static final String PN_VIDEO_IDS = "videoIds";

    private Map<String, String> genericValues;

    private List<ImageGallery> galleryList;

    private Set<String> tagSet;

    private String carouselResourcePath;

    private boolean empty = true;


    /**
     * Method to perform Post Initialization Tasks
     */
    @PostConstruct
    public void init() throws Exception{

        LOGGER.debug("init method called for gallery / carousel");
        ValueMap properties = ResourceUtil.getValueMap(request.getResource(), PN_CAROUSEL);
            this.galleryList = new LinkedList<ImageGallery>();
            String animation = properties.get(PN_ANIMATION, ANIMATION_TYPE);
            String transitionSpeed = properties.get("speed", "5000");
            String autoPlay = properties.get("autoPlay", String.class);
            String[] paths = properties.get("paths", String[].class);
            setGalleryList(createSlideShowList(paths));
            setGenericValues(createMap(animation, transitionSpeed, autoPlay));
            this.empty = this.galleryList.isEmpty();
    }


    /**
     * Creates a map for common values configured by the author
     *
     * @return Map<String,String>
     */
    private Map<String, String> createMap(String animation, String transitionSpeed, String autoPlay) {

        LOGGER.debug("set Generic Values for gallery/carousel");
        Map<String, String> genericItems = new HashMap<String, String>();
        genericItems.put(DATA_ANIMATION_TYPE, animation);
        genericItems.put(DATA_TRANSITION_SPEED, transitionSpeed);
        genericItems.put(DATA_AUTO_PLAY, autoPlay);
        return genericItems;
    }

    /**
     * Creates a list of Slide Show Items using Customized Links configured by the author
     *
     * @return List<ImageGallery>
     */
    private List<ImageGallery> createSlideShowList(String[] paths) {

        int count = 1;
        String pageTitle = PageUtil.getPageNameForAnalytics(getCurrentPage, request);
        this.tagSet = new HashSet<String>();
        List<ImageGallery> slideShowItems = new ArrayList<ImageGallery>();
        if(null != paths) {
            for (String path : paths) {
                String target;
                LOGGER.debug("gallery/carousel Paths Configured {}", path);
                if(null != pageManager) {
                LOGGER.info("Tag length "+ pageManager.getPage(path).getTags().length);
                Page page = pageManager.getPage(path);

                if (null != page && null != page.getContentResource(PromoConstants.PN_PROMO) && page.getTags().length == 0 ) {
                    Resource carouselResource = page.getContentResource(PromoConstants.PN_PROMO);
                    ValueMap properties = carouselResource.getValueMap();

                    ImageGallery carousel = new ImageGallery();
                    carousel.setImagePath(ImageUtil.getImagePath(carouselResource));
                    carousel.setMobileImagePath(
                            ImageUtil.getImagePath(carouselResource, PromoConstants.PROPERTY_MOBILE_IMAGE));
                    carousel.setCornerlogo(ImageUtil.getImagePath(carouselResource, PromoConstants.PN_CORNER_LOGO));
                    carousel.setCornerLogoAlt(properties.get(PromoConstants.PN_CORNER_LOGO_ALT, String.class));
                    carousel.setEyetitle(properties.get(PromoConstants.EYETITLE, String.class));
                    carousel.setTitle(properties.get(ApplicationConstants.PN_TITLE, String.class));
                    carousel.setDescription(properties.get(ApplicationConstants.PN_DESC, String.class));
                    carousel.setDisclaimer(properties.get(PromoConstants.PN_DISCLAIMER, String.class));

                    carousel.setHref(LinkUtil.getHref(properties.get(ApplicationConstants.PN_LINK, String.class)));
                    carousel.setLinkText(properties.get(ApplicationConstants.PN_CTA, String.class));

                    target = Boolean.valueOf(properties.get(ApplicationConstants.PN_TARGET, false)) ? "_blank" : "_self";
                    carousel.setTarget(target);

                    carousel.setCtaPosition(properties.get(ApplicationConstants.STYLE_CTA_POSITION, String.class));
                    carousel.setAriaLabel(properties.get(ApplicationConstants.PN_ARIA_LABEL, String.class));
                    carousel.setTitlePosition(properties.get(ApplicationConstants.STYLE_POSITION, String.class));
                    carousel.setImageAlt(properties.get(ApplicationConstants.PN_ALT, String.class));
                    carousel.setTitleColor(properties.get(ApplicationConstants.PN_COLOR, String.class));
                    carousel.setCtaAlt(properties.get(PromoConstants.STYLE_CTA_ALT, String.class));
                    carousel.setAnalytics("Carousal:"+ pageTitle + ":" + carousel.getLinkText()+ ":" + count++);

                    String[] videoIds = properties.get(PN_VIDEO_IDS, String[].class);
                    carousel.setVideoIds((null != videoIds) ?  getYouTubeId(videoIds) : null);

                    slideShowItems.add(carousel);
                }
                else if(null != page) {
                    LOGGER.info("page tags "+ page.getTags());
                    addPageTags(this.tagSet, page.getTags());
                    this.carouselResourcePath = request.getResource().getPath();
                    LOGGER.info("carouselResourcePath "+ request.getResource().getPath());
                }
            }
        }
        }
        LOGGER.info("List Size in SlideShow :: {}", slideShowItems.size());
        return slideShowItems;

    }

    /**
     * Function for add tags
     * @param tagSet
     * @param tags
     */
    private void addPageTags(Set<String> tagSet, Tag[] tags) {
        if(tags.length > 0) {
            for (int k = 0; k < tags.length; k++) {
                LOGGER.info("Tag Name "+ tags[k].getName());
                tagSet.add(tags[k].getName());
            }
            LOGGER.debug("#######tags Added#######");
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

    public List<ImageGallery> getGalleryList() {
        return galleryList;
    }

    public void setGalleryList(List<ImageGallery> galleryList) {
        this.galleryList = galleryList;
    }

    /**
     * Getter Method for genericValues
     *
     * @return Map<String,String>
     */
    public Map<String, String> getGenericValues() {
        return genericValues;
    }

    /**
     * Setter Method of genericValues
     *
     * @param genericValues
     */
    public void setGenericValues(Map<String, String> genericValues) {
        this.genericValues = genericValues;
    }

    public boolean isEmpty() {
        return empty;
    }

    public Set<String> getTagSet() {
        return tagSet;
    }

    public String getCarouselResourcePath() {
        return carouselResourcePath;
    }
}
