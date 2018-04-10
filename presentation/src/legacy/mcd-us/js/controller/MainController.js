app.controller('MainController', ['$scope', 'marginService', '$http', 'getCoop', '$cookies','$location','GetPathForCookie',
    function($scope, marginService, $http, getCoop, $cookies, $location, GetPathForCookie) {
    $scope.marginTop = marginService.getMargin();
    $scope.flyoutMargin = marginService.getFlyoutMargin();

    $scope.$on('handleMargins', function() {
        $scope.marginTop = marginService.getMargin();
        $scope.flyoutMargin = marginService.getFlyoutMargin();
    });

    $scope.setCookieData = function(coop){
    	//var countrycode = $location.path().split("/"+config.get("country").toLowerCase()+"/")[0] + "/"+config.get("country").toLowerCase()+"/";
    	var countrycode = GetPathForCookie.setPathForCookie($location.path());
    	if(coop){
            //coop is available and we would be finding that whether Local item exists for this coop or not.
            $http.get("/services/mcd/localItemsList.json?country=" + config.get("country")
                + "&language=" + config.get("lang") + "&coopID=" + coop).then(function(response) {
                $scope.localItemList = response.data.items;
                $cookies.put('LocalItemsCount', $scope.localItemList.length ,{path:countrycode});
                $cookies.put("CoopIdAvailable",coop,{ path:countrycode});
                if($cookies.get("LocalItemsCount")>0) {
                			angular.forEach(angular.element(".localitemimagelist"), function(value, key){
                		        var a = angular.element(value);
                        		a.removeClass("localitemimagelist");
                		   	});
                }
            });
        }
    };

    var coop=getCoop.getCoopId();
    //check if cookie has information available for coopid
    if($cookies.get("CoopIdAvailable")){
        //coop id already there in cookie so check if latest and one in cookie is same or not
        if($cookies.get("CoopIdAvailable")!=coop){
			//update cookie with count of latest cookie.
            $scope.setCookieData(coop);
        }else{
        	if($cookies.get("LocalItemsCount")>0) {
        			angular.forEach(angular.element(".localitemimagelist"), function(value, key){
        		        var a = angular.element(value);
                		a.removeClass("localitemimagelist");
        		   	});
        		}
        }
    }else{
		//no coop in cookie approach same
        $scope.setCookieData(coop);
    }
}]);

