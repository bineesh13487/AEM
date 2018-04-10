app.factory('marginService', ['$rootScope', '$window', function ($rootScope, $window) {
    var shared = {};

    //shared.margin = document.getElementById('maincontent').offsetTop;
    shared.flyoutMargin = Modernizr.mq('(max-width: 767px)') ? 90 : 0;

    angular.element($window).on('resize', function() {
        //shared.setMargin(document.getElementById('maincontent').offsetTop);
        shared.setFlyoutMargin(Modernizr.mq('(max-width: 767px)') ? 90 : 0);
        $rootScope.$digest();
    });

    shared.getMargin = function() {
        return shared.margin;
    };

    shared.getFlyoutMargin = function() {
        return shared.flyoutMargin;
    };

    shared.setMargin = function(newMargin) {
        shared.margin = newMargin;
        $rootScope.$broadcast('handleMargins', shared.margin);
    };

    shared.setFlyoutMargin = function(newMargin) {
        shared.flyoutMargin = newMargin;
        $rootScope.$broadcast('handleMargins', shared.margin);
    };

    shared.addMargin = function(extraMargin) {
        shared.margin += extraMargin;
        shared.flyoutMargin += extraMargin;
        $rootScope.$broadcast('handleMargins', shared.margin);
    };

    shared.reduceMargin = function(extraMargin) {
        shared.margin = shared.margin - extraMargin;
        shared.flyoutMargin = shared.flyoutMargin - extraMargin;
        $rootScope.$broadcast('handleMargins', shared.margin);
    };

    return shared;
}]);