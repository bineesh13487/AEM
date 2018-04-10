ssoApp.service('SecureComponent', ['customer', 'config', function(customer, config) {
    
    var homePageURLConfigKey = 'homeURL';
    
    var redirectURL = config.get('config').redirectNotLoggedInUserPath;

    if (typeof redirectURL !== 'string' || redirectURL.length < 1) {
        redirectURL = '/';
    }

    this.secure = function () {
        customer.isUserLoggedIn().then(
            function (isLoggedIn) {
                if (!isLoggedIn && !config.get('inEditorOrPreview')) {
                    window.location = redirectURL;
                }
            }
        );
    };
    
}]);