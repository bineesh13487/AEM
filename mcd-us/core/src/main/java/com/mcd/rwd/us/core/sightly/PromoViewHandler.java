package com.mcd.rwd.us.core.sightly;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.sightly.McDUse;
import com.mcd.rwd.global.core.utils.BumperUtil;
import com.mcd.rwd.global.core.utils.ImageUtil;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.us.core.bean.promo.Promo;
import com.mcd.rwd.us.core.constants.PromoConstants;

/**
 * Handler class for set the configuration into Promo bean.
 * Created by prahlad.d on 6/4/2016.
 */
public class PromoViewHandler extends McDUse {

	private static final Logger LOG = LoggerFactory.getLogger(PromoViewHandler.class);

	private Promo promo;

	@Override 
	public void activate() throws Exception {
		ValueMap properties = getProperties();
		Resource resource = getResource();
		BumperUtil bumperUtil = new BumperUtil(getSiteConfigResource(ApplicationConstants.RES_BUMPER));
		String pageTitle = PageUtil.getPageNameForAnalytics(getCurrentPage(), getRequest());
		this.promo = new Promo();

		//Set Promo bean from the properties
		LOG.debug("set the promo data from properties");
		promo.setImagePath(ImageUtil.getImagePath(resource));
		promo.setMobileImagePath(ImageUtil.getImagePath(resource, PromoConstants.PROPERTY_MOBILE_IMAGE));
		promo.setMcdlogo(ImageUtil.getImagePath(resource, PromoConstants.PROPERTY_MCD_LOGO));
		promo.setLogoAlt(properties.get(PromoConstants.PROPERTY_LOGO_ALT, String.class));
		promo.setCornerlogo(ImageUtil.getImagePath(resource, PromoConstants.PN_CORNER_LOGO));
		promo.setCornerLogoAlt(properties.get(PromoConstants.PN_CORNER_LOGO_ALT, String.class));
		promo.setEyetitle(properties.get(PromoConstants.EYETITLE, String.class));
		promo.setEyetitleFontStyle(null != properties.get(PromoConstants.PN_FONTSTYLE_EYETITLE, String.class) ? properties.get(PromoConstants.PN_FONTSTYLE_EYETITLE, String.class) : PromoConstants.DEFAULT_FONT_STYLE);
		promo.setTitle(properties.get(ApplicationConstants.PN_TITLE, String.class));
		promo.setTitleFontStyle(null != properties.get(PromoConstants.PN_FONTSTYLE_TITLE, String.class) ? properties.get(PromoConstants.PN_FONTSTYLE_TITLE, String.class) : PromoConstants.DEFAULT_TITLE_FONT_STYLE);
		promo.setSubtitle(properties.get(PromoConstants.PN_SUBTITLE, String.class));
		promo.setSubtitleFontStyle(null != properties.get(PromoConstants.PN_FONTSTYLE_SUBTITLE, String.class) ? properties.get(PromoConstants.PN_FONTSTYLE_SUBTITLE, String.class) : PromoConstants.DEFAULT_FONT_STYLE);
		promo.setDescription(properties.get(ApplicationConstants.PN_DESC, String.class));
		promo.setDescriptionFontStyle(null != properties.get(PromoConstants.PN_FONTSTYLE_DESCR, String.class) ? properties.get(PromoConstants.PN_FONTSTYLE_DESCR, String.class) : PromoConstants.DEFAULT_FONT_STYLE);
		promo.setDisclaimer(properties.get(PromoConstants.PN_DISCLAIMER, String.class));
		promo.setLink(bumperUtil.getLink(properties.get(ApplicationConstants.PN_LINK, String.class),
				properties.get(ApplicationConstants.PN_CTA, String.class), properties.get(ApplicationConstants.PN_TARGET, false)));
		promo.setAlignment(properties.get(ApplicationConstants.STYLE_POSITION, String.class));
		promo.setImageAlt(properties.get(ApplicationConstants.PN_ALT, String.class));
		promo.setAriaLabel(properties.get(ApplicationConstants.PN_ARIA_LABEL, String.class));
		
		if (StringUtils.isNotBlank(properties.get(ApplicationConstants.PN_COLOR, String.class))) {
			promo.setTitleColor("color: #" + properties.get(ApplicationConstants.PN_COLOR, String.class));
		}
		if (StringUtils.isNotBlank(properties.get(ApplicationConstants.PN_DISCLAIMERCOLOR, String.class))) {
			promo.setDisclaimercolor("color: #" + properties.get(ApplicationConstants.PN_DISCLAIMERCOLOR, String.class));
		}

		promo.setCtaAlt(properties.get(PromoConstants.STYLE_CTA_ALT, String.class));
		promo.setBottomAligned(properties.get(PromoConstants.BOTTOM_ALIGNED, false));
		promo.setRetainAlignment(properties.get(PromoConstants.RETAIN_ALIGNMENT, false));
		promo.setAnalytics("Promotion:"+ pageTitle + ":" + promo.getLink().getText());
	}

	public Promo getPromo() {
		return promo;
	}

}
