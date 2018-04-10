app.controller('searchController', ['$scope', '$http', function($scope, $http) {
    $scope.searchresults = {};
    var params = $('#search-params');
    var q;
    var limit;
    $scope.totalHits = 0;
    $scope.pageCount = 0;
    $scope.hits = [];
    var defaultImagePath = $('.defaultImagePath').text();
    var hiddenPagePath= $('.hiddenPagePath').text();
	var newsSearchText = $('.newsSearchText').text();
	var resultText = $('.resultText').text();
	var descriptionCutOff = $('.description_cutoff').text();
    if ( descriptionCutOff == null ){
    	descriptionCutOff = 150;
    	}

    $.more.setDefaults({length: parseInt(descriptionCutOff, 10),moreText: "[+]",lessText: "[-]"});
    q = decodeURIComponent(params.data('q'));
    $scope.init = function() {
        $scope.loadSearchResults(true);
		angular.element('.removeMore').more();
		$scope.showNewsSearchText();		
    }
    $scope.loadSearchResults = function(isPageInit) {

        if (!(isPageInit)) {
            $scope.pageCount++;
        }
		else
		{
			$scope.hits = [];
			$scope.pageCount = 0;
		}

        
        limit = params.data('limit');
        pageType = pageType;
        if (q!='' && q!='undefined') {
            $http.get("/services/mcd/searchResults.json?country=" + config.get("country") + "&language=" + config.get("lang") + "&q=" + q + "&limit=" + limit + "&page=" + $scope.pageCount + "&pageType=" + pageType+"&hidePage=" +hiddenPagePath )
                .then(function(response) {
                    $scope.searchresults = response.data;
                    if($scope.searchresults && $scope.searchresults.length != 0)
                    {
                    	$scope.totalHits = $scope.searchresults.totalHits;
                        if($scope.searchresults.hits.length > 0)
                        {
                            for (var count = 0; count < $scope.searchresults.hits.length; count++) {
                                $scope.hits.push($scope.searchresults.hits[count]);
                            }
                            $(".searchtresulttitle").show();
                            $(".nosearchtext").hide();
                            if($scope.searchresults.totalHits <= limit)
                            {
                                $(".noMoreResults").hide();
                                $scope.disableLoadMore();
                            }
                            else
                            {
                                $scope.enableLoadMore();
                            }
                        }
                        else
                        {
                            //if no result found for the search query entered, show no search result error Msg.
                            $(".noMoreResults").show();
                            $scope.disableLoadMore();
                        }
                    }
                    else
                    {
                    	$scope.totalHits= 0;
                        $(".noMoreResults").hide();
                        $scope.disableLoadMore();
                    }
                    $scope.searchResultText();
                    if ((isPageInit)) {
                    	$('.finalResultText').focus();
                    }
                    
                });

        } else {
            //If no search query entered, show no search query error Msg.
            $(".searchtresulttitle").hide();
            $(".nosearchtext").show();
            $('.noMoreResults').hide();
            $scope.disableLoadMore();

        }
		angular.element('.removeMore').more();
    };

    $scope.showThumbnail = function(element) {
        if (element) {
            if (element.thumbnail) {
                return element.thumbnail;
            } else {
                return defaultImagePath;
            }
        } else {
            return defaultImagePath;
        }

    };

    $scope.enableLoadMore = function() {
         $('.noMoreResults').hide();
         $('.load-more-btn').removeAttr("disabled");
         $('.load-more-btn').removeClass("search_deactivate");
         $('.fa-chevron-down').removeClass("search_deactivate");
         $('.load-more-btn').show();
         $('.fa-chevron-down').show();
    };

    $scope.disableLoadMore = function() {
        $('.load-more-btn').attr("disabled", "disabled");
        $('.load-more-btn').addClass("search_deactivate");
        $('.fa-chevron-down').addClass("search_deactivate");
        $('.load-more-btn').hide();
        $('.fa-chevron-down').hide();
    };

	 $scope.showNewsSearchText = function() {

     if (newsSearchText != undefined && newsSearchText!='') {
                var searchKeyword = $('.searchKeyword').text();
                var start = newsSearchText.indexOf("{");
                var end = newsSearchText.indexOf("}");
                var finalnewsSearchMessage = newsSearchText.substring(0, start) + "<b>" + searchKeyword + "</b>" + newsSearchText.substring(end + 1, newsSearchText.length);
                var finalnewsSearchMessageForAda = newsSearchText.substring(0, start) + searchKeyword + newsSearchText.substring(end + 1, newsSearchText.length);
                $('.newsSearchFinalText').html(finalnewsSearchMessage);
                var newsUrl = $('.newsUrl').text();
                var searchNewsHref = newsUrl + "/corporate/site-search?searchtext=" + searchKeyword + "&searchmode=anyword";
                $("a.newsLink").attr("href", searchNewsHref);
                /*$("a.newsLink").attr("title", finalnewsSearchMessageForAda);*/
                $("a.newsLink").attr("aria-label", finalnewsSearchMessageForAda);
            }

	 };
	 
	 $scope.searchResultText = function() {
	     if (resultText != undefined && resultText!='') {
	    	 		var totalHitsText = "<span class='bold'>"+$scope.totalHits+"</span>";
	    	 		var queryTextToAppend ='"'+q+'"';
	                var finalResultText = resultText.replace("{0}", totalHitsText).replace("{1}", queryTextToAppend);
	                $('.finalResultText').html(finalResultText);
	            }

		 };

	 $scope.$on('ngRepeatFinished', function(ngRepeatFinishedEvent) {
     angular.element('.removeMore').more();
     setTimeout(function(){
         var scrollTop     = $(window).scrollTop(),
             elementOffset = $('.search-results-cont .first-of-set').last().offset().top,
             distance      = (elementOffset - scrollTop);

         if(distance < 183){
           $(window).scrollTop(scrollTop - 183);
         }
         if ($scope.pageCount > 0) {
         $('.search-results-cont .first-of-set').last().find(':tabbable').first().focus();
         }
         
       }, 400);
});

}]);

app.directive('onFinishRender', ['$timeout',function ($timeout) {
    return {
        restrict: 'A',
        link: function (scope, element, attr) {
            if (scope.$last === true) {
                $timeout(function () {
                    scope.$emit('ngRepeatFinished');
                });
            }
        }
    }
}]);

