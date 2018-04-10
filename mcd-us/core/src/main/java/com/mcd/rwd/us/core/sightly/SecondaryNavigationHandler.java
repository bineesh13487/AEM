package com.mcd.rwd.us.core.sightly;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Cookie;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.mcd.rwd.global.core.sightly.McDUse;
import com.mcd.rwd.global.core.utils.LinkUtil;
import com.mcd.rwd.global.core.utils.MultiFieldPanelUtil;
import com.mcd.rwd.us.core.bean.Link;
import com.mcd.rwd.us.core.bean.MulticulturalLanguage;
import com.mcd.rwd.us.core.constants.ApplicationConstants;

/**
 * It is used as Handler for Secondary Navigation Component.
 * 
 * @author brijesh.t
 *
 */
public class SecondaryNavigationHandler extends McDUse {

	private static final Logger logger = LoggerFactory.getLogger(SecondaryNavigationHandler.class);
	private List<MulticulturalLanguage> presentPageLanguageList = new ArrayList<MulticulturalLanguage>(10);
	private String title;
	private String hexColor;
	private String bgUrl;
	private String logo;
	private String logoAltText;
	private String bannerAreaLabel;
	private String linkAreaLabel;
	String globalSiteLangNode;
	List<Map<String, String>> links;
	List<Link> linksList;
	private ValueMap properties;

	public static final String CLASSACTIVE = "active";

	public static final String MICROSITECOOKIENAME = "microSiteDefaultlanguage";

	@Override
	public final void activate() {
		logger.error("SecondaryNavigationHandler activate method start----------------------------------------------");
		List<MulticulturalLanguage> micrositeLangObject = null;
		int depth = 1;
		String cookieValue = "";
		final Page currentPage = getCurrentPage();
		properties = getProperties();
		final PageManager manager = getPageManager();
		SlingHttpServletRequest request = getRequest();
		Cookie cookie = request.getCookie(MICROSITECOOKIENAME);
		cookieValue = getCookieValue(cookie);
		logger.debug("CookieValue----------------------------------------------",cookieValue);
		title = properties.get("title", String.class);
		hexColor = properties.get("titleHexcolor", "FFFFFF");
		bgUrl = properties.get("titleBg", String.class);
		logo = properties.get("logo", String.class);
		logoAltText = properties.get("logoAltText", String.class);
		bannerAreaLabel = properties.get("bannerArealabel", String.class);
		linkAreaLabel = properties.get("linkArealabel", String.class);
		links = MultiFieldPanelUtil.getMultiFieldPanelValues(getResource(), "navLinks");

		generateLangListDialog(currentPage);

		logger.debug("linksList is :: " + linksList.toString());
		/* start for language toggle */
		if (StringUtils.equalsIgnoreCase(currentPage.getAbsoluteParent(1).getName(), ApplicationConstants.PRELAUNCH)) {
			depth++;
		}
		Map<String, List<MulticulturalLanguage>> dialogFieldsDetail = null;
		if (properties.get(ApplicationConstants.PN_ENABLED, false) && currentPage.getDepth() > depth) {
			dialogFieldsDetail = getLanguageToggleDialogFields();
			logger.debug("dialog fields------------------------- " + dialogFieldsDetail.toString());
		}
		logger.debug("global Site Lang Node------------------------- " + globalSiteLangNode);
		if (null != dialogFieldsDetail && !dialogFieldsDetail.isEmpty() && globalSiteLangNode != null) {
			String globalLangPath = currentPage.getAbsoluteParent(depth).getPath();
			for (Map.Entry<String, List<MulticulturalLanguage>> microNavLang : dialogFieldsDetail.entrySet()) {
				if (currentPage.getPath().contains(microNavLang.getKey()))
					globalLangPath = microNavLang.getKey();
				micrositeLangObject = microNavLang.getValue();
			}
			getPageForLanguageToggle(micrositeLangObject, globalLangPath, currentPage, manager, cookieValue);
			setMicrositeLanguageDefaultPage(micrositeLangObject);
		}
		logger.error("SecondaryNavigationHandler activate method end----------------------------------------------");
		/* end language toggle */
	}

