package com.mcd.rwd.global.core.bean;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.widgets.Html5SmartFile;

/**
 * Created by sandeepc on 18/07/17.
 */

public interface VideoFormats {

    @DialogField(tab = 2, fieldLabel = "OGG Video", name = "./ogg/file", additionalProperties = {
            @Property(name = "mimeTypes", value = "video/ogg"),
            @Property(name = "fileNameParameter", value = "./ogg/fileName"),
            @Property(name = "fileReferenceParameter", value = "./ogg/fileReference")
    })
    @Html5SmartFile(ddAccept = "video/ogg", ddGroups = "media", sizeLimit=1000, fileNameParameter = "./ogg/fileName",
            fileReferenceParameter = "./ogg/fileReference")
    String getOgg();

    @DialogField(tab = 2, fieldLabel = "Webm Video", name = "./webm/file", additionalProperties = {
            @Property(name = "mimeTypes", value = "video/webm"),
            @Property(name = "fileNameParameter", value = "./webm/fileName"),
            @Property(name = "fileReferenceParameter", value = "./webm/fileReference")
    })
    @Html5SmartFile(ddAccept = "video/webm", ddGroups = "media", sizeLimit=1000, fileNameParameter = "./webm/fileName",
            fileReferenceParameter = "./webm/fileReference")
    String getWebm();

}
