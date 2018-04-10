$(document).ready(function() {
    var baseUrl= window.location.origin;
    var imgsrc=$('#heroEmail').attr('value');
    $('#emailImagePath').val(window.location.origin + $('#heroEmail').val());
    var imglogo=$('#logoEmailImagepath').attr('value');
    $('#logoEmailImage').val(window.location.origin + $('#logoEmailImagepath').val());

        $("#main-arabiaform-select .dropdown-menu ul li").on('click', function(e){
        	if(e.currentTarget.getAttribute("data-original-index") != 0){
    			var formURL = $("#main-arabiaform-select").find('select :nth-child('+(parseInt(e.currentTarget.getAttribute("data-original-index"))+1)+')').val();
    	        var newWindow = $("#main-arabiaform-select").find('select :nth-child('+(parseInt(e.currentTarget.getAttribute("data-original-index"))+1)+')').attr("label");
    	        if(formURL != "#"){
    	            if(newWindow == "true"){
    	                window.open(formURL, "_blank");
    	            }
    	            else{
    					window.open(formURL, "_self");
    	            }
    	        }
        	}
        });
    
  /*  if($("#file1").length > 0){
	    $("#file1")[0].onchange = function () {
	           $("#contactusArabiaform").validate().element("#contactus-file-input-1");
	    };
    }
}*/
 /*   if($("#datepickerparty").length > 0){
	    $("#datepickerparty")[0].onchange = function () {
	           $("#contactusArabiaform").validate().element("#datepickerparty");
	    };
    }*/

});

