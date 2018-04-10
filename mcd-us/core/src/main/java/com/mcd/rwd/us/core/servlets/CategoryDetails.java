package com.mcd.rwd.us.core.servlets;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mcd.rwd.global.core.utils.ConnectionUtil;
import com.mcd.rwd.global.core.utils.LinkUtil;
import com.mcd.rwd.us.core.bean.category.CategoryDetail;
import com.mcd.rwd.us.core.bean.category.CategoryItem;
import com.mcd.rwd.us.core.bean.category.CategoryItemInfo;
import com.mcd.rwd.us.core.bean.category.CategoryItemThumbnailImage;
import com.mcd.rwd.us.core.bean.category.CategoryItemTypeAdapter;
import com.mcd.rwd.us.core.bean.category.CategoryItems;
import com.mcd.rwd.us.core.bean.category.CategoryItemsDetail;
import com.mcd.rwd.us.core.bean.category.CategoryItemsResponse;
import com.mcd.rwd.us.core.bean.category.RelatedItem;
import com.mcd.rwd.us.core.bean.category.RelatedItemTypeAdapter;
import com.mcd.rwd.us.core.bean.category.RelationType;
import com.mcd.rwd.us.core.bean.category.RelationTypeAdapter;
import com.mcd.rwd.us.core.bean.evm.Coop;
import com.mcd.rwd.us.core.bean.evm.CoopAdapter;
import com.mcd.rwd.us.core.constants.ApplicationConstants;

import com.mcd.rwd.us.core.service.McdFactoryConfig;
import com.mcd.rwd.us.core.service.McdFactoryConfigConsumer;
import com.mcd.rwd.us.core.service.McdWebServicesConfig;

/**
 * Created by deepti_b on 3/9/2016.
 */

