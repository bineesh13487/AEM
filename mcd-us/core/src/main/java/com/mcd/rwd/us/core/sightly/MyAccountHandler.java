package com.mcd.rwd.us.core.sightly;

import com.mcd.rwd.global.core.utils.PageUtil;
import com.mcd.rwd.us.core.constants.ApplicationConstants;
import com.mcd.rwd.global.core.sightly.McDUse;
import com.mcd.rwd.global.core.utils.LinkUtil;
import com.mcd.rwd.global.core.utils.MultiFieldPanelUtil;
import com.mcd.rwd.us.core.service.McdWebServicesConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by deepti_b on 12/1/2016.
 */
public class MyAccountHandler extends McDUse {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyAccountHandler.class);

    private String signInTitle;

    private String rememberMeText;

    private String signInButtonText;

    private String forgotPasswordText;

    private String registerText;

    private String welcomeText;

    private String updateProfileText;

    private String logoutText;

    private String epsilonURL;

    private String signInURL;
	
	private String signInRedirectLink;
	
	private String updateStoreId;

    private String forgotPassURL;

    private String registerUrl;

    private String updateProfileUrl;

    private String logoutUrl;

    private String emailReqMsg;

    private String passReqMsg;


    private String loginFailedMsg;

    private String emailPlaceholderText;

    private String passwordPlaceholderText;


    private String emailInvalidText;

    private String netErrorText;
    private String myAccountInfo;

    private String resetPassWordLink;

    private String changePassWordLink;

    private String activationErrorMsg;
	
	private String ecpccpsyncerror;
	

    private String myaccountImagePath;

    private String cookieDomainName;

    private String regexPattern;
    
    
    private String signInLink;
    
    private String mydealsLink;

    List<Map<String, String>> additionalLinks;
    
    private String termsAndConditionsPagePath;
    
    private String updateTermsLink;
    
    private String responseTermLink;

    private String pageNameForAnalytics = StringUtils.EMPTY;

    private String uuid;

    /**
     * Method to perform Post Initialization Tasks.
     */
    @Override
    public final void activate() {

        ValueMap properties = getSiteConfig("myaccount");
        Resource myaccountResource = getSiteConfigResource("myaccount");
        uuid = UUID.randomUUID().toString();

        if (properties != null) {
        	setSignInLink(properties.get("signinlink", String.class));
        	responseTermLink = properties.get("responseTermLink",String.class);
        	setMydealsLink(properties.get("mydealslink", String.class));
            signInTitle = properties.get("signinTitle", String.class);
            rememberMeText = properties.get("remembermeText", String.class);
            
            signInButtonText = properties.get("siginButtonText", String.class);
            forgotPasswordText = properties.get("forgotPassText", String.class);
            registerText = properties.get("registrationTitle", String.class);
            welcomeText = properties.get("welcomeTitle", String.class);
            updateProfileText = properties.get("updateProfileTitle", String.class);
            setTermsAndConditionsPagePath(properties.get("termsAndConditionsPagePath", ""));
            logoutText = properties.get("logoutTitle", String.class);
            McdWebServicesConfig mcdWebServicesConfig = getSlingScriptHelper().getService(McdWebServicesConfig.class);
            updateTermsLink = mcdWebServicesConfig.getUpdateTerm();
            epsilonURL=mcdWebServicesConfig.getEpsilonUrl();
            cookieDomainName=mcdWebServicesConfig.getCookiesDomain();
           // signInURL = properties.get("signinPath", String.class);
            signInURL = mcdWebServicesConfig.getSignInUrl();
			updateStoreId = mcdWebServicesConfig.getUpdateStore();
			signInRedirectLink = properties.get("signInRedirectLink",String.class);
            forgotPassURL = properties.get("forgotpassPath", String.class);
            registerUrl = properties.get("registerLink", String.class);
            updateProfileUrl = properties.get("updateProfileLink", String.class);
           // logoutUrl = properties.get("logoutPath", String.class);
            logoutUrl = mcdWebServicesConfig.getSignOutUrl();
            emailReqMsg = properties.get("emailReqText", String.class);
            passReqMsg = properties.get("passReqText", String.class);
            loginFailedMsg = properties.get("loginErrorMsg", String.class);
            emailPlaceholderText = properties.get("emailPlaceholder", String.class);
            passwordPlaceholderText = properties.get("passwordPlaceholder", String.class);
            myAccountInfo = properties.get("myaccountInfo", String.class);
            emailInvalidText = properties.get("emailInvalidText", String.class);
            netErrorText = properties.get("errorNetText", String.class);
            resetPassWordLink = properties.get("resetPassWordLink", String.class);
            changePassWordLink = properties.get("changePassLink", String.class);
            activationErrorMsg = properties.get("acctivateErrorText", String.class);
			ecpccpsyncerror = properties.get("ecpccpsyncerror", String.class);
			
            myaccountImagePath= LinkUtil.getImageHref(myaccountResource, "image");
            additionalLinks= populateAdditionalLinks(MultiFieldPanelUtil.getMultiFieldPanelValues(myaccountResource, "additionalLinks"));
            regexPattern=ApplicationConstants.REGEX_PATTERN;
            pageNameForAnalytics = PageUtil.getPageNameForAnalytics(getCurrentPage());
        }

    }


    public String getSignInLink() {
		return signInLink;
	}

	public void setSignInLink(String signInLink) {
		this.signInLink = signInLink;
	}

	public String getMydealsLink() {
		return mydealsLink;
	}

	public void setMydealsLink(String mydealsLink) {
		this.mydealsLink = mydealsLink;
	}


    private List<Map<String, String>> populateAdditionalLinks(List<Map<String, String>> urlsList){
        if (urlsList != null && !urlsList.isEmpty()) {
            Iterator<Map<String, String>> itr = urlsList.iterator();
            while (itr.hasNext()) {
                Map<String, String> item = itr.next();
                String linkPath=item.get(ApplicationConstants.PN_PATH);
                if (null != linkPath){
                    linkPath = linkPath.contains("/content") ? linkPath + ".html" : linkPath;
                    item.put(ApplicationConstants.PN_PATH,linkPath);
                }
            }
        }
        return urlsList;
    }

    public String getSignInTitle() {
        return signInTitle;
    }

    public String getRememberMeText() {
        return rememberMeText;
    }

    public String getSignInButtonText() {
        return signInButtonText;
    }

    public String getForgotPasswordText() {
        return forgotPasswordText;
    }

    public String getRegisterText() {
        return registerText;
    }

    public String getWelcomeText() {
        return welcomeText;
    }

    public String getUpdateProfileText() {
        return updateProfileText;
    }

    public String getLogoutText() {
        return logoutText;
    }

    public String getEpsilonURL() {
        return epsilonURL;
    }

    public String getSignInURL() {
        return signInURL;
    }
	public String getSignInRedirectLink() {
		return signInRedirectLink;
	}
	public String getUpdateStoreId() {
        return updateStoreId;
    }

    public String getForgotPassURL() {
        return forgotPassURL;
    }

    public String getRegisterUrl() {
        return registerUrl;
    }

    public String getUpdateProfileUrl() {
        return updateProfileUrl;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public String getEmailReqMsg() {
        return emailReqMsg;
    }

    public String getPassReqMsg() {
        return passReqMsg;
    }

    public String getLoginFailedMsg() {
        return loginFailedMsg;
    }


    public String getEmailPlaceholderText() {
        return emailPlaceholderText;
    }

    public String getPasswordPlaceholderText() {
        return passwordPlaceholderText;
    }

    public String getMyAccountInfo() {
        return myAccountInfo;
    }


    public String getEmailInvalidText() {
        return emailInvalidText;
    }


    public String getNetErrorText() {
        return netErrorText;
    }


    public String getResetPassWordLink() {
        return resetPassWordLink;
    }

    public String getChangePassWordLink() {
        return changePassWordLink;
    }

    public String getActivationErrorMsg() {
        return activationErrorMsg;
    }
	 public String getEcpccpsyncerror() {
        return ecpccpsyncerror;
    }


    public String getMyaccountImagePath() {
        return myaccountImagePath;
    }


    public String getCookieDomainName() {
        return cookieDomainName;
    }

    public String getRegexPattern() {
        return regexPattern;
    }


    public List<Map<String, String>> getAdditionalLinks() {
        return additionalLinks;
    }

	public String getTermsAndConditionsPagePath() {
		return termsAndConditionsPagePath;
	}

	public void setTermsAndConditionsPagePath(String termsAndConditionsPagePath) {
		this.termsAndConditionsPagePath = termsAndConditionsPagePath;
	}

	public String getUpdateTermsLink() {
		return updateTermsLink;
	}

	public void setUpdateTermsLink(String updateTermsLink) {
		this.updateTermsLink = updateTermsLink;
	}

	public String getResponseTermLink() {
		return responseTermLink;
	}

	public void setResponseTermLink(String responseTermLink) {
		this.responseTermLink = responseTermLink;
	}

    public String getPageNameForAnalytics() {
        return pageNameForAnalytics;
    }

    public String getUuid() {
        return uuid;
    }
}