function validateArabiaForm() {
    $('#restaurant-feedback-form-container').addClass("in");
    $('#restaurantLocatorForm').addClass("in");
    var rules = new Object();

    if ($("#contactUs-title-select") && $("#contactUs-title-select").hasClass("yes")) {
        rules[$('#contactUs-title-input').attr('name')] = {
            required: true
        };
    }


    if ($("#contactUs-firstName") && $("#contactUs-firstName").hasClass("yes")) {
        rules[$('#contactUs-firstName').attr('name')] = {
            required: true,
            NameCheck:true

                  };
    }

    if ($("#contactUs-middleName") && $("#contactUs-middleName").hasClass("yes")) {
        rules[$('#contactUs-middleName').attr('name')] = {
            required: true,
            NameCheck:true
        };
    }

    if ($("#contactUs-middleName") && $("#contactUs-middleName").hasClass("no")) {
         rules[$('#contactUs-middleName').attr('name')] = {
                NameCheck:true
        };
    }

    if ($("#contactUs-lastName") && $("#contactUs-lastName").hasClass("yes")) {
        rules[$('#contactUs-lastName').attr('name')] = {
            required: true,
            NameCheck:true
        };
    }

    if ($("#contactUs-email") && $("#contactUs-email").hasClass("yes")) {
        rules[$('#contactUs-email').attr('name')] = {
            required: true,
            email: true
        };
    }

    if ($("#contactUs-gender-select") && $("#contactUs-gender-select").hasClass("yes")) {
        rules[$('#contactUs-gender-input').attr('name')] = {
            required: true
        };
    }



    if ($("#contactUs-nationality-select") && $("#contactUs-nationality-select").hasClass("yes")) {
        rules[$('#contactUs-nationality-input').attr('name')] = {
            required: true
        };
    }

    if ($("#contactUs-maritalstatus-select") && $("#contactUs-maritalstatus-select").hasClass("yes")) {
        rules[$('#contactUs-maritalstatus-input').attr('name')] = {
            required: true
        };
    }


    if ($("#contactUs-prefcontact-select") && $("#contactUs-prefcontact-select").hasClass("yes")) {
        rules[$('#contactUs-prefcontact-input').attr('name')] = {
            required: true
        };
    }


    if ($("#contactUs-hearfromus-select") && $("#contactUs-hearfromus-select").hasClass("yes")) {
        rules[$('#contactUs-hearfromus-input').attr('name')] = {
            required: true
        };
    }


    if ($("#contactUs-childName") && $("#contactUs-childName").hasClass("yes")) {
        rules[$('#contactUs-childName').attr('name')] = {
            required: true
        };
    }

    if ($("#contactUs-noOfKids") && $("#contactUs-noOfKids").hasClass("yes")) {
        rules[$('#contactUs-noOfKids').attr('name')] = {
            required: true,
            digits: true,
            isValidnumber: true
        };
    }

     if ($("#contactUs-noOfKids") && $("#contactUs-noOfKids").hasClass("no")) {
        rules[$('#contactUs-noOfKids').attr('name')] = {
            isValidnumber: true
        };
    }
    if ($("#contactUs-dept-select") && $("#contactUs-dept-select").hasClass("yes")) {
        rules[$('#contactUs-dept-input').attr('name')] = {
            required: true
        };
    }

    if ($("#restAddress") && $("#restAddress").hasClass("yes")) {
        rules[$('#restAddress').attr('name')] = {
            required: true
        };
    }

    if ($("#contactUs-resstate-select") && $("#contactUs-resstate-select").hasClass("yes")) {
        rules[$('#contactUs-resstate-input').attr('name')] = {
            required: true
        };
    }

    if ($("#datepicker") && $("#datepicker").hasClass("yes")) {
        rules[$('#datepicker').attr('name')] = {
            required: true
        };
    }

    if ($("#datepicker") && $("#datepicker").hasClass("yes")) {
            rules[$('#datepicker').attr('name')] = {
                required: true
        };
    }

    if ($("#datepickerparty") && $("#datepickerparty").hasClass("yes")) {
        rules[$('#datepickerparty').attr('name')] = {
            required: true
        };
    }

    if ($("#dob") && $("#dob").hasClass("yes")) {
            rules[$('#dob').attr('name')] = {
                dateValidationCheckArabia: true
        };
    }

    if ($("#dob") && $("#dob").hasClass("no")) {
         rules[$('#dob').attr('name')] = {
               dateValidationNonMandateCheck: true
         };
    }
    
    if ($("#dateOfVisit") && $("#dateOfVisit").hasClass("yes")) {
        rules[$('#dateOfVisit').attr('name')] = {
            dateValidationCheckRest: true
        };
	}
	
	if ($("#dateOfVisit") && $("#dateOfVisit").hasClass("no")) {
	     rules[$('#dateOfVisit').attr('name')] = {
	           dateValidationNonMandateCheckRest: true
	     };
	}
	
	if ($("#dateOfParty") && $("#dateOfParty").hasClass("yes")) {
        rules[$('#dateOfParty').attr('name')] = {
            dateValidationCheckParty: true
        };
	}
	
	if ($("#dateOfParty") && $("#dateOfParty").hasClass("no")) {
	     rules[$('#dateOfParty').attr('name')] = {
	    		 dateValidationNonMandateCheckParty: true
	     };
	}

    if ($("#enter-coverLetterArabia") && $("#enter-coverLetterArabia").hasClass("yes")) {
        rules[$('#enter-coverLetterArabia').attr('name')] = {
            required: true
        };
    }

    if ($("#enter-commentsArabia") && $("#enter-commentsArabia").hasClass("yes")) {
        rules[$('#enter-commentsArabia').attr('name')] = {
            required: true
        };
    }

    if ($("#checkbox_termscond") && $("#checkbox_termscond").hasClass("yes")) {
        rules[$('#termscondVal').attr('name')] = {
            required: true
        };
    }

    if ($("#contactus-file-input-1") && $("#contactus-file-input-1").hasClass("yes")) {
        rules[$('#contactus-file-input-1').attr('name')] = {
            required: true,
			cvFileValidation: true
        };
    }
	
	if ($("#contactus-file-input-1") && $("#contactus-file-input-1").hasClass("no")) {
        rules[$('#contactus-file-input-1').attr('name')] = {
            cvFileValidation: true
        };
    }
	
	
    if ($("#contactUs-countryCode") && $("#contactUs-countryCode").hasClass("yes")) {
        rules[$('#contactUs-countryCode').attr('name')] = {
            required: true,
           digits: true,
           isValidnumber: true
        };
    }

   if ($("#contactUs-countryCode") && $("#contactUs-countryCode").hasClass("no")) {
        rules[$('#contactUs-countryCode').attr('name')] = {
           isValidnumber: true
        };
    }


    if ($("#contactUs-areaCode") && $("#contactUs-areaCode").hasClass("yes")) {
        rules[$('#contactUs-areaCode').attr('name')] = {
            required: true,
            digits: true,
            isValidnumber: true
        };
    }
    if ($("#contactUs-areaCode") && $("#contactUs-areaCode").hasClass("no")) {
        rules[$('#contactUs-areaCode').attr('name')] = {
            isValidnumber: true
        };
    }

    if ($("#contactUs-mobNum") && $("#contactUs-mobNum").hasClass("yes")) {
        rules[$('#contactUs-mobNum').attr('name')] = {
            required: true,
            digits: true,
            isValidnumber: true
        };
    }
    if ($("#contactUs-mobNum") && $("#contactUs-mobNum").hasClass("no")) {
        rules[$('#contactUs-mobNum').attr('name')] = {

            isValidnumber: true
        };
    }
    var rulesObj = JSON.stringify(rules);
    console.log("Rules JSON OBJ :: " + rulesObj);

    $("#contactusArabiaform").validate({
        ignore: "",
        rules: rules,
        messages: {
            fname: $("#contactUs-first-name-message").html(),
            Mname: $("#contactUs-middle-name-message").html(),
            lname: $("#contactUs-lastName-message").html(),
            Title: $("#contactUs-title-message").html(),
            email: $("#contactUs-email-message").html(),
            Gender: $("#contactUs-gender-message").html(),
            Nationality: $("#contactUs-nationality-message").html(),
            Marital_Status: $("#contactUs-maritalstatus-message").html(),
            Pref_Contact: $("#contactUs-prefcontact-message").html(),
            Hear_abt_us: $("#contactUs-hearfromus-message").html(),
            childName: $("#contactUs-child-name-message").html(),
            kidsAttending: $("#contactUs-nokids-message").html(),
            department: $("#contactUs-deptname-message").html(),
            storeAdd: $("#contactUs-resaddress-message").html(),
            storeCity: $("#contactUs-rescity-message").html(),
            dob: $("#contactUs-dob-message").html(),
            dateOfVisit: $("#contactUs-resdate-message").html(),            
            dateOfParty: $("#contactUs-dateofparty-message").html(),
            incidentDate  : $("#contactUs-resdate-message").html(),
            Feedback: $("#contactUs-comments-message").html(),
            applicantCoverLetter: $("#contactUs-cover-letter-message").html(),
            terms_conditions: $("#contactUs-terms-cond-message").html(),
            countryCode: $("#contactUs-country-code-message").html(),
            areaCode: $("#contactUs-area-code-message").html(),
            number: $("#contactUs-mob-number-message").html(),
            uploadedCvFileName: $("#contactUs-uplaodcv-message").html()
        },

        highlight: function(element) {

            if ($(element).attr("name") == "fname") {
                $(element).closest("div").addClass('error');
            } else if ($(element).attr("name") == "Mname") {
                $(element).closest("div").addClass('error');
            } else if ($(element).attr("name") == "lname") {
                $(element).closest("div").addClass('error');
            } else if ($(element).attr("name") == "Title") {
              //  $('#titleValue').addClass("select-border");
                 $(element).closest("div").parent("div").addClass('error');
            } else if ($(element).attr("name") == "email") {
                $(element).closest("div").addClass('error');
            } else if ($(element).attr("name") == "Hear_abt_us") {
              //  $('#hearfromusValue').addClass("select-border");
                 $(element).closest("div").parent("div").addClass('error');
            } else if ($(element).attr("name") == "Gender") {
               // $('#genderValue').addClass("select-border");
                 $(element).closest("div").parent("div").addClass('error');
            } else if ($(element).attr("name") == "Nationality") {
             $(element).closest("div").parent("div").addClass('error');
               // $('#nationalityValue').addClass("select-border");
            } else if ($(element).attr("name") == "Marital_Status") {
               // $('#maritalValue').addClass("select-border");
                 $(element).closest("div").parent("div").addClass('error');
            } else if ($(element).attr("name") == "Pref_Contact") {
               // $('#prefcontactValue').addClass("select-border");
                 $(element).closest("div").parent("div").addClass('error');
            } else if ($(element).attr("name") == "childName") {
                $(element).closest("div").addClass('error');
            } else if ($(element).attr("name") == "kidsAttending") {
                $(element).closest("div").addClass('error');
            } else if ($(element).attr("name") == "department") {
               // $('#deptValue').addClass("select-border");
                 $(element).closest("div").parent("div").addClass('error');
            } else if ($(element).attr("name") == "storeAdd") {
                $(element).closest("div").addClass('error');
            } else if ($(element).attr("name") == "storeCity") {
               // $('#resStateValue').addClass("select-border");
                 $(element).closest("div").parent("div").addClass('error');
            } else if ($(element).attr("name") == "dob") {
                 $(element).closest("div.form-group").addClass('error');
            } else if ($(element).attr("name") == "dateOfVisit") {
                $(element).closest("div.form-group").addClass('error');
            } else if ($(element).attr("name") == "dateOfParty") {
                $(element).closest("div.form-group").addClass('error');
            } else if ($(element).attr("name") == "incidentDate ") {
                $(element).closest("div").addClass('error');
            } else if ($(element).attr("name") == "Feedback") {
                $(element).closest("div").addClass('error');
            } else if ($(element).attr("name") == "applicantCoverLetter") {
                $(element).closest("div").addClass('error');
            } else if ($(element).attr("name") == "terms_conditions") {
                $(element).closest("div").addClass('error');
            }
            if ($(element).attr("name") == "uploadedCvFileName") {
                $(element).closest("div").addClass('error');
            } else if ($(element).attr("name") == "countryCode") {
                $(element).closest("div").addClass('error');
            } else if ($(element).attr("name") == "areaCode") {
                $(element).closest("div").addClass('error');
            } else if ($(element).attr("name") == "number") {
                $(element).closest("div").addClass('error');
            }

        },
        unhighlight: function(element) {

            if ($(element).attr("name") == "fname") {
                $(element).closest("div").removeClass('error');
            } else if ($(element).attr("name") == "Mname") {
                $(element).closest("div").removeClass('error');
            } else if ($(element).attr("name") == "lname") {
                $(element).closest("div").removeClass('error');
            } else if ($(element).attr("name") == "Title") {
              //  $('#titleValue').removeClass("select-border");
                 $(element).closest("div").parent("div").removeClass('error');
            } else if ($(element).attr("name") == "email") {
                $(element).closest("div").removeClass('error');
            } else if ($(element).attr("name") == "Hear_abt_us") {
              //  $('#hearfromusValue').removeClass("select-border");
                 $(element).closest("div").parent("div").removeClass('error');
            } else if ($(element).attr("name") == "Gender") {
                $('#genderValue').removeClass("select-border");
            } else if ($(element).attr("name") == "Nationality") {
             $(element).closest("div").parent("div").removeClass('error');
              //  $('#nationalityValue').removeClass("select-border");
            } else if ($(element).attr("name") == "Marital_Status") {
               // $('#maritalValue').removeClass("select-border");
                 $(element).closest("div").parent("div").removeClass('error');
            } else if ($(element).attr("name") == "Pref_Contact") {
              //  $('#prefcontactValue').removeClass("select-border");
                 $(element).closest("div").parent("div").removeClass('error');
            } else if ($(element).attr("name") == "childName") {
                $(element).closest("div").removeClass('error');
            } else if ($(element).attr("name") == "kidsAttending") {
                $(element).closest("div").removeClass('error');
            } else if ($(element).attr("name") == "department") {
               // $('#deptValue').removeClass("select-border");
                 $(element).closest("div").parent("div").removeClass('error');
            } else if ($(element).attr("name") == "storeAdd") {
                $(element).closest("div").removeClass('error');
            } else if ($(element).attr("name") == "storeCity") {
              //  $('#resStateValue').removeClass("select-border");
                 $(element).closest("div").parent("div").removeClass('error');
            } else if ($(element).attr("name") == "dob") {
                $(element).closest("div.form-group").removeClass('error');
            } else if ($(element).attr("name") == "dateOfVisit") {
                 $(element).closest("div.form-group").removeClass('error');
            }else if ($(element).attr("name") == "dateOfParty") {
                $(element).closest("div.form-group").removeClass('error');
            }else if ($(element).attr("name") == "incidentDate") {
                $(element).closest("div").removeClass('error');
            } else if ($(element).attr("name") == "Feedback") {
                $(element).closest("div").removeClass('error');
            } else if ($(element).attr("name") == "applicantCoverLetter") {
                $(element).closest("div").removeClass('error');
            } else if ($(element).attr("name") == "terms_conditions") {
                $(element).closest("div").removeClass('error');
            }
            if ($(element).attr("name") == "uploadedCvFileName") {
                $(element).closest("div").removeClass('error');
            } else if ($(element).attr("name") == "countryCode") {
                $(element).closest("div").removeClass('error');
            } else if ($(element).attr("name") == "areaCode") {
                $(element).closest("div").removeClass('error');
            } else if ($(element).attr("name") == "number") {
                $(element).closest("div").removeClass('error');
            }

        },
        submitHandler: function(form) {
            if (document.getElementById("confirmcustomermailid")) {
                var confirm_customer_email = document.getElementById("confirm_customer_email").value;
                if (confirm_customer_email == "") {
                    $('#scancelModal').modal('show');
                    return false;
                }
            } else {
                $('#scancelModal').modal('show');
                return false;
            }

            return false;
        }
    });


}

