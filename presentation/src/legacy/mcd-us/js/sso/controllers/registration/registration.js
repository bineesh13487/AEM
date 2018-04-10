ssoApp.controller('RegistrationController', [
    '$scope', 'customer', 'temporaryNotificationCookie', '$attrs', '$element', '$timeout', '$q', 'config', 'backendValidationParser', '$rootScope',
    function($scope, customer, temporaryNotificationCookie, $attrs, $element, $timeout, $q, configService, backendValidationParser, $rootScope) {

    var componentParameters = angular.fromJson($attrs.json);
    var genericError = configService.get('errorGeneric') || 'Generic Error';

    $scope.backLinkText = configService.get('config').backLinkText;

    $scope.currentStep = {step:'initial'};
    $scope.tempProfile = {};
    $scope.attrs = {
        redirect: componentParameters.redirectPage,
        ageVerificationDialogMessage: componentParameters.ageVerificationDialogMessage
    };

    $scope.fieldList = {
        required: [],
        optional: []
    };

    $scope.dirtyForms = {
        required: false,
        optional: false
    };

    $scope.backedValidationFailedFields = [];

    $scope.reset = function () {
        $scope.tempProfile = {
            postalCode: ''
        };
    };

    $scope.reset();

    $scope.register = function (forcedProfile, referenceProfile) {
        forcedProfile = forcedProfile || $scope.tempProfile;
        referenceProfile = referenceProfile || forcedProfile;
        $scope.backedValidationFailedFields = [];
        return $q(function (resolve, reject) {
            var registerRequest = customer.register($scope.tempProfile.type, forcedProfile);
            registerRequest.then(
                function (resp) {
                    if(resp.data.statusCode == '11011') {
                        if(resp.data.verificationNeeded == true) {
                            temporaryNotificationCookie.set('userRegisteredNeedsVerification');
                        } else {
                            temporaryNotificationCookie.set('userRegistered');
                        }
                    }
                    if ($scope.attrs.redirect) {
                        window.location = $scope.attrs.redirect;
                    }
                },
                function (resp) {
                    var toGoBack = false;
                    var detailArray = [];
                    if (resp.data && resp.data.statusCode == '11380') {
                        var errorFieldName = "email";
                        watchBEVErrorFields(["email"]);
                        if ($scope.fieldList.required.indexOf(errorFieldName) > -1) {
                            toGoBack = true;
                        }
                        $scope.backedValidationFailedFields.push(errorFieldName);
                        if (toGoBack) {
                            $scope.nextStep('required');
                            $timeout(function () {$scope.$evalAsync(function () {
                                $rootScope.$broadcast('requiredFormError', componentParameters.error11380);
                            });}, 0);
                        } else {
                            $rootScope.$broadcast('optionalFormError', componentParameters.error11380);
                        }
                    } else if (resp.data && resp.data.statusCode == '11931') {
                        $timeout(function () {$scope.$evalAsync(function () {
                            $rootScope.$broadcast('requiredFormError', componentParameters.error11931);
                        });}, 0);
                        $scope.nextStep('required');
                    } else if (resp.data && resp.data.statusCode == '11820') {
                        var errorFieldName = "email";
                        watchBEVErrorFields(["email"]);
                        if ($scope.fieldList.required.indexOf(errorFieldName) > -1) {
                            toGoBack = true;
                        }
                        $scope.backedValidationFailedFields.push(errorFieldName);
                        if (toGoBack) {
                            $scope.nextStep('required');
                            $timeout(function () {$scope.$evalAsync(function () {
                                $rootScope.$broadcast('requiredFormError', componentParameters.error11820);
                            });}, 0);
                        } else {
                            $rootScope.$broadcast('optionalFormError', componentParameters.error11820);
                        }
                    } else if (resp.data && resp.data.statusCode == '10120') {
                        detailArray = backendValidationParser.parseFieldErrorDetails(resp.data.details, customer.mapProfileObject(referenceProfile));
                        if (!detailArray || detailArray.length < 1) {
                            throw new Error('Validation error code without error details.');
                        }
                        watchBEVErrorFields(detailArray);
                        for (var detailsI = 0; detailsI < detailArray.length; detailsI++) {
                            var errorFieldName = detailArray[detailsI];
                            if ($scope.fieldList.required.indexOf(errorFieldName) > -1) {
                                toGoBack = true;
                            }
                            $scope.backedValidationFailedFields.push(errorFieldName);
                        }
                        if (toGoBack) {
                            $scope.nextStep('required');
                            resolve();
                        } else {
                            reject();
                        }
                    } else {
                        $rootScope.$broadcast('optionalFormError', genericError);
                        reject();
                    }
                }
            );
        });
    };



    var steps = [
        'initial',
        'required',
        'optional'
    ];

    function syncDeliveryAddress () {
        $scope.tempProfile.deliveryAddress_addressLine1 = $scope.tempProfile.mailingAddress_addressLine1;
        $scope.tempProfile.deliveryAddress_addressLine2 = $scope.tempProfile.mailingAddress_addressLine2;
        $scope.tempProfile.deliveryAddress_addressLine3 = $scope.tempProfile.mailingAddress_addressLine3;
        $scope.tempProfile.deliveryAddress_city = $scope.tempProfile.mailingAddress_city;
        $scope.tempProfile.deliveryAddress_state = $scope.tempProfile.mailingAddress_state;
        $scope.tempProfile.deliveryAddress_postalCode = $scope.tempProfile.mailingAddress_postalCode;
    }

    function syncBillingAddress () {
        $scope.tempProfile.billingAddress_addressLine1 = $scope.tempProfile.mailingAddress_addressLine1;
        $scope.tempProfile.billingAddress_addressLine2 = $scope.tempProfile.mailingAddress_addressLine2;
        $scope.tempProfile.billingAddress_addressLine3 = $scope.tempProfile.mailingAddress_addressLine3;
        $scope.tempProfile.billingAddress_city = $scope.tempProfile.mailingAddress_city;
        $scope.tempProfile.billingAddress_state = $scope.tempProfile.mailingAddress_state;
        $scope.tempProfile.billingAddress_postalCode = $scope.tempProfile.mailingAddress_postalCode;
    }

    function syncAddressesIfAppropriate () {
        if ($scope.tempProfile.useAsBillingAddress == 'Y') {
            syncBillingAddress();
        }
        if ($scope.tempProfile.useAsDeliveryAddress == 'Y') {
            syncDeliveryAddress();
        }
    }


    $scope.$watch('tempProfile.postalCode', function () {
        $scope.tempProfile.mailingAddress_postalCode = $scope.tempProfile.postalCode;
    });
    $scope.$watch('tempProfile.useAsBillingAddress', function () {
        syncAddressesIfAppropriate();
    });
    $scope.$watch('tempProfile.useAsDeliveryAddress', function () {
        syncAddressesIfAppropriate();
    });

    $scope.$watch('tempProfile.mailingAddress_addressLine1', function () {
        syncAddressesIfAppropriate();
    });
    $scope.$watch('tempProfile.mailingAddress_addressLine2', function () {
        syncAddressesIfAppropriate();
    });
    $scope.$watch('tempProfile.mailingAddress_addressLine3', function () {
        syncAddressesIfAppropriate();
    });
    $scope.$watch('tempProfile.mailingAddress_city', function () {
        syncAddressesIfAppropriate();
    });
    $scope.$watch('tempProfile.mailingAddress_state', function () {
        syncAddressesIfAppropriate();
    });
    $scope.$watch('tempProfile.mailingAddress_postalCode', function () {
        syncAddressesIfAppropriate();
    });

    function bindBEVErrorField (field, index) {
        $scope.$watch('tempProfile.'+field, function (o,n) {
            if ($scope.backedValidationFailedFields.indexOf(field) > -1 && o != n) {
                $scope.backedValidationFailedFields.splice($scope.backedValidationFailedFields.indexOf(field), 1);
                $scope.$broadcast('clearBEVError', field);
            }
        });
    }

     function watchBEVErrorFields (fields) {
        var _field;
        for (var i = 0; i < fields.length; i++) {
            _field = fields[i];
            bindBEVErrorField(_field);
        }
    }

    $scope.nextStep = function (step, cb) {
        var currentStepIndex = steps.indexOf($scope.currentStep.step);
        var nextStepIndex = (currentStepIndex === steps.length - 1) ? 0 : currentStepIndex + 1;

        if (!step) {
            step = steps[nextStepIndex];
        }

        $scope.currentStep.step = step;

        $timeout(function () {$scope.$evalAsync(function () {

            var top = Math.max($element.offset().top - 115, 0);
            window.aemScrollTo(top);

            if (typeof cb === 'function') {
                cb();
            }

            $scope.$broadcast('step', step);

        });}, 0);

    };

}]);