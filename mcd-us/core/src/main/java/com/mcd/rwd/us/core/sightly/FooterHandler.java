package com.mcd.rwd.us.core.sightly;

import com.citytechinc.cq.component.annotations.Component;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Design;
import com.day.cq.wcm.api.designer.Style;
import com.mcd.rwd.global.core.bean.Link;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.utils.BumperUtil;
import com.mcd.rwd.global.core.utils.LinkUtil;
import com.mcd.rwd.global.core.utils.MultiFieldPanelUtil;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.us.core.bean.SocialChannel;
import com.mcd.rwd.us.core.constants.FooterConstants;
import com.mcd.rwd.us.core.models.Footer;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Seema Pandey on 01-04-2016.
 */

@Component(
        value = "Footer",
        name = "footer",
        path = "content",
        description = "To render footer items for US RWD",
        group = ".hidden",
        editConfig = false
)
@Model(adaptables = {SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public final class FooterHandler {

    private static final Logger LOG = LoggerFactory.getLogger(FooterHandler.class);

    private List<SocialChannel> socialChannels = new ArrayList<SocialChannel>();

    private List<Link> channels = new ArrayList<Link>();

    private String copyRight;

    private String bgColor;

    private String socialChannelTitle;

    private Boolean showAppBadge;

    private String appBadgeTitle;
    
    private String ariaLabel;
    
    private String pageNameForAnalytics = StringUtils.EMPTY;

    @Inject
    Design currentDesign;

    @Inject
    Style currentStyle;

    @Inject
    Page currentPage;

    @PostConstruct
    public void init() {
        if (null != currentDesign) {
            Resource resource = currentDesign.getContentResource();
            Resource footerResource = resource.getChild("footer");
            if (null != footerResource) {
                Footer footer = footerResource.adaptTo(Footer.class);
                if (null != footer) {
                    this.copyRight = footer.getCopyright();
                    this.socialChannelTitle = footer.getSocialchanneltitle();
                    this.showAppBadge = footer.getShowappbadge();
                    this.ariaLabel = footer.getAria();
                    if (this.showAppBadge) {
                        this.appBadgeTitle = footer.getAppbadgetitle();
                    }
                }
                pageNameForAnalytics = PageUtil.getPageNameForAnalytics(currentPage);
                BumperUtil bumperUtil = new BumperUtil(resource.getChild(ApplicationConstants.RES_BUMPER));
                populateChannels(MultiFieldPanelUtil.getMultiFieldPanelValues(footerResource, "channels"), bumperUtil);
                populateSocialChannels(MultiFieldPanelUtil.getMultiFieldPanelValues(footerResource, "socialchannels"));
            }
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
            LOG.debug("No channel configured for footer. ChannelsList is empty..");
        }
    }

    /**
     * Populates the SocialChannel with the values authored.
     *
     * @param socialChannelsList The list of social channels.
     */

    private void populateSocialChannels(final List<Map<String, String>> socialChannelsList) {
        LOG.debug("Inside populateSocialChannels to populate social channel..");
        if (socialChannelsList != null && !socialChannelsList.isEmpty()) {
            LOG.debug("Number of social channels configured{}", socialChannelsList.size());
            Iterator<Map<String, String>> itr = socialChannelsList.iterator();
            while (itr.hasNext()) {
                Map<String, String> item = itr.next();
                String target;
                SocialChannel socialChannel = new SocialChannel();
                socialChannel.setSrc(item.get(FooterConstants.PN_SOCICON));
                socialChannel.setAlt(item.get(ApplicationConstants.PN_ALT));
                socialChannel
                        .setLink(LinkUtil.getHref(item.get(FooterConstants.PN_SOCLINK), String.valueOf('#')));
                if (Boolean.valueOf(item.get(ApplicationConstants.PN_TARGET))) {
                    target = "_blank";
                } else {
                    target = "_self";
                }
                socialChannel.setTarget(target);
                socialChannel.setScript(item.get(FooterConstants.PN_SOCSCRIPT));
                this.socialChannels.add(socialChannel);
            }
        } else {
            LOG.debug("No social channel configured for footer. socialChannelsList is empty..");
        }
    }

    public boolean isFooterComponentConfigured() {
        if ((channels != null && !channels.isEmpty()) && (socialChannels != null && !socialChannels
                .isEmpty())) {
            return true;
        }
        return false;
    }

    public List<SocialChannel> getSocialChannels() {
        return socialChannels;
    }

    public List<Link> getChannels() {
        return channels;
    }

    public String getCopyRight() {
        return copyRight;
    }

    public String getBgColor() {
        return bgColor;
    }

    public String getSocialChannelTitle() {
        return socialChannelTitle;
    }
    
    public String getAriaLabel() {
        return ariaLabel;
    }

    public Boolean getShowAppBadge() {
        return showAppBadge;
    }

    public String getAppBadgeTitle() {
        return appBadgeTitle;
    }

	public String getPageNameForAnalytics() {
		return pageNameForAnalytics;
	}
}