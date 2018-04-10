ssoApp.controller('NewsletterSubscribeController', ['$scope', '$window', 'customer', 'temporaryNotificationCookie', '$attrs', 'config',
    function($scope, $window, customer, temporaryNotificationCookie, $attrs, configService) {

    var redirectUrl = $attrs.redirect || "/";
    var jsonObject = $attrs.json || {};
    var json = JSON.parse(jsonObject);
    var genericError = configService.get('errorGeneric') || 'Generic Error';

    $scope.preloader = false;

    $scope.formName = 'NewsletterSubscribe';
    $scope.session = {};
    $scope.ref = [];
    $scope.buttonText = 'Proceed';
    $scope.errorMsg = {
        msg: '',
        is: false
    };

    $scope.backedValidationFailedFields = [];

    //if a user is logged in, redirect to the Profile page (but only when not in Edit or Preview mode)
    customer.isUserLoggedIn().then(function(bool) {
        if (bool && !configService.get('inEditorOrPreview')) {
            $window.location = json.manageSubscriptionHref;
        }
    }, function(err) {
        console.log(err);
    });

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

    function getBEErrors () {
        if ($scope.backedValidationFailedFields.length) {
            var _beField;
            for (var beErrorsI = 0; beErrorsI < $scope.backedValidationFailedFields.length; beErrorsI++) {
                _beField = $scope.backedValidationFailedFields[beErrorsI];
                if (fieldNames.indexOf(_beField) > -1) {
                    $scope.thisForm.NewsletterSubscribe[_beField].$setValidity('required', false);
                    $scope.thisForm.NewsletterSubscribe[_beField].$setTouched();
                }
            }
            $scope.$broadcast('scrollToErrors');
        }
    }

    $scope.$on('clearBEVError', function (ev, fieldName) {
        if ($scope.thisForm.NewsletterSubscribe[fieldName]) {
            $scope.thisForm.NewsletterSubscribe[fieldName].$setValidity('invalid', undefined);
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

    $scope.submitFn = function(session, ref) {
        $scope.preloader = true;
        var detailArray;
        var req = customer.newsletterSubscribe($scope.session);
        $scope.backedValidationFailedFields = [];
        req.then(
            function (resp) {
                $scope.preloader = false;
                if ((resp.data.statusCode && resp.data.statusCode  === '11011') || (resp.data.statusCode && resp.data.statusCode  === '11931')) {
                    //user successfully subscribed or a subscription account already exists
                    temporaryNotificationCookie.set('userSubscribed');
                    window.location = redirectUrl;
                } else if (resp.data.statusCode && resp.data.statusCode  === '11928') {
                    $scope.errorMsg.msg = json['error' + resp.data.statusCode] || genericError;
                    $scope.errorMsg.is = true;
                } else if (resp.data.statusCode == '10120') {
                    detailArray = [];
                    for (var i = 0; i < resp.data.details.length; i++) {
                        if (resp.data.details[i].Field == 'zipCode') {
                            detailArray.push('postalCode');
                        }
                        if (resp.data.details[i].Field == 'loginUsername') {
                            detailArray.push('email');
                        }
                    }
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
                    $scope.errorMsg.msg = genericError;
                    $scope.errorMsg.is = true;
                }
            },
            function (resp) {
                $scope.preloader = false;
                $scope.errorMsg.msg = genericError;
                $scope.errorMsg.is = true;
            }
        );
    };

}]);