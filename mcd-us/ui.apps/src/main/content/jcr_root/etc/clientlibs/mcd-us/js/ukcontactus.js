var menuItemResult;
var menuItemResultsSearch;

$(document).on('keyup', '#contactUs-menuItem',function(){
	var resultsFound = 0;
	if($(this).val().length > 1 && menuItemResult){
		$("#search-result-menu-item").find(".item-details-line").remove();
		for(var count = 0; count < menuItemResult.length; count++){
			if(menuItemResult[count].toLowerCase().indexOf($(this).val().toLowerCase()) > -1){
				!$("#restaurantVisitRadio").hasClass("mt50")? $("#restaurantVisitRadio").addClass("mt50"): "";
				var element = $("#search-result-menu-item").find(".main-line").clone();
				element.find(".item-name").html(menuItemResult[count]);
				element.addClass("item-details-line");
				element.removeClass("main-line hide");
				element.appendTo($("#search-result-menu-item").find(".tt-menu"));
				resultsFound = resultsFound + 1;
			}
		}
		if(resultsFound === 0) {
			$('#search-result-menu-item').css("display","none");
			$("#restaurantVisitRadio").hasClass("mt50")? $("#restaurantVisitRadio").removeClass("mt50"): "";
		}
		else
			$('#search-result-menu-item').css("display","block");
	} else {
		$("#search-result-menu-item").find(".item-details-line").remove();
		$('#search-result-menu-item').css("display","none");
		$("#restaurantVisitRadio").hasClass("mt50")? $("#restaurantVisitRadio").removeClass("mt50"): "";
	}
});

$(document).on('click', '#search-result-menu-item ul li a',function(event){
	event.preventDefault();
	var res = $(this).text();
	$("#restaurantVisitRadio").hasClass("mt50")? $("#restaurantVisitRadio").removeClass("mt50"): "";
	$("#contactUs-menuItem").val(res);
	$('#search-result-menu-item').css("display","none");
	if($("#restaurant-feedback-form-container .menu-item-height.error").length > 0 )	{
		$("#restaurant-feedback-form-container .menu-item-height.error").removeClass("error");
		$("#restaurant-feedback-form-container .menu-item-height label.error").css("display","none");
	}
	$("#contactUs-menuItem").focus();
});

