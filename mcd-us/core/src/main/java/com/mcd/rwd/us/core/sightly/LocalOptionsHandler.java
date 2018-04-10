package com.mcd.rwd.us.core.sightly;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Listener;
import com.citytechinc.cq.component.annotations.Tab;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.wcm.api.Page;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mcd.rwd.global.core.utils.ConnectionUtil;
import com.mcd.rwd.global.core.utils.LinkUtil;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.us.core.bean.category.CategoryItem;
import com.mcd.rwd.us.core.bean.category.CategoryItemTypeAdapter;
import com.mcd.rwd.us.core.bean.category.RelatedItem;
import com.mcd.rwd.us.core.bean.category.RelatedItemTypeAdapter;
import com.mcd.rwd.us.core.bean.category.RelationType;
import com.mcd.rwd.us.core.bean.category.RelationTypeAdapter;
import com.mcd.rwd.us.core.bean.evm.Coop;
import com.mcd.rwd.us.core.bean.evm.CoopAdapter;
import com.mcd.rwd.us.core.bean.product.LocalItemsResponse;
import com.mcd.rwd.us.core.constants.ApplicationConstants;
import com.mcd.rwd.us.core.service.McdDynamicMediaServiceConfig;
import com.mcd.rwd.us.core.service.McdFactoryConfig;
import com.mcd.rwd.us.core.service.McdFactoryConfigConsumer;
import com.mcd.rwd.us.core.service.McdWebServicesConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by deepti_b on 5/20/2016.
 */

