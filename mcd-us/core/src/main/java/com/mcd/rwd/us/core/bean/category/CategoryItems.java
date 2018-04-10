package com.mcd.rwd.us.core.bean.category;

/**
 * Created by deepti_b on 3/9/2016.
 */
public class CategoryItems {

    private CategoryItem[] item;

    public CategoryItem[] getItem() {
        return item;
    }

    public void setItem(CategoryItem[] item) {
        this.item = item.clone();
    }
}
