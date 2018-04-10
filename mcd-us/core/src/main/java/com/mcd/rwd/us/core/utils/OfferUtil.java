package com.mcd.rwd.us.core.utils;

import com.day.cq.wcm.api.WCMMode;
import com.mcd.rwd.global.core.bean.Link;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.utils.ImageUtil;
import com.mcd.rwd.global.core.utils.LinkUtil;
import com.mcd.rwd.us.core.bean.offers.Offer;
import com.mcd.rwd.us.core.constants.PromoConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

/**
 * Created by Rakesh.Balaiah on 21-06-2016.
 */
public class OfferUtil {

	private OfferUtil() {

	}

	/**
	 * Returns an offer from the underlying resource.
	 * @param resource
	 * @param validityText
	 * @return The offer object.
	 */
	public static final Offer getOffer(Resource resource, String validityText) {

		if (null != resource) {
			ValueMap properties = resource.getValueMap();
			Offer offer = new Offer();
			offer.setImagePath(ImageUtil.getImagePath(resource));
			offer.setMobileImagePath(
					ImageUtil.getImagePath(resource, PromoConstants.PROPERTY_MOBILE_IMAGE));
			offer.setMcdlogo(ImageUtil.getImagePath(resource, PromoConstants.PROPERTY_MCD_LOGO));
			offer.setLogoAlt(properties.get(PromoConstants.PROPERTY_LOGO_ALT, String.class));
			offer.setEyetitle(properties.get(PromoConstants.EYETITLE, String.class));
			offer.setTitle(properties.get(ApplicationConstants.PN_TITLE, String.class));
			offer.setDescription(properties.get(ApplicationConstants.PN_DESC, String.class));
			Link link = new Link();
			link.setHref(LinkUtil.getHref(properties.get(ApplicationConstants.PN_LINK, String.class)));
			link.setText(properties.get(ApplicationConstants.PN_CTA, String.class));
			offer.setLink(link);
			offer.setAlignment(properties.get(ApplicationConstants.STYLE_POSITION, String.class));
			offer.setImageAlt(properties.get(ApplicationConstants.PN_ALT, String.class));
			offer.setTitleColor(properties.get(ApplicationConstants.PN_COLOR, String.class));
			offer.setCtaAlt(properties.get(PromoConstants.STYLE_CTA_ALT, String.class));
			String validity = validityText.replace("{0}", properties.get("start", StringUtils.EMPTY));
			validity = validity.replace("{1}", properties.get("end", StringUtils.EMPTY));
			offer.setValidity(validity);
			offer.setValidFrom(properties.get("start", String.class));
			offer.setValidTo(properties.get("end", String.class));
			return offer;
		}
		return null;
	}
    public static boolean getPublishRunMode(SlingHttpServletRequest request){
    	boolean runmode = Boolean.FALSE;
    	WCMMode mode = WCMMode.fromRequest(request);
    	if(mode.name().equals(WCMMode.DISABLED.toString())){
    		runmode = true;
    	}	
    	return runmode;
    }
}
