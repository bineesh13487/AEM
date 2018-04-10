CQ.customwidgetJsonReader = function(meta, recordType) {
    CQ.customwidgetJsonReader.superclass.constructor.call(this, meta, recordType || meta.fields);
};
CQ.Ext.extend(CQ.customwidgetJsonReader, CQ.Ext.data.JsonReader, {
    read: function(response) {
        var json = response.responseText;
        var o = CQ.Ext.decode(json);
        var output = {
            hits: new Array()
        };

        var keys = [];
        for (var key in o) {
        	if( typeof o[key] === 'object' ) {
        		if (key !="jcr:mixinTypes") {
        			keys.push(key);
        		}

            }
        }

        var hitsobj = [];
        var hits1 ={};

        if (o[keys[0]]) {
            for ( var i = 0; i < o[keys[0]].length; i++) {
                for ( var j = 0; j < keys.length; j++) {
                    hits1[keys[j]] = o[keys[j]][i];
                }
                output.hits.push(JSON.parse(JSON.stringify(hits1)));
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
* Custom panel for the customwidget item editing CQ.customwidgetPanel CQ.customwidgetPanel
* extends CQ.Ext.Panel
*/
CQ.customwidgetPanel = CQ.Ext.extend(CQ.Ext.Panel, {
    originPath: CQ.WCM.getPagePath(),
    ajaxRequestURL: null,
    ajaxmethod: null,
    perPage: 6,
    height: 520,
    listOfObjects : [],
    listOfIds : [],
    generalListOfIds : [],
    Width: 900,
    maximumAllowed: 8,
    initComponent: function() {
        var pagesArray = new Array();
        this.ajaxRequestURL = this.originPath + ".json";
        var originalObject = this;
        var customkey = "custom-key";

        function myFunction(obj) {
            originalObject.listOfObjects = [];
            for(var i in obj){
                if (i.indexOf(customkey) >-1){
                    var value = obj[i];
                    var newJson = value.replace(/([a-zA-Z0-9]+?):/g, '"$1":');
                    newJson = newJson.replace(/'/g, '"');
                    originalObject.listOfObjects.push(JSON.parse("["+newJson+"]"));
                }
            }
        }

        function searchObj( obj ){
            for( var key in obj ) {
                if( typeof obj[key] === 'object' ){
                    searchObj( obj[key] );
                }
                if( key == 'id' ){
                    originalObject.generalListOfIds.push(obj[key]);
                }
            }
            return obj;
        }
        	
        function searchObj2( obj ){
            for( var key in obj ) {
                if( typeof obj[key] === 'object' ){
                    searchObj2( obj[key] );
                }
                if( key == 'id' ){
                    originalObject.listOfIds.push(obj[key]);
                }
            }
            return obj;
        }

        function searchObj1( obj ){
            for( var key in obj ) {
                if( typeof obj[key] === 'object' ){
                    searchObj1( obj[key] );
                }
                if( key == 'id' ){
                    obj[key] = obj[key]+originalObject.id;
                }
            }
            return obj;
        }

        function myFunction1(a,obj) {
            for(var i in obj){
                if (i==a){
                    var value = obj[i];
                    var newJson = value.replace(/([a-zA-Z0-9]+?):/g, '"$1":');
                    newJson = newJson.replace(/'/g, '"');
                    return newJson;
                }
            }
        }

        var fields =  JSON.parse("["+myFunction1("left",originalObject)+"]");

        var cols = JSON.parse("["+myFunction1("cols",originalObject)+"]");

        myFunction(originalObject);
        originalObject.generalListOfIds = [];
        searchObj(originalObject.listOfObjects);

        var fields1 =  searchObj1(originalObject.listOfObjects);
        originalObject.listOfIds = [];
        searchObj2(originalObject.listOfObjects);
        var self = this;
         
        function renderImage(val) {
            return '<img src="' + val + '" style="width:50px;height:50px"> ';
        }

        function columnWrap(val) {
            return '<div style="white-space:normal !important;"><p>' + val + '</p></div>';
        }

        function renderActivateAd(val) {
            var value = (val != "") ? "Yes" : "No";
            return '<div style="white-space:normal !important;"><p>' + value + '</p></div>';
        }

        var d = this;
        // The grid store
        var customGridStore = new CQ.Ext.data.Store({
            "id": "customGridStore" + d.id,
            "proxy": new CQ.Ext.data.HttpProxy({
                "url": this.ajaxRequestURL,
                "method": "GET"
            }),
            "reader": new CQ.customwidgetJsonReader({
                "id": "itemId",
                "root": "hits",
                "fields": fields
            }),
            "xtype": "store"
        });
        // Left hand form panel fields

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
                var myNewRecord = CQ.Ext.data.Record.create(myFunction1("store",originalObject));
                var myRecord;
                var arrayOfObjects = {};
                for(var i=0; i<self.listOfIds.length; i++){
                    if(self.listOfIds[i].toLowerCase().indexOf("image") >-1){
                        arrayOfObjects[self.generalListOfIds[i]] = CQ.Ext.ComponentMgr.get(self.listOfIds[i]).fileReferenceField.getValue();
                    }
                    else {
                        arrayOfObjects[self.generalListOfIds[i]] = CQ.Ext.ComponentMgr.get(self.listOfIds[i]).getValue();
                    }
                }
                myRecord = new myNewRecord(arrayOfObjects);
                // Add record to the grid store
                CQ.Ext.ComponentMgr.get('customGrid' + d.id).store.add(myRecord);
                // Clear the form panel fields after form
                // submit

                for(var i=0; i<self.listOfIds.length; i++){
                      CQ.Ext.ComponentMgr.get(self.listOfIds[i]).reset();
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
            items: [self.listOfObjects, submitButtonPanel]
        };

        // create the destination Grid
        var customGrid = {
            id: "customGrid" + d.id,
            store: customGridStore,
            ddGroup: 'customGridDDGroup',
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
                    var customwidgetHandle = CQ.Ext.ComponentMgr.get('customGrid' + d.id);
                    var customGridDropTargetEl = customwidgetHandle.getView().scroller.dom;
                    var customGridDropTarget = new CQ.Ext.dd.DropTarget(customGridDropTargetEl, {
                        ddGroup: 'customGridDDGroup',
                        notifyDrop: function(ddSource, e, data) {
                            if ((self.maximumAllowed) && (customwidgetHandle.store.getCount() >= self.maximumAllowed)) {
                                //CQ.Ext.MessageBox.alert(CQ.I18n.getMessage("Warning"), CQ.I18n.getMessage("Maximum " + self.maximumAllowed + " are allowed! Try to delete a few then, try again."));
                                //return false;
                            }

                            var targetRowIndex = customwidgetHandle.getView().findRowIndex(e.getTarget());
                            var records = ddSource.dragData.selections;
                            CQ.Ext.each(records, ddSource.grid.store.remove, ddSource.grid.store);
                            if (targetRowIndex !== false) {
                                customwidgetHandle.store.insert(targetRowIndex, records);
                            } else {
                                customwidgetHandle.store.add(records);
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
                                function imgHelper(id) {
                                    var dragMainImageData = {
                                        records: [{
                                            pathMain: rowHandle.get(id),
                                            get: function() {
                                                return this.pathMain;
                                            }
                                        }],
                                        single: "true"
                                    };
                                    return dragMainImageData;
                                }

                                for(var i=0; i<self.listOfIds.length; i++){
                                    if(self.listOfIds[i].toLowerCase().indexOf("image") >-1){
                                        CQ.Ext.ComponentMgr.get(self.listOfIds[i]).handleDrop(imgHelper(self.generalListOfIds[i]));
                                    } else {
                                        var referencedComp = CQ.Ext.ComponentMgr.get(self.listOfIds[i]);
                                        if (referencedComp && referencedComp.xtype === 'tags') {
                                            var tagValues = rowHandle.get(self.generalListOfIds[i]);
                                            referencedComp.setValue(CQ.Ext.isArray(tagValues) ? tagValues : tagValues.split(','));
                                        } else {
                                            referencedComp.setValue(rowHandle.get(self.generalListOfIds[i]));
                                        }
                                    }
                                }

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
            items: [AdPanel, customGrid]
        });
        CQ.Ext.apply(this, CQ.Ext.apply(this.initialConfig, config));
        CQ.customwidgetPanel.superclass.initComponent.call(this, arguments);
    },
    onRender: function() {
        CQ.customwidgetPanel.superclass.onRender.apply(this, arguments);
        var parentDialog = this.findParentByType("dialog");
        var panelFields = this.listOfIds;
        var me = this;
        if (parentDialog) {
            // Load content on page load
            parentDialog.on("loadContent", function(obj) {
                // Load the right grid
                me.originpath = parentDialog.path;
                me.ajaxRequestURL = parentDialog.path + "/customwidget.json";
                CQ.Ext.ComponentMgr.get('customGrid' + me.id).store.proxy.setUrl(me.ajaxRequestURL);
                CQ.Ext.ComponentMgr.get('customGrid' + me.id).store.load({});
                CQ.Ext.each(panelFields, function(panelField, index) {
                    CQ.Ext.ComponentMgr.get(panelField).reset();
                });
            }, parentDialog);
            // Before submit set the form values in hidden
            // fields.
            parentDialog.on("beforesubmit", function() {
                var dis = CQ.Ext.ComponentMgr.get(me.id);
                var storeHandle = CQ.Ext.ComponentMgr.get('customGrid' + me.id).getStore();
                var pageform = dis.findParentByType("form");
                var fieldNames = me.generalListOfIds;
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
                    var oldItems = pageform.find("name", './customwidget/' + fieldName);
                    CQ.Ext.each(oldItems, function(item) {
                        pageform.remove(item, true);
                    });
                });

                CQ.Ext.each(fieldNames, function(fieldName, index) {
                    pageform.add(new CQ.Ext.form.Hidden({
                        "name": './customwidget/' + fieldName + '@TypeHint',
                        "value": 'String[]'
                    }));
                });

                if (storeHandle.getCount() <= 0) {
                    pageform.add(new CQ.Ext.form.Hidden({
                        "name": './customwidget@Delete'
                    }));
                }

                CQ.Ext.each(fieldNames, function(fieldName, index) {
                    for (var i = 0; i < CQ.Ext.ComponentMgr.get('customGrid' + me.id).getStore().getCount(); i++) {
                        var fieldValue = CQ.Ext.ComponentMgr.get('customGrid' + me.id).getStore().getAt(i)["data"][fieldName];
                        if (fieldValue == null || fieldValue == "") fieldValue = " ";
                        var fieldVar = new CQ.Ext.form.Hidden({
                            "name": './customwidget/' + fieldName,
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
CQ.Ext.reg("customwidget", CQ.customwidgetPanel);