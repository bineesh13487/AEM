app.service('getCoop', function () {
  this.getCoopId = function () {
    const cookieVal = 'coopid';
    let restaurant;
    const name = `${cookieVal}=`;
    const isCookie = false;
    const ca = document.cookie.split(';');
    for (let i = 0; i < ca.length; i++) {
      let c = ca[i];
      while (c.charAt(0) == ' ') c = c.substring(1);
      if (c.indexOf(name) == 0) {
        c = decodeURIComponent(c);

        return c.substring(name.length, c.length);

      }
    }


  };
  this.getCoopIdBackup = function () {
    const cookieVal = 'geoLocationData';
    let restaurant;
    const name = `${cookieVal}=`;
    const ca = document.cookie.split(';');
    for (let i = 0; i < ca.length; i++) {
      let c = ca[i];
      while (c.charAt(0) == ' ') c = c.substring(1);
      if (c.indexOf(name) == 0) {
        c = decodeURIComponent(c);

        restaurant = JSON.parse(c.substring(name.length, c.length));
        const storeId = restaurant.properties.identifiers.storeIdentifier;
        for (j = 0; j < storeId.length; j++) {
          if (storeId[j].identifierType === 'Co-Op ID') {
            return storeId[j].identifierValue;
          }
        }
      }
    }

  };

  this.getCoop = function () {
    const cookieVal = 'geoLocationData';
    let restaurant;
    const name = `${cookieVal}=`;
    const ca = document.cookie.split(';');
    for (let i = 0; i < ca.length; i++) {
      let c = ca[i];
      while (c.charAt(0) == ' ') c = c.substring(1);
      if (c.indexOf(name) == 0) {
        c = decodeURIComponent(c);

        restaurant = JSON.parse(c.substring(name.length, c.length));
        const storeId = restaurant.properties.identifiers.storeIdentifier;
        for (j = 0; j < storeId.length; j++) {
          if (storeId[j].identifierType === 'Co-Op') {
            return storeId[j].identifierValue;
          }
        }
      }
    }

  };

});

/* global angular, config */
/* eslint no-use-before-define: ["error", { "functions": false }] */
/* eslint func-names: ["error", "never"] */

