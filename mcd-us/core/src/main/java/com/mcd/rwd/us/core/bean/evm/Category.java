package com.mcd.rwd.us.core.bean.evm;

import com.google.gson.annotations.SerializedName;

public class Category {

	
	@SerializedName(value = "category_id")
    private int categoryId;
	
	@SerializedName(value = "meal_items")
	private MealItems mealItems;

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public MealItems getMealItems() {
		return mealItems;
	}

	public void setMealItems(MealItems mealItems) {
		this.mealItems = mealItems;
	}
	
	
}
