package com.mcd.rwd.us.core.service.impl;

import com.day.cq.commons.Filter;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.ValueMap;

/**
 * Created by Rakesh.Balaiah on 07-06-2016.
 */
public class OfferPageFilter implements Filter<Page> {

	private final String name;

	private final String value;

	public OfferPageFilter() {
		this("templatetype", "offer");
	}

	public OfferPageFilter(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Override public boolean includes(Page page) {
		ValueMap properties = page.getProperties();

		if (value != null && value.equals(properties.get(name, String.class))) {
			return true;
		}

		return false;
	}
}
