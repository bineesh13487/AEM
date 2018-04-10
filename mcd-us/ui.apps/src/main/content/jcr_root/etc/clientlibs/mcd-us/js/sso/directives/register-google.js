/**
 * This directive should be applied to a button or link like so:
 * <button login-google></button>
 */
ssoApp.directive('registerGoogle', ['socialLoginService', 'temporaryNotificationCookie', function (socialLoginService,temporaryNotificationCookie) {
	
    socialLoginService.initGoogle();

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
		link: function (scope, ele, attr){
			ele.on('click', function(){
                temporaryNotificationCookie.clear();
				socialLoginService.loginGoogle().then(
                    function (profile) {
                        scope.profile.profile = {profile: profile, provider: 'google'};
                        scope.loginCallback();
                    },
                    onError
                );
	        });
		}
	};
}]);