package com.mcd.rwd.us.core.bean.evm;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class MealItems {
	
	@SerializedName(value = "meal_item")
	private List<MealItem> mealItem;

	public List<MealItem> getMealItem() {
		return mealItem;
	}

	public void setMealItem(List<MealItem> mealItem) {
		this.mealItem = mealItem;
	}

	
	

	
}
