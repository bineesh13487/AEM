app.controller('fcontroller', ['$scope','Scopes', function($scope,Scopes) {
    $scope.multipleLocations = [];
	$scope.init = function () {
	Scopes.store('fcontroller', $scope);
	}
	$scope.setMultipleLocations = function(multipleLocation) {
	$scope.multipleLocations = multipleLocation;
	}

	$scope.showRestaurantForMulti=function(event,location) {

        $('#restaurantLocatorDisambiguityModal').find('.btn-close').click();


				$('.modal-backdrop').remove();
	if(Scopes.get('rlcontroller')!=undefined) {

        $('button[data-target="#restaurantLocatorMapModal"]').click();




        $.each(["show", "toggle", "toggleClass", "addClass", "removeClass"], function(){
    var _oldFn = $.fn[this];
    $.fn[this] = function(){
        var hidden = this.find(":hidden").add(this.filter(":hidden"));
        var result = _oldFn.apply(this, arguments);
        hidden.filter(":visible").each(function(){
            $(this).triggerHandler("show"); //No bubbling
        });
        return result;
    }
});

        var onlyOnce = true;
if (window.innerWidth < 768) {
      if(onlyOnce){
        onlyOnce=false;
        Scopes.get('rlcontroller').showRestaurantForMulti(event,location);
/*$("button[name='searchRestaurantbutton']").click();*/
    }
 }
 else {
	$("#gmap_canvas").bind("show", function(){
		if(onlyOnce){
			onlyOnce=false;
	/*$("button[name='searchRestaurantbutton']").click();*/
			Scopes.get('rlcontroller').showRestaurantForMulti(event,location);
		}
	});
 }
 
 
 
	}
	}
}]);