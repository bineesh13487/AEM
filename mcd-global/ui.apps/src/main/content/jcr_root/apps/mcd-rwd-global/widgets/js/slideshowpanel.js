/**
* Custom JsonReader CQ.SlideshowJsonReader To read from the slideshowGrid node and
* convert it into the desired format as expected by the Grid store.
*/

CQ.SlideshowJsonReader = function(meta, recordType) {
	CQ.SlideshowJsonReader.superclass.constructor.call(this, meta, recordType || meta.fields);
};
CQ.Ext.extend(CQ.SlideshowJsonReader, CQ.Ext.data.JsonReader, {
	read : function(response) {
		var json = response.responseText;
		var o = CQ.Ext.decode(json);
		var output = { hits : new Array() };
		if (o.mainImage) {
			if ((typeof o.mainImage) == "string") {
				output.hits[0] = {
					itemId : "storeItemId",

					titleImage : o.titleImage.trim(),
					titleImageAlign : o.titleImageAlign.trim(),
					mainImage : o.mainImage.trim(),
					mainImageAlign : o.mainImageAlign.trim(),
					bgImage : o.bgImage.trim(),
					description : o.description.trim(),
					ctaImage : o.ctaImage.trim(),
					ctaImageAlign : o.ctaImageAlign.trim(),
					ctaPath : o.ctaPath.trim(),
					hideDesc : o.hideDesc.replace(/\s+/g, '').trim()

				};
			} else {
				for ( var i = 0; i < o.mainImage.length; i++) {
					output.hits[i] = {
						itemId : "storeItemId" + (i + 1),

						titleImage : o.titleImage[i].trim(),
						titleImageAlign : o.titleImageAlign[i].trim(),
						mainImage : o.mainImage[i],
						mainImageAlign : o.mainImageAlign[i].trim(),
						bgImage : o.bgImage[i].trim(),
    					description : o.description[i].trim(),
						ctaImage : o.ctaImage[i].trim(),
						ctaImageAlign : o.ctaImageAlign[i].trim(),
						ctaPath : o.ctaPath[i].trim(),
						hideDesc : o.hideDesc[i].replace(/\s+/g, '').trim()
					};
				}
			}
		}
		if (!o) {
			throw {
				message : 'JsonReader.read: Json object not found'
			};
		}
		return this.readRecords(output);
	}
});

