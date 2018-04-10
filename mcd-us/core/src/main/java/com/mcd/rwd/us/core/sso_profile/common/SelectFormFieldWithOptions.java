package com.mcd.rwd.us.core.sso_profile.common;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.widgets.MultiField;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.icfolson.aem.library.core.constants.ComponentConstants;
import com.mcd.rwd.us.core.constants.SSOProfileConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import javax.inject.Inject;
import java.util.List;

@Component(
        value = "Select Form Field With Options",
        path = SSOProfileConstants.SSO_COMPONENTS_PATH + "/common",
        disableTargeting = true,
        editConfig = false,
        group = ComponentConstants.GROUP_HIDDEN)
@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class SelectFormFieldWithOptions extends SelectFormField {

    //TODO replace with a multicomposite field as an enhancement
    @DialogField(
            fieldLabel = "Drop down options",
            name = "./options",
            fieldDescription = "For each option, include the value (which is saved to the database) followed by  double colon <code>::</code> "
                    + "followed by label (which is displayed to the user).<br/>E.g.: <i>databaseValue::Displayable Label</i>",
            ranking = SSOProfileConstants.RANKING_FOUR)
    @TextField
    @MultiField(addItemLabel = "Add Option")
    @Inject
    @Default(values = "[]") @Optional
    private List<String> options;

//    @DialogField(
//            name = "./" + JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY,
//            defaultValue = SSOProfileConstants.RESOURCE_SELECT_FORM_FIELD_WITH_OPTIONS_COMPONENT)
//    @Hidden(value = SSOProfileConstants.RESOURCE_SELECT_FORM_FIELD_WITH_OPTIONS_COMPONENT)
//    private String resourceType;

    public List<String> getOptions() {
        return options;
    }
}
