(function($) {
    $(document).on('ready', function() {
        var bumper = $('#leavingMcD');

        if (bumper && bumper.length) {
            $('body, .dropdown-menu > li').on('click', 'a.external', function(e) {
                if ($(this).attr('href')) {
                    e.preventDefault();
                    $(bumper).find('#bumper-continue').attr({
                        'href': $(this).attr('href'),
                        'target': $(this).attr('target') ? $(this).attr('target') : '_self'
                    });
                }
                $(bumper).modal("toggle");
            });

            $(bumper).find('#bumper-continue').on('click', function(e) {
                $(bumper).modal('toggle');
            });
        }
        
        var ncExitBumper = $('#ncExitModal');
        
        if (ncExitBumper && ncExitBumper.length) {
            $('body, .dropdown-menu > li').on('click', 'a', function(e) {
            	if ($(".row.meal-row").length>0) {
            	if (!$(this).hasClass("external") && $(this).attr("id")!='bumper-continue') {
            		if ($(this).attr('href')) {
		            		if ($(this).attr('href').indexOf('#')<0 && $(this).attr('href').indexOf('javaScript')<0) {
                         e.preventDefault();
		                        $(ncExitBumper).find('.nc-exit-yes > a').attr({
                             'href': $(this).attr('href'),
                             'target': $(this).attr('target') ? $(this).attr('target') : '_self'
                         });
                         $(ncExitBumper).modal("show");
                     }
            		}
            		}
            		
            	}
               
            });

            $(ncExitBumper).find('.nxExitClick').on('click', function(e) {
                $(ncExitBumper).modal('hide');
                e.stopPropagation();
            });
        }
        
        
        
    });
})(jQuery);