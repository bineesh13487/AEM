package com.mcd.rwd.us.core.bean.evm;

import com.google.gson.annotations.SerializedName;

public class MealItem {

	@SerializedName(value = "category_id")
    private int categoryId;
	
	@SerializedName(value = "item_marketing_name")
	private String itemMarketingName;
	
	@SerializedName(value = "attach_item_hero_image")
	private HeroImage image;
		
	private Object description;
	
	private Coops coops;
	
	private Items items;
	
	@SerializedName(value = "do_not_show")
    private Object doNotShow;
	
	public String getDoNotShow() {
		if (doNotShow instanceof String) {
			return (String) doNotShow;
	    }
	    return null;
	}

	public void setDoNotShow(Object doNotShow) {
		this.doNotShow = doNotShow;
	}
	
	public Coops getCoops() {
		return coops;
	}

	public void setCoops(Coops coops) {
		this.coops = coops;
	}
	
	public Items getItems() {
		return items;
	}

	public void setItems(Items items) {
		this.items = items;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public HeroImage getImage() {
		return image;
	}

	public void setImage(HeroImage image) {
		this.image = image;
	}

	public String getDescription() {
		if (description instanceof String) {
			return (String)description;	
		}
		return null;
		
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getItemMarketingName() {
		return itemMarketingName;
	}

	public void setItemMarketingName(String itemMarketingName) {
		this.itemMarketingName = itemMarketingName;
	}
	
}
