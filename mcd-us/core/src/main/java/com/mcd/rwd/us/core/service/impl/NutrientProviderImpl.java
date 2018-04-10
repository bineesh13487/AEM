package com.mcd.rwd.us.core.service.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.icfolson.aem.library.core.servlets.optionsprovider.Option;
import com.mcd.rwd.global.core.utils.ConnectionUtil;
import com.mcd.rwd.global.core.utils.ResourceUtil;
import com.mcd.rwd.us.core.bean.product.Nutrient;
import com.mcd.rwd.us.core.bean.product.NutrientFacts;
import com.mcd.rwd.us.core.bean.product.NutrientsResponse;
import com.mcd.rwd.us.core.constants.ApplicationConstants;
import com.mcd.rwd.us.core.datasource.NutrientsListDataSource;
import com.mcd.rwd.us.core.service.McdFactoryConfig;
import com.mcd.rwd.us.core.service.McdFactoryConfigConsumer;
import com.mcd.rwd.us.core.service.McdWebServicesConfig;
import com.mcd.rwd.us.core.service.NutrientProvider;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Component(immediate = true)
public class NutrientProviderImpl implements NutrientProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(NutrientsListDataSource.class);

    @Reference
    private transient McdFactoryConfigConsumer mcdFactoryConfigConsumer;

    @Reference
    private transient McdWebServicesConfig mcdWebServicesConfig;

    @Override
    public Map<String, String> getNutrientIdMapping(final Resource resource, final String component) {
        List<Option> options = getOptions(resource, component);
        Map<String, String> out = new HashMap<>();
        options.forEach(option -> out.put(option.getValue(), option.getText()));
        return out;
    }

    @Override
    public List<Option> getNutrientOptions(Resource resource, String component) {
        return getOptions(resource, component);
    }


    private List<Option> getOptions(final Resource resource, final String component) {
        String resourceCountryCode = ResourceUtil.getCountryCodeFromResource(resource);
        String resourceLanguageCode = ResourceUtil.getLanguageCodeFromResource(resource);
        LOGGER.info(" In NutrientsList  Resource Country Code is" + resourceCountryCode + "Resource Language Code is" + resourceLanguageCode);
        if (resourceCountryCode != null && resourceLanguageCode != null) {
            McdFactoryConfig mcdFactoryConfig = mcdFactoryConfigConsumer
                    .getMcdFactoryConfig(resourceCountryCode, resourceLanguageCode);
            if (mcdFactoryConfig != null) {
                String nutrientFactsUrl = getNutrientFactsUrl(mcdFactoryConfig);
                String nutrientsDetails;
                LOGGER.debug("In NutrientsList url to Call DNA WebService to fetch Nutrients Informationis  is" + nutrientFactsUrl);
                ConnectionUtil connUtil = new ConnectionUtil();
                nutrientsDetails = connUtil.sendGet(nutrientFactsUrl);

                if (nutrientsDetails != null) {
                    LOGGER.debug("In NutrientsList response obtained from DNA WEbService for Nutrients");
                    return getNutrientData(nutrientFactsUrl, nutrientsDetails, component);

                } else {
                    LOGGER.error("response from the web service: {} is null", nutrientFactsUrl);
                }
            } else {
                LOGGER.error("No Factory Configuration found for the country - {} and language - {}",
                        resourceCountryCode, resourceLanguageCode);
            }
        } else {
            LOGGER.error("No country code or language code found for the resource");
        }
        return Lists.newArrayList();
    }

    /**
     * @param nutrientFactsUrl
     * @param nutrientsDetails
     */
    private List<Option> getNutrientData(String nutrientFactsUrl, String nutrientsDetails, String component) {
        NutrientsResponse nutrientsResponse = new Gson().fromJson(nutrientsDetails, NutrientsResponse.class);
        if (nutrientsResponse != null) {
            NutrientFacts nutrientFacts = nutrientsResponse.getNutrientFacts();
            if (nutrientFacts != null) {
                List<Nutrient> nutrients = nutrientFacts.getNutrient();

                if (nutrients != null && !nutrients.isEmpty())
                    return processNutrientResponse(component, nutrients);
                else {
                    LOGGER.error("Nutrients array associated with the key 'nutrient' is either null" +
                            " " +
                            "or empty for the url: {}", nutrientFactsUrl);
                }
            } else {
                LOGGER.error("No nutrient facts found for the key 'nutrient_facts' in the url: {}",
                        nutrientFactsUrl);
            }
        } else {
            LOGGER.error("Unable to parse json for the nutrient facts. Json might be null or "
                    + "incorrect for the url: {}", nutrientFactsUrl);
        }
        return Lists.newArrayList();
    }

    private List<Option> processNutrientResponse(String component, List<Nutrient> nutrients) {
        final List<Option> out = new ArrayList<>();
        for (Nutrient nutrient : nutrients) {
            final String text = nutrient.getName();
            final String value;
            if (NUTRITION_CALCULATOR_COMPONENT.equals(component)) {
                value = nutrient.getId()+"_"+nutrient.getName();
            }
            else {
                value = Integer.toString(nutrient.getId());
            }
            out.add(new Option(value, text));
        }
        return out;
    }

    /**
     * @param mcdFactoryConfig
     * @return
     */
    private String getNutrientFactsUrl(McdFactoryConfig mcdFactoryConfig) {
        StringBuilder url = new StringBuilder();
        url.append(mcdWebServicesConfig.getDomain()).append(mcdWebServicesConfig.getNutrientFactsUrl()).append(ApplicationConstants.URL_QS_START).append(ApplicationConstants.PN_COUNTRY).append(ApplicationConstants.PN_EQUALS).append(mcdFactoryConfig.getDnaCountryCode()).append(ApplicationConstants.URL_QS_DELIMITER_CHAR).append(ApplicationConstants.PN_LANGUAGE)
                .append(ApplicationConstants.PN_EQUALS).append(mcdFactoryConfig.getDnaLanguageCode()).append(ApplicationConstants.URL_QS_DELIMITER_CHAR).append(ApplicationConstants.PN_SHOW_LIVE_DATA).append(ApplicationConstants.PN_EQUALS).append(true);
        return url.toString();
    }
}
