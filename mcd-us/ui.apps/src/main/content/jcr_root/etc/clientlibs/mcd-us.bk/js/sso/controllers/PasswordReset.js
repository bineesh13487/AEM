ssoApp.controller('PasswordResetController', [
  '$scope', 'customer', 'temporaryNotificationCookie', '$attrs', 'config',
  function($scope, customer, temporaryNotificationCookie, $attrs, configService) {

    var redirectUrl = $attrs.redirect || "/";
    // var jsonObject = $attrs.json || {};
    // var json = JSON.parse(jsonObject);
    var genericError = configService.get('errorGeneric') || 'Generic Error';

    $scope.preloader = false;

    $scope.formName = 'ResetForm';
    $scope.session = {};
    $scope.ref = [];
    $scope.verificationCode = "";
    $scope.email = "";

    var actual_queryString;
    var temp_queryString;
    var queryString = {};
    if (window.location.search.length > 1) {
        actual_queryString = window.location.search.substr(1).split('&');
        for (var i = 0; i < actual_queryString.length; i++) {
            temp_queryString = actual_queryString[i].split('=');
            queryString[temp_queryString[0]] = temp_queryString[1];
        }
    }

    $scope.haveCredentials = false;
    if (queryString.c && queryString.u) {
        $scope.haveCredentials = true;
        $scope.verificationCode = queryString.c;
        $scope.email = queryString.u;
    }

    $scope.passwordResetError = {
        msg: '',
        is: false
    };

    $scope.submitFn = function(session, ref) {

        $scope.preloader = true;
        var params = {
            username: $scope.email,
            verificationCode: $scope.verificationCode,
            password: session.password,
            confirmPassword: session.confirmPassword
        };

        var resetResponse = customer.changePassword(params);

        resetResponse.then(
            function (resp) {
                $scope.preloader = false;
                if (resp.statusCode == '11011') {
                    document.location = redirectUrl;
                    temporaryNotificationCookie.set('passwordReset');
                }
            },
            function (err) {
                $scope.preloader = false;
                /*if (err.statusCode == '11935') {
                   $scope.errorMsg = json['error' + err.statusCode] || genericError;
                   $scope.passwordResetError.msg =  $scope.errorMsg;
                   $scope.passwordResetError.is = true;
                } else */ if (err.statusCode == '11920' || err.statusCode == '11922') {
                   temporaryNotificationCookie.show('verificationCodeExpired');
                } else {
                   $scope.errorMsg = genericError;
                   $scope.passwordResetError.msg =  $scope.errorMsg;
                   $scope.passwordResetError.is = true;
                }
            }
        );
    };

}]);