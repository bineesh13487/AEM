app.controller('carouselController', ['$scope', '$http', 'getCoop', function($scope, $http, getCoop) {

	$scope.slides = {};
    $scope.localSlides = [];
    var coop=getCoop.getCoopId();

    $scope.Prev=function(){
        //$('.home-flexslider').flexslider("prev");
	};
	$scope.Next=function(){
	    //$('.home-flexslider').flexslider("next");
	};

    setTimeout(function(){
        $("#homeCarouselComponent").find(".flex-direction-nav").css("display","none");
				/* Start: DEPS-24357 - This line has moved to scripts.js */
				// $('.custom-paging li:first-child a').addClass('flex-active');
				/* End */
    }, 1500);


    $scope.init = function(path) {
			$http.get("/services/mcd/localGallerySlide.json?coopID=" + coop +"&carouselPath=" +path)
			.then(function(response) {
				$scope.slides = response.data.slides;
				for (var count = 0; count < $scope.slides.imagegallery.length; count++) {
					$scope.localSlides.push($scope.slides.imagegallery[count]);
				}
			});
    };
}]);

app.directive('onSlideFinishRender', ['$timeout' , function ($timeout) {
    return {
        restrict: 'A',
        link: function (scope) {
            if (scope.$last === true) {
                $timeout(function () {
                    //scope.$emit('ngLocalSlidesFinished');
                    videosList.initHomeYoutubeAPICalls();
					videosList.initFlexsliders();
                },100);
            }
        }
    }
}]);
