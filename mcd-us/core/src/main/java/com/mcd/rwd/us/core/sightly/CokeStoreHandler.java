package com.mcd.rwd.us.core.sightly;

import java.util.ArrayList;
import java.util.List;

import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.mcd.rwd.us.core.constants.CokeStoreHandlerConstants;


import com.google.gson.JsonObject;
import com.mcd.rwd.global.core.sightly.McDUse;
import com.mcd.rwd.us.core.service.McdWebServicesConfig;

public class CokeStoreHandler extends McDUse {
	private static final Logger LOG = LoggerFactory.getLogger(CokeStoreHandler.class);

	private static final String COKE_SITE_CONFIG = "coke";
	
	//Coke Store Widget Params
	private String distanceUnit;
	private String radiusNearestLocation;
	private String cokeStoreLocationPath;
	private String clientId;
	private String locale;
	private String maxResults;
	private String[] radius;
	private String geoCountry;
	private String latitude;
	private String longitude;
	private String storeLocatorHeading;
	private String searchFieldHeading;
	private String locateMe;
	private String selectLocationBelow;
	private String searchBoxDefaultText;
	private String categoryFieldText;
	private String flavorFieldText;
	private String radiusAriaLabel;
	private String changeLocation;
	private String searchBoxSubmit;
	private String kilometerText;
	private String milesText;
	private String orText;
	private String allCategoriesText;
	private String allFlavorsText;
	private Boolean redirect;
	

	
	//Coke Store Results Params
	private String cokeMapView;
	private String cokeListView;
	private String resultIterate;
	private String resultMessage;
	private String awayText;
	private String loadMoreText;
	private String ariaLabelSearchResult;
	private String directionLinkText;
	private String phoneHeading;
	private String locNearLabel;
	private String checkNearestLocLabel;
	private String mcdName;
	private String onText;
	private String ariaLabelCokeStore;
	private String ariaLabelGetDir;
	private String ariaLabelExpandStore;
	private String ariaLabelCollapseStore;
	private String locationPermissionDeniedText;
	private String locationUnavailableText;
	private String locationTimeoutText;
	private String locationUnknownErrorText;
	private String emptySearchErrorText;
	private String cokeStoreJSONString;
	private String categories;
	private String serviceUnavailableErrorText;
	private String invalidGeoLocationErrorText;
	
	//JSON to send to coke store controller for further manipulation using author configured values
	private JsonObject cokeStoreJSON = new JsonObject();

	private String cokeStoreLocationsUrl;

