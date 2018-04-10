package com.mcd.rwd.us.core.sightly;

import com.icfolson.aem.library.api.page.PageDecorator;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.models.annotations.DesignAnnotation;
import com.mcd.rwd.global.core.utils.LinkUtil;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.us.core.constants.SearchConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Created by Seema Pandey on 18-04-2016.
 */
@Model(adaptables =  SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SearchHeader {

    private static final Logger LOG = LoggerFactory.getLogger(SearchHeader.class);

    private boolean enabled;

    private String searchPagePath;

    private String pageTitle;

    private String placeHolder;

    private String searchButtonText;

    private String pageName;

    @DesignAnnotation("searchwidget")
    Resource searchWidget;

    @Inject
    SlingHttpServletRequest request;

    @Inject
    @Via("resource")
    private PageDecorator currentPage;

    @PostConstruct
    public void activate() {
        LOG.debug("SearchHeader Activate Method Called..");
        if(null != searchWidget) {
            ValueMap properties = searchWidget.getValueMap();
            enabled = properties.get(ApplicationConstants.PN_ENABLED, false);
            searchPagePath = LinkUtil.getHref(properties.get(SearchConstants.SEARCH_PAGE_PATH, String.class));
            pageTitle = properties.get(ApplicationConstants.PN_TITLE, "Search");
            placeHolder = properties.get(SearchConstants.PLACEHOLDER, "Search mcdonalds.com");
            searchButtonText = properties.get(SearchConstants.SEARCHBTN_TEXT, "Submit");
            pageName = PageUtil.getPageNameForAnalytics(currentPage, request);
        }
        else {
            LOG.error("No configuration done for search header..");
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getSearchPagePath() {
        return searchPagePath;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public String getSearchButtonText() {
        return searchButtonText;
    }

    public String getPageName() { return pageName; }

}