	private String getCookieValue(Cookie cookie) {
		String cookieValue = StringUtils.EMPTY;
		if (cookie != null && cookie.getValue() != null) {
			try {
				cookieValue = (URLDecoder.decode(cookie.getValue(), "utf8")).replace("%2F", "/");
			} catch (UnsupportedEncodingException e) {
				logger.error("Exception in decoding the cookie ", e);
			}
		}
		return cookieValue;
	}

	private void generateLangListDialog(final Page currentPage) {
		linksList = new ArrayList<Link>();
		boolean activeLinkFlag = Boolean.FALSE;
		if (null != links && !links.isEmpty()) {
			for (Map<String, String> map : links) {

				Link linkFields = new Link();
				linkFields.setText(map.get("navTitle"));
				linkFields.setHref(LinkUtil.getHref(map.get("navUrlLink")));
				String target = map.get("target").replaceAll("\\[\"", "").replaceAll("\"\\]", "");
				logger.debug("target----------------------------------------------" + target);
				if (currentPage.getPath().equals(map.get("navUrlLink"))) {
					linkFields.setActiveLink(CLASSACTIVE);
					activeLinkFlag = Boolean.TRUE;
				}
				if ("true".equals(target)) {
					linkFields.setTarget("_blank");
				} else {
					linkFields.setTarget("_self");
				}
				logger.debug("target---" + linkFields.getTarget());
				linksList.add(linkFields);
				logger.debug("fields for language code : " + linkFields.getText() + "added in the list");
			}
			if (!linksList.isEmpty() && !activeLinkFlag) {
				linksList.get(0).setActiveLink(CLASSACTIVE);

			}
		}
	}

	/**
	 * This method is used to set the default language from the list of language
	 * configured in dialog.
	 * 
	 */
	private void setMicrositeLanguageDefaultPage(List<MulticulturalLanguage> micrositeLangObject) {
		if (presentPageLanguageList.isEmpty()) {
			for (MulticulturalLanguage micrositeLang : micrositeLangObject) {
				micrositeLang.setCurrentPage(LinkUtil.getHref(micrositeLang.getMultiSiteLangNode()));
				presentPageLanguageList.add(micrositeLang);
			}
			micrositeLangObject.get(0).setDefaultLanguage(true);
		} else {
			boolean defaultFlag = false;
			for (MulticulturalLanguage micrositeLang : micrositeLangObject) {
				if (micrositeLang.isDefaultLanguage()) {
					defaultFlag = true;
				}
			}
			if (!defaultFlag) {
				micrositeLangObject.get(0).setDefaultLanguage(true);
			}
		}
	}

	/**
	 * This method is used to create the list of language toggle to display in
	 * front end.
	 * 
	 * @param List<MulticulturalLanguage>
	 *            micrositeLangObject list of language from dialog
	 * @param globalLangPath
	 *            global page path
	 * @param currentPage
	 *            global page path
	 * @param manager
	 * @param cookieValue
	 *            to value to save in cookie
	 * 
	 */
	private void getPageForLanguageToggle(List<MulticulturalLanguage> micrositeLangObject, String globalLangPath,
			final Page currentPage, final PageManager manager, String cookieValue) {
		if (micrositeLangObject != null) {
			String pathAfterLang = getPagePathAfterMicrositeLanguage(micrositeLangObject, currentPage);
			logger.debug("GlobalPath----------------------------------------------", globalLangPath + cookieValue);
			for (MulticulturalLanguage micrositeLang : micrositeLangObject) {
				setDefaultFlag(currentPage, pathAfterLang, micrositeLang);
				if (manager.getPage(micrositeLang.getMultiSiteLangNode() + pathAfterLang) != null) {
					micrositeLang
							.setCurrentPage(LinkUtil.getHref(micrositeLang.getMultiSiteLangNode() + pathAfterLang));
				} else {
					micrositeLang.setCurrentPage(LinkUtil.getHref(micrositeLang.getMultiSiteLangNode()));
				}
				presentPageLanguageList.add(micrositeLang);
			}
		}
	}

