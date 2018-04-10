package com.mcd.rwd.global.core.sightly;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.widgets.CheckBox;
import com.citytechinc.cq.component.annotations.widgets.DialogFieldSet;
import com.citytechinc.cq.component.annotations.widgets.NumberField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.day.cq.wcm.api.designer.Design;
import com.icfolson.aem.multicompositeaddon.widget.MultiCompositeField;
import com.mcd.rwd.global.core.bean.ImageGalleryItems;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.utils.BumperUtil;
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
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by viswanath_n on 7/16/2015.
 */
@Component(name = "imagegallery", value = "Gallery / Carousel",
		description = "Displays multiple images with different layout options.",
		disableTargeting = true,
		actions = { "text: Gallery/Carousel", "-", "editannotate", "copymove", "delete" },
		group = ".hidden", path = "content",
		tabs = {@Tab(touchUINodeName = "basic", title = "Gallery / Carousel"),
				@Tab(touchUINodeName = "slideshow", title = "Slide Show Gallery"),
				@Tab(touchUINodeName = "thumbnail", title = "Thumbnail Gallery"),
				@Tab(touchUINodeName = "showcase", title = "Showcase Gallery")},
		listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
				@Listener(name = "afterinsert", value = "REFRESH_PAGE"),
				@Listener(name = "beforesubmit", value = "function(dlg) {\n" +
						"    var val = dlg.getField('./galleryType').getValue();\n" +
						"    if(!val || val == 'select') {\n" +
						"        CQ.Ext.Msg.alert('Sorry', 'Please select one of the styles for the gallery component.');\n" +
						"        return false;\n" +
						"    }\n" +
						"}")
		})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ImageGalleryHandler {

	@DialogField(defaultValue = "slide", value = "select", required = true, fieldLabel = "Image Gallery Type",
			fieldDescription = "Please select any one of the gallery styles - Slideshow, Thumbnail or Showcase.",
			listeners = {@Listener(name = "loadcontent", value = "function(selection,value){imageGallOnLoad(selection,value);}"),
					@Listener(name = "selectionchanged", value = "function(selection,value){imageGallOnSelect(selection,value);}")})

	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Please Select" , value="select"),
					@Option(text="Slideshow" , value="slideshow"),
					@Option(text="Thumbnail" , value="thumbnail"),
					@Option(text="Showcase" , value="showcase")
			})
	@Inject @Default(values = COMPONENT_NOT_CONFIGURED)
	private String galleryType;

	@DialogField(defaultValue = "slide", value = "slide", fieldLabel = "Animation type",
			fieldDescription = "Please select either Slide or Fade as an animation type.",
			listeners = {@Listener(name = "selectionchanged", value = "function(selection,value){animateOnSelect(selection,value);}")})
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Slide" , value="slide"),
					@Option(text="Fade" , value="fade")
			})
	@Inject @Default(values = ANIMATION_TYPE_SLIDE)
	private String animation;

	@DialogField(defaultValue = "slide", value = "horizontal", fieldLabel = "Transition",
			fieldDescription = "Please select any one of the Transition styles - Horizontal or Vertical.")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Horizontal" , value="horizontal"),
					@Option(text="Vertical" , value="vertical")
			})
	@Inject @Default(values = TRANSITION_HORIZONTAL)
	private String transition;

	@DialogField(fieldDescription = "Speed is in milliseconds (ms)", fieldLabel = "Speed",
	defaultValue = "5000")
	@NumberField
	@Inject @Default(values = TRANSITION_SPEED)
	private String speed;

	@DialogField(fieldDescription = "Please check the checkbox to enable AutoPlay",
			defaultValue = "{Boolean}true" , value = "on", fieldLabel = "Auto Play")
	@CheckBox(text = "Auto Play")
	@Inject @Default(values = AUTO_PLAY_FALSE)
	String autoplay;

	@DialogField(name = "./thumbPosition", defaultValue = "slide", value = "bottom", fieldLabel = "Thumbnail position",
			fieldDescription = "This field is applicable only for Thumbnail gallery")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Bottom" , value="bottom"),
					@Option(text="Left" , value="left"),
					@Option(text="Right" , value = "right")
			})
	@Inject @Named("thumbPosition") @Default(values = THUMBNAIL_POSITION_DEFAULT)
	private String thumbnailPosition;

	@DialogField(fieldLabel = "Text Decoration")
	@DialogFieldSet(collapsible = true, collapsed = false, title = "Text Decoration")
	@Inject
	private TextDecoration text;

	@DialogField(fieldLabel = "Slide show Gallery", tab = 2,
			additionalProperties = @Property(name = "class", value = "image-gallery")
		/*listeners = {@Listener(name = "render", value = "function(selection,value){renderData(selection,value,1);}")}*/)
	@MultiCompositeField
	@Inject
	private List<SlideShowGallery> slideshow;

	@DialogField(fieldLabel = "Thumbnail Gallery", fieldDescription = "*Applicable only for Thumbnail gallery.", tab = 3,
			additionalProperties = @Property(name = "class", value = "image-gallery")
		/*listeners = {@Listener(name = "render", value = "function(selection,value){renderData(selection,value,2);}")}*/)
	@MultiCompositeField
	@Inject
	private List<ThumbnailGallery> thumbnail;

	@DialogField(fieldLabel = "Showcase Gallery", tab = 4,
			additionalProperties = @Property(name = "class", value = "image-gallery")
		/*listeners = {@Listener(name = "render", value = "function(selection,value){renderData(selection,value,3);}")}*/)
	@MultiCompositeField
	@Inject
	private List<ShowcaseGallery> showcase;

	@Inject @Via("request")
	Resource resource;

	@Inject
	Design currentDesign;

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageGalleryHandler.class);

	private String imageGalleryType;

	private String textColor;

	private String textBackgroundColor;

	private String uniqueID;

	private Map<String, String> genericValues;

	private List<ImageGalleryItems> galleryList;

	private String thumbnailClassName;

