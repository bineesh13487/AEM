app.service('CokeLocatorService', ['$http', '$q', '$rootScope', function($http, $q, $rootScope) {
    return {
        initializeDialogConfigurations: initializeDialogConfigurations,
        initializeErrorMessages: initializeErrorMessages,
        showError: showError,
        calculateDistance: calculateDistance,
        phoneNumberForScreenReader: phoneNumberForScreenReader,
        getCokeStores: getCokeStores,
        addMarkerToMap: addMarkerToMap
    };

    var restaurantsResultCountry = "";
    var restaurantSearchCountry = "";
    var restaurantRadius = 5;
    var data;

    /**
     * Returns coke stores with matched search criteria
     * @param inputData : Object constructed with user input
     */
    function getCokeStores(inputData, apiUrl) {
        var deferred = $q.defer();
        $http({
            method: 'POST',
            url: apiUrl,
            data: inputData
        }).then(function(success) {
            deferred.resolve(success.data);
        }, function(error) {
            deferred.reject(error.data);
        });
        return deferred.promise;
    }
    //Initialize the dialog config
    function initializeDialogConfigurations() {
        initializeErrorMessages();
    }
    //Initialize error message
    function initializeErrorMessages() {
        //harish

    };

    //Show error message for Locate Me
    function showError(error) {
        var parseCokstoreJson = JSON.parse(cokeStoreJSON);
        switch (error.code) {
            case error.PERMISSION_DENIED:
                alert(parseCokstoreJson.locationPermissionDeniedText);
                break;
            case error.POSITION_UNAVAILABLE:
                alert(parseCokstoreJson.locationUnavailableText);
                break;
            case error.TIMEOUT:
                alert(parseCokstoreJson.locationTimeoutText);
                break;
            case error.UNKNOWN_ERROR:
                alert(parseCokstoreJson.locationUnknownErrorText);
                break;
        }

    }

    function calculateDistance(restLatitude, restLongitude) {
        var restLatLong = new google.maps.LatLng(restLatitude, restLongitude);
        var startLatLng = new google.maps.LatLng(referenceLatitudeForDistance, referenceLongitudeForDistance);
        var distanceInMeter = google.maps.geometry.spherical.computeDistanceBetween(startLatLng, restLatLong);
        return distanceInMeter;
    }



    function phoneNumberForScreenReader() {
        for (var i = 0; i < $scope.restaurants.length; i++) {
            var restaurant = $scope.restaurants[i];
            restaurant.properties.telephoneTitle = restaurant.properties.telephone != undefined ? (restaurant.properties.telephone).split("").join(" ") : "";
        }

    }

    function addMarkerToMap(cokeOutlets, restaurantLat, restaurantLong, restaurantId, map, markers, markerCount) {
        var restaurants = cokeOutlets;
        var myLatLng = new google.maps.LatLng(restaurantLat, restaurantLong);
        var mcdPinIcon = "//chart.googleapis.com/chart?chst=d_map_pin_letter&chld=" + markerCount + "|ff4022";
        var marker = new google.maps.Marker({
            position: myLatLng,
            map: map,
            icon: mcdPinIcon,
            width: 33,
            height: 46,
            animation: google.maps.Animation.DROP,
            isEnabled: true
        });
        //Creates the event listener for clicking the marker and places the marker on the map
        marker.addListener('click', function() {
            map.setCenter(marker.getPosition());
            var mapviewitems = $('#rl-mapView li.restaurant-location');
            mapviewitems.attr('ng-show', false).addClass('ng-hide');
            $('#rl-mapView #restaurant-location-map-' + [markerCount - 1]).attr('ng-show', true).removeClass('ng-hide');
            restaurants = restaurants;
        });
        markers.push(marker);
    }

}]);