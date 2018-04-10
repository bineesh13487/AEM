/**
 * 
 */
package com.mcd.rwd.wifi.core.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mcd.rwd.wifi.core.service.McDWifiFactoryConfig;

/**
 * @author mc52689
 *
 */
@SlingServlet(paths = { "/bin/services/mcd-wifi/restaurantLocator" })
public class LocationServicesServlet extends SlingAllMethodsServlet {
	
	@Reference
	private McDWifiFactoryConfig mcdWifiFactoryConfig;
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(LocationServicesServlet.class);
	@Override
	public void doGet(SlingHttpServletRequest request,
			SlingHttpServletResponse response) throws ServletException,
			IOException {
		doPost(request,response);
	}

	@Override
	public void doPost(SlingHttpServletRequest request,
			SlingHttpServletResponse response) throws ServletException,
			IOException {
		try{
			String latitude = request.getParameter("latitude");
			String longitude = request.getParameter("longitude");
			String restuarantLocator = mcdWifiFactoryConfig.getRestaurantLocatorUrl()+"?filter=geodistance&coords="+latitude+"%2c"+longitude+"&distance="+mcdWifiFactoryConfig.getRestaurantLocatorRadius()+"&market="+mcdWifiFactoryConfig.getRestaurantLocatorCountry()+"&languageName="+mcdWifiFactoryConfig.getLanguage()+"&size="+mcdWifiFactoryConfig.getRestaurantLocatorResultsSize();
			LOGGER.info("Location Services URL:::::::"+ restuarantLocator);
			String jsonResponse = getJsonResponse(restuarantLocator);
            response.getWriter().println(jsonResponse);
    	}catch(Exception e){
    		LOGGER.error("Exceptions in LocationGreetingServlet: ",e);
    	}

	}

	public String getJsonResponse(String postUrl) {
	    StringBuilder response = new StringBuilder();
	    HttpURLConnection con = null;
		try{
			URL obj = new URL(postUrl);
	        con = (HttpURLConnection) obj.openConnection();
	        con.setRequestMethod("GET");
	        con.setRequestProperty("Content-Type", "application/json");
	        con.setRequestProperty("mcd_apikey", mcdWifiFactoryConfig.getRestaurantLocatorApiKey());
	        con.setRequestProperty("MarketId", mcdWifiFactoryConfig.getRestaurantLocatorMarketId());
	        int responseCode = con.getResponseCode();
	        LOGGER.info("POST Response Code :: " + responseCode);
	        if (responseCode == HttpURLConnection.HTTP_OK) { //success
	            BufferedReader in = new BufferedReader(new InputStreamReader(
	                    con.getInputStream()));
	            String inputLine;
	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }
	            in.close();
	        } else {
	        	// All the other status code messages will be getting here and processed accordingly.
	            LOGGER.info("GET request not worked");
	        }

    	}catch(Exception e){
    		 LOGGER.error("Exceptions in LocationGreetingServiceCall: "+e);
    	}finally{
    		if(con != null){
    			con.disconnect();
    		}
    	}
		return response.toString();
	}
}
