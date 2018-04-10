package com.mcd.rwd.newsroom.core.servlets;

import com.mcd.rwd.newsroom.core.models.ArticleDetailBean;
import com.mcd.rwd.newsroom.core.service.ArticleSearchService;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import com.google.gson.Gson;
import com.mcd.rwd.us.core.constants.ApplicationConstants;
import com.mcd.rwd.us.core.service.SolrSearchService;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@SlingServlet(paths = { "/services/mcd/articlelisting" }, methods = { "GET" }, extensions = "json")
@Properties(value = {
		@Property(name = "service.pid", value = "ArticleDetailServlet", propertyPrivate = false),
		@Property(name = "service.description", value = "Provides article details in json format (key-value)", propertyPrivate = false),
		@Property(name = "service.vendor", value = "HCL Technologies", propertyPrivate = false) })
public class ArticleDetailServlet extends SlingSafeMethodsServlet {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleDetailServlet.class);

	@Reference
	ArticleSearchService articleService;

	@Reference
	private transient SolrSearchService search;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		LOGGER.info("In ArticleDetailServlet Servlet...... ");
		Map<String, List<ArticleDetailBean>> map = new HashMap<String, List<ArticleDetailBean>>();
		response.setContentType(ApplicationConstants.CONTENT_TYPE_JSON);

		try {
			PrintWriter out = response.getWriter();
			String country = request.getParameter("country");
			String language = request.getParameter("language");
			String newsType = request.getParameter("newsType");
			String newsFolderPath = request.getParameter("newsFolderPath");
			String limit = request.getParameter("limit");
			
			if (articleService != null) {
				List<ArticleDetailBean> articleDetailBeans = articleService.getArticleResults(newsType, newsFolderPath,
						country, language,limit);

				map.put("articles", articleDetailBeans);

				String searchResults = new Gson().toJson(map);
				/*Map<String, Long> map1 = new HashMap<String, Long>();
				if(articleDetailBeans.size()>=1){
					map1.put("totalMatches", articleDetailBeans.get(0).getTotalMatches());
					String totalMatches = new Gson().toJson(map1);
				}*/
				try {
					response.getWriter().print(searchResults);
					//response.getWriter().print(totalMatches);
				} catch (IOException ioe) {
					LOGGER.error("Exception in GoesWellWithList while writing response", ioe);
				}

			} else {
				LOGGER.debug("Search Result Response is null.");
				response.getWriter().print("[]");
			}
		} catch (IOException ioe) {
			LOGGER.error(ioe.getMessage(), ioe);
		}
	}

}
