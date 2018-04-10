ssoApp.controller('loginStatusIndicator', 
    ['$rootScope', '$scope', '$window', 'customer', 'temporaryNotificationCookie', 'SecureComponent', 'socialLoginService', '$attrs', 'config','$sce', 
    function($rootScope, $scope, $window, customer, temporaryNotificationCookie, SecureComponent, socialLoginService, $attrs, config, $sce) {

    $scope.redirectURL = config.get('config').redirectNotLoggedInUserPath;
    customer.isUserLoggedIn().then(function(bool) {
        if (bool) {
            $scope.loggedout = false;
            $scope.loggedin = true;
        } else {
            $scope.loggedout = true;
            $scope.loggedin = false;
        }
    }, function(err) {
        console.log(err);
    });

    $scope.logout = function() {
        var isSocial = $window.localStorage.getItem('mcd-sso-l') === 'social';
        var logoutReq;

        if (isSocial) {
            logoutReq = socialLoginService.logout();
        } else {
            logoutReq = customer.logout();
        }
        logoutReq.then(
            function(res) {
                $scope.loggedout = true;
                $scope.loggedin = false;

                //redirect the user only when on the 'restricted access' page and when not in edit or preview mode
                if ($rootScope.isRestrictedPage && !config.get('inEditorOrPreview')) {
                    temporaryNotificationCookie.set('userLoggedOut');
                    $window.location = $scope.redirectURL;
                } else {
                    temporaryNotificationCookie.show('userLoggedOut');
                }
            },
            function(err) {
                console.log(err.data);

                //display generic error message in a temporary notification
                temporaryNotificationCookie.show('errorGeneric');
            }
        );
    }

    $scope.$on('loginStatus', function(event, userLoggedIn) {
        $scope.loggedin = userLoggedIn;
        $scope.loggedout = !userLoggedIn;
    });
}]);
