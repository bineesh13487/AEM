package com.mcd.rwd.us.core.sso_profile.common;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.widgets.DialogFieldSet;
import com.citytechinc.cq.component.annotations.widgets.Hidden;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.icfolson.aem.library.api.link.Link;
import com.icfolson.aem.library.api.node.ComponentNode;
import com.icfolson.aem.library.core.constants.ComponentConstants;
import com.icfolson.aem.library.models.annotations.LinkInject;
import com.mcd.rwd.us.core.constants.SSOProfileConstants;
import com.mcd.rwd.us.core.sso_profile.model.DirectiveInput;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.jcr.resource.JcrResourceConstants;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.LinkedList;
import java.util.List;

@Component(
        value = "Privacy and Terms & Conditions",
        path = SSOProfileConstants.SSO_COMPONENTS_PATH + "/common",
        disableTargeting = true,
        editConfig = false,
        group = ComponentConstants.GROUP_HIDDEN)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class PrivacyAndTC {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrivacyAndTC.class);
    private static final String LINK_FORMAT = "<a href=\"{{href}}\" title=\"{{title}}\" target=\"_blank\">{{title}}</a>";

    private static final String PROP_PRIVACY_PAGE_PATH = "privacyPage";
    private static final String PROP_TERMS_CONDITIONS_PAGE_PATH = "termsAndConditionsPage";

    @DialogField(
            fieldLabel = "Privacy Policy page path",
            name = "./" + PROP_PRIVACY_PAGE_PATH,
            fieldDescription = "Path to the privacy policy page.",
            ranking = SSOProfileConstants.RANKING_ZERO,
            required = true)
    @PathField(rootPath = SSOProfileConstants.CONTENT_ROOT)
    @LinkInject(titleProperty = "privacyPolicyLinkTitle") @Named(PROP_PRIVACY_PAGE_PATH) @Optional
    private Link privacyPolicyLink;

    @DialogField(
            fieldLabel = "Privacy Policy link title",
            name = "./privacyPolicyLinkTitle",
            ranking = SSOProfileConstants.RANKING_ONE,
            required = true
    )
    @TextField
    @Inject @Default(values = "")
    private String privacyPolicyLinkTitle;

    @DialogField(xtype = "displayfield", hideLabel = true,
            fieldDescription = "The label for 'Privacy Policy' checkbox can include a <code>{{privacy}}</code> placeholder that will be replaced "
                    + "with the Privacy Policy link.<br/>"
                    + "E.g.:<i>I agree to the {{privacy}}.</i>",
            ranking = SSOProfileConstants.RANKING_TWO)
    private String privacyNote;

    @DialogField(fieldLabel = "Privacy Policy checkbox", ranking = SSOProfileConstants.RANKING_THREE)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "privacy/", title = "Privacy Policy checkbox related properties")
    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private CheckBoxFormField privacyPolicyCheckbox;

    @DialogField(
            fieldLabel = "Terms & Conditions page path",
            name = "./" + PROP_TERMS_CONDITIONS_PAGE_PATH,
            fieldDescription = "Path to the terms and conditions page.",
            ranking = SSOProfileConstants.RANKING_FOUR,
            required = true)
    @PathField(rootPath = SSOProfileConstants.CONTENT_ROOT)
    @LinkInject(titleProperty = "termsAndConditionsLinkTitle") @Named(PROP_TERMS_CONDITIONS_PAGE_PATH) @Optional
    private Link termsAndConditionsLink;

    @DialogField(
            fieldLabel = "Terms & Conditions link title",
            name = "./termsAndConditionsLinkTitle",
            ranking = SSOProfileConstants.RANKING_FIVE,
            required = true
    )
    @TextField
    @Inject @Default(values = "")
    private String termsAndConditionsLinkTitle;

    @DialogField(xtype = "displayfield", hideLabel = true,
            fieldDescription = "The label for 'Terms & Conditions' checkbox can include a <code>{{terms}}</code> placeholder that will be replaced "
                    + "with the Terms & Conditions link.<br/>"
                    + "E.g.:<i>I agree to the {{terms}}.</i>",
            ranking = SSOProfileConstants.RANKING_SIX)
    private String termsNote;

    @DialogField(fieldLabel = "Terms & Conditions checkbox", ranking = SSOProfileConstants.RANKING_SEVEN)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "terms/", title = "Terms & Conditions checkbox related properties")
    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private CheckBoxFormField termsAndConditionsCheckbox;

    @DialogField(
            fieldLabel = "Combine into single checkbox",
            name = "./singleCheckbox",
            fieldDescription = "Indicates whether Privacy Policy and Terms & Conditions should be combined into a single checkbox.<br/>"
                    + "When checked, a single checkbox will be rendered and 'Combined checkbox' properties must be defined.",
            ranking = SSOProfileConstants.RANKING_EIGHT
    )
    @Selection(type = "checkbox")
    @Inject @Default(values = "false")
    private boolean singleCheckbox;

    @DialogField(xtype = "displayfield", hideLabel = true,
            fieldDescription = "The label for the Privacy Policy and Terms & Conditions combined checkbox can include <code>{{privacy}}</code> and "
                    + "<code>{{terms}}</code> placeholders that will be replaced with the Privacy Policy and Terms & Conditions links respectively.<br/>"
                    + "E.g.:<i>By registering you agree to our {{privacy}} and {{terms}}.</i>",
            ranking = SSOProfileConstants.RANKING_NINE)
    private String combinedNote;

    @DialogField(fieldLabel = "Combined checkbox", ranking = SSOProfileConstants.RANKING_TEN)
    @DialogFieldSet(collapsible = true, collapsed = true, namePrefix = "combined/",
            title = "Privacy Policy and Terms & Conditions combined checkbox related properties")
    @ChildResource(injectionStrategy = InjectionStrategy.OPTIONAL)
    private CheckBoxFormField combinedCheckbox;

    @DialogField(
            name = "./" + JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY,
            defaultValue = SSOProfileConstants.RESOURCE_PRIVACYANDTC_COMPONENT)
    @Hidden(value = SSOProfileConstants.RESOURCE_PRIVACYANDTC_COMPONENT)
    private String resourceType;

    @Inject
    private ComponentNode componentNode;

    private List<DirectiveInput> directiveInputs = new LinkedList<>();

    @PostConstruct
    private void init() {

        try {
            String privacyLink = StringUtils.replace(LINK_FORMAT, "{{href}}", privacyPolicyLink != null ? privacyPolicyLink.getHref() : "");
            privacyLink = StringUtils.replace(privacyLink, "{{title}}", privacyPolicyLinkTitle);

            String termsLink = StringUtils.replace(LINK_FORMAT, "{{href}}", termsAndConditionsLink != null ? termsAndConditionsLink.getHref() : "");
            termsLink = StringUtils.replace(termsLink, "{{title}}", termsAndConditionsLinkTitle);

            if (singleCheckbox && componentNode.getComponentNode("combined").isPresent()) {
                final ComponentNode checkboxNode = componentNode.getComponentNode("combined").get();
                if (checkboxNode.get("included", false)) {
                    //create a single directive input
                    final DirectiveInput directiveInput = new DirectiveInput(checkboxNode);
                    directiveInput.setType("checkbox");
                    //replace placeholders in the label with the links
                    String label = directiveInput.getLabel();
                    label = StringUtils.replace(label, "{{privacy}}", privacyLink);
                    label = StringUtils.replace(label, "{{terms}}", termsLink);
                    directiveInputs.add(directiveInput.setLabel(label));
                }
            } else {
                //separate checkboxes for Privacy Policy and Terms & Conditions
                if (componentNode.getComponentNode("privacy").isPresent()) {
                    final ComponentNode checkboxNode = componentNode.getComponentNode("privacy").get();
                    if (checkboxNode.get("included", false)) {
                        final DirectiveInput directiveInput = new DirectiveInput(checkboxNode);
                        directiveInput.setType("checkbox");
                        //replace placeholder in the label with the link
                        final String label = StringUtils.replace(directiveInput.getLabel(), "{{privacy}}", privacyLink);
                        directiveInputs.add(directiveInput.setLabel(label));
                    }
                }
                if (componentNode.getComponentNode("terms").isPresent()) {
                    final ComponentNode checkboxNode = componentNode.getComponentNode("terms").get();
                    if (checkboxNode.get("included", false)) {
                        final DirectiveInput directiveInput = new DirectiveInput(checkboxNode);
                        directiveInput.setType("checkbox");
                        //replace placeholder in the label with the link
                        final String label = StringUtils.replace(directiveInput.getLabel(), "{{terms}}", termsLink);
                        directiveInputs.add(directiveInput.setLabel(label));
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("exception in init {}", e);
        }
    }

    public List<DirectiveInput> getDirectiveInputs() {
        return directiveInputs;
    }
}
