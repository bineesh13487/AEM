package com.mcd.rwd.us.core.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mcd.rwd.us.core.service.McdWebServicesConfig;
import com.mcd.rwd.us.core.utils.McdCommonService;



import java.io.*;

@SlingServlet(paths = { "/services/coke-filters" }, methods = { "GET" }, extensions = "json")
@Properties(value = {
		@Property(name = "service.pid", value = "com.mcd.rwd.us.core.servlets.CokeFiltersListServlet", propertyPrivate = false),
		@Property(name = "service.description", value = "Provides category /flavor in json format from coke-Api (key-value)", propertyPrivate = false),
		@Property(name = "service.vendor", value = "McD", propertyPrivate = false) })
public class CokeFiltersListServlet extends SlingSafeMethodsServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** default logger. */
	private final transient Logger logger = LoggerFactory
			.getLogger(CokeFiltersListServlet.class);

	/** The resolver factory. */
	@Reference
	private transient ResourceResolverFactory resolverFactory;

	/** The mcd common service. */
	@Reference
	private transient McdCommonService mcdCommonService;

	/** The mcd web services config. */
	@Reference
	private transient McdWebServicesConfig mcdWebServicesConfig;

	/** The session. */

	private static final String COKE_DATA = "cokedata";
	private static final String APPLICATION_JSON = "application/json";
	private static final String JCR_DATA = "jcr:data";
	private static final String COKE_JSON = "cokeFilters.json";

	@Override
	protected void doGet(SlingHttpServletRequest request,
			SlingHttpServletResponse response) throws ServletException,
			IOException {
		ResourceResolver resourceResolver = request.getResourceResolver();
		Resource resource;
		String currentPagePath = request.getParameter("pagePath");
		String compPath = currentPagePath;
		resource = resourceResolver.getResource(compPath);
		
		String id = null;
		String locale = null;
		String resPath = currentPagePath + "/jcr:content";
		Session session;
		Node coke;
		String cokeStoreFilterJsonUrl = mcdWebServicesConfig
				.getCokeStoreFiltersUrl();
		try {
			if(resource!=null){
				Node compNode = resource.adaptTo(Node.class);	
			if( compNode != null){
				
				coke = compNode.getNode("jcr:content/coke");
				id = coke.getProperty("clientId").getString();
				locale = coke.getProperty("locale").getString();

				cokeStoreFilterJsonUrl = cokeStoreFilterJsonUrl + "/" + id
						+ "/" + locale;

				
			}}

			String result;

			URL url = new URL(cokeStoreFilterJsonUrl);
			logger.error("URLLLLL" + cokeStoreFilterJsonUrl);
			 
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", APPLICATION_JSON);
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			if ((result = br.readLine()) != null && resource!=null
					&& resource.adaptTo(Node.class) != null
					&& resourceResolver.getResource(resPath) != null
					&& resourceResolver.adaptTo(Session.class) != null) {
				logger.error("In CokeCategoryDetails Servlet...... " + result
						+ cokeStoreFilterJsonUrl);
				resourceResolver = request.getResourceResolver();
				session = resourceResolver.adaptTo(Session.class);

				resource = resourceResolver.getResource(resPath);

				Node rootnode = resource.adaptTo(Node.class);

				if ( rootnode!=null && !(rootnode.hasNode(COKE_DATA)) && session!=null) {
				
					Node folder = rootnode.addNode(COKE_DATA, "nt:folder");
					Node child = folder.addNode(COKE_JSON, "nt:file");
					Node jcrchild = child.addNode("jcr:content", "nt:resource");
					jcrchild.setProperty("jcr:mimeType", APPLICATION_JSON);
					jcrchild.setProperty(JCR_DATA, result);
					session.save();
				} else if(rootnode!=null) {	
					
					
					Node childFolder = rootnode.getNode(COKE_DATA);

					if (!(childFolder.hasNode(COKE_JSON)) && session!=null) {
						
						Node child = childFolder.addNode(COKE_JSON, "nt:file");
						Node jcrchild = child.addNode("jcr:content",
								"nt:resource");
						jcrchild.setProperty("jcr:mimeType", APPLICATION_JSON);
						jcrchild.setProperty(JCR_DATA, result);
						session.save();
					}
					 else {
						
					
						String resourcePath = currentPagePath
								+ "/jcr:content/cokedata"
								+ "/cokeFilters.json/jcr:content";
					logger.error("the resource Path is"+ resourcePath );
						resource = resourceResolver.getResource(resourcePath);
						if(resource!=null){
						Node node = resource.adaptTo(Node.class);
						
						if(node!=null && session!=null){
							
						node.setProperty(JCR_DATA, result);
						node.setProperty("jcr:lastModified",
								Calendar.getInstance());

						session.save();

						response.getWriter().print(result);
						conn.disconnect();
						}
						}
					}
				}

			}

		} catch (RepositoryException | IOException e) {

			logger.error("the exception is" , e);
		}

	}
}