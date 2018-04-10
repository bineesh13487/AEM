package com.mcd.rwd.global.core.utils;

import com.day.cq.wcm.foundation.Image;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;

/**
 * Common methods used for processing image are defined here.
 *
 * @author HCL
 */
public final class ImageUtil {

	private ImageUtil() {
		//Private Constructor to avoid initialization.
	}

	/**
	 * Returns image path for image resource.
	 *
	 * @param res
	 * @return
	 */
	public static String getImagePath(Resource res) {
		return getImagePath(res, null);
	}

	/**
	 * Returns image path for image resource present
	 * under the component resource.
	 *
	 * @param res
	 * @param imageResource
	 * @return
	 */
	public static String getImagePath(Resource res, String imageResource) {
		if (null != res) {
			Image image;
			if (StringUtils.isNotBlank(imageResource)) {
				image = new Image(res, imageResource);
			} else {
				image = new Image(res);
			}
			return getImagePath(image);
		}
		return null;
	}

	/**
	 * Returns the image path for the image.
	 *
	 * @param img
	 * @return
	 */
	private static String getImagePath(Image img) {
		String imagePath = null;
		if (null != img /*&& img.hasContent()*/) {
			//Check whether the image is uploaded through DAM or local disc.
			if (StringUtils.isNotBlank(img.getFileReference())) {
				imagePath = img.getFileReference();
			} else {
				img.setSelector(ApplicationConstants.SELECTOR_IMG);
				imagePath = img.getHref();
			}
		}
		return imagePath;
	}

	/**
	 * Get image object from the specified resource.
	 *
	 * @param res
	 * @param name
	 * @return
	 */
	private static Image getImage(final Resource res, final String name) {
		Image image = null;
		if (res != null) {
			if (StringUtils.isNotBlank(name)) {
				image = new Image(res, name);
			} else {
				image = new Image(res);
			}
		}
		return image;
	}

	/**
	 * Get the extension of the image.
	 *
	 * @param resource
	 * @param name
	 * @return
	 */
	public static String getImageExtension(final Resource resource, final String name) {
		String imageExtension = null;
		Image image = getImage(resource, name);
		if (image != null && image.hasContent()) {
			imageExtension = image.getExtension();
		}
		return imageExtension;
	}

	/**
	 * Get the resource path of the image.
	 *
	 * @param resource
	 * @param name
	 * @return
	 */
	public static String getImageResourcePath(final Resource resource, final String name) {
		String imagePath = null;
		Image image = getImage(resource, name);
		if (image != null && image.hasContent()) {
			imagePath = image.getPath();
		}
		return imagePath;
	}
}
