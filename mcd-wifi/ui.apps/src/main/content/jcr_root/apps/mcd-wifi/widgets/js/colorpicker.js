/**
 * @class CQ.form.CustomColorField
 * @extends CQ.Ext.form.TriggerField
 * The CustomColorField lets the user to choose the color
 * using the {@link CQ.form.CustomColorField}.
 * @constructor
 * Creates a new CustomColorField.
 * @param {Object} config The config object
 */
CQ.form.CustomColorField = CQ.Ext.extend(CQ.Ext.form.TriggerField, {

    /**
     *An array of 6-digit color hex code strings (without the # symbol). This array can contain any number
     **/
    colors : undefined,

    /**
     * A class for the drop-down color palette.  Defaults to 'x-color-palette'.
     */
    paletteCls: 'x-color-palette x-color-picker',

    /**
     * A style applied to the drop-down color palette.  Particularly useful for setting the height and width
     * for a CustomColorField instantiated by a dialog.
     */
    paletteStyle: '',

    constructor: function(config) {
        config = CQ.Util.applyDefaults(config, {
            showHexValue:false,
            triggerClass:"x-form-color-trigger",

            defaultAutoCreate: {
                tag:"input",
                type:"text",
                size:"10",
                autocomplete:"off",
                maxlength:"6"
            },

            lengthText:CQ.I18n.getMessage("Hexadecimal color values must have either 3 or 6 characters."),
            blankText:CQ.I18n.getMessage("Must have a hexidecimal value of the format ABCDEF."),

            defaultColor:"FFFFFF",
            curColor:"ffffff",
            colors: [
				'FFFFFF', '000000', 'EEECE1', '1F497D', '4F81BD', 'C0504D', '9BBB59', '8064A2', '4BACC6', 'F79646',
				
				'F2F2F2', '7F7F7F', 'DDD9C3', 'C6D9F0', 'DBE5F1', 'F2DCDB', 'EBF1DD', 'E5E0EC', 'DBEEF3', 'FDEADA',
				'D9D9D9', '595959', 'C4BD97', '8DB3E2', 'B8CCE4', 'E5B9B7', 'D7E3BC', 'CCC1D9', 'B7DDE8', 'FBD5B5',
				'BFBFBF', '404040', '938953', '548DD4', '95B3D7', 'D99694', 'C3D69B', 'B2A2C7', '92CDDC', 'FAC08F',
				'A6A6A6', '262626', '494429', '17365D', '366092', '953734', '76923C', '5F497A', '31859B', 'E36C09',
				'808080', '0D0D0D', '1D1B10', '0F243E', '244061', '632423', '4F6128', '3F3151', '205867', '974806',
				
				'C00000', 'FF0000', 'FFC000', 'FFFF00', '92D050', '00B050', '00B0F0', '0070C0', '002060', '7030A0'				
			],  
            maskRe:/[a-f0-9]/i,
            regex:/[a-f0-9]/i
        });
        CQ.form.CustomColorField.superclass.constructor.call(this, config);
        this.on("render", this.setDefaultColor);
    },

    validateValue: function(value) {
        if (!this.showHexValue) {
            return true;
        }
        if (value.length < 1) {
            this.el.setStyle( {
                "background-color":"#" + this.defaultColor
            });
            if (!this.allowBlank) {
                this.markInvalid(String.format(this.blankText, value));
                return false
            }
            return true;
        }
        if (value.length !== 3 && value.length !== 6) {
            this.markInvalid(String.format(this.lengthText, value));
            return false;
        }
        this.setColor(value);
        return true;
    },

    validateBlur: function() {
        return !this.menu || !this.menu.isVisible();
    },

    getValue: function() {
        return this.curColor || this.defaultColor;
    },

    setValue: function(hex) {
        CQ.form.CustomColorField.superclass.setValue.call(this, hex);
        this.setColor(hex);
    },

    setColor: function(hex) {
        this.curColor = hex;

        this.el.setStyle( {
            "background-color":"#" + hex,
            "background-image":"none"
        });
        if (!this.showHexValue) {
            this.el.setStyle( {
                "text-indent":"-100px",
                "width":"180px"
            });
            if (CQ.Ext.isIE) {
                this.el.setStyle( {
                    "margin-left":"100px"
                });
            }
        }
    },

    setDefaultColor: function() {
        this.setValue(this.defaultColor);
    },

    menuListeners: {
        select: function(m, d) {
            this.setValue(d);
        },
        show: function() { // retain focus styling
            this.onFocus();
        },
        hide: function() {
            this.focus();
            var ml = this.menuListeners;
            this.menu.un("select", ml.select, this);
            this.menu.un("show", ml.show, this);
            this.menu.un("hide", ml.hide, this);
        }
    },

    handleSelect: function(palette, selColor) {
        this.setValue(selColor);
    },

    onTriggerClick: function() {
        if (this.disabled) {
            return;
        }
        if (this.menu == null) {
            var menuConfig = {
                colors: this.colors.slice(0),  // array copy
                itemCls: this.paletteCls,
                style: this.paletteStyle
            };
            this.menu = new CQ.Ext.menu.ColorMenu(menuConfig);
            this.menu.palette.on("select", this.handleSelect, this);
        }
        this.menu.on(CQ.Ext.apply( {}, this.menuListeners, {
            scope:this
        }));
        this.menu.show(this.el, "tl-bl?");
    }
});
CQ.Ext.reg("customcolorfield", CQ.form.CustomColorField);