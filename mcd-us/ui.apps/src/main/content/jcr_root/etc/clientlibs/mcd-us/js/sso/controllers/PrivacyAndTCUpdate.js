ssoApp.controller('PrivacyAndTCUpdateController', ['$scope', '$element', 'temporaryNotificationCookie', 'config', 'customer', 
function($scope, $element, temporaryNotificationCookie, config, customer) {

    var privcytcupdatedata = $element.data('json');
    var _config = angular.fromJson($element.data('json').config);
    var privacyTermsField = $element.find('#checkbox-disclaimer');
    $scope.isChecked = false;

    var updateParams = {
        privacy: false,
        terms: false,
        combined: false,

        currentVersionPrivacy: '',
        currentVersionTC: ''
    };

    var active = false;

    $scope.tmplFields = {
        popuptitle: '',
        popupsubtitle: '',
        popupbutton: _config.proceedButtonTitle,

        privacylink: false,
        termslink: false
    };

    var currentPrivacyPolicyVersion = Number(_config.currentPrivacyPolicyVersion);
    var currentTCVersion = Number(_config.currentTermsAndConditionsVersion);

    customer.isUserLoggedIn().then(function (isLoggedIn) {
        if (isLoggedIn) {
            customer.getPrivacyPolicyandTCVersions().then(
                function (resp) {
                    var privacyPolicyVersion = '';
                    var tcVersion = '';

                    if (resp) {
                        active = true;
                        privacyPolicyVersion = Number(resp.privacyPolicy);
                        tcVersion = Number(resp.termsConditions);

                        if (tcVersion !== currentTCVersion && 
                            privacyPolicyVersion !== currentPrivacyPolicyVersion && 
                            currentTCVersion != '0' && currentPrivacyPolicyVersion != '0') {

                            $scope.tmplFields.popuptitle = _config.combinationUpdateFormTitle;
                            $scope.tmplFields.popupsubtitle = _config.combinationUpdateFormSubTitle;
                            $scope.tmplFields.combinedlink = true;

                            updateParams.terms = true;
                            updateParams.privacy = true;
                            updateParams.currentVersionPrivacy = currentPrivacyPolicyVersion;
                            updateParams.currentVersionTC = currentTCVersion;

                        } else if  (tcVersion !== currentTCVersion && 
                                    currentTCVersion !== '') {
                            
                            $scope.tmplFields.popuptitle = _config.termsAndConditionsUpdateFormTitle;
                            $scope.tmplFields.popupsubtitle = _config.termsAndConditionsUpdateFormSubTitle;
                            $scope.tmplFields.termslink = true;

                            updateParams.terms = true;
                            updateParams.currentVersionTC = currentTCVersion;
                            
                        } else if  (privacyPolicyVersion !== currentPrivacyPolicyVersion && 
                                    currentPrivacyPolicyVersion !== '') {
                            
                            $scope.tmplFields.popuptitle = _config.privacyPolicyUpdateFormTitle;
                            $scope.tmplFields.popupsubtitle = _config.privacyPolicyUpdateFormSubTitle;
                            $scope.tmplFields.privacylink = true;

                            updateParams.privacy = true;
                            updateParams.currentVersionPrivacy = currentPrivacyPolicyVersion;

                        } else {

                            active = false;
                            return;
                        }

                        $('#sso-tc-modal').modal();
                    }
                },
                function () {
                    temporaryNotificationCookie.show('errorGeneric');
                }
            );
        }
    });

    $scope.submitPolicyUpdate = function() {
        if (active) {
            customer.updateProfile(updateParams).then(
                function (resp) {
                    // do nothing temporaryNotificationCookie.show('accountUpdatedPersonalInformation');
                },
                function () {
                    temporaryNotificationCookie.show('errorGeneric');
                }
            );
        }
    };

    $scope.toggleCheckPrivacyTerms = function () {
        $scope.isChecked = !privacyTermsField.is(':checked');
    };

}]);