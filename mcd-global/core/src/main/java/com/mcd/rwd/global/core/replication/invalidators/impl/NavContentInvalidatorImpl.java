package com.mcd.rwd.global.core.replication.invalidators.impl;

import com.mcd.rwd.global.core.replication.invalidators.Invalidator;
import org.apache.commons.lang.ArrayUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingConstants;
import org.osgi.service.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

@Component(
        label = "Nav Content Invalidator",
        description = "looks for changes to navigation templates and forwards paths to a dispatcher flusher",
        configurationFactory = true)
@Properties({
        @Property(
                label = "Service Name",
                name = Invalidator.PROP_NAME,
                description = "uniquely identify the service impl by name.",
                value = NavContentInvalidatorImpl.SERVICE_NAME
        )
})
@Service
public class NavContentInvalidatorImpl implements Invalidator {

    public static final String SERVICE_NAME = "navContentInvalidator";
    private static final Logger LOG = LoggerFactory.getLogger(NavContentInvalidatorImpl.class);
    private static final String JCR_TITLE = "jcr:title";
    private static final String PAGE_NAV_RESOURCE_TYPE = "mcd-us/components/page/page-navigation";

    @Override
    public List<String> processResourceEvent(final Event event) {
        final String path = event.getProperty(SlingConstants.PROPERTY_PATH).toString();
        final List<String> changedAttributes = Arrays.asList((String[]) event.getProperty(SlingConstants.PROPERTY_CHANGED_ATTRIBUTES));
        final String resourceType = event.getProperty(SlingConstants.PROPERTY_RESOURCE_TYPE).toString();
        List<String> flushPaths = new ArrayList<>();

        boolean isRootPath = ROOT_SITE_PATTERN.matcher(path).matches();

        if (changedAttributes.contains(JCR_TITLE) && resourceType.equals(PAGE_NAV_RESOURCE_TYPE) && isRootPath) {
            LOG.debug("jcr title property was modified on a page nav resource --> clear the tree = path " + path);
            final Matcher matcher = ROOT_SITE_PATTERN.matcher(path);
            if (matcher.find()) {
                final String country = matcher.group(GROUP_COUNTRY);
                final String language = matcher.group(GROUP_LANGUAGE);
                flushPaths.add(String.format(SITE_TEMPLATE, country, language));
            }
            LOG.info("sending paths to flush: " + ArrayUtils.toString(flushPaths.toArray()));
        }

        return flushPaths;
    }

    @Override
    public String getInvalidatorServiceId() {
        return SERVICE_NAME;
    }

}
