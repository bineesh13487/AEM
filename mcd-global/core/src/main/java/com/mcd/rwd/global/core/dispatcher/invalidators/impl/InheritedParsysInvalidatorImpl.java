package com.mcd.rwd.global.core.dispatcher.invalidators.impl;

import com.day.cq.commons.jcr.JcrConstants;
import com.mcd.rwd.global.core.dispatcher.invalidators.Invalidator;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.List;

@Component(
        label = "Inherited Parsys Content Invalidator",
        description = "looks for changes to ipar's and forwards paths to a dispatcher flusher",
        configurationFactory = true)
@Properties({
        @Property(
                label = "Service Name",
                name = Invalidator.PROP_NAME,
                description = "uniquely identify the service impl by name.",
                value = InheritedParsysInvalidatorImpl.SERVICE_NAME
        )
})
@Service
public class InheritedParsysInvalidatorImpl implements Invalidator {

    public static final String SERVICE_NAME = "inheritedParsysInvalidator";
    private static final Logger LOG = LoggerFactory.getLogger(InheritedParsysInvalidatorImpl.class);
    private static final long ONE_MINUTE = 60000L;
    private static final String IPAR_NODE = "ipar";

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    public List<String> processResourceEvent(final Event event) {
        final String path = event.getProperty(SlingConstants.PROPERTY_PATH).toString();
        List<String> flushPaths = new ArrayList<>();

        // for root site level -> look under the content for each page for changes to ipar content
        boolean isRootPath = ROOT_SITE_JCR_CONTENT_PATTERN.matcher(path).matches();
        if (isRootPath) {
            flushPaths.addAll(checkForIparChanges(path));
        }

        return flushPaths;
    }

    private List<String> checkForIparChanges(String path) {
        List<String> flushPaths = new ArrayList<>();
        ResourceResolver resolver = null;

        try {
            resolver = resolverFactory.getAdministrativeResourceResolver(null);
            Resource resource = resolver.resolve(path);
            Node homepageNode = resource.adaptTo(Node.class);
            if (homepageNode.hasNode(IPAR_NODE)) {
                NodeIterator iparNodes = homepageNode.getNode(IPAR_NODE).getNodes();
                while (iparNodes.hasNext()) {
                    Node node = iparNodes.nextNode();
                    Long lastModified = node.getProperty(JcrConstants.JCR_LASTMODIFIED).getDate().getTimeInMillis();
                    if ((System.currentTimeMillis() - lastModified) < ONE_MINUTE) {
                        LOG.debug(node.getPath() + " was modified in the last minute - flush the homepage path");
                        flushPaths.add(StringUtils.strip(path, "jcr:content"));
                    }
                }
            }
        } catch (LoginException | RepositoryException e) {
            LOG.error("Error occurred clearing mapped dispatcher paths for ipar(s):", e);
        } finally {
            if (resolver != null && resolver.isLive()) {
                resolver.close();
            }
        }
        return flushPaths;
    }

    @Override
    public String getInvalidatorServiceId() {
        return SERVICE_NAME;
    }

}
