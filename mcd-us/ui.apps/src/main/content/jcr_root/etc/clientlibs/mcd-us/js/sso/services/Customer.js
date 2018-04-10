
/**
 * The UserProfile service provides an interface for the controllers to access and update user profile data
 * (in promises) and to access account methods (login, register, etc).
 */
ssoApp.service('customer', ['$q', '$window', 'customerDSS', '$timeout', function ($q, $window, customerDSS, $timeout) {

    var _attributes = {};

    var modelIsLoaded = false;
    var modelIsLoading = false;
    var modelLoadCallbackQueue = [];
    var modelLoadErrorCallbackQueue = [];
    var modelLoadNonProfileQueue = [];
	var userEmailinfo;
    var loggedIn = false;

    function mapModel (profileObject) {
        return profileObject;
    }

    function callModelLoadCallbackQueue () {
        $timeout(function () {
            if (modelLoadCallbackQueue.length > 0) {
                angular.forEach(modelLoadCallbackQueue, function (func) {
                    if (typeof func === 'function') {
                        func();
                    }
                });
            }
            modelLoadCallbackQueue = [];
            modelLoadErrorCallbackQueue = [];
        }, 0);
    }

    function callModelLoadNonProfileQueue() {
        $timeout(function() {
            if (modelLoadNonProfileQueue.length > 0) {
                angular.forEach(modelLoadNonProfileQueue, function(func) {
                    if (typeof func === 'function') {
                        func();
                    }
                });
            }
        }, 0);
    }

    function callModelLoadErrorCallbackQueue () {
        $timeout(function () {
            if (modelLoadErrorCallbackQueue.length > 0) {
                angular.forEach(modelLoadErrorCallbackQueue, function (func) {
                    if (typeof func === 'function') {
                        func();
                    }
                });
            }
            modelLoadCallbackQueue = [];
            modelLoadErrorCallbackQueue = [];
        }, 0);
    }

    function loadModel () {
        modelIsLoading = true;
        modelIsLoaded = false;
        customerDSS.getProfile().then(
            function (resp) {
                if (resp.username) {
                    _attributes = mapModel(resp);
                    callModelLoadCallbackQueue();
                    modelIsLoaded = true;
                    modelIsLoading = false;
                    loggedIn = true;
                } else {
                    callModelLoadNonProfileQueue();
                }
            },
            function () {
                _attributes = {};
                callModelLoadErrorCallbackQueue();
                modelIsLoaded = false;
                modelIsLoading = false;
            }
        );
    }

    function onModelLoad (func, errorFunc) {
        if (!modelIsLoaded) {
            modelLoadCallbackQueue.push(func);
            modelLoadErrorCallbackQueue.push(errorFunc);
            if (!modelIsLoading) {
                loadModel();
            }
        } else {
            callModelLoadCallbackQueue();
        }
    }



    /**
     * Get a specific field from the profile model in a resolved promise.
     */
    this.get = function (field) {
        return $q(function (resolve, reject) {
            if (typeof field === 'string') {
                if (modelIsLoaded) {
                    if (loggedIn && _attributes[field]) {
                        resolve(_attributes[field]);
                    } else {
                        resolve(undefined);
                    }
                } else {
                    onModelLoad(function () {resolve(_attributes[field]);}, reject);
                }
            } else {
                reject('invalid field specified');
            }
        });
    };



    this.getPrivacyPolicyandTCVersions = function () {
        return $q(function (resolve, reject) {
            if (modelIsLoaded) {
                    if (loggedIn) {
                        var resp = {
                            privacyPolicy: _attributes.privacyPolicy,
                            termsConditions: _attributes.termsConditions
                        };
                        resolve(resp);
                    } else {
                        resolve(undefined);
                    }
            } else {
                onModelLoad(
                    function () {
                        var resp = {
                            privacyPolicy: _attributes.privacyPolicy,
                            termsConditions: _attributes.termsConditions
                        };
                        resolve(resp);
                    },
                    reject
                );
            }
        });
    };



    this.getAll = function () {
        return $q(function (resolve, reject) {
            if (modelIsLoaded) {
                    if (loggedIn) {
                        resolve(_attributes);
                    } else {
                        resolve(undefined);
                    }
            } else {
                onModelLoad(function () {resolve(_attributes);}, reject);
            }
        });
    };




    /**
     * Log the user in after a social login and return a new profile model in a resolved promise.
     */
    this.loginSocial = function (params) {

        if (params &&
            typeof params.username === 'string' &&
            typeof params.provider === 'string' &&
            typeof params.idpToken === 'string' &&
            typeof params.type === 'string') {
            var req = customerDSS.loginSocial(params);
            req.then(
                function (resp) {
                    _attributes = {};
                    modelIsLoaded = false;
                }
            );
            return req;
        } else {
            ssoError('Customer-loginSocial-2', 'Invalid parameters.');
        }
    };

    this.getUserEmail = function() {
        if(sessionStorage.registeredUserName) {
            userEmailinfo = sessionStorage.registeredUserName;
        }
		return userEmailinfo;
    }
    /**
     * Log the user in and return a new profile model in a resolved promise.
     */
    this.login = function (params) {
        userEmailinfo = params.username;
        if (params &&
            typeof params.username === 'string' &&
            typeof params.password === 'string' &&
            typeof params.type === 'string') {

            var req = customerDSS.login(params);

            req.then(
                function (resp) {
                    _attributes = {};
                    modelIsLoaded = false;
                }
            );
            return req;
        } else {
            ssoError('Customer-login-2', 'Invalid parameters.');
        }
    };

    /**
     * Log the user out and return a new profile model in a resolved promise.
     */
    this.logout = function () {
        // call DCS.Logout();
        var req = customerDSS.logout();

        req.then(
            function(res) {
                if (res.data.statusCode === '11011') {
                    $window.localStorage.removeItem('mcd-sso-l');
                } else {
                    ssoError('Customer-Logout-1', 'Request error.');
                }
            }
        );
        return req;
    };

    this.newsletterSubscribe = function (params) {
        return customerDSS.lightRegistration(params);
    };

    this.unsubscribe = function (params) {
        return customerDSS.unsubscribe(params);
    };

    /**
     * Change the current user's password.
     *
     * @param params - object of parameters - all required:
     *  - username (String)
     *  - oldpassword (String)
     *  - newPassword (String)
     *  - newPasswordConfirm (String)
     *
     *  @return Promise
     */


    /*
    change:
     - username
     - oldPassword
     - newPassword
     - newPasswordConfirm

     verify:
      - username
      - verificationCode
      - password
      - confirmPassword
    */
    this.changePassword = function (params) {
        var req;
        if (params &&
            typeof params.username === 'string' &&
            typeof params.oldPassword === 'string' &&
            typeof params.newPassword === 'string' &&
            typeof params.newPasswordConfirm === 'string') {
            req = customerDSS.changePassword(params);
            req.then(
                function (resp) {
                    // noop
                },
                function () {
                    ssoError('Customer-ChangePassword-1', 'Request error.');
                }
            );
            return req;
        } else if (params &&
            typeof params.username === 'string' &&
            typeof params.verificationCode === 'string' &&
            typeof params.password === 'string' &&
            typeof params.confirmPassword === 'string') {
            req = customerDSS.resetPasswordConfirmation(params);
            req.then(
                function (resp) {
                    // noop
                },
                function () {
                    ssoError('Customer-ResetPasswordConfirmation-1', 'Request error.');
                }
            );
            return req;
        } else {
            ssoError('Customer-ChangePassword-2', 'Invalid parameters.');
        }
    };

    this.initiateChangePassword = function (params) {
        if (params && typeof params.username === 'string') {
            var req = customerDSS.initiateChangePassword(params);
            req.then(
                function (resp) {
                    // noop
                    return resp;
                },
                function () {
                    ssoError('Customer-InitiateChangePassword-1', 'Request error.');
                }
            );
            return req;
        } else {
            ssoError('Customer-InitiateChangePassword-2', 'Invalid parameters.');
        }
    };

    /**
    * Does the Customer Profile Verification
    *params - object
    * -useremail (String)
    */
     this.userVerification = function (params) {
        if (params && typeof params.username === 'string' && typeof params.verificationCode === 'string') {
            var verificationreq = customerDSS.userVerification(params);
            return verificationreq;
        } else {
            ssoError('userVerification Error1', '');
        }
    };

    this.userVerificationInitiation = function (params) {
        if (params && typeof params.username === 'string') {
            var verificationreq = customerDSS.userVerificationInitiation(params);
            return verificationreq;
        } else {
            ssoError('userVerification Error1', '');
        }
    };



    this.updateProfile = function (params) {
        return $q(function (resolve, reject) {
            customerDSS.updateProfile(params).then(resolve, reject);
        });
    };



    this.register = function (type, params) {
        sessionStorage.registeredUserName = params.email;
        if (params && typeof params.email === 'string') {
            var registrationreq;
            if (type === 'email') {
                registrationreq = customerDSS.registrationTraditional(params);
            } else {
                registrationreq = customerDSS.registrationSocial(params);
            }
            return registrationreq;
        } else {
            ssoError('Error2', '');
        }
    };



    this.deleteAccount = function () {
        var deleteReq = customerDSS.deleteAccount();
        return deleteReq;
    };

    this.isUserLoggedIn = function () {
        return $q(function (resolve, reject) {
            if (modelIsLoaded) {
                resolve(loggedIn);
            } else {
                modelLoadNonProfileQueue.push(function() {resolve(loggedIn)});
                onModelLoad(function () {resolve(loggedIn);}, reject);
            }
        });
    };

    this.mapProfileObject = customerDSS.mapProfileObject;


    loadModel();

}]);
