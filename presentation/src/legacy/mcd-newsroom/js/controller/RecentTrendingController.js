/* eslint no-undef: 0 */
app.controller('RecentTrendingController', ['$scope', '$http', function RecentTrendingController($scope, $http) {
  let rtlFlow = false;
  let dir = 'ltr';

  if (window.location.href.indexOf('ar-') > -1) {
    rtlFlow = true;
    dir = 'rtl';
  }

  function getNewGridSize() {
    if (window.innerWidth < 700) {
      return 1;
    }
    return 3;
  }

  $('.newsFlexslider').css('direction', 'ltr');
  $('.newsFlexslider').flexslider({
    animation: 'slide',
    animationLoop: true,
    itemWidth: 125,
    itemMargin: 12,
    rtl: rtlFlow,
    minItems: getNewGridSize(),
    maxItems: getNewGridSize(),
    // pausePlay: true,
    start(slider) {
      slider.css('direction', dir);
      $('body').removeClass('loading');
    }
  });

  $('.component-newsroom-slider').closest('.columncontrol').find('.row:first > div').addClass('col-xs-12');

  $('.localTabActive').click(() => {
    localStorage.setItem('navMobActiveIndex', 1);
    localStorage.setItem('navActiveIndex', 1);
  });

}]);
