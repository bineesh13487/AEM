package com.mcd.rwd.us.core.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mcd.rwd.global.core.utils.ConnectionUtil;
import com.mcd.rwd.global.core.utils.LinkUtil;
import com.mcd.rwd.us.core.bean.category.*;
import com.mcd.rwd.us.core.bean.evm.Coop;
import com.mcd.rwd.us.core.bean.evm.CoopAdapter;
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
 * Created by deepti_b on 4/22/2016.
 */

@SlingServlet(
        paths = {"/services/mcd/goesWellWithList"},
        methods = {"GET"},extensions = "json"
)
@Properties(value = {
        @Property(name = "service.pid", value = "com.mcd.rwd.us.core.servlets.GoesWellWithList",
                propertyPrivate = false),
        @Property(name = "service.description", value = "Provides goes well data for a Particular Product  in json format (key-value)",
                propertyPrivate = false),
        @Property(name = "service.vendor", value = "HCL Technologies", propertyPrivate = false)})
public class GoesWellWithList extends SlingSafeMethodsServlet {

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
        String productID = request.getParameter("itemID");
        String showNationalItemsFlag = request.getParameter("showNationalCoopType");
        String showLiveData = request.getParameter("showLiveData");
        if (StringUtils.isBlank(showLiveData)) {
            showLiveData = "true";       /*Deafult showLivedata is set to true*/
        }
        String coopID = request.getParameter("coopID");
        configMap = populateOsgiConfigurations(resourceCountryCode, resourceLanguageCode,showLiveData);
        Map<String,List<CategoryItemInfo>> object= retrieveItemsInfoFromServer(productID,showNationalItemsFlag,coopID);
        String itemsInfoResponse = new Gson().toJson(object);
        try {
            response.getWriter().print(itemsInfoResponse);
        } catch (IOException ioe) {
            logger.error("Exception in GoesWellWithList while writing response", ioe);
        }

    }

    private String getGoesWellWithUrl(String productID,String showNationalItemsFlag,String coopID){
        StringBuilder itemUrl = new StringBuilder();
        itemUrl.append(configMap.get("goesWellWithUrl")).append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
                .append(ApplicationConstants.PN_ITEM_ID).append(ApplicationConstants.PN_EQUALS).append(productID)
                .append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
                .append(ApplicationConstants.PN_COPS_FILTER).append(ApplicationConstants.PN_EQUALS)
                .append(true).append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
                .append(ApplicationConstants.PN_SHOW_NATIONAL_COOP).append(ApplicationConstants.PN_EQUALS)
                .append(showNationalItemsFlag).append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
                .append(ApplicationConstants.PN_COPS_ID).append(ApplicationConstants.PN_EQUALS)
                .append(coopID);;
        return itemUrl.toString();

    }


    public Map<String,List<CategoryItemInfo>> retrieveItemsInfoFromServer(String productID, String showNationalItemsFlag, String coopID) {
        Map<String,List<CategoryItemInfo>> map=new HashMap<String,List<CategoryItemInfo>>();
        ConnectionUtil connUtil = new ConnectionUtil();
        List itemsList =new ArrayList<CategoryItemInfo>();
        String itemsResponse;
        try{
            String itemDetailFinalUrl=getGoesWellWithUrl(productID,showNationalItemsFlag,coopID);
            logger.debug("In GoesWellWithList Requesting ... {}", itemDetailFinalUrl);
            itemsResponse = connUtil.sendGet(itemDetailFinalUrl);
            // parse response(using gson) coming from server
            Type itemTypeToken = new TypeToken<CategoryItem[]>() {
            }.getType();
            Type relationTypeToken = new TypeToken<List<RelationType>>() {
            }.getType();
            Type relatedItemTypeToken = new TypeToken<List<RelatedItem>>() {
            }.getType();
            Type coopTypeToken = new TypeToken<List<Coop>>() {
            }.getType();
            Gson gson = new GsonBuilder().registerTypeAdapter(relationTypeToken, new RelationTypeAdapter())
                    .registerTypeAdapter(coopTypeToken, new CoopAdapter())
                    .registerTypeAdapter(itemTypeToken, new CategoryItemTypeAdapter())
                    .registerTypeAdapter(relatedItemTypeToken, new RelatedItemTypeAdapter()).create();
            CategoryItemsDetail categoryItemsResponse = gson.fromJson(itemsResponse, CategoryItemsDetail.class);
            if (categoryItemsResponse != null)
                itemsList = addItemList(categoryItemsResponse);
            else{
                logger.error("Error in GoesWellWithList class Unable to parse json for the product_id {} . Json might be null or "
                                + "incorrect for the url: {}", itemDetailFinalUrl);
            }
            logger.debug("In GoesWellWithList Number of Items Obtained fron server and Sending in response is"+itemsList.size());
            map.put("items", itemsList);
        }catch (Exception e){
            logger.error("Error in GoesWellWithList class", e);
        }

        return map;
    }

    private List<CategoryItemInfo> addItemList(CategoryItemsDetail categoryItemsResponse) {
        List itemsList =new ArrayList<CategoryItemInfo>();
        CategoryItems catItems = categoryItemsResponse.getItems();
        if(catItems !=null){
            CategoryItem[] catItemArray = catItems.getItem();
            if (catItemArray != null && catItemArray.length > 0) {
                for (int catItemIndex = 0; catItemIndex < catItemArray.length; catItemIndex++) {
                    CategoryItemInfo catItemInfo = new CategoryItemInfo();
                    catItemInfo.setId(String.valueOf(catItemArray[catItemIndex].getId()));
                    catItemInfo.setExternalId(catItemArray[catItemIndex].getExternalId());
                    catItemInfo.setMarketingName(catItemArray[catItemIndex].getItemMarketingName());
                    catItemInfo.setName(catItemArray[catItemIndex].getItemName());
                    catItemInfo.setImagePath(getCategoryItemImagePath(catItemArray[catItemIndex]));
                    catItemInfo.setPath(getCategoryItemPageURL(catItemArray[catItemIndex]));
                    itemsList.add(catItemInfo);
                }
            }
        }
        return itemsList;
    }


    private String getCategoryItemPageURL(CategoryItem catItem) {
        String catItemPageUrl;
        Boolean productShortUrlRequired = Boolean.valueOf(configMap.get("productShortUrlRequired"));
        String productPagePath = configMap.get("productPagePath");
        if (productShortUrlRequired) {
            catItemPageUrl = LinkUtil.getProductShortUrl(productPagePath,
                    catItem.getShortName());
        } else {
            catItemPageUrl = LinkUtil.getProductUrl(productPagePath, catItem.getExternalId());
        }
        return catItemPageUrl;
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


    /**
     * Populates the properties from the OSGi Config.
     */
    private Map<String, String> populateOsgiConfigurations(String resourceCountryCode, String resourceLanguageCode,String showLiveData) {
        Map<String, String> data = new HashMap<String, String>();
        StringBuilder url = new StringBuilder();
        if (resourceCountryCode != null && resourceLanguageCode != null) {
            McdFactoryConfig mcdFactoryConfig = mcdFactoryConfigConsumer.getMcdFactoryConfig(resourceCountryCode, resourceLanguageCode);
            if (mcdFactoryConfig != null) {
                url.append(mcdWebServicesConfig.getDomain())
                        .append(mcdWebServicesConfig.getGoesWellWithUrl())
                        .append(ApplicationConstants.URL_QS_START).append(ApplicationConstants.PN_COUNTRY)
                        .append(ApplicationConstants.PN_EQUALS)
                        .append(mcdFactoryConfig.getDnaCountryCode())
                        .append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
                        .append(ApplicationConstants.PN_LANGUAGE).append(ApplicationConstants.PN_EQUALS)
                        .append(mcdFactoryConfig.getDnaLanguageCode())
                        .append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
                        .append(ApplicationConstants.PN_SHOW_LIVE_DATA)
                        .append(ApplicationConstants.PN_EQUALS).append(showLiveData);
                data.put("productShortUrlRequired", Boolean.toString(mcdFactoryConfig.isProductShortUrlRequired()));
                data.put("productPagePath", mcdFactoryConfig.getProductPagePath());
                data.put("goesWellWithUrl",url.toString());
            }
        }
        return data;
    }
}
