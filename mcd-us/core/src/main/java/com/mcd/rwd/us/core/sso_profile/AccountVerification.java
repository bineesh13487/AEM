package com.mcd.rwd.us.core.sso_profile;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.icfolson.aem.library.api.link.Link;
import com.icfolson.aem.library.core.constants.ComponentConstants;
import com.icfolson.aem.library.models.annotations.LinkInject;
import com.mcd.rwd.us.core.constants.SSOProfileConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
        value = AccountVerification.COMPONENT_TITLE,
        path = SSOProfileConstants.SSO_COMPONENTS_PATH,
        actions = {
                ComponentConstants.EDIT_BAR_TEXT + AccountVerification.COMPONENT_TITLE,
                ComponentConstants.EDIT_BAR_SPACER,
                ComponentConstants.EDIT_BAR_EDIT + ComponentConstants.EDIT_BAR_ANNOTATE,
                ComponentConstants.EDIT_BAR_COPY_MOVE,
                ComponentConstants.EDIT_BAR_DELETE,
                ComponentConstants.EDIT_BAR_SPACER,
                ComponentConstants.EDIT_BAR_INSERT },
        disableTargeting = true,
        dialogWidth = SSOProfileConstants.DIALOG_DEFAULT_WIDTH
)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public final class AccountVerification implements Serializable {

    public static final String COMPONENT_TITLE = "Account Verification";

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountVerification.class);

    @DialogField(
            fieldLabel = "Page to redirect on success",
            name = "./redirectPage",
            fieldDescription = "Path to the page (or fully qualified URL) where user should be redirected when they successfully verify their account.",
            required = true)
    @PathField(rootPath = SSOProfileConstants.CONTENT_ROOT)
    @LinkInject @Optional
    private Link redirectPage;

//    @DialogField(
//            fieldLabel = "Error 11922",
//            name = "./error11922",
//            fieldDescription = "Supplied verification code has expired.<br/>"
//                    + "You can place a CTA within the error text. Wrap any portion of the text within double square brackets <code>[[...]]</code> "
//                    + "and it will be turned into a link that will re-send the verification email.<br/>"
//                    + "E.g.: <i>The code within your verification email has expired. Click [[here]] to re-send verification email.</i>",
//            required = true)
//    @TextArea
//    @Inject @Default(values = "")
//    @JsonProperty
//    private String error11922;
//
//    @DialogField(
//            fieldLabel = "Error 11415",
//            name = "./error11415",
//            fieldDescription = "The supplied authorization_code has expired.<br/>"
//                    + "You can place a CTA within the error text. Wrap any portion of the text within double square brackets <code>[[...]]</code> "
//                    + "and it will be turned into a link that will re-send the verification email.<br/>"
//                    + "E.g.: <i>The code within your verification email has expired. Click [[here]] to re-send verification email.</i>",
//            required = true)
//    @TextArea
//    @Inject @Default(values = "")
//    @JsonProperty
//    private String error11415;
//
//    @DialogField(
//            fieldLabel = "Error 11920",
//            name = "./error11920",
//            fieldDescription = "Supplied verification code does not match.<br/>"
//                    + "You can place a CTA within the error text. Wrap any portion of the text within double square brackets <code>[[...]]</code> "
//                    + "and it will be turned into a link that will re-send the verification email.<br/>"
//                    + "E.g.: <i>The code within your verification email has expired. Click [[here]] to re-send verification email.</i>",
//            required = true)
//    @TextArea
//    @Inject @Default(values = "")
//    @JsonProperty
//    private String error11920;

    public String getRedirectPage() {
        return redirectPage != null ? redirectPage.getHref() : "";
    }
}
