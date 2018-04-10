ssoApp.controller('LoginForm', ['$scope', '$window', '$element', 'config', 'customer', 'temporaryNotificationCookie', 'RememberMe', '$timeout',
function($scope, $window, $element, configService, customer, tempNotification, rememberMe, $timeout) {
    var json = $element.data('json');
    var genericError = configService.get('errorGeneric') || 'Generic Error';

    $scope.backLinkText = configService.get('config').backLinkText;

    var rememberedUserData = rememberMe.get();

    $scope.loginFormData = {
        username: rememberedUserData && rememberedUserData.flag ? rememberedUserData.un : '',
        rememberMe: rememberedUserData && rememberedUserData.flag
    };
    $scope.view = 'login';
    $scope.preloader = false;
    $scope.forgotPWFormData = {};
    $scope.hasError = false;
    $scope.forgotError = false;
    $scope.errorMsg = genericError;
    $scope.email = '';
    $scope.emailRegEx = $element.data('regex') ? new RegExp($element.data('regex'), 'i') : '';

    $scope.redirectAfter = json.redirectAfterLoginHref;

    $scope.viewSwitch = function(view) {
        $scope.hasError = false;
        $scope.errorMsg = '';
        if ($scope.loginFormData.username && $scope.loginFormData.username.length) {
            $scope.email = $scope.loginFormData.username;
            $scope.forgotPWFormData.username = $scope.email;
        }
        $scope.view = view;
    };

    $scope.revalidate = function(form, fieldName) {
        form[fieldName].$setValidity('errorCb', true);
    };

    $scope.socialLoginCB = function (form) {
        $scope.hasError = false;
        $scope.forgotError = false;
        $scope.errorMsg = genericError;
        $scope.loginFormData = {
            username: rememberedUserData && rememberedUserData.flag ? rememberedUserData.un : '',
            rememberMe: rememberedUserData && rememberedUserData.flag,
            password: ''
        };
        form.$setPristine();
        form.$setUntouched();
        tempNotification.clear();
    };

    $scope.submitForm = function(form) {
        $scope.preloader = true;
        var data = $scope.loginFormData;
        $scope.email = data.username;

        customer.login({
            username: data.username,
            password: data.password,
            type: 'email'
        }).then(
            function(res) {
                if (!res.status) {
                    $scope.preloader = false;
                    $scope.hasError = true;
                    $scope.errorMsg = genericError;
                    invalidateFields('0', form);
                } else if (res.statusCode === '11011') {
                    $scope.preloader = false;
                    $scope.hasError = false;
                    $window.localStorage.setItem('mcd-sso-l', 'true');
                    tempNotification.set('userLoggedIn');

                    if (data.rememberMe) {
                        rememberMe.set(true, data.username);
                    } else {
                        rememberMe.clear();
                    }

                    $timeout(function() {
                        if (json.redirectAfterLoginHref) {
                            document.location.href = json.redirectAfterLoginHref;
                        } else {
                            window.location.reload();
                        }
                    }, 100);
                }
            },
            function(err) {
                $scope.preloader = false;
                if (err.statusCode === '11390') {
                    window.aemScrollTo();
                    $scope.viewSwitch('locked');
                } else if (err.statusCode === '11934' || err.statusCode === '11923') {
                    //11934 statusCode is when user needs verification after changing their email address
                    //11923 statusCode is returned when user is freshly registered, but did not verify their account yet

                    //the account has not been verified - show temp notification 'verificationNeeded'
                    //the temp notification should be clickable and when the user clicks on it, a new AccountVerificationInitiation
                    //web service call should be made ({{host}}/v2/customer/security/account/verification?type=email)
                    tempNotification.show('verificationNeeded');
                } else if (err.statusCode === '11928') {
                    $scope.hasError = true;
                    $scope.errorMsg = json['error' + err.statusCode] || genericError;
                } else {
                    $scope.hasError = true;
                    $scope.errorMsg = json['error' + err.statusCode] || genericError;
                    invalidateFields(err.statusCode, form);
                }
            }
        );
    };

    $scope.forgotPassword = function() {
        $scope.preloader = true;
        $scope.errorMsg = '';
        customer.initiateChangePassword({
            username: $scope.forgotPWFormData.username
        }).then(
            function(res) {
                $scope.preloader = false;
                if (res.statusCode && res.statusCode == '11011') {
                    if ($scope.email !== $scope.forgotPWFormData.username) {
                        $scope.email = $scope.forgotPWFormData.username;
                    }
                    $scope.view = 'forgot-password-submitted';
                }
            },
            function(err) {
                $scope.preloader = false;
                $scope.forgotError = true;
                if (err.statusCode && err.statusCode == '11540') {
                    //show error on the form
                    $scope.hasError = true;
                    $scope.errorMsg = json['error' + err.statusCode] || genericError;
                } else {
                    $scope.hasError = true;
                    $scope.errorMsg = genericError;
                }
            }
        );
    };

    function invalidateFields(error, form) {
        $scope.loginFormData.password = '';
        switch(error) {
            case '11210':
                // username or password incorrect
                form.password.$setValidity('errorCb', false);
                break;
            case '11211':
                // social login only
                form.username.$setValidity('errorCb', false);
                form.password.$setValidity('errorCb', false);
                break;
            case '11212':
                // email address not found
                form.username.$setValidity('errorCb', false);
                break;
            case '11213':
                // password for email incorrect
                form.password.$setValidity('errorCb', false);
                break;
            default:
                // fallback
                form.password.$setValidity('errorCb', false);
        }
    }
}]);


ssoApp.directive('watchChange', function() {
    return {
        scope: {
            onchange: '&watchChange'
        },
        link: function(scope, element, attrs) {
            element.on('input', function() {
                scope.$apply(function () {
                    scope.onchange();
                });
            });
        }
    };
});