package com.mcd.rwd.wifi.core.bean;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Option;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.mcd.rwd.global.core.widget.colorpicker.ColorPicker;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.Resource;
import javax.inject.Inject;

@Model(adaptables = { Resource.class ,SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MainSliderBean {

	@DialogField( fieldLabel = "Background Element", required = true , fieldDescription = "Select a background element." , additionalProperties = {
			@Property(name = "class" , value = "wifi_slider_background_element")})
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Image" , value="image"),
					@Option(text="Video" , value="video")
			})
	@Inject
	String bgElement;

	@DialogField(fieldLabel="Background Image*",additionalProperties =
			{@Property(name = "regexText", value = "Please provide an image with a valid extension i.e : jpeg|gif|png"),@Property(name = "regex", value = "content/(.*)?(?:jpe?g|gif|png)$") , @Property(name = "width", value = "335") , @Property(name = "class" , value = "wifi_slider_background_image")})
	@PathField(rootPath = "/content/dam")
	@Inject
	String bgImage;

	@DialogField( fieldLabel="Background Image Alt Text*" , additionalProperties =
			{@Property(name = "emptyText", value = "Enter alt text for background image."), @Property(name = "width", value = "335") , @Property(name = "maxLengthText" , value = "A maximum of 20 characters is allowed for the Title.") , @Property(name = "class" , value = "wifi_slider_background_image_alt_text")} )
	@TextField
	@Inject
	String bgImageAltText;

	@DialogField(fieldLabel="Background Video*", additionalProperties =
			{@Property(name = "regexText", value = "Please provide a video with a valid extension i.e : webm|ogg|mp4"), @Property(name = "regex", value = "content/(.*)?(?:webm|WEBM|ogg|OGG|mp4|MP4)$") , @Property(name = "width", value = "335") , @Property(name = "editable", value = "false") , @Property(name = "class" , value = "wifi_slider_background_video")})
	@PathField(rootPath = "/content/dam")
	@Inject
	String bgVideo;

	@DialogField(fieldLabel="Tile Image", required = true , additionalProperties =
			{@Property(name = "regexText", value = "Please provide an image with a valid extension i.e : jpeg|gif|png"),@Property(name = "regex", value = "content/(.*)?(?:jpe?g|gif|png)$") , @Property(name = "width", value = "335") ,@Property(name = "emptyText", value = "Select an image for tile.") })
	@PathField(rootPath = "/content/dam")
	@Inject
	String tileImageLinke;

	@DialogField( fieldLabel="Image Alt Text" ,required = true , additionalProperties =
			{@Property(name = "emptyText", value = "Enter alt text for tile image."), @Property(name = "width", value = "335") , @Property(name = "maxLengthText" , value = "A maximum of 20 characters is allowed for the alt text.") , @Property(name = "maxLength", value = "20")} )
	@TextField
	@Inject
	String tileImageAltText;

	@DialogField( fieldLabel="Tile Image Text" , additionalProperties =
			{@Property(name = "emptyText", value = "Enter image text for tile image."), @Property(name = "width", value = "335") , @Property(name = "maxLengthText" , value = "A maximum of 25 characters is allowed for the tile image text.") , @Property(name = "maxLength", value = "25")} )
	@TextField
	@Inject
	String tileImageText;

	@DialogField(fieldLabel="URL", additionalProperties =
			{ @Property(name = "width", value = "335") ,@Property(name = "emptyText", value = "Select a hyperlink for tile.") , @Property(name = "class" , value = "wifi_slider_background_url") })
	@PathField()
	@Inject
	String tileImage;

	@DialogField( fieldLabel = "Target",  fieldDescription = "Choose a target to open image/video link. Note: Default target is Modal.." , additionalProperties ={
			@Property(name = "class" , value = "wifi_slider_background_url_target")
	})
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Modal" , value="modal"),
					@Option(text="New Window" , value="new"),
					@Option(text="Same Window" , value="same")
			})
	@Inject
	String target;

	@DialogField( fieldLabel="Title" , additionalProperties =
			{@Property(name = "emptyText", value = "Enter Title"), @Property(name = "width", value = "335") , @Property(name = "maxLengthText" , value = "A maximum of 50 characters is allowed for the Title.") , @Property(name = "maxLength", value = "50") , @Property(name = "class" , value = "wifi_slider_background_url_title")} )
	@TextField
	@Inject
	String title;

	@DialogField( fieldLabel="Sub Title" , additionalProperties =
			{@Property(name = "emptyText", value = "Enter Sub-Title."), @Property(name = "width", value = "335") , @Property(name = "maxLengthText" , value = "A maximum of 100 characters is allowed for the Sub Title.") , @Property(name = "maxLength", value = "100") ,  @Property(name = "class" , value = "wifi_slider_background_url_subTitle")} )
	@TextField
	@Inject
	String subTitle;

	@DialogField( fieldLabel = "Text alignment",  fieldDescription = "Default alignment is Bottom." )
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Top" , value="top"),
					@Option(text="Top Left" , value="topLeft"),
					@Option(text="Top Right" , value="topRight"),
					@Option(text="Middle" , value="middle"),
					@Option(text="Middle Left" , value="middleLeft"),
					@Option(text="Middle Right" , value="middleRight"),
					@Option(text="Bottom" , value="Bottom"),
					@Option(text="Bottom Left" , value="BottomLeft"),
					@Option(text="Bottom Right" , value="bottomRight")
			})
	@Inject
	String textAlign;

	@DialogField( fieldDescription="Choose the color code for title and subtitle. Note: Default colour is \"#FFFFFF\" (White).",fieldLabel="Color" ,additionalProperties =
			{ @Property(name = "width", value = "175")})
	@ColorPicker
	@Default(values = "FFFFFF")
	@Inject
	String colorPicker;


	@DialogField( fieldLabel = "Video Type",  fieldDescription = "Choose a video type which you want to open." , additionalProperties = {
			@Property(name = "class" , value = "wifi_slider_background_video_type")})
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Standard" , value="standard"),
					@Option(text="Youtube" , value="youtube")
			})
	@Inject
	String videoType;


	@DialogField( fieldLabel="Light Box Video" , fieldDescription = "Select the popup video path from Digital Assets.", additionalProperties =
			{ @Property(name = "width", value = "335") , @Property(name = "regexText" , value = "Please provide a video with a valid extension i.e : webm|ogg|mp4") , @Property(name = "regex", value = "content/(.*)?(?:webm|WEBM|ogg|OGG|mp4|MP4)$") , @Property(name = "class" , value = "wifi_slider_background_light_box")} )
	@PathField(rootPath = "/content/dam")
	@Inject
	String popupVideoPath;

	@DialogField(fieldLabel="Youtube Video", fieldDescription = "Please provide an embed youtube video url.",additionalProperties =
			{ @Property(name = "width", value = "335")  , @Property(name = "regex",value = "(https?\\:\\/\\/)?((www\\.)youtube\\.com|youtu\\.?be)\\/embed\\/.+$") , @Property(name = "class" , value = "wifi_slider_background_youtube") })
	@TextField
	@Inject
	String youtubeVideo;

	public String getBgElement() {
		return bgElement;
	}

	public void setBgElement(String bgElement) {
		this.bgElement = bgElement;
	}

	public String getBgImage() {
		return bgImage;
	}

	public void setBgImage(String bgImage) {
		this.bgImage = bgImage;
	}

	public String getBgImageAltText() {
		return bgImageAltText;
	}

	public void setBgImageAltText(String bgImageAltText) {
		this.bgImageAltText = bgImageAltText;
	}

	public String getBgVideo() {
		return bgVideo;
	}

	public void setBgVideo(String bgVideo) {
		this.bgVideo = bgVideo;
	}

	public String getTileImageLinke() {
		return tileImageLinke;
	}

	public void setTileImageLinke(String tileImageLinke) {
		this.tileImageLinke = tileImageLinke;
	}

	public String getTileImageAltText() {
		return tileImageAltText;
	}

	public void setTileImageAltText(String tileImageAltText) {
		this.tileImageAltText = tileImageAltText;
	}

	public String getTileImageText() {
		return tileImageText;
	}

	public void setTileImageText(String tileImageText) {
		this.tileImageText = tileImageText;
	}

	public String getTileImage() {
		return tileImage;
	}

	public void setTileImage(String tileImage) {
		this.tileImage = tileImage;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getTextAlign() {
		return textAlign;
	}

	public void setTextAlign(String textAlign) {
		this.textAlign = textAlign;
	}

	public String getColorPicker() {
		return colorPicker;
	}

	public void setColorPicker(String colorPicker) {
		this.colorPicker = colorPicker;
	}

	public String getVideoType() {
		return videoType;
	}

	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}

	public String getPopupVideoPath() {
		return popupVideoPath;
	}

	public void setPopupVideoPath(String popupVideoPath) {
		this.popupVideoPath = popupVideoPath;
	}

	public String getYoutubeVideo() {
		return youtubeVideo;
	}

	public void setYoutubeVideo(String youtubeVideo) {
		this.youtubeVideo = youtubeVideo;
	}
}
