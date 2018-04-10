/**
 * The customerDSS provides the data interface for the customer data backend. It retains an up-to-date
 * cache of the user profile and returns it in a promise to consuming services and controllers. It provides
 * an interface to specific api methods.
 *
 * This service calls the bootstrap service to load the cached session data on initialization.
 *
 *
 */
ssoApp.service('customerDSS', ['$http', 'sessionBootstrap', '$q', 'config', 'temporaryNotificationCookie', '$timeout',
function ($http, sessionBootstrap, $q, config, temporaryNotification, $timeout) {

    var apiConfigObject = config.get('config') || {
        dcsApiEndpoint: '/',
        mcdMarketid: '',
        mcdSourceapp: '',
        mcdLocale: ''
    };


    /** API configuration object */
    var apiConfig = {
        root: apiConfigObject.dcsApiDomain,
        marketid: apiConfigObject.mcdMarketID,
        sourceapp: apiConfigObject.mcdSourceApp,
        locale: apiConfigObject.mcdLocale,

        currentVersionPrivacy: apiConfigObject.currentVersionPrivacy,
        currentVersionTC: apiConfigObject.currentVersionTC,

        changePasswordPath: '/v2/customer/security/password/change',
        initiateChangePasswordPath: '/v2/customer/security/password',
        resetPasswordConfirmation: '/v2/customer/security/password',
        loginPath: '/v2/customer/security/authentication',
        updateProfile: '/v2/customer/security/account',
        accountVerification: '/v2/customer/security/account/verification',
        deleteAccount: '/v2/customer/security/account',
        registration: '/v2/customer/security/account',
        getProfilePath: '/v2/customer/security/account',
        logoutPath: '/v2/customer/security/authentication',
        lightRegistration: '/v2/customer/security/account/subscription',
        unsubscribe: '/v2/customer/security/account/subscription',

        getSession: '/v2/customer/session'
    };

    // TODO: remove the || once this settles down
    function getHeaderParams () {
        return {
            'Content-Type': 'application/json',
            'mcd-marketid': apiConfig.marketid || 'US',
            'mcd-sourceapp': apiConfig.sourceapp,
            'mcd-locale': apiConfig.locale || 'en-US'
        };
    }


    function isUserLoggedOutByServer (code) {
        if (code == '99999') {
            temporaryNotification.set('userLoggedOutBySystem');
            $timeout(function () {
                window.location.reload();
                window.aemScrollTo(0,0);
            }, 250);
            return true;
        }
        return false;
    }



    var profile = {};
    var returnedSession = {};
    var haveProfile = false;
    var cachedLoaded = false;
    var profileRequestQueue = [];


    /**
     * This utility maps the profile data in the API response objects
     * to a simple object for use in the front end app.
     */
    function mapProfileData (profileResponse) {

        if (!profileResponse.details && profileResponse.data.details) {
            profileResponse.details = profileResponse.details || {};
            profileResponse.details.data = profileResponse.data.details;
        }

        var i, j, _key, _prefix;
        var simpleProfile = {};
        var hasDetails = profileResponse.details;
        var isLoggedIn;
        var profileObject;
        var prefs;
        var demographics;
        var externalId;

        if (hasDetails) {
            isLoggedIn = profileResponse.details.data.profile ? true : false;
            profileObject = isLoggedIn ? profileResponse.details.data.profile.base : {};
        } else {
            isLoggedIn = profileResponse.data.profile ? true : false;
            profileObject = isLoggedIn ? profileResponse.data.profile.base : {};
        }

        var baseFields = ['username', 'firstName', 'middleName', 'lastName', 'shortName', 'displayName', 'suffix', 'prefix', 'activeInd', 'emailUpdatedNeedsVerification'];
        var addressDetailFields = ['area', 'building', 'city', 'company', 'department', 'district', 'garden', 'state', 'addressLine1', 'addressLine2', 'addressLine3', 'addressLine4', 'streetType', 'suburb', 'zipCode', 'block', 'level', 'unit', 'houseNumber', 'addressPreferenceTypeID', 'streetLonNumber', 'remark', 'latitude', 'longitude', 'isRedZone', 'isAmberZone', 'landmark', 'county', 'shortZipCode', 'country'];
        var addressKeys = {
            mailing1: {key: 'mailing1', namePrefix: 'mailingAddress'},
            mailing2: {key: 'mailing2', namePrefix: 'mailingAddress2'},
            delivery: {key: 'delivery', namePrefix: 'deliveryAddress'},
            billing: {key: 'billing', namePrefix: 'billingAddress'}
        };
        var phoneKeys = {
            home: {key: 'home', namePrefix: 'phone'},
            mobile: {key: 'mobile', namePrefix: 'mobile'}
        };

        if (isLoggedIn) {
            // Base fields
            for (i = 0; i < baseFields.length; i++) {
                simpleProfile[baseFields[i]] = profileObject[baseFields[i]];
            }

            // Addresses
            if (profileObject.address) {
                for (i = 0; i < profileObject.address.length; i++) {
                    _key = profileObject.address[i].addressType;
                    _prefix = addressKeys[_key] ? addressKeys[_key].namePrefix : false;
                    if (_prefix) {
                        for (j = 0; j < addressDetailFields.length; j++) {
                            simpleProfile[_prefix + '_' + addressDetailFields[j]] = profileObject.address[i].details[0].addressLineDetails[addressDetailFields[j]];
                        }
                        simpleProfile[_prefix + '_postalCode'] = simpleProfile[_prefix + '_zipCode'];
                        if (_prefix == 'mailing1') {
                            simpleProfile.postalCode = simpleProfile[_prefix + '_zipCode'];
                        }
                    }
                }
            }

            // Email
            if (profileObject.email && profileObject.email.length === 1 && !profileObject.email[0].type) {
                simpleProfile.email = profileObject.email[0].emailAddress;
            } else {
                if (profileObject.email) {
                    for (i = 0; i < profileObject.email.length; i++) {
                        if (profileObject.email[i].type == 'personal') {
                            simpleProfile.email = profileObject.email[i].emailAddress;
                            simpleProfile.emailIsVerified = profileObject.email[i].verifiedInd === 'Y';
                        }
                    }
                }
            }

            // Phone
            if (profileObject.phone) {
                for (i = 0; i < profileObject.phone.length; i++) {
                    _key = profileObject.phone[i].type;
                    _prefix = phoneKeys[_key].namePrefix;
                    simpleProfile[_prefix + 'Number'] = profileObject.phone[i].number;
                }
            }

            // Communication Preferences
            if (isLoggedIn && profileResponse.details.data.profile.extended && profileResponse.details.data.profile.extended.preferences) {
                prefs = profileResponse.details.data.profile.extended.preferences;
                for (i = 0; i < prefs.length; i++) {
                    if (prefs[i].preferenceId.toLowerCase() === 'websitesms') {
                        simpleProfile.sendSMS = prefs[i].isActive;
                    } else if (prefs[i].preferenceId.toLowerCase() === 'websitepush') {
                        simpleProfile.pushNotifications = prefs[i].isActive;
                    } else if (prefs[i].preferenceId.toLowerCase() === 'websiteemail') {
                        simpleProfile.emailPromotions = prefs[i].isActive;
                    }
                }
            }
            simpleProfile.emailPromotions = simpleProfile.emailPromotions || 'N';
            simpleProfile.pushNotifications = simpleProfile.pushNotifications || 'N';
            simpleProfile.sendSMS = simpleProfile.sendSMS || 'N';

            // Demographics: Birthday, Gender, ageRange
            if (isLoggedIn && profileResponse.details.data.profile.extended && profileResponse.details.data.profile.extended.demographics) {
                demographics = profileResponse.details.data.profile.extended.demographics;
                if (demographics.dob) {
                    simpleProfile.birthMonth = '' + demographics.dob.birthMonth;
                    simpleProfile.birthYear = '' + demographics.dob.birthYear;
                    if (simpleProfile.birthMonth.length == 1) {
                        simpleProfile.birthMonth = '0'+simpleProfile.birthMonth;
                    }
                }
                simpleProfile.gender = '' + demographics.gender;
                simpleProfile.ageRange = '' + demographics.ageRange;
            }

            // My Coke Rewards
            if (isLoggedIn && profileResponse.details.data.profile.extended && profileResponse.details.data.profile.extended.externalId) {
                externalId = profileResponse.details.data.profile.extended.externalId;
                for (i = 0; i < externalId.length; i++) {
                    if (externalId[i].appName == 'CokeRewards') {
                        simpleProfile.myCokeRewardsNumber = externalId[i].appId;
                    }
                }
            }

            // AcceptancePolicies
            if (isLoggedIn && profileResponse.details.data.profile.extended.policies) {
                simpleProfile.privacyPolicy = profileResponse.details.data.profile.extended.policies.accessPolicy ? profileResponse.details.data.profile.extended.policies.accessPolicy[0].version : '';
                simpleProfile.termsConditions = profileResponse.details.data.profile.extended.policies.acceptancePolicies ? profileResponse.details.data.profile.extended.policies.acceptancePolicies[0].version : '';
            }
        }

        return simpleProfile;
    }




    function loadProfile (profileData) {
        profile = profileData;
        returnedSession = profile;
        haveProfile = true;
        if (profileRequestQueue.length > 0) {
            angular.forEach(profileRequestQueue, function(request) {
                if (typeof request === 'function') {
                    request();
                }
            });
        }
        profileRequestQueue = [];
    }



    /**
     * Change Password method - returns a promise wrapped around the API request object.
     * @params object takes the params required by the API changePassword method.
     */
    this.changePassword = function (params) {
        if (params && typeof params === 'object') {

            return $q(function (resolve, reject) {
                var req = $http.put(apiConfig.root + apiConfig.changePasswordPath, params, {headers: getHeaderParams(), withCredentials: true});
                req.then(
                    function (resp) {
                        var statusCode = resp.data.statusCode,
                            statusObject = {
                                    status: resp.data.status,
                                    statusCode: statusCode,
                                    statusDescription: resp.data.statusDescription
                                };
                        if (!isUserLoggedOutByServer(resp.data.statusCode) && resp.data.statusCode == '11011') {
                            resolve(statusObject);
                        } else {
                            reject(statusObject);
                        }
                    }, reject);
            });
        }
    };



    /**
     *
     */
    this.initiateChangePassword = function (params) {
        if (params && typeof params === 'object') {

            return $q(function (resolve, reject) {
                var req = $http.post(apiConfig.root + apiConfig.initiateChangePasswordPath, params, {headers: getHeaderParams(), withCredentials: true});
                req.then(
                    function (resp) {
                        var statusCode = resp.data.statusCode,
                            statusObject = {
                                    status: resp.data.status,
                                    statusCode: statusCode,
                                    statusDescription: resp.data.statusDescription
                                };
                        if (!isUserLoggedOutByServer(resp.data.statusCode) && resp.data.statusCode == '11011') {
                            resolve(statusObject);
                        } else {
                            reject(statusObject);
                        }
                    }, reject);
            });
        }
    };

    this.resetPasswordConfirmation = function (params) {
        if (params && typeof params === 'object') {

            return $q(function (resolve, reject) {
                var req = $http.put(apiConfig.root + apiConfig.resetPasswordConfirmation, params, {headers: getHeaderParams(), withCredentials: true});
                req.then(
                    function (resp) {
                        var statusCode = resp.data.statusCode,
                            statusObject = {
                                    status: resp.data.status,
                                    statusCode: statusCode,
                                    statusDescription: resp.data.statusDescription
                                };
                        if (!isUserLoggedOutByServer(resp.data.statusCode) && resp.data.statusCode == '11011') {
                            resolve(statusObject);
                        } else {
                            reject(statusObject);
                        }
                    }, reject);
            });
        }
    };

    /**
     * Log the user in after a social login
     * params object takes username, type and password.
     * returns a promise that resolves with the request status code/message/description.
     * on sucessful login, the profile has been udpated when the promise resolves.
     */
    this.loginSocial = function (params) {
        if (params && typeof params === 'object' && params.username && params.type && params.provider && params.idpToken) {
            return $q(function (resolve, reject) {
                var req = $http({
                    url: apiConfig.root + apiConfig.loginPath + '?type=social',
                    method: 'POST',
                    data: {
                        loginUsername: params.username,
                        type: params.type,
                        provider: params.provider,
                        idpToken: params.idpToken
                    },
                    headers: getHeaderParams(),
                    withCredentials: true
                });

                req.then(
                    function (resp) {
                        var statusCode = resp.data.statusCode,
                            statusObject = {
                                    status: resp.data.status,
                                    statusCode: statusCode,
                                    statusDescription: resp.data.statusDescription
                                };
                        if (!isUserLoggedOutByServer(resp.data.statusCode) && resp.data.statusCode == '11011') {
                            profileRequestQueue.push(function () {
                                resolve(statusObject);
                            });
                            loadProfile(mapProfileData(resp));
                        } else {
                            reject(statusObject);
                        }
                    }, reject);

            });
        } else {
            ssoError('Customer-loginSocial-2', 'Invalid parameters.');
        }
    };

    /**
     * Log the user in.
     * params object takes username, type and password.
     * returns a promise that resolves with the request status code/message/description.
     * on sucessful login, the profile has been udpated when the promise resolves.
     */
    this.login = function (params) {
        if (params && typeof params === 'object' && params.username && params.type && params.password) {
            return $q(function (resolve, reject) {
                var req = $http({
                    url: apiConfig.root + apiConfig.loginPath + '?type=traditional',
                    method: 'POST',
                    data: {
                        loginUsername: params.username,
                        type: params.type,
                        password: params.password
                    },
                    headers: getHeaderParams(),
                    withCredentials: true
                });

                req.then(
                    function (resp) {
                        var statusCode = resp.data.statusCode,
                            statusObject = {
                                    status: resp.data.status,
                                    statusCode: statusCode,
                                    statusDescription: resp.data.statusDescription
                                };
                        if (!isUserLoggedOutByServer(resp.data.statusCode) && resp.data.statusCode == '11011') {
                            profileRequestQueue.push(function () {
                                resolve(statusObject);
                            });
                            loadProfile(mapProfileData(resp));
                        } else {
                            reject(statusObject);
                        }
                    }, reject);
            });
        }
    };

    /**
     * Logout function
     */
    this.logout = function () {

        return $q(function(resolve, reject) {
            // hard coding until node services are up
            var headerParams = getHeaderParams();

            var req = $http({
                url: apiConfig.root + apiConfig.logoutPath,
                method: 'DELETE',
                data: {},
                headers: headerParams,
                withCredentials: true
            });

            req.then(function(res) {
                if (!isUserLoggedOutByServer(res.data.statusCode) && res.data.statusCode === '11011') {
                    // clear info
                    resolve(res);
                } else {
                    reject(res);
                }
            }, reject);


        });
    };

    /**
     * Returns the user's profile promise.
     */
    this.getProfile = function () {
        var req;
        return $q(function (resolve, reject) {
            if (haveProfile) {
                resolve(profile);
            } else {
                profileRequestQueue.push(function () {
                    if (haveProfile) {
                        resolve(profile);
                    } else {
                        reject();
                    }
                });
                if (cachedLoaded) {
                    req = $http.get(apiConfig.root + apiConfig.getProfilePath, {withCredentials: true});
                    req.then(
                        function (resp) {
                            if (!isUserLoggedOutByServer(resp.data.statusCode)) {
                                loadProfile(mapProfileData(resp));
                            }
                        },
                        function () {
                            ssoError('CustomerDSS.getProfile-Fetch', 'Unable to load the profile.');
                        }
                    );
                }
            }
        });
    };


    /**
         * Returns the user's account verified  status.
    */
    this.userVerification = function (params) {
        if (params && typeof params === 'object') {
            return $q(function (resolve, reject) {
                var requserVerification = $http.post(apiConfig.root + apiConfig.accountVerification, params, {headers: getHeaderParams(), withCredentials: true});
                requserVerification.then(
                    function (resp) {
                        var statusCode = resp.data.statusCode,
                        statusObject = {
                            status: resp.data.status,
                            statusCode: statusCode,
                            statusDescription: resp.data.statusDescription
                        };
                        if (!isUserLoggedOutByServer(resp.data.statusCode) && resp.data.statusCode == '11011') {
                            resolve(statusObject);
                        } else {
                            reject(statusObject);
                        }
                    },
                reject);
            });
        }
    };

    /**
         * .
    */
    this.userVerificationInitiation = function (params) {
        if (params && typeof params === 'object') {
            return $q(function (resolve, reject) {
                var requserVerification = $http.put(apiConfig.root + apiConfig.accountVerification + '?type=email', params, {headers: getHeaderParams(), withCredentials: true});
                requserVerification.then(
                    function (resp) {
                        var statusCode = resp.data.statusCode,
                        statusObject = {
                            status: resp.data.status,
                            statusCode: statusCode,
                            statusDescription: resp.data.statusDescription
                        };
                        if (!isUserLoggedOutByServer(resp.data.statusCode) && resp.data.statusCode == '11011') {
                            resolve(statusObject);
                        } else {
                            reject(statusObject);
                        }
                    },
                reject);
            });
        }
    };

    function formatProfileSubmission (params, isUpdate) {
        var submitParams = {};
        var addressKeys = [
            {key: 'mailing1', namePrefix: 'mailingAddress'},
            {key: 'mailing2', namePrefix: 'mailingAddress2'},
            {key: 'delivery', namePrefix: 'deliveryAddress'},
            {key: 'billing', namePrefix: 'billingAddress'}
        ];
        var phoneKeys = [
            {key: 'home', namePrefix: 'phone'},
            {key: 'mobile', namePrefix: 'mobile'}
        ];
        var addresses = [];
        var phones = [];
        if (params && typeof params === 'object') {
            submitParams = {
                loginUsername: params.email,
                type: 'email',
                profile: {
                    base: {
                        username: params.email,
                        firstName: params.firstName,
                        lastName: params.lastName,
                        suffix: params.suffix,
                        prefix: params.prefix,
                        shortName: params.firstName+' '+params.lastName
                    },
                    extended: {
                        demographics: {
                            ageRange: params.ageRange,
                            dob: (params.birthMonth || params.birthYear) ? {
                                birthMonth: params.birthMonth,
                                birthYear: params.birthYear,
                                birthDay: '01'
                            } : undefined,
                            gender: params.gender
                        }
                    },
                    security: {
                        ageVerified: params.ageVerification
                    },
                }
            };

            if (!params.firstName || !params.lastName) {
                submitParams.profile.base.shortName = undefined;
            }

            if (isUpdate) {
                if ((!params.email && !params.firstName && !params.lastName && !params.suffix && !params.prefix) &&
                    (params.email !== '' && params.firstName !== '' && params.lastName !== '' && params.suffix !== '' && params.prefix !== '')) {
                    submitParams.profile.base = undefined;
                }
                if ((!params.ageRange && !params.birthMonth && !params.birthYear && !params.gender) &&
                    (params.ageRange !== '' && params.birthMonth !== '' && params.birthYear !== '' && params.gender !== '')) {
                    submitParams.profile.extended = undefined;
                } else if ((!params.ageRange && !params.birthMonth && !params.birthYear && !params.gender) &&
                           (params.ageRange !== '' && params.birthMonth !== '' && params.birthYear !== '' && params.gender !== '')) {
                    submitParams.profile.extended.demographics = undefined;
                }
                if (!params.ageVerification && params.ageVerification !== '') {
                    submitParams.profile.security = undefined;
                }
            }



            if (params.myCokeRewardsNumber || params.myCokeRewardsNumber === '') {
                submitParams.profile.extended = submitParams.profile.extended || {externalId:[]};
                submitParams.profile.extended.externalId = [
                    {
                        appName: "CokeRewards",
                        appId: params.myCokeRewardsNumber,
                        activeInd: "Y"
                    }
                ];
            }



            if (!isUpdate) {
                submitParams.profile.extended = submitParams.profile.extended || {};
                submitParams.profile.extended.preferences = submitParams.profile.extended.preferences || [];
                submitParams.profile.extended.preferences.push({
                    preferenceId: "communicationPref",
                    sourceId: "DEP",
                    preferenceDesc: "Communication preference",
                    type: "email",
                    isActive: "Y"
                });
            }
            if (params.sendSMS || params.sendSMS === '') {
                submitParams.profile.extended = submitParams.profile.extended || {};
                submitParams.profile.extended.preferences = submitParams.profile.extended.preferences || [];
                submitParams.profile.extended.preferences.push({
                    preferenceId: "websiteSMS",
                    sourceId: "DEP",
                    preferenceDesc: "SMS marketing/offer communications",
                    type: "SMS",
                    isActive: params.sendSMS ? params.sendSMS : "N"
                });
            }
            if (params.pushNotifications || params.pushNotifications === '') {
                submitParams.profile.extended = submitParams.profile.extended || {};
                submitParams.profile.extended.preferences = submitParams.profile.extended.preferences || [];
                submitParams.profile.extended.preferences.push({
                    preferenceId: "websitePUSH",
                    sourceId: "DEP",
                    preferenceDesc: "push marketing/offer communications",
                    type: "PUSH",
                    isActive: params.pushNotifications ? params.pushNotifications : "N"
                });
            }
            if (params.emailPromotions || params.emailPromotions === '') {
                submitParams.profile.extended = submitParams.profile.extended || {};
                submitParams.profile.extended.preferences = submitParams.profile.extended.preferences || [];
                submitParams.profile.extended.preferences.push({
                    preferenceId: "websiteEMAIL",
                    sourceId: "DEP",
                    preferenceDesc: "email marketing/offer communications",
                    type: "EMAIL",
                    isActive: params.emailPromotions ? params.emailPromotions : "N"
                });
            }



            if (params.email || params.email === '') {
                submitParams.profile.base = submitParams.profile.base || {};
                submitParams.profile.base.email = [{
                    emailAddress: params.email,
                    type: 'personal',
                    primaryInd: 'Y',
                    activeInd: 'Y'
                }];
            }

            var _key, _prefix;

            for (var i = 0; i < addressKeys.length; i++) {
                _key = addressKeys[i].key;
                _prefix = addressKeys[i].namePrefix;
                if ((params[_prefix + '_addressLine1'] || params[_prefix + '_addressLine1'] === '') ||
                    (params[_prefix + '_postalCode'] || params[_prefix + '_postalCode'] === '')) {
                    addresses.push({
                        addressType: _key,
                        activeInd: 'Y',
                        details: [
                            {
                                addressLocale: apiConfigObject.mcdLocale,
                                addressLineDetails: {
                                    addressLine1: params[_prefix + '_addressLine1'],
                                    addressLine2: params[_prefix + '_addressLine2'],
                                    addressLine3: params[_prefix + '_addressLine3'],
                                    city: params[_prefix + '_city'],
                                    state: params[_prefix + '_state'],
                                    zipCode: params[_prefix + '_postalCode']
                                }
                            }
                        ]
                    });
                }
            }

            var primaryPhoneInd = 'Y';
            for (i = 0; i < phoneKeys.length; i++) {
                if (params[phoneKeys[i].namePrefix + 'Number'] || params[phoneKeys[i].namePrefix + 'Number'] === '') {
                    phones.push({
                        type: phoneKeys[i].key,
                        number: params[phoneKeys[i].namePrefix + 'Number'].replace(/[^0-9]+/g, ''),
                        primaryInd: primaryPhoneInd,
                        activeInd: 'Y'
                    });
                    primaryPhoneInd = 'N';
                }
            }

            var addPrivacy = params.privacy || params.combined || false;
            var addTerms = params.terms || params.combined || false;

            if (addPrivacy) {
                submitParams.profile.extended = submitParams.profile.extended || {policies: {}};
                submitParams.profile.extended.policies = submitParams.profile.extended.policies || {};
                submitParams.profile.extended.policies.accessPolicy = [{
                    type: 'PrivacyPolicy',
                    name: 'DEP-PrivacyPolicy',
                    version: apiConfig.currentVersionPrivacy,
                    acceptanceInd: 'Y'
                }];
            }
            if (addTerms) {
                submitParams.profile.extended = submitParams.profile.extended || {policies: {}};
                submitParams.profile.extended.policies = submitParams.profile.extended.policies || {};
                submitParams.profile.extended.policies.acceptancePolicies = [{
                    type: 'Terms&Conditions',
                    name: 'DEP-Terms&Conditions',
                    version: apiConfig.currentVersionTC,
                    acceptanceInd: 'Y'
                }];
            }

            if (addresses.length) {
                submitParams.profile.base = submitParams.profile.base || {};
                submitParams.profile.base.address = addresses;
            }

            if (phones.length) {
                submitParams.profile.base = submitParams.profile.base || {};
                submitParams.profile.base.phone = phones;
            }
        }

        return submitParams;
    }
    this.mapProfileObject = formatProfileSubmission;


    function register (type, params) {
        var submitParams = formatProfileSubmission(params);
        if (params && typeof params === 'object') {
            if (type === 'social') {
                submitParams.provider = params.provider;
                submitParams.idpToken = params.idpToken;
            } else {
                 submitParams.password = params.password;
            }

            return $q(function (resolve, reject) {
                //var reqUrl = (type === 'social') ? apiConfig.root + apiConfig.registration + '/social' : apiConfig.root + apiConfig.registration + '/traditional';
                var reqUrl = (type === 'social') ? apiConfig.root + apiConfig.registration + '?type=social' : apiConfig.root + apiConfig.registration + '?type=traditional';
                var reqRegistration = $http.post(
                    reqUrl,
                    submitParams,
                    {headers: getHeaderParams(), withCredentials: true}
                );
                reqRegistration.then(
                    function (resp) {
                        if (!isUserLoggedOutByServer(resp.data.statusCode) && resp.data.statusCode == '11011') {
                            resolve(resp);
                        } else {
                            reject(resp);
                        }
                    },
                reject);
            });
        }
    }

    this.registrationTraditional = function (params) {
        return register('traditional', params);
    };

    this.registrationSocial = function (params) {
        return register('social', params);
    };

    this.updateProfile = function (params) {
        var submitParams = formatProfileSubmission(params, true);
        if (params && typeof params === 'object') {
            return $q(function (resolve, reject) {
                var reqUrl = apiConfig.root + apiConfig.updateProfile;
                var reqUpdateProfile = $http.put(
                    reqUrl,
                    submitParams,
                    {headers: getHeaderParams(), withCredentials: true}
                );
                reqUpdateProfile.then(
                    function (resp) {
                        if (!isUserLoggedOutByServer(resp.data.statusCode) && resp.data.statusCode == '11011') {
                            profile = Object.assign(profile, params);
                            //check for the emailUpdatedNeedsVerification flag on the response
                            profile.emailUpdatedNeedsVerification = resp.data.emailUpdatedNeedsVerification;
                            resolve(resp);
                        } else {
                            reject(resp);
                        }
                    },
                reject);
            });
        }
    };

    this.lightRegistration = function (params) {
        var locale = apiConfig.locale;

        if (params.localeHeader) {
            locale = params.localeHeader;
        }

        var data = {
            loginUsername: params.email,
            type: 'email',
            profile: {
                base: {
                    address: [
                            {
                            addressType: 'home1',
                            details: [{
                                addressLocale: locale,
                                addressLineDetails: {
                                    zipCode: params.postalCode
                                }
                            }]
                        }
                    ]
                },
                extended: {
                    subscriptions: [
                        {
                            subscriptionId: "webSubscriptionId",
                            subscriptionDesc: "Website Subscription",
                            optInStatus: "Y"
                        }
                    ]
                }
            }
        };

        var req = $http.post(apiConfig.root + apiConfig.lightRegistration, data, {
            headers: angular.extend(getHeaderParams(), {'mcd-locale': locale}),
            withCredentials: true
        });

        return $q(function (resolve, reject) {
            req.then(
                function (resp) {
                    if (!isUserLoggedOutByServer(resp.data.statusCode)) {
                        resolve(resp);
                    }
                },
                reject
            );
        });

    };

    this.unsubscribe = function (params) {
        var data = {
            loginUsername: params.email,
            type: 'email',
            profile: {
                extended: {
                    subscriptions: [
                        {
                            subscriptionId: "webSubscriptionId",
                            subscriptionDesc: "Website Subscription",
                            optOutReason: "Not Interested"
                        }
                    ]
                }
            }
        };

        var req = $http({
            method: 'DELETE',
            url: apiConfig.root + apiConfig.unsubscribe,
            headers: getHeaderParams(),
            data: data,
            withCredentials: true
        });

        return $q(function (resolve, reject) {
            req.then(
                function (resp) {
                    if (!isUserLoggedOutByServer(resp.data.statusCode)) {
                        resolve(resp);
                    }
                },
                reject
            );
        });

    };

    this.deleteAccount = function (dcsId) {
        var req = $http({
            method: 'DELETE',
            url: apiConfig.root + apiConfig.deleteAccount,
            headers: getHeaderParams(),
            data: {dcsId: dcsId},
            withCredentials: true
        });
        return $q(function (resolve, reject) {
            req.then(
                function (resp) {
                    if (!isUserLoggedOutByServer(resp.data.statusCode)) {
                        resolve(resp);
                    }
                },
                reject
            );
        });
    };

    /** During intialization, call the sessionBootstrap.load() method to load the session cache. */
    
    if(apiConfigObject.hideLinks == "false"){
        sessionBootstrap.load(apiConfig.root + apiConfig.getSession, getHeaderParams()).then(
            function (sessionData) {
                cachedLoaded = true;
                if (sessionData) {
                    loadProfile(mapProfileData(sessionData));
                }
            },
            function () {
                cachedLoaded = true;
                ssoError('CustomerDSS-1', 'Session Bootstrap failed to load.');
            }
        );
        }


}]);
