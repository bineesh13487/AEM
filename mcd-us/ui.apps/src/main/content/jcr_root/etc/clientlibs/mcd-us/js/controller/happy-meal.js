app.controller('happyMealController', ['$scope', '$cookies', 'getCoop', '$interval', '$element', function($scope, $cookies, getCoop, $interval, $element) {

    $scope.addToNc = function(e) {

        var items = "";
        var slides = angular.element(e.target).parents(".page-happy-meal").find(".slides");

        angular.forEach(angular.element(slides), function(value, key) {
            var a = angular.element(value);
            if (a.find(".active").attr("data-id")) {
                items = items + a.find(".active").attr("data-id") + ",";
            }
        });


        if (angular.element(e.target).attr("data-defaultitems")) {
            items = items + angular.element(e.target).attr("data-defaultitems");
        }

        items = items.trim();
        items = items.replace(/[,]\s*$/, "");
        console.log(items);




        $cookies.put('MyMealItems', items, {
            path: '/'
        });

    };

    var coop = getCoop.getCoopId();
    setTimeout(function() {
        $(".related-flexslider").append('<ul class="flex-direction-nav"><li class="flex-nav-prev active-slides"><a class="flex-prev" href="#">Previous</a></li><li class="flex-nav-next active-slides"><a class="flex-next" href="#">Next</a></li></ul>');
        $(".related-flexslider").find(".flex-viewport").find("li:first").addClass("first-focus");
        var sliderIndex = function() {
            $(".related-flexslider").find(".custom-navigation").find("a").removeClass("flex-disabled");
            $(".related-flexslider").find(".custom-navigation").find("a").attr('tabindex', "0");
            $(".related-flexslider").find(".flex-viewport").find("li").attr('tabindex', "-1");
            var $window = $(window);
            var windowsize = $window.width();
            if (windowsize > 767) {
                $(".related-flexslider").find(".flex-viewport").find("li").find("a").attr('tabindex', "0");
            }

        }

        $interval(sliderIndex, 1000);
    }, 1000);

    if (coop) {
        angular.forEach(angular.element(".happymeallocalitem"), function(value, key) {
            var a = angular.element(value);
            if (a.attr('data-coopid')) {
                if (a.attr('data-coopid').indexOf(",") >= 0) {
                    var arr = a.attr('data-coopid').split(",");
                    for (var z = 0; z < arr.length; z++) {
                        if (arr[z] == coop) {
                            a.removeClass("happymeallocalitem");
                        }
                    }
                } else {
                    if (a.attr('data-coopid') == coop) {
                        a.removeClass("happymeallocalitem");
                    }
                }
            }
        });
    }
    
    $($element.find(".related-flexslider")).each(function(index){
    	var slider = $(this);
    	$($(this).find("li")).each(function(index1){
    		if ($(this).hasClass("happymeallocalitem")) {
    			//slider.data('flexslider').removeSlide(index1);
    			$(this).remove();
    		}
    	});
    });

    if ($(".tabs-container").find("li:first").hasClass("active")) {
        $(".tabs-container").find("li:first").find("a").attr("aria-expanded", "true")
    }

    $(".tabs-container").find("li:not(.active)").find("a").attr("aria-expanded", "false");


}]);