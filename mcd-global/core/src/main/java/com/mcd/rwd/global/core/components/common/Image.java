package com.mcd.rwd.global.core.components.common;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.editconfig.DropTarget;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.day.cq.dam.api.DamConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by sandeepc on 12/06/17.
 */

@Component(
        value = Image.COMPONENT_TITLE,
        actions = { "text: "+ Image.COMPONENT_TITLE, "-", "editannotate", "copymove", "delete" },
        path = "common",
        disableTargeting = true,
        editConfig = false,
        group = "GWS-Global",
        dropTargets = {
            @DropTarget(propertyName = "./imagePath", accept = {"image/.*"}, groups = {"media"}, nodeName = "image")
        },
        resourceSuperType = "foundation/components/image",
        inPlaceEditingEditorType = "image"
        )
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Image {

    public static final String COMPONENT_TITLE = "Image Component";
    public static final String COMMON_IMAGE_COMPONENT = "mcd-rwd-global/components/common/image";

    @DialogField(fieldLabel = "Image Path", name = "./imagePath")
    @PathField(rootPath = DamConstants.MOUNTPOINT_ASSETS, rootTitle = "DAM")
    @Inject @Named("imagePath") @Default(values = "")
    private String imagePath;

    @DialogField(fieldLabel = "Image Title", name = "./imgTitle")
    @TextField
    @Inject @Named("imgTitle") @Default(values = "Image")
    private String imgTitle;

    @DialogField(fieldLabel = "Alt Text", name = "./altText")
    @TextField
    @Inject @Named("imgTitle") @Default(values = "Image")
    private String altText;

    @Inject
    @Self
    private Resource resource;
    private com.day.cq.wcm.foundation.Image imageModel;

    @PostConstruct
    public void postConstruct() {
        if (resource != null) {
            imageModel = new com.day.cq.wcm.foundation.Image(resource);
        }
    }

    public String getImagePath() {
        return imageModel != null && imageModel.hasContent() ? resource.getPath() + ".img.png" : imagePath;
    }

    public String getImgTitle() {
        return imgTitle;
    }

    public String getAltText() {
        return altText;
    }

}
