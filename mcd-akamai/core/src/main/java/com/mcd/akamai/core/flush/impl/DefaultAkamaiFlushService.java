package com.mcd.akamai.core.flush.impl;

import com.adobe.granite.crypto.CryptoException;
import com.adobe.granite.crypto.CryptoSupport;
import com.day.cq.replication.Agent;
import com.day.cq.replication.AgentConfig;
import com.day.cq.replication.AgentManager;
import com.mcd.akamai.core.exception.AkamaiFlushException;
import com.mcd.akamai.core.flush.AkamaiFlushRequest;
import com.mcd.akamai.core.flush.AkamaiFlushService;
import org.apache.commons.codec.CharEncoding;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.jackrabbit.util.Base64;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Service
@Component(immediate = true)
public class DefaultAkamaiFlushService implements AkamaiFlushService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultAkamaiFlushService.class);

    /** Protocol for replication agent transport URI that triggers this transport handler. */
    private final static String AKAMAI_PROTOCOL = "akamai://";

    /** Akamai CCU REST API URL */
    private final static String AKAMAI_CCU_REST_API_URL_TPL = "https://api.ccu.akamai.com/ccu/v3/%s/url/%s";

    /** Replication agent domain property name. Valid values are "staging" and "production". */
    private final static String PROPERTY_DOMAIN = "akamaiDomain";

    /** Replication agent action property name. Valid values are "remove" and "invalidate". */
    private final static String PROPERTY_ACTION = "akamaiAction";

    /** Replication agent default domain value */
    private final static String PROPERTY_DOMAIN_DEFAULT = "production";

    /** Replication agent default action value */
    private final static String PROPERTY_ACTION_DEFAULT = "delete";

    @Reference
    private AgentManager agentManager;

    @Reference
    private CryptoSupport cryptoSupport;


    @Override
    public void flush(final AkamaiFlushRequest request) throws AkamaiFlushException {
        doActivate(request);
    }

    @Override
    public void test(final AkamaiFlushRequest request) throws AkamaiFlushException {
        doTest(request.getUser(), request.getPassword());
    }

    @Override
    public void populateAgentConfigParameters(AgentConfig config, AkamaiFlushRequest request) {
        request.setUser(config.getTransportUser());
        final String password = config.getTransportPassword();
        request.setPassword(password);
        if (cryptoSupport.isProtected(password)) {
            try {
                request.setPassword(cryptoSupport.unprotect(password));
            } catch (CryptoException e) {
                LOG.error("Error decrypting akamai transport password", e);
            }
        }
        final ValueMap props = config.getProperties();
        request.setDomain(props.get(PROPERTY_DOMAIN, PROPERTY_DOMAIN_DEFAULT));
        request.setAction(props.get(PROPERTY_ACTION, PROPERTY_ACTION_DEFAULT));
    }

    @Override
    public void populateDefaultAgentConfigParameters(AkamaiFlushRequest request) {
        if (request.getUser() == null || request.getPassword() == null) {
            for (Agent agent : agentManager.getAgents().values()) {
                final AgentConfig config = agent.getConfiguration();
                final String transportURI = config.getTransportURI();
                if (transportURI != null && transportURI.toLowerCase().startsWith(AKAMAI_PROTOCOL)
                        && agent.isEnabled()) {
                    populateAgentConfigParameters(config, request);
                    return;
                }
            }
        }
    }

    private void doTest(final String user, final String password) throws AkamaiFlushException {
        final String url = String.format(AKAMAI_CCU_REST_API_URL_TPL, PROPERTY_ACTION_DEFAULT, PROPERTY_DOMAIN_DEFAULT);
        final HttpGet request = new HttpGet(url);
        final HttpResponse response = sendRequest(request, user, password);

        if (response != null) {
            final int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                throw new AkamaiFlushException("Error testing Akamai Flush: " + response.toString());
            }
        }
    }

    private void doActivate(final AkamaiFlushRequest akamaiFlushRequest) throws AkamaiFlushException {

        final String url = String.format(AKAMAI_CCU_REST_API_URL_TPL, akamaiFlushRequest.getAction(),
                akamaiFlushRequest.getDomain());
        final HttpPost request = new HttpPost(url);

        createPostBody(request, akamaiFlushRequest);

        final HttpResponse response =
                sendRequest(request, akamaiFlushRequest.getUser(), akamaiFlushRequest.getPassword());

        if (response != null) {
            final int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_CREATED) {
                return;
            }
        }

        throw new AkamaiFlushException("Replication failed");
    }

    private <T extends HttpRequestBase> HttpResponse sendRequest(final T request, final String user,
                                                                 final String password) throws AkamaiFlushException {

        final String auth = user + ":" + password;
        final String encodedAuth = Base64.encode(auth);

        request.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + encodedAuth);
        request.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());

        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response;

        try {
            response = client.execute(request);
        } catch (IOException e) {
            throw new AkamaiFlushException("Could not send replication request.", e);
        }

        return response;
    }

    private void createPostBody(final HttpPost request, final AkamaiFlushRequest akamaiFlushRequest)
            throws AkamaiFlushException {

        /*
         * Get list of CP codes or ARLs/URLs depending on agent setting
         */
        final JSONArray purgeObjects = new JSONArray(akamaiFlushRequest.getArls());


        final JSONObject json = new JSONObject();
        if (purgeObjects.length() > 0) {
            try {
                json.put("objects", purgeObjects);
            } catch (JSONException e) {
                throw new AkamaiFlushException("Could not build purge request content", e);
            }

            final StringEntity entity = new StringEntity(json.toString(), CharEncoding.ISO_8859_1);
            request.setEntity(entity);

        } else {
            throw new AkamaiFlushException("No pages to purge");
        }
    }
}
