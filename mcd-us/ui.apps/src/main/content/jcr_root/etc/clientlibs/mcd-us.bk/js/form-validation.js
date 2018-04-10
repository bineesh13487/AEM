$(document).ready(function(){
	var adaMonthNames;
	if($('#adaTextJson').attr('data-ada-json'))	{
		adaMonthNames = JSON.parse($('#adaTextJson').attr('data-ada-json')).monthList.replace(/[\[\]']+/g, '').split(',');
	} else {
		adaMonthNames = ["January","February","March","April","May","June","July","August","September","October","November","December"];
	}
	var currentMonth = moment().month();
	var currentYear = moment().year();
	var yearArray = [];
	
	if(currentMonth == 0)
		yearArray = [currentYear, currentYear-1];
	else 
		yearArray = [currentYear];
	
	var monthsArray = [currentMonth > 9 ? currentMonth : "0" + currentMonth, (currentMonth + 1) > 9 ? currentMonth : "0" + (currentMonth +1)];
	var adaMonthsArray = [adaMonthNames[moment(new Date()).subtract(1,'months').month()], adaMonthNames[currentMonth]];
	
	if($("#contactUs-month-select").length > 0 || $("#contactUs-month-select-trade").length > 0) {
		$.each(monthsArray, function(index, value){
			var htmlElement = '<li data-original-index="'+(index+1)+'"><a tabindex="0" class="" style="" data-tokens="null" aria-disabled="false" role="option"><span aria-hidden="true">'+monthsArray[index]+'</span> <span class="visuallyhidden">'+adaMonthsArray[index]+'</span><span class="glyphicon glyphicon-ok check-mark"></span></a></li>';
			var selectHTMLElement = '<option data-content="<span aria-hidden=\'true\'>'+monthsArray[index] +'</span> <span class=\'visuallyhidden\'>'+adaMonthsArray[index]+ '</span>" value="'+monthsArray[index]+'">'+monthsArray[index]+'</option>'
			$(htmlElement).appendTo($("#contactUs-month-select").length > 0 ? $("#contactUs-month-select .dropdown-menu.open .dropdown-menu.inner")[0] : $("#contactUs-month-select-trade .dropdown-menu.open .dropdown-menu.inner")[0]);
			$(selectHTMLElement).appendTo($("#contactUs-month-select").length > 0 ? $("#contactUs-month-select select")[0] : $("#contactUs-month-select-trade select")[0]);
		});
		
		$.each(yearArray, function(index, value){
			var htmlElement = '<li data-original-index="'+(index+1)+'"><a tabindex="0" class="" style="" data-tokens="null" aria-disabled="false" role="option"><span class="text">'+yearArray[index]+'</span><span class="glyphicon glyphicon-ok check-mark"></span></a></li>';
			var selectHTMLElement = '<option value="'+yearArray[index]+'">'+yearArray[index]+'</option>';
			$(htmlElement).appendTo($("#contactUs-year-select").length > 0 ? $("#contactUs-year-select .dropdown-menu.open .dropdown-menu.inner")[0]: $("#contactUs-year-select-trade .dropdown-menu.open .dropdown-menu.inner")[0]);
			$(selectHTMLElement).appendTo($("#contactUs-year-select").length > 0 ? $("#contactUs-year-select select")[0] : $("#contactUs-year-select-trade select")[0]);
		});
	}
	
	if($("#contactUs-month-input").length > 0 && $("#contactUs-month-input").val() != ""){
		$("#contactUs-month-select #month-select").attr("title", $("#contactUs-month-input").val());
		$("#contactUs-month-select #month-select :first").remove();
		var staticHTML = '<span class="filter-option pull-left click-before-outline" style="outline: none;"><span aria-hidden="true">'+$("#contactUs-month-input").val()+'</span><span class="visuallyhidden"> '+adaMonthNames[parseInt($("#contactUs-month-input").val())-1]+'</span></span>';
		$("#contactUs-month-select #month-select").prepend(staticHTML);
		
	} else if ($("#contactUs-month-input-trade").length > 0 && $("#contactUs-month-input-trade").val() != ""){
		$("#contactUs-month-select-trade #month-select-trade").attr("title", $("#contactUs-month-input-trade").val());
		$("#contactUs-month-select-trade #month-select-trade :first").remove();
		var staticHTML = '<span class="filter-option pull-left click-before-outline" style="outline: none;"><span aria-hidden="true">'+$("#contactUs-month-input-trade").val()+'</span><span class="visuallyhidden"> '+adaMonthNames[parseInt($("#contactUs-month-input-trade").val())-1]+'</span></span>';
		$("#contactUs-month-select-trade #month-select-trade").prepend(staticHTML);
	}
	
	if($("#contactUs-year-input").length > 0 && $("#contactUs-year-input").val() != "" ){
		$("#contactUs-year-select #year-select").attr("title", $("#contactUs-year-input").val());
		$("#contactUs-year-select #year-select :first").remove();
		var staticHTML = '<span class="filter-option pull-left click-before-outline" style="outline: none;"><span aria-hidden="true">'+$("#contactUs-year-input").val()+'</span><span class="visuallyhidden"> '+$("#contactUs-year-input").val()+'</span></span>';
		$("#contactUs-year-select #year-select").prepend(staticHTML);
		
	} else if ($("#contactUs-year-input-trade").length > 0 && $("#contactUs-year-input-trade").val() != "" ){
		$("#contactUs-year-select-trade #year-select-trade").attr("title", $("#contactUs-year-input-trade").val());
		$("#contactUs-year-select-trade #year-select-trade :first").remove();
		var staticHTML = '<span class="filter-option pull-left click-before-outline" style="outline: none;"><span aria-hidden="true">'+$("#contactUs-year-input-trade").val()+'</span><span class="visuallyhidden"> '+$("#contactUs-year-input-trade").val()+'</span></span>';
		$("#contactUs-year-select-trade #year-select-trade").prepend(staticHTML);
	}
});

function validateForm(){
	var errorSymbol = '<i class="fa fa-exclamation-triangle" aria-hidden="true"></i><span class="visuallyhidden">error</span>';
	$(".personalinfo button:last-child").removeClass("no-ring click-before-outline");
	$(".personalinfo button:last-child").removeAttr( 'style' );


	var locationCheck = $('input:radio[name=contactUs-restaurantFeedback-locationSpecific]:checked').val();

    if(locationCheck == "yes"){

        if($("#restAddress").hasClass("yes") || $("#restCity").hasClass("yes") || $("#restLandmark").hasClass("yes") || $("#contactUs-visitType").hasClass("yes")){
            $('#restaurant-feedback-form-container').addClass("in");
            $('#restaurantLocatorForm').addClass("in");
    	}
    }
	if($(".restInfo #restAddress").hasClass("yes") || $(".restInfo #restCity").hasClass("yes") || $(".restInfo #contactUs-state").hasClass("yes") || $(".restInfo #restLandmark").hasClass("yes") || $(".restInfo #contactUs-visitType").hasClass("yes")){

        $('#restaurant-feedback-form-container').addClass("in");
        $('#restaurantLocatorForm').addClass("in");
    }

	var rules = new Object();
	
    if($("#contactUs-first-name").hasClass("yes")){
        rules[$('#contactUs-first-name').attr('name')] = { required: true };
    }
    if($("#contactUs-lastName").hasClass("yes")){
        rules[$('#contactUs-lastName').attr('name')] = { required: true };
    }
	if($("#contactUs-address-street").hasClass("yes")){
        rules[$('#contactUs-address-street').attr('name')] = { required: true };
    }
	if($("#contactUs-city").hasClass("yes")){
        rules[$('#contactUs-city').attr('name')] = { required: true };
    }
    if($("#contactUs-state-select").hasClass("yes")){
        rules[$('#contactUs-state-input').attr('name')] = { required: true };
    }
    if($("#mobileDevicedd").hasClass("deviceType")){
            rules[$('#contactUs-mobileDeviceType-input').attr('name')] = { required: true };
    }
    if($("#operatingSystemdd").hasClass("operatingSystemdd")){
                rules[$('#contactUs-operatingSystem-input').attr('name')] = { required: true };
    }
    if($("#issueTypetopicId").hasClass("yes")) {
        rules[$('#contactUs-topic-input').attr('name')] = {required: true};
    }
	if($("#contactUs-zip").hasClass("yes")){
        rules[$('#contactUs-zip').attr('name')] = { required: true,digits: true,minlength: 5 };
    }
    if($("#contactUs-followup").hasClass("yes")){
        rules["responseText"] = { required: true};
    }
    if($("#contactUs-email").hasClass("yes")){
        rules[$('#contactUs-email').attr('name')] = { required: true,email: true };
    }
    if($("#contactUs-emailConfirmation").hasClass("yes")){
        rules[$('#contactUs-emailConfirmation').attr('name')] = { required: true,equalTo: '[name="email"]' };
    }
    if($("#contactUs-phone").hasClass("yes")){
        rules[$('#contactUs-phone').attr('name')] = { required: true,digits: true,minlength: 10 };
    }

    if($("#enter-comments").hasClass("yes")){
        rules[$('#enter-comments').attr('name')] = { required: true};
    }
    if($("#restAddress").hasClass("yes")){
        rules[$('#restAddress').attr('name')] = {
            required: {
                depends: function() {
                    if($('input:radio[name=contactUs-restaurantFeedback-locationSpecific]:checked').val() == 'yes' || $(".restInfo #restAddress").hasClass("yes")){
                        return true;
                    }
                }
            }
        };
    }
    if($("#restCity").hasClass("yes")){
        rules[$('#restCity').attr('name')] = {
            required: {
                depends: function() {
					if($('input:radio[name=contactUs-restaurantFeedback-locationSpecific]:checked').val() == 'yes' || $(".restInfo #restCity").hasClass("yes")){
                        return true;
                    }
                }
            }
        };
    }
    if($("#contactUs-resstate-select").hasClass("yes")){
        rules[$('#contactUs-resstate-input').attr('name')] = {
            required: {
                depends: function() {
					if($('input:radio[name=contactUs-restaurantFeedback-locationSpecific]:checked').val() == 'yes' || $(".restInfo #contactUs-resstate-select").hasClass("yes")){
                        return true;
                    }
                }
            }
        };
    }
	if($("#restLandmark").hasClass("yes")){
        rules[$('#restLandmark').attr('name')] = {
            required: {
                depends: function() {
                    if($('input:radio[name=contactUs-restaurantFeedback-locationSpecific]:checked').val() == 'yes' || $(".restInfo #restLandmark").hasClass("yes")){
                        return true;
                    }
                }
            }
        };
    }
    if($("#resDate").hasClass("yes")){
        rules[$('#resDate').attr('name')] = {
            required: {
                depends: function() {
                    if(($('input:radio[name=contactUs-restaurantFeedback-locationSpecific]:checked').val() == 'yes' || $(".restInfo #resDate").hasClass("yes")) 
                    		&& $('#contactUs-day-input').val() != "" && $('#contactUs-month-input').val() != "" && $('#contactUs-year-input').val() != ""){
                        return true;
                    }
                }
            }, dateValidationCheckUS :{
				depends : function() {
					if (($('input:radio[name=contactUs-restaurantFeedback-locationSpecific]:checked').val() == 'yes' || $(".restInfo #resDate").hasClass("yes")) 
							&& $('#contactUs-day-input').val() != "" && $('#contactUs-month-input').val() != "" && $('#contactUs-year-input').val() != "") {
						return true;
					}
				}
			}
        };
    }
    
    if($("#resDate").hasClass("no")){
    	rules[$('#resDate').attr('name')] = {
    		exceptionalDateValidationCheckUS :true
    	};
    }
    
    if($("#contactUs-visitType").hasClass("yes")){
        rules["visitType"] = {
            required: {
                depends: function() {
                    if($('input:radio[name=contactUs-restaurantFeedback-locationSpecific]:checked').val() == 'yes' || $(".restInfo #contactUs-visitType").hasClass("yes")){
                        return true;
                    }
                }
            }
        };
    }
    if ($("#contactUs-day-select").hasClass("yes")) {
		rules[$('#contactUs-day-input').attr('name')] = { required : true }
	}
    if ($("#contactUs-month-select").hasClass("yes")) {
		rules[$('#contactUs-month-input').attr('name')] = { required : true }
	}
	if ($("#contactUs-year-select").hasClass("yes")) {
		rules[$('#contactUs-year-input').attr('name')] = { required : true }
	}
	if ($("#contactUs-day-select-trade").hasClass("yes")) {
		rules[$('#contactUs-day-input-trade').attr('name')] = {
			required : {
				depends : function() {
					if ($('input:radio[name=previousSubmissionDate]:checked').val() == 'yes') {
						return true;
					}
				}
			}
		};
	}
	if ($("#contactUs-month-select-trade").hasClass("yes")) {
		rules[$('#contactUs-month-input-trade').attr('name')] = {
			required : {
				depends : function() {
					if ($('input:radio[name=previousSubmissionDate]:checked').val() == 'yes') {
						return true;
					}
				}
			}
		};
	}
	if ($("#contactUs-year-select-trade").hasClass("yes")) {
		rules[$('#contactUs-year-input-trade').attr('name')] = {
			required : {
				depends : function() {
					if ($('input:radio[name=previousSubmissionDate]:checked').val() == 'yes') {
						return true;
					}
				}
			}
		};
	}
	if($("#how-used").hasClass("yes")){
        rules[$('#how-used').attr('name')] = { required: true };
    }
    if($("#intended-audience").hasClass("yes")){
        rules[$('#intended-audience').attr('name')] = { required: true };
    }
    if($("#how-long").hasClass("yes")){
        rules[$('#how-long').attr('name')] = { required: true };
    }
	if($("#prevPermissionRequest").hasClass("yes")){
        rules["previousSubmissionDate"] = { required: true}; 
    }
    if($("#when-submitted").hasClass("yes")){
        rules["requestSubmissionDate"] = {
            required: {
                depends: function() {
                    if($('input:radio[name=previousSubmissionDate]:checked').val() == 'yes' && $('#contactUs-day-input-trade').val() != "" 
                    		&& $('#contactUs-month-input-trade').val() != "" && $('#contactUs-year-input-trade').val() != ""){
                        return true;
                    }
                }
            }, dateValidationCheckUS :{
				depends : function() {
					if ($('input:radio[name=previousSubmissionDate]:checked').val() == 'yes' && $('#contactUs-day-input-trade').val() != "" 
							&& $('#contactUs-month-input-trade').val() != "" && $('#contactUs-year-input-trade').val() != "") {
						return true;
					}
				}
			}
        };
    }
	if($("#outcomeRequired").hasClass("yes")){
        rules["outcome"] = {
            required: {
                depends: function() {
                    if($('input:radio[name=previousSubmissionDate]:checked').val() == 'yes'){
                        return true;
                    }
                }
            }
        };
    }
	if($("#author").hasClass("yes")){
        rules[$('#author').attr('name')] = { required: true };
    }
    if($("#publisher").hasClass("yes")){
        rules[$('#publisher').attr('name')] = { required: true };
    }
    if($("#pub-title").hasClass("yes")){
        rules[$('#pub-title').attr('name')] = { required: true };
    }
    if($("#app-version").hasClass("yes")){
            rules[$('#app-version').attr('name')] = { required: true };
    }
	
	if($("#aboutOptionsValueSelection").hasClass("yes")){
        rules[$('#contactUs-topic-input').attr('name')] = { required: true };
    }
	
	var rulesObj = JSON.stringify(rules);
    $("#contactus").validate({
        ignore: "",
        rules: rules,
        messages: {
			firstName: errorSymbol + $("#contactUs-first-name-message").html(),
			lastName: errorSymbol + $("#contactUs-lastName-message").html(),
			customerAddress1: errorSymbol + $("#contactUs-address-street-message").html(),
			city: errorSymbol + $("#contactUs-city-message").html(),
			state: errorSymbol + $("#contactUs-state-select-message").html(),
			zip: errorSymbol + $("#contactUs-zip-message").html(),
			responseText: errorSymbol + $("#contactUs-followup-message").html(),
			email: errorSymbol + $("#contactUs-email-message").html(),
			confirmEmail: errorSymbol + $("#contactUs-emailConfirmation-message").html(),
			phone: errorSymbol + $("#contactUs-phone-message").html(),
			restAddress: errorSymbol + $("#restAddress-message").html(),
			restCity: errorSymbol + $("#restCity-message").html(),
			restState: errorSymbol + $("#restState-message").html(),
			restLandmark: errorSymbol + $("#restLandmark-message").html(),
			visitType: errorSymbol + $("#visitType-message").html(),
			incidentDate : errorSymbol + $("#resDate-validation-message").html(),
			comments : errorSymbol + $("#enter-comments-message").html(),
			usageType: errorSymbol + $("#how-used-message").html(),
			targetAudience: errorSymbol + $("#intended-audience-message").html(),
			operatingDuration: errorSymbol + $("#how-long-message").html(),
			previousSubmissionDate: errorSymbol + $("#previousSubmissionDate-message").html(),
			requestSubmissionDate: errorSymbol + $("#date-validation-message").html(),
			outcome: errorSymbol + $("#outcome-message").html(),
			author: errorSymbol + $("#author-message").html(),
			publisher: errorSymbol + $("#publisher-message").html(),
			publicationTitle: errorSymbol + $("#publicationTitle-message").html(),
			deviceType: errorSymbol + $("#contactUs-deviceType-message").html(),
			os: errorSymbol + $("#contactUs-operatingSystem-message").html(),
			appVersion: errorSymbol + $("#contactUs-appVersion-message").html(),
			topic: errorSymbol + $("#contactUs-topic-message").html(),
			dayInput : errorSymbol + $("#generalize-drop-down-message").html(),
			monthInput : errorSymbol + $("#generalize-drop-down-message").html(),
			yearInput : errorSymbol + $("#generalize-drop-down-message").html(),
			dayInputTrade : errorSymbol + $("#generalize-drop-down-message-trade").html(),
			monthInputTrade : errorSymbol + $("#generalize-drop-down-message-trade").html(),
			yearInputTrade : errorSymbol + $("#generalize-drop-down-message-trade").html(),
        },
   
        highlight: function(element) {
            if ($(element).attr("name") == "firstName") {
                $(element).closest("div").addClass('error');
				$(element).next("label.error").attr("aria-hidden",true);
            }
            else if ($(element).attr("name") == "lastName") {
                $(element).closest("div").addClass('error');
				$(element).next("label.error").attr("aria-hidden",true);
            }
            else if ($(element).attr("name") == "customerAddress1") {
                $(element).closest("div").addClass('error');
				$(element).next("label.error").attr("aria-hidden",true);
            }
            else if ($(element).attr("name") == "city") {
               $(element).closest("div").addClass('error');
				$(element).next("label.error").attr("aria-hidden",true);
            }
			else if ($(element).attr("name") == "state") {
               $(element).closest("div").parent("div").addClass('error');
				$(element).next("label.error").attr("aria-hidden",true);
            }
            else if ($(element).attr("name") == "zip") {
                $(element).closest("div").addClass('error');
				$(element).next("label.error").attr("aria-hidden",true);
            }
            else if ($(element).attr("name") == "responseText") {
                $(element).closest("div").parent("div").addClass('error');
				$(element).parent("div").parent().parent().find("label.error").attr("aria-hidden",true);;
            }
            else if ($(element).attr("name") == "email") {
                $(element).closest("div").addClass('error');
				$(element).next("label.error").attr("aria-hidden",true);
            }
            else if ($(element).attr("name") == "confirmEmail") {
                $(element).closest("div").addClass('error');
				$(element).next("label.error").attr("aria-hidden",true);
            }
            else if ($(element).attr("name") == "phone") {
                $(element).closest("div").addClass('error');
				$(element).next("label.error").attr("aria-hidden",true);
            }
            else if ($(element).attr("name") == "comments") {
                $(element).closest("div").addClass('error');
				$(element).next("label.error").attr("aria-hidden",true);
            }
            else if ($(element).attr("name") == "restAddress") {
                $(element).closest("div").addClass('error');
				$(element).next("label.error").attr("aria-hidden",true);
            }
            else if ($(element).attr("name") == "restCity") {
                $(element).closest("div").addClass('error');
				$(element).next("label.error").attr("aria-hidden",true);
            }
            else if ($(element).attr("name") == "restState") {
                $(element).closest("div").parent("div").addClass('error');
				$(element).next("label.error").attr("aria-hidden",true);
            }
            else if ($(element).attr("name") == "dayInput") {
                $("#day-select").addClass('select-border');
				$(element).next("label.error").attr("aria-hidden",true);
            }
            else if ($(element).attr("name") == "monthInput") {
            	$("#month-select").addClass('select-border');
				$(element).next("label.error").attr("aria-hidden",true);
            }
            else if ($(element).attr("name") == "yearInput") {
            	$("#year-select").addClass('select-border');
				$(element).next("label.error").attr("aria-hidden",true);
            }
            else if ($(element).attr("name") == "dayInputTrade") {
                $("#day-select-trade").addClass('select-border');
				$(element).next("label.error").attr("aria-hidden",true);
            }
            else if ($(element).attr("name") == "monthInputTrade") {
            	$("#month-select-trade").addClass('select-border');
				$(element).next("label.error").attr("aria-hidden",true);
            }
            else if ($(element).attr("name") == "yearInputTrade") {
            	$("#year-select-trade").addClass('select-border');
				$(element).next("label.error").attr("aria-hidden",true);
            }
			else if ($(element).attr("name") == "restLandmark") {
                $(element).closest("div").addClass('error');
				$(element).next("label.error").attr("aria-hidden",true);
            }
            else if ($(element).attr("name") == "visitType") {
                $(element).closest("div").parent("div").addClass('error');
				$(element).parent("div").parent().parent().find("label.error").attr("aria-hidden",true);
            }
            else if ($(element).attr("name") == "incidentDate") {
                $(element).closest("div").addClass('error');
            }
            else if ($(element).attr("name") == "usageType") {
                $(element).closest("div").addClass('error');
				$(element).next("label.error").attr("aria-hidden",true);
            }
            else if ($(element).attr("name") == "targetAudience") {
                $(element).closest("div").addClass('error');
				$(element).next("label.error").attr("aria-hidden",true);
            }
            else if ($(element).attr("name") == "operatingDuration") {
                $(element).closest("div").addClass('error');
				$(element).next("label.error").attr("aria-hidden",true);
            }
            else if ($(element).attr("name") == "previousSubmissionDate") {
                $(element).closest("div").parent("div").addClass('error');
				$(element).parent("div").parent().parent().find("label.error").attr("aria-hidden",true);
                $("#prevPermissionRequestOptions label").css("color","#bf0c0c");
            }
            else if ($(element).attr("name") == "requestSubmissionDate") {
                $(element).closest("div").addClass('error');
            }
            else if ($(element).attr("name") == "outcome") {
                $(element).closest("div").parent("div").parent("div").addClass('error');
				$(element).parent("div").parent().parent().find("label.error").attr("aria-hidden",true);
                $("#outcomeRequiredOptions label").css("color","#bf0c0c");
            }
            else if ($(element).attr("name") == "author") {
                $(element).closest("div").addClass('error');
				$(element).next("label.error").attr("aria-hidden",true);
            }
            else if ($(element).attr("name") == "publisher") {
                $(element).closest("div").addClass('error');
				$(element).next("label.error").attr("aria-hidden",true);
            }
            else if ($(element).attr("name") == "publicationTitle") {
                $(element).closest("div").addClass('error');
				$(element).next("label.error").attr("aria-hidden",true);
            }
            else if ($(element).attr("name") == "deviceType") {
                $(element).closest("div.form-group").addClass('error');
				$(element).next("label.error").attr("aria-hidden",true);
            }
            else if ($(element).attr("name") == "os") {
                $(element).closest("div.form-group").addClass('error');
				$(element).next("label.error").attr("aria-hidden",true);
			}
			else if ($(element).attr("name") == "appVersion") {
                 $(element).closest("div.form-group").addClass('error');
				$(element).next("label.error").attr("aria-hidden",true);
            }
            else if ($(element).attr("name") == "topic") {
                $(element).closest("div.form-group").addClass('error');
				$("#aboutOptionsValue-label").html(errorSymbol + $("#contactUs-topic-message").html());
				$("#contactUs-topic-input-error").attr("aria-hidden",true);
				$("#contactUs-topic-input-error").attr("tabindex","0");
            }
        },
        unhighlight: function(element) {
         
            if ($(element).attr("name") == "firstName") {
                $(element).closest("div").removeClass('error');
            }
            else if ($(element).attr("name") == "lastName") {
                $(element).closest("div").removeClass('error');
            }
            else if ($(element).attr("name") == "customerAddress1") {
                $(element).closest("div").removeClass('error');
            }
            else if ($(element).attr("name") == "city") {
                $(element).closest("div").removeClass('error');
            }
            else if ($(element).attr("name") == "state") {
               $(element).closest("div").parent("div").removeClass('error');
            }
            else if ($(element).attr("name") == "zip") {
                $(element).closest("div").removeClass('error');
            }
            else if ($(element).attr("name") == "responseText") {
                $(element).closest("div").parent("div").removeClass('error');
            }
            else if ($(element).attr("name") == "email") {
                $(element).closest("div").removeClass('error');
            }
            else if ($(element).attr("name") == "confirmEmail") {
                $(element).closest("div").removeClass('error');
            }
            else if ($(element).attr("name") == "phone") {
                $(element).closest("div").removeClass('error');
            }
            else if ($(element).attr("name") == "comments") {
                $(element).closest("div").removeClass('error');
            }
            else if ($(element).attr("name") == "restAddress") {
                $(element).closest("div").removeClass('error');
            }
            else if ($(element).attr("name") == "restCity") {
                $(element).closest("div").removeClass('error');
            }
            else if ($(element).attr("name") == "restState") {
                $(element).closest("div").parent("div").removeClass('error');
            }
            else if ($(element).attr("name") == "dayInput") {
                $("#day-select").removeClass('select-border');
            }
            else if ($(element).attr("name") == "monthInput") {
            	$("#month-select").removeClass('select-border');
            }
            else if ($(element).attr("name") == "yearInput") {
            	$("#year-select").removeClass('select-border');
            }
            else if ($(element).attr("name") == "dayInputTrade") {
                $("#day-select-trade").removeClass('select-border');
            }
            else if ($(element).attr("name") == "monthInputTrade") {
            	$("#month-select-trade").removeClass('select-border');
            }
            else if ($(element).attr("name") == "yearInputTrade") {
            	$("#year-select-trade").removeClass('select-border');
            }
            else if ($(element).attr("name") == "restLandmark") {
                $(element).closest("div").removeClass('error');
            }
            else if ($(element).attr("name") == "visitType") {
                $(element).closest("div").parent("div").removeClass('error');
            }
            else if ($(element).attr("name") == "incidentDate") {
                $(element).closest("div").removeClass('error');
            }
            else if ($(element).attr("name") == "usageType") {
                $(element).closest("div").removeClass('error');
            }
            else if ($(element).attr("name") == "targetAudience") {
                $(element).closest("div").removeClass('error');
            }
            else if ($(element).attr("name") == "operatingDuration") {
                $(element).closest("div").removeClass('error');
            }
            else if ($(element).attr("name") == "previousSubmissionDate") {
                $(element).closest("div").parent("div").removeClass('error');
                $("#prevPermissionRequestOptions label").css("color","");
            }
            else if ($(element).attr("name") == "requestSubmissionDate") {
                $(element).closest("div").removeClass('error');
            }
            else if ($(element).attr("name") == "outcome") {
                $(element).closest("div").parent("div").parent("div").removeClass('error');
                $("#outcomeRequiredOptions label").css("color","");
            }
            else if ($(element).attr("name") == "author") {
                $(element).closest("div").removeClass('error');
            }
            else if ($(element).attr("name") == "publisher") {
                $(element).closest("div").removeClass('error');
            }
            else if ($(element).attr("name") == "publicationTitle") {
                $(element).closest("div").removeClass('error');
            }else if ($(element).attr("name") == "deviceType") {
                $(element).closest("div.form-group").removeClass('error');
            }
            else if ($(element).attr("name") == "os") {
                $(element).closest("div.form-group").removeClass('error');
            }else if ($(element).attr("name") == "appVersion") {
                $(element).closest("div.form-group").removeClass('error');
            }
            else if ($(element).attr("name") == "topic") {
                $(element).closest("div.form-group").removeClass('error');
				$("#aboutOptionsValue-label").html($("#contactUs-topic-label").html());
            }
        },
       
        submitHandler: function (form) {
            console.log("check captcha");
            if(document.getElementById("captcha")){
            	var v = grecaptcha.getResponse();
        		if(v.length == 0){
        			$("#captchaError").show();
        			return false;
        		}
        		else{
        			$("#captchaError").hide();
        			$('#scancelModal').modal('show');
        			return false;
        		}
            }else{
            	if(document.getElementById("honeypotDiv")){
            		var email_hidn_norealuser = document.getElementById("email_hidn_norealuser").value;
            		if(email_hidn_norealuser == ""){
            			$("#captchaError").hide();
            			$('#scancelModal').modal('show');
            			return false;
            		}
            	}else{
            		$("#captchaError").hide();
            		$('#scancelModal').modal('show');
            		return false;
            	}
            }                    
       }
   });

}
$(document).ready(function() {
	
    if($("#contactUsFormName")) {
    	if($("#contactUsFormName").length > 0) {
    			var formName = $("#contactUsFormName").val();
    			if(formName.indexOf("gma") > -1){
    				$('.rowContainer').hide();
    				$('#contactUsRWD-followup').hide();
    			}
    			else if(formName.indexOf("general") > -1) {
    				$("#contactUsRWD-followup").hide();
    			}
    	}
    }

	var formURL;
	var newWindow;
    $("#main-form-select .dropdown-menu ul li").on('click', function(e){
		formURL = $("#main-form-select").find('select :nth-child('+(parseInt(e.currentTarget.getAttribute("data-original-index"))+1)+')').val();
		newWindow = $("#main-form-select").find('select :nth-child('+(parseInt(e.currentTarget.getAttribute("data-original-index"))+1)+')').attr("label");
	        if(formURL != "#"){
			$(".select-container #goButton").attr('disabled',false);
		}else{
			$(".select-container #goButton").attr('disabled',true);
		}
	});

	//Click go button
	$(".select-container #goButton").on('click', function(e){
	            if(newWindow == "true"){
	                window.open(formURL, "_blank");
	            }
	            else{
					window.open(formURL, "_self");
	            }
    });
    
    $("#mobileDevicedd .dropdown-menu ul li").on('click', function(e){
		$("#deviceOptionsValue-label").html("");
		$("#deviceOptionsValue-label").css("display","none");
		$("#deviceOptionsValue-label").closest("div").parent("div").removeClass('error');
	        $('#contactUs-mobileDeviceType-input').val($("#mobileDevicedd").find('select :nth-child('+(parseInt(e.currentTarget.getAttribute("data-original-index"))+1)+')').val());
	        e.preventDefault();
    });
    
    $('#operatingSystemdd .dropdown-menu ul li').on('click', function(e){
		$("#operatingSystemOptionsValue-label").html("");
		$("#operatingSystemOptionsValue-label").css("display","none");
		$("#operatingSystemOptionsValue-label").closest("div").parent("div").removeClass('error');
	        $('#contactUs-operatingSystem-input').val($("#operatingSystemdd").find('select :nth-child('+(parseInt(e.currentTarget.getAttribute("data-original-index"))+1)+')').val());
	        e.preventDefault();
    });
    
    $('#contactUs-state-select .dropdown-menu ul li').on('click', function(e){
		$("#stateValue-label").html("");
		$("#stateValue-label").css("display","none");
		$("#stateValue-label").closest("div").parent("div").removeClass('error');
	        $('#contactUs-state-input').val($("#contactUs-state-select").find('select :nth-child('+(parseInt(e.currentTarget.getAttribute("data-original-index"))+1)+')').val());
        e.preventDefault();
    });
    
    $('#aboutOptionsValueSelection .dropdown-menu ul li').on('click', function(e){
		$("#contactUs-topic-input-error").html("");
			$("#contactUs-topic-input-error").css("display","none");
		$("#aboutOptionsValue-label").html($("#contactUs-topic-label").html());
		$("#aboutOptionsValue-label").closest("div.form-group").removeClass('error');
			$('#contactUs-topic-input').val($("#aboutOptionsValueSelection").find('select :nth-child('+(parseInt(e.currentTarget.getAttribute("data-original-index"))+1)+')').val());
    	e.preventDefault();
    });

    $('#contactUs-resstate-select .dropdown-menu ul li').on('click', function(e){
		$("#contactUs-state-label").html("");
		$("#contactUs-state-label").css("display","none");
		$("#contactUs-state-label").closest("div").parent("div").removeClass('error');
	        $('#contactUs-resstate-input').val($("#contactUs-resstate-select").find('select :nth-child('+(parseInt(e.currentTarget.getAttribute("data-original-index"))+1)+')').val());
        e.preventDefault();
    });
    
    $('#contactUs-day-select .dropdown-menu ul li').on('click', function(e){
		$('#day-select-label').html("");
		$('#day-select-label').css("display","none");
			$('#day-select').removeClass("select-border");
			$('#contactUs-day-input').val($("#contactUs-day-select").find('select :nth-child('+(parseInt(e.currentTarget.getAttribute("data-original-index"))+1)+')').val());
			$("#resDate").val($('#contactUs-month-input').val()+"/"+$('#contactUs-day-input').val()+"/"+$('#contactUs-year-input').val());
	});
	
	$('#contactUs-month-select .dropdown-menu ul li').on('click', function(e){
		$('#month-select-label').html("");
		$('#month-select-label').css("display","none");
			$('#month-select').removeClass("select-border");
			$('#contactUs-month-input').val($("#contactUs-month-select").find('select :nth-child('+(parseInt(e.currentTarget.getAttribute("data-original-index"))+1)+')').val());
			$("#resDate").val($('#contactUs-month-input').val()+"/"+$('#contactUs-day-input').val()+"/"+$('#contactUs-year-input').val());
	});
	
	$('#contactUs-year-select .dropdown-menu ul li').on('click', function(e){
		$('#year-select-label').html("");
		$('#year-select-label').css("display","none");
			$('#year-select').removeClass("select-border");
			$('#contactUs-year-input').val($("#contactUs-year-select").find('select :nth-child('+(parseInt(e.currentTarget.getAttribute("data-original-index"))+1)+')').val());
			$("#resDate").val($('#contactUs-month-input').val()+"/"+$('#contactUs-day-input').val()+"/"+$('#contactUs-year-input').val());
	});
	
	$('#contactUs-day-select-trade .dropdown-menu ul li').on('click', function(e){
		$('#day-select-trade-label').html("");
		$('#day-select-trade-label').css("display","none");
			$('#day-select-trade').removeClass("select-border");
			$('#contactUs-day-input-trade').val($("#contactUs-day-select-trade").find('select :nth-child('+(parseInt(e.currentTarget.getAttribute("data-original-index"))+1)+')').val());
			$("#when-submitted").val($('#contactUs-month-input-trade').val()+"/"+$('#contactUs-day-input-trade').val()+"/"+$('#contactUs-year-input-trade').val());
	});
			
	$('#contactUs-month-select-trade .dropdown-menu ul li').on('click', function(e){
		$('#month-select-trade-label').html("");
		$('#month-select-trade-label').css("display","none");
			$('#month-select-trade').removeClass("select-border");
			$('#contactUs-month-input-trade').val($("#contactUs-month-select-trade").find('select :nth-child('+(parseInt(e.currentTarget.getAttribute("data-original-index"))+1)+')').val());
			$("#when-submitted").val($('#contactUs-month-input-trade').val()+"/"+$('#contactUs-day-input-trade').val()+"/"+$('#contactUs-year-input-trade').val());
	});
			
	$('#contactUs-year-select-trade .dropdown-menu ul li').on('click', function(e){
		$('#year-select-trade-label').html("");
		$('#year-select-trade-label').css("display","none");
			$('#year-select-trade').removeClass("select-border");
			$('#contactUs-year-input-trade').val($("#contactUs-year-select-trade").find('select :nth-child('+(parseInt(e.currentTarget.getAttribute("data-original-index"))+1)+')').val());
			$("#when-submitted").val($('#contactUs-month-input-trade').val()+"/"+$('#contactUs-day-input-trade').val()+"/"+$('#contactUs-year-input-trade').val());
	});

	if($('#enter-comments')){
		var maxLength = $("#characterCount").html();
        $('#enter-comments').keyup(updateCount);
        $('#enter-comments').keydown(updateCount);
        function updateCount() {
        	var cs = [maxLength- $(this).val().length];
        	$('#characterCount').text(cs);
        }
	}
	
	$.validator.addMethod("dateValidationCheckUS", function(value) {
		var isValidDate = false;
		var enteredDate = moment($("#when-submitted").val() ? $("#when-submitted").val() : $("#resDate").val(),'MM-DD-YYYY').isValid();
		if(enteredDate)	{
			var dateCheck = moment().format('MM-DD-YYYY');
			var minAllowedDate = moment().subtract(1, "months").startOf('month').format("MM-DD-YYYY");
			var actualDifferenceTime = moment(dateCheck, 'MM-DD-YYYY').diff(moment($("#when-submitted").val() ? $("#when-submitted").val() : $("#resDate").val(), 'MM-DD-YYYY'), 'hours');
			var allowedDifference = moment(dateCheck, 'MM-DD-YYYY').diff(moment(minAllowedDate, 'MM-DD-YYYY'), 'hours');
			if(actualDifferenceTime >= 0 && allowedDifference >= actualDifferenceTime) {
				isValidDate =  true;
			}
		}
		if(!isValidDate){
			$('#day-select').attr("aria-labelledby", "invalid-entered-date");
			$('#month-select').attr("aria-labelledby", "invalid-entered-date");
			$('#year-select').attr("aria-labelledby", "invalid-entered-date");
		}
		return isValidDate;
	});
	
	$.validator.addMethod("exceptionalDateValidationCheckUS", function(value) {
		var isValidDate = false;
		if($('#contactUs-day-input').val() === "" && $('#contactUs-month-input').val() === "" && $('#contactUs-year-input').val() === "") {
			isValidDate =  true;
		}
		else if ($('#contactUs-day-input').val() != "" && $('#contactUs-month-input').val() != "" && $('#contactUs-year-input').val() != "") {
			var enteredDate = moment($("#when-submitted").val() ? $("#when-submitted").val() : $("#resDate").val(),'MM-DD-YYYY').isValid();
			if(enteredDate)	{
				var dateCheck = moment().format('MM-DD-YYYY');
				var minAllowedDate = moment().subtract(1, "months").startOf('month').format("MM-DD-YYYY");
				var actualDifferenceTime = moment(dateCheck, 'MM-DD-YYYY').diff(moment($("#when-submitted").val() ? $("#when-submitted").val() : $("#resDate").val(), 'MM-DD-YYYY'), 'hours');
				var allowedDifference = moment(dateCheck, 'MM-DD-YYYY').diff(moment(minAllowedDate, 'MM-DD-YYYY'), 'hours');
				if(actualDifferenceTime >= 0 && allowedDifference >= actualDifferenceTime) {
					isValidDate =  true;
				}
			}
		}
		if(!isValidDate){
			$('#day-select').attr("aria-labelledby", "invalid-entered-date");
			$('#month-select').attr("aria-labelledby", "invalid-entered-date");
			$('#year-select').attr("aria-labelledby", "invalid-entered-date");
		}
		return isValidDate;
	});
	
	$("#contactFormSubmit").on('click', function(e) {

		console.log("modal cicked");

		if(customerIp != ""){

            $("#customerIp").val(customerIp);
        }
        var screenWidth = $(window).width();
        var mobileFlag = "N";
        if(screenWidth <= 768){
			mobileFlag = "Y";
        }
        $("#mobileFlag").val(mobileFlag);

        var socailValue = document.URL.substr(document.URL.lastIndexOf('/')+1).toLowerCase();
        if(socailValue == "facebook"){
			$("#social").val("Facebook");
        }
        else if(socailValue == "twitter"){
			$("#social").val("Twitter");
        }
        else if(socailValue == "gmaapp"){
			$("#social").val("GMA");
        }
        var formAction = $("#formAction").html();
        $("#contactus").attr('action', formAction);
   		$("#contactus").attr("method", "POST");
   		$("#contactus")[0].submit();
        $(this).attr("disabled","true");
        e.preventDefault();
        	
    });

     $("input[type=file]").on('change', function () {
		$( this ).each(function( index ) {
  			$(this).parent().siblings().find('div.form-group > input').val($(this).val());
		});
	});

});

$(document).on("click", "#selectRestaurant", function() {
	var parentId = $(this).parent("div").parent("div").attr("id");

    $("#restAddress").val(($("#"+parentId+" .restaurant-location__title").html()));
    $("#restCity").val(($("#"+parentId+" #resSearchCity").val()));
    var storeId = ($("#"+parentId+" #storeIdentifier").val());
    storeId= JSON.parse(storeId);
    var NatlStrNumber= "";
    for(var j=0;j<storeId.length;j++)
    {
        if(storeId[j].identifierType==='NatlStrNumber') {
             NatlStrNumber = storeId[j].identifierValue;
        }
    }
    $("#resNtlStrNumber").val(NatlStrNumber);
	/*$('.resStateList>button>span').text(($("#"+parentId+" #restaurantState").val()));*/
    /*$('.resStateList>input').val(($("#"+parentId+" #restaurantState").val()));*/

    $('#contactUs-resstate-input').val(($("#"+parentId+" #restaurantState").val()));
    $('.resStateList button span.filter-option').text(($("#"+parentId+" #restaurantState").val()));

    $("#contactUs-state-label.error").css("display","none");
    $("#contactUs-state-label.error").closest("div").parent("div").removeClass('error');

    $("#restAddress-error").css("display","none");
    $("#restCity-error").css("display","none");
    $("#restCity-error").closest("div").removeClass('error');
    $("#restAddress-error").closest("div").removeClass('error');

    $('#restaurant-feedback-form-container').addClass("in");
    $('#restaurantLocatorForm').addClass("in");
});

$("#modalCloseButton").on('click', function(e){
	e.preventDefault();
	setTimeout(function(){ 
	$("#continue-button").focus();
	}, 500);
});