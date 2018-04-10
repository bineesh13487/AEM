package com.mcd.rwd.global.core.utils;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Rakesh.Balaiah on 14-10-2015.
 */
public final class TextUtil {
	private TextUtil() {
		//Make it private to prevent from instantiating.
	}

	public static Object sanitizeDnaValues(final Object value) {
		if (value instanceof String) {
			String val = (String) value;
			if (StringUtils.isBlank(val) || "N/A".equalsIgnoreCase(val) || "-".equalsIgnoreCase(val)) {
				return StringUtils.EMPTY;
			}
		}
		return value;
	}
}
