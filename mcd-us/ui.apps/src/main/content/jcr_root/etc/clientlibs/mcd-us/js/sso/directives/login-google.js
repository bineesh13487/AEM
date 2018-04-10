/**
 * This directive should be applied to a button or link like so:
 * <button login-google></button>
 */
ssoApp.directive('loginGoogle', ['socialLoginService', 'temporaryNotificationCookie', 
    function (socialLoginService, temporaryNotificationCookie) {
	
    socialLoginService.initGoogle();

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
		restrict: 'EA',
        scope: {
            cb: '&?'
        },
		replace: true,
		link: function (scope, ele, attr){
			ele.on('click', function(){
                if (scope.cb || typeof scope.cb === 'function') {
                    scope.cb();
                }
				socialLoginService.loginGoogle().then(
                    function (profile) {
                        socialLoginService.login(profile.token, profile, 'google').then(
                            function (resp) {
                                temporaryNotificationCookie.set('userLoggedIn');
                                redirect(attr.redirectafterloginpath);
                            },
                            onError
                        );
                    },
                    onError
                );
	        });
		}
	};
}]);