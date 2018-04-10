package com.mcd.rwd.us.core.sightly.callouts;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Option;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.icfolson.aem.multicompositeaddon.widget.MultiCompositeField;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by sandeepc on 03/07/17.
 */

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CalloutRes {

    @DialogField(fieldLabel = "Width*", name = "./width", additionalProperties = {
            @Property(name = "emptyOption", value = "true")
    })
    @Selection(type = Selection.SELECT, options = {
            @Option(text = "~25%", value = "3"),@Option(text = "~33%", value = "4"),@Option(text = "~50%", value = "6")
    })
    @Inject
    private String width;

    @DialogField(fieldLabel = "Promo Paths*", name = "./promo")
    @MultiCompositeField
    @ChildResource(name = "promo")
    @Inject
    private List<CallOutPromo> promoList;

    public String getWidth() {
        return width;
    }

    public List<CallOutPromo> getPromoList() {
        return promoList;
    }

}

