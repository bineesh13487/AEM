package com.mcd.rwd.global.core.utils;

import com.day.cq.wcm.foundation.Download;
import com.day.cq.wcm.foundation.Image;
import com.day.text.Text;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;

/**
 * Created by Rakesh.Balaiah on 04-08-2015.
 */
public final class LinkUtil {

	/**
	 * Private Constructor.
	 */
	private LinkUtil() {
	}

	/**
	 * Returns the Hyperlink for the authored path.
	 *
	 * @param link         - The authored link.
	 * @param ignoreValues - The links to ignore.
	 * @return The processed link.
	 */
	public static String getHref(final String link, final String... ignoreValues) {
		if (StringUtils.isNotBlank(link)) {

			if (isIgnored(link, ignoreValues)) {
				return link;
			}

			if (isDamOrExternal(link)) {
				return link;
			} else if (Text.isDescendant(ApplicationConstants.PATH_CONTENT, link)) {
				if (link.contains(".html")) {
					return link;
				}
				else {
					return link + ApplicationConstants.URL_EXT_HTML;
				}
				
			} else {
				return ApplicationConstants.URL_PROTOCOL_HTTP + "://" + link;
			}
		}
		return null;
	}

	private static final boolean isIgnored(final String link, final String... ignoreValues) {
		for (String ignoreValue : ignoreValues) {
			if (link.equals(ignoreValue)) {
				return true;
			}
		}
		return false;
	}

	private static final boolean isDamOrExternal(final String link) {
		if (Text.isDescendant(ApplicationConstants.PATH_DAM, link) || StringUtils
				.startsWithIgnoreCase(link, ApplicationConstants.URL_PROTOCOL_HTTP) || StringUtils
				.startsWithIgnoreCase(link, "//")) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the link for the Image.
	 *
	 * @param resource - The resource.
	 * @param name     - The name of the image resource under the given resource. Optional.
	 * @return The href of the image.
	 */
	public static String getImageHref(final Resource resource, final String name) {
		if (resource != null) {
			if (StringUtils.isNotBlank(name)) {
				return getImageHref(new Image(resource, name));
			} else {
				return getImageHref(new Image(resource));
			}
		}
		return null;
	}

	/**
	 * Returns the href for a download resource such as video, pdf.
	 *
	 * @param resource - The resource
	 * @param name     - The child of the resource containing the downloadable resource.
	 * @return - The link for the download.
	 */
	public static String getDownloadHref(final Resource resource, final String name) {
		Download download = null;

		if (StringUtils.isNotBlank(name) && resource.getChild(name) != null) {
			download = new Download(resource.getChild(name));
		} else if (StringUtils.isBlank(name)) {
			download = new Download(resource);
		}

		if (download != null) {
			return download.getHref();
		}
		return null;
	}

	/**
	 * Returns the link for the Image.
	 *
	 * @param image - The image object
	 * @return The image link.
	 */
	private static String getImageHref(final Image image) {
		if (image != null && image.hasContent()) {
			image.setSelector(ApplicationConstants.SELECTOR_IMG);
			return image.getHref();
		}
		return null;
	}

	/**
	 * Return Product Short URL.
	 * @param productPagePath
	 * @param shortName
	 * @return
	 */
	public static String getProductShortUrl(final String productPagePath, final String shortName) {
		if (StringUtils.isNotBlank(productPagePath)) {
			return productPagePath + ApplicationConstants.PATH_SEPARATOR + shortName
					+ ApplicationConstants.URL_EXT_HTML;
		}
		return null;
	}

	/**
	 * Returns the product short URL.
	 * @param productPagePath
	 * @param id
	 * @return
	 */
	public static String getProductUrl(final String productPagePath, final String id) {
		if (StringUtils.isNotBlank(productPagePath)) {
			return productPagePath + "." + id + ApplicationConstants.URL_EXT_HTML;
		}
		return null;
	}
}
