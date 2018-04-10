/* global angular, config */
/* eslint no-use-before-define: ["error", { "functions": false }] */
/* eslint func-names: ["error", "never"] */

(function () {

  angular
    .module('usr')
    .controller('relatedProductsController', RelatedProductsController);

  RelatedProductsController.$inject = ['$scope', '$http', 'getCoop'];
  function RelatedProductsController($scope, $http, getCoop) {

    $scope.items = [];
    let coop;
    $scope.init = function (id, itemID) {
      $scope.productId = itemID;
      $scope.categoryId = id;
      const showLiveData = $('.showLiveData').data('showlive-value');
      // coop to be obtained from cookie
      coop = getCoop.getCoopId();
      if (!(coop)) {
        coop = '';
      }
      if ($scope.categoryId !== 0) {
        const selectorString = `.${config.get('country')}.${config.get('lang')}.${
          $scope.categoryId}.${showLiveData}.true.${coop}.json`;
        $http.get(`/services/mcd/categoryDetails${selectorString}`).then(response => {
          if (response.data.category) {
            $scope.title = response.data.category.marketingName;
            $scope.filterItems(response.data.category.items);
          }
        }).then(() => {
          $(`.loadingDiv_${id}`).hide();
        });
      }
    };

    $scope.filterItems = function (items) {
      angular.forEach(items, value => {
        if (value.itemVisibility && (value.externalId !== $scope.productId)) {
          $scope.items.push(value);
        }
      });
    };
  }
}());

