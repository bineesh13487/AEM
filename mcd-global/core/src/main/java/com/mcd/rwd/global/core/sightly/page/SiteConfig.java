package com.mcd.rwd.global.core.sightly.page;

import com.day.cq.wcm.api.Page;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Created by Rakesh.Balaiah on 02-03-2016.
 */

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SiteConfig {

	private String formURL;

	private String dialogPath;

	private String title;

	@Inject
	Page page;

	@Self
	Resource resource;

	@PostConstruct
	protected void activate() {
		this.formURL = page.getPath() + "/jcr:content";
		this.dialogPath = resource.getPath() + "/dialog";
		this.title = StringUtils.isNotBlank(page.getTitle()) ? page.getTitle() : page.getName();
	}

	public String getFormURL() {
		return this.formURL;
	}

	public String getDialogPath() {
		return this.dialogPath;
	}

	public String getTitle() {
		return this.title;
	}
}
