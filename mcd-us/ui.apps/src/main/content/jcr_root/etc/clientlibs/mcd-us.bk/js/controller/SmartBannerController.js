app.controller('SmartBannerController', ['$scope', '$cookies', 'marginService', '$window',
    function($scope, $cookies, marginService, $window) {
    $scope.defaults = {
        title: null, // What the title of the app should be in the banner (defaults to <title>)
        description: null, // Description of the app
        appStoreLanguage: 'us', // Language code for App Store
        icon: null, // The URL of the icon (defaults to <meta name="apple-touch-icon">)
        //iconGloss: true, // Force gloss effect for iOS even for precomposed
        button: 'VIEW', // Text for the install button
        url: null, // The URL for the button. Keep null if you want the button to link to the app store.
        daysHidden: 15, // Duration to hide the banner after being closed (0 = always show banner)
        daysReminder: 90, // Duration to hide the banner after "VIEW" is clicked *separate from when the close button is clicked* (0 = always show banner)
        force: null, // Choose 'ios', 'android' or 'windows'. Don't do a browser check, just always show this banner
        hideOnInstall: true, // Hide the banner after "VIEW" is clicked.
        layer: false, // Display as overlay layer or slide down the page
        iOSUniversalApp: true // If the iOS App is a universal app for both iPad and iPhone, display Smart Banner to iPad users, too.
    };

    $scope.banner;
    $scope.bannerHeight = 0;

    $scope.init = function(data) {
        $scope.banner = angular.extend($scope.defaults, data);
        $scope.initBanner();
    };

    angular.element($window).on('resize', function() {
        $scope.initBanner();
        $scope.$digest();
    });

    $scope.initBanner = function() {
        $scope.bannerHeight = 0;
        var UA = navigator.userAgent;
        var type;
        var appId;

        // Detect banner type (iOS or Android)
        if ($scope.force) {
            type = $scope.banner.force;
        } else if (UA.match(/Windows Phone/i) !== null && UA.match(/Edge|Touch/i) !== null) {
            type = 'windows';
            appId = $scope.banner.windowsId;
        } else if (UA.match(/iPhone|iPod/i) !== null || (UA.match(/iPad/) && this.options.iOSUniversalApp)) {
            type = 'ios';
            appId = $scope.banner.iosId;
        } else if (UA.match(/Android/i) !== null) {
            type = 'android';
            appId = $scope.banner.androidId;
        }

        if ($cookies.get('sb-closed') || $cookies.get('sb-installed') || !Modernizr.mq('(max-width: 767px)')) {
            $scope.banner.show = false;
            return;
        }

        if (!appId) {
            $scope.banner.show = false;
            return;
        } else {
            $scope.banner.show = true;
            $scope.banner.appId = appId;

            if (!$scope.banner.title) {
                $scope.banner.title = $('title').text().replace(/\s*[|\-·].*$/, '');
            }

            if (!$scope.banner.description) {
                $scope.banner.description = $scope.banner.title;
            }

            $scope.banner.link = ($scope.banner.url ? $scope.banner.url :
                (type === 'windows' ? 'ms-windows-store:navigate?appid=' :
                (type === 'android' ? 'http://play.google.com/store/apps/details?id=' :
                'https://itunes.apple.com/' + $scope.banner.appStoreLanguage + '/app/id')) + appId);

            $scope.bannerHeight = angular.element(document.getElementById('smartbanner')).outerHeight();
            marginService.addMargin($scope.bannerHeight);
        }
    };

    $scope.hideBanner = function($event) {
        $event.preventDefault();
        $scope.banner.show = false;
        marginService.reduceMargin($scope.bannerHeight);
        $scope.setCookie('sb-closed', 'true', $scope.banner.daysHidden);
        $('body').trigger('bannerClosed');
    };

    $scope.installApp = function() {
        if ($scope.banner.hideOnInstall) {
            $scope.banner.show = false;
            marginService.reduceMargin($scope.bannerHeight);
            $('body').trigger('bannerClosed');
        }
        $scope.setCookie('sb-installed', 'true', $scope.banner.daysReminder);
    };

    $scope.setCookie = function(name, val, expiresIn) {
        var expiry = new Date();
        expiry.setDate(expiry.getDate() + expiresIn);
        $cookies.put(name, val, {expires: expiry, path: '/'});
    }
}]);