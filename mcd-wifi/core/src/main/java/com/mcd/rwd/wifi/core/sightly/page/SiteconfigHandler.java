package com.mcd.rwd.wifi.core.sightly.page;

import com.day.cq.wcm.api.WCMMode;
import com.mcd.rwd.global.core.models.annotations.DesignAnnotation;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SiteconfigHandler {

	private String bgColor;
	
	private String logoText;

	@DesignAnnotation("backgroundColor")
	Resource backgroundColorRes;

	@DesignAnnotation("logoText")
	Resource logoTextRes;

	@Inject
	SlingHttpServletRequest request;

	@Inject
	SlingHttpServletResponse response;

	@PostConstruct
	public void activate() {
		ValueMap bgcolorProperties = null != backgroundColorRes ? backgroundColorRes.getValueMap() : null;
		if (null != bgcolorProperties) {
			String hexCode = bgcolorProperties.get("hexcode", String.class);
			if(hexCode != null && !hexCode.isEmpty()){
				this.bgColor = hexCode;
			}
			else{
				this.bgColor = "#efefef";
			}
		}
		
		ValueMap logoTextProps = null != logoTextRes ? logoTextRes.getValueMap() : null;
		if (null != logoTextProps){
			String logoTxt = logoTextProps.get("logotext", String.class);
			this.logoText = logoTxt;
		}

		final WCMMode wcmMode = WCMMode.fromRequest(request);

		if(wcmMode == WCMMode.DISABLED && !(request.getServletPath().equals("/")))
		{
		 	response.addHeader("X-FRAME-OPTIONS", "DENY" );

		}


	}

	/**
	 * @return the logoText
	 */
	public String getLogoText() {
		return logoText;
	}

	public String getBgColor() {
		return bgColor;
	}
}
