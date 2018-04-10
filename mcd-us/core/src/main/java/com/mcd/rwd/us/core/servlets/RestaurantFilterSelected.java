package com.mcd.rwd.us.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.designer.Design;
import com.day.cq.wcm.api.designer.Designer;
import com.mcd.rwd.global.core.utils.MultiFieldPanelUtil;
import com.mcd.rwd.us.core.constants.RLConstants;


@SuppressWarnings("serial")
@SlingServlet(resourceTypes = {"mcd-us/components/content/prefiltered-restaurant"}, methods = "GET", selectors = "restaurantSelectedAttributes", extensions = "json")
@Properties(value = {
		@Property(name = "service.pid", value = "com.mcd.rwd.us.core.servlets.RestaurantFilterSelected", propertyPrivate = false),
		@Property(name = "service.description", value = "Provides restaurant attributes in json format", propertyPrivate = false),
		@Property(name = "service.vendor", value = "HCL Technologies", propertyPrivate = false) })
public class RestaurantFilterSelected extends SlingSafeMethodsServlet {

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
	
	private ResourceResolver resourceResolver;
	private Page currentPage;
	private static final String DEFAULT_DESIGN = "/etc/designs/default";
	private static final String FILTER_NAME_KEY = "filterName";
	private static final String RL_SITE_CONFIG = "rl";
	
	
		
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException,
			IOException {
		
		ResourceResolver resourceResolver = request.getResourceResolver();
		String resourcePath = request.getResource().getPath();
		String [] pagePath=resourcePath.split("jcr:content");
		PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
		Page currentPage = pageManager.getPage(pagePath[0]);
		String filterName=getSelectedFilter(resourceResolver,currentPage);
		PrintWriter out = response.getWriter();
		out.print(getRequiredJson(filterName));
	}
	private JSONArray getRequiredJson(String filterName) {
		JSONArray restAttributeArray = new JSONArray();
		
		String[] filterNameArr=filterName.split(",");
		
		for(int i=0;i<filterNameArr.length;i++)			
		{
		JSONObject restAttributeJson = new JSONObject();
		try {
		if(filterNameArr[i]!=null)
		{
			restAttributeJson.put(TEXT_LABEL,filterNameArr[i] );
			restAttributeJson.put(VALUE_LABEL,filterNameArr[i]);
			restAttributeArray.put(restAttributeJson);
		}
		} catch (JSONException e) {
			LOGGER.error("JSONException while creating json in RestaurantAttribute class", e);
		}
	}
		return restAttributeArray;

	}
	
	
	public String getSelectedFilter(ResourceResolver resourceResolver, Page page)
	{
		this.resourceResolver=resourceResolver;
		this.currentPage=page;
		
		String filterName=null;
		String filterString=null;
		if (getSiteConfigResourceDialog(RL_SITE_CONFIG) != null) {
			List<Map<String, String>> restaurantFilters = MultiFieldPanelUtil
					.getMultiFieldPanelValues(getSiteConfigResourceDialog(RL_SITE_CONFIG), RLConstants.RESTAURANTFILTERS);
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
						filterName=filterString+",";
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
	
	public Resource getSiteConfigResourceDialog(String name) {
		
		Designer designer = this.resourceResolver.adaptTo(Designer.class);
		Design currentDesign = designer.getDesign(this.currentPage);
		
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