@SlingServlet(
		paths = { "/services/mcd/categoryDetails" },
		methods = { "GET" },extensions = "json") @Properties(value = {
		@Property(name = "service.pid", value = "com.mcd.usrwd.core.servlets.CategoryDetails",
				propertyPrivate = false),
		@Property(name = "service.description", value = "Provides category details in json format (key-value)",
				propertyPrivate = false),
		@Property(name = "service.vendor", value = "HCL Technologies", propertyPrivate = false) }) public class CategoryDetails
		extends SlingSafeMethodsServlet {

	/**
	 * default logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryDetails.class);

	@Reference private transient McdWebServicesConfig mcdWebServicesConfig;

	@Reference private transient McdFactoryConfigConsumer mcdFactoryConfigConsumer;

	private Map<String, String> configMap;

	private String cops;
	private String	resourceCountryCode;
	private String	resourceLanguageCode;
	private String	categoryID;
	private String	showLiveData;
	private String	showNationalCoop;

	@Override protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		LOGGER.info("In CategoryDetails Servlet...... ");
		cops="";
		String[] selectorsArray=request.getRequestPathInfo().getSelectors();
		if (selectorsArray != null && selectorsArray.length > 0) {
			LOGGER.info("In CategoryDetails Servlet.....No: Of Selector Obtained from URL is ="+selectorsArray.length);
				LOGGER.info("Country from servlet is="+selectorsArray[0]);
				resourceCountryCode=selectorsArray[0];
				LOGGER.info("Language from servlet is="+selectorsArray[1]);
				resourceLanguageCode=selectorsArray[1];
				LOGGER.info("CategoryId from servlet is="+selectorsArray[2]);
				categoryID=selectorsArray[2];
				LOGGER.info("ShowLiveData Value  from servlet is="+selectorsArray[3]);
				showLiveData=selectorsArray[3];
				LOGGER.info("ShowNationalCoop Value  from servlet is="+selectorsArray[4]);
				showNationalCoop=selectorsArray[4];
				if(selectorsArray.length>=6){
					LOGGER.info("Coop Value from servlet is="+selectorsArray[5]);
					cops=selectorsArray[5];
				}
		}
		configMap = populateOsgiConfigurations();
		JSONObject categoryObj = new JSONObject();
		response.setContentType(ApplicationConstants.CONTENT_TYPE_JSON);
		String jsonString = retrieveCategoriesInfoFromServer();
		try {
			JSONObject obj = new JSONObject(jsonString);
			categoryObj.put("category", obj);
			response.getWriter().print(categoryObj);
		} catch (JSONException e) {
			LOGGER.error("JSONException while creating json in CategoryDetails class", e);
		} catch (IOException ioe) {
			LOGGER.error(ioe.getMessage(), ioe);
		}

	}

	/**
	 * Method retrieves Categories Information from DNA Webservice
	 */
	public String retrieveCategoriesInfoFromServer() {
		ConnectionUtil connUtil = new ConnectionUtil();
		String catDetailResponse = StringUtils.EMPTY;
		try {

			String catDetailFinalUrl = getCategoryDetailsUrl();
			LOGGER.info(" In CategoryDetails Requesting ... {}", catDetailFinalUrl);
			String itemsResponse;
			itemsResponse = connUtil.sendGet(catDetailFinalUrl);
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
			CategoryItemsResponse categoryItemsResponse = gson
					.fromJson(itemsResponse, CategoryItemsResponse.class);
			if (categoryItemsResponse != null) {
				CategoryDetail obj = processCategoryDetailsItemFromServer(categoryItemsResponse);
				catDetailResponse = new Gson().toJson(obj);
			} else {
				LOGGER.error("Unable to parse json for the category id {} . Json might be null or "
						+ "incorrect for the url: {}", catDetailFinalUrl);
			}
		} catch (Exception e) {
			LOGGER.error("Error in CategoryDetails class", e);
		}

		return catDetailResponse;
	}

	/**
	 * Method retrieves Category Url to be queried from DNA
	 */
	private String getCategoryDetailsUrl() {
		StringBuilder categoryDetails = new StringBuilder();

		if (StringUtils.isBlank(categoryID)) {
			categoryID = Integer.toString(ApplicationConstants.DEFAULT_INT);
		}
		if (StringUtils.isBlank(showLiveData)) {
			showLiveData = "true";       /*Deafult showLivedata is set to true*/
		}

		if (resourceCountryCode != null && resourceLanguageCode != null) {
			McdFactoryConfig mcdFactoryConfig = mcdFactoryConfigConsumer
					.getMcdFactoryConfig(resourceCountryCode, resourceLanguageCode);
			if (mcdFactoryConfig != null && mcdWebServicesConfig != null) {
				categoryDetails.append(mcdWebServicesConfig.getDomain())
						.append(mcdWebServicesConfig.getCategoryDetailUrl())
						.append(ApplicationConstants.URL_QS_START).append(ApplicationConstants.PN_COUNTRY)
						.append(ApplicationConstants.PN_EQUALS).append(mcdFactoryConfig.getDnaCountryCode())
						.append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
						.append(ApplicationConstants.PN_LANGUAGE).append(ApplicationConstants.PN_EQUALS)
						.append(mcdFactoryConfig.getDnaLanguageCode())
						.append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
						.append(ApplicationConstants.PN_SHOW_LIVE_DATA).append(ApplicationConstants.PN_EQUALS)
						.append(showLiveData).append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
						.append(ApplicationConstants.PN_CATEGORY_ID).append(ApplicationConstants.PN_EQUALS)
						.append(categoryID).append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
						.append(ApplicationConstants.PN_COPS_FILTER).append(ApplicationConstants.PN_EQUALS)
						.append("true").append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
						.append(ApplicationConstants.PN_SHOW_NATIONAL_COOP).append(ApplicationConstants.PN_EQUALS)
				        .append(showNationalCoop).append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
						.append(ApplicationConstants.PN_COPS_ID).append(ApplicationConstants.PN_EQUALS)
						//Start for backlog update 
						.append(cops).append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
						.append(ApplicationConstants.NUTRITION_ATTRIBUTE_ID).append(ApplicationConstants.PN_EQUALS)
						.append(mcdFactoryConfig.getNutrition_attribute_id_val()).append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
						.append(ApplicationConstants.ITEM_ATTRIBUTE_ID).append(ApplicationConstants.PN_EQUALS)
						.append(mcdFactoryConfig.getItem_attribute());
						
						
			}
		}
			return categoryDetails.toString();

	}

	/**
	 * Method process categories Information obtained from DNA
	 */
	private CategoryDetail processCategoryDetailsItemFromServer(CategoryItemsResponse categoryItemsResponse) {
		CategoryDetail categoryDetail = new CategoryDetail();
		List<CategoryItemInfo> catItemInfoList=new ArrayList<CategoryItemInfo>();
		CategoryItemsDetail categoryItemDetailsFromServer = categoryItemsResponse.getCategory();
		if (categoryItemDetailsFromServer != null) {
			categoryDetail.setDescription(categoryItemDetailsFromServer.getCategoryDescription());
			categoryDetail.setId(Integer.toString(categoryItemDetailsFromServer.getCategoryId()));
			categoryDetail.setMarketingName(categoryItemDetailsFromServer.getCategoryMarketingName());
			categoryDetail.setName(categoryItemDetailsFromServer.getCategoryName());
			categoryDetail.setExternalId(Integer.toString(categoryItemDetailsFromServer.getExternalId()));
			categoryDetail.setId(Integer.toString(categoryItemDetailsFromServer.getId()));
			CategoryItems catItems = categoryItemDetailsFromServer.getItems();
			if(catItems.getItem()!=null)
				addCatItemInfoList(catItemInfoList, catItems);


			categoryDetail.setItems(catItemInfoList);

		} else {
			LOGGER.error("CategoryItemsDetail from the server is null in CategoryDetails servlet ");
		}
		return categoryDetail;

	}

	private void addCatItemInfoList(List<CategoryItemInfo> catItemInfoList, CategoryItems catItems) {
		CategoryItem[] catItemArray = catItems.getItem();
		if (catItemArray != null && catItemArray.length > 0) {
            for (int j = 0; j < catItemArray.length; j++)
                catItemInfoList.add(processCategoryItemInfo(catItemArray[j]));
        }
	}

	private CategoryItemInfo processCategoryItemInfo(CategoryItem categoryItem) {
		CategoryItemInfo catItemInfo = new CategoryItemInfo();
		CategoryItem catItem = categoryItem;
		if (catItem != null) {
            catItemInfo.setId(Integer.toString(catItem.getId()));
            catItemInfo.setExternalId(catItem.getExternalId());
            catItemInfo.setDisplayOrder(Integer.toString(catItem.getDisplayOrder()));
            catItemInfo.setDoNotShow(catItem.getDoNotShow());
            catItemInfo.setId(Integer.toString(catItem.getItemId()));
            catItemInfo.setMarketingName(catItem.getItemMarketingName());
            catItemInfo.setName(catItem.getItemName());
            catItemInfo.setShortName(catItem.getShortName());
            catItemInfo.setSpecializationText1(catItem.getSpecializationText1());
            catItemInfo.setSpecializationText2(catItem.getSpecializationText2());
            catItemInfo.setPath(getCategoryItemPageURL(catItem));
            catItemInfo.setImagePath(getCategoryItemImagePath(catItem));
			catItemInfo.setItemVisibility(setProductVisibility(catItem));
        }
		return catItemInfo;
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
	private Map<String, String> populateOsgiConfigurations() {
		Map<String, String> data = new HashMap<String, String>();
		if (resourceCountryCode != null && resourceLanguageCode != null) {
			McdFactoryConfig mcdFactoryConfig = mcdFactoryConfigConsumer
					.getMcdFactoryConfig(resourceCountryCode, resourceLanguageCode);
			if (mcdFactoryConfig != null) {
				data.put("productShortUrlRequired",
						Boolean.toString(mcdFactoryConfig.isProductShortUrlRequired()));
				data.put("productPagePath", mcdFactoryConfig.getProductPagePath());

			}
		}
		return data;
	}

	private boolean setProductVisibility(CategoryItem categoryItem) {
		boolean itemVisibility;
		if(!(ApplicationConstants.DO_NOT_SHOW.equalsIgnoreCase(categoryItem.getDoNotShow()))){
			if(categoryItem.getRelationTypes() != null && categoryItem.getRelationTypes().getRelationType() != null){
				List<RelationType> relationTypeList = categoryItem.getRelationTypes().getRelationType();
				itemVisibility=evaluateItemVisiblity(relationTypeList,categoryItem);
			}else{
				itemVisibility=true;
			}
		}else{
			itemVisibility=false;
		}

		return itemVisibility;
	}


	private boolean evaluateItemVisiblity(List<RelationType> relationTypeList,CategoryItem categoryItem){
		boolean itemvisiblity = false;
		for (RelationType relationType : relationTypeList) {
			itemvisiblity=false;
			if (relationType.getType() != null && relationType.getRelatedItems() != null && relationType.getRelatedItems().getRelatedItem() != null)
				itemvisiblity = isItemvisiblity(categoryItem,relationType);
			if(!itemvisiblity)
				break;
		}
		return itemvisiblity;
	}

	private boolean isItemvisiblity(CategoryItem categoryItem,RelationType relationType) {
		boolean itemvisiblity = false;
		List<RelatedItem> relatedItemArray = relationType.getRelatedItems().getRelatedItem();
		for (RelatedItem relatedItem : relatedItemArray) {
            if (categoryItem.getId() == relatedItem.getId()) {
                itemvisiblity=relatedItem.getIsDefault();
                break;
            }
        }
		return itemvisiblity;
	}


}


