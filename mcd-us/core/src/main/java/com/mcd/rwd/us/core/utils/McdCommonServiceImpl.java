package com.mcd.rwd.us.core.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;
import org.apache.sling.settings.SlingSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.WCMMode;
import com.mcd.rwd.us.core.constants.ApplicationConstants;

/**
 * @author brijesh.t
 */
@Component(immediate = true)
@Service(value = McdCommonService.class)
public class McdCommonServiceImpl implements McdCommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(McdCommonServiceImpl.class);

    /**
     * ResolverFactory service.
     */
    @Reference
    private ResourceResolverFactory resolverFactory;
    /**
     * SlingRepository service.
     */
    @Reference
    private SlingRepository repository;
    /**
     * Sling service to find out run mode of instance.
     */
    @Reference
    private SlingSettingsService slingService;
    

    @Override
    public final Resource getResource(final String path, final Session session) throws LoginException,
            RepositoryException {
        final ResourceResolver resourceResolver = getResourceResolver();
        Resource resource = null;
        if (resourceResolver != null) {
            resource = resourceResolver.getResource(path);
        }
        return resource;
    }

    @Override
    public final ResourceResolver getResourceResolver() {
    	org.apache.sling.api.resource.ResourceResolver resourceResolver = null;
        try {
            final Map<String, Object> serviceParams = new HashMap<>();
            serviceParams.put(ResourceResolverFactory.SUBSERVICE, "gwsService");
            resourceResolver = resolverFactory.getServiceResourceResolver(serviceParams);
        } catch (final LoginException exp) {
            LOGGER.error("Error in method getResourceResolver fro getting resourcereolver {} ", exp);
        }
        return resourceResolver;
    }

    @Override
    public final Session getSession() {
        Session session;
        ResourceResolver resolver;
        resolver = getResourceResolver();
        session = resolver.adaptTo(Session.class);
        return session;
    }

    @Override
    public final void terminateSession(final Session session) {
        if (session != null) {
            session.logout();
        }
    }

    @Override
    public boolean isAuthorMode() {
        final Set<String> set = slingService.getRunModes();
        final Iterator<String> itr = set.iterator();
        boolean isAuthor = false;
        while (itr.hasNext()) {
            final String runMode = itr.next();
            if (ApplicationConstants.AUTHOR_RUN_MODE.equalsIgnoreCase(runMode)) {
                isAuthor = true;
            }
        }
        return isAuthor;
    }

    @Override
    public boolean isPublishMode() {
    	  final Set<String> set = slingService.getRunModes();
          final Iterator<String> itr = set.iterator();
          boolean isPublish = false;
          while (itr.hasNext()) {
              final String runMode = itr.next();
              if (ApplicationConstants.PUBLISH_RUN_MODE.equalsIgnoreCase(runMode)) {
            	  isPublish = true;
              }
          }
          return isPublish;
}

	@Override
	public boolean getRequestAuthorMode(SlingHttpServletRequest request) {
    	boolean runmode = Boolean.FALSE;
    	WCMMode mode = WCMMode.fromRequest(request);
    	if(mode.name().equals(WCMMode.EDIT.toString())){
    		runmode = true;
    	}	
    	return runmode;
	}

	@Override
	public boolean getRequestPublishMode(SlingHttpServletRequest request) {
    	boolean runmode = Boolean.FALSE;
    	WCMMode mode = WCMMode.fromRequest(request);
    	if(mode.name().equals(WCMMode.DISABLED.toString())){
    		runmode = true;
    	}	
    	return runmode;
    }
	}
