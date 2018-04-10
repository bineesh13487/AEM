/**
 * Custom JsonReader CQ.PromosectionJsonReader To read from the promosectionGrid node and
 * convert it into the desired format as expected by the Grid store.
 */
CQ.PromosectionJsonReader = function(meta, recordType) {
    CQ.PromosectionJsonReader.superclass.constructor.call(this, meta, recordType || meta.fields);
};
CQ.Ext.extend(CQ.PromosectionJsonReader, CQ.Ext.data.JsonReader, {
    read: function(response) {
        var json = response.responseText;
        var o = CQ.Ext.decode(json);
        var output = {
            hits: new Array()
        };
        if (o.mainImage) {
            if ((typeof o.mainImage) == "string") {
                output.hits[0] = {
                    itemId: "storeItemId",
                    mainImage: o.mainImage.trim(),
                    path: o.path.trim(),
                    title: o.title.trim(),
                    description: o.description.trim(),
                    activateAd: o.activateAd.replace(/\s+/g, '').trim()
                };
            } else {
                for (var i = 0; i < o.mainImage.length; i++) {
                    output.hits[i] = {
                        itemId: "storeItemId" + (i + 1),
                        mainImage: o.mainImage[i],
                        path: o.path[i].trim(),
                        title: o.title[i].trim(),
                        description: o.description[i].trim(),
                        activateAd: o.activateAd[i].replace(/\s+/g, '').trim()
                    };
                }
            }
        }
        if (!o) {
            throw {
                message: 'JsonReader.read: Json object not found'
            };
        }
        return this.readRecords(output);
    }
});
/**
 * Custom panel for the Promosection item editing CQ.PromosectionPanel CQ.PromosectionPanel
 * extends CQ.Ext.Panel
 */
