package com.mcd.rwd.global.core.dispatcher.invalidators;

import org.osgi.service.event.Event;

import java.util.List;
import java.util.regex.Pattern;

/**
 *  Invalidator interface
 *   implements strategies for clearing dispatcher cache in particular replication events
 */
public interface Invalidator {

    String PROP_NAME = "service-name";
    Pattern ROOT_SITE_PATTERN = Pattern.compile("/content/([^/]+)/([^/]+)/.*");
    String ROOT_DESIGN_PATH = "/etc/designs/mcd";
    Pattern ROOT_DESIGN_SITE_PATTERN = Pattern.compile("/etc/designs/mcd/([^/]+)/([^/]+)/.*");
    Pattern ROOT_SITE_JCR_CONTENT_PATTERN = Pattern.compile("/content/([^/]+)/([^/]+)/jcr:content");
    int GROUP_COUNTRY = 1;
    int GROUP_LANGUAGE = 2;
    String SITE_TEMPLATE = "/content/%1$s/%2$s/";
    String DESIGN_TEMPLATE = "/etc/designs/mcd/%1$s/%2$s/";

    List<String> processResourceEvent(Event event);
    String getInvalidatorServiceId();

}

