package com.mcd.rwd.wifi.core.service.twitter.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.adapter.AdapterFactory;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.webservicesupport.ConfigurationConstants;
import com.day.cq.wcm.webservicesupport.ConfigurationManager;
import com.day.cq.wcm.webservicesupport.ConfigurationManagerFactory;
import com.mcd.rwd.wifi.core.service.twitter.TwitterClient;
import com.mcd.rwd.wifi.core.utils.ConfigurationUtils;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

@Component(metatype = true, label = "MCD Wifi - Twitter Client Adapter Factory",
    description = "Adapter Factory to generate TwitterClient objects.")
@Service
@Properties({
        @Property(name = AdapterFactory.ADAPTABLE_CLASSES, value = { "com.day.cq.wcm.api.Page",
                "com.day.cq.wcm.webservicesupport.Configuration" }, propertyPrivate = true),
        @Property(name = AdapterFactory.ADAPTER_CLASSES, value = { "twitter4j.Twitter",
                "com.mcd.rwd.wifi.core.service.twitter.TwitterClient" }, propertyPrivate = true) })
public final class TwitterAdapterFactory implements AdapterFactory {

    private static final String CLOUD_SERVICE_NAME = "twitterconnect";

    private static final Logger log = LoggerFactory.getLogger(TwitterAdapterFactory.class);

    @Property(label = "HTTP Proxy Host", description = "HTTP Proxy Host, leave blank for none")
    private static final String PROP_HTTP_PROXY_HOST = "http.proxy.host";

    @Property(label = "HTTP Proxy Port", description = "HTTP Proxy Port, leave 0 for none", intValue = 0)
    private static final String PROP_HTTP_PROXY_PORT = "http.proxy.port";

    @Reference
    private ResourceResolverFactory resourceResolverFactory;
    @Reference
    private ConfigurationManagerFactory configurationManagerFactory;

    private TwitterFactory factory;

    private String httpProxyHost;

    private int httpProxyPort;

    @SuppressWarnings("unchecked")
    @Override
    public <AdapterType> AdapterType getAdapter(Object adaptable, Class<AdapterType> type) {
        TwitterClient client = null;
        if (adaptable instanceof Page) {
            client = createTwitterClient((Page) adaptable);
        } else if (adaptable instanceof com.day.cq.wcm.webservicesupport.Configuration) {
            client = createTwitterClient((com.day.cq.wcm.webservicesupport.Configuration) adaptable);
        }

        if (client != null) {
            if (type == TwitterClient.class) {
                return (AdapterType) client;
            } else if (type == Twitter.class) {
                return (AdapterType) client.getTwitter();
            }
        }

        return null;
    }

    private Configuration buildConfiguration() {
        final ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setJSONStoreEnabled(true);
        builder.setApplicationOnlyAuthEnabled(true);
        if (StringUtils.isNotBlank(httpProxyHost) && httpProxyPort > 0) {
            builder.setHttpProxyHost(httpProxyHost);
            builder.setHttpProxyPort(httpProxyPort);
        }
        return builder.build();
    }

    private TwitterClient createTwitterClient(com.day.cq.wcm.webservicesupport.Configuration config) {
        Resource oauthConfig = config.getContentResource().listChildren().next();
        ValueMap oauthProps = oauthConfig.getValueMap();
        String consumerKey = oauthProps.get("oauth.client.id", String.class);
        String consumerSecret = oauthProps.get("oauth.client.secret", String.class);

        if (consumerKey != null && consumerSecret != null) {
            Twitter t = factory.getInstance();
            log.debug("Creating client for key {}.", consumerKey);
            t.setOAuthConsumer(consumerKey, consumerSecret);
            try {
                t.getOAuth2Token();
                return new TwitterClientImpl(t, config);
            } catch (TwitterException e) {
                log.error("Unable to create Twitter client.", e);
                return null;
            }
        } else {
            log.warn("Key or Secret missing for configuration {}", config.getPath());
        }

        return null;
    }

    private TwitterClient createTwitterClient(Page page) {
    	ResourceResolver resourceResolver = ConfigurationUtils.getResourceResolver(resourceResolverFactory);
    	ConfigurationManager configurationManager = ConfigurationUtils.getConfigurationManager(resourceResolver, configurationManagerFactory);
		com.day.cq.wcm.webservicesupport.Configuration config = findTwitterConfiguration(page, configurationManager);
		TwitterClient twitterClient = null;
		if (config != null) {
			twitterClient = createTwitterClient(config);
		}
		if (resourceResolver != null) {
			ConfigurationUtils.closedResourceResolver(resourceResolver);
		}
        return twitterClient;
    }

    private com.day.cq.wcm.webservicesupport.Configuration findTwitterConfiguration(Page page, ConfigurationManager configurationManager) {
        final HierarchyNodeInheritanceValueMap pageProperties = new HierarchyNodeInheritanceValueMap(
                page.getContentResource());
        final String[] services = pageProperties.getInherited(ConfigurationConstants.PN_CONFIGURATIONS,
                new String[0]);
        return configurationManager.getConfiguration(CLOUD_SERVICE_NAME, services);
    }

    @Activate
    protected void activate(Map<String, Object> properties) {
        this.httpProxyHost = PropertiesUtil.toString(properties.get(PROP_HTTP_PROXY_HOST), null);
        this.httpProxyPort = PropertiesUtil.toInteger(properties.get(PROP_HTTP_PROXY_PORT), 0);
        this.factory = new TwitterFactory(buildConfiguration());
    }

}
