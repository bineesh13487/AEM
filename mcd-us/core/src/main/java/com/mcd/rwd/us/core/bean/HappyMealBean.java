package com.mcd.rwd.us.core.bean;

import java.util.List;


/**
 * Created by Dipankar Gupta on 25-03-2016.
 */
public class HappyMealBean {

	private String title;

	private List<HappyMealItem> happyMealItem;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<HappyMealItem> getHappyMealItem() {
		return happyMealItem;
	}

	public void setHappyMealItem(List<HappyMealItem> happyMealItem) {
		this.happyMealItem = happyMealItem;
	}
	
	
	}
