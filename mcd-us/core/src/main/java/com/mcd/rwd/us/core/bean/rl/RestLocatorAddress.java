package com.mcd.rwd.us.core.bean.rl;

/**
 * Created by srishma yarra 
 */
public class RestLocatorAddress {

	private String addressLine1;

	private String cityTown;

	private String postalZip;

	private String country;

	private String region;

	private String subdivision;

	private RestLocatorLocation location;

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getCityTown() {
		return cityTown;
	}

	public void setCityTown(String cityTown) {
		this.cityTown = cityTown;
	}

	public String getPostalZip() {
		return postalZip;
	}

	public void setPostalZip(String postalZip) {
		this.postalZip = postalZip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public RestLocatorLocation getLocation() {
		return location;
	}

	public void setLocation(RestLocatorLocation location) {
		this.location = location;
	}

	public String getSubdivision() {
		return subdivision;
	}

	public void setSubdivision(String subdivision) {
		this.subdivision = subdivision;
	}
}
