package com.mcd.rwd.us.core.bean;


/**
 * Created by Dipankar Gupta on 25-03-2016.
 */
public class Nutrient {
		
	private String id;

	private String name;
	
	private String marketingName;
	
	private String percentageUnitFlag;
	
	private String measurementUnitFlag;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMarketingName() {
		return marketingName;
	}

	public void setMarketingName(String marketingName) {
		this.marketingName = marketingName;
	}
	
	public void setPercentageUnitFlag(String flag){
		this.percentageUnitFlag = flag;
	}
	
	public String getPercentageUnitFlag(){
		return percentageUnitFlag;
	}
	
	public void setMeasurementUnitFlag(String flag){
		this.measurementUnitFlag = flag;
	}
	
	public String getMeasurementUnitFlag(){
		return measurementUnitFlag;
	}
}
