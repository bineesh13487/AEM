/* eslint no-undef: 0 */
app.controller('articleListingController', [
  '$scope',
  '$http',
  function articleListingController($scope, $http) {
    const params = $('#search-params');
    $scope.pageCount = 0;
    $scope.totalHits = 0;
    let limitCount;
    let count;
    $scope.init = function init(newsType, folderPath, loadMoreCount, limit) {
      $('.articleListing').closest('.columncontrol').css('background-color', '#dcdbdb');
      $('.articleListing').closest('.columncontrol').children().css('padding', '3% 0');

      $scope.newsType = newsType;
      $scope.folderPath = folderPath;
      $scope.pagecount = 0;
      $scope.loadMoreCount = loadMoreCount;
      $scope.limit = limit;
      limitCount = limit;

      $scope.loadSearchResults(true);

      /* if ($scope.newsType) {

        $http.get(
            "/services/mcd/articlelisting.json?country="
                + config.get("country") + "&language="
                + config.get("lang") + "&newsType="
                + $scope.newsType + "&newsFolderPath="
                + $scope.folderPath + "&limit="
                + limit).then(
            function(response) {
              $scope.itemsList = response.data.articles;
              console.log($scope.itemsList);

            });

      } */

    };

    $scope.loadSearchResults = function loadSearchResults(isPageInit) {

      if (!(isPageInit)) {
        $scope.pageCount++;
      }
      else {
        $scope.pageCount = 0;
      }
      // $scope.limit = params.data('limit');
      count = $scope.loadMoreCount * $scope.pageCount;
      limitCount = parseInt($scope.limit, 10) + count;

      if ($scope.newsType) {

        $http.get(`/services/mcd/articlelisting.json?country=${
          config.get('country')}&language=${
          config.get('lang')}&newsType=${
          $scope.newsType}&newsFolderPath=${
          $scope.folderPath}&limit=${
          limitCount}`).then(response => {
          $scope.itemsList = response.data.articles;
          console.log($scope.itemsList);
          if ($scope.itemsList && $scope.itemsList.length != 0) {
            $scope.totalMatches = $scope.itemsList[0].totalMatches;
            $scope.totalHits = $scope.totalMatches;
            if ($scope.itemsList.length > 0) {
              if ($scope.totalHits <= limitCount) {
                $('.noMoreResults').hide();
                $scope.disableLoadMore();
              }
              else {
                $scope.enableLoadMore();
              }
            }
            else {
              // if no result found for the search query entered, show no search result error Msg.
              $('.noMoreResults').show();
              $scope.disableLoadMore();
            }
          }
          else {
            $scope.totalHits = 0;
            $('.noMoreResults').show();
            $scope.disableLoadMore();
          }
        });

      }
    };

    $scope.enableLoadMore = function enableLoadMore() {
      $('.noMoreResults').hide();
      $('.load-more-btn').removeAttr('disabled');
      //  $('.load-more-btn').removeClass("search_deactivate");
      //  $('.fa-chevron-down').removeClass("search_deactivate");
      $('.load-more-btn').show();
      $('.fa-chevron-down').show();
    };

    $scope.disableLoadMore = function disableLoadMore() {
      $('.load-more-btn').attr('disabled', 'disabled');
      // $('.load-more-btn').addClass("search_deactivate");
      // $('.fa-chevron-down').addClass("search_deactivate");
      $('.load-more-btn').hide();
      $('.fa-chevron-down').hide();
    };

  }]);
