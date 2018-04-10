ssoApp.controller('ProfileDetailsController',
    ['$scope', '$element', 'customer', 'temporaryNotificationCookie', 'config', '$attrs', '$timeout', 'backendValidationParser',
    function($scope, $element, customer, temporaryNotificationCookie, configService, $attrs, $timeout, backendValidationParser) {

    var _profile = {data: {}};
    var _profileCache = {};

    var _isDisabled = true;
    var genericError = configService.get('errorGeneric') || 'Generic Error';
    $scope.preloader = false;
    $scope.isDisabled = function (fieldname) {
        var isBlockableDeliveryAddressField =  (fieldname == 'deliveryAddress_addressLine1' ||
                                                fieldname == 'deliveryAddress_addressLine2' ||
                                                fieldname == 'deliveryAddress_addressLine3' ||
                                                fieldname == 'deliveryAddress_city' ||
                                                fieldname == 'deliveryAddress_state' ||
                                                fieldname == 'deliveryAddress_postalCode'
        );
        var isBlockableBillingAddressField =   (fieldname == 'billingAddress_addressLine1' ||
                                                fieldname == 'billingAddress_addressLine2' ||
                                                fieldname == 'billingAddress_addressLine3' ||
                                                fieldname == 'billingAddress_city' ||
                                                fieldname == 'billingAddress_state' ||
                                                fieldname == 'billingAddress_postalCode'
        );

        if (($scope.session.data.useAsDeliveryAddress == 'Y' && isBlockableDeliveryAddressField) || ($scope.session.data.useAsBillingAddress == 'Y' && isBlockableBillingAddressField)) {
            return true;
        } else {
            return _isDisabled;
        }
    };

    var fieldNames = [];
    var rendered = false;
    $scope.afterRender = function () {
        fieldNames = [];
        for (var i = 0; i < $scope.ref.length; i++) {
            fieldNames.push($scope.ref[i].name);
            if ($scope.session[$scope.ref[i].name]) {
                $scope.$parent.dirtyForms.optional = true;
            }
        }

        rendered = true;
    };

    $scope.errorMsg = {
        msg: '',
        is: false
    };

    function enable () {
        _isDisabled = false;
    }

    function isAddressField (fieldName) {
        return fieldName.indexOf('_addressLine1') > -1 || fieldName.indexOf('_addressLine2') > -1 || fieldName.indexOf('_addressLine3') > -1 || fieldName.indexOf('_city') > -1 || fieldName.indexOf('_state') > -1 || fieldName.indexOf('_postalCode') > -1;
    }

    customer.getAll().then(
        function (profile) {
            _profile.data = angular.copy(profile);
            _profileCache = angular.copy(profile);
            enable();
        },
        function () {
            enable();
        }
    );

    $scope.formName = 'ProfileDetails';
    $scope.session = _profile;
    $scope.ref = [];

    function syncDeliveryAddress () {
        _profile.data.deliveryAddress_addressLine1 = _profile.data.mailingAddress_addressLine1;
        _profile.data.deliveryAddress_addressLine2 = _profile.data.mailingAddress_addressLine2;
        _profile.data.deliveryAddress_addressLine3 = _profile.data.mailingAddress_addressLine3;
        _profile.data.deliveryAddress_city = _profile.data.mailingAddress_city;
        _profile.data.deliveryAddress_state = _profile.data.mailingAddress_state;
        _profile.data.deliveryAddress_postalCode = _profile.data.mailingAddress_postalCode;
    }

    function syncBillingAddress () {
        _profile.data.billingAddress_addressLine1 = _profile.data.mailingAddress_addressLine1;
        _profile.data.billingAddress_addressLine2 = _profile.data.mailingAddress_addressLine2;
        _profile.data.billingAddress_addressLine3 = _profile.data.mailingAddress_addressLine3;
        _profile.data.billingAddress_city = _profile.data.mailingAddress_city;
        _profile.data.billingAddress_state = _profile.data.mailingAddress_state;
        _profile.data.billingAddress_postalCode = _profile.data.mailingAddress_postalCode;
    }
    function syncAddressesIfAppropriate () {
        if (_profile.data.useAsBillingAddress == 'Y') {
            syncBillingAddress();
        }
        if (_profile.data.useAsDeliveryAddress == 'Y') {
            syncDeliveryAddress();
        }
    }

    $scope.$watch('session.data.postalCode', function () {
        _profile.data.mailingAddress_postalCode = _profile.data.postalCode;
    });
    $scope.$watch('session.data.useAsBillingAddress', function () {
        syncAddressesIfAppropriate();
    });
    $scope.$watch('session.data.useAsDeliveryAddress', function () {
        syncAddressesIfAppropriate();
    });

    $scope.$watch('session.data.mailingAddress_addressLine1', function () {
        syncAddressesIfAppropriate();
    });
    $scope.$watch('session.data.mailingAddress_addressLine2', function () {
        syncAddressesIfAppropriate();
    });
    $scope.$watch('session.data.mailingAddress_addressLine3', function () {
        syncAddressesIfAppropriate();
    });
    $scope.$watch('session.data.mailingAddress_city', function () {
        syncAddressesIfAppropriate();
    });
    $scope.$watch('session.data.mailingAddress_state', function () {
        syncAddressesIfAppropriate();
    });
    $scope.$watch('session.data.mailingAddress_postalCode', function () {
        syncAddressesIfAppropriate();
    });

    $scope.submitFn = function(session, ref) {

         $scope.preloader = true;

        var updated = {};
        var haveUpdates = false;

        $scope.backedValidationFailedFields = [];

        var addressKeys = [
            {key: 'mailing1', namePrefix: 'mailingAddress'},
            {key: 'mailing2', namePrefix: 'mailingAddress2'},
            {key: 'delivery', namePrefix: 'deliveryAddress'},
            {key: 'billing', namePrefix: 'billingAddress'}
        ];

        var _key, _prefix;

        $element.find('input[type="checkbox"]').each(function () {
            var $this = $(this);
            if (!$this.is(':checked')) {
                session.data[$this.attr('name')] = 'N';
            } else {
                session.data[$this.attr('name')] = 'Y';
            }
        });

        $element.find('input[type="radio"]:checked').each(function () {
            var $this = $(this);
            session.data[$this.attr('name')] = $this.val();
        });

        angular.forEach(_profile.data, function (val, key) {
            if (!isAddressField(key)) {
                if (val !== _profileCache[key]) {
                    haveUpdates = true;
                    updated[key] = val;
                }
            }
        });

        for (var i = 0; i < addressKeys.length; i++) {
            _prefix = addressKeys[i].namePrefix;
            if (_profile.data[_prefix + '_addressLine1'] || _profile.data[_prefix + '_addressLine2'] || _profile.data[_prefix + '_addressLine3'] || _profile.data[_prefix + '_state'] || _profile.data[_prefix + '_city'] || _profile.data[_prefix + '_postalCode']) {
                if (_profile.data[_prefix + '_addressLine1'] != _profileCache[_prefix + '_addressLine1'] ||
                    _profile.data[_prefix + '_addressLine2'] != _profileCache[_prefix + '_addressLine2'] ||
                    _profile.data[_prefix + '_addressLine3'] != _profileCache[_prefix + '_addressLine3'] ||
                    _profile.data[_prefix + '_city'] != _profileCache[_prefix + '_city'] ||
                    _profile.data[_prefix + '_state'] != _profileCache[_prefix + '_state'] ||
                    _profile.data[_prefix + '_postalCode'] != _profileCache[_prefix + '_postalCode']
                ) {
                    updated[_prefix + '_addressLine1'] = _profile.data[_prefix + '_addressLine1'];
                    updated[_prefix + '_addressLine2'] = _profile.data[_prefix + '_addressLine2'];
                    updated[_prefix + '_addressLine3'] = _profile.data[_prefix + '_addressLine3'];
                    updated[_prefix + '_state'] = _profile.data[_prefix + '_state'];
                    updated[_prefix + '_city'] = _profile.data[_prefix + '_city'];
                    updated[_prefix + '_postalCode'] = _profile.data[_prefix + '_postalCode'];
                    haveUpdates = true;
                }
            }
        }

        $scope.backedValidationFailedFields = [];

        function getBEErrors () {
            if ($scope.backedValidationFailedFields.length) {
                var _beField;
                for (var beErrorsI = 0; beErrorsI < $scope.backedValidationFailedFields.length; beErrorsI++) {
                    _beField = $scope.backedValidationFailedFields[beErrorsI];
                    if (fieldNames.indexOf(_beField) > -1) {
                        $scope.thisForm.ProfileDetails[_beField].$setValidity('required', false);
                        $scope.thisForm.ProfileDetails[_beField].$setTouched();
                    }
                }
                $scope.$broadcast('scrollToErrors');
            }
        }

        $scope.$on('clearBEVError', function (ev, fieldName) {
            if ($scope.thisForm.ProfileDetails[fieldName]) {
                $scope.thisForm.ProfileDetailsProfileDetails[fieldName].$setValidity('invalid', undefined);
            }
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

        if (haveUpdates) {
            customer.updateProfile(updated).then(
                function (resp) {
                     $scope.preloader = false;
                    if (resp.data.statusCode == "11011") {
                        temporaryNotificationCookie.show('accountUpdatedPersonalInformation');
                        //CM not sure if it's enough that profile is already updated with the emailUpdatedNeedsVerification flag,
                        //or whether we need the $scope.emailUpdatedNeedsVerification as well
//                        if (resp.data.emailUpdatedNeedsVerification) {
//                            $scope.emailUpdatedNeedsVerification = true;
//                        }
                        $scope.$parent.nextStep('profileMenu');
                    } else {
                        $scope.errorMsg = $scope.errorMsg || {};
                        $scope.errorMsg.msg = genericError;
                        $scope.errorMsg.is = true;
                        $scope.$broadcast('scrollToTop');
                    }
                },
                function (err) {
                    $scope.preloader = false;
                    var detailArray;
                    if (err.data && err.data.statusCode == '10120') {
                        detailArray = backendValidationParser.parseFieldErrorDetails(err.data.details, customer.mapProfileObject(updated, true));
                        if (!detailArray || detailArray.length < 1) {
                            throw new Error('Validation error code without error details.');
                        }
                        watchBEVErrorFields(detailArray);
                        for (var detailsI = 0; detailsI < detailArray.length; detailsI++) {
                            var errorFieldName = detailArray[detailsI];
                            $scope.backedValidationFailedFields.push(errorFieldName);
                        }
                        getBEErrors();
                    } else {
                        $scope.errorMsg = $scope.errorMsg || {};
                        $scope.errorMsg.msg = genericError;
                        $scope.errorMsg.is = true;
                        $scope.$broadcast('scrollToTop');
                    }
                }
            );
        } else {
            $scope.$parent.nextStep('profileMenu');
        }
    };
}]);