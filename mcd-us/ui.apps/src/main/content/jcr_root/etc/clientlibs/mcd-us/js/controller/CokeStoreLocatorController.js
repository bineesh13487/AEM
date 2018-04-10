app.controller("CokeStoreLocatorController", ['$scope', '$http', 'Scopes', 'CokeLocatorService', '$timeout',
    function($scope, $http, Scopes, CokeLocatorService, $timeout) {

        //Scope variables
        $scope.searchText = "";
        $scope.radiusList = radiusList;
        $scope.flavOptions = [];
        $scope.catOption = "";
        $scope.flavOption = "";
        $scope.resultShown = "";
        $scope.displayRlResultView = false;
        $scope.restaurants = [];
        $scope.radius = [];
        $scope.showTabContent = false;
        $scope.enabledRestaurantsCount = 0;
        $scope.multipleLocations = [];
        $scope.restaurantRadius = "";
        $scope.locatorcount = 0;
        $scope.currentLatitude;
        $scope.currentLongitude;
        $scope.showListview;
        $scope.cokeStoreJSON = {};
        $scope.isMulti = false;
        $scope.restaurantEndSize;
        $scope.cokeFilters;
        $scope.filteredData = {};
        $scope.searchType;
        $scope.redirectUrl;
        $scope.resultShown = 5;
        var initialResultSize = $scope.resultShown;
        var latitude, longitude;
        $scope.map = "";

        //Private Methods
        var init;
        /**
         * Initialization method. Called on load.
         */
        init = function() {
            Scopes.store('CokeStoreLocatorController', $scope);
            if ($('.cat-flavselect').length > 0) {
                $('.search-locate').addClass('flavor-exist');
            }
            $scope.radiusList = radiusList;
            $scope.cokeStoreJSON = JSON.parse(cokeStoreJSON);
            $scope.resultMessage = $scope.cokeStoreJSON.resultMessage.replace('{0}', '<span class="result-count">' + $scope.locatorcount + '</span>');
            $scope.cokeFilters = JSON.parse($scope.cokeStoreJSON.cokeFilters);
            if ($scope.cokeFilters) {
                $scope.cokeFilters.map(function(item, index) {
                    if (item.categoryVal in $scope.filteredData) {
                        if (item.flavorCode.length) {
                            $scope.filteredData[item.categoryVal].flavorCode = $scope.filteredData[item.categoryVal].flavorCode.concat(item.flavorCode);
                        }
                        if (item.flavorVal) {
                            $scope.filteredData[item.categoryVal].flavorVal = $scope.filteredData[item.categoryVal].flavorVal.concat(',', item.flavorVal);
                        }
                    } else {
                        $scope.filteredData[item.categoryVal] = $.extend(true, {}, item);
                    }
                });
            }
            $scope.filteredData = $scope.filteredData;

            var urlParams;
            (window.onpopstate = function() {
                var match,
                    pl = /\+/g, // Regex for replacing addition symbol with a space
                    search = /([^&=]+)=?([^&]*)/g,
                    decode = function(s) {
                        return decodeURIComponent(s.replace(pl, " "));
                    },
                    query = window.location.search.substring(1);
                //alert(match.length);
                urlParams = {};
                while (match = search.exec(query))
                    urlParams[decode(match[1])] = decode(match[2]);

            })();

            setTimeout(function() {
                $scope.defaultCategory = $('div[name="cslCategory"]').find('button').find('.filter-option').text();
                $scope.defaultFlavor = $('div[name="cslFlavors"]').find('button').find('.filter-option').text();
                if (urlParams['searchText'] != undefined || urlParams["radius"] != undefined || urlParams['searchType'] != undefined || urlParams['searchType'] != undefined || urlParams['categoryCode'] != undefined || urlParams['flavorCode'] != undefined || urlParams['latitude'] != undefined || urlParams['longitude'] != undefined) {

                    $scope.searchText = urlParams['searchText'];
                    $scope.searchType = urlParams['searchType'];
                    $scope.currentLatitude = urlParams['latitude'];
                    $scope.currentLongitude = urlParams['longitude'];
                    $scope.catCode = urlParams['categoryCode'];
                    $scope.flavCode = urlParams['flavorCode'];

                    $scope.categoryCode = $scope.catCode == $scope.defaultCategory || $scope.catCode == "" || $scope.catCode == null || $scope.catCode == undefined ? "*" : $scope.catCode;
                    flavOption = $scope.flavCode == $scope.defaultFlavor || $scope.flavCode == "" || $scope.flavCode == undefined ? "*" : $scope.flavCode;
                    $scope.distanceText = $scope.cokeStoreJSON.distanceUnit == "mi" ? $scope.cokeStoreJSON.milesText : $scope.cokeStoreJSON.kilometerText;
                    $scope.restaurantRadius = urlParams["radius"] + " " + $scope.distanceText;
                    $('div[name="cslRadius"]').find('button').find('.filter-option').text($scope.restaurantRadius);
                    $scope.radius = parseInt($('div[name="cslRadius"]').find('button').find('.filter-option').text());
                    $scope.getStoresDetail($scope.currentLatitude, $scope.currentLongitude, $scope.searchText, $scope.radius, $scope.categoryCode, flavOption, $scope.searchType);
                }
            }, 300);



        };


        /**
         *  Scope Methods
         */

        //Category and Flavors Drop-down selection
        $scope.getFlavours = function() {
            if ($scope.catOption === null) {
                $(".coke-flavor-select").addClass('disabled');
                $scope.flavOptions = [];
                $scope.newflavOptions = $scope.flavOptions;
                $scope.flavOption = "All";
            } else {
                $(".coke-flavor-select").removeClass('disabled');
                $scope.newflavOptions = $scope.catOption.flavorVal.split(",");
            }
            setTimeout(function() {
                $('.coke-flavor-select').selectpicker('refresh');
            }, 100);
        }
        $scope.setFlavors = function() {
            setTimeout(function() {
                var flavors = $scope.catOption.flavorCode
                var getIndex = $scope.newflavOptions.indexOf($scope.flavOptions)
                $scope.flavOption = flavors[getIndex];
            }, 500);
        }
        //Search button click coke Locater 
        $scope.searchCokeLocator = function(searchText) {
            if (searchText != undefined && searchText != '') {
                $scope.restaurantRadius = parseInt($('div[name="cslRadius"]').find('button').find('.filter-option').text());
                $scope.searchType = "NON-GEO";
                if ($scope.catOption != undefined) {
                    $scope.categoryCode = $scope.catOption.categoryVal;
                }

                $scope.categoryCode = $scope.categoryCode == $scope.defaultCategory || $scope.categoryCode == "" || $scope.catOption == null || $scope.categoryCode == undefined ? "*" : $scope.categoryCode;
                flavOption = $scope.flavOption == $scope.defaultFlavor || $scope.flavOption == "" || $scope.flavOption == undefined ? "*" : $scope.flavOption;

                if ($scope.cokeStoreJSON.redirect == true) {
                    $scope.categoryCode = $scope.categoryCode == "*" ? $scope.defaultCategory : $scope.categoryCode;
                    flavOption = flavOption == "*" ? $scope.defaultFlavor : flavOption;
                    $scope.redirectUrl = $scope.cokeStoreJSON.cokeStoreLocationPath + ".html?searchText=" + $scope.searchText + "&radius=" + $scope.restaurantRadius + "&categoryCode=" + $scope.categoryCode + "&flavorCode=" + flavOption + "&searchType=" + $scope.searchType;
                    window.location.href = $scope.redirectUrl;
                } else {
                    $scope.getStoresDetail(latitude, longitude, searchText, $scope.restaurantRadius, $scope.categoryCode, flavOption, $scope.searchType);
                }

            } else {
                alert("Please enter a zip code or city & state to search");
            }


        }
        //Get Restaurant Details 
        $scope.getStoresDetail = function(latitude, longitude, searchText, restaurantRadius, catOption, flavOption, searchType) {
            var markers = [];
            var markerCount = 1;

            latitude = latitude ? latitude : '';
            longitude = longitude ? longitude : '';
            var inputData = {
                searchType: searchType,
                searchText: searchText,
                clientId: $scope.cokeStoreJSON.clientId,
                latitude: latitude,
                longitude: longitude,
                distance: restaurantRadius,
                beverageCategoryCode: $scope.categoryCode,
                flavorCode: flavOption,
                locale: $scope.cokeStoreJSON.locale,
                distanceUnit: $scope.cokeStoreJSON.distanceUnit,
                recordsCount: $scope.cokeStoreJSON.maxResults
            };
            CokeLocatorService.getCokeStores(inputData, $scope.cokeStoreJSON.cokeStoreLocationsUrl).then(function(data) {
                var cokeStores = data;
                $scope.resultShown = 5;
                initialResultSize = $scope.resultShown;
                if (cokeStores.statusCode) {
                    if (cokeStores.data.location.length > 0) {
                        if (cokeStores.data.locationAmbiguity == true) {
                            $scope.isMulti = true;
                            $scope.multipleLocations = cokeStores.data.location;
                            $scope.showDisambiguityModal();
                        } else {
                            $scope.locatorcount = 0;
                            $scope.displayRlResultView = true;
                            $scope.showTabContent = true;
                            $scope.locatorcount = cokeStores.data.location.length;
                            $scope.restaurantEndSize = $scope.locatorcount;
                            $scope.restaurants = cokeStores.data.location;
                            $scope.restaurantsResult = cokeStores.data.location;
                            if ($scope.currentLatitude == undefined || $scope.currentLongitude == undefined) {
                                if (navigator.geolocation) {
                                    navigator.geolocation.getCurrentPosition($scope.showPosition);

                                } else {
                                    console.log("Geo-location is not supported by this browser.");
                                };
                            }

                            $scope.initMap(cokeStores);
                            if ($scope.restaurantEndSize <= $scope.resultShown) {
                                $scope.showLoadMore = false;
                            } else {
                                $scope.showLoadMore = true;
                            }
                            //phoneNumberForScreenReader(); 
                            //$scope.restaurants =  $scope.restaurants;   
                            $('span.result-count').text($scope.locatorcount);

                        }
                    } else {
                        $scope.displayRlResultView = true;
                        $scope.showTabContent = true;
                        $scope.locatorcount = 0;
                        $('span.result-count').text($scope.locatorcount);
                        $scope.restaurants = [];
                        $scope.showTabContent = false;

                    }
                } else {
                    if (cokeStores.error.status == 401) {
                        alert($scope.cokeStoreJSON.invalidGeoLocationErrorText);
                    } else if (cokeStores.error.status == 402) {
                        alert($scope.cokeStoreJSON.serviceUnavailableErrorText);
                    } else {
						 alert($scope.cokeStoreJSON.serviceUnavailableErrorText);
					}
                    $scope.locatorcount = 0;
                    $('span.result-count').text($scope.locatorcount);
                    $scope.restaurants = [];
                    $scope.showTabContent = false;
                }

            }, function(data) {
				alert($scope.cokeStoreJSON.serviceUnavailableErrorText);
            });

        }
        $scope.showCokeStorePosition = function(position) {
            $scope.$apply(function() {
                $scope.currentLatitude = position.coords.latitude;
                $scope.currentLongitude = position.coords.longitude;
                var latitude = $scope.currentLatitude;
                var longitude = $scope.currentLongitude;
                $scope.searchText = "";
                $scope.searchType = "GEO";
                $scope.restaurantRadius = parseInt($('div[name="cslRadius"]').find('button').find('.filter-option').text());

                if ($scope.catOption != undefined) {
                    $scope.categoryCode = $scope.catOption.categoryVal;
                }
                $scope.categoryCode = $scope.categoryCode == $scope.defaultCategory || $scope.categoryCode == "" || $scope.catOption == null || $scope.categoryCode == undefined ? "*" : $scope.categoryCode;
                flavOption = $scope.flavOption == $scope.defaultFlavor || $scope.flavOption == "" || $scope.flavOption == undefined ? "*" : $scope.flavOption;

                if ($scope.cokeStoreJSON.redirect == true) {
                    $scope.categoryCode = $scope.categoryCode == "*" ? $scope.defaultCategory : $scope.categoryCode;
                    flavOption = flavOption == "*" ? $scope.defaultFlavor : flavOption;
                    $scope.redirectUrl = $scope.cokeStoreJSON.cokeStoreLocationPath + ".html?radius=" + $scope.restaurantRadius + "&categoryCode=" + $scope.categoryCode + "&flavorCode=" + flavOption + "&searchType=" + $scope.searchType + "&latitude=" + $scope.currentLatitude + "&longitude=" + $scope.currentLongitude;;
                    window.location.href = $scope.redirectUrl;
                } else {
                    $scope.getStoresDetail(latitude, longitude, $scope.searchText, $scope.restaurantRadius, $scope.categoryCode, flavOption, $scope.searchType);
                }
            });
        }
        $scope.showPosition = function(position) {
            $scope.$apply(function() {
                $scope.currentLatitude = position.coords.latitude;
                $scope.currentLongitude = position.coords.longitude;
            });
        }
        //Locate Me button click
        $scope.showStoresForCurrentLocation = function() {
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition($scope.showCokeStorePosition, CokeLocatorService.showError);
            } else {
                console.log("Geo-location is not supported by this browser.");
            }
        }
        $scope.setAlerts = function(event) {
            try {
                if ($(event.currentTarget).hasClass("collapsed")) {
                    $("<div role='alert' class='visuallyhidden'>" + "Exapanded" + "</div>").appendTo("body");
                } else {
                    $("<div role='alert' class='visuallyhidden'>" + "Collapsed" + "</div>").appendTo("body");
                }
            } catch (ex) {}
        }
        $scope.showStore = function(item, index) {
            if (index == 0) {
                return true;
            }
            return false;
        }
        //List view Load More 
        $scope.loadMoreCokeStores = function() {
            $scope.resultShown = $scope.resultShown + initialResultSize;
            if ($scope.restaurantEndSize > $scope.resultShown) {
                $scope.showLoadMore = true;
            } else {
                $scope.showLoadMore = false;
            }

        };
        //Map view tab click
        $scope.mapViewClick = function() {
            if ($('#rl-mapView').hasClass("tab-pane")) {
                $('#rl-mapView').removeClass("tab-pane");
            }

            if ($scope.restaurants.length > 0) {
				var latlngbounds = new google.maps.LatLngBounds();
                //$scope.initMap($scope.restaurants,$scope.map);
                google.maps.event.trigger($scope.map, 'resize');
                for (i = 0; i < $scope.restaurants.length; i++) {
					var myLatLng = new google.maps.LatLng($scope.restaurants[i].outlet.address.latitude, $scope.restaurants[i].outlet.address.longitude);
					latlngbounds.extend(myLatLng);
                } 
				$scope.map.fitBounds(latlngbounds); 
				
            }
        };
        // Create a map object and specify the DOM element for display. 
        $scope.initMap = function(cokeOutlets) {
            $scope.map = "";
            var mapOptions;
            var cokeOutlets = cokeOutlets.data.location;

            mapOptions = {
                zoom: 11,
                center: new google.maps.LatLng(cokeOutlets[0].outlet.address.latitude, cokeOutlets[0].outlet.address.longitude),
                zoomControl: true,
                zoomControlOptions: {
                    position: google.maps.ControlPosition.LEFT_CENTER
                },
                streetViewControl: true,
                mapTypeId: google.maps.MapTypeId.ROADMAP
            };
            $scope.map = new google.maps.Map(document.getElementById("gmap_canvas"), mapOptions);
            var distanceUnit;
            var markers = [];
            var markerCount = 0;
            $.each(cokeOutlets, function(index, element) {
                var restaurantLat = cokeOutlets[index].outlet.address.latitude;
                var restaurantLong = cokeOutlets[index].outlet.address.longitude;
                var restaurantId = cokeOutlets[index].outlet.nameId2;
                markerCount++;
                CokeLocatorService.addMarkerToMap(cokeOutlets, restaurantLat, restaurantLong, restaurantId, $scope.map, markers, markerCount);
            });

            var latlngbounds = new google.maps.LatLngBounds();
            for (var i = 0; i < cokeOutlets.length; i++) {
                var myLatLng = new google.maps.LatLng(cokeOutlets[i].outlet.address.latitude, cokeOutlets[i].outlet.address.longitude);
                latlngbounds.extend(myLatLng);
            }
            $scope.map.fitBounds(latlngbounds);
        };

        //List view tab click
        $scope.listViewClick = function() {
            $('#rl-mapView').addClass("tab-pane");
            $scope.showListview = true;
        };

        $scope.showCokestoreForMulti = function(e, location) {
            $scope.searchText = $(e.target).text();
            $scope.isMulti = false;
            $scope.searchType = "GEO";
            $('#restaurantLocatorDisambiguityModal').find('.btn-close').click();
            $scope.restaurantRadius = parseInt($('div[name="cslRadius"]').find('button').find('.filter-option').text());
            if ($scope.catOption != undefined) {
                $scope.categoryCode = $scope.catOption.categoryVal;
            }

            $scope.categoryCode = $scope.categoryCode == $scope.defaultCategory || $scope.categoryCode == "" || $scope.catOption == null || $scope.categoryCode == undefined ? "*" : $scope.categoryCode;
            flavOption = $scope.flavOption == $scope.defaultFlavor || $scope.flavOption == "" || $scope.flavOption == undefined ? "*" : $scope.flavOption;
            $scope.getStoresDetail(location.latitude, location.longitude, $scope.searchText, $scope.restaurantRadius, $scope.categoryCode, flavOption, $scope.searchType);
            $('.modal-backdrop').remove();

        }
        $scope.showDisambiguityModal = function() {
            $('.disambiguitydemo').click();
        };


        //Init method call
        init();
    }
]);