/*	private String[] mainImage;

	private String[] imageDescription;

	private String[] hideDescripition;*/

	private static final String COMPONENT_NOT_CONFIGURED = "select";

	private static final String SLIDESHOW_GALLERY = "slideshow";

	private static final String SHOWCASE_GALLERY = "showcase";

	private static final String THUMBNAIL_GALLERY = "thumbnail";

	private static final String TRANSITION_HORIZONTAL = "horizontal";

	private static final String TRANSITION_SPEED = "5000";

	private static final String AUTO_PLAY_FALSE = "false";

	private static final String THUMBNAIL_POSITION_DEFAULT = "default";

	private static final String POSITION_LEFT = "left";

	private static final String POSITION_RIGHT = "right";

	private static final String POSITION_TOP = "top";

	private static final String POSITION_CENTER = "center";

	private static final String ANIMATION_TYPE_SLIDE = "slide";

	private static final String THUMBNAIL_LEFT_CLASS_NAME = "thumbnailsPosition verticalAlignLeft";

	private static final String THUMBNAIL_RIGHT_CLASS_NAME = "thumbnailsPosition verticalAlignRight";

	private static final String IMAGE_LEFT_CLASS_NAME = "flLeft-title-cta";

	private static final String IMAGE_RIGHT_CLASS_NAME = "flRight-title-cta";

	private static final String IMAGE_TOP_CLASS_NAME = "verticalTop";

	private static final String IMAGE_CENTER_CLASS_NAME = "verticalMiddle";

	private static final String CTA_POSITION_DELIMITER = "-";

	/**
	 * Method to perform Post Initialization Tasks
	 */
	@PostConstruct
	public void activate() throws Exception {
		try {
			ValueMap properties = resource.getValueMap();
			if(null != currentDesign) {
				imageGalleryType = this.galleryType;
				if (imageGalleryType.equals(COMPONENT_NOT_CONFIGURED)) {
					LOGGER.info("Please configure the Image Gallery Component");
				} else {
					Resource contentResource = currentDesign.getContentResource();
					if(null != contentResource) {
						BumperUtil bumperUtil = new BumperUtil(contentResource.getChild(ApplicationConstants.RES_BUMPER));
						if(null!=properties) {
							textColor = properties.get("textColor", String.class);
							textBackgroundColor = properties.get("bgColor", String.class);
						}
						if(null!=resource)
						uniqueID = resource.getName();
						String animationType;
						if (imageGalleryType.equals(SHOWCASE_GALLERY)) {
							animationType = ANIMATION_TYPE_SLIDE;
						} else {
							animationType = this.animation;
						}
						String transitionType = this.transition;
						String transitionSpeed = this.speed;
						String autoPlay = this.autoplay;
						String thumbnailType = this.thumbnailPosition;

						setGenericValues(createMap(transitionType, transitionSpeed, autoPlay, animationType));

						if (StringUtils.equals(imageGalleryType, SLIDESHOW_GALLERY)) {
							setGalleryList(createSlideShowList(slideshow , bumperUtil));

						} else if (StringUtils.equals(imageGalleryType, THUMBNAIL_GALLERY)) {
							setThumbnailClassName(checkAlignment(thumbnailType, THUMBNAIL_LEFT_CLASS_NAME,
									THUMBNAIL_RIGHT_CLASS_NAME));
							setGalleryList(createThumbnailList(thumbnail,bumperUtil));
						} else {
							setGalleryList(createShowcaseList(showcase, bumperUtil));
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in Image Gallery {}", e);
		}


	}

	/**
	 * Creates a map for common values configured by the author
	 *
	 * @return Map<String,String>
	 */
	private Map<String, String> createMap(String transitionType, String transitionSpeed, String autoPlay,
			String animationType) {
		Map<String, String> genericItems = new HashMap<String, String>();
		genericItems.put("data-transitionType", transitionType);
		genericItems.put("data-transitionSpeed", transitionSpeed);
		genericItems.put("data-autoPlay", autoPlay);
		genericItems.put("data-animationType", animationType);
		return genericItems;
	}

	/**
	 * checks for the Alignment based on author input left or right
	 *
	 * @return String
	 */
	private String checkAlignment(String currentInput, String leftAlignClass, String rightAlignClass) {
		String className;
		if (StringUtils.equals(currentInput, POSITION_LEFT)) {
			className = leftAlignClass;
		} else if (StringUtils.equals(currentInput, POSITION_RIGHT)) {
			className = rightAlignClass;
		} else {
			className = StringUtils.EMPTY;
		}
		return className;
	}

	/**
	 * Checks for the vertical alignment based on author input top, center or bottom.
	 *
	 * @return String
	 */
	private String checkVerticalAlignment(String currentInput, String topClass, String centerClass) {
		String className;
		if (StringUtils.equals(currentInput, POSITION_TOP)) {
			className = topClass;
		} else if (StringUtils.equals(currentInput, POSITION_CENTER)) {
			className = centerClass;
		} else {
			className = StringUtils.EMPTY;
		}
		return className;
	}

	private String getCTAPositionClass(String currentInput) {
		String className = StringUtils.EMPTY;
		if (StringUtils.isNotBlank(currentInput)) {
			String[] alignments = currentInput.split(CTA_POSITION_DELIMITER);
			if (alignments.length > 1) {
				className = checkVerticalAlignment(alignments[0], IMAGE_TOP_CLASS_NAME,
						IMAGE_CENTER_CLASS_NAME);
				className += ApplicationConstants.DELIM_SPACE + checkAlignment(alignments[1],
						IMAGE_LEFT_CLASS_NAME, IMAGE_RIGHT_CLASS_NAME);
			}
		}
		return className;
	}

	/**
	 * Creates a list of Slide Show Items using Customized Links configured by the author
	 *
	 * @return List<ImageGalleryItems>
	 */
	private List<ImageGalleryItems> createSlideShowList(List<SlideShowGallery> slideShowData , BumperUtil bumperUtil) {
		List<ImageGalleryItems> slideShowItems = new ArrayList<ImageGalleryItems>();
		for (SlideShowGallery slideShowItemData :slideShowData) {
			ImageGalleryItems galleryItems = new ImageGalleryItems();

			galleryItems.setTitleImage(slideShowItemData.getTitleImage());
			galleryItems.setMainImage(slideShowItemData.getMainImage());
			galleryItems.setBackGroundImage(slideShowItemData.getBgImage());
			galleryItems.setImageDescription(slideShowItemData.getDescriptionField());
			galleryItems.setCtaImage(slideShowItemData.getCtaImage());
			galleryItems.setLink(bumperUtil.getLink(slideShowItemData.getCtaPathField(), null, false));
			galleryItems.setTitleImageAlign(
					checkAlignment(slideShowItemData.getTitleImageAlignField(), IMAGE_LEFT_CLASS_NAME, IMAGE_RIGHT_CLASS_NAME));
			galleryItems.setMainImageAlign(
					checkAlignment(slideShowItemData.getMainImageAlignField(), IMAGE_LEFT_CLASS_NAME, IMAGE_RIGHT_CLASS_NAME));
			galleryItems.setCtaImageAlign(getCTAPositionClass(slideShowItemData.getCtaImageAlignField()));
			galleryItems.setHideDescripition(slideShowItemData.getHideDescField());

			slideShowItems.add(galleryItems);
		}
		LOGGER.info("List Size in SlideShow :: {}", slideShowItems.size());
		return slideShowItems;
	}

	/**
	 * Creates a list of Thumbnail List Items using Customized Links configured by the author
	 *
	 * @return List<ImageGalleryItems>
	 */
	private List<ImageGalleryItems> createThumbnailList(List<ThumbnailGallery> thumbnailData, final BumperUtil bumperUtil) {
		List<ImageGalleryItems> thumbnailItems = new ArrayList<ImageGalleryItems>();
		for (ThumbnailGallery thumbnailItemData : thumbnailData) {
			ImageGalleryItems galleryItems = new ImageGalleryItems();

			galleryItems.setMainImage(thumbnailItemData.getMainImage());
			galleryItems.setImageTitle(thumbnailItemData.getTitleField());
			galleryItems.setImageDescription(thumbnailItemData.getDescriptionField());
			galleryItems.setLink(bumperUtil.getLink(thumbnailItemData.getCtaPathField(), null, false));
			galleryItems.setThumbnailImage(thumbnailItemData.getThumbnailImage());
			galleryItems.setHideDescripition(thumbnailItemData.getHideDescField());

			thumbnailItems.add(galleryItems);
		}
		LOGGER.info("List Size in Thumbnail Gallery :: {}", thumbnailItems.size());
		return thumbnailItems;
	}

	/**
	 * Creates a list of Show Case Items using Customized Links configured by the author
	 *
	 * @return List<ImageGalleryItems>
	 */
	private List<ImageGalleryItems> createShowcaseList(List<ShowcaseGallery> showcaseData, final BumperUtil bumperUtil) {
		List<ImageGalleryItems> showcaseItems = new ArrayList<ImageGalleryItems>();
		for (ShowcaseGallery showcaseItemData : showcaseData) {
			ImageGalleryItems galleryItems = new ImageGalleryItems();

			galleryItems.setMainImage(showcaseItemData.getMainImage());
			galleryItems.setImageTitle(showcaseItemData.getTitleField());
			galleryItems.setImageDescription(showcaseItemData.getShowcaseDescField());
			galleryItems.setLink(bumperUtil.getLink(showcaseItemData.getCtaPathField(), null, false));
			galleryItems.setHideDescripition(showcaseItemData.getHideDescField());

			showcaseItems.add(galleryItems);
		}
		LOGGER.info("List Size in Showcase Gallery :: {}", showcaseItems.size());
		return showcaseItems;
	}

	/**
	 * Getter Method for galleryList
	 *
	 * @return List<ImageGalleryItems>
	 */
	public List<ImageGalleryItems> getGalleryList() {
		return galleryList;
	}

	/**
	 * Setter Method of galleryList
	 *
	 * @param galleryList
	 */
	public void setGalleryList(List<ImageGalleryItems> galleryList) {
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

	/**
	 * Getter Method for thumbnail class name based on position
	 *
	 * @return String
	 */
	public String getThumbnailClassName() {
		return thumbnailClassName;
	}

	/**
	 * Setter Method of thumbnail class name based on position
	 *
	 * @param thumbnailClassName
	 */
	public void setThumbnailClassName(String thumbnailClassName) {
		this.thumbnailClassName = thumbnailClassName;
	}

	public String getImageGalleryType() {
		return imageGalleryType;
	}

	public String getTextColor() {
		return textColor;
	}

	public String getTextBackgroundColor() {
		return textBackgroundColor;
	}

	public String getUniqueID() {
		return uniqueID;
	}

/*	private List<String[]> getGalleryData(){

	}*/

}
