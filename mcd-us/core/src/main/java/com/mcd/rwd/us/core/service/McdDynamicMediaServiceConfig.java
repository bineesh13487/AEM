package com.mcd.rwd.us.core.service;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.commons.util.DynamicMediaHelper;
import com.mcd.rwd.us.core.constants.ApplicationConstants;

@Component(label = "McD Dynamic Media Configurations", immediate = true, metatype = true, description = "Configurations for Dynamic Media and Util functions")
@Service(McdDynamicMediaServiceConfig.class)
@Properties({
		@Property(name = "mcd.factoryConfig.dynamicmedia.image.large", label = "Large image syntax", value = "", description = "Used to find large Image preset(1920 px)"),
		@Property(name = "mcd.factoryConfig.dynamicmedia.image.medium", label = "Medium image syntax", value = "", description = "Used to find Medium Image preset(1024 px)"),
		@Property(name = "mcd.factoryConfig.dynamicmedia.image.small", label = "Small image syntax", value = "", description = "Used to find Small Image preset(767 px)"),
		@Property(name = "mcd.factoryConfig.dynamicmedia.image.xsmall", label = "Extra small image syntax", value = "", description = "Used to find Extra small Image preset(432 px)"),
		@Property(name = "mcd.factoryConfig.dynamicmedia.image.herotype", label = "Type of Image Hero", value = "", description = "Used to define Hero Image"),
		@Property(name = "mcd.factoryConfig.dynamicmedia.image.thumbnailtype", label = "Type of Image Thumbnail", value = "", description = "Used to define Thumbnail Image"),
		@Property(name = "mcd.factoryConfig.dynamicmedia.image.localoptiontype", label = "Type of Image LocalOption", value = "", description = "Used to define Localoption Image"),
		@Property(name = "mcd.factoryConfig.dynamicmedia.image.iconicthumbnailtype", label = "Type of Image IconicThumbnail", value = "", description = "Used to define Iconicthumbnail Image"),
		@Property(name = "mcd.factoryConfig.dynamicmedia.image.transparenttype", label = "Type of Image Transparent", value = "", description = "Used to define Transparent Image"),
		@Property(name = "mcd.factoryConfig.dynamicmedia.image.properties", label = "Dynamic Media Properties for images", value = "wid,hei,op_sharpen", description = "Properties to be used for Rendition for image"),
		@Property(name = "mcd.factoryConfig.dynamicmedia.server.url.appender", label = "Dynamic media image server url", value = "", description = "Used append the after context path"),
		@Property(name = "mcd.factoryConfig.dynamicmedia.server.enable", label = "Dynamic media server enable flag", value = "", description = "Used to enable and disable dynamic media progrmatically"),
		@Property(name = "mcd.factoryConfig.dynamicmedia.imagepreset.location", label = "Dynamic media image preset Location", value = "", description = "Used to get the image preset") })

public class McdDynamicMediaServiceConfig {

	private String largeImageSyntax;
	private String mediumImageSyntax;
	private String smallImageSyntax;
	private String xsmallImageSyntax;
	private String imageTypeHero;
	private String imageTypeThumbnail;
	private String imageTypeLocalOption;
	private String imageTypeIconicThumbnail;
	private String imageTypeTransparent;
	private String[] imageProperties;
	private String dynamicMediaImageServerappender;
	private String dynamicMediaImagePresetLocation;
	
	private String imagePresetHeroLarge = StringUtils.EMPTY;
	private String imagePresetHeroMedium = StringUtils.EMPTY;
	private String imagePresetHeroSmall = StringUtils.EMPTY;
	private String imagePresetHeroXSmall = StringUtils.EMPTY;
	private String imagePresetThumbnailLarge = StringUtils.EMPTY;
	private String imagePresetThumbnailMedium = StringUtils.EMPTY;
	private String imagePresetThumbnailSmall = StringUtils.EMPTY;
	private String imagePresetThumbnailXSmall = StringUtils.EMPTY;
	private String imagePresetIconicThumbnailLarge = StringUtils.EMPTY;
	private String imagePresetIconicThumbnailMedium =  StringUtils.EMPTY;
	private String imagePresetIconicThumbnailSmall = StringUtils.EMPTY;
	private String imagePresetIconicThumbnailXSmall = StringUtils.EMPTY;
	private String imagePresetLocalOptionLarge = StringUtils.EMPTY;
	private String imagePresetLocalOptionMedium = StringUtils.EMPTY;
	private String imagePresetLocalOptionSmall = StringUtils.EMPTY;
	private String imagePresetLocalOptionXSmall = StringUtils.EMPTY;
	private boolean dynamicMediaEnableFlag = Boolean.FALSE;
	