	public void activate() throws Exception {
		
		LOG.debug("In CokeStoreHandler Activate method");
		ValueMap dialogProperties = getSiteConfig(COKE_SITE_CONFIG);
		ValueMap properties = getProperties();
		this.redirect = properties.get(CokeStoreHandlerConstants.REDIRECT, Boolean.class);
		this.searchFieldHeading = dialogProperties.get("searchFieldHeading","");
		this.locateMe = dialogProperties.get("locateMe","");
		this.selectLocationBelow = dialogProperties.get("selectLocationBelow","");
		this.searchBoxDefaultText = dialogProperties.get("searchBoxDefaultText","");
		this.searchBoxSubmit = dialogProperties.get("searchBoxSubmit","");
		this.storeLocatorHeading = dialogProperties.get("storeLocatorHeading","");
		this.categoryFieldText = dialogProperties.get("categoryFieldText","");
		this.flavorFieldText = dialogProperties.get("flavorFieldText","");
		this.radiusAriaLabel = dialogProperties.get("radiusAriaLabel","");
		this.radius = getList(dialogProperties.get("radius",String[].class));
		this.orText = dialogProperties.get("ortext","");
		this.allCategoriesText = dialogProperties.get("allCategoriesText","");
		this.allFlavorsText = dialogProperties.get("allFlavorsText","");
		
		this.cokeMapView = dialogProperties.get("cokeMapView","");
		this.cokeListView = dialogProperties.get("cokeListView","");
		this.awayText = dialogProperties.get("awayText","");
		this.loadMoreText = dialogProperties.get("loadMoreText","");
		this.ariaLabelSearchResult = dialogProperties.get("ariaLabelSearchResult",""); 
		this.directionLinkText = dialogProperties.get("directionLinkText","");
		this.phoneHeading = dialogProperties.get("phoneHeading","");
		this.locNearLabel = dialogProperties.get("locNearLabel","");
		this.checkNearestLocLabel = dialogProperties.get("checkNearestLocLabel","");
		this.onText = dialogProperties.get("onText","");
		this.ariaLabelCokeStore = dialogProperties.get("ariaLabelCokeStore","");
		this.ariaLabelGetDir = dialogProperties.get("ariaLabelGetDir","");
		this.ariaLabelExpandStore = dialogProperties.get("ariaLabelExpandStore","");
		this.ariaLabelCollapseStore = dialogProperties.get("ariaLabelCollapseStore","");
		
		this.kilometerText = dialogProperties.get(CokeStoreHandlerConstants.KILOMETER_TEXT,""); //Send to controller
		this.milesText = dialogProperties.get("milesText",""); //Send to controller
		this.distanceUnit = dialogProperties.get("distanceUnit","miles"); //Send to controller
		this.radiusNearestLocation = dialogProperties.get("radiusNearestLocation"," "); //Send to controller
		this.cokeStoreLocationPath = dialogProperties.get("cokeLocPath"," "); //Send to controller
		this.clientId = dialogProperties.get("clientId"," "); //Send to controller
		this.locale = dialogProperties.get("locale","");//send to controller
		this.maxResults = dialogProperties.get("maxResults",""); //Send to controller
		this.radius = getList(dialogProperties.get("radius",String[].class)); //Send to controller
		this.mcdName = dialogProperties.get("mcdName",""); //Send to controller
		this.resultIterate = dialogProperties.get("resultIterate",""); //Send to controller
		this.resultMessage = dialogProperties.get("resultMessage",""); //Send to controller
		this.locationPermissionDeniedText = dialogProperties.get("locationPermissionDeniedText",""); //Send to controller
		this.locationUnavailableText = dialogProperties.get("locationUnavailableText",""); //Send to controller
		this.locationTimeoutText = dialogProperties.get(CokeStoreHandlerConstants.LOCATION_TIMEOUT_TEXT,""); //Send to controller
		this.locationUnknownErrorText = dialogProperties.get(CokeStoreHandlerConstants.LOCATION_UNKNOWN_ERROR_TEXT,""); //Send to controller
		this.emptySearchErrorText = dialogProperties.get(CokeStoreHandlerConstants.EMPTY_SEARCH_ERROR_TEXT,""); //Send to controller
		this.invalidGeoLocationErrorText = dialogProperties.get(CokeStoreHandlerConstants.INVALID_GEO_ERROR_TEXT,""); //Send to controller
		this.serviceUnavailableErrorText = dialogProperties.get(CokeStoreHandlerConstants.SERVICE_UNAVAILABLE_ERROR_TEXT,""); //Send to controller
		this.cokeStoreLocationsUrl = getCokeStoreLocationsUrl();
		
		this.cokeStoreJSON = generateCokeStoreJSON(dialogProperties, properties);
		LOG.error("cokeStoreJSON AAAA"+cokeStoreJSON);
		this.cokeStoreJSONString = cokeStoreJSON.toString();
		LOG.error("cokeStoreJSONString AAAA"+cokeStoreJSONString);
		LOG.debug("End of CokeStoreHandler Activate method");
		
	}

