/*
 * File Name: language-toggle.js
 * Author: mcd-wifi
 * Description: This file is used for language toggle component.
*/

$(window).on('load', function() {
	var mobile767 = window.matchMedia('only screen and (max-width: 767px)') ;

	/*Replace value of language based on devices*/
	$('.languageFlyout li a').each(function(){
		var concatLang = $(this).text();
		var langArr = concatLang.split('-');
		if(mobile767.matches){
			/*User array index 0 to show EN instead of showing English*/
			$(this).text(langArr[1]);
		}else{
			$(this).text(langArr[1]);
		}
	});
	
	/*Copy text of active link*/
	if($('.languageFlyout li a').hasClass('active')){
        var str = $('.languageFlyout li a.active').text();
        $('.languageLink').text(str);
	}
	
    /*On mouseover and click functionality*/
    $( ".languageLink" ).click(function(e) {
    	e.preventDefault();
		$(".languageFlyout").toggle();
	});

    $('html').click(function(e) {                    
        if(!$(e.target).hasClass('languageLink') )
        {
            $('.languageFlyout').hide();               
        }
     }); 

    
    /* OnChange functionality for language toggler */
    $('.languageFlyout li a').on('click', function () {
        var url = $(this).attr("href");
        if (url) {
            window.location = url;
        }
        $('.languageFlyout').hide();
        return false;
    });
});

