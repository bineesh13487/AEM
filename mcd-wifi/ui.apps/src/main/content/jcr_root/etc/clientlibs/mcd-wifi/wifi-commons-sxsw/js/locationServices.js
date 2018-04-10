var latitude = "";
var longitude = "";
var defaultJoinUs = "";
defaultJoinUs = $('.dataforjoinus').data('joinuslink');

$('#joinUsLink').attr('data-src', defaultJoinUs);

if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(showPosition);
} else {
    alert("Geolocation is not supported by this browser.");
}

function showPosition(position) {
    latitude = position.coords.latitude;
    longitude = position.coords.longitude;

    $.ajax({
        type: 'GET',
        url: '/bin/services/mcd-wifi/restaurantLocator',
        data: {
            'latitude': latitude,
            'longitude': longitude
        },
        success: function(data) {
            var result = $.parseJSON(data);
            if(result.length > 0){
                var addressLine1 = result[0].address.addressLine1;
                var cityTown = result[0].address.cityTown;
                var subdivision = result[0].address.subdivision;
                var country = result[0].address.country;
			}

            function titleCase(str) {
                if(str !=null & str!='undefined'){
                    var splitStr = str.toLowerCase().split(' ');
                    for (var i = 0; i < splitStr.length; i++) {
                        splitStr[i] = splitStr[i].charAt(0).toUpperCase() + splitStr[i].substring(1);
                    }
                    return splitStr.join(' ');
                }
                return '';
            }

            cityTown = titleCase(cityTown);

            if (country !== "USA" && country !== "usa") {
                country = titleCase(country);
            }

            if (subdivision !== null && cityTown !== null && country!== null) {
                $('#location').html(timeGreeting + "</br>" + returnGreeting + "</br>" + "  <span class='fa fa-map-marker'></span> " + cityTown + ", " + subdivision + ", " + country + ".");
				$('.mcd-logo + div').css('margin-top','0px');
            } else {
                $('#location').html(timeGreeting + "</br>" + returnGreeting);
                 $('.mcd-logo + div').css('margin-top','10px');
            }

            if(result.length > 0){
                var joinUsLink = result[0].urls.url[1].url;
                $('#joinUsLink').attr('data-src', joinUsLink + "/employment");
            }

        },
        
        failure: function(data) {}
    });
}