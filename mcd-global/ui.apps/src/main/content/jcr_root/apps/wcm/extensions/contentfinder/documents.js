{
    "tabTip": CQ.I18n.getMessage("Documents"),
    "id": "cfTab-Documents",
    "xtype": "contentfindertab",
    "iconCls": "cq-cft-tab-icon documents",
    "ranking": 20,
    "allowedPaths": [
        "/content/*",
        "/etc/scaffolding/*",
        "/etc/commerce/*",
        "/etc/workflow/packages/*",
        "/etc/designs/mcd/*"
    ],
    "items": [
        CQ.wcm.ContentFinderTab.getQueryBoxConfig({
            "id": "cfTab-Documents-QueryBox",
            "items": [
                CQ.wcm.ContentFinderTab.getSuggestFieldConfig({"url": "/bin/wcm/contentfinder/suggestions.json/content/dam"})
            ]
        }),
        CQ.wcm.ContentFinderTab.getResultsBoxConfig({
            "itemsDDGroups": [CQ.wcm.EditBase.DD_GROUP_ASSET],
            "itemsDDNewParagraph": {
                "path": "foundation/components/download",
                "propertyName": "./fileReference"
            },
            "items": {
                "tpl": CQ.wcm.ContentFinderTab.THUMBS_SHADOW_TEMPLATE,
                "itemSelector": CQ.wcm.ContentFinderTab.THUMBS_SHADOW_ITEMSELECTOR
            },
            "tbar": [
                CQ.wcm.ContentFinderTab.REFRESH_BUTTON,
                "->",
                {
                    "toggleGroup": "cfTab-Documents-TG",
                    "enableToggle": true,
                    "toggleHandler": function(button, pressed) {
                        var tab = CQ.Ext.getCmp("cfTab-Documents");
                        if (pressed) {
                            tab.dataView.tpl = new CQ.Ext.XTemplate(CQ.wcm.ContentFinderTab.THUMBS_SHADOW_TEMPLATE);
                            tab.dataView.itemSelector = CQ.wcm.ContentFinderTab.THUMBS_SHADOW_ITEMSELECTOR;
                        }
                        if (tab.dataView.store != null) {
                            tab.dataView.refresh();
                        }
                    },
                    "pressed": true,
                    "allowDepress": false,
                    "cls": "cq-btn-thumbs cq-cft-dataview-btn",
                    "iconCls":"cq-cft-dataview-mosaic",
                    "tooltip": {
                        "text": CQ.I18n.getMessage("Mosaic View"),
                        "autoHide":true
                    }
                },
                {
                    "toggleGroup": "cfTab-Documents-TG",
                    "enableToggle": true,
                    "toggleHandler": function(button, pressed) {
                        var tab = CQ.Ext.getCmp("cfTab-Documents");
                        if (pressed) {
                            tab.dataView.tpl = new CQ.Ext.XTemplate(CQ.wcm.ContentFinderTab.DETAILS_SHADOW_TEMPLATE);
                            tab.dataView.itemSelector = CQ.wcm.ContentFinderTab.DETAILS_SHADOW_ITEMSELECTOR;
                        }
                        if (tab.dataView.store != null) {
                            tab.dataView.refresh();
                        }
                    },
                    "pressed": false,
                    "allowDepress": false,
                    "cls": "cq-btn-details",
                    "cls": "cq-btn-details cq-cft-dataview-btn",
                    "iconCls":"cq-cft-dataview-list",
                    "tooltip": {
                        "text": CQ.I18n.getMessage("List View"),
                        "autoHide": true
                    }
                }
            ]
        }, {
            "url": "/bin/wcm/contentfinder/asset/view.json/content"
        }, {
            "baseParams": {
                "mimeType": "application/vnd.openxmlformats,application/msword,application/vnd.ms-powerpoint,application/mspowerpoint,application/powerpoint,application/x-mspowerpoint,application/x-msexcel,application/x-excel,application/excel,application/vnd.ms-excel,application/pdf,application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            }
        })
    ]
}