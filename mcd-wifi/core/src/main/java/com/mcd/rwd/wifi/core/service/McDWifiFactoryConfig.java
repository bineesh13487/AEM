package com.mcd.rwd.wifi.core.service;

import org.apache.felix.scr.annotations.*;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.ComponentContext;

import java.io.Serializable;
import java.util.Dictionary;

/**
 * Created  by Vamsi Jetty
 */
@Component(label = "McD Wifi Configurations", immediate = true, enabled = true, metatype = true, description = "Configurations for Mcdonalds Wifi", policy = ConfigurationPolicy.REQUIRE, configurationFactory = true)
@Service(McDWifiFactoryConfig.class)
@Properties({
		@Property(name = "mcd-wifi.factoryConfig.country", label = "Country Code", value = "", description = "Please provide name of the country node available in CRX"),
		@Property(name = "mcd-wifi.factoryConfig.country.language", label = "Language", value = "", description = "Please provide name of the language node available in CRX"),
		@Property(name = "mcd-wifi.factoryConfig.language.notAvailable", boolValue = false, label = "Language node not available", description =
				"Check this field if there is no language node for the website. If checked, "
						+ "language code provided above will be ignored'"),
		@Property(name = "restaurantLocatorApiKey", label = "restaurant locator api key", value = "GtZFXGWxxz2BPpbTytmOeagquGQQrvEd"),
		@Property(name = "restaurantLocatorMarketId", label = "restaurant locator market id", value = ""),
		@Property(name = "restaurantLocatorCountry", label = "restaurant locator country", value = "us"),
		@Property(name = "restaurantLocatorLanguage", label = "restaurant locator language", value = "en"),
		@Property(name = "mcd-wifi.factoryConfig.restaurantLocatorUrl", label = "restaurant locator url ", value = "https://apidev-mcd.cloudhub.io/v3/restaurant/location"),	
		@Property(name = "mcd-wifi.factoryConfig.restaurantLocatorRadius", label = "restaurant locator radius ", value = "2"),
		@Property(name = "mcd-wifi.factoryConfig.restaurantLocatorResultsSize", label = "restaurant locator results size ", value = "1")
})
public class McDWifiFactoryConfig implements Serializable{

	private static final long serialVersionUID = 3867209543778596853L;

	private String restaurantLocatorApiKey;
	
	private String restaurantLocatorMarketId;

	private String restaurantLocatorCountry;

	private String restaurantLocatorLanguage;
	
	private String language;

	private boolean languageNodeNotAvailable;
	
	private String restaurantLocatorUrl;
	
	private String restaurantLocatorRadius;
	
	private String restaurantLocatorResultsSize;


	@Activate 
	public void activate(ComponentContext componentContext) {
		Dictionary properties = componentContext.getProperties();
		setRestaurantLocatorApiKey(PropertiesUtil.toString(properties.get("restaurantLocatorApiKey"), ""));
		setRestaurantLocatorMarketId(PropertiesUtil.toString(properties.get("restaurantLocatorMarketId"), ""));
		setRestaurantLocatorCountry(PropertiesUtil.toString(properties.get("restaurantLocatorCountry"), ""));
		setRestaurantLocatorLanguage(
				PropertiesUtil.toString(properties.get("restaurantLocatorLanguage"), ""));
		setLanguage(PropertiesUtil.toString(properties.get("mcd-wifi.factoryConfig.country.language"), ""));
		setLanguageNodeNotAvailable(
				PropertiesUtil.toBoolean(properties.get("mcd-wifi.factoryConfig.language.notAvailable"), false));
		setRestaurantLocatorUrl(
				PropertiesUtil.toString(properties.get("mcd-wifi.factoryConfig.restaurantLocatorUrl"), ""));
		setRestaurantLocatorRadius(
				PropertiesUtil.toString(properties.get("mcd-wifi.factoryConfig.restaurantLocatorRadius"), ""));
		setRestaurantLocatorResultsSize(
				PropertiesUtil.toString(properties.get("mcd-wifi.factoryConfig.restaurantLocatorResultsSize"), ""));
	}


	/**
	 * @return the restaurantLocatorResultsSize
	 */
	public String getRestaurantLocatorResultsSize() {
		return restaurantLocatorResultsSize;
	}


	/**
	 * @param restaurantLocatorResultsSize the restaurantLocatorResultsSize to set
	 */
	public void setRestaurantLocatorResultsSize(String restaurantLocatorResultsSize) {
		this.restaurantLocatorResultsSize = restaurantLocatorResultsSize;
	}


	/**
	 * @return the restaurantLocatorRadius
	 */
	public String getRestaurantLocatorRadius() {
		return restaurantLocatorRadius;
	}


	/**
	 * @param restaurantLocatorRadius the restaurantLocatorRadius to set
	 */
	public void setRestaurantLocatorRadius(String restaurantLocatorRadius) {
		this.restaurantLocatorRadius = restaurantLocatorRadius;
	}


	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public boolean isLanguageNodeNotAvailable() {
		return languageNodeNotAvailable;
	}

	public void setLanguageNodeNotAvailable(boolean languageNodeNotAvailable) {
		this.languageNodeNotAvailable = languageNodeNotAvailable;
	}

	public String getRestaurantLocatorCountry() {
		return this.restaurantLocatorCountry;
	}

	public void setRestaurantLocatorCountry(String restaurantLocatorCountry) {
		this.restaurantLocatorCountry = restaurantLocatorCountry;
	}

	public String getRestaurantLocatorLanguage() {
		return this.restaurantLocatorLanguage;
	}

	public void setRestaurantLocatorLanguage(String restaurantLocatorLanguage) {
		this.restaurantLocatorLanguage = restaurantLocatorLanguage;
	}

	public String getRestaurantLocatorApiKey() {
		return this.restaurantLocatorApiKey;
	}

	public void setRestaurantLocatorApiKey(String restaurantLocatorApiKey) {
		this.restaurantLocatorApiKey = restaurantLocatorApiKey;
	}
	public String getRestaurantLocatorMarketId() {
		return this.restaurantLocatorMarketId;
	}

	public void setRestaurantLocatorMarketId(String restaurantLocatorMarketId) {
		this.restaurantLocatorMarketId = restaurantLocatorMarketId;
	}
	public String getRestaurantLocatorUrl() {
		return this.restaurantLocatorUrl;
	}

	public void setRestaurantLocatorUrl(String restaurantLocatorUrl) {
		this.restaurantLocatorUrl = restaurantLocatorUrl;
	}
}