$('#titleValue').on('change', function(e) {
    e.preventDefault();
    if (e.currentTarget.selectedIndex == 0) {
        $('#contactUs-title-input').val("");
    } else {
       // $('#titleValue').removeClass("select-border");
        $("#contactUs-title-input-error").closest("div").parent("div").removeClass('error');
        $('#contactUs-title-input').val(e.currentTarget.value);
        $('#contactUs-title-input-error').css("display", "none");
    }
});


$('#genderValue').on('change', function(e) {
    e.preventDefault();
    if (e.currentTarget.selectedIndex == 0) {
        $('#contactUs-gender-input').val("");
    } else {
     //   $('#genderValue').removeClass("select-border");
            $("#contactUs-gender-input-error").closest("div").parent("div").removeClass('error');
        $('#contactUs-gender-input').val(e.currentTarget.value);
        $('#contactUs-gender-input-error').css("display", "none");
    }
});


$('#nationalityValue').on('change', function(e) {
    e.preventDefault();
    if (e.currentTarget.selectedIndex == 0) {
        $('#contactUs-nationality-input').val("");
    } else {
       // $('#nationalityValue').removeClass("select-border");
        $("#contactUs-nationality-input-error").closest("div").parent("div").removeClass('error');
        $('#contactUs-nationality-input').val(e.currentTarget.value);
        $('#contactUs-nationality-input-error').css("display", "none");
    }
});


