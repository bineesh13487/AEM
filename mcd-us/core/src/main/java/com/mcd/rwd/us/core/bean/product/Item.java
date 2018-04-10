package com.mcd.rwd.us.core.bean.product;

import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;
import com.mcd.rwd.us.core.bean.category.RelationTypes;

/**
 * Created by deepti_b on 3/22/2016.
 */
public class Item {

	@SerializedName(value = "item_id")
	private String itemId;

	@SerializedName(value = "external_id")
	private Object externalId;

	private Object description;

	@SerializedName(value = "show_product_images")
	private Object showProductImages;

	private Object text;
	@SerializedName(value = "item_marketing_name")
	private String itemMarketingName;

	@SerializedName(value = "item_meta_title")
	private Object itemMetaTitle;

	@SerializedName(value = "item_name")
	private Object itemName;
	@SerializedName(value = "attach_item_thumbnail_image")
	private HeroImage attachItemThumbnailImage;
	@SerializedName(value = "attach_item_hero_image")
	private HeroImage attachItemHeroImage;

	private ItemComponents components;

	@SerializedName(value = "mutex_groups")
	private MutexGroups mutexGroups;

	@SerializedName(value = "nutrient_facts")
	private NutrientFacts nutrientFacts;

	private List<Nutrient> nutrientInfo;

	private Object keywords;
	@SerializedName(value = "default_category")
	private DefaultCategory defaultCategory;

	private String productDesktopImagePath;

	private String productMobileImagePath;

	private String productSearchImagePath;

	private Map<String,String> footer;

	@SerializedName(value = "short_name")
	private Object shortName;

	@SerializedName(value = "relation_types")
	private RelationTypes relationTypes;

	@SerializedName(value = "item_meta_description")
    private Object itemMetaDescription;
	@SerializedName(value = "attach_transparent_icon_image")
	private HeroImage attachItemTransparentImage;

	public DefaultCategory getDefaultCategory() {
		return defaultCategory;
	}

	public void setDefaultCategory(DefaultCategory defaultCategory) {
		this.defaultCategory = defaultCategory;
	}

	public Object getKeywords() {
		if (keywords instanceof String) {
			return keywords;
		}
		return null;
	}

	public void setKeywords(Object keywords) {
		this.keywords = keywords;
	}

	public Object getDescription() {
		if (description instanceof String) {
			return description;
		}
		return null;
	}

	public void setDescription(Object description) {
		this.description = description;
	}

	public String getItemMarketingName() {
		return itemMarketingName;
	}

	public void setItemMarketingName(String itemMarketingName) {
		this.itemMarketingName = itemMarketingName;
	}

	public Object getItemName() {
		if (itemName instanceof String) {
			return itemName;
		}
		return null;
	}

	public void setItemName(Object itemName) {
		this.itemName = itemName;
	}

	public Object getText() {
		if (text instanceof String) {
			return text;
		}
		return null;
	}

	public void setText(Object text) {
		this.text = text;
	}

	public HeroImage getAttachItemHeroImage() {
		return attachItemHeroImage;
	}

	public void setAttachItemHeroImage(HeroImage attachItemHeroImage) {
		this.attachItemHeroImage = attachItemHeroImage;
	}

	public HeroImage getAttachItemThumbnailImage() {
		return attachItemThumbnailImage;
	}

	public void setAttachItemThumbnailImage(HeroImage attachItemThumbnailImage) {
		this.attachItemThumbnailImage = attachItemThumbnailImage;
	}

	public ItemComponents getComponents() {
		return components;
	}

	public void setComponents(ItemComponents components) {
		this.components = components;
	}

	public NutrientFacts getNutrientFacts() {
		return nutrientFacts;
	}

	public void setNutrientFacts(NutrientFacts nutrientFacts) {
		this.nutrientFacts = nutrientFacts;
	}

	public List<Nutrient> getNutrientInfo() {
		return nutrientInfo;
	}

	public void setNutrientInfo(List<Nutrient> nutrientInfo) {
		this.nutrientInfo = nutrientInfo;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
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

	public String getProductDesktopImagePath() {
		return productDesktopImagePath;
	}

	public void setProductDesktopImagePath(String productDesktopImagePath) {
		this.productDesktopImagePath = productDesktopImagePath;
	}

	public String getProductMobileImagePath() {
		return productMobileImagePath;
	}

	public void setProductMobileImagePath(String productMobileImagePath) {
		this.productMobileImagePath = productMobileImagePath;
	}

	public String getProductSearchImagePath() {
		return productSearchImagePath;
	}

	public void setProductSearchImagePath(String productSearchImagePath) {
		this.productSearchImagePath = productSearchImagePath;
	}


	public MutexGroups getMutexGroups() {
		return mutexGroups;
	}

	public void setMutexGroups(MutexGroups mutexGroups) {
		this.mutexGroups = mutexGroups;
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

	public RelationTypes getRelationTypes() {
		return relationTypes;
	}

	public void setRelationTypes(RelationTypes relationTypes) {
		this.relationTypes = relationTypes;
	}

	public String getItemMetaTitle() {
		if (itemMetaTitle instanceof String) {
			return (String) itemMetaTitle;
		}
		return null;
	}

	public void setItemMetaTitle(Object itemMetaTitle) {
		this.itemMetaTitle = itemMetaTitle;
	}

	public String getShowProductImages() {
		if (showProductImages instanceof String) {
			return (String) showProductImages;
		}
		return null;
	}

	public void setShowProductImages(Object showProductImages) {
		this.showProductImages = showProductImages;
	}


	public String getItemMetaDescription() {
		if (itemMetaDescription instanceof String) {
            return (String) itemMetaDescription;
        }
        return null;
	}

	public void setItemMetaDescription(Object itemMetaDescription) {
		this.itemMetaDescription = itemMetaDescription;
	}

	public HeroImage getAttachItemTransparentImage() {
		return attachItemTransparentImage;
	}

	public void setAttachItemTransparentImage(HeroImage attachItemTransparentImage) {
		this.attachItemTransparentImage = attachItemTransparentImage;
	}



	public Map<String, String> getFooter() {
		return footer;
	}

	public void setFooter(Map<String, String> footer) {
		this.footer = footer;
	}

}
