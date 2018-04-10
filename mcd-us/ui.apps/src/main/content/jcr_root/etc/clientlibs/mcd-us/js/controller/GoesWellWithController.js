app.controller('goesWellWithController', ['$scope', '$http', 'getCoop', '$interval', function ($scope, $http, getCoop, $interval) {
  $scope.itemId;
  $scope.itemsList = {};
  let coop;
  let isAuthorMode;
  $scope.init = function (id) {
    $scope.itemId = id;
    const showLiveData = $('.showLiveData').data('showlive-value');
    // coop to be obtained from cookies
    coop = getCoop.getCoopId();
    if (!coop) {
      coop = '';
    }

    const authorStr = $('.goes-well-with').data('mode');
    if (authorStr) {
      const strArr = authorStr.split('_');
      isAuthorMode = strArr[1];
    }

    $http.get(`/services/mcd/goesWellWithList.json?country=${  config.get('country')  }&language=${ 
      config.get('lang')  }&itemID=${  $scope.itemId  }&showNationalCoopType=true&coopID=${ 
      coop  }&showLiveData=${  showLiveData}`).then(response => {
      $scope.itemsList = response.data.items;
      if ($scope.itemsList) {
        if (!($scope.itemsList.length > 0)) {
          if (isAuthorMode == 'false') {
            $('.goes-well-with').hide();
          }
        }
      }
 else {
        // in case itemlist come out to be undefined than hide slider on publish
        if (isAuthorMode == 'false') {
          $('.goes-well-with').hide();
        }
      }
    }).then(() => {
      $('.loadingDiv').hide();
      setTimeout(() => {
        $(".related-flexslider").append('<ul class="flex-direction-nav"><li class="flex-nav-prev active-slides"><a class="flex-prev" href="#">Previous</a></li><li class="flex-nav-next active-slides"><a class="flex-next" href="#">Next</a></li></ul>');
        $(".related-flexslider").find(".flex-viewport").find("li:first").addClass("first-focus");
        var sliderIndex = function () {
          // $(".related-flexslider").find(".custom-navigation").find("a").removeClass( "flex-disabled");
          //$(".related-flexslider").find(".custom-navigation").find("a").attr('tabindex',"0");
          $(".related-flexslider").find(".flex-custom-nav").find("a").removeClass("flex-disabled");
          $(".related-flexslider").find(".flex-custom-nav").find("a").attr('tabindex', "0");

          $(".related-flexslider").find(".flex-viewport").find("li").attr('tabindex', "-1");
          var $window = $(window);
          var windowsize = $window.width();
          if (windowsize > 767) {
            $(".related-flexslider").find(".flex-viewport").find("li").find("a").attr('tabindex', "0");
          }
        }

        $interval(sliderIndex, 1000);
      }, 1000);

    });
  };
}]);
