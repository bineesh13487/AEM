package com.mcd.rwd.global.core.bean;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Option;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.widgets.Html5SmartImage;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.mcd.rwd.global.core.widget.colorpicker.ColorPicker;
import com.mcd.rwd.global.core.widget.timefield.TimeField;
import com.mcd.rwd.global.core.widget.timefield.Type;

public class FeatureCallout {


	@DialogField(fieldLabel = "Image")
	@Html5SmartImage(tab = false)
	private String imagePath;

	@DialogField(fieldLabel = "Link", fieldDescription = "Please choose or enter any URL for the callout.")
	@PathField
	private String linkPath;

	@DialogField(fieldLabel = "Text", fieldDescription = "Please provide the text to be displayed over" +
			" the callout image.")
	@TextField
	private String overlayText;

	@DialogField(fieldLabel = "Text Position", fieldDescription = "Please choose the horizontal alignment of " +
			"text over image.", additionalProperties = @Property(name = "emptyOption", value = "true"))
	@Selection(type = Selection.RADIO, options = { @Option(text = "Left", value = "left"),
			@Option(text = "Center", value = "center"),
			@Option(text = "Right", value = "right")})
	private String overlayPosition;

	@DialogField(fieldLabel = "Text Color")
	@ColorPicker
	private String overlayColor;

	@DialogField(fieldLabel = "Alt Text")
	@TextField
	private String imageAlt;

	@DialogField(fieldLabel = "Start Time", fieldDescription = "Please choose the start time for this " +
			"callout to display.", additionalProperties = @Property(name = "class", value = "feature-startTime"))
	@TimeField(type = Type.TIME)
	private String startTime;

	@DialogField(fieldLabel = "End Time", fieldDescription = "Please choose the end time for this callout. " +
			"The end time should always be greater than the start time.", additionalProperties =
			@Property(name = "class", value = "feature-endTime"))
	@TimeField(type = Type.TIME)
	private String endTime;

	private Link link;
	
	private String target;

	/**
	 * @return the imagePath
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * @param imagePath the imagePath to set
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	/**
	 * @return the overlayPosition
	 */
	public String getOverlayPosition() {
		return overlayPosition;
	}

	/**
	 * @param overlayPosition the overlayPosition to set
	 */
	public void setOverlayPosition(String overlayPosition) {
		this.overlayPosition = overlayPosition;
	}

	/**
	 * @return the overlayColor
	 */
	public String getOverlayColor() {
		return overlayColor;
	}

	/**
	 * @param overlayColor the overlayColor to set
	 */
	public void setOverlayColor(String overlayColor) {
		this.overlayColor = overlayColor;
	}

	/**
	 * @return the imageAlt
	 */
	public String getImageAlt() {
		return imageAlt;
	}

	/**
	 * @param imageAlt the imageAlt to set
	 */
	public void setImageAlt(String imageAlt) {
		this.imageAlt = imageAlt;
	}

	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setLink(Link link) {
		this.link = link;
	}

	public Link getLink() {
		return link;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
}
