package com.mcd.rwd.us.core.sso_profile.common;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.icfolson.aem.library.core.constants.ComponentConstants;
import com.mcd.rwd.us.core.constants.SSOProfileConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

@Component(
        value = "Text Form Field No Include",
        path = SSOProfileConstants.SSO_COMPONENTS_PATH + "/common",
        disableTargeting = true,
        editConfig = false,
        group = ComponentConstants.GROUP_HIDDEN)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class TextFormFieldNoIncl extends TextFormFieldBase {

    @DialogField(
            fieldLabel = "Make this field required",
            name = "./required",
            fieldDescription = "Check this checkbox if this field should be required.",
            ranking = SSOProfileConstants.RANKING_THREE
    )
    @Selection(type = "checkbox")
    @Inject
    @Default(values = "false")
    private boolean required;

    @DialogField(
            fieldLabel = "Required Error Message",
            name = "./requiredError",
            fieldDescription = "Error message to show when required field validation fails.",
            additionalProperties = @Property(name = "emptyText", value = SSOProfileConstants.FIELD_REQUIRED_DEFAULT_ERROR),
            ranking = SSOProfileConstants.RANKING_FOUR
    )
    @TextField
    @Inject @Default(values = SSOProfileConstants.FIELD_REQUIRED_DEFAULT_ERROR)
    private String requiredError;

//    @DialogField(
//            name = "./" + JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY,
//            defaultValue = SSOProfileConstants.RESOURCE_TEXT_FORM_FIELD_NO_INCL_COMPONENT)
//    @Hidden(value = SSOProfileConstants.RESOURCE_TEXT_FORM_FIELD_NO_INCL_COMPONENT)
//    private String resourceType;

    public boolean isRequired() {
        return required;
    }

    public String getRequiredError() {
        return requiredError;
    }
}
