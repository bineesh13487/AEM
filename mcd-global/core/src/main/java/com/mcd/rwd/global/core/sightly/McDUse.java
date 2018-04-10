package com.mcd.rwd.global.core.sightly;

import com.adobe.cq.sightly.SightlyWCMMode;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.components.Component;
import com.day.cq.wcm.api.designer.Design;
import com.day.cq.wcm.api.designer.Designer;
import com.day.cq.wcm.api.designer.Style;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.scripting.sightly.pojo.Use;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Bindings;

/**
 * Created by Rakesh.Balaiah on 04-03-2016.
 */
public abstract class McDUse implements Use {

	private final Logger log = LoggerFactory.getLogger(McDUse.class);

	private Bindings bindings;

	private static final String DEFAULT_DESIGN = "/etc/designs/default";

	@Override public void init(Bindings scriptBindings) {
		this.bindings = scriptBindings;

		try {
			this.activate();
		} catch (Exception e) {
			this.log.error("Failed to activate Use class", e);
		}

	}

	public abstract void activate() throws Exception;

	public <T> T get(String name, Class<T> type) {
		Object obj = this.bindings.get(name);

		try {
			return type.cast(obj);
		} catch (ClassCastException var5) {
			this.log.error("Failed to cast value", var5);
			return null;
		}
	}

	public SightlyWCMMode getWcmMode() {
		return this.get("wcmmode", SightlyWCMMode.class);
	}

	public PageManager getPageManager() {
		return this.get("pageManager", PageManager.class);
	}

	public Page getCurrentPage() {
		return this.get("currentPage", Page.class);
	}

	public Page getResourcePage() {
		return this.get("resourcePage", Page.class);
	}

	public ValueMap getPageProperties() {
		return this.get("pageProperties", ValueMap.class);
	}

	public ValueMap getProperties() {
		return this.get("properties", ValueMap.class);
	}

	public Designer getDesigner() {
		return this.get("designer", Designer.class);
	}

	public Design getCurrentDesign() {
		return this.get("currentDesign", Design.class);
	}

	public Style getCurrentStyle() {
		return this.get("currentStyle", Style.class);
	}

	public Component getComponent() {
		return this.get("component", Component.class);
	}

	public ValueMap getInheritedProperties() {
		return this.get("inheritedPageProperties", ValueMap.class);
	}

	public Resource getResource() {
		return this.get("resource", Resource.class);
	}

	public ResourceResolver getResourceResolver() {
		return this.getRequest().getResourceResolver();
	}

	public SlingHttpServletRequest getRequest() {
		return this.get("request", SlingHttpServletRequest.class);
	}

	public SlingHttpServletResponse getResponse() {
		return this.get("response", SlingHttpServletResponse.class);
	}

	public SlingScriptHelper getSlingScriptHelper() {
		return this.get("sling", SlingScriptHelper.class);
	}

	/**
	 * Returns Site Level Configuration Properties.
	 *
	 * @param name
	 * @return ValueMap
	 */
	public ValueMap getSiteConfig(String name) {
		if (getSiteConfigResource(name) != null) {
			return getSiteConfigResource(name).adaptTo(ValueMap.class);
		}
		return null;
	}

	/**
	 * Returns Site Configuration Resource.
	 *
	 * @param name
	 * @return Resource
	 */
	public Resource getSiteConfigResource(String name) {
		if (StringUtils.isNotBlank(name)) {
			if (DEFAULT_DESIGN.equals(getCurrentDesign().getPath())) {
				this.log.info("No design configured for the site. Please configure it in the home page.");
			}
			Resource resource = getCurrentDesign().getContentResource();
			if (resource != null && null != resource.getChild(name)) {
				return resource.getChild(name);
				
			}
		}
		return null;
	}
}
