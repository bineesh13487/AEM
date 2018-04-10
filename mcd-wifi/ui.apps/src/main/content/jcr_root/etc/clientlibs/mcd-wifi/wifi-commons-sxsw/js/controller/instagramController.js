app.controller('instagramCtrl', ['$scope', '$http', '$sce', function($scope, $http, $sce) {
    var instagramData = $('#posts').val();
    var instagramJson = $.parseJSON(instagramData);
	$scope.instagram = instagramJson;
    $scope.instagram.items.splice($('#numberOfPosts').val());

    $scope.trustSrc = function(src) {
        return $sce.trustAsResourceUrl(src);
    }
    
    function getString(startChar, str) {
        var newStr = "";
        var wrd = "";
        str.split(" ").filter(function(v) {
            if(v.indexOf(startChar) > -1) {
                wrd = v.substring(1);
                newStr += " " + ( (wrd.lastIndexOf('.') > -1 || wrd.lastIndexOf(',') > -1) ? wrd.substring(0, wrd.length -1): wrd);
            }
        });
        return newStr.trim();
    }
    
    $scope.getHashTagInstagram = function(hasTagValue){
    	if(hasTagValue){
    		return getString("#", hasTagValue);
    	}
    }

    $scope.getMentionsInstagram = function(hasMentions){
        if(hasMentions){
    		return getString("@", hasMentions);
        }
    }
    
}]);
