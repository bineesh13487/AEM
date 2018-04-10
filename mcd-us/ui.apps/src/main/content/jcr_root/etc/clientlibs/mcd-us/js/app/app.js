const app = angular.module('usr', ['ngCookies', 'ngSanitize', 'sso', 'yrtk'])
  .config(['$locationProvider', $locationProvider => {
    $locationProvider.html5Mode({
      enabled: true,
      requireBase: false,
      rewriteLinks: false
    });
  }]);

/* app.service('Scopes', function() {
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
app.factory('Scopes', ['$rootScope', $rootScope => {
  const mem = {};
  return {
    store(key, value) {
      $rootScope.$emit('scope.stored', key);
      mem[key] = value;
    },
    get(key) {
      return mem[key];
    }
  };
}]);
