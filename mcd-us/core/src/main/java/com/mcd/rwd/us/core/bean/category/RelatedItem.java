package com.mcd.rwd.us.core.bean.category;

import com.google.gson.annotations.SerializedName;
import com.mcd.rwd.us.core.constants.ApplicationConstants;

/**
 * Created by deepti_b on 3/31/2016.
 */
public class RelatedItem {

    private Object id;

    @SerializedName(value = "external_id")
    private Object externalId;

    @SerializedName(value = "is_default")
    private Object isDefault;

    private Object label;

    @SerializedName(value = "display_order")
    private Object displayOrder;

    @SerializedName(value = "short_name")
    private Object shortName;

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

    public int getExternalId() {
        if (externalId instanceof Double) {
            return ((Double) externalId).intValue();
        }
        if (externalId instanceof Integer) {
            return ((Integer) externalId).intValue();
        }
        return ApplicationConstants.DEFAULT_INT;
    }

    public void setExternalId(Object externalId) {
        this.externalId = externalId;
    }

    public boolean getIsDefault() {
        if (isDefault instanceof Boolean) {
            return (Boolean) isDefault;
        }
        return false;
    }

    public void setIsDefault(Object isDefault) {
        this.isDefault = isDefault;
    }

    public Object getLabel() {
        if (label instanceof String) {
            return (String) label;
        }
        return null;
    }

    public void setLabel(Object label) {
        this.label = label;
    }

    public int getDisplayOrder() {
        if (displayOrder instanceof Double) {
            return ((Double) displayOrder).intValue();
        }
        if (displayOrder instanceof Integer) {
            return ((Integer) displayOrder).intValue();
        }
        return ApplicationConstants.DEFAULT_INT;
    }

    public void setDisplayOrder(Object displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    public void setShortName(Object shortName) {
		this.shortName = shortName;
	}

	public String getShortName() {
		if (shortName instanceof String) {
            return (String) shortName;
        }
        return null;
	}
}
