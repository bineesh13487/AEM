yrtkApp.controller('questionSearchController', ['$rootScope', '$scope', 'questionService', 'socialLoginService', '$attrs', '$http', '$timeout',
    function($rootScope, $scope, questionService, socialLoginService, $attrs, $http, $timeout) {

        $scope.searchKeyWord;
        $rootScope.configuredJSONObject = $attrs.json || {};
        $scope.configCountryCode = JSON.parse($rootScope.configuredJSONObject).countryCode;
        $scope.configLangCode = JSON.parse($rootScope.configuredJSONObject).languageCode;
        $scope.reCaptchaError = false;
        $scope.captchaEnable = false;
        $scope.captchaResponse = angular.element('#g-recaptcha-response');

        $scope.pattern = {
            'userName': JSON.parse($rootScope.configuredJSONObject).nameRegEx,
            'userEmail': JSON.parse($rootScope.configuredJSONObject).emailRegEx
        };
        $scope.userNameRequiredMsg = JSON.parse($rootScope.configuredJSONObject).nameErrorRequired;
        $scope.userNameInvalidMsg = JSON.parse($rootScope.configuredJSONObject).nameErrorInvalid;
        $scope.userEmailRequiredMsg = JSON.parse($rootScope.configuredJSONObject).emailErrorRequired;
        $scope.userEmailInvalidMsg = JSON.parse($rootScope.configuredJSONObject).emailErrorInvalid;

        $scope.checkboxError;

        $scope.$watch('searchKeyWord', function(searchKeyWord) {
            $rootScope.searchedQuestion = $scope.searchKeyWord;
        });

        $scope.user = {
            name: '',
            email: '',
            subscribe: false,
            questionEmpty: false
        };
        $scope.questionsList = [];

        socialLoginService.initFacebook();

        //FB LOGIN
        $scope.loginFacebook = function() {
            socialLoginService.loginFacebook().
                then(function onSuccess(resp) {
                    if (resp.status === 'connected') {
                        var userDetails = socialLoginService.getFacebookUserDetails();
                        userDetails.then(function(resp) {
                                $scope.user.name = resp.first_name + ' ' + resp.last_name;
                                $scope.user.email = resp.email;

                            },
                            function(err) {
                                console.log("error");
                            }
                        );

                    }
                }, function onError(err) {
                    console.log("error");
                });
        }
        //FB LOGIN ENDS HERE

        //TWITTER LOGIN
        $scope.loginTwitter = function() {
            if ($scope.user.question) {
                if ($scope.user.subscribe) {

                    var dataObj = {
                        openId: $scope.user.email,
                        description: $scope.user.question,
                        socialUserName: $scope.user.name,
                        languageCode: $scope.configLangCode,
                        countryCode: $scope.configCountryCode,
                        appId: 'GWSGlobal',
                        userEmail: $scope.user.email
                    };
                    questionService.submitTwitterQuestion(dataObj);
                } else {
                    $scope.checkboxError = true;
                }
            } else if ($scope.user.subscribe) {
                $scope.user.questionEmpty = true;

            } else {
                $scope.checkboxError = true;
                $scope.user.questionEmpty = true;
            }

        }
        //TWITTER LOGIN ENDS HERE


        //SEARCH
        $scope.searchKeyDown = function() {
            if ($scope.searchKeyWord.length > 2) {
                $('.after-search').fadeIn(500);
            } else {
                $('.after-search').fadeOut(500);
            }
            $timeout(function() {
                appyrtk.masonry.masonry('reloadItems');
                appyrtk.masonry.masonry();
            }, 100);
        }

        $scope.getQuestionsList = function() {
            var resp = questionService.getAllQuestions();
            $scope.questionsList = resp.result.rows;
        }

        // function to submit the form after all validation has occurred
        $scope.submitForm = function() {

            // check to make sure the form is completely valid
            if ($scope.userForm.$valid) {
                var dataObj = $.param({
                    openId: $scope.user.email,
                    description: $scope.user.question,
                    socialUserName: $scope.user.name,
                    languageCode: $scope.configLangCode,
                    countryCode: $scope.configCountryCode,
                    appId: 'GWSGlobal',
                    userEmail: $scope.user.email,
                    imageUrl: '',
                    socialMedia: 'Email', //Facebook || Email //TODO for Facebook login, use 'Facebook' value
                    accessToken: '',
                    kaptchaValue: '',
                    randomKey: ''
                });
                dataObj = dataObj + '&g-recaptcha-response=' + angular.element('#g-recaptcha-response')[0].value;

                questionService.submitQuestion(dataObj).
                    then(function onSuccess(resp) {
                        //show the success modal
                        $(".ovr-ask-a-question").hide();
                        $(".ovr-thank-you").fadeIn();
                        console.log('new question call success callback response: ', resp);
                    }, function onError(resp) {
                        //TODO show the error msg on the question submission form
                        console.log('new question call error callback response: ', resp);
                    });
            }
        }

        $scope.someSelected = function(object) {
            if (!object) return false;
            return Object.keys(object).some(function(key) {
                return object[key];
            });
        }
        $scope.doTouched = function() {
            $scope.userForm.subscribe.$setTouched();
        }
        $scope.checkStatusofTerms = function(e) {
            if (e != true) {
                $scope.checkboxError = true;
            } else {
                $scope.checkboxError = false;
            }
        }

        $scope.childmethod = function(e) {
            $rootScope.$emit("CallParentMethod", e);
        }
        $scope.viewAllEmitMethod = function(e) {
            $rootScope.$emit("ViewAll");
        }
        $rootScope.$on("DataAvailable", function(evt) {
            $scope.getQuestionsList();
        });

    }
]);