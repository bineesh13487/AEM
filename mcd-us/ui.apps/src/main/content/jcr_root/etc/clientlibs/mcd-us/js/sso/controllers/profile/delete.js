ssoApp.controller('DeleteAccountController', ['$scope', 'customer', 'temporaryNotificationCookie', '$attrs', '$element', 'config', function($scope, customer, temporaryNotificationCookie, $attrs, $element, configService) {

    var req;
    var redirectUrl = $attrs.successRedirect;
    var deleteAccountDataField = $element.find('#deleteAccountData');
    var genericError = configService.get('errorGeneric') || 'Generic Error';

    $scope.cancel = function () {
        $scope.$parent.nextStep('settings');
    };

    $scope.deleteAccountData = ($attrs.deleteDataChecked == 'true');
    $scope.errorMsg = {
        msg: '',
        is: false
    };

    $scope.toggleDeleteAccountData = function () {
        $scope.deleteAccountData = !deleteAccountDataField.is(':checked');
    };

    $scope.deleteAccount = function () {
        req = customer.deleteAccount();
        req.then(
            function () {
                temporaryNotificationCookie.set('accountDeleted');
                window.location = redirectUrl;
            },
            function () {
                $scope.hasError = true;
                $scope.errorMsg = genericError;
            }
        );
    };
}]);