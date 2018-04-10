/**
* Custom JsonReader CQ.ShowcaseJsonReader To read from the showcase node and
* convert it into the desired format as expected by the Grid store.
*/

CQ.ShowcaseJsonReader = function(meta, recordType) {
	CQ.ShowcaseJsonReader.superclass.constructor.call(this, meta, recordType || meta.fields);
};
CQ.Ext.extend(CQ.ShowcaseJsonReader, CQ.Ext.data.JsonReader, {
	read : function(response) {
		var json = response.responseText;
		var o = CQ.Ext.decode(json);
		var output = { hits : new Array() };
		if (o.title) {
			if ((typeof o.mainImage) == "string") {
				output.hits[0] = {
					itemId : "storeItemId",

					mainImage : o.mainImage.trim(),
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
						title : o.title[i].trim(),
						description : o.description[i].trim(),
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
* Custom panel for the Showcase item editing CQ.ShowcasePanel CQ.ShowcasePanel
* extends CQ.Ext.Panel
*/
CQ.ShowcasePanel = CQ.Ext.extend(CQ.Ext.Panel,{
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
            } ];

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
			id : 'mainImage',
			header : "Show case Image",
			width : "70px",
			sortable : false,
			dataIndex : 'mainImage',
			renderer : renderImage
		},  {
			id : 'title',
			name : 'title',
			width : "100px",
			header : "Showcase Title",
			mapping : 'title',
			renderer : columnWrap
		}, {
			id : 'description',
			name : 'description',
			width : "120px",
			header : "Showcase Description",
			mapping : 'description',
			renderer : columnWrap
		}, {
			id : 'ctaPath',
			name : 'ctaPath',
			width : "100px",
			header : "Showcase Link",
			mapping : 'ctaPath',
			renderer : columnWrap
		}, {
            id : 'hideDesc',
            header : "Hide Description for Mobile",
            width : "100px",
            sortable : true,
            dataIndex : 'hideDesc',
            renderer : renderHideDesc
        } ];
		var d = this;

		// The grid store
		var ShowcaseGridStore = new CQ.Ext.data.Store({
			"id" : "ShowcaseGridStore" + d.id,
			"proxy" : new CQ.Ext.data.HttpProxy({
							"url" : this.ajaxRequestURL,
							"method" : "GET"
			}),
			"reader" : new CQ.ShowcaseJsonReader({
							"id" : "itemId",
							"root" : "hits",
							"fields" : fields
			}),
			"xtype" : "store"
		});

		// Left hand form panel fields
		// 1. Showcase image
		var mainImage = {
			name : 'mainImage',
			ddGroups : "media",
			text : "Showcase Image*",
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

	    // 2.Title
		var titleField = {
			fieldLabel : 'Showcase Title',
			fieldDescription : 'Provide title text for showcase image',
			name : 'title',
			height : 30,
			width : 180,
			id : 'titleId' + d.id,
			xtype : 'textfield',
		};

		// 3.Description
		var showcaseDescField = {
		    fieldLabel : 'Showcase Description',
		    fieldDescription : 'Provide the description text',
            name : 'description',
            width : 180,
            id : 'descriptionId' + d.id,
            maxLength:115,
            maxLengthText:'limit reached',
            xtype : 'textarea',
		};

		// 4. Link
		var ctaPathField = {
			fieldLabel : 'Showcase Link',
			fieldDescription : "eg: /content, http://w, https://w",
			name : 'ctaPath',
			height : 30,
			width : 180,
			id : 'ctaPathId' + d.id,
			xtype : 'pathfield',
			rootPath : '/content',
		};

		//5. hide desc for mobile
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
				if (CQ.Ext.ComponentMgr.get('mainImageArea' + d.id).fileReferenceField.getValue() != "") {
                    var link = CQ.Ext.ComponentMgr.get('ctaPathId' + d.id).getValue();
                    var pattern = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;
                    var descriptionText = CQ.Ext.ComponentMgr.get('descriptionId' + d.id).getValue();
                    if(link != '' && !/\/content\/.*/.test(link) && !pattern.test(link)) {
                        CQ.Ext.Msg.alert(CQ.I18n.getMessage("Warning"),
                        CQ.I18n.getMessage("Showcase Link must start with '/content' or 'http://w' or 'https://w'"));
                        return false;
                    }
					if ((self.maximumAllowed) && (CQ.Ext.ComponentMgr.get('showcaseGrid' + d.id).store.getCount() >= self.maximumAllowed)) {
						CQ.Ext.MessageBox.alert(CQ.I18n.getMessage("Warning"),
						CQ.I18n.getMessage("Maximum " + self.maximumAllowed + " are allowed! Try to delete a few then, try again."));
						return false;
				    }
				    if(descriptionText.length>115){
                        CQ.Ext.Msg.alert(CQ.I18n.getMessage("Warning"),
                        CQ.I18n.getMessage("Showcase description can't be more than 115 characters."));
                    	return false;
                    }

                    var myNewRecord = CQ.Ext.data.Record.create({
                        id : 'mainImage',
                        header : "Showcase Image",
                        sortable : true,
                        dataIndex : 'mainImage',
                        renderer : renderImage
                    },  {
                        id : 'title',
                        name : 'title',
                        mapping : 'title'
                    }, {
                        id : 'description',
                        name : 'description',
                        mapping : 'description'
                    }, {
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
                        title : CQ.Ext.ComponentMgr.get('titleId' + d.id).getValue(),
                        description : CQ.Ext.ComponentMgr.get('descriptionId' + d.id).getValue(),
                        ctaPath : CQ.Ext.ComponentMgr.get('ctaPathId' + d.id).getValue(),
                        hideDesc : CQ.Ext.ComponentMgr.get('hideDescId' + d.id).getValue()
                    });
                    // Add record to the grid store
                    CQ.Ext.ComponentMgr.get('showcaseGrid'+ d.id).store.add(myRecord);

                    // Clear the form panel fields after form
                    // submit
                    CQ.Ext.ComponentMgr.get('mainImageArea' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('titleId' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('descriptionId' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('ctaPathId' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('hideDescId' + d.id).reset();

                } else {
				
                    CQ.Ext.Msg.alert("Error","Please select a picture to be the showcase image.");
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
            //autoWidth : true,
            xtype : 'panel',
            layout : {
                type : 'hbox',
                align : 'middle'
            },
            items : [ {
                text : 'Showcase Image*',
                width : 130,
                xtype : 'label'
            }, mainImage

            ]
        };

        // 2.title
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

        //3.description
        var showcaseDescPanel = {
            height : 100,
            border : false,
            autoWidth : true,
            xtype : 'panel',
            layout : {
                type : 'form'
            },
            items : [ showcaseDescField ]
        };

	    // 4.Link
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

        var showcasePanel = {
            border : false,
            xtype : 'panel',
            columnWidth : 0.45,
            layout : {
                type : 'form'
            },
            style : {
                padding : '10px'
            },
            items : [ mainImagePanel, titlePanel, showcaseDescPanel, ctaPathPanel, hideDescPanel, submitButtonPanel ]
        };

        // create the destination Grid
        var showcaseGrid = {
            id : "showcaseGrid" + d.id,
            store : ShowcaseGridStore,
            ddGroup : 'showcaseGridDDGroup',
            enableDragDrop : true,
            columns : cols,
            height : 350,
            enableDragDrop : true,
            columnWidth : 0.55,
            stripeRows : true,
            xtype : "grid",
            listeners : {
                afterrender : function() {
                    // This will make sure we only drop to the
                    // view scroller element
                    var showCaseGridHandle = CQ.Ext.ComponentMgr.get('showcaseGrid' + d.id);
                    var showcaseGridDropTargetEl = showCaseGridHandle.getView().scroller.dom;
                    var showcaseGridDropTarget = new CQ.Ext.dd.DropTarget(showcaseGridDropTargetEl,	{
                        ddGroup : 'showcaseGridDDGroup',
                        notifyDrop : function(ddSource,e, data) {
                            if ((self.maximumAllowed) && (showCaseGridHandle.store.getCount() >= self.maximumAllowed)) {
                                CQ.Ext.MessageBox.alert(CQ.I18n.getMessage("Warning"),
                                CQ.I18n.getMessage("Maximum "+ self.maximumAllowed + " are allowed! Try to delete a few then, try again."));
                                return false;
                            }

                            var targetRowIndex =showCaseGridHandle.getView().findRowIndex(e.getTarget());
                            var records = ddSource.dragData.selections;
                            CQ.Ext.each(records, ddSource.grid.store.remove, ddSource.grid.store);

                            if (targetRowIndex !== false) {
                                showCaseGridHandle.store.insert(targetRowIndex,records);
                            } else {
                                showCaseGridHandle.store.add(records);
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
            items : [ showcasePanel, showcaseGrid ]
        });

        CQ.Ext.apply(this, CQ.Ext.apply(this.initialConfig,config));
        CQ.ShowcasePanel.superclass.initComponent.call(this,arguments);
	},
	onRender : function() {
        CQ.ShowcasePanel.superclass.onRender.apply(this,arguments);
        var parentDialog = this.findParentByType("dialog");
        var panelFields = ["mainImageArea","titleId","descriptionId","ctaPathId","hideDescId"];
        var me = this;
        if (parentDialog) {
            // Load content on page load
            parentDialog.on("loadContent",function(obj) {
                // Load the right grid
                me.originpath = parentDialog.path;
                me.ajaxRequestURL = parentDialog.path+ "/showcase.json";
                CQ.Ext.ComponentMgr.get('showcaseGrid'+ me.id).store.proxy.setUrl(me.ajaxRequestURL);
                CQ.Ext.ComponentMgr.get('showcaseGrid'+ me.id).store.load({});
                CQ.Ext.each(panelFields, function(panelField, index) {
                    CQ.Ext.ComponentMgr.get(panelField + me.id).reset();
                });

            }, parentDialog);

			// Before submit set the form values in hidden
			// fields.
			parentDialog.on("beforesubmit",function() {
				var dis = CQ.Ext.ComponentMgr.get(me.id);
				var showCaseStore = CQ.Ext.ComponentMgr.get('showcaseGrid'+ me.id).getStore();
				var pageform = dis.findParentByType("form");

				var fieldNames = [ "mainImage", "title", "description", "ctaPath",
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
                    } catch(e) { /*do nothing*/ }
                }

                CQ.Ext.each(fieldNames,function(fieldName,index) {
					var oldItems = pageform.find("name",'./showcase/'+ fieldName);
					CQ.Ext.each(oldItems,function(item) {
						pageform.remove(item,true);
					});
				});

				CQ.Ext.each(fieldNames,function(fieldName,index) {
                    pageform.add(new CQ.Ext.form.Hidden({
                        "name": './showcase/'+fieldName+'@TypeHint',
                        "value": 'String[]'
                    }));
                });

			    if(showCaseStore.getCount() <= 0) {
			        pageform.add(new CQ.Ext.form.Hidden({
			            "name" : './showcase@Delete'
			        }));
			    }

				CQ.Ext.each(fieldNames,function(fieldName,index) {
					for ( var i = 0; i < showCaseStore.getCount(); i++) {
                        var fieldValue = showCaseStore.getAt(i)["data"][fieldName];
                        if (fieldValue == null || fieldValue == "")
                            fieldValue = " ";
                            var fieldVar = new CQ.Ext.form.Hidden({
                                    "name" : './showcase/'+ fieldName,
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

CQ.Ext.reg("showcasepanel", CQ.ShowcasePanel);
