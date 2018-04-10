app.controller('heroController', ['$scope', '$http','getCoop','$cookies', function($scope, $http,getCoop,$cookies) {
    $scope.isLocalUrl;
    $scope.showCoopName;
    var coop;
    var coopName;
    $scope.init = function(url,showCoopName) {
        $scope.isLocalUrl = url;
        $scope.showCoopName = showCoopName;
        coop = getCoop.getCoopId();
        coopName = getCoop.getCoop();
        if ($scope.isLocalUrl) {
            if (coop && $cookies.get("LocalItemsCount") > 0) {
                $('.component-hero a').show();
            }
        } else {
			$('.component-hero a').show();
        }

        if ($scope.showCoopName && coopName) {
            // showCoopName is selected and user has shared his info so replacing title with coopName
            $('.component-hero h1').text(coopName);
        }
    };
}]);
