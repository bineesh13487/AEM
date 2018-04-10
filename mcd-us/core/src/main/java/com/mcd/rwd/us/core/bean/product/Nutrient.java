package com.mcd.rwd.us.core.bean.product;

import com.google.gson.annotations.SerializedName;
import com.mcd.rwd.global.core.utils.TextUtil;
import com.mcd.rwd.us.core.constants.ApplicationConstants;

/**
 * Created by deepti_b on 3/21/2016.
 */
public class Nutrient {
    private Object id;

    private Object name;

    private Object value;

    @SerializedName(value = "uom_description")
    private Object uomDescription;

    private Object uom;

    public int getId() {
        if (id instanceof Double) {
            return ((Double) id).intValue();
        }
        if (id instanceof Integer) {
            return ((Integer) id).intValue();
        }
        return ApplicationConstants.DEFAULT_INT;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getName() {
        if (name instanceof String) {
            return (String) name;
        }
        return null;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public Object getValue() {
        if (value instanceof Double) {
            Double doubleVal = (Double) value;
            if(Double.valueOf(0.0).equals(doubleVal % 1)){
                return (int) doubleVal.doubleValue();
            }
        }
        return value;
    }

    public void setValue(Object value) {
        this.value = TextUtil.sanitizeDnaValues(value);
    }

    public String getUomDescription() {
        if (uomDescription instanceof String) {
            return (String) uomDescription;
        }
        return null;
    }

    public void setUomDescription(Object uomDescription) {
        this.uomDescription = uomDescription;
    }

    public String getUom() {
        if (uom instanceof String) {
            return (String) uom;
        }
        return null;
    }

    public void setUom(Object uom) {
        this.uom = uom;
    }


}
