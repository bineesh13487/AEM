package com.mcd.rwd.us.core.utils;


import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

/**
 * . Service exposing common utilities providing handle to basic Sling and JCR implementations like {@link Resource},
 * {@link ResourceResolver} and {@link Session}
 * @author tewari.b
 */
public interface McdCommonService {


    /**
     * Gets the resource.
     *
     * @param path the path
     * @param session the session
     * @return the resource
     * @throws LoginException the login exception
     * @throws RepositoryException the repository exception
     */
    Resource getResource(
            String path, Session session)
                    throws LoginException, RepositoryException;


    /**
     * Gets the resource resolver.
     *
     * @param session the session
     * @return the resource resolver
     * @throws LoginException the login exception
     * @throws RepositoryException the repository exception
     */
    ResourceResolver getResourceResolver();


    /**
     * Gets the session.
     *
     * @return the session
     */
    Session getSession() ;

    /**
     * . Gets the JCR {@code Session} to the specified workspace
     * @param session object
     */
    void terminateSession(
            Session session);

    /**
     * check if it author mode of running instance.
     * @return boolean
     */
    boolean isAuthorMode();

    /**
     * check if it publish mode of running instance.
     * @return boolean
     */
    boolean isPublishMode();
    
    /**
     * check if it author mode of running instance from request.
     * @return boolean
     */
    boolean getRequestAuthorMode(SlingHttpServletRequest request);
    
    /**
     * check if it publish mode of running instance from request.
     * @return boolean
     */
    boolean getRequestPublishMode(SlingHttpServletRequest request);
    
    

}
