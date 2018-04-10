package com.mcd.rwd.us.core.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mcd.rwd.global.core.utils.ConnectionUtil;
import com.mcd.rwd.global.core.utils.LinkUtil;
import com.mcd.rwd.us.core.bean.category.*;
import com.mcd.rwd.us.core.bean.evm.Coop;
import com.mcd.rwd.us.core.bean.evm.CoopAdapter;
import com.mcd.rwd.us.core.bean.product.LocalItemsResponse;
import com.mcd.rwd.us.core.constants.ApplicationConstants;
import com.mcd.rwd.us.core.service.McdFactoryConfig;
import com.mcd.rwd.us.core.service.McdFactoryConfigConsumer;
import com.mcd.rwd.us.core.service.McdWebServicesConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by deepti_b on 5/21/2016.
 */
@SlingServlet(
        paths = {"/services/mcd/localItemsList"},
        methods = {"GET"},extensions = "json"
)
@Properties(value = {
        @Property(name = "service.pid", value = "com.mcd.rwd.us.core.servlets.LocalItemsList",
                propertyPrivate = false),
        @Property(name = "service.description", value = "Provides local Itrems data  in json format (key-value)",
                propertyPrivate = false),
        @Property(name = "service.vendor", value = "HCL Technologies", propertyPrivate = false)})
public class LocalItemsList extends SlingSafeMethodsServlet {
    private static final String GOES_WELL_WITH_URL = "goesWellWithUrl";
    /**
     * default logger
     */
    private final transient Logger logger = LoggerFactory.getLogger(getClass());
    @Reference
    private transient McdWebServicesConfig mcdWebServicesConfig;

    @Reference
    private transient McdFactoryConfigConsumer mcdFactoryConfigConsumer;


