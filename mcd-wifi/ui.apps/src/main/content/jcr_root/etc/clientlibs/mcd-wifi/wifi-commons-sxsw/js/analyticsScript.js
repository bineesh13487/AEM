var analyticsData = {site:{website:""}, page:{country:"", locale:"", section:"", platform:"", page_name:"", pageurl:""}, user:{time:"", day:"", profileID:"", userID:"", useragent:"", profileStatus:"", loggedInStatus:"", lat:"", longitude:""}, restaurant:{storeID:""}};

populateDataLayerVariables();
function populateDataLayerVariables() {
       var domain = "",country="",language="",platform="",page_name="",page_url="";
       var section = "",time="",day="",loggedIn_Status="", profile_Status="", user_ID="",profile_ID="",lat="",longitude="", store_ID="";

        lat = $('#lat').html();
        longitude = $('#lon').html();
       page_url = window.location.href;
       var path = window.location.pathname;
	   if(path.indexOf("/content/mcd-wifi/") != -1){
	     path = path.replace("/content/mcd-wifi","")
	   }
	   else if(path.indexOf("/mcd-wifi/") != -1){
		   path = path.replace("/mcd-wifi", "")
	   }
       var pathArray = path.split("/");
	   country = pathArray[1];
       language = pathArray[2];
       section = pathArray[3];
       if(section)
	   section = section.replace(".html","");

       domain = window.location.host;
       //page_name = domain+":"+country+":"+language+":"+section;
	   page_name = section;
       
       if(/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
         platform = "Mobile";
       }else{
         platform = "Desktop";
       }
       
	/*Find current day and time */  
      var days = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
      var date = new Date();
      day = days[date.getDay()];
      var hr = date.getHours();
      var min = date.getMinutes();
	  if (min < 10) {
		  min = "0" + min;
	  }
	  var ampm = hr < 12 ? "AM" : "PM";
	  if(hr > 12){
	     hr = hr - 12;
	  }
      time =  hr + ":" + min + ampm ;

    /* assigning values in the object */
 		 analyticsData.site.website = domain;
     	 analyticsData.page.country = country,
		 analyticsData.page.locale = language;
		 analyticsData.page.section = section;
		 analyticsData.page.platform = platform;
		 analyticsData.page.page_name = page_name;
		 analyticsData.user.time = time;
		 analyticsData.user.day = day;
		 analyticsData.user.useragent = navigator.userAgent;
		 analyticsData.page.pageurl = page_url;
		 analyticsData.user.loggedInStatus = loggedIn_Status;
		 analyticsData.user.profileStatus = profile_Status;
		 analyticsData.user.userID = user_ID;
		 analyticsData.user.profileID = profile_ID;
		 analyticsData.user.lat = lat;
		 analyticsData.user.longitude = longitude;
		 analyticsData.restaurant.storeID = store_ID;
}