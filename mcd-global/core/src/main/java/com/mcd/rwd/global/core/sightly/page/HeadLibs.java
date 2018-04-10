package com.mcd.rwd.global.core.sightly.page;

import com.mcd.rwd.global.core.models.annotations.DesignAnnotation;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;

/**
 * Created by Rakesh.Balaiah on 30-05-2016.
 */

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeadLibs { //extends McDUse {

	private String analytics;

	@DesignAnnotation("scripts")
	Resource scriptsRes;

	@PostConstruct
	protected void activate() {
		ValueMap properties = null != scriptsRes ? scriptsRes.getValueMap() : null; //getSiteConfig("scripts");
		if (null != properties) {
			this.analytics = properties.get("head", String.class);
		}
	}

	public String getAnalytics() {
		return this.analytics;
	}
}
