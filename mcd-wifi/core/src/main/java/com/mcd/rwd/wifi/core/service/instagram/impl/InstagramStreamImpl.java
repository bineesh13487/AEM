/**
 * 
 */
package com.mcd.rwd.wifi.core.service.instagram.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.jcr.api.SlingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Vamsi Jetty on 04-29-2016.
 *
 */
@Service(value = InstagramStreamImpl.class)
@Component(metatype = false, immediate=true)
public class InstagramStreamImpl{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InstagramStreamImpl.class);
    
    @Reference
    private SlingRepository repository;
	
	public String getJsonResponse(String postUrl) {
	    StringBuilder response = new StringBuilder();
	    HttpURLConnection con = null;
		try{
			URL obj = new URL(postUrl);
	        con = (HttpURLConnection) obj.openConnection();
	        con.setRequestMethod("GET");
	        con.setRequestProperty("Content-Type", "application/json");
	        int responseCode = con.getResponseCode();
	        LOGGER.info("GET Response Code :: " + responseCode);
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

    	} catch (MalformedURLException e) {
    		LOGGER.error("MalformedURLException in InstagramStreamImpl : "+e);
		} catch (IOException e) {
			LOGGER.error("IOException in InstagramStreamImpl : "+e);
		}finally{
			if(con != null){
				try {
					con.disconnect();				
				} catch (Exception e2) {
					LOGGER.error("Exception occers while trying to disconnect HttpUrlConnection : "+e2);
				}
			}
    	}
		return response.toString();
	}

}