	private JsonObject generateCokeStoreJSON(ValueMap dialogProperties, ValueMap properties) {
		JsonObject finalJSON = new JsonObject();


		finalJSON.addProperty(CokeStoreHandlerConstants.KILOMETER_TEXT, dialogProperties.get(CokeStoreHandlerConstants.KILOMETER_TEXT,"")); //Send to controller
		finalJSON.addProperty(CokeStoreHandlerConstants.MILES_TEXT, dialogProperties.get(CokeStoreHandlerConstants.MILES_TEXT,"")); //Send to controller
		finalJSON.addProperty(CokeStoreHandlerConstants.DISTANCE_UNIT, dialogProperties.get(CokeStoreHandlerConstants.DISTANCE_UNIT,"miles")); //Send to controller
		finalJSON.addProperty(CokeStoreHandlerConstants.RADIUS_NEAREST_LOCATION, dialogProperties.get(CokeStoreHandlerConstants.RADIUS_NEAREST_LOCATION," ")); //Send to controller
		finalJSON.addProperty("cokeStoreLocationPath", dialogProperties.get("cokeLocPath"," ")); //Send to controller
		finalJSON.addProperty(CokeStoreHandlerConstants.CLIENT_ID, dialogProperties.get(CokeStoreHandlerConstants.CLIENT_ID,"")); //Send to controller
		finalJSON.addProperty(CokeStoreHandlerConstants.LOCALE, dialogProperties.get(CokeStoreHandlerConstants.LOCALE,"")); //Send to controller
		finalJSON.addProperty(CokeStoreHandlerConstants.MAX_RESULTS, dialogProperties.get(CokeStoreHandlerConstants.MAX_RESULTS,"")); //Send to controller
		finalJSON.addProperty(CokeStoreHandlerConstants.REDIRECT, properties.get(CokeStoreHandlerConstants.REDIRECT,false)); //Send to controller
		finalJSON.addProperty(CokeStoreHandlerConstants.MCD_NAME, dialogProperties.get(CokeStoreHandlerConstants.MCD_NAME,"")); //Send to controller
		finalJSON.addProperty(CokeStoreHandlerConstants.RESULT_ITERATE, dialogProperties.get(CokeStoreHandlerConstants.RESULT_ITERATE,"")); //Send to controller
		finalJSON.addProperty(CokeStoreHandlerConstants.RESULT_MESSAGE, dialogProperties.get(CokeStoreHandlerConstants.RESULT_MESSAGE,"")); //Send to controller
		finalJSON.addProperty(CokeStoreHandlerConstants.LOCATION_PERMISSION_DENIED_TEXT, dialogProperties.get(CokeStoreHandlerConstants.LOCATION_PERMISSION_DENIED_TEXT,"")); //Send to controller
		finalJSON.addProperty(CokeStoreHandlerConstants.LOCATION_UNAVAILABLE_TEXT, dialogProperties.get(CokeStoreHandlerConstants.LOCATION_UNAVAILABLE_TEXT,"")); //Send to controller
		finalJSON.addProperty(CokeStoreHandlerConstants.LOCATION_TIMEOUT_TEXT, dialogProperties.get(CokeStoreHandlerConstants.LOCATION_TIMEOUT_TEXT,"")); //Send to controller
		finalJSON.addProperty(CokeStoreHandlerConstants.LOCATION_UNKNOWN_ERROR_TEXT, dialogProperties.get(CokeStoreHandlerConstants.LOCATION_UNKNOWN_ERROR_TEXT,"")); //Send to controller
		finalJSON.addProperty(CokeStoreHandlerConstants.EMPTY_SEARCH_ERROR_TEXT, dialogProperties.get(CokeStoreHandlerConstants.EMPTY_SEARCH_ERROR_TEXT,"")); //Send to controller
		finalJSON.addProperty(CokeStoreHandlerConstants.INVALID_GEO_ERROR_TEXT, dialogProperties.get(CokeStoreHandlerConstants.INVALID_GEO_ERROR_TEXT,"")); //Send to controller
		finalJSON.addProperty(CokeStoreHandlerConstants.SERVICE_UNAVAILABLE_ERROR_TEXT, dialogProperties.get(CokeStoreHandlerConstants.SERVICE_UNAVAILABLE_ERROR_TEXT,"")); //Send to controller
		finalJSON.addProperty("cokeFilters", getCokeFilters(dialogProperties.get("categories",String[].class))); //Send to controller
		finalJSON.addProperty("cokeStoreLocationsUrl", getCokeStoreLocationsUrl()); //Send to controller
		
		return finalJSON;
	}
	
	 private String getCokeFilters(String[] cokeFilters) {
			List<String> cokeFiltersList = new ArrayList<>();
			String cokeFiltersString = "";
			
			for(String cokeFilter: cokeFilters){
				cokeFiltersList.add(cokeFilter);
			}
			cokeFiltersString = cokeFiltersList.toString();
			return cokeFiltersString;
		} 

	public String[] getList(String[] radiusProp) {
		List<String> radiusList = new ArrayList<>();
		for(String radiusVar : radiusProp){
			radiusList.add(radiusVar.trim());
		}
		LOG.debug("radiusProp is{}", radiusProp);
		return radiusProp;
	}
	
