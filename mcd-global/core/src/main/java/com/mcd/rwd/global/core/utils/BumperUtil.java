package com.mcd.rwd.global.core.utils;

import com.mcd.rwd.global.core.bean.Link;
import com.mcd.rwd.global.core.models.Bumper;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Rakesh.Balaiah on 29-09-2015.
 */
public final class BumperUtil {

	private static final Logger LOG = LoggerFactory.getLogger(BumperUtil.class);

	private Bumper bumper;

	/**
	 * Creates the bumper from the resource.
	 * @param resource
	 */
	public BumperUtil(Resource resource) {
		if (null != resource) {
			this.bumper = resource.adaptTo(Bumper.class);
			LOG.debug("Bumper Configuration available. Set to {}", this.bumper.isEnabled());
		}
	}

	/**
	 * returns a Link from the values passed.
	 * @param href
	 * @param text
	 * @param target
	 * @return The link object.
	 */
	public Link getLink(String href, String text, boolean target) {

		Link link = new Link();
		String finalLink = LinkUtil.getHref(href);
		link.setHref(finalLink);

		if (null != bumper && bumper.isEnabled() && StringUtils.isNotBlank(finalLink)
				&& finalLink.charAt(0) != '/') {
			LOG.debug("Using bumper to check link and set the class appropriately.");
			link.setLinkClass("external");
			List<String> ignoredDomains = bumper.getIgnore();

			if (isIgnored(finalLink, ignoredDomains)) {
				link.setLinkClass(null);
			}
		}

		link.setText(text);
		link.setTarget("_self");

		if (target) {
			link.setTarget("_blank");
		}

		return link;
	}

	private boolean isIgnored(final String link, final List<String> ignoredDomains) {
		if (ignoredDomains != null && !ignoredDomains.isEmpty()) {
			for (String domain : ignoredDomains) {
				if (link.startsWith(domain)) {
					return true;
				}
			}
		}
		return false;
	}
}
