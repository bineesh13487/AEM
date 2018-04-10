ssoApp.controller('NewsletterUnsubscribeController', ['$scope', '$window', 'customer', 'temporaryNotificationCookie', '$attrs', 'config',
function($scope, $window, customer, temporaryNotificationCookie, $attrs, configService) {

    var redirectUrl = $attrs.redirect || "/";

    $scope.formName = 'NewsletterUnsubscribe';
    $scope.session = {};
    $scope.ref = [];
    $scope.errorMsg = {
        msg: '',
        is: false
    };
    $scope.preloader = false;

    var genericError = configService.get('errorGeneric') || 'Generic Error';

    $scope.submitFn = function(session, ref) {
        $scope.preloader = true;
        customer.unsubscribe(session).then(
            function(res) {
                $scope.preloader = false;
                if (res.data.statusCode === '11011') {
                    temporaryNotificationCookie.set('userUnsubscribed');
                    $window.document.location.href = redirectUrl;
                } else {
                    $scope.errorMsg.msg = genericError;
                    $scope.errorMsg.is = true;
                }
            },
            function(res) {
                $scope.preloader = false;
                $scope.errorMsg.msg = genericError;
                $scope.errorMsg.is = true;
            }
        );
    };

}]);
