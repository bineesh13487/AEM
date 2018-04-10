package com.mcd.rwd.global.core.sightly;

import com.mcd.rwd.global.core.bean.AppBadge;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.models.annotations.DesignAnnotation;
import com.mcd.rwd.global.core.utils.BumperUtil;
import com.mcd.rwd.global.core.utils.ImageUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * Created by Rakesh.Balaiah on 25-04-2016.
 */

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AppBadgeHandler {

	private static final Logger LOG = LoggerFactory.getLogger(AppBadgeHandler.class);

	private AppBadge android;

	private AppBadge ios;

	@DesignAnnotation("appbadge")
	Resource appBadge;

	@DesignAnnotation(ApplicationConstants.RES_BUMPER)
	Resource bumerRes;

	@PostConstruct
	protected void activate() {
		//Resource appBadge = getSiteConfigResource("appbadge");

		if (null != appBadge) {
			LOG.debug("App Badge Resource {}", appBadge);
			Resource androidRes = appBadge.getChild("android");
			BumperUtil bumperUtil = new BumperUtil(bumerRes);

			if (null != androidRes) {
				LOG.debug("Android App Resource {}", androidRes);
				android = populateAppProperties(androidRes, bumperUtil);
			}

			Resource iosRes = appBadge.getChild("ios");
			if (null != iosRes) {
				LOG.debug("iOS App Resource {}", iosRes);
				ios = populateAppProperties(iosRes, bumperUtil);
			}
		}
	}

	private AppBadge populateAppProperties(Resource resource, BumperUtil bumperUtil) {
		AppBadge badge = new AppBadge();
		ValueMap props = resource.adaptTo(ValueMap.class);
		badge.setLink(bumperUtil.getLink(props.get(ApplicationConstants.PN_LINK, String.class),
				props.get(ApplicationConstants.PN_TITLE, String.class), false));
		badge.setImage(ImageUtil.getImagePath(resource));
		return badge;
	}

	public AppBadge getAndroid() {
		return this.android;
	}

	public AppBadge getIos() {
		return this.ios;
	}
}
