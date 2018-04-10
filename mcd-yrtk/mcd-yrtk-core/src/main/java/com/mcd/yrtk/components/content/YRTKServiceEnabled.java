package com.mcd.yrtk.components.content;

import com.day.cq.wcm.api.Page;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public interface YRTKServiceEnabled {

    Logger LOGGER = LoggerFactory.getLogger(YRTKServiceEnabled.class);

    /** holds mapping between the actual ISO country code and country code used by the YRTK backend */
    Map<String, String> COUNTRY_MAPPING = ImmutableMap.of(
            "ae", "ua",
            "kw", "qu",
//            "qa", "qa",
//            "om", "om",
            "bh", "ba"
    );

    String SAUDI_ARABIA_COUNTRY_CODE = "sa";
    String SA_RIYADH = "riyadh";
    String SA_RIYADH_COUNTRY_CODE = "ri";
    String SA_JEDDAH = "jeddah";
    String SA_JEDDAH_COUNTRY_CODE = "je";

    String LOCALE_SEPARATOR = "-";

    int COUNTRY_PAGE_DEPTH = 1;
    int LANGUAGE_PAGE_DEPTH = 2;

    default String getYRTKCountryCode(final Page currentPage) {

        String country = currentPage.getAbsoluteParent(COUNTRY_PAGE_DEPTH).getName();
        if (StringUtils.equalsIgnoreCase(country, "prelaunch")
                && currentPage.getAbsoluteParent(COUNTRY_PAGE_DEPTH + 1) != null) {

            country = currentPage.getAbsoluteParent(COUNTRY_PAGE_DEPTH + 1).getName();
        }

        if (StringUtils.equalsIgnoreCase(country, SAUDI_ARABIA_COUNTRY_CODE)) {
            final String pagePath = currentPage.getPath();
            if (StringUtils.contains(pagePath, SA_RIYADH)) {
                country = SA_RIYADH_COUNTRY_CODE;
            } else if (StringUtils.contains(pagePath, SA_JEDDAH)) {
                country = SA_JEDDAH_COUNTRY_CODE;
            }
        } else if (COUNTRY_MAPPING.containsKey(country)) {
            country = COUNTRY_MAPPING.get(country);
        }
        LOGGER.debug("countryCode for YRTK services: {}", country);
        return country;
    }

    default String getYRTKLanguageCode(final Page currentPage, final String country) {

        String language;
        int depth = LANGUAGE_PAGE_DEPTH;
        if (currentPage.getAbsoluteParent(depth) != null) {
            language = currentPage.getAbsoluteParent(depth).getName();
        } else {
            depth--;
            language = currentPage.getAbsoluteParent(depth).getName();
        }

        if (StringUtils.equalsIgnoreCase(language, country) && currentPage.getAbsoluteParent(++depth) != null) {
            language = currentPage.getAbsoluteParent(depth).getName();
        }

        if (StringUtils.contains(language, LOCALE_SEPARATOR)) {
            language = StringUtils.substringBefore(language, LOCALE_SEPARATOR);
        }

        LOGGER.debug("languageCode for YRTK services: {}", language);
        return language;
    }
}

