package com.mcd.rwd.us.core.servlets;

import com.google.gson.Gson;
import com.mcd.rwd.us.core.bean.search.*;
import com.mcd.rwd.us.core.constants.ApplicationConstants;
import com.mcd.rwd.us.core.service.SolrSearchService;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by seema pandey on 6/03/2016.
 */

@SlingServlet(paths = "/services/mcd/searchResults", methods = "GET", extensions = "json")
@Properties({
		@Property(name = "service.pid", value = "com.mcd.rwd.us.core.servlets.SearchServlet", propertyPrivate = false),
		@Property(name = "service.description", value = "Provides the list of possible search pages for the searched text", propertyPrivate = false),
		@Property(name = "service.vendor", value = "HCL Technologies", propertyPrivate = false)
})
public class SearchServlet extends SlingSafeMethodsServlet {

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchServlet.class);

	@Reference
	private transient SolrSearchService search;

	private String doNotShowPage;

	private boolean isHiddenPageConfigured=false;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType(ApplicationConstants.CONTENT_TYPE_JSON);
		try {
			PrintWriter out = response.getWriter();
			String resourceCountryCode = request.getParameter("country");
			String resourceLanguageCode = request.getParameter("language");
			boolean isPaginated;
			long totalHits;
			if (search != null) {
				SearchResultBean result = search.getSearchResults(resourceCountryCode, resourceLanguageCode, request);
				if (result != null) {
					doNotShowPage=request.getParameter("hidePage");
					isPaginated = result.getResponse().getStart() > 0;
					//totalHits = result.getResponse().getNumFound();
					SearchResults finalResults = new SearchResults();
					finalResults.setHits(populateResults(result));
					finalResults.setPaginated(isPaginated);
					//finalResults.setTotalHits(totalHits);
					finalResults.setTotalHits(countTotalHits(result,isHiddenPageConfigured));
					String searchResults = new Gson().toJson(finalResults);
					LOGGER.debug("Search Result Response {}", searchResults);
					out.print(searchResults);
				} else {
					LOGGER.debug("Search Result Response is null.");
					out.print("[]");
				}
			} else {
				LOGGER.debug("Search Result Response is null.");
				out.print("[]");
			}
		} catch (IOException ioe) {
			LOGGER.error(ioe.getMessage(), ioe);
		}
	}
	private List<Hit> populateResults(final SearchResultBean result) {
		LOGGER.debug("In populateResults method to create response JSON from solr response..");
		List<Hit> hits = new ArrayList<Hit>();
		if (!(result.getResponse().getDocs().isEmpty())) {
			Iterator<Document> docs = result.getResponse().getDocs().iterator();
			Map<String, Match> highlighting = result.getHighlighting();

			while (docs.hasNext()) {
				Document doc = docs.next();
				if(StringUtils.isNotEmpty(doNotShowPage)){
					if(!(doc.getUrl().contains(doNotShowPage))){
						Hit hit = new Hit();
						hit.setDate(doc.getTimestamp());
						hit.setUrl(doc.getUrl());
						hit.setProductNutrition(doc.getProductNutrition());
						hit.setThumbnail(doc.getThumbnail());
						hit.setDefaultImagePath(doc.getDefaultImagePath());
						hit.setTitleForAlt(doc.getTitle());
						hit.setDescriptionAria(doc.getDescription() != null && !(doc.getDescription().isEmpty())? doc.getDescription().replaceAll("\\<.*?>",""):doc.getDescription());
						//hit.setDescriptionAria(doc.getDescription().replaceAll("\\<.*?>",""));
						Match match = highlighting.get(doc.getId());
						hit.setTitle(match != null && match.getTitle() != null && !(match.getTitle().isEmpty()) ? match.getTitle().get(0) : doc.getTitle());
						hit.setDescription(match != null && match.getDescription() != null && !(match.getDescription().isEmpty()) ? StringEscapeUtils.unescapeHtml(match.getDescription().get(0)) : StringEscapeUtils.unescapeHtml(doc.getDescription()));
						hits.add(hit);
					}else{
						isHiddenPageConfigured=true;
					}
				}else{
					Hit hit = new Hit();
					hit.setDate(doc.getTimestamp());
					hit.setUrl(doc.getUrl());
					hit.setProductNutrition(doc.getProductNutrition());
					hit.setThumbnail(doc.getThumbnail());
					hit.setDefaultImagePath(doc.getDefaultImagePath());
					hit.setTitleForAlt(doc.getTitle());
					//hit.setDescriptionAria(doc.getDescription().replaceAll("\\<.*?>",""));
					hit.setDescriptionAria(doc.getDescription() != null && !(doc.getDescription().isEmpty())? doc.getDescription().replaceAll("\\<.*?>",""):doc.getDescription());
					Match match = highlighting.get(doc.getId());
					hit.setTitle(match != null && match.getTitle() != null && !(match.getTitle().isEmpty()) ? match.getTitle().get(0) : doc.getTitle());
					hit.setDescription(match != null && match.getDescription() != null && !(match.getDescription().isEmpty()) ? StringEscapeUtils.unescapeHtml(match.getDescription().get(0)) : StringEscapeUtils.unescapeHtml(doc.getDescription()));
					hits.add(hit);
				}
			}
		}
		return hits;
	}

	private long countTotalHits(SearchResultBean result,boolean isHiddenPage){
		long totalHits;
		if(isHiddenPage){
			totalHits=result.getResponse().getNumFound()-1;
		}else{
			totalHits=result.getResponse().getNumFound();
		}
		return totalHits;
	}
}
