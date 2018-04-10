/* 
 Author : 
 */

/**
 * @class CQ.wcm.FilterRuleField
 * @extends CQ.form.CompositeField
 * The filter rule field lets the user select "include" or "exclude" and enter a regexp
 * @constructor
 * Creates a new FilterRuleField.
 * @param {Object} config The config object
 */
 
ContactusFormLinksEntry = {}; 
 
ContactusFormLinksEntry.ContactusFormLinks = CQ.Ext.extend(CQ.form.CompositeField, {

    /**
     * @private
     * @type CQ.Ext.form.TextField
     */
    hiddenField: null, 

	/**
     * @private
     * @type CQ.Ext.form.Label
     */
    linkTextLabel: null,
    
    /**
     * @private
     * @type CQ.Ext.form.TextField
     */
    linkText: null,

    /**
     * @private
     * @type CQ.Ext.form.Label
     */
    linkURLLabel: null,

	/**
     * @private
     * @type CQ.form.PathField
     */
    linkURL: null,

    /**
     * @private
     * @type CQ.Ext.form.Checkbox
     */
    chkType: null,

	constructor: function(config) {
        config = config || { };
        var defaults = {
            "border": false,
            "stateful": false,
            "style": "padding:5px 0 0 5px;"
        };
        config = CQ.Util.applyDefaults(config, defaults);
        ContactusFormLinksEntry.ContactusFormLinks.superclass.constructor.call(this, config);
    },

    // overriding CQ.Ext.Component#initComponent
    initComponent: function() {
        ContactusFormLinksEntry.ContactusFormLinks.superclass.initComponent.call(this);
        this.hiddenField = new CQ.Ext.form.Hidden({
            name: this.name,
            "stateful": false
        });
        this.add(this.hiddenField);

		this.linkTextLabel = new CQ.Ext.form.Label({
            html: "Form Name",
            style:'width:90px;float:left;'
        });
        this.add(this.linkTextLabel);
        
        this.linkText = new CQ.Ext.form.TextField({ 
            width: '400px',
            "stateful": false,
            "border":true,
            "hideLabel":true,
            style:'float:left;'
        });
		this.add(this.linkText);

        this.linkURLLabel = new CQ.Ext.form.Label({
            html: "Form URL",
            style:'width:90px;float:left;'
        });
        this.add(this.linkURLLabel);
        
		this.linkURL = new CQ.form.PathField({
            style:'width:384px;', 
            width:'100%',
            "hideLabel":true
        });
        this.add(this.linkURL);

        this.chkType = new CQ.Ext.form.Checkbox({
            boxLabel: 'Open In New Window',
            style:'margin-bottom:5px;margin-left: -105px;',
            height:35
        });
          
        this.add(this.chkType);
    },
   
    // overriding CQ.form.CompositeField#setValue
    setValue: function(value) {
        if (!value) {
            return null;
        }
        
        var linkText = '';
        var linkURL = '';
        var newWindow = '';

        if (value) {
            var linksValue = value.split("^");
            if(linksValue.length>0) {
				linkText = linksValue[0];
                linkURL = linksValue[1];
                newWindow = linksValue[2];
            }
        }
		this.linkText.setValue(linkText);
        this.linkURL.setValue(linkURL);
        this.chkType.setValue(newWindow);
    },

    // overriding CQ.form.CompositeField#getValue
    getValue: function() {
        return this.getRawValue();
    },

    // overriding CQ.form.CompositeField#getRawValue
    getRawValue: function() {
		var value = "";
        var linkText = this.linkText.getValue() || "";
        var linkURL = this.linkURL.getValue() || "";
		var newWindow = this.chkType.getValue() || "";

        var value = linkText + "^" + linkURL + "^" + newWindow;
        this.hiddenField.setValue(value);
        return value;
    },
 
    // to make the the field mandatory
    markInvalid : function(msg){

    },

    clearInvalid : function(){

    } 

});
// register xtype
CQ.Ext.reg('formlinks', ContactusFormLinksEntry.ContactusFormLinks); 