package com.mcd.rwd.us.core.sightly;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.sightly.McDUse;
import com.mcd.rwd.global.core.utils.BumperUtil;
import com.mcd.rwd.global.core.utils.ImageUtil;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.us.core.bean.FeatureCallout;
import com.mcd.rwd.us.core.constants.PromoConstants;

/**
 * Created by Ashish.Ya on 06/07/2017.
 */
public class PromoRibbonViewHandler extends McDUse {

	private static final Logger logger = LoggerFactory.getLogger(PromoRibbonViewHandler.class);
	
	private List<FeatureCallout> ribbons;
	private Page scaffPage;
	private String pageTitle=StringUtils.EMPTY;
	private String promoRibbonSize=StringUtils.EMPTY;
	private String promoGutter=StringUtils.EMPTY;
	private String textColor=StringUtils.EMPTY;
	private String buttonColor=StringUtils.EMPTY;
	private String backgroundColor=StringUtils.EMPTY;
	
	

	@Override 
	public void activate() throws Exception {
		logger.debug("activate() method called inside PromoRibbonViewHandler.");
		
		this.pageTitle = PageUtil.getPageNameForAnalytics(getCurrentPage(), getRequest());
        ValueMap properties = getProperties();
        promoRibbonSize=properties.get(PromoConstants.PROMO_RIBBON_SIZE, String.class);
        promoGutter=properties.get(PromoConstants.PROMO_GUTTER, String.class);
        textColor="#"+properties.get(PromoConstants.RIBBON_TEXT_COLOR, PromoConstants.RIBBON_TEXT_COLOR_VAL);
        buttonColor="#"+properties.get(PromoConstants.RIBBON_BTTN_COLOR,PromoConstants.RIBBON_BTTN_COLOR_VAL);
        backgroundColor="#"+properties.get(PromoConstants.RIBBON_BG_COLOR,PromoConstants.RIBBON_BG_COLOR_VAL);
        this.ribbons = new ArrayList<FeatureCallout>();
        StringTokenizer st=new StringTokenizer(promoRibbonSize,PromoConstants.RIBBON_SIZE_SEPARATOR);
        int count=1;
        while(st.hasMoreTokens()){
        	String width=st.nextToken();
        	String scaffPagePath =  properties.get(PromoConstants.PROMO_PAGE_LINK+count, String.class);
        	scaffPage = getPageManager().getPage(scaffPagePath);
        	this.ribbons.add(populateCalloutList(scaffPage, width));
        	count++;
        }
        logger.debug("activate() method complete successfully inside PromoRibbonViewHandler.");
    }
	
	/**
     * Retrieves and populates the promo values
     * 
     * @param page
     * @param width
     */
	private FeatureCallout populateCalloutList(Page page, String width) {
        if (null!= width && null != page && null != page.getContentResource(PromoConstants.PN_PROMO)) {
            Resource promoResource = page.getContentResource(PromoConstants.PN_PROMO);
            ValueMap properties = promoResource.getValueMap();
            FeatureCallout ribbon = new FeatureCallout();
            if(width.equals(PromoConstants.IMAGE_WIDTH_25)){
            	ribbon.setImagePath(ImageUtil.getImagePath(promoResource, PromoConstants.PROPERTY_MOBILE_IMAGE));
            }else{
            	ribbon.setImagePath(ImageUtil.getImagePath(promoResource));
            }
            ribbon.setMobileImagePath(ImageUtil.getImagePath(promoResource, PromoConstants.PROPERTY_MOBILE_IMAGE));
            ribbon.setMcdlogo(ImageUtil.getImagePath(promoResource, PromoConstants.PROPERTY_MCD_LOGO));
            ribbon.setLogoAlt(properties.get(PromoConstants.PROPERTY_LOGO_ALT, String.class));
            ribbon.setEyetitle(properties.get(PromoConstants.EYETITLE, String.class));
            ribbon.setEyetitleFontStyle(null != properties.get(PromoConstants.PN_FONTSTYLE_EYETITLE, String.class) ? properties.get(PromoConstants.PN_FONTSTYLE_EYETITLE, String.class) : PromoConstants.DEFAULT_FONT_STYLE);
            ribbon.setTitle(properties.get(ApplicationConstants.PN_TITLE, String.class));
            ribbon.setTitleFontStyle(null != properties.get(PromoConstants.PN_FONTSTYLE_TITLE, String.class) ? properties.get(PromoConstants.PN_FONTSTYLE_TITLE, String.class) : PromoConstants.DEFAULT_TITLE_FONT_STYLE);
            ribbon.setSubtitle(properties.get(PromoConstants.PN_SUBTITLE, String.class));
            ribbon.setSubtitleFontStyle(null != properties.get(PromoConstants.PN_FONTSTYLE_SUBTITLE, String.class) ? properties.get(PromoConstants.PN_FONTSTYLE_SUBTITLE, String.class) : PromoConstants.DEFAULT_FONT_STYLE);
            ribbon.setDescription(properties.get(ApplicationConstants.PN_DESC, String.class));
            ribbon.setDescriptionFontStyle(null != properties.get(PromoConstants.PN_FONTSTYLE_DESCR, String.class) ? properties.get(PromoConstants.PN_FONTSTYLE_DESCR, String.class) : PromoConstants.DEFAULT_FONT_STYLE);
            ribbon.setDisclaimer(properties.get(PromoConstants.PN_DISCLAIMER, String.class));
            String link = properties.get(ApplicationConstants.PN_LINK, String.class);
            
            ribbon.setLink(new BumperUtil(getSiteConfigResource(ApplicationConstants.RES_BUMPER))
                    .getLink(link, properties.get(ApplicationConstants.PN_CTA, String.class), false));
            ribbon.setAlignment(properties.get(ApplicationConstants.STYLE_POSITION, String.class));
            ribbon.setImageAlt(properties.get(ApplicationConstants.PN_ALT, String.class));
            ribbon.setTitleColor(properties.get(ApplicationConstants.PN_COLOR, String.class));
            ribbon.setCtaAlt(properties.get(PromoConstants.STYLE_CTA_ALT, String.class));
            ribbon.setAriaLabel(properties.get(PromoConstants.PN_ARIA_LABEL, String.class));
            ribbon.setAnalytics(PromoConstants.PROMOTION+ this.pageTitle + ":" + ribbon.getLink().getText());
            ribbon.setWidth(width);

            ribbon.setBottomAligned(properties.get(PromoConstants.BOTTOM_ALIGNED, false));
            ribbon.setRetainAlignment(properties.get(PromoConstants.RETAIN_ALIGNMENT, false));
            return ribbon;
        }
        logger.debug("ScaffPage in PromoRibbonViewHandler : "+page+" width of promo : "+width);
        return null;
    }

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public String getPromoGutter() {
		return promoGutter;
	}

	public void setPromoGutter(String promoGutter) {
		this.promoGutter = promoGutter;
	}

	public List<FeatureCallout> getRibbons() {
		return ribbons;
	}

	public void setRibbons(List<FeatureCallout> ribbons) {
		this.ribbons = ribbons;
	}

	public String getTextColor() {
		return textColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

	public String getButtonColor() {
		return buttonColor;
	}

	public void setButtonColor(String buttonColor) {
		this.buttonColor = buttonColor;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
    
}
