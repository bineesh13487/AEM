package com.mcd.rwd.us.core.bean;


/**
 * Class is used as bean for Multicultural Language toggle
 * @author brijesh.t
 *
 */
public class MulticulturalLanguage {
	
	private String langName;

    private String langCode;

    private String multiSiteLangNode;
    
    private String ariaLabel;
    
    private String currentPage;
    
    private boolean defaultLanguage = false;

	public String getLangName() {
		return langName;
	}

	public void setLangName(String langName) {
		this.langName = langName;
	}

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public String getMultiSiteLangNode() {
		return multiSiteLangNode;
	}

	public void setMultiSiteLangNode(String multiSiteLangNode) {
		this.multiSiteLangNode = multiSiteLangNode;
	}

	public String getAriaLabel() {
		return ariaLabel;
	}

	public void setAriaLabel(String ariaLabel) {
		this.ariaLabel = ariaLabel;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public boolean isDefaultLanguage() {
		return defaultLanguage;
	}

	public void setDefaultLanguage(boolean defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}


}
