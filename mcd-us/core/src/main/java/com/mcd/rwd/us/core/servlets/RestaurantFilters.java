package com.mcd.rwd.us.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.mcd.rwd.global.core.utils.ConnectionUtil;
import com.mcd.rwd.global.core.utils.ResourceUtil;
import com.mcd.rwd.us.core.bean.rl.RestaurantAttributesResponse;
import com.mcd.rwd.us.core.constants.ApplicationConstants;
import com.mcd.rwd.us.core.service.McdFactoryConfig;
import com.mcd.rwd.us.core.service.McdFactoryConfigConsumer;
import com.mcd.rwd.us.core.service.McdWebServicesConfig;

/**
 * Created by SRISHMA Yarra.
 * <p/> 
 * This Servlet returns a json containing attributes/facilities available in the
 * restaurants of a market.
 */
@SuppressWarnings("serial")
//@SlingServlet(paths = { "/services/mcd/us/rlfilter" }, methods = "GET", extensions = "json")
@SlingServlet(resourceTypes = {"mcd-us/components/page/siteconfig"}, methods = "GET", selectors = "restaurantUserAttributes", extensions = "json")
@Properties(value = {
		@Property(name = "service.pid", value = "com.mcd.rwd.us.core.servlets.RestaurantFilters", propertyPrivate = false),
		@Property(name = "service.description", value = "Provides restaurant attributes in json format", propertyPrivate = false),
		@Property(name = "service.vendor", value = "HCL Technologies", propertyPrivate = false) })
public class RestaurantFilters extends SlingSafeMethodsServlet {

	/**
	 * default LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantFilters.class);

	/**
	 * text label
	 */
	private static final String TEXT_LABEL = "text";

	/**
	 * value label
	 */
	private static final String VALUE_LABEL = "value";

	/**
	 * market label
	 */
	private static final String LABEL_MARKET = "market";

	/**
	 * API_KEY attribute
	 */
	private static final String API_KEY_ATTRIBUTE = "mcd_apikey";

	@Reference
	private transient McdFactoryConfigConsumer mcdRdFactoryConfigConsumer;

	@Reference
	private transient McdWebServicesConfig mcdWebServicesConfig;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException,
			IOException {
		Resource resource = request.getResource();
		String resourceCountryCode = ResourceUtil
				.getCountryCodeFromResource(resource);
		String resourceLanguageCode = ResourceUtil
				.getLanguageCodeFromResource(resource);
		if (resourceCountryCode != null && resourceLanguageCode != null) {
			McdFactoryConfig mcdFactoryConfig = mcdRdFactoryConfigConsumer.getMcdFactoryConfig(resourceCountryCode, resourceLanguageCode);
			if (mcdFactoryConfig != null) {/*
				String restaurantLocatorApiKey = mcdFactoryConfig.getRestaurantLocatorApiKey();
				String restaurantLocatorCountry = mcdFactoryConfig.getRestaurantLocatorCountry();
				String restaurantLocatorUrl = mcdWebServicesConfig.getRestaurantLocatorUrl();
				String restaurantLocatorAttributeUrl = mcdWebServicesConfig.getRestaurantAttributeUrl();
				String requiredUrl = restaurantLocatorUrl + restaurantLocatorAttributeUrl
						+ ApplicationConstants.URL_QS_START + LABEL_MARKET + ApplicationConstants.PN_EQUALS
						+ restaurantLocatorCountry.toUpperCase();
				LOGGER.info("attribute requiredurl " + requiredUrl);
				Map<String, String> header = new HashMap<String, String>();
				LOGGER.info("mcdonalds api key" + restaurantLocatorApiKey);
				header.put(API_KEY_ATTRIBUTE, restaurantLocatorApiKey);
				ConnectionUtil conUtil = new ConnectionUtil();
				LOGGER.info("restaurant attribute url" + requiredUrl + "\t api key" + restaurantLocatorApiKey);
				String restResponse = conUtil.sendGet(requiredUrl, header);
				LOGGER.debug("restaurant response" + restResponse);
				response.setContentType(ApplicationConstants.CONTENT_TYPE_JSON);
				PrintWriter out = response.getWriter();
				out.print(getRequiredJson(restResponse, requiredUrl));

			*/
				/*URL url = new URL(request.getHeader("referer").toString());
				String proto = url.getProtocol();
				String authority = url.getAuthority();
				String domain = String.format("%s://%s:%d", proto, authority);*/
				String restaurantLocatorCountry = mcdFactoryConfig.getRestaurantLocatorCountry();
				String restaurantLocatorUrl = mcdWebServicesConfig.getRestaurantLocatorUrl();
				String requiredUrl = restaurantLocatorUrl+"&country="+restaurantLocatorCountry.toUpperCase();
				LOGGER.info("attribute requiredurl " + requiredUrl);
				ConnectionUtil conUtil = new ConnectionUtil();
				String restResponse = conUtil.sendGet(requiredUrl);
				LOGGER.debug("restaurant response" + restResponse);
				response.setContentType(ApplicationConstants.CONTENT_TYPE_JSON);
				PrintWriter out = response.getWriter();
				out.print(getRequiredJson(restResponse, requiredUrl));
			} else {
				LOGGER.error("No Factory Configuration found for the country - {} and language - {}", resourceCountryCode, resourceLanguageCode);
			}
		} else {
			LOGGER.error("No country code or language code found for the resource");
		}
	}

	private JSONArray getRequiredJson(String jsonFromServer, String requestedUrl) {
		RestaurantAttributesResponse [] restAttributeResponse = new Gson().fromJson(jsonFromServer,
				RestaurantAttributesResponse[].class);
		JSONArray restAttributeArray = new JSONArray();
		if (restAttributeResponse != null && restAttributeResponse.length > 0) {
			for (int i = 0; i < restAttributeResponse.length; i++) {
				try {
					RestaurantAttributesResponse attrResponse = restAttributeResponse[i];
					JSONObject restAttributeJson = new JSONObject();
					restAttributeJson.put(TEXT_LABEL, attrResponse.getText());
					restAttributeJson.put(VALUE_LABEL, attrResponse.getText());
					restAttributeArray.put(restAttributeJson);
				} catch (JSONException e) {
					LOGGER.error("JSONException while creating json in RestaurantAttribute class", e);
				}
			}
		} else {
			LOGGER.error("Restaurant Attribute array is either null or empty for the url: {}", requestedUrl);
		}
		return restAttributeArray;

	}
	
	
}