/**
* Custom panel for the Slideshow item editing CQ.SlideshowPanel CQ.SlideshowPanel
* extends CQ.Ext.Panel
*/
CQ.SlideshowPanel = CQ.Ext.extend(CQ.Ext.Panel,{
	originPath : CQ.WCM.getPagePath(),
	ajaxRequestURL : null,
	ajaxmethod : null,
	perPage : 6,
	height : 520,
	Width : 900,
	maximumAllowed : null,
	initComponent : function() {
		var pagesArray = new Array();
		this.ajaxRequestURL = this.originPath + ".json";
		var fields = [ {
			name : 'titleImage',
			mapping : 'titleImage'
			},{
			name : 'titleImageAlign',
			mapping : 'titleImageAlign'
			}, {
			name : 'mainImage',
			mapping : 'mainImage'
			}, {
            name : 'mainImageAlign',
            mapping : 'mainImageAlign'
            }, {
			name : 'bgImage',
			mapping : 'bgImage'
			}, {
			name : 'description',
			mapping : 'description'
			}, {
			name : 'ctaImage',
			mapping : 'ctaImage'
			}, {
			name : 'ctaImageAlign',
			mapping : 'ctaImageAlign'
			},{
			name : 'ctaPath',
			mapping : 'ctaPath'
			}, {
            name : 'hideDesc',
            mapping : 'hideDesc'
        }];

		var self = this;
		/**
		* Image renderer function for rendering of the image in
		* the grid panel
		*
		* @param val -
		* the image source
		* @returns {String}
		*/
		function renderImage(val) {
			return '<img src="' + val + '" style="width:50px;height:50px"> ';
		}

		/**
		* The column wrap function to allow wrapping of long
		* text in the grid panel columns.
		*
		* @param val -
		* The HTML content
		* @returns {String}
		*/
		function columnWrap(val) {
			return '<div style="white-space:normal !important;"><p>' + val + '</p></div>';
		}

		/**
		 * The renderer function to display the disclosure
		 * status
		 *
		 * @param val -
		 *            String value
		 * @returns {String}
		 */
		function renderHideDesc(val) {
			var value = (val != "") ? "Yes" : "No";
			return '<div style="white-space:normal !important;"><p>'+ value + '</p></div>';
		}

		var cols = [  {
			id : 'titleImage',
			header : "Title Image",
			width : "70px",
			sortable : false,
			dataIndex : 'titleImage',
			renderer : renderImage
		}, {
			id : 'mainImage',
			header : "Slide Image",
			width : "70px",
			sortable : false,
			dataIndex : 'mainImage',
			renderer : renderImage
		},  {
			id : 'bgImage',
			header : "Background Image*",
			width : "70px",
			sortable : false,
			dataIndex : 'bgImage',
			renderer : renderImage
		}, 	{
            id : 'ctaImage',
            header : "CTA Image",
            width : "70px",
            sortable : false,
            dataIndex : 'ctaImage',
            renderer : renderImage
        }, {
			id : 'ctaPath',
			name : 'ctaPath',
			width : "100px",
			header : "Link",
			mapping : 'ctaPath',
			renderer : columnWrap
        }, {
			id : 'description',
			name : 'description',
			width : "100px",
			header : "Slide Description",
			mapping : 'description',
			renderer : columnWrap
		}];
		var d = this;

		// The grid store
		var SlideshowGridStore = new CQ.Ext.data.Store({
			"id" : "SlideshowGridStore" + d.id,
			"proxy" : new CQ.Ext.data.HttpProxy({
				"url" : this.ajaxRequestURL,
				"method" : "GET"
			}),
			"reader" : new CQ.SlideshowJsonReader({
				"id" : "itemId",
				"root" : "hits",
				"fields" : fields
			}),
			"xtype" : "store"
		});

		// Left hand form panel fields
		// 1. Title image
		var titleImage = {
			name : 'titleImage',
			ddGroups : "media",
			text : "Title Image",
			xtype : 'html5smartimage',
			fileNameParameter : "./fileName",
			allowUpload : false,
			height : 150,
			width : 180,
			border : true,
			disableZoom : 'true',
			id : 'titleImageArea' + d.id,
			name : './file',
			uploadUrl : '/tmp/upload_test/*',
            listeners : {
                imagestate : RWD.CQ.util.enableImageToolbar
            }
		};
		// 2.Title Image Align
		var titleImageAlignField = {
			fieldLabel : 'Title Image Alignment',
			fieldDescription : 'Choose the horizontal alignment of title image',
			name : 'titleImageAlign',
			width : 155,
			id : 'titleImageAlignId' + d.id,
			xtype : 'selection',
			defaultValue:'Select',
			type : 'select',
			options: [{
				value: "Select",
				text: "Select",
				
			},{
				value: "left",
				text: "Left",
				qtip: "Align the Title Image Left"
			},{
				value: "right",
				text: "Right",
				qtip: "Align the Title Image Right"
			},{
                value: "center",
                text: "Center",
                qtip: "Align the Title Image Center"
            }],
            optionsConfig: {
                              width : 155
                                    }

		};
		// 3. Main image
		var mainImage = {
			name : 'mainImage',
			ddGroups : "media",
			text : "Slide Image",
			fieldDescription : 'Choose the Slide image',
			xtype : 'html5smartimage',
			fileNameParameter : "./fileName",
			allowUpload : false,
			width : 180,
			height : 150,
			border : true,
			disableZoom : 'true',
			id : 'mainImageArea' + d.id,
			name : './file',
			uploadUrl : '/tmp/upload_test/*',
            listeners : {
                imagestate : RWD.CQ.util.enableImageToolbar
            }
		};
		// 4.Main Image Align
        var mainImageAlignField = {
            fieldLabel : 'Slide Image Alignment',
            fieldDescription : 'Choose the horizontal alignment of Slide image',
            name : 'mainImageAlign',
            width : 155,
            id : 'mainImageAlignId' + d.id,
            xtype : 'selection',
			defaultValue:'Select',
            type : 'select',
            options: [{
                value: "Select",
                text: "Select"
            },{
                value: "left",
                text: "Left",
                qtip: "Align the Main slide Image Left"
            }, {
                value: "right",
                text: "Right",
                qtip: "Align the Main Slide Image Right"
            }, {
                  value: "center",
                  text: "Center",
                  qtip: "Align Main Slide Image Center"
            }],
			optionsConfig: {
                              width : 155
                                    }

        };
		// 5. Background image
		var bgImage = {
			name : 'bgImage',
			ddGroups : "media",
			text : "Background Image",
			xtype : 'html5smartimage',
			fileNameParameter : "./fileName",
			allowUpload : false,
			height : 150,
			width : 180,
			border : true,
			disableZoom : 'true',
			id : 'bgImageArea' + d.id,
			name : './file',
			uploadUrl : '/tmp/upload_test/*',
            listeners : {
                imagestate : RWD.CQ.util.enableImageToolbar
            }
		};
		// 6.Description
		var descriptionField = {
			fieldLabel : 'Slide Description',
            fieldDescription : 'Provide slide description',
            name : 'description',
            width:180,
            id : 'descriptionId' + d.id,
            maxLength:115,
            maxLengthText:'limit reached',
            xtype : 'textarea'
		};
		// 7. Call to Action image
		var ctaImage = {
			name : 'ctaImage',
			ddGroups : "media",
			text : "Call To Action Image",
			xtype : 'html5smartimage',
			fileNameParameter : "./fileName",
			allowUpload : false,
			height : 150,
			width : 180,
			border : true,
			disableZoom : 'true',
			id : 'ctaImageArea' + d.id,
			name : './file',
			uploadUrl : '/tmp/upload_test/*',
            listeners : {
                imagestate : RWD.CQ.util.enableImageToolbar
            }
		};
		// 8.CTA Image Align
		var ctaImageAlignField = {
			fieldLabel : 'Call To Action Image Alignment',
			fieldDescription : 'Choose the alignment of CTA image. Default value is Bottom (centered)',
			name : 'ctaImageAlign',
			width : 155,
			id : 'ctaImageAlignId' + d.id,
			xtype : 'selection',
			defaultValue:'Select',
			type : 'select',
			options: [{
                value: "Select",
                text: "Select"
            },{
                value: "bottom-center",
                text: "Bottom (centered)"
            }, {
				value: "bottom-left",
				text: "Bottom-Left"
			}, {
				value: "bottom-right",
				text: "Bottom-Right"
			}],
            optionsConfig: {
                              width : 155
                                    }

		};
		// 9. Call to Action Path
		var ctaPathField = {
			fieldLabel : 'Link',
			fieldDescription : "If CTA Image is not provided, entire slide will be clickable.",
			name : 'ctaPath',
			width : 180,
			id : 'ctaPathId' + d.id,
			xtype : 'pathfield',
			rootPath : '/content'
		};
		//10. hide desc for mobile
        var hideDescField = {
            fieldLabel : 'Hide description for Mobile?',
            name : 'hideDesc',
            id : 'hideDescId' + d.id,
            xtype : 'selection',
            type : 'checkbox',
			listeners: {
				'render': function() {
					this.setValue(true);
				}
			}
        };
		// The submit button for the form panel
		var submitButton = {
			name : 'button',
			width : 80,
			text : 'Add to List',
			style : {
				marginLeft : '20px'
			},
			xtype : 'button',
			handler : function() {
				if (CQ.Ext.ComponentMgr.get('bgImageArea' + d.id).fileReferenceField.getValue() != "") {
                    var link = CQ.Ext.ComponentMgr.get('ctaPathId' + d.id).getValue();
					var descriptionText = CQ.Ext.ComponentMgr.get('descriptionId' + d.id).getValue();
                    var pattern = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;
                    var ctaImage= CQ.Ext.ComponentMgr.get('ctaImageArea'+ d.id).fileReferenceField.getValue();
                    if(ctaImage!='' && link==''){
                    		CQ.Ext.Msg.alert(CQ.I18n.getMessage("Warning"),
                    				CQ.I18n.getMessage("Please provide Call to action (Slide link) path."));
                    		return false;
                    }
                    if(link != '' && !/\/content\/.*/.test(link) && !pattern.test(link)) {
                        CQ.Ext.Msg.alert(CQ.I18n.getMessage("Warning"),
                        CQ.I18n.getMessage("Call to Action Path must start with '/content' or 'http://w' or 'https://w'"));
                        return false;
                    }
					if ((self.maximumAllowed) && (CQ.Ext.ComponentMgr.get('slideshowGrid' + d.id).store.getCount() >= self.maximumAllowed)) {
						CQ.Ext.MessageBox.alert(CQ.I18n.getMessage("Warning"),
						CQ.I18n.getMessage("Maximum " + self.maximumAllowed + " are allowed! Try to delete a few then, try again."));
						return false;
			    	}
					if(descriptionText.length>115){
						CQ.Ext.Msg.alert(CQ.I18n.getMessage("Warning"),
                        CQ.I18n.getMessage("Slide description can't be more than 115 characters."));
						return false;
					}

					var myNewRecord = CQ.Ext.data.Record.create({
						id : 'titleImage',
						header : "Title Image",
						sortable : true,
						dataIndex : 'titleImage',
						renderer : renderImage
					}, {
						id : 'titleImageAlign',
						name : 'titleImageAlign',
						mapping : 'titleImageAlign'
					}, {
						id : 'mainImage',
						header : "Slide Image",
						sortable : true,
						dataIndex : 'mainImage',
						renderer : renderImage
					},  {
						id : 'mainImageAlign',
						name : 'mainImageAlign',
						mapping : 'mainImageAlign'
					}, {
						id : 'bgImage',
						header : "Background Image",
						sortable : true,
						dataIndex : 'bgImage',
						renderer : renderImage
					},  {
						id : 'description',
						name : 'description',
						mapping : 'description'
					}, {
						id : 'ctaImage',
						header : "CTA Image",
						sortable : true,
						dataIndex : 'ctaImage',
						renderer : renderImage
					},{
						id : 'ctaImageAlign',
						name : 'ctaImageAlign',
						mapping : 'ctaImageAlign'
					}, {
						id : 'ctaPath',
						name : 'ctaPath',
						mapping : 'ctaPath'
					}, {
						id : 'hideDesc',
						name : 'hideDesc',
						mapping : 'hideDesc'
					});
					var myRecord;
					myRecord = new myNewRecord({
						titleImage : CQ.Ext.ComponentMgr.get('titleImageArea'+ d.id).fileReferenceField.getValue(),
						titleImageAlign : CQ.Ext.ComponentMgr.get('titleImageAlignId' + d.id).getValue(),
						mainImage : CQ.Ext.ComponentMgr.get('mainImageArea'+ d.id).fileReferenceField.getValue(),
						mainImageAlign : CQ.Ext.ComponentMgr.get('mainImageAlignId'+d.id).getValue(),
						bgImage : CQ.Ext.ComponentMgr.get('bgImageArea'+ d.id).fileReferenceField.getValue(),
						description : CQ.Ext.ComponentMgr.get('descriptionId' + d.id).getValue(),
						ctaImage : CQ.Ext.ComponentMgr.get('ctaImageArea'+ d.id).fileReferenceField.getValue(),
						ctaImageAlign : CQ.Ext.ComponentMgr.get('ctaImageAlignId' + d.id).getValue(),
						ctaPath : CQ.Ext.ComponentMgr.get('ctaPathId' + d.id).getValue(),
						hideDesc : CQ.Ext.ComponentMgr.get('hideDescId' + d.id).getValue(),
					});

					// Add record to the grid store
					CQ.Ext.ComponentMgr.get('slideshowGrid'+ d.id).store.add(myRecord);

                    // Clear the form panel fields after form
                    // submit
                    CQ.Ext.ComponentMgr.get('titleImageArea' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('titleImageAlignId' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('mainImageArea' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('mainImageAlignId' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('bgImageArea' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('descriptionId' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('ctaImageArea' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('ctaImageAlignId' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('ctaPathId' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('hideDescId' + d.id).reset();

                } else {
				     
					 
                    CQ.Ext.Msg.alert("Error","Provide Background Image for the item, If not Applicable provide tranparent/zero pixel image");
				
                }
	        }
        };
        var submitButtonPanel = {
            height : 50,
            border : false,
            autoWidth : true,
            xtype : 'panel',
            layout : {
                type : 'hbox',
                padding : '10 0 0 140'
            },
            items : [ submitButton ]
        };
        // 1.Title Image
        var titleImagePanel = {
            height : 180,
            border : false,
            //autoWidth : true,
            xtype : 'panel',
            layout : {
                type : 'hbox',
                align : 'middle'
            },
            items : [{
                text : 'Title Image',
                width : 130,
                xtype : 'label'
            }, titleImage]
        };
        // 9.title Image Align
        var titleImageAlignPanel = {
            /*height : 80,
            width : 100,*/
            border : false,
            autoWidth : true,
            xtype : 'panel',
            layout : {
                type : 'form'
            },
            items : [ titleImageAlignField ]
        };
        // 2.Main Image
        var mainImagePanel = {
            height : 180,
            border : false,
            //autoWidth : true,
            xtype : 'panel',
            layout : {
                type : 'hbox',
                align : 'middle'
            },
            items : [{
                text : 'Slide Image',
                width : 130,
                xtype : 'label'
            }, mainImage]
        };
        // 9.main Image Align
        var mainImageAlignPanel = {
            /*height : 0,
            width : 100,*/
            border : false,
            autoWidth : true,
            xtype : 'panel',
            layout : {
                type : 'form'
            },
            items : [ mainImageAlignField ]
        };
        // 3.Background Image
        var bgImagePanel = {
            height : 180,
            border : false,
            //autoWidth : true,
            xtype : 'panel',
            layout : {
                type : 'hbox',
                align : 'middle'
            },
            items : [{
                text : 'Background Image*',
                width : 130,
                xtype : 'label'
            }, bgImage]
        };
        //5.description
        var descriptionPanel = {
            height : 100,
            border : false,
            autoWidth : true,
            xtype : 'panel',
            layout : {
                type : 'form'
            },
            items : [ descriptionField ]
        };
        // 6.CTA Image
        var ctaImagePanel = {
            height : 180,
            border : false,
            xtype : 'panel',
            layout : {
                type : 'hbox',
                align : 'middle'
            },
            items : [ {
                text : 'Call To Action Image',
                width : 130,
                xtype : 'label'
            }, ctaImage]
        };
        // 10.cta Image Align
        var ctaImageAlignPanel = {
            /*height : 80,
            width : 100,*/
            border : false,
            autoWidth : true,
            xtype : 'panel',
            layout : {
                type : 'form'
            },
            items : [ ctaImageAlignField ]
        };
        // 7.CTA Path
        var ctaPathPanel = {
            height : 50,
            border : false,
            autoWidth : true,
            xtype : 'panel',
            layout : {
                type : 'form'
            },
            items : [ ctaPathField ]
        };

    	var hideDescPanel = {
            height : 60,
            border : false,
            autoWidth : true,
            xtype : 'panel',
            style : {
                margin : '5,5,45,50'
            },
            layout : {
                type : 'form'
            },
            items : [ hideDescField ]
        };

        var galleryPanel = {
            border : false,
            xtype : 'panel',
            columnWidth : 0.45,
            layout : {
                type : 'form'
            },
            style : {
                padding : '10px'
            },
            items : [ titleImagePanel, titleImageAlignPanel, mainImagePanel, mainImageAlignPanel, bgImagePanel,
                  ctaImagePanel, ctaImageAlignPanel, ctaPathPanel, descriptionPanel,
                   hideDescPanel, submitButtonPanel ]
        };
        // create the destination Grid
        var slideshowGrid = {
            id : "slideshowGrid" + d.id,
            store : SlideshowGridStore,
            ddGroup : 'slideshowGridDDGroup',
            enableDragDrop : true,
            columns : cols,
            height : 350,
            enableDragDrop : true,
            columnWidth : 0.55,
            stripeRows : true,
            xtype : "grid",
            listeners : {
                afterrender : function() {
                    // This will make sure we only drop to the view scroller element
                    var slideShowHandle = CQ.Ext.ComponentMgr.get('slideshowGrid' + d.id);
                    var slideshowGridDropTargetEl = slideShowHandle.getView().scroller.dom;
                    var slideshowGridDropTarget = new CQ.Ext.dd.DropTarget(slideshowGridDropTargetEl,	{
                        ddGroup : 'slideshowGridDDGroup',
                        notifyDrop : function(ddSource,e, data) {
                            if ((self.maximumAllowed) && (slideShowHandle.store.getCount() >= self.maximumAllowed)) {
                                CQ.Ext.MessageBox.alert(CQ.I18n.getMessage("Warning"),
                                CQ.I18n.getMessage("Maximum "+ self.maximumAllowed+ " are allowed! Try to delete a few then, try again."));
                                        return false;
                            }

                            var targetRowIndex = slideShowHandle.getView().findRowIndex(e.getTarget());
                            var records = ddSource.dragData.selections;
                            CQ.Ext.each(records,ddSource.grid.store.remove,ddSource.grid.store);
                            if (targetRowIndex !== false) {
                                slideShowHandle.store.insert(targetRowIndex,records);
                            } else {
                                slideShowHandle.store.add(records);
                            }
                            CQ.Ext.select('.x-grid3-body').setStyle('margin-bottom','50px');
                            return true;
                        }
                    });
                },
                rowcontextmenu : function(grid, rowIndex, e) {
                    e.stopEvent();
                    menu = new CQ.Ext.menu.Menu({
                        items : [{
                            text : 'Update',
                            iconCls : 'icon-update',
                            handler : function() {
                                var rowHandle = grid.getStore().getAt(rowIndex);
                                // hide desc
                                CQ.Ext.ComponentMgr.get('hideDescId'+ d.id).setValue(rowHandle.get('hideDesc'));
                                //cta align
                                CQ.Ext.ComponentMgr.get('ctaImageAlignId'+ d.id).setValue(rowHandle.get('ctaImageAlign'));
                                //title align
                                CQ.Ext.ComponentMgr.get('titleImageAlignId'+ d.id).setValue(rowHandle.get('titleImageAlign'));
                                // main image align
                                CQ.Ext.ComponentMgr.get('mainImageAlignId'+ d.id).setValue(rowHandle.get('mainImageAlign'));
                                //description
                                CQ.Ext.ComponentMgr.get('descriptionId'+ d.id).setValue(rowHandle.get('description'));
                                //cta Path
                                CQ.Ext.ComponentMgr.get('ctaPathId'+ d.id).setValue(rowHandle.get('ctaPath'));

                                //title image
                                var dragTitleImageData = {
                                    records : [{
                                        pathTitle : rowHandle.get('titleImage'),
                                        get : function() {	return this.pathTitle; }
                                    }],
                                    single : "true"
                                };

                                CQ.Ext.ComponentMgr.get('titleImageArea'+ d.id).handleDrop(dragTitleImageData);
                                //main image
                                var dragMainImageData = {
                                    records : [{
                                        pathMain : rowHandle.get('mainImage'),
                                        get : function() {	return this.pathMain;	}
                                    }],
                                    single : "true"
                                };
                                CQ.Ext.ComponentMgr.get('mainImageArea'+ d.id).handleDrop(dragMainImageData);

                                //Bg image
                                var dragBgImageData = {
                                    records : [{
                                        pathBg : rowHandle.get('bgImage'),
                                        get : function() {	return this.pathBg;	}
                                    }],
                                    single : "true"
                                };
                                CQ.Ext.ComponentMgr.get('bgImageArea'+ d.id).handleDrop(dragBgImageData);
                                //CTA image
                                var dragCtaImageData = {
                                    records : [{
                                        pathCta : rowHandle.get('ctaImage'),
                                        get : function() {	return this.pathCta;	}
                                    }],
                                    single : "true"
                                };
                                CQ.Ext.ComponentMgr.get('ctaImageArea'+ d.id).handleDrop(dragCtaImageData);
                                grid.getStore().removeAt(rowIndex);
                            }
                        }, {
                            text : 'Delete',
                            iconCls : 'icon-delete',
                            handler : function() {
                                grid.getStore().removeAt(rowIndex);
                            }
                        }]
                    });
                    menu.showAt(e.getXY());
                }
	        }
        };
        // used to add records to the destination stores
        var blankRecord = CQ.Ext.data.Record.create(fields);

        var config = CQ.Util.applyDefaults(config, {
            layout : 'column',
            items : [ galleryPanel, slideshowGrid ]
        });
        CQ.Ext.apply(this, CQ.Ext.apply(this.initialConfig,config));
        CQ.SlideshowPanel.superclass.initComponent.call(this,arguments);
    },
    onRender : function() {
        CQ.SlideshowPanel.superclass.onRender.apply(this,arguments);
        var parentDialog = this.findParentByType("dialog");
        var panelFields = ["mainImageArea","titleImageArea","mainImageAlignId","titleImageAlignId","bgImageArea","descriptionId",
                           "ctaImageArea","ctaImageAlignId","ctaPathId","hideDescId"];
        var me = this;
        if (parentDialog) {
            // Load content on page load
            parentDialog.on("loadContent",function(obj) {
                // Load the right grid
                me.originpath = parentDialog.path;
                me.ajaxRequestURL = parentDialog.path+ "/slideshow.json";
                CQ.Ext.ComponentMgr.get('slideshowGrid'+ me.id).store.proxy.setUrl(me.ajaxRequestURL);
                CQ.Ext.ComponentMgr.get('slideshowGrid'+ me.id).store.load({});
                CQ.Ext.each(panelFields, function(panelField, index) {
                    CQ.Ext.ComponentMgr.get(panelField + me.id).reset();
                });

            }, parentDialog);

			// Before submit set the form values in hidden
			// fields.
			parentDialog.on("beforesubmit",function() {
				var dis = CQ.Ext.ComponentMgr.get(me.id);
				var storeHandle = CQ.Ext.ComponentMgr.get('slideshowGrid'+ me.id).getStore();
				var pageform = dis.findParentByType("form");

				var fieldNames = [ "titleImage", "titleImageAlign", "mainImage", "mainImageAlign", "bgImage",
				                    "ctaImage", "ctaImageAlign","ctaPath", "description",
				                    "hideDesc" ];

                //Check for unsaved values
                for(var i=0; i<panelFields.length; i++) {
					try {
						var value = '';
						var field = CQ.Ext.ComponentMgr.get(panelFields[i] + me.id);
						if(field.getXType().indexOf('image')>-1) {
							value = field.fileReferenceField.getValue().trim();
						} else {
                                   
							value = field.getValue().trim();
							if(value==="Select"){
							value= '';
							}
						}
						if(value) {
							CQ.Ext.Msg.show({title: 'Warning',  msg: 'There are unsaved changes in the dialog!<br><br>Please save all the updates in <b>'
											 + me.title + '</b> before continuing.', icon:CQ.Ext.MessageBox.WARNING,buttons: CQ.Ext.Msg.OK});
							return false;
						}
					} catch(e) { /* do nothing */ }
                }

                CQ.Ext.each(fieldNames,function(fieldName,index) {
					var oldItems = pageform.find("name",'./slideshow/'+ fieldName);
					CQ.Ext.each(oldItems,function(item) {
						pageform.remove(item,true);
					});
				});



                CQ.Ext.each(fieldNames,function(fieldName,index) {
                    pageform.add(new CQ.Ext.form.Hidden({
                	    "name": './slideshow/'+fieldName+'@TypeHint',
                		"value": 'String[]'
                	}));
                });

                if(storeHandle.getCount() <= 0) {
                    pageform.add(new CQ.Ext.form.Hidden({
                        "name": './slideshow@Delete'
                    }));
                }

				CQ.Ext.each(fieldNames,function(fieldName,index) {
					for ( var i = 0; i < CQ.Ext.ComponentMgr.get('slideshowGrid'+ me.id).getStore().getCount(); i++) {
                        var fieldValue = CQ.Ext.ComponentMgr.get('slideshowGrid'+ me.id).getStore().getAt(i)["data"][fieldName];
                        if (fieldValue == null || fieldValue == "")
                            fieldValue = " ";
                            var fieldVar = new CQ.Ext.form.Hidden({
                                "name" : './slideshow/'+ fieldName,
                                "value" : fieldValue
                            });
                        pageform.add(fieldVar);
					}
				});
				pageform.doLayout();
			}, parentDialog);
		}
	}
});

CQ.Ext.reg("slideshowpanel", CQ.SlideshowPanel);