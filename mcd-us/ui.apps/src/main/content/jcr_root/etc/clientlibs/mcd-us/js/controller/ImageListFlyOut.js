app.controller('imageListFlyOut',['$scope','$cookies','getCoop', function($scope, $cookies, getCoop) {
	
	var coop=getCoop.getCoopId();
	
	if(coop){
		
		angular.forEach(angular.element(".localeventitem"), function(value, key){
	        var a = angular.element(value);
	        if (a.attr('data-coopid')) {
	        	if (a.attr('data-coopid').indexOf(",") >= 0) {
	        		var arr = a.attr('data-coopid').split(",");
	        		for (var z= 0; z < arr.length; z++) {
	        			if (arr[z]==coop) {
	        				a.removeClass("localeventitem");
	        			}
	        		}
	        	}
	        	else {
	        		if (a.attr('data-coopid')==coop) {
	        			a.removeClass("localeventitem");
	        		}
	        	}
	        }
	        
	   	});
	}
	
	
	
	angular.forEach(angular.element(".imglist-topnav"), function(value, key){
		var a = angular.element(value);
		var count = 0;
		angular.forEach(angular.element(a).find("li"), function(value1, key1){
			var b = angular.element(value1);
			if (b.css('display') !== 'none' && count==0) {
				count++;
				b.find(".visible-xs-block").css("border-top","0px");
			}
		});
	
   	});
   
}]);