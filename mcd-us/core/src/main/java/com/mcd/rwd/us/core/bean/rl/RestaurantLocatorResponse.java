package com.mcd.rwd.us.core.bean.rl;

/**
 * Created by srishma yarra
 */
public class RestaurantLocatorResponse {

	private String id;

	private String countryCode;

	private String publicName;

	private String shortDescription;
	
	private String longDescription;

	private RestLocatorAddress address;
	
	private  Identifiers identifiers;

	private String marketCode;

	private RestLocStoreAttributes storeAttributes;

	private RestLocStoreNumbers storeNumbers;

	private RestLocStoreServices storeServices;

	private RestLocatorUrls urls;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getPublicName() {
		return publicName;
	}

	public void setPublicName(String publicName) {
		this.publicName = publicName;
	}

	public RestLocatorAddress getAddress() {
		return address;
	}

	public void setAddress(RestLocatorAddress address) {
		this.address = address;
	}

	public String getMarketCode() {
		return marketCode;
	}

	public void setMarketCode(String marketCode) {
		this.marketCode = marketCode;
	}

	public RestLocStoreAttributes getStoreAttributes() {
		return storeAttributes;
	}

	public void setStoreAttributes(RestLocStoreAttributes storeAttributes) {
		this.storeAttributes = storeAttributes;
	}

	public RestLocStoreNumbers getStoreNumbers() {
		return storeNumbers;
	}

	public void setStoreNumbers(RestLocStoreNumbers storeNumbers) {
		this.storeNumbers = storeNumbers;
	}

	public RestLocStoreServices getStoreServices() {
		return storeServices;
	}

	public void setStoreServices(RestLocStoreServices storeServices) {
		this.storeServices = storeServices;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public RestLocatorUrls getUrls() {
		return urls;
	}

	public void setUrls(RestLocatorUrls urls) {
		this.urls = urls;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public Identifiers getIdentifier() {
		return identifiers;
	}

	public void setIdentifier(Identifiers identifiers) {
		this.identifiers = identifiers;
	}
}
