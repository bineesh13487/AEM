ssoApp.controller('RegistrationOptionalController', ['$scope', '$element', 'customer', 'temporaryNotificationCookie', 'config', '$attrs', '$timeout',
    function($scope, $element, customer, temporaryNotificationCookie, config, $attrs, $timeout) {

    var fieldNames = [];

    var _profileCache = angular.copy($scope.$parent.tempProfile);

    $scope.formName = 'RegistrationRequired';
    $scope.session = $scope.$parent.tempProfile;
    $scope.ref = [];
    $scope.buttonText = 'Register';

    $scope.errorMsg = {
        msg: '',
        is: false
    };

    $scope.$on('optionalFormError', function (ev, msg) {
        $scope.errorMsg = {
            msg: msg,
            is: true
        };
        $scope.$broadcast('scrollToTop');
    });

    $scope.isSocial = $scope.$parent.tempProfile.type === 'social';

    $scope.hideOnInit = ['firstName', 'lastName', 'email', 'gender', 'pw1', 'pw2'];

    $scope.hideCheck = function () {
        return $scope.$parent.tempProfile.type == 'social';
    };

    $scope.noVisibleFieldsCallback = function () {
        $scope.$parent.register();
    };

    $scope.session.dirty = $scope.$parent.dirtyForms.optional;

    $scope.skip = function () {
        var _referenceProfile;

        _referenceProfile = angular.copy($scope.$parent.tempProfile);

        for (var x = 0; x < fieldNames.length; x++) {
            _referenceProfile[fieldNames[x]] = _referenceProfile[fieldNames[x]] || '';
            if (_profileCache[fieldNames[x]]) {
                delete _profileCache[fieldNames[x]];
            }
        }

        $scope.$parent.dirtyForms.optional = true;

        var regRes = $scope.$parent.register(_profileCache, _referenceProfile);

        regRes.then(
            function () {},
            function () {
                $timeout(function () {
                    $scope.afterRender();
                }, 0);
            }
        );
    };

    $scope.blockingValidationCheck = function (fieldname) {
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

        if (($scope.session.useAsDeliveryAddress == 'Y' && isBlockableDeliveryAddressField) || ($scope.session.useAsBillingAddress == 'Y' && isBlockableBillingAddressField)) {
            return true;
        } else {
            return false;
        }
    };

    function getBEErrors () {
        if ($scope.$parent.backedValidationFailedFields.length) {
            var _beField;
            for (var beErrorsI = 0; beErrorsI < $scope.$parent.backedValidationFailedFields.length; beErrorsI++) {
                _beField = $scope.$parent.backedValidationFailedFields[beErrorsI];
                if (fieldNames.indexOf(_beField) > -1) {
                    $scope.thisForm.RegistrationRequired[_beField].$setValidity('required', false);
                    $scope.thisForm.RegistrationRequired[_beField].$setTouched();
                }
            }
            $scope.$broadcast('scrollToErrors');
        }
    }

    $scope.$on('clearBEVError', function (ev, fieldName) {
        if ($scope.thisForm.RegistrationRequired[fieldName]) {
            $scope.thisForm.RegistrationRequired[fieldName].$setValidity('invalid', undefined);
        }
    });

    var rendered = false;
    $scope.afterRender = function () {

        var _hideOnInit = [];
        for (var hoiI = 0; hoiI < $scope.hideOnInit; hoiI++) {
            if ($scope.$parent.tempProfile[$scope.hideOnInit[hoiI]]) {
                _hideOnInit.push($scope.hideOnInit[hoiI]);
            }
        }
        $scope.hideOnInit = _hideOnInit;

        fieldNames = [];
        for (var i = 0; i < $scope.ref.length; i++) {
            fieldNames.push($scope.ref[i].name);
            if ($scope.session[$scope.ref[i].name]) {
                $scope.$parent.dirtyForms.optional = true;
            }
        }

        getBEErrors();

        rendered = true;
    };


    $scope.$on('step', function (ev, step) {
        if (rendered && step == 'optional') {
            getBEErrors();
        }
    });



    $scope.submitFn = function(session, ref) {

        for (var i = 0; i < $scope.ref.length; i++) {
            fieldNames.push($scope.ref[i].name);
        }
        $scope.$parent.fieldList.optional = fieldNames;

        angular.extend($scope.$parent.tempProfile, session);

        $scope.$parent.dirtyForms.optional = true;

        var regRes = $scope.$parent.register();

        regRes.then(
            function () {},
            function () {
                $timeout(function () {
                    $scope.afterRender();
                }, 0);
            }
        );

    };

}]);