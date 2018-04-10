package com.mcd.rwd.global.core.bean.style;

/**
 * Created by Rakesh.Balaiah on 21-03-2016.
 */
public enum Unit {
	PIXELS("px"),
	PERCENTILES("%"),
	EMS("em");

	private final String unitValue;

	Unit(String unitValue) {
		this.unitValue = unitValue;
	}

	String getValue() {
		return unitValue;
	}
}
