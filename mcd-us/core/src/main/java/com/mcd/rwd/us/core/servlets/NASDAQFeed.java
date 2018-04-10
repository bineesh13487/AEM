package com.mcd.rwd.us.core.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@SlingServlet(paths = { "/services/mcd/NASDAQFeed" }, methods = { "GET" }, extensions = "json")

@Properties(value = {
		@Property(name = "service.pid", value = "com.mcd.rwd.us.core.servlets.NASDAQFeed", propertyPrivate = false),
		@Property(name = "service.description", value = "Provides server call to pull in this JSON From NASDAQ ", propertyPrivate = false),
		@Property(name = "service.vendor", value = "McD", propertyPrivate = false) })

public class NASDAQFeed extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = -4144228449737038698L;

	private static final String NASDAQ_CLIENT_URL = "https://clientapi.gcs-web.com/data/";
	private static final String NASDAQ_ACCESS_ID = "e6dcefce-c02d-4ae4-9c99-88022996a098";
	private static final String CONTENT_PATH = "/News?";
	private static final String QUERY_PRAMETERS = "category=Our%20People%7CWhole%20New%20Experience%7CUsing%20Our%20Scale%20for%20Good%7CMedia%20Statements%7CAbout%20Our%20Food";

	private final transient Logger logger = LoggerFactory.getLogger(getClass());

	@Reference
	ResourceResolverFactory resolverFactory;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		String jsonData;
		CloseableHttpClient httpClient =null;
		logger.debug(" NASDAQFeed Servlet Called");	
		
		try {

			httpClient = HttpClientBuilder.create().build();
			String URL = NASDAQ_CLIENT_URL + NASDAQ_ACCESS_ID + CONTENT_PATH + QUERY_PRAMETERS;
			HttpGet httpGet = new HttpGet(URL);
			httpGet.addHeader("Accept", "application/json");
			HttpResponse httpResp = httpClient.execute(httpGet);

			if (httpResp.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + httpResp.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(httpResp.getEntity().getContent(), "UTF-8"));
			if ((jsonData = br.readLine()) != null) {

				Gson gson = new Gson();
				JsonObject jobj = gson.fromJson(jsonData, JsonObject.class);
				logger.debug(" JSON RETURNED :::: " + jobj);
				response.setContentType("application/json");
				response.getWriter().print(jobj);

			}
		} catch (Exception e) {
			

		} finally {
			if (httpClient != null) {				
				httpClient.close();
			}
		}

	}

}
