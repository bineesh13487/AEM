CQ.form.VideoCarouselWidget = CQ.Ext.extend(CQ.form.CompositeField, {

    /**
     * @private
     * @type CQ.Ext.form.TextField
     */
    hiddenField: null,

	/**
     * @private
     * @type CQ.Ext.form.Selection
     */
	 gallerytype:null,
	 videotype:null,
	 overlayalign:null,
	 icontype:null,

    /**
     * @private
     * @type CQ.Ext.form.TextField
     */
    alttext: null,
    title: null,
    youtubevideo:null,

	 /**
     * @private
     * @type CQ.Ext.form.RichText
     */
    subtitle: null,

    /**
     * @private
     * @type CQ.form.PathField
     */
    imagePath: null,
    videoPath: null,
    popupvideoPath: null,
    fallbackImagePath: null,
    
    /**
     * @private
     * @type CQ.form.BrowseField
     */
    imageLink:null,
    
    /**
     * @private
     * @type CQ.Ext.form.Checkbox
     */
    popupcheckbox:null,
    
    /**
     * @private
     * @type CQ.form.ColorList
     */
    colorpicker:null,
    
    constructor: function(config) {
        config = config || { };
        var defaults = {
            "border": true,
            "stateful": false,
            "style": "padding:3px;"
        };
        config = CQ.Util.applyDefaults(config, defaults);
        CQ.form.VideoCarouselWidget.superclass.constructor.call(this, config);
    },

    // overriding CQ.Ext.Component#initComponent
    initComponent: function() {
        CQ.form.VideoCarouselWidget.superclass.initComponent.call(this);
        this.hiddenField = new CQ.Ext.form.Hidden({
            name: this.name
        });
        this.add(this.hiddenField);		

		//selection
		this.gallerytype = new CQ.form.Selection({
            fieldLabel:"Select Image/Video",
            type:"select",
            name:"gallery-type",
            allowBlank:false,
            width: 300,
            labelStyle: 'padding-left: 10px;padding-top: 10px;',
            style: {
            	padding: '10px 0 0 0'
            },
            options: [
            {
                text:"Image",
                value:"image"				
            },{
                text:"Video",
                value:"video"
            }],
           listeners : {
            selectionchanged : {
                scope : this,
                fn : this.updateHidden
            },
			 afterrender: {
				scope: this,
                fn : this.dialogrender			
			 }
          }
        });
        this.add(this.gallerytype);

        this.initializationOfFields();        
        this.initializationOfCommonFields();		
		this.initializationOfFieldsForVideo(); 
	},
    // overriding CQ.form.CompositeField#setValue
    setValue: function(value) {
		var gallerytype = '';
        var subtitle = '';
        var alttext = '';
        var title = '';
        var imagepath = '';
        var imagelink = '';
        var videopath = '';
        var popupvideopath = '';
        var popupcheckbox = '';
        var fallbackimagepath = '';
        var videotype = '';  
        var youtubevideo = '';
        var overlayalign = '';
        var colorpicker = '';
        var icontype = '';
        
        var parts = value.split("|");
		if(parts.length > 0) {
	        gallerytype=parts[0];
	        subtitle=parts[1];
	        alttext=parts[2];
	        title=parts[3];
	        imagepath=parts[4];
	        imagelink=parts[5];
	        videopath=parts[6];
	        popupvideopath=parts[7];
	        popupcheckbox=parts[8];
	        fallbackimagepath=parts[9];
	        videotype=parts[10];
	        youtubevideo=parts[11];
	        overlayalign=parts[12];
	        colorpicker=parts[13];
	        icontype=parts[14];
		}
        this.gallerytype.setValue(gallerytype);
        this.subtitle.setValue(subtitle);
		this.alttext.setValue(alttext);
		this.title.setValue(title);
        this.imagePath.setValue(imagepath);
        this.imageLink.setValue(imagelink);
        this.videoPath.setValue(videopath);
        this.popupvideoPath.setValue(popupvideopath);
        this.popupcheckbox.setValue(popupcheckbox);
        this.fallbackImagePath.setValue(fallbackimagepath);
        this.videotype.setValue(videotype);
        this.youtubevideo.setValue(youtubevideo);
        this.overlayalign.setValue(overlayalign);
        this.colorpicker.setValue(colorpicker);
        this.icontype.setValue(icontype);
        
        var gallerytypevalue = this.gallerytype.getValue();
         if(gallerytypevalue === "image"){
        	this.imagePath.show();
        	this.imageLink.show();
        	this.title.show();
        	this.subtitle.show();
        	this.overlayalign.show();
        	this.overlayalign.syncSize();
        	this.colorpicker.show();
        	this.icontype.show();
        	this.icontype.syncSize();
        	
        	this.alttext.hide();
        	this.videoPath.hide();
        	this.popupcheckbox.hide();
        	this.popupvideoPath.hide();
        	this.videotype.hide();
        	this.fallbackImagePath.hide();
        } 
        else if(gallerytypevalue === "video") {
        	this.videoPath.show();
        	this.fallbackImagePath.show();
            this.title.show();
            this.subtitle.show();
            this.overlayalign.show();
            this.overlayalign.syncSize();
            this.colorpicker.show();
            this.icontype.show();
        	this.icontype.syncSize();
            this.popupcheckbox.show();
            this.popuprender();
            
            this.imagePath.hide();
            this.alttext.hide();
			this.imageLink.hide();
        }
	 },

    // overriding CQ.form.CompositeField#getValue
    getValue: function() {
        return this.getRawValue();
    },

    // overriding CQ.form.CompositeField#getRawValue
    getRawValue: function() {
		var gallery_type = this.gallerytype.getValue() ||"" ;
		var sub_title = this.subtitle.getValue() || "" ;
        var alt_text = this.alttext.getValue() || "" ;
        var title_field = this.title.getValue() || "" ;
		var image_path = this.imagePath.getValue() || "" ;
		var image_link = this.imageLink.getValue() || "" ;
		var video_path = this.videoPath.getValue() || "" ;
		var popup_video_path = this.popupvideoPath.getValue() || "" ;
		var popup_checkbox = this.popupcheckbox.getValue() || "" ;
		var fallback_image_path = this.fallbackImagePath.getValue() || "" ;
		var video_type = this.videotype.getValue() || "" ;
		var youtube_video = this.youtubevideo.getValue() || "" ;
		var overlay_align = this.overlayalign.getValue() || "" ;		
		var color_picker = this.colorpicker.getValue() || "" ;
		var icon_type = this.icontype.getValue() || "" ;
		
		var value= gallery_type + "|"  + sub_title + "|"  + alt_text + "|" + title_field + "|" + image_path + "|" + image_link 
        + "|" + video_path + "|" + popup_video_path + "|" + popup_checkbox + "|" + fallback_image_path + "|" + video_type 
        + "|" + youtube_video + "|" + overlay_align + "|" + color_picker + "|" + icon_type + "| -value";
    
        this.hiddenField.setValue(value);
        
        return value;
    },

    // private
    updateHidden: function() {
        var gallerytypevalue = this.gallerytype.getValue();
        if(gallerytypevalue === "image"){
        	this.imagePath.show();
        	this.imageLink.show();
        	this.title.show();
        	this.subtitle.show();
        	this.overlayalign.show();
        	this.overlayalign.syncSize();
        	this.colorpicker.show();
        	this.icontype.show();
        	this.icontype.syncSize();
        	
        	this.alttext.hide();
        	this.videoPath.hide();
        	this.popupcheckbox.hide();
        	this.popupvideoPath.hide();
        	this.videotype.hide();
        	this.fallbackImagePath.hide();
        } 
        else if(gallerytypevalue === "video") {
        	this.videoPath.show();
        	this.fallbackImagePath.show();
            this.title.show();
            this.subtitle.show();
            this.overlayalign.show();
            this.overlayalign.syncSize();
            this.colorpicker.show();
            this.icontype.show();
        	this.icontype.syncSize();
            this.popupcheckbox.show();
            this.popuprender();
            
            this.imagePath.hide();
            this.alttext.hide();
			this.imageLink.hide();
        }
        this.hiddenField.setValue(this.getValue());
	},
	
	dialogrender: function() {		
		this.imagePath.hide();	
		this.alttext.hide();	
		this.imageLink.hide();
        this.title.hide();	
        this.subtitle.hide();	
        this.overlayalign.hide();
        this.colorpicker.hide(); 
        this.icontype.hide();	
        this.videoPath.hide();	
        this.popupcheckbox.hide();
        this.popupvideoPath.hide();	
        this.videotype.hide();	
        this.fallbackImagePath.hide();
        this.youtubevideo.hide();
	},
	
	popuprender: function(){
		var check = this.popupcheckbox.getValue();
        if(check) {
        	this.videotype.show();
        	this.videotype.syncSize();
        	var checkType = this.videotype.getValue();
        	if(checkType === 'standard'){
        		this.popupvideoPath.show();
        		this.youtubevideo.hide();
        	}
        	else if(checkType === 'youtube'){
        		this.popupvideoPath.hide();
        		this.youtubevideo.show();
        	}
        }
        else {
        	this.videotype.hide();
        	this.popupvideoPath.hide();
        	this.youtubevideo.hide();
        }
	},
	
	initializationOfFields: function() {
		/*Image Path field*/
        this.imagePath = new CQ.form.PathField({
        	allowBlank:true,
            editable: false,
			fieldLabel:"Image Path*",
			fieldDescription:"Select the image path from Digital Assets.",
			key: "imagePath",
			name: "./imagePath",
			regex: new RegExp("content/(.*)?(?:jpe?g|gif|png)$"),
			regexText: "Please provide an image with a valid extension i.e : jpeg|gif|png",
			rootPath:"/content/dam",
			showTitlesInTree: false,
			labelStyle: 'padding-left: 10px;',
			width: 300
        });
        this.add(this.imagePath);

		/*Video Path field*/
        this.videoPath = new CQ.form.PathField({
        	allowBlank:true,
            editable: false,
			fieldLabel:"Video Path*",
			fieldDescription:"Select the video path from Digital Assets.",
			key: "videoPath",
			name: "./videoPath",
			regex: new RegExp("content/(.*)?(?:webm|WEBM|ogg|OGG|mp4|MP4)$"),
			regexText: "Please provide a video with a valid extension i.e : webm|ogg|mp4",
			rootPath:"/content/dam",
			showTitlesInTree: false,
			labelStyle: 'padding-left: 10px;',
			width: 300
        });
        this.add(this.videoPath);
        
        /*Fallback Image Path field*/
        this.fallbackImagePath = new CQ.form.PathField({
        	allowBlank:true,
            editable: false,
			fieldLabel:"Fallback Image Path*",
			fieldDescription:"Select the fallback image path from Digital Assets. Note : In mobile view this " +
					"background image will appear. ",
			key: "fallbackImagePath",
			name: "./fallbackImagePath",
			regex: new RegExp("content/(.*)?(?:jpe?g|gif|png)$"),
			regexText: "Please provide an image with a valid extension i.e : jpeg|gif|png",
			rootPath:"/content/dam",
			showTitlesInTree: false,
			labelStyle: 'padding-left: 10px;width:140px;',
			width: 300
        });
        this.add(this.fallbackImagePath);
        
        /*Alt Text*/
        this.alttext = new CQ.Ext.form.TextField({
        	allowBlank:true,
        	fieldLabel:"Alt Text",
        	key: "alttext",
        	labelStyle: 'padding-left: 10px;',
        	width: 250
        });
        this.add(this.alttext);

        /*Image Link*/
        this.imageLink = new CQ.form.BrowseField({
        	allowBlank:true,
            editable: false,
			fieldLabel:"Link",
			fieldDescription:"Choose the hyperlink for the image.",
			key: "imagelink",
			labelStyle: 'padding-left: 10px;',
			width: 300
        });
        this.add(this.imageLink);
	},
	
	initializationOfCommonFields: function() {
		 /*Title*/
        this.title = new CQ.Ext.form.TextField({
        	allowBlank:true,
        	fieldLabel:"Title",
        	key: "title",
        	labelStyle: 'padding-left: 10px;',
        	width: 250
        });
        this.add(this.title);

        /*Sub Title*/	
        this.subtitle = new CQ.Ext.form.TextField({
        	allowBlank:true,
			fieldLabel:"Subtitle",
			labelStyle: 'padding-left: 10px;',
			width: 250
		});
		this.add(this.subtitle);
		
		/*Text Alignment Video*/
		this.overlayalign = new CQ.form.Selection({
            fieldLabel:"Text alignment",
            fieldDescription:"Default alignment is centre.",
            type:"select",
            width: 300,
            allowBlank:true,
            valueField: 'centre',
            displayField: 'Centre',
            options: [
              {text:"Centre", value:"centre"},
              {text:"Left", value:"left" },
              {text:"Right", value:"right" }
            ],
            labelStyle: 'padding-left: 10px;'
        }); 
        this.add(this.overlayalign);
        
        /*Color picker*/
		this.colorpicker = new CQ.form.ColorField({
			fieldLabel: 'Color',
			fieldDescription: 'Choose the color code for title and subtitle. Note: Default colour is "#FFFFFF" (White).',
			defaultValue: 'FFFFFF',
			width: 175,
			labelStyle: 'padding-left: 10px;',
			allowBlank: true
		}); 
        this.add(this.colorpicker);
        
        /*Icon selection field*/
		this.icontype = new CQ.form.Selection({
            fieldLabel:"Select Icon*",
            type:"select",            
            allowBlank:true,
            width: 300,
            labelStyle: 'padding-left: 10px;width:100px;',
            options: [
            {
                text:"Music",
                value:"fa-music"
            },{
                text:"Game",
                value:"fa-gamepad"
            },{
                text:"Video",
                value:"fa-video-camera"
            },{
                text:"Users",
                value:"fa-users"
            }]         
        });
        this.add(this.icontype);
	},
	
	initializationOfFieldsForVideo: function() {
		/*Popup Checkbox*/	
        this.popupcheckbox = new CQ.Ext.form.Checkbox({
        	allowBlank:true,
			fieldLabel:"Check this to open video on popup",
			labelStyle: 'padding-left: 10px;width:215px;',
	        inputValue: true,
			listeners: {
				check: {
	                scope : this,
	                fn : this.popuprender
	            }
	        }
		});
		this.add(this.popupcheckbox);
		
		/*Video Type selection field*/
		this.videotype = new CQ.form.Selection({
            fieldLabel:"Select Video Type*",
            type:"select",            
            allowBlank:true,
            width: 320,
            labelStyle: 'padding-left: 10px;width:120px;',
            options: [
            {
                text:"Standard",
                value:"standard"
            },{
                text:"Youtube",
                value:"youtube"
            }],
            listeners : {
            	selectionchanged : {
	                scope : this,
	                fn : this.popuprender
            	}
            }          
        });
        this.add(this.videotype);
		
		/*PopUp Video Path field*/
        this.popupvideoPath = new CQ.form.PathField({
        	allowBlank:true,
            editable: false,
			fieldLabel:"Popup Video Path",
			fieldDescription:"Select the popup video path from Digital Assets.",
			key: "popupVideoPath",
			name: "./popupVideoPath",
			regex: new RegExp("content/(.*)?(?:webm|WEBM|ogg|OGG|mp4|MP4)$"),
			regexText: "Please provide a video with a valid extension i.e : webm|ogg|mp4",
			rootPath:"/content/dam",
			showTitlesInTree: false,
			labelStyle: 'padding-left: 10px;',
			width: 300
        });
        this.add(this.popupvideoPath);
        
        /*Youtube Video*/	
        this.youtubevideo = new CQ.Ext.form.TextField({
        	allowBlank:true,
			fieldLabel:"Youtube Video",
			fieldDescription:"Please provide an embed youtube video url.",
			labelStyle: 'padding-left: 10px;',
			regex: new RegExp("(https?\:\/\/)?((www\.)youtube\.com|youtu\.?be)\/embed\/.+$"),
			regexText: "Please provide a valid embed youtube url i.e: https://www.youtube.com/embed/SzTdgE04uA8?vq=hd1080",
			width: 350
		});
		this.add(this.youtubevideo);
	}

});

// register xtype
CQ.Ext.reg('carouselwidget', CQ.form.VideoCarouselWidget);