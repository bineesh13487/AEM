package com.mcd.rwd.global.core.sightly;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Listener;
import com.citytechinc.cq.component.annotations.Option;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.Tab;
import com.citytechinc.cq.component.annotations.widgets.*;
import com.day.cq.wcm.api.Page;
import com.mcd.rwd.global.core.bean.VideoFormats;
import com.mcd.rwd.global.core.bean.VideoProps;
import com.mcd.rwd.global.core.bean.VideoSettings;
import com.mcd.rwd.global.core.bean.YouTubeSettings;
import com.mcd.rwd.global.core.components.common.Image;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.utils.LinkUtil;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.global.core.widget.colorpicker.ColorPicker;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rakesh.Balaiah on 25-07-2015.
 */

@Component(
		name = "video",
		value = "Video",
		path = "content",
		description = "Videos can be embedded on the page with different layout options using this component. " +
				"Videos can also be sourced from Youtube.",
		allowedParents = {"*/parsys"},
		resourceSuperType = "foundation/components/parbase",
		group = " GWS-Global",
		actions = {"text: Video" ,"-","edit","copymove","delete"},
		disableTargeting = true,
		listeners = {
				@Listener(name = "afteredit", value = "REFRESH_PAGE"),
				@Listener(name = "beforesubmit", value = "function(dlg) {    var type = dlg.getField('./type').getValue();    var src = dlg.getField('./src').getValue();    if ((type == 'thumbnail' || type == 'normal') && src.length > 0) {        if(!dlg.getField('./ytsrc').getValue()) {            CQ.Ext.Msg.alert(\"Sorry!!\", \"YouTube source is mandatory\");            return false;        } else if(!(dlg.getField('./image/file').hasReferencedData() || dlg.getField('./image/file').hasUploadedData()) && type == 'thumbnail') {CQ.Ext.Msg.alert(\"Sorry!!\", \"Fallback image is mandatory for lightbox video\");return false;}    } else if (!(dlg.getField('./mp4/file').hasReferencedData() || dlg.getField('./mp4/file').hasUploadedData())) {        CQ.Ext.Msg.alert(\"Sorry!!\", \"MP4 video is mandatory\");        return false;    } else if(!(dlg.getField('./image/file').hasReferencedData() || dlg.getField('./image/file').hasUploadedData()) && src.length == 0 && type != 'background') {CQ.Ext.Msg.alert(\"Sorry!!\", \"Fallback image is mandatory\");return false;}}"),
				//@Listener(name = "", value = "")
		},
		tabs = {
				@Tab(title = "Generic", touchUINodeName = "tab1"),
				@Tab(title = "Upload", touchUINodeName = "tab2"),
				@Tab(title = "YouTube", touchUINodeName = "tab3"),
				@Tab(title = "Title", touchUINodeName = "tab4"),
				@Tab(title = "Fallback Image", touchUINodeName = "tab5")
		}
)
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class VideoHandler {

	/* The Default Logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(VideoHandler.class);

	private static final String VIDEO_NORMAL = "normal";

	private static final String VIDEO_BG = "background";

	private static final String VIDEO_THUMBNAIL = "thumbnail";
    private static final String PN_PULL_RIGHT = "pullRight";

    private String videoSrc;

	private String webmSrc;

	private String oggSrc;

	private Options options;

	private boolean isBackground;

	private boolean isThumbnail;

	private boolean useYouTube;

	private String youtubeSrc;

	private String poster;

	private boolean verticalAlign;

	private String id;

    private String pageTitle;

	@DialogField(name = "./type", fieldLabel = "Video Type", fieldDescription = "Please select the video type.",
			listeners = {
				@Listener(name = "selectionchanged", value = "function(box, value) {    var dlg = box.findParentByType('dialog');    var srcField = dlg.getField('./src');var autoPlayField = dlg.getField('./autoplay');var loopField = dlg.getField('./loop');var tabpanel = box.findParentByType('tabpanel');if(value == 'background') {        srcField.setValue(false);        srcField.disable();autoPlayField.setValue(false);        autoPlayField.disable();loopField.setValue(false);        loopField.disable();dlg.getField('./pullRight').setValue(false);dlg.getField('./pullRight').disable();dlg.getField('./align').disable();dlg.getField('./share').disable();tabpanel.hideTabStripItem('fallback-image-tab');} else if (value == 'thumbnail') {        srcField.setValue(dlg.previousState);        srcField.enable();autoPlayField.setValue(false);        autoPlayField.disable();loopField.setValue(false);        loopField.disable();if(dlg.getField('./align').getValue() == 'h') {dlg.getField('./pullRight').setValue(dlg.previousMove);dlg.getField('./pullRight').enable();}dlg.getField('./align').enable();dlg.getField('./share').enable();tabpanel.unhideTabStripItem('fallback-image-tab');} else {        srcField.setValue(dlg.previousState);        srcField.enable();autoPlayField.setValue(dlg.previousAutoPlayState);        autoPlayField.enable();loopField.setValue(dlg.previousLoopState);        loopField.enable();dlg.getField('./pullRight').setValue(false);dlg.getField('./pullRight').disable();dlg.getField('./align').disable();dlg.getField('./share').enable();if(srcField.getValue().length > 0) {tabpanel.hideTabStripItem('fallback-image-tab');}else {tabpanel.unhideTabStripItem('fallback-image-tab');}}}")
			})
	@Selection(type = Selection.SELECT, options = {
			@Option(text = "Embedded Video", value = "normal"),
			@Option(text = "Background Video", value = "background"),
			@Option(text = "Lightbox Video", value = "thumbnail")
	})
	@Inject @Named("type") @Default(values = "normal")
	private String videoType;

	@DialogField(fieldLabel = "Align", listeners = {
			@Listener(name = "selectionchanged", value = "function(box, value) {    var dlg = box.findParentByType('dialog');    if(value == 'v') {        dlg.getField('./pullRight').setValue(false);dlg.getField('./pullRight').disable();} else {        dlg.getField('./pullRight').setValue(dlg.previousMove);dlg.getField('./pullRight').enable();}}")
	})
	@Selection(type = Selection.SELECT, options = {
			@Option(text = "Horizontal", value = "h"), @Option(text = "Vertical", value = "v")
	})
	@Inject @Default(values = "v")
	private String align;

	@DialogField(additionalProperties = @Property(name = "value", value = "true"))
	@CheckBox(text = "Move video in right side")
	@Inject @Default(booleanValues = false)
	private boolean pullRight;

	@DialogField(additionalProperties = @Property(name = "value", value = "true"), listeners = {
			@Listener(name = "selectionchanged", value = "function(comp, val, isChecked) {    var tabpanel = comp.findParentByType('tabpanel');    var dlg = comp.findParentByType('dialog');    if(dlg.getField('./type').getValue() != 'background') {        dlg.previousState = isChecked;    }        if(isChecked) {        tabpanel.hideTabStripItem('video-upload-tab');        tabpanel.unhideTabStripItem('youtube-tab');tabpanel.hideTabStripItem('fallback-image-tab');    } else {        tabpanel.hideTabStripItem('youtube-tab');        tabpanel.unhideTabStripItem('video-upload-tab');tabpanel.unhideTabStripItem('fallback-image-tab');    }if(dlg.getField('./type').getValue() == 'thumbnail') {        tabpanel.unhideTabStripItem('fallback-image-tab');    }}")
	})
	@CheckBox(text = "Is the Video Source from YouTube?")
	@Inject @Default(booleanValues = false)
	private boolean src;

	@DialogField
	@DialogFieldSet(collapsible = true, collapsed = true, title = "Video Settings for all videos")
	@Inject
	private VideoProps videoProps;

	@DialogField(tab = 2, fieldLabel = "MP4 Video*", name = "./mp4/file", additionalProperties = {
			@Property(name = "mimeTypes", value = "video/mp4"),
			@Property(name = "fileNameParameter", value = "./mp4/fileName"),
			@Property(name = "fileReferenceParameter", value = "./mp4/fileReference")
	})
	@Html5SmartFile(ddAccept = "video/mp4", ddGroups = "media", sizeLimit = 1000, fileNameParameter = "./mp4/fileName",
			fileReferenceParameter = "./mp4/fileReference", mimeTypes = "*.*")
	@ChildResource(name = "mp4")
	@Inject @Named("fileReference")
	private String mp4;

	@DialogField(tab = 2)
	@DialogFieldSet(title = "Optional Formats", collapsed = true, collapsible = true)
	@Inject
	private VideoFormats videoFormats;

	@DialogField(tab = 2)
	@DialogFieldSet(collapsible = true, title = "Additional Video Settings")
	@Inject
	private VideoSettings videoSettings;

	@DialogField(tab = 3, fieldLabel = "YouTube Source*")
	@TextField
	@Inject
	private String ytsrc;

	@DialogField(tab = 3)
	@DialogFieldSet(collapsible=true, title="Additional YouTube Video settings")
	private YouTubeSettings youTubeSettings;

	@DialogField(tab = 4, fieldLabel = "Title")
	@TextField
	@Inject
	private String title;

	@DialogField(tab = 4, fieldLabel = "Description")
	@TextArea
	@Inject
	private String description;

	@DialogField(tab = 4, fieldLabel = "Call to Action Text")
	@TextField
	@Inject
	private String cta;
	@DialogField(tab = 4, fieldLabel = "Call to action Link")
	@PathField
	@Inject
	private String ctaTarget;

	@DialogField(tab = 4, fieldLabel = "Cta Title Text")
	@TextField
	@Inject
	private String ctaTitle;

	@DialogField(tab = 4, fieldLabel = "Text Color")
	@ColorPicker
	@Inject
	private String color;

	@DialogField(tab = 5)
	@DialogFieldSet(namePrefix = "image/")
	@ChildResource(name = "image")
	@Inject
	private Image image;

    @DialogField(name = "verticalspace", tab = 4, fieldLabel = "Vertical Video Space")
    @NumberField(allowDecimals = false)
    @Inject
    @Named("verticalspace")
    private String verticalVideoSpace;

	@DialogField(name = "./textAlign", tab = 4, value = "left",
			fieldLabel = "Video Text Alignment")
	@Selection(type = Selection.RADIO,
			options = {
					@Option(text="Left" , value="left"),
					@Option(text="Center" , value="center"),
					@Option(text="Right" , value="right")
			})
	@Inject @Named("textAlign")
	@Default(values = "left")
    private String videoTextAlign;


    @DialogField(fieldDescription = "This will align the text in both Desktop and mobile. Default behaviour is Center align", tab = 4)
    @CheckBox(text = "Enable Align Text")
    @Inject @Default(booleanValues = false)
    private boolean maintainAlign;

    @DialogField(name = "textbottompace", tab = 4, fieldLabel = "Vertical Video Space")
    @NumberField(allowDecimals = false)
    @Inject
    @Named("textbottompace")
    private String textbottompace;

    @DialogField(fieldLabel = "Aria Label for Play Button", tab = 4)
    @TextField
    @Inject
    private String ariaLabel;

	@Inject
	@Default(booleanValues = true)
	private boolean controls;

	@Inject @Named("fs")
	@Default(booleanValues = true)
	private boolean allowFullScreen;

	@Inject @Named("hd")
	private String videoQuality;

	@Inject @Default(booleanValues = false)
	private boolean related;

	@Inject
	@Default(booleanValues = false)
	private boolean autoplay;

	@Inject @Default(booleanValues = false)
	private boolean loop;

	@Inject
	@Default(booleanValues = false)
	private boolean preload;

	@Inject @Default(booleanValues = false)
	private boolean muted;

	@Inject
	private String[] share;

	@ChildResource(name = "ogg")
	@Inject @Named("fileReference")
	private String ogg;

	@ChildResource(name = "webm")
	@Inject
	@Named("fileReference")
	private String webm;

	@Inject
	SlingHttpServletRequest request;

	@Inject
	Page currentPage;

	@Inject
	@Via("request")
	Resource resource;


	@PostConstruct
	public void activate() {

		LOGGER.debug("activate() method called");


		this.pageTitle = PageUtil.getPageNameForAnalytics(currentPage, request);

		LOGGER.debug("get the dialog property and set into the variables");

		this.isBackground = VIDEO_BG.equals(videoType);
		this.isThumbnail = VIDEO_THUMBNAIL.equals(videoType);
		this.poster = null != image ? image.getImagePath() : null;

		if (!isBackground && src) {
			this.useYouTube = true;
			youtubeSrc = getEmbedUrl(ytsrc);
			options = new Options();
			options.fullScreen = allowFullScreen;
		} else {
			this.videoSrc = LinkUtil.getDownloadHref(resource, ApplicationConstants.VIDEO_TYPE_MP4);
			this.webmSrc = LinkUtil.getDownloadHref(resource, ApplicationConstants.VIDEO_TYPE_WEBM);
			this.oggSrc = LinkUtil.getDownloadHref(resource, ApplicationConstants.VIDEO_TYPE_OGG);

			if (isBackground) {
				setBackgroundOptions();
			} else {
				setVideoOptions();
			}
		}

		if (isThumbnail) {
			if ("v".equals(align)) {
				this.verticalAlign = true;
			}

			this.id = StringUtils.replace(resource.getPath(), currentPage.getContentResource().getPath(),
					StringUtils.EMPTY);
			this.id = StringUtils.replace(this.id, ApplicationConstants.PATH_SEPARATOR,
					ApplicationConstants.PATH_CONVERTER);
		}
	}

	/**
	 * Returns the YouTube embed URL.
	 *
	 * @param str The author provided URL.
	 * @return a valid embed URL if available.
	 */
	private String getEmbedUrl(final String str) {
		LOGGER.debug("getEmbedUrl() method called");
		StringBuilder embedURL = new StringBuilder();
		try {
			String videoId;
			if (str != null) {
				Pattern pattern = Pattern.compile("[\\?\\&]v=([^\\?\\&]+)");
				Matcher matcher = pattern.matcher(str);

				if (matcher.find() && matcher.groupCount() > 0) {
					videoId = matcher.group(1);
                    videoId = getVideoId(videoId);
                } else if (str.indexOf(ApplicationConstants.QS_START_CHAR) != -1) {
					videoId = str.substring(str.lastIndexOf('/') + 1,
							str.indexOf(ApplicationConstants.QS_START_CHAR));
				} else {
					videoId = str.substring(str.lastIndexOf('/') + 1, str.length());
				}

				if (StringUtils.isNotBlank(videoId)) {
					embedURL.append("https://www.youtube.com/embed/");
					embedURL.append(videoId).append(ApplicationConstants.QS_START_CHAR);
					embedURL.append(getYoutubeOptions(resource.getValueMap(), videoId));
					LOGGER.debug("method getEmbedUrl(final String str): return embelURL: "+embedURL.toString());
					return embedURL.toString();
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception while generating YouTube Embed URL", e);
		}
		LOGGER.debug("method getEmbedUrl(final String str): return null");
		return null;
	}

    private String getVideoId(String videoId) {
        LOGGER.debug("getVideoId(String videoId) called");
        return (videoId.indexOf(ApplicationConstants.URL_HASH) >= 0) ? videoId.substring(0, videoId.indexOf(ApplicationConstants.URL_HASH)) : videoId;
    }

    /**
	 * Returns the background video options.
	 *
	 * @return
	 */
	private void setBackgroundOptions() {
		LOGGER.debug("setBackgroundOptions() method called");
		options = new Options();
		options.attributes.put(ApplicationConstants.VIDEO_OPTS_AUTOPLAY, ApplicationConstants.FLAG_TRUE);
		options.attributes.put(ApplicationConstants.VIDEO_OPTS_MUTED, ApplicationConstants.FLAG_TRUE);
		options.attributes.put(ApplicationConstants.VIDEO_OPTS_LOOP, ApplicationConstants.FLAG_TRUE);
	}

	/**
	 * Returns the Uploaded Video Options.
	 *
	 * @param
	 * @return
	 */
	private void setVideoOptions() {
		LOGGER.debug("setVideoOptions(final ValueMap map) method called");
		options = new Options();
		if (muted) {
			options.attributes.put(ApplicationConstants.VIDEO_OPTS_MUTED, ApplicationConstants.FLAG_TRUE);
		}

		if (preload) {
			options.attributes.put(ApplicationConstants.VIDEO_OPTS_PRELOAD, ApplicationConstants.FLAG_TRUE);
		}

		if (loop) {
			options.attributes.put(ApplicationConstants.VIDEO_OPTS_LOOP, ApplicationConstants.FLAG_TRUE);
		}

		if (autoplay) {
			options.attributes.put(ApplicationConstants.VIDEO_OPTS_AUTOPLAY, ApplicationConstants.FLAG_TRUE);
		}

		/*String[] channels = map.get("share", String[].class);*/

		if (share != null && share.length > 0) {
			for (int i = 0; i < share.length; i++) {
				if (StringUtils.isNotBlank(share[i])) {
					String[] channel = share[i].split("=");
					options.socialChannels.put(channel[0], channel[1]);
				}
			}
		}
	}

	/**
	 * Returns the youtube video options in a query string.
	 *
	 * @param map
	 * @return
	 */
	private String getYoutubeOptions(ValueMap map, String videoId) {

		LOGGER.debug("getYoutubeOptions(ValueMap map, String videoId) method called");
		if (map == null || map.size() == 0) return StringUtils.EMPTY;
		StringBuilder builder = new StringBuilder("vq=");
		builder.append(map.get("vq", "large"));
		builder.append(ApplicationConstants.QS_DELIMITER);

		if (map.get(ApplicationConstants.VIDEO_OPTS_AUTOPLAY, false)) {
			builder.append("autoplay=1&");
		}

		if (!map.get("related", false)) {
			builder.append("rel=0&");
		}

		if (map.get(ApplicationConstants.VIDEO_OPTS_LOOP, false)) {
			builder.append("playlist=").append(videoId).append("&loop=1&");
		}

		if (!map.get("fs", false)) {
			builder.append("fs=0&");
		}

		if (!map.get(ApplicationConstants.VIDEO_OPTS_CONTROLS, false)) {
			builder.append("controls=0");
		}

		return builder.toString();
	}

	/**
	 * Getter for webm source.
	 *
	 * @return webmSrc.
	 */
	public String getWebmSrc() {
		return webmSrc;
	}

	/**
	 * Getter for ogg source.
	 *
	 * @return oggSrc.
	 */
	public String getOggSrc() {
		return oggSrc;
	}

	/**
	 * Getter for mp4 source.
	 *
	 * @return videoSrc.
	 */
	public String getVideoSrc() {
		return videoSrc;
	}

	/**
	 * Getter for Video Options.
	 *
	 * @return options.
	 */
	public Options getOptions() {
		return options;
	}

	/**
	 * Getter for background check.
	 *
	 * @return isBackground.
	 */
	public boolean isBackground() {
		return isBackground;
	}

	/**
	 * Getter for thumbnail check.
	 *
	 * @return isThumbnail.
	 */
	public boolean isThumbnail() {
		return isThumbnail;
	}

    public boolean isPullRight() {
        return pullRight;
    }

    /**
	 * Getter for youtube source.
	 *
	 * @return youtubeSrc.
	 */
	public String getYoutubeSrc() {
		return youtubeSrc;
	}

	/**
	 * Getter for Poster Image.
	 *
	 * @return poster.
	 */
	public String getPoster() {
		return poster;
	}

	/**
	 * Getter for Title.
	 *
	 * @return title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Getter for Description.
	 *
	 * @return description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Getter for Call to Action.
	 *
	 * @return cta.
	 */
	public String getCta() {
		return cta;
	}

	/**
	 * Getter for CTA Target.
	 *
	 * @return ctaTarget.
	 */
	public String getCtaTarget() {
		return ctaTarget;
	}

	public String getCtaTitle() {
		return ctaTitle;
	}

	/**
	 * Getter for vertical align check.
	 *
	 * @return verticalAlign.
	 */
	public boolean isVerticalAlign() {
		return verticalAlign;
	}

	/**
	 * Getter for Video ID.
	 *
	 * @return id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Getter for Title Color.
	 *
	 * @return color.
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Getter for Use YouTube
	 */
	public boolean isUseYouTube() {
		return useYouTube;
	}

	public static final class Options {

		private boolean fullScreen;

		private Map<String, String> socialChannels = new LinkedHashMap<String, String>();

		private String width;

		private Map<String, String> attributes = new HashMap<String, String>();

		private Options() {
			//There is no property named width in video component resource, so using constant value itself.
			//String videoWidth = map.get(ApplicationConstants.PN_WIDTH, ApplicationConstants.MAX_DIMENSION);
			//this.width = videoWidth + "%";
			this.width = ApplicationConstants.MAX_DIMENSION + "%";
		}

		/**
		 * Getter for Width of the Video.
		 *
		 * @return width.
		 */
		public String getWidth() {
			return width;
		}

		/**
		 * Getter for Share Configuration.
		 *
		 * @return isShare.
		 */
		public Map<String, String> getSocialChannels() {
			return socialChannels;
		}

		/**
		 * Getter for Fullscreen Configuration.
		 *
		 * @return fullScreen.
		 */
		public boolean isFullScreen() {
			return fullScreen;
		}

		/**
		 * Getter for Attributes.
		 *
		 * @return attributes.
		 */
		public Map<String, String> getAttributes() {
			return attributes;
		}
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public String getVerticalVideoSpace() {
		return verticalVideoSpace;
	}

	public void setVerticalVideoSpace(String verticalVideoSpace) {
		this.verticalVideoSpace = verticalVideoSpace;
	}

	public String getVideoTextAlign() {
		return videoTextAlign;
	}

	public void setVideoTextAlign(String videoTextAlign) {
		this.videoTextAlign = videoTextAlign;
	}

	public boolean isMaintainAlign() {
		return maintainAlign;
	}

	public void setMaintainAlign(boolean maintainAlign) {
		this.maintainAlign = maintainAlign;
	}

	public String getTextbottompace() {
		return textbottompace;
	}

	public void setTextbottompace(String textbottompace) {
		this.textbottompace = textbottompace;
	}

	public String getAriaLabel() {
		return ariaLabel;
	}

}
