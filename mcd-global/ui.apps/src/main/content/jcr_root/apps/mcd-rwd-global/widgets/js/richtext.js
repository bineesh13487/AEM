/**
 * @class McdRich.RichFieldConfig
 * @extends CQ.form.CompositeField
 * The accordion field lets the user to enter a question, answer and select a folder path from DAM.
 * @constructor
 * Creates a new RichFieldConfigField.
 * @param {Object} config The config object
 */ 

McdRich = {}; //creating namespace

McdRich.RichFieldConfig = CQ.Ext.extend(CQ.form.CompositeField, {

    /**
     * @private
     * @type CQ.Ext.form.TextField
     */
    hiddenField: null,

    /**
     * @private
     * @type CQ.Ext.form.TextField
     */
    quesField: null,

    /**
     * @private
     * @type CQ.Ext.form.Label
     */
     /*
    cssStyles: null,
    rtePlugins: null,
    linkbrowseConfig: null,
    */
    constructor: function(config) {
       config = config || { };

       var defaults = {
            "border": false,
            "stateful": false,
            "style": "padding:5px 0 0 0;"
        };
        config = CQ.Util.applyDefaults(config, defaults);
        McdRich.RichFieldConfig.superclass.constructor.call(this, config);
    },

    // overriding CQ.Ext.Component#initComponent
    initComponent: function() {
      McdRich.RichFieldConfig.superclass.initComponent.call(this);
        var pageHandle = location.toString();
        pageHandle = pageHandle.replace(".html",".getStyleSheet.html");
        //alert(pageHandle);
        var data = CQ.Util.eval(CQ.HTTP.get(pageHandle));
        //alert("data--"+CQ.Util.eval(pageHandle));
        //alert("tet here=" + data[0].designPath);


        //Update it
        var extCSS = {"sheet1": "/etc/clientlibs/mcd-rwd-global/css/rtStyles.css"};

        if (data && data.styles) {
			var extCSS = {"sheet1": data.styles};
        }

        this.richText = new CQ.form.RichText ({
           "cssStyles":this.cssStyles,
           "externalStyleSheets": extCSS,
           "rtePlugins": this.rtePlugins,
           "linkbrowseConfig": this.linkbrowseConfig,
           "name": this.name,
           "hideLabel": true,
           "stateful": false,
            "height": "200px",
            "listeners": {
                "afterrender": function(comp){
                    comp.setWidth("98%");
                },
                "editmodechange": function(comp, sourceEdit) {
                    if(sourceEdit) {
                        comp.el.dom.style.height = "153px";
                        comp.el.dom.style.width = "100%";
                    }
                }
            }
        });
        this.add(this.richText);
    },

    // overriding CQ.form.CompositeField#setValue
    setValue: function(value) {
        if(value) {
            this.richText.setValue(value);
        }
    },

    // overriding CQ.form.CompositeField#getValue
    getValue: function() {
        return this.getRawValue();
    },

    // overriding CQ.form.CompositeField#getRawValue
    getRawValue: function() {
        if (!this.richText) {
            return null;
        }
        var value = this.richText.getValue() || "";
        return value;
    }
    
});

// register xtype
CQ.Ext.reg('richTextField', McdRich.RichFieldConfig);