

(function (undefined) {
    var addPathBrowserDropSupport;
    addPathBrowserDropSupport = function () {
        if (window.$) {
            // when a dialog shows up, add drop capabilities to pathbrowser widgets.
            $(document).on("foundation-contentloaded", function (event) {
                $(document).find(".cq-dialog .coral-PathBrowser").each(function () {
                    var $element = $(this);
                    var widget = $element ? $element.data("pathBrowser") : undefined;

                    if (widget && !$element.data("pathbrowserprocessed")) {
                        //hook into file upload drag and drop interaction
                        widget.$element.addClass('cq-FileUpload');
                        widget.$element.on('assetselected', function (event) {
                            widget.$element.find('input').val(event.path);
                        });
                        $element.data("pathbrowserprocessed", true);
                    }
                });
            });
        } else {
            setTimeout(addPathBrowserDropSupport, 3000);
        }
    };
    addPathBrowserDropSupport();
})();
