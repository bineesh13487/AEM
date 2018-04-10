app.controller('imageGalleryController', ['$scope','$interval', function($scope,$interval) {
    setTimeout(function(){
 	   $(".related-flexslider").append('<ul class="flex-direction-nav"><li class="flex-nav-prev active-slides"><a class="flex-prev" href="#">Previous</a></li><li class="flex-nav-next active-slides"><a class="flex-next" href="#">Next</a></li></ul>');
 				 $(".related-flexslider").find(".flex-viewport").find("li:first").addClass("first-focus");
 		var sliderIndex = function () {


 			$(".related-flexslider").find(".flex-custom-nav").find("a").removeClass( "flex-disabled");
            $(".related-flexslider").find(".flex-custom-nav").find("a").attr('tabindex',"0");
 		  $(".related-flexslider").find(".flex-viewport").find("li").attr('tabindex',"-1");
 		  var $window = $(window);
 		  var windowsize = $window.width();
 			$(".related-flexslider").find(".flex-viewport").find("li").find("a").attr('tabindex', "0");

 	}

 $interval(sliderIndex, 1000);
 		}, 1000);


}]);