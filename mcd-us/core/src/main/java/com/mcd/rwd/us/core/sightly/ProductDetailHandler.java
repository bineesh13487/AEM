package com.mcd.rwd.us.core.sightly;

import com.citytechinc.cq.component.annotations.Component;
import com.day.cq.wcm.api.Page;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mcd.rwd.global.core.utils.ConnectionUtil;
import com.mcd.rwd.global.core.utils.LinkUtil;
import com.mcd.rwd.global.core.utils.MultiFieldPanelUtil;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.us.core.bean.category.RelatedItem;
import com.mcd.rwd.us.core.bean.category.RelatedItemTypeAdapter;
import com.mcd.rwd.us.core.bean.category.RelationType;
import com.mcd.rwd.us.core.bean.category.RelationTypeAdapter;
import com.mcd.rwd.us.core.bean.product.*;
import com.mcd.rwd.us.core.constants.ApplicationConstants;
import com.mcd.rwd.us.core.service.McdDynamicMediaServiceConfig;
import com.mcd.rwd.us.core.service.McdFactoryConfig;
import com.mcd.rwd.us.core.service.McdFactoryConfigConsumer;
import com.mcd.rwd.us.core.service.McdWebServicesConfig;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by deepti_b on 3/16/2016.
 */

@Component(
		name = "productdetail",
		value = "Product Detail",
		path = "content",
		description = "Component that show Product Details",
		group = " GWS-Global"
)
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductDetailHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductDetailHandler.class);

	@Inject @Via("request")
	Resource resource;

	@Self
	SlingHttpServletRequest request;

	@Inject
	Page currentPage;

	@Inject
	SlingScriptHelper slingScriptHelper;

	@OSGiService
	McdDynamicMediaServiceConfig mcdDynamicMediaServiceConfig;

	@OSGiService
	McdFactoryConfigConsumer mcdFactoryConfigConsumer;

	@OSGiService
	McdWebServicesConfig mcdWebServicesConfig;

	private String itemId;

	private String resourceCountry;

	private String resourceLanguage;

	private String domain;

	private String itemDetailsByIdService;

	private ItemsResponse productInfo;

	private List<com.mcd.rwd.us.core.bean.product.Nutrient> nutrientInfo;

	private List<ItemComponent> componentInfo;

	@OSGiService
	private McdFactoryConfig mcdFactoryConfig;

	private Gson gson;

	@Inject @Named("nutritionCalcUrl")
	private String nutritionCalculatorURL;

	@Inject @Named("linkText")
	private String nutritionCalcLinkText;

	private List<String> footerList = new ArrayList<String>();

	private String showLiveDataValue;

	private List<com.mcd.rwd.us.core.bean.Nutrient> pcNutrientsList = new ArrayList<com.mcd.rwd.us.core.bean.Nutrient>();

	private Map<Integer, ProductIngredients> ingredients = new TreeMap<Integer, ProductIngredients>();

	private List<ProductIngredients> ingredientsList = new ArrayList<ProductIngredients>();

	private static final String NUTRINT_MARKETING_NAME = "nutrientMarketingName";

	private static final String SECONDARY_NUTRINT_MARKETING_NAME = "secondaryNutrientMarketingName";

	private String dnaItemDetailsUrl;

	@Inject @Named("tab2Text")
	@Default(values = "view INGREDIENTS & allergens")
	private String tab2Text;

	@Inject @Named("tab1Text")
	@Default(values = "view nutrition summary")
	private String tab1Text;

	@Inject @Named("serveText")
	@Default(values = "Select Serving Size")
	private String serveText;

	@Inject @Named("servesText")
	@Default(values = "Serves:")
	private String servesText;

	@Inject @Named("legalText")
	@Default(values = " %DV = % Daily Value")
	private String legalText;

	@Inject @Named("allergenText")
	@Default(values = "Contains")
	private String allergenText;

	@Inject @Named("customizeText")
	@Default(values = "")
	private String customizeText;

	@Inject @Named("DvText")
	@Default(values = "DV")
	private String dvText;

	@Inject @Named("showGoesWell")
	@Default(values = "True")
	private String showGoesWell;

	@Inject @Named("showMoreItems")
	@Default(values = "True")
	private String showMoreItems;

	@Inject @Named("showIngredients")
	@Default(values = "True")
	private String showIngredients;

	@Inject @Named("showNutritionTabs")
	@Default(values = "True")
	private String showNutritionTabs;

    @Inject @Named("orderNow")
    @Default(values = "Order Now")
	private String ordernowTitle;


    @Inject @Named("orderNowURL")
	private String showOrderNow;


    @Inject @Named("showordernow")
    @Default(values = "false")
	private String orderNowURL;




	private String encodedProductMarketingName;

	private String flagCheck = "[\"true\"]";

	private static final String TWO = "two";

	private static final String THREE = "three";

	private static final String SECONDARY_NUTRINT_LIST = "secondaryNutrientList";

	private List<com.mcd.rwd.us.core.bean.Nutrient> secondaryNutrientsList = new ArrayList<com.mcd.rwd.us.core.bean.Nutrient>();

	private List<com.mcd.rwd.us.core.bean.Nutrient> secondaryNutrientsList2 = new ArrayList<com.mcd.rwd.us.core.bean.Nutrient>();

	private List<com.mcd.rwd.us.core.bean.Nutrient> secondaryNutrientsList3 = new ArrayList<com.mcd.rwd.us.core.bean.Nutrient>();
	
	private List<com.mcd.rwd.us.core.bean.Nutrient> secondaryNutrientsListContainer = new ArrayList<com.mcd.rwd.us.core.bean.Nutrient>();
	
	private List<com.mcd.rwd.us.core.bean.Nutrient> secondaryNutrientsListMobile = new ArrayList<com.mcd.rwd.us.core.bean.Nutrient>();

	private List<com.mcd.rwd.us.core.bean.Nutrient> secondaryNutrientsListMobile2 = new ArrayList<com.mcd.rwd.us.core.bean.Nutrient>();

	private List<com.mcd.rwd.us.core.bean.Nutrient> secondaryNutrientsListMobile3 = new ArrayList<com.mcd.rwd.us.core.bean.Nutrient>();
	
	private List<com.mcd.rwd.us.core.bean.Nutrient> secondaryNutrientsListMobileContainer = new ArrayList<com.mcd.rwd.us.core.bean.Nutrient>();

	@Inject @Named("secondaryColCount")
	@Default(values = THREE)
	private String secondaryColCount;

	@Inject @Named("secondaryColCountMobile")
	@Default(values = TWO)
	private String secondaryColCountMobile;

	private String canonicalURL;

	private String itemMetaTitle;

	private String metaTitleConstant;

	private String unit_flag;
	
	private String percenatage_unit_flag;

	@Inject @Named("viewTypeSelection")
	@Default(values = "tableView")
	private String secondaryNutrientsViewType;

	@Inject @Named("otherAllergenText")
	@Default(values = "May Contains")
	private String allergenMayText;
	
	private String hyperlink = "";

	@Inject @Named("nutritionColumnHeading")
	@Default(values = "Nutritional Information")
	private String nutritionColumnHeading;

	@Inject @Named("measurementUnitColumnHeading")
	@Default(values = "Per PTN")
	private String measurementUnitColumnHeading;

	@Inject @Named("percentageUnitColumnHeading")
	@Default(values = "% DV (Adult)")
	private String percentageUnitColumnHeading;

	@Inject @Named("healthHyperlink")
	@Default(values = "")
	private String oldHyperlink;

	private String itemMetaDescription;
	
	private Map<String, String> configMap;
	
	private boolean isNutritionInfoAtProductLevel = true;

	private String dynamicMediaLargeParameter = StringUtils.EMPTY;
	private String dynamicMediaMediumParameter = StringUtils.EMPTY;
	private String dynamicMediaSmallParameter = StringUtils.EMPTY;
	private String dynamicMediaXSmallParameter =StringUtils.EMPTY;
	private String dynamicMediaServerContextURL = StringUtils.EMPTY;

	ValueMap valueMap = null;

	@PostConstruct
	public void activate() {
		this.resourceCountry = PageUtil.getCountry(currentPage);
		this.resourceLanguage = PageUtil.getLanguage(currentPage);
		LOGGER.info(" In ProductDetailHandler  Resource Country Code is" + resourceCountry + "Resource Language Code is"
				+ resourceLanguage);
		populateConfigProperties();
		String[] selectorsArray = request.getRequestPathInfo().getSelectors();
		if (selectorsArray != null && selectorsArray.length > 0) {
			this.itemId = selectorsArray[0];
		}
		valueMap = resource.getValueMap();
		//String url = valueMap.get("nutritionCalcUrl", String.class);
		if (StringUtils.isNotBlank(nutritionCalculatorURL)) {
			nutritionCalculatorURL = nutritionCalculatorURL + ApplicationConstants.URL_EXT_HTML;
		}
		//nutritionCalcLinkText = valueMap.get("linkText", String.class);
		//tab1Text = valueMap.get("tab1Text", "view nutrition summary");
		//tab2Text = valueMap.get("tab2Text", "view INGREDIENTS & allergens");
		//serveText = valueMap.get("serveText", "Select Serving Size");
		//servesText = valueMap.get("servesText", "Serves:");
		//legalText = valueMap.get("legalText", " %DV = % Daily Value");
		//allergenText = valueMap.get("allergenText", "Contains");
		//allergenMayText = valueMap.get("otherAllergenText","May Contains");
		//customizeText = valueMap.get("customizeText", "");
		//dvText = valueMap.get("DvText", "DV");
		//showGoesWell = valueMap.get("showGoesWell", "True");
		//showMoreItems = valueMap.get("showMoreItems", "True");
		//showIngredients = valueMap.get("showIngredients", "True");
		//showNutritionTabs = valueMap.get("showNutritionTabs", "True");
		showLiveDataValue = request.getParameter("showLiveData");
		//secondaryColCount = valueMap.get("secondaryColCount", THREE);
		//secondaryColCountMobile = valueMap.get("secondaryColCountMobile", TWO);
		//String oldHyperlink = valueMap.get("healthHyperlink", "");
		//secondaryNutrientsViewType = valueMap.get("viewTypeSelection", "tableView");
		//nutritionColumnHeading = valueMap.get("nutritionColumnHeading", "Nutritional Information");
		//measurementUnitColumnHeading = valueMap.get("measurementUnitColumnHeading", "Per PTN");
		//percentageUnitColumnHeading = valueMap.get("percentageUnitColumnHeading", "% DV (Adult)");
		tagRemoval(oldHyperlink);
		List<com.mcd.rwd.us.core.bean.product.Nutrient> popupNutrientsList = getList(
		MultiFieldPanelUtil.getMultiFieldPanelValues(resource, "nutfactPopup"));
		String allPopupNutrients = getId(popupNutrientsList);
		StringBuilder nutritionAttributeIds = new StringBuilder();
		nutritionAttributeIds.append(allPopupNutrients);
		nutrientInfo = new ArrayList<com.mcd.rwd.us.core.bean.product.Nutrient>();
		getItemDetail(itemId, nutritionAttributeIds.toString(), popupNutrientsList);

		populateNutrients(MultiFieldPanelUtil.getMultiFieldPanelValues(resource, "nutrientList"));
		populateAllSecondaryNutrients();
		populateItemDetailsUrl();
		configMap = populateOsgiConfigurations();
		setCanonicalURL(productInfo);
		this.itemMetaTitle = createItemMetaTitle(productInfo);
		this.itemMetaDescription = createItemMetaDescription(productInfo);

		dynamicMediaServerContextURL = mcdDynamicMediaServiceConfig.getImageServerUrl(request, slingScriptHelper);
		if(StringUtils.isNotEmpty(dynamicMediaServerContextURL)){
		   setDynamicMediaLargeParameter(mcdDynamicMediaServiceConfig.getImagePresetHeroLarge());
		   setDynamicMediaMediumParameter(mcdDynamicMediaServiceConfig.getImagePresetHeroMedium());
		   setDynamicMediaSmallParameter(mcdDynamicMediaServiceConfig.getImagePresetHeroSmall());
		   setDynamicMediaXSmallParameter(mcdDynamicMediaServiceConfig.getImagePresetHeroXSmall());
		   
		}
		
		LOGGER.debug("Generated Hyperlink thorugh RichText id: " + hyperlink);
		LOGGER.debug("Optional Allergence Contains: " + allergenMayText);
		LOGGER.debug("Nutrition Column Heading: " + nutritionColumnHeading);
		LOGGER.debug("Measurement Unit Column Heading: " + measurementUnitColumnHeading);
		LOGGER.debug("Percentage Unti Column Heading: " + percentageUnitColumnHeading);
	}

	/**
	 * Backlog Update for add canonical URL Checking the Relationship and
	 * Related Item in product Detail. if current page is not a default page for
	 * related pages, set the canonical href link for default product otherwise
	 * if page has not related pages set current page url as a canonical href
	 * link.
	 * 
	 * @param productInfo
	 */
	private void setCanonicalURL(ItemsResponse productInfo) {
		Boolean productShortUrlRequired = Boolean.valueOf(configMap.get("productShortUrlRequired"));
		String productPagePath = configMap.get("productPagePath");
		if (null != productInfo && null != productInfo.getItem()) {
			if (productInfo.getItem().getRelationTypes() != null
					&& productInfo.getItem().getRelationTypes().getRelationType() != null) {
				List<RelationType> relationTypeList = productInfo.getItem().getRelationTypes().getRelationType();
				for (RelationType relationType : relationTypeList) {
					if (relationType.getType() != null && relationType.getRelatedItems() != null
							&& relationType.getRelatedItems().getRelatedItem() != null) {
						List<RelatedItem> relatedItemArray = relationType.getRelatedItems().getRelatedItem();
						for (RelatedItem relatedItem : relatedItemArray) {
							if (relatedItem.getIsDefault() && relatedItem.getShortName() != null) {
								if (productShortUrlRequired) {
									this.canonicalURL = LinkUtil.getProductShortUrl(productPagePath,
											relatedItem.getShortName());
								} else {
									this.canonicalURL = LinkUtil.getProductUrl(productPagePath,
											String.valueOf(relatedItem.getExternalId()));
								}

							}
						}
					}
				}
			} else {
				if (productShortUrlRequired) {
					this.canonicalURL = LinkUtil.getProductShortUrl(productPagePath, productInfo.getItem().getShortName());
				} else {
					this.canonicalURL = LinkUtil.getProductUrl(productPagePath, productInfo.getItem().getExternalId());
				}
			}
		}
	}

	/**
	 * Backlog Update getting site configuration
	 * 
	 * @return
	 */
	private Map<String, String> populateOsgiConfigurations() {
		Map<String, String> data = new HashMap<String, String>();
		StringBuilder url = new StringBuilder();
		String country = PageUtil.getCountry(currentPage);
		String language = PageUtil.getLanguage(currentPage);
		LOGGER.info(" In CategoryDetailHandler  Resource Country Code is" + country + "Resource Language Code is"
				+ language);
		if (StringUtils.isBlank(showLiveDataValue)) {
			showLiveDataValue = String.valueOf(true);
		}

		if (country != null && language != null) {
			if (mcdFactoryConfigConsumer != null && mcdWebServicesConfig != null) {
				mcdFactoryConfig = mcdFactoryConfigConsumer.getMcdFactoryConfig(country, language);
				if (null != mcdFactoryConfig) {
					data.put("productShortUrlRequired", Boolean.toString(mcdFactoryConfig.isProductShortUrlRequired()));
					data.put("productPagePath", mcdFactoryConfig.getProductPagePath());
				}
				data.put("categoryDetailUrl", url.toString());

			} else {
				LOGGER.error(
						"Could not get reference to the McdFactoryConfigConsumer service in CategoryDetailHandler in populateOsgiConfigurations method");
			}
		} else {
			LOGGER.error(
					"No country code or language code found for the resource in CategoryDetailHandler in populateOsgiConfigurations method");
		}
		return data;
	}

	/**
	 * Backlog Update setting Item Meta Title for product Detail Page
	 * 
	 * @return
	 */
	public String createItemMetaTitle(ItemsResponse productInfo) {
		String metaDescrp = StringUtils.EMPTY;
		if(productInfo != null && null != productInfo.getItem()){
		String metaTitle = productInfo.getItem().getItemMetaTitle()
				+ valueMap.get("metaTitle", StringUtils.EMPTY);
		String itemMarketingName = productInfo.getItem().getItemMarketingName()
				+ valueMap.get("metaTitle", StringUtils.EMPTY);

		metaDescrp =  (null != productInfo && null != productInfo.getItem().getItemMetaTitle()) ? metaTitle
				: itemMarketingName;
		}
		return metaDescrp;
	}

	public String createItemMetaDescription(ItemsResponse productInfo){
		String metaDescrp = StringUtils.EMPTY;
		if(productInfo != null && null != productInfo.getItem()){
		String metaDescription = productInfo.getItem().getItemMetaDescription();
		String description = (String)productInfo.getItem().getDescription();

		metaDescrp = ( null != productInfo.getItem().getItemMetaDescription()) ? metaDescription : description;
		}
		return metaDescrp;
	}


	private void populateItemDetailsUrl() {
		StringBuilder url = new StringBuilder();
		if (mcdFactoryConfig != null) {
			
			isNutritionInfoAtProductLevel = mcdFactoryConfig.isNutritionInfoAtProductLevel();
						url.append(mcdWebServicesConfig.getItemDetailUrl()).append(ApplicationConstants.URL_QS_START)
					.append(ApplicationConstants.PN_COUNTRY).append(ApplicationConstants.PN_EQUALS)
					.append(mcdFactoryConfig.getDnaCountryCode()).append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
					.append(ApplicationConstants.PN_LANGUAGE).append(ApplicationConstants.PN_EQUALS)
					.append(mcdFactoryConfig.getDnaLanguageCode()).append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
					.append(ApplicationConstants.PN_SHOW_LIVE_DATA).append(ApplicationConstants.PN_EQUALS)
					.append(showLiveDataValue);
			dnaItemDetailsUrl = url.toString();
			LOGGER.debug("Item details url ---- " + url.toString());

		}
	}

	private void populateNutrients(final List<Map<String, String>> nutrientList) {
		LOGGER.debug("Inside populate Nutrients to populate nutrient list..");
		if (nutrientList != null && !nutrientList.isEmpty()) {
			Iterator<Map<String, String>> itr = nutrientList.iterator();
			while (itr.hasNext()) {

				com.mcd.rwd.us.core.bean.Nutrient nutrient = new com.mcd.rwd.us.core.bean.Nutrient();
				Map<String, String> item = itr.next();
				String[] parts = item.get("nutrientList").split("_");
				nutrient.setId(parts[0]);
				nutrient.setName(parts[1]);
				if (item.get(NUTRINT_MARKETING_NAME) != null && !"".equals(item.get(NUTRINT_MARKETING_NAME))) {
					nutrient.setMarketingName(item.get(NUTRINT_MARKETING_NAME));
				} else {
					nutrient.setMarketingName(parts[1]);
				}
				
				boolean percenatage_unit_check = item.containsKey("percentageUnitFlag");
				if(percenatage_unit_check){
					String percenatage_unit_flag = item.get("percentageUnitFlag");
					if (percenatage_unit_flag != null && !""
							.equals(percenatage_unit_flag) && flagCheck.equalsIgnoreCase(percenatage_unit_flag)) {
						nutrient.setPercentageUnitFlag("false");
					} else{
						nutrient.setPercentageUnitFlag("true");
					}
					LOGGER.debug("Primary Percentage Unit Hide Flag:" + percenatage_unit_flag);
				} else {
					nutrient.setPercentageUnitFlag("true");
				}
				pcNutrientsList.add(nutrient);
			}
		} else {
			LOGGER.error("NutrientList is empty..");
		}
	}

	/**
	 * Populates the properties from the OSGi Config.
	 */
	private void populateConfigProperties() {
		if (resourceCountry != null && resourceLanguage != null) {

			if (mcdFactoryConfigConsumer != null) {
				mcdFactoryConfig = mcdFactoryConfigConsumer.getMcdFactoryConfig(this.resourceCountry,
						this.resourceLanguage);
				if (mcdFactoryConfig != null && mcdWebServicesConfig != null) {

					domain = mcdWebServicesConfig.getDomain();
					itemDetailsByIdService = mcdWebServicesConfig.getItemOnExternalIdUrl();
				} else {
					LOGGER.error("No Factory Configuration found for the country - {} and language - {}",
							this.resourceCountry, this.resourceLanguage);
				}
			} else {
				LOGGER.error("Could not get reference to the McdFactoryConfigConsumer service");
			}
		} else {
			LOGGER.error(
					"No country code or language code found for the resource in ProductDetailHandler in populateConfigProperties");
		}
	}

	private void populateAllSecondaryNutrients() {

		populateSecondaryNutrients(MultiFieldPanelUtil.getMultiFieldPanelValues(resource, SECONDARY_NUTRINT_LIST));

		if (secondaryColCount.equals(TWO)) {

			populateSecondaryNutrients2(
					MultiFieldPanelUtil.getMultiFieldPanelValues(resource, "secondaryNutrientList2"));
		}

		if (secondaryColCount.equals(THREE)) {

			populateSecondaryNutrients2(
					MultiFieldPanelUtil.getMultiFieldPanelValues(resource, "secondaryNutrientList2"));
			populateSecondaryNutrients3(
					MultiFieldPanelUtil.getMultiFieldPanelValues(resource, "secondaryNutrientList3"));
		}

		populateSecondaryNutrientsMobile(
				MultiFieldPanelUtil.getMultiFieldPanelValues(resource, "secondaryNutrientListMobile"));

		if (secondaryColCountMobile.equals(TWO)) {
			populateSecondaryNutrientsMobile2(
					MultiFieldPanelUtil.getMultiFieldPanelValues(resource, "secondaryNutrientListMobile2"));
		}

		if (secondaryColCountMobile.equals(THREE)) {
			populateSecondaryNutrientsMobile2(
					MultiFieldPanelUtil.getMultiFieldPanelValues(resource, "secondaryNutrientListMobile2"));
			populateSecondaryNutrientsMobile3(
					MultiFieldPanelUtil.getMultiFieldPanelValues(resource, "secondaryNutrientListMobile3"));
		}

	}

	/**
	 * Method retrieves all Product Information from DNA Webservice
	 */
	private void getItemDetail(final String itemId, final String nutritionAttributeIds,
			List<com.mcd.rwd.us.core.bean.product.Nutrient> nutrientsList) {
		Item item = null;
		if (mcdFactoryConfig != null) {
			productInfo = fetchItemDetailFromService(itemId, nutritionAttributeIds);
		}
		if (productInfo != null && productInfo.getItem() != null) {
			item = productInfo.getItem();
			if (productInfo.getItem().getShowProductImages()!=null && ("yes").equalsIgnoreCase(productInfo.getItem().getShowProductImages())) {
			populateIngredients(item);
			}
			encodeProductMarketingName(productInfo);
		}
		if (item != null) {
			if (StringUtils.isNotBlank(nutritionAttributeIds) && item.getNutrientFacts() != null) {
				List<com.mcd.rwd.us.core.bean.product.Nutrient> nutrient = item.getNutrientFacts().getNutrientsList();
				nutrientInfo = getNutritionData(nutrientsList, nutrient);
			}
			if (item.getComponents() != null && item.getComponents().getComponent() != null) {
				componentInfo = evaluateComponentList(productInfo.getItem().getComponents().getComponent());
				productInfo.getItem().getComponents().setComponent(componentInfo);
			}
			productInfo.getItem().setNutrientInfo(nutrientInfo);
			setComponentImagePath(productInfo);
			setProductImagePath(productInfo);
			setProductFooter(productInfo);
		}

	}

	private void populateSecondaryNutrients(final List<Map<String, String>> nutrientList) {
		LOGGER.debug("Inside populate Secondary Nutrients to populate nutrient list..");
		if (nutrientList != null && !nutrientList.isEmpty()) {
			Iterator<Map<String, String>> itr = nutrientList.iterator();
			while (itr.hasNext()) {

				com.mcd.rwd.us.core.bean.Nutrient nutrient = new com.mcd.rwd.us.core.bean.Nutrient();
				Map<String, String> item = itr.next();
				String[] parts = item.get(SECONDARY_NUTRINT_LIST).split("_");
				nutrient.setId(parts[0]);
				nutrient.setName(parts[1]);
				if (item.get(SECONDARY_NUTRINT_MARKETING_NAME) != null
						&& !"".equals(item.get(SECONDARY_NUTRINT_MARKETING_NAME))) {
					nutrient.setMarketingName(item.get(SECONDARY_NUTRINT_MARKETING_NAME));
				} else {
					nutrient.setMarketingName(parts[1]);
				}
				
				boolean unit_flag_check = item.containsKey("unitFlag");
				
				if(unit_flag_check){
					unit_flag = item.get("unitFlag");
					if (unit_flag != null && !"".equals(unit_flag) && unit_flag.equalsIgnoreCase("bothUnit")) {
						nutrient.setMeasurementUnitFlag("true");
						nutrient.setPercentageUnitFlag("true");
					} else if (unit_flag != null && !"".equals(unit_flag)
							&& unit_flag.equalsIgnoreCase("measurementUnit")) {
						nutrient.setMeasurementUnitFlag("true");
						nutrient.setPercentageUnitFlag("false");
					} else {
						nutrient.setMeasurementUnitFlag("false");
						nutrient.setPercentageUnitFlag("true");
					}
					LOGGER.debug("Unit Flag Status:" + unit_flag);
				} else {
					nutrient.setMeasurementUnitFlag("true");
					nutrient.setPercentageUnitFlag("true");
				}
				
				secondaryNutrientsList.add(nutrient);
				secondaryNutrientsListContainer.add(nutrient);
			}
		} else {
			LOGGER.error("Secondary NutrientList is empty..");
		}
	}

	private void populateSecondaryNutrients2(final List<Map<String, String>> nutrientList) {
		LOGGER.debug("Inside populate Secondary Nutrients2 to populate nutrient list..");
		if (nutrientList != null && !nutrientList.isEmpty()) {
			Iterator<Map<String, String>> itr = nutrientList.iterator();
			while (itr.hasNext()) {

				com.mcd.rwd.us.core.bean.Nutrient nutrient = new com.mcd.rwd.us.core.bean.Nutrient();
				Map<String, String> item = itr.next();
				String[] parts = item.get(SECONDARY_NUTRINT_LIST).split("_");
				nutrient.setId(parts[0]);
				nutrient.setName(parts[1]);
				if (item.get(SECONDARY_NUTRINT_MARKETING_NAME) != null
						&& !"".equals(item.get(SECONDARY_NUTRINT_MARKETING_NAME))) {
					nutrient.setMarketingName(item.get(SECONDARY_NUTRINT_MARKETING_NAME));
				} else {
					nutrient.setMarketingName(parts[1]);
				}
				
				boolean unit_flag_check = item.containsKey("unitFlag");
				
				if(unit_flag_check){
					unit_flag = item.get("unitFlag");
					if (unit_flag != null && !"".equals(unit_flag) && unit_flag.equalsIgnoreCase("bothUnit")) {
						nutrient.setMeasurementUnitFlag("true");
						nutrient.setPercentageUnitFlag("true");
					} else if (unit_flag != null && !"".equals(unit_flag)
							&& unit_flag.equalsIgnoreCase("measurementUnit")) {
						nutrient.setMeasurementUnitFlag("true");
						nutrient.setPercentageUnitFlag("false");
					} else {
						nutrient.setMeasurementUnitFlag("false");
						nutrient.setPercentageUnitFlag("true");
					}
					LOGGER.debug("Unit Flag Status:" + unit_flag);
				} else {
					nutrient.setMeasurementUnitFlag("true");
					nutrient.setPercentageUnitFlag("true");
				}
				
				secondaryNutrientsList2.add(nutrient);
				secondaryNutrientsListContainer.add(nutrient);
			}
		} else {
			LOGGER.error("Secondary NutrientList2 is empty..");
		}
	}

	private void populateSecondaryNutrients3(final List<Map<String, String>> nutrientList) {
		LOGGER.debug("Inside populate Secondary Nutrients3 to populate nutrient list..");
		if (nutrientList != null && !nutrientList.isEmpty()) {
			Iterator<Map<String, String>> itr = nutrientList.iterator();
			while (itr.hasNext()) {

				com.mcd.rwd.us.core.bean.Nutrient nutrient = new com.mcd.rwd.us.core.bean.Nutrient();
				Map<String, String> item = itr.next();
				String[] parts = item.get(SECONDARY_NUTRINT_LIST).split("_");
				nutrient.setId(parts[0]);
				nutrient.setName(parts[1]);
				if (item.get(SECONDARY_NUTRINT_MARKETING_NAME) != null
						&& !"".equals(item.get(SECONDARY_NUTRINT_MARKETING_NAME))) {
					nutrient.setMarketingName(item.get(SECONDARY_NUTRINT_MARKETING_NAME));
				} else {
					nutrient.setMarketingName(parts[1]);
				}
				
				boolean unit_flag_check = item.containsKey("unitFlag");
				
				if(unit_flag_check){
					unit_flag = item.get("unitFlag");
					if (unit_flag != null && !"".equals(unit_flag) && unit_flag.equalsIgnoreCase("bothUnit")) {
						nutrient.setMeasurementUnitFlag("true");
						nutrient.setPercentageUnitFlag("true");
					} else if (unit_flag != null && !"".equals(unit_flag)
							&& unit_flag.equalsIgnoreCase("measurementUnit")) {
						nutrient.setMeasurementUnitFlag("true");
						nutrient.setPercentageUnitFlag("false");
					} else {
						nutrient.setMeasurementUnitFlag("false");
						nutrient.setPercentageUnitFlag("true");
					}
					LOGGER.debug("Unit Flag Status:" + unit_flag);
				} else {
					nutrient.setMeasurementUnitFlag("true");
					nutrient.setPercentageUnitFlag("true");
				}
				
				secondaryNutrientsList3.add(nutrient);
				secondaryNutrientsListContainer.add(nutrient);
			}
		} else {
			LOGGER.error("Secondary NutrientList3 is empty..");
		}
	}

	private void populateSecondaryNutrientsMobile(final List<Map<String, String>> nutrientList) {
		LOGGER.debug("Inside populate Secondary Nutrients to populate nutrient list..");
		if (nutrientList != null && !nutrientList.isEmpty()) {
			Iterator<Map<String, String>> itr = nutrientList.iterator();
			while (itr.hasNext()) {

				com.mcd.rwd.us.core.bean.Nutrient nutrient = new com.mcd.rwd.us.core.bean.Nutrient();
				Map<String, String> item = itr.next();
				String[] parts = item.get(SECONDARY_NUTRINT_LIST).split("_");
				nutrient.setId(parts[0]);
				nutrient.setName(parts[1]);
				if (item.get(SECONDARY_NUTRINT_MARKETING_NAME) != null
						&& !"".equals(item.get(SECONDARY_NUTRINT_MARKETING_NAME))) {
					nutrient.setMarketingName(item.get(SECONDARY_NUTRINT_MARKETING_NAME));
				} else {
					nutrient.setMarketingName(parts[1]);
				}
				
				boolean unit_flag_check = item.containsKey("unitFlag");
				
				if(unit_flag_check){
					unit_flag = item.get("unitFlag");
					if (unit_flag != null && !"".equals(unit_flag) && unit_flag.equalsIgnoreCase("bothUnit")) {
						nutrient.setMeasurementUnitFlag("true");
						nutrient.setPercentageUnitFlag("true");
					} else if (unit_flag != null && !"".equals(unit_flag)
							&& unit_flag.equalsIgnoreCase("measurementUnit")) {
						nutrient.setMeasurementUnitFlag("true");
						nutrient.setPercentageUnitFlag("false");
					} else {
						nutrient.setMeasurementUnitFlag("false");
						nutrient.setPercentageUnitFlag("true");
					}
					LOGGER.debug("Unit Flag Status:" + unit_flag);
				} else {
					nutrient.setMeasurementUnitFlag("true");
					nutrient.setPercentageUnitFlag("true");
				}
				
				secondaryNutrientsListMobile.add(nutrient);
				secondaryNutrientsListMobileContainer.add(nutrient);
			}
		} else {
			LOGGER.error("Secondary NutrientListMobile is empty..");
		}
	}

	private void populateSecondaryNutrientsMobile2(final List<Map<String, String>> nutrientList) {
		LOGGER.debug("Inside populate Secondary Nutrients Mobile 2 to populate nutrient list..");
		if (nutrientList != null && !nutrientList.isEmpty()) {
			Iterator<Map<String, String>> itr = nutrientList.iterator();
			while (itr.hasNext()) {

				com.mcd.rwd.us.core.bean.Nutrient nutrient = new com.mcd.rwd.us.core.bean.Nutrient();
				Map<String, String> item = itr.next();
				String[] parts = item.get(SECONDARY_NUTRINT_LIST).split("_");
				nutrient.setId(parts[0]);
				nutrient.setName(parts[1]);
				if (item.get(SECONDARY_NUTRINT_MARKETING_NAME) != null
						&& !"".equals(item.get(SECONDARY_NUTRINT_MARKETING_NAME))) {
					nutrient.setMarketingName(item.get(SECONDARY_NUTRINT_MARKETING_NAME));
				} else {
					nutrient.setMarketingName(parts[1]);
				}
				
				boolean unit_flag_check = item.containsKey("unitFlag");
				
				if(unit_flag_check){
					unit_flag = item.get("unitFlag");
					if (unit_flag != null && !"".equals(unit_flag) && unit_flag.equalsIgnoreCase("bothUnit")) {
						nutrient.setMeasurementUnitFlag("true");
						nutrient.setPercentageUnitFlag("true");
					} else if (unit_flag != null && !"".equals(unit_flag)
							&& unit_flag.equalsIgnoreCase("measurementUnit")) {
						nutrient.setMeasurementUnitFlag("true");
						nutrient.setPercentageUnitFlag("false");
					} else {
						nutrient.setMeasurementUnitFlag("false");
						nutrient.setPercentageUnitFlag("true");
					}
					LOGGER.debug("Unit Flag Status:" + unit_flag);
				} else {
					nutrient.setMeasurementUnitFlag("true");
					nutrient.setPercentageUnitFlag("true");
				}
				
				secondaryNutrientsListMobile2.add(nutrient);
				secondaryNutrientsListMobileContainer.add(nutrient);
			}
		} else {
			LOGGER.error("Secondary NutrientListMobile2 is empty..");
		}
	}

	private void populateSecondaryNutrientsMobile3(final List<Map<String, String>> nutrientList) {
		LOGGER.debug("Inside populate Secondary NutrientsMobile 3 to populate nutrient list..");
		if (nutrientList != null && !nutrientList.isEmpty()) {
			Iterator<Map<String, String>> itr = nutrientList.iterator();
			while (itr.hasNext()) {

				com.mcd.rwd.us.core.bean.Nutrient nutrient = new com.mcd.rwd.us.core.bean.Nutrient();
				Map<String, String> item = itr.next();
				String[] parts = item.get(SECONDARY_NUTRINT_LIST).split("_");
				nutrient.setId(parts[0]);
				nutrient.setName(parts[1]);
				if (item.get(SECONDARY_NUTRINT_MARKETING_NAME) != null
						&& !"".equals(item.get(SECONDARY_NUTRINT_MARKETING_NAME))) {
					nutrient.setMarketingName(item.get(SECONDARY_NUTRINT_MARKETING_NAME));
				} else {
					nutrient.setMarketingName(parts[1]);
				}
				
				boolean unit_flag_check = item.containsKey("unitFlag");
				
				if(unit_flag_check){
					unit_flag = item.get("unitFlag");
					if (unit_flag != null && !"".equals(unit_flag) && unit_flag.equalsIgnoreCase("bothUnit")) {
						nutrient.setMeasurementUnitFlag("true");
						nutrient.setPercentageUnitFlag("true");
					} else if (unit_flag != null && !"".equals(unit_flag)
							&& unit_flag.equalsIgnoreCase("measurementUnit")) {
						nutrient.setMeasurementUnitFlag("true");
						nutrient.setPercentageUnitFlag("false");
					} else {
						nutrient.setMeasurementUnitFlag("false");
						nutrient.setPercentageUnitFlag("true");
					}
					LOGGER.debug("Unit Flag Status:" + unit_flag);
				} else {
					nutrient.setMeasurementUnitFlag("true");
					nutrient.setPercentageUnitFlag("true");
				}
				
				secondaryNutrientsListMobile3.add(nutrient);
				secondaryNutrientsListMobileContainer.add(nutrient);
			}
		} else {
			LOGGER.error("Secondary NutrientListMobile3 is empty..");
		}
	}

	private Map<Integer, ProductIngredients> populateIngredients(Item item) {

		String productComponentFolder = valueMap.get("productComponentsFolder", "#");

		if (item.getComponents() != null && item.getComponents().getComponent() != null
				&& !item.getComponents().getComponent().isEmpty()) {

			for (int i = 0; i < item.getComponents().getComponent().size(); i++) {
				if ("Core Product".equalsIgnoreCase(item.getComponents().getComponent().get(i).getProductTypeName())) {
					ProductIngredients ingredientInfo = new ProductIngredients();
					ingredientInfo.setName(item.getComponents().getComponent().get(i).getProductMarketingName() != null
							? item.getComponents().getComponent().get(i).getProductMarketingName()
							: item.getComponents().getComponent().get(i).getProductName());
					HeroImage componentImage = item.getComponents().getComponent().get(i).getAttachComponentImage();
					if (componentImage != null && !StringUtils.isBlank(componentImage.getImageName())) {

						ingredientInfo.setImagePath(productComponentFolder + ApplicationConstants.PATH_SEPARATOR
								+ componentImage.getImageName());
					} else {

						ingredientInfo.setImagePath(productComponentFolder + ApplicationConstants.PATH_SEPARATOR
								+ ApplicationConstants.DEFAULT_PRODUCT_IMAGE_NAME);

					}
					ingredients.put(item.getComponents().getComponent().get(i).getDisplayOrder(), ingredientInfo);
				}
			}
		}

		if (item.getMutexGroups() != null && item.getMutexGroups().getMutexGroup() != null
				&& !item.getMutexGroups().getMutexGroup().isEmpty()) {

			for (int i = 0; i < item.getMutexGroups().getMutexGroup().size(); i++) {

				MutexGroup mutexGroup = item.getMutexGroups().getMutexGroup().get(i);

				if (mutexGroup.getConstituents() != null && mutexGroup.getConstituents().getConstituent() != null
						&& !mutexGroup.getConstituents().getConstituent().isEmpty()) {

					for (int k = 0; k < mutexGroup.getConstituents().getConstituent().size(); k++) {

						Constituent constituent = mutexGroup.getConstituents().getConstituent().get(k);

						if (constituent.isDefault()) {

							ProductIngredients ingredientInfo = new ProductIngredients();
							ingredientInfo.setName(constituent.getProductMarketingName() != null
									? constituent.getProductMarketingName() : constituent.getProductName());
							HeroImage componentImage = constituent.getAttachComponentImage();
							if (componentImage != null && !StringUtils.isBlank(componentImage.getImageName())) {

								ingredientInfo.setImagePath(productComponentFolder + ApplicationConstants.PATH_SEPARATOR
										+ componentImage.getImageName());
							} else {

								ingredientInfo.setImagePath(productComponentFolder + ApplicationConstants.PATH_SEPARATOR
										+ ApplicationConstants.DEFAULT_PRODUCT_IMAGE_NAME);

							}
							ingredients.put(mutexGroup.getDisplayOrder(), ingredientInfo);

						}

					}

				}

			}

		}

		List<ProductIngredients> list = new ArrayList<ProductIngredients>(ingredients.values());

		ingredientsList = list;

		return ingredients;

	}

	private void encodeProductMarketingName(ItemsResponse productInfo) {
		if (productInfo.getItem().getItemMarketingName() != null) {
			try {
				encodedProductMarketingName = URLEncoder.encode(productInfo.getItem().getItemMarketingName(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				LOGGER.error("Error in ProductDetial Handler while Decoding Product Marketing nmae", e);
			}
		}

	}

	private List<ItemComponent> evaluateComponentList(List<ItemComponent> componentList) {
		List<ItemComponent> compList = new ArrayList<ItemComponent>();
		for (int i = 0; i < componentList.size(); i++) {
			if ("Core Product".equalsIgnoreCase(componentList.get(i).getProductTypeName())) {
				compList.add(componentList.get(i));
			}
		}

		return compList;
	}

	/**
	 * Method to query DNA Webservice and fetch data.
	 */
	private ItemsResponse fetchItemDetailFromService(String itemId, String nutritionAttributeIds) {
		LOGGER.info("In ProductDetailHandler product Id on page is" + itemId);
		StringBuilder url = new StringBuilder();
		if (StringUtils.isBlank(showLiveDataValue)) {
			showLiveDataValue = String.valueOf(true);
		}

		url.append(domain).append(itemDetailsByIdService).append(ApplicationConstants.URL_QS_START)
				.append(ApplicationConstants.PN_COUNTRY).append(ApplicationConstants.PN_EQUALS)
				.append(mcdFactoryConfig.getDnaCountryCode()).append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
				.append(ApplicationConstants.PN_LANGUAGE).append(ApplicationConstants.PN_EQUALS)
				.append(mcdFactoryConfig.getDnaLanguageCode()).append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
				.append(ApplicationConstants.PN_EXTERNAL_ITEM_ID).append(ApplicationConstants.PN_EQUALS).append(itemId)
				.append(ApplicationConstants.URL_QS_DELIMITER_CHAR).append(ApplicationConstants.PN_SHOW_LIVE_DATA)
				.append(ApplicationConstants.PN_EQUALS).append(showLiveDataValue)
				.append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
				.append(ApplicationConstants.PN_NUTRITION_ATTRIBUTE_ID).append(ApplicationConstants.PN_EQUALS)
				.append(nutritionAttributeIds);
		String itemDetailUrl = url.toString();
		ConnectionUtil connUtil = new ConnectionUtil();
		try {
			LOGGER.error(" In ProductDetailHandler Requesting Item Details using {}", itemDetailUrl);
			String itemDetail = connUtil.sendGet(itemDetailUrl);
			if (itemDetail != null) {
				return getGson().fromJson(itemDetail, ItemsResponse.class);
			}
		} catch (Exception e) {
			LOGGER.error("Data Not Found for item {}", itemId);
			LOGGER.error("Error in ProductDetailHandler class", e);
		}
		return null;
	}

	public ItemsResponse getProductInfo() {
		return productInfo;
	}

	public void setProductInfo(ItemsResponse productInfo) {
		this.productInfo = productInfo;
	}

	public void setProductFooter(ItemsResponse productInfo) {
		if (productInfo.getItem() != null && productInfo.getItem().getFooter() != null) {
			Map<String, String> map = productInfo.getItem().getFooter();
			for (Map.Entry<String, String> entry : map.entrySet()) {
				if ("Yes".equals(entry.getValue())) {
					if (entry.getKey()!=null && entry.getKey() instanceof String) {
						String number = entry.getKey().substring(entry.getKey().length() - 1);
						footerList.add(map.get("footer_name"+number) != null ? map.get("footer_name"+number) : "");
					}
				}
			}
		}
		//footerList.add(getSiteConfig("legalfooter").get("text", String.class));
	}

	/**
	 * Method sets path for Product Desktop and mobile Images
	 */
	public void setProductImagePath(ItemsResponse productInfo) {
		String productDesktopFolder = valueMap.get("productPageDesktopFolder", String.class);
		String productMobileFolder = valueMap.get("productPageMobileFolder", String.class);
		String productSearchImageFolder = valueMap.get("productSearchImageFolder", String.class);
		HeroImage prodImg = productInfo.getItem().getAttachItemThumbnailImage();
		HeroImage transImg = productInfo.getItem().getAttachItemTransparentImage();
		if (prodImg != null && transImg != null) {
			String productImage = prodImg.getImageName();
			String transparentImage = transImg.getImageName();
			if (StringUtils.isNotBlank(productImage)) {
				productInfo.getItem().setProductDesktopImagePath(
						productDesktopFolder + ApplicationConstants.PATH_SEPARATOR + productImage);
				productInfo.getItem().setProductMobileImagePath(
						productMobileFolder + ApplicationConstants.PATH_SEPARATOR + productImage);
				productInfo.getItem().setProductSearchImagePath(
						productSearchImageFolder + ApplicationConstants.PATH_SEPARATOR + transparentImage);
			} else {
				// NO Name Coming from dNA so will set default Image.
				productInfo.getItem().setProductDesktopImagePath(productDesktopFolder
						+ ApplicationConstants.PATH_SEPARATOR + ApplicationConstants.DEFAULT_PRODUCT_IMAGE_NAME);
				productInfo.getItem().setProductMobileImagePath(productMobileFolder
						+ ApplicationConstants.PATH_SEPARATOR + ApplicationConstants.DEFAULT_PRODUCT_IMAGE_NAME);
				productInfo.getItem().setProductSearchImagePath(productSearchImageFolder
						+ ApplicationConstants.PATH_SEPARATOR + ApplicationConstants.DEFAULT_TRANSPARENT_IMAGE_NAME);
			}
		} else {
			// NO Name Coming from dNA so will set default Image.
			productInfo.getItem().setProductDesktopImagePath(productDesktopFolder + ApplicationConstants.PATH_SEPARATOR
					+ ApplicationConstants.DEFAULT_PRODUCT_IMAGE_NAME);
			productInfo.getItem().setProductMobileImagePath(productMobileFolder + ApplicationConstants.PATH_SEPARATOR
					+ ApplicationConstants.DEFAULT_PRODUCT_IMAGE_NAME);
			productInfo.getItem().setProductSearchImagePath(productSearchImageFolder
					+ ApplicationConstants.PATH_SEPARATOR + ApplicationConstants.DEFAULT_TRANSPARENT_IMAGE_NAME);
		}

	}

	/**
	 * Method sets path for Component Image
	 */
	public void setComponentImagePath(ItemsResponse productInfo) {
		String productComponentFolder = valueMap.get("productComponentsFolder", String.class);
		if (productInfo != null && productInfo.getItem().getComponents() != null
				&& productInfo.getItem().getComponents().getComponent() != null) {
			List<ItemComponent> componentList = productInfo.getItem().getComponents().getComponent();
			for (int componentIndex = 0; componentIndex < componentList.size(); componentIndex++) {
				ItemComponent comp = componentList.get(componentIndex);
				HeroImage componentImage = comp.getAttachComponentImage();
				if (componentImage != null)
					evaluateProductComponentName(productComponentFolder, componentImage);
				else {
					// No Image Name is Coming from DNA so will set Default
					// Component Image
					componentImage = new HeroImage();
					componentImage.setImagePath(productComponentFolder + ApplicationConstants.PATH_SEPARATOR
							+ ApplicationConstants.DEFAULT_PRODUCT_IMAGE_NAME);
				}

				comp.setAttachComponentImage(componentImage);
			}
		}
	}

	private void evaluateProductComponentName(String productComponentFolder, HeroImage componentImage) {
		String compImageName = componentImage.getImageName();
		if (StringUtils.isNotBlank(compImageName)) {
			componentImage.setImagePath(productComponentFolder + ApplicationConstants.PATH_SEPARATOR + compImageName);
		} else {
			// No Name is coming from DNA so will set Default Image.
			componentImage.setImagePath(productComponentFolder + ApplicationConstants.PATH_SEPARATOR
					+ ApplicationConstants.DEFAULT_PRODUCT_IMAGE_NAME);
		}
	}

	private List<com.mcd.rwd.us.core.bean.product.Nutrient> getList(final List<Map<String, String>> values) {
		List<com.mcd.rwd.us.core.bean.product.Nutrient> list = new ArrayList();

		if (values != null && !values.isEmpty()) {
			Iterator<Map<String, String>> itr = values.iterator();
			while (itr.hasNext()) {
				Map<String, String> item = itr.next();
				if (StringUtils.isNotBlank(item.get(ApplicationConstants.NUTRITION))) {
					com.mcd.rwd.us.core.bean.product.Nutrient nutrient = new com.mcd.rwd.us.core.bean.product.Nutrient();
					nutrient.setId(Integer.valueOf(item.get(ApplicationConstants.NUTRITION)));
					nutrient.setName(item.get(ApplicationConstants.PN_TITLE));
					list.add(nutrient);
				}
			}
		}
		return list;
	}

	private String getId(final List<com.mcd.rwd.us.core.bean.product.Nutrient> list) {
		List<String> ids = new ArrayList<String>();

		if (list != null && !list.isEmpty()) {
			Iterator itr = list.iterator();
			while (itr.hasNext()) {
				com.mcd.rwd.us.core.bean.product.Nutrient nutrient = (com.mcd.rwd.us.core.bean.product.Nutrient) itr
						.next();
				ids.add(String.valueOf(nutrient.getId()));
			}
		}
		return StringUtils.join(ids, '|');
	}

	private List<com.mcd.rwd.us.core.bean.product.Nutrient> getNutritionData(
			final List<com.mcd.rwd.us.core.bean.product.Nutrient> nutrientsList,
			final List<com.mcd.rwd.us.core.bean.product.Nutrient> nutrients) {
		List<com.mcd.rwd.us.core.bean.product.Nutrient> nutritionData = new ArrayList<com.mcd.rwd.us.core.bean.product.Nutrient>();
		for (int nutrientIndex = 0; nutrientIndex < nutrientsList.size(); nutrientIndex++) {
			for (int j = 0; j < nutrients.size(); j++) {
				com.mcd.rwd.us.core.bean.product.Nutrient nut = nutrients.get(j);
				if (nutrientsList.get(nutrientIndex).getId() == nut.getId()) {
					String name;
					name = (!(nutrientsList.get(nutrientIndex).getName()).equals(StringUtils.EMPTY))
							? nutrientsList.get(nutrientIndex).getName() : nut.getName();
					com.mcd.rwd.us.core.bean.product.Nutrient info = new com.mcd.rwd.us.core.bean.product.Nutrient();
					info.setName(name);
					info.setUom(nut.getUom());
					info.setValue(nut.getValue());
					nutritionData.add(info);
				}
			}
		}

		return nutritionData;
	}

	private Gson getGson() {
		// parse response(using gson) coming from server
		/*
		 * if (gson == null) { Type nutrientItemTypeToken = new
		 * TypeToken<List<com.mcd.rwd.us.core.bean.product.Nutrient>>() {
		 * }.getType(); Type constituentTypeToken = new
		 * TypeToken<List<Constituent>>() { }.getType(); Type
		 * mutexGroupTypeToken = new TypeToken<List<MutexGroup>>() {
		 * }.getType(); Type itemComponentTypeToken = new
		 * TypeToken<List<ItemComponent>>() { }.getType(); gson = new
		 * GsonBuilder() .registerTypeAdapter(itemComponentTypeToken, new
		 * ItemComponentTypeAdapter())
		 * .registerTypeAdapter(constituentTypeToken, new ConstituentAdapter())
		 * .registerTypeAdapter(mutexGroupTypeToken, new
		 * MutexGroupTypeAdapter()) .registerTypeAdapter(nutrientItemTypeToken,
		 * new NutrientTypeAdapter()).create(); }
		 */

		if (gson == null) {
			Type nutrientItemTypeToken = new TypeToken<List<com.mcd.rwd.us.core.bean.product.Nutrient>>() {
			}.getType();
			Type constituentTypeToken = new TypeToken<List<Constituent>>() {
			}.getType();
			Type mutexGroupTypeToken = new TypeToken<List<MutexGroup>>() {
			}.getType();
			Type itemComponentTypeToken = new TypeToken<List<ItemComponent>>() {
			}.getType();
			Type relationTypeToken = new TypeToken<List<RelationType>>() {
			}.getType();
			Type relatedItemTypeToken = new TypeToken<List<RelatedItem>>() {
			}.getType();

			gson = new GsonBuilder().registerTypeAdapter(relationTypeToken, new RelationTypeAdapter())
					.registerTypeAdapter(relatedItemTypeToken, new RelatedItemTypeAdapter())
					.registerTypeAdapter(itemComponentTypeToken, new ItemComponentTypeAdapter())
					.registerTypeAdapter(constituentTypeToken, new ConstituentAdapter())
					.registerTypeAdapter(mutexGroupTypeToken, new MutexGroupTypeAdapter())
					.registerTypeAdapter(nutrientItemTypeToken, new NutrientTypeAdapter()).create();
		}

		return gson;
	}
	
	private void tagRemoval(String oldHyperlink) {
		String[] hyper = oldHyperlink.split("");
		for (int counter = 0; counter < hyper.length; counter++) {
			if (counter > 2 && (hyper.length - (counter + 4)) > 1) {
				hyperlink = hyperlink + hyper[counter];
			}
		}
	}

	public String getNutritionCalculatorURL() {
		return nutritionCalculatorURL;
	}

	public String getNutritionCalcLinkText() {
		return nutritionCalcLinkText;
	}

	public List<String> getFooterList() {
		return footerList;
	}

	public String getShowLiveDataValue() {
		return showLiveDataValue;
	}

	public void setShowLiveDataValue(String showLiveDataValue) {
		this.showLiveDataValue = showLiveDataValue;
	}

	public String getEncodedProductMarketingName() {
		return encodedProductMarketingName;
	}

	public void setEncodedProductMarketingName(String encodedProductMarketingName) {
		this.encodedProductMarketingName = encodedProductMarketingName;
	}

	public List<com.mcd.rwd.us.core.bean.Nutrient> getPcNutrientsList() {
		return pcNutrientsList;
	}

	public String getTab2Text() {
		return tab2Text;
	}

	public String getTab1Text() {
		return tab1Text;
	}

	public String getDnaItemDetailsUrl() {
		return dnaItemDetailsUrl;
	}

	public String getLegalText() {
		return legalText;
	}

	public String getAllergenText() {
		return allergenText;
	}

	public String getServeText() {
		return serveText;
	}

	public Map<Integer, ProductIngredients> getIngredients() {
		return ingredients;
	}

	public List<ProductIngredients> getIngredientsList() {
		return ingredientsList;
	}

	public String getShowGoesWell() {
		return showGoesWell;
	}

	public String getShowMoreItems() {
		return showMoreItems;
	}

	public String getShowIngredients() {
		return showIngredients;
	}

	public String getShowNutritionTabs() {
		return showNutritionTabs;
	}

	public String getDvText() {
		return dvText;
	}

	public String getCustomizeText() {
		return customizeText;
	}

	public List<com.mcd.rwd.us.core.bean.Nutrient> getSecondaryNutrientsList() {
		return secondaryNutrientsList;
	}

	public List<com.mcd.rwd.us.core.bean.Nutrient> getSecondaryNutrientsList2() {
		return secondaryNutrientsList2;
	}

	public List<com.mcd.rwd.us.core.bean.Nutrient> getSecondaryNutrientsList3() {
		return secondaryNutrientsList3;
	}

	public String getSecondaryColCountMobile() {
		return secondaryColCountMobile;
	}

	public String getSecondaryColCount() {
		return secondaryColCount;
	}

	public List<com.mcd.rwd.us.core.bean.Nutrient> getSecondaryNutrientsListMobile3() {
		return secondaryNutrientsListMobile3;
	}

	public List<com.mcd.rwd.us.core.bean.Nutrient> getSecondaryNutrientsListMobile2() {
		return secondaryNutrientsListMobile2;
	}

	public List<com.mcd.rwd.us.core.bean.Nutrient> getSecondaryNutrientsListMobile() {
		return secondaryNutrientsListMobile;
	}

	public String getServesText() {
		return servesText;
	}

	/**
	 * Added for Backlog value for iTemMetaTitle string is set in
	 * setCanonicalURL(ItemsResponse productInfo) method.
	 */
	public String getCanonicalURL() {
		return this.canonicalURL;
	}

	/**
	 * Added for Backlog value for iTemMetaTitle string is set in
	 * setItemMetaTitle(ItemsResponse productInfo) method.
	 */
	public String getItemMetaTitle() {
		return this.itemMetaTitle;
	}

	public String getMetaTitleConstant() {
		return this.metaTitleConstant;
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
	
	public List<com.mcd.rwd.us.core.bean.Nutrient> getSecondaryNutrientsListContainer() {
		return secondaryNutrientsListContainer;
	}

	public void setSecondaryNutrientsListContainer(
			List<com.mcd.rwd.us.core.bean.Nutrient> secondaryNutrientsListContainer) {
		this.secondaryNutrientsListContainer = secondaryNutrientsListContainer;
	}

	public List<com.mcd.rwd.us.core.bean.Nutrient> getSecondaryNutrientsListMobileContainer() {
		return secondaryNutrientsListMobileContainer;
	}

	public void setSecondaryNutrientsListMobileContainer(
			List<com.mcd.rwd.us.core.bean.Nutrient> secondaryNutrientsListMobileContainer) {
		this.secondaryNutrientsListMobileContainer = secondaryNutrientsListMobileContainer;
	}
	
	public String getHyperlink() {
		return hyperlink;
	}

	public void setHyperlink(String hyperlink) {
		this.hyperlink = hyperlink;
	}
	
	public String getNutritionColumnHeading() {
		return nutritionColumnHeading;
	}

	public void setNutritionColumnHeading(String nutritionColumnHeading) {
		this.nutritionColumnHeading = nutritionColumnHeading;
	}

	public String getMeasurementUnitColumnHeading() {
		return measurementUnitColumnHeading;
	}

	public void setMeasurementUnitColumnHeading(String measurementUnitColumnHeading) {
		this.measurementUnitColumnHeading = measurementUnitColumnHeading;
	}

	public String getPercentageUnitColumnHeading() {
		return percentageUnitColumnHeading;
	}

	public void setPercentageUnitColumnHeading(String percentageUnitColumnHeading) {
		this.percentageUnitColumnHeading = percentageUnitColumnHeading;
	}

	public String getAllergenMayText() {
		return allergenMayText;
	}

	public String getItemMetaDescription() {
		return this.itemMetaDescription;
	}
	
	public String getSecondaryNutrientsViewType() {
		return secondaryNutrientsViewType;
	}
	
	public boolean isNutritionInfoAtProductLevel() {
		return isNutritionInfoAtProductLevel;
	}
	public String getOrdernowTitle() {
		return ordernowTitle;
	}

	public String getOrderNowURL() {
		return orderNowURL;
	}
	public String getShowOrderNow() {
		return showOrderNow;
	}
}