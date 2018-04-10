package com.mcd.rwd.us.core.sightly;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.Listener;
import com.day.cq.wcm.api.designer.Style;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Created by deepti_b on 5/23/2016.
 */

@Component(
        name = "legalfooter",
        value = "Legal Disclaimer",
        path = "content",
        description = "Legal footer for all Nutrition Components",
        group = " GWS-Global",
        disableTargeting = true,
        listeners = {
                @Listener(name = "afteredit", value = "REFRESH_PAGE"),
                @Listener(name = "afterinsert", value = "REFRESH_PAGE"),
                @Listener(name = "aftersubmit", value = "REFRESH_PAGE")
        }
)
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LegalFooterHandler { //extends McDUse {

    private static final Logger LOGGER = LoggerFactory.getLogger(LegalFooterHandler.class);

    private String legalText;
    
    private String legalTextHeading;

    @Inject
    Style currentStyle;
    
	/**
     * Method to perform Post Initialization Tasks.
     */
    
    @PostConstruct
    public final void activate(){
        LOGGER.debug("In LegalFooterHandler class ");
        if(null != currentStyle) {
            Resource legalResource = currentStyle.getDefiningResource("");
            //Resource legalResource = null != resource.getChild("legalfooter") ? resource.getChild("legalfooter") : null;
            if(null != legalResource) {
                ValueMap properties = legalResource.getValueMap();
                if (null != properties) {
                    legalText = properties.get("text", String.class);
                    legalTextHeading = properties.get("heading", String.class);
                }
            }
        }
    }

    public String getLegalText() {
        return legalText;
    }
    
    public String getLegalTextHeading() {
		return legalTextHeading;
	}
}
