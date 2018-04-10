package com.mcd.rwd.us.core.servlets;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
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
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mcd.rwd.global.core.utils.ConnectionUtil;
import com.mcd.rwd.global.core.utils.ResourceUtil;
import com.mcd.rwd.us.core.bean.category.Categories;
import com.mcd.rwd.us.core.bean.category.CategoriesResponse;
import com.mcd.rwd.us.core.bean.category.Category;
import com.mcd.rwd.us.core.bean.evm.CategoryAdapter;
import com.mcd.rwd.us.core.constants.ApplicationConstants;
import com.mcd.rwd.us.core.service.McdFactoryConfig;
import com.mcd.rwd.us.core.service.McdFactoryConfigConsumer;
import com.mcd.rwd.us.core.service.McdWebServicesConfig;

/**
 * Created by deepti_b on 3/4/2016.
 */
@SuppressWarnings("serial")
@SlingServlet(resourceTypes = {"mcd-us/components/content/category",
        "mcd-us/components/content/relatedproducts","mcd-us/components/content/nutrition-calculator","mcd-us/components/content/happyMeal","mcd-us/components/content/extraValueMeal"}, methods = "GET", selectors = "categorylist", extensions = "json")
@Properties(value = {
        @Property(name = "service.pid", value = "com.mcd.rwd.us.core.servlets.CategoryList",
                propertyPrivate = false),
        @Property(name = "service.description", value = "Provides category in json format (key-value)",
                propertyPrivate = false),
        @Property(name = "service.vendor", value = "HCL Technologies", propertyPrivate = false)})
public class CategoryList extends SlingSafeMethodsServlet {

    /**
     * text label
     */
    private static final String TEXT_LABEL = "text";

    /**
     * value label
     */
    private static final String VALUE_LABEL = "value";

    /**
     * default logger
     */
    private final transient Logger logger = LoggerFactory.getLogger(getClass());

    @Reference
    private transient McdFactoryConfigConsumer mcdFactoryConfigConsumer;

    @Reference
    private transient McdWebServicesConfig mcdWebServicesConfig;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType(ApplicationConstants.CONTENT_TYPE_JSON);
        String categoryListUrl = StringUtils.EMPTY;
        Resource resource = request.getResource();
        String component = request.getParameter("comp");
        String categoryType = "1";
        if ("extraValueMeal".equals(request.getParameter("comp"))) {
        	categoryType = "4";
        }
        if ("happy-meal".equals(request.getParameter("comp"))) {
        	categoryType = "2";
        }
        String resourceCountryCode = ResourceUtil.getCountryCodeFromResource(resource);
        String resourceLanguageCode = ResourceUtil.getLanguageCodeFromResource(resource);
        logger.info(" In CategoryList  Resource Country Code is" + resourceCountryCode + "Resource Language Code is" + resourceLanguageCode);
        if (resourceCountryCode != null && resourceLanguageCode != null) {
            McdFactoryConfig mcdFactoryConfig = mcdFactoryConfigConsumer.getMcdFactoryConfig(resourceCountryCode, resourceLanguageCode);
            if (mcdFactoryConfig != null) {
                categoryListUrl = getCategoryListUrl(mcdFactoryConfig,categoryType);
            }
        }
        logger.debug("In CategoryList url to Call DNA WebService is" + categoryListUrl);
        String categoriesDetails;
        ConnectionUtil connUtil = new ConnectionUtil();
        categoriesDetails = connUtil.sendGet(categoryListUrl);
        JSONArray categoryArray = new JSONArray();
        if (categoriesDetails != null) {
            logger.debug("In CategoryList response obtained from DNA WEbService");
            getCategoryJsonFromCategoryResponse(categoryListUrl, categoriesDetails, categoryArray, component);
        } else {
            logger.error("response from the web service: {} is null", categoryListUrl);
        }
        try {
            response.getWriter().print(categoryArray);
        } catch (IOException ioe) {
            logger.error("Exception in CategoryList", ioe);
        }
    }

    /**
     * @param categoryListUrl
     * @param categoriesDetails
     * @param categoryArray
     */
    private void getCategoryJsonFromCategoryResponse(String categoryListUrl, String categoriesDetails, JSONArray categoryArray, String component) {
    	Type categoryTypeToken = new TypeToken<List<Category>>() {
		}.getType();
    	Gson gson = new GsonBuilder().registerTypeAdapter(categoryTypeToken, new CategoryAdapter()).create();
        CategoriesResponse categoryResponse = gson.fromJson(categoriesDetails, CategoriesResponse.class);
        if (categoryResponse != null) {
            Categories categories = categoryResponse.getCategories();
            if (categories != null) {
                List<Category> categoryArrayFromServer = categories.getCategory();
                if (categoryArrayFromServer != null && categoryArrayFromServer.size() > 0)
                    processCategoryjson(categoryArray, component, categoryArrayFromServer);
                else {
                    logger.error(
                            "Categories array associated with the key 'category' is either null "
                                    + "or empty for the url: {}", categoryListUrl);
                }
            } else {
                logger.error(
                        "No categories found corresponding to the key 'categories' in the url:" +
                                " {}", categoryListUrl);
            }
        } else {
            logger.error(
                    "unable to parse json for the category list. Json might be null or " +
                            "incorrect for the url: {}", categoryListUrl);
        }
    }

    private void processCategoryjson(JSONArray categoryArray, String component, List<Category> categoryArrayFromServer) {
        for (int i = 0; i < categoryArrayFromServer.size(); i++) {
            try {
                Category category = categoryArrayFromServer.get(i);
                JSONObject categoriesJson = new JSONObject();
                categoriesJson.put(TEXT_LABEL, category.getCategoryName());

                if (component!=null && ("nutrition-calculator".equals(component) || "extraValueMeal".equals(component) || "happy-meal".equals(component))) {
                    categoriesJson.put(VALUE_LABEL, category.getCategoryId()+"_"+category.getCategoryName());
                }
                else {
                    categoriesJson.put(VALUE_LABEL, category.getCategoryId());
                }

                categoryArray.put(categoriesJson);

            } catch (JSONException e) {
                logger.error(
                        "JSONException while creating json in CategoryList class", e);
            }
        }
    }

    private String getCategoryListUrl(McdFactoryConfig mcdFactoryConfig, String categoryType) {
        StringBuilder url = new StringBuilder();
        url.append(mcdWebServicesConfig.getDomain()).append(mcdWebServicesConfig.getCategoryListUrl()).append(ApplicationConstants.URL_QS_START)
                .append(ApplicationConstants.PN_COUNTRY).append(ApplicationConstants.PN_EQUALS).append(mcdFactoryConfig.getDnaCountryCode()).append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
                .append(ApplicationConstants.PN_LANGUAGE).append(ApplicationConstants.PN_EQUALS).append(mcdFactoryConfig.getDnaLanguageCode()).append(ApplicationConstants.URL_QS_DELIMITER_CHAR).append(ApplicationConstants.PN_SHOW_LIVE_DATA)
                .append(ApplicationConstants.PN_EQUALS).append(true).append(ApplicationConstants.URL_QS_DELIMITER_CHAR).append(ApplicationConstants.PN_CATEGORY_TYPE).append(ApplicationConstants.PN_EQUALS).append(categoryType);

        return url.toString();
    }

}
