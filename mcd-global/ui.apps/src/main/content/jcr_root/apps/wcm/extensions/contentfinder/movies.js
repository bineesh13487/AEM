{
    "tabTip": CQ.I18n.getMessage("Movies"),
    "id": "cfTab-Movies",
    "xtype": "contentfindertab",
        "iconCls": "cq-cft-tab-icon movies",
    "ranking": 25,
    "allowedPaths": [
        "/content/*",
        "/etc/scaffolding/*",
        "/etc/commerce/*",
        "/etc/workflow/packages/*",
        "/etc/designs/mcd/*"
    ],
    "items": [
        CQ.wcm.ContentFinderTab.getQueryBoxConfig({
            "id": "cfTab-Movies-QueryBox",
            "items": [
                CQ.wcm.ContentFinderTab.getSuggestFieldConfig({"url": "/bin/wcm/contentfinder/suggestions.json/content/dam"})
            ]
        }),
        CQ.wcm.ContentFinderTab.getResultsBoxConfig({
            "itemsDDGroups": [CQ.wcm.EditBase.DD_GROUP_ASSET],
            /* TODO: make multiple per-mimeType configs possible, see bug 31703 */
            "itemsDDNewParagraph": {
                "path": "foundation/components/video",
                "propertyName": "./asset"
            },
/*
            "itemsDDNewParagraph": {
                "path": "foundation/components/flash",
                "propertyName": "./fileReference"
            },
*/
            "tbar": [
                CQ.wcm.ContentFinderTab.REFRESH_BUTTON,
                "->",
                {
                    "toggleGroup": "cfTab-Movies-TG",
                    "enableToggle": true,
                    "toggleHandler": function(button, pressed) {
                        var tab = CQ.Ext.getCmp("cfTab-Movies");
                        if (pressed) {
                            tab.dataView.tpl = new CQ.Ext.XTemplate(CQ.wcm.ContentFinderTab.THUMBS_TEMPLATE);
                            tab.dataView.itemSelector = CQ.wcm.ContentFinderTab.THUMBS_ITEMSELECTOR;
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
                    "toggleGroup": "cfTab-Movies-TG",
                    "enableToggle": true,
                    "toggleHandler": function(button, pressed) {
                        var tab = CQ.Ext.getCmp("cfTab-Movies");
                        if (pressed) {
                            tab.dataView.tpl = new CQ.Ext.XTemplate(CQ.wcm.ContentFinderTab.DETAILS_TEMPLATE);
                            tab.dataView.itemSelector = CQ.wcm.ContentFinderTab.DETAILS_ITEMSELECTOR;
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
        },{
            "url": "/bin/wcm/contentfinder/asset/view.json/content/dam"
        }, {
            "baseParams": {
                "mimeType": "video,application/x-shockwave-flash"
            }
        })
    ]
}