$('#maritalValue').on('change', function(e) {
    e.preventDefault();
    if (e.currentTarget.selectedIndex == 0) {
        $('#contactUs-maritalstatus-input').val("");
    } else {
      //  $('#maritalValue').removeClass("select-border");
           $("#contactUs-maritalstatus-input-error").closest("div").parent("div").removeClass('error');
        $('#contactUs-maritalstatus-input').val(e.currentTarget.value);
        $('#contactUs-maritalstatus-input-error').css("display", "none");
    }
});


$('#prefcontactValue').on('change', function(e) {
    e.preventDefault();
    if (e.currentTarget.selectedIndex == 0) {
        $('#contactUs-prefcontact-input').val("");
    } else {
       // $('#prefcontactValue').removeClass("select-border");
        $("#contactUs-prefcontact-input-error").closest("div").parent("div").removeClass('error');
        $('#contactUs-prefcontact-input').val(e.currentTarget.value);
        $('#contactUs-prefcontact-input-error').css("display", "none");
    }
});


$('#hearfromusValue').on('change', function(e) {
    e.preventDefault();
    if (e.currentTarget.selectedIndex == 0) {
        $('#contactUs-hearfromus-input').val("");
    } else {
      //  $('#hearfromusValue').removeClass("select-border");
         $("#contactUs-hearfromus-input-error").closest("div").parent("div").removeClass('error');
        $('#contactUs-hearfromus-input').val(e.currentTarget.value);
        $('#contactUs-hearfromus-input-error').css("display", "none");
    }
});

