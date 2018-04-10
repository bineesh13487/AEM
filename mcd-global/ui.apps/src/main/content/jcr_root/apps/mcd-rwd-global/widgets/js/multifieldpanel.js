/**
 * @class RWD.CQ.MultiFieldPanel
 * @extends CQ.form.Panel
 * <p>The MultiFieldPanel widget is a replacement for the normal multifield widget which
 * supports multiple structures in a single JCR property. It does this by storing a set of
 * key/value pairs serialized as a JSON object. The keys for each pair is defined by setting the
 * 'key' property on the field.</p>
 */
RWD.CQ.MultiFieldPanel = CQ.Ext.extend(CQ.Ext.Panel, {
    panelValue: '',

    /**
     * @constructor
     * Creates a new MultiFieldPanel.
     * @param {Object} config The config object
     */
    constructor: function(config){
        config = config || {};
        if (!config.layout) {
            config.layout = 'form';
            config.padding = '10px';
        }
        RWD.CQ.MultiFieldPanel.superclass.constructor.call(this, config);
    },

    initComponent: function() {
        RWD.CQ.MultiFieldPanel.superclass.initComponent.call(this);

        this.panelValue = new CQ.Ext.form.Hidden({
            name: this.name
        });

        this.add(this.panelValue);

        var dialog = this.findParentByType('dialog');

        if(dialog) {
            dialog.on('beforesubmit', function(){
                var value = this.getValue();

                if (value){
                    this.panelValue.setValue(value);
                }
            },this);
        }
    },

    afterRender : function(){
        RWD.CQ.MultiFieldPanel.superclass.afterRender.call(this);

        this.items.each(function(){
            if(!this.contentBasedOptionsURL
                || this.contentBasedOptionsURL.indexOf(CQ.form.Selection.PATH_PLACEHOLDER) < 0){
                return;
            }
			if(this.name!=undefined) {
             if(this.name.indexOf('restaurantFilters')>-1) {
			 console.log(this.name);
			 }
			 }
			 if(this.contentBasedOptionsURL.indexOf('restaurantUserAttributes')>-1) {
			  this.processPath(window.location.pathname.substring(0,window.location.pathname.indexOf('.html'))+"/jcr:content");
			 return ;
			 }
            this.processPath(this.findParentByType('dialog').path);
        });
    },

    getValue: function() {
        var pData = {};

        this.items.each(function(i){
            if(i.xtype === "label" || i.xtype === "hidden" || !i.hasOwnProperty("key")){
                return;
            }
            pData[i.key] = i.getValue();
        });

        return $.isEmptyObject(pData) ? "" : JSON.stringify(pData);
    },

    setValue: function(value) {
        this.panelValue.setValue(value);

        var pData = JSON.parse(value);

        this.items.each(function(i){
            if(i.xtype === "label" || i.xtype === "hidden" || !i.hasOwnProperty("key")){
                return;
            }

            i.setValue(pData[i.key]);
        });
    },

    validate: function(){
        return true;
    },

    getName: function(){
        return this.name;
    }
});

CQ.Ext.reg("multifieldpanel", RWD.CQ.MultiFieldPanel);	