package com.mcd.rwd.us.core.sightly;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.widgets.NumberField;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.wcm.api.designer.Style;
import com.mcd.rwd.us.core.bean.product.RelatedProducts;
import com.mcd.rwd.us.core.constants.ApplicationConstants;
import com.mcd.rwd.us.core.service.McdDynamicMediaServiceConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.scripting.SlingScriptHelper;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.UUID;

/**
 * Created by deepti_b on 4/4/2016.
 */
@Component(name = "relatedproducts", value = "Related Products",
		actions = { "text: Related Products", "-", "editannotate", "copymove", "delete" },
		disableTargeting = true,
		tabs = {@Tab( touchUINodeName = "productDetails" , title = "Related Product Details" )},
		group = " GWS-Global", path = "content",
		listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
				@Listener(name = "afterinsert", value = "REFRESH_PAGE"),
				@Listener(name = "beforesubmit", value = "function(dialog) {\n" +
						"   var number = dialog.getField('./itemCount').getValue();\n" +
						"   if(number) {\n" +
						"\t\tif (number > 7) {\n" +
						"\t\t\tCQ.Ext.Msg.show({\n" +
						"\t\t\t\ttitle: 'Realted Products Validation',\n" +
						"\t\t\t\tmsg: 'Value Greater than 7 is not allowed', \n" +
						"\t\t\t\ticon: CQ.Ext.MessageBox.WARNING,\n" +
						"\t\t\t\tbuttons: CQ.Ext.Msg.OK\n" +
						"\t\t\t});\n" +
						"\t\t\treturn false;\n" +
						"\t\t}\n" +
						"\t\tif (number < 4) {\n" +
						"\t\t\tCQ.Ext.Msg.show({\n" +
						"\t\t\ttitle: 'Realted Products Validation',\n" +
						"\t\t\tmsg: 'Value Less than 4 is not allowed',\n" +
						"\t\t\ticon: CQ.Ext.MessageBox.WARNING,\n" +
						"\t\t\tbuttons: CQ.Ext.Msg.OK\n" +
						"\t\t\t});\n" +
						"\t\t\treturn false;\n" +
						"\t\t}\n" +
						"\t}\n" +
						"\treturn true;\n" +
						"}")})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RelatedProductsHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(RelatedProductsHandler.class);

	@DialogField(name = "./sectionTitle", fieldLabel = "Section Heading")
	@TextField
	@Inject @Named("sectionTitle")
	@Default(values = "")
	private String title;

	@DialogField(name = "./id", fieldLabel = "Category")
	@Selection(optionsProvider = "$PATH.categorylist.json", type = Selection.SELECT,
	dataSource = "mcd-us/dataSource/relatedproducts")
	@Inject @Named("id")
	private String categoryId;

	@DialogField(name = "./ariaNext", fieldLabel = "Aria Label for Next Button")
	@TextField
	@Inject @Named("ariaNext") @Default(values = "")
	private String ariaPrev;

	@DialogField(name = "./ariaPrev", fieldLabel = "Aria Label for Previous Button")
	@TextField
	@Inject @Named("ariaPrev") @Default(values = "")
	private String ariaNext;

	@DialogField(name = "./name", fieldLabel = "Marketing Name")
	@TextField
	@Inject @Named("name") @Default(values = "")
	private String marketingName;

	@DialogField(fieldLabel = "Link Text", fieldDescription = "Enter text for link to be shown on page.")
	@TextField
	@Inject @Default(values = "")
	private String linkText;

	@DialogField(name = "./linkURL", fieldLabel = "Path", additionalProperties = {@Property(name = "editable", value = "{Boolean}false")})
	@PathField
	@Inject @Named("linkURL")
	@Default(values = "")
	private String linkUrl;

	@DialogField(defaultValue = "6", name = "./itemCount", fieldLabel = "No: of items",
			fieldDescription = "Configure no: of items to be displayed on slider .")
	@NumberField
	@Inject @Named("itemCount")
	private String countItems;

	@DialogField(name = "./productSliderImageFolder", fieldLabel = "Product Image Folder Path")
	@PathField(rootPath = DamConstants.MOUNTPOINT_ASSETS, rootTitle = "DAM")
	@Inject @Named("productSliderImageFolder") @Default(values = "")
	private String imagefolderPath;

	@DialogField(fieldLabel = "Aria Label for Slider ")
	@TextField
	@Inject @Default(values = "")
	private String ariaSlider;

	@Inject
	SlingHttpServletRequest request;

	@Inject
	SlingScriptHelper slingScriptHelper;

	@OSGiService
	McdDynamicMediaServiceConfig mcdDynamicMediaServiceConfig;

	@Inject
	Style currentStyle;

	private RelatedProducts relatedProducts = new RelatedProducts();

	private String productImageFolderPath;

	private String productID;

	private String showLiveDataValue;

	private String uuid;

	private String dynamicMediaLargeParameter = StringUtils.EMPTY;
	private String dynamicMediaMediumParameter = StringUtils.EMPTY;
	private String dynamicMediaSmallParameter = StringUtils.EMPTY;
	private String dynamicMediaXSmallParameter =StringUtils.EMPTY;
	private String dynamicMediaServerContextURL = StringUtils.EMPTY;

	@PostConstruct
	public void init() throws Exception {
		String productId = StringUtils.EMPTY;
		uuid = UUID.randomUUID().toString();
		LOGGER.info("In RelatedProductsHandler categoryId configured in dialog is" + categoryId);
		showLiveDataValue = request.getParameter("showLiveData");
		if (StringUtils.isBlank(showLiveDataValue)) {
			showLiveDataValue = String.valueOf(true);
		}
		if (StringUtils.isBlank(categoryId)) {
			String[] selectorsArray = request.getRequestPathInfo().getSelectors();
			if (selectorsArray != null && selectorsArray.length > 0) {
				productId = selectorsArray[0];
				categoryId = selectorsArray[1];
				LOGGER.info(
						"In RelatedProductsHandler value of productId obtained from selector[0]" + productId
								+ "and default Category Obtained from selector[1]" + categoryId);
			}
		}
		if (StringUtils.isNotBlank(categoryId)) {
			relatedProducts.setCategoryID(Integer.valueOf(categoryId));
			relatedProducts.setItemsCount(countItems);
			relatedProducts.setLinkTitle(linkText);
			relatedProducts.setLinkUrl(StringUtils.isNotBlank(linkUrl) ?
					linkUrl + ApplicationConstants.URL_EXT_HTML :
					StringUtils.EMPTY);
			relatedProducts.setSectionTitle(title);
			relatedProducts.setAriaPrev(ariaNext);
			relatedProducts.setAriaNext(ariaPrev);
			relatedProducts.setAriaSlider(ariaSlider);
			relatedProducts.setCategoryName(marketingName);
		} else {
			relatedProducts.setCategoryID(ApplicationConstants.DEFAULT_INT);
		}
		productImageFolderPath = getProductImageFolder();
		if (StringUtils.isNotBlank(productId)) {
			productID = productId;
		} else {
			productID = String.valueOf(ApplicationConstants.DEFAULT_INT);
		}

		if(null!=mcdDynamicMediaServiceConfig)
		setDynamicMediaServerContextURL(mcdDynamicMediaServiceConfig.getImageServerUrl(request,
				slingScriptHelper));
		if(StringUtils.isNotEmpty(dynamicMediaServerContextURL)){
			setDynamicMediaLargeParameter(mcdDynamicMediaServiceConfig.getImagePresetThumbnailLarge());
			setDynamicMediaMediumParameter(mcdDynamicMediaServiceConfig.getImagePresetThumbnailMedium());
			setDynamicMediaSmallParameter(mcdDynamicMediaServiceConfig.getImagePresetThumbnailSmall());
			setDynamicMediaXSmallParameter(mcdDynamicMediaServiceConfig.getImagePresetThumbnailXSmall());
		}

	}

	public String getProductImageFolder() {
		String folderPathDialog = this.imagefolderPath;
		if (StringUtils.isNotBlank(folderPathDialog)) {
			return folderPathDialog;
		} else {
			//fetch path configured in design_dialog
			return currentStyle.get("productSliderImageFolder", String.class);

		}
	}

	public RelatedProducts getRelatedProducts() {
		return relatedProducts;
	}



	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getProductImageFolderPath() {
		return productImageFolderPath;
	}

	public void setProductImageFolderPath(String productImageFolderPath) {
		this.productImageFolderPath = productImageFolderPath;
	}

	public String getShowLiveDataValue() {
		return showLiveDataValue;
	}

	public void setShowLiveDataValue(String showLiveDataValue) {
		this.showLiveDataValue = showLiveDataValue;
	}

	public String getUuid() {
		return uuid;
	}

	public String getDynamicMediaLargeParameter() {
		return dynamicMediaLargeParameter;
	}

	public void setDynamicMediaLargeParameter(String dynamicMediaLargeParameter) {
		this.dynamicMediaLargeParameter = dynamicMediaLargeParameter;
	}

	public String getDynamicMediaMediumParameter() {
		return dynamicMediaMediumParameter;
	}

	public void setDynamicMediaMediumParameter(String dynamicMediaMediumParameter) {
		this.dynamicMediaMediumParameter = dynamicMediaMediumParameter;
	}

	public String getDynamicMediaSmallParameter() {
		return dynamicMediaSmallParameter;
	}

	public void setDynamicMediaSmallParameter(String dynamicMediaSmallParameter) {
		this.dynamicMediaSmallParameter = dynamicMediaSmallParameter;
	}

	public String getDynamicMediaXSmallParameter() {
		return dynamicMediaXSmallParameter;
	}

	public void setDynamicMediaXSmallParameter(String dynamicMediaXSmallParameter) {
		this.dynamicMediaXSmallParameter = dynamicMediaXSmallParameter;
	}

	public String getDynamicMediaServerContextURL() {
		return dynamicMediaServerContextURL;
	}

	public void setDynamicMediaServerContextURL(String dynamicMediaServerContextURL) {
		this.dynamicMediaServerContextURL = dynamicMediaServerContextURL;
	}
}

