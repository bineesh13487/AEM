package com.mcd.rwd.global.core.sightly.page;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Design;
import com.day.cq.wcm.commons.WCMUtils;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.models.annotations.DesignAnnotation;
import com.mcd.rwd.global.core.utils.ImageUtil;
import com.mcd.rwd.global.core.utils.PageUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;

/**
 * Created by Rakesh.Balaiah on 07-03-2016.
 */

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PageMeta { //extends McDUse {

	private String thumbnail;

	private String title;

	private String keywords;

	private String desc;

	private String designPath;

	private String faviconPath;

	private String renderDateString;

	@Inject @Named("ogType")
	@Default(values = "website")
	private String ogType;

	@Inject @Named("ogImg")
	private String ogImg;

	@Inject @Named("ogUrl")
	private String ogUrl;

	@Inject @Named(ApplicationConstants.PN_TYPE)
	private String pageType;

	@Inject
	private Page currentPage;

	@Inject
	private Design currentDesign;

	@DesignAnnotation("favicon")
	private Resource faviconRes;

	@DesignAnnotation("thumbnail")
	private Resource thumbnailRes;

	@PostConstruct
	protected void activate() {

		//ValueMap props = getProperties();
		//Page page = getCurrentPage();

		this.title = PageUtil.getPageTitle(currentPage);
		this.keywords = WCMUtils.getKeywords(currentPage);
		this.desc = StringUtils.isNotBlank(currentPage.getDescription()) ? currentPage.getDescription() : this.title;
		//this.ogType = props.get("ogType", "website");
		//this.ogImg = props.get("ogImg", String.class);
		//this.ogUrl = props.get("ogUrl", String.class);
		//this.pageType = props.get(ApplicationConstants.PN_TYPE, String.class);
		this.designPath = currentDesign.getPath();
		this.faviconPath = ImageUtil.getImagePath(faviconRes, null);
		this.thumbnail = ImageUtil.getImagePath(currentPage.getContentResource("thumbnail"), null);
		this.renderDateString = new Date().toString();

		if (StringUtils.isBlank(this.thumbnail)) {
			this.thumbnail = ImageUtil.getImagePath(thumbnailRes, null);
		}
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public String getTitle() {
		return title;
	}

	public String getKeywords() {
		return keywords;
	}

	public String getDesc() {
		return desc;
	}

	public String getOgType() {
		return ogType;
	}

	public String getOgImg() {
		return ogImg;
	}

	public String getOgUrl() {
		return ogUrl;
	}

	public String getDesignPath() {
		return designPath;
	}

	public String getFaviconPath() {
		return faviconPath;
	}

	public String getPageType() {
		return pageType;
	}

	public String getRenderDateString() {
		return renderDateString;
	}
}
