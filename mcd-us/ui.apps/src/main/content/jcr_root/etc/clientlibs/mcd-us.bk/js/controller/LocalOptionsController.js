app.controller('localOptionsController', ['$scope', '$http','getCoop', function($scope, $http,getCoop) {
    $scope.itemsList = {};
    var coop;
    $scope.init = function() {
       var coopErrorMsg = $(".coopErrorMsg").data("errormsg-id");
        var showLiveData=$(".showLiveData").data("showlive-value");
    //here coop will be pick from Cookies
        coop = getCoop.getCoopId();
        if (coop) {
            $http.get("/services/mcd/localItemsList.json?country=" + config.get("country") + "&language=" + config.get("lang") + "&coopID=" + coop+"&showLiveData="+showLiveData)
                .then(function(response) {
                    $scope.itemsList = response.data.items;
                    if (!($scope.itemsList.length > 0)) {
                    //if no items are returned for a coop than show the error msg div
                        $(".cont-banner-itemError-msg").show();

                    }

                });

        } else {
        //If no coop information available than show no coop available error Msg.
        if(coopErrorMsg){
          $(".cont-banner-coopError-msg").show();
        }
        }
    };






}]);

app.directive("zoomDirective", function() {
    return function($scope, $element, $attr) {
        if ($scope.$last) {
            initZoomAnim('.zoom-anim', '.zoom-anim-target', 'bglarge', '.zoom-anim-parent');
        }
    }
});

function initZoomAnim(triggerElem, targetElem, animClass, parentElem) {

        function zoomInBg() {
            if (parentElem !== undefined) {
                $(this).closest(parentElem).find(targetElem).addClass(animClass);
            } else {
                $(targetElem).addClass(animClass);
            }
        }


        function zoomOutBg() {
            if (parentElem !== undefined) {
                $(this).closest(parentElem).find(targetElem).removeClass(animClass);
            } else {
                $(targetElem).removeClass(animClass);
            }
        }

        $(triggerElem).unbind('mouseenter mouseleave');
        $(triggerElem).hover(zoomInBg, zoomOutBg);
    }



