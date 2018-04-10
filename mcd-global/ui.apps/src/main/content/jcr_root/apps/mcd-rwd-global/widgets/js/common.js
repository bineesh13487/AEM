CQ.Ext.ns("RWD.CQ");

RWD.CQ.util = {
    'enableImageToolbar': function(imageComponent, state) {
        if(state == 'originalavailable' || state == 'processedavailable'){
          imageComponent.enableToolbar();
        }
    },


    'getLanguageOptions': function() {
        try {
            var languages = CQ.I18n.getLanguages();
            var opts = [];
            for (var name in languages) {
                var lang = languages[name];
                if (lang.title) {
                    opts.push({
                        value: lang.defaultCountry ? lang.defaultCountry : name,
                        text: lang.title
                    });
                }
            }

            // CQ-19848: Enforcing use of proper Chinese collation methods (Pinyin for Simplified, stroke count for Traditional)
            var sortingLocale = CQ.I18n.getLocale();
            if (/^zh.(cn\b|sg\b|hans\b)/gi.exec(sortingLocale)){
                sortingLocale = "zh-Hans-cn-u-co-pinyin";
            } else if (/^zh.(tw\b|hk\b|mo\b|hant\b)/gi.exec(sortingLocale)){
                sortingLocale = "zh-Hant-tw-u-co-stroke";
            } else {
                sortingLocale = sortingLocale.replace("_", "-");
            }

            opts.sort(function(l1, l2) {
                if (l1.text.localeCompare(l2.text, sortingLocale) < 0) {
                    return -1;
                } else if (l1.text.localeCompare(l2.text, sortingLocale) == 0) {
                    return 0;
                } else {
                    return 1;
                }
            });
            return opts;
        } catch (e) {
            CQ.Log.error("CQ.utils.WCM#getLanguageOptions failed: " + e.message);
        }
        return [];
	}
};