package com.mcd.rwd.us.core.sightly;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Listener;
import com.citytechinc.cq.component.annotations.Tab;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.day.cq.dam.api.DamConstants;
import com.mcd.rwd.global.core.widget.colorpicker.ColorPicker;
import com.mcd.rwd.us.core.service.McdDynamicMediaServiceConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.UUID;

/**
 * Created by deepti_b on 4/29/2016.
 */
@Component(name = "goeswellwith", value = "Goes well With",
		description = "Goes well with Component show all products related to a particular Product",
		actions = { "text: Goes well With", "-", "editannotate", "copymove", "delete" },
		tabs = {@Tab(touchUINodeName = "productDetail", title = "Goes Well With")},
		disableTargeting = true,
		group = " GWS-Global", path = "content",
		listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
				@Listener(name = "afterinsert", value = "REFRESH_PAGE")})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class GoesWellWithHandler {

	@DialogField(name = "./sectionTitle", fieldLabel = "Section Heading")
	@TextField
	@Inject @Named("sectionTitle")
	@Default(values = "")
	private String title;

	@DialogField(name = "./ariaNext", fieldLabel = "Aria Label for Next Button")
	@TextField
	@Inject @Named("ariaNext")
	@Default(values = "")
	private String ariaPrev;

	@DialogField(name = "./ariaPrev", fieldLabel = "Aria Label for Previous Button")
	@TextField
	@Inject @Named("ariaPrev")
	@Default(values = "")
	private String ariaNext;

	@DialogField(name = "./productSliderImageFolder", fieldLabel = "Product Image Folder", required = true)
	@PathField(rootPath = DamConstants.MOUNTPOINT_ASSETS, rootTitle = "DAM")
	@Inject @Named("productSliderImageFolder") @Default(values = "")
	private String imageFolderPath;

	@DialogField(fieldLabel = "Aria Label for Slider ")
	@TextField
	@Inject
	@Default(values = "")
	private String ariaSlider;

	@DialogField(name = "./colorfull", fieldLabel = "color field")
	@ColorPicker
	@Inject
	@Default
	private String colorfull;

	@Inject
	SlingHttpServletRequest request;

	@Inject
	SlingScriptHelper slingScriptHelper;

	@OSGiService
	McdDynamicMediaServiceConfig mcdDynamicMediaServiceConfig;

	@Inject
	ValueMap valueMap;

	private static final Logger logger = LoggerFactory.getLogger(GoesWellWithHandler.class);

	private String productMarketingName;

	private String productId;

	private String showLiveDataValue;

	private String sectionTitle;

	private String productImageFolderPath;

	private String dynamicMediaLargeParameter = StringUtils.EMPTY;
	private String dynamicMediaMediumParameter = StringUtils.EMPTY;
	private String dynamicMediaSmallParameter = StringUtils.EMPTY;
	private String dynamicMediaXSmallParameter =StringUtils.EMPTY;
	private String dynamicMediaServerContextURL = StringUtils.EMPTY;

	private String uuid;

	@PostConstruct
	public void init() throws UnsupportedEncodingException {
		this.sectionTitle = this.title;
		this.ariaNext = this.ariaPrev;
		this.ariaPrev = this.ariaNext;
		this.productImageFolderPath = this.imageFolderPath;
		logger.info("GoesWellWithHandler Activate Method Called.");
		String[] selectorsArray = request.getRequestPathInfo().getSelectors();
		showLiveDataValue = request.getParameter("showLiveData");
		uuid = UUID.randomUUID().toString();
		if (StringUtils.isBlank(showLiveDataValue)) {
			showLiveDataValue = String.valueOf(true);
		}
		if (selectorsArray != null && selectorsArray.length > 0) {
			productId = selectorsArray[0];
			productMarketingName = selectorsArray[1];
			if(StringUtils.isNotBlank(productMarketingName)){
				productMarketingName= URLDecoder.decode(productMarketingName, "UTF-8");
			}
			logger.info("In GoesWellWithHandler value of productId obtained from selector[0]" + productId
					+ "and Product name  Obtained from selector[1]" + productMarketingName);
		}
		if (StringUtils.isBlank(productId)) {
			productId = valueMap.get("itemId", String.class);
		}
		if (StringUtils.isBlank(productMarketingName)) {
			productMarketingName = valueMap.get("productName", String.class);
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

	public String getProductMarketingName() {
		return productMarketingName;
	}

	public String getProductId() {
		return productId;
	}

	public String getSectionTitle() {
		return sectionTitle;
	}

	public String getAriaPrev() {
		return ariaPrev;
	}

	public String getAriaNext() {
		return ariaNext;
	}

	public String getAriaSlider() {
		return ariaSlider;
	}

	public String getProductImageFolderPath() {
		return productImageFolderPath;
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