$('#deptValue').on('change', function(e) {
    e.preventDefault();
    if (e.currentTarget.selectedIndex == 0) {
        $('#contactUs-dept-input').val("");
    } else {
       // $('#deptValue').removeClass("select-border");
        $("#contactUs-dept-input-error").closest("div").parent("div").removeClass('error');
        $('#contactUs-dept-input').val(e.currentTarget.value);
        $('#contactUs-dept-input-error').css("display", "none");
    }
});

$('#resStateValue').on('change', function(e) {
    e.preventDefault();
    if (e.currentTarget.selectedIndex == 0) {
        $('#contactUs-resstate-input').val("");
    } else {
        $("#contactUs-resstate-input-error").closest("div").parent("div").removeClass('error');
        $('#contactUs-resstate-input').val(e.currentTarget.value);
        $('#contactUs-resstate-input-error').css("display", "none");
    }
});



$('#checkbox_termscond').on('click', function(e) {
    if (($("#checkbox_termscond").is(':checked'))) {
        $('#termscondVal').val('Y');
        $('#termscondVal-error').css("display", "none");
    } else {
        $('#termscondVal').val('');
    }
});

$('#checkbox_optIn').on('click', function(e) {
    if (($("#checkbox_optIn").is(':checked'))) {
        $('#optInVal').val('Y');
    } else {
        $('#optInVal').val('N');
    }
});

if ($('#enter-commentsArabia')) {
    var maxLength = $("#characterCount").html();
    $('#enter-commentsArabia').keyup(updateCount);
    $('#enter-commentsArabia').keydown(updateCount);

    function updateCount() {
        var cs = [maxLength - $(this).val().length];
        $('#characterCount').text(cs);
    }

}

$("#contactArabiaFormSubmit").on('click', function(e) {
    e.preventDefault();
    var formAction = $("#formAction").html();
    $("#contactusArabiaform").attr('action', formAction);
    $("#contactusArabiaform").attr("method", "POST");
    $("#contactusArabiaform")[0].submit();
    $(this).attr("disabled", "true");
});
$.validator.addMethod("isValidnumber", function(value) {
	var isValidnum = false;
	var numberExp = new RegExp("^[0-9- ]*$");
	if(numberExp.test(value)){
		isValidnum = true;
	}
	return isValidnum;
});

