package com.mcd.rwd.us.core.sightly;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.editconfig.DropTarget;
import com.citytechinc.cq.component.annotations.widgets.*;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.designer.Style;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mcd.rwd.global.core.components.common.Image;
import com.mcd.rwd.global.core.utils.ConnectionUtil;
import com.mcd.rwd.global.core.utils.LinkUtil;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.us.core.bean.category.*;
import com.mcd.rwd.us.core.bean.evm.Coop;
import com.mcd.rwd.us.core.bean.evm.CoopAdapter;
import com.mcd.rwd.us.core.constants.ApplicationConstants;
import com.mcd.rwd.us.core.constants.PromoConstants;
import com.mcd.rwd.us.core.service.McdDynamicMediaServiceConfig;
import com.mcd.rwd.us.core.service.McdFactoryConfig;
import com.mcd.rwd.us.core.service.McdFactoryConfigConsumer;
import com.mcd.rwd.us.core.service.McdWebServicesConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.ValueFormatException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by deepti_b on 3/11/2016.
 */
@Component(name = "category", value = "Product Category",
		description = "Used to display the details of a category defined in DNA.",
		tabs = {
				@Tab( touchUINodeName = "general" , title = "General Settings" ),
				@Tab( touchUINodeName = "promotab", title = "Promo Tab"),
				@Tab( touchUINodeName = "hero", title = "Desktop Image"),
				@Tab( touchUINodeName = "mobileImg", title = "Mobile Image"),
				@Tab( touchUINodeName = "cornerlogo", title = "Corner Logo Image")},
		actions = { "text: Product Category", "-", "editannotate", "copymove", "delete" },
		group = " GWS-Global", path = "content",
		dropTargets = {
				@DropTarget(propertyName = "./hero/imagePath", accept = {"hero/.*"},
					groups = {"media"}, nodeName = "hero"),
				@DropTarget(propertyName = "./mobileImg/imagePath", accept = {"mobileImg/.*"},
					groups = {"media"}, nodeName = "mobileImg"),
				@DropTarget(propertyName = "./cornerlogo/imagePath", accept = {"cornerlogo/.*"},
					groups = {"media"}, nodeName = "cornerlogo")
		},
		listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
				@Listener(name = "afterinsert", value = "REFRESH_PAGE"),
		@Listener(name = "beforesubmit", value = "function(dialog){var img = dialog.getField('./hero/file').hasData();" +
				"var mobileImg = dialog.getField('./mobile/file').hasData();" +
				"if(!(img)){CQ.Ext.Msg.show({'title':'Catgeory Dialog Validation'," +
				"'msg':'Please provide Desktop Image for Category. ','buttons':CQ.Ext.Msg.OK," +
				"'icon':CQ.Ext.MessageBox.INFO,'scope':this}); " +
				"return false;} " +
				"if(!(mobileImg)){CQ.Ext.Msg.show({'title':'Catgeory Dialog Validation'," +
				"'msg':'Please provide Mobile Image for Category. '," +
				"'buttons':CQ.Ext.Msg.OK,'icon':CQ.Ext.MessageBox.INFO,'scope':this});" +
				" return false;} }")})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CategoryDetailHandler{

	@DialogField(defaultValue = "0", name = "./id", fieldLabel = "Category",
			fieldDescription = "Please select category which are available from web services",
			required = false, ranking = 1D)
	@Selection(optionsProvider = "$PATH.categorylist.json", type = Selection.SELECT,
		dataSource = "mcd-us/dataSource/category")
	@Inject @Named("id") @Default(values = "0")
	private int categoryId;

	@DialogField(name = "./shortDescription", fieldLabel = "Category Tag line")
	@TextField
	@Inject @Named("shortDescription") @Default(values = "")
	private String categoryTagLine;

	@DialogField(name = "./name", fieldLabel = "Marketing Name")
	@TextField
	@Inject @Named("name") @Default(values = StringUtils.EMPTY)
	private String categoryMarketingName;

	@DialogField(name = "./description", fieldLabel = "Category Description", fieldDescription = "Default value: Category description would be retrieved from DNA Web services.")
	@TextField
	@Inject @Named("description") @Default(values = "")
	private String categoryDescription;

	@DialogField(name = "./align", fieldLabel = "Hero Text Alignment", fieldDescription = "Please select the alignment for Hero Text" , required = true)
	@Selection(type = Selection.SELECT,
			options = {
					@Option(text="Left" , value="left"),
					@Option(text="Center" , value="center"),
					@Option(text="Right", value="right")
			})
	@Named("align") @Default(values = ApplicationConstants.DEFAULT_BUTTON_TEXT)
	@Inject
	private String style;

	@DialogField(fieldLabel = "Product Button CTA Text")
	@TextField
	@Inject @Default(values = ApplicationConstants.DEFAULT_BUTTON_TEXT)
	private String productButtonText;

	@DialogField(fieldLabel = "Legal Disclaimer Text", fieldDescription = "Please provide the disclaimer text.")
	@TextField
	@Inject @Default(values = StringUtils.EMPTY)
	private String disclaimer;

	@DialogField(fieldLabel = "Corner Logo Alt Text", fieldDescription = "Please provide alternate text for Corner logo.")
	@TextField
	@Inject @Default(values = StringUtils.EMPTY)
	private String cornerLogoAlt;

	@DialogField(fieldLabel = "Carousel Paths", tab = 2,
		listeners = {
			@Listener(name = "beforeadd", value = "function(list,component,index) { " +
				"if(this.fieldConfig.limit!=0) { " +
				"if((list.items.getCount()) > this.fieldConfig.limit) {" +
				"CQ.Ext.Msg.show( { title : 'Limit reached',     msg : 'You are only allowed to add ' + " +
				"this.fieldConfig.limit + ' items to the Promo List', " +
				"buttons : CQ.Ext.Msg.OK, icon : CQ.Ext.MessageBox.WARNING});" +
				"return false; } } }"
			)
		}, additionalProperties = { @Property(name = "limit", value = "2")}
	)
	@MultiField
	@PathField
	@Inject @Default(values = "")
	private String[] promoList;

	@DialogField(fieldLabel = "Desktop Image", tab = 3)
	@DialogFieldSet(title = "Desktop Image", namePrefix = "hero/")
	@ChildResource(name = "hero")
	@Inject
	private Image hero;

	@DialogField(fieldLabel = "Mobile Image", tab = 4)
	@DialogFieldSet(title = "Mobile Image", namePrefix = "mobileImg/")
	@ChildResource(name = "mobileImg")
	@Inject
	private Image mobileImg;

	@DialogField(fieldLabel = "Corner Logo Image", tab = 5)
	@DialogFieldSet(title = "Corner Logo Image", namePrefix = "cornerlogo/")
	@ChildResource(name = "cornerlogo")
	@Inject
	private Image cornerlogo;

	@Inject
	Page currentPage;

	@Inject
	PageManager pageManager;

	@Inject
	SlingHttpServletRequest request;

	@Inject
	SlingScriptHelper slingScriptHelper;

	@OSGiService
	McdDynamicMediaServiceConfig mcdDynamicMediaServiceConfig;

	@OSGiService
	McdFactoryConfigConsumer mcdFactoryConfigConsumer;

	@OSGiService
	McdWebServicesConfig mcdWebServicesConfig;

	@Inject
	ValueMap valueMap;

	@Inject
	Style currentStyle;

	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryDetailHandler.class);

	/**
	 * key for storing image for the category.
	 */
	private static final String HERO_KEY = "hero";

	/**
	 * key for storing image for the category for mobile.
	 */
	private static final String MOBILE_KEY = "mobile";

	private CategoryItemsDetail category;

	private int currentPageCategoryId;

	private boolean dnaDataRetrieved;

	private McdFactoryConfig mcdFactoryConfig;

	private Map<String, String> configMap;

	private List<CategoryItem> iconicProductsList;

	private List<String> promoResourceList;

	private String iconicDesktopFolderPath;

	private String categoryTextAlign;

	private String showLiveDataValue;

	private String disclaimerText;

	// private String cornerLogoAlt = StringUtils.EMPTY;

	private String cornerlogoImagePath = StringUtils.EMPTY;
	private String dynamicMediaLargeParameter = StringUtils.EMPTY;
	private String dynamicMediaMediumParameter = StringUtils.EMPTY;
	private String dynamicMediaSmallParameter = StringUtils.EMPTY;
	private String dynamicMediaXSmallParameter =StringUtils.EMPTY;
	private String dynamicMediaServerContextURL = StringUtils.EMPTY;

	@PostConstruct
	public void init() throws ValueFormatException {
		if(null!=valueMap) {
			setCurrentPageCategoryId();
			showLiveDataValue = request.getParameter("showLiveData");
			categoryTextAlign = this.style;
			//productButtonText = valueMap.get("productButtonText", ApplicationConstants.DEFAULT_BUTTON_TEXT);
			//cornerlogoImagePath = ImageUtil.getImagePath(getResource(), ApplicationConstants.PN_CORNER_LOGO);
			if (null != cornerlogo)
				cornerlogoImagePath = cornerlogo.getImagePath();
			//cornerLogoAlt = valueMap.get(ApplicationConstants.PN_CORNER_LOGO_ALT, String.class);
			disclaimerText = this.disclaimer;
		}

		configMap = populateOsgiConfigurations();
		populateDesignDialogParams();
		retrieveCategoriesInfoFromServer();
		addPromoComponents();

		if(null!=mcdDynamicMediaServiceConfig)
		setDynamicMediaServerContextURL(mcdDynamicMediaServiceConfig.getImageServerUrl(request,
				slingScriptHelper));
		if(StringUtils.isNotEmpty(dynamicMediaServerContextURL)){
			setDynamicMediaLargeParameter(mcdDynamicMediaServiceConfig.getImagePresetIconicThumbnailLarge());
			setDynamicMediaMediumParameter(mcdDynamicMediaServiceConfig.getImagePresetIconicThumbnailMedium());
			setDynamicMediaSmallParameter(mcdDynamicMediaServiceConfig.getImagePresetIconicThumbnailSmall());
			setDynamicMediaXSmallParameter(mcdDynamicMediaServiceConfig.getImagePresetIconicThumbnailXSmall());

		}
	}

	/**
	 * Method sets the current CategoryID configured on page.
	 */
	public void setCurrentPageCategoryId() {
		this.currentPageCategoryId = categoryId>0 ? categoryId : ApplicationConstants.DEFAULT_INT;
		LOGGER.info("CategoryId configured on page is {}", currentPageCategoryId);
	}

	/**
	 * Method sets the category Information after querying from DNA Webservice
	 */
	public void retrieveCategoriesInfoFromServer() {
		CategoryItemsDetail categoryPageDetails = getCategoryPagesDetails();
		ConnectionUtil connUtil = new ConnectionUtil();
		if (categoryPageDetails.getCategoryId() != ApplicationConstants.DEFAULT_INT) {
			try {
				String catDetailFinalUrl = getCategoryDetailsUrl(
						Integer.toString(categoryPageDetails.getCategoryId()));
				LOGGER.info("In CategoryDetailHandler Requesting ... {}", catDetailFinalUrl);
				String itemsResponse = connUtil.sendGet(catDetailFinalUrl);
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
						.registerTypeAdapter(relationTypeToken, new RelationTypeAdapter())
						.registerTypeAdapter(coopTypeToken, new CoopAdapter())
						.registerTypeAdapter(itemTypeToken, new CategoryItemTypeAdapter())
						.registerTypeAdapter(relatedItemTypeToken, new RelatedItemTypeAdapter()).create();
				CategoryItemsResponse categoryItemsResponse = gson
						.fromJson(itemsResponse, CategoryItemsResponse.class);
				if (categoryItemsResponse != null)
					processCategoryItemsResponse(categoryPageDetails, categoryItemsResponse);
				else {
					setDnaDataRetrieved(false);
					LOGGER.error("Unable to parse json for the category id {} . Json might be null or "
									+ "incorrect for the url: {}", categoryPageDetails.getCategoryId(),
							catDetailFinalUrl);
				}
			} catch (Exception e) {
				LOGGER.error("Error in CategoryDetailsHandler class", e);
			}
		}
	}

	private void processCategoryItemsResponse(CategoryItemsDetail categoryPageDetails,
											  CategoryItemsResponse categoryItemsResponse) {
		CategoryItemsDetail categoryItemDetailsFromServer = categoryItemsResponse.getCategory();
		if (categoryItemDetailsFromServer != null) {
			setDnaDataRetrieved(true);
			LOGGER.info("Retrieved details from server for the Category id {}",
					categoryItemDetailsFromServer.getCategoryId());
			categoryItemDetailsFromServer = processCategoryDetailsItemFromServer(
					categoryItemDetailsFromServer, categoryPageDetails);
			fetchIconicProductsList(categoryItemDetailsFromServer);
			this.category = categoryItemDetailsFromServer;

		} else {
			setDnaDataRetrieved(false);
			LOGGER.error("CategoryItemsDetail from the server is null for category id: {}",
					categoryPageDetails.getCategoryId());
		}
	}

	/**
	 * Method retrieves all category Information configured by author in dialog and return the object
	 */
	public CategoryItemsDetail getCategoryPagesDetails() {
		CategoryItemsDetail categoryItemsBean = new CategoryItemsDetail();
		Resource categoryResource = request.getResource();
		if (categoryResource != null) {
			ValueMap categoryValueMap = categoryResource.adaptTo(ValueMap.class);
			int categoryId = this.currentPageCategoryId;
			if (categoryId != ApplicationConstants.DEFAULT_INT) {
				categoryItemsBean.setCategoryId(categoryId);
				categoryItemsBean.setCategoryMarketingName(this.categoryMarketingName);
				categoryItemsBean.setCategoryShortDescription(this.categoryTagLine);
				categoryItemsBean.setCategoryDescription(this.categoryDescription);
				categoryItemsBean
						.setHeroTextAlignment(this.style);
				if(hero!=null)
				categoryItemsBean.setCategoryHeroImagePath(hero.getImagePath());
				if(mobileImg!=null)
				categoryItemsBean.setCategoryMobileImagePath(mobileImg.getImagePath());
			}

		} else {
			LOGGER.error("can't resolve to a resource in getCategoryPagesDetails()");
		}
		return categoryItemsBean;
	}

	/**
	 * Method take category Information from DNA as well that configured by author nad finally compuites the Category object
	 */
	private CategoryItemsDetail processCategoryDetailsItemFromServer(
			CategoryItemsDetail categoryItemDetailsFromServer, CategoryItemsDetail categoryPageDetails) {
		// if category description is configured on page, use that description. If not,then description coming from server will be used.
		if (categoryPageDetails.getCategoryDescription() != null) {
			categoryItemDetailsFromServer
					.setCategoryDescription(categoryPageDetails.getCategoryDescription());
		}
		// if category marketing name is configured on tha page, use that name else the marketting name
		// coming from server will be used
		if (categoryPageDetails.getCategoryMarketingName() != null) {
			categoryItemDetailsFromServer
					.setCategoryMarketingName(categoryPageDetails.getCategoryMarketingName());
		}
		categoryItemDetailsFromServer.setHeroTextAlignment(categoryPageDetails.getHeroTextAlignment());
		categoryItemDetailsFromServer
				.setCategoryShortDescription(categoryPageDetails.getCategoryShortDescription());
		categoryItemDetailsFromServer
				.setCategoryHeroImagePath(categoryPageDetails.getCategoryHeroImagePath());
		categoryItemDetailsFromServer
				.setCategoryMobileImagePath(categoryPageDetails.getCategoryMobileImagePath());
		CategoryItems catItems = categoryItemDetailsFromServer.getItems();
		if (catItems != null) {
			CategoryItem[] catItemArray = catItems.getItem();
			if (catItemArray != null && catItemArray.length > 0) {
				callCategoryItem(catItemArray);
			}
		}

		return categoryItemDetailsFromServer;
	}

	private void callCategoryItem(CategoryItem[] catItemArray) {
		for (int catItemIndex = 0; catItemIndex < catItemArray.length; catItemIndex++) {
			CategoryItem catItem = catItemArray[catItemIndex];
			if (null != catItem) {
				catItem.setCategoryItemFullUrl(getCategoryItemPageURL(catItem));
				setProductVisibility(catItem);
				setAllProductImages(catItem);
				LOGGER.error("Specialization Text 1" + catItem.getSpecializationText1());
				LOGGER.error("Specialization Text 1" + catItem.getSpecializationText2());
				if (catItem.getSpecializationText1() != null && catItem.getSpecializationText2() != null) {
					catItem.setWrapperClass("tag-limited-and-market");
				} else if (catItem.getSpecializationText1() != null) {
					catItem.setWrapperClass("tag-limited");
				} else if (catItem.getSpecializationText2() != null) {
					catItem.setWrapperClass("tag-market");
				} else {
					catItem.setWrapperClass("no-class");
				}
			}
		}
	}

	private void setAllProductImages(CategoryItem catItem) {
		CategoryItemThumbnailImage categoryItemImage = catItem.getAttachItemThumbnailImage();
		if (categoryItemImage != null) {
			String imageName = categoryItemImage.getImageName();
			if (StringUtils.isNotBlank(imageName)) {
				setImageForProducts(catItem, imageName);
			} else {
				// Name from DNA is not coming so setting default Image for all sizes
				setImageForProducts(catItem, ApplicationConstants.DEFAULT_PRODUCT_IMAGE_NAME);
			}
		} else {
			// Name from DNA is not coming so setting  default Image for all sizes
			setImageForProducts(catItem, ApplicationConstants.DEFAULT_PRODUCT_IMAGE_NAME);
		}
	}

	private void setImageForProducts(CategoryItem catItem, String imageName) {
		catItem.setItemIconicDesktopImagePath(
				iconicDesktopFolderPath + ApplicationConstants.PATH_SEPARATOR + imageName);
	}

	/**
	 * Method compute the URL of each Item in category
	 */
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

	private void setProductVisibility(CategoryItem categoryItem) {
		if (!(ApplicationConstants.DO_NOT_SHOW.equalsIgnoreCase(categoryItem.getDoNotShow()))) {
			if (categoryItem.getRelationTypes() != null
					&& categoryItem.getRelationTypes().getRelationType() != null) {
				List<RelationType> relationTypeList = categoryItem.getRelationTypes().getRelationType();
				evaluateItemVisiblity(relationTypeList, categoryItem);
			} else {
				categoryItem.setItemVisibility(true);
			}
		}

	}

	private void evaluateItemVisiblity(List<RelationType> relationTypeList, CategoryItem categoryItem) {
		boolean itemvisiblity = false;
		for (RelationType relationType : relationTypeList) {
			itemvisiblity = false;
			if (relationType.getType() != null && relationType.getRelatedItems() != null
					&& relationType.getRelatedItems().getRelatedItem() != null)
				itemvisiblity = isItemvisiblity(categoryItem, relationType);
			if (!itemvisiblity)
				break;
		}
		categoryItem.setItemVisibility(itemvisiblity);
	}

	private boolean isItemvisiblity(CategoryItem categoryItem, RelationType relationType) {
		boolean itemvisiblity = false;
		List<RelatedItem> relatedItemArray = relationType.getRelatedItems().getRelatedItem();
		for (RelatedItem relatedItem : relatedItemArray) {
			if (categoryItem.getId() == relatedItem.getId()) {
				itemvisiblity = relatedItem.getIsDefault();
				break;
			}
		}
		return itemvisiblity;
	}

	private String getCategoryDetailsUrl(String pCategoryID) {
		StringBuilder catUrl = new StringBuilder();
		String catId = (null == pCategoryID || pCategoryID.equalsIgnoreCase(StringUtils.EMPTY)) ?
				Integer.toString(ApplicationConstants.DEFAULT_INT) :
				pCategoryID;

		catUrl.append(configMap.get("categoryDetailUrl")).append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
				.append(ApplicationConstants.PN_CATEGORY_ID).append(ApplicationConstants.PN_EQUALS)
				.append(catId).append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
				.append(ApplicationConstants.PN_COPS_FILTER).append(ApplicationConstants.PN_EQUALS)
				.append("true").append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
				.append(ApplicationConstants.PN_SHOW_NATIONAL_COOP).append(ApplicationConstants.PN_EQUALS)
				.append(true);

		return catUrl.toString();

	}

	private void fetchIconicProductsList(CategoryItemsDetail catobject) {
		CategoryItem[] itemsArr = catobject.getItems().getItem();
		iconicProductsList = new ArrayList<CategoryItem>();
		for (CategoryItem categoryItem :itemsArr ) {
			if (!(ApplicationConstants.DO_NOT_SHOW.equalsIgnoreCase(categoryItem.getDoNotShow()))) {
				if (categoryItem.getRelationTypes() != null
						&& categoryItem.getRelationTypes().getRelationType() != null) {
					List<RelationType> relationTypeList = categoryItem.getRelationTypes().getRelationType();
					evaluateItemVisiblity(relationTypeList, categoryItem);
				} else {
					categoryItem.setItemVisibility(true);
				}

				if (categoryItem.isItemVisibility()) {
					iconicProductsList.add(categoryItem);
				}
			}
		}

		/*for (int j = 0; j < iconicProductsList.size(); j++) {
			itemsArr = (CategoryItem[]) ArrayUtils.removeElement(itemsArr, iconicProductsList.get(j));
		}*/
		//catobject.getItems().setItem(itemsArr);

	}

	/**
	 * Populates the properties from the OSGi Config.
	 */
	private Map<String, String> populateOsgiConfigurations() {
		Map<String, String> data = new HashMap<String, String>();
		StringBuilder url = new StringBuilder();
		String country = PageUtil.getCountry(currentPage);
		String language = PageUtil.getLanguage(currentPage);
		LOGGER.info(" In CategoryDetailHandler  Resource Country Code is" + country
				+ "Resource Language Code is" + language);
		if (StringUtils.isBlank(showLiveDataValue)) {
			showLiveDataValue = String.valueOf(true);
		}

		if (country != null && language != null) {

			if (mcdFactoryConfigConsumer != null && mcdWebServicesConfig != null) {
				mcdFactoryConfig = mcdFactoryConfigConsumer
						.getMcdFactoryConfig(country, language);
				if (mcdFactoryConfig != null) {
					url.append(mcdWebServicesConfig.getDomain())
							.append(mcdWebServicesConfig.getCategoryDetailUrl())
							.append(ApplicationConstants.URL_QS_START).append(ApplicationConstants.PN_COUNTRY)
							.append(ApplicationConstants.PN_EQUALS)
							.append(mcdFactoryConfig.getDnaCountryCode())
							.append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
							.append(ApplicationConstants.PN_LANGUAGE).append(ApplicationConstants.PN_EQUALS)
							.append(mcdFactoryConfig.getDnaLanguageCode())
							.append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
							.append(ApplicationConstants.PN_SHOW_LIVE_DATA)
							.append(ApplicationConstants.PN_EQUALS).append(showLiveDataValue);

					data.put("productShortUrlRequired",
							Boolean.toString(mcdFactoryConfig.isProductShortUrlRequired()));
					data.put("productPagePath", mcdFactoryConfig.getProductPagePath());
					data.put("categoryDetailUrl", url.toString());

				} else {
					LOGGER.error(
							"No Factory Configuration found for the country - {} and language - {} in CategoryDetailHandler in populateOsgiConfigurations method",
							country, language);
				}
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

	private void addPromoComponents() {
		this.promoResourceList = new ArrayList<String>();
		String[] paths = valueMap.get("promoList", String[].class);
		if(null!=paths) {
			for (String path : paths) {
				LOGGER.info("In categoryHandler Promo Configured on page are", path);
				Resource promoResource = request.getResourceResolver()
						.getResource(path + PromoConstants.JCR_CONTENT_PROMO);
				if (promoResource != null) {
					this.promoResourceList.add(path + PromoConstants.JCR_CONTENT_PROMO);
				}
				LOGGER.info("In categoryHandler Number of Promo Configured on Catgeory page are",
						promoResourceList.size());
			}
		}
	}

	private void populateDesignDialogParams() {
		//ValueMap props = getCurrentStyle();
		if (null!=currentStyle)
		iconicDesktopFolderPath = currentStyle.get("desktopIconicFolder", String.class);
	}

	public int getCurrentPageCategoryId() {
		return this.currentPageCategoryId;
	}

	public boolean isDnaDataRetrieved() {
		return dnaDataRetrieved;
	}

	public void setDnaDataRetrieved(boolean dnaDataRetrieved) {
		this.dnaDataRetrieved = dnaDataRetrieved;
	}

	public CategoryItemsDetail getCategory() {
		return category;
	}

	public List<CategoryItem> getIconicProductsList() {
		return iconicProductsList;
	}

	public void setIconicProductsList(List<CategoryItem> iconicProductsList) {
		this.iconicProductsList = iconicProductsList;
	}

	public List<String> getPromoResourceList() {
		return promoResourceList;
	}

	public String getIconicDesktopFolderPath() {
		return iconicDesktopFolderPath;
	}

	public String getCategoryTextAlign() {
		return categoryTextAlign;
	}

	public String getProductButtonText() {
		return productButtonText;
	}

	public String getShowLiveDataValue() {
		return showLiveDataValue;
	}

	public void setShowLiveDataValue(String showLiveDataValue) {
		this.showLiveDataValue = showLiveDataValue;
	}

	public String getDisclaimerText() {
		return disclaimerText;
	}

	public String getCornerLogoAlt() {
		return cornerLogoAlt;
	}

	public String getCornerlogoImagePath() {
		return cornerlogoImagePath;
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