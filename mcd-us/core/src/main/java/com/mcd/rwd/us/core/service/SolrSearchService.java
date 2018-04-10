package com.mcd.rwd.us.core.service;

import com.mcd.rwd.us.core.bean.search.SearchResultBean;
import org.apache.sling.api.SlingHttpServletRequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Seema Pandey on 10-05-2016.
 */
public interface SolrSearchService {

    SearchResultBean getSearchResults(String resourceCountryCode, String resourceLanguageCode, SlingHttpServletRequest request)
            throws IOException;

    StringBuilder doQuery(Map<String, Object> params) throws UnsupportedEncodingException;

}
