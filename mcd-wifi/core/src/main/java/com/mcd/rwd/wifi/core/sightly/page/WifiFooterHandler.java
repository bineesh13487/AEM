/**
 * 
 */
package com.mcd.rwd.wifi.core.sightly.page;

import com.citytechinc.cq.component.annotations.Component;
import com.mcd.rwd.global.core.bean.Link;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.models.annotations.DesignAnnotation;
import com.mcd.rwd.global.core.utils.BumperUtil;
import com.mcd.rwd.global.core.utils.MultiFieldPanelUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * Created by Vamsi Jetty on 05-11-2016.
 *
 */

@Component(
		name = "wifi-footer",
		value = "Wifi-Footer",
		description = "To render footer for McD Wifi",
		resourceSuperType = "foundation/components/parbase",
		group = ".hidden"
)
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class WifiFooterHandler { //extends McDUse {
	
	private static final Logger LOG = LoggerFactory.getLogger(WifiFooterHandler.class);

	private List<Link> channels = new ArrayList<Link>();

	private String copyRight;
	
	private String textColor;
	
	private String bgColor;

	@DesignAnnotation("footer")
	Resource footer;

	@DesignAnnotation(ApplicationConstants.RES_BUMPER)
	Resource bumperRes;

	@PostConstruct
	protected void activate() {
		LOG.debug("FooterHandler Activate Method Called..");

		ValueMap properties = null != footer ? footer.getValueMap() : null;

		//ValueMap properties = getSiteConfig("footer");
		//Resource footer = getSiteConfigResource("footer");

		if (null != properties) {
			this.copyRight = properties.get("copyright", String.class);
			this.textColor = properties.get("textColor", String.class);
			this.bgColor = properties.get("bgColor", String.class);

			BumperUtil bumperUtil = new BumperUtil(bumperRes);
			populateChannels(MultiFieldPanelUtil.getMultiFieldPanelValues(footer, "channels"), bumperUtil);
		}
	}

	/**
	 * Populates the FooterChannel with the values authored.
	 *
	 * @param channelsList The list of channels.
	 */
	private void populateChannels(final List<Map<String, String>> channelsList, BumperUtil bumperUtil) {
		LOG.debug("Inside populateChannels to populate footer channels..");
		if (channelsList != null && !channelsList.isEmpty()) {
			LOG.debug("Number of footer channels configured {}", channelsList.size());
			Iterator<Map<String, String>> itr = channelsList.iterator();
			while (itr.hasNext()) {
				Map<String, String> item = itr.next();
				Link channel = bumperUtil.getLink(item.get(ApplicationConstants.PN_PATH),
						item.get(ApplicationConstants.PN_TITLE),
						Boolean.valueOf(item.get(ApplicationConstants.PN_TARGET)));
				this.channels.add(channel);
			}
		} else {
			LOG.error("No channel configured for footer. ChannelsList is empty..");
		}
	}

	public boolean isFooterComponentConfigured() {
		if (channels != null && !channels.isEmpty()) {
			return true;
		}
		return false;
	}

	public List<Link> getChannels() {
		return channels;
	}

	public String getCopyRight() {
		return copyRight;
	}
	/**
	 * @return the textColor
	 */
	public String getTextColor() {
		return textColor;
	}

	/**
	 * @return the bgColor
	 */
	public String getBgColor() {
		return bgColor;
	}
}
