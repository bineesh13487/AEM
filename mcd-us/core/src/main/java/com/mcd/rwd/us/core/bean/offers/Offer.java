package com.mcd.rwd.us.core.bean.offers;

import com.mcd.rwd.us.core.bean.promo.Promo;

/**
 * Created by Rakesh.Balaiah on 07-06-2016.
 */
public class Offer extends Promo {

	private String validFrom;

	private String validTo;

	private String validity;

	private String analytics;

	public String getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}

	public String getValidTo() {
		return validTo;
	}

	public void setValidTo(String validTo) {
		this.validTo = validTo;
	}

	public String getValidity() {
		return validity;
	}

	public void setValidity(String validity) {
		this.validity = validity;
	}

	public String getAnalytics() {
		return analytics;
	}

	public void setAnalytics(String analytics) {
		this.analytics = analytics;
	}
}
