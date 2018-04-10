package com.mcd.rwd.us.core.sightly;

import com.mcd.rwd.us.core.constants.ApplicationConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import com.mcd.rwd.global.core.sightly.McDUse;
import com.mcd.rwd.us.core.constants.PromoConstants;
import com.mcd.rwd.us.core.service.McdWebServicesConfig;

/**
 * Created by deepti_b on 12/2/2016.
 */
public class SignInHandler extends McDUse {

    private String emailPlaceholder;

    private String passwordPlaceholder;

    private String remembermeText;

    private String signinButtonText;

    private String signinLink;

    private String registerText;

    private String registerLink;

    private String forgotPassText;

    private String forgotPassLink;

    private String emailReqText;

    private String passReqText;

    private String loginFailedMsg;

    private String epsilonURL;

    private String emailInvalidMsg;

    private String netErrorMsg;

    private String activationErrorMsg;


    private String resetPasswordLink;

    private String changePasswordLink;

    private String cookiesDomain;

    private boolean popupCheck;
    private String signinTitle;
    private String promoPath;

    private String redirectLink;

    private String regexPattern;
    /**
     * Method to perform Post Initialization Tasks.
     */
    @Override public final void activate() {
        ValueMap properties = getProperties();
        ValueMap styleProperties = getSiteConfig("myaccount");
        
        emailPlaceholder = styleProperties.get("emailSigninPlaceholder", String.class);
        passwordPlaceholder = styleProperties.get("passwordSigninPlaceholder", String.class);
        remembermeText = styleProperties.get("remembermeText", String.class);
        signinButtonText = styleProperties.get("siginButtonText", String.class);
        registerText = styleProperties.get("registrationTitle", String.class);
        registerLink = styleProperties.get("registerLink", String.class);
        forgotPassText = styleProperties.get("forgotPassText", String.class);
        forgotPassLink = styleProperties.get("forgotpassPath", String.class);
        emailReqText = styleProperties.get("emailReqText", String.class);
        passReqText = styleProperties.get("passReqText", String.class);
        loginFailedMsg = styleProperties.get("loginErrorMsg", String.class);
        emailInvalidMsg = styleProperties.get("emailInvalidText", String.class);
        netErrorMsg = styleProperties.get("errorNetMsg", String.class);
        McdWebServicesConfig mcdWebServicesConfig = getSlingScriptHelper().getService(McdWebServicesConfig.class);
        epsilonURL=mcdWebServicesConfig.getEpsilonUrl();
        signinLink = mcdWebServicesConfig.getSignInUrl();
        cookiesDomain=mcdWebServicesConfig.getCookiesDomain();
        activationErrorMsg = styleProperties.get("acctivateErrorText", String.class);
        resetPasswordLink = styleProperties.get("resetPassWordLink", String.class);
        changePasswordLink = styleProperties.get("changePassLink", String.class);
        signinTitle = styleProperties.get("popupSignInTitle", String.class);
        regexPattern= ApplicationConstants.REGEX_PATTERN;
        redirectLink = properties.get("redirectPath", String.class);
        if (null != redirectLink){
            redirectLink = redirectLink.contains("/content") ? redirectLink + ".html" : redirectLink;
        }

        popupCheck = properties.get("signinPopup", false);
        if(popupCheck){
            String promo = properties.get("promoPath", String.class);
            Resource promoResource = getResourceResolver()
                    .getResource(promo + PromoConstants.JCR_CONTENT_PROMO);
            if (promoResource != null) {
                promoPath = promo + PromoConstants.JCR_CONTENT_PROMO;
            }
        }
    }

    public String getEmailPlaceholder() {
        return emailPlaceholder;
    }

    public String getPassReqText() {
        return passReqText;
    }

    public String getPasswordPlaceholder() {
        return passwordPlaceholder;
    }

    public String getRemembermeText() {
        return remembermeText;
    }

    public String getSigninButtonText() {
        return signinButtonText;
    }

    public String getSigninLink() {
        return signinLink;
    }

    public String getForgotPassText() {
        return forgotPassText;
    }

    public String getRegisterLink() {
        return registerLink;
    }

    public String getForgotPassLink() {
        return forgotPassLink;
    }

    public String getEmailReqText() {
        return emailReqText;
    }

    public String getRegisterText() {
        return registerText;
    }


    public String getLoginFailedMsg() {
        return loginFailedMsg;
    }

    public String getEpsilonURL() {
        return epsilonURL;
    }


    public String getEmailInvalidMsg() {
        return emailInvalidMsg;
    }


    public String getNetErrorMsg() {
        return netErrorMsg;
    }

    public String getActivationErrorMsg() {
        return activationErrorMsg;
    }

    public String getResetPasswordLink() {
        return resetPasswordLink;
    }

    public String getChangePasswordLink() {
        return changePasswordLink;
    }

    public String getCookiesDomain() {
        return cookiesDomain;
    }


    public boolean isPopupCheck() {
        return popupCheck;
    }


    public String getSigninTitle() {
        return signinTitle;
    }


    public String getPromoPath() {
        return promoPath;
    }

    public String getRedirectLink() {
        return redirectLink;
    }


    public String getRegexPattern() {
        return regexPattern;
    }


}
