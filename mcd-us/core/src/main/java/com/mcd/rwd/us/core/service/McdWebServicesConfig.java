package com.mcd.rwd.us.core.service;


import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.ComponentContext;
import com.mcd.rwd.us.core.sightly.CokeStoreHandler;

import java.util.Dictionary;

@Component(label = "McD  Web Services Configurations", immediate = true, metatype = true,
        description = "Configurations for web services used in the Mcdonalds website")
@Service(McdWebServicesConfig.class)
@Properties({
        @Property(name = "mcd.factoryConfig.dna.webservice.domain", label = "DNA Web Service Domain", value = "", description = "DNA Web Service Domain"),
        @Property(name = "mcd.factoryConfig.categoryDetails", label = "Category Details Url", value = "", description = "URL For Category Details"),
        @Property(name = "mcd.factoryConfig.categoryListUrl", label = "Category List URL", value = "", description = "URL for getting category list"),
        @Property(name = "mcd.factoryConfig.nutrientFactsUrl", label = "Nutrient Facts URL", value = "", description = "URL for getting Nutrient Facts"),
        @Property(name = "mcd.factoryConfig.allergenUrl", label = "Allergen URL", value = "", description = "URL For Allergens"),
        @Property(name = "mcd.factoryConfig.itemOnExternalId", label = "Item On External Id Url", value = "", description = "URL For Item On External Id"),
        @Property(name = "mcd.factoryConfig.goesWellWithListUrl", label = "Goes Well With List URL", value = "", description = "URL For Goes Well With"),
        @Property(name = "mcd.factoryConfig.getAllItemsUrl", label = "Get All Items List URL", value = "",
                description = "URL For retrieving data of all Items"),
        @Property(name = "mcd.factoryConfig.itemDetail", label = "Item Detail Url", value = "", description = "URL For Item Detail"),
        @Property(name = "mcd.factoryConfig.itemIntroduction", label = "Item itemIntroduction Url", value = "", description = "URL For Item itemIntroduction"),
        @Property(name = "mcd.factoryConfig.itemList", label = "Item List Url", value = "",
                description = "URL For Item List used in addition of nutrition value for many products"),
        @Property(name = "mcd.factoryConfig.categoryBundle", label = "Category Bundle Url", value = "", description = "Url for bundles"),
        @Property(name = "mcd.factoryConfig.assest.query", label = "Query for last DAM Assest update", value = "", description = "Query used to fetch dam asset lastupdated in provided time"),
        @Property(name = "mcd.factoryConfig.shortName", label = "Short Name Url", value = "", description = "URL For product short name")
        })
		@Property(name = "mcd.factoryConfig.franchise.StateDropDown", label = "Frachise State Drop Down", value="https://www1.staging.mcdonalds.com/usfranchise/loadStateList?src=", description = "Frachise State Drop Down Value")

public class McdWebServicesConfig {

    /*@Property(name = "mcd.factoryConfig.restaurantLocatorUrl", label = "restaurant locator url ", value = "https://api.mcd.com/v3/restaurant/location")
    private String restaurantLocatorUrl;*/
	@Property(name = "mcd.factoryConfig.restaurantLocatorUrl", label = "restaurant locator url ", value = "http://www5.development.mcdonalds.com/googleappsgws/GoogleRestaurantLocAction.do?method=searchServices")
    private String restaurantLocatorUrl;
	
	@Property(name = "mcd.factoryConfig.cookieExpiry", label = "Cookie Expiry Time ", value = "")
    private String cookieExpiry;
	
	@Property(name = "mcd.factoryConfig.cookieDisclaimerExpiryInDays", label = "Cookie Disclaimer Expiry Time", value = "")
    private String cookieDisclaimerExpiryInDays;

    @Property(name = "mcd.factoryConfig.restaurantAttributeUrl", label = "restaurant Attribute url ", value = "/marketAttributes")
    private String restaurantAttributeUrl;
	
    @Property(name = "mcd.factoryConfig.cokeStoreFiltersUrl", label = "cokeStoreFiltersUrl", value="http://www1.development.mcdonalds.com/cokeintegration/cokestore/filters", description = "Coke Store filter url")
    private String cokeStoreFiltersUrl;
  
    @Property(name = "mcd.factoryConfig.cokeStoreLocationsUrl", label = "cokeStoreLocationsUrl", value="http://www1.development.mcdonalds.com/cokeintegration/cokestore/locations", description = "Coke Store location url")
    private String cokeStoreLocationsUrl;
	
	@Property(name = "mcd.factoryConfig.peopleStoriesFormSubmitUrl", label = "peopleStoriesFormSubmitUrl", value="http://www1.development.mcdonalds.com/services/form", description = "People Stories Form Submit Service")
    private String peopleStoriesFormSubmitUrl;
    
