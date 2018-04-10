package com.mcd.rwd.us.core.sightly;

import com.day.cq.wcm.api.Page;
import com.icfolson.aem.library.api.page.PageDecorator;
import com.mcd.rwd.global.core.sightly.McDUse;
import com.mcd.rwd.global.core.utils.ImageUtil;
import com.mcd.rwd.global.core.utils.MultiFieldPanelUtil;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.us.core.bean.Nutrient;
import com.mcd.rwd.us.core.bean.category.CategoryDetail;
import com.mcd.rwd.us.core.constants.ApplicationConstants;
import com.mcd.rwd.us.core.service.McdFactoryConfig;
import com.mcd.rwd.us.core.service.McdFactoryConfigConsumer;
import com.mcd.rwd.us.core.service.McdWebServicesConfig;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Dipankar Gupta on 11/28/2016.
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

public class ProductNutrition {

	private static final Logger LOGGER = LoggerFactory.getLogger(NutritionCalculator.class);

	private static final String NUTRINT_MARKETING_NAME = "nutrientMarketingName";

	private static final String SECONDARY_NUTRINT_MARKETING_NAME = "secondaryNutrientMarketingName";

	private String tab1;

	private String tab2;

	private String dnaItemDetailsUrl;

	private List<Nutrient> nutrientsList = new ArrayList<Nutrient>();

	private List<Nutrient> secondaryNutrientsList = new ArrayList<Nutrient>();

	private StringBuilder allNutritionId = new StringBuilder();

	private StringBuilder secondaryNutritionId = new StringBuilder();

	private String allNutrients = "";

	private String secondaryNutrients = "";


	@Inject
	SlingHttpServletRequest request;

	@Inject
	@Via("resource")
	private Page currentPage;

	@OSGiService
	McdFactoryConfigConsumer mcdFactoryConfigConsumer;


	@OSGiService
	McdFactoryConfig mcdFactoryConfig;

	@OSGiService
	McdWebServicesConfig mcdWebServicesConfig;


	/**
	 * Method to perform Post Initialization Tasks.
	 */
	@PostConstruct
	public final void activate() {


		populateMcdConfig();
		populateItemDetailsUrl();
		populateNutrients(MultiFieldPanelUtil.getMultiFieldPanelValues(request.getResource(), "nutrientList"));
		populateSecondaryNutrients(
				MultiFieldPanelUtil.getMultiFieldPanelValues(request.getResource(), "secondaryNutrientList"));

		if (allNutritionId != null) {
			allNutrients = allNutritionId.toString().trim();

			if (allNutrients.endsWith("|")) {
				allNutrients = allNutrients.substring(0, allNutrients.length() - 1);
			}
		}

		if (secondaryNutritionId != null) {
			secondaryNutrients = secondaryNutritionId.toString().trim();

			if (secondaryNutrients.endsWith("|")) {
				secondaryNutrients = secondaryNutrients.substring(0, secondaryNutrients.length() - 1);
			}
		}

		LOGGER.debug("Primary Nutrients - " + allNutrients);
		LOGGER.debug("Secondary Nutrients - " + secondaryNutrients);

	}



private void populateNutrients(final List<Map<String, String>> nutrientList) {
	LOGGER.debug("Inside populate Nutrients to populate nutrient list..");
	if (nutrientList != null && !nutrientList.isEmpty()) {
		Iterator<Map<String, String>> itr = nutrientList.iterator();
		while (itr.hasNext()) {

			Nutrient nutrient = new Nutrient();
			Map<String, String> item = itr.next();
			String[] parts = item.get("nutrientList").split("_");
			allNutritionId = allNutritionId.append(parts[0] + "|");
			nutrient.setId(parts[0]);
			nutrient.setName(parts[1]);
			if (item.get(NUTRINT_MARKETING_NAME) != null && !""
					.equals(item.get(NUTRINT_MARKETING_NAME))) {
				nutrient.setMarketingName(item.get(NUTRINT_MARKETING_NAME));
			} else {
				nutrient.setMarketingName(parts[1]);
			}

			nutrientsList.add(nutrient);
		}
	} else {
		LOGGER.error("NutrientList is empty..");
	}
}

private void populateSecondaryNutrients(final List<Map<String, String>> nutrientList) {
	LOGGER.debug("Inside populate Secondary Nutrients to populate nutrient list..");
	if (nutrientList != null && !nutrientList.isEmpty()) {
		Iterator<Map<String, String>> itr = nutrientList.iterator();
		while (itr.hasNext()) {

			Nutrient nutrient = new Nutrient();
			Map<String, String> item = itr.next();
			String[] parts = item.get("secondaryNutrientList").split("_");
			secondaryNutritionId = secondaryNutritionId.append(parts[0] + "|");
			nutrient.setId(parts[0]);
			nutrient.setName(parts[1]);
			if (item.get(SECONDARY_NUTRINT_MARKETING_NAME) != null && !""
					.equals(item.get(SECONDARY_NUTRINT_MARKETING_NAME))) {
				nutrient.setMarketingName(item.get(SECONDARY_NUTRINT_MARKETING_NAME));
			} else {
				nutrient.setMarketingName(parts[1]);
			}

			secondaryNutrientsList.add(nutrient);
		}
	} else {
		LOGGER.error("Secondary NutrientList is empty..");
	}
}

private void populateMcdConfig() {
	String country = PageUtil.getCountry(currentPage);
	String language = PageUtil.getLanguage(currentPage);

	LOGGER.debug(
			" In Nutrition Calculator  Resource Country Code is" + country + "Resource Language Code is"
					+ language);
	if (country != null && language != null) {
		if (mcdFactoryConfigConsumer != null && mcdWebServicesConfig != null) {
			mcdFactoryConfig = mcdFactoryConfigConsumer.getMcdFactoryConfig(country, language);
		}
	}
}


private void populateItemDetailsUrl() {
	StringBuilder url = new StringBuilder();
	if (mcdFactoryConfig != null) {
		url.append(mcdWebServicesConfig.getDomain()).append(mcdWebServicesConfig.getItemDetailUrl())
				.append(ApplicationConstants.URL_QS_START).append(ApplicationConstants.PN_COUNTRY)
				.append(ApplicationConstants.PN_EQUALS).append(mcdFactoryConfig.getDnaCountryCode())
				.append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
				.append(ApplicationConstants.PN_LANGUAGE).append(ApplicationConstants.PN_EQUALS)
				.append(mcdFactoryConfig.getDnaLanguageCode())
				.append(ApplicationConstants.URL_QS_DELIMITER_CHAR)
				.append(ApplicationConstants.PN_SHOW_LIVE_DATA).append(ApplicationConstants.PN_EQUALS)
				.append(true);
		dnaItemDetailsUrl = url.toString();
		LOGGER.debug("Item details url ---- " + url.toString());

	}
}


	
}
