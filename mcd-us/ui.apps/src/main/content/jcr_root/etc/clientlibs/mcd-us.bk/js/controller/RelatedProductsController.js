app.controller('relatedProductsController', ['$scope', '$http','getCoop','$interval', function($scope, $http,getCoop,$interval) {
    $scope.categoryId;
    $scope.productId;
    $scope.items = [];
    var coop;
    $scope.init = function(id, itemID) {
        $scope.productId = itemID;
        $scope.categoryId = id;
        var showLiveData=$(".showLiveData").data("showlive-value");
        //coop to be obtained from cookie
        coop=getCoop.getCoopId();
        if(!(coop)){
			coop='';
        }
        if($scope.categoryId !=0){
            var selectorString="." + config.get("country") + "." + config.get("lang") + "."
                + $scope.categoryId + "." + showLiveData + "." + "true" + "." + coop + ".json";
            $http.get("/services/mcd/categoryDetails"+selectorString).then(function(response) {
                if(response.data.category){
                    $scope.title = response.data.category.marketingName;
                    $scope.filterItems(response.data.category.items);
                }
            }).then(function() {
                $(".loadingDiv_" + id).hide();
            });
        }
    };
    
    setTimeout(function(){ 
 	   $(".related-flexslider").append('<ul class="flex-direction-nav"><li class="flex-nav-prev active-slides"><a class="flex-prev" href="#">Previous</a></li><li class="flex-nav-next active-slides"><a class="flex-next" href="#">Next</a></li></ul>');
 				 $(".related-flexslider").find(".flex-viewport").find("li:first").addClass("first-focus");
 		var sliderIndex = function () {
 		   //$(".related-flexslider").find(".custom-navigation").find("a").removeClass( "flex-disabled");
 		   //$(".related-flexslider").find(".custom-navigation").find("a").attr('tabindex',"0");
 			
 			$(".related-flexslider").find(".flex-custom-nav").find("a").removeClass( "flex-disabled");
            $(".related-flexslider").find(".flex-custom-nav").find("a").attr('tabindex',"0");
 		  $(".related-flexslider").find(".flex-viewport").find("li").attr('tabindex',"-1");
 		  var $window = $(window);
 		  var windowsize = $window.width();
 		 // if (windowsize > 767) {
 			$(".related-flexslider").find(".flex-viewport").find("li").find("a").attr('tabindex', "0");
 	//	}

 	}

 $interval(sliderIndex, 1000);   
 		}, 1000);

    $scope.filterItems = function(items) {
        angular.forEach(items, function(value, key) {
            if (value.itemVisibility && (value.externalId != $scope.productId)) {
                $scope.items.push(value);
            }
        });
    }
}]);