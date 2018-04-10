package com.mcd.rwd.us.core.datasource;

import com.day.cq.commons.PathInfo;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.icfolson.aem.library.api.request.ComponentServletRequest;
import com.icfolson.aem.library.core.servlets.datasource.AbstractOptionsDataSourceServlet;
import com.icfolson.aem.library.core.servlets.optionsprovider.Option;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sandeepc on 29/06/17.
 */

@SlingServlet(resourceTypes = {
        "mcd-us/dataSource/category",
        "mcd-us/dataSource/relatedproducts",
        "mcd-us/dataSource/nutrition-calculator",
        "mcd-us/dataSource/happyMeal",
        "mcd-us/dataSource/extraValueMeal"}, methods = "GET")
public class CategoryListDataSource extends AbstractOptionsDataSourceServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryListDataSource.class);

    @Reference
    private transient McdFactoryConfigConsumer mcdFactoryConfigConsumer;

    @Reference
    private transient McdWebServicesConfig mcdWebServicesConfig;

    @Override
    protected List<Option> getOptions(ComponentServletRequest componentServletRequest) {
        List<Option> options = new ArrayList<>();
        String categoryListUrl = StringUtils.EMPTY;
        SlingHttpServletRequest request = componentServletRequest.getSlingRequest();
        String resourcePath = null;
        if (null != request.getParameter("item")) {
            resourcePath = request.getParameter("item");
        }
        else {
            resourcePath = new PathInfo((String)
                    request.getAttribute("javax.servlet.include.path_info")).getSuffix();
        }
        if (StringUtils.isNotEmpty(resourcePath)) {
            Resource resource = componentServletRequest.getResourceResolver().getResource(resourcePath);
            ValueMap properties = componentServletRequest.getProperties();
            String comp = null != properties ? properties.get("comp", String.class) : null;

            String categoryType = "1";
            if ("extraValueMeal".equals(comp)) {
                categoryType = "4";
            }
            if ("happy-meal".equals(comp)) {
                categoryType = "2";
            }
            String resourceCountryCode = ResourceUtil.getCountryCodeFromResource(resource);
            String resourceLanguageCode = ResourceUtil.getLanguageCodeFromResource(resource);
            LOGGER.info(" In CategoryList  Resource Country Code is" + resourceCountryCode + "Resource Language Code is"
                    + resourceLanguageCode);
            if (resourceCountryCode != null && resourceLanguageCode != null) {
                McdFactoryConfig mcdFactoryConfig = mcdFactoryConfigConsumer.getMcdFactoryConfig(resourceCountryCode,
                        resourceLanguageCode);
                if (mcdFactoryConfig != null) {
                    categoryListUrl = getCategoryListUrl(mcdFactoryConfig,categoryType);
                }
            }
            LOGGER.debug("In CategoryList url to Call DNA WebService is" + categoryListUrl);
            String categoriesDetails;
            ConnectionUtil connUtil = new ConnectionUtil();
            categoriesDetails = connUtil.sendGet(categoryListUrl);

            if (categoriesDetails != null) {
                LOGGER.debug("In CategoryList response obtained from DNA WEbService");
                getCategoryJsonFromCategoryResponse(categoryListUrl, categoriesDetails, options, comp);
            }
            else {
                LOGGER.error("response from the web service: {} is null", categoryListUrl);
            }
        }
        return options;
    }

    /**
     * @param categoryListUrl
     * @param categoriesDetails
     * @param options
     */
    private void getCategoryJsonFromCategoryResponse(String categoryListUrl, String categoriesDetails,
                                                     List<Option> options, String component) {
        Type categoryTypeToken = new TypeToken<List<Category>>() {}.getType();
        Gson gson = new GsonBuilder().registerTypeAdapter(categoryTypeToken, new CategoryAdapter()).create();
        CategoriesResponse categoryResponse = gson.fromJson(categoriesDetails, CategoriesResponse.class);
        if (categoryResponse != null) {
            Categories categories = categoryResponse.getCategories();
            if (categories != null) {
                List<Category> categoryArrayFromServer = categories.getCategory();
                if (categoryArrayFromServer != null && categoryArrayFromServer.size() > 0)
                    processCategoryjson(options, component, categoryArrayFromServer);
                else {
                    LOGGER.error(
                            "Categories array associated with the key 'category' is either null "
                                    + "or empty for the url: {}", categoryListUrl);
                }
            } else {
                LOGGER.error(
                        "No categories found corresponding to the key 'categories' in the url:" +
                                " {}", categoryListUrl);
            }
        } else {
            LOGGER.error(
                    "unable to parse json for the category list. Json might be null or " +
                            "incorrect for the url: {}", categoryListUrl);
        }
    }

    private void processCategoryjson(List<Option> options, String component, List<Category> categoryArrayFromServer) {
        for (int i = 0; i < categoryArrayFromServer.size(); i++) {
            Category category = categoryArrayFromServer.get(i);
            Option categoryOption = null;
            if (component!=null && ("nutrition-calculator".equals(component) || "extraValueMeal".equals(component)
                    || "happy-meal".equals(component))) {
                categoryOption = new Option( category.getCategoryId()+"_"+category.getCategoryName(),
                        category.getCategoryName());
            }
            else {
                categoryOption = new Option( Integer.toString(category.getCategoryId()), category.getCategoryName());
            }
            options.add(categoryOption);
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


