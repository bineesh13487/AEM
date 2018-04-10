app.controller('OffersController', ['$scope', '$http','getCoop','$cookies',
    function($scope, $http, getCoop, $cookies) {

    $scope.offers = {};
    $scope.hasMoreOffers = false;
    $scope.size = 0;
    $scope.currentRow = 1;
	$scope.elem;

    $scope.init = function(data) {
        $scope.currentRow = data.limit;
        var itemCount = $scope.currentRow * 3;
        var locations = eval(data.locations);
        var resource = data.resource;
        var coop = getCoop.getCoopId();
        var pos = data.pos;

        if (!pos || isNaN(parseInt(pos))) {
            pos = 0;
        }

        if (!isNaN(parseInt(data.size))) {
            $scope.size = parseInt(data.size);
        }

        $scope.elem = $(data.id);
        $($scope.elem).find(".col-sm-4.large:lt(" + itemCount + ")").show();
        $($scope.elem).find(".col-sm-4.large:gt(" + (itemCount - 1) + ")").hide();

        if ($scope.size > itemCount) {
            $scope.hasMoreOffers = true;
        }

        for (var i = 0; i < locations.length; i++) {
            var location = locations[i];
            if (coop == location.substring(location.lastIndexOf('/') + 1)) {
                fetchLocalOffers(coop, resource, location, pos);
                break;
            }
        }
    };

    function fetchLocalOffers(coopId, resource, location, pos) {
        $http.get(resource + ".personalize.json?location=" + location + "&pos=" + pos)
            .then(function(response) {
            $scope.offers = response.data.offers;
            $scope.size += $scope.offers.length;
        });
    }

    $scope.loadMore = function(rowCount) {
        $scope.currentRow += rowCount;
        var itemCount = $scope.currentRow * 3;
        $($scope.elem).find(".col-sm-4.large:lt(" + itemCount + ")").show();
        $($scope.elem).find(".col-sm-4.large:gt(" + (itemCount - 1) + ")").hide();
        if ($scope.size > itemCount) {
            $scope.hasMoreOffers = true;
        } else {
            $scope.hasMoreOffers = false;
        }
    }
}]);
