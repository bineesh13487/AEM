package com.mcd.rwd.global.core.sightly.page;


import com.day.cq.wcm.api.WCMMode;
import com.mcd.rwd.global.core.bean.style.Margin;
import com.mcd.rwd.global.core.bean.style.Unit;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.models.annotations.DesignAnnotation;
import com.mcd.rwd.global.core.utils.ImageUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Created by Rakesh.Balaiah on 21-03-2016.
 */

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Body { //extends McDUse {

	private Margin margin;

	private Margin mobileMargin;

	private String analyticsStart;

	private String analyticsEnd;
	
	private String background;

	private String textColor="";

	@Inject
	SlingHttpServletRequest request;

	@Inject
	@Via("request")
	Resource resource;

	@DesignAnnotation("scripts")
	Resource scriptsRes;

	@PostConstruct
	protected void activate() {
		int defaultMargin = 0;

		if (WCMMode.fromRequest(request) != WCMMode.DESIGN) {
			defaultMargin = 110;
		}
		ValueMap properties = null != resource ? resource.getValueMap() : null;
		if(null != properties) {
			int marginTop = properties.get("margin", defaultMargin);
			this.margin = new Margin.MarginBuilder(Unit.PIXELS).top(marginTop).createMargin();

			int mobileMarginTop = marginTop >= 19 ? marginTop - 19 : 0;
			this.mobileMargin = new Margin.MarginBuilder(Unit.PIXELS).top(mobileMarginTop).createMargin();

			ValueMap analyticsProps = null != scriptsRes ? scriptsRes.getValueMap() : null; //getSiteConfig("scripts");

			if (null != analyticsProps) {
				this.analyticsStart = analyticsProps.get("bodyStart", String.class);
				this.analyticsEnd = analyticsProps.get("bodyEnd", String.class);
			}

			String bgImage = ImageUtil.getImagePath(resource, ApplicationConstants.RES_IMAGE);
			String bgColor = properties.get("bgColor", String.class);
			textColor= properties.get("textColor",String.class);
			if(StringUtils.isNotBlank(bgImage)) {
				background = "background:";
				if(StringUtils.isNotBlank(bgColor)) {
					background += " #" + bgColor;
				}
				else{
					background+="#FFF";
				}
				background += " url('" + bgImage + "') " + properties.get("bgRepeat", "no-repeat") + ";";
			} else if (StringUtils.isNotBlank(bgColor)) {
				background = "background: #" + bgColor + ";";
			}
		}

	}

	public Margin getMargin() {
		return this.margin;
	}

	public Margin getMobileMargin() {
		return this.mobileMargin;
	}

	public String getAnalyticsStart() {
		return this.analyticsStart;
	}

	public String getAnalyticsEnd() {
		return this.analyticsEnd;
	}
	
	/**
	 * Getter for Background.
	 *
	 * @return background
	 */
	public String getBackground() {
		return background;
	}
	public String getTextColor(){
		return textColor;
	}


}
