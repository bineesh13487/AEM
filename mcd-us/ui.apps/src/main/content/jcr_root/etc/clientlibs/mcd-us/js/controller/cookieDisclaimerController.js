
/* Cookie Policy Disclaimer */
app.controller('cookieDisclaimerController', ['$scope', '$location', 'GetPathForCookie', function ($scope,$location,GetPathForCookie) {
    $scope.init = function () {
        var acceptCookies = $.cookie("acceptCookies");
        if (acceptCookies) {
            $(".cookie-message").hide();
            display = 'none'
        }
        else {
            $(".cookie-message").show();
            display = 'block'
        }
        var cbannerHeight = $(".cookie-message").outerHeight();
        $('body').addClass("cookie-banner");
        //$('body').css("margin-top", "0");
    }
    $scope.setCookieBannercookie = function (e) {
    	//var countrycode = $location.path().split("/"+config.get("country").toLowerCase()+"/")[0] + "/"+config.get("country").toLowerCase()+"/";
    	var countrycode = GetPathForCookie.setPathForCookie($location.path());
    	var cookieDisclaimerExpiryInDays = $(".cookieDisclaimerExpiryInDays").text();
        var expiryDate = new Date(Number(new Date()) + cookieDisclaimerExpiryInDays * 24 * 60 * 60 * 1000);
        $.cookie("acceptCookies", true, {
            path: countrycode
            , HttpOnly: true
            , Secure: true
            , expires: expiryDate
        });
        e.preventDefault();
        $('body').removeClass("cookie-banner");
        //$('body').css("margin-top", "0");
        //$('.cookie-message').slideUp(200);
        $('header a:not(.not-first)').first().focus();
    }
}]);
