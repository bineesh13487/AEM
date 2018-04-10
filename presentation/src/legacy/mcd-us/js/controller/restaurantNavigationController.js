app.controller("restaurantNavigationController", ['$scope','$http', function($scope, $http) {

	    $scope.restaurantSearchText='';
	    $scope.latitude;
	    $scope.longitude;
	    $scope.showRestaurantsForCurrentLocation = function() {
			var form = $(event.target).parents('form:first');

	    	var input = $("<input>")
            .attr("type", "hidden")
            .attr("name", "locate").val("true");
			$(form).append($(input));
			
			var radiusSelected;
			$(form).find('.dropdown-toggle').each(function(){
				radiusSelected = parseInt($(this).text());
				})

				

			var input1 = $("<input>")
            .attr("type", "hidden")
            .attr("name", "radiusSelected").val(radiusSelected);
			$(form).append($(input1));
			
			
	      /*  if (navigator.geolocation) {
	            navigator.geolocation.getCurrentPosition(showPosition, showError);
	        } else {
	            console.log("Geolocation is not supported by this browser.");
	        };*/
	       
			
	    };
	    $scope.init = function ()  {
	    	var distanceUnit;
	    	 if ($("div#hiddenDistanceUnit").length > 0) {
		            distanceUnit = $("div#hiddenDistanceUnit").data("distance-unit");
	                $scope.distanceUnit=distanceUnit;
		        }
		        $('.dropdown-menu.dropdown-menu-right li').on('click', function() {
		            $('.btn.btn-default.dropdown-toggle').val($(this).text());
		            $('.btn.btn-default.dropdown-toggle').text($(this).text());
		            $scope.restaurantRadius = parseInt($(this).text());
		        });
		    
	    }
		
		$scope.submit = function() {
		console.log('submit');
		return false;
		}
		$scope.searchRestaurants=function(restaurantSearchText) {
			if(restaurantSearchText==undefined || restaurantSearchText == '') {
				event.preventDefault();
				alert("Please enter a zip code or city & state to search");
				return false;
			}
			var form = $(event.target).parents('form:first');
			var input = $("<input>")
            .attr("type", "hidden")
            .attr("name", "submit").val("true");
			var radiusSelected;
			$(form).find('.dropdown-toggle').each(function(){
				radiusSelected = parseInt($(this).text());
				})
			$(form).append($(input));
			var input1 = $("<input>")
            .attr("type", "hidden")
            .attr("name", "radiusSelected").val(radiusSelected);
			$(form).append($(input1));
			var input2 = $("<input>")
            .attr("type", "hidden")
            .attr("name", "searchText").val(restaurantSearchText);
			$(form).append($(input2));
	
		}
		
	
	
	}]);