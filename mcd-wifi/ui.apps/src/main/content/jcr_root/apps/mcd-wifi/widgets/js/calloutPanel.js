/**
 * Custom JsonReader CQ.CalloutJsonReader To read from the callout node and
 * convert it into the desired format as expected by the Grid store.
 */

CQ.CalloutJsonReader = function(meta, recordType) {
    CQ.CalloutJsonReader.superclass.constructor.call(this, meta, recordType
            || meta.fields);
};

CQ.Ext.extend(CQ.CalloutJsonReader, CQ.Ext.data.JsonReader, {
    read : function(response) {
        var json = response.responseText;
        var o = CQ.Ext.decode(json);
        var output = {
            hits : new Array()
        };

        if (o.links) {
            if ((typeof o.links) === "string") {
                output.hits[0] = {
                    itemId : "storeItemId1",
                    calloutImages : o.calloutImages,
                    altTexts : o.altTexts,
                    targets : o.targets,
                    overlayTexts : o.overlayTexts,
                    overlayPosition : o.overlayPosition,
                    overlayColor : o.overlayColor,
                    links : o.links,
                    startTime : o.startTime,
                    endTime : o.endTime
                };
            } else {
                for (var i = 0; i < o.links.length; i++) {
                    output.hits[i] = {
                        itemId : "storeItemId" + (i + 1),
                        calloutImages : o.calloutImages[i],
                        altTexts : o.altTexts[i],
                        targets : o.targets[i],
                        overlayTexts : o.overlayTexts[i],
                        overlayPosition : o.overlayPosition[i],
                        overlayColor : o.overlayColor[i],
                        links : o.links[i],
                        startTime : o.startTime[i],
                        endTime : o.endTime[i]
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
 * Custom panel for the Callout item editing CQ.CalloutPanel
 * CQ.CalloutPanel extends CQ.Ext.Panel
 */
CQ.CalloutPanel = CQ.Ext.extend(CQ.Ext.Panel, {
    originPath : CQ.WCM.getPagePath(),
    ajaxRequestURL : null,
    ajaxmethod : null,
    perPage : 6,
    height : 520,
    Width : 700,
    maximumAllowed : null,
    nodePath : '',
    displayTime : false,
    initComponent : function() {
        this.ajaxRequestURL = this.originPath + ".json";
        var fields = [{
            name : 'calloutImages',
            mapping : 'calloutImages'
        }, {
            name : 'altTexts',
            mapping : 'altTexts'
        }, {
            name : 'targets',
            mapping : 'targets'
        }, {
            name : 'overlayTexts',
            mapping : 'overlayTexts'
        }, {
            name : 'overlayPosition',
            mapping : 'overlayPosition'
        }, {
            name : 'overlayColor',
            mapping : 'overlayColor'
        }, {
            name : 'links',
            mapping : 'links'
        }, {
            name : 'startTime',
            mapping : 'startTime'
        }, {
            name : 'endTime',
            mapping : 'endTime'
        }];

        var self = this;

        /**
         * Image renderer function for rendering of the image in
         * the grid panel
         * 
         * @param val -
         *            the image source
         * @returns {String}
         */
        function renderImage(val) {
            return '<img src="' + val
                    + '" style="width:50px;"> ';
        }

        /**
         * The column wrap function to allow wrapping of long
         * text in the grid panel columns.
         * 
         * @param val -
         *            The HTML content
         * @returns {String}
         */
        function columnWrap(val) {
            return '<div style="white-space:normal !important;"><p>'
                    + val + '</p></div>';
        }

        var cols = [{
            id : 'image',
            width : "60px",
            sortable : false,
            header : "Image",
            dataIndex : "calloutImages",
            mapping : 'calloutImages',
            renderer : renderImage
        }, {
            id : 'alttext',
            header : "Alt Text",
            width : "0px",
            name : "altTexts",
            mapping : 'altTexts',
            renderer : columnWrap
        }, {
            id : 'target',
            header : "Target",
            width : "0px",
            name : "targets",
            mapping : 'targets',
            renderer : columnWrap
        }, {
            id : 'overlayText',
            name : "overlayTexts",
            header : "Text",
            width : "120px",
            sortable : false,
            mapping : 'overlayTexts',
            renderer : columnWrap
        }, {
            id : 'position',
            header : "Position",
            width : "0px",
            name : "overlayPosition",
            mapping : 'overlayPosition',
            renderer : columnWrap
        }, {
            id : 'color',
            header : "Color",
            width : "0px",
            name : "overlayColor",
            mapping : 'overlayColor',
            renderer : columnWrap
        }, {
            id : 'link',
            name : 'links',
            width : "120px",
            sortable : false,
            header : "Link",
            mapping : 'links',
            renderer : columnWrap
        }, {
            id : 'startTime',
            name : 'startTime',
            width : "50px",
            header : "Start Time",
            mapping : 'startTime',
            renderer : columnWrap
        }, {
            id : 'endTime',
            header : "End Time",
            width : "50px",
            name : "endTime",
            mapping : 'endTime',
            renderer : columnWrap
        }];

        var d = this;

        // The grid store
        var CalloutGridStore = new CQ.Ext.data.Store({
            "id" : "CalloutGridStore" + d.id,
            "proxy" : new CQ.Ext.data.HttpProxy({
                "url" : this.ajaxRequestURL,
                "method" : "GET"
            }),
            "reader" : new CQ.CalloutJsonReader({
                "id" : "itemId",
                "root" : "hits",
                "fields" : fields
            }),
            "xtype" : "store"
        });

        // Left hand form panel fields
    	var image = {
            name : 'calloutImage',
            ddGroups : "media",
            fieldLabel : "Image*",
			text:"Image*",
            xtype : 'html5smartimage',
            uploadTextReference : 'Drop an image.',
            fileNameParameter : "./fileName",
            fileReferenceParameter : "./fileReference",
            allowUpload : false,
            height : 150,
            width : 150,
            border : true,
            disableZoom : 'true',
            id : 'image' + d.id,
            name : './file',
            uploadUrl : '/tmp/upload_test/*',
            listeners : {
                imagestate : RWD.CQ.util.enableImageToolbar
            }
        };

        var link = {
            fieldLabel : 'Link*',
            fieldDescription : 'Please choose or enter any URL for the callout.',
            name : 'links',
            width: 155,
            id : 'link' + d.id,
            xtype : 'pathfield'
        }; 

        var target = {
            fieldLabel : 'Target',
            fieldDescription : 'Please choose the target for URL.',
            name : 'targets',
            id : 'target' + d.id,
            xtype : 'selection',
            type : 'select',
            options	: [{"text":"Same Window","value":"_self"},{"text":"New Window","value":"_blank"},{"text":"Modal Window","value":"popupLink"}]
        };

        var text = {
            fieldLabel : 'Text',
            fieldDescription : 'Please provide the text to be displayed over the callout image.',
            name : 'overlayTexts',
            id : 'overlay' + d.id,
            xtype : 'textfield'
        };

        var textPosition= {
            fieldLabel : 'Text Position',
            fieldDescription : 'Please choose the horizontal alignment of text over image.',
            name : 'overlayPosition'+d.id,
            width : 300,
            id : 'position' + d.id,
            xtype : 'selection',
            type: 'radio',
            options : [{"text":"Left","value":"left"},{"text":"Center","value":"center"},{"text":"Right","value":"right"}]
        };

        var textColor= {
            fieldLabel : 'Text Color',
            fieldDescription : '',
            name : 'overlayColor',
            width : 155,
            id : 'color' + d.id,
            xtype : 'colorfield',
            defaultColor: ''
        };

        var altText = {
            fieldLabel : 'Alt Text',
            fieldDescription : '',
            name : 'altTexts',
            id : 'alttext' + d.id,
            xtype : 'textfield'
        };

        var startTime = {
            fieldLabel : 'Start Time',
            fieldDescription : 'Please choose the start time for this callout to display.',
            name : 'startTime',
            width: 155,
            id : 'startTime' + d.id,
            xtype : 'timefield',
			format:'H:i'
        };

        var endTime = {
            fieldLabel : 'End Time',
            fieldDescription : 'Please choose the end time for this callout. The end time should always be greater than the start time.',
            name : 'endTime',
            width: 155,
            id : 'endTime' + d.id,
            xtype : 'timefield',
			format:'H:i'
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
                if (CQ.Ext.ComponentMgr.get('link' + d.id).getValue() != "" && CQ.Ext.ComponentMgr.get('image' + d.id).fileReferenceField.getValue() != "") {

                    //Validation for maximum number of values allowed
                    if ((self.maximumAllowed) && (CQ.Ext.ComponentMgr
                        .get('calloutGrid' + d.id).store.getCount() >= self.maximumAllowed)) {
                        CQ.Ext.MessageBox.alert("Error", "Maximum " + self.maximumAllowed + " are allowed! Try to delete a few then, try again.");
                        return false;
                    }
                    //Validation for start and end time
                    var startTime = CQ.Ext.ComponentMgr.get('startTime' + d.id).getValue();
                    var endTime = CQ.Ext.ComponentMgr.get('endTime' + d.id).getValue();
                    if(startTime != '') {
                        if(endTime != '' && startTime > endTime) {
                            CQ.Ext.MessageBox.alert('Error','Value of Start Time should be less than that of End Time.');
                            return false;
                        } else if(endTime == '') {
                            CQ.Ext.MessageBox.alert('Error','Please provide value of End Time.');
                            return false;
                        }
                    } else if(endTime != '') {
                        CQ.Ext.MessageBox.alert('Error','Please provide value of Start Time.');
                        return false;
                    }

                    //Create a record
                    var myNewRecord = CQ.Ext.data.Record.create({
                        id : 'image',
                        name : "calloutImages",
                        mapping : "calloutImages",
                    }, {
                        id : 'alttext',
                        name : "altTexts",
                        mapping : "altTexts",
                    }, {
                        id : 'target',
                        name : "targets",
                        mapping : "targets",
                    }, {
                        id : 'overlay',
                        name : 'overlayTexts',
                        mapping : 'overlayTexts'
                    }, {
                        id : 'position',
                        name : 'overlayPosition',
                        mapping : 'overlayPosition'
                    }, {
                        id : 'color',
                        name : 'overlayColor',
                        mapping : 'overlayColor'
                    }, {
                        id : 'link',
                        name : 'links',
                        mapping : 'links'
                    }, {
                        id : 'startTime',
                        name : 'startTime',
                        mapping : 'startTime'
                    }, {
                        id : 'endTime',
                        name : 'endTime',
                        mapping : 'endTime'
                    });

                    //Save values in the record
                    var myRecord = new myNewRecord({
                        calloutImages : CQ.Ext.ComponentMgr.get('image' + d.id).fileReferenceField.getValue(),
                        altTexts : CQ.Ext.ComponentMgr.get('alttext' + d.id).getValue(),
                        targets : CQ.Ext.ComponentMgr.get('target' + d.id).getValue(),
                        overlayTexts : CQ.Ext.ComponentMgr.get('overlay' + d.id).getValue(),
                        overlayPosition : CQ.Ext.ComponentMgr.get('position' + d.id).getValue(),
                        overlayColor : CQ.Ext.ComponentMgr.get('color' + d.id).getValue(),
                        links : CQ.Ext.ComponentMgr.get('link' + d.id).getValue(),
                        startTime : CQ.Ext.ComponentMgr.get('startTime' + d.id).getValue(),
                        endTime : CQ.Ext.ComponentMgr.get('endTime' + d.id).getValue()
                    });

                    // Add record to the grid store
                    CQ.Ext.ComponentMgr.get('calloutGrid' + d.id).store.add(myRecord);

                    // Clear the form panel fields after form submit
                    CQ.Ext.ComponentMgr.get('image' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('alttext' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('target' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('overlay' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('position' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('color' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('link' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('startTime' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('endTime' + d.id).reset();
                } else {
				    
					  CQ.Ext.Msg.alert("Error", "Please provide the mandatory fields for the Callout.");
					
					
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
                padding : '10 0 0 100'
            },
            items : [ submitButton ]
        };

		var imagePanel = {
            height : 180,
            border : false,
            xtype : 'panel',
            layout : {
                type : 'hbox',
                align : 'left'
            },
            items : [{
                text : 'Image*',
                width : 130,
                xtype : 'label'
            }, image]
        };

        var linkPanel = {
            /*height : 50,
            width :100,*/
            border : false,
            autoWidth : true,
            xtype : 'panel',
            /*style : {
                margin : '5,5,45,50'
            },*/
            layout : {
                type : 'form'
            },
            items : [ link ]
        }; 

		 var targetPanel = {
            /*height : 50,*/
            border : false,
            autoWidth : true,
            xtype : 'panel',
            layout : {
                type : 'form'
            },
            items : [ target ]
        };

		var textPanel = {
            /*height : 50,*/
            border : false,
            autoWidth : true,
            xtype : 'panel',
            layout : {
                type : 'form'
            },
            items : [ text ]
        };

		var positionPanel = {
            /*height : 50,*/
            border : false,
            autoWidth : true,
            xtype : 'panel',
            layout : {
                type : 'form'
            },
            items : [ textPosition ]
        };

		var colorPanel = {
            /*height : 50,*/
            border : false,
            autoWidth : true,
            xtype : 'panel',
            layout : {
                type : 'form'
            },
            items : [ textColor ]
        };

		var altTextPanel = {
            /*height : 50,*/
            border : false,
            autoWidth : true,
            xtype : 'panel',
            layout : {
                type : 'form'
            },
            items : [ altText ]
        };

		var startTimePanel = {
            /*height : 50,*/
            border : false,
            autoWidth : true,
            xtype : 'panel',
            layout : {
                type : 'form'
            },
            items : [ startTime ]
        };

		var endTimePanel = {
            /*height : 50,*/
            border : false,
            autoWidth : true,
            xtype : 'panel',
            layout : {
                type : 'form'
            },
            items : [ endTime ]
        };

        var formPanel = {
            border : false,
            xtype : 'panel',
            columnWidth : 0.45,
            layout : {
                type : 'form'
            },
            style : {
                padding : '10px'
            },
            items : [imagePanel, linkPanel, targetPanel, textPanel, positionPanel, colorPanel, altTextPanel, startTimePanel, endTimePanel, submitButtonPanel] 
        };

        // create the destination Grid
        var calloutGrid = {
            id : "calloutGrid" + d.id,
            store : CalloutGridStore,
            ddGroup : 'calloutGridDDGroup',
            enableDragDrop : true,
            columns : cols,
            height : 500,
            enableDragDrop : true,
            columnWidth : 0.55, 
            stripeRows : true,
            xtype : "grid",
            listeners : {
                afterrender : function() {
                    // This will make sure we only drop to the
                    // view scroller element
                    var calloutGridDropTargetEl = CQ.Ext.ComponentMgr
                        .get('calloutGrid' + d.id).getView().scroller.dom;
                    var calloutGridDropTarget = new CQ.Ext.dd.DropTarget(
                        calloutGridDropTargetEl, {
                        ddGroup : 'calloutGridDDGroup',
                        notifyDrop : function(ddSource, e, data) {

                            if ((self.maximumAllowed) && (CQ.Ext.ComponentMgr
                                .get('calloutGrid' + d.id).store.
                                getCount() >= self.maximumAllowed)) {
                                CQ.Ext.MessageBox.alert(CQ.I18n.getMessage("Warning"),
                                    CQ.I18n.getMessage("Maximum " + self.maximumAllowed
                                        + " are allowed! Try to delete a few then, try again."));
                                return false;
                            }

                            var targetRowIndex = CQ.Ext.ComponentMgr.get('calloutGrid' + d.id)
                                .getView().findRowIndex(e.getTarget());
                            var records = ddSource.dragData.selections;

                            CQ.Ext.each(records, ddSource.grid.store.remove, ddSource.grid.store);

                            if (targetRowIndex !== false) {
                                CQ.Ext.ComponentMgr.get('calloutGrid' + d.id).store.insert(
                                    targetRowIndex, records);
                            } else {
                                CQ.Ext.ComponentMgr.get('calloutGrid' + d.id).store.add(records);
                            }

                            CQ.Ext.select('.x-grid3-body').setStyle('margin-bottom', '50px');
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

                                var dragimagedata = {
                                    records : [{
                                        path : grid.getStore().getAt(rowIndex).get(
                                            'calloutImages'),
                                        get : function() {
                                            return this.path;
                                        }
                                    }],
                                    single : "true"
                                };
                                CQ.Ext.ComponentMgr.get('image'+ d.id).handleDrop(dragimagedata);
                                CQ.Ext.ComponentMgr.get('alttext' + d.id).setValue(grid.getStore().getAt(rowIndex).get('altTexts'));
                                CQ.Ext.ComponentMgr.get('target' + d.id).setValue(grid.getStore().getAt(rowIndex).get('targets'));
                                CQ.Ext.ComponentMgr.get('overlay' + d.id).setValue(grid.getStore().getAt(rowIndex).get('overlayTexts'));
                                CQ.Ext.ComponentMgr.get('position' + d.id).setValue(grid.getStore().getAt(rowIndex).get('overlayPosition'));
                                CQ.Ext.ComponentMgr.get('color' + d.id).setValue(grid.getStore().getAt(rowIndex).get('overlayColor'));
                                CQ.Ext.ComponentMgr.get('link' + d.id).setValue(grid.getStore().getAt(rowIndex).get('links'));
                                CQ.Ext.ComponentMgr.get('startTime' + d.id).setValue(grid.getStore().getAt(rowIndex).get('startTime'));
                                CQ.Ext.ComponentMgr.get('endTime' + d.id).setValue(grid.getStore().getAt(rowIndex).get('endTime'));
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
            items : [ formPanel, calloutGrid ]
        });

        CQ.Ext.apply(this, CQ.Ext.apply(this.initialConfig, config));
        CQ.CalloutPanel.superclass.initComponent.call(this, arguments);
    },
    onRender : function() {
        CQ.CalloutPanel.superclass.onRender.apply(this, arguments);
        var parentDialog = this.findParentByType("dialog");
        var panelFields = [ "image","alttext","target","overlay","position","color","link","startTime","endTime"];

        var me = this;
        if (parentDialog) {
            // Load content on page load
            parentDialog.on("loadContent", function(obj) {
                // Load the right grid
                me.originpath = parentDialog.path;
                me.ajaxRequestURL = parentDialog.path + me.nodePath + ".json";
                CQ.Ext.ComponentMgr.get('calloutGrid' + me.id).store.proxy
                    .setUrl(me.ajaxRequestURL);
                CQ.Ext.ComponentMgr.get('calloutGrid' + me.id).store.load({});
                CQ.Ext.each(panelFields, function(panelField, index) {
                    CQ.Ext.ComponentMgr.get(panelField + me.id).reset();
                });

                if(me.displayTime) {
                    me.showTimeFields();
                } else {
                    me.hideTimeFields();
                }
            }, parentDialog);

            // Before submit set the form values in hidden
            // fields.
            parentDialog.on("beforesubmit", function() {
                var dis = CQ.Ext.ComponentMgr.get(me.id);
                var pageform = dis.findParentByType("form");
                var fieldNames = [ "calloutImages","altTexts", "targets", "overlayTexts", "overlayPosition", "overlayColor", "links", "startTime", "endTime"];

                //Validation Checks
                var type = this.getField('./type').getValue();
                if(type != 'static') {
                    var number = this.getField('./number').getValue();
                    for(var i=1; i<=number; i++) {
                        if(me.nodePath == ('/callout' + i)) {
                            //Check for unsaved values
                            for(var i=0; i<panelFields.length; i++) {
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
                            }
                            //Added validation check for empty panel
                			var gridLength = CQ.Ext.ComponentMgr.get('calloutGrid' + me.id).getStore().getCount();
                            if(gridLength == 0) {
                                CQ.Ext.Msg.show({title: 'Callout Validation',  msg: 'Please provide the mandatory configurations for '
                                                 +me.title+'.',icon:CQ.Ext.MessageBox.ERROR,buttons: CQ.Ext.Msg.OK});
                                return false;
                            }
                        }
                    }
                }

                CQ.Ext.each(fieldNames, function(fieldName, index) {
                    var oldItems = pageform.find("name", '.' + me.nodePath + '/' + fieldName);
                    CQ.Ext.each(oldItems, function(item) {
                        pageform.remove(item, true);
                    });
                });

                CQ.Ext.each(fieldNames, function(fieldName, index) {
                    for (var i = 0; i < gridLength; i++) {
                        var fieldValue = CQ.Ext.ComponentMgr.get('calloutGrid' + me.id)
                            .getStore().getAt(i)["data"][fieldName];
                        if (fieldValue == null || fieldValue == "")
                            fieldValue = " ";
                        var fieldVar = new CQ.Ext.form.Hidden({
                            "name" : '.' + me.nodePath + '/' + fieldName,
                            "value" : fieldValue
                        });

                        pageform.add(fieldVar);
                    }
                });
                pageform.doLayout();
            }, parentDialog);
        }
    },
    showTimeFields: function() {
        CQ.Ext.ComponentMgr.get('startTime' + this.id).show();
        CQ.Ext.ComponentMgr.get('endTime' + this.id).show();
    },
    hideTimeFields: function() {
        CQ.Ext.ComponentMgr.get('startTime' + this.id).hide();
        CQ.Ext.ComponentMgr.get('endTime' + this.id).hide();
    }
});
CQ.Ext.reg("calloutpanel", CQ.CalloutPanel);