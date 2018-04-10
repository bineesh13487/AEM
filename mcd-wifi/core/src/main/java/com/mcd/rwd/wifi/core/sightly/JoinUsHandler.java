package com.mcd.rwd.wifi.core.sightly;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Listener;
import com.citytechinc.cq.component.annotations.Tab;
import com.citytechinc.cq.component.annotations.widgets.DialogFieldSet;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Created by Krupa on 18/07/17.
 */

@Component(name = "join-us", value = "Join Us Local McD",
        description = "Grid Header for bottom half of the page. ",
        tabs = {
                @Tab( touchUINodeName = "tab1" , title = "Tile Configuration" )
        },
        resourceSuperType = "foundation/components/parbase",
        actions = { "text: Join Us", "-", "editannotate", "copymove", "delete" },
        allowedParents = "[*/parsys]",
        group = "McD-Wifi", path = "content",
        listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
                @Listener(name = "afterinsert", value = "REFRESH_PAGE")})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class JoinUsHandler {

        @DialogField(fieldLabel = "Address Line 1")
        @DialogFieldSet(collapsible = true, collapsed = false, namePrefix = "joinus/", title = "Join Us Tile")
        @Inject
        @ChildResource(name = "joinus")
        private JoinUsTile joinus;

        private String defaultAltText = StringUtils.EMPTY;
        private String defaultTarget = StringUtils.EMPTY;
        private String defaultTitle = StringUtils.EMPTY;
        private String defaultTextAlign = StringUtils.EMPTY;
        private String defaultImagePath = StringUtils.EMPTY;
        private String defaultURL = StringUtils.EMPTY;
        private String defaultColorpicker = StringUtils.EMPTY;

        public String getDefaultAltText() {
                return defaultAltText;
        }

        public String getDefaultTarget() {
                return defaultTarget;
        }

        public String getDefaultTitle() {
                return defaultTitle;
        }

        public String getDefaultTextAlign() {
                return defaultTextAlign;
        }

        public String getDefaultImagePath() {
                return defaultImagePath;
        }

        public String getDefaultURL() {
                return defaultURL;
        }

        public String getDefaultColorpicker() {
                return defaultColorpicker;
        }

        @PostConstruct
        public void init(){
                if(null!=joinus) {
                        defaultAltText = joinus.getImagealttext();
                        defaultTarget = joinus.getTarget();
                        defaultTitle = joinus.getImagetitle();
                        defaultTextAlign = joinus.getTextalign();
                        defaultImagePath = joinus.getImagepath();
                        defaultURL = joinus.getDefaulturl();
                        defaultColorpicker = joinus.getColorpicker();
                }
        }
}
