/*
 * File Name: dayPart-greetings.js
 * Author: mcd-wifi
 * Description: This file is used for day part greetings component.
*/

/* cookie vars */
var timeGreeting = '';
var returnGreeting = '';;
var date = new Date();
var hour = date.getHours();
var cookieVisit = "visitsCookie";
var count = 1;
var dpMorning = $('.dataforheader').data('dpmorning');
var dpAfternoon = $('.dataforheader').data('dpafternoon');
var dpEvening = $('.dataforheader').data('dpevening');
var defaultGreeting = $('.dataforheader').data('defaultgreeting');
var cookieGreetings = '';

if($('.dataforheader').data('cookiegreetings') !== '' && typeof $('.dataforheader').data('cookiegreetings') !== 'undefined'){
	cookieGreetings = $('.dataforheader').data('cookiegreetings').split(',');
}

if (cookieGreetings[0] !== null && typeof cookieGreetings[0] !== 'undefined'){
	returnGreeting = cookieGreetings[0];
}

/* cookies start */
 if (hour < 12 && dpMorning != null) {
	timeGreeting = dpMorning;
} else if ((hour >= 12 && hour < 18) && dpAfternoon != null) {
	timeGreeting = dpAfternoon;
} else if((hour >= 18 && hour < 24) && dpEvening != null) {
	timeGreeting = dpEvening;
}
document.cookie = 'cookTimeGreet=' + timeGreeting;
doCookie();
/*cookies - end*/

function doCookie() {
	var index;
	if (document.cookie) {
		index = document.cookie.indexOf(cookieVisit);
	} else {
		index = -1;
	}
	var expires = "Wed, 30 Jun 2021 20:47:11 UTC";
	if (index === -1) {
		document.cookie = cookieVisit + "=1; expires=" + expires;
	} else {
		var countbegin = (document.cookie.indexOf("=", index) + 1);
		var countend = document.cookie.indexOf(";", index);
		if (countend === -1) {
			countend = document.cookie.length;
		}
		var count = parseInt(document.cookie.substring(countbegin, countend), 10) + 1;

		for (var i = 2; i <= cookieGreetings.length; i++){

		  if (count === i) {
			returnGreeting = cookieGreetings[i-1];
		  } else if (count > i && (defaultGreeting !== null && defaultGreeting !== 'undefined')){

			returnGreeting = defaultGreeting;
		  }
		}

		document.cookie = cookieVisit + "=" + count + "; expires=" + expires;
	} 
	$('#location').html(timeGreeting + "</br>" + returnGreeting);
}