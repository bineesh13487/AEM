package com.mcd.rwd.us.core.sightly.page;

import com.day.cq.wcm.api.Page;
import com.mcd.rwd.global.core.utils.PageUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import com.mcd.rwd.us.core.service.McdFactoryConfig;
import com.mcd.rwd.us.core.service.McdFactoryConfigConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Created by Rakesh.Balaiah on 01-05-2016.
 */

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Config {

	private static final Logger LOG = LoggerFactory.getLogger(Config.class);

	private McdFactoryConfig mcdFactoryConfig;
	private String country;

	private String language;

    private String analyticsPageName;

    private String clientLibFolder = "mcd.us";

    @Inject
	SlingHttpServletRequest request;

    @Inject
    McdFactoryConfigConsumer mcdFactoryConfigConsumer;

    @Inject
    Page currentPage;

    private boolean isRtl;

	@PostConstruct
	public void activate() throws Exception {
		this.country = PageUtil.getCountry(currentPage);
		this.language = PageUtil.getLanguage(currentPage);
        this.analyticsPageName = PageUtil.getPageNameForAnalytics(currentPage, request);
		LOG.debug("Country {} and language {} set.", this.country, this.language);
		clientLibFolder = "mcd.us";
		if (country != null && language != null) {
			if (mcdFactoryConfigConsumer != null){
				mcdFactoryConfig = mcdFactoryConfigConsumer.getMcdFactoryConfig(country, language);
				if (mcdFactoryConfig != null) {
					isRtl=mcdFactoryConfig.isRTL();
				}
			}
		}
	}

    public String getClientLibFolder() {
        return clientLibFolder;
    }

    public void setClientLibFolder(String clientLibFolder) {
        this.clientLibFolder = clientLibFolder;
    }

	public String getCountry() {
		return country;
	}

	public String getLanguage() {
		return language;
	}

    public String getAnalyticsPageName() {
        return analyticsPageName;
    }

	public boolean isRtl() {
		return isRtl;
	}

}
