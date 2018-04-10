package com.mcd.rwd.global.core.bean;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Option;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.widgets.*;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by apple on 18/07/17.
 */
@Model(adaptables = { Resource.class ,SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public interface PublicationAdvancedImageItems {
    @DialogField( tab = 4, fieldDescription="Enter HTTP Path for Image",fieldLabel="HTTP Path of Image", ranking = 1D)
    @TextField
    String getExternalImagePath();

    @DialogField( fieldDescription="Enter alternate text (or title) for the image", fieldLabel="Alt Text/Title",
        ranking = 2D)
    @TextField
    String getImageAlt();

    @DialogField(value = "left", fieldLabel="Image Size", ranking = 3D)
    @Selection(type = Selection.RADIO,
            options = {
                    @Option(text="25%" , value="25"),
                    @Option(text="50%" , value="50"),
                    @Option(text="75%" , value="75"),
                    @Option(text="100%" , value="100")
            })
     String getImageSize();

    @DialogField(fieldLabel = "Upload a file for downloading", ranking = 4D,
    name = "./file", additionalProperties = {
        @Property(name = "fileNameParameter", value = "./fileName"),
        @Property(name = "fileReferenceParameter", value = "./fileReference")
    })
    @Html5SmartFile(ddAccept = "*", ddGroups = "media", fileNameParameter = "./fileName",
            fileReferenceParameter = "./fileReference", mimeTypes = "*.*")
    //@ChildResource(name = "uploadfile")
    @Inject
    @Named("fileReference")
    String getFile();

    @DialogField(fieldLabel="Create Thumbnail", ranking = 5D, additionalProperties =
            {@Property(name = "width", value = "100"), @Property(name = "value", value = "true")})
    @CheckBox(text = "Create Thumbnail")
    boolean getCreateThumbnail();

    @DialogField( fieldLabel="Image Link" , ranking = 6D)
    @PathField
    String getImageLink();

    @DialogField(value = "bottomLeft", fieldDescription="Choose whether the image is on the right or left of the text",
            fieldLabel="Image Position", ranking = 7D)
    @Selection(type = Selection.RADIO,
            options = {
                    @Option(text="Bottom-Left" , value="bottomLeft"),
                    @Option(text="Bottom (centered)" , value="bottom"),
                    @Option(text="Bottom-Right" , value="bottomRight"),
                    @Option(text="Left" , value="left"),
                    @Option(text="Right" , value="right"),
                    @Option(text="Top-Left" , value="topLeft"),
                    @Option(text="Top (centered)" , value="top"),
                    @Option(text="Top-Right" , value="topRight")

            })
    String getImagePosition();

    @DialogField(  fieldLabel="Caption Text" , ranking = 8D)
    @TextArea
    String getCaption();


    @DialogField( value = "left", fieldDescription="Choose how the caption text should " +
            "be vertically aligned with the image",fieldLabel="Caption Alignment", ranking = 9D)
    @Selection(type = Selection.RADIO,
            options = {
                    @Option(text="Left" , value="left"),
                    @Option(text="Center" , value="center"),
                    @Option(text="Right" , value="right")
            })
     String getCaptionAlignment();


    @DialogField(  fieldDescription="Enter top padding to be included in the image. Default value 0px",
            fieldLabel="Top Padding", ranking = 10D)
    @NumberField
    Double getPaddingTopImage();

    @DialogField( fieldDescription="Enter bottom padding to be included in the image. Default value 0px",
            fieldLabel="Bottom Padding", ranking = 11D)
    @NumberField
    Double getPaddingBottomImage();

    @DialogField(  fieldDescription="Enter left padding to be included in the image. Default value 0px",
            fieldLabel="Left Padding", ranking = 12D)
    @NumberField
    Double getPaddingLeftImage();


    @DialogField( fieldDescription="Enter right padding to be included in the image. Default value 0px",
            fieldLabel="Right Padding", ranking = 13D)
    @NumberField
    Double getPaddingRightImage();

}
