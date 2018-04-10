ssoApp.controller('TemporaryNotificationController', ['$scope', '$element', 'customer', 'temporaryNotificationCookie', 'config', function($scope, $element, customer, temporaryNotificationCookie, config) {
    var notificationKey;

    $scope.notification = {
        show: false,
        msg: ''
    };
    $scope.notificationwithemail;
    $scope.notificationCTA = false;

    $scope.params = {
        username: ''
    };

    function show() {
        window.aemScrollTo(0, 0);
        notificationKey = temporaryNotificationCookie.get();
        $scope.params.username = customer.getUserEmail();
        if (notificationKey !== '__msg__') {
            if (notificationKey) {
                if (notificationKey == 'verificationNeeded' || notificationKey == 'verificationCodeExpired') {
                    $scope.notificationCTA = true;
                    $scope.notification.msg = config.get(notificationKey);
                } else if (notificationKey == 'verificationEmailSent' || notificationKey == 'emailUpdatedNeedsVerification' || notificationKey == 'userRegisteredNeedsVerification') {
                    $scope.notificationwithemail = config.get(notificationKey);
                    $scope.notification.msg = temporaryNotificationCookie.setEmail($scope.params.username, $scope.notificationwithemail);
                } else {
                    $scope.notification.msg = config.get(notificationKey);
                }
                $scope.notification.show = true;

            } else {
                $scope.notification.show = false;
                $scope.notification.msg = '_blank_';
            }
        } else {
            $scope.notification.show = true;
            $scope.notification.msg = temporaryNotificationCookie.getMsg();
        }
        sessionStorage.clear();
    }

    $scope.clear = function() {
        temporaryNotificationCookie.clear();
    };

    $scope.ctcAction = function() {
        var initiateResponse = customer.userVerificationInitiation($scope.params);
        initiateResponse.then(
            function(resp) {
                if (resp.statusCode == '11011') {
                    $scope.notificationCTA = false;
                    temporaryNotificationCookie.show('verificationEmailSent');

                }
            },
            function(resp) {
                $scope.notificationCTA = false;
                temporaryNotificationCookie.show('errorGeneric');
            }
        );
    };

    temporaryNotificationCookie.onShow(show);

    show();

}]);