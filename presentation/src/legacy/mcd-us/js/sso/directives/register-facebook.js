/**
 * This directive should be applied to a button or link like so:
 * <button login-facebook></button>
 */
ssoApp.directive('registerFacebook', ['socialLoginService', 'temporaryNotificationCookie', function (socialLoginService, temporaryNotificationCookie) {
	
    socialLoginService.initFacebook();

    function onError() {
        temporaryNotificationCookie.show('socialLoginFailed');
    }
    
    return {
		restrict: 'EA',
		scope: {
            loginCallback: '&advanceFn',
            profile: '='
        },
		replace: true,
		link: function (scope, ele, attr) {
			ele.on('click', function() {
                temporaryNotificationCookie.clear();
				socialLoginService.loginFacebook().then(
                    function (resp) {
					    if (resp.status == 'connected') {
						    socialLoginService.getFacebookUserDetails().then(
                                function (profile) {
                                    scope.profile.profile = {profile: profile, provider: 'facebook'};
                                    scope.loginCallback();
						        },
                                onError
                            );
					    }
                        else if(resp.status == 'unknown') {
							onError();
                        }
				    }
                );
			});
		}
	};
}]);