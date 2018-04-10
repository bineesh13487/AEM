yrtkApp.service('socialLoginService', ['$rootScope','$q','questionService',function ($rootScope, $q, questionService) {

    var facebookAPIVersion = 'v2.7';

    var fbIsInitialized = false;
    var facebookAppID = questionService.fbApiKey();
  //  606303369543251;


    function _initFacebook () {
        var facebookScriptTag;
        var firstScriptTag = document.getElementsByTagName('script')[0];

        facebookScriptTag = document.createElement('script');
        facebookScriptTag.id = 'facebook-jssdk';
        facebookScriptTag.async = true;
        facebookScriptTag.src = "//connect.facebook.net/en_US/sdk.js";

        facebookScriptTag.onload = function() {
            FB.init({
                appId: facebookAppID,
                status: true,
                cookie: true,
                xfbml: true,
                version: 'v2.7'
            });
        };

        firstScriptTag.parentNode.insertBefore(facebookScriptTag, firstScriptTag);
    }


    function signoutFB (cb) {
        FB.logout(function (res) {
            if (typeof cb === 'function') {
                cb();
            }
        });
    }


    this.loginFacebook = function () {
        return $q(function (resolve, reject) {
			FB.login(function (res) {
				resolve(res);
			}, {scope: 'email', auth_type: 'rerequest'});
        });
    };

    this.getFacebookUserDetails = function () {
        return $q(function (resolve, reject) {
			FB.api('/me?fields=email,first_name,last_name', function(res) {
                var authResponse;
				if (!res || res.error){
					reject();
				} else {
                    authResponse = FB.getAuthResponse();
                    angular.extend(res, {token: authResponse.accessToken, id: authResponse.id});
					resolve(res);
				}
			});
        });
    };

    this.initFacebook = function () {
        if (!fbIsInitialized) {
            _initFacebook();
            fbIsInitialized = true;
        }

    };

}]);
