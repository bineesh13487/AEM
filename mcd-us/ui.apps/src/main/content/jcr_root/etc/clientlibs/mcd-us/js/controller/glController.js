app.controller("glController", ['$rootScope', '$scope', '$http', 'customer', 'Scopes', '$location', 'GetPathForCookie', function ($rootScope, $scope, $http, customer, Scopes, $location, GetPathForCookie) {

  var markerCount = 1;
  var cookieVal = "geoLocationData";
  var cookieValNearestLoc = "geoLocationDataNearestLoc";
  var coopid = "coopid";
  var map;
  var infowindow;
  $scope.isMulti = false;
  isGeo = false;
  $scope.showDefault = false;
  $scope.showPrefered = false;
  $scope.showGeo = false;
  var markers = [];
  var markerCluster;
  var configuredRestaurantRadius;
  var maxResults, grayedOutRestImagePath, markerHeight = 33, markerWidth = 46;
  $scope.restaurants = [];
  $scope.restaurantFilters = [];
  $scope.restaurantAmenities = [];
  $scope.header = false;
  $scope.radius = [];
  $scope.resultFooterLink = [];
  $scope.restaurantFilterKey = "";
  $scope.displayRlResultView = false;
  $scope.fullRlData;

  $scope.restaurantRadius = 5;
  $scope.resultsPerPage = 0;
  $scope.currentPageNo = 1;
  $scope.restaurantSearchText = "";
  $scope.enabledRestaurantsCount = 0;
  $scope.multipleLocations = [];

  var referenceLatitudeForDistance, referenceLongitudeForDistance;
  var locationPermissionDeniedText, locationUnavailableText, locationTimeoutText, locationUnknownErrorText, noRouteText, directionUnknownErrorText;
  var restaurantsResultCountry, restaurantSearchCountry, restaurantsResultLanguage, resultMessage, showClosed, hours24Text, Hours24TextForAriaLabel, pathCountry;
  $scope.distanceUnit;
  //$scope.noResultFoundText;
  $scope.firstRestaurantName = "State & Main";

  $scope.loggedout = true;

  //if a user is logged in, hide the Join link
  customer.isUserLoggedIn().then(function (bool) {
    if (bool) {
      $scope.loggedout = false;
    }
  }, function (err) {
  });
  //listen for the loginStatus event to update the $scope.loggedout property
  $scope.$on('loginStatus', function (event, userLoggedIn) {
    $scope.loggedout = !userLoggedIn;
  });


  function sendStoreId(storeNaId) {
    $.ajax({
      type: 'GET',

      method: "GET",
      async: false,
      //url: 'http://gwc.localhost.com:3000/gwc/en/api/user/sign-out'
      url: $('#updateStoreLink').attr('data-domain') + storeNaId,
      //url: logoutUrl,
      crossDomain: true,
      xhrFields: {
        withCredentials: true
      },
      dataType: "json",
      success: function (response) {

        console.log("success in store ID");
      }

    });
  }

  function setCoopId(cname, cvalue, exdays) {
    var storeId = cvalue.properties.identifiers.storeIdentifier;
    //var countrycode = $location.path().split("/"+restaurantsResultCountry.toLowerCase()+"/")[0] + "/"+restaurantsResultCountry.toLowerCase()+"/";
    var countrycode = GetPathForCookie.setPathForCookie($location.path());
    var coopId = '';
    var storeNaId = '';
    for (j = 0; j < storeId.length; j++) {
      if (storeId[j].identifierType === 'Co-Op ID') {
        coopId = storeId[j].identifierValue;
      } else if (storeId[j].identifierType === 'NatlStrNumber') {
        storeNaId = storeId[j].identifierValue;
      }
    }
    var d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    var expires = "expires=" + d.toGMTString();
    document.cookie = cname + "=" + coopId + "; " + expires + ";path=" + countrycode;
    sendStoreId(storeNaId);
    $('.storeidstored').attr('val', storeNaId);

  }

  function setCookie(cname, cvalue, exdays) {

    function commaReplacerGl(key, value) {
      if (key == 'identifierValue' || key == 'addressLine1' || key == 'addressLine3') {
        if (value.indexOf(",") > 0) {
          return value.replace(/\,/g, '');
        }
      }
      return value;
    }

    cvalue = removeHoursForScreenReader(cvalue);

    cvalue = JSON.stringify(cvalue);

    cvalue = encodeURIComponent(cvalue);

    //var countrycode = $location.path().split("/"+restaurantsResultCountry.toLowerCase()+"/")[0] + "/"+restaurantsResultCountry.toLowerCase()+"/";
    var countrycode = GetPathForCookie.setPathForCookie($location.path());
    var d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    var expires = "expires=" + d.toGMTString();
    document.cookie = cname + "=" + cvalue + "; " + expires + ";path=" + countrycode;
  }
  function setCookieNearestLoc(cname, cvalue, exdays) {
    cvalue = removeHoursForScreenReader(cvalue);

    cvalue = JSON.stringify(cvalue);
    cvalue = encodeURIComponent(cvalue);
    //var countrycode = $location.path().split("/"+restaurantsResultCountry.toLowerCase()+"/")[0] + "/"+restaurantsResultCountry.toLowerCase()+"/";
    var countrycode = GetPathForCookie.setPathForCookie($location.path());
    var d = new Date();
    d.setTime(d.getTime() + (exdays * 60 * 1000));
    var expires = "expires=" + d.toGMTString();
    document.cookie = cname + "=" + cvalue + "; " + expires + ";path=" + countrycode;
  }


  function expireCookie(cname, cvalue, exdays) {
    cvalue = JSON.stringify(cvalue);
    //var countrycode = $location.path().split("/"+restaurantsResultCountry.toLowerCase()+"/")[0] + "/"+restaurantsResultCountry.toLowerCase()+"/";
    var countrycode = GetPathForCookie.setPathForCookie($location.path());
    var d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    var expires = "expires=" + d.toGMTString();
    document.cookie = cname + "=" + cvalue + "; " + expires + ";path=" + countrycode;
  }

  function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
      var c = ca[i];
      while (c.charAt(0) == ' ') c = c.substring(1);
      if (c.indexOf(name) == 0) {
        c = decodeURIComponent(c);
        var restaurant = JSON.parse(c.substring(name.length, c.length));
        if (restaurant != undefined && restaurant.properties != undefined && restaurant.properties.identifiers != undefined && restaurant.properties.identifiers.storeIdentifier != undefined) {
          var storeId = restaurant.properties.identifiers.storeIdentifier;
          var domainName = $('#cookieDomainName').attr('data-domain');
          var coopId = '';
          var storeNaId = '';
          for (j = 0; j < storeId.length; j++) {
            if (storeId[j].identifierType === 'Co-Op ID') {
              coopId = storeId[j].identifierValue;
            } else if (storeId[j].identifierType === 'NatlStrNumber') {
              storeNaId = storeId[j].identifierValue;
            }
          }
          sendStoreId(storeNaId);
          $('.storeidstored').attr('val', storeNaId);
        }
        return restaurant;
      }

    }
  }

  $scope.showPopUp = function () {

    var path = window.location.href;
    if (path.indexOf(".gma.html") >= 0) {

      return false;

    }

    return true;
  }


  $scope.setPrefered = function (cvalue) {
    setCookie(cookieVal, cvalue, 2);
    setCoopId(coopid, cvalue, 2);
    expireCookie(cookieValNearestLoc, "", 0);
    $scope.init(true, true);
  }

  $scope.todayRestaurantHours = function (restaurant) {
    if (restaurant.noHours) {
      return $scope.fullRlData.restaurantNotAvailable;
    }
    if (restaurant != undefined && undefined != restaurant.properties.todayHours) {
      if (restaurant.properties.todayHours.indexOf("-") != -1) {
        var restaurantsProp = restaurant.properties.todayHours.split(' - ');
        return restaurantsProp[0] + " to " + restaurantsProp[1];
      }
      return restaurant.properties.todayHours;
    }
  }

  $scope.init = function (isGeo, isPrefered) {

    if ($scope.showPopUp()) {

      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPositionMap, showErrorMap);
      } else {
        console.log("Geolocation is not supported by this browser.");
      };
    }


    if (!isPrefered) {
      Scopes.store('glController', $scope);
      $scope.fullRlData = JSON.parse($('#rlJson').attr('data-rl-json'));
      $scope.driveOpen = false;
      $scope.restaurantClosed = false;
      $scope.RestaurantOpen = false;
      $scope.restaurantSearchText = $scope.fullRlData.restaurantSearchText;
      $scope.restaurantLocateIsSelected = Boolean($scope.fullRlData.locate);
      $scope.restaurantRadius = $scope.fullRlData.radiusNearestLocation;
      $scope.enableDriveHours = $scope.fullRlData.enableDriveHours;
      $scope.restaurantSubmit = Boolean($scope.fullRlData.submit);
      $scope.filterShown = 1;
      $scope.FilterSize = 5;
      $scope.FilterSizeDesktop = 5;
      $scope.FilterSizeMobile = 4;
      $scope.resultShown = parseInt($scope.fullRlData.resultIterate);
      $scope.restaurantEndSize = parseInt($scope.fullRlData.resultIterate);

      initializeDialogConfigurations();
      initializeRadiusDropDown();

      $scope.filterLimit();
      var isDefaultAddress = false;

      if (!$scope.hasMoreItemsToShow()) {
        $scope.noShowMore = true;
      } else {
        //$scope.showMoreLessText = $scope.showMoreText;
      }
    }


    if ($scope.showPopUp()) {
      if (isGeo) {
        $scope.showPrefered = false;
        $scope.showGeo = false;
        $scope.showDefault = false;
        var restaurant = getCookie(cookieVal);

        var restaurantGeo = getCookie(cookieValNearestLoc);
        if (restaurant != undefined) {
          $scope.showPrefered = true;
          $scope.restaurants = [];
          $scope.restaurants.push(restaurant);
          $scope.firstRestaurantName = restaurant.properties.name ? restaurant.properties.name : restaurant.properties.addressLine1;
        }
        else if (navigator.geolocation && restaurantGeo != undefined) {
          $scope.showGeo = true;
          $scope.restaurants = [];
          $scope.restaurants.push(restaurantGeo);
          $scope.firstRestaurantName = restaurantGeo.properties.name ? restaurantGeo.properties.name : restaurantGeo.properties.addressLine1;
        } else if (navigator.geolocation) {
          maxResults = 1;
          restaurantSearchCountry = $scope.fullRlData.country;
          restaurantsResultLanguage = $scope.fullRlData.language;
          showClosed = $scope.fullRlData.showClosed;
          hours24Text = $scope.fullRlData.Hours24Text;
          Hours24TextForAriaLabel = $scope.fullRlData.Hours24TextForAriaLabel;
          if ($scope.showPopUp()) {
            navigator.geolocation.getCurrentPosition(showPosition, showError);
          } else {
            $scope.showDefault = true;
          }
        }
      }
    }
  };

  $scope.updateRestaurants = function () {
    if ($scope.showPopUp()) {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPositionMap, showErrorMap);
      } else {
        console.log("Geolocation is not supported by this browser.");
      };
    }
    restaurant = $scope.restaurants[0];
    var restaurantLong = restaurant.geometry.coordinates[0];
    var restaurantLat = restaurant.geometry.coordinates[1];
    var restaurantName = restaurant.properties.name;
    var distanceUnit = $scope.fullRlData.distanceUnit;
    restaurant.RestaurantOpen = false;
    restaurant.driveOpen = false;
    restaurant.spcialOpen = false;
    restaurant.closed = false;
    restaurant.noHours = true;
    restaurant.noDriveHours = true;
    if (restaurant.properties.todayHours != undefined) {// ||  restaurant.properties.driveTodayHours !=undefined || restaurant.properties.todaySpecialHours) {
      restaurant.noHours = false;
    }
    if (restaurant.properties.driveTodayHours != undefined) {
      restaurant.noDriveHours = false;
    }

    if (!restaurant.noHours) {
      var todayRestaurantHours = restaurant.properties.todayHours;
      if (todayRestaurantHours != undefined) {
        var hourSplit = todayRestaurantHours.split(" - ");
        if (hourSplit.length == 1) {
          restaurant.RestaurantOpen = true;
        } else {
          var startTime = hourSplit[0];
          var endTime = hourSplit[1];

          var now = new Date();
          if (endTime == '12:00 AM') {
            endTime = '12:00 PM';
          }
          var startDate = dateObj(startTime);
          var endDate = dateObj1(startTime, endTime);



          if (now <= endDate && now >= startDate) {
            restaurant.RestaurantOpen = true;
          } else if (now < startDate) {
            if (startDate.getDate() == now.getDate()) {
              endDate.setDate(endDate.getDate() - 1);
              if (now < endDate) {
                restaurant.RestaurantOpen = true;
              }
            }
          }
        }
      }
    }

    /*if (!restaurant.RestaurantOpen) {*/
    if (!restaurant.noDriveHours) {
      todayRestaurantHours = restaurant.properties.driveTodayHours;
      if (todayRestaurantHours != undefined) {
        hourSplit = todayRestaurantHours.split(" - ");
        if (hourSplit.length == 1) {
          restaurant.driveOpen = true;
        } else {
          var startTime = hourSplit[0];
          var endTime = hourSplit[1];

          var now = new Date();
          if (endTime == '12:00 AM') {
            endTime = '12:00 PM';
          }
          var startDate = dateObj(startTime);
          var endDate = dateObj1(startTime, endTime);


          if (now <= endDate && now >= startDate) {
            restaurant.driveOpen = true;
          } else if (now < startDate) {
            if (startDate.getDate() == now.getDate()) {
              endDate.setDate(endDate.getDate() - 1);
              if (now < endDate) {
                restaurant.driveOpen = true;
              }
            }

          }
        }
      }

      /* }*/

      /*if (!restaurant.RestaurantOpen && !restaurant.driveOpen) {*/
      /*   todayRestaurantHours = restaurant.properties.todaySpecialHours;
         if (todayRestaurantHours != undefined) {
             hourSplit = todayRestaurantHours.split(" - ");
             if (hourSplit.length == 1) {
                 restaurant.spcialOpen = true;
             } else {
                 var startTime = hourSplit[0];
                 var endTime = hourSplit[1];

                 var now = new Date();
                 var startDate = dateObj(startTime);
                 var endDate = dateObj1(startTime,endTime);


                 if (now <= endDate && now >= startDate) {
                     restaurant.spcialOpen = true;
                 }else if(now<startDate) {
                     if(startDate.getDate()==now.getDate()) {
                         endDate.setDate(endDate.getDate()-1);
                         if(now<endDate) {
                              restaurant.spcialOpen = true;
                         }
                     }
                 }
             }
         }
     }*/

    }


    if (!restaurant.RestaurantOpen && !restaurant.driveOpen) {//} && !restaurant.spcialOpen) {
      restaurant.closed = true;
    }

    if ($scope.currentLatitude != undefined && $scope.currentLongitude != undefined) {
      referenceLatitudeForDistance = $scope.currentLatitude;
      referenceLongitudeForDistance = $scope.currentLongitude;
      if (distanceUnit === 'kilometer') {
        restaurant.properties.distance = parseFloat(Math.round((calculateDistance(restaurantLat, restaurantLong) / 1000) * 100) / 100).toFixed(2);
      } else if (distanceUnit === 'miles') {
        restaurant.properties.distance = parseFloat(Math.round((calculateDistance(restaurantLat, restaurantLong) * 0.000621371192) * 100) / 100).toFixed(2);
      }

    }

    var restaurantId = restaurant.properties.id;



    $scope.restaurants[0] = restaurant;

  }

  var initializeRadiusDropDown = function () {
    $('.dropdown-menu.dropdown-menu-right li').on('click', function () {
      $('.btn.btn-default.dropdown-toggle').val($(this).text());
      $('.btn.btn-default.dropdown-toggle').text($(this).text());
      $scope.restaurantRadius = $scope.fullRlData.radiusNearestLocation;
    });

    if ($scope.restaurantRadius != undefined) {
      $('button[name="restaurantRadius"]').text($scope.restaurantRadius)
    } else {
      $scope.restaurantRadius = $scope.fullRlData.radiusNearestLocation;
    }
  }

  $scope.updateRadiusDropDown = function (event) {
    $scope.restaurantRadius = parseInt($(event.target).text() + " <i class='fa fa-chevron-down'></i>");
  }

  $scope.filterLimit = function (data) {
    return $scope.FilterSize * $scope.filterShown;
  };

  $scope.hasMoreItemsToShow = function () {
    if (window.innerWidth < 768) {
      return $scope.filterShown < ($scope.restaurantFilters.length / $scope.FilterSizeMobile);
    }
    else {
      return $scope.filterShown < ($scope.restaurantFilters.length / $scope.FilterSizeDesktop);
    }

  };

  $scope.showMoreItems = function () {
    if ($scope.hasMoreItemsToShow()) {
      $scope.filterShown = ($scope.restaurantFilters.length / $scope.FilterSize) + 1;

    } else {
      $scope.filterShown = 1;
    }
    if ($scope.hasMoreItemsToShow()) {
      $scope.showMoreLessText = $scope.showMoreText;
    } else {
      $scope.showMoreLessText = $scope.showLessText;

    }
  };


  $scope.geocodeAddress = function (searchedAddress) {
    var geocoder = new google.maps.Geocoder();

    if (restaurantsResultCountry != undefined) {
      searchedAddress = searchedAddress + "," + restaurantsResultCountry;
    }

    var geocodeRequest = {
      address: searchedAddress
    }

    geocoder.geocode(geocodeRequest, function (results, status) {
      if (status == google.maps.GeocoderStatus.OK) {
        if (results.length > 1) {
          $scope.multipleLocations = [];
          var tempMultiLocation = [];
          var counter = 0;
          for (var i = 0; i < results.length; i++) {
            if (checkIfAddressIsInConfiguredCountry(results[i])) {
              var tempMultiLocObject = {};
              tempMultiLocObject["formattedAddress"] = results[i].formatted_address;
              tempMultiLocObject["latitude"] = results[i].geometry.location.lat();
              tempMultiLocObject["longitude"] = results[i].geometry.location.lng();
              tempMultiLocation[counter] = tempMultiLocObject;
              counter = counter + 1;
            }
          }

          if (tempMultiLocation.length > 0) {
            $scope.$apply(function () {
              $scope.multipleLocations = tempMultiLocation;
              $scope.isMulti = false;
              $('.disambiguitydemo').click()
              $scope.displayRlResultView = true;
            });
            $('#myModal').appendTo("body").modal('show');
          } else {
            showNoResults();
          }
        } else {
          if (checkIfAddressIsInConfiguredCountry(results[0])) {
            var latitude = results[0].geometry.location.lat();
            var longitude = results[0].geometry.location.lng();
            $scope.getGlRestaurantsDetail(latitude, longitude);
          } else {
            showNoResults();
          }
        }
      } else {
        showNoResults();
      }
    });
  };

  function checkIfAddressIsInConfiguredCountry(addressSearched) {
    if (addressSearched != undefined) {
      var addressComponents = addressSearched.address_components;
      if (addressComponents != undefined) {
        for (var n = 0; n < addressComponents.length; n++) {
          var addressComp = addressComponents[n];
          if (addressComp != undefined && addressComp.types != undefined && addressComp.types.length > 0) {
            var addressType = addressComp.types[0];
            if (addressType != undefined && addressType == "country") {
              var countryShortName = addressComp.short_name;
              var restSearchCountry = restaurantSearchCountry.toLowerCase();
              countryShortName = countryShortName.toLowerCase();
              if (countryShortName != undefined && countryShortName == restSearchCountry) {
                return true;
              }
              else if ((countryShortName != undefined && countryShortName == "sa") && (restSearchCountry == "saj" || restSearchCountry == "sar")) {
                return true;
              }
            }
          }
        }
      }
    }
    return false;
  };

  function showNoResults() {
    deleteMarkers();
    $scope.showRestaurantsOnMap([]);
    $scope.$apply(function () {
      $scope.currentPageNo = 1;
      $scope.restaurants = [];
    });
  };

  $scope.getGlRestaurantsDetail = function (latitude, longitude) {

    referenceLatitudeForDistance = latitude;
    referenceLongitudeForDistance = longitude;
    $scope.distanceReference = 0;
    $scope.resultShown = parseInt($scope.fullRlData.resultIterate);
    markerCount = 1;
    maxResults = 1;

    if ($scope.distanceUnit === 'kilometer') {
      $scope.resRadius = $scope.restaurantRadius;
    } else if ($scope.distanceUnit === 'miles') {
      $scope.resRadius = $scope.restaurantRadius * 1.609;
    }

    $scope.distanceReference = $scope.restaurantRadius;

    if ($scope.currentLatitude == undefined || $scope.currentLongitude == undefined) {
      $scope.currentLatitude = latitude;
      $scope.currentLongitude = longitude;
    }

    if ($scope.resRadius === undefined) {
      $scope.resRadius = 5000;
    }

    var parser = document.createElement('a');
    parser.href = $('#sign-inlink').attr('data-domain');
    var parserDomainName = '';

    if (parser.port) {
      if (parser.protocol && parser.hostname) {
        parserDomainName = parser.protocol + "//" + parser.hostname + ":" + parser.port;
      }

    } else {
      if (parser.protocol && parser.hostname) {
        parserDomainName = parser.protocol + "//" + parser.hostname;
      }
    }

    $.get(parserDomainName + "/googleapps/GoogleRestaurantLocAction.do?method=searchLocation&latitude=" + latitude + "&longitude=" + longitude +
      "&radius=" + $scope.resRadius +
      "&maxResults=" + maxResults + "&country=" + restaurantSearchCountry + "&language=" + restaurantsResultLanguage + "&showClosed=" + showClosed + "&hours24Text=" + encodeURIComponent(hours24Text),
      function (data) {
        $scope.displayRlResultView = true;
        var results = data.features;
        resultDuplicate = [];


        for (rIndex = 0; rIndex < results.length; rIndex++) {
          if (results[rIndex].geometry != null) {
            if ($scope.distanceUnit === 'kilometer') {
              results[rIndex].properties.distance = parseFloat(Math.round((calculateDistance(results[rIndex].geometry.coordinates[1], results[rIndex].geometry.coordinates[0]) / 1000) * 100) / 100).toFixed(2);
            } else if ($scope.distanceUnit === 'miles') {
              results[rIndex].properties.distance = parseFloat(Math.round((calculateDistance(results[rIndex].geometry.coordinates[1], results[rIndex].geometry.coordinates[0]) * 0.000621371192) * 100) / 100).toFixed(2);
            }

            if (parseFloat($scope.distanceReference).toFixed(1) >= parseFloat(results[rIndex].properties.distance)) {
              resultDuplicate.push(results[rIndex]);
            }
          }
        }

        results = resultDuplicate;

        if (results.length == 0) {
          var restaurant = getCookie(cookieVal);

          if (restaurant != undefined) {
            $scope.showPrefered = true;
            $scope.restaurants.push(restaurant);
            $scope.firstRestaurantName = restaurant.properties.name ? restaurant.properties.name : restaurant.properties.addressLine1;
            $scope.$digest();
          } else {
            $scope.showDefault = true;
            $scope.$digest();
          }

          return;
        }

        $scope.firstRestaurantName = results[0].properties.name ? results[0].properties.name : results[0].properties.addressLine1;
        $scope.showGeo = true;
        $scope.$digest();
        $scope.showRestaurantsOnMap(results);
        $scope.restaurants = results;
        phoneNumberForScreenReader();
        HoursForScreenReader();

        $scope.fullRlData.filterSelected = [];
        $scope.filterSelected = [];

        setCoopId(coopid, results[0], 2);

        if (getCookie(cookieVal) == null) {
          setCookieNearestLoc(cookieValNearestLoc, results[0], $scope.fullRlData.cookieExpiry);
        }
        $scope.restaurants = $scope.restaurants;
      });
  };

  $scope.resizeMap = function (restaurants) {
    var mapOptionsNav = {
      zoom: 13,
      draggable: false,
      disableDefaultUI: true,
      center: new google.maps.LatLng(restaurants[0].geometry.coordinates[1], restaurants[0].geometry.coordinates[0]),
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };

    var markerimage = {
      url: '/etc/clientlibs/mcd-us/images/restaurant-locator/map-pin.png',
      size: new google.maps.Size(16, 21),
      origin: new google.maps.Point(0, 0),
      anchor: new google.maps.Point(8, 21)
    };

    navMap = new google.maps.Map(document.getElementById('gmap_canvas_nav'), mapOptionsNav);
    navMarker = new google.maps.Marker({
      map: navMap,
      icon: markerimage,
      position: new google.maps.LatLng(restaurants[0].geometry.coordinates[1], restaurants[0].geometry.coordinates[0])
    });
  }

  function dateObj1(d, endDate) {
    var parts = d.split(/:|\s/);
    var parts1 = endDate.split(/:|\s/);
    date = new Date();
    var format = parts.pop();
    var format1 = parts1.pop();
    if (format === format1) {
      var startMinute = parseInt(parts[0] * 60) + parseInt(parts[1]);
      var endMinute = parseInt(parts1[0] * 60) + parseInt(parts1[1])
      if (startMinute >= endMinute) {
        date.setDate(date.getDate() + 1);
      }
    }
    if (format1.toLowerCase() == 'pm') parts1[0] = (+parts1[0]) + 12;
    date.setHours(+parts1.shift());
    date.setMinutes(+parts1.shift());
    return date;
  }

  function dateObj(d) {
    var parts = d.split(/:|\s/);
    date = new Date();
    if (parts.pop().toLowerCase() == 'pm') parts[0] = (+parts[0]) + 12;
    date.setHours(+parts.shift());
    date.setMinutes(+parts.shift());
    return date;
  }

  $scope.showRestaurantsOnMap = function (results) {
    var distanceUnit;

    if ($("div#hiddenDistanceUnit").length > 0) {
      distanceUnit = $("div#hiddenDistanceUnit").data("distance-unit");
    }

    var sundayText, mondayText, tuesdayText, wednesdayText, thursdayText, fridayText, saturdayText;

    if ($("div#hideSundayText").length > 0) {
      sundayText = $("div#hideSundayText").data("sunday-text");
    }
    if ($("div#hideMondayText").length > 0) {
      mondayText = $("div#hideMondayText").data("monday-text");
    }
    if ($("div#hideTuesdayText").length > 0) {
      tuesdayText = $("div#hideTuesdayText").data("tuesday-text");
    }
    if ($("div#hideWednesdayText").length > 0) {
      wednesdayText = $("div#hideWednesdayText").data("wednesday-text");
    }
    if ($("div#hideThursdayText").length > 0) {
      thursdayText = $("div#hideThursdayText").data("thursday-text");
    }
    if ($("div#hideFridayText").length > 0) {
      fridayText = $("div#hideFridayText").data("friday-text");
    }
    if ($("div#hideSaturdayText").length > 0) {
      saturdayText = $("div#hideSaturdayText").data("saturday-text");
    }
    var enabledCount = 0;

    if ($scope.restaurantEndSize > results.length) {
      $scope.showLoadMore = false;
    } else {
      $scope.showLoadMore = true;
    }

    var date = new Date();
    $.each(results, function (index, element) {
      var restaurantLong = results[index].geometry.coordinates[0];
      var restaurantLat = results[index].geometry.coordinates[1];
      var restaurantName = results[index].properties.name;
      results[index].RestaurantOpen = false;
      results[index].driveOpen = false;
      results[index].spcialOpen = false;
      results[index].closed = false;
      results[index].noHours = true;
      results[index].noDriveHours = true;
      if (results[index].properties.todayHours != undefined) {// ||  results[index].properties.driveTodayHours !=undefined || results[index].properties.todaySpecialHours) {
        results[index].noHours = false;
      }
      if (results[index].properties.driveTodayHours != undefined) {
        results[index].noDriveHours = false;
      }
      if (!results[index].noHours) {
        var todayRestaurantHours = results[index].properties.todayHours;

        if (todayRestaurantHours != undefined) {
          var hourSplit = todayRestaurantHours.split(" - ");
          if (hourSplit.length == 1) {
            results[index].RestaurantOpen = true;
          } else {
            var startTime = hourSplit[0];
            var endTime = hourSplit[1];
            var now = new Date();
            var startDate = dateObj(startTime);
            var endDate = dateObj1(startTime, endTime);

            if (now <= endDate && now >= startDate) {
              results[index].RestaurantOpen = true;
            } else if (now < startDate) {
              if (startDate.getDate() == now.getDate()) {
                endDate.setDate(endDate.getDate() - 1);
                if (now < endDate) {
                  results[index].RestaurantOpen = true;
                }
              }
            }
          }
        }
      }
      if (!results[index].noDriveHours) {
        todayRestaurantHours = results[index].properties.driveTodayHours;
        if (todayRestaurantHours != undefined) {
          hourSplit = todayRestaurantHours.split(" - ");
          if (hourSplit.length == 1) {
            results[index].driveOpen = true;
          } else {
            var startTime = hourSplit[0];
            var endTime = hourSplit[1];

            var now = new Date();
            var startDate = dateObj(startTime);
            var endDate = dateObj1(startTime, endTime);


            if (now <= endDate && now >= startDate) {
              results[index].driveOpen = true;
            } else if (now < startDate) {
              if (startDate.getDate() == now.getDate()) {
                endDate.setDate(endDate.getDate() - 1);
                if (now < endDate) {
                  results[index].driveOpen = true;
                }
              }
            }
          }
        }
      }
      /*todayRestaurantHours = results[index].properties.todaySpecialHours;
      if (todayRestaurantHours != undefined) {
          hourSplit = todayRestaurantHours.split(" - ");
          if (hourSplit.length == 1) {
              results[index].spcialOpen = true;
          } else {
              var startTime = hourSplit[0];
              var endTime = hourSplit[1];

              var now = new Date();
              var startDate = dateObj(startTime);
              var endDate = dateObj1(startTime,endTime);


              if (now <= endDate && now >= startDate) {
                  results[index].spcialOpen = true;
              }else if(now<startDate) {
                  if(startDate.getDate()==now.getDate()) {
                      endDate.setDate(endDate.getDate()-1);
                      if(now<endDate) {
                          results[index].spcialOpen = true;
                      }
                  }
              }
          }
      }*/

      if (!results[index].RestaurantOpen && !results[index].driveOpen) {// && !results[index].spcialOpen) {
        results[index].closed = true;
      }

      var restaurantId = results[index].properties.id;

      if (distanceUnit === 'kilometer') {
        results[index].properties.distance = parseFloat(Math.round((calculateDistance(restaurantLat, restaurantLong) / 1000) * 100) / 100).toFixed(2);
      } else if (distanceUnit === 'miles') {
        results[index].properties.distance = parseFloat(Math.round((calculateDistance(restaurantLat, restaurantLong) * 0.000621371192) * 100) / 100).toFixed(2);
      }

      var resEnabled = isRestaurantEnabled(results[index]);
      if (resEnabled) {
        enabledCount++;
      }

      results[index]["enabledCount"] = enabledCount;
      results[index]["isEnabled"] = resEnabled;

      if (index == 0) {
        results[index]["showMapItem"] = true;
      } else {
        results[index]["showMapItem"] = false;
      }

      $scope.addMarkerToMap(restaurantLat, restaurantLong, resEnabled, restaurantId);
    });
    $scope.enabledRestaurantsCount = enabledCount;
  };

  function calculateDistance(restLatitude, restLongitude) {
    var restLatLong = new google.maps.LatLng(restLatitude, restLongitude);
    var startLatLng = new google.maps.LatLng(referenceLatitudeForDistance, referenceLongitudeForDistance);
    var distanceInMeter = google.maps.geometry.spherical.computeDistanceBetween(startLatLng, restLatLong);
    return distanceInMeter;
  }

  $scope.addMarkerToMap = function (restaurantLat, restaurantLong, resEnabled, restaurantId) {
    var mcdPinIcon = resEnabled ? '/etc/clientlibs/mcd-us/img/mcd_marker.svg' : grayedOutRestImagePath;
    var myLatLng = new google.maps.LatLng(restaurantLat, restaurantLong);
    mcdPinIcon = "//chart.googleapis.com/chart?chst=d_map_pin_letter&chld=" + markerCount + "|ff4022";
    var marker = new google.maps.Marker({
      position: myLatLng,
      map: map,
      icon: mcdPinIcon,
      width: 33,
      height: 46,
      animation: google.maps.Animation.DROP,
      isEnabled: resEnabled
    });

    // Gives each marker an Id for the on click
    markerCount++;

    // Creates the event listener for clicking the marker and places the marker on the map
    marker.addListener('click', function () {
      map.setCenter(marker.getPosition());
      for (i = 0; i < $scope.restaurants.length; i++) {
        if ($scope.restaurants[i].properties.id == restaurantId) {
          $scope.restaurants[i]["showMapItem"] = true;
        } else {
          $scope.restaurants[i]["showMapItem"] = false;
        }
      }
      $scope.$apply(function () {
        $scope.restaurants = $scope.restaurants;
      });
    });

    markers.push(marker);
  };

  function initializeRestaurantFilters() {
    var sortField;
    var resFilters = $scope.fullRlData.filterString;
    if (resFilters !== undefined) {
      var filters = resFilters.split(',');
      $.each(filters, function (i, item) {
        if (item.indexOf(":") != -1) {
          var filterData = item.split(':');
          if (filterData[1].indexOf('dividecss') != -1) {
            var filterObject = {
              "filterName": filterData[0],
              "filterValue": filterData[1].split("dividecss")[0],
              "filterCss": filterData[1].split("dividecss")[1]
            };
          }
          $scope.restaurantAmenities.push(filterObject);
          if (filterData[2] == 'true') {
            $scope.restaurantFilters.push(filterObject);
          }
          $scope.restaurantFilterKey = $scope.restaurantFilterKey + filterData[1] + ",";
        }
      });
      removeDelimiterAtEnd();
    }
  }

  function initializeDialogConfigurations() {
    initializeErrorMessages();
    initializeRestaurantFilters();
    $scope.radius = $scope.fullRlData.radius;
    maxResults = $scope.fullRlData.maxResults;
    $scope.distanceUnit = $scope.fullRlData.distanceUnit;
    restaurantsResultCountry = $scope.fullRlData.geocountry;
    restaurantSearchCountry = $scope.fullRlData.country;
    restaurantsResultLanguage = $scope.fullRlData.language;
    resultMessage = $scope.fullRlData.resultMessage;
    showClosed = $scope.fullRlData.showClosed;
    hours24Text = $scope.fullRlData.Hours24Text;
    Hours24TextForAriaLabel = $scope.fullRlData.Hours24TextForAriaLabel;
    $scope.showLessText = $scope.fullRlData.showLess;
    $scope.showMoreText = $scope.fullRlData.showMore;
    //$scope.noResultFoundText=$scope.fullRlData.noResultFoundText;
    initializeRestaurantResultFooters();
  }

  function initializeRestaurantResultFooters() {
    if ($("div#hiddenRestaurantResultFooters").length > 0) {
      var sortField;
      var resFilters = $("div#hiddenRestaurantResultFooters").data("restaurant-footers");
      if (resFilters !== undefined) {
        var filters = resFilters.split(',');
        $.each(filters, function (i, item) {
          var filterData = item.split(':');

          var filterObject = {
            "footerName": filterData[0],
            "footerLink": filterData[2] != undefined ? filterData[1] + ":" + filterData[2] : filterData[1]
          };

          $scope.resultFooterLink.push(filterObject);
        });
        var lastChar = $scope.resultFooterLink.slice(-1);
        if (lastChar == ',') {
          $scope.resultFooterLink = $scope.resultFooterLink.substring(0, $scope.resultFooterLink.length - 1);
        }
      }
    }
  }

  function retrieveMaxResultListView() {
    if ($("div#hiddenRestaurantMax").length > 0) {
      $scope.restaurantEndSize = $("div#hiddenRestaurantMax").data("restaurant-result");
    }
  }

  $scope.clickLoadMore = function () {
    $scope.resultShown = parseInt($scope.resultShown) + parseInt($scope.restaurantEndSize);
    $scope.$apply(function () {
      $scope.resultShown = $scope.resultShown;
    });
    if ($scope.resultShown >= $scope.restaurants.length) {
      $scope.showLoadMore = false;
    }
  }

  function removeDelimiterAtEnd() {
    var lastChar = $scope.restaurantFilterKey.slice(-1);
    if (lastChar == ',') {
      $scope.restaurantFilterKey = $scope.restaurantFilterKey.substring(0, $scope.restaurantFilterKey.length - 1);
    }
  }

  $scope.selectedRestaurantFilters = [];

  $scope.filterRestaurants = function (filterName) {
    filterName = filterName.replace(/"/g, '');
    var idx = $scope.selectedRestaurantFilters.indexOf(filterName);
    // is currently selected
    if (idx > -1) {
      $scope.selectedRestaurantFilters.splice(idx, 1);
    }
    // is newly selected
    else {
      $scope.selectedRestaurantFilters.push(filterName);
    }
    processRestaurantFilters();
  };

  $scope.getFilterName = function (restaurant) {
    selectFilter = [];
    for (i = 0; i < $scope.restaurantAmenities.length; i++) {
      for (var k = 0; k < restaurant.properties.filterType.length; k++) {
        if (restaurant.properties.filterType[k] === $scope.restaurantAmenities[i].filterValue) {
          selectFilter.push($scope.restaurantAmenities[i]);
        }
      }
    }
    return selectFilter;
  }

  function phoneNumberForScreenReader() {
    for (var i = 0; i < $scope.restaurants.length; i++) {
      var restaurant = $scope.restaurants[i];
      restaurant.properties.telephoneTitle = restaurant.properties.telephone != undefined ? (restaurant.properties.telephone).split("").join(" ") : "";
    }

  }

  function HoursForScreenReader() {
    for (var i = 0; i < $scope.restaurants.length; i++) {
      var restaurant = $scope.restaurants[i];
      if (restaurant.properties.restauranthours != undefined) {
        restaurant.properties.restauranthours.hoursMondayForScreenReader = restaurant.properties.restauranthours.hoursMonday != undefined ? (((restaurant.properties.restauranthours.hoursMonday) != hours24Text) ? (restaurant.properties.restauranthours.hoursMonday).split(" - ").join(" to ") : Hours24TextForAriaLabel) : "";
        restaurant.properties.restauranthours.hoursTuesdayForScreenReader = restaurant.properties.restauranthours.hoursTuesday != undefined ? (((restaurant.properties.restauranthours.hoursTuesday) != hours24Text) ? (restaurant.properties.restauranthours.hoursTuesday).split(" - ").join(" to ") : Hours24TextForAriaLabel) : "";
        restaurant.properties.restauranthours.hoursWednesdayForScreenReader = restaurant.properties.restauranthours.hoursWednesday != undefined ? (((restaurant.properties.restauranthours.hoursWednesday) != hours24Text) ? (restaurant.properties.restauranthours.hoursWednesday).split(" - ").join(" to ") : Hours24TextForAriaLabel) : "";
        restaurant.properties.restauranthours.hoursThursdayForScreenReader = restaurant.properties.restauranthours.hoursThursday != undefined ? (((restaurant.properties.restauranthours.hoursThursday) != hours24Text) ? (restaurant.properties.restauranthours.hoursThursday).split(" - ").join(" to ") : Hours24TextForAriaLabel) : "";
        restaurant.properties.restauranthours.hoursFridayForScreenReader = restaurant.properties.restauranthours.hoursFriday != undefined ? (((restaurant.properties.restauranthours.hoursFriday) != hours24Text) ? (restaurant.properties.restauranthours.hoursFriday).split(" - ").join(" to ") : Hours24TextForAriaLabel) : "";
        restaurant.properties.restauranthours.hoursSaturdayForScreenReader = restaurant.properties.restauranthours.hoursSaturday != undefined ? (((restaurant.properties.restauranthours.hoursSaturday) != hours24Text) ? (restaurant.properties.restauranthours.hoursSaturday).split(" - ").join(" to ") : Hours24TextForAriaLabel) : "";
        restaurant.properties.restauranthours.hoursSundayForScreenReader = restaurant.properties.restauranthours.hoursSunday != undefined ? (((restaurant.properties.restauranthours.hoursSunday) != hours24Text) ? (restaurant.properties.restauranthours.hoursSunday).split(" - ").join(" to ") : Hours24TextForAriaLabel) : "";
      }
      if (restaurant.properties.drivethruhours != undefined && $scope.enableDriveHours) {
        restaurant.properties.drivethruhours.driveHoursMondayForScreenReader = restaurant.properties.drivethruhours.driveHoursMonday != undefined ? (((restaurant.properties.drivethruhours.driveHoursMonday) != hours24Text) ? (restaurant.properties.drivethruhours.driveHoursMonday).split(" - ").join(" to ") : Hours24TextForAriaLabel) : "";
        restaurant.properties.drivethruhours.driveHoursTuesdayForScreenReader = restaurant.properties.drivethruhours.driveHoursTuesday != undefined ? (((restaurant.properties.drivethruhours.driveHoursTuesday) != hours24Text) ? (restaurant.properties.drivethruhours.driveHoursTuesday).split(" - ").join(" to ") : Hours24TextForAriaLabel) : "";
        restaurant.properties.drivethruhours.driveHoursWednesdayForScreenReader = restaurant.properties.drivethruhours.driveHoursWednesday != undefined ? (((restaurant.properties.drivethruhours.driveHoursWednesday) != hours24Text) ? (restaurant.properties.drivethruhours.driveHoursWednesday).split(" - ").join(" to ") : Hours24TextForAriaLabel) : "";
        restaurant.properties.drivethruhours.driveHoursThursdayForScreenReader = restaurant.properties.drivethruhours.driveHoursThursday != undefined ? (((restaurant.properties.drivethruhours.driveHoursThursday) != hours24Text) ? (restaurant.properties.drivethruhours.driveHoursThursday).split(" - ").join(" to ") : Hours24TextForAriaLabel) : "";
        restaurant.properties.drivethruhours.driveHoursFridayForScreenReader = restaurant.properties.drivethruhours.driveHoursFriday != undefined ? (((restaurant.properties.drivethruhours.driveHoursFriday) != hours24Text) ? (restaurant.properties.drivethruhours.driveHoursFriday).split(" - ").join(" to ") : Hours24TextForAriaLabel) : "";
        restaurant.properties.drivethruhours.driveHoursSaturdayForScreenReader = restaurant.properties.drivethruhours.driveHoursSaturday != undefined ? (((restaurant.properties.drivethruhours.driveHoursSaturday) != hours24Text) ? (restaurant.properties.drivethruhours.driveHoursSaturday).split(" - ").join(" to ") : Hours24TextForAriaLabel) : "";
        restaurant.properties.drivethruhours.driveHoursSundayForScreenReader = restaurant.properties.drivethruhours.driveHoursSunday != undefined ? (((restaurant.properties.drivethruhours.driveHoursSunday) != hours24Text) ? (restaurant.properties.drivethruhours.driveHoursSunday).split(" - ").join(" to ") : Hours24TextForAriaLabel) : "";
      }
    }

  }

  function processRestaurantFilters() {
    var enabledCount = 0;
    markerCount = 1;
    var resFilter = [];
    var resFilterfalse = [];
    deleteMarkers();
    var mapIcon = {
      url: grayedOutRestImagePath, // url
      scaledSize: new google.maps.Size(markerHeight, markerWidth), // scaled size
    };
    var mapMatched = 0;
    for (var i = 0; i < $scope.restaurants.length; i++) {
      var filterMatch = true;
      for (var j = 0; j < $scope.selectedRestaurantFilters.length; j++) {
        var restaurant = $scope.restaurants[i];
        var isInRestaurant = false;
        for (var k = 0; k < restaurant.properties.filterType.length; k++) {
          if (restaurant.properties.filterType[k] === $scope.selectedRestaurantFilters[j]) {
            isInRestaurant = true;
          }
        }
        if (!isInRestaurant) {
          filterMatch = false;
        }
      }
      if (filterMatch) {
        mapMatched++;
        if (mapMatched === 1) {
          $scope.restaurants[i]["showMapItem"] = true;
        } else {
          $scope.restaurants[i]["showMapItem"] = false;
        }
        enabledCount++;
        $scope.restaurants[i]["enabledCount"] = enabledCount;
        $scope.restaurants[i]["isEnabled"] = true;
        $scope.addMarkerToMap($scope.restaurants[i].geometry.coordinates[1], $scope.restaurants[i].geometry.coordinates[0], true, $scope.restaurants[i].properties.id);
      } else {
        $scope.restaurants[i]["isEnabled"] = false;
      }
    }

    for (var i = 0; i < $scope.restaurants.length; i++) {
      if ($scope.restaurants[i].isEnabled === true) {
        resFilter.push($scope.restaurants[i]);
      } else {
        resFilterfalse.push($scope.restaurants[i]);
      }
    }
    for (var j = 0; j < resFilterfalse.length; j++) {
      resFilter.push(resFilterfalse[j]);
    }
    $scope.restaurants = resFilter;
    $scope.enabledRestaurantsCount = enabledCount;
    google.maps.event.trigger(map, 'resize');
  };

  function isRestaurantEnabled(restaurant) {
    for (var j = 0; j < $scope.selectedRestaurantFilters.length; j++) {
      if (restaurant.properties[$scope.selectedRestaurantFilters[j]] != "1") {
        return false;
      }
    }
    return true;
  };

  $scope.searchRestaurants = function (searchText) {
    if (searchText != undefined && searchText != '') {
      $scope.geocodeAddress(searchText);
    }
  };

  $scope.showRestaurantForMulti = function (e) {
    var value = $(e.target).text();
    $scope.restaurantSearchText = value;
    $scope.isMulti = false;
    $('#restaurantLocatorDisambiguityModal').find('.btn-close').click()
    $scope.searchRestaurants(value);
  }

  function setMapOnAll(map) {
    for (var i = 0; i < markers.length; i++) {
      markers[i].setMap(map);
    }
  };

  // Deletes all markers in the array by removing references to them.
  function deleteMarkers() {
    setMapOnAll(null);
    markers = [];
  };

  $scope.showRestaurantsForCurrentLocation = function () {
    if ($scope.showPopUp()) {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPosition, showError);
      } else {
        console.log("Geolocation is not supported by this browser.");
      };
    }
  };

  function showPositionMap(position) {
    var latitude = position.coords.latitude;
    var longitude = position.coords.longitude;
    $scope.currentLatitude = latitude;
    $scope.currentLongitude = longitude;
  };

  function showPosition(position) {

    var latitude = position.coords.latitude;
    var longitude = position.coords.longitude;
    $scope.currentLatitude = latitude;
    $scope.currentLongitude = longitude;
    if (getCookie("geoLocationDataNearestLoc") == null) {
      $scope.getGlRestaurantsDetail(latitude, longitude);
    }

  };

  function showErrorMap(error) {
    switch (error.code) {
      case error.PERMISSION_DENIED:
        console.log($scope.fullRlData.locationPermissionDeniedText);
        break;
      case error.POSITION_UNAVAILABLE:
        console.log($scope.fullRlData.locationUnavailableText);
        break;
      case error.TIMEOUT:
        console.log($scope.fullRlData.locationTimeoutText);
        break;
      case error.UNKNOWN_ERROR:
        console.log($scope.fullRlData.locationUnknownErrorText);
        break;
    }
  };

  function showError(error) {
    var restaurant = getCookie(cookieVal);

    if (restaurant != undefined) {
      $scope.showPrefered = true;
      $scope.restaurants.push(restaurant);
      $scope.firstRestaurantName = restaurant.properties.name ? restaurant.properties.name : restaurant.properties.addressLine1;
    } else {
      $scope.showDefault = true;
    }

    switch (error.code) {
      case error.PERMISSION_DENIED:
        console.log($scope.fullRlData.locationPermissionDeniedText);
        break;
      case error.POSITION_UNAVAILABLE:
        console.log($scope.fullRlData.locationUnavailableText);
        break;
      case error.TIMEOUT:
        console.log($scope.fullRlData.locationTimeoutText);
        break;
      case error.UNKNOWN_ERROR:
        console.log($scope.fullRlData.locationUnknownErrorText);
        break;
    }

    $scope.$apply(function () {
      $scope.showDefault = $scope.showDefault;
      $scope.showPrefered = $scope.showPrefered;
      $scope.showGeo = $scope.showGeo;
    });
  };

  function initializeErrorMessages() {
    if ($("div#hideLocationPermissionDeniedText").length > 0) {
      locationPermissionDeniedText = $("div#hideLocationPermissionDeniedText").data("location-permission-denied-text");
    }
    if ($("div#hideLocationUnavailableText").length > 0) {
      locationUnavailableText = $("div#hideLocationUnavailableText").data("location-unavailable-text");
    }
    if ($("div#hideLocationTimeoutText").length > 0) {
      locationTimeoutText = $("div#hideLocationTimeoutText").data("location-timeout-text");
    }
    if ($("div#hideLocationUnknownErrorText").length > 0) {
      locationUnknownErrorText = $("div#hideLocationUnknownErrorText").data("location-unknown-error-text");
    }
    if ($("div#hideNoRouteText").length > 0) {
      noRouteText = $("div#hideNoRouteText").data("no-route-text");
    }
    if ($("div#hideDirectionUnknownErrorText").length > 0) {
      directionUnknownErrorText = $("div#hideDirectionUnknownErrorText").data("direction-unknown-error-text");
    }
  };

  // First, checks if it isn't implemented yet.
  if (!String.prototype.format) {
    String.prototype.format = function () {
      var args = arguments;
      return this.replace(/{(\d+)}/g, function (match, number) {
        return typeof args[number] != 'undefined' ?
          args[number] : match;
      });
    };
  }

  $scope.showRestaurantsForCurrentLocationHeader = function (event) {
    var form = $(event.target).parents('form:first');
    var input = $("<input>").attr("type", "hidden").attr("name", "locate").val("true");
    $(form).append($(input));

    var radiusSelected;
    $(form).find('.dropdown-toggle').each(function () {
      radiusSelected = parseInt($(this).text());
    });

    var input1 = $("<input>").attr("type", "hidden").attr("name", "radiusSelected").val(radiusSelected);
    $(form).append($(input1));
  };

  $scope.searchRestaurantsHeader = function (restaurantSearchText, event) {
    if (restaurantSearchText == undefined || restaurantSearchText == '') {
      event.preventDefault();
      alert($scope.fullRlData.emptySearchErrorMsg);
      return false;
    }
    var form = $(event.target).parents('form:first');
    var input = $("<input>").attr("type", "hidden").attr("name", "submit").val("true");
    var radiusSelected;
    $(form).find('.dropdown-toggle').each(function () {
      radiusSelected = parseInt($(this).text());
    });

    $(form).append($(input));
    var input1 = $("<input>").attr("type", "hidden").attr("name", "radiusSelected").val(radiusSelected);
    $(form).append($(input1));
    var input2 = $("<input>").attr("type", "hidden").attr("name", "searchText").val(restaurantSearchText);
    $(form).append($(input2));
  }
  $scope.getMFilterLimit = function (index) {
    return index == 4 ? true : false;
  }

  $scope.getRestaurantFullAddress = function (restAddressLine1, restAddressLine2, restAddressLine3) {
    var fullAddress = "";
    if (restAddressLine1 !== undefined && restAddressLine1.trim() != '') {
      fullAddress = fullAddress + restAddressLine1;
    }
    if (restAddressLine2 !== undefined && restAddressLine2.trim() != '') {
      fullAddress = fullAddress + ", " + restAddressLine2;
    }
    if (restAddressLine3 !== undefined && restAddressLine3.trim() != '') {
      fullAddress = fullAddress + " " + restAddressLine3;
    }
    return fullAddress;
  };

  $scope.getRestaurantFullAddress1 = function (name, restAddressLine1, restAddressLine3, subdivision, postcode) {
    var fullAddress = "";
    if (name !== undefined && name.trim() != '') {

      if (restAddressLine1 !== undefined && restAddressLine1.trim() != '') {
        fullAddress = fullAddress + "<b>" + restAddressLine1 + "</b><br>";
      }
      if (restAddressLine3 !== undefined && restAddressLine3.trim() != '') {
        fullAddress = fullAddress + restAddressLine3;
      }
      if (subdivision !== undefined && subdivision.trim() != '') {
        fullAddress = fullAddress + ", " + subdivision;
      }
      if (postcode !== undefined && postcode.trim() != '') {
        fullAddress = fullAddress + ", " + postcode;
      }
    }
    else {
      if (restAddressLine3 !== undefined && restAddressLine3.trim() != '') {
        fullAddress = fullAddress + restAddressLine3;
      }
      if (subdivision !== undefined && subdivision.trim() != '') {
        fullAddress = fullAddress + ", " + subdivision;
      }
      if (postcode !== undefined && postcode.trim() != '') {
        fullAddress = fullAddress + " " + postcode;
      }
    }
    return fullAddress;
  };

  $scope.getResultMessage = function () {
    if (resultMessage != undefined) {
      var totalResults = $scope.enabledRestaurantsCount;
      var start = resultMessage.indexOf("{");
      var end = resultMessage.indexOf("}");
      resultFinalMessage = resultMessage.substring(0, start) + totalResults + resultMessage.substring(end + 1, resultMessage.length);
      return resultFinalMessage;
    }
  };

  $scope.getPhoneNumber = function (phone) {
    return "tel:" + phone;
  }

  function removeHoursForScreenReader(restCookie) {
    var restaurant = restCookie;
    if (restaurant.properties.restauranthours != undefined && restaurant.properties.restauranthours.hoursMondayForScreenReader != undefined) {
      delete restaurant.properties.restauranthours.hoursMondayForScreenReader;
      delete restaurant.properties.restauranthours.hoursTuesdayForScreenReader;
      delete restaurant.properties.restauranthours.hoursWednesdayForScreenReader;
      delete restaurant.properties.restauranthours.hoursThursdayForScreenReader;
      delete restaurant.properties.restauranthours.hoursFridayForScreenReader;
      delete restaurant.properties.restauranthours.hoursSaturdayForScreenReader;
      delete restaurant.properties.restauranthours.hoursSundayForScreenReader;
    }
    if (restaurant.properties.drivethruhours != undefined && restaurant.properties.drivethruhours.driveHoursMondayForScreenReader != undefined) {
      delete restaurant.properties.drivethruhours.driveHoursMondayForScreenReader;
      delete restaurant.properties.drivethruhours.driveHoursTuesdayForScreenReader;
      delete restaurant.properties.drivethruhours.driveHoursWednesdayForScreenReader;
      delete restaurant.properties.drivethruhours.driveHoursThursdayForScreenReader;
      delete restaurant.properties.drivethruhours.driveHoursFridayForScreenReader;
      delete restaurant.properties.drivethruhours.driveHoursSaturdayForScreenReader;
      delete restaurant.properties.drivethruhours.driveHoursSundayForScreenReader;
    }

    /*if(restaurant.properties.filterType !=undefined){
      delete  restaurant.properties.filterType;
    }*/
    return restaurant;
  }


}]);