	/**
	 * This method is used set the default language from list of language
	 */
	private void setDefaultFlag(final Page currentPage, String pathAfterLang, MulticulturalLanguage micrositeLang) {
		if (currentPage.getPath().equals(micrositeLang.getMultiSiteLangNode() + pathAfterLang)) {
			micrositeLang.setDefaultLanguage(Boolean.TRUE);
		} else {
			micrositeLang.setDefaultLanguage(Boolean.FALSE);
		}
	}

	private String getPagePathAfterMicrositeLanguage(List<MulticulturalLanguage> micrositeLangObject,
			final Page currentPage) {
		String pathAfterLang = "";
		for (MulticulturalLanguage micrositeLang : micrositeLangObject) {
			if (currentPage.getPath().contains(micrositeLang.getMultiSiteLangNode())) {
				String currentLanguagePage = micrositeLang.getMultiSiteLangNode();
				if (currentPage.getPath().split(currentLanguagePage).length > 1)
					pathAfterLang = currentPage.getPath().split(currentLanguagePage)[1];
			}
		}
		return pathAfterLang;
	}

	/**
	 * This method is used fetch the dialogue value from secondary navigation
	 * 
	 * @return Map<String, List<MulticulturalLanguage>> This returns map of
	 *         language toggle..
	 */
	public Map<String, List<MulticulturalLanguage>> getLanguageToggleDialogFields() {
		List<MulticulturalLanguage> langListFromDailog;
		Map<String, List<MulticulturalLanguage>> languageToggleFields = new HashMap<String, List<MulticulturalLanguage>>();
		List<Map<String, String>> languageField = MultiFieldPanelUtil.getMultiFieldPanelValues(getResource(),
				ApplicationConstants.PN_MULTISITE_LANGUAGE);
		langListFromDailog = new ArrayList<MulticulturalLanguage>();
		if (null != languageField && !languageField.isEmpty()) {
			for (Map<String, String> map : languageField) {

				MulticulturalLanguage langFields = new MulticulturalLanguage();
				langFields.setLangName(map.get(ApplicationConstants.LANGUAGE_NAME));
				langFields.setLangCode(map.get(ApplicationConstants.LANGUAGE_CODE));
				langFields.setAriaLabel(map.get(ApplicationConstants.LANGUAGE_ARIA_LABEL));
				langFields.setMultiSiteLangNode(map.get(ApplicationConstants.MULTISITE_LANGUAGE_NODE));
				langFields.setCurrentPage(getCurrentPage().getPath());
				langListFromDailog.add(langFields);
				logger.debug("fields for language code : " + langFields.getLangCode() + "added in the list");
			}
			globalSiteLangNode = properties.get(ApplicationConstants.GLOBAL_LANGUAGE_NODE, String.class);
			logger.debug("global site lang node-----------------------" + globalSiteLangNode);
			languageToggleFields.put(globalSiteLangNode, langListFromDailog);
		}
		return languageToggleFields;
	}

	public List<MulticulturalLanguage> getPresentPageLanguageList() {
		return presentPageLanguageList;
	}

	public void setPresentPageLanguageList(List<MulticulturalLanguage> presentPageLanguageList) {
		this.presentPageLanguageList = presentPageLanguageList;
	}

	public String getTitle() {
		return title;
	}

	public String getHexColor() {
		return hexColor;
	}

	public String getBgUrl() {
		return bgUrl;
	}

	public String getLogo() {
		return logo;
	}

	public List<Map<String, String>> getLinks() {
		return links;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getBannerAreaLabel() {
		return bannerAreaLabel;
	}

	public void setBannerAreaLabel(String bannerAreaLabel) {
		this.bannerAreaLabel = bannerAreaLabel;
	}

	public String getLinkAreaLabel() {
		return linkAreaLabel;
	}

	public void setLinkAreaLabel(String linkAreaLabel) {
		this.linkAreaLabel = linkAreaLabel;
	}

	public String getLogoAltText() {
		return logoAltText;
	}

	public void setLogoAltText(String logoAltText) {
		this.logoAltText = logoAltText;
	}

	public List<Link> getLinksList() {
		return linksList;
	}

	public void setLinksList(List<Link> linksList) {
		this.linksList = linksList;
	}

}
