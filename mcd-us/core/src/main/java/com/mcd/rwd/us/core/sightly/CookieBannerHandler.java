package com.mcd.rwd.us.core.sightly;

import com.day.cq.wcm.api.designer.Design;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.models.annotations.DesignAnnotation;
import com.mcd.rwd.us.core.service.McdWebServicesConfig;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Created by Seema Pandey on 17-01-2017.
 */

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CookieBannerHandler {

    private static final Logger LOG = LoggerFactory.getLogger(CookieBannerHandler.class);

    private boolean enableCookieMsg;

    private String cookieMsg;

    private String cookieFontColor;

    private String cookieBgColor;

    private String cookieButtonText;

    private String buttonTextFontColor;

    private String buttonColor;
    
    private int cookieDisclaimerExpiryInDays;

    @Inject
    Design currentDesign;

    @DesignAnnotation(value = "cookiedisclaimer")
    Resource cookieDisclaimerResource;

    @OSGiService
    McdWebServicesConfig mcdWebServicesConfig;

    @PostConstruct
    public void activate() {
        LOG.debug("CookieBannerHandler Activate Method Called..");
        if(null != currentDesign) {
            Resource contentResource = currentDesign.getContentResource();
            Resource resource = null != contentResource.getChild("cookiedisclaimer") ?
                    contentResource.getChild("cookiedisclaimer") : null;
            if(null != resource) {
                ValueMap properties = resource.getValueMap();
                if (properties != null) {
                    this.enableCookieMsg = properties.get(ApplicationConstants.COOKIE_ENABLED, false);
                    this.cookieMsg = properties.get(ApplicationConstants.COOKIE_MSG, ApplicationConstants.BLANK);
                    this.cookieFontColor = properties.get(ApplicationConstants.COOKIE_FONTCOLOR, "000000");
                    this.cookieBgColor = properties.get(ApplicationConstants.COOKIE_BGCOLOR, "fcc64d");
                    this.cookieButtonText = properties.get(ApplicationConstants.COOKIE_BTNTEXT, "Accept and Close");
                    this.buttonTextFontColor = properties.get(ApplicationConstants.COOKIE_BTNFONTCOLOR, "FFFFFF");
                    this.buttonColor = properties.get(ApplicationConstants.COOKIE_BTNCOLOR, "000000");
                    if (mcdWebServicesConfig != null) {
                        this.cookieDisclaimerExpiryInDays = Integer.parseInt(mcdWebServicesConfig.getCookieDisclaimerExpiryInDays());
                    }
                }
                else {
                    LOG.error("No configuration done for Cookie Disclaimer Banner.");
                }
            }
        }
    }


	public boolean isEnableCookieMsg() {
		return enableCookieMsg;
	}

	public String getCookieMsg() {
		return cookieMsg;
	}

	public String getCookieFontColor() {
		return cookieFontColor;
	}

	public String getCookieBgColor() {
		return cookieBgColor;
	}

	public String getCookieButtonText() {
		return cookieButtonText;
	}

	public String getButtonTextFontColor() {
		return buttonTextFontColor;
	}

	public String getButtonColor() {
		return buttonColor;
	}
	
	public int getCookieDisclaimerExpiryInDays() {
		return cookieDisclaimerExpiryInDays;
	}
   
}

