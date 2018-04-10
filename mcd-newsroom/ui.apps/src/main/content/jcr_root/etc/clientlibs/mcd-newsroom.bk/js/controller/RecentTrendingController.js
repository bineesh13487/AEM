
app.controller('RecentTrendingController',['$scope','$http',function($scope, $http) {

            var rtlFlow=false,dir="ltr";
             if (window.location.href.indexOf("ar-") > -1){
                rtlFlow=true;
                dir="rtl";
            };
			$scope.newsType;
			$scope.folderPath;


			    function getNewGridSize() {
                     if (window.innerWidth < 700) {
                        return 1;
                     }
                     else {
                         return 3;
                     }

                 }
                 $('.newsFlexslider').css("direction","ltr")
                $('.newsFlexslider').flexslider({
                      animation: "slide",
                      animationLoop: true,
                      itemWidth: 125,
                      itemMargin:12,
                      rtl: rtlFlow,
                      minItems: getNewGridSize(),
                      maxItems: getNewGridSize(),
                     // pausePlay: true,
                      start: function(slider){
                           slider.css("direction",dir);
                        $('body').removeClass('loading');
                      }
                    });

             $(".component-newsroom-slider").closest(".columncontrol").find(".row:first > div").addClass("col-xs-12");

              $(".localTabActive").click(function(){
                   localStorage.setItem("navMobActiveIndex",1);
                     localStorage.setItem("navActiveIndex",1);
              });

		} ]);