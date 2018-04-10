/**
 * This directive should be applied to a button or link like so:
 * <button login-facebook></button>
 */
ssoApp.directive('loginFacebook', ['socialLoginService', 'temporaryNotificationCookie',
    function (socialLoginService, temporaryNotificationCookie) {
	
    socialLoginService.initFacebook();

    function onError() {
        temporaryNotificationCookie.show('socialLoginFailed');
    }

    function redirect (url) {
        if (typeof url === 'string' && url.length > 0) {
            window.location = url;
        } else {
           location.reload();
        }
    }

    return {
		restrict: 'AE',
		scope: {
            cb: '&?'
        },
		replace: true,
		link: function (scope, ele, attr) {
			ele.on('click', function() {
                if (scope.cb || typeof scope.cb === 'function') {
                    scope.cb();
                }
				socialLoginService.loginFacebook().then(
                    function (resp) {
					    if (resp.status == 'connected') {
						    socialLoginService.getFacebookUserDetails().then(
                                function (profile) {
                                    socialLoginService.login(profile.token, profile, 'facebook').then(
                                        function (_resp) {
                                            temporaryNotificationCookie.set('userLoggedIn');
                                            redirect(attr.redirectafterloginpath);
                                        },
                                        onError
                                    );
						        },
                                onError
                            );
					    }
                        if (resp.status == 'not_authorized') {
							onError();
                        }
				    }
                );
			});
		}
	};
}]);