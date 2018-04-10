package com.mcd.rwd.us.core.service.impl;

import com.adobe.granite.xss.XSSAPI;
import com.google.gson.Gson;
import com.mcd.rwd.us.core.bean.search.SearchResultBean;
import com.mcd.rwd.us.core.constants.ApplicationConstants;
import com.mcd.rwd.us.core.constants.SearchConstants;
import com.mcd.rwd.us.core.service.McdFactoryConfig;
import com.mcd.rwd.us.core.service.McdFactoryConfigConsumer;
import com.mcd.rwd.us.core.service.SolrSearchService;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Seema Pandey on 18-04-2016.
 */
@Component(immediate = true, metatype = false, label = "Solr Search")
@Service
public class SolrSearchServiceImpl implements SolrSearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolrSearchServiceImpl.class);

    @Reference
    private McdFactoryConfigConsumer configConsumer;

    @Reference
    private XSSAPI xssapi;

    /**
     * @param request
     * @return
     * @throws IOException
     */
    @Override
    public final SearchResultBean getSearchResults(String resourceCountryCode, String resourceLanguageCode, final SlingHttpServletRequest request) throws IOException {

        InputStreamReader inputStreamReader = null;
        SearchResultBean resultBean = null;

        try {
            String text = xssapi.filterHTML(request.getParameter(SearchConstants.REQ_PARAM_QUERY_TXT));
            if (StringUtils.isNotBlank(text)) {
                String searchType = SearchConstants.SEARCH_TYPE_SIMPLE;

                if (SearchConstants.SEARCH_TYPE_SIMPLE.equals(searchType)) {
                    Map<String, Object> params = getSearchParameters(request, Integer.parseInt(request.getParameter(SearchConstants.NUM_OF_RESULTS_PER_PAGE)));

                    StringBuilder query = fetchServiceUrl(resourceCountryCode, resourceLanguageCode, "/select");

                    query.append(doQuery(params).toString());

                    URL collectionServer = new URL(query.toString());
                    URLConnection con = collectionServer.openConnection();

                    inputStreamReader = new InputStreamReader(con.getInputStream(),
                            SearchConstants.POST_CHARSET_NAME);
                    resultBean = new Gson().fromJson(inputStreamReader, SearchResultBean.class);
                    inputStreamReader.close();
                }
            }
        } catch (UnsupportedEncodingException use) {
            LOGGER.error(use.getMessage(), use);
        } catch (IOException ioe) {
            LOGGER.error(ioe.getMessage(), ioe);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
        }

        return resultBean;
    }

    /**
     * @param request
     * @return
     */
    private Map<String, Object> getSearchParameters(final SlingHttpServletRequest request, int limit) {
        Map<String, Object> params = new HashMap<String, Object>();
        String pageType = request.getParameter(SearchConstants.REQ_PARAM_PAGETYPE);
        String searchText = xssapi.filterHTML(request.getParameter(SearchConstants.REQ_PARAM_QUERY_TXT));
        StringBuilder searchQuery = new StringBuilder();
        searchQuery.append("((description:");
        searchQuery.append(searchText);
        searchQuery.append(")^5OR(keywords:");
        searchQuery.append(searchText);
        searchQuery.append(")^10OR(title:");
        searchQuery.append(searchText);
        searchQuery.append(")^100OR(content:");
        searchQuery.append(searchText);
        searchQuery.append(")^0.5OR(url:");
        searchQuery.append(searchText);
        searchQuery.append(")^50)");
        if (pageType != null && !("".equals(pageType)) && !("All".equals(pageType)) && !("undefined".equals(pageType))) {
            searchQuery.append(" AND pagetype:'");
            searchQuery.append(pageType.trim());
            searchQuery.append("'");
        }
        params.put(SearchConstants.REQ_PARAM_QUERY_TXT, searchQuery);
        params.put(SearchConstants.IS_SIMPLE_SEARCH, true);
        params.put(SearchConstants.IS_SEARCH_WITHIN_SEARCH, false);

        int start = 0;

        if (StringUtils.isNotBlank(request.getParameter(SearchConstants.REQ_PARAM_PAGE))) {
            try {
                start = Integer.parseInt(request.getParameter(SearchConstants.REQ_PARAM_PAGE));
            } catch (NumberFormatException nfe) {
                LOGGER.error("Invalid Search Result Page requested.");
            }
        }

        params.put(SearchConstants.START_INDEX, start * limit);
        params.put(SearchConstants.NUM_OF_RESULTS_PER_PAGE, limit);


        return params;
    }

    /**
     * @param params
     * @return
     */
    @Override
    public final StringBuilder doQuery(final Map<String, Object> params) throws UnsupportedEncodingException {

        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(encodeQuery(params.get(SearchConstants.REQ_PARAM_QUERY_TXT).toString()));
        solrQuery.append("&hl=true&");
        solrQuery.append("hl.fragsize=0&hl.fl=description&hl.fl=title&hl.snippets=1&hl.simple.pre=");
        solrQuery.append(SearchConstants.SOLR_HIGHLIGHT_SIMPLE_PRE);
        solrQuery.append("&hl.simple.post=").append(SearchConstants.SOLR_HIGHLIGHT_SIMPLE_POST);
        solrQuery.append("&start=");
        solrQuery.append(params.get(SearchConstants.START_INDEX));
        solrQuery.append("&rows=");
        solrQuery.append(params.get(SearchConstants.NUM_OF_RESULTS_PER_PAGE));
        solrQuery.append("&fl=productnutrition%2Cthumbnail%2Cdefaultimagepath%2Ckeywords%2Ctitle%2Ctstamp%2cdescription%2cid%2curl&enableElevation=true&");
        solrQuery.append("exclusive=true&wt=json");
        LOGGER.info("SOLR query:" + solrQuery);

        return solrQuery;
    }


    private StringBuilder fetchServiceUrl(String country, String locale, String servlet) {
        LOGGER.info(" In SolrSearchServiceImpl  Resource Country Code is" + country
                + "Resource Language Code is" + locale);
        McdFactoryConfig mcdFactoryConfig = configConsumer.getMcdFactoryConfig(country, locale);
        StringBuilder serviceURL = new StringBuilder();

        if (mcdFactoryConfig != null) {
            serviceURL.append(mcdFactoryConfig.getSolrCollection());
            if (serviceURL.toString().endsWith("/")) {
                serviceURL.deleteCharAt(serviceURL.length() - 1);
            }
            serviceURL.append(servlet);
            serviceURL.append(ApplicationConstants.URL_QS_START_CHAR);
            serviceURL.append(SearchConstants.REQ_PARAM_QUERY_TXT);
            serviceURL.append(ApplicationConstants.PN_EQUALS);
        }
        return serviceURL;
    }

    private String encodeQuery(String q) throws UnsupportedEncodingException {
        return URLEncoder.encode(q, "ISO-8859-1").replaceAll("\\+", "%20");
    }
}
