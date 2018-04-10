package com.mcd.rwd.wifi.core.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.webservicesupport.ConfigurationManager;
import com.day.cq.wcm.webservicesupport.ConfigurationManagerFactory;

public final class ConfigurationUtils {
	 
	private static final Logger logger = LoggerFactory.getLogger(ConfigurationUtils.class);
	 
	/**
	 * Private Constructor.
	 */
	private ConfigurationUtils() {
		
	}
	
	public static ResourceResolver getResourceResolver(ResourceResolverFactory resourceResolverFactory) {
		ResourceResolver resourceResolver = null;
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put(ResourceResolverFactory.SUBSERVICE, "wifiservice");
			resourceResolver = resourceResolverFactory.getServiceResourceResolver(param);
		} catch (LoginException e) {
			logger.error("LoginException while running ConfigurationUrils.", e);
		} 
		return resourceResolver;
	}
    
	public static ConfigurationManager getConfigurationManager(ResourceResolver resourceResolver, ConfigurationManagerFactory configurationManagerFactory) {
    	return configurationManagerFactory.getConfigurationManager(resourceResolver);
	}
	
	public static void closedResourceResolver(ResourceResolver resourceResolver){
		if (resourceResolver != null) {
			resourceResolver.close();
		}
	}
}