    private Map<String, String> configMap;
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType(ApplicationConstants.CONTENT_TYPE_JSON);
        String resourceCountryCode = request.getParameter("country");
        String resourceLanguageCode = request.getParameter("language");
        String coopID = request.getParameter("coopID");
        String showLiveData = request.getParameter("showLiveData");
        if (StringUtils.isBlank(showLiveData)) {
            showLiveData = "true";       /*Deafult showLivedata is set to true*/
        }
        configMap = populateOsgiConfigurations(resourceCountryCode, resourceLanguageCode,coopID,showLiveData);
        Map<String,List<CategoryItemInfo>> object= retrieveItemsInfoFromServer();
        String itemsInfoResponse = new Gson().toJson(object);
        try {
            response.getWriter().print(itemsInfoResponse);
        } catch (IOException ioe) {
            logger.error("Exception in LocalItemsList while writing response", ioe);
        }
    }

    public Map<String,List<CategoryItemInfo>> retrieveItemsInfoFromServer() {
        Map<String,List<CategoryItemInfo>> map=new HashMap<String,List<CategoryItemInfo>>();
        ConnectionUtil connUtil = new ConnectionUtil();
        List itemsList =new ArrayList<CategoryItemInfo>();
        String itemsResponse;
        try{
            logger.info("In LocalItemsList Requesting ... {}", configMap.get(GOES_WELL_WITH_URL));
            itemsResponse = connUtil.sendGet(configMap.get(GOES_WELL_WITH_URL));
            // parse response(using gson) coming from server
            Type itemTypeToken = new TypeToken<CategoryItem[]>() {
            }.getType();
            Type relationTypeToken = new TypeToken<List<RelationType>>() {
            }.getType();
            Type relatedItemTypeToken = new TypeToken<List<RelatedItem>>() {
            }.getType();
            Type coopTypeToken = new TypeToken<List<Coop>>() {
            }.getType();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(itemTypeToken, new CategoryItemTypeAdapter())
                    .registerTypeAdapter(relationTypeToken, new RelationTypeAdapter())
                    .registerTypeAdapter(coopTypeToken, new CoopAdapter())
                    .registerTypeAdapter(relatedItemTypeToken, new RelatedItemTypeAdapter()).create();
            LocalItemsResponse itemResponse = gson.fromJson(itemsResponse, LocalItemsResponse.class);
            if (itemResponse != null)
                itemsList = processItemsResponse(itemResponse);
            else{
                logger.error("Error in LocalItemsList class Unable to parse json  Json might be null or "
                        + "incorrect for the url: {}", configMap.get(GOES_WELL_WITH_URL));
            }
            logger.info("In LocalItemsList Number of Items Obtained fron server and Sending in response is"+itemsList.size());
            map.put("items", itemsList);
        }catch (Exception e){
            logger.error("Error in LocalItemsList class", e);
        }

        return map;
    }

    private List<CategoryItemInfo> processItemsResponse(LocalItemsResponse itemResponse) {
        List itemsList =new ArrayList<CategoryItemInfo>();
        if(itemResponse.getFullMenu()!=null && itemResponse.getFullMenu().getItems()!=null){
            CategoryItem[] catItemArray = itemResponse.getFullMenu().getItems().getItem();
            if(catItemArray != null && catItemArray.length > 0)
                addLocalCategoryItems(itemsList, catItemArray);
        }
        return itemsList;
    }

    private void addLocalCategoryItems(List itemsList, CategoryItem[] catItemArray) {
        for (int j = 0; j < catItemArray.length; j++){
            if(!(ApplicationConstants.DO_NOT_SHOW.equalsIgnoreCase(catItemArray[j].getDoNotShow()))){
                CategoryItemInfo catItemInfo = new CategoryItemInfo();
                catItemInfo.setId(String.valueOf(catItemArray[j].getId()));
                catItemInfo.setExternalId(catItemArray[j].getExternalId());
                catItemInfo.setMarketingName(catItemArray[j].getItemMarketingName());
                catItemInfo.setName(catItemArray[j].getItemName());
                catItemInfo.setImagePath(getCategoryItemImagePath(catItemArray[j]));
                catItemInfo.setPath(getCategoryItemPageURL(catItemArray[j]));
                catItemInfo.setDescription(catItemArray[j].getDescription());
                itemsList.add(catItemInfo);
            }

        }
    }

    private String getCategoryItemImagePath(CategoryItem catItem) {
        String catItemImagePath;
        CategoryItemThumbnailImage categoryItemImage = catItem.getAttachItemThumbnailImage();
        if(categoryItemImage!=null){
            String imageName = categoryItemImage.getImageName();
            if(StringUtils.isNotBlank(imageName)){
                catItemImagePath=imageName;
            }else{
                //NO Name is returned from DNA so default image name is returned
                catItemImagePath=ApplicationConstants.DEFAULT_PRODUCT_IMAGE_NAME;
            }

        }else{
            //NO Name is returned from DNA so default image name is returned
            catItemImagePath=ApplicationConstants.DEFAULT_PRODUCT_IMAGE_NAME;
        }

        return catItemImagePath;
    }
    private String getCategoryItemPageURL(CategoryItem catItem) {
        String catItemPageUrl;
        Boolean productShortUrlRequired = Boolean.valueOf(configMap.get("productShortUrlRequired"));
        String productPagePath = configMap.get("productPagePath");
        if (productShortUrlRequired) {
            catItemPageUrl = LinkUtil.getProductShortUrl(productPagePath, catItem.getShortName());
        } else {
            catItemPageUrl = LinkUtil.getProductUrl(productPagePath, catItem.getExternalId());
        }
        return catItemPageUrl;
    }

    /**
     * Populates the properties from the OSGi Config.
     */
    private Map<String, String> populateOsgiConfigurations(String resourceCountryCode, String resourceLanguageCode,String coopID,String showLiveData) {
        Map<String, String> data = new HashMap<String, String>();
        StringBuilder url = new StringBuilder();
        if (resourceCountryCode != null && resourceLanguageCode != null) {
            McdFactoryConfig mcdFactoryConfig = mcdFactoryConfigConsumer.getMcdFactoryConfig(resourceCountryCode, resourceLanguageCode);
            if (mcdFactoryConfig != null) {
                url.append(mcdWebServicesConfig.getDomain())
                        .append(mcdWebServicesConfig.getGetAllItemsUrl())
                        .append(ApplicationConstants.URL_QS_START).append(ApplicationConstants.PN_COUNTRY)
                        .append(ApplicationConstants.PN_EQUALS)
                        .append(mcdFactoryConfig.getDnaCountryCode())
                        .append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
                        .append(ApplicationConstants.PN_LANGUAGE).append(ApplicationConstants.PN_EQUALS)
                        .append(mcdFactoryConfig.getDnaLanguageCode())
                        .append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
                        .append(ApplicationConstants.PN_SHOW_LIVE_DATA)
                        .append(ApplicationConstants.PN_EQUALS).append(showLiveData).append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
                        .append(ApplicationConstants.PN_COPS_FILTER).append(ApplicationConstants.PN_EQUALS)
                        .append("true").append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
                        .append(ApplicationConstants.PN_SHOW_NATIONAL_COOP).append(ApplicationConstants.PN_EQUALS)
                            .append(false).append(ApplicationConstants.URL_QS_DELIMITER_CHAR) .append(ApplicationConstants.PN_COPS_ID).append(ApplicationConstants.PN_EQUALS)
                        .append(coopID) ;
                data.put("productShortUrlRequired", Boolean.toString(mcdFactoryConfig.isProductShortUrlRequired()));
                data.put("productPagePath", mcdFactoryConfig.getProductPagePath());
                data.put(GOES_WELL_WITH_URL,url.toString());
            }
        }
        return data;
    }
}


