package com.mcd.rwd.wifi.core.bean;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Option;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.day.cq.dam.api.DamConstants;
import com.mcd.rwd.global.core.widget.colorpicker.ColorPicker;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import javax.inject.Named;

@Component(
		value = "Tile Bean Component",
		path = "content",
		disableTargeting = true,
		editConfig = false,
		group = ".hidden")
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TileComponentBean {

	@DialogField(name = "./defaultTitle", fieldLabel = "Tile Text", fieldDescription = "")
	@TextField
	@Inject @Named("defaultTitle")
	private String title;

	@DialogField(name = "./defaultColorpicker", fieldLabel = "Text Color", fieldDescription = "Choose the color code" +
			" for title. Note: Default colour is '#FFFFFF' (White).", xtype = "colorfield", additionalProperties =
	@Property(name = "showHexValue", value = "true"))
	@ColorPicker
	@Inject @Named("defaultColorpicker")
	private String colorpicker;

	@DialogField(name = "./defaultTextAlign", fieldLabel = "Text Align", fieldDescription = "Choose an option for " +
			"text alignment of title. Note : Default alignment is 'Top'", additionalProperties =
	@Property(name = "emptyOption", value = "true"))
	@Selection(type = Selection.SELECT, options = {
			@Option(text = "Top", value = "top"), @Option(text = "Top Left", value = "topLeft"),
			@Option(text = "Top Right", value = "topRight"), @Option(text = "Middle", value = "middle"),
			@Option(text = "Middle Left", value = "middleLeft"), @Option(text = "Middle Right", value = "middleRight"),
			@Option(text = "Bottom", value = "bottom"), @Option(text = "Bottom Left", value = "bottomLeft"),
			@Option(text = "Bottom Right", value = "bottomRight")
	})
	@Inject @Named("defaultTextAlign")
	@Default(values = "top")
	private String textalign;

	@DialogField(name = "./defaultImagePath", fieldLabel = "Image Path", fieldDescription = "Select the image path " +
			"from DAM", required = true, additionalProperties = {
			@Property(name = "regex", value = "new RegExp('content/(.*)?(?:jpe?g|gif|png)$')"),
			@Property(name = "regexText", value = "Please provide an image with a valid extension i.e : jpeg|gif|png"),
			@Property(name = "class", value = "tile-imagePath")
	})
	@PathField(rootPath = DamConstants.MOUNTPOINT_ASSETS, showTitleInTree = false, rootTitle = "Image")
	@Inject @Named("defaultImagePath")
	private String imagePath;

	@DialogField(name = "./defaultAltText", fieldLabel = "Image Alt Text", fieldDescription = "Enter image alt text",
			required = true)
	@TextField
	@Inject @Named("defaultAltText")
	private String alttext;

	@DialogField(name = "./defaultImageUrl", fieldLabel = "Image Action Link", fieldDescription = "Enter image action" +
			" link for redirect")
	@PathField
	@Inject @Named("defaultImageUrl")
	private String imageurl;

	@DialogField(name = "./daypartType", fieldLabel = "Daypart", fieldDescription = "Choose an option to display tile based " +
			"on daypart. Note : In case of Default rest of the options will not work.", required = true,
			additionalProperties = { @Property(name = "emptyOption", value = "true"),
					@Property(name = "class", value = "daypartType") })
	@Selection(type = Selection.SELECT, options = { @Option(text = "Morning (6:00 - 11:59 AM)", value = "morning"),
			@Option(text = "Afternoon (12:00 - 5:59 PM)", value = "afternoon"),
			@Option(text = "Evening (6:00 - 11 PM)", value = "evening")
	})
	@Named("daypartType")
	@Inject @Default(values = "default")
	private String daypartType;

	@DialogField(name = "./defaultTarget", fieldLabel = "Target", fieldDescription = "Choose a target to open " +
			"image link. Note: Default target is 'Modal'.", additionalProperties =
	@Property(name = "emptyOption", value = "true"))
	@Selection(type = Selection.SELECT, options = { @Option(text = "Modal", value = "modal"),
			@Option(text = "New Window", value = "new"), @Option(text = "Same Window", value = "same")
	})
	@Inject @Named("defaultTarget")
	@Default(values = "modal")
	private String target;

	public String getTitle() {
		return title;
	}

	public String getColorpicker() {
		return colorpicker;
	}

	public String getTextalign() {
		return textalign;
	}

	public String getImagePath() {
		return imagePath;
	}

	public String getAlttext() {
		return alttext;
	}

	public String getImageurl() {
		return imageurl;
	}

	public String getDaypartType() {
		return daypartType;
	}

	public String getTarget() {
		return target;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
}