(function () {

  angular
    .module('usr')
    .controller('restaurantLocatorController', RestaurantLocatorController);

  RestaurantLocatorController.$inject = ['$scope', 'Scopes', 'GetPathForCookie', '$location'];
  function RestaurantLocatorController($scope, Scopes, GetPathForCookie, $location) {

    let markerCount = 1;
    let infowindow;
    let htmlMarker;
    let initializeMapStatus = true;
    $scope.displaySearchResults = true;
    $scope.isSearchInputFocus = false;
    $scope.isMulti = false;
    $scope.showListView = false;
    $scope.showFilters = false;
    $scope.totalVisibleRestaurants = 5;
    $scope.displayRlResultView = false;
    $scope.fullRlData = {};
    $scope.enabledRestaurantsCount = 0;
    let coopid = "coopid";
    let cookieVal = "geoLocationData";
    let cookieValNearestLoc = "geoLocationDataNearestLoc";
    let markers = [];
    let map;
    let modalExistanceCheck = false;
    let defaultLatLang = {lat: 34.0002802, lng: -118.2908032};
    const restaurantData = {
      "features": [{
        "geometry": {
          "coordinates": [-118.265526, 33.945194]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "05:00 - 22:00",
          "driveTodayHours": "05:00 - 01:00",
          "id": "195500220291-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS", "INDOORPLAYGROUND", "INDOORDININGAVAILABLE", "OUTDOORPLAYPLACEAVAILABLE", "CREATEYOURTASTEAVAILABLE"],
          "addressLine1": "10011 S AVALON BLVD",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "LOS ANGELES",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90003",
          "telephone": "(323) 756-7094",
          "restauranthours": {
            "hoursMonday": "05:00 - 22:00",
            "hoursTuesday": "05:00 - 22:00",
            "hoursWednesday": "05:00 - 22:00",
            "hoursThursday": "05:00 - 22:00",
            "hoursFriday": "05:00 - 22:00",
            "hoursSaturday": "05:00 - 22:00",
            "hoursSunday": "05:00 - 22:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "05:00 - 01:00",
            "driveHoursTuesday": "05:00 - 01:00",
            "driveHoursWednesday": "05:00 - 01:00",
            "driveHoursThursday": "05:00 - 01:00",
            "driveHoursFriday": "04:00 - 04:00",
            "driveHoursSaturday": "04:00 - 04:00",
            "driveHoursSunday": "04:00 - 01:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "40247"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "1418"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500220291"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/USMCD1000889131/Hire/Recruiting/Application/Index?businessUnitId\u003dC21DCFFE-F2D2-4CAC-9948-A6A600277017",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "1418"
        }
      }, {
        "geometry": {
          "coordinates": [-118.231385, 33.95817]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "04:00 - 22:00",
          "driveTodayHours": "04:00 - 04:00",
          "id": "195500227306-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS", "INDOORDININGAVAILABLE", "CREATEYOURTASTEAVAILABLE"],
          "addressLine1": "8681 S ALAMEDA",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "LOS ANGELES",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90002",
          "telephone": "(323) 583-3266",
          "restauranthours": {
            "hoursMonday": "04:00 - 22:00",
            "hoursTuesday": "04:00 - 22:00",
            "hoursWednesday": "04:00 - 22:00",
            "hoursThursday": "04:00 - 22:00",
            "hoursFriday": "04:00 - 23:00",
            "hoursSaturday": "04:00 - 23:00",
            "hoursSunday": "04:00 - 22:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "04:00 - 04:00",
            "driveHoursTuesday": "04:00 - 04:00",
            "driveHoursWednesday": "04:00 - 04:00",
            "driveHoursThursday": "04:00 - 04:00",
            "driveHoursFriday": "04:00 - 04:00",
            "driveHoursSaturday": "04:00 - 04:00",
            "driveHoursSunday": "04:00 - 04:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "42408"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "14369"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500227306"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/USMCD1000889278/Hire/Recruiting/Application/Index?businessUnitId\u003d32C46B83-3BDA-42DE-8345-A6A60027F5BA",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "14369"
        }
      }, {
        "geometry": {
          "coordinates": [-118.274338, 33.96051]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "05:00 - 23:00",
          "driveTodayHours": "04:00 - 04:00",
          "id": "195500226019-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS", "INDOORPLAYGROUND", "INDOORDININGAVAILABLE", "INDOORPLAYPLACEAVAILABLE"],
          "addressLine1": "101 W MANCHESTER AVE",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "LOS ANGELES",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90003-3323",
          "telephone": "(323) 758-2740",
          "restauranthours": {
            "hoursMonday": "05:00 - 23:00",
            "hoursTuesday": "05:00 - 23:00",
            "hoursWednesday": "05:00 - 23:00",
            "hoursThursday": "05:00 - 23:00",
            "hoursFriday": "05:00 - 23:00",
            "hoursSaturday": "05:00 - 23:00",
            "hoursSunday": "05:00 - 23:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "04:00 - 04:00",
            "driveHoursTuesday": "04:00 - 04:00",
            "driveHoursWednesday": "04:00 - 04:00",
            "driveHoursThursday": "04:00 - 04:00",
            "driveHoursFriday": "04:00 - 04:00",
            "driveHoursSaturday": "04:00 - 04:00",
            "driveHoursSunday": "04:00 - 04:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "41904"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "11826"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500226019"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/mcdonalds/Hire/Recruiting/Application/Index?businessUnitId\u003d33899871-E217-4673-B2D5-A6A6001A000E",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "11826"
        }
      }, {
        "geometry": {
          "coordinates": [-118.238625, 33.92564]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "04:00 - 04:00",
          "driveTodayHours": "04:00 - 04:00",
          "id": "195500224558-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS", "INDOORDININGAVAILABLE"],
          "addressLine1": "11800 WILMINGTON AVE",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "LOS ANGELES",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90059-3016",
          "telephone": "(323) 564-3448",
          "restauranthours": {
            "hoursMonday": "04:00 - 04:00",
            "hoursTuesday": "04:00 - 04:00",
            "hoursWednesday": "04:00 - 04:00",
            "hoursThursday": "04:00 - 04:00",
            "hoursFriday": "04:00 - 04:00",
            "hoursSaturday": "04:00 - 04:00",
            "hoursSunday": "04:00 - 04:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "04:00 - 04:00",
            "driveHoursTuesday": "04:00 - 04:00",
            "driveHoursWednesday": "04:00 - 04:00",
            "driveHoursThursday": "04:00 - 04:00",
            "driveHoursFriday": "04:00 - 04:00",
            "driveHoursSaturday": "04:00 - 04:00",
            "driveHoursSunday": "04:00 - 04:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "41430"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "10295"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500224558"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/USMCD1000889150/Hire/Recruiting/Application/Index?businessUnitId\u003d87FC0EF5-A784-45F7-ABB8-A6AA00F2F711",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "10295"
        }
      }, {
        "geometry": {
          "coordinates": [-118.231201, 33.975014]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "05:00 - 01:00",
          "driveTodayHours": "04:00 - 04:00",
          "id": "195500219622-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS", "INDOORDININGAVAILABLE", "CREATEYOURTASTEAVAILABLE"],
          "addressLine1": "2303 E FLORENCE AVE",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "HUNTINGTON PARK",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90255-5620",
          "telephone": "(323) 587-2449",
          "restauranthours": {
            "hoursMonday": "05:00 - 01:00",
            "hoursTuesday": "05:00 - 01:00",
            "hoursWednesday": "05:00 - 01:00",
            "hoursThursday": "05:00 - 01:00",
            "hoursFriday": "05:00 - 04:00",
            "hoursSaturday": "05:00 - 04:00",
            "hoursSunday": "05:00 - 01:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "04:00 - 04:00",
            "driveHoursTuesday": "04:00 - 04:00",
            "driveHoursWednesday": "04:00 - 04:00",
            "driveHoursThursday": "04:00 - 04:00",
            "driveHoursFriday": "04:00 - 04:00",
            "driveHoursSaturday": "04:00 - 04:00",
            "driveHoursSunday": "04:00 - 04:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "40142"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "930"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500219622"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/mcdonalds/Hire/Recruiting/Application/Index?businessUnitId\u003dA719B09F-F72A-4969-8B4E-A6A60012FDE3",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "930"
        }
      }, {
        "geometry": {
          "coordinates": [-118.211472, 33.945129]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "05:00 - 00:00",
          "driveTodayHours": "05:00 - 00:00",
          "id": "195500219332-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS", "INDOORPLAYGROUND", "INDOORDININGAVAILABLE", "INDOORPLAYPLACEAVAILABLE"],
          "addressLine1": "3309 TWEEDY BLVD",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "SOUTH GATE",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90280",
          "telephone": "(323) 567-1315",
          "restauranthours": {
            "hoursMonday": "05:00 - 00:00",
            "hoursTuesday": "05:00 - 00:00",
            "hoursWednesday": "05:00 - 00:00",
            "hoursThursday": "05:00 - 00:00",
            "hoursFriday": "05:00 - 00:00",
            "hoursSaturday": "05:00 - 00:00",
            "hoursSunday": "05:00 - 00:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "05:00 - 00:00",
            "driveHoursTuesday": "05:00 - 00:00",
            "driveHoursWednesday": "05:00 - 00:00",
            "driveHoursThursday": "05:00 - 00:00",
            "driveHoursFriday": "05:00 - 01:00",
            "driveHoursSaturday": "05:00 - 01:00",
            "driveHoursSunday": "05:00 - 00:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "40097"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "801"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500219332"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/USMCD1000888041/Hire/Recruiting/Application/Index?businessUnitId\u003d5CBDCFFB-5613-4999-9E6C-A6A60026779F",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "801"
        }
      }, {
        "geometry": {
          "coordinates": [-118.282951, 33.931297]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "05:00 - 21:00",
          "driveTodayHours": "04:30 - 02:00",
          "id": "195500224893-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS", "INDOORDININGAVAILABLE", "CREATEYOURTASTEAVAILABLE"],
          "addressLine1": "501 W IMPERIAL HWY",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "LOS ANGELES",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90044-4222",
          "telephone": "(323) 756-9006",
          "restauranthours": {
            "hoursMonday": "05:00 - 21:00",
            "hoursTuesday": "05:00 - 21:00",
            "hoursWednesday": "05:00 - 21:00",
            "hoursThursday": "05:00 - 21:00",
            "hoursFriday": "05:00 - 21:00",
            "hoursSaturday": "05:00 - 21:00",
            "hoursSunday": "05:00 - 21:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "04:30 - 02:00",
            "driveHoursTuesday": "04:30 - 02:00",
            "driveHoursWednesday": "04:30 - 02:00",
            "driveHoursThursday": "04:30 - 02:00",
            "driveHoursFriday": "04:30 - 02:00",
            "driveHoursSaturday": "04:30 - 02:00",
            "driveHoursSunday": "04:30 - 02:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "41532"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "10805"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500224893"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/USMCD1000889174/Hire/Recruiting/Application/Index?businessUnitId\u003d9E1EC44A-F990-48D3-A231-A6AC010970B1",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "10805"
        }
      }, {
        "geometry": {
          "coordinates": [-118.216632, 33.973601]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "05:00 - 23:00",
          "driveTodayHours": "04:00 - 04:00",
          "id": "195500510965-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS", "CREATEYOURTASTEAVAILABLE"],
          "addressLine1": "2931 E FLORENCE",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "HUNTINGTON PARK",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90255",
          "telephone": "(323) 585-7873",
          "restauranthours": {
            "hoursMonday": "05:00 - 23:00",
            "hoursTuesday": "05:00 - 23:00",
            "hoursWednesday": "05:00 - 23:00",
            "hoursThursday": "05:00 - 23:00",
            "hoursFriday": "05:00 - 23:00",
            "hoursSaturday": "05:00 - 23:00",
            "hoursSunday": "05:00 - 23:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "04:00 - 04:00",
            "driveHoursTuesday": "04:00 - 04:00",
            "driveHoursWednesday": "04:00 - 04:00",
            "driveHoursThursday": "04:00 - 04:00",
            "driveHoursFriday": "04:00 - 04:00",
            "driveHoursSaturday": "04:00 - 04:00",
            "driveHoursSunday": "04:00 - 04:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "44279"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "31665"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500510965"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/mcdonalds/Hire/Recruiting/Application/Index?businessUnitId\u003d131C643A-A0A9-4C42-9B6D-A6A600212658",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "31665"
        }
      }, {
        "geometry": {
          "coordinates": [-118.283102, 33.974981]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "06:00 - 21:00",
          "driveTodayHours": "04:00 - 04:00",
          "id": "195500346038-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS", "INDOORDININGAVAILABLE", "CREATEYOURTASTEAVAILABLE"],
          "addressLine1": "505 W FLORENCE AVE",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "LOS ANGELES",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90044",
          "telephone": "(323) 750-3693",
          "restauranthours": {
            "hoursMonday": "06:00 - 21:00",
            "hoursTuesday": "06:00 - 21:00",
            "hoursWednesday": "06:00 - 21:00",
            "hoursThursday": "06:00 - 21:00",
            "hoursFriday": "06:00 - 21:00",
            "hoursSaturday": "06:00 - 21:00",
            "hoursSunday": "06:00 - 21:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "04:00 - 04:00",
            "driveHoursTuesday": "04:00 - 04:00",
            "driveHoursWednesday": "04:00 - 04:00",
            "driveHoursThursday": "04:00 - 04:00",
            "driveHoursFriday": "04:00 - 04:00",
            "driveHoursSaturday": "04:00 - 04:00",
            "driveHoursSunday": "04:00 - 04:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "43925"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "27507"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500346038"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/USMCD1000889238/Hire/Recruiting/Application/Index?businessUnitId\u003d27DA7BB8-25D4-4590-9E2C-A6B8012951DD",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "27507"
        }
      }, {
        "geometry": {
          "coordinates": [-118.255989, 33.989006]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "05:00 - 21:00",
          "driveTodayHours": "04:00 - 04:00",
          "id": "195500226248-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS", "INDOORPLAYGROUND", "INDOORDININGAVAILABLE", "INDOORPLAYPLACEAVAILABLE"],
          "addressLine1": "1118 SLAUSON AVENUE",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "LOS ANGELES",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90011-4800",
          "telephone": "(323) 231-2740",
          "restauranthours": {
            "hoursMonday": "05:00 - 21:00",
            "hoursTuesday": "05:00 - 21:00",
            "hoursWednesday": "05:00 - 21:00",
            "hoursThursday": "05:00 - 21:00",
            "hoursFriday": "05:00 - 22:00",
            "hoursSaturday": "05:00 - 22:00",
            "hoursSunday": "05:00 - 21:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "04:00 - 04:00",
            "driveHoursTuesday": "04:00 - 04:00",
            "driveHoursWednesday": "04:00 - 04:00",
            "driveHoursThursday": "04:00 - 04:00",
            "driveHoursFriday": "04:00 - 04:00",
            "driveHoursSaturday": "04:00 - 04:00",
            "driveHoursSunday": "04:00 - 04:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "42011"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "12191"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500226248"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/USMCD1000889131/Hire/Recruiting/Application/Index?businessUnitId\u003d0BEB2231-2596-45FE-ABB4-A6A6002772DC",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "12191"
        }
      }, {
        "geometry": {
          "coordinates": [-118.210922, 33.930168]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "05:00 - 23:00",
          "driveTodayHours": "04:00 - 04:00",
          "id": "195500225395-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS", "INDOORPLAYGROUND", "INDOORDININGAVAILABLE", "CREATEYOURTASTEAVAILABLE", "INDOORPLAYPLACEAVAILABLE"],
          "addressLine1": "11170 LONG BEACH BLVD",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "LYNWOOD",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90262",
          "telephone": "(310) 631-4311",
          "restauranthours": {
            "hoursMonday": "05:00 - 23:00",
            "hoursTuesday": "05:00 - 23:00",
            "hoursWednesday": "05:00 - 23:00",
            "hoursThursday": "05:00 - 23:00",
            "hoursFriday": "05:00 - 23:00",
            "hoursSaturday": "05:00 - 23:00",
            "hoursSunday": "05:00 - 23:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "04:00 - 04:00",
            "driveHoursTuesday": "04:00 - 04:00",
            "driveHoursWednesday": "04:00 - 04:00",
            "driveHoursThursday": "04:00 - 04:00",
            "driveHoursFriday": "04:00 - 04:00",
            "driveHoursSaturday": "04:00 - 04:00",
            "driveHoursSunday": "04:00 - 04:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "41662"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "11032"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500225395"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/USMCD1000889131/Hire/Recruiting/Application/Index?businessUnitId\u003d2FC47ED9-4644-47D4-B5EB-A6A600277124",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "11032"
        }
      }, {
        "geometry": {
          "coordinates": [-118.225685, 33.989025]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "04:00 - 22:00",
          "driveTodayHours": "04:00 - 00:00",
          "id": "195500223797-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS", "INDOORDININGAVAILABLE", "CREATEYOURTASTEAVAILABLE"],
          "addressLine1": "2584 SLAUSON AVE",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "HUNTINGTON PARK",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90255-2837",
          "telephone": "(323) 581-4267",
          "restauranthours": {
            "hoursMonday": "04:00 - 22:00",
            "hoursTuesday": "04:00 - 22:00",
            "hoursWednesday": "04:00 - 22:00",
            "hoursThursday": "04:00 - 22:00",
            "hoursFriday": "04:00 - 22:00",
            "hoursSaturday": "04:00 - 22:00",
            "hoursSunday": "04:00 - 22:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "04:00 - 00:00",
            "driveHoursTuesday": "04:00 - 00:00",
            "driveHoursWednesday": "04:00 - 00:00",
            "driveHoursThursday": "04:00 - 00:00",
            "driveHoursFriday": "04:00 - 00:00",
            "driveHoursSaturday": "04:00 - 00:00",
            "driveHoursSunday": "04:00 - 00:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "41253"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "7657"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500223797"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/USMCD1000889229/Hire/Recruiting/Application/Index?businessUnitId\u003d10F6F5C9-0825-4F0E-A484-A6AB00F86B6B",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "7657"
        }
      }, {
        "geometry": {
          "coordinates": [-118.300648, 33.959759]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "05:00 - 22:00",
          "driveTodayHours": "04:00 - 04:00",
          "id": "195500219486-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS", "INDOORPLAYGROUND", "INDOORDININGAVAILABLE", "CREATEYOURTASTEAVAILABLE", "INDOORPLAYPLACEAVAILABLE"],
          "addressLine1": "1406 W MANCHESTER AVE.",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "LOS ANGELES",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90047-5422",
          "telephone": "(323) 752-9363",
          "restauranthours": {
            "hoursMonday": "05:00 - 22:00",
            "hoursTuesday": "05:00 - 22:00",
            "hoursWednesday": "05:00 - 22:00",
            "hoursThursday": "05:00 - 22:00",
            "hoursFriday": "05:00 - 22:00",
            "hoursSaturday": "05:00 - 22:00",
            "hoursSunday": "05:00 - 22:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "04:00 - 04:00",
            "driveHoursTuesday": "04:00 - 04:00",
            "driveHoursWednesday": "04:00 - 04:00",
            "driveHoursThursday": "04:00 - 04:00",
            "driveHoursFriday": "04:00 - 04:00",
            "driveHoursSaturday": "04:00 - 04:00",
            "driveHoursSunday": "04:00 - 04:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "40119"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "905"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500219486"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/USMCD1000892378/Hire/Recruiting/Application/Index?businessUnitId\u003d726DF599-E51E-472E-A9DB-A6AA00F217BA",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "905"
        }
      }, {
        "geometry": {
          "coordinates": [-118.197145, 33.953527]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "05:00 - 00:00",
          "driveTodayHours": "05:00 - 00:00",
          "id": "195500343341-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS"],
          "addressLine1": "4135 E FIRESTONE BOULEVARD",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "SOUTH GATE",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90280",
          "telephone": "(323) 564-8385",
          "restauranthours": {
            "hoursMonday": "05:00 - 00:00",
            "hoursTuesday": "05:00 - 00:00",
            "hoursWednesday": "05:00 - 00:00",
            "hoursThursday": "05:00 - 00:00",
            "hoursFriday": "05:00 - 00:00",
            "hoursSaturday": "05:00 - 00:00",
            "hoursSunday": "05:00 - 00:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "05:00 - 00:00",
            "driveHoursTuesday": "05:00 - 00:00",
            "driveHoursWednesday": "05:00 - 00:00",
            "driveHoursThursday": "05:00 - 00:00",
            "driveHoursFriday": "05:00 - 01:00",
            "driveHoursSaturday": "05:00 - 01:00",
            "driveHoursSunday": "05:00 - 00:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "44061"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "28892"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500343341"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/USMCD1000888041/Hire/Recruiting/Application/Index?businessUnitId\u003dA0B90697-F9C5-4BDC-B1ED-A6A600267F8E",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "28892"
        }
      }, {
        "geometry": {
          "coordinates": [-118.202391, 33.979374]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "05:00 - 22:00",
          "driveTodayHours": "05:00 - 23:00",
          "id": "195500227955-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS", "INDOORDININGAVAILABLE", "CREATEYOURTASTEAVAILABLE"],
          "addressLine1": "3526 E GAGE AVE",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "CITY OF BELL",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90201",
          "telephone": "(323) 277-9673",
          "restauranthours": {
            "hoursMonday": "05:00 - 22:00",
            "hoursTuesday": "05:00 - 22:00",
            "hoursWednesday": "05:00 - 22:00",
            "hoursThursday": "05:00 - 22:00",
            "hoursFriday": "05:00 - 23:00",
            "hoursSaturday": "05:00 - 23:00",
            "hoursSunday": "05:00 - 22:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "05:00 - 23:00",
            "driveHoursTuesday": "05:00 - 23:00",
            "driveHoursWednesday": "05:00 - 23:00",
            "driveHoursThursday": "05:00 - 23:00",
            "driveHoursFriday": "05:00 - 00:00",
            "driveHoursSaturday": "05:00 - 00:00",
            "driveHoursSunday": "05:00 - 23:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "42562"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "15995"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500227955"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/USMCD1000889278/Hire/Recruiting/Application/Index?businessUnitId\u003d9BBBDFD3-641A-45BC-9542-A6A60027F6BF",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "15995"
        }
      }, {
        "geometry": {
          "coordinates": [-118.254015, 33.902501]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "05:00 - 21:00",
          "driveTodayHours": "05:00 - 01:00",
          "id": "195500476315-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS", "INDOORPLAYGROUND", "INDOORDININGAVAILABLE", "INDOORPLAYPLACEAVAILABLE"],
          "addressLine1": "1160 E ROSECRANS AVENUE",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "LOS ANGELES",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90059-3645",
          "telephone": "(310) 604-0502",
          "restauranthours": {
            "hoursMonday": "05:00 - 21:00",
            "hoursTuesday": "05:00 - 21:00",
            "hoursWednesday": "05:00 - 21:00",
            "hoursThursday": "05:00 - 21:00",
            "hoursFriday": "05:00 - 21:00",
            "hoursSaturday": "05:00 - 21:00",
            "hoursSunday": "05:00 - 21:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "05:00 - 01:00",
            "driveHoursTuesday": "05:00 - 01:00",
            "driveHoursWednesday": "05:00 - 01:00",
            "driveHoursThursday": "05:00 - 01:00",
            "driveHoursFriday": "05:00 - 02:00",
            "driveHoursSaturday": "05:00 - 02:00",
            "driveHoursSunday": "05:00 - 01:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "44081"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "29411"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500476315"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/USMCD1000889131/Hire/Recruiting/Application/Index?businessUnitId\u003dF773DDA7-07DD-4ED9-B4C1-A6A6002778FB",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "29411"
        }
      }, {
        "geometry": {
          "coordinates": [-118.308594, 33.945728]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "06:00 - 21:00",
          "driveTodayHours": "05:00 - 01:00",
          "id": "195500225401-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS", "INDOORDININGAVAILABLE", "CREATEYOURTASTEAVAILABLE"],
          "addressLine1": "1763 W CENTURY BLVD",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "LOS ANGELES",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90050",
          "telephone": "(323) 754-7738",
          "restauranthours": {
            "hoursMonday": "06:00 - 21:00",
            "hoursTuesday": "06:00 - 21:00",
            "hoursWednesday": "06:00 - 21:00",
            "hoursThursday": "06:00 - 21:00",
            "hoursFriday": "06:00 - 21:00",
            "hoursSaturday": "06:00 - 21:00",
            "hoursSunday": "06:00 - 21:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "06:00 - 01:00",
            "driveHoursTuesday": "05:00 - 01:00",
            "driveHoursWednesday": "05:00 - 01:00",
            "driveHoursThursday": "05:00 - 01:00",
            "driveHoursFriday": "05:00 - 02:00",
            "driveHoursSaturday": "05:00 - 02:00",
            "driveHoursSunday": "05:00 - 21:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "41663"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "11751"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500225401"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/USMCD1000891765/Hire/Recruiting/Application/Index?businessUnitId\u003d578FBDBE-93EC-4B90-825C-A6AB00FA8C2F",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "11751"
        }
      }, {
        "geometry": {
          "coordinates": [-118.291023, 33.988823]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "06:00 - 22:00",
          "driveTodayHours": "05:00 - 23:00",
          "id": "195500223100-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS", "INDOORDININGAVAILABLE", "CREATEYOURTASTEAVAILABLE"],
          "addressLine1": "988 W SLAUSON ST",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "LOS ANGELES",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90043",
          "telephone": "(323) 778-3526",
          "restauranthours": {
            "hoursMonday": "06:00 - 22:00",
            "hoursTuesday": "06:00 - 22:00",
            "hoursWednesday": "06:00 - 22:00",
            "hoursThursday": "06:00 - 22:00",
            "hoursFriday": "06:00 - 22:00",
            "hoursSaturday": "06:00 - 22:00",
            "hoursSunday": "06:00 - 22:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "05:00 - 23:00",
            "driveHoursTuesday": "05:00 - 23:00",
            "driveHoursWednesday": "05:00 - 23:00",
            "driveHoursThursday": "05:00 - 23:00",
            "driveHoursFriday": "05:00 - 23:00",
            "driveHoursSaturday": "05:00 - 23:00",
            "driveHoursSunday": "05:00 - 23:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "41098"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "5940"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500223100"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/USMCD1000889238/Hire/Recruiting/Application/Index?businessUnitId\u003dAAA31F7B-DB98-45D5-932E-A6B801295043",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "5940"
        }
      }, {
        "geometry": {
          "coordinates": [-118.186255, 33.961299]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "05:00 - 22:00",
          "driveTodayHours": "05:00 - 23:00",
          "id": "195500226309-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS", "INDOORDININGAVAILABLE", "CREATEYOURTASTEAVAILABLE"],
          "addressLine1": "8029 ATLANTIC AVE",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "CUDAHY",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90201",
          "telephone": "(323) 560-1660",
          "restauranthours": {
            "hoursMonday": "05:00 - 22:00",
            "hoursTuesday": "05:00 - 22:00",
            "hoursWednesday": "05:00 - 22:00",
            "hoursThursday": "05:00 - 22:00",
            "hoursFriday": "05:00 - 22:00",
            "hoursSaturday": "05:00 - 22:00",
            "hoursSunday": "05:00 - 22:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "05:00 - 23:00",
            "driveHoursTuesday": "05:00 - 23:00",
            "driveHoursWednesday": "05:00 - 23:00",
            "driveHoursThursday": "05:00 - 23:00",
            "driveHoursFriday": "05:00 - 23:00",
            "driveHoursSaturday": "05:00 - 23:00",
            "driveHoursSunday": "05:00 - 23:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "42055"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "14287"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500226309"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/USMCD1000889119/Hire/Recruiting/Application/Index?businessUnitId\u003d5001E4D9-F82E-4532-BA1C-A6AE00F7DA36",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "14287"
        }
      }, {
        "geometry": {
          "coordinates": [-118.208983, 33.903895]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "05:00 - 22:00",
          "driveTodayHours": "04:00 - 04:00",
          "id": "195500337579-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS", "INDOORPLAYGROUND", "INDOORDININGAVAILABLE", "OUTDOORPLAYPLACEAVAILABLE", "CREATEYOURTASTEAVAILABLE", "INDOORPLAYPLACEAVAILABLE"],
          "addressLine1": "1105 LONG BEACH BLVD",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "COMPTON",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90221",
          "telephone": "(310) 638-9417",
          "restauranthours": {
            "hoursMonday": "05:00 - 22:00",
            "hoursTuesday": "05:00 - 22:00",
            "hoursWednesday": "05:00 - 22:00",
            "hoursThursday": "05:00 - 22:00",
            "hoursFriday": "05:00 - 23:00",
            "hoursSaturday": "05:00 - 23:00",
            "hoursSunday": "05:00 - 22:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "04:00 - 04:00",
            "driveHoursTuesday": "04:00 - 04:00",
            "driveHoursWednesday": "04:00 - 04:00",
            "driveHoursThursday": "04:00 - 04:00",
            "driveHoursFriday": "04:00 - 04:00",
            "driveHoursSaturday": "04:00 - 04:00",
            "driveHoursSunday": "04:00 - 04:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "44024"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "28366"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500337579"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/USMCD1000889131/Hire/Recruiting/Application/Index?businessUnitId\u003dE5010B95-2305-4496-BEE7-A6A6002777DB",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "28366"
        }
      }, {
        "geometry": {
          "coordinates": [-118.183135, 33.932606]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "05:00 - 23:00",
          "driveTodayHours": "04:00 - 01:00",
          "id": "195500229065-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS", "INDOORDININGAVAILABLE"],
          "addressLine1": "ATLANTIC \u0026 ABBOTT",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "LYNWOOD",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90262",
          "telephone": "(310) 632-5255",
          "restauranthours": {
            "hoursMonday": "05:00 - 23:00",
            "hoursTuesday": "05:00 - 23:00",
            "hoursWednesday": "05:00 - 23:00",
            "hoursThursday": "05:00 - 23:00",
            "hoursFriday": "05:00 - 23:00",
            "hoursSaturday": "05:00 - 23:00",
            "hoursSunday": "05:00 - 23:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "04:00 - 01:00",
            "driveHoursTuesday": "04:00 - 01:00",
            "driveHoursWednesday": "04:00 - 01:00",
            "driveHoursThursday": "04:00 - 01:00",
            "driveHoursFriday": "04:00 - 03:00",
            "driveHoursSaturday": "04:00 - 03:00",
            "driveHoursSunday": "04:00 - 01:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "43011"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "19964"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500229065"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/USMCD1000889131/Hire/Recruiting/Application/Index?businessUnitId\u003d3FF373CE-E32C-4779-A26A-A6A60027739C",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "19964"
        }
      }, {
        "geometry": {
          "coordinates": [-118.225038, 33.896434]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "05:00 - 21:00",
          "driveTodayHours": "05:00 - 00:00",
          "id": "195500229416-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS", "INDOORDININGAVAILABLE"],
          "addressLine1": "101 S COMPTON",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "COMPTON",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90220",
          "telephone": "(310) 635-0688",
          "restauranthours": {
            "hoursMonday": "05:00 - 21:00",
            "hoursTuesday": "05:00 - 21:00",
            "hoursWednesday": "05:00 - 21:00",
            "hoursThursday": "05:00 - 21:00",
            "hoursFriday": "05:00 - 21:00",
            "hoursSaturday": "05:00 - 21:00",
            "hoursSunday": "05:30 - 21:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "05:00 - 00:00",
            "driveHoursTuesday": "05:00 - 00:00",
            "driveHoursWednesday": "05:00 - 00:00",
            "driveHoursThursday": "05:00 - 00:00",
            "driveHoursFriday": "05:00 - 00:00",
            "driveHoursSaturday": "05:00 - 00:00",
            "driveHoursSunday": "05:30 - 00:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "43282"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "22578"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500229416"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/USMCD1000889131/Hire/Recruiting/Application/Index?businessUnitId\u003dA802DE2B-EE8D-449E-940E-A6A600277585",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "22578"
        }
      }, {
        "geometry": {
          "coordinates": [-118.308289, 33.916763]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "05:00 - 22:00",
          "driveTodayHours": "05:00 - 00:00",
          "id": "195500218915-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS", "INDOORDININGAVAILABLE", "CREATEYOURTASTEAVAILABLE"],
          "addressLine1": "1747 WEST EL SEGUNDO BLVD",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "GARDENA",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90249-2011",
          "telephone": "(323) 779-7322",
          "restauranthours": {
            "hoursMonday": "05:00 - 22:00",
            "hoursTuesday": "05:00 - 22:00",
            "hoursWednesday": "05:00 - 22:00",
            "hoursThursday": "05:00 - 22:00",
            "hoursFriday": "05:00 - 22:00",
            "hoursSaturday": "05:00 - 22:00",
            "hoursSunday": "05:00 - 22:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "05:00 - 00:00",
            "driveHoursTuesday": "05:00 - 00:00",
            "driveHoursWednesday": "05:00 - 00:00",
            "driveHoursThursday": "05:00 - 00:00",
            "driveHoursFriday": "05:00 - 01:00",
            "driveHoursSaturday": "05:00 - 01:00",
            "driveHoursSunday": "05:00 - 00:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "40030"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "572"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500218915"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/USMCD1000889119/Hire/Recruiting/Application/Index?businessUnitId\u003dEE508A9D-FAC0-436B-AA34-A6AE00F7D5CA",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "572"
        }
      }, {
        "geometry": {
          "coordinates": [-118.256904, 34.010548]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "05:00 - 23:00",
          "driveTodayHours": "04:30 - 00:00",
          "id": "195500226545-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS", "INDOORDININGAVAILABLE"],
          "addressLine1": "4011 S CENTRAL",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "LOS ANGELES",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90059",
          "telephone": "(323) 233-3548",
          "restauranthours": {
            "hoursMonday": "05:00 - 23:00",
            "hoursTuesday": "05:00 - 23:00",
            "hoursWednesday": "05:00 - 23:00",
            "hoursThursday": "05:00 - 23:00",
            "hoursFriday": "05:00 - 23:00",
            "hoursSaturday": "05:00 - 23:00",
            "hoursSunday": "05:00 - 23:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "04:30 - 00:00",
            "driveHoursTuesday": "04:30 - 00:00",
            "driveHoursWednesday": "04:30 - 00:00",
            "driveHoursThursday": "04:30 - 00:00",
            "driveHoursFriday": "04:30 - 02:00",
            "driveHoursSaturday": "04:30 - 02:00",
            "driveHoursSunday": "04:30 - 00:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "42199"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "13788"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500226545"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/USMCD1000889174/Hire/Recruiting/Application/Index?businessUnitId\u003dB01A0140-D276-4BB4-BEF7-A6AC01097288",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "13788"
        }
      }, {
        "geometry": {
          "coordinates": [-118.290919, 33.901595]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "06:00 - 21:00",
          "driveTodayHours": "05:00 - 00:00",
          "id": "195500228884-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS", "INDOORDININGAVAILABLE"],
          "addressLine1": "850 W ROSECRANS AVE",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "GARDENA",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90247",
          "telephone": "(310) 538-3279",
          "restauranthours": {
            "hoursMonday": "06:00 - 21:00",
            "hoursTuesday": "06:00 - 21:00",
            "hoursWednesday": "06:00 - 21:00",
            "hoursThursday": "06:00 - 21:00",
            "hoursFriday": "06:00 - 21:00",
            "hoursSaturday": "06:00 - 21:00",
            "hoursSunday": "06:00 - 21:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "05:00 - 00:00",
            "driveHoursTuesday": "05:00 - 00:00",
            "driveHoursWednesday": "05:00 - 00:00",
            "driveHoursThursday": "05:00 - 00:00",
            "driveHoursFriday": "05:00 - 00:00",
            "driveHoursSaturday": "05:00 - 00:00",
            "driveHoursSunday": "05:00 - 00:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "42916"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "19140"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500228884"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/USMCD1000892353/Hire/Recruiting/Application/Index?businessUnitId\u003dAB281DBD-1732-4E6D-B393-A6AA00F5A82C",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "19140"
        }
      }, {
        "geometry": {
          "coordinates": [-118.220238, 34.008423]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "04:00 - 04:00",
          "driveTodayHours": "04:00 - 04:00",
          "id": "195500225975-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS", "INDOORDININGAVAILABLE", "CREATEYOURTASTEAVAILABLE"],
          "addressLine1": "3737 SOTO STREET",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "VERNON",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90058",
          "telephone": "(323) 585-2251",
          "restauranthours": {
            "hoursMonday": "04:00 - 04:00",
            "hoursTuesday": "04:00 - 04:00",
            "hoursWednesday": "04:00 - 04:00",
            "hoursThursday": "04:00 - 04:00",
            "hoursFriday": "04:00 - 04:00",
            "hoursSaturday": "04:00 - 23:00",
            "hoursSunday": "04:00 - 23:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "04:00 - 04:00",
            "driveHoursTuesday": "04:00 - 04:00",
            "driveHoursWednesday": "04:00 - 04:00",
            "driveHoursThursday": "04:00 - 04:00",
            "driveHoursFriday": "04:00 - 04:00",
            "driveHoursSaturday": "04:00 - 23:00",
            "driveHoursSunday": "04:00 - 23:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "41881"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "11839"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500225975"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/USMCD1000889159/Hire/Recruiting/Application/Index?businessUnitId\u003d9EC2B183-4881-490A-8961-A6A701237327",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "11839"
        }
      }, {
        "geometry": {
          "coordinates": [-118.185936, 33.98679]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "05:00 - 23:00",
          "driveTodayHours": "04:00 - 04:00",
          "id": "195500223896-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS", "INDOORDININGAVAILABLE"],
          "addressLine1": "5901 ATLANTIC AVE",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "MAYWOOD",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90270-3116",
          "telephone": "(323) 560-2300",
          "restauranthours": {
            "hoursMonday": "05:00 - 23:00",
            "hoursTuesday": "05:00 - 23:00",
            "hoursWednesday": "05:00 - 23:00",
            "hoursThursday": "05:00 - 23:00",
            "hoursFriday": "05:00 - 23:00",
            "hoursSaturday": "05:00 - 23:00",
            "hoursSunday": "05:00 - 23:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "04:00 - 04:00",
            "driveHoursTuesday": "04:00 - 04:00",
            "driveHoursWednesday": "04:00 - 04:00",
            "driveHoursThursday": "04:00 - 04:00",
            "driveHoursFriday": "04:00 - 04:00",
            "driveHoursSaturday": "04:00 - 04:00",
            "driveHoursSunday": "04:00 - 04:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "41280"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "7528"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500223896"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/USMCD1000889113/Hire/Recruiting/Application/Index?businessUnitId\u003d11140900-1996-4A0B-982D-A6AE00F7D070",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "7528"
        }
      }, {
        "geometry": {
          "coordinates": [-118.311896, 33.988877]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "05:00 - 22:00",
          "driveTodayHours": "05:00 - 04:00",
          "id": "195500333045-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "MOBILEOFFERS", "MOBILEORDERS", "INDOORPLAYGROUND", "INDOORDININGAVAILABLE", "INDOORPLAYPLACEAVAILABLE"],
          "addressLine1": "1900 W SLAUSON AVE",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "LOS ANGELES",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90062",
          "telephone": "(323) 294-5990",
          "restauranthours": {
            "hoursMonday": "05:00 - 22:00",
            "hoursTuesday": "05:00 - 22:00",
            "hoursWednesday": "05:00 - 22:00",
            "hoursThursday": "05:00 - 22:00",
            "hoursFriday": "05:00 - 22:00",
            "hoursSaturday": "05:00 - 22:00",
            "hoursSunday": "05:00 - 22:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "05:00 - 04:00",
            "driveHoursTuesday": "05:00 - 04:00",
            "driveHoursWednesday": "05:00 - 04:00",
            "driveHoursThursday": "05:00 - 04:00",
            "driveHoursFriday": "05:00 - 04:00",
            "driveHoursSaturday": "04:00 - 04:00",
            "driveHoursSunday": "04:00 - 04:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "43557"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "24695"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500333045"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/USMCD1000889131/Hire/Recruiting/Application/Index?businessUnitId\u003d9A10DF62-4312-4362-BCF3-A85801089C26",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "24695"
        }
      }, {
        "geometry": {
          "coordinates": [-118.283485, 33.893238]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "05:00 - 22:00",
          "driveTodayHours": "04:00 - 04:00",
          "id": "195500225371-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS", "INDOORDININGAVAILABLE", "CREATEYOURTASTEAVAILABLE"],
          "addressLine1": "15235 S FIGUEROA AVE",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "GARDENA",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90248",
          "telephone": "(310) 329-5511",
          "restauranthours": {
            "hoursMonday": "05:00 - 22:00",
            "hoursTuesday": "05:00 - 22:00",
            "hoursWednesday": "05:00 - 22:00",
            "hoursThursday": "05:00 - 22:00",
            "hoursFriday": "05:00 - 22:00",
            "hoursSaturday": "05:00 - 22:00",
            "hoursSunday": "05:00 - 22:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "04:00 - 04:00",
            "driveHoursTuesday": "04:00 - 04:00",
            "driveHoursWednesday": "04:00 - 04:00",
            "driveHoursThursday": "04:00 - 04:00",
            "driveHoursFriday": "04:00 - 04:00",
            "driveHoursSaturday": "04:00 - 04:00",
            "driveHoursSunday": "04:00 - 04:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "41653"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "11211"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500225371"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/USMCD1000889131/Hire/Recruiting/Application/Index?businessUnitId\u003dB89F5DEC-A88E-4C15-9395-A6A6002771CC",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "11211"
        }
      }, {
        "geometry": {
          "coordinates": [-118.281881, 34.010781]
        },
        "properties": {
          "jobUrl": "",
          "longDescription": "",
          "todayHours": "04:00 - 00:00",
          "driveTodayHours": "04:00 - 04:00",
          "id": "195500229393-en-us",
          "filterType": ["DRIVETHRU", "WIFI", "GIFTCARDS", "MOBILEOFFERS", "MOBILEORDERS", "INDOORDININGAVAILABLE", "CREATEYOURTASTEAVAILABLE"],
          "addressLine1": "4000 S FIGUEROA",
          "addressLine2": "SOUTHERN CALIFORNIA REGION",
          "addressLine3": "LOS ANGELES",
          "addressLine4": "USA",
          "subDivision": "CA",
          "postcode": "90037",
          "telephone": "(323) 232-6768",
          "restauranthours": {
            "hoursMonday": "04:00 - 00:00",
            "hoursTuesday": "04:00 - 00:00",
            "hoursWednesday": "04:00 - 00:00",
            "hoursThursday": "04:00 - 00:00",
            "hoursFriday": "04:00 - 00:00",
            "hoursSaturday": "04:00 - 00:00",
            "hoursSunday": "04:00 - 00:00"
          },
          "drivethruhours": {
            "driveHoursMonday": "04:00 - 04:00",
            "driveHoursTuesday": "04:00 - 04:00",
            "driveHoursWednesday": "04:00 - 04:00",
            "driveHoursThursday": "04:00 - 04:00",
            "driveHoursFriday": "04:00 - 04:00",
            "driveHoursSaturday": "04:00 - 04:00",
            "driveHoursSunday": "04:00 - 04:00"
          },
          "familyevent": [],
          "identifiers": {
            "storeIdentifier": [{
              "identifierType": "SiteIdNumber",
              "identifierValue": "43279"
            }, {
              "identifierType": "NatlStrNumber",
              "identifierValue": "22570"
            }, {
              "identifierType": "Region ID",
              "identifierValue": "55"
            }, {
              "identifierType": "Co-Op",
              "identifierValue": "LOS ANGELES SAN DIEGO BAKERSFIELD"
            }, {
              "identifierType": "Co-Op ID",
              "identifierValue": "206"
            }, {
              "identifierType": "TV-Market",
              "identifierValue": "LOS ANGELES, CA."
            }, {
              "identifierType": "TV-Market ID",
              "identifierValue": "15080"
            }],
            "gblnumber": "195500229393"
          },
          "birthDaysParties": "0",
          "driveThru": "0",
          "outDoorPlayGround": "0",
          "indoorPlayGround": "0",
          "wifi": "0",
          "breakFast": "0",
          "nightMenu": "0",
          "giftCards": "0",
          "mobileOffers": "0",
          "restaurantUrl": "https://my.peoplematter.com/USMCD1000889174/Hire/Recruiting/Application/Index?businessUnitId\u003d68E7B608-5123-4952-80BB-A6AC010973B0",
          "storeNotice": "",
          "openstatus": "OPEN",
          "identifierValue": "22570"
        }
      }]
    }
    $scope.restaurants = [];
    $scope.selectedRestaurant = {};
    $scope.restaurantRadius = 5;
    $scope.multipleLocations = [];
    let referenceLatitudeForDistance;
    let referenceLongitudeForDistance;
    $scope.distanceUnit;
    var maxResults, grayedOutRestImagePath, markerHeight = 33,
      markerWidth = 46;
    var restaurantsResultCountry, restaurantsResultLanguage, resultMessage, showClosed, hours24Text, Hours24TextForAriaLabel;
    $scope.widget = $('[data-widget="accessible-autocomplete"]');
    $scope.input = $scope.widget.find('#search');
    let restaurantSearchCountry = '';
    $scope.restaurantFilters = [];

    $scope.searchInputFocus = function (focus) {
      $scope.isSearchInputFocus = focus;
      if (focus) {
        angular.element('.mcd-rlresults').css('opacity', '0.05');
        angular.element('.mcd-rlwidget__search-input').css('width', '85%');
        angular.element('.mcd-rlwidget__locate-button').css('display', 'none');
        angular.element('.mcd-rlwidget__search-cancel').css('display', 'inherit');
      }
      else {
        angular.element('.mcd-rlresults').css('opacity', 'inherit');
        angular.element('.mcd-rlwidget__search-input').css('width', '');
        angular.element('.mcd-rlwidget__locate-button').css('display', '');
        angular.element('.mcd-rlwidget__search-cancel').css('display', 'none');
        $scope.searchSuggestion = false;
      }
    };

    $scope.init = function () {
      if ($('#rlJson') && $('#rlJson').attr('data-rl-json')) {
        $scope.fullRlData = JSON.parse($('#rlJson').attr('data-rl-json'));
      }
      restaurantsResultCountry = $scope.fullRlData.geocountry;
      restaurantSearchCountry = $scope.fullRlData.country;
      $scope.distanceUnit = $scope.fullRlData.distanceUnit;
      restaurantsResultCountry = $scope.fullRlData.geocountry;
      maxResults = $scope.fullRlData.maxResults;

      //For initial Testing
      if(window.innerWidth < 600) {
        $('.rl-mapview__map').css('width', '100vw');
      } else {
        $('.rl-mapview__details').addClass('hide');
        $('.rl-mapview__map').css('width', '100%');
      }
      //-------------------
    };
    
    $scope.init();

    initializeDialogConfigurations();

    $scope.infoWindowClick = function(selectedLocation) {
      console.log("yo yo yo");
      $scope.openRestaurantDetailsPopup($scope.selectedRestaurant, selectedLocation, 'open');
    }

    $scope.capitalizeFirstLetter = function(string) {
      let lowercase = string.toLowerCase();
      let temp = string.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
      return temp;
    }


    $scope.showRestaurantsForCurrentLocation = function () {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPosition, showError);
      }
      else {
        console.log('Geolocation is not supported by this browser.');
      }
    };

    $scope.getResultMessage = function() {
      if($scope.restaurants.length > 0) {
        return `There are ${$scope.restaurants.length} McDonald's restaurants near you.`;
      } else {
        return `We have 25,000 restaurants worldwide`;
      }
    }

    function initializeDialogConfigurations() {
      initializeRestaurantFilters();
    }

    function initializeRestaurantFilters() {

      console.log('initializeRestaurantFilters');

      var sortField;
      let resFilters = $scope.fullRlData.filterString;
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
            //$scope.restaurantAmenities.push(filterObject);
            if (filterData[2] == 'true') {
              $scope.restaurantFilters.push(filterObject);
            }
            //$scope.restaurantFilterKey = $scope.restaurantFilterKey + filterData[1] + ",";
          }
        });
        //removeDelimiterAtEnd();
      }
    }

    $scope.matchItems = input => {
      const reg = new RegExp(input.split('').join('\\w*').replace(/\W/, ''), 'i');
      $scope.searchRestaurants(input);
      // const items = ['State Hwy 2401, Middlesboro, KY 40965, USA',
      //   'State Rte 2010, Haymarket, VA 20169, USA',
      //   'State Hwy 2010, Jackson, SC 29831, USA',
      //   'State Rd 3402, Pleasant Garden, NC 27313, USA',
      //   'FM3402, Cuero, TX 77954, USA'];
      const items = $scope.multipleLocations.map(item => item.formattedAddress);
      console.log(items, input);
      return items.filter(item => {
        if (item.match(reg)) {
          return item;
        }
        return '';
      });
    };

    $scope.clearSelected = function () {
      const results = $scope.widget.find('#results');
      $scope.input.attr('aria-activedescendant', '');
      results.find('[aria-selected="true"]').attr('aria-selected', 'false').attr('id', '');
    };

    $scope.changeInput = function (event) {
      if ([13, 38, 40].indexOf(event.keyCode) === -1) {
        if (event.target.value.length > 0) {
          const autoCompleteResult = $scope.matchItems(event.target.value);
          console.log(`Result ${autoCompleteResult}`);
          let predictedData = '';
          if (autoCompleteResult.length > 0) {
            for (let i = 0; i < autoCompleteResult.length;) {
              predictedData += `<li ng-click="showRestaurantForMulti($event,${$scope.multipleLocations[i]}})" class="mcd-rlwidget__suggestion--predicted-text autocomplete-item" role="option" aria-selected="false" tabindex="-1">${autoCompleteResult[i]}</li>`;
              i += 1;
            }
            const [backgroundInputData] = autoCompleteResult;
            $scope.searchInputBackVal = event.target.value + backgroundInputData.substring(event.target.value.length);
          }
          else {
            $scope.displaySearchResults = true;
          }
          document.querySelector('.mcd-rlwidget__suggestion--section').innerHTML = predictedData;
          $scope.searchInputCloseBtn = true;
          $scope.searchSuggestion = true;
        }
        else {
          $scope.searchInputBackVal = '';
          $scope.searchInputCloseBtn = false;
          $scope.searchSuggestion = false;
        }
      }
      else {
        const $listItems = $('.mcd-rlwidget__suggestion--section li');
        const key = event.keyCode;
        const $selected = $listItems.filter('.selected');
        let currentElm;

        $listItems.removeClass('selected');

        if (key === 40) {
          if (!$selected.length || $selected.is(':last-child')) {
            currentElm = $listItems.eq(0);
          }
          else {
            currentElm = $selected.next();
          }
        }
        else if (key === 38) {
          if (!$selected.length || $selected.is(':first-child')) {
            currentElm = $listItems.last();
          }
          else {
            currentElm = $selected.prev();
          }
        }
        if (currentElm && currentElm.length > 0) {
          $scope.clearSelected();
          currentElm.addClass('selected');
          currentElm.attr('aria-selected', 'true').attr('id', 'selectedOption');
          $scope.input.attr('aria-activedescendant', 'selectedOption');
          $scope.input.attr('aria-expanded', 'true');
          $scope.searchInputVal = currentElm[0].innerText;
          $scope.searchInputBackVal = '';
        }
        if (key === 13) {
          $scope.searchSuggestion = false;
          $scope.loadSearchRes($scope.searchInputVal);
        }
      }
    };

    $scope.searchRestaurants = function (event) {
      //This is just for initial testing
      $('.rl-mapview__details').removeClass('hide');
      $('.rl-mapview__map').css('width', '');
      $scope.restaurants = restaurantData.features;
      $scope.selectedRestaurant = $scope.restaurants[0];
      markerCount = 1;
      getRestaurantsTemp($scope.restaurants);
      //-------------------------------
      if ([13, 38, 40].indexOf(event.keyCode) === -1) {
        if ($scope.searchInputVal !== undefined && $scope.searchInputVal !== '') {
          $scope.geocodeAddress($scope.searchInputVal);
        }
        else {
          $scope.searchInputBackVal = '';
          $scope.searchSuggestion = false;
        }
      }
      else {
        const $listItems = $('.mcd-rlwidget__suggestion--section li');
        const key = event.keyCode;
        const $selected = $listItems.filter('.selected');
        let currentElm;

        $listItems.removeClass('selected');

        if (key === 40) {
          if (!$selected.length || $selected.is(':last-child')) {
            currentElm = $listItems.eq(0);
          }
          else {
            currentElm = $selected.next();
          }
        }
        else if (key === 38) {
          if (!$selected.length || $selected.is(':first-child')) {
            currentElm = $listItems.last();
          }
          else {
            currentElm = $selected.prev();
          }
        }
        if (currentElm && currentElm.length > 0) {
          $scope.clearSelected();
          currentElm.addClass('selected');
          currentElm.attr('aria-selected', 'true').attr('id', 'selectedOption');
          $scope.input.attr('aria-activedescendant', 'selectedOption');
          $scope.input.attr('aria-expanded', 'true');
          $scope.searchInputVal = currentElm[0].innerText;
          $scope.searchInputBackVal = '';
        }
        if (key === 13) {
          $scope.searchSuggestion = false;
          $scope.loadSearchRes($scope.searchInputVal);
        }
      }
    };

    function showPosition(position) {
      const latitude = position.coords.latitude;
      const longitude = position.coords.longitude;
      $scope.currentLatitude = latitude;
      $scope.currentLongitude = longitude;
      initMap(latitude, longitude);
      $scope.getRestaurantsDetail(latitude, longitude);
    }

    function showError(error) {
      switch (error.code) {
        case error.PERMISSION_DENIED:
          alert($scope.fullRlData.locationPermissionDeniedText);
          break;
        case error.POSITION_UNAVAILABLE:
          alert($scope.fullRlData.locationUnavailableText);
          break;
        case error.TIMEOUT:
          alert($scope.fullRlData.locationTimeoutText);
          break;
        case error.UNKNOWN_ERROR:
          alert($scope.fullRlData.locationUnknownErrorText);
          break;
        default:
          break;
      }
    }

    function showPositionMap(position) {
      const latitude = position.coords.latitude;
      const longitude = position.coords.longitude;
      $scope.currentLatitude = latitude;
      $scope.currentLongitude = longitude;

    }

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
        default:
          break;
      }
    }

    $scope.showPopUp = function () {

      const path = window.location.href;
      console.log(path);
      if (path.indexOf('.gma.html') >= 0) {

        return false;

      }

      return true;
    }

    $scope.resetFilters = function() {
      $('.filter-item input').removeProp('checked');
      $scope.searchRestaurants();
    }

    /**
       * The CenterControl adds a control to the map that recenters the map on
       * Chicago.
       * This constructor takes the control DIV as an argument.
       * @constructor
       */
      function CenterControl(controlDiv, map) {

        // Set CSS for the control border.
        var controlUI = document.createElement('div');
        controlUI.style.backgroundColor = '#fff';
        controlUI.style.border = '1px solid #adadad';
        controlUI.style.borderRadius = '4px'; 
        controlUI.style.boxShadow = '0 2px 6px rgba(0,0,0,.3)';
        controlUI.style.cursor = 'pointer';
        controlUI.style.marginBottom = '22px';
        controlUI.style.marginRight = '12px';
        controlUI.style.textAlign = 'center';
        controlUI.title = 'Click to recenter the map';
        controlDiv.appendChild(controlUI);

        // Set CSS for the control interior.
        var controlText = document.createElement('div');
        controlText.style.fontSize = '27px';
        controlText.style.height = '36px';
        controlText.style.width = '36px';
        controlText.style.position = 'relative';
        controlText.style.top = '5px';
        controlText.innerHTML = '<span class="icon-crosshairs"><span class="path1"></span><span class="path2"></span><span class="path3"></span><span class="path4"></span><span class="path5"></span><span class="path6"></span></span>';
        controlUI.appendChild(controlText);

        // Setup the click event listeners: simply set the map to Chicago.
        controlUI.addEventListener('click', function() {
          map.setCenter({lat: lat, lng: lng});
        });

      }

      /**
       * The CenterControl adds a control to the map that recenters the map on
       * Chicago.
       * This constructor takes the control DIV as an argument.
       * @constructor
       */
      function RedoSearch(controlDiv, map) {

        // Set CSS for the control border.
        var controlUI = document.createElement('div');
        controlUI.style.backgroundColor = '#fff';
        controlUI.style.border = '1px solid #adadad';
        controlUI.style.borderRadius = '4px'; 
        controlUI.style.boxShadow = '0 2px 6px rgba(0,0,0,.3)';
        controlUI.style.cursor = 'pointer';
        controlUI.style.marginBottom = '22px';
        controlUI.style.marginRight = '12px';
        controlUI.style.textAlign = 'center';
        controlUI.title = 'Click to recenter the map';
        controlDiv.appendChild(controlUI);

        // Set CSS for the control interior.
        var controlText = document.createElement('div');
        controlText.style.fontSize = '15px';
        controlText.style.height = '36px';
        controlText.style.width = '204px';
        controlText.style.position = 'relative';
        controlText.style.top = '9px';
        controlText.innerHTML = `<span class="icon-meatball"><span class="path1"></span><span class="path2"></span><span class="path3"></span><span class="path4"></span></span>
                                <span>Redo Search Here</span>`;
        controlUI.appendChild(controlText);

        // Setup the click event listeners: simply set the map to Chicago.
        controlUI.addEventListener('click', function() {
          map.setCenter({lat: lat, lng: lng});
        });

      }

    $scope.addMarkerToMap = function (restaurantLat, restaurantLong, resEnabled, restaurantId) {
      var mcdPinIcon = resEnabled ? '/etc/clientlibs/mcd-us/img/mcd_marker.svg' : grayedOutRestImagePath;
      //var myLatLng = new google.maps.LatLng(restaurantLat, restaurantLong);
      mcdPinIcon = "//chart.googleapis.com/chart?chst=d_map_pin_letter&chld=" + markerCount + "|ff4022"
      //mcdPinIcon = "/etc/clientlibs/mcd-us/resources/images/user-location.png"
      var marker, i;  
      infowindow = null;


    function HTMLMarker(lat,lng){
      this.restaurantLat = restaurantLat;
      this.restaurantLong = restaurantLong;
      this.pos = new google.maps.LatLng(restaurantLat,restaurantLong);
    }
    
    HTMLMarker.prototype = new google.maps.OverlayView();
    HTMLMarker.prototype.onRemove= function() {
      div.parentNode.removeChild(div);
      div = null;
    }
    
    let div;
    
    //init your html element here
    HTMLMarker.prototype.onAdd= function() {
        div = document.createElement('DIV');
        div.className = "htmlMarker";
        var markerDiv = document.createElement('DIV');
        markerDiv.className = 'rl-map-pin';
        markerDiv.innerHTML = `<span class="icon-map-pin"><span class="path1"></span><span class="path2"></span><span class="path3"></span><span class="path4"></span><span class="path5"></span></span>
        <span class="rl-map-pin__count">${markerCount}</span>`;
        //div.innerHTML = `<div onclick="document.getElementById('hidden-button-infowindow').click()" class="rl-map-pin">
        
        markerDiv.onclick = function(e) {
          $scope.$apply(function () {
            $scope.selectedRestaurant = $scope.restaurants[Number(e.target.innerHTML)];
            $scope.selectedRestaurant.index = Number(e.target.innerHTML);
          }); 
          angular.element('.rl-mapview__details .rl-map-pin__count').text(e.target.innerHTML);
          let contentString = `<div onclick="document.getElementById('hidden-button-infowindow').click()" style="font-family:ArchSans; padding: 3px 0px 3px 9px;" class="rl-infowindow-content text-center">
          <div class="rl-address">${$scope.restaurants[Number(e.target.innerHTML)].properties.addressLine1}</div>
           <div style="color: #60605b; font-size: 11px; font-family: ArchSans-Light" class="rl-distance">0.9 ${$scope.fullRlData.distanceUnitLocal}  ${$scope.fullRlData.away}</div>
          </div>`

        if (infowindow) {
          infowindow.close();
        }

        infowindow = new google.maps.InfoWindow({
          content: contentString,
          pixelOffset: new google.maps.Size(12,-15)
        });
        //infowindow.setContent($scope.restaurants[selectedLocation].properties.addressLine1);
        infowindow.setPosition({lat: restaurantLat, lng: restaurantLong});
        infowindow.open(map, htmlMarker);
        //$scope.openRestaurantDetailsPopup($scope.selectedRestaurant, e.target.innerHTML, 'open');
        }
        div.appendChild(markerDiv);
        var panes = this.getPanes();
        panes.overlayMouseTarget.appendChild(div);
        markerCount++;
    }
    
    HTMLMarker.prototype.draw = function() {
        var overlayProjection = this.getProjection();
        var position = overlayProjection.fromLatLngToDivPixel(this.pos);
        div.style.position = 'absolute';
        div.style.left = position.x + 'px';
        div.style.top = position.y - 30 + 'px';
    }
    
    //to use it
    htmlMarker = new HTMLMarker(restaurantLat, restaurantLong);
    htmlMarker.setMap(map);


      
      // marker = new google.maps.Marker({
      //   position: new google.maps.LatLng(restaurantLat, restaurantLong),
      //   map: map,
      //   icon: mcdPinIcon
      // });
     

      //Gives each marker an Id for the on click

     //Creates the event listener for clicking the marker and places the marker on the map

      // marker.addListener('click', function () {
      //   let selectedLocation = Number(marker.icon.substring(marker.icon.indexOf('chld=') + 5, marker.icon.indexOf('|')))
      //   map.setCenter(marker.getPosition());

      //   let contentString = `<div onclick="document.getElementById('hidden-button-infowindow').click()" style="font-family:ArchSans; padding: 3px 0px 3px 9px;" class="rl-infowindow-content text-center">
      //     <div class="rl-address">${$scope.restaurants[selectedLocation].properties.addressLine1}</div>
      //     <div style="color: #60605b; font-size: 11px; font-family: ArchSans-Light" class="rl-distance">0.9 ${$scope.fullRlData.distanceUnitLocal}  ${$scope.fullRlData.away}</div>
      //   </div>`

      //   if (infowindow) {
      //     infowindow.close();
      //   }

      //   infowindow = new google.maps.InfoWindow({content: contentString});
      //   //infowindow.setContent($scope.restaurants[selectedLocation].properties.addressLine1);
      //   infowindow.open(map, marker);

      //   $scope.$apply(function () {
      //     $scope.restaurants = $scope.restaurants;
      //     $scope.selectedRestaurant = $scope.restaurants[selectedLocation];
      //     angular.element('.rl-map-pin__count').text(selectedLocation);
      //   }); 
      //   // if(window.innerWidth < 600) {
      //   //   $scope.openRestaurantDetailsPopup($scope.selectedRestaurant, selectedLocation, 'open');
      //   // }
        
      // });

      markers.push(htmlMarker);    
    };

    function getRestaurantsTemp(restaurants) {

      var options = {
        zoom: 11,
        center: new google.maps.LatLng(restaurants[0].geometry.coordinates[1], restaurants[0].geometry.coordinates[0]),
        zoomControl: true,
        disableDefaultUI: true,
        zoomControlOptions: {
          position: google.maps.ControlPosition.RIGHT_BOTTOM
        },
        mapTypeId: google.maps.MapTypeId.ROADMAP
      };
      map = new google.maps.Map(document.getElementById('map'), options);

      // Create the DIV to hold the control and call the CenterControl()
      // constructor passing in this DIV.
      var centerControlDiv = document.createElement('div');
      var redoSearchDiv = document.createElement('div');
      var centerControl = new CenterControl(centerControlDiv, map);
      var centerControl1 = new RedoSearch(redoSearchDiv, map);

      centerControlDiv.index = 1;
      map.controls[google.maps.ControlPosition.RIGHT_BOTTOM].push(centerControlDiv);
      map.controls[google.maps.ControlPosition.BOTTOM_CENTER].push(redoSearchDiv);

      for(let i=0; i<$scope.restaurants.length; i+=1) {
        $scope.addMarkerToMap(restaurants[i].geometry.coordinates[1], restaurants[i].geometry.coordinates[0], true, restaurants[i].properties.id);
      }
    }

    function initMap(lat, lng) {
      var options = {
        zoom: 5,
        center: new google.maps.LatLng(lat, lng),
        disableDefaultUI: true,
        zoomControl: true,
          zoomControlOptions: {
            position: google.maps.ControlPosition.RIGHT_BOTTOM
          },
      };

      map = new google.maps.Map(document.getElementById('map'), options);


      // Create the DIV to hold the control and call the CenterControl()
      // constructor passing in this DIV.
      var centerControlDiv = document.createElement('div');
      var redoSearchDiv = document.createElement('div');
      var centerControl = new CenterControl(centerControlDiv, map);
      var centerControl1 = new RedoSearch(redoSearchDiv, map);

      centerControlDiv.index = 1;
      map.controls[google.maps.ControlPosition.RIGHT_BOTTOM].push(centerControlDiv);
      map.controls[google.maps.ControlPosition.BOTTOM_CENTER].push(redoSearchDiv);

      marker = new google.maps.Marker({
        position: new google.maps.LatLng(lat, lng),
        map: map,
        icon: '/etc/clientlibs/mcd-us/resources/images/user-location.png'
      });

      markers.push(marker);
    }

    initMap(defaultLatLang.lat, defaultLatLang.lng);

    $scope.geocodeAddress = function (searchedAddress) {
      const geocoder = new google.maps.Geocoder();
      if (restaurantsResultCountry !== undefined) {
        searchedAddress = `${searchedAddress}`;//`${searchedAddress},${restaurantsResultCountry}`;
      }
      const geocodeRequest = {
        address: searchedAddress,
      };
      geocoder.geocode(geocodeRequest, (results, status) => {
        if (status === google.maps.GeocoderStatus.OK) {
          if (results.length > 1) {
            $scope.multipleLocations = [];
            let tempMultiLocation = [];
            let counter = 0;
            for (let i = 0; i < results.length; i += 1) {
              if (checkIfAddressIsInConfiguredCountry(results[i])) {
                let tempMultiLocObject = {};
                tempMultiLocObject.formattedAddress = results[i].formatted_address;
                tempMultiLocObject.latitude = results[i].geometry.location.lat();
                tempMultiLocObject.longitude = results[i].geometry.location.lng();
                tempMultiLocation[counter] = tempMultiLocObject;
                counter += 1;
              }
            }
            if (tempMultiLocation.length > 0) {

              $scope.$apply(() => {
                $scope.multipleLocations = tempMultiLocation;
                console.log(`$scope.multipleLocations ${$scope.multipleLocations}`);
                if (Scopes.get('fcontroller') !== undefined) {
                  Scopes.get('fcontroller').setMultipleLocations(tempMultiLocation);
                }
                $scope.isMulti = false;
                $scope.searchSuggestion = true;
                const [backgroundInputData] = $scope.multipleLocations.map(item => item.formattedAddress);

                $scope.searchInputBackVal = searchedAddress + backgroundInputData.substring(searchedAddress.length);

                $scope.displayRlResultView = true;
              });
            }
            else {
              //showNoResults();
            }


          }
          else if (checkIfAddressIsInConfiguredCountry(results[0])) {
            // const latitude = results[0].geometry.location.lat();
            // const longitude = results[0].geometry.location.lng();
            // $scope.getRestaurantsDetail(latitude, longitude);
          } else {
            //showNoResults();
          }
          if (initializeMapStatus) { initializeMapStatus = !initializeMapStatus; }
        }
        else {
          //showNoResults();
        }
      });
    };

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
  
          console.log("store Id Is Set");
        }
  
      });
    }

    function setCoopId(cname, cvalue, exdays) {
      var storeId = cvalue.properties.identifiers.storeIdentifier;
      var countrycode = GetPathForCookie.setPathForCookie($location.path());
      //var countrycode = $location.path().split("/"+restaurantsResultCountry.toLowerCase()+"/")[0] + "/"+restaurantsResultCountry.toLowerCase()+"/";
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
      //document.cookie = "storeId="+storeNaId+"; "+expires+";domain="+domainName+";path=/";
  
    }
  
    function setCookie(cname, cvalue, exdays) {
  
      function commaReplacer(key, value) {
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
  
      // var countrycode = $location.path().split("/"+restaurantsResultCountry.toLowerCase()+"/")[0] + "/"+restaurantsResultCountry.toLowerCase()+"/";
      var countrycode = GetPathForCookie.setPathForCookie($location.path());
  
      var d = new Date();
      d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
      var expires = "expires=" + d.toGMTString();
      document.cookie = cname + "=" + cvalue + "; " + expires + ";path=" + countrycode;
    }
  
    function expireCookie(cname, cvalue, exdays) {
      cvalue = JSON.stringify(cvalue);
      // var countrycode = $location.path().split("/"+restaurantsResultCountry.toLowerCase()+"/")[0] + "/"+restaurantsResultCountry.toLowerCase()+"/";
      var countrycode = GetPathForCookie.setPathForCookie($location.path());
      var d = new Date();
      d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
      var expires = "expires=" + d.toGMTString();
      document.cookie = cname + "=" + cvalue + "; " + expires + ";path=" + countrycode;
    }

    $scope.setPrefered = function (cvalue) {
      setCoopId(coopid, cvalue, 2);
      setCookie(cookieVal, cvalue, 2);
      expireCookie(cookieValNearestLoc, "", 0);
      Scopes.get('glController').init(true, true);
      if (window.location.hash.substr(1) == 'isGwc') {
        var myDealsLink = $('#mydealsLink').attr('data-domain');
        window.location = myDealsLink;
      }
    }

    function showNoResults() {
      // deleteMarkers();
      // $scope.showRestaurantsOnMap([]);
      // $scope.$apply(function () {
      //   $scope.currentPageNo = 1;
      //   $scope.restaurants = [];

      // });
    }

    function setMapOnAll(map) {
      for (let i = 0; i < markers.length; i++) {
        markers[i].setMap(map);
      }
    };

    // Deletes all markers in the array by removing references to them.
    function deleteMarkers() {
      setMapOnAll(null);
      markers = [];
    }

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
                if (countryShortName !== undefined && countryShortName === restSearchCountry) {
                  return true;
                }
                else if ((countryShortName !== undefined && countryShortName === 'sa') && (restSearchCountry === "saj" || restSearchCountry == "sar")) {
                  return true;
                }
              }
            }

          }
        }
      }
      return false;
    }

    $scope.showRestaurantForMulti = function (e, location) {
      const value = $(e.target).text();
      console.log(value, location);
      $scope.restaurantSearchText = value;
      $scope.isMulti = false;
      $('#restaurantLocatorDisambiguityModal').find('.btn-close').click();
      $scope.getRestaurantsDetail(location.latitude, location.longitude);
      $('.modal-backdrop').remove();
    };

    $scope.getRestaurantsDetail = function (latitude, longitude) {

      // if ($('div[name="rlRadius"]').find('button').attr('title') != undefined) {
      //   if (isNaN(parseInt($('div[name="rlRadius"]').find('button').attr('title')))) {
      //     $scope.restaurantRadius = $scope.fullRlData.radius[0];
      //   } else {
      //     $scope.restaurantRadius = parseInt($('div[name="rlRadius"]').find('button').find('.filter-option').text());
      //   }
      // }
      $scope.restaurantRadius = $scope.fullRlData.radius[0];
      referenceLatitudeForDistance = latitude;
      referenceLongitudeForDistance = longitude;
      if ($scope.distanceUnit === 'kilometer') {
        $scope.resRadius = parseInt($scope.restaurantRadius);
      } else if ($scope.distanceUnit === 'miles') {
        $scope.resRadius = parseInt($scope.restaurantRadius) * 1.609;
      }
      $scope.distanceReference = 0;
      $scope.resultShown = parseInt($scope.fullRlData.resultIterate);

      markerCount = 1;
      $scope.distanceReference = $scope.restaurantRadius;
      if ($scope.currentLatitude === undefined || $scope.currentLongitude === undefined) {
        if ($scope.showPopUp()) {
          if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(showPositionMap, showErrorMap);
          } else {

            console.log("Geolocation is not supported by this browser.");
          };
        }
      }

      if ($scope.currentLatitude === undefined || $scope.currentLongitude === undefined) {
        $scope.currentLatitude = latitude;
        $scope.currentLongitude = longitude;
      }

      $.get('/googleapps/GoogleRestaurantLocAction.do?method=searchLocation&latitude=' + latitude + '&longitude=' + longitude +
        '&radius=' + $scope.resRadius +
        '&maxResults=' + maxResults + '&country=' + restaurantSearchCountry + '&language=' + restaurantsResultLanguage + '&showClosed=' + showClosed + '&hours24Text=' + encodeURIComponent(hours24Text),
        function (data) {
          $scope.displayRlResultView = true;

          var results = data.features;
          //results.sort(function(a,b){return a.properties.distance-b.properties.distance});

          var myOptions = {
            zoom: 11,
            center: new google.maps.LatLng(latitude, longitude),
            zoomControl: true,
            zoomControlOptions: {
              position: google.maps.ControlPosition.LEFT_CENTER
            },
            streetViewControl: true,
            mapTypeId: google.maps.MapTypeId.ROADMAP
          };
          map = new google.maps.Map(document.getElementById("gmap_canvas"), myOptions);
          deleteMarkers();
          resultDuplicate = [];

          for (rIndex = 0; rIndex < results.length; rIndex++) {
            if (results[rIndex].geometry != null) {
              if ($scope.distanceUnit === 'kilometer') {
                results[rIndex].properties.distance = parseFloat(Math.round((calculateDistance(results[rIndex].geometry.coordinates[1], results[rIndex].geometry.coordinates[0]) / 1000) * 100) / 100).toFixed(1);
              } else if ($scope.distanceUnit === 'miles') {
                results[rIndex].properties.distance = parseFloat(Math.round((calculateDistance(results[rIndex].geometry.coordinates[1], results[rIndex].geometry.coordinates[0]) * 0.000621371192) * 100) / 100).toFixed(1);

              }
              if (parseFloat($scope.distanceReference).toFixed(1) >= parseFloat(results[rIndex].properties.distance)) {
                resultDuplicate.push(results[rIndex]);
              }
            }
          }
          results = resultDuplicate;


          /*  google.maps.event.trigger( map, 'resize' );
            map.setCenter(new google.maps.LatLng(latitude, longitude));*/
          $scope.showRestaurantsOnMap(results);
          $scope.restaurants = results;
          phoneNumberForScreenReader();
          HoursForScreenReader();

          if (!$scope.header) {
            $scope.filterSelected = $scope.fullRlData.filterSelected;
            if ($scope.filterSelected != undefined) {

              var filterSplit = $scope.filterSelected;
              for (var indexFilter = 0; indexFilter < filterSplit.length; indexFilter++) {
                $('input[value=' + filterSplit[indexFilter] + ']').click();
              }
            }
          }
          $scope.fullRlData.filterSelected = [];
          $scope.filterSelected = [];
          processRestaurantFilters();

          $scope.$apply(function () {
            $scope.restaurants = $scope.restaurants;

          });

        });
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

    $scope.openRestaurantDetailsPopup = function(restaurant, index, toggle) {
      if(window.innerWidth < 600) {
        $scope.selectedRestaurant = restaurant;
        if(toggle === 'open') {
          console.log('if');
          angular.element(".rl_load-details-mobile").removeClass('hide');
          angular.element('.rl_load-details-mobile .rl-map-pin__count').text(index);
        } else {
          console.log('else');
          setTimeout(function() {angular.element(".rl_load-details-mobile").addClass('hide');}, 100)  
        }
      }
    }

    $scope.openFiltersPopup = function(toggle) {
      if(toggle === 'open') {
        console.log('if');
        angular.element(".rl-filters-popup-mobile").removeClass('hide');
      } else {
        console.log('else');
        setTimeout(function() {angular.element(".rl-filters-popup-mobile").addClass('hide');}, 100)  
      }
    }

    $scope.closeRestaurantDetailsPopup = function() {
      console.log("close");
    }

    $scope.toggleListMapView = function() {
      $scope.showListView = !$scope.showListView;
    }

    $scope.toggleTotalVisibleRestaurants = function() {
      if($scope.totalVisibleRestaurants === 5) {
        $scope.totalVisibleRestaurants = $scope.restaurants.length;
      } else {
        $scope.totalVisibleRestaurants = 5;
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
      for (let j = 0; j < resFilterfalse.length; j++) {
        resFilter.push(resFilterfalse[j]);
      }
      $scope.restaurants = resFilter;
  
      $scope.enabledRestaurantsCount = enabledCount;
      if ($scope.enabledRestaurantsCount == 0) {
        $scope.showLoadMore = false;
      }
      else {
        if ($scope.enabledRestaurantsCount <= $scope.resultShown) {
          $scope.showLoadMore = false;
        } else {
          $scope.showLoadMore = true;
        }
      }
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

    $scope.selectedRestaurantFilters = [];
    $scope.filterRestaurants = function (filterName) {
      console.log(htmlMarker);
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

  }
}());
