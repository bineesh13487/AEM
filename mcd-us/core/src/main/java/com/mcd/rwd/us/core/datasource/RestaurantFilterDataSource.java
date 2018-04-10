package com.mcd.rwd.us.core.datasource;

import com.day.cq.commons.PathInfo;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.designer.Design;
import com.day.cq.wcm.api.designer.Designer;
import com.icfolson.aem.library.api.request.ComponentServletRequest;
import com.icfolson.aem.library.core.servlets.datasource.AbstractOptionsDataSourceServlet;
import com.icfolson.aem.library.core.servlets.optionsprovider.Option;
import com.mcd.rwd.global.core.utils.MultiFieldPanelUtil;
import com.mcd.rwd.us.core.constants.RLConstants;
import com.mcd.rwd.us.core.servlets.RestaurantFilterSelected;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by apple on 11/08/17.
 */
@SlingServlet(resourceTypes = {
        "mcd-us/dataSource/prefiltered-restaurant"}, methods = "GET")
public class RestaurantFilterDataSource extends AbstractOptionsDataSourceServlet {

    /**
     * default LOGGER
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantFilterSelected.class);

    /**
     * text label
     */
    private static final String TEXT_LABEL = "text";

    /**
     * value label
     */
    private static final String VALUE_LABEL = "value";
    private static final String DEFAULT_DESIGN = "/etc/designs/default";
    private static final String FILTER_NAME_KEY = "filterName";
    private static final String RL_SITE_CONFIG = "rl";

    @Override
    protected List<Option> getOptions(ComponentServletRequest componentServletRequest) {
        List<Option> options = new ArrayList<>();
        SlingHttpServletRequest request = componentServletRequest.getSlingRequest();
        if(null != request) {
            ResourceResolver resourceResolver = request.getResourceResolver();
            String resourcePath = null;
            if (null != request.getParameter("item")) {
                resourcePath = request.getParameter("item");
            }
            else {
                resourcePath = new PathInfo((String)
                        request.getAttribute("javax.servlet.include.path_info")).getSuffix();
            }
            if (StringUtils.isNotEmpty(resourcePath)) {
                String[] pagePath = resourcePath.split("jcr:content");
                PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
                Page currentPage = pageManager.getPage(pagePath[0]);
                String filterName = getSelectedFilter(resourceResolver, currentPage);
                getRequiredOption(filterName, options);
            }
        }
        return options;
    }

    private void getRequiredOption(String filterName , List<Option> options) {

        String[] filterNameArr=filterName.split(",");

        for(int i=0;i<filterNameArr.length;i++)
        {
            Option categoryOption = null;
                if(filterNameArr[i]!=null)
                {
                    categoryOption = new Option(filterNameArr[i], filterNameArr[i]);
                    options.add(categoryOption);

                }
        }

    }


    public String getSelectedFilter(ResourceResolver resourceResolver, Page page)
    {

        String filterName=null;
        String filterString=null;
        if (getSiteConfigResourceDialog(RL_SITE_CONFIG, resourceResolver , page) != null) {
            List<Map<String, String>> restaurantFilters = MultiFieldPanelUtil
                    .getMultiFieldPanelValues(getSiteConfigResourceDialog(RL_SITE_CONFIG , resourceResolver , page), RLConstants.RESTAURANTFILTERS );
            if (restaurantFilters != null && !restaurantFilters.isEmpty()) {
                Iterator<Map<String, String>> itr = restaurantFilters.iterator();
                while (itr.hasNext()) {
                    Map<String, String> item = itr.next();
                    Boolean serviceAsFilter = Boolean.valueOf(item.get(RLConstants.SERVICEASFILTER));
                    filterString = item.get(FILTER_NAME_KEY);
                    if(serviceAsFilter.equals(true))
                    {
                        if(filterName!=null)
                        {
                            filterName=filterName+","+filterString;
                        }
                        else{
                            filterName=filterString;
                        }
                    }
                }
            } else {
                LOGGER.info("No restaurant filters configured in the restaurant locator page");
            }
        }

        LOGGER.info("Filter Name "+filterName);
        return filterName;

    }

    /**
     * Returns Site Configuration Resource.
     *
     * @param name
     * @return Resource
     */

    public Resource getSiteConfigResourceDialog(String name , ResourceResolver resourceResolver , Page page) {

        Designer designer = resourceResolver.adaptTo(Designer.class);
        Design currentDesign = designer.getDesign(page);

        if (StringUtils.isNotBlank(name)) {
            if (DEFAULT_DESIGN.equals(currentDesign.getPath())) {
                LOGGER.info("No design configured for the site. Please configure it in the home page.");
            }
            Resource resource = currentDesign.getContentResource();
            if (resource != null && null != resource.getChild(name)) {
                return resource.getChild(name);

            }
        }
        return null;
    }
}
