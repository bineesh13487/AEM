app.directive("fxSliderDirective", function() {
    return function($scope, $element, $attr) {
        if ($scope.$last) {
            var elem = $($element).closest('.related-flexslider');
            initNutritionRelatedFlexSliders(elem);
        }
    }
});