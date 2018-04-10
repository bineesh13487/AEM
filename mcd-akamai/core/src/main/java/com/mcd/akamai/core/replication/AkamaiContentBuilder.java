package com.mcd.akamai.core.replication;

import com.day.cq.replication.*;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.mcd.akamai.core.externalize.AkamaiExternalizer;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.jcr.resource.JcrResourceConstants;

import javax.jcr.Session;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Akamai content builder to create replication content containing a JSON array
 * of URLs for Akamai to purge through the Akamai Transport Handler. This class
 * takes the internal resource path and converts it to external URLs as well as
 * adding vanity URLs and pages that may Sling include the activated resource.
 */
@Component(metatype=false)
@Service(ContentBuilder.class)
@Property(name="name", value="akamai")
public class AkamaiContentBuilder implements ContentBuilder {

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private AkamaiExternalizer externalizer;

    /** The name of the replication agent */
    public static final String NAME = "akamai";

    /**
     * The serialization type as it will display in the replication
     * agent edit dialog selection field.
     */
    public static final String TITLE = "Akamai Purge Agent";

    /**
     * {@inheritDoc}
     */
    @Override
    public ReplicationContent create(Session session, ReplicationAction action,
            ReplicationContentFactory factory) throws ReplicationException {
        return create(session, action, factory, null);
    }

    /**
     * Create the replication content containing the public facing URLs for
     * Akamai to purge.
     */
    @Override
    public ReplicationContent create(Session session, ReplicationAction action,
            ReplicationContentFactory factory, Map<String, Object> parameters)
            throws ReplicationException {

        String path = action.getPath();
        ReplicationLog log = action.getLog();

        ResourceResolver resolver = null;
        PageManager pageManager = null;
        JSONArray jsonArray = new JSONArray();

        if (StringUtils.isNotBlank(path)) {
            try {
                HashMap<String, Object> sessionMap = new HashMap<>();
                sessionMap.put(JcrResourceConstants.AUTHENTICATION_INFO_SESSION, session);
                resolver = resolverFactory.getResourceResolver(sessionMap);

                if (resolver != null) {
                    pageManager = resolver.adaptTo(PageManager.class);
                }
            } catch (LoginException e) {
                log.error("Could not retrieve Page Manager", e);
            }

            if (pageManager != null) {
                Page purgedPage = pageManager.getPage(path);

                /*
                 * Get the external URL if the resource is a page. Otherwise, use the
                 * provided resource path.
                 */
                if (purgedPage != null) {

                    externalizer.mapPath(purgedPage.getPath()).forEach(url -> jsonArray.put(url + ".html"));

                    /*
                     * Add page's vanity URL if it exists.
                     */
                    String vanityUrl = purgedPage.getVanityUrl();

                    if (StringUtils.isNotBlank(vanityUrl)) {
                        jsonArray.put(vanityUrl);
                        log.info("Vanity URL added: " + vanityUrl);
                    }

                } else {
                    externalizer.mapPath(path).forEach(url -> jsonArray.put(url));
                    log.info("Resource path added: " + path);
                }

                return createContent(factory, jsonArray);
            }
        }

        return ReplicationContent.VOID;
    }

    /**
     * Create the replication content containing 
     *
     * @param factory Factory to create replication content
     * @param jsonArray JSON array of URLS to include in replication content
     * @return replication content
     *
     * @throws ReplicationException if an error occurs
     */
    private ReplicationContent createContent(final ReplicationContentFactory factory,
            final JSONArray jsonArray) throws ReplicationException {

        Path tempFile;

        try {
            tempFile = Files.createTempFile("akamai_purge_agent", ".tmp");
        } catch (IOException e) {
            throw new ReplicationException("Could not create temporary file", e);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(tempFile, Charset.forName("UTF-8"))) {
            writer.write(jsonArray.toString());
            writer.flush();

            return factory.create("text/plain", tempFile.toFile(), true);
        } catch (IOException e) {
            throw new ReplicationException("Could not write to temporary file", e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return {@value #NAME}
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@value #TITLE}
     */
    @Override
    public String getTitle() {
        return TITLE;
    }
}
