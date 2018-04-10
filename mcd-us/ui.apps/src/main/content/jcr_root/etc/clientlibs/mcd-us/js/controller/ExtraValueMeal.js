app.controller('evmController',['$scope','$cookies','getCoop', function($scope, $cookies, getCoop) {
	
    $scope.addToNc = function(ids) {
		
		if (ids!=null && ids!="") {
			console.log("---"+ids);
	        
			
            $cookies.put('MyMealItems', ids,{ path: '/' });
	        
	       
	        console.log($cookies.get("MyMealItems"));
		}
    };
    
    
    var coop=getCoop.getCoopId();
	
	if(coop){
		
		angular.forEach(angular.element(".evmcoop"), function(value, key){
		        var a = angular.element(value);
		        if (a.attr('data-coopid')) {
		        	if (a.attr('data-coopid').indexOf(",") >= 0) {
		        		var arr = a.attr('data-coopid').split(",");
		        		for (var z= 0; z < arr.length; z++) {
		        			if (arr[z]==coop) {
		        				a.show();
		        			}
		        		}
		        	}
		        	else {
		        		if (a.attr('data-coopid')==coop) {
		        			a.show();
		        		}
		        	}
		        }
		        
		        console.log(a.attr('data-coopid'));
		});
	}
    if($(".tabs-container").find("li:first").hasClass("active")){
        $(".tabs-container").find("li:first").find("a").attr("aria-expanded", "true");
    }
    $(".tabs-container").find("li:not(.active)").find("a").attr("aria-expanded", "false");

   
   
}]);