package com.mcd.rwd.us.core.sightly;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.editconfig.DropTarget;
import com.citytechinc.cq.component.annotations.widgets.*;
import com.day.cq.wcm.api.Page;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.icfolson.aem.multicompositeaddon.widget.MultiCompositeField;
import com.mcd.rwd.global.core.components.common.Image;
import com.mcd.rwd.global.core.utils.ConnectionUtil;
import com.mcd.rwd.global.core.utils.LinkUtil;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.us.core.bean.HappyMealBean;
import com.mcd.rwd.us.core.bean.HappyMealItem;
import com.mcd.rwd.us.core.bean.category.*;
import com.mcd.rwd.us.core.bean.evm.Coop;
import com.mcd.rwd.us.core.bean.evm.CoopAdapter;
import com.mcd.rwd.us.core.constants.ApplicationConstants;
import com.mcd.rwd.us.core.service.McdDynamicMediaServiceConfig;
import com.mcd.rwd.us.core.service.McdFactoryConfig;
import com.mcd.rwd.us.core.service.McdFactoryConfigConsumer;
import com.mcd.rwd.us.core.service.McdWebServicesConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dipankar Gupta on 3/24/2016.
 */

@Component(
		name = "happyMeal",
		value = "Happy Meal",
		path = "content",
		group = " GWS-Global",
		dropTargets = {
			@DropTarget(propertyName = "./image/imagePath", accept = {"image/.*"}, groups = {"media"},
					nodeName = "image")
		},
		tabs = {
			@Tab(title = "Image Configurations"), @Tab(title = "Advanced")
		},
		listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
				@Listener(name = "afterinsert", value = "REFRESH_PAGE")
		}
)
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HappyMeal {

	private static final Logger LOGGER = LoggerFactory.getLogger(HappyMeal.class);

	private String defaultItems;

	private String imageHref;

	private String ariaLabel1;

	private String dynamicMediaLargeParameter = StringUtils.EMPTY;
	private String dynamicMediaMediumParameter = StringUtils.EMPTY;
	private String dynamicMediaSmallParameter = StringUtils.EMPTY;
	private String dynamicMediaXSmallParameter =StringUtils.EMPTY;
	private String dynamicMediaServerContextURL = StringUtils.EMPTY;

	private List<CategoryDetail> categoryDetailList = new ArrayList<CategoryDetail>();

	private List<HappyMealBean> happyMealList = new ArrayList<HappyMealBean>();

	private McdFactoryConfig mcdFactoryConfig;

	public String getButtonTitle() {
		return buttonTitle;
	}

	public String getLink() {
		return link;
	}

	public String getButtonName() {
		return buttonName;
	}

	public String getImageSubTitle() {
		return imageSubTitle;
	}

	public String getImageHref() {
		return imageHref;
	}

	public List<HappyMealBean> getHappyMealList() {
		return happyMealList;
	}

	@Inject @Via("resource")
	Resource resource;

	@Inject
	SlingHttpServletRequest request;

	@Inject
	SlingScriptHelper slingScriptHelper;

	@Inject
	Page currentPage;

	@OSGiService
	McdDynamicMediaServiceConfig mcdDynamicMediaServiceConfig;

	@OSGiService
	McdFactoryConfigConsumer mcdFactoryConfigConsumer;

	@OSGiService
	McdWebServicesConfig mcdWebServicesConfig;

	@DialogFieldSet(title = "Image", namePrefix = "image/")
	@DialogField(tab = 1)
	@Inject
	@ChildResource(name = "image")
	private Image image;

	@DialogField(fieldLabel = "Nutrition Calculator Url", required = true, additionalProperties =
			{@Property(name = "disabled", value = "{Boolean}false")}, tab = 2)
	@PathField
	@Inject
	private String link;

	@DialogField(fieldLabel = "Image Sub Title", tab = 2)
	@TextField
	@Inject
	private String imageSubTitle;

	@DialogField(fieldLabel = "Button Title", name = "./buttontitle", tab = 2)
	@TextField
	@Inject @Named("buttontitle")
	private String buttonTitle;

	@DialogField(fieldLabel = "Button Name", name = "./buttonname", required = true, tab = 2)
	@TextField
	@Inject @Named("buttonname")
	private String buttonName;

	@DialogField(fieldLabel = "Item Image Folder Path", fieldDescription = "name of Image should be in this format " +
			"- t-mcdonalds-Cheeseburger.png", required = true, name = "./itemImageFolder", tab = 2)
	@PathField(rootPath = "/content/dam")
	@Inject @Named("itemImageFolder")
	private String itemImageFolderPath;

	@DialogField(fieldLabel = "Aria Label", fieldDescription = "Aria label eg: 'Click to select'", required = true,
			tab = 2, name = "./arialable")
	@TextField
	@Inject @Named("arialable")
	private String ariaLabel;

	@DialogField(fieldLabel = "Default Item ID", name = "./defaultItems", tab = 2)
	@MultiField
	@NumberField
	@Inject @Named("defaultItems")
	private String[] items;

	@DialogField(fieldLabel = "Category", fieldDescription = "Choose the categories.", name = "./categoryList", tab = 2)
	@MultiCompositeField
	@Inject @Named("categoryList")
	private List<HappyMealCategoryPOJO> happyMealCategories;

	/**
	 * Method to perform Post Initialization Tasks.
	 */
	@PostConstruct
	public final void activate() throws Exception{

		if (items != null && items.length > 0) {
			for (int i = 0; i < items.length; i++) {
				defaultItems = defaultItems + items[i] + ",";
			}

			if (defaultItems.endsWith(",")) {
				defaultItems = defaultItems.substring(0, defaultItems.length() - 1);
			}
		}
		if(null != link) {
			link = LinkUtil.getHref(link);
		}
		if (image != null) {
			imageHref = image.getImagePath();
		}

		populateMcdConfig();
		populateCategory(happyMealCategories);

        ariaLabel1 = "";
        if (null!=ariaLabel && ariaLabel.contains("##")) {
            String [] arr = ariaLabel.split("##");
            ariaLabel = arr[0];
            if (arr.length>1) {
                ariaLabel1 = arr[1];
            }
        }

		if (!categoryDetailList.isEmpty()) {
			for (int i = 0; i < categoryDetailList.size(); i++) {
				LOGGER.debug("Happy meal cat id - " + categoryDetailList.get(i).getId());
				getCategoryResponseFromServer(categoryDetailList.get(i).getId(),
						categoryDetailList.get(i).getMarketingName());

			}
		}

		setDynamicMediaServerContextURL(mcdDynamicMediaServiceConfig.getImageServerUrl(request, slingScriptHelper));
		if(StringUtils.isNotEmpty(dynamicMediaServerContextURL)){
			   setDynamicMediaLargeParameter(mcdDynamicMediaServiceConfig.getImagePresetThumbnailLarge());
			   setDynamicMediaMediumParameter(mcdDynamicMediaServiceConfig.getImagePresetThumbnailMedium());
			   setDynamicMediaSmallParameter(mcdDynamicMediaServiceConfig.getImagePresetThumbnailSmall());
			   setDynamicMediaXSmallParameter(mcdDynamicMediaServiceConfig.getImagePresetThumbnailXSmall());
			   
			}
	}

	private void getCategoryResponseFromServer(String catId, String title) {
		LOGGER.debug("happy meal getCategoryResponseFromServer catid = " + catId);
		ConnectionUtil connUtil = new ConnectionUtil();

		String url = getCategoryDetailsUrl();
		LOGGER.debug("happy meal url - " + url);
		String itemsResponse = connUtil.sendGet(url + "&categoryId=" + catId);
		Type relationTypeToken = new TypeToken<List<RelationType>>() {
		}.getType();
		Type relatedItemTypeToken = new TypeToken<List<RelatedItem>>() {
		}.getType();
		Type coopTypeToken = new TypeToken<List<Coop>>() {
		}.getType();
		Gson gson = new GsonBuilder().registerTypeAdapter(relationTypeToken, new RelationTypeAdapter())
				.registerTypeAdapter(coopTypeToken, new CoopAdapter())
				.registerTypeAdapter(relatedItemTypeToken, new RelatedItemTypeAdapter()).create();
		CategoryItemsResponse categoryItemsResponse = gson
				.fromJson(itemsResponse, CategoryItemsResponse.class);
		if (categoryItemsResponse != null) {

			processCategoryResponse(categoryItemsResponse, title);
		} else {
			LOGGER.debug("happy meal categoryItemsResponse is null");
		}

	}

	private void processCategoryResponse(CategoryItemsResponse categoryResponse, String title) {
		LOGGER.debug("happy meal processCategoryResponse");
		try {
			if (categoryResponse != null && categoryResponse.getCategory() != null
					&& categoryResponse.getCategory().getItems() != null
					&& categoryResponse.getCategory().getItems().getItem() != null) {
				LOGGER.debug("happy meal processCategoryResponse NOT NULL");
				CategoryItems categoryItems = categoryResponse.getCategory().getItems();
				CategoryItem[] items = categoryItems.getItem();
				List<HappyMealItem> happyMealItemList = populateHappyMealItemList(items);
				HappyMealBean hm = new HappyMealBean();
				hm.setTitle(title);
				hm.setHappyMealItem(happyMealItemList);
				happyMealList.add(hm);
			}
		} catch (Exception e) {
			LOGGER.error("Error in HAPPY MEAL class - ", e);
		}
	}

	private List<HappyMealItem> populateHappyMealItemList(CategoryItem[] items) {
		List<HappyMealItem> happyMealItemList = new ArrayList<HappyMealItem>();
		for (int i = 0; i < items.length; i++) {
			if (!(ApplicationConstants.DO_NOT_SHOW.equalsIgnoreCase(items[i].getDoNotShow()))) {
				HappyMealItem happyMealItem = new HappyMealItem();
				happyMealItem.setItemId(items[i].getItemId());
				happyMealItem.setTitle(items[i].getItemMarketingName());
				happyMealItem.setImageAlt(items[i].getItemMarketingName());
				happyMealItem.setCoops(
						items[i].getCoops() != null && items[i].getCoops().getCoop() != null && !items[i]
								.getCoops().getCoop().isEmpty() ?
								getCoopIds(items[i].getCoops().getCoop()) :
								StringUtils.EMPTY);
				happyMealItem.setImageUrl(getImageName(items[i]));
				happyMealItemList.add(happyMealItem);
			}
		}
		return happyMealItemList;
	}

	private String getCoopIds(List<Coop> c) {
		String coopIds = "";
		for (int k = 0; k < c.size(); k++) {
			coopIds = coopIds + c.get(k).getCoopId() + ",";
		}

		if (coopIds.endsWith(",")) {
			coopIds = coopIds.substring(0, coopIds.length() - 1);
		}
		return coopIds;
	}

	private void populateCategory(final List<HappyMealCategoryPOJO> categoryList) {
		LOGGER.debug("Inside happy meal populate Category to populate category list..");
		if (categoryList != null && !categoryList.isEmpty()) {

			for (HappyMealCategoryPOJO categoryPOJO : categoryList) {
				CategoryDetail categoryDetail = new CategoryDetail();
				String[] parts = categoryPOJO.getCategoryList().split("_");
				categoryDetail.setId(parts[0]);
				categoryDetail.setName(parts[1]);
				if (categoryPOJO.getCategoryMarketingName() != null
						&& !"".equals(categoryPOJO.getCategoryMarketingName())) {
					categoryDetail.setMarketingName(categoryPOJO.getCategoryMarketingName());
				} else {
					categoryDetail.setMarketingName(parts[1]);
				}

				categoryDetailList.add(categoryDetail);
			}
		} else {
			LOGGER.error("Happy meal CategoryList is empty..");
		}
	}

	private void populateMcdConfig() {
		String country = PageUtil.getCountry(currentPage);
		String language = PageUtil.getLanguage(currentPage);
		LOGGER.debug(
				" In happy meal  Resource Country Code is" + country + "Resource Language Code is"
						+ language);
		if (country != null && language != null) {


			if (mcdFactoryConfigConsumer != null && mcdWebServicesConfig != null) {
				mcdFactoryConfig = mcdFactoryConfigConsumer.getMcdFactoryConfig(country, language);
			}
		}
	}

	private String getImageName(CategoryItem items) {
		if (items.getAttachItemThumbnailImage() != null) {
			if (items.getAttachItemThumbnailImage().getImageName() != null) {
				return itemImageFolderPath + ApplicationConstants.PATH_SEPARATOR + items
						.getAttachItemThumbnailImage().getImageName();
			} else {
				return itemImageFolderPath + ApplicationConstants.PATH_SEPARATOR
						+ ApplicationConstants.DEFAULT_PRODUCT_IMAGE_NAME;
			}
		} else {
			return itemImageFolderPath + ApplicationConstants.PATH_SEPARATOR
					+ ApplicationConstants.DEFAULT_PRODUCT_IMAGE_NAME;
		}
	}

	private String getCategoryDetailsUrl() {
		StringBuilder url = new StringBuilder();
		if (mcdFactoryConfig != null) {
			url.append(mcdWebServicesConfig.getDomain()).append(mcdWebServicesConfig.getCategoryDetailUrl())
					.append(ApplicationConstants.URL_QS_START).append(ApplicationConstants.PN_COUNTRY)
					.append(ApplicationConstants.PN_EQUALS).append(mcdFactoryConfig.getDnaCountryCode())
					.append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
					.append(ApplicationConstants.PN_LANGUAGE).append(ApplicationConstants.PN_EQUALS)
					.append(mcdFactoryConfig.getDnaLanguageCode())
					.append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
					.append(ApplicationConstants.PN_COPS_FILTER).append(ApplicationConstants.PN_EQUALS)
					.append(false).append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
					.append(ApplicationConstants.PN_SHOW_NATIONAL_COOP).append(ApplicationConstants.PN_EQUALS)
					.append(true).append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
					.append(ApplicationConstants.PN_SHOW_LIVE_DATA).append(ApplicationConstants.PN_EQUALS)
					.append(true);
			LOGGER.debug("happy meal category Details url ---- " + url.toString());
		}
		return url.toString();
	}

	public String getDynamicMediaLargeParameter() {
		return dynamicMediaLargeParameter;
	}

	public void setDynamicMediaLargeParameter(String dynamicMediaLargeParameter) {
		this.dynamicMediaLargeParameter = dynamicMediaLargeParameter;
	}

	public String getAriaLabel() {
		return ariaLabel;
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

	public String getAriaLabel1() {
		return ariaLabel1;
	}

	public void setAriaLabel1(String ariaLabel1) {
		this.ariaLabel1 = ariaLabel1;
	}
	public Image getImage() {
		return image;
	}
}
