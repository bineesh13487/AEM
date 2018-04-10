/**
* Custom JsonReader CQ.ThumbnailJsonReader To read from the Thumbnail node and
* convert it into the desired format as expected by the Grid store.
*/
CQ.ThumbnailJsonReader = function(meta, recordType) {
	CQ.ThumbnailJsonReader.superclass.constructor.call(this, meta, recordType || meta.fields);
};
CQ.Ext.extend(CQ.ThumbnailJsonReader, CQ.Ext.data.JsonReader, {
	read : function(response) {
		var json = response.responseText;
		var o = CQ.Ext.decode(json);
		var output = { hits : new Array() };
		if (o.mainImage) {
			if ((typeof o.mainImage) == "string") {
				output.hits[0] = {
					itemId : "storeItemId",

					mainImage : o.mainImage.trim(),
					thumbnailImage : o.thumbnailImage.trim(),
					title : o.title.trim(),
					description : o.description.trim(),
					ctaPath : o.ctaPath.trim(),
					hideDesc : o.hideDesc.replace(/\s+/g, '').trim()

				};
			} else {
				for ( var i = 0; i < o.mainImage.length; i++) {
					output.hits[i] = {
						itemId : "storeItemId" + (i + 1),

						mainImage : o.mainImage[i].trim(),
						thumbnailImage : o.thumbnailImage[i].trim(),
						ctaPath : o.ctaPath[i].trim(),
						title : o.title[i].trim(),
						description : o.description[i].trim(),
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
* Custom panel for the Thumbnail item editing CQ.ThumbnailPanel CQ.ThumbnailPanel
* extends CQ.Ext.Panel
*/
CQ.ThumbnailPanel = CQ.Ext.extend(CQ.Ext.Panel,{
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
			name : 'mainImage',
			mapping : 'mainImage'
			}, {
            name : 'thumbnailImage',
            mapping : 'thumbnailImage'
            }, {
			name : "title",
			mapping : 'title'
			}, {
			name : "description",
			mapping : 'description'
			}, {
            name : "ctaPath",
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

		var cols = [{
			id : 'mainImage',
			header : "Slide Image",
			width : "70px",
			sortable : false,
			dataIndex : 'mainImage',
			renderer : renderImage
		},  {
            id : 'thumbnailImage',
            header : "Thumbnail Image*",
            width : "70px",
            sortable : false,
            dataIndex : 'thumbnailImage',
            renderer : renderImage
        }, {
			id : 'title',
			name : 'title',
			width : "90px",
			header : "Slide Title",
			mapping : 'title',
			renderer : columnWrap
		}, {
			id : 'description',
			name : 'description',
			width : "150px",
			header : "Slide Description",
			mapping : 'description',
			renderer : columnWrap
		}, {
			id : 'ctaPath',
			name : 'ctaPath',
			width : "100px",
			header : "Slide Link",
			mapping : 'ctaPath',
			renderer : columnWrap
		}];
		var d = this;

		// The grid store
		var ThumbnailGridStore = new CQ.Ext.data.Store({
			"id" : "ThumbnailGridStore" + d.id,
			"proxy" : new CQ.Ext.data.HttpProxy({
				"url" : this.ajaxRequestURL,
				"method" : "GET"
			}),
			"reader" : new CQ.ThumbnailJsonReader({
				"id" : "itemId",
				"root" : "hits",
				"fields" : fields
			}),
			"xtype" : "store"
		});

		// Left hand form panel fields
		// 1. Slide image
		var mainImage = {
			name : 'mainImage',
			ddGroups : "media",
			text : "Slide Image",
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
        // 2.Thumbnail Image
		var thumbnailImage = {
			fieldLabel : 'Thumbnail Image',
			name : 'thumbnailImage',
			ddGroups : "media",
			text : "Thumbnail Image",
			xtype : 'html5smartimage',
			fileNameParameter : "./fileName",
			allowUpload : false,
			height : 150,
			width : 180,
			border : true,
			disableZoom : 'true',
			id : 'thumbnailImageArea' + d.id,
			name : './file',
			uploadUrl : '/tmp/upload_test/*',
            listeners : {
                imagestate : RWD.CQ.util.enableImageToolbar
            }
		};
		// 3.Title
		var titleField = {
			fieldLabel : 'Slide Title',
			fieldDescription : 'Provide title text for the slide',
			name : 'title',
			height : 30,
			width : 180,
			id : 'titleId' + d.id,
			xtype : 'textfield'
		};
		// 4.Description
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
		// 6. Call to Action Path
		var ctaPathField = {
			fieldLabel : 'Slide Link',
			fieldDescription : "eg: /content, http://w, https://w",
			name : 'ctaPath',
			height : 30,
			width : 180,
			id : 'ctaPathId' + d.id,
			xtype : 'pathfield',
			rootPath : '/content',
		};
		//8. hide desc for mobile
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
				if (CQ.Ext.ComponentMgr.get('thumbnailImageArea' + d.id).fileReferenceField.getValue() != "" && CQ.Ext.ComponentMgr.get('mainImageArea' + d.id).fileReferenceField.getValue() != "" ) {
					var link = CQ.Ext.ComponentMgr.get('ctaPathId' + d.id).getValue();
					var pattern = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;
					var descriptionText = CQ.Ext.ComponentMgr.get('descriptionId' + d.id).getValue();
					if(link != '' && !/\/content\/.*/.test(link) && !pattern.test(link)) {
						CQ.Ext.Msg.alert(CQ.I18n.getMessage("Warning"),
						CQ.I18n.getMessage("Slide Link must start with '/content' or 'http://w' or 'https://w'"));
						return false;
					}
					if ((self.maximumAllowed) && (CQ.Ext.ComponentMgr.get('ThumbnailGrid' + d.id).store.getCount() >= self.maximumAllowed)) {
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
						id : 'mainImage',
						header : "Main Image",
						sortable : true,
						dataIndex : 'mainImage',
						renderer : renderImage
					}, {
						id : 'thumbnailImage',
						header : 'Thumbnail Image',
						sortable : true,
						dataIndex : 'thumbnailImage',
						renderer : renderImage
					},  {
						id : 'title',
						name : 'title',
						mapping : 'title'
					}, {
						id : 'description',
						name : 'description',
						mapping : 'description'
					},  {
						id : 'ctaPath',
						name : 'ctaPath',
						mapping : 'ctaPath'
					}, {
						id : 'hideDesc',
						name : 'hideDesc',
						mapping : 'hideDesc'
					});

					var myRecord = new myNewRecord({
                        mainImage : CQ.Ext.ComponentMgr.get('mainImageArea'+ d.id).fileReferenceField.getValue(),
                        thumbnailImage : CQ.Ext.ComponentMgr.get('thumbnailImageArea'+ d.id).fileReferenceField.getValue(),
                        title : CQ.Ext.ComponentMgr.get('titleId' + d.id).getValue(),
                        description : CQ.Ext.ComponentMgr.get('descriptionId' + d.id).getValue(),
                        ctaPath : CQ.Ext.ComponentMgr.get('ctaPathId' + d.id).getValue(),
                        hideDesc : CQ.Ext.ComponentMgr.get('hideDescId' + d.id).getValue()
    				});
                    // Add record to the grid store
                    CQ.Ext.ComponentMgr.get('ThumbnailGrid'+ d.id).store.add(myRecord);

                    // Clear the form panel fields after form
                    // submit
                    CQ.Ext.ComponentMgr.get('mainImageArea' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('thumbnailImageArea' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('titleId' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('descriptionId' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('ctaPathId' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('hideDescId' + d.id).reset();
                } else {
	                CQ.Ext.Msg.alert("Error","Provide the mandatory fields for the item!");
					   
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

        // 1.Main Image
        var mainImagePanel = {
            height : 180,
            border : false,
            xtype : 'panel',
            layout : {
                type : 'hbox',
                align : 'middle'
            },
            items : [ {
                text : 'Slide Image*',
                width : 130,
                xtype : 'label'
            }, mainImage]
        };
        // 2. Thumbnail Image
        var thumbnailImagePanel = {
            height : 180,
            border : false,
            //autoWidth : true,
            xtype : 'panel',
            layout : {
                type : 'hbox',
                align : 'middle'
            },
            items : [ {
                text : 'Thumbnail Image*',
                width : 130,
                xtype : 'label'
            }, thumbnailImage]
        };
        // 3.title
        var titlePanel = {
            height : 50,
            border : false,
            autoWidth : true,
            xtype : 'panel',
            layout : {
                type : 'form'
            },
            items : [ titleField ]
        };
        //4.description
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
        // 5.CTA Path
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
            items : [ mainImagePanel, thumbnailImagePanel, titlePanel, descriptionPanel, ctaPathPanel,
                      hideDescPanel, submitButtonPanel ]
        };
        // create the destination Grid
        var ThumbnailGrid = {
            id : "ThumbnailGrid" + d.id,
            store : ThumbnailGridStore,
            ddGroup : 'thumbnailGridDDGroup',
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
                    var gridHandle = CQ.Ext.ComponentMgr.get('ThumbnailGrid' + d.id);
                    var ThumbnailGridDropTargetEl = gridHandle.getView().scroller.dom;
                    var ThumbnailGridDropTarget = new CQ.Ext.dd.DropTarget(ThumbnailGridDropTargetEl, {
                        ddGroup : 'thumbnailGridDDGroup',
                        notifyDrop : function(ddSource,e, data) {
                            if ((self.maximumAllowed) && (gridHandle.store.getCount() >= self.maximumAllowed)) {
                                CQ.Ext.MessageBox.alert(CQ.I18n.getMessage("Warning"),
                                CQ.I18n.getMessage("Maximum "+ self.maximumAllowed+ " are allowed! Try to delete a few then, try again."));
                                return false;
                            }
                            var targetRowIndex = gridHandle.getView().findRowIndex(e.getTarget());
                            var records = ddSource.dragData.selections;
                            CQ.Ext.each(records,ddSource.grid.store.remove,ddSource.grid.store);
                            if (targetRowIndex !== false) {
                                gridHandle.store.insert(targetRowIndex,records);
                            } else {
                                gridHandle.store.add(records);
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
                                //title
                                CQ.Ext.ComponentMgr.get('titleId'+ d.id).setValue(rowHandle.get('title'));
                                // hide desc
                                CQ.Ext.ComponentMgr.get('hideDescId'+ d.id).setValue(rowHandle.get('hideDesc'));
                                //description
                                CQ.Ext.ComponentMgr.get('descriptionId'+ d.id).setValue(rowHandle.get('description'));
                                //cta Path
                                CQ.Ext.ComponentMgr.get('ctaPathId'+ d.id).setValue(rowHandle.get('ctaPath'));
                                // thumbnailImage
                                var dragThumbnailImageData = {
                                    records : [{
                                        pathThumb : rowHandle.get('thumbnailImage'),
                                        get : function() { return this.pathThumb; }
                                    }],
                                    single : "true"
                                };
                                CQ.Ext.ComponentMgr.get('thumbnailImageArea'+ d.id).handleDrop(dragThumbnailImageData);

                                //main image
                                var dragMainImageData = {
                                    records : [{
                                        pathMain : rowHandle.get('mainImage'),
                                        get : function() {	return this.pathMain;	}
                                    }],
                                    single : "true"
                                };
                                CQ.Ext.ComponentMgr.get('mainImageArea'+ d.id).handleDrop(dragMainImageData);
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
            items : [ galleryPanel, ThumbnailGrid ]
        });
        CQ.Ext.apply(this, CQ.Ext.apply(this.initialConfig,config));
        CQ.ThumbnailPanel.superclass.initComponent.call(this,arguments);
	},
	onRender : function() {
        CQ.ThumbnailPanel.superclass.onRender.apply(this,arguments);
        var parentDialog = this.findParentByType("dialog");
        var panelFields = ["mainImageArea","thumbnailImageArea","titleId","descriptionId","ctaPathId","hideDescId"];

        var me = this;
        if (parentDialog) {
            // Load content on page load
            parentDialog.on("loadContent",function(obj) {
                // Load the right grid
                me.originpath = parentDialog.path;
                me.ajaxRequestURL = parentDialog.path+ "/thumbnail.json";
                CQ.Ext.ComponentMgr.get('ThumbnailGrid'+ me.id).store.proxy.setUrl(me.ajaxRequestURL);
                CQ.Ext.ComponentMgr.get('ThumbnailGrid'+ me.id).store.load({});
                CQ.Ext.each(panelFields, function(panelField, index) {
                    CQ.Ext.ComponentMgr.get(panelField + me.id).reset();
                });

            }, parentDialog);

			// Before submit set the form values in hidden
			// fields.
			parentDialog.on("beforesubmit",function() {
				var dis = CQ.Ext.ComponentMgr.get(me.id);
				var pageform = dis.findParentByType("form");
                var storeHandle = CQ.Ext.ComponentMgr.get('ThumbnailGrid'+ me.id).getStore();

				var fieldNames = [ "mainImage", "thumbnailImage", "title", "description", "ctaPath",
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
						}
						if(value) {
							CQ.Ext.Msg.show({title: 'Warning',  msg: 'There are unsaved changes in the dialog!<br><br>Please save all the updates in <b>'
											 + me.title + '</b> before continuing.', icon:CQ.Ext.MessageBox.WARNING,buttons: CQ.Ext.Msg.OK});
							return false;
						}
					} catch(e) { /* do nothing */ }
                }

                CQ.Ext.each(fieldNames,function(fieldName,index) {
					var oldItems = pageform.find("name",'./thumbnail/'+ fieldName);
					CQ.Ext.each(oldItems,function(item) {
						pageform.remove(item,true);
					});
				});

				CQ.Ext.each(fieldNames,function(fieldName,index) {
                    pageform.add(new CQ.Ext.form.Hidden({
                        "name": './thumbnail/'+fieldName+'@TypeHint',
                        "value": 'String[]'
                    }));
                });

                if(storeHandle.getCount() <= 0) {
                    pageform.add(new CQ.Ext.form.Hidden({
                        "name": './thumbnail@Delete'
                    }));
                }

				CQ.Ext.each(fieldNames,function(fieldName,index) {
					for ( var i = 0; i < storeHandle.getCount(); i++) {
                        var fieldValue = storeHandle.getAt(i)["data"][fieldName];
                        if (fieldValue == null || fieldValue == "")
                            fieldValue = " ";
                            var fieldVar = new CQ.Ext.form.Hidden({
                                    "name" : './thumbnail/'+ fieldName,
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

CQ.Ext.reg("thumbnailpanel", CQ.ThumbnailPanel);
