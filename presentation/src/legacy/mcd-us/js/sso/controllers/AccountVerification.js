ssoApp.controller('AccountVerificationController', ['$scope', 'customer', 'temporaryNotificationCookie', 'SecureComponent', '$attrs', 'config','$sce',
function($scope, customer, temporaryNotificationCookie, SecureComponent, $attrs, config, $sce) {

    var redirectPath = $attrs.dataredirect;

    var a_qs;
    var t_qs;
    var qs = {};
    if (window.location.search.length > 1) {
        a_qs = window.location.search.substr(1).split('&');
        for (var i = 0; i < a_qs.length; i++) {
            t_qs = a_qs[i].split('=');
            qs[t_qs[0]] = t_qs[1];
        }
    }

    $scope.haveCredentials = false;
    if (qs.c && qs.u) {
        $scope.haveCredentials = true;
        $scope.verificationCode = qs.c;
        $scope.email = qs.u;
    }

    var verResponse;

    if ($scope.haveCredentials && !config.get('inEditor')) {
        verResponse = customer.userVerification({username: $scope.email, verificationCode: $scope.verificationCode});
        verResponse.then(
            function (resp) {
                if (resp.statusCode == "11011") {
                    temporaryNotificationCookie.set('accountVerified');
                } else {
                    temporaryNotificationCookie.set('errorGeneric');
                }
                document.location.href = redirectPath;
            },
            function (resp) {
                if (resp.statusCode == "11933") {
                    temporaryNotificationCookie.set('accountAlreadyVerified');
                } else if (resp.statusCode == "11922" || resp.statusCode == '11920' || resp.statusCode == '11415') {
                    temporaryNotificationCookie.set('verificationCodeExpired');
                } else {
                    temporaryNotificationCookie.set('errorGeneric');
                }
                document.location.href = redirectPath;
            }
        );
    } else {
        temporaryNotificationCookie.show('errorGeneric');
    }
}]);
