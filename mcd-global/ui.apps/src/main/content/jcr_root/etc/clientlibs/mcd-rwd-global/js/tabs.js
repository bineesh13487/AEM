(function($) {
    $('ul.nav.nav-tabs li').each(function(){
        if($(this).data('start') && $(this).data('end')) {
			var start = $(this).data('start');
            var end = $(this).data('end');

            if(matchesTimeInterval(start, end)) {
                $(this).find('a').tab('show');
            }
		}
    });
})(jQuery);