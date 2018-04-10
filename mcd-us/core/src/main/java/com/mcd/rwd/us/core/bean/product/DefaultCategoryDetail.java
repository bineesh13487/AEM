package com.mcd.rwd.us.core.bean.product;

import com.mcd.rwd.us.core.constants.ApplicationConstants;

/**
 * Created by deepti_b on 4/4/2016.
 */
public class DefaultCategoryDetail {

    private Object id;
    private String name;

    public Object getId() {
        if (id instanceof Double) {
            return ((Double) id).intValue();
        }
        if (id instanceof Integer) {
            return id;
        }
        return ApplicationConstants.DEFAULT_INT;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
