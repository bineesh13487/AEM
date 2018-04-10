package com.mcd.rwd.us.core.bean.product;

import com.google.gson.annotations.SerializedName;

/**
 * Created by deepti_b on 5/20/2016.
 */
public class LocalItemsResponse {

    @SerializedName(value = "full_menu")
    private LocalItemsDetail fullMenu;

    public LocalItemsDetail getFullMenu() {
        return fullMenu;
    }

    public void setFullMenu(LocalItemsDetail fullMenu) {
        this.fullMenu = fullMenu;
    }
}
