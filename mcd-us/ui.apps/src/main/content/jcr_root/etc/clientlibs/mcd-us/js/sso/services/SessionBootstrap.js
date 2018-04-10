/**
 * The SessionBootstrap service requests the session cache from the backend on first request,
 * and returns a promise representing that request and resolving with that data to that and 
 * ever subsequent reguest.
 * 
 * Usage:
 *   sessionBootstrap.load() - returns a promise that resolves with the response from the session cache request.
 */
ssoApp.service('sessionBootstrap', ['$http', '$q', function ($http, $q, config) {



    var request;
    var sessionRequest = function (uri, headers) {
        return $q(function (resolve, reject) {
            $http.get(uri, {
                headers: headers,
                withCredentials: true
            }).then(
                function (resp) {
                    var profileObj = {
                        details: resp.data
                    };
                    resolve(profileObj);
                },
                function (resp) {
                    reject(resp);
                }
            );
        });
    };

    /**
     * returns a promise representing the session cache request, which resolves with the response.
     */
    this.load = function (uri, headers) {
        if (request === undefined) {
            request = sessionRequest(uri, headers);
        }
        return request;
    };
}]);