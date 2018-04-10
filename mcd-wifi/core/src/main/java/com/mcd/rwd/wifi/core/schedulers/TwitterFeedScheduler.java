package com.mcd.rwd.wifi.core.schedulers;

import javax.jcr.RepositoryException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mcd.rwd.wifi.core.service.twitter.TwitterFeedUpdater;
import com.mcd.rwd.wifi.core.utils.ConfigurationUtils;
import com.mcd.rwd.wifi.core.utils.RunnableOnMaster;

@Component(immediate = true, metatype = true,
    label = "MCD Wifi - Twitter Feed Refresh Scheduler",
    description = "Schedule job which refreshes Twitter Feed components on a recurring basis",
    policy = ConfigurationPolicy.REQUIRE)
@Service
@Properties(value = {
        @Property(name = "scheduler.expression", value = "0 0/15 * * * ?", label = "Refresh Interval",
                description = "Twitter Feed Refresh interval (Quartz Cron Expression)"),
        @Property(name = "scheduler.concurrent", boolValue = false, propertyPrivate = true) })
public final class TwitterFeedScheduler extends RunnableOnMaster {

    private static final Logger log = LoggerFactory.getLogger(TwitterFeedScheduler.class);

    @Reference
    private TwitterFeedUpdater twitterFeedService;
    @Reference
    private ResourceResolverFactory resourceResolverFactory;

	@Override
    public void runOnMaster() {
		ResourceResolver resourceResolver = null;
        try {
            log.info("Wifi Twitter Scheduler is running");
            log.debug("Master Instance, Running MCD Wifi Twitter Feed Scheduler");
            resourceResolver = ConfigurationUtils.getResourceResolver(resourceResolverFactory);
            twitterFeedService.updateTwitterFeedComponents(resourceResolver);
        } catch (RepositoryException e) {
			log.error("RepositoryException while running TwitterFeedScheduler.", e);
		}
        finally {
        	if (resourceResolver != null) {
        		ConfigurationUtils.closedResourceResolver(resourceResolver);
        	}
		}
    }
}
