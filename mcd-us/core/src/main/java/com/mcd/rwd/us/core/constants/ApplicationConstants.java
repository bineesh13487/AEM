package com.mcd.rwd.us.core.constants;

/**
 * Created by Rakesh.Balaiah on 16-07-2015.
 */
public final class ApplicationConstants {

	public static final int DEFAULT_INT = 0;

	public static final int DEFAULT_ITEM_COUNT = 6;

	public static final String DEFAULT_BUTTON_TEXT = "Learn More";

	public static final String PN_HEIGHT = "height";

	public static final String PN_TITLE = "title";

	public static final String PN_WIDTH = "width";

	/* Path Specific Properties */
	public static final String PATH_SEPARATOR = "/";

	public static final String URL_EXT_HTML = ".html";

	public static final String DEFAULT_PRODUCT_IMAGE_NAME = "default.png";
	public static final String DEFAULT_TRANSPARENT_IMAGE_NAME = "default.jpg";

	/**
	 * path for site configuration page. Some attributes-nutrients, web service urls are configured in site
	 * configuration
	 */
	public static final String PRELAUNCH = "prelaunch";
	
	public static final String LANGUAGE_NAME = "langName";
	
	public static final String LANGUAGE_CODE = "langCode";
	
	public static final String LANGUAGE_ARIA_LABEL = "ariaLabel";
	
	public static final String MULTISITE_LANGUAGE_NODE = "multisiteLangNode";
	
	public static final String GLOBAL_LANGUAGE_NODE = "globalSiteUrl";
	
	public static final String PN_COUNTRY = "country";

	public static final String PN_LANGUAGE = "language";
	
	public static final String PN_MULTISITE_LANGUAGE = "multiSitelanguage";
	
	public static final String PN_ENABLED = "enabled";

	public static final String URL_QS_START = "?";

	public static final char URL_QS_DELIMITER_CHAR = '&';

	public static final char URL_QS_START_CHAR = URL_QS_START.charAt(0);

	public static final String PN_EQUALS = "=";
	
	public static final String SPACE = " ";
	
	public static final String RES_BUMPER = "bumper";

	public static final String DO_NOT_SHOW = "Do not Show";
	
	public static final String PN_LINK = "link";

	public static final String PN_SHOW_LIVE_DATA = "showLiveData";

	public static final String PN_CATEGORY_ID = "categoryId";

	public static final String PN_ITEM_ID = "item";

	public static final String PN_CATEGORY_TYPE = "categoryType";

	public static final String CONTENT_TYPE_JSON = "application/json";
	
	public static final String CALLBACK = "callback";
	
	public static final String JSON_CALLBACK = "JSON_CALLBACK";
	
	public static final String NUTRIENT_REQ = "nutrient_req";
	
	public static final String PN_YES = "Y";

	public static final String PN_COPS_ID = "coopId";

	public static final String PN_COPS_FILTER = "coopFilter";

	public static final String PN_SHOW_NATIONAL_COOP = "showNationalCoop";

	public static final String PRODUCT_THUMBNAIL_IMAGE_WIDTH = "190";

	public static final String PRODUCT_THUMBNAIL_IMAGE_HEIGHT = "190";

	public static final String PN_KEY = "key";

	public static final String URL_HTTP = "http://";

	public static final String URL_HTTPS = "https://";

	public static final String ID = "id";

	public static final String NAME = "name";

	public static final String SHORT_DESCRIPTION = "shortDescription";

	public static final String DESCRIPTION = "description";

	public static final String ALIGN = "align";

	public static final String PN_NUTRITION_ATTRIBUTE_ID = "nutritionAttributeId";

	public static final String PN_EXTERNAL_ITEM_ID = "externalItemId";

	public static final String NUTRITION = "nutrition";
	
	public static final String PRODUCT_RESTYPE = "mcd-us/components/page/productdetail";

	public static final String REGEX_PATTERN = "/^(([^<>()[\\]\\\\.,;:\\s@\\\"]+(\\.[^<>()[\\]\\\\.,;:\\s@\\\"]+)*)|(\\\".+\\\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$/";
			
	// Constants defined for the Dynamic media Service
	public static final String DYNAMIC_M_LARGE = "LARGE";
	public static final String DYNAMIC_M_MEDIUM = "MEDIUM";
	public static final String DYNAMIC_M_SMALL = "SMALL";
	public static final String DYNAMIC_M_XSMALL = "XSMALL";
	public static final String DYNAMIC_M_SERVERAPP = "/is/content";
	public static final String DYNAMIC_M_IMAGEPRESET_PATH = "/etc/dam/imageserver/macros/";
	public static final String EQUAL_OPERATOR = "=";
	public static final String AMPERSTAND_OPERATOR = "&";
	public static final String HYPHEN_OPERATOR = "&";
	public static final String IMAGETYPE_HERO = "HERO";
	public static final String IMAGETYPE_THUMBNAIL = "THUMBNAIL";
	public static final String IMAGETYPE_LOCALOPTION = "LOCALOPTION";
	public static final String IMAGETYPE_ICONICTHUMNAIL = "ICONICTHUMNAIL";
	public static final String IMAGETYPE_TRANSPARENT = "TRANSPARENT";
	public static final String JCR_CONTENT = "jcr:content";
	public static final String FORWARD_SLASH = "/";
	public static final String UNDERSCORE = "_";
	public static final String DYNAMICMEDIA_PRESETSYMBOL = "?$";
	public static final String COMMMA_SEPARTOR = ",";
	public static final String JCR_FILEREFERENCE = "fileReference";
	public static final String PN_DISCLAIMER_TEXT = "disclaimer";	
    public static final String PN_CORNER_LOGO = "cornerlogo";
	
	public static final String PN_CORNER_LOGO_ALT = "cornerLogoAlt";
	public static final String DOLLAR_SIGN="$";
	public static final String DAMASSETLIST_CONSTANT="dam:Asset";
	public static final String JCR_LASTMODIFIED_CONSTANT="jcr:content/jcr:lastModified";
	
	//Update for backlog: constant to create Item Attribute parameter for categoryURL
    /** The author mode constant. */
    public static final String AUTHOR_RUN_MODE = "author";

    /** The publish mode Constant. */
    public static final String PUBLISH_RUN_MODE = "publish";
	
	//Update for backlog: constant to create Item Attribute parameter for categoryURL

	public static final String NUTRITION_ATTRIBUTE_ID = "nutritionAttributeId";
	
	public static final String ITEM_ATTRIBUTE_ID = "itemAttributeId";
	
	public static final String PIPE = "%7C";

	public static final String PN_PATH = "path";
	/**
	 * The Private Constructor.
	 */
	private ApplicationConstants() {
	}

}