	public String getCokeStoreLocationsUrl() {
		SlingScriptHelper helper = getSlingScriptHelper();
		String cokeLocURL = "";
		if(helper!=null){
		McdWebServicesConfig mcdConfig=helper.getService(McdWebServicesConfig.class);
		if(null != mcdConfig)
		cokeLocURL = mcdConfig.getCokeStoreLocationsUrl();
		}
		return cokeLocURL;	
	}

	public String getRadiusNearestLocation() {
		return radiusNearestLocation;
	}

	public String getCokeStoreLocationPath() {
		return cokeStoreLocationPath;
	}
	
	public Boolean getRedirect() {
		return redirect;
	}
	
	
	public String getClientId() {
		return clientId;
	}
	public String getlocale() {
		return locale;
	}
	
	public String getMaxResults() {
		return maxResults;
	}

	public String getGeoCountry() {
		return geoCountry;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public String getSearchFieldHeading() {
		return searchFieldHeading;
	}

	public String getLocateMe() {
		return locateMe;
	}

	public String getSelectLocationBelow() {
		return selectLocationBelow;
	}

	public String getSearchBoxDefaultText() {
		return searchBoxDefaultText;
	}

	public String getCokeMapView() {
		return cokeMapView;
	}

	public String getCokeListView() {
		return cokeListView;
	}

	public String getChangeLocation() {
		return changeLocation;
	}

	public String getSearchBoxSubmit() {
		return searchBoxSubmit;
	}

	public String getKilometerText() {
		return kilometerText;
	}

	public String getMilesText() {
		return milesText;
	}

	public String getResultIterate() {
		return resultIterate;
	}

	public String[] getRadius() {
		return radius;
	}
	
	public String getStoreLocatorHeading() {
		return storeLocatorHeading;
	}

	public String getCategoryFieldText() {
		return categoryFieldText;
	}

	public String getFlavorFieldText() {
		return flavorFieldText;
	}

	public String getRadiusAriaLabel() {
		return radiusAriaLabel;
	}
	
	public String getResultMessage() {
		return resultMessage;
	}

	public String getAwayText() {
		return awayText;
	}

	public String getLoadMoreText() {
		return loadMoreText;
	}

	public String getAriaLabelSearchResult() {
		return ariaLabelSearchResult;
	}

	public String getDirectionLinkText() {
		return directionLinkText;
	}

	public String getPhoneHeading() {
		return phoneHeading;
	}

	public String getLocNearLabel() {
		return locNearLabel;
	}

	public String getCheckNearestLocLabel() {
		return checkNearestLocLabel;
	}

	public String getMcdName() {
		return mcdName;
	}

	public String getOnText() {
		return onText;
	}
	
	public String getAllCategoriesText() {
		return allCategoriesText;
	}
	
	public String getAllFlavorsText() {
		return allFlavorsText;
	}
	
	public String getLocationPermissionDeniedText() {
		return locationPermissionDeniedText;
	}

	public String getLocationUnavailableText() {
		return locationUnavailableText;
	}

	public String getLocationTimeoutText() {
		return locationTimeoutText;
	}

	public String getLocationUnknownErrorText() {
		return locationUnknownErrorText;
	}

	public String getEmptySearchErrorText() {
		return emptySearchErrorText;
	}
	
	public String getServiceUnavailableErrorText() {
		return serviceUnavailableErrorText;
	}
	
	public String getInvalidGeoLocationErrorText() {
		return invalidGeoLocationErrorText;
	}
	
	public JsonObject getCokeStoreJSON() {
		return cokeStoreJSON;
	}
	
	public String getCokeStoreJSONString() {
		return cokeStoreJSONString;
	}
	
	public String getAriaLabelCokeStore() {
		return ariaLabelCokeStore;
	}

	public String getAriaLabelGetDir() {
		return ariaLabelGetDir;
	}

	public String getAriaLabelExpandStore() {
		return ariaLabelExpandStore;
	}

	public String getAriaLabelCollapseStore() {
		return ariaLabelCollapseStore;
	}

	public String getOrText() {
		return orText;
	}
	
	public String getCategories() {
		return categories;
	}
	
	public String getDistanceUnit() {
		return distanceUnit;
	}
}
