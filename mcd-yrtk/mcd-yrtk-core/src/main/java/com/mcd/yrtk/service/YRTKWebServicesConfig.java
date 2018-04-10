package com.mcd.yrtk.service;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Modified;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Dictionary;

@Component(label = "YRTK Web Services Configuration", immediate = true, metatype = true,
        description = "Configurations for web services used in the YRTK components")
@Service(YRTKWebServicesConfig.class)
public final class YRTKWebServicesConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(YRTKWebServicesConfig.class);

    @Property(label = "YRTK web services base URL", value = "",
            description = "Base URL for the YRTK web services. If left empty, relative URLs will be used. "
                    + "E.g.: http://www5.development.mcdonalds.com")
    public static final String YRTK_SERVICES_BASE_URL = "yrtkServicesBaseURL";
    private String yrtkServicesBaseURL;

    @Property(label = "YRTK Services Reverse Proxy Implemented", boolValue = false,
            description = "Check this checkbox if reverse proxy is implemented for the YRTK backend services.")
    public static final String YRTK_SERVICES_REVERSE_PROXY_IMPLEMENTED = "yrtkServicesReverseProxyImplemented";
    private boolean yrtkServicesReverseProxyImplemented;

    @Property(label = "YRTK Google reCAPTCHA site key", value = "",
            description = "Site key to use wiht Google reCAPTCHA. The secret key is configured within the YRTK backend "
                    + "application.")
    public static final String YRTK_RECAPTCHA_SITE_KEY = "yrtkRecaptchaSiteKey";
    private String yrtkRecaptchaSiteKey;


    @Activate
    @Modified
    public void updateProperty(final ComponentContext componentContext) {

        final Dictionary properties = componentContext.getProperties();
        LOGGER.debug("updateProperty");
        LOGGER.debug("raw property yrtkServicesBaseURL: {}", properties.get(YRTK_SERVICES_BASE_URL));
        LOGGER.debug("raw property yrtkServicesReverseProxyImplemented: {}",
                properties.get(YRTK_SERVICES_REVERSE_PROXY_IMPLEMENTED));
        LOGGER.debug("raw property yrtkRecaptchaSiteKey: {}", properties.get(YRTK_RECAPTCHA_SITE_KEY));

        setYrtkServicesBaseURL(PropertiesUtil.toString(properties.get(YRTK_SERVICES_BASE_URL), ""));
        setYrtkServicesReverseProxyImplemented(
                PropertiesUtil.toBoolean(properties.get(YRTK_SERVICES_REVERSE_PROXY_IMPLEMENTED), false));
        setYrtkRecaptchaSiteKey(PropertiesUtil.toString(properties.get(YRTK_RECAPTCHA_SITE_KEY), ""));
    }

    public String getYrtkServicesBaseURL() {
        return yrtkServicesBaseURL;
    }

    private void setYrtkServicesBaseURL(final String yrtkServicesBaseURL) {
        this.yrtkServicesBaseURL = yrtkServicesBaseURL;
    }

    public boolean isYrtkServicesReverseProxyImplemented() {
        return yrtkServicesReverseProxyImplemented;
    }

    private void setYrtkServicesReverseProxyImplemented(final boolean yrtkServicesReverseProxyImplemented) {
        this.yrtkServicesReverseProxyImplemented = yrtkServicesReverseProxyImplemented;
    }

    public String getYrtkRecaptchaSiteKey() {
        return yrtkRecaptchaSiteKey;
    }

    private void setYrtkRecaptchaSiteKey(final String yrtkRecaptchaSiteKey) {
        this.yrtkRecaptchaSiteKey = yrtkRecaptchaSiteKey;
    }
}
