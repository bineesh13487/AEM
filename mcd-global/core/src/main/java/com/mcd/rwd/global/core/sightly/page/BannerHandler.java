package com.mcd.rwd.global.core.sightly.page;

import com.mcd.rwd.global.core.models.SmartBanner;
import com.mcd.rwd.global.core.models.annotations.DesignAnnotation;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;

/**
 * Created by Rakesh.Balaiah on 04-03-2016.
 */

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BannerHandler {

	private SmartBanner banner;

	@DesignAnnotation("smartbanner")
	Resource smartBannerResource;

	@PostConstruct
	protected void activate() {
		//Resource smartBannerResource = getSiteConfigResource("smartbanner");

		if (null != smartBannerResource) {
			this.banner = smartBannerResource.adaptTo(SmartBanner.class);
		}
	}

	public SmartBanner getBanner() {
		return this.banner;
	}
}
