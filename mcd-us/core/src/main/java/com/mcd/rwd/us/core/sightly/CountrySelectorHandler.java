package com.mcd.rwd.us.core.sightly;

import com.adobe.cq.sightly.WCMUsePojo;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by sakshi.sr on 01-07-2017.
 */
public class CountrySelectorHandler extends WCMUsePojo {

	private static final Logger LOGGER = LoggerFactory.getLogger(CountrySelectorHandler.class);

	private Map<String, List<Map<String, String>>> link;


	public void activate() throws Exception {

		// ValueMap languageToggleProps = getSiteConfig("languagetoggle");

		LOGGER.info("CountrySelectorHandler: ");
		// links = getProperties().get("selectcountry/links", String[].class);
		link = getDropdownOptions("links");
		

	}

	public Map<String, List<Map<String, String>>> getDropdownOptions(String propValue) {
		LOGGER.info("getDropdownOptions");
		
		String[] links = getProperties().containsKey(propValue)
				? getProperties().get(propValue, String[].class) : null;

		Map<String, List<Map<String, String>>> linksList = new LinkedHashMap<String, List<Map<String, String>>>();
		
		for (String regionInfo : links) {
			
			if (regionInfo.contains("region")) {
				String region = regionInfo.substring(regionInfo.indexOf(":") + 1, regionInfo.indexOf(",") - 1);
				
				region = region.replaceAll("\"", "");
	
				regionInfo = regionInfo.substring(regionInfo.indexOf("[") + 2, regionInfo.indexOf("]"));
				regionInfo = regionInfo.replace("\\\"", "\"");
				StringTokenizer tokenizer = new StringTokenizer(regionInfo, "{");
				
				List<Map<String, String>> countryInfoList = new ArrayList<Map<String, String>>();

				while (tokenizer.hasMoreTokens()) {

					String token = "{" + tokenizer.nextToken().toString();
					
					try {
						JSONObject parsed = new JSONObject(token);
						
						Map<String, String> columnMap = new HashMap<String, String>();
						for (Iterator<String> iter = parsed.keys(); iter.hasNext();) {
							String key = iter.next();
							String innerValue = parsed.getString(key);
							if(key.equalsIgnoreCase("countryurl")&& !innerValue.endsWith(".html")){
								innerValue=innerValue+".html";
							}
							columnMap.put(key, innerValue);
							
						}
						countryInfoList.add(columnMap);
						linksList.put(region, countryInfoList);

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				
			}

		}

		return linksList;
	}

	public Map<String, List<Map<String, String>>> getLinks() {
		return link;
	}
}
