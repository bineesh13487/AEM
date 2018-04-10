package com.mcd.rwd.global.core.sightly.page;

import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.models.Bumper;
import com.mcd.rwd.global.core.models.annotations.DesignAnnotation;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;

/**
 * Created by Rakesh.Balaiah on 10-05-2016.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BumperHandler { //extends McDUse {

	private Bumper bumper;

	@DesignAnnotation(ApplicationConstants.RES_BUMPER)
	Resource bumperRes;

	@PostConstruct
	protected void activate() {
		//Resource bumperRes = getSiteConfigResource(ApplicationConstants.RES_BUMPER);

		if (null != bumperRes) {
			this.bumper = bumperRes.adaptTo(Bumper.class);
		}
	}

	public Bumper getBumper() {
		return this.bumper;
	}
}
