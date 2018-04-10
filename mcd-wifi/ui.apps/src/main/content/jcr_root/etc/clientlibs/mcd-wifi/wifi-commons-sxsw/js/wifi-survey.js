/*
 * File Name: wifi-survey.js
 * Author: mcd-wifi
 * Description: This file is used for wifi survey component.
*/

$(document).ready(function() {
    $(".survey-complete").hide();
	
	/*function call on submit of survey*/
    $('.promotional-content-btn').on('click', function(e) {
        e.preventDefault();
        runSubmission();
    });

   /*function to chosse the option for the survey*/
    $('input:radio[name="food"]').change(
        function() {

            if (this.value === $('.dataforselector').data('option1')) {
                $("#option1").attr("src", $('.dataforselector').data('option1selectedimage'));
                $("#option1").attr("alt", $('.dataforselector').data('option1altselected'));
				$("#option1").attr("aria-live", 'polite');
				$("#option1").attr("aria-live", 'off');
                $("#option2").attr("src", $('.dataforselector').data('option2image'));
                $("#option2").attr("alt", $('.dataforselector').data('option2alt'));
                //$(".promotional-content-btn").removeClass('not-active');
                $('.survey-error-msg-box').css("display","none");
            }
          
            if (this.value === $('.dataforselector').data('option2')) {
                $("#option2").attr("src", $('.dataforselector').data('option2selectedimage'));
                $("#option2").attr("alt", $('.dataforselector').data('option2altselected'));
				$("#option2").attr("aria-live", 'polite');
				$("#option1").attr("aria-live", 'off');
                $("#option1").attr("src", $('.dataforselector').data('option1image'));
                $("#option1").attr("alt", $('.dataforselector').data('option1alt'));
                //$(".promotional-content-btn").removeClass('not-active');
                $('.survey-error-msg-box').css("display","none");
            }
        });


    var databaseUrl = $('.dataforselector').data('databaseurl');
    if (databaseUrl){
		var myDataRef = new Firebase($('.dataforselector').data('databaseurl'));
    }
	
    var resultsJson = [];
    var optionOneCount = 0;
    var optionTwoCount = 0;

    /*function to submit the survey*/
	function runSubmission() {

        if ($('input[name=food]:checked').length > 0) {
        	$('.survey-error-msg-box').css("display","none");
        	
            var value = $('input[name=food]:checked').val();
            myDataRef.push({
            text: value
            });
            
            myDataRef.on('value', function(s) {
                resultsJson = s.val();
                gotData();
            })
        }else{
        	//alert('Select at least one option!');
        	$('.survey-error-msg-box').css("display","block");
        }
    }

    function gotData() {

        for (var key in resultsJson) {
            if (resultsJson.hasOwnProperty(key)) {
                var val = resultsJson[key].text;
                switch (val) {
                    case $('.dataforselector').data('option1'):
                        optionOneCount++;
                        break;
                    case $('.dataforselector').data('option2'):
                        optionTwoCount++;
                }
            }
        }
        
        $("#optionOneCountID").html(optionOneCount); 
        $("#optionTwoCountID").html(optionTwoCount);

        $(".survey-complete").show();
        $(".survey-complete").show().attr('aria-hidden', 'false');
        $(".survey-complete").show().attr('role', 'alert');
        $(".survey-container").hide();
        optionOneCount = 0;
        optionTwoCount = 0;
    }

});

function surveyBoxMargin() {
    var surveyGridHeight = $('.grid-item ').height();
    var surveyCompleteHeight = $('.survey-complete').height();
    var surveyContainerHeight = $('.survey-container').height();
    
    var surveyCompleteMargin = (surveyGridHeight - surveyCompleteHeight)/2;
    $(".survey-complete").css({"margin-top": surveyCompleteMargin, "margin-bottom": surveyCompleteMargin});
    
    var surveyContainerMargin = (surveyGridHeight - surveyContainerHeight)/2;
    $(".survey-container").css({"margin-top": surveyContainerMargin, "margin-bottom": surveyContainerMargin});
}


$(window).on('load', function() {	
	$('.survey-container img').keypress(function (e) {
     var key = e.which;
     if(key === 13 || key === 32)  
      {
       $(this).click();
       e.preventDefault();
      }
    });
    surveyBoxMargin();
});


$(window).on('resize', function() {
    surveyBoxMargin();
});