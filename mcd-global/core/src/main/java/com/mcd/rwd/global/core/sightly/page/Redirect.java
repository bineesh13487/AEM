package com.mcd.rwd.global.core.sightly.page;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.WCMMode;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.utils.LinkUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by Rakesh.Balaiah on 04-03-2016.
 */

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public final class Redirect {

	private static final Logger LOGGER = LoggerFactory.getLogger(Redirect.class);

	private String path;

	@Inject
	SlingHttpServletRequest request;

	@Inject
	SlingScriptHelper slingScriptHelper;

	@Inject @Via("resource")
	String location;

	@Inject @Via("resource")
	String faqTopicId;

	@Inject
	Page currentPage;

	@PostConstruct
	protected void activate() {

		try {
			if (StringUtils.isNotBlank(location) && (currentPage != null && !location
					.equals(currentPage.getPath()))) {

				setRedirectPath(location);
				this.path = null != faqTopicId ? this.path + ApplicationConstants.URL_HASH + faqTopicId : this.path;

				if ((WCMMode.fromRequest(request) == WCMMode.DESIGN || WCMMode.fromRequest(request) == WCMMode.PREVIEW) && null != slingScriptHelper) {
					slingScriptHelper.getResponse().sendRedirect(this.path);
					return;
				}
			}
		} catch (IOException ioe) {
			LOGGER.error("Unable to process redirection.", ioe);
		}
		catch (IllegalStateException iste) {
			LOGGER.error("Response commited", iste);
		}
	}

	public String getPath() {
		return this.path;
	}

	private void setRedirectPath(String location) {

		// check for absolute path
		final int protocolIndex = location.indexOf(":/");
		final int queryIndex = location.indexOf('?');

		this.path = LinkUtil.getHref(location);

		if (protocolIndex > -1 && (queryIndex == -1 || queryIndex > protocolIndex)) {
			this.path = location;
		}
	}
}
