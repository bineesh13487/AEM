package com.mcd.rwd.us.core.bean.category;

import org.apache.commons.lang.StringUtils;

import com.google.gson.annotations.SerializedName;
import com.mcd.rwd.us.core.bean.evm.Coops;
import com.mcd.rwd.us.core.constants.ApplicationConstants;

/**
 * Created by deepti_b on 3/9/2016.
 */
public class CategoryItem {

    @SerializedName(value = "item_marketing_name")
    private Object itemMarketingName;


    @SerializedName(value = "description")
    private Object description;

    @SerializedName(value = "external_id")
    private Object externalId;

    @SerializedName(value = "item_id")
    private Object itemId;

    private Object id;
    
    private Coops coops;

	@SerializedName(value = "item_name")
    private Object itemName;

    @SerializedName(value = "display_order")
    private Object displayOrder;

    @SerializedName(value = "do_not_show")
    private Object doNotShow;

    @SerializedName(value = "short_name")
    private Object shortName;

    @SerializedName(value = "attach_item_hero_image")
    private CategoryItemThumbnailImage attachItemHeroImage;

    @SerializedName(value = "attach_item_thumbnail_image")
    private CategoryItemThumbnailImage attachItemThumbnailImage;

    private String itemIconicDesktopImagePath;

    private String itemIconicMobileImagePath;

    private String itemDesktopImagePath;

    private String itemMobileImagePath;

    private String itemFlexSliderImagePath;

    private String categoryItemFullUrl;

    private boolean itemVisibility = false;

    @SerializedName(value = "relation_types")
    private RelationTypes relationTypes;

    private String categoryItemImagePath;
    
    @SerializedName(value = "specialization_text1")
    private Object specializationText1;
    
    @SerializedName(value = "specialization_text2")
    private Object specializationText2;
    
    private String wrapperClass = StringUtils.EMPTY;

    public String getCategoryItemImagePath() {
        return categoryItemImagePath;
    }

    public void setCategoryItemImagePath(String categoryItemImagePath) {
        this.categoryItemImagePath = categoryItemImagePath;
    }

    public String getCategoryItemFullUrl() {
        return categoryItemFullUrl;
    }

    public void setCategoryItemFullUrl(String categoryItemFullUrl) {
        this.categoryItemFullUrl = categoryItemFullUrl;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public String getDescription() {
        if (description instanceof String) {
            return (String) description;
        }
        return null;
    }

    public String getItemMarketingName() {
        if (itemMarketingName instanceof String) {
            return (String) itemMarketingName;
        }
        return null;
    }

    public void setItemMarketingName(Object itemMarketingName) {
        this.itemMarketingName = itemMarketingName;
    }

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

    public String getItemName() {
        if (itemName instanceof String) {
            return (String) itemName;
        }
        return null;
    }

    public void setItemName(Object itemName) {
        this.itemName = itemName;
    }

    public CategoryItemThumbnailImage getAttachItemThumbnailImage() {
        return attachItemThumbnailImage;
    }

    public void setAttachItemThumbnailImage(CategoryItemThumbnailImage attachItemThumbnailImage) {
        this.attachItemThumbnailImage = attachItemThumbnailImage;
    }

    public CategoryItemThumbnailImage getAttachItemHeroImage() {
        return attachItemHeroImage;
    }

    public void setAttachItemHeroImage(CategoryItemThumbnailImage attachItemHeroImage) {
        this.attachItemHeroImage = attachItemHeroImage;
    }

    public String getExternalId() {
        if (externalId instanceof Double) {
            return String.valueOf(((Double) externalId).intValue());
        } else if (externalId instanceof String) {
            return (String) externalId;
        }
        return null;
    }

    public void setExternalId(Object externalId) {
        this.externalId = externalId;
    }

    public int getItemId() {
        if (itemId instanceof Double) {
            return ((Double) itemId).intValue();
        }
        return ApplicationConstants.DEFAULT_INT;
    }

    public void setItemId(Object itemId) {
        this.itemId = itemId;
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

    public String getDoNotShow() {
        if (doNotShow instanceof String) {
            return (String) doNotShow;
        }
        return null;
    }

    public void setDoNotShow(Object doNotShow) {
        this.doNotShow = doNotShow;
    }

    public String getShortName() {
        if (shortName instanceof String) {
            return (String) shortName;
        }
        return null;
    }

    public void setShortName(Object shortName) {
        this.shortName = shortName;
    }


    public boolean isItemVisibility() {
        return itemVisibility;
    }

    public void setItemVisibility(boolean itemVisibility) {
        this.itemVisibility = itemVisibility;
    }

    public RelationTypes getRelationTypes() {
        return relationTypes;
    }

    public void setRelationTypes(RelationTypes relationTypes) {
        this.relationTypes = relationTypes;
    }

    public String getItemIconicDesktopImagePath() {
        return itemIconicDesktopImagePath;
    }

    public void setItemIconicDesktopImagePath(String itemIconicDesktopImagePath) {
        this.itemIconicDesktopImagePath = itemIconicDesktopImagePath;
    }

    public String getItemIconicMobileImagePath() {
        return itemIconicMobileImagePath;
    }

    public void setItemIconicMobileImagePath(String itemIconicMobileImagePath) {
        this.itemIconicMobileImagePath = itemIconicMobileImagePath;
    }
    public String getItemDesktopImagePath() {
        return itemDesktopImagePath;
    }

    public String getItemMobileImagePath() {
        return itemMobileImagePath;
    }
    public void setItemDesktopImagePath(String itemDesktopImagePath) {
        this.itemDesktopImagePath = itemDesktopImagePath;
    }

    public void setItemMobileImagePath(String itemMobileImagePath) {
        this.itemMobileImagePath = itemMobileImagePath;
    }


    public String getItemFlexSliderImagePath() {
        return itemFlexSliderImagePath;
    }

    public void setItemFlexSliderImagePath(String itemFlexSliderImagePath) {
        this.itemFlexSliderImagePath = itemFlexSliderImagePath;
    }
    
    public Coops getCoops() {
		return coops;
	}

	public void setCoops(Coops coops) {
		this.coops = coops;
	}

	public String getSpecializationText1() {
  		if (specializationText1 instanceof String) {
            return (String) specializationText1;
        }
        return null;
	}

	public void setSpecializationText1(Object specializationText1) {
		this.specializationText1 = specializationText1;
	}

	public String getSpecializationText2() {
		if (specializationText2 instanceof String) {
            return (String) specializationText2;
        }
        return null;
	}

	public void setSpecializationText2(Object specializationText2) {
		this.specializationText2 = specializationText2;
	}
	
	public void setWrapperClass(String wrapperClass) {
		this.wrapperClass = wrapperClass;
	}

	public String getWrapperClass() {
		return wrapperClass;
	}

}