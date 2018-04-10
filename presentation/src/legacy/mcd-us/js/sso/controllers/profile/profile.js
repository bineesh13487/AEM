ssoApp.controller('ProfileController', ['$rootScope','$scope', '$element', '$window', 'config', 'customer', 'temporaryNotificationCookie', 'SecureComponent','socialLoginService',
function($rootScope, $scope, $element, $window, config, customer, temporaryNotificationCookie, secureComponent, socialLoginService) {

    $rootScope.isRestrictedPage = true; //this flag is read on logout to see if user needs to be redirected
    $scope.backLinkText = config.get('config').backLinkText;
    $scope.redirectLoginPageURL = config.get('config').redirectNotLoggedInUserPath;
    $scope.view = {view: 'profileMenu'};

    $scope.preloader = false;

    var steps = [
        'profileMenu',
        'profileDetails',
        'settings',
        'updatepassword',
        'deleteaccount',
        'communications'
    ];

    $scope.emailUpdatedNeedsVerification = false;
    customer.get('emailUpdatedNeedsVerification').then(
        function (_emailUpdatedNeedsVerification) {
            $scope.emailUpdatedNeedsVerification = _emailUpdatedNeedsVerification;
        }
    );

    $scope.nextStep = function (step, cb) {

        var currentStepIndex = steps.indexOf($scope.view.view);
        var nextStepIndex = (currentStepIndex === steps.length - 1) ? 0 : currentStepIndex + 1;

        if (!step) {
            step = steps[nextStepIndex];
        }
        console.log();
        $scope.view.view = step;

        window.setTimeout(function () {
        window.aemScrollTo();
            if (typeof cb === 'function') {
                cb();
            }
        }, 1);
    };

    $scope.logOutProfile = function(){
        $scope.preloader = true;

        var isSocial = $window.localStorage.getItem('mcd-sso-l') === 'social';
        var logoutReq;

        if (isSocial) {
            logoutReq = socialLoginService.logout();
        } else {
            logoutReq = customer.logout();
        }
        logoutReq.then(
            function(res) {
                $scope.preloader = false;

                //redirect the user only when not in edit or preview mode
                if (!config.get('inEditorOrPreview')) {
                    temporaryNotificationCookie.set('userLoggedOut');
                    setTimeout(function() {
                        $window.location = $scope.redirectLoginPageURL;
                    }, 100);

                } else {
                    $rootScope.$broadcast('loginStatus', false);
                    temporaryNotificationCookie.show('userLoggedOut');
                }
            },
            function(err) {
                $scope.preloader = false;
                console.log(err.data);

                //display generic error message in a temporary notification
                temporaryNotificationCookie.show('errorGeneric');
            }
        );
    };
    secureComponent.secure();
}]);
