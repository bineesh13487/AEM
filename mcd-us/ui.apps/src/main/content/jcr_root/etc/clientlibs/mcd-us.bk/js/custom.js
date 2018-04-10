//state dropdown, popular faq dropdown//
var pageType;
var path;
var pathtarget;
$('.ddCustom ul li a').on('click', function(e){
    $(this).parents('.ddCustom').find('button>span').text($(this).text());
    $(this).parents('.ddCustom').find('input').val($(this).text());
//    $('.stateList>button>span, .ddCustom>button>span').text($(this).text());
	 pageType = $(this).text();
	 path = $(this).attr('href');
	 pathtarget = $(this).attr('target');
	 if(path!=undefined && path!='' && path!='#')
	 {
	    $('a.searchfilter-btn').attr('href', path);
	 }
	 else
	 {
	    $('a.searchfilter-btn').attr('href', '');
	 }
	 $('a.searchfilter-btn').attr('target', pathtarget);
	e.preventDefault();
});

$('.popularFAQBox .q-a__question a').on('click', function(e){
	return false;
});


$(".modal").on("hidden.bs.modal", function(){
if($("#mask").length>0)
    {
        $("#mask").hide();
    }
});

//Smartapp banner fix
$('button.hamburger').on('click', function(){
    if($('#smartbanner').is(':visible')){
       var smartappbannerHeight = $('#smartbanner').height();
        var totalMarginS = smartappbannerHeight + 110;
        $('.navbar-collapse.top-nav-collapse').css("margin-top", totalMarginS);

    }else{
        $('.navbar-collapse.top-nav-collapse').css("margin-top", 90);
    }
});

$(document).ready(function() {
    var host = window.location.host;
    $('a').each(function() {
        var dataTrack = $(this).attr('data-track')
        if(dataTrack && dataTrack.indexOf('video') === -1) {
            var href = $(this).attr('href');
            if (href && (href.indexOf('http') === 0 || href.indexOf('https') === 0) && href.indexOf(host) === -1) {
                    var text = $(this).attr('title');
                    if (!text) {
                        text = $(this).attr('alt') ? $(this).attr('alt') : $(this).text();
                    }
                    $(this).attr('data-track', 'externalLink')
                        .attr('data-at', 'Exit:' + config.get('analyticsPageName') + ':' + text.trim());
                }
            }
    });
    

});

//FAQ Component: To get the hash value from url
$(window).load(function(){
	var hValue = window.location.hash.substr(1);
	if(isEmpty(hValue)) {
		$('#' + hValue).collapse('show');
		$('html, body').animate({scrollTop:$('#' + hValue).offset().top - 140}, 'slow');
        }
    });

function isEmpty(val){
	return (val === undefined || val == null || val.length <= 0) ? false : true;
}