    private String categoryListUrl;

    private String nutrientFactsUrl;

    private String domain;

    private String allergenUrl;

    private String itemOnExternalIdUrl;

    private String itemIntroductionUrl;

    private String itemListUrl;

    private String categoryBundleUrl;

    private String categoryDetailUrl;

    private String goesWellWithUrl;

    private String getAllItemsUrl;

    private String itemDetailUrl;

    private String shortNameUrl;

    private String damAssetQuery = StringUtils.EMPTY;

    private String stateDropDownURL;
    
	@Property(name = "mcd.factoryConfig.dcs.webservice.domain", label = "Node.js base URL",
            value = "", description = "Base URL for the Node.js web services. If left empty, relative URLs will be used. E.g.: https://dev-dep.mcd.com")
    private String dcsApiDomain;

    @Activate
    public void activate(ComponentContext componentContext) {
        Dictionary properties = componentContext.getProperties();
        setRestaurantLocatorUrl(
                PropertiesUtil.toString(properties.get("mcd.factoryConfig.restaurantLocatorUrl"), ""));
        setCookieExpiry(
                PropertiesUtil.toString(properties.get("mcd.factoryConfig.cookieExpiry"), ""));
        setCookieDisclaimerExpiryInDays(
                PropertiesUtil.toString(properties.get("mcd.factoryConfig.cookieDisclaimerExpiryInDays"), ""));
        setRestaurantAttributeUrl(
                PropertiesUtil.toString(properties.get("mcd.factoryConfig.restaurantAttributeUrl"), ""));
        setCategoryListUrl(PropertiesUtil.toString(properties.get("mcd.factoryConfig.categoryListUrl"), ""));
        setGoesWellWithUrl(
                PropertiesUtil.toString(properties.get("mcd.factoryConfig.goesWellWithListUrl"), ""));
        setGetAllItemsUrl(PropertiesUtil.toString(properties.get("mcd.factoryConfig.getAllItemsUrl"), ""));
        setNutrientFactsUrl(
                PropertiesUtil.toString(properties.get("mcd.factoryConfig.nutrientFactsUrl"), ""));
        setDomain(PropertiesUtil.toString(properties.get("mcd.factoryConfig.dna.webservice.domain"), ""));
        setAllergenUrl(PropertiesUtil.toString(properties.get("mcd.factoryConfig.allergenUrl"), ""));
        setItemOnExternalIdUrl(
                PropertiesUtil.toString(properties.get("mcd.factoryConfig.itemOnExternalId"), ""));
        setItemIntroductionUrl(
                PropertiesUtil.toString(properties.get("mcd.factoryConfig.itemIntroduction"), ""));
        setItemListUrl(PropertiesUtil.toString(properties.get("mcd.factoryConfig.itemList"), ""));
        setCategoryBundleUrl(PropertiesUtil.toString(properties.get("mcd.factoryConfig.categoryBundle"), ""));
        setCategoryDetailUrl(
                PropertiesUtil.toString(properties.get("mcd.factoryConfig.categoryDetails"), ""));
        setItemDetailUrl(PropertiesUtil.toString(properties.get("mcd.factoryConfig.itemDetail"), ""));
        setShortNameUrl(PropertiesUtil.toString(properties.get("mcd.factoryConfig.shortName"), ""));
        setDcsApiDomain(PropertiesUtil.toString(properties.get("mcd.factoryConfig.dcs.webservice.domain"), ""));
        setDamAssetQuery(PropertiesUtil.toString(properties.get("mcd.factoryConfig.assest.query"), ""));
        setStateDropDownURL(PropertiesUtil.toString(properties.get("mcd.factoryConfig.franchise.StateDropDown"), ""));
		setCokeStoreFiltersUrl(PropertiesUtil.toString(properties.get("mcd.factoryConfig.cokeStoreFiltersUrl"), ""));
		setCokeStoreLocationsUrl(PropertiesUtil.toString(properties.get("mcd.factoryConfig.cokeStoreLocationsUrl"), ""));
		setPeopleStoriesFormSubmitUrl(PropertiesUtil.toString(properties.get("mcd.factoryConfig.peopleStoriesFormSubmitUrl"), ""));      
    }

	private void setPeopleStoriesFormSubmitUrl(String peopleStoriesFormSubmitUrl) {
    	this.peopleStoriesFormSubmitUrl=peopleStoriesFormSubmitUrl;		
	}
	
    private void setCokeStoreFiltersUrl(String cokeStoreFiltersUrl) {
    	this.cokeStoreFiltersUrl=cokeStoreFiltersUrl;		
	}
    
