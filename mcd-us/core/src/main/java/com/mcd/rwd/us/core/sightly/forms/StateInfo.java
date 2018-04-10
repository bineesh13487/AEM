package com.mcd.rwd.us.core.sightly.forms;

import com.adobe.cq.sightly.WCMUsePojo;
import com.mcd.rwd.us.core.bean.offers.Offer;
import com.mcd.rwd.us.core.constants.FormConstants;
import com.mcd.rwd.us.core.constants.PromoConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class StateInfo {
	Map<String, String> states = new HashMap<String, String>();

	public StateInfo() {
		states.put("AL", "Alabama");
		states.put("AK", "Alaska");
		states.put("AB", "Alberta");
		states.put("AZ", "Arizona");
		states.put("AR", "Arkansas");
		states.put("BC", "British Columbia");
		states.put("CA", "California");
		states.put("CO", "Colorado");
		states.put("CT", "Connecticut");
		states.put("DE", "Delaware");
		states.put("DC", "District Of Columbia");
		states.put("FL", "Florida");
		states.put("GA", "Georgia");
		states.put("GU", "Guam");
		states.put("HI", "Hawaii");
		states.put("ID", "Idaho");
		states.put("IL", "Illinois");
		states.put("IN", "Indiana");
		states.put("IA", "Iowa");
		states.put("KS", "Kansas");
		states.put("KY", "Kentucky");
		states.put("LA", "Louisiana");
		states.put("ME", "Maine");
		states.put("MB", "Manitoba");
		states.put("MD", "Maryland");
		states.put("MA", "Massachusetts");
		states.put("MI", "Michigan");
		states.put("MN", "Minnesota");
		states.put("MS", "Mississippi");
		states.put("MO", "Missouri");
		states.put("MT", "Montana");
		states.put("NE", "Nebraska");
		states.put("NV", "Nevada");
		states.put("NB", "New Brunswick");
		states.put("NH", "New Hampshire");
		states.put("NJ", "New Jersey");
		states.put("NM", "New Mexico");
		states.put("NY", "New York");
		states.put("NF", "Newfoundland");
		states.put("NC", "North Carolina");
		states.put("ND", "North Dakota");
		states.put("NT", "Northwest Territories");
		states.put("NS", "Nova Scotia");
		states.put("NU", "Nunavut");
		states.put("OH", "Ohio");
		states.put("OK", "Oklahoma");
		states.put("ON", "Ontario");
		states.put("OR", "Oregon");
		states.put("PA", "Pennsylvania");
		states.put("PE", "Prince Edward Island");
		states.put("PR", "Puerto Rico");
		states.put("QC", "Quebec");
		states.put("RI", "Rhode Island");
		states.put("SK", "Saskatchewan");
		states.put("SC", "South Carolina");
		states.put("SD", "South Dakota");
		states.put("TN", "Tennessee");
		states.put("TX", "Texas");
		states.put("UT", "Utah");
		states.put("VT", "Vermont");
		states.put("VI", "Virgin Islands");
		states.put("VA", "Virginia");
		states.put("WA", "Washington");
		states.put("WV", "West Virginia");
		states.put("WI", "Wisconsin");
		states.put("WY", "Wyoming");
		states.put("YT", "Yukon Territory");

	}

	public String getStateName(String state) {

		String value = "";
		Iterator<Map.Entry<String, String>> it = states.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> pair = it.next();
			if (pair.getKey().equals(state.trim())) {
				value = pair.getValue();
				return value;
			}

		}

		return value;

	}

}