@Component(
		name = "localoptions",
		value = "Local Options",
		description = "Local Options Component.",
		path = "content",
		disableTargeting = true,
		group = " GWS-Global",
		listeners = {
				@Listener(name = "afteredit", value = "REFRESH_PAGE"),
				@Listener(name = "afterinsert", value = "REFRESH_PAGE"),
				@Listener(name = "aftersubmit", value = "REFRESH_PAGE")
		},
		tabs = {
				@Tab(title = "Local Options")
		}
)
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LocalOptionsHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocalOptionsHandler.class);

	@DialogField(name = "./desktopImgFolder", fieldLabel = "Product Desktop  Image Folder", fieldDescription =
			"Please Select the Folder for Product Images", required = true)
	@PathField(rootPath = DamConstants.MOUNTPOINT_ASSETS)
	@Inject @Named("desktopImgFolder")
	private String desktopFolderImgPath;

	@DialogField(name = "./noLocalItemMsg", fieldLabel = "Coop no Item Error message", fieldDescription =
			"Please Enter the Message to be Displayed if no Local Item is there.", required = true)
	@TextField
	@Inject @Named("noLocalItemMsg")
	private String itemsErrorMsg;

	@DialogField(name = "./coopInformationMsg", fieldLabel = "Coop Information Error Message", fieldDescription =
			"Please Enter the Error Message to be displayed if no Coop is available", required = true)
	@TextField
	@Inject @Named("coopInformationMsg")
	private String coopErrorMsg;

	@DialogField(name = "./productTitle", fieldLabel = "Product Sub Title", fieldDescription = "Please Enter the " +
			"section sub title for Local items.")
	@TextField
	@Inject @Named("productTitle")
	private String productSubTitle;

	@DialogField(name = "./localProductButtonText", fieldLabel = "Local Product Button CTA Text", required = true)
	@TextField
	@Inject @Named("localProductButtonText")
	private String productCtaText;

	@Inject
	private SlingHttpServletRequest request;

	@Inject
	private Page currentPage;

	@Inject
	private SlingScriptHelper slingScriptHelper;

	@OSGiService
	private McdDynamicMediaServiceConfig mcdDynamicMediaServiceConfig;

	@OSGiService
	private McdFactoryConfigConsumer mcdFactoryConfigConsumer;

	@OSGiService
	private McdWebServicesConfig mcdWebServicesConfig;

	private static final String GET_ALL_ITEMS_URL = "getAllItemsUrl";

	private Map<String, String> configMap;

	private McdFactoryConfig mcdFactoryConfig;

	private List<CategoryItem> allLocalItemsUrl = new ArrayList<CategoryItem>();

	private String showLiveDataValue;
	private String dynamicMediaLargeParameter = StringUtils.EMPTY;
	private String dynamicMediaMediumParameter = StringUtils.EMPTY;
	private String dynamicMediaSmallParameter = StringUtils.EMPTY;
	private String dynamicMediaXSmallParameter =StringUtils.EMPTY;
	private String dynamicMediaServerContextURL = StringUtils.EMPTY;

	/**
	 * Method to perform Post Initialization Tasks.
	 */
	@PostConstruct
	public final void activate() {
		showLiveDataValue = request.getParameter("showLiveData");
		configMap = populateOsgiConfigurations();
		retrieveAllItemsInfoFromServer();
		setDynamicMediaServerContextURL(mcdDynamicMediaServiceConfig.getImageServerUrl(request, slingScriptHelper));
		if(StringUtils.isNotEmpty(dynamicMediaServerContextURL)){
		   setDynamicMediaLargeParameter(mcdDynamicMediaServiceConfig.getImagePresetLocalOptionLarge());
		   setDynamicMediaMediumParameter(mcdDynamicMediaServiceConfig.getImagePresetLocalOptionMedium());
		   setDynamicMediaSmallParameter(mcdDynamicMediaServiceConfig.getImagePresetLocalOptionSmall());
		   setDynamicMediaXSmallParameter(mcdDynamicMediaServiceConfig.getImagePresetLocalOptionXSmall());
		   
		}
	}

	/**
	 * Populates the properties from the OSGi Config.
	 */
	private Map<String, String> populateOsgiConfigurations() {
		Map<String, String> data = new HashMap<String, String>();
		StringBuilder url = new StringBuilder();
		String country = PageUtil.getCountry(currentPage);
		String language = PageUtil.getLanguage(currentPage);
		if (StringUtils.isBlank(showLiveDataValue)) {
			showLiveDataValue = String.valueOf(true);
		}
		LOGGER.info(
				" In LocalOptionsHandler  Resource Country Code is" + country + "Resource Language Code is"
						+ language);
		if (country != null && language != null) {
			if (mcdFactoryConfigConsumer != null && mcdWebServicesConfig != null) {
				mcdFactoryConfig = mcdFactoryConfigConsumer.getMcdFactoryConfig(country, language);
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
							.append(ApplicationConstants.PN_EQUALS).append(showLiveDataValue)
							.append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
							.append(ApplicationConstants.PN_COPS_FILTER)
							.append(ApplicationConstants.PN_EQUALS).append("true")
							.append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
							.append(ApplicationConstants.PN_SHOW_NATIONAL_COOP)
							.append(ApplicationConstants.PN_EQUALS).append(false);
					data.put("productShortUrlRequired",
							Boolean.toString(mcdFactoryConfig.isProductShortUrlRequired()));
					data.put("productPagePath", mcdFactoryConfig.getProductPagePath());
					data.put(GET_ALL_ITEMS_URL, url.toString());

				} else {
					LOGGER.error(
							"No Factory Configuration found for the country - {} and language - {} in " +
									"CategoryDetailHandler in populateOsgiConfigurations method",
							country, language);
				}
			} else {
				LOGGER.error(
						"Could not get reference to the McdFactoryConfigConsumer service in " +
								"CategoryDetailHandler in populateOsgiConfigurations method");
			}
		} else {
			LOGGER.error(
					"No country code or language code found for the resource in " +
							"CategoryDetailHandler in populateOsgiConfigurations method");
		}
		return data;
	}

	public void retrieveAllItemsInfoFromServer() {
		try {
			ConnectionUtil connUtil = new ConnectionUtil();
			LOGGER.info("In LocalOptionsHandler Requesting ... {}", configMap.get(GET_ALL_ITEMS_URL));
			String itemsResponse = connUtil.sendGet(configMap.get(GET_ALL_ITEMS_URL));
			// parse response(using gson) coming from server
			Type itemTypeToken = new TypeToken<CategoryItem[]>() {
			}.getType();
			Type relationTypeToken = new TypeToken<List<RelationType>>() {
			}.getType();
			Type relatedItemTypeToken = new TypeToken<List<RelatedItem>>() {
			}.getType();
			Type coopTypeToken = new TypeToken<List<Coop>>() {
			}.getType();
			Gson gson = new GsonBuilder().registerTypeAdapter(itemTypeToken, new CategoryItemTypeAdapter())
					.registerTypeAdapter(relationTypeToken, new RelationTypeAdapter())
					.registerTypeAdapter(coopTypeToken, new CoopAdapter())
					.registerTypeAdapter(relatedItemTypeToken, new RelatedItemTypeAdapter()).create();
			LocalItemsResponse itemResponse = gson.fromJson(itemsResponse, LocalItemsResponse.class);
			if (itemResponse != null)
				processItemsResponse(itemResponse);
			else {
				LOGGER.error("In LocalOptionsHandler Unable to parse json .Json might be null or "
						+ "incorrect for the url: {}", configMap.get(GET_ALL_ITEMS_URL));
			}
		} catch (Exception e) {
			LOGGER.error("Error in LocalOptionsHandler class", e);
		}
	}

	private void processItemsResponse(LocalItemsResponse itemResponse) {

		if (itemResponse.getFullMenu() != null && itemResponse.getFullMenu().getItems() != null) {
			CategoryItem[] catItemArray = itemResponse.getFullMenu().getItems().getItem();
			if (catItemArray != null && catItemArray.length > 0)
				addLocalCategoryItems(catItemArray);
		}

	}

	private void addLocalCategoryItems(CategoryItem[] catItemArray) {
		for (int j = 0; j < catItemArray.length; j++) {
			if (!(ApplicationConstants.DO_NOT_SHOW.equalsIgnoreCase(catItemArray[j].getDoNotShow()))) {
				allLocalItemsUrl.add(getCategoryItemPageURL(catItemArray[j]));
			}
		}
	}

	private CategoryItem getCategoryItemPageURL(CategoryItem catItem) {
		String catItemPageUrl;
		Boolean productShortUrlRequired = Boolean.valueOf(configMap.get("productShortUrlRequired"));
		String productPagePath = configMap.get("productPagePath");
		if (productShortUrlRequired) {
			catItemPageUrl = LinkUtil.getProductShortUrl(productPagePath, catItem.getShortName());
		} else {
			catItemPageUrl = LinkUtil.getProductUrl(productPagePath, catItem.getExternalId());
		}
		catItem.setCategoryItemFullUrl(catItemPageUrl);
		return catItem;
	}

	public List<CategoryItem> getAllLocalItemsUrl() {
		return allLocalItemsUrl;
	}

	public String getDesktopFolderImgPath() {
		return desktopFolderImgPath;
	}

	public String getItemsErrorMsg() {
		return itemsErrorMsg;
	}

	public String getCoopErrorMsg() {
		return coopErrorMsg;
	}

	public String getProductSubTitle() {
		return productSubTitle;
	}

	public String getProductCtaText() {
		return productCtaText;
	}

	public String getShowLiveDataValue() {
		return showLiveDataValue;
	}

	public void setShowLiveDataValue(String showLiveDataValue) {
		this.showLiveDataValue = showLiveDataValue;
	}

	public String getDynamicMediaLargeParameter() {
		return dynamicMediaLargeParameter;
	}

	public void setDynamicMediaLargeParameter(String dynamicMediaLargeParameter) {
		this.dynamicMediaLargeParameter = dynamicMediaLargeParameter;
	}

	public String getDynamicMediaMediumParameter() {
		return dynamicMediaMediumParameter;
	}

	public void setDynamicMediaMediumParameter(String dynamicMediaMediumParameter) {
		this.dynamicMediaMediumParameter = dynamicMediaMediumParameter;
	}

	public String getDynamicMediaSmallParameter() {
		return dynamicMediaSmallParameter;
	}

	public void setDynamicMediaSmallParameter(String dynamicMediaSmallParameter) {
		this.dynamicMediaSmallParameter = dynamicMediaSmallParameter;
	}

	public String getDynamicMediaXSmallParameter() {
		return dynamicMediaXSmallParameter;
	}

	public void setDynamicMediaXSmallParameter(String dynamicMediaXSmallParameter) {
		this.dynamicMediaXSmallParameter = dynamicMediaXSmallParameter;
	}

	public String getDynamicMediaServerContextURL() {
		return dynamicMediaServerContextURL;
	}

	public void setDynamicMediaServerContextURL(String dynamicMediaServerContextURL) {
		this.dynamicMediaServerContextURL = dynamicMediaServerContextURL;
	}

}
