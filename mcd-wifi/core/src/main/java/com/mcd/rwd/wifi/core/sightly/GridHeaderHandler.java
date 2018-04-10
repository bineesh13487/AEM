package com.mcd.rwd.wifi.core.sightly;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

/**
 * Created by Krupa on 18/07/17.
 */

@Component(name = "grid-header", value = "Grid Header",
        description = "Grid Header for bottom half of the page. ",
        tabs = {
                @Tab( touchUINodeName = "gridHeader" , title = "Grid Header" )
        },
        actions = { "text: Grid Header", "-", "editannotate", "copymove", "delete" },
        allowedParents = "[*/parsys]",
        group = "McD-Wifi", path = "content",
        listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
                @Listener(name = "afterinsert", value = "REFRESH_PAGE")})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class GridHeaderHandler {

        @DialogField(fieldLabel = "Grid Header Text", fieldDescription = "Enter the grid header text for the bottom half of page.",
             additionalProperties = {@Property(name = "boxMaxWidth", value = "{Long}400"),
                @Property(name = "maxLength", value = "{Long}50"),
                @Property(name = "maxLengthText", value = "Please enter short string below 50 characters. ")})
        @TextField
        @Inject @Default(values = StringUtils.EMPTY)
        private String gridHeaderText;

        @DialogField(fieldLabel = "Text Color", fieldDescription = "Choose hex-code for text color. (optional)")
        @TextField
        @Inject @Default(values = StringUtils.EMPTY)
        private String textColor;

        @DialogField(fieldLabel = "Background Color", fieldDescription = "Choose hex-code for background color. (optional)")
        @TextField
        @Inject @Default(values = StringUtils.EMPTY)
        private String bgColor;

        public String getGridHeaderText() {
                return gridHeaderText;
        }

        public String getTextColor() {
                return textColor;
        }

        public String getBgColor() {
                return bgColor;
        }
}
