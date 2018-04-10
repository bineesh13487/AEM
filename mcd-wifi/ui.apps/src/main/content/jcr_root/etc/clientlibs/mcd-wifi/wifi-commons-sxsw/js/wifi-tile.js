/*
 * File Name: dayPart-greetings.js
 * Author: mcd-wifi
 * Description: This file is used for wifi tile component.
*/
var currentHour = date.getHours();
$(document).ready(function() {	
    var tileComponentLength = $('.wifi-tile').length;
    var dataTime;
    for(var i =0;i<tileComponentLength;i++){
        var recruiterTileLenth = $($('.wifi-tile')[i]).find('.recruiter-tile').length;
        var tileToShow = $($('.wifi-tile')[i]).find('.recruiter-tile');
        var flag = false;
        if(recruiterTileLenth > 1){
           for(var j =0; j<recruiterTileLenth;j++ ){
                dataTime = $(tileToShow[j]).parent().data('time');
                flag = calculateDayPart(currentHour, dataTime);
                if(flag){
                    $(tileToShow[j]).closest('.grid-item').css("display","block");
                    break;
                }
            }
        }
        if(!flag){
          $(tileToShow[0]).closest('.grid-item').css("display","block");
        }
    }
});

function calculateDayPart(currentHour, dataTime){
	var flag = false;
	 if(currentHour < 12 && dataTime === "morning"){
         flag = true;
     }
     else if(currentHour < 18 && currentHour >= 12 && dataTime === "afternoon"){
       flag = true;
     }
     else if(currentHour < 23 && currentHour >= 18 && dataTime === "evening"){
       flag = true;
     }
	 
	 return flag;
}