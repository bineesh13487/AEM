	app.controller('myAccountController', ['$scope', '$q', '$http', 'getCoop', '$cookies', function($scope, $q, $http, getCoop, $cookies) {
		$scope.isLoggedIn = false;
		$scope.UserName;
		$scope.cookieDomain;



	 function sendStoreId(storeNaId){
						  $.ajax({
					type: 'GET',
					method: "GET",
					async:false,
					//url: 'http://gwc.localhost.com:3000/gwc/en/api/user/sign-out'
					url: $('#updateStoreLink').attr('data-domain')+storeNaId,
					//url: logoutUrl,
					crossDomain: true,
					xhrFields: {
						withCredentials: true
					},
					dataType: "json",
					success: function(response) {

						console.log("success in Store ID Store");
						}

						});
		}


		$scope.init = function(cookieValue,screen,ispopup) {
				   if((screen =='signin') && (ispopup == 'false') && ($cookies.get("ocfbli"))){
						window.location.href = "/content/us/en-us.html";
				   }else{
			$scope.checkUserIsLoggedIn();
			$scope.cookieDomain = cookieValue;
			var cookieemail = $cookies.get("semail");
			//   var cookiepass = $cookies.get("spassword");
			//  if(cookieemail  && cookiepass ){

		   


	   

					
				


			if (cookieemail) {
				$(".signinremember").attr("checked", true);
				$(".myaccremember").attr("checked", true);
				// $("#emailIdSignIn").val(cookieemail);
				//$("#passwdIdSignIn").val(cookiepass) ;
				// $(".emailId").val(cookieemail);
				// $(".passwdId").val(cookiepass) ;
				$scope.myaccountEmail = cookieemail;
				//  $scope.myaccountPassword=cookiepass;
				$scope.signinEmail = cookieemail;
				//  $scope.signinPassword=cookiepass;
			}
			$('.termsconditionsubmit').click(function() {

				if (!$('input[id="checkbox_accept_terms"]').prop('checked')) {
					return false;
				}
	$('input[id="checkbox_accept_terms"]').click();

				$.ajax({
					type: 'POST',
					method: "POST",
					async:false,
				   url: $('#updateTermsLink').attr('data-domain'),
				   crossDomain: true,
					xhrFields: {
						withCredentials: true
					},
					dataType: "json",
					success: function(response) {
					  

					  if (response.errors.length > 0 || (response.isUpdateTerms!=undefined && response.isUpdateTerms==false)) {
							// some exception thrown from webservice than show message some error occurred.
						   console.log('error while updating termscondition');
						} else {

							console.log("success LOGIN");
							ecpResponse = response;
							isECPLoginSuccess = ecpResponse.model.ECPLoginSuccess;
							isCCPLoginSuccess = ecpResponse.model.CCPLoginSuccess;

							if (!isECPLoginSuccess && !isCCPLoginSuccess) { //if Ecp and CCP login failed show error message
								if (ecpResponse.model.ResultCode === -1091) { //If User has registered and not activated the account

									$scope.hideSpinner(type, $this);
									if (type == "myaccount"){
									$($this).closest('.cont-form').find('.myaccountErrActivation').show();
									$($this).closest('.cont-form').find('.hasErrors').focus();
									}

									else{
									$('.errActivation').show();
									$('.hasErrors').focus();
									}

								} else if (ecpResponse.model.ResultCode === -1012) { //change password screen call
									// alert("calling changepassword screen");
									//$cookies.put('changepassvar1',emailId,{path:'/',domain:'.localhost.com'});
									//$cookies.put('changepassvar2',passwd,{path:'/',domain:'.localhost.com'});
									$cookies.put('cemail', emailId, {
										path: '/',
										domain: '.' + $scope.cookieDomain,
										secure:true
									});
									$scope.hideSpinner(type, $this);
									window.location.href = changePasswordUrl;
								} else {
									$scope.hideSpinner(type, $this);

									if (type == "myaccount"){
									$($this).closest('.cont-form').find('.myaccountErrLogin').show();
                                    $($this).closest('.cont-form').find('.hasErrors').focus();
									}else{
									$('.errLoginFailed').show();
									$('.hasErrors').focus();
                                }

								}
							} else if (!isECPLoginSuccess && isCCPLoginSuccess) { //If CCP sucess and ECP fail show update password screen
								//alert("Calling reset password screen");
								ccpProfileId = ecpResponse.model.CCPProfileID;
								//$cookies.put('ccpProfileData',ccpProfileId,{path:'/',domain:'.localhost.com'});
							/*	$cookies.put('ccpProfileData', ccpProfileId, {
									path: '/',
									domain: '.' + $scope.cookieDomain,
									secure:true
								}); */
								$scope.hideSpinner(type, $this);
								window.location.href = resetPasswordUrl;
								// alert("CCP Success and ECP Login Failed");
							} else if ((isECPLoginSuccess && !isCCPLoginSuccess) || (isECPLoginSuccess && isCCPLoginSuccess)) { //If ECP success and CCp fail show login  //If both ECP and CCP sucess show login
								// alert("ECP Success and CCP Login Failed");
								if (ecpResponse.model.authenticated) {
									//alert("user is authenticated");
									//$cookies.put('isUserLoggedIn',ecpResponse.model.authenticated ,{path: '/',domain:'.localhost.com'});
									//$cookies.put('UserName',ecpResponse.model.userName,{path: '/',domain:'.localhost.com'});
								  /*  $cookies.put('isUserLoggedIn', ecpResponse.model.authenticated, {
										path: '/',
										domain: '.' + $scope.cookieDomain
									});*/
									$cookies.put('UserName', ecpResponse.model.userName, {
										path: '/',
										domain: '.' + $scope.cookieDomain,
										secure:true
									});






										$scope.$apply(function() {
											$scope.isLoggedIn = true;
											$scope.UserName = ecpResponse.model.userName;

										});
										 var storeNaId =  $('.storeidstored').attr('val');
								 if(storeNaId.trim()!==''){
								 sendStoreId(storeNaId);
								 }

									//$scope.isLoggedIn=true;
									//$scope.UserName=ecpResponse.model.userName;

								}

							}

						}



	var parser = document.createElement('a');
						parser.href = $('#sign-inlink').attr('data-domain');
						var parserDomainName = '';
						if (parser.port) {
							parserDomainName = parser.protocol + "//" + parser.hostname + ":" + parser.port;
						} else {
							parserDomainName = parser.protocol + "//" + parser.hostname;
						}
	 window.open(parserDomainName+$('.component-logo').attr('href'),"_self");

										 return ;
					   /* if(document.referrer!=undefined && document.referrer!=''){
						window.open(document.referrer, "_self");
						}else {
							window.history.back();
						}*/
					}
				});


			});
	}
		};




		$scope.checkUserIsLoggedIn = function() {
			//check if user is logged In or Not by looking to cookies

				$scope.UserName = $cookies.get("UserName");
				var isUserName = ($scope.UserName!=undefined && $scope.UserName !='')?true:false;

		   // if ($cookies.get("isUserLoggedIn") && isUserName) {ocfbli
			 if ($cookies.get("ocfbli") ) {
			 //   $scope.isLoggedIn = $cookies.get("isUserLoggedIn");
				 $scope.isLoggedIn = true;
				$scope.UserName = $cookies.get("UserName");

			}
			//   $scope.isLoggedIn=false;
		};

		$scope.submitForm = function(value, type, event) {
			//check if user is logged In or Not
			//alert("value obtained is"+value);
			event.preventDefault();
			if (value == 'logout') {
				var logoutUrl;
				logoutUrl = $(".myAccountLogoutURL").data("logout-url");
				$.ajax({
					type: 'GET',
					method: "GET",
					//url: 'http://gwc.localhost.com:3000/gwc/en/api/user/sign-out'
					//url: 'http://offers-dev.mcdonalds.com/gwc/en/api/user/sign-in'
					url: logoutUrl,
					crossDomain: true,
					xhrFields: {
						withCredentials: true
					},
					dataType: "json",
					success: function(response) {

						if (typeof event == 'string' && event == 'nohome') {

						} else {
							var signInLink = $('#sign-inlink').attr('data-domain');
							window.open(signInLink + '.html', "_self")
						}

					},
					error: function(r) {
						console.log("error logout");
					}

				});

			}



			if (value == 'register') {
				var registerUrl;
				// alert("In register");
				if (type == "myaccount")
					registerUrl = $(".myAccountRegisterURL").data("registration-url");
				else if (type == "signin")
					registerUrl = $(".registerURL").data("registration-url");
				window.location.href = registerUrl;
			}


			if (value == 'updateProfile') {
				var updateProfileUrl = $(".myAccountUpdateprofileURL").data("update-url");
				// window.location.href="http://gwc.localhost.com:3000/gwc/en/account";
				window.location.href = updateProfileUrl;
			}
			if (value == 'forgotpassword') {
				var forgotPasswordUrl;
				// alert("In forgot password");
				if (type == "myaccount")
					forgotPasswordUrl = $(".myAccountForgotpassURL").data("forgotpass-url");
				else if (type == "signin")
					forgotPasswordUrl = $(".forgotpassURL").data("forgotpass-url");
				window.location.href = forgotPasswordUrl;
			}
			if (value == "sign") {
				// alert("inside sign IN ");
				var promises = [];
				var ecpResponse;
				var emailId;
				var passwd;
				var isCCPLoginSuccess = false;
				var isECPLoginSuccess = false;
				var signInUrl;
				var epsilonurl;
				var resetPasswordUrl;
				var changePasswordUrl;
				var ccpProfileId;
				var isMyAccountRememberMeChecked =false;
				if (type == "myaccount") {
					var $this = event.target;
					$($this).find('.icon-spinner').show();
					$($this).closest('.cont-form').find('.myaccountErrWrong').hide();
					emailId = $($this).closest('.cont-form').find('.emailId').val();
					passwd = $($this).closest('.cont-form').find('.passwdId').val();
					epsilonurl = $(".myAccountEpsilonURL").data("epsilon-url");
					isMyAccountRememberMeChecked = $($this).closest('.cont-form').find('.myaccremember').is(':checked');
					signInUrl = $(".myAccountSigninURL").data("sign-url");
					resetPasswordUrl = $(".myAccountResetPasswordURL").data("resetpassword-url");
					changePasswordUrl = $(".myAccountChangePasswordURL").data("changepassword-url");
				} else if (type == "signin") {
					$('.signinSpinner').show();
					$('.errSomeWentWrong').hide();

					emailId = $('.signin_form input[type="email"]').val();
					console.log('Email Id value :: > ' + emailId);
					//  $('#emailIdSignIn').val();
					passwd = $('.signin_form input[type="password"]').val();
					//$('#passwdIdSignIn').val();
					console.log('Password value :: > ' + passwd);
					epsilonurl = $(".epsilonURL").data("epsilon-url");
					signInUrl = $(".signinURL").data("sign-url");
					resetPasswordUrl = $(".resetPasswordURL").data("resetpassword-url");
					changePasswordUrl = $(".changePasswordURL").data("changepassword-url");
				}

				var newUser = {
					email: emailId,
					password: passwd,
					rememberMe: 'on'
				}
				window.newUser = newUser;

			  if (($(".signinremember").is(':checked')) || isMyAccountRememberMeChecked ) {
					$scope.setCookie("semail", emailId, 365);
					// $scope.setCookie("spassword",passwd,365);
				} else {
					$scope.setCookie("semail", "", 0);
					// $scope.setCookie("spassword","",0);
				}
				// Use AJAX to post the object to our adduser service
				$.ajax({
					type: 'POST',
					method: "POST",
					data: newUser,
					// url: 'http://gwc.localhost.com:3000/gwc/en/api/user/sign-in'
					//url: 'http://offers-dev.mcdonalds.com/gwc/en/api/user/sign-in'
					url: signInUrl,
					crossDomain: true,
					xhrFields: {
						withCredentials: true
					},
					dataType: "json",
					success: function(response) {

					  var parser = document.createElement('a');
						parser.href = $('#sign-inlink').attr('data-domain');
						var parserDomainName = '';
						if (parser.port) {
							parserDomainName = parser.protocol + "//" + parser.hostname + ":" + parser.port;
						} else {
							parserDomainName = parser.protocol + "//" + parser.hostname;
						}



						if (response.errors.length > 0) {
							// some exception thrown from webservice than show message some error occurred.
							$scope.showErrorMessage(type, $this);
						} else {
							var isExpired = false;


						   isExpired = response.model.isExpired;


							console.log("success LOGIN");
							ecpResponse = response;
							isECPLoginSuccess = ecpResponse.model.ECPLoginSuccess;
							isCCPLoginSuccess = ecpResponse.model.CCPLoginSuccess;

							if (!isECPLoginSuccess && !isCCPLoginSuccess) { //if Ecp and CCP login failed show error message
								if (ecpResponse.model.ResultCode === -1091) { //If User has registered and not activated the account
									//  alert("Customer is not activated");
									$scope.hideSpinner(type, $this);
									if (type == "myaccount"){
									$($this).closest('.cont-form').find('.myaccountErrActivation').show();
									$($this).closest('.cont-form').find('.hasErrors').focus();

									}


									else{
									$('.errActivation').show();
									$('.hasErrors').focus();
									}

								} else if (ecpResponse.model.ResultCode === -1012) { //change password screen call
									// alert("calling changepassword screen");
									//$cookies.put('changepassvar1',emailId,{path:'/',domain:'.localhost.com'});
									//$cookies.put('changepassvar2',passwd,{path:'/',domain:'.localhost.com'});
									$cookies.put('cemail', emailId, {
										path: '/',
										domain: '.' + $scope.cookieDomain,
										secure:true
									});
									$scope.hideSpinner(type, $this);
									window.location.href = changePasswordUrl;
								} else {
									$scope.hideSpinner(type, $this);
									if (type == "myaccount"){
									$($this).closest('.cont-form').find('.myaccountErrLogin').show();
									$($this).closest('.cont-form').find('.hasErrors').focus();

									}


									else{
									$('.errLoginFailed').show();
									$('.hasErrors').focus();

									}

								}
							} else if (!isECPLoginSuccess && isCCPLoginSuccess) { //If CCP sucess and ECP fail show update password screen
								//alert("Calling reset password screen");
								var replaceAll = function(string, omit, place, prevstring) {
									  if (prevstring && string === prevstring){
										return string;
										}
									  prevstring = string.replace(omit, place);
									  return replaceAll(prevstring, omit, place, string)
									}
								ccpProfileId = ecpResponse.model.CCPProfileID;
								lastAcceptanceDate = ecpResponse.model.lastAcceptanceDate;
								//$cookies.put('ccpProfileData',ccpProfileId,{path:'/',domain:'.localhost.com'});
							  /*  $cookies.put('ccpProfileData', ccpProfileId, {
									path: '/',
									domain: '.' + $scope.cookieDomain,
									secure:true
								});*/
								if(!ecpResponse.model.isRedirect){
									$('.ecpccpsyncerror').show();
									$scope.hideSpinner(type, $this);
									return;
								}
								lastAcceptanceDate = replaceAll(lastAcceptanceDate,".",":");
								 var d = new Date();
					d.setTime(d.getTime() + (2*24*60*60*1000));
					var expires = "expires=" + d.toGMTString();
					/* document.cookie ="ccpProfileData="+ccpProfileId+"; "+expires+";domain=."+ $scope.cookieDomain+";path=/"; */
					document.cookie ="lastAcceptanceDate="+lastAcceptanceDate+"; "+expires+";domain=."+ $scope.cookieDomain+";path=/";


								/*$cookies.put('lastAcceptanceDate', lastAcceptanceDate, {
									path: '/',
									domain: '.' + $scope.cookieDomain,
									secure:true
								});*/
								$scope.hideSpinner(type, $this);
								if(ecpResponse.model.isRedirect=true){
								window.location.href = resetPasswordUrl;
								} else{
								$('.ecpccpsyncerror').show();
								}
								// alert("CCP Success and ECP Login Failed");
							} else if ((isECPLoginSuccess && !isCCPLoginSuccess) || (isECPLoginSuccess && isCCPLoginSuccess)) { //If ECP success and CCp fail show login  //If both ECP and CCP sucess show login
								// alert("ECP Success and CCP Login Failed");
								if (ecpResponse.model.authenticated) {
									//alert("user is authenticated");
									//$cookies.put('isUserLoggedIn',ecpResponse.model.authenticated ,{path: '/',domain:'.localhost.com'});
									//$cookies.put('UserName',ecpResponse.model.userName,{path: '/',domain:'.localhost.com'});
								  /*  $cookies.put('isUserLoggedIn', ecpResponse.model.authenticated, {
										path: '/',
										domain: '.' + $scope.cookieDomain
									});*/


								  if (isExpired) {





												   window.open(parserDomainName+$('#termsAndConditionsPagePath').attr('data-domain')+'.html',"_self");

										 return ;
								  }

								 var storeNaId =  $('.storeidstored').attr('val');
								 if(storeNaId.trim()!==''){
								 sendStoreId(storeNaId);
								 }
								  $cookies.put('UserName', ecpResponse.model.userName, {
										path: '/',
										domain: '.' + $scope.cookieDomain,
										secure:true
									});

                                    if (type == "signin") {
										//window.location.reload();
										window.location.href = $(".redirectURL").data("redirect-url");
									}
									$scope.hideSpinner(type, $this);
									if (!isExpired) {
										/*$scope.$apply(function() {
											$scope.isLoggedIn = true;
											$scope.UserName = ecpResponse.model.userName;

										});*/
									}
									//$scope.isLoggedIn=true;
									//$scope.UserName=ecpResponse.model.userName;

									if($('.signInRedirectLink').attr('data-sign-redirectLink').indexOf("http")>-1){
										window.open($('.signInRedirectLink').attr('data-sign-redirectLink'),"_self");
									} else{
									 window.open(parserDomainName+$('.signInRedirectLink').attr('data-sign-redirectLink')+'.html',"_self");
									}

										 return ;
								}

							}

						}


					},
					error: function(r) {
						console.log("error LOGIN");
						$scope.showErrorMessage(type, $this);
						$($this).closest('.cont-form').find('.hasErrors').focus();
					}

				});

			}
		};

		$scope.validatesignin = function(type, event) {
			var $this = event.target;
			$('.errEmail').hide();
			$('.errPassword').hide();
			//  $('.myaccountErrEmail').hide();
			//$('.myaccountErrPassword').hide();
			$($this).closest('.cont-form').find('.myaccountErrEmail').hide();
			$($this).closest('.cont-form').find('.myaccountErrPassword').hide();
			$($this).closest('.cont-form').find('.emailId').closest('.row').removeClass('has-error');
			$($this).closest('.cont-form').find('.passwdId').closest('.row').removeClass('has-error');

			var v1 = true;
			var v2 = true;
			if (type == "myaccount") {
				emailId = $($this).closest('.cont-form').find('.emailId').val();
				passwd = $($this).closest('.cont-form').find('.passwdId').val();
				//  emailId =$('.emailId').val();
				// passwd = $('.passwdId').val();
			} else if (type == "signin") {
				emailId = $('#emailIdSignIn').val();
				passwd = $('#passwdIdSignIn').val();
			}
			if ($.trim(emailId) == '') {
				v1 = false;
				if (type == "myaccount") {
					//$('.myaccountErrEmail').show();
					$($this).closest('.cont-form').find('.myaccountErrEmail').show();
					// $('.emailId').closest('.row').addClass('has-error');
					$($this).closest('.cont-form').find('.emailId').closest('.row').addClass('has-error');
				} else {
					$('.errEmail').show();
				}

			}
			if ($.trim(passwd) == '') {
				v2 = false;
				if (type == "myaccount") {
					// $('.myaccountErrPassword').show();
					$($this).closest('.cont-form').find('.myaccountErrPassword').show();
					//   $('.passwdId').closest('.row').addClass('has-error');
					$($this).closest('.cont-form').find('.passwdId').closest('.row').addClass('has-error');
				} else {
					$('.errPassword').show();
				}

			}
			var isvalid = v1 && v2;
			//  alert("value of isValid is"+isvalid);
			return (isvalid);
		};


		$scope.setCookie = function(name, val, expiresIn) {
			var expiry = new Date();
			expiry.setDate(expiry.getDate() + expiresIn);
			//$cookies.put(name, val, {expires: expiry, path: '/',domain:'.localhost.com'});
			$cookies.put(name, val, {
				expires: expiry,
				path: '/',
				domain: '.' + $scope.cookieDomain,
				secure:true
			});
		};

		$scope.showErrorMessage = function(type, event) {

			if (type == "myaccount") {
				$(event).closest('.cont-form').find('.myaccountErrWrong').show();
				$(event).find('.icon-spinner').hide();
				$(event).closest('.cont-form').find('.hasErrors').focus();
			} else {
				$('.errSomeWentWrong').show();
				$('.signinSpinner').hide();
				$('.hasErrors').focus();
			}
		};

		$scope.hideSpinner = function(type, event) {
			if (type == "myaccount") {
				$(event).find('.icon-spinner').hide();
			} else {
				$('.signinSpinner').hide();
			}
		};

	  $scope.clearErrorMessages = function() {
					$('.myaccountErrWrong').hide();
					$('.myaccountErrLogin').hide();
					$('.myaccountErrActivation').hide();
					$('.ecpccpsyncerror').hide();


		};

	}]);


	$(window).load(function() {
		$('#signbox1 .btn-red').removeAttr("target");
		$('#signbox1 .btn-red').attr("data-toggle", "modal");
		$('#signbox1 .btn-red').attr("data-target", "#signInToAccount");
		i = 0;
        $('.modal').each(function(e){
        if(this.id=="signInToAccount"){
        	if(i){
        	$("#signInToAccount").remove();
        	}
        	i++;
        }

        });
	})