CQ.PromosectionPanel = CQ.Ext.extend(CQ.Ext.Panel, {
    originPath: CQ.WCM.getPagePath(),
    ajaxRequestURL: null,
    ajaxmethod: null,
    perPage: 6,
    height: 520,
    Width: 900,
    maximumAllowed: 8,
    initComponent: function() {
        var pagesArray = new Array();
        this.ajaxRequestURL = this.originPath + ".json";
        var fields = [{
            name: 'mainImage',
            mapping: 'mainImage'
        }, {
            name: 'path',
            mapping: 'path'
        }, {
            name: 'title',
            mapping: 'title'
        }, {
            name: 'description',
            mapping: 'description'
        }, {
            name: 'activateAd',
            mapping: 'activateAd'
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
        function renderActivateAd(val) {
            var value = (val != "") ? "Yes" : "No";
            return '<div style="white-space:normal !important;"><p>' + value + '</p></div>';
        }
        var cols = [{
            id: 'mainImage',
            header: "Image",
            width: "70px",
            sortable: false,
            dataIndex: 'mainImage',
            renderer: renderImage
        }, {
            id: 'path',
            name: 'path',
            width: "100px",
            header: "Link",
            mapping: 'path',
            renderer: columnWrap
        }, {
            id: 'title',
            name: 'title',
            width: "100px",
            header: "title",
            mapping: 'title',
            renderer: columnWrap
        }, {
            id: 'description',
            name: 'description',
            width: "100px",
            header: "Description",
            mapping: 'description',
            renderer: columnWrap
        }, {
            id: 'activateAd',
            name: 'activateAd',
            width: "100px",
            header: "Activate Promo",
            mapping: 'activateAd',
            renderer: columnWrap
        }];
        var d = this;
        // The grid store
        var PromosectionGridStore = new CQ.Ext.data.Store({
            "id": "PromosectionGridStore" + d.id,
            "proxy": new CQ.Ext.data.HttpProxy({
                "url": this.ajaxRequestURL,
                "method": "GET"
            }),
            "reader": new CQ.PromosectionJsonReader({
                "id": "itemId",
                "root": "hits",
                "fields": fields
            }),
            "xtype": "store"
        });
        // Left hand form panel fields
        // 1. Main image
        var mainImage = {
            name: 'mainImage',
            ddGroups: "media",
            text: "Image*",
            xtype: 'html5smartimage',
            fileNameParameter: "./fileName",
            allowUpload: false,
            width: 180,
            height: 150,
            border: true,
            fieldDescription: 'Drop an image or click to upload.',
            disableZoom: 'true',
            id: 'mainImageArea' + d.id,
            name: './file',
            uploadUrl: '/tmp/upload_test/*',
            listeners: {
                imagestate: RWD.CQ.util.enableImageToolbar
            }
        };
        // 2.  Path
        var pathField = {
            fieldLabel: 'Link*',
            name: 'path',
            fieldDescription: 'Please choose or enter any URL for promotion.',
            width: 180,
            id: 'pathId' + d.id,
            xtype: 'pathfield',
            rootPath: '/content'
        };
        // 3.title
        var titleField = {
            fieldLabel: 'Product Title',
            fieldDescription: 'Please provide the product title.',
            name: 'title',
            width: 180,
            id: 'titleId' + d.id,
            xtype: 'textfield'
        };
        // 4.Description
        var descriptionField = {
            fieldLabel: ' Product Description',
            fieldDescription: 'Please provide the product description.',
            name: 'description',
            width: 180,
            id: 'descriptionId' + d.id,
            xtype: 'textarea'
        };
        //5. Activate Ad
        var activateAdField = {
            fieldLabel: 'Activate Promo',
            fieldDescription: 'Please select to make the promotion as active. <br><br><br><br><b> Note:</b> Maximum of 8 promotions can be added. <br>You may choose one or more than one promotions to be active. <br>At a given time only 2 promotions will be visible to the user. <br>If more than 2 promotions are made as <b>Active </b><br>then, also only 2 promotions will be visible to the user. These two promotions will be randomly selected and displayed. ',
            name: 'activateAd',
            id: 'activateAdId' + d.id,
            xtype: 'selection',
            type: 'checkbox',
            listeners: {
                'render': function() {
                    this.setValue(false);
                }
            }
        };
        // The submit button for the form panel
        var submitButton = {
            name: 'button',
            width: 80,
            text: 'Add to List',
            style: {
                marginLeft: '20px'
            },
            xtype: 'button',
            handler: function() {
                if (CQ.Ext.ComponentMgr.get('mainImageArea' + d.id).fileReferenceField.getValue() != "" && CQ.Ext.ComponentMgr.get('pathId' + d.id).getValue() != "") {
                    var link = CQ.Ext.ComponentMgr.get('pathId' + d.id).getValue();
                    var descriptionText = CQ.Ext.ComponentMgr.get('descriptionId' + d.id).getValue();
                    var pattern = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;
                    if (link != '' && !/\/content\/.*/.test(link) && !pattern.test(link)) {
                        CQ.Ext.Msg.alert(CQ.I18n.getMessage("Warning"), CQ.I18n.getMessage("Link start with '/content' or 'http://w' or 'https://w'"));
                        return false;
                    }
                    if ((self.maximumAllowed) && (CQ.Ext.ComponentMgr.get('promosectionGrid' + d.id).store.getCount() >= self.maximumAllowed)) {
                        CQ.Ext.MessageBox.alert(CQ.I18n.getMessage("Warning"), CQ.I18n.getMessage("Maximum " + self.maximumAllowed + " are allowed! Try to delete a few then, try again."));
                        return false;
                    }
                    var myNewRecord = CQ.Ext.data.Record.create({
                        id: 'mainImage',
                        header: "Image",
                        sortable: true,
                        dataIndex: 'mainImage',
                        renderer: renderImage
                    }, {
                        id: 'path',
                        name: 'path',
                        mapping: 'path'
                    }, {
                        id: 'title',
                        name: 'title',
                        mapping: 'title'
                    }, {
                        id: 'description',
                        name: 'description',
                        mapping: 'description'
                    }, {
                        id: 'activateAd',
                        name: 'activateAd',
                        mapping: 'activateAd'
                    });
                    var myRecord;
                    myRecord = new myNewRecord({
                        mainImage: CQ.Ext.ComponentMgr.get('mainImageArea' + d.id).fileReferenceField.getValue(),
                        path: CQ.Ext.ComponentMgr.get('pathId' + d.id).getValue(),
                        title: CQ.Ext.ComponentMgr.get('titleId' + d.id).getValue(),
                        description: CQ.Ext.ComponentMgr.get('descriptionId' + d.id).getValue(),
                        activateAd: CQ.Ext.ComponentMgr.get('activateAdId' + d.id).getValue()
                    });
                    // Add record to the grid store
                    CQ.Ext.ComponentMgr.get('promosectionGrid' + d.id).store.add(myRecord);
                    // Clear the form panel fields after form
                    // submit
                    CQ.Ext.ComponentMgr.get('mainImageArea' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('pathId' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('titleId' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('descriptionId' + d.id).reset();
                    CQ.Ext.ComponentMgr.get('activateAdId' + d.id).reset();
                } else {
                    CQ.Ext.Msg.alert("Error", "Provide Mandatory details");
                }
            }
        };
        var submitButtonPanel = {
            height: 50,
            border: false,
            autoWidth: true,
            xtype: 'panel',
            layout: {
                type: 'hbox',
                padding: '10 0 0 140'
            },
            items: [submitButton]
        };
        //1.Main Image
        var mainImagePanel = {
            height: 200,
            border: false,
            //autoWidth : true,
            xtype: 'panel',
            layout: {
                type: 'hbox',
                align: 'middle'
            },
            items: [{
                text: 'Image*',
                width: 130,
                xtype: 'label'
            }, mainImage]
        };
        // 2.Path
        var pathPanel = {
            height: 50,
            border: false,
            autoWidth: true,
            xtype: 'panel',
            layout: {
                type: 'form'
            },
            items: [pathField]
        };
        //3.title
        var titlePanel = {
            height: 50,
            border: false,
            autoWidth: true,
            xtype: 'panel',
            layout: {
                type: 'form'
            },
            items: [titleField]
        };
        //4.description
        var descriptionPanel = {
            height: 100,
            border: false,
            autoWidth: true,
            xtype: 'panel',
            layout: {
                type: 'form'
            },
            items: [descriptionField]
        };
        var activateAdPanel = {
            border: false,
            autoWidth: true,
            xtype: 'panel',
            style: {
                margin: '5,5,45,50'
            },
            layout: {
                type: 'form'
            },
            items: [activateAdField]
        };
        var AdPanel = {
            border: false,
            xtype: 'panel',
            columnWidth: 0.45,
            layout: {
                type: 'form'
            },
            style: {
                padding: '10px'
            },
            items: [mainImagePanel, pathPanel, titlePanel, descriptionPanel,
                activateAdPanel, submitButtonPanel
            ]
        };
        // create the destination Grid
        var promosectionGrid = {
            id: "promosectionGrid" + d.id,
            store: PromosectionGridStore,
            ddGroup: 'promosectionGridDDGroup',
            enableDragDrop: true,
            columns: cols,
            height: 350,
            enableDragDrop: true,
            columnWidth: 0.55,
            stripeRows: true,
            xtype: "grid",
            listeners: {
                afterrender: function() {
                    // This will make sure we only drop to the view scroller element
                    var promoSectionHandle = CQ.Ext.ComponentMgr.get('promosectionGrid' + d.id);
                    var promosectionGridDropTargetEl = promoSectionHandle.getView().scroller.dom;
                    var promosectionGridDropTarget = new CQ.Ext.dd.DropTarget(promosectionGridDropTargetEl, {
                        ddGroup: 'promosectionGridDDGroup',
                        notifyDrop: function(ddSource, e, data) {
                            if ((self.maximumAllowed) && (promoSectionHandle.store.getCount() >= self.maximumAllowed)) {
                                CQ.Ext.MessageBox.alert(CQ.I18n.getMessage("Warning"), CQ.I18n.getMessage("Maximum " + self.maximumAllowed + " are allowed! Try to delete a few then, try again."));
                                return false;
                            }
                            var targetRowIndex = promoSectionHandle.getView().findRowIndex(e.getTarget());
                            var records = ddSource.dragData.selections;
                            CQ.Ext.each(records, ddSource.grid.store.remove, ddSource.grid.store);
                            if (targetRowIndex !== false) {
                                promoSectionHandle.store.insert(targetRowIndex, records);
                            } else {
                                promoSectionHandle.store.add(records);
                            }
                            CQ.Ext.select('.x-grid3-body').setStyle('margin-bottom', '50px');
                            return true;
                        }
                    });
                },
                rowcontextmenu: function(grid, rowIndex, e) {
                    e.stopEvent();
                    menu = new CQ.Ext.menu.Menu({
                        items: [{
                            text: 'Update',
                            iconCls: 'icon-update',
                            handler: function() {
                                var rowHandle = grid.getStore().getAt(rowIndex);
                                // Activate Ad
                                CQ.Ext.ComponentMgr.get('activateAdId' + d.id).setValue(rowHandle.get('activateAd'));
                                //description
                                CQ.Ext.ComponentMgr.get('descriptionId' + d.id).setValue(rowHandle.get('description'));
                                //title
                                CQ.Ext.ComponentMgr.get('titleId' + d.id).setValue(rowHandle.get('title'));
                                //Path
                                CQ.Ext.ComponentMgr.get('pathId' + d.id).setValue(rowHandle.get('path'));
                                //main image
                                var dragMainImageData = {
                                    records: [{
                                        pathMain: rowHandle.get('mainImage'),
                                        get: function() {
                                            return this.pathMain;
                                        }
                                    }],
                                    single: "true"
                                };
                                CQ.Ext.ComponentMgr.get('mainImageArea' + d.id).handleDrop(dragMainImageData);
                                grid.getStore().removeAt(rowIndex);
                            }
                        }, {
                            text: 'Delete',
                            iconCls: 'icon-delete',
                            handler: function() {
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
            layout: 'column',
            items: [AdPanel, promosectionGrid]
        });
        CQ.Ext.apply(this, CQ.Ext.apply(this.initialConfig, config));
        CQ.PromosectionPanel.superclass.initComponent.call(this, arguments);
    },
    onRender: function() {
        CQ.PromosectionPanel.superclass.onRender.apply(this, arguments);
        var parentDialog = this.findParentByType("dialog");
        var panelFields = ["mainImageArea", "pathId", "titleId", "descriptionId", "activateAdId"];
        var me = this;
        if (parentDialog) {
            // Load content on page load
            parentDialog.on("loadContent", function(obj) {
                // Load the right grid
                me.originpath = parentDialog.path;
                me.ajaxRequestURL = parentDialog.path + "/promosection.json";
                CQ.Ext.ComponentMgr.get('promosectionGrid' + me.id).store.proxy.setUrl(me.ajaxRequestURL);
                CQ.Ext.ComponentMgr.get('promosectionGrid' + me.id).store.load({});
                CQ.Ext.each(panelFields, function(panelField, index) {
                    CQ.Ext.ComponentMgr.get(panelField + me.id).reset();
                });
            }, parentDialog);
            // Before submit set the form values in hidden
            // fields.
            parentDialog.on("beforesubmit", function() {
                var dis = CQ.Ext.ComponentMgr.get(me.id);
                var storeHandle = CQ.Ext.ComponentMgr.get('promosectionGrid' + me.id).getStore();
                var pageform = dis.findParentByType("form");
                var fieldNames = ["mainImage", "path", "title", "description", "activateAd"];
                //Check for unsaved values
                for (var i = 0; i < panelFields.length; i++) {
                    try {
                        var value = '';
                        var field = CQ.Ext.ComponentMgr.get(panelFields[i] + me.id);
                        if (field.getXType().indexOf('image') > -1) {
                            value = field.fileReferenceField.getValue().trim();
                        } else {
                            value = field.getValue().trim();
                        }
                        if (value) {
                            CQ.Ext.Msg.show({
                                title: 'Warning',
                                msg: 'There are unsaved changes in the dialog!<br><br>Please save all the updates in <b>' + me.title + '</b> before continuing.',
                                icon: CQ.Ext.MessageBox.WARNING,
                                buttons: CQ.Ext.Msg.OK
                            });
                            return false;
                        }
                    } catch (e) { /* do nothing */ }
                }
                CQ.Ext.each(fieldNames, function(fieldName, index) {
                    var oldItems = pageform.find("name", './promosection/' + fieldName);
                    CQ.Ext.each(oldItems, function(item) {
                        pageform.remove(item, true);
                    });
                });
                CQ.Ext.each(fieldNames, function(fieldName, index) {
                    pageform.add(new CQ.Ext.form.Hidden({
                        "name": './promosection/' + fieldName + '@TypeHint',
                        "value": 'String[]'
                    }));
                });
                if (storeHandle.getCount() <= 0) {
                    pageform.add(new CQ.Ext.form.Hidden({
                        "name": './promosection@Delete'
                    }));
                }
                CQ.Ext.each(fieldNames, function(fieldName, index) {
                    for (var i = 0; i < CQ.Ext.ComponentMgr.get('promosectionGrid' + me.id).getStore().getCount(); i++) {
                        var fieldValue = CQ.Ext.ComponentMgr.get('promosectionGrid' + me.id).getStore().getAt(i)["data"][fieldName];
                        if (fieldValue == null || fieldValue == "") fieldValue = " ";
                        var fieldVar = new CQ.Ext.form.Hidden({
                            "name": './promosection/' + fieldName,
                            "value": fieldValue
                        });
                        pageform.add(fieldVar);
                    }
                });
                pageform.doLayout();
            }, parentDialog);
        }
    }
});
CQ.Ext.reg("promosection", CQ.PromosectionPanel);