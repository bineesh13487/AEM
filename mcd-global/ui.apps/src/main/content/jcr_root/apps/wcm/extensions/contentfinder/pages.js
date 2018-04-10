{
    "tabTip": CQ.I18n.getMessage("Pages"),
    "id": "cfTab-Pages",
    "iconCls": "cq-cft-tab-icon pages",
    "xtype": "contentfindertab",
    "ranking": 30,
    "allowedPaths": [
        "/content/*",
        "/etc/scaffolding/*",
        "/etc/commerce/*",
        "/etc/workflow/packages/*",
        "/etc/designs/mcd/*"
    ],
    "items": [
        CQ.wcm.ContentFinderTab.getQueryBoxConfig({
            "id": "cfTab-Pages-QueryBox",
            "items": [
                CQ.wcm.ContentFinderTab.getSuggestFieldConfig({"url": "/bin/wcm/contentfinder/suggestions.json/content"})
            ]
        }),
        CQ.wcm.ContentFinderTab.getResultsBoxConfig({
            "itemsDDGroups": [CQ.wcm.EditBase.DD_GROUP_PAGE],
            "items": {
                "tpl":
                    '<tpl for=".">' +
                            '<div class="cq-cft-search-item" title="{pathEncoded}" ondblclick="CQ.wcm.ContentFinder.loadContentWindow(\'{[CQ.HTTP.encodePath(values.path)]}.html\');">' +
                                    '<div class="cq-cft-search-thumb-top" style="background-image:url(\'{[CQ.wcm.ContentFinderTab.THUMBS_URL(values, 48, 48)]}\');"></div>' +
                                         '<div class="cq-cft-search-text-wrapper">' +
                                            '<div class="cq-cft-search-title">{[CQ.shared.XSS.getXSSTablePropertyValue(values, \"title\")]}</div>' +
                                        '</div>' +
                                    '<div class="cq-cft-search-separator"></div>' +
                            '</div>' +
                    '</tpl>',
                "itemSelector": CQ.wcm.ContentFinderTab.DETAILS_ITEMSELECTOR
            },
            "tbar": [
                CQ.wcm.ContentFinderTab.REFRESH_BUTTON
            ]
        },{
            "url": "/bin/wcm/contentfinder/page/view.json/content"
        }, {
            "baseParams": {
                "type": "cq:Page"
            }
        })
    ]

}