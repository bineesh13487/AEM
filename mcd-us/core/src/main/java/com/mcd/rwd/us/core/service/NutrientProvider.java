package com.mcd.rwd.us.core.service;

import com.icfolson.aem.library.core.servlets.optionsprovider.Option;
import org.apache.sling.api.resource.Resource;

import java.util.List;
import java.util.Map;

public interface NutrientProvider {

    String NUTRITION_CALCULATOR_COMPONENT = "nutrition-calculator";

    Map<String, String> getNutrientIdMapping(final Resource resource, final String component);

    List<Option> getNutrientOptions(final Resource resource, final String component);

}
