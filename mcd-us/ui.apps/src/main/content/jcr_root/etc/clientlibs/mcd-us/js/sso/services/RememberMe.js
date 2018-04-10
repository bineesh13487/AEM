ssoApp.service('RememberMe', ['$cookies', function ($cookies) {
    var cookieKey = 'dcs-sso-rm';
    var expires = new Date();
    expires.setFullYear(expires.getFullYear() + 1);
    var cookieOptions = {
        expires: expires,
        path: '/'
    };
    var self = this;
    this.set = function (flag, un) {
        if (self.isSet()) {
            self.clear();
        }
        $cookies.putObject(cookieKey, {flag: flag, un: un}, cookieOptions);
        return;
    };
    this.setFlag = function (flag) {
        var un = this.getUserName();
        return this.set(flag, un);
    };
    this.setUserName = function (un) {
        var flag = this.getFlag();
        return this.set(flag, un);
    };

    this.isSet = function () {
        var cookie = this.get();
        return cookie ? cookie.flag === true : false;
    };

    this.clear = function () {
        if (this.isSet()) {
            $cookies.remove(cookieKey, cookieOptions);
        }
        return;
    };

    this.get = function () {
        var obj = $cookies.getObject(cookieKey);
        return obj;
    };
    this.getFlag = function () {
        var cookie = this.get();
        return cookie.flag;
    };
    this.getUserName = function () {
        var cookie = this.get();
        return cookie.un;
    };
}]);
