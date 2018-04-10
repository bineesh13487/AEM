package com.mcd.rwd.us.core.servlets;

import com.google.gson.Gson;
import com.mcd.rwd.global.core.utils.ConnectionUtil;
import com.mcd.rwd.global.core.utils.ResourceUtil;
import com.mcd.rwd.us.core.bean.product.Nutrient;
import com.mcd.rwd.us.core.bean.product.NutrientFacts;
import com.mcd.rwd.us.core.bean.product.NutrientsResponse;
import com.mcd.rwd.us.core.constants.ApplicationConstants;
import com.mcd.rwd.us.core.service.McdFactoryConfig;
import com.mcd.rwd.us.core.service.McdFactoryConfigConsumer;
import com.mcd.rwd.us.core.service.McdWebServicesConfig;
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

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

/**
 * Created by deepti_b on 3/21/2016.
 */
@SuppressWarnings("serial")
@SlingServlet(resourceTypes = {"mcd-us/components/content/category",
        "mcd-us/components/page/productdetail","mcd-us/components/content/nutrition-calculator"}, methods = "GET", selectors = "nutrientsList",
        extensions = "json")
@Properties(value = {
        @Property(name = "service.pid", value = "com.mcd.rwd.us.core.servlets.NutrientsList",
                propertyPrivate = false),
        @Property(name = "service.description", value = "Provides Nutrients in json format (key-value)",
                propertyPrivate = false),
        @Property(name = "service.vendor", value = "HCL Technologies", propertyPrivate = false)
})
public class NutrientsList extends SlingSafeMethodsServlet {

    /**
     * default logger
     */
    private static final Logger logger = LoggerFactory.getLogger(NutrientsList.class);

    /**
     * text label
     */
    private static final String TEXT_LABEL = "text";

    /**
     * value label
     */
    private static final String VALUE_LABEL = "value";

    @Reference
    private transient McdFactoryConfigConsumer mcdFactoryConfigConsumer;

    @Reference
    private transient McdWebServicesConfig mcdWebServicesConfig;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        Resource resource = request.getResource();
        String component = request.getParameter("comp");
        String resourceCountryCode = ResourceUtil.getCountryCodeFromResource(resource);
        String resourceLanguageCode = ResourceUtil.getLanguageCodeFromResource(resource);
        logger.info(" In NutrientsList  Resource Country Code is" + resourceCountryCode + "Resource Language Code is" + resourceLanguageCode);
        if (resourceCountryCode != null && resourceLanguageCode != null) {
            McdFactoryConfig mcdFactoryConfig = mcdFactoryConfigConsumer
                    .getMcdFactoryConfig(resourceCountryCode, resourceLanguageCode);
            if (mcdFactoryConfig != null) {
                String nutrientFactsUrl = getNutrientFactsUrl(mcdFactoryConfig);
                String nutrientsDetails;
                logger.debug("In NutrientsList url to Call DNA WebService to fetch Nutrients Informationis  is" + nutrientFactsUrl);
                ConnectionUtil connUtil = new ConnectionUtil();
                nutrientsDetails = connUtil.sendGet(nutrientFactsUrl);

                JSONArray nutrientArray = new JSONArray();

                if (nutrientsDetails != null) {
                    logger.debug("In NutrientsList response obtained from DNA WEbService for Nutrients");
                    getJSonResponseFromNutrientData(nutrientFactsUrl, nutrientsDetails, nutrientArray, component);

                } else {
                    logger.error("response from the web service: {} is null", nutrientFactsUrl);
                }
                response.setContentType(ApplicationConstants.CONTENT_TYPE_JSON);
                try {
                    response.getWriter().print(nutrientArray);
                } catch (IOException ioe) {
                    logger.error("Exception in NutrientsList", ioe);
                }
            } else {
                logger.error("No Factory Configuration found for the country - {} and language - {}",
                        resourceCountryCode, resourceLanguageCode);
            }
        } else {
            logger.error("No country code or language code found for the resource");
        }
    }

    /**
     * @param nutrientFactsUrl
     * @param nutrientsDetails
     * @param nutrientArray
     */
    private void getJSonResponseFromNutrientData(String nutrientFactsUrl, String nutrientsDetails,
                                                 JSONArray nutrientArray, String component) {
        NutrientsResponse nutrientsResponse = new Gson().fromJson(nutrientsDetails, NutrientsResponse.class);
        if (nutrientsResponse != null) {
            NutrientFacts nutrientFacts = nutrientsResponse.getNutrientFacts();
            if (nutrientFacts != null) {
                List<Nutrient> nutrients = nutrientFacts.getNutrient();

                if (nutrients != null && !nutrients.isEmpty())
                    processNutrientJsonResponse(nutrientArray, component, nutrients);
                else {
                    logger.error("Nutrients array associated with the key 'nutrient' is either null" +
                            " " +
                            "or empty for the url: {}", nutrientFactsUrl);
                }
            } else {
                logger.error("No nutrient facts found for the key 'nutrient_facts' in the url: {}",
                        nutrientFactsUrl);
            }
        } else {
            logger.error("Unable to parse json for the nutrient facts. Json might be null or "
                    + "incorrect for the url: {}", nutrientFactsUrl);
        }
    }

    private void processNutrientJsonResponse(JSONArray nutrientArray, String component, List<Nutrient> nutrients) {
        for (Nutrient nutrient : nutrients) {
            try {
                JSONObject nutrientsJson = new JSONObject();
                nutrientsJson.put(TEXT_LABEL, nutrient.getName());
                if (component!=null && "nutrition-calculator".equals(component)) {
                    nutrientsJson.put(VALUE_LABEL, nutrient.getId()+"_"+nutrient.getName());
                }
                else {
                    nutrientsJson.put(VALUE_LABEL, nutrient.getId());
                }
                nutrientArray.put(nutrientsJson);
            } catch (JSONException e) {
                logger.error("JSONException while creating json in NutrientsList class", e);
            }
        }
    }

    /**
     * @param mcdFactoryConfig
     * @return
     */
    private String getNutrientFactsUrl(McdFactoryConfig mcdFactoryConfig) {
        StringBuilder url = new StringBuilder();
        url.append(mcdWebServicesConfig.getDomain()).append(mcdWebServicesConfig.getNutrientFactsUrl()).append(ApplicationConstants.URL_QS_START).append(ApplicationConstants.PN_COUNTRY).append(ApplicationConstants.PN_EQUALS).append(mcdFactoryConfig.getDnaCountryCode()).append(ApplicationConstants.URL_QS_DELIMITER_CHAR).append(ApplicationConstants.PN_LANGUAGE)
                .append(ApplicationConstants.PN_EQUALS).append(mcdFactoryConfig.getDnaLanguageCode()).append(ApplicationConstants.URL_QS_DELIMITER_CHAR).append(ApplicationConstants.PN_SHOW_LIVE_DATA).append(ApplicationConstants.PN_EQUALS).append(true);
        return url.toString();
    }
}