$.validator.addMethod("NameCheck", function(value) {
		var isValidname = false;
	    var enExp = new RegExp("^[a-zA-Z0-9- ]*$");
    var arExp=new RegExp("^[\u0621-\u064A\u0660-\u0669 ]*$");
 var lang = $("#language").val();
    if(lang =="en"){
	    if(enExp.test(value)){
	    	isValidname = true;
        }}
    else if(lang =="ar"){
        if(arExp.test(value)){
            isValidname = true; }}

	    return isValidname;
});

    $.validator.addMethod("dateValidationCheckArabia", function(value) {
		var isValidDate = false;
		var day = $("#contactUs-day-input").val();
		var month = $("#contactUs-month-input").val();
		var year = $("#contactUs-year-input").val();
		var date = day+"/"+month+"/"+year;
		if(day==""||month==""||year==""){
            return false;
        }

	    var d = new Date(year, month, day);
        if (d.getFullYear() != year && d.getMonth() != month && d.getDate() != day) {
            return isValidDate;
        }
		var enteredDate = moment(date,'DD/MM/YYYY').isValid();
		if(enteredDate)	{
		    $("#dob").val(date);
			var todayDate = new Date();
			var dateCheck = moment(todayDate).format('DD/MM/YYYY');
			var differenceTime = moment(dateCheck, 'DD/MM/YYYY').diff(moment(date, 'DD/MM/YYYY'));
			if(differenceTime >= 0) {
				isValidDate =  true;
			}
		}
		return isValidDate;
    });

    $.validator.addMethod("dateValidationNonMandateCheck", function(value) {
    		var isValidDate = false;
    		var day = $("#contactUs-day-input").val();
    		var month = $("#contactUs-month-input").val();
    		var year = $("#contactUs-year-input").val();
    		var date = day+"/"+month+"/"+year;

    		if(day==""&&month==""&&year==""){
    		    return true;
    		}
    		else if(day==""||month==""||year==""){
                return false;
            }

    	    var d = new Date(year, month, day);
            if (d.getFullYear() != year && d.getMonth() != month && d.getDate() != day) {
                return isValidDate;
            }
    		var enteredDate = moment(date,'DD/MM/YYYY').isValid();
    		if(enteredDate)	{
    		    $("#dob").val(date);
    			var todayDate = new Date();
    			var dateCheck = moment(todayDate).format('DD/MM/YYYY');
    			var differenceTime = moment(dateCheck, 'DD/MM/YYYY').diff(moment(date, 'DD/MM/YYYY'));
    			if(differenceTime >= 0) {
    				isValidDate =  true;
    			}
    		}
    		return isValidDate;
        });
    
    //Restaurant feedback date validation -  mandatory
    $.validator.addMethod("dateValidationCheckRest", function(value) {
		var isValidDate = false;
		var day = $("#contactUs-restday-input").val();
		var month = $("#contactUs-restmonth-input").val();
		var year = $("#contactUs-restyear-input").val();
		var date = day+"/"+month+"/"+year;
		
		if(day==""||month==""||year==""){
            return false;
        }

	    var d = new Date(year, month, day);
        if (d.getFullYear() != year && d.getMonth() != month && d.getDate() != day) {
            return isValidDate;
        }
		var enteredDate = moment(date,'DD/MM/YYYY').isValid();
		if(enteredDate)	{
		    $("#dateOfVisit").val(date);
			var todayDate = new Date();
			var dateCheck = moment(todayDate).format('DD/MM/YYYY');
			var differenceTime = moment(dateCheck, 'DD/MM/YYYY').diff(moment(date, 'DD/MM/YYYY'));
			if(differenceTime >= 0) {
				isValidDate =  true;
			}
		}
		return isValidDate;
    });

    //Restaurant feedback date validation - non mandatory
    $.validator.addMethod("dateValidationNonMandateCheckRest", function(value) {
    		var isValidDate = false;
    		var day = $("#contactUs-restday-input").val();
    		var month = $("#contactUs-restmonth-input").val();
    		var year = $("#contactUs-restyear-input").val();
    		var date = day+"/"+month+"/"+year;
    		
    		if(day==""&&month==""&&year==""){
    		    return true;
    		}
    		else if(day==""||month==""||year==""){
                return false;
            }

    	    var d = new Date(year, month, day);
            if (d.getFullYear() != year && d.getMonth() != month && d.getDate() != day) {
                return isValidDate;
            }
    		var enteredDate = moment(date,'DD/MM/YYYY').isValid();
    		if(enteredDate)	{
    		    $("#dateOfVisit").val(date);
    			var todayDate = new Date();
    			var dateCheck = moment(todayDate).format('DD/MM/YYYY');
    			var differenceTime = moment(dateCheck, 'DD/MM/YYYY').diff(moment(date, 'DD/MM/YYYY'));
    			if(differenceTime >= 0) {
    				isValidDate =  true;
    			}
    		}
    		return isValidDate;
        });
    
    
  //Restaurant Feedback - on change 
    $('#arabia-restday-select').on('change', function(e){
		e.preventDefault();
		if(e.currentTarget.selectedIndex == 1){
		    $('#contactUs-restday-input').val("");
		} else {
			//$('#arabia-restday-select').closest("div.form-group").removeClass("error");
			$('#contactUs-restday-input').val(e.currentTarget.value);
			$('#contactUs-restday-input-error').css("display","none");
			//$("#dob").val($('#contactUs-restday-input').val() + "/" + $('#month-numeric-value').val() + "/" + $('#contactUs-year-input').val());
		}
	});

	$('#arabia-restmonth-select').on('change', function(e){
		e.preventDefault();
		if(e.currentTarget.selectedIndex == 1){
		    $('#contactUs-restmonth-input').val("");
		} else {
			
			$('#contactUs-restmonth-input').val(e.currentTarget.value);
			$('#contactUs-restmonth-input-error').css("display","none");
			//$("#dob").val($('#contactUs-day-input').val() + "/" + $('#month-numeric-value').val() + "/" + $('#contactUs-year-input').val());
		}
	});

	$('#arabia-restyear-select').on('change', function(e){
		e.preventDefault();
		if(e.currentTarget.selectedIndex == 1){
		    $('#contactUs-restyear-input').val("");
		} else {
			
			$('#contactUs-restyear-input').val(e.currentTarget.value);
			$('#contactUs-restyear-input-error').css("display","none");
			//$("#dob").val($('#contactUs-day-input').val() + "/" + $('#month-numeric-value').val() + "/" + $('#contactUs-year-input').val())
		}
	});
    
    
	//Personalinfo - on change 
    $('#arabia-day-select').on('change', function(e){
		e.preventDefault();
		if(e.currentTarget.selectedIndex == 1){
		    $('#contactUs-day-input').val("");
		} else {
			$('#arabia-day-select').closest("div.form-group").removeClass("error");
			$('#contactUs-day-input').val(e.currentTarget.value);
			$('#contactUs-day-input-error').css("display","none");
			$("#dob").val($('#contactUs-day-input').val() + "/" + $('#month-numeric-value').val() + "/" + $('#contactUs-year-input').val());
		}
	});

	$('#arabia-month-select').on('change', function(e){
		e.preventDefault();
		if(e.currentTarget.selectedIndex == 1){
		    $('#contactUs-month-input').val("");
		} else {
			$('#arabia-month-select').closest("div.form-group").removeClass("error");
			$('#contactUs-month-input').val(e.currentTarget.value);
			$('#contactUs-month-input-error').css("display","none");
			$("#dob").val($('#contactUs-day-input').val() + "/" + $('#month-numeric-value').val() + "/" + $('#contactUs-year-input').val());
		}
	});

	$('#arabia-year-select').on('change', function(e){
		e.preventDefault();
		if(e.currentTarget.selectedIndex == 1){
		    $('#contactUs-year-input').val("");
		} else {
			$('#arabia-year-select').closest("div.form-group").removeClass("error");
			$('#contactUs-year-input').val(e.currentTarget.value);
			$('#contactUs-year-input-error').css("display","none");
			$("#dob").val($('#contactUs-day-input').val() + "/" + $('#month-numeric-value').val() + "/" + $('#contactUs-year-input').val())
		}
	});
	
    
    //Celebration - on change
	 $('#arabia-partyday-select').on('change', function(e){
			e.preventDefault();
			if(e.currentTarget.selectedIndex == 1){
			    $('#contactUs-partyday-input').val("");
			} else {
				
				$('#contactUs-partyday-input').val(e.currentTarget.value);
				$('#contactUs-partyday-input-error').css("display","none");
				//$("#dob").val($('#contactUs-day-input').val() + "/" + $('#month-numeric-value').val() + "/" + $('#contactUs-year-input').val());
			}
		});

		$('#arabia-partymonth-select').on('change', function(e){
			e.preventDefault();
			if(e.currentTarget.selectedIndex == 1){
			    $('#contactUs-partymonth-input').val("");
			} else {
				
				$('#contactUs-partymonth-input').val(e.currentTarget.value);
				$('#contactUs-partymonth-input-error').css("display","none");
				//$("#dob").val($('#contactUs-day-input').val() + "/" + $('#month-numeric-value').val() + "/" + $('#contactUs-year-input').val());
			}
		});

		$('#arabia-partyyear-select').on('change', function(e){
			e.preventDefault();
			if(e.currentTarget.selectedIndex == 1){
			    $('#contactUs-partyyear-input').val("");
			} else {
				
				$('#contactUs-partyyear-input').val(e.currentTarget.value);
				$('#contactUs-partyyear-input-error').css("display","none");
				//$("#dob").val($('#contactUs-day-input').val() + "/" + $('#month-numeric-value').val() + "/" + $('#contactUs-year-input').val())
			}
		});
    
    
    
		//Celebration date validation - mandatory
	    $.validator.addMethod("dateValidationCheckParty", function(value) {
			var isValidDate = false;
			var day = $("#contactUs-partyday-input").val();
			var month = $("#contactUs-partymonth-input").val();
			var year = $("#contactUs-partyyear-input").val();
			var date = day+"/"+month+"/"+year;
			
			if(day==""||month==""||year==""){
	            return false;
	        }

		    var d = new Date(year, month, day);
	        if (d.getFullYear() != year && d.getMonth() != month && d.getDate() != day) {
	            return isValidDate;
	        }
			var enteredDate = moment(date,'DD/MM/YYYY').isValid();
			if(enteredDate)	{
			    $("#dateOfParty").val(date);
				var todayDate = new Date();
				var dateCheck = moment(todayDate).format('DD/MM/YYYY');
				var differenceTime = moment(dateCheck, 'DD/MM/YYYY').diff(moment(date, 'DD/MM/YYYY'));
				
				if(differenceTime < 0) {
					isValidDate =  true;
				}
			}
			return isValidDate;
	    });

	    //Celebration date validation - non mandatory
	    $.validator.addMethod("dateValidationNonMandateCheckParty", function(value) {
	    		var isValidDate = false;
	    		var day = $("#contactUs-partyday-input").val();
	    		var month = $("#contactUs-partymonth-input").val();
	    		var year = $("#contactUs-partyyear-input").val();
	    		var date = day+"/"+month+"/"+year;
	    		
	    		if(day==""&&month==""&&year==""){
	    		    return true;
	    		}
	    		else if(day==""||month==""||year==""){
	                return false;
	            }

	    	    var d = new Date(year, month, day);
	            if (d.getFullYear() != year && d.getMonth() != month && d.getDate() != day) {
	                return isValidDate;
	            }
	    		var enteredDate = moment(date,'DD/MM/YYYY').isValid();
	    		if(enteredDate)	{
	    		    $("#dateOfParty").val(date);
	    			var todayDate = new Date();
	    			var dateCheck = moment(todayDate).format('DD/MM/YYYY');
	    			var differenceTime = moment(dateCheck, 'DD/MM/YYYY').diff(moment(date, 'DD/MM/YYYY'));
	    			
	    			if(differenceTime < 0) {
	    				isValidDate =  true;
	    			}
	    		}
	    		return isValidDate;
	        });
    
    
    

