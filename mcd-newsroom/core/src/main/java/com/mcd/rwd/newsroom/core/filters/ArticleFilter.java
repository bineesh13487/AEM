package com.mcd.rwd.newsroom.core.filters;


import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.sling.SlingFilter;
import org.apache.felix.scr.annotations.sling.SlingFilterScope;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestDispatcherOptions;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.cq.wcm.api.Page;
import javax.servlet.*;
import java.io.IOException;

@SlingFilter(scope = SlingFilterScope.REQUEST, generateService = true, order = -2501)
public final class ArticleFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleFilter.class);

	private static final int ERR_STATUS = 404;

	private static final String TEMPLATE_PATH = "/apps/mcd-newsroom/templates/articledetailconfig";


	/**
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		// Do Nothing
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@Override
	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
						 final FilterChain chain) throws IOException, ServletException {

		if (!(servletRequest instanceof SlingHttpServletRequest)) {

			chain.doFilter(servletRequest, servletResponse);
			return;
		}

		SlingHttpServletRequest request = (SlingHttpServletRequest) servletRequest;
		SlingHttpServletResponse response = (SlingHttpServletResponse) servletResponse;
		response.addHeader("Access-control-Allow-Origin", "*");
		response.addHeader("Access-control-Allow-Methods", "GET, POST, OPTIONS");
		response.addHeader("Access-Control-Allow-Headers",
				"X-PINGOTHER,Origin, X-Requested-With, Content-Type, Accept");
		Resource resource = request.getResource();
		Page page = resource.adaptTo(Page.class);
		if (isArticleDetailPage(page) && containsArticleName(request, page)) {
			String articleNodeName=getArticleNodeName(request);

			if(StringUtils.isNotBlank(articleNodeName)){
				RequestDispatcherOptions options = new RequestDispatcherOptions();
				options.setReplaceSelectors(articleNodeName);
				request.getRequestDispatcher(request.getResource(), options).include(request, response);
			}

			return;
		}

		chain.doFilter(servletRequest, servletResponse);
	}

	/**
	 * @see Filter#destroy()
	 */
	@Override
	public void destroy() {
		// Do Nothing
	}

	public  boolean isArticleDetailPage(Page page) {
		return page != null && page.getTemplate() != null && TEMPLATE_PATH.equals(page.getTemplate().getPath());
	}

	public  boolean containsArticleName(SlingHttpServletRequest request, Page page) {
		String[] selectors = request.getRequestPathInfo().getSelectors();
		String pathInfo = request.getRequestURI();
		return (selectors == null || selectors.length == 0) && !pathInfo.contains('/' + page.getName() + ".html");
	}


	private String getArticleNodeName(SlingHttpServletRequest request)
			throws IOException, ServletException {
		String pathInfo = request.getRequestURI();
		String articleName = pathInfo.substring(pathInfo.lastIndexOf('/') + 1, pathInfo.indexOf(".html"));
		return articleName;
	}
}