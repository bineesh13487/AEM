var app = angular.module("usr", ['ngCookies', 'ngSanitize', 'sso','yrtk'])
    .config(['$locationProvider', function($locationProvider) {
    	$locationProvider.html5Mode({
    		  enabled: true,
    		  requireBase: false,
			   rewriteLinks : false
    		});
    }]);

/*app.service('Scopes', function() {
return {
        store: function (key, value) {
            $rootScope.$emit('scope.stored', key);
            mem[key] = value;
        },
        get: function (key) {
            return mem[key];
        }
    };

});
*/
app.factory('Scopes', ['$rootScope', function($rootScope) {
    var mem = {};

    return {
        store: function(key, value) {
            $rootScope.$emit('scope.stored', key);
            mem[key] = value;
        },
        get: function(key) {
            return mem[key];
        }
    };
}]);