$(document).on("click", "#selectRestaurantArabia", function() {
	var parentId = $(this).parent("div").parent("div").attr("id");

    $("#restAddress").val(($("#"+parentId+" .restaurant-location__title").html()));

    $('#contactUs-resstate-input').val(($("#"+parentId+" #resSearchCity").val()));
    $('.resStateList button span.filter-option').text(($("#"+parentId+" #resSearchCity").val()));

    $("#contactUs-resstate-input-error").css("display","none");
    $("#contactUs-resstate-input-error").closest("div").parent("div").removeClass('error');

    $("#restAddress-error").css("display","none");
    $("#restAddress-error").closest("div").removeClass('error');

    $('#restaurant-feedback-form-container').addClass("in");
    $('#restaurantLocatorForm').addClass("in");
});

$.validator.addMethod("cvFileValidation", function () {

    //var appCvValidation = "Please provide CV in one of the following format: .doc, .docx or .pdf";
    //var appCvSize = "CV cannot be more than 1Mb ";
    var flag = false;
    var sizeFlag = false;
    var extensions = new Array("doc", "docx", "pdf");
    var cv_file = $("#contactus-file-input-1").val();
    if(cv_file!=""&&cv_file!="undefined"){
        var cv_length = cv_file.length;
                    var pos = cv_file.lastIndexOf('.') + 1;
                    var ext = cv_file.substring(pos, cv_length);
                    var final_ext = ext.toLowerCase();
                    for (i = 0; i < extensions.length; i++) {
                        if (extensions[i] == final_ext) {
                            flag = true;
                        }
                    }
            
            if (!flag) {
                 //alert(appCvValidation);
                  return false;
            }
            else if (($("#file1")[0].files[0].size) > 1048576){
                //alert(appCvSize);
                return false;
            }
            else{
                return true;
            }
    }
	else{
		return true;
	}
});