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
import com.mcd.rwd.us.core.service.McdFactoryConfig;
import com.mcd.rwd.us.core.service.McdFactoryConfigConsumer;
import com.mcd.rwd.us.core.service.McdWebServicesConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public final class LoginStatusIndicator implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginStatusIndicator.class);

    private String pageNameForAnalytics = StringUtils.EMPTY;
    private boolean hideLinks;
    private String loginLinkTitle;
    private String loginPagePath;
    private String logoutLinkTitle;

    @JsonProperty
    private String userRegistered;
    @JsonProperty
    private String userRegisteredNeedsVerification;
    @JsonProperty
    private String passwordReset;
    @JsonProperty
    private String userLoggedIn;
    @JsonProperty
    private String userLoggedOut;
    @JsonProperty
    private String accountVerified;
    @JsonProperty
    private String accountAlreadyVerified;
//    @JsonProperty
//    private String accountsMerged;
    @JsonProperty
    private String verificationEmailSent;
    @JsonProperty
    private String verificationNeeded;
    @JsonProperty
    private String verificationCodeExpired;
    @JsonProperty
    private String accountDeleted;
    @JsonProperty
    private String emailUpdatedNeedsVerification;
    @JsonProperty
    private String userSubscribed;
    @JsonProperty
    private String userUnsubscribed;
    @JsonProperty
    private String socialLoginFailed;
    @JsonProperty
    private String userLoggedOutBySystem;
    @JsonProperty
    private String accountUpdatedPersonalInformation;
    @JsonProperty
    private String accountUpdatedCommunicationPreferences;

    @JsonProperty
    private String errorGeneric;
    private boolean disableConfig;
    private String redirectNotLoggedInUserPath;
    private String whyLinkText;
    private String backLinkText;
    private double currentVersionPrivacy;
    private double currentVersionTC;

    @JsonProperty
    private Map<String, String> config;
    private String loginLinkHref;

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
                        hideLinks = ssoNode.get("hideLinks", false);
                        disableConfig = ssoNode.get("disableConfig", false);
                        whyLinkText = ssoNode.get("whyLinkText", "Why?");
                        backLinkText = ssoNode.get("backLinkText", "Back");
                        redirectNotLoggedInUserPath = ssoNode.get("redirectNotLoggedInUserPath", "");
                        loginLinkTitle = ssoNode.get("loginLinkTitle", "Login");
                        loginPagePath = ssoNode.get("loginPagePath", "");
                        logoutLinkTitle = ssoNode.get("logoutLinkTitle", "Logout");
                        currentVersionPrivacy = ssoNode.get("currentPrivacyPolicyVersion", 1.0);
                        currentVersionTC = ssoNode.get("currentTermsAndConditionsVersion", 1.0);

                        userRegistered = ssoNode.get("userRegistered", "");
                        userRegisteredNeedsVerification = ssoNode.get("userRegisteredNeedsVerification", "");
                        passwordReset = ssoNode.get("passwordReset", "");
                        userLoggedIn = ssoNode.get("userLoggedIn", "");
                        userLoggedOut = ssoNode.get("userLoggedOut", "");
                        accountVerified = ssoNode.get("accountVerified", "");
                        accountAlreadyVerified = ssoNode.get("accountAlreadyVerified", "");
//                        accountsMerged = ssoNode.get("accountsMerged", "");
                        verificationEmailSent = ssoNode.get("verificationEmailSent", "");
                        verificationNeeded = ssoNode.get("verificationNeeded", "");
                        verificationCodeExpired = ssoNode.get("verificationCodeExpired", "");
                        accountDeleted = ssoNode.get("accountDeleted", "");
                        emailUpdatedNeedsVerification = ssoNode.get("emailUpdatedNeedsVerification", "");
                        userSubscribed = ssoNode.get("userSubscribed", "");
                        userUnsubscribed = ssoNode.get("userUnsubscribed", "");
                        socialLoginFailed = ssoNode.get("socialLoginFailed", "");
                        userLoggedOutBySystem = ssoNode.get("userLoggedOutBySystem", "");
                        accountUpdatedPersonalInformation = ssoNode.get("accountUpdatedPersonalInformation", "");
                        accountUpdatedCommunicationPreferences = ssoNode.get("accountUpdatedCommunicationPreferences", "");

                        errorGeneric = ssoNode.get("errorGeneric", "");
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

        config.put("whyLinkText", StringUtils.remove(whyLinkText, " "));
        config.put("backLinkText", backLinkText);
        if (StringUtils.isNotBlank(redirectNotLoggedInUserPath)) {
            config.put("redirectNotLoggedInUserPath", LinkBuilderFactory.forPath(redirectNotLoggedInUserPath).build().getHref());
        }
        config.put("loginLinkTitle", loginLinkTitle);
        config.put("logoutLinkTitle", logoutLinkTitle);
        if (StringUtils.isNotBlank(loginPagePath)) {
            loginLinkHref = LinkBuilderFactory.forPath(loginPagePath).build().getHref();
            config.put("loginPagePath", loginLinkHref);
        }
        config.put("hideLinks", String.valueOf(hideLinks));
        config.put("currentVersionPrivacy", Double.toString(currentVersionPrivacy));
        config.put("currentVersionTC", Double.toString(currentVersionTC));

        //hardcoded for website channel
        config.put("mcdSourceApp", "WEB");

        String pageCountryCode = PageUtil.getCountry(currentPage);
        String pageLanguageCode = PageUtil.getLanguage(currentPage);
        if (pageCountryCode != null && pageLanguageCode != null) {
            McdFactoryConfig mcdFactoryConfig = mcdFactoryConfigConsumer.getMcdFactoryConfig(pageCountryCode, pageLanguageCode);
            if (mcdFactoryConfig != null) {
                config.put("mcdMarketID", mcdFactoryConfig.getDcsMarketId());
                config.put("mcdLocale", mcdFactoryConfig.getDcsLocale());
                config.put("apiKeyFacebook", mcdFactoryConfig.getApiKeyFacebook());
                config.put("facebookLocale", mcdFactoryConfig.getFacebookLocale());
                config.put("apiKeyGoogle", mcdFactoryConfig.getApiKeyGoogle());
            }
        }

        //this is the same config for all websites
        config.put("dcsApiDomain", webServicesConfig.getDcsApiDomain());
    }

    public boolean isIncludeLinksInHeader() {
        return !(hideLinks || disableConfig);
    }

    public boolean isIncludeConfig() {
        return !disableConfig;
    }

    public String getLoginLinkTitle() {
        return loginLinkTitle;
    }

    public String getLoginLinkHref() {
        return loginLinkHref;
    }

    public String getLogoutLinkTitle() {
        return logoutLinkTitle;
    }

	public String getPageNameForAnalytics() {
		return pageNameForAnalytics;
	}
}
