package com.mcd.rwd.global.core.bean;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Option;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.widgets.CheckBox;
import com.citytechinc.cq.component.annotations.widgets.MultiField;
import com.citytechinc.cq.component.annotations.widgets.Selection;

/**
 * Created by sandeepc on 18/07/17.
 */

public interface VideoSettings {

    @DialogField(tab = 2, additionalProperties = @Property(name = "value", value = "true"))
    @CheckBox(text = "Pre-load")
    boolean isPreload();

    @DialogField(tab = 2, fieldDescription = "Background videos are muted by default. Applicable only for " +
            "normal and lightbox videos.", additionalProperties = @Property(name = "value", value = "true"))
    @CheckBox(text = "Mute")
    boolean isMuted();

    @DialogField(tab = 2, fieldLabel = "Social Share", fieldDescription = "Please select any social share option " +
            "- Facebook, Twitter and Google +")
    @MultiField
    @Selection(type = Selection.SELECT, options = {
            @Option(text = "Facebook", value = "fb=https://www.facebook.com/sharer.php?u"),
            @Option(text = "Twitter", value = "twt=https://twitter.com/intent/tweet?url"),
            @Option(text = "Google+", value = "gplus=https://plus.google.com/share?url")
    })
    String[] getShare();

}