	private static final Logger LOGGER = LoggerFactory.getLogger(McdDynamicMediaServiceConfig.class);

	@Activate
	public void activate(ComponentContext componentContext) {
		@SuppressWarnings("rawtypes")
		Dictionary properties = componentContext.getProperties();
		setLargeImageSyntax(PropertiesUtil.toString(properties.get("mcd.factoryConfig.dynamicmedia.image.large"),
				ApplicationConstants.DYNAMIC_M_LARGE));
		setMediumImageSyntax(PropertiesUtil.toString(properties.get("mcd.factoryConfig.dynamicmedia.image.medium"),
				ApplicationConstants.DYNAMIC_M_MEDIUM));
		setSmallImageSyntax(PropertiesUtil.toString(properties.get("mcd.factoryConfig.dynamicmedia.image.small"),
				ApplicationConstants.DYNAMIC_M_SMALL));
		setXsmallImageSyntax(PropertiesUtil.toString(properties.get("mcd.factoryConfig.dynamicmedia.image.xsmall"),
				ApplicationConstants.DYNAMIC_M_XSMALL));
		setDynamicMediaImageServerappender(
				PropertiesUtil.toString(properties.get("mcd.factoryConfig.dynamicmedia.server.url.appender"),StringUtils.EMPTY));
		setDynamicMediaImagePresetLocation(
				PropertiesUtil.toString(properties.get("mcd.factoryConfig.dynamicmedia.imagepreset.location"),
						ApplicationConstants.DYNAMIC_M_IMAGEPRESET_PATH));
		setImageTypeHero(PropertiesUtil.toString(properties.get("mcd.factoryConfig.dynamicmedia.image.herotype"),
				ApplicationConstants.IMAGETYPE_HERO));
		setImageTypeThumbnail(
				PropertiesUtil.toString(properties.get("mcd.factoryConfig.dynamicmedia.image.thumbnailtype"),
						ApplicationConstants.IMAGETYPE_THUMBNAIL));

		setImageTypeLocalOption(
				PropertiesUtil.toString(properties.get("mcd.factoryConfig.dynamicmedia.image.localoptiontype"),
						ApplicationConstants.IMAGETYPE_LOCALOPTION));
		setImageTypeIconicThumbnail(
				PropertiesUtil.toString(properties.get("mcd.factoryConfig.dynamicmedia.image.iconicthumbnailtype"),
						ApplicationConstants.IMAGETYPE_ICONICTHUMNAIL));
		setImageTypeTransparent(
				PropertiesUtil.toString(properties.get("mcd.factoryConfig.dynamicmedia.image.transparenttype"),
						ApplicationConstants.IMAGETYPE_TRANSPARENT));
		setDynamicMediaEnableFlag(
                PropertiesUtil.toBoolean(properties.get("mcd.factoryConfig.dynamicmedia.server.enable"), true));
		if(StringUtils.isNotEmpty(getDynamicMediaImageServerappender())){
			setImagePresetHeroLarge(ApplicationConstants.DYNAMICMEDIA_PRESETSYMBOL+getImageTypeHero()+ApplicationConstants.UNDERSCORE+getLargeImageSyntax()+ApplicationConstants.DOLLAR_SIGN);
			setImagePresetHeroMedium(ApplicationConstants.DYNAMICMEDIA_PRESETSYMBOL+getImageTypeHero()+ApplicationConstants.UNDERSCORE+getMediumImageSyntax()+ApplicationConstants.DOLLAR_SIGN);
			setImagePresetHeroSmall(ApplicationConstants.DYNAMICMEDIA_PRESETSYMBOL+getImageTypeHero()+ApplicationConstants.UNDERSCORE+getSmallImageSyntax()+ApplicationConstants.DOLLAR_SIGN);
			setImagePresetHeroXSmall(ApplicationConstants.DYNAMICMEDIA_PRESETSYMBOL+getImageTypeHero()+ApplicationConstants.UNDERSCORE+getXsmallImageSyntax()+ApplicationConstants.DOLLAR_SIGN);
			setImagePresetThumbnailLarge(ApplicationConstants.DYNAMICMEDIA_PRESETSYMBOL+getImageTypeThumbnail()+ApplicationConstants.UNDERSCORE+getLargeImageSyntax()+ApplicationConstants.DOLLAR_SIGN);
			setImagePresetThumbnailMedium(ApplicationConstants.DYNAMICMEDIA_PRESETSYMBOL+getImageTypeThumbnail()+ApplicationConstants.UNDERSCORE+getMediumImageSyntax()+ApplicationConstants.DOLLAR_SIGN);
			setImagePresetThumbnailSmall(ApplicationConstants.DYNAMICMEDIA_PRESETSYMBOL+getImageTypeThumbnail()+ApplicationConstants.UNDERSCORE+getSmallImageSyntax()+ApplicationConstants.DOLLAR_SIGN);
			setImagePresetThumbnailXSmall(ApplicationConstants.DYNAMICMEDIA_PRESETSYMBOL+getImageTypeThumbnail()+ApplicationConstants.UNDERSCORE+getXsmallImageSyntax()+ApplicationConstants.DOLLAR_SIGN);
			setImagePresetIconicThumbnailLarge(ApplicationConstants.DYNAMICMEDIA_PRESETSYMBOL+getImageTypeIconicThumbnail()+ApplicationConstants.UNDERSCORE+getLargeImageSyntax()+ApplicationConstants.DOLLAR_SIGN);
			setImagePresetIconicThumbnailMedium(ApplicationConstants.DYNAMICMEDIA_PRESETSYMBOL+getImageTypeIconicThumbnail()+ApplicationConstants.UNDERSCORE+getMediumImageSyntax()+ApplicationConstants.DOLLAR_SIGN);
			setImagePresetIconicThumbnailSmall(ApplicationConstants.DYNAMICMEDIA_PRESETSYMBOL+getImageTypeIconicThumbnail()+ApplicationConstants.UNDERSCORE+getSmallImageSyntax()+ApplicationConstants.DOLLAR_SIGN);
			setImagePresetIconicThumbnailXSmall(ApplicationConstants.DYNAMICMEDIA_PRESETSYMBOL+getImageTypeIconicThumbnail()+ApplicationConstants.UNDERSCORE+getXsmallImageSyntax()+ApplicationConstants.DOLLAR_SIGN);
			setImagePresetLocalOptionLarge(ApplicationConstants.DYNAMICMEDIA_PRESETSYMBOL+getImageTypeLocalOption()+ApplicationConstants.UNDERSCORE+getLargeImageSyntax()+ApplicationConstants.DOLLAR_SIGN);
			setImagePresetLocalOptionMedium(ApplicationConstants.DYNAMICMEDIA_PRESETSYMBOL+getImageTypeLocalOption()+ApplicationConstants.UNDERSCORE+getMediumImageSyntax()+ApplicationConstants.DOLLAR_SIGN);
			setImagePresetLocalOptionSmall(ApplicationConstants.DYNAMICMEDIA_PRESETSYMBOL+getImageTypeLocalOption()+ApplicationConstants.UNDERSCORE+getSmallImageSyntax()+ApplicationConstants.DOLLAR_SIGN);
			setImagePresetLocalOptionXSmall(ApplicationConstants.DYNAMICMEDIA_PRESETSYMBOL+getImageTypeLocalOption()+ApplicationConstants.UNDERSCORE+getXsmallImageSyntax()+ApplicationConstants.DOLLAR_SIGN);
			
			   
			}
		

	}


	public String getLargeImageSyntax() {
		return largeImageSyntax;
	}

	public void setLargeImageSyntax(String largeImageSyntax) {
		this.largeImageSyntax = largeImageSyntax;
	}

	public String getMediumImageSyntax() {
		return mediumImageSyntax;
	}

	public void setMediumImageSyntax(String mediumImageSyntax) {
		this.mediumImageSyntax = mediumImageSyntax;
	}

	public String getSmallImageSyntax() {
		return smallImageSyntax;
	}

	public void setSmallImageSyntax(String smallImageSyntax) {
		this.smallImageSyntax = smallImageSyntax;
	}

	public String getXsmallImageSyntax() {
		return xsmallImageSyntax;
	}

	public void setXsmallImageSyntax(String xsmallImageSyntax) {
		this.xsmallImageSyntax = xsmallImageSyntax;
	}

	public String getDynamicMediaImageServerappender() {
		return this.dynamicMediaImageServerappender;
	}

	public void setDynamicMediaImageServerappender(String dynamicMediaImageServerappender) {
		this.dynamicMediaImageServerappender = dynamicMediaImageServerappender;
	}

	public String getImagePresetHeroLarge() {
		return imagePresetHeroLarge;
	}

	public void setImagePresetHeroLarge(String imagePresetHeroLarge) {
		this.imagePresetHeroLarge = imagePresetHeroLarge;
	}

	public String getImagePresetHeroMedium() {
		return imagePresetHeroMedium;
	}

	public void setImagePresetHeroMedium(String imagePresetHeroMedium) {
		this.imagePresetHeroMedium = imagePresetHeroMedium;
	}

	public String getImagePresetHeroSmall() {
		return imagePresetHeroSmall;
	}

	public void setImagePresetHeroSmall(String imagePresetHeroSmall) {
		this.imagePresetHeroSmall = imagePresetHeroSmall;
	}

	public String getImagePresetHeroXSmall() {
		return imagePresetHeroXSmall;
	}

	public void setImagePresetHeroXSmall(String imagePresetHeroXSmall) {
		this.imagePresetHeroXSmall = imagePresetHeroXSmall;
	}

	public String getImagePresetThumbnailLarge() {
		return imagePresetThumbnailLarge;
	}

	public void setImagePresetThumbnailLarge(String imagePresetThumbnailLarge) {
		this.imagePresetThumbnailLarge = imagePresetThumbnailLarge;
	}

	public String getImagePresetThumbnailMedium() {
		return imagePresetThumbnailMedium;
	}

	public void setImagePresetThumbnailMedium(String imagePresetThumbnailMedium) {
		this.imagePresetThumbnailMedium = imagePresetThumbnailMedium;
	}

	public String getImagePresetThumbnailSmall() {
		return imagePresetThumbnailSmall;
	}

	public void setImagePresetThumbnailSmall(String imagePresetThumbnailSmall) {
		this.imagePresetThumbnailSmall = imagePresetThumbnailSmall;
	}

	public String getImagePresetThumbnailXSmall() {
		return imagePresetThumbnailXSmall;
	}

	public void setImagePresetThumbnailXSmall(String imagePresetThumbnailXSmall) {
		this.imagePresetThumbnailXSmall = imagePresetThumbnailXSmall;
	}

	public String getImagePresetIconicThumbnailLarge() {
		return imagePresetIconicThumbnailLarge;
	}

	public void setImagePresetIconicThumbnailLarge(String imagePresetIconicThumbnailLarge) {
		this.imagePresetIconicThumbnailLarge = imagePresetIconicThumbnailLarge;
	}

	public String getImagePresetIconicThumbnailMedium() {
		return imagePresetIconicThumbnailMedium;
	}

	public void setImagePresetIconicThumbnailMedium(String imagePresetIconicThumbnailMedium) {
		this.imagePresetIconicThumbnailMedium = imagePresetIconicThumbnailMedium;
	}

	public String getImagePresetIconicThumbnailSmall() {
		return imagePresetIconicThumbnailSmall;
	}

	public void setImagePresetIconicThumbnailSmall(String imagePresetIconicThumbnailSmall) {
		this.imagePresetIconicThumbnailSmall = imagePresetIconicThumbnailSmall;
	}

	public String getImagePresetIconicThumbnailXSmall() {
		return imagePresetIconicThumbnailXSmall;
	}

	public void setImagePresetIconicThumbnailXSmall(String imagePresetIconicThumbnailXSmall) {
		this.imagePresetIconicThumbnailXSmall = imagePresetIconicThumbnailXSmall;
	}

	public String getImagePresetLocalOptionLarge() {
		return imagePresetLocalOptionLarge;
	}

	public void setImagePresetLocalOptionLarge(String imagePresetLocalOptionLarge) {
		this.imagePresetLocalOptionLarge = imagePresetLocalOptionLarge;
	}

	public String getImagePresetLocalOptionMedium() {
		return imagePresetLocalOptionMedium;
	}

	public void setImagePresetLocalOptionMedium(String imagePresetLocalOptionMedium) {
		this.imagePresetLocalOptionMedium = imagePresetLocalOptionMedium;
	}

	public String getImagePresetLocalOptionSmall() {
		return imagePresetLocalOptionSmall;
	}

	public void setImagePresetLocalOptionSmall(String imagePresetLocalOptionSmall) {
		this.imagePresetLocalOptionSmall = imagePresetLocalOptionSmall;
	}

	public String getImagePresetLocalOptionXSmall() {
		return imagePresetLocalOptionXSmall;
	}

	public void setImagePresetLocalOptionXSmall(String imagePresetLocalOptionXSmall) {
		this.imagePresetLocalOptionXSmall = imagePresetLocalOptionXSmall;
	}

	public String getDynamicMediaImagePresetLocation() {
		return dynamicMediaImagePresetLocation;
	}

	public void setDynamicMediaImagePresetLocation(String dynamicMediaImagePresetLocation) {
		this.dynamicMediaImagePresetLocation = dynamicMediaImagePresetLocation;
	}

	public String getImageTypeHero() {
		return imageTypeHero;
	}

	public void setImageTypeHero(String imageTypeHero) {
		this.imageTypeHero = imageTypeHero;
	}

	public String getImageTypeThumbnail() {
		return imageTypeThumbnail;
	}

	public void setImageTypeThumbnail(String imageTypeThumbnail) {
		this.imageTypeThumbnail = imageTypeThumbnail;
	}

	public String getImageTypeLocalOption() {
		return imageTypeLocalOption;
	}

	public void setImageTypeLocalOption(String imageTypeLocalOption) {
		this.imageTypeLocalOption = imageTypeLocalOption;
	}

	public String getImageTypeIconicThumbnail() {
		return imageTypeIconicThumbnail;
	}

	public void setImageTypeIconicThumbnail(String imageTypeIconicThumbnail) {
		this.imageTypeIconicThumbnail = imageTypeIconicThumbnail;
	}

	public String getImageTypeTransparent() {
		return imageTypeTransparent;
	}

	public void setImageTypeTransparent(String imageTypeTransparent) {
		this.imageTypeTransparent = imageTypeTransparent;
	}

	public boolean getDynamicMediaEnableFlag() {
		return dynamicMediaEnableFlag;
	}


	public void setDynamicMediaEnableFlag(boolean dynamicMediaEnableFlag) {
		this.dynamicMediaEnableFlag = dynamicMediaEnableFlag;
	}


	public String[] getImageProperties() {
		return imageProperties;
	}

	public void setImageProperties(String[] imageProperties) {
		this.imageProperties = imageProperties;
	}

	public String getImageServerUrl(SlingHttpServletRequest request, SlingScriptHelper slingScriptHelper) {
		String dynamicMediaContextServerUrl = StringUtils.EMPTY;
		//String imageServerUrl = request.getContextPath() + getDynamicMediaImageServerappender();
		//ResourceResolver resourceResolver = request.getResourceResolver();
		//boolean enabledDynamicMedia = DynamicMediaHelper.isDynamicMediaEnabled(resourceResolver);
		if (getDynamicMediaEnableFlag()) {
			dynamicMediaContextServerUrl = this.dynamicMediaImageServerappender;
		}
		LOGGER.error("Displaying the getImageServerUrl getting publish ServerURL", dynamicMediaContextServerUrl);
		return dynamicMediaContextServerUrl;
	}

}
