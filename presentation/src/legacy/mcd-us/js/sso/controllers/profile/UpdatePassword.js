ssoApp.controller('UpdatePasswordController', ['$scope', 'customer', 'temporaryNotificationCookie', '$attrs', 'config', function($scope, customer, temporaryNotificationCookie, $attrs, configService) {

    var redirectUrl = $attrs.redirectAfterUpdatePassword;
    var genericError = configService.get('errorGeneric') || 'Generic Error';
    $scope.preloader = false;
    $scope.formName = 'UpdatePassword';
    $scope.session = {};
    $scope.ref = [];
    $scope.errorMsg = {
        msg: '',
        is: false
    };
    
    $scope.submitFn = function(session, ref) {
        $scope.preloader = true;
        var params = {
            username: '',
            oldPassword: session.currentPassword,
            newPassword: session.password,
            newPasswordConfirm: session.confirmPassword
        };

        var updateResponse = customer.changePassword(params);
        
        updateResponse.then(
            function (resp) {
                 $scope.preloader = false;
                if (resp.statusCode == "11011") {
                    temporaryNotificationCookie.show('passwordReset');
                    $scope.$parent.nextStep('settings');
                } else {
                    $scope.errorMsg.msg = genericError;
                    $scope.errorMsg.is = true;
                }
            },
            function (resp) {
                 $scope.preloader = false;
                $scope.errorMsg.msg = genericError;
                $scope.errorMsg.is = true;
            }
        );
    };
}]);