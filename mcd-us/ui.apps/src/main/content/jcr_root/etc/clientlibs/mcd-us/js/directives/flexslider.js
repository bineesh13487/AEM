/* global angular */
/* eslint no-use-before-define: ["error", { "functions": false }] */
/* eslint func-names: ["error", "never"] */

(function () {

  angular
    .module('usr')
    .directive('fxSliderDirective', Directive);

  function Directive() {
    const directive = {
      link
    };
    return directive;

    function link(scope, element) {
      if (scope.$last) {
        const elem = $(element).closest('.related-flexslider');
        initNutritionRelatedFlexSliders(elem);
      }
    }
  }
}());

