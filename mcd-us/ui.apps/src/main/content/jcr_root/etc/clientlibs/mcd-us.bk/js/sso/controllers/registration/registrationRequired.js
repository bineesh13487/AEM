ssoApp.controller('RegistrationRequiredController', 
    ['$scope', '$element', 'customer', 'temporaryNotificationCookie', 'config', '$attrs', 
    function($scope, $element, customer, temporaryNotificationCookie, config, $attrs) {

    var fieldNames = [];

    $scope.formName = 'RegistrationRequired';
    $scope.session = $scope.$parent.tempProfile;
    $scope.ref = [];
    $scope.buttonText = 'Register';

    $scope.errorMsg = {
        msg: '',
        is: false
    };

    $scope.$on('requiredFormError', function (ev, msg) {
        $scope.errorMsg = {
            msg: msg,
            is: true
        };
        $scope.$broadcast('scrollToTop');
    });

    $scope.isSocial = $scope.$parent.tempProfile.type === 'social';

    $scope.hideOnInit = ['firstName', 'lastName', 'email', 'gender', 'password', 'passwordConfirm'];

    $scope.hideCheck = function () {
        return $scope.$parent.tempProfile.type == 'social';
    };

    $scope.noVisibleFieldsCallback = function () {
        $scope.$parent.nextStep('optional');
    };

    $scope.session.dirty = $scope.$parent.dirtyForms.required;

    $scope.blockingValidationCheck = function (fieldname) {
        var basic = $scope.names.length &&
                $scope.names.indexOf('ageVerification') > -1 &&
                fieldname !== 'ageVerification' &&
                $scope.session.ageVerification != 'Y';
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
            return basic;
        }
    };

    function getBEErrors () {
        if ($scope.$parent.backedValidationFailedFields.length) {
            var _beField, hiddenIndex;
            for (var beErrorsI = 0; beErrorsI < $scope.$parent.backedValidationFailedFields.length; beErrorsI++) {
                _beField = $scope.$parent.backedValidationFailedFields[beErrorsI];
                hiddenIndex = $scope.hideOnInit.indexOf(_beField);
                if (hiddenIndex > -1) {
                    $scope.hideOnInit.splice(hiddenIndex, 1);
                }
                if (fieldNames.indexOf(_beField) > -1) {
                    $scope.thisForm.RegistrationRequired[_beField].$setValidity('invalid', false);
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
        
        var $ageChecks = $('input[name="ageVerification"]');

        var $ageCheckYes = $ageChecks.filter('[value="Y"]');
        var $ageCheckNo  = $ageChecks.filter('[value="N"]');
        
        var $ageCheckNoLabel = $ageCheckNo.siblings('label');

        var _hideOnInit = [];
        for (var hoiI = 0; hoiI < $scope.hideOnInit; hoiI++) {
            if ($scope.$parent.tempProfile[$scope.hideOnInit[hoiI]]) {
                _hideOnInit.push($scope.hideOnInit[hoiI]);
            }
        }
        $scope.hideOnInit = _hideOnInit;

        if ($ageCheckNoLabel.length) {
            $ageCheckNoLabel.tooltip({
                    trigger: 'click',
                    title: $scope.$parent.attrs.ageVerificationDialogMessage
            });
            $ageCheckNo.change(function (ev) {
                if ($(ev.target).is(':checked')) {
                    $ageCheckNoLabel.tooltip('show');
                } else {
                    $ageCheckNoLabel.tooltip('hide');
                }
            });
            $ageCheckYes.change(function (ev) {
                if ($(ev.target).is(':checked')) {
                    $ageCheckNoLabel.tooltip('hide');
                }
            });
        }

        fieldNames = [];
        for (var i = 0; i < $scope.ref.length; i++) {
            fieldNames.push($scope.ref[i].name);
            if ($scope.session[$scope.ref[i].name]) {
                $scope.$parent.dirtyForms.required = true;
            }
        }

        getBEErrors();

        rendered = true;
    };


    $scope.$on('step', function (ev, step) {
        if (rendered && step == 'required') {
            getBEErrors();
        }
    });


    $scope.submitFn = function(session, ref) {
        
        fieldNames = [];
        for (var i = 0; i < $scope.ref.length; i++) {
            fieldNames.push($scope.ref[i].name);
        }
        $scope.$parent.fieldList.required = fieldNames;
    
        $scope.$parent.dirtyForms.required = true;

        angular.extend($scope.$parent.tempProfile, session);
        $scope.$parent.nextStep('optional');
    };

}]);