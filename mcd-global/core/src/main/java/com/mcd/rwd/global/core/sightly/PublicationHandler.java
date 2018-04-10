package com.mcd.rwd.global.core.sightly;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.Tab;
import com.citytechinc.cq.component.annotations.editconfig.DropTarget;
import com.citytechinc.cq.component.annotations.widgets.*;
import com.day.cq.wcm.foundation.Download;
import com.mcd.rwd.global.core.bean.*;
import com.mcd.rwd.global.core.bean.style.Margin;
import com.mcd.rwd.global.core.bean.style.Padding;
import com.mcd.rwd.global.core.bean.style.Unit;
import com.mcd.rwd.global.core.components.common.Image;
import com.mcd.rwd.global.core.components.common.Text;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.models.annotations.DesignAnnotation;
import com.mcd.rwd.global.core.utils.BumperUtil;
import com.mcd.rwd.global.core.utils.ImageUtil;
import com.mcd.rwd.global.core.utils.LinkUtil;
import com.mcd.rwd.global.core.widget.colorpicker.ColorPicker;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by km.nidhisa on 7/17/2015.
 */
@Component(
		name = "publication",
		value = "Publication",
		dropTargets = {
				@DropTarget(propertyName = "./image/imagePath", accept = {"image/.*"}, groups = {"media"},
						nodeName = "image")
		},
		actions = { "text: Publication", "-", "editannotate", "copymove", "delete" },
		disableTargeting = true,
		path = "content/",
		tabs = {
				@Tab(title = "Title" , touchUINodeName = "title"), @Tab(title = "Text" , touchUINodeName = "text"),@Tab(title = "Image" , touchUINodeName = "image") , @Tab(title = "Advanced" , touchUINodeName = "advanced")
		},
		inPlaceEditingActive = true,
		inPlaceEditingConfigPath = "../dialog/items/tabs/items/Text/items/text/items/text",
		inPlaceEditingEditorType = "text",
		listeners = {@Listener(name="beforesubmit",value = "function(dialog){ var img = dialog.getField('./image/file').hasData(); var imgPath = dialog.getField('./externalImagePath').getValue();if(img && imgPath.length>0){CQ.Ext.Msg.show({'title':'Dialog Validation','msg':'Please provide Image either from Image Tab or using HTTP Path of Image path.','buttons':CQ.Ext.Msg.OK,'icon':CQ.Ext.MessageBox.INFO,'scope':this}); return false;} var imageLink = dialog.getField('./imageLink').getValue();var thumb = dialog.getField('./createThumbnail').getValue();var uploadLink = dialog.getField('./file').hasData();if(thumb == ''){thumb = false;}if((thumb && uploadLink && imageLink.length>0) || (thumb && uploadLink) || (thumb && imageLink.length>0) || (uploadLink && imageLink.length>0)){CQ.Ext.Msg.show({'title':'Dialog Validation','msg':'Please provide Only Thumbnail Or Image Link Or Upload a file in Advanced Image Tab.','buttons':CQ.Ext.Msg.OK,'icon':CQ.Ext.MessageBox.INFO,'scope':this}); return false;} }"),
						@Listener(name = "loadContent",value = "function(dialog) { var checkFlag = dialog.getField('./flag').getValue(); if(checkFlag == '1') { dialog.getField('./flag').setValue('2'); var path=location.toString();path=path.replace('.html','.getTextDirection.html'); var value=CQ.Util.formatData(CQ.Util.eval(CQ.HTTP.get(path)));   if(value.direction=='rtl') { dialog.getField('./textAlign').setValue('right'); dialog.getField('./titleAlign').setValue('right'); dialog.getField('./borderTitleAlign').setValue('right'); dialog.getField('./captionAlignment').setValue('right'); dialog.getField('./imagePosition').setValue('right'); }  } }")},
		group = " GWS-Global")
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public final class PublicationHandler {

	@DialogField( value = "siteTitle", cssClass = "publicationhandler",
			fieldLabel = "Title Type", fieldDescription = "Select the type of title to insert")
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Main Title" , value="mainTitle"),
					@Option(text="Paragraph Title" , value="paragraphTitle"),
					@Option(text="Section Sub Title" , value="sectionSubTitle"),
					@Option(text="Section Title" , value="sectionTitle"),
					@Option(text="Site Title" , value="siteTitle"),
					@Option(text="H1 Title" , value="h1title"),
					@Option(text="H2 Title" , value="h2title"),
					@Option(text="H3 Title" , value="h3title")
			})
	@Inject
	@Default(values = "siteTitle")
	private String titleType;

	@DialogField( fieldDescription="Enter text for the section title.",fieldLabel="Title", name = "./title")
	@TextField
	@Inject @Named("title")
	private String mainTitle;

	@DialogField(value = "left",
			fieldLabel = "Horizontal Title Alignment")
	@Selection(type = Selection.RADIO,
			options = {
					@Option(text="Left" , value="left"),
					@Option(text="Center" , value="center"),
					@Option(text="Right" , value="right")
			})
	@Inject
	@Default(values = "left")
	private String titleAlign;

	@DialogField( fieldDescription="Choose the title color.",fieldLabel="Title Color")
	@ColorPicker
	@Inject
	private String titleColor;

	@DialogField( fieldDescription="Enter the title font size in px.",fieldLabel="Title Font Size")
	@NumberField
	@Inject
	private String titleFontSize;

	@DialogField( fieldDescription="Choose the Separator color.Leave this blank to not insert a separator.",fieldLabel="Separator Color")
	@ColorPicker
	@Inject
	private String separatorColor;

	@DialogField( fieldDescription="Enter the Line Separator size.",fieldLabel="Vertical Padding")
	@NumberField
	@Inject
	private String verticalPadding;

	@DialogField(tab = 2, fieldLabel = "Text", fieldDescription = "Use the in place text editor to edit this field.")
	@DialogFieldSet(title = "Text" , namePrefix = "text/")
	@ChildResource(name = "text")
	@Inject
	Text text;

	@DialogField(tab = 2,value = "left", fieldDescription="Choose how the text should be horizontally aligned",fieldLabel="Horizontal Text Alignment")
	@Selection(type = Selection.RADIO,
			options = {
					@Option(text="Left" , value="left"),
					@Option(text="Center" , value="center"),
					@Option(text="Right" , value="right")
			})
	@Inject
	@Default(values = "left")
	private String textAlign;

	@DialogField( tab = 2, fieldDescription="Choose the background color.",fieldLabel="Background Color")
	@ColorPicker
	@Inject
	private String backgroundColor;

	@DialogField( tab = 2, fieldDescription="Choose the font color",fieldLabel="Text Color")
	@ColorPicker
	@Inject
	private String textColor;

	@DialogField( tab = 2, fieldDescription="Anchor to be placed at the top of this paragraph.",fieldLabel="Anchor")
	@TextField
	@Inject
	private String anchor;

	@DialogField( tab = 2, fieldLabel="Call to Action Text")
	@TextField
	@Inject
	private String cta;

	@DialogField( tab = 2, fieldLabel="Call To Action Link", name = "./ctaLink")
	@PathField
	@Inject @Named("ctaLink")
	private String ctaPath;
	private Link ctaLink;

	@DialogField( tab = 2, fieldDescription="Aria Label for Call To Action Link", fieldLabel="Aria Label")
	@TextField
	@Inject
	@Named(ARIA_LABEL)
	private String ariaLabel;

	@DialogField(tab = 3, cssClass = "publicationimagetab", fieldLabel = "image*", name="./image/file", additionalProperties = {
			@Property(name = "fileNameParameter", value = "./image/fileName"),
			@Property(name = "fileReferenceParameter", value = "./image/fileReference"),
			@Property(name = "cropParameter", value = "./image/imageCrop"),
			@Property(name = "allowUpload", value = "false")})
	@Html5SmartFile(sizeLimit = 100, fileNameParameter = "./image/fileName",
			fileReferenceParameter = "./image/fileReference",allowUpload = false)
	@ChildResource(name = "image")
	@Inject @Named("fileReference")
	private String image;

	@DialogFieldSet(title = "Border", collapsible = true , collapsed = false )
	@DialogField(tab = 4)
	@Inject
	private PublicationBorderItems publicationBorderPOJO;

	@DialogFieldSet(title="Advanced Image", collapsible = true , collapsed = true )
	@DialogField(tab = 4, cssClass = "publicationadvancedimage")
	@Inject
	private PublicationAdvancedImageItems publicationAdvancedImageItems;

	@DialogFieldSet(title="Padding", collapsible = true , collapsed = true )
	@DialogField(tab = 4)
	@Inject
	private PublicationPaddingItems publicationPaddingItems;

	@DialogFieldSet(title="Margin", collapsible = true , collapsed = true )
	@DialogField(tab = 4)
	@Inject
	private PublicationMarginItems publicationMarginItems;

	@DesignAnnotation(value = ApplicationConstants.RES_BUMPER)
	Resource bumperRes;

	@Inject
	Resource currentResource;

	@Inject
	@Default(values = "left")
	String borderTitleAlign;

	@Inject
	String borderTitleColor;

	@Inject
	Double marginTop;

	@Inject
	Double marginBottom;

	@Inject
	Double marginLeft;

	@Inject
	Double marginRight;

	@Inject
	String paragraphTitle;


	@Inject
	String borderColor;

	@Inject
	String borderSize;

	@Inject
	String externalImagePath;

	@Inject
	String imageAlt;

	@Inject
	@Default(values = "left")
	String imageSize;

	@Inject
	@ChildResource(name = "file")
	Image imageFile;

	@Inject
	boolean createThumbnail;

	@Inject
	String imageLink;

	@Inject
	@Default(values = "bottomLeft")
	String imagePosition;

	@Inject
	String caption;

	@Inject
	@Default(values = "left")
	String captionAlignment;

	@Inject
	Double paddingTopImage;

	@Inject
	Double paddingBottomImage;

	@Inject
	Double paddingRightImage;

	@Inject
	Double paddingLeftImage;

	@Inject
	Double paddingTop;

	@Inject
	Double paddingBottom;

	@Inject
	Double paddingLeft;

	@Inject
	Double paddingRight;

	@Inject
	Resource resource;

	private static final Logger LOGGER = LoggerFactory.getLogger(PublicationHandler.class);

	private static final String BACKGROUND_COLOR_PREFIX = "background: #";

	private static final String FILE_NAME = "fileName";

	private static final String ENABLE_ZOOM = "enableZoom";

	private static final String CONSTANT_TEXTALIGN = "text-align: ";

	private static final String CONSTANT_COLOR = "color: ";
	
	private static final String ARIA_LABEL = "aria";

	private String cssClass = StringUtils.EMPTY;

	private boolean bottomShow = false;

	private String imageAlignment = StringUtils.EMPTY;

	private Padding padding;

	private Padding imagePadding;

	private Margin margin;

	private String richText;

	private String imgWidth = "100";


	private String anchorClass = StringUtils.EMPTY;

	private String backgroundStyle;

	private String border;

	private String borderTitle = StringUtils.EMPTY;

	private String titleStyle = StringUtils.EMPTY;

	private String width = "auto";

	private String imagePath = StringUtils.EMPTY;

	private String fileReference = StringUtils.EMPTY;

	private String fileName = StringUtils.EMPTY;

	private String imageHref = StringUtils.EMPTY;

	private boolean enableZoom;

	@PostConstruct
	public void activate() throws Exception {
		LOGGER.debug("activate() method called");
		populateDialogProperties();
		populateColors();
		populateStyles();

		width = "auto";
		if (imageAlignment.toLowerCase().contains("left")) {
			imagePosition = "left";
		} else if (imageAlignment.toLowerCase().contains("right")) {
			imagePosition = "right";
		} else if (imageAlignment.toLowerCase().contains("top") || imageAlignment.toLowerCase()
				.contains("bottom")) {
			imagePosition = "center";
		}

		if (imageAlignment.contains("bottom")) {
			bottomShow = true;
		}
		if (StringUtils.isBlank(imagePath)) {
			if (StringUtils.EMPTY.equals(externalImagePath)) {
				imagePath = StringUtils.EMPTY;
				imgWidth = "overflow: hidden; width: " + imgWidth + ApplicationConstants.PERCENTAGE + ";";
			} else {
				imagePath = externalImagePath;
			}
		}

		Download dld = new Download(currentResource);
		if (dld.hasContent()) {
			fileReference = com.day.text.Text.escape(dld.getHref(), ApplicationConstants.URL_PERCENTAGE, true);
			fileName = dld.getFileName();
			LOGGER.debug("Download Reference {}", fileReference);
		}

		if(null != imagePath) {
			imageHref = ImageUtil.getImagePath(currentResource,  ApplicationConstants.RES_IMAGE);
		}
	}

	/**
	 * Set the dialog properites value.
	 */
	public void populateDialogProperties() {
		LOGGER.debug("populateDialogProperties() method called");
			imageAlignment = imagePosition; // To get the alignment of image
			imageLink = LinkUtil.getHref(imageLink);

			if(null != paddingTopImage && null !=  paddingRightImage && null != paddingBottomImage && null != paddingLeftImage){
				imagePadding = new Padding.PaddingBuilder(Unit.PIXELS).top(paddingTopImage)
						.right(paddingRightImage).bottom(paddingBottomImage)
						.left(paddingLeftImage).createPadding();
			}


		if(null !=  resource){
			if(text != null){
				richText = validateLinks(text.getText());
			}

		}

		imagePath = LinkUtil.getDownloadHref(currentResource, ApplicationConstants.RES_IMAGE);
		/*fileName = properties.get(FILE_NAME, String.class);
		enableZoom = properties.get(ENABLE_ZOOM, false);*/
		if(null != bumperRes){
			BumperUtil bumperUtil = new BumperUtil(bumperRes);
			if(null != bumperUtil){
				ctaLink = bumperUtil.getLink(ctaPath, cta, false);
			}
		}

		// Padding to be included in the component
		LOGGER.debug("populateDialogProperties(): Padding to be included in the component");
		if(null != paddingTop && null !=  paddingRight && null != paddingLeft && null != paddingBottom) {
			padding = new Padding.PaddingBuilder(Unit.PIXELS).top(paddingTop)
					.right(paddingRight).bottom(paddingBottom)
					.left(paddingLeft).createPadding();
		}


			// Margin to be included in the component
		if(null != marginTop && null !=  marginRight && null != marginBottom && null != marginLeft) {
			margin = new Margin.MarginBuilder(Unit.PIXELS)
					.top(marginTop)
					.right(marginRight)
					.bottom(marginBottom)
					.left(marginLeft).createMargin();
		}

	}

	private void populateStyles() {

			imgWidth = imageSize;

			if (imgWidth != null) {
				imgWidth = "overflow: hidden; width: " + imgWidth + ApplicationConstants.PERCENTAGE + ";";
			}
			if (StringUtils.isNotBlank(captionAlignment)) {
				captionAlignment = CONSTANT_TEXTALIGN + captionAlignment + ";";
			}
			if (StringUtils.isNotBlank(borderTitleAlign)) {
				borderTitle += CONSTANT_TEXTALIGN + borderTitleAlign + ";";
			}

		if (StringUtils.isNotBlank(textAlign)) {
			titleStyle +=  textAlign;
		}

		if (StringUtils.isNotBlank(titleFontSize) && !titleFontSize.equals("0")) {
			titleStyle += "font-size: " + titleFontSize + "px; line-height: " + titleFontSize + "px";
			verticalPadding = "height: " + titleFontSize + "px;";
		}


	}

	private void populateColors() {

			if (StringUtils.isNotBlank(borderSize)) {
				border = borderSize + "px solid #" + borderColor + ";";
			}
			if (StringUtils.isNotBlank(borderTitleColor)) {
				borderTitle += CONSTANT_COLOR + borderTitleColor + ";";
			}


		if (StringUtils.isNotBlank(textColor)) {
			textColor = CONSTANT_COLOR + textColor + ";";
		}
		if (StringUtils.isNotBlank(backgroundColor)) {
			this.backgroundStyle = BACKGROUND_COLOR_PREFIX + backgroundColor + ";";
		}
		if (StringUtils.isNotBlank(titleColor)) {
			titleStyle += CONSTANT_COLOR + titleColor + ";";
		}
	}

	private String validateLinks(String text) {
		LOGGER.debug("validateLinks() method called");
		String finalText = text;
		if (StringUtils.isNotBlank(text)) {
			try {
				BumperUtil bumperUtil = new BumperUtil(bumperRes);
				int currentIndex = finalText.indexOf("href=");
				int endIndex;
				while (currentIndex != -1) {
					endIndex = finalText.indexOf('>', currentIndex);
					String link = finalText.substring(currentIndex + 6, endIndex - 1);

					finalText = !(link.startsWith("/content") || link.startsWith("mailto:")
							|| link.indexOf('#') == 0) ?
							getFinalText(finalText, endIndex, bumperUtil.getLink(link, null, false)) :
							text;
					currentIndex = finalText.indexOf("href=", endIndex);
				}
			} catch (Exception e) {
				LOGGER.error("Error in validating links " + e);
			}
		}
		return finalText;
	}

	private String getFinalText(String finalText, int endIndex, Link bumperLink) {
		LOGGER.debug("getFinalText(String finalText, int endIndex, Link bumperLink) method called");
		return StringUtils.isNotBlank(bumperLink.getLinkClass()) ?
				finalText.substring(0, endIndex) + " class=\"" + bumperLink.getLinkClass() + "\"" + finalText
						.substring(endIndex, finalText.length()) :
				finalText;
	}
	
	public String getAriaLabel() {
		return ariaLabel;
	}

	public String getCssClass() {
		return cssClass;
	}

	public boolean isBottomShow() {
		return bottomShow;
	}

	public String getImageAlignment() {
		return imageAlignment;
	}

	public Padding getPadding() {
		return padding;
	}

	public Padding getImagePadding() {
		return imagePadding;
	}

	public String getImgWidth() {
		return imgWidth;
	}

	public boolean isCreateThumbnail() {
		return createThumbnail;
	}

	public String getImageLink() {
		return imageLink;
	}

	public String getAnchorClass() {
		return anchorClass;
	}

	public String getCaptionAlignment() {
		return captionAlignment;
	}

	public String getTextAlign() {
		return textAlign;
	}

	public String getTextColor() {
		return textColor;
	}

	public String getBackgroundStyle() {
		return backgroundStyle;
	}

	public String getAnchor() {
		return anchor;
	}

	public String getBorder() {
		return border;
	}

	public String getBorderColor() {
		if(null != publicationBorderPOJO.getBorderColor()){
			return publicationBorderPOJO.getBorderColor();
		}
		return "";
	}

	public String getBorderTitle() {
		return borderTitle;
	}

	public String getParagraphTitle() {
		return paragraphTitle;
	}

	public String getMainTitle() {
		return mainTitle;
	}

	public String getTitleAlign() {
		return titleAlign;
	}

	public String getSeparatorColor() {
		return separatorColor;
	}

	public String getTitleStyle() {
		return titleStyle;
	}

	public String getTitleType() {
		return titleType;
	}

	public String getVerticalPadding() {
		return verticalPadding;
	}

	public String getWidth() {
		return width;
	}

	public String getImagePosition() {
		return imagePosition;
	}

	public String getExternalImagePath() {
		return externalImagePath;
	}

	public String getImagePath() {
		return imagePath;
	}

	public String getImageAlt() {
		return imageAlt;
	}

	public String getText() {
		return richText;
	}

	public String getCaption() {
		return caption;
	}

	public String getFileReference() {
		return fileReference;
	}

	public String getFileName() {
		return fileName;
	}

	public String getImageHref() {
		return imageHref;
	}

	public boolean isEnableZoom() {
		return enableZoom;
	}

	public Link getCtaLink() {
		return ctaLink;
	}

	public Margin getMargin() {
		return margin;
	}
}
