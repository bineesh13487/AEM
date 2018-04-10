var GeoLocationData = (function(){
	var preferredLocation = false;
	var location;
	var cookieVal = "geoLocationData";
	
		function setCookie(cname,cvalue,exdays) {
			var d = new Date();
			d.setTime(d.getTime() + (exdays*24*60*60*1000));
			var expires = "expires=" + d.toGMTString();
			document.cookie = cname+"="+cvalue+"; "+expires;
		}
		
		function getCookie(cname) {
			var name = cname + "=";
			var ca = document.cookie.split(';');
			for(var i=0; i<ca.length; i++) {
				var c = ca[i];
				while (c.charAt(0)==' ') c = c.substring(1);
				if (c.indexOf(name) == 0) {
					return c.substring(name.length, c.length);
				}
			}
			return "";
		}
		
		function getRLData() {
			var jsonObject = 
				   {
					   "id": "195500283654-en-us",
						"countryCode": "US",
						"generalStatus": {
						"status": "OPEN"
						},
						"address": {
							"addressLine1": "317 BROADWAY",
							"cityTown": "NEW YORK",
							"postalZip": "10007",
							"country": "USA",
							"region": "NEW YORK METRO REGION",
							"subdivision": "NY",
							"location": {
								"lat": 40.715771,
								"lon": -74.005234
							}
						}
				   };
			return JSON.stringify(jsonObject);
		}
		
		function successFunction(position) {
			var lat = position.coords.latitude;
			var lng = position.coords.longitude;
			console.log("Latitude and Longitude for current location: "+lat+", "+lng);
			var jsonRLData = getRLData();
			setCookie(cookieVal, jsonRLData, 30);
			console.log("cookie added");
			location = jsonRLData;
			
		}
		
		function errorFunction() {
			console.log("Failed to get user's geolocation !!!");
		}
	
		
		function showPopUp() {
			
	    	var path = window.location.href;
	    	console.log(path);
	    	if (path.indexOf(".gma.html") >= 0) {
	    		
	    		return false;
	    		
	    	}
	    	
	    	return true;
	    }
		
	
	var getlocationVal = getCookie(cookieVal);

	
	if (showPopUp()) {
	
	if(getlocationVal == "") {
		if (navigator.geolocation) {
				navigator.geolocation.getCurrentPosition(successFunction, errorFunction);
			}
		}
		else {
			location = getlocationVal;
			console.log("cookie exist: "+location);
		}
	}
	return {
		getLocation: function(){
			return location;
		},
		setLocation: function(pLocation) {
			location = pLocation;
		},
		isPreferredLocation: function() {
			return preferredLocation;
		},
		setPreferredLocation: function(pPreferredLocation) {
			preferredLocation = pPreferredLocation;
		}
	};
	
})();
