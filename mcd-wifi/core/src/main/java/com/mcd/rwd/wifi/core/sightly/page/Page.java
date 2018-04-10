/**
 * 
 */
package com.mcd.rwd.wifi.core.sightly.page;

import com.mcd.rwd.global.core.models.annotations.DesignAnnotation;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;

/**
 * @author mc52689
 *
 */

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Page {

	private String dayPartMorning;
	
	private String dayPartAfternoon;

	private String dayPartEvening;
		
	private String[] cookieGreetings = new String[5];
	
	private String defaultGreeting;

	@DesignAnnotation("headerGreetings")
	Resource headerGreetingsRes;

	@PostConstruct
	public void activate() {

		ValueMap properties = null != headerGreetingsRes ? headerGreetingsRes.getValueMap() : null;
		
		if (null != properties) {
			this.dayPartMorning = properties.get("dayPartMorning", String.class);
			this.dayPartAfternoon = properties.get("dayPartAfternoon", String.class);
			this.dayPartEvening = properties.get("dayPartEvening", String.class);		
			this.cookieGreetings = properties.get("cookieGreetings", String[].class);
			this.defaultGreeting = properties.get("defaultGreeting", String.class);
		}
	}
	
	/**
	 * @return the dayPartMorning
	 */
	public String getDayPartMorning() {
		return dayPartMorning;
	}

	/**
	 * @return the dayPartAfternoon
	 */
	public String getDayPartAfternoon() {
		return dayPartAfternoon;
	}

	/**
	 * @return the dayPartEvening
	 */
	public String getDayPartEvening() {
		return dayPartEvening;
	}
	/**
	 * @return the cookieGreetings
	 */
	public String[] getCookieGreetings() {
		return cookieGreetings;
	}

	/**
	 * @return the defaultGreeting
	 */
	public String getDefaultGreeting() {
		return defaultGreeting;
	}

}
