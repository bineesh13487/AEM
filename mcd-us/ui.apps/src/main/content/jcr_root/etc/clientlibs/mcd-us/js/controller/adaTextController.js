app.controller('adaTextController', ['$scope', function($scope) {
    $scope.adaGenericText;

    $scope.getAdaGenericText = function(data) {
    	if($('#adaTextJson') && $('#adaTextJson').attr('data-ada-json')){
    		$scope.adaGenericText = JSON.parse($('#adaTextJson').attr('data-ada-json'));
    	}
    };
}]);