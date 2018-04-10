ssoApp.service('socialLoginService', ['config', '$q', '$window', 'customer', function (config, $q, $window, customer) {

    var facebookAPIVersion = 'v2.7';



    var localhostAppID = '1403926742968110';



    var fbIsInitialized = false;
    var googleIsInitialized = false;
    var facebookAppID = (window.location.hostname.indexOf('localhost') > -1) ? localhostAppID : config.get('config').apiKeyFacebook;
    var facebookLocale = config.get('config').facebookLocale;

    var gauth; // Google Authentication Instance



    function _initFacebook (cb) {
        var facebookScriptTag;
        var firstScriptTag = document.getElementsByTagName('script')[0];

        facebookScriptTag = document.createElement('script');
        facebookScriptTag.id = 'facebook-jssdk';
        facebookScriptTag.async = true;
        facebookScriptTag.src = "//connect.facebook.net/" + facebookLocale +"/sdk.js";

        facebookScriptTag.onload = function() {
            FB.init({
                appId: facebookAppID,
                status: true,
                cookie: true,
                xfbml: true,
                version: 'v2.7'
            });
            if (typeof cb === 'function') {
                cb();
            }
        };

        firstScriptTag.parentNode.insertBefore(facebookScriptTag, firstScriptTag);
    }

    function _initGoogle (cb) {
        var googleScriptTag;
        var firstScriptTag = document.getElementsByTagName('script')[0];
        googleScriptTag = document.createElement('script');
        googleScriptTag.async = true;
        googleScriptTag.src = "//apis.google.com/js/platform.js";

        googleScriptTag.onload = function() {
            var params = {
                client_id: config.get('config').apiKeyGoogle,
                scope: 'email'
            };
            gapi.load('auth2', function() {
                gapi.auth2.init(params);
            });
            if (typeof cb === 'function') {
                cb();
            }
        };

        firstScriptTag.parentNode.insertBefore(googleScriptTag, firstScriptTag);
    }

    function signoutGoogle (cb) {
        var existingGoogleElement = document.getElementById("gSignout");
        var googleSignOutElement;
        var firstScriptElement = document.getElementsByTagName('script')[0];

        if (typeof(existingGoogleElement) != 'undefined' && existingGoogleElement != null) {
            existingGoogleElement.remove();
        }

        googleSignOutElement = document.createElement('script');
        googleSignOutElement.src = "https://accounts.google.com/Logout";
        googleSignOutElement.type = "text/javascript";
        googleSignOutElement.id = "gSignout";
        firstScriptElement.parentNode.insertBefore(googleSignOutElement, firstScriptElement);

        if (typeof cb === 'function') {
            cb();
        }
    }

    function signoutFB (cb) {
        FB.logout(function (res) {
            if (typeof cb === 'function') {
                cb();
            }
        });
    }



    this.logout = function () {
        var deferred = $q.defer();
        var resolvedGoogle = false;
        var resolvedFB = false;

        function resolveService (service) {
            switch (service) {
                case 'facebook':
                    resolvedFB = true;
                    break;
                default:
                case 'google':
                    resolvedGoogle = true;
                    break;
            }
            if (resolvedGoogle && resolvedFB) {
                customer.logout().then(function () {
                    deferred.resolve();
                });
            }
        }

        function resolveFB () {
            resolveService('facebook');
        }

        function resolveGoogle () {
            resolveService('google');
        }

        function checkGoogle() {
            if (typeof gauth  === 'undefined') {
                gauth = gapi.auth2.getAuthInstance();
            }
            if (gauth.isSignedIn.get()) {
                signoutGoogle(resolveGoogle);
            } else {
                resolveGoogle();
            }
        }

        if (googleIsInitialized) {
            checkGoogle();
        } else {
            this.initGoogle(checkGoogle);
        }


        function checkFacebook() {
            FB.getLoginStatus(function(response) {
                if (response.status === 'connected') {
                    signoutFB(resolveFB);
                } else {
                    resolveFB();
                }
            });
        }

        if (fbIsInitialized) {
            checkFacebook();
        } else {
            this.initFacebook(checkFacebook);
        }

        return deferred.promise;
    };

    this.loginGoogle = function () {
        return $q(function (resolve, reject) {
            if (typeof gauth  === 'undefined') {
                gauth = gapi.auth2.getAuthInstance();
            }
            if (!gauth.isSignedIn.get()) {
                gauth.isSignedIn.listen(function (b) {
                    var googleUser, profile, accessToken;
                    if (b) {
                        googleUser = gauth.currentUser.get();
                        profile = googleUser.getBasicProfile();
                        accessToken = googleUser.getAuthResponse().access_token;
                        profile.token = accessToken;
                        resolve(profile);
                    }
                });
            }
            gauth.signIn().then(
                function (googleUser) {
                    var profile = googleUser.getBasicProfile();
                    var accessToken = googleUser.getAuthResponse().access_token;
                    profile.token = accessToken;
                    resolve(profile);
                },
                function (err) {
                    if (err.error !== 'IMMEDIATE_FAILED') {
                        reject();
                    }
                }
            );
        });
    };

    this.loginFacebook = function () {
        return $q(function (resolve, reject) {
			FB.login(function (res) {
				resolve(res);
			}, {scope: 'email', auth_type: 'rerequest'});
        });
    };

    this.getFacebookUserDetails = function () {
        return $q(function (resolve, reject) {
			FB.api('/me?fields=email,first_name,last_name,birthday,gender,age_range', function(res) {
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

    this.login = function (token, profile, provider) {
        var username = (provider === 'facebook') ? profile.email : profile.getEmail();
        var providerName = (provider === 'google') ? 'googleplus' : provider;
        return customer.loginSocial({
            username: username,
            provider: providerName,
            type: 'email',
            idpToken: token
        }).then(function() {
            $window.localStorage.setItem('mcd-sso-l', 'social');
        });
    };

    this.initFacebook = function (cb) {
        if (!fbIsInitialized) {
            _initFacebook(cb);
            fbIsInitialized = true;
        }

    };

    this.initGoogle = function (cb) {
        if (!googleIsInitialized) {
            _initGoogle(cb);
            googleIsInitialized = true;
        }

    };

}]);
