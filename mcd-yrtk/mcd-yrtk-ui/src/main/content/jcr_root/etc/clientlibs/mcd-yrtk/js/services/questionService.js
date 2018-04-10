yrtkApp.service('questionService', ['$rootScope', '$http', '$q', '$sce', function($rootScope, $http, $q, $sce) {

    var configElement = angular.element('#question-search-component');
    var authorConfig = (configElement.length > 0) ? angular.fromJson(configElement.data('json')) : {};
    var baseURL = authorConfig.webServicesBaseUrl || '';
    var submitQuestionUrl = baseURL + '/ofyq/ask/saveQuestion.htm';
    var twitterQuestionUrl = baseURL + '/ofyq/twitter/signin.do';
    var countryCode = authorConfig.countryCode;
    var languageCode = authorConfig.languageCode;
    var allQuestions;



    function getHeaderParams() {
        return {
            'Content-Type': 'application/x-www-form-urlencoded'
        };
    }

    this.fbApiKey = function(){
        return authorConfig.apiKeyFacebook;
    }



    //Service to Get the feed of the questions. Requires two parameters - CountryCode & LanguageCode
    this.getRealFeed = function() {
        return $q(function(resolve, reject) {
            var url = baseURL + '/ofyq/questions/' + countryCode + '/' + languageCode + '/0/0/jsonp/JSON_CALLBACK';
            $http.jsonp($sce.trustAsResourceUrl(url)).
                then(function onSuccess(response) {
                    if (response.data.status === 'success') {
                        allQuestions = response.data;
                        resolve(response.data);
                    } else {
                        reject(response);
                    }
                }, function onError(response) {
                   reject(response);
                });
        });

    }

    this.submitTwitterQuestion = function (params) {
      var currLoc=window.location.href;
      var tstamp = new Date();
      // set form values
      $("#socialform").attr('action', twitterQuestionUrl);
      $("#descriptionAsk").val(params.description);
      $("#redirect_urlAsk").val(currLoc);
      $("#language_code").val(params.languageCode);
      $("#country_codeAsk").val(params.countryCode);
      $("#question_tstamp").val(tstamp.getTime());
      // submit form
      $("#socialform").submit();
    }


    this.submitQuestion = function(params) {
        return $q(function(resolve, reject) {
            var req = $http.post(submitQuestionUrl, params, {
                headers: getHeaderParams()
            });
            req.then(
                function(resp) {
                    if (resp.data == 1) {
                        resolve(resp);
                    } else {
                        reject(resp);
                    }
                }, reject);
        });
    }

    this.getAllQuestions = function() {
        return allQuestions;
    }

}]);