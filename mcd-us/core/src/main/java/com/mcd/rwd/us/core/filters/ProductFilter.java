package com.mcd.rwd.us.core.filters;

import com.day.cq.wcm.api.Page;
import com.google.gson.Gson;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.utils.ConnectionUtil;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.us.core.bean.product.ShortURLItem;
import com.mcd.rwd.us.core.bean.product.ShortURLResponse;
import com.mcd.rwd.us.core.service.McdFactoryConfig;
import com.mcd.rwd.us.core.service.McdFactoryConfigConsumer;
import com.mcd.rwd.us.core.service.McdWebServicesConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingFilter;
import org.apache.felix.scr.annotations.sling.SlingFilterScope;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestDispatcherOptions;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Rakesh.Balaiah on 23-05-2016.
 */
@SlingFilter(scope = SlingFilterScope.REQUEST, generateService = true, order = -2501)
public final class ProductFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductFilter.class);

	private static final int ERR_STATUS = 404;

	private static final String TEMPLATE_PATH = "/apps/mcd-us/templates/product-detail";

	@Reference
	private transient McdWebServicesConfig mcdWebServicesConfig;

	@Reference
	private transient McdFactoryConfigConsumer configConsumer;

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
		if (PageUtil.isProductPage(page) && PageUtil.containsShortURL(request, page)) {
			String productId = processProductPageURL(request, page);
			if (StringUtils.isBlank(productId)) {
				if (response.isCommitted()) {
					LOGGER.error("Response is already Committed !! Product Unavailable {}", request.getRequestURI());
				} else {
					response.setStatus(ERR_STATUS);
					response.sendError(ERR_STATUS);
				}
			} else {
				RequestDispatcherOptions options = new RequestDispatcherOptions();
				options.setReplaceSelectors(productId);
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

	private String processProductPageURL(SlingHttpServletRequest request, Page page)
			throws IOException, ServletException {
		String pathInfo = request.getRequestURI();
		String productName = pathInfo.substring(pathInfo.lastIndexOf('/') + 1, pathInfo.indexOf(".html"));
		String productId = getProductId(getServiceURL(PageUtil.getCountry(page), PageUtil.getLanguage(page)),
				productName);
		LOGGER.error("Testing for GWS processProductPageURL product id " + productId);
		LOGGER.debug("Received product id {} for the short-url {}", productId, productName);
		return productId;
	}

	private String getProductId(String url, String name) {
		String productId = null;
		if (StringUtils.isNotBlank(url)) {
			InputStreamReader inputStreamReader = null;
			try {
				ConnectionUtil connectionUtil = new ConnectionUtil();

				LOGGER.debug("Requesting DNA Short URL service {}", url);

				inputStreamReader = new InputStreamReader(connectionUtil.makeRequest(url), "UTF-8");
				ShortURLResponse response = new Gson().fromJson(inputStreamReader, ShortURLResponse.class);

				productId = findProductIdInResponse(response, name);
			} catch (IOException ioe) {
				LOGGER.error("IO Exception in Product Filter", ioe);
			} finally {
				if (inputStreamReader != null) {
					try {
						inputStreamReader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						LOGGER.error("IO Exception in Product Filter", e);
					}
				}
			}
		}

		return productId;
	}

	private String getServiceURL(String country, String language) {
		if (StringUtils.isNotBlank(country) && StringUtils.isNotBlank(language)) {
			McdFactoryConfig mcdFactoryConfig = configConsumer.getMcdFactoryConfig(country, language);
			if (mcdFactoryConfig != null) {
				StringBuilder serviceURL = new StringBuilder(mcdWebServicesConfig.getDomain())
						.append(mcdWebServicesConfig.getShortNameUrl()).append(ApplicationConstants.QS_START_CHAR)
						.append("country=").append(mcdFactoryConfig.getDnaCountryCode())
						.append(ApplicationConstants.QS_DELIMITER).append("language=")
						.append(mcdFactoryConfig.getDnaLanguageCode()).append(ApplicationConstants.QS_DELIMITER)
						.append("showLiveData=true");
				return serviceURL.toString();

			}
		}
		return null;
	}

	private String findProductIdInResponse(ShortURLResponse response, String name) {
		if (response != null) {
			List<ShortURLItem> list = response.getItems().getItem();
			Iterator<ShortURLItem> iterator = list.iterator();

			while (iterator.hasNext()) {
				ShortURLItem info = iterator.next();

				if (name.equals(info.getShortName())) {
					return info.getMenuItemNo();
				}
			}
		}
		return null;
	}
}