package com.mcd.rwd.global.core.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;

import com.day.cq.wcm.api.Page;
import com.mcd.rwd.global.core.constants.ApplicationConstants;

/**
 * Created by Rakesh.Balaiah on 04-08-2015.
 */
public final class PageUtil {
	
    private static final String TEMPLATE_PATH = "/apps/mcd-us/templates/product-detail";

    /* Disable Instantiation */
    private PageUtil() {
        //Private Constructor
    }

    /**
     * Returns the Title for Navigation for the given Page.
     *
     * @param page - The Page for which the title is to be fetched.
     * @return String - The title of the page.
     */
    public static String getNavTitle(final Page page) {
        if (StringUtils.isNotBlank(page.getNavigationTitle())) {
            return page.getNavigationTitle();
        }
        return getPageTitle(page);
    }

    /**
     * Returns the Title for Page.
     *
     * @param page - The Page for which the title needs to be fetched.
     * @return String - The title of the page.
     */
    public static String getPageTitle(final Page page) {
        if (StringUtils.isNotBlank(page.getPageTitle())) {
            return page.getPageTitle();
        } else if (StringUtils.isNotBlank(page.getTitle())) {
            return page.getTitle();
        }
        return page.getName();
    }

    /**
     * Returns the Name for Page.
     *
     * @param page - The Page for which the Name needs to be fetched.
     * @return String - The Name of the page.
     */
    public static String getPageNameForAnalytics(final Page page) {
        if (page != null) {
            if (page.getName().equalsIgnoreCase(ApplicationConstants.USRWD_EN_HOME)
                    || page.getName().equalsIgnoreCase(ApplicationConstants.USRWD_ES_HOME)) {
                return "Home";
            }
            return page.getName();
        }
        return null;
    }

    public static String getPageNameForAnalytics(final Page page, final SlingHttpServletRequest request) {
        if (page != null) {
            if (isProductPage(page)) {
                String pathInfo = request.getRequestURI();
                return pathInfo.substring(pathInfo.lastIndexOf('/') + 1, pathInfo.indexOf(".html"));
            }
            if (page.getName().equalsIgnoreCase(ApplicationConstants.USRWD_EN_HOME)
                    || page.getName().equalsIgnoreCase(ApplicationConstants.USRWD_ES_HOME)) {
                return "Home";
            }
            return page.getName();
        }
        return null;
    }

    /**
     * Returns the country for the Page.
     *
     * @param page - The Page for which the country code is needed.
     * @return The country code for the page.
     */
    public static final String getCountry(final Page page) {
        int depth = 1;
        if (page != null) {
            if (StringUtils.startsWith(page.getPath(), ApplicationConstants.PATH_DESIGN)
                    && page.getAbsoluteParent(3) != null) {
                depth = 3;
            }
            if (((StringUtils.containsIgnoreCase(page.getPath(),ApplicationConstants.JEDDAH_CONSTANT))||
                    (StringUtils.containsIgnoreCase(page.getPath(),ApplicationConstants.RIYADTH_CONSTANT)))
                    && page.getAbsoluteParent(3) != null) {
                if(StringUtils.containsIgnoreCase(page.getPath(),"prelaunch")){
                    depth = 4;
                }else{
                    depth = 3;

                }

            }
            String country = page.getAbsoluteParent(depth).getName();
            if (StringUtils.equalsIgnoreCase(country, "prelaunch") && page.getAbsoluteParent(++depth) != null) {
                country = page.getAbsoluteParent(depth).getName();
            }
            return country;
        }
        return null;
    }

    /**
     * Returns the language code for the page.
     *
     * @param page - The page for which the language code is needed.
     * @return The language code for the page.
     */
    public static final String getLanguage(final Page page) {
        String country = getCountry(page);
        if (page != null && country != null) {
            int depth = 2;

            if (StringUtils.startsWith(page.getPath(), ApplicationConstants.PATH_DESIGN)
                    && page.getAbsoluteParent(3) != null) {
                depth = page.getAbsoluteParent(4) != null ? 4 : 3;
            }
            if((StringUtils.containsIgnoreCase(page.getPath(),"prelaunch"))&& ((StringUtils.containsIgnoreCase(page.getPath(),ApplicationConstants.JEDDAH_CONSTANT))||
                    (StringUtils.containsIgnoreCase(page.getPath(),ApplicationConstants.RIYADTH_CONSTANT)))){
                depth=3;
            }
            String language;
            if (page.getAbsoluteParent(depth) != null) {
                language = page.getAbsoluteParent(depth).getName();
            } else {
                depth--;
                language = page.getAbsoluteParent(depth).getName();
            }

            if (StringUtils.equalsIgnoreCase(language, country) && page.getAbsoluteParent(++depth) != null) {
                language = page.getAbsoluteParent(depth).getName();
            }
            return language;
        }
        return null;
    }

    public static boolean isProductPage(Page page) {
        return page != null && page.getTemplate() != null && TEMPLATE_PATH
                .equals(page.getTemplate().getPath());
    }

    public static boolean containsShortURL(SlingHttpServletRequest request, Page page) {
        String[] selectors = request.getRequestPathInfo().getSelectors();
        String pathInfo = request.getRequestURI();
        return (selectors == null || selectors.length == 0) && !pathInfo
                .contains('/' + page.getName() + ".html");
    }
    
}
