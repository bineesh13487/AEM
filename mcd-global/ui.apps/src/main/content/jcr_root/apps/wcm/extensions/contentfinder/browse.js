{
    "id":"cfTab-Browse",
    "xtype":"contentfindertab",
    "tabTip":CQ.I18n.getMessage("Browse"),
    "iconCls":"cq-cft-tab-icon full",
    "ranking":50,
    "allowedPaths": [
        "/content/*",
        "/etc/scaffolding/*",
        "/etc/commerce/*",
        "/etc/workflow/packages/*",
        "/etc/designs/mcd/*"
    ],
    "items":[
        CQ.wcm.ContentFinderTab.getBrowseTree({
            "treeRoot":{
                "text": CQ.I18n.getMessage("Content")
            }
        }, "cfTab-Browse-Tree", "cfTab-Browse-resultBox"),
        CQ.wcm.ContentFinderTab.getResultsBoxConfig({
            "id":"cfTab-Browse-resultBox",
			"disableContinuousLoading":true,
            "tbar":[
                CQ.wcm.ContentFinderTab.REFRESH_BUTTON,
                "->",
                {
                    "toggleGroup":"cfTab-Browse-TG",
                    "enableToggle":true,
                    "toggleHandler":function(button, pressed) {
                        var tab = CQ.Ext.getCmp("cfTab-Browse");
                        if (pressed) {
                            tab.dataView.tpl = new CQ.Ext.XTemplate(CQ.wcm.ContentFinderTab.THUMBS_TEMPLATE);
                            tab.dataView.itemSelector = CQ.wcm.ContentFinderTab.THUMBS_ITEMSELECTOR;
                        }
                        if (tab.dataView.store != null) {
                            tab.dataView.refresh();
                        }
                    },
                    "pressed":true,
                    "allowDepress":false,
                    "cls": "cq-btn-thumbs cq-cft-dataview-btn",
                    "iconCls":"cq-cft-dataview-mosaic",
                    "tooltip":{
                        "text":CQ.I18n.getMessage("Mosaic View"),
                        "autoHide":true
                    }
                },{
                    "toggleGroup":"cfTab-Browse-TG",
                    "enableToggle":true,
                    "toggleHandler":function(button, pressed) {
                        var tab = CQ.Ext.getCmp("cfTab-Browse");
                        if (pressed) {
                            tab.dataView.tpl = new CQ.Ext.XTemplate(CQ.wcm.ContentFinderTab.DETAILS_TEMPLATE);
                            tab.dataView.itemSelector = CQ.wcm.ContentFinderTab.DETAILS_ITEMSELECTOR;
                        }
                        if (tab.dataView.store != null) {
                            tab.dataView.refresh();
                        }
                    },
                    "pressed":false,
                    "allowDepress":false,
                    "cls": "cq-btn-details cq-cft-dataview-btn",
                    "iconCls":"cq-cft-dataview-list",
                    "tooltip":{
                        "text":CQ.I18n.getMessage("List View"),
                        "autoHide":true
                    }
                }
            ]
        },{},{
            "autoLoad":false,
            "reader": new CQ.Ext.data.JsonReader({
                "totalProperty": "results",
                "root": "hits",
                "fields": [
                    "name", "path", "title", "mimeType", "isFolder", "contentType", "ddGroups", "ddNewParagraph", "size", "lastModified", "ck", "templateParams", "imageWidth", "imageHeight"
                ],
                "id": "path"
            })
        })
    ]
}
