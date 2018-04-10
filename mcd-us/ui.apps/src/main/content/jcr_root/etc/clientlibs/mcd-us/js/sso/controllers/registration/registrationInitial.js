ssoApp.controller('RegistrationInitialController', ['$scope', 'socialLoginService', 'temporaryNotificationCookie', function($scope, socialLoginService, temporaryNotificationCookie) {

    $scope.socialProfile = {value: {a:1}};

    $scope.useEmail = function () {
        $scope.$parent.reset();
        angular.extend($scope.$parent.tempProfile, {
            provider: undefined,
            type: 'email'
        });
        $scope.$parent.nextStep('required');
    };

    $scope.socialRegistrationFacebook = function () {
        var fbProfile = $scope.socialProfile.value.profile.profile;
        var gender;
        var birthMonth;
        var birthYear;
        var birthPieces;

        if (fbProfile.birthday) {
            birthPieces = fbProfile.birthday.split('/');
            if (birthPieces.length === 1) {
                birthYear = birthPieces[0];
            } else if (birthPieces.length === 2) {
                birthMonth = birthPieces[0];
            } else if (birthPieces.length === 3) {
                birthMonth = birthPieces[0];
                birthYear = birthPieces[2];
            }
        }

        if (fbProfile.gender === 'male') {
            gender = 'M';
        } else if (fbProfile.gender === 'female') {
            gender = 'F';
        }

        $scope.$parent.reset();
        angular.extend($scope.$parent.tempProfile, {
            email: fbProfile.email,
            firstName: fbProfile.first_name,
            lastName: fbProfile.last_name,
            gender: gender,
            birthMonth: birthMonth,
            birthYear: birthYear,
            provider: 'facebook',
            type: 'social',
            idpToken: fbProfile.token
        });

        $scope.$parent.nextStep('required');
    };

    $scope.socialRegistrationGoogle = function () {
        var gProfile = $scope.socialProfile.value.profile.profile;

        $scope.$parent.reset();
        angular.extend($scope.$parent.tempProfile, {
            email: gProfile.getEmail(),
            firstName: gProfile.getGivenName(),
            lastName: gProfile.getFamilyName(),
            provider: 'googleplus',
            type: 'social',
            idpToken: gProfile.token
        });

        $scope.$parent.nextStep('required');
    };

}]);