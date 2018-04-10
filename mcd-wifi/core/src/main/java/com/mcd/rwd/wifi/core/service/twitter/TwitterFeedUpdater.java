package com.mcd.rwd.wifi.core.service.twitter;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.ResourceResolver;

public interface TwitterFeedUpdater {

    void updateTwitterFeedComponents(ResourceResolver resourceResolver) throws RepositoryException;

}
