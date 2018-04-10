app.factory('GetPathForCookie', function() {
	return {
		setPathForCookie: setPathForCookie
	};
	function setPathForCookie (path) {
	  	if (path.indexOf("/content/") == 0) {
	  		if (path.indexOf("/prelaunch/") > -1) {
	  			glCookiePath = "/content/prelaunch/"+path.split("/")[3]+"/";
	  		}
	  		else {
	  			glCookiePath = "/content/"+path.split("/")[2]+"/";
	  		}
	      }
	  	else {
	  		if (path.indexOf("/prelaunch/") > -1) {
	  			glCookiePath = "/prelaunch/"+path.split("/")[2]+"/";
	  		}
	  		else {
	  			glCookiePath = "/"+path.split("/")[1]+"/";
	  		}
	  	}
	  	return glCookiePath;
	}
});