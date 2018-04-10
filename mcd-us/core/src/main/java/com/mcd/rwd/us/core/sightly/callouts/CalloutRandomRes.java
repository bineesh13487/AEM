package com.mcd.rwd.us.core.sightly.callouts;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Option;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.widgets.MultiField;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.us.core.constants.PromoConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

/**
 * Created by sandeepc on 04/07/17.
 */

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CalloutRandomRes {

    @DialogField(fieldLabel = "Width*", name = "./" + ApplicationConstants.PN_WIDTH, additionalProperties = {
            @Property(name = "emptyOption", value = "true")
    })
    @Selection(type = Selection.SELECT, options = {
            @Option(text = "~25%", value = "3"),@Option(text = "~33%", value = "4"),@Option(text = "~50%", value = "6")
    })
    @Inject
    private String width;

    @DialogField(fieldLabel = "Promo Paths*", name = "./" + PromoConstants.PN_PATHS)
    @MultiField
    @PathField
    @Inject
    private String[] paths;

}
