package com.mcd.rwd.global.core.utils;

import com.day.cq.wcm.api.Page;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

/**
 * Defines some common utility functions which are used
 * while working with resource.
 *
 * @author HCL
 */
public final class ResourceUtil {

    /**
     * Private Constructor.
     */
    private ResourceUtil() {

    }

    /**
     * Retrieves the child resource.
     *
     * @param res
     * @param child
     * @return
     */
    public static Resource getChildResource(final Resource res, final String child) {
        return res.getChild(child);
    }

    /**
     * Retrieves the Value Map of the child Resource.
     *
     * @param res
     * @param child
     * @return
     */
    public static ValueMap getValueMap(final Resource res, final String child) {
        Resource childResource = getChildResource(res, child);
        ValueMap properties = null;
        if (null != childResource) {
            properties = childResource.getValueMap();
        }
        return properties;
    }

    /**
     * Retrieves the String property of the child resource.
     *
     * @param res
     * @param child
     * @param name
     * @return
     */
    public static String getChildResourceProperty(final Resource res, final String child, final String name) {
        ValueMap properties = getValueMap(res, child);
        String value = StringUtils.EMPTY;
        if (null != properties) {
            value = properties.get(name, StringUtils.EMPTY);
        }
        return value;
    }

    /**
     * Retrieves the String[] (or multi-valued) property of the child resource.
     *
     * @param res
     * @param child
     * @param name
     * @return
     */
    public static String[] getChildResourceMultiProperty(final Resource res, final String child,
                                                         final String name) {
        ValueMap properties = getValueMap(res, child);
        String[] value = null;
        if (null != properties) {
            value = properties.get(name, String[].class);
        }
        return value;
    }

    public static String getCountryCodeFromResource(Resource resource) {
        Page page = getContainerPage(resource);

        if (null != page) {
            return PageUtil.getCountry(page);
        }
        return null;
    }

    public static String getLanguageCodeFromResource(Resource resource) {
        Page page = getContainerPage(resource);
        if (null != page) {
            return PageUtil.getLanguage(page);
        }
        return null;
    }

    private static boolean isDesignPath(String path) {
        return path != null && path.contains(ApplicationConstants.PATH_DESIGN);
    }

    /**
     * Returns the page containing this resource.
     *
     * @param resource
     * @return Page
     */
    public static Page getContainerPage(Resource resource) {
        if (resource != null) {
            if (resource.adaptTo(Page.class) != null) {
                return resource.adaptTo(Page.class);
            } else {
                return getContainerPage(resource.getParent());
            }
        }
        return null;
    }
}
