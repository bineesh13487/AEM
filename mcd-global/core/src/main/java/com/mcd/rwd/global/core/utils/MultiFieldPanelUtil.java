package com.mcd.rwd.global.core.utils;

import com.day.cq.commons.inherit.InheritanceValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * JSP functions for working with MultiFieldPanel widget.
 */
public final class MultiFieldPanelUtil {

	/* The default Logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(MultiFieldPanelUtil.class);

	/**
	 * Private Constructor.
	 */
	private MultiFieldPanelUtil() {
	}

	/**
	 * Extract the value of a MultiFieldPanel property into a list of maps. Will never return
	 * a null map, but may return an empty one. Invalid property values are logged and skipped.
	 *
	 * @param resource the resource
	 * @param name     the property name
	 * @return a list of maps.
	 */
	public static List<Map<String, String>> getMultiFieldPanelValues(final Resource resource,
			final String name) {
		ValueMap map = resource.adaptTo(ValueMap.class);
		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		if(map!=null && map.containsKey(name)) {
			String[] values = map.get(name, new String[0]);
			for (String value : values) {

				try {
					populateList(results, value);
				} catch (JSONException e) {
					LOGGER.error(String.format("Unable to parse JSON in %s property of %s", name,
							resource.getPath()), e);
				}
			}
		}
		return results;
	}

	/**
	 * Extract the inherited value of a MultiFieldPanel property into a list of maps. Will never return
	 * a null map, but may return an empty one. Invalid property values are logged and skipped.
	 *
	 * @param map  The inherited value map
	 * @param name The property name
	 * @return a list of maps.
	 */
	public static List<Map<String, String>> getInheritedMultiFieldPanelValues(final InheritanceValueMap map,
			final String name) {
		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		String[] values = map.getInherited(name, new String[0]);
		for (String value : values) {

			try {
				populateList(results, value);
			} catch (JSONException e) {
				LOGGER.error("Unable to parse JSON", e);
			}
		}
		return results;
	}

	private static void populateList(List<Map<String, String>> results, String value) throws JSONException {
		JSONObject parsed = new JSONObject(value);
		Map<String, String> columnMap = new HashMap<String, String>();
		for (Iterator<String> iter = parsed.keys(); iter.hasNext(); ) {
			String key = iter.next();
			String innerValue = parsed.getString(key);
			columnMap.put(key, innerValue);
		}
		results.add(columnMap);
	}
}
