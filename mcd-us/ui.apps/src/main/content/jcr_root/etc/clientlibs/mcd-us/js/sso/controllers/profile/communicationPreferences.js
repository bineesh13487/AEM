ssoApp.controller('CommunicationPreferencesController', ['$scope', 'customer', 'temporaryNotificationCookie', '$attrs', '$element', 'config',
    function($scope, customer, temporaryNotificationCookie, $attrs, $element, configService) {
        var genericError = configService.get('errorGeneric') || 'Generic Error';
        $scope.session = {data:{}};
        var _profileCache = {};
        $scope.preloader = false;
        $scope._isDisabled = true;

        $scope.errorMsg = {
            msg: '',
            is: false
        };

        function enable () {
            $scope._isDisabled = false;
        }

        function scrollTop () {
            var top = 0;
            var $e = $element.parent('.ssoMainContent');
            if ($e.length) {
                top = Math.max($e.offset().top - 115, 0);
            }
            window.aemScrollTo(top);
        }

        customer.getAll().then(
            function (profile) {
                $scope.session.data = angular.copy(profile);
                _profileCache = angular.copy(profile);
                enable();
            },
            function () {
                enable();
            }
        );

        $scope.submitForm = function () {
            $scope.preloader = true;

            var _profile = {};
            var _changes = false;

            if (_profileCache.sendSMS != $scope.session.data.sendSMS) {
                _profile.sendSMS = $scope.session.data.sendSMS;
                _changes = true;
            }
            if (_profileCache.emailPromotions != $scope.session.data.emailPromotions) {
                _profile.emailPromotions = $scope.session.data.emailPromotions;
                _changes = true;
            }
            if (_profileCache.pushNotifications != $scope.session.data.pushNotifications) {
                _profile.pushNotifications = $scope.session.data.pushNotifications;
                _changes = true;
            }

            if (_changes) {
                customer.updateProfile(_profile).then(
                    function (resp) {
                        $scope.preloader = false;
                        if (resp.data.statusCode == "11011") {
                            temporaryNotificationCookie.show('accountUpdatedCommunicationPreferences');
                            $scope.$parent.nextStep('profileMenu');
                        }
                    },
                    function () {
                        $scope.preloader = false;
                        $scope.errorMsg = $scope.errorMsg || {};
                        $scope.errorMsg.msg = genericError;
                        $scope.errorMsg.is = true;
                        scrollTop();
                    }
                );
            } else {
                $scope.$parent.nextStep('profileMenu');
            }
        };

    }
]);