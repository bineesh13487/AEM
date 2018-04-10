package com.mcd.rwd.global.core.sightly;

import com.citytechinc.cq.component.annotations.Component;
import com.day.cq.wcm.api.Page;
import com.mcd.rwd.global.core.bean.html.Image;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.models.annotations.DesignAnnotation;
import com.mcd.rwd.global.core.utils.ImageUtil;
import com.mcd.rwd.global.core.utils.LinkUtil;
import com.mcd.rwd.global.core.utils.PageUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;

import javax.annotation.PostConstruct;
import javax.inject.Inject;


@Component(value = "Logo", name="logo" , description = "Logo Component for McD Responsive Site" ,path = "content", group = ".hidden", disableTargeting = true)
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public final class LogoHandler{

	private Image image;

	private String pageNameForAnalytics = StringUtils.EMPTY;

	@Inject
	Page currentPage;

	@DesignAnnotation("logo")
	Resource logoResource;


	@Inject @Via("request")
	Resource resource;

	/**
	 * Post Initialization control.
	 */
	@PostConstruct
	public void activate() throws Exception {

		pageNameForAnalytics = PageUtil.getPageNameForAnalytics(currentPage);
		image = new Image();
		if(null != logoResource) {
			ValueMap properties = logoResource.getValueMap();
			image.setSrc(ImageUtil.getImagePath(logoResource, "image"));
			image.setLink(LinkUtil.getHref(properties
							.get(ApplicationConstants.PN_LINK, String.valueOf(ApplicationConstants.URL_HASH)),
					String.valueOf(ApplicationConstants.URL_HASH)));
			image.setAlt(properties.get(ApplicationConstants.PN_ALT, String.class));
			image.setHeader(properties.get(ApplicationConstants.PN_HEADER, String.class));
		}
	}

	public Image getImage() {
		return image;
	}

	public String getPageNameForAnalytics() {
		return pageNameForAnalytics;
	}
}
