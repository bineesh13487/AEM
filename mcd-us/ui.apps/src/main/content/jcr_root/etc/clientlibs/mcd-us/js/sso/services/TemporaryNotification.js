/**
 *  The TemporaryNotification Service checks once on pageload for the presence of a 
 *  temporary notification / alert key cookie and, if one is present, caches it for 
 *  the duration of the pageview, then clears it. Also has a set method to set the
 *  key for the next pageview.
 * 
 *  Usage:
 *    temporaryNotification.get() - returns the [string] key of this pageview's 
 *        notification, or undefined
 *    temporaryNotification.set(key) - sets the key for the next pageview (of this 
 *        session).
 */
ssoApp.service('temporaryNotificationCookie', ['$cookies', '$timeout', function ($cookies, $timeout) {
    
    /** cookieKey is the cookie name. */
    var cookieKey = 'dcs-sso-alert';

    
    
    /** cookieOptions is an object of parameters to pass through to $cookies.put() */
    var cookieOptions = {
        path: '/'
    };

    var immediateShowQueue = [];
    var immediatelyShowed = false;


    var msg = '';
    var alertKey = $cookies.get(cookieKey);

    if (alertKey) {
        var pl = $cookies.remove(cookieKey, cookieOptions);
    }

    function callbacks () {
        if (immediateShowQueue.length > 0) {
            angular.forEach(immediateShowQueue, function (func) {
                if (typeof func === 'function') {
                    $timeout(func, 10);
                }
            });
            immediatelyShowed = true;
        }
    }



    /**
     * Immediately show a notification.
     */
    this.show = function (id) {
        if (id) {
            alertKey = id;
            callbacks();
        }
    };

    
    this.useImmediate = function (_msg) {
        if (_msg && typeof _msg === 'string') {
            msg = _msg;
            this.show('__msg__');
        }
    };



    /**
     * Queue a callback for immediately showing a notification
     */
    this.onShow = function (func) { 
        if (typeof func === 'function') {
            immediateShowQueue.push(func);
            if (immediatelyShowed) {
                func();
            }
        }
    };



    /**
     * Sets the temporary notification key cookie for the next pageview of the session.
     */
    this.set = function (id) {
        return $cookies.put(cookieKey, id, cookieOptions);
    };

   	 /**
     * Sets the temporary notification msg with the scope email.
     */
    this.setEmail = function (username, msg){
        msg =  msg.replace("{{email}}", username);
       return msg;
    };

    /**
     * Returns the key for this pageview, or undefined if none exists.
     * Note - the cookie has already been deleted, this returns the cached version.
     */
    this.get = function () {
        return alertKey;
    };

    this.getMsg = function () {
        return msg;
    };

    this.clear = function () {
        msg = '';
        alertKey = undefined;
        $cookies.remove(cookieKey, cookieOptions);
        callbacks();
    };

}]);