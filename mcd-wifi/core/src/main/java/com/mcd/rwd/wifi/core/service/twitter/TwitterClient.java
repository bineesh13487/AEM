package com.mcd.rwd.wifi.core.service.twitter;

import twitter4j.Twitter;
import aQute.bnd.annotation.ProviderType;
import com.day.cq.wcm.webservicesupport.Configuration;

/**
 * Service interface which wraps the Twitter4j API to expose the originating
 * Cloud Service Configuration.
 * 
 * To obtain an instance of this class, adapt either a Page or a Configuration object.
 * 
 * Note that these clients always use only Application authentication.
 */
@ProviderType
public interface TwitterClient {

    /**
     * Get the Cloud Service Configuration from which this client was created.
     * 
     * @return the service configuration
     */
    Configuration getServiceConfiguration();

    /**
     * Get the Twitter4j client.
     * 
     * @return the client
     */
    Twitter getTwitter();

}
