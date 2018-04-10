package com.mcd.rwd.us.core.sso_profile;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.icfolson.aem.library.api.node.ComponentNode;
import com.icfolson.aem.library.api.page.PageDecorator;
import com.icfolson.aem.library.api.page.PageManagerDecorator;
import com.icfolson.aem.library.core.link.builders.factory.LinkBuilderFactory;
import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.us.core.service.McdFactoryConfigConsumer;
import com.mcd.rwd.us.core.service.McdWebServicesConfig;

@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class PrivacyAndTCUpdate implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrivacyAndTCUpdate.class);
    private static final String LINK_FORMAT = "<a href=\"{{href}}\" title=\"{{title}}\" target=\"_blank\">{{title}}</a>";
    private String pageNameForAnalytics = StringUtils.EMPTY;

    private String privacyPolicyUpdateFormTitle;
    private String privacyPolicyUpdateFormSubTitle;
    private String privacyPolicyPagePath;
    private String privacyPolicyLinkTitle;
    private String currentPrivacyPolicyVersion;
    private String lastUpdatedDateOfPrivacyPolicy;
    private String privacyNote;
    private String termsAndConditionsUpdateFormTitle;
    private String termsAndConditionsUpdateFormSubTitle;
    private String termsAndConditionsLinkTitle;
    private String termsAndConditionsPagePath;
    private String currentTermsAndConditionsVersion;
    private String lastUpdatedDateOfTermsAndConditions;
    private String termsNote;
    private String combinationUpdateFormTitle;
    private String combinationUpdateFormSubTitle;
    private String combinedNote;
    private String proceedButtonTitle;

    @JsonProperty
    private Map<String, String> config;
    private String privacyPolicyLinkHref;
    private String termsAndConditionsLinkHref;

    private String privacyWithLink;
    private String termsWithLink;
    private String combinedWithLink;

    @Inject
    private McdWebServicesConfig webServicesConfig;
    @Inject
    private McdFactoryConfigConsumer mcdFactoryConfigConsumer;
    @Inject
    private PageDecorator currentPage;
    @Inject
    private PageManagerDecorator pageManagerDecorator;

    @PostConstruct
    public void init() {
        try {
            final String designPath = currentPage.getInherited("cq:designPath", "");
            this.pageNameForAnalytics = PageUtil.getPageNameForAnalytics(currentPage);

            if (StringUtils.isNotBlank(designPath)) {
                //get design Node
                final PageDecorator designPage = pageManagerDecorator.getPage(designPath);
                if (designPage != null) {
                    ComponentNode ssoNode = designPage.getComponentNode("sso").orNull();
                    if (ssoNode != null) {
                        privacyPolicyUpdateFormTitle = ssoNode.get("privacyPolicyUpdateFormTitle", "NEW PRIVACY POLICY");
                        privacyPolicyUpdateFormSubTitle = ssoNode.get("privacyPolicyUpdateFormSubTitle", "");
                        privacyPolicyLinkTitle = ssoNode.get("privacyPolicyLinkTitle", "Privacy Policy");
                        privacyPolicyPagePath = ssoNode.get("privacyPolicyPagePath", "");
                        currentPrivacyPolicyVersion = ssoNode.get("currentPrivacyPolicyVersion", "");
                        lastUpdatedDateOfPrivacyPolicy = ssoNode.get("lastUpdatedDateOfPrivacyPolicy", "");
                        privacyNote = ssoNode.get("privacyNote", "");
                        termsAndConditionsUpdateFormTitle = ssoNode.get("termsAndConditionsUpdateFormTitle", "NEW T&C");
                        termsAndConditionsUpdateFormSubTitle = ssoNode.get("termsAndConditionsUpdateFormSubTitle", "");
                        termsAndConditionsLinkTitle = ssoNode.get("termsAndConditionsLinkTitle", "Terms & Conditions");
                        termsAndConditionsPagePath = ssoNode.get("termsAndConditionsPagePath", "");
                        currentTermsAndConditionsVersion = ssoNode.get("currentTermsAndConditionsVersion", "");
                        lastUpdatedDateOfTermsAndConditions = ssoNode.get("lastUpdatedDateOfTermsAndConditions", "");
                        termsNote = ssoNode.get("termsNote", "");
                        combinationUpdateFormTitle = ssoNode.get("combinationUpdateFormTitle", "NEW T&C AND PRIVACY POLICY");
                        combinationUpdateFormSubTitle = ssoNode.get("combinationUpdateFormSubTitle", "");
                        combinedNote = ssoNode.get("combinedNote", "");
                        proceedButtonTitle = ssoNode.get("proceedButtonTitle", "Proceed");
                    }
                }
            }
            initConfig();

        } catch (Exception e) {
            LOGGER.error("Exception in init()\n",e);
        }
    }

    private void initConfig() {
        config = new HashMap<>();
        String privacyLink = "";
        String termsLink = "";

        config.put("privacyPolicyUpdateFormTitle", privacyPolicyUpdateFormTitle);
        config.put("privacyPolicyUpdateFormSubTitle", privacyPolicyUpdateFormSubTitle);
        if (StringUtils.isNotBlank(privacyPolicyPagePath)) {
            privacyPolicyLinkHref = LinkBuilderFactory.forPath(privacyPolicyPagePath).build().getHref();
            privacyLink = StringUtils.replace(LINK_FORMAT, "{{href}}", privacyPolicyLinkHref);
            privacyLink = StringUtils.replace(privacyLink, "{{title}}", privacyPolicyLinkTitle);
            privacyWithLink = StringUtils.replace(privacyNote, "{{privacy}}", privacyLink);
        }
        config.put("currentPrivacyPolicyVersion", currentPrivacyPolicyVersion);
        config.put("lastUpdatedDateOfPrivacyPolicy", lastUpdatedDateOfPrivacyPolicy);

        config.put("termsAndConditionsUpdateFormTitle", termsAndConditionsUpdateFormTitle);
        config.put("termsAndConditionsUpdateFormSubTitle", termsAndConditionsUpdateFormSubTitle);
        if (StringUtils.isNotBlank(termsAndConditionsPagePath)) {
            termsAndConditionsLinkHref = LinkBuilderFactory.forPath(termsAndConditionsPagePath).build().getHref();
            termsLink = StringUtils.replace(LINK_FORMAT, "{{href}}", termsAndConditionsLinkHref);
            termsLink = StringUtils.replace(termsLink, "{{title}}", termsAndConditionsLinkTitle);
            termsWithLink = StringUtils.replace(termsNote, "{{terms}}", termsLink);
        }
        config.put("currentTermsAndConditionsVersion", currentTermsAndConditionsVersion);
        config.put("lastUpdatedDateOfTermsAndConditions", lastUpdatedDateOfTermsAndConditions);

        config.put("combinationUpdateFormTitle", combinationUpdateFormTitle);
        config.put("combinationUpdateFormSubTitle", combinationUpdateFormSubTitle);
        combinedWithLink = StringUtils.replace(combinedNote, "{{terms}}", termsLink);
        combinedWithLink = StringUtils.replace(combinedWithLink, "{{privacy}}", privacyLink);
        config.put("proceedButtonTitle", proceedButtonTitle);

    }

    public String getPrivacyPolicyUpdateFormSubTitle() {
        return privacyPolicyUpdateFormSubTitle;
    }

    public String getPrivacyPolicyLinkTitle() {
        return privacyPolicyLinkTitle;
    }

    public String getCurrentPrivacyPolicyVersion() {
        return currentPrivacyPolicyVersion;
    }

    public String getLastUpdatedDateOfPrivacyPolicy() {
        return lastUpdatedDateOfPrivacyPolicy;
    }

    public String getPrivacyNote() {
        return privacyNote;
    }

    public String getPrivacyPolicyLinkHref() {
        return privacyPolicyLinkHref;
    }

    public String getTermsAndConditionsUpdateFormTitle() {
        return termsAndConditionsUpdateFormTitle;
    }

    public String getTermsAndConditionsUpdateFormSubTitle() {
        return termsAndConditionsUpdateFormSubTitle;
    }

    public String getTermsAndConditionsLinkTitle() {
        return termsAndConditionsLinkTitle;
    }

    public String getCurrentTermsAndConditionsVersion() {
        return currentTermsAndConditionsVersion;
    }

    public String getLastUpdatedDateOfTermsAndConditions() {
        return lastUpdatedDateOfTermsAndConditions;
    }

    public String getTermsNote() {
        return termsNote;
    }

    public String getTermsAndConditionsLinkHref() {
        return termsAndConditionsLinkHref;
    }

    public String getCombinationUpdateFormTitle() {
        return combinationUpdateFormTitle;
    }

    public String getCombinationUpdateFormSubTitle() {
        return combinationUpdateFormSubTitle;
    }

    public String getCombinedNote() {
        return combinedNote;
    }

    public String getProceedButtonTitle() {
        return proceedButtonTitle;
    }

    public String getCombinedWithLink() {
        return combinedWithLink;
    }

    public String getPrivacyWithLink() {
        return privacyWithLink;
    }

    public String getTermsWithLink() {
        return termsWithLink;
    }

	public String getPageNameForAnalytics() {
		return pageNameForAnalytics;
	}
}
