app.controller('redirectController', ['$scope', '$http', 'getCoop','$cookies', function($scope, $http, getCoop,$cookies) {
   var isCoopIn=false;
    var coop;
    var isAuthorMode;
    $scope.coopInUrl;
    $scope.coopOutUrl;
    $scope.dealPageUrl;
    $scope.init = function(coopData, dealPageUrl, coopInUrl, coopOutUrl,isEvaluate) {
        console.log(coopData);
        $scope.dealPageUrl=dealPageUrl;
         $scope.coopInUrl=coopInUrl;
         $scope.coopOutUrl=coopOutUrl;
        $scope.isEvaluate = isEvaluate;

        var authorStr = $(".page-redirect-template").data("mode");
        if (authorStr) {
            var strArr = authorStr.split("_");
            isAuthorMode = strArr[1];
        }
        if (isAuthorMode == "false") {       //redirect functionality will work only on publish and not on author
            if($scope.isEvaluate ==='true'){
            window.location.href = $scope.dealPageUrl;
                return;
            }

            coop = getCoop.getCoopId();
            if (coop) {
            $scope.checkCoopInOut(coopData,$scope.processCoop);
            } else {
                window.location.href = coopOutUrl;
            }
        }
    };
    $scope.checkCoopInOut= function(coopData,callBack){
             for (var i = 0; i < coopData.length; i++) {
                         if (coopData[i] == coop) {
                             isCoopIn = true;
                             break;
                         }
                     }
                     callBack(isCoopIn);
         }

          $scope.processCoop= function(isCoopIn){
                       if (isCoopIn) {
                                         if ($cookies.get("ocfbli")) {
                                             window.location.href = $scope.dealPageUrl;
                                         } else {
                                             window.location.href = $scope.coopInUrl;
                                         }

                                     } else {
                                         window.location.href = $scope.coopOutUrl;
                                     }

             }
}]);