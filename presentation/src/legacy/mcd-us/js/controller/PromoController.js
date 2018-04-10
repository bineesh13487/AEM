app.controller('PromoController', ['$scope', '$filter', function($scope, $filter) {
    $scope.promo = {};

    $scope.init = function(data, type) {
        if (type === 'random') {
			$scope.promo = $scope.shuffle(data);
        } else if (type === 'time') {
			$scope.promo = $scope.getCurrentCallout(data);
        }
    };

    $scope.shuffle = function(data) {
        return data[Math.floor(Math.random() * data.length)];
    }

        /* Function used to retrieve the callout applicable to the current time window */
    $scope.getCurrentCallout = function(data) {
        var callout;

        for(var i = 0; i < data.length; i++) {
            var obj = data[i];

            if (matchesTimeInterval(obj.startTime, obj.endTime)) {
                callout = obj;
                break;
            }
        }

        if (!callout) {
			var found = $filter('filter')(data, {defaultCallout: true}, true);

            if (found.length) {
				return found[0];
            } else {
				return data[0];
            }
        }

        return callout;
    };
}]);