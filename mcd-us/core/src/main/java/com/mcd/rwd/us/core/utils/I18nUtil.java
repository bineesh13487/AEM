package com.mcd.rwd.us.core.utils;

import com.day.cq.i18n.I18n;
import com.day.cq.wcm.api.Page;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.us.core.service.McdFactoryConfig;
import com.mcd.rwd.us.core.service.McdFactoryConfigConsumer;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.MissingResourceException;

public final class I18nUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(I18nUtil.class);

    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    private I18nUtil() { }


    public static Locale getLocaleFromConfigLanguageForPage (final Page page, final McdFactoryConfigConsumer mcdFactoryConfigConsumer) {

        Locale locale = DEFAULT_LOCALE;

        String pageCountryCode = PageUtil.getCountry(page);
        String pageLanguageCode = PageUtil.getLanguage(page);
        if (pageCountryCode != null && pageLanguageCode != null && mcdFactoryConfigConsumer != null) {
            final McdFactoryConfig mcdFactoryConfig = mcdFactoryConfigConsumer.getMcdFactoryConfig(pageCountryCode, pageLanguageCode);
            //final String languageCode = mcdFactoryConfig.getLanguage();
            //we need the language code, not a specific locale e.g. es instead of es-us or en instead of en-us en-gb
            final String languageCode = mcdFactoryConfig.getDnaLanguageCode();
            if (StringUtils.isNotBlank(languageCode)) {
                locale = new Locale(languageCode);
            }
        }

        return locale;
    }

    public static String getTranslation(final I18n i18n, final String key, final String defaultValue) {
        String translation = null;

        try {
            translation = i18n.get(key);
        } catch (final MissingResourceException e) {
            LOGGER.error("Could not find translation for key '" + key + "'.",e);
        }

        if (StringUtils.isNotBlank(defaultValue) && (StringUtils.equals(key, translation) || StringUtils.isBlank(translation))) {
            translation = defaultValue;
        }

        LOGGER.debug("tryGet() key = {}, translation = {}", key, translation);
        return translation;
    }
}