    private void setCokeStoreLocationsUrl(String cokeStoreLocationsUrl) {
    	this.cokeStoreLocationsUrl=cokeStoreLocationsUrl;		
	}

	private void setCookieDisclaimerExpiryInDays(String cookieDisclaimerExpiryInDays) {
    	this.cookieDisclaimerExpiryInDays=cookieDisclaimerExpiryInDays;	
	}

	private void setCookieExpiry(String cookieExpiry) {
		this.cookieExpiry=cookieExpiry;
	}

	public String getCategoryListUrl() {
        return categoryListUrl;
    }
	
	public String getPeopleStoriesFormSubmitUrl() {
        return this.peopleStoriesFormSubmitUrl;
    }
	
	public String getCokeStoreFiltersUrl() {
        return this.cokeStoreFiltersUrl;
    }

	public String getCokeStoreLocationsUrl() {
		return this.cokeStoreLocationsUrl;
    }
	
    public void setCategoryListUrl(String categoryListUrl) {
        this.categoryListUrl = categoryListUrl;
    }

    public String getNutrientFactsUrl() {
        return nutrientFactsUrl;
    }

    public void setNutrientFactsUrl(String nutrientFactsUrl) {
        this.nutrientFactsUrl = nutrientFactsUrl;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getAllergenUrl() {
        return allergenUrl;
    }

    public void setAllergenUrl(String allergenUrl) {
        this.allergenUrl = allergenUrl;
    }

    public String getItemOnExternalIdUrl() {
        return itemOnExternalIdUrl;
    }

    public void setItemOnExternalIdUrl(String itemOnExternalIdUrl) {
        this.itemOnExternalIdUrl = itemOnExternalIdUrl;
    }

    public String getItemIntroductionUrl() {
        return itemIntroductionUrl;
    }

    public void setItemIntroductionUrl(String itemIntroductionUrl) {
        this.itemIntroductionUrl = itemIntroductionUrl;
    }

    public String getItemListUrl() {
        return itemListUrl;
    }

    public void setItemListUrl(String itemListUrl) {
        this.itemListUrl = itemListUrl;
    }

    public String getCategoryDetailUrl() {
        return categoryDetailUrl;
    }

    public void setCategoryDetailUrl(String categoryDetailUrl) {
        this.categoryDetailUrl = categoryDetailUrl;
    }

    public String getItemDetailUrl() {
        return itemDetailUrl;
    }

    public void setItemDetailUrl(String itemDetailUrl) {
        this.itemDetailUrl = itemDetailUrl;
    }

    public String getCategoryBundleUrl() {
        return categoryBundleUrl;
    }

    public void setCategoryBundleUrl(String categoryBundle) {
        this.categoryBundleUrl = categoryBundle;
    }

    public String getShortNameUrl() {
        return shortNameUrl;
    }

    public void setShortNameUrl(String shortNameUrl) {
        this.shortNameUrl = shortNameUrl;
    }

    public String getRestaurantLocatorUrl() {
        return this.restaurantLocatorUrl;
    }
    
    public String getCookieDisclaimerExpiryInDays() {
        return this.cookieDisclaimerExpiryInDays;
    }
    
    public String getCookieExpiry() {
        return this.cookieExpiry;
    }

    public void setRestaurantLocatorUrl(String restaurantLocatorUrl) {
        this.restaurantLocatorUrl = restaurantLocatorUrl;
    }

    public String getRestaurantAttributeUrl() {
        return this.restaurantAttributeUrl;
    }

    public void setRestaurantAttributeUrl(String restaurantAttributeUrl) {
        this.restaurantAttributeUrl = restaurantAttributeUrl;
    }

    public String getGoesWellWithUrl() {
        return goesWellWithUrl;
    }

    public void setGoesWellWithUrl(String goesWellWithUrl) {
        this.goesWellWithUrl = goesWellWithUrl;
    }

    public String getGetAllItemsUrl() {
        return getAllItemsUrl;
    }

    public void setGetAllItemsUrl(String getAllItemsUrl) {
        this.getAllItemsUrl = getAllItemsUrl;
    }

    public String getDcsApiDomain() {
        return dcsApiDomain;
    }

    public void setDcsApiDomain(final String dcsApiDomain) {
        this.dcsApiDomain = dcsApiDomain;


    }
    
    public String getDamAssetQuery() {
 		return damAssetQuery;
 	}

 	public void setDamAssetQuery(String damAssetQuery) {
 		this.damAssetQuery = damAssetQuery;
 	}

 	public String getStateDropDownURL() {
		return stateDropDownURL;
	}

 	public void setStateDropDownURL(String stateDropDownURL) {
		this.stateDropDownURL = stateDropDownURL;
	}
   	
}