function validateUKForm(){
	var rules = new Object();
	if ($("#contactUs-day-select-uk").hasClass("yes")) {
		rules[$('#contactUs-day-input-uk').attr('name')] = {
			required : {
				depends : function() {
					if ($("input:radio[name='restaurantVisit']").is(":checked") && $('input:radio[name=restaurantVisit]:checked').val() == 'yes') {
						return true;
					}
				}
			}
		};
	}
	if ($("#contactUs-month-select-uk").hasClass("yes")) {
		rules[$('#contactUs-month-input-uk').attr('name')] = {
			required : {
				depends : function() {
					if ($("input:radio[name='restaurantVisit']").is(":checked") && $('input:radio[name=restaurantVisit]:checked').val() == 'yes') {
						return true;
					}
				}
			}
		};
	}
	if ($("#contactUs-year-select-uk").hasClass("yes")) {
		rules[$('#contactUs-year-input-uk').attr('name')] = {
			required : {
				depends : function() {
					if ($("input:radio[name='restaurantVisit']").is(":checked") && $('input:radio[name=restaurantVisit]:checked').val() == 'yes') {
						return true;
					}
				}
			}
		};
	}
	
	rules[$('#contactUs-hour-input-uk').attr('name')] = {
		required : {
			depends : function() {
				if ($("input:radio[name='restaurantVisit']").is(":checked") && $('input:radio[name=restaurantVisit]:checked').val() == 'yes' && $("#contactUs-hour-select-uk").hasClass("yes")) {
					return true;
				}
			}
		}
	};
	
	rules[$('#contactUs-minute-input-uk').attr('name')] = {
		required : {
			depends : function() {
				if ($("input:radio[name='restaurantVisit']").is(":checked") && $('input:radio[name=restaurantVisit]:checked').val() == 'yes' && $("#contactUs-minute-select-uk").hasClass("yes")) {
					return true;
				}
			}
		}
	};
	
	if ($("#visit-date-check-uk").hasClass("yes")) {
		rules[$('#visit-date-check-uk').attr('name')] = {
			required : {
				depends : function() {
					if ($("input:radio[name='restaurantVisit']").is(":checked") && $('input:radio[name=restaurantVisit]:checked').val() == 'yes' 
						&& $('#contactUs-day-input-uk').val() != "" && $('#contactUs-month-input-uk').val() != "" && $('#contactUs-year-input-uk').val() != "") {
						return true;
					}
				}
			}, dateValidationCheck :{
				depends : function() {
					if ($("input:radio[name='restaurantVisit']").is(":checked") && $('input:radio[name=restaurantVisit]:checked').val() == 'yes' 
						&& $('#contactUs-day-input-uk').val() != "" && $('#contactUs-month-input-uk').val() != "" && $('#contactUs-year-input-uk').val() != "") {
						return true;
					}
				}
			}
		};
	}
	
	if($("#contactUs-title-select").hasClass("yes")){
        rules[$('#contactUs-title-input').attr('name')] = { required: true };
    }
	if($("#contactUs-first-name").hasClass("yes")){
        rules[$('#contactUs-first-name').attr('name')] = { required: true };
    }
	if($("#contactUs-last-name").hasClass("yes")){
        rules[$('#contactUs-last-name').attr('name')] = { required: true, minlength: 2 };
    }
	if($("#contactUs-addess").hasClass("yes")){
        rules[$('#contactUs-addess').attr('name')] = { required: true };
    }
	rules[$('#contactUs-postcode').attr('name')] = { 
		required: {
        	depends : function() {
        		if ($("#contactUs-postcode").hasClass("yes")) {
        			return true;
        		}
        	}
		}, postalCodeCheck: true 
	};
	rules[$('#contactUs-email').attr('name')] = { 
		required: {
        	depends : function() {
        		if ($("#contactUs-email").hasClass("yes")) {
        			return true;
        		}
        	}
		}, emailValidationCheck: true 
	};
	rules[$('#contactUsuk-phone').attr('name')] = { 
		required: {
        	depends : function() {
        		if ($("#contactUsuk-phone").hasClass("yes")) {
        			return true;
        		}
        	}
        }, minlength: 11, phoneNumberCheck: true
    };
	if($("#enter-commentsuk").hasClass("yes")){
        rules[$('#enter-commentsuk').attr('name')] = { required: true };
    }
	rules[$('#contactUs-menuItem').attr('name')] = { 
		required: {
			depends : function() {
				if ($("#contactUs-menuItem").hasClass("yes")) {
					return true;
				}
			}
		}, menuItemsCheck: true 
	};
	if($("#restaurantVisitRadio").hasClass("yes")){
        rules["restaurantVisit"] = { required: true };
    }
	if($("#contactUs-tableServiceType").hasClass("yes")){
        rules["tableServiceType"] = {
    		required : {
    			depends : function() {
    				if ($("input:radio[name='restaurantVisit']").is(":checked") && $('input:radio[name=restaurantVisit]:checked').val() == 'yes' && $("#contactUs-tableServiceType").hasClass("existance-check")) {
    					return true;
    				}
    			}
    		}
    	};
    }
	if ($("#contactUs-visitType").hasClass("yes")) {
		rules["contactUsSelectRestaurantType"] = {
			required : {
				depends : function() {
					if ($("input:radio[name='restaurantVisit']").is(":checked") && $('input:radio[name=restaurantVisit]:checked').val() == 'yes' && $("#contactUs-visitType").hasClass("visitTypeAvailable")) {
						return true;
					}
				}
			}
		};
	}
	rules["clickButton"] = {
		required : {
			depends : function() {
				if ($("input:radio[name='restaurantVisit']").is(":checked") && $('input:radio[name=restaurantVisit]:checked').val() == 'yes') {
					return true;
				}
			}
		}
	};
	rules["visitTime"] = {
		customTimeValidationCheck : {
			depends : function() {
				if ($("input:radio[name='restaurantVisit']").is(":checked") && $('input:radio[name=restaurantVisit]:checked').val() == 'yes' && !$("#visit-time-check-uk").hasClass("yes")) {
					return true;
				}
			}
		}
	};
	var rulesObj = JSON.stringify(rules);
    $("#contactusuk").validate({
        ignore: "",
        rules: rules,
        messages: {
        	dateValidation : $("#visit-date-validation-message").html(),
        	clickButton : $("#restaurant-selection-way").html(),
        	tableServiceType : $("#contactus-radio-validation-message").html(),
        	contactUsSelectRestaurantType : $("#contactus-radio-validation-message").html(),
        	restaurantVisit : $("#contactus-radio-validation-message").html(),
        	dayInput : $("#contactus-drop-down-validation-message").html(),
        	monthInput : $("#contactus-drop-down-validation-message").html(),
        	yearInput : $("#contactus-drop-down-validation-message").html(),
        	titleInput: $("#contactUs-title-input-message").html(),
        	hourInput : $("#contactus-drop-down-validation-message").html(),
        	minuteInput : $("#contactus-drop-down-validation-message").html(),
    		firstName: $("#contactUs-first-name-message").html(),
            lastName: $("#contactUs-last-name-message").html(),
            address: $("#contactUs-address-message").html(),
            postcode: $("#contactUs-postcode-message").html(),
            email: $("#contactUs-email-message").html(),
            phone: $("#contactUs-phone-message").html(),
            comments:$("#enter-comments-message").html(),
            menuItem:$("#contactus-menuItem-message").html(),
            restName:$("#contactus-restaurantName-message").html(),
            incidentDate:$("#contactus-incidentDate-message").html(),
            visitTime:$("#visit-time-validation-message").html(),
        },
   
        highlight: function(element) {
        	if ($(element).attr("name") == "dateValidation") {
        		//$('#day-select').addClass("select-border");
            }
        	else if ($(element).attr("name") == "dayInput") {
        		$('#day-select-uk').addClass("select-border");
            }
        	else if ($(element).attr("name") == "monthInput") {
        		$('#month-select-uk').addClass("select-border");
            }
        	else if ($(element).attr("name") == "yearInput") {
        		$('#year-select-uk').addClass("select-border");
            }
        	else if ($(element).attr("name") == "hourInput") {
        		$('#hour-select-uk').addClass("select-border");
            }
        	else if ($(element).attr("name") == "minuteInput") {
        		$('#minute-select-uk').addClass("select-border");
            }
        	else if ($(element).attr("name") == "titleInput") {
        		$(element).closest("div").addClass('error');
        		$('#title-select').addClass("select-border");
            }
        	else if ($(element).attr("name") == "tableServiceType") {
                //$(element).closest("div").addClass('error');
            }
        	else if ($(element).attr("name") == "contactUsSelectRestaurantType") {
                //$(element).closest("div").addClass('error');
            }
        	else if ($(element).attr("name") == "restaurantVisit") {
                //$(element).closest("div").addClass('error');
            }
        	else if ($(element).attr("name") == "firstName") {
                $(element).closest("div").addClass('error');
            }
        	else if ($(element).attr("name") == "lastName") {
                $(element).closest("div").addClass('error');
            }
        	else if ($(element).attr("name") == "address") {
                $(element).closest("div").addClass('error');
            }
        	else if ($(element).attr("name") == "postcode") {
                $(element).closest("div").addClass('error');
            }
        	else if ($(element).attr("name") == "email") {
                $(element).closest("div").addClass('error');
            }
        	else if ($(element).attr("name") == "phone") {
                $(element).closest("div").addClass('error');
            }
        	else if ($(element).attr("name") == "comments") {
                $(element).closest("div").addClass('error');
            }
        	else if ($(element).attr("name") == "menuItem") {
                $(element).closest("div").addClass('error');
                $("#restaurant-feedback-form-container .menu-item-drop").removeClass("margin-without-error").addClass("margin-with-error");
            }
        },
        unhighlight: function(element) {
        	if ($(element).attr("name") == "dateValidation") {
        		//$('#day-select').removeClass("select-border");
            }
        	else if ($(element).attr("name") == "dayInput") {
        		$('#day-select-uk').removeClass("select-border");
            }
        	else if ($(element).attr("name") == "monthInput") {
        		$('#month-select-uk').removeClass("select-border");
            }
        	else if ($(element).attr("name") == "yearInput") {
        		$('#year-select-uk').removeClass("select-border");
            }
        	else if ($(element).attr("name") == "hourInput") {
        		$('#hour-select-uk').removeClass("select-border");
            }
        	else if ($(element).attr("name") == "minuteInput") {
        		$('#minute-select-uk').removeClass("select-border");
            }
        	else if ($(element).attr("name") == "titleInput") {
        		$(element).closest("div").removeClass('error');
        		$('#title-select').removeClass("select-border");
            }
        	else if ($(element).attr("name") == "tableServiceType") {
                //$(element).closest("div").removeClass('error');
            }
        	else if ($(element).attr("name") == "contactUsSelectRestaurantType") {
                //$(element).closest("div").removeClass('error');
            }
        	else if ($(element).attr("name") == "restaurantVisit") {
                //$(element).closest("div").removeClass('error');
            }
        	else if ($(element).attr("name") == "firstName") {
                $(element).closest("div").removeClass('error');
            }
        	 else if ($(element).attr("name") == "lastName") {
                 $(element).closest("div").removeClass('error');
             }
        	 else if ($(element).attr("name") == "address") {
                 $(element).closest("div").removeClass('error');
             }
         	else if ($(element).attr("name") == "postcode") {
                 $(element).closest("div").removeClass('error');
             }
         	else if ($(element).attr("name") == "email") {
                 $(element).closest("div").removeClass('error');
             }
         	else if ($(element).attr("name") == "phone") {
                 $(element).closest("div").removeClass('error');
             }
         	else if ($(element).attr("name") == "comments") {
                $(element).closest("div").removeClass('error');
            }
         	else if ($(element).attr("name") == "menuItem") {
                $(element).closest("div").removeClass('error');
                $("#restaurant-feedback-form-container .menu-item-drop").removeClass("margin-with-error").addClass("margin-without-error");
            }
        },
        submitHandler: function (form) {
	        if(document.getElementById("roboUserCheck")){
	    		var confirm_email_hidn_norealuser = document.getElementById("confirm_email_hidn_norealuser").value;
	    		if(confirm_email_hidn_norealuser == ""){
	    			submitForm();
	    			return false;
	    		}
	    	}else{
	    		submitForm();
	    		return false;
	    	}
        }
   });   
}

	$('#title-select').on('change', function(e){
		e.preventDefault();
		if(e.currentTarget.selectedIndex == 1){
		    $('#contactUs-title-input').val("");
		} else {
		    $('#title-select').removeClass("select-border");
		    $('#contactUs-title-input').val(e.currentTarget.value);
		    $('#contactUs-title-input-error').css("display","none");
		}
	});
	
	$('#day-select-uk').on('change', function(e){
		e.preventDefault();
		if(e.currentTarget.selectedIndex == 1){
		    $('#contactUs-day-input-uk').val("");
		} else {
			$('#day-select-uk').removeClass("select-border");
			$('#contactUs-day-input-uk').val(e.currentTarget.value);
			$('#contactUs-day-input-uk-error').css("display","none");
			$("#visit-date-check-uk").val($('#contactUs-day-input-uk').val() + "-" + $('#month-numeric-value-uk').val() + "-" + $('#contactUs-year-input-uk').val());
		}
	});
	
	$('#month-select-uk').on('change', function(e){
		e.preventDefault();
		if(e.currentTarget.selectedIndex == 1){
		    $('#contactUs-month-input-uk').val("");
		} else {
			$('#month-select-uk').removeClass("select-border");
			$('#contactUs-month-input-uk').val(e.currentTarget.value);
			$('#contactUs-month-input-uk-error').css("display","none");
			if(e.currentTarget.selectedIndex < 11){
				$("#month-numeric-value-uk").val("0"+(e.currentTarget.selectedIndex - 1));
			} else {
				$("#month-numeric-value-uk").val(e.currentTarget.selectedIndex - 1);
			}
			$("#visit-date-check-uk").val($('#contactUs-day-input-uk').val() + "-" + $('#month-numeric-value-uk').val() + "-" + $('#contactUs-year-input-uk').val());
		}
	});
	
	$('#year-select-uk').on('change', function(e){
		e.preventDefault();
		if(e.currentTarget.selectedIndex == 1){
		    $('#contactUs-year-input-uk').val("");
		} else {
			$('#year-select-uk').removeClass("select-border");
			$('#contactUs-year-input-uk').val(e.currentTarget.value);
			$('#contactUs-year-input-uk-error').css("display","none");
			$("#visit-date-check-uk").val($('#contactUs-day-input-uk').val() + "-" + $('#month-numeric-value-uk').val() + "-" + $('#contactUs-year-input-uk').val())
		}
	});
	
	$('#hour-select-uk').on('change', function(e){
		e.preventDefault();
		if(e.currentTarget.selectedIndex == 1){
		    $('#contactUs-hour-input-uk').val("");
		} else {
			$('#hour-select-uk').removeClass("select-border");
			var hoursPart = e.currentTarget.value.split(" ");
			if(hoursPart[1] && hoursPart[1] == "PM"){
				var hoursPM = parseInt(hoursPart[0]) + 12;
				$('#contactUs-hour-input-uk').val(hoursPM);
			} else {
				$('#contactUs-hour-input-uk').val(hoursPart[0]);
			}
			$('#contactUs-hour-input-uk-error').css("display","none");
		}
		$("#visit-time-check-uk").val($('#contactUs-hour-input-uk').val() + ":" + $('#contactUs-minute-input-uk').val());
	});
	
	$('#minute-select-uk').on('change', function(e){
		e.preventDefault();
		if(e.currentTarget.selectedIndex == 1){
		    $('#contactUs-minute-input-uk').val("");
		} else {
			$('#minute-select-uk').removeClass("select-border");
			$('#contactUs-minute-input-uk').val(e.currentTarget.value);
			$('#contactUs-minute-input-uk-error').css("display","none");
		}
		$("#visit-time-check-uk").val($('#contactUs-hour-input-uk').val() + ":" + $('#contactUs-minute-input-uk').val());
	});

	$.validator.addMethod("phoneNumberCheck", function(value) {
		var isValidPhoneNumber = false;
	    var phoneExp = new RegExp("^0[0-9]{10}$");
	    var phNo = value.trim().replace(/ /g,'');
	    if(phoneExp.test(phNo) || value === ""){
	    	isValidPhoneNumber = true;
	    }
	    return isValidPhoneNumber;
	});

	$.validator.addMethod("postalCodeCheck", function(value) {
		var isValidPostalCode = false;
	    var postalCodeRegEx = new RegExp("^([A-PR-UWYZ0-9][A-HK-Y0-9][AEHMNPRTVXY0-9]?[ABEHMNPRV-Y0-9]? {0,2}[0-9][ABD-HJLNP-UW-Z]{2}|GIR 0AA)$");
	    var postalCode = value.toUpperCase().trim();
	    if(postalCodeRegEx.test(postalCode) || value === ""){
	    	isValidPostalCode = true;
	    }
	    return isValidPostalCode;
	});
	
	$.validator.addMethod("dateValidationCheck", function(value) {
		var isValidDate = false;
		var enteredDate = moment($("#visit-date-check-uk").val(),'DD-MM-YYYY').isValid();
		if(enteredDate)	{
			var todayDate = new Date();
			var dateCheck = moment(todayDate).format('DD-MM-YYYY');
			var differenceTime = moment(dateCheck, 'DD-MM-YYYY').diff(moment($("#visit-date-check-uk").val(), 'DD-MM-YYYY'), 'hours');
			if(differenceTime >= 0) {
				isValidDate =  true;
			}
		}
		return isValidDate;
	});
	
	$.validator.addMethod("emailValidationCheck", function(value) {
		var isValidEmailAddress = false;
	    var emailAddressRegEx = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	    var enteredAddress = value.toUpperCase().trim();
	    if(emailAddressRegEx.test(enteredAddress) || value === ""){
	    	isValidEmailAddress = true;
	    }
	    return isValidEmailAddress;
	});
	
	$.validator.addMethod("menuItemsCheck", function(value) {
		var isValidMenuItem = false;
		$.each(menuItemResultsSearch, function( index, objectValue ) {
			if(objectValue === value.toUpperCase() || value === ""){
		    	isValidMenuItem = true;
		    }
		});
	    return isValidMenuItem;
	});
	
	$.validator.addMethod("customTimeValidationCheck", function(value) {
		var isValidTimeCondition = false;
	    if(($("#contactUs-hour-input-uk").val()!= "" && $("#contactUs-minute-input-uk").val()!= "") || ($("#contactUs-hour-input-uk").val() === "" && $("#contactUs-minute-input-uk").val() === "")){
	    	isValidTimeCondition = true;
	    }
	    return isValidTimeCondition;
	});
	
	$("#contactUs-restaurantvisitYes").on("click", function(){
		$(".form-group-uk").show();
	});
	
	$("#contactUs-restaurantvisitNo").on("click", function(){
		 $(".form-group-uk").hide();
	});
	
	$("#contactUs-uk-agecheck-yes").on("click", function(){
		$("#contact-form-main-uk").removeClass('hide');
		if(!$("#contact-form-underage-uk").hasClass('hide')){
			$("#contact-form-underage-uk").addClass('hide');
		}
		$.getJSON("/contactusukform/ukhome/customerService/mnu_item?menuItem=&callback=?",
			function (data) {
				menuItemResult=data;
				menuItemResultsSearch = data.map(function(value) {
				      return value.toUpperCase();
				});
		});
	});
	
	$("#contactUs-uk-agecheck-no").on("click", function(){
		 $("#contact-form-underage-uk").removeClass('hide');
		 if(!$("#contact-form-main-uk").hasClass('hide')){
			 $("#contact-form-main-uk").addClass('hide');
		 }
	});
	
    if($('#enter-commentsuk')){
        var maxLength = $("#characterCount").html();
        $('#enter-commentsuk').keyup(updateCount);
        $('#enter-commentsuk').keydown(updateCount);
        function updateCount() {
            var cs = [maxLength- $(this).val().length];
            $('#characterCount').text(cs);
        }
    }
    
    function submitForm(){
    	$("#contactFormSubmituk").attr("disabled", true);
    	$("#contactFormResetuk").attr("disabled", true);
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
        else {
			$("#social").val(socailValue);
		}
        var formAction = $("#formAction").html();
        $("#contactusuk").attr('action', formAction);
   		$("#contactusuk").attr("method", "POST");
   		$("#contactusuk")[0].submit();
    }

    var tableServiceFlag = false;    

$(document).on("click", "#selectRestaurantuk", function() {
	 $("#contactUs-visitType").hide();
	 $("#contactUs-driveThru").hide();
	 $("#contactUs-frontCounter").hide();
	 $("#contactUs-kioskOrdering").hide();
	 $("#contactUs-mobileOrdering").hide();
	 $("#contactUs-tableServiceType").hide();
	 
	 $('input[name="contactUsSelectRestaurantType"]').prop('checked', false);
	 $('input[name="tableServiceType"]').prop('checked', false);
	 
	 $("#clickButtonCheck").val("yes");
	 
	 if(!$("#contactUs-visitType").hasClass("visitTypeAvailable")) {
		$("#contactUs-visitType").removeClass("visitTypeAvailable");
	 }
	 if(!$("#contactUs-tableServiceType").hasClass("existance-check")) {
    	$("#contactUs-tableServiceType").removeClass("existance-check");
	 }
	 
	var parentId = $(this).parent("div").parent("div").attr("id");
	
    $("#restAddress").html(($("#"+parentId+" .restaurant-location__title").html()));
    $("#restaurantNameId").val($("#"+parentId+" .restaurant-location__title").html());
    var storeId = ($("#"+parentId+" #storeIdentifier").val());
    storeId= JSON.parse(storeId);
    var NatlStrNumber= "";
    var restaurantId= "";
    for(var j=0;j<storeId.length;j++)
    {
        if(storeId[j].identifierType==='NatlStrNumber') {
             NatlStrNumber = storeId[j].identifierValue;
        }
        if(storeId[j].identifierType==='ID') {
        	restaurantId = storeId[j].identifierValue;
       }
    }
    $("#resNtlStrNumber").val(NatlStrNumber);
    $("#restId").val(restaurantId);
    
    var restaurantFilter = ($("#"+parentId+" #restaurantFilterType").val());
    
    var splitFilter=restaurantFilter.replace("[","");
    splitFilter=splitFilter.replace("]","");
    splitFilter=splitFilter.split(",");
    tableServiceFlag = false;
    for(var i=0;i<splitFilter.length;i++)  	{
    	if(splitFilter[i].replace(/"/g, "")==="DRIVETHRU") {
        	$("#contactUs-visitType").show();
        	$("#contactUs-driveThru").show();
        	$("#contactUs-frontCounter").show();
        	if(!$("#contactUs-visitType").hasClass("visitTypeAvailable")) {
    			$("#contactUs-visitType").addClass("visitTypeAvailable");
    		}
        }
    	if(splitFilter[i].replace(/"/g, "")==="SELFORDERKIOSK") {
    		$("#contactUs-visitType").show();
    		$("#contactUs-kioskOrdering").show();
    		$("#contactUs-frontCounter").show();
    		if(!$("#contactUs-visitType").hasClass("visitTypeAvailable")) {
    			$("#contactUs-visitType").addClass("visitTypeAvailable");
    		}
    	}
    	if(splitFilter[i].replace(/"/g, "")==="MOBILEORDERS") {
    		$("#contactUs-visitType").show();
    		$("#contactUs-mobileOrdering").show();
    		$("#contactUs-frontCounter").show();
    		if(!$("#contactUs-visitType").hasClass("visitTypeAvailable")) {
    			$("#contactUs-visitType").addClass("visitTypeAvailable");
    		}
    	}
    	if(splitFilter[i].replace(/"/g, "")==="TABLESERVICE") {
    		tableServiceFlag = true;
    	}
    }
    $('#restaurant-feedback-form-container').addClass("in");
    $('#restaurantLocatorForm').addClass("in");
});


$("input[name='contactUsSelectRestaurantType']").on("click", function(event) {  
	if(tableServiceFlag && (event.currentTarget.value == "KioskOrdering" ||  event.currentTarget.value == "MobileOrdering")){
    		$("#contactUs-tableServiceType").show();
    		$("#contactUs-tableServiceType").addClass("existance-check");
	} else {
		if(!$("#contactUs-tableServiceType").hasClass("existance-check")) {
			$("#contactUs-tableServiceType").removeClass("existance-check");
    	}
		$("#contactUs-tableServiceType").hide();
    }
});