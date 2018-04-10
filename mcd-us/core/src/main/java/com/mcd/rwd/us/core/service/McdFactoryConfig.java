package com.mcd.rwd.us.core.service;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.ComponentContext;

import java.util.Dictionary;

@Component(label = "McD Configurations", immediate = true, enabled = true, metatype = true, description = "Configurations for Mcdonalds",
        policy = ConfigurationPolicy.REQUIRE, configurationFactory = true)
@Service(McdFactoryConfig.class)
@Properties({
        @Property(name = "mcd.factoryConfig.country", label = "Country Code", value = "",
                description = "Please provide name of the country node available in CRX"),
        @Property(name = "mcd.factoryConfig.country.language", label = "Language", value = "",
                description = "Please provide name of the language node available in CRX"),
        @Property(name = "mcd.factoryConfig.social.share.locale", label = "Social Share Locale", value = "",
                description = "Locale for showing social share icons as per country"),
        @Property(name = "mcd.factoryConfig.language.notAvailable", boolValue = false, label = "Language node not available", description =
                "Check this field if there is no language node for the website. If checked, "
                        + "language code provided above will be ignored'"),
        @Property(name = "mcd.factoryConfig.dna.country", label = "DNA Country Code", value = "", description = "Please provide country code for DNA"),
        @Property(name = "mcd.factoryConfig.dna.country.language", label = "DNA Language Code", value = "",
                description = "Please provide language code for DNA"),
        @Property(name = "mcd.factoryConfig.productPage.path", label = "Path for Product Page", value = "",
                description = "Please provide path for product Page"),
        @Property(name = "mcd.factoryConfig.product.shortUrl", boolValue = false, label = "Display products using Short URL"),
        @Property(name = "mcd.factoryConfig.solrCollection", label = "Solr Collection Name", value = "", description = "Solr Collection Name"),
        @Property(name = "mcd.factoryConfig.articleDetailPage.path", label = "ArticleDetail PagePath", value = "", description = "Please provide path of articleDetail Page"),
        @Property(name = " mcd.factoryConfig.newsRoom.localTag", label = "Newsroom LocalTag", value = "", description = "Please provide Local Tag value of market for newsroom")
})
public class McdFactoryConfig {

    @Property(name = "restaurantLocatorApiKey", label = "restaurant locator api key", value = "Myteou0SwsA4EIzwU7gVcAauch6Gwxzs")
    private String restaurantLocatorApiKey;

    @Property(name = "restaurantLocatorCountry", label = "restaurant locator country", value = "us")
    private String restaurantLocatorCountry;

    @Property(name = "restaurantLocatorLanguage", label = "restaurant locator language", value = "en")
    private String restaurantLocatorLanguage;

    private String country;

    @Property(name = "mcd.factoryConfig.restaurantLocatorMapLanguage", label = "restaurant locator Map Language", value = "en")
    private String restaurantLocatorMapLanguage;

    @Property(name = "mcd.factoryConfig.restaurantsResultCountry", label = "restaurant locator Result Country", value = "US")
    private String restaurantsResultCountry;


    private String language;

    private boolean languageNodeNotAvailable;

    private String dnaCountryCode;

    private String dnaLanguageCode;

    private String productPagePath;

    private boolean productShortUrlRequired;

    private String socialShareLocale;

    private String solrCollection;

    private String articleDetailPagePath;

    private String localTagValue;

    @Property(name = "mcd.factoryConfig.dcs.marketId", label = "DCS API Market ID", value = "",
            description = "Please provide market ID to use with the DCS API e.g. US")
    private String dcsMarketId;

    @Property(name = "mcd.factoryConfig.dcs.locale", label = "DCS API Locale", value = "",
            description = "Please provide locale to use with the DCS API e.g. en-US")
    private String dcsLocale;

    @Property(name = "mcd.factoryConfig.socialLogin.apiKeyFacebook", label = "Facebook API Key", value = "",
            description = "Facebook API key for social login.")
    private String apiKeyFacebook;

    @Property(name = "mcd.factoryConfig.socialLogin.facebookLocale", label = "Facebook Locale", value = "",
            description = "Please provide locale to use for Facebook plugins e.g. en_US. Note that this only applies to plugins and buttons that are directly"
                    + " integrated within the site. Dialogs such as the Login Dialog render in the language that the person has picked as their native "
                    + "language on Facebook, even if it's different than the language you select. List of valid locales "
                    + "can be found here: https://www.facebook.com/translations/FacebookLocales.xml")
    private String facebookLocale;

    @Property(name = "mcd.factoryConfig.socialLogin.apiKeyGoogle", label = "Google API Key", value = "",
            description = "Google API key for social login.")
    private String apiKeyGoogle;

    private boolean isNutritionInfoAtProductLevel;


	//start Backlog Update//
    @Property(name = "mcd.factoryConfig.itemAttr.nutrition_attribute_id_val", label = "Nutrition Attribute Id Value", value = "",description = "")
    private String nutrition_attribute_id_val;
    @Property(name = "mcd.factoryConfig.itemAttr", label = "Item Attribute", value = "",description = "")
    private String item_attribute;

    @Property(name = "mcd.factoryConfig.countryShortName", label = "Country Short Name", value = "",description = "")
    private String country_short_name;


    @Property(name = "mcd.factoryConfig.isRtl", label = "RTL Value", value = "",description = "")
    private boolean isRTL;

    @Activate
    public void activate(ComponentContext componentContext) {
        Dictionary properties = componentContext.getProperties();
        setRestaurantLocatorApiKey(PropertiesUtil.toString(properties.get("restaurantLocatorApiKey"), ""));

        setRestaurantLocatorCountry(PropertiesUtil.toString(properties.get("restaurantLocatorCountry"), ""));

        setRestaurantLocatorLanguage(
                PropertiesUtil.toString(properties.get("restaurantLocatorLanguage"), ""));
        setCountry(PropertiesUtil.toString(properties.get("mcd.factoryConfig.country"), ""));
        setLanguage(PropertiesUtil.toString(properties.get("mcd.factoryConfig.country.language"), ""));
        setLanguageNodeNotAvailable(
                PropertiesUtil.toBoolean(properties.get("mcd.factoryConfig.language.notAvailable"), false));
        setDnaCountryCode(PropertiesUtil.toString(properties.get("mcd.factoryConfig.dna.country"), ""));
        setDnaLanguageCode(
                PropertiesUtil.toString(properties.get("mcd.factoryConfig.dna.country.language"), ""));
        setProductPagePath(PropertiesUtil.toString(properties.get("mcd.factoryConfig.productPage.path"), ""));
        setProductShortUrlRequired(
                PropertiesUtil.toBoolean(properties.get("mcd.factoryConfig.product.shortUrl"), false));
        setSocialShareLocale(
                PropertiesUtil.toString(properties.get("mcd.factoryConfig.social.share.locale"), ""));
        setRestaurantLocatorMapLanguage(PropertiesUtil
                .toString(properties.get("mcd.factoryConfig.restaurantLocatorMapLanguage"), ""));

        setRestaurantsResultCountry(
                PropertiesUtil.toString(properties.get("mcd.factoryConfig.restaurantsResultCountry"), ""));
        setSolrCollection(PropertiesUtil.toString(properties.get("mcd.factoryConfig.solrCollection"), ""));

        setDcsMarketId(PropertiesUtil.toString(properties.get("mcd.factoryConfig.dcs.marketId"), ""));
        setDcsLocale(PropertiesUtil.toString(properties.get("mcd.factoryConfig.dcs.locale"), ""));
        setApiKeyFacebook(PropertiesUtil.toString(properties.get("mcd.factoryConfig.socialLogin.apiKeyFacebook"), ""));
        setFacebookLocale(PropertiesUtil.toString(properties.get("mcd.factoryConfig.socialLogin.facebookLocale"), "en_US"));
        setApiKeyGoogle(PropertiesUtil.toString(properties.get("mcd.factoryConfig.socialLogin.apiKeyGoogle"), ""));
        
        //Start for backlog update 
        setNutrition_attribute_id_val(PropertiesUtil.toString(properties.get("mcd.factoryConfig.itemAttr.nutrition_attribute_id_val"), ""));
    	setItem_attribute(PropertiesUtil.toString(properties.get("mcd.factoryConfig.itemAttr"), ""));
    	//End for backlog update 
    	
    	setNutritionInfoAtProductLevel(
                PropertiesUtil.toBoolean(properties.get("mcd.factoryConfig.product.isNutritionInfoAtProductLevel"), true));

        setCountry_short_name(PropertiesUtil.toString(properties.get("mcd.factoryConfig.countryShortName"), ""));
        setRTL(PropertiesUtil.toBoolean(properties.get("mcd.factoryConfig.isRtl"), false));
        setArticleDetailPagePath(PropertiesUtil.toString(properties.get("mcd.factoryConfig.articleDetailPage.path"), ""));
        setLocalTagValue(PropertiesUtil.toString(properties.get("mcd.factoryConfig.newsRoom.localTag"), ""));

    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setDnaCountryCode(String dnaCountryCode) {
        this.dnaCountryCode = dnaCountryCode;
    }

    public String getDnaLanguageCode() {
        return dnaLanguageCode;
    }

    public void setDnaLanguageCode(String dnaLanguageCode) {
        this.dnaLanguageCode = dnaLanguageCode;
    }

    public String getDnaCountryCode() {
        return dnaCountryCode;
    }

    public boolean isLanguageNodeNotAvailable() {
        return languageNodeNotAvailable;
    }

    public void setLanguageNodeNotAvailable(boolean languageNodeNotAvailable) {
        this.languageNodeNotAvailable = languageNodeNotAvailable;
    }

    public String getProductPagePath() {
        return productPagePath;
    }

    public void setProductPagePath(String productPagePath) {
        this.productPagePath = productPagePath;
    }

    public boolean isProductShortUrlRequired() {
        return productShortUrlRequired;
    }

    public void setProductShortUrlRequired(boolean productShortUrlRequired) {
        this.productShortUrlRequired = productShortUrlRequired;
    }

    public String getSocialShareLocale() {
        return socialShareLocale;
    }

    public void setSocialShareLocale(String socialShareLocale) {
        this.socialShareLocale = socialShareLocale;
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

    public String getRestaurantLocatorMapLanguage() {
        return this.restaurantLocatorMapLanguage;
    }

    public void setRestaurantLocatorMapLanguage(String restaurantLocatorMapLanguage) {
        this.restaurantLocatorMapLanguage = restaurantLocatorMapLanguage;
    }

    public String getRestaurantsResultCountry() {
        return this.restaurantsResultCountry;
    }

    public void setRestaurantsResultCountry(String restaurantsResultCountry) {
        this.restaurantsResultCountry = restaurantsResultCountry;
    }

    public String getSolrCollection() {
        return solrCollection;
    }

    public void setSolrCollection(String solrCollection) {
        this.solrCollection = solrCollection;
    }

    public String getDcsMarketId() {
        return dcsMarketId;
    }

    public void setDcsMarketId(final String dcsMarketId) {
        this.dcsMarketId = dcsMarketId;
    }

    public String getDcsLocale() {
        return dcsLocale;
    }

    public void setDcsLocale(final String dcsLocale) {
        this.dcsLocale = dcsLocale;
    }

    public String getApiKeyFacebook() {
        return apiKeyFacebook;
    }

    public void setApiKeyFacebook(final String apiKeyFacebook) {
        this.apiKeyFacebook = apiKeyFacebook;
    }

    public String getFacebookLocale() {
        return facebookLocale;
    }

    public void setFacebookLocale(String facebookLocale) {
        this.facebookLocale = facebookLocale;
    }

    public String getApiKeyGoogle() {
        return apiKeyGoogle;
    }

    public void setApiKeyGoogle(final String apiKeyGoogle) {
        this.apiKeyGoogle = apiKeyGoogle;
    }


	public String getNutrition_attribute_id_val() {
		return nutrition_attribute_id_val;
	}


	public void setNutrition_attribute_id_val(String nutrition_attribute_id_val) {
		this.nutrition_attribute_id_val = nutrition_attribute_id_val;
	}


	public String getItem_attribute() {
		return item_attribute;
	}


	public void setItem_attribute(String item_attribute) {
		this.item_attribute = item_attribute;
	}
	
	public boolean isNutritionInfoAtProductLevel() {
		return isNutritionInfoAtProductLevel;
	}


	public void setNutritionInfoAtProductLevel(boolean isNutritionInfoAtProductLevel) {
		this.isNutritionInfoAtProductLevel = isNutritionInfoAtProductLevel;
	}


    public String getCountry_short_name() {
        return country_short_name;
    }

    public void setCountry_short_name(String country_short_name) {
        this.country_short_name = country_short_name;
    }


    public boolean isRTL() {
        return isRTL;
    }

    public void setRTL(boolean RTL) {
        isRTL = RTL;
    }

    public String getArticleDetailPagePath() {
        return articleDetailPagePath;
    }

    public void setArticleDetailPagePath(String articleDetailPagePath) {
        this.articleDetailPagePath = articleDetailPagePath;
    }

    public String getLocalTagValue() {
        return localTagValue;
    }

    public void setLocalTagValue(String localTagValue) {
        this.localTagValue = localTagValue;
    }


}
