var app = angular.module('mcdWifi', ['ngSanitize']);

app.filter('hrsSplit', function() {
    return function(input, splitChar, splitIndex) {
        return input.split(splitChar)[splitIndex];
    }
});

app.filter('dateFilter', function() {
	return function(input) {
		if (input == null) {
			return "";
		}
        var twitterDate = input;
        return diffTime(twitterTimeFormat(twitterDate), localTimeFormat());
	};
});

app.filter('timeStampFilter', function() {
	return function(input) {
		if (input == null) {
			return "";
		}
		var currentTimeInMillis = new Date().getTime();
		var inputInMillis = new Date(input * 1000);
		var hoursDiff = (currentTimeInMillis - inputInMillis)
				/ (1000 * 60 * 60);
		var days = (hoursDiff / 24).toFixed(1);
		var totalDays = parseInt(days.toString().split('.')[0]);
		var totalHours = parseInt(days.toString().split('.')[1]);
		if (totalDays === 0) {
			return totalHours + " hours";
		} else {
			return totalDays + " days";
		}
	};
});

/* Find month index number*/
function monthIndex(month){
	var monthNumber = month;
	monthNumber = monthNumber.toLowerCase();
	var months = ["jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"];
	monthNumber = months.indexOf(monthNumber) + 1;
	return monthNumber;
}

/*Local time format*/
function localTimeFormat(){
	var localDate = new Date();
	var localDateUTC = localDate.toUTCString();
	var localDateTime = localDateUTC.split(" ");
	var localMonth = localDateTime[2];
	var localdate = localDateTime[1];	
	var localYear = localDateTime[3];
	var localTime = localDateTime[4];
	var localFMonth = monthIndex(localMonth);
	if(localFMonth < 10){
		localFMonth = '0' + localFMonth;
	}
	var finalLocalDateTime = localFMonth + '/' + localdate + '/' + localYear + ' ' + localTime;
	return finalLocalDateTime;
}

/*Twitter time format*/
function twitterTimeFormat(TweetDT){
	var twitterDate = TweetDT;
	var tweetDateTime = twitterDate.split(" ");
	var tweetMonth = tweetDateTime[1];
	var tweetdate = tweetDateTime[2];
	var tweetYear = tweetDateTime[5];
	var tweetTime = tweetDateTime[3];
	var tweetFMonth = monthIndex(tweetMonth);
		if(tweetFMonth < 10){
			tweetFMonth = '0' + tweetFMonth;
		}
	var finaltweetDateTime = tweetFMonth + '/' + tweetdate + '/' + tweetYear + ' ' + tweetTime;
	return finaltweetDateTime;
}

/*Difference between two date (mm/dd/yyyy hh:mm:ss) for twitter*/
function diffTime(fDate, sDate){
	var date1 = new Date(fDate);
	var date2 = new Date(sDate);
	var diff = date2.getTime() - date1.getTime();
	var msec = diff;
	var hh = Math.round(msec / 1000 / 60 / 60);
	msec -= hh * 1000 * 60 * 60;
	var mm = Math.round(msec / 1000 / 60);
	msec -= mm * 1000 * 60;
	var ss = Math.round(msec / 1000);
	msec -= ss * 1000;
	var finalTime;

	if(hh>24){
		finalTime = Math.floor( hh/24 ) + " days";
	}else{
		finalTime = hh + " hours";
	}
	
	return finalTime;	
}