app.controller('twitterCtrl', ['$scope', '$http', '$sce', function($scope, $http, $sce) {
   var tweetData = $('#tweets').val();
   var tweetJson = $.parseJSON(tweetData);   
    $scope.data = tweetJson;    

    $scope.getHashTag = function(hasTagValue){
    	if(hasTagValue){
    		var value = "";
    		for(var i = 0; i < hasTagValue.length;i++){
	            value = value + " " + hasTagValue[i].text;
    		}
    		return value;
    		
    	}
    }

    $scope.getMentions = function(hasMentions){
        if(hasMentions){
        	var value = "";
    		for(var i = 0; i < hasMentions.length;i++){
	            value = value + " " + hasMentions[i].screen_name;
    		}
    		return value;
        }
    }
    $scope.getVideoFormat =  function(videoFormats){
    	if(videoFormats){
    		for(var i=0;i<videoFormats.length;i++){
    			var strSrc = videoFormats[i].url; 
    			var strExtenion = strSrc.substring(strSrc.lastIndexOf('.'));
    			if(strExtenion === '.mp4'){
    				break;
    			}
    			else{
    				strSrc="";
    			}            
    		}
    	}
    	return strSrc;
    }

    $scope.trustSrc = function(src) {
        return $sce.trustAsResourceUrl(src);
    }

}]);
