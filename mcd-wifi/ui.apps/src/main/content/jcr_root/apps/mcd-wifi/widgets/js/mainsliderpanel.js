/**
* @extends CQ.form.CompositeField
* This is a custom multi field panel with number of fields as per the requirement
* @param {Object} config the config object
*/
/**
* @class Ejst.CustomWidget
* @extends CQ.form.CompositeField
* This is a custom widget based on {CQ.form.CompositeField}.
* @constructor
* Creates a new CustomWidget.
* @param {Object} config The config object
*/

CQ.form.MainSliderPanel = CQ.Ext.extend(CQ.form.CompositeField, {
	/**
	* @private
	* @type CQ.Ext.form.TextField
	*/
	hiddenField: null,
	/**
	* @private
	* @type CQ.form.Selection
	*/
	bgElement: null,
	tileImageText: null,
	textalign: null,
	target: null,
	videoType: null,
	/**
	* @private
	* @type CQ.Ext.form.TextField
	*/
	title: null,
	subTitle: null,
	tileImageAltText: null,
	bgImageAltText: null,
	youtubeVideo: null,
	/**
	* @private
	* @type CQ.Ext.form.PathField
	*/
	tileImage: null,
	bgImage: null,
	popupVideoPath: null,
	/**
	* @private
	* @type CQ.form.BrowseField
	*/
	tileImageLink: null,
	/**
    * @private
    * @type CQ.form.ColorList
    */
    colorpicker:null,
	
	constructor: function (config) {
		config = config || {};
		var defaults = {
		    "border": true,
		    "layout": "form",
		    "stateful": false
		}; 
		config = CQ.Util.applyDefaults(config, defaults);
		CQ.form.MainSliderPanel.superclass.constructor.call(this, config);
	},
	initComponent: function () {
	    CQ.form.MainSliderPanel.superclass.initComponent.call(this);
	    // Hidden field
	    this.hiddenField = new CQ.Ext.form.Hidden({
	         name: this.name
	    });
	    this.add(this.hiddenField);
	    
	    // Background Element Selection field
	    this.bgElement = new CQ.form.Selection({
            fieldLabel:"Background Element",
            fieldDescription:"Select a background element.",
            type:"select",
            name:"./bgElement",
            allowBlank:false,
            autoWidth: true,
            labelStyle:'padding-left: 10px;padding-top: 10px;',
            style: {
            	padding: '10px 58px 0 0'
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
            		fn : this.showHideBackgroundElement
            	}
            }
        });
        this.add(this.bgElement);
        
        /*Background Image PathField to map a URL*/
	    this.bgImage = new CQ.form.PathField({
	        allowBlank: true,
	        hidden: true,
	        editable: false,
	        fieldLabel:"Background Image*",
	        emptyText: "Select a background image.",
	        name:"./bgImage",
	        labelStyle: 'padding-left: 10px;',
	        regex: new RegExp("content/(.*)?(?:jpe?g|gif|png)$"),
			regexText: "Please provide an image with a valid extension i.e : jpeg|gif|png",
			rootPath:"/content/dam",
	        width: 335
	    });
	    this.add(this.bgImage);
	    
	    /*Tile BG Image alt text textfield*/
	    this.bgImageAltText = new CQ.Ext.form.TextField({
	    	allowBlank: true,
	    	hidden: true,
	        maxLength: 20,
	        fieldLabel:"Background Image Alt Text*",
	        emptyText: "Enter alt text for background image.",
	        maxLengthText: "A maximum of 20 characters is allowed for the Title.",
	        labelStyle: 'padding-left: 10px;',
	        name : "./bgImageAltText",
        	width: 335
	    }); 
	    this.add(this.bgImageAltText);
	    
	    /*Background Image PathField to map a URL*/
        this.bgVideo = new CQ.form.PathField({
        	allowBlank:true,
        	hidden: true,
            editable: false,
			fieldLabel:"Background Video*",
			emptyText: "Select a background video.",
			name: "./bgVideo",
			regex: new RegExp("content/(.*)?(?:webm|WEBM|ogg|OGG|mp4|MP4)$"),
			regexText: "Please provide a video with a valid extension i.e : webm|ogg|mp4",
			rootPath:"/content/dam",
			labelStyle: 'padding-left: 10px;',
			width: 335
        });
        this.add(this.bgVideo);
        
        this.initializationOfCommonFields();
	},
	processInit: function (path, record) { 
		this.bgElement.processInit(path, record);
		this.bgImage.processInit(path, record);
		this.bgImageAltText.processInit(path, record);
		this.bgVideo.processInit(path, record);
	    this.tileImage.processInit(path, record);
	    this.tileImageAltText.processInit(path, record);
	    this.tileImageText.processInit(path, record);
	    this.tileImageLink.processInit(path, record);
	    this.title.processInit(path, record);
	    this.subTitle.processInit(path, record);
	    this.textalign.processInit(path, record);
	    this.colorpicker.processInit(path, record);
	    this.target.processInit(path, record);
	    this.videoType.processInit(path, record);
	    this.popupVideoPath.processInit(path, record);
	    this.youtubeVideo.processInit(path, record);
	},
	setValue: function (value) {
	    var data = JSON.parse(value);
	    this.bgElement.setValue(data.bgElement);		
	    this.tileImage.setValue(data.tileImage);
	    this.tileImageAltText.setValue(data.tileImageAltText);
	    this.tileImageText.setValue(data.tileImageText);
	    
	    if(data.bgElement === "video"){
	    	this.bgVideo.setValue(data.bgVideo);
	    	this.videoType.setValue(data.videoType);
	 	    this.popupVideoPath.setValue(data.popupVideoPath);
	 	    this.youtubeVideo.setValue(data.youtubeVideo);
	    	
	 	    this.bgImage.setValue("");
	 	    this.bgImageAltText.setValue("");
	    	this.tileImageLink.setValue("");
	    	this.target.setValue("");
	    	this.title.setValue("");
		    this.subTitle.setValue("")
	    	
	    }else {
	    	this.bgImage.setValue(data.bgImage);
	 	    this.bgImageAltText.setValue(data.bgImageAltText);
	    	this.tileImageLink.setValue(data.tilePopupUrl);
	    	this.target.setValue(data.target);
	    	this.title.setValue(data.title);
		    this.subTitle.setValue(data.subTitle)
		    
		    this.bgVideo.setValue("");
	    	this.videoType.setValue("");
	 	    this.popupVideoPath.setValue("");
	 	    this.youtubeVideo.setValue("");
	    }
	    
	    this.textalign.setValue(data.textAlign);
	    this.colorpicker.setValue(data.colorPicker);	   
	    
	    this.showHideBackgroundElement();
	},
	getValue: function () {
	    return this.getRawValue();
	},
	getRawValue: function () { 
	    var rawData = {
		    "bgElement": this.bgElement.getValue(),
		    "bgImage": this.bgImage.getValue(),
		    "bgImageAltText": this.bgImageAltText.getValue(),
		    "bgVideo": this.bgVideo.getValue(),
		    "tileImage": this.tileImage.getValue(),
		    "tileImageAltText": this.tileImageAltText.getValue(),
		    "tileImageText": this.tileImageText.getValue(),
		    "tilePopupUrl": this.tileImageLink.getValue(),
		    "title": this.title.getValue(),
		    "subTitle": this.subTitle.getValue(),
		    "textAlign": this.textalign.getValue(),
		    "colorPicker": this.colorpicker.getValue(),
		    "target": this.target.getValue(),
		    "videoType": this.videoType.getValue(),
		    "popupVideoPath": this.popupVideoPath.getValue(),
		    "youtubeVideo": this.youtubeVideo.getValue()
	    }; 
	    this.hiddenField.setValue(JSON.stringify(rawData));
	    return JSON.stringify(rawData);
	},
	updateHidden: function () {
		this.hiddenField.setValue(this.getValue());
	},
	showHideBackgroundElement: function () {
		var bgElement = this.bgElement.getValue();
        if(bgElement === "image"){
        	this.bgVideo.hide();
        	this.videoType.hide();
        	this.popupVideoPath.hide();
        	this.youtubeVideo.hide();
        	
        	this.bgImage.show(); 
        	this.bgImageAltText.show();
        	this.tileImageLink.show();
        	this.target.show();
        	this.target.syncSize();
        	this.title.show();
        	this.subTitle.show();
        } 
        else if(bgElement === "video") {
        	this.bgImage.hide();
        	this.bgImageAltText.hide();
        	this.tileImageLink.hide();
        	this.target.hide();        	
        	this.title.hide();
        	this.subTitle.hide();
        	
        	this.bgVideo.show();
        	this.videoType.show();
        	this.videoType.syncSize();
        	
        	this.showHideVideoType();
        }
	},
	initializationOfCommonFields: function() {		
		/*Tile Image PathField to map a URL*/
	    this.tileImage = new CQ.form.PathField({
	        allowBlank: false,
	        editable: false,
	        name:"./titleImage",
	        fieldLabel:"Tile Image",
	        emptyText: "Select an image for tile.",
	        labelStyle: 'padding-left: 10px;',
	        regex: new RegExp("content/(.*)?(?:jpe?g|gif|png)$"),
			regexText: "Please provide an image with a valid extension i.e : jpeg|gif|png",
			rootPath:"/content/dam",
	        width: 335
	    });
	    this.add(this.tileImage);
	    
	    /*Tile Image alt text textfield*/
	    this.tileImageAltText = new CQ.Ext.form.TextField({
	    	allowBlank: false,
	        maxLength: 20,
	        fieldLabel:"Image Alt Text",
	        emptyText: "Enter alt text for tile image.",
	        maxLengthText: "A maximum of 20 characters is allowed for the alt text.",
	        labelStyle: 'padding-left: 10px;',
	        name : "./tileAltText",
        	width: 335
	    }); 
	    this.add(this.tileImageAltText);
	    
	    /*Tile image text*/
		this.tileImageText = new CQ.Ext.form.TextField({
			allowBlank:true,
			maxLength: 25,
	        fieldLabel:"Tile Image Text",
	        emptyText: "Enter image text for tile image.",
	        maxLengthText: "A maximum of 25 characters is allowed for the tile image text.",
	        labelStyle: 'padding-left: 10px;',
	        name : "./tileText",
        	width: 335
		}); 
	    this.add(this.tileImageText);

	    this.initializationOfAdditionalFields();
	    
	    /*Tile Image BrowseField to map link URL */
        this.tileImageLink = new CQ.form.BrowseField({
        	allowBlank:true,
        	hidden: true,
			fieldLabel:"URL",
			emptyText: "Select a hyperlink for tile.",
			name: "./tileImageLink",
			labelStyle: 'padding-left: 10px;',
			width: 335
        });
        this.add(this.tileImageLink);
        
        /*Target to open Image/Video*/
        this.target = new CQ.form.Selection({
        	allowBlank:true,
        	autoWidth: true,
        	hidden: true,
        	fieldLabel:"Target",
            fieldDescription:"Choose a target to open image/video link. Note: Default target is \"Modal\".",
            type:"select",
			name: "./target",
			labelStyle: 'padding-left: 10px;',
			style: {
            	padding: '0 58px 0 0'
            },
			options: [
	            {
	                text:"Modal",
	                value:"modal"				
	            },{
	                text:"New Window",
	                value:"new"
	            },{
	                text:"Same Window",
	                value:"same"
	            }
            ]
        });
        this.add(this.target);
	    
	    /*Text TextField to enter Title*/
	    this.title = new CQ.Ext.form.TextField({
	    	allowBlank: true,
	    	hidden: true,
	        maxLength: 50,
	        fieldLabel:"Title",
	        emptyText: "Enter Title",
	        maxLengthText: "A maximum of 50 characters is allowed for the Title.",
	        labelStyle: 'padding-left: 10px;',
	        name : "./title",
        	width: 335
	    }); 
	    this.add(this.title);
	    
	    /*Text TextField to enter Title*/
	    this.subTitle = new CQ.Ext.form.TextField({
	    	allowBlank: true,
	    	hidden: true,
	        maxLength: 100,
	        fieldLabel:"Sub Title",
	        emptyText: "Enter Sub-Title",
	        maxLengthText: "A maximum of 100 characters is allowed for the Sub Title.",
	        labelStyle: 'padding-left: 10px;',
	        name : "./subTitle",
	        width: 335
	    }); 
	    this.add(this.subTitle);

	    this.initializationOfVideoFields();
	},
	initializationOfAdditionalFields: function() {
		/*Text alignment selection field  for title & subtitle*/
		this.textalign = new CQ.form.Selection({
			allowBlank:true,
			autoWidth: true,
            fieldLabel:"Text alignment",
            fieldDescription:"Default alignment is Bottom.",
            type:"select",
            name:"./textAlign",
            options: [
              {text:"Top", value:"top" },
              {text:"Top Left", value:"topLeft" },
              {text:"Top Right", value:"topRight" },
              {text:"Middle", value:"middle" },
              {text:"Middle Left", value:"middleLeft" },
              {text:"Middle Right", value:"middleRight" },
              {text:"Bottom", value:"bottom" },
              {text:"Bottom Left", value:"bottomLeft" },
              {text:"Bottom Right", value:"bottomRight" }
            ],
            labelStyle: 'padding-left: 10px;',
			style: {
            	padding: '0 58px 0 0'
            }
        }); 
        this.add(this.textalign);
        
        /*ColorField picker for title & subtitle */
		this.colorpicker = new CQ.form.CustomColorField({
			allowBlank: true,
			/*hidden: true,*/
			fieldLabel: 'Color',
			name:"./colorPicker",
			fieldDescription: 'Choose the color code for title and subtitle. Note: Default colour is "#FFFFFF" (White).',
			defaultValue: 'FFFFFF',
			width: 175,
			labelStyle: 'padding-left: 10px;'
		}); 
        this.add(this.colorpicker);
	},
	initializationOfVideoFields: function() {
		/*Target to open Image/Video*/
        this.videoType = new CQ.form.Selection({
        	allowBlank:true,
        	autoWidth: true,
        	hidden: true,
        	fieldLabel:"Video Type",
            fieldDescription:"Choose a video type which you want to open.",
            type:"select",
			name: "./videoType",
			labelStyle: 'padding-left: 10px;',
			style: {
            	padding: '0 58px 0 0'
            },
			options: [
	            {
	            	text:"Standard",
	                value:"standard"
	            },{
	                text:"Youtube",
	                value:"youtube"
	            }
            ],
            listeners : {
            	selectionchanged : {
	                scope : this,
	                fn : this.showHideVideoType
            	}
            }
        });
        this.add(this.videoType);
        
        /*PopUp Video Path field*/
        this.popupVideoPath = new CQ.form.PathField({
        	allowBlank:true,
            editable: false,
            hidden: true,
			fieldLabel:"Light Box Video",
			fieldDescription:"Select the popup video path from Digital Assets.",
			name: "./popupVideoPath",
			regex: new RegExp("content/(.*)?(?:webm|WEBM|ogg|OGG|mp4|MP4)$"),
			regexText: "Please provide a video with a valid extension i.e : webm|ogg|mp4",
			rootPath:"/content/dam",
			labelStyle: 'padding-left: 10px;',
			width: 335
        });
        this.add(this.popupVideoPath);
        
        /*Youtube Video*/	
        this.youtubeVideo = new CQ.Ext.form.TextField({
        	allowBlank:true,
        	hidden: true,
			fieldLabel:"Youtube Video",
			fieldDescription:"Please provide an embed youtube video url.",
			labelStyle: 'padding-left: 10px;',
			regex: new RegExp("(https?\:\/\/)?((www\.)youtube\.com|youtu\.?be)\/embed\/.+$"),
			regexText: "Please provide a valid embed youtube url i.e: https://www.youtube.com/embed/SzTdgE04uA8?vq=hd1080",
			width: 335
		});
		this.add(this.youtubeVideo);
        
	},
	showHideVideoType: function () {
		var videoType = this.videoType.getValue();
        if(videoType === "standard"){
        	this.youtubeVideo.hide();
        	this.popupVideoPath.show();
        } 
        else if(videoType === "youtube") {
        	this.popupVideoPath.hide();
        	this.youtubeVideo.show();
        }
        else{
        	this.youtubeVideo.hide();
        	this.popupVideoPath.hide();
        }
	}
});
CQ.Ext.reg("mainsliderpanel", CQ.form.MainSliderPanel);