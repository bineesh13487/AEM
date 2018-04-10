package com.mcd.rwd.us.core.sightly;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.wcm.api.Page;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mcd.rwd.global.core.utils.ConnectionUtil;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.global.core.utils.TextUtil;
import com.mcd.rwd.us.core.bean.ExtraValueMealItem;
import com.mcd.rwd.us.core.bean.evm.*;
import com.mcd.rwd.us.core.constants.ApplicationConstants;
import com.mcd.rwd.us.core.service.McdFactoryConfig;
import com.mcd.rwd.us.core.service.McdFactoryConfigConsumer;
import com.mcd.rwd.us.core.service.McdWebServicesConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
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
import java.util.List;

/**
 * Created by Dipankar Gupta on 3/24/2016.
 */

@Component(name = "extraValueMeal", value = "Extra Value Meal",
		tabs = { @Tab( touchUINodeName = "general", title = "EVM" )},
		actions = { "text: Extra Value Meal", "-", "editannotate", "copymove", "delete" },
		disableTargeting = true,
		group = " GWS-Global", path = "content",
		listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
		@Listener(name = "afterinsert", value = "REFRESH_PAGE")})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ExtraValueMeal {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExtraValueMeal.class);

	@DialogField(name = "./link", fieldLabel = "Nutrition Calculator Url", required = true)
	@PathField(rootPath = "/", rootTitle = "Website")
	@Inject @Named("link")
	@Default(values = "")
	private String path;

	@DialogField(name = "./imageFolder", fieldLabel = "Dam folder path", required = true, fieldDescription = "Name of Image should be in this format - h-mcdonalds-Cheeseburger.png")
	@PathField(rootPath = DamConstants.MOUNTPOINT_ASSETS, rootTitle = "DAM")
	@Inject @Named("imageFolder")
	@Default(values = "")
	private String imagepath;

	@DialogField(name = "./title", fieldLabel = "Url Title")
	@TextField
	@Inject @Named("title")
	@Default(values = "")
	private String text;

	@DialogField(name = "./catInfo", fieldLabel = "Extra value meal category", required = true, ranking = 2D,
	additionalProperties = @Property(name = "comp", value = "extraValueMeal"))
	@Selection(optionsProvider = "$PATH.categorylist.json?comp=extraValueMeal", type = Selection.SELECT,
		dataSource = "mcd-us/dataSource/extraValueMeal")
	@Inject @Named("catInfo")
	private String category;

	@Inject
	Page getCurrentPage;

	@OSGiService
	McdFactoryConfigConsumer mcdFactoryConfigConsumer;

	@OSGiService
	McdWebServicesConfig mcdWebServicesConfig;

	private String link;
	private String imageFolder;
	private String title;
	private String catInfo;

	private McdFactoryConfig mcdFactoryConfig;

	private List<ExtraValueMealItem> evmList = new ArrayList<ExtraValueMealItem>();

	/**
	 * Method to perform Post Initialization Tasks.
	 */
	@PostConstruct
	 public void extraMeal() {
		this.link = this.path;
		this.imageFolder = this.imagepath;
		this.title = this.text;
		this.catInfo = this.category;
		if (catInfo != null && !"".equals(catInfo)) {
			String catId = catInfo.split("_")[0];
			getCategoryResponseFromServer(catId);
		}
	}

	private void getCategoryResponseFromServer(String catId) {
		LOGGER.debug("evm getCategoryResponseFromServer catid = " + catId);
		ConnectionUtil connUtil = new ConnectionUtil();
		populateMcdConfig();
		String url = getCategoryBundleUrl();
		String itemsResponse = connUtil.sendGet(url + "&categoryId=" + catId);
		Type mealItemTypeToken = new TypeToken<List<MealItem>>() {
		}.getType();
		Type itemTypeToken = new TypeToken<List<Item>>() {
		}.getType();
		Type coopTypeToken = new TypeToken<List<Coop>>() {
		}.getType();
		Gson gson = new GsonBuilder().registerTypeAdapter(mealItemTypeToken, new MealItemAdapter())
				.registerTypeAdapter(itemTypeToken, new ItemAdapter())
				.registerTypeAdapter(coopTypeToken, new CoopAdapter()).create();
		CategoryItemsResponse categoryResponse = gson.fromJson(itemsResponse, CategoryItemsResponse.class);
		processCategoryResponse(categoryResponse);

	}

	private void processCategoryResponse(CategoryItemsResponse categoryResponse) {
		LOGGER.debug("evm processCategoryResponse");
		try {
			if (categoryResponse != null && categoryResponse.getCategory() != null
					&& categoryResponse.getCategory().getMealItems() != null
					&& categoryResponse.getCategory().getMealItems().getMealItem() != null) {
				LOGGER.debug("evm processCategoryResponse NOT NULL");
				populateEvmList(categoryResponse);
			}
		} catch (Exception e) {
			LOGGER.error("Error in EVM class - ", e);
		}
	}

	private void populateEvmList(CategoryItemsResponse categoryResponse) {
		List<MealItem> mealItem = categoryResponse.getCategory().getMealItems().getMealItem();

		for (int i = 0; i < mealItem.size(); i++) {

			if (!(ApplicationConstants.DO_NOT_SHOW.equalsIgnoreCase(mealItem.get(i).getDoNotShow()))) {
				ExtraValueMealItem evm = new ExtraValueMealItem();
				evm.setDescription((String) TextUtil.sanitizeDnaValues(mealItem.get(i).getDescription()));
				evm.setTitle((String) TextUtil.sanitizeDnaValues(mealItem.get(i).getItemMarketingName()));
				evm.setCoopIds(findCoopIds(mealItem.get(i).getCoops()));
				evm.setImageAlt((String) TextUtil.sanitizeDnaValues(mealItem.get(i).getItemMarketingName()));
				evm.setImageUrl(getImageName(mealItem.get(i)));
				evm.setItemIds(findItemIds(mealItem.get(i).getItems()));
				evmList.add(evm);
			}
		}
	}

	private String findCoopIds(Coops coops) {
		return coops != null && coops.getCoop() != null && !coops.getCoop().isEmpty() ?
				getCoopIds(coops.getCoop()) :
				StringUtils.EMPTY;
	}

	private String findItemIds(Items items) {
		return items != null && items.getItem() != null ? getIds(items.getItem()) : StringUtils.EMPTY;
	}

	private String getImageName(MealItem mealItem) {
		if (mealItem.getImage() != null
				&& TextUtil.sanitizeDnaValues(mealItem.getImage().getImageName()) != null) {
			return imageFolder + ApplicationConstants.PATH_SEPARATOR + TextUtil
					.sanitizeDnaValues(mealItem.getImage().getImageName());
		} else {
			return imageFolder + ApplicationConstants.PATH_SEPARATOR
					+ ApplicationConstants.DEFAULT_PRODUCT_IMAGE_NAME;
		}
	}

	private String getCoopIds(List<Coop> c) {
		StringBuilder coopIds = new StringBuilder();
		for (int k = 0; k < c.size(); k++) {
			coopIds.append(c.get(k).getCoopId()).append((char) 44);
		}

		int pos = coopIds.length() - 1;

		if (coopIds.charAt(pos) == 44) {
			coopIds.deleteCharAt(pos);
		}

		return coopIds.toString();
	}

	private String getIds(List<Item> items) {
		StringBuilder str = new StringBuilder();

		for (int k = 0; k < items.size(); k++) {
			if (!(ApplicationConstants.DO_NOT_SHOW.equalsIgnoreCase(items.get(k).getDoNotShow()))) {
				str.append(items.get(k).getItemId()).append((char) 44);
			}
		}

		int pos = str.length() - 1;

		if (str.charAt(pos) == 44) {
			str.deleteCharAt(pos);
		}

		return str.toString();
	}

	private void populateMcdConfig() {
		Page currentPage = getCurrentPage;
		String country = PageUtil.getCountry(currentPage);
		String language = PageUtil.getLanguage(currentPage);
		LOGGER.debug(
				" In EVM  Resource Country Code is" + country + "Resource Language Code is"
						+ language);
		if (country != null && language != null) {

			if (mcdFactoryConfigConsumer != null && mcdWebServicesConfig != null) {
				mcdFactoryConfig = mcdFactoryConfigConsumer.getMcdFactoryConfig(country, language);
			}
		}
	}

	private String getCategoryBundleUrl() {
		StringBuilder url = new StringBuilder();
		if (mcdFactoryConfig != null) {
			url.append(mcdWebServicesConfig.getDomain()).append(mcdWebServicesConfig.getCategoryBundleUrl())
					.append(ApplicationConstants.URL_QS_START).append(ApplicationConstants.PN_COUNTRY)
					.append(ApplicationConstants.PN_EQUALS).append(mcdFactoryConfig.getDnaCountryCode())
					.append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
					.append(ApplicationConstants.PN_LANGUAGE).append(ApplicationConstants.PN_EQUALS)
					.append(mcdFactoryConfig.getDnaLanguageCode())
					.append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
					.append(ApplicationConstants.PN_SHOW_LIVE_DATA).append(ApplicationConstants.PN_EQUALS)
					.append(true);
			LOGGER.debug("Category Bundle url - " + url.toString());
		}
		return url.toString();
	}

	public String getLink() {
		return link;
	}

	public String getTitle() {
		return title;
	}

	public List<ExtraValueMealItem> getEvmList() {
		return evmList;
	}
}
