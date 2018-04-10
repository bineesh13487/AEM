yrtkApp.controller('questionFeedController', ['$rootScope', '$scope', 'questionService', '$attrs', '$http', '$sanitize', 'angularGridInstance', '$timeout',
    function($rootScope, $scope, questionService, $attrs, $http, $sanitize, angularGridInstance, $timeout) {

        $scope.modalVideoURL = undefined;
        $scope.questions;
        $scope.overAllQuestionSet;

        $scope.categoryFilterModel;
        $scope.categoryFilterOn = false;
        $scope.categoryFilterArray = [];
        $scope.limit = JSON.parse($attrs.json).chunkSize || 20;
        $scope.catfilteredQuestions = [];
        $scope.viewAll = false;
        $scope.showError = false;
        $scope.errorMsg = '';
        $scope.pinnedQuestionStatus;
        $scope.pinnedQuestionPosition;
        $scope.selectedQuestionTypeText = 'Text';
        $scope.selectedQuestionTypeImage = 'Image';
        $scope.selectedQuestionTypeVideo = 'Video';
        $scope.typeFilterSwitch;
        $scope.noResultsText = JSON.parse($attrs.json).noResultsText;
        $scope.twitterUsername = JSON.parse($attrs.json).twitterUsername;
        $scope.twitterHashtags = JSON.parse($attrs.json).twitterHashtags;
        $scope.searchComponentConfig = JSON.parse($rootScope.configuredJSONObject);
        $scope.searchSuggestionBtn1 = $scope.searchComponentConfig.suggestionBtn1;
        $scope.searchPlaceholder = $scope.searchComponentConfig.searchBoxPlaceholder;
        $scope.searchSuggestionBtn2 = $scope.searchComponentConfig.suggestionBtn2;
        $scope.searchBtnText = $scope.searchComponentConfig.questionBtnText;
        $scope.genericErrorMsg = $scope.searchComponentConfig.genericErrorMsg;
        $scope.questionsList = []; // this is used for the search suggestions

        //SEO Accommodation
        $scope.selectedQuestionID = JSON.parse($attrs.json).selectedQuestionID;
        $scope.originalTitle = $('title').text();
        $scope.originalMetaDescription = $("meta[name='description']").attr("content");
        $scope.originalMetaKeywords = $("meta[name='keywords']").attr("content") || '';
        $scope.originalURL = window.location.href;


        $scope.$watch('searchedQuestion', function(searchedQuestion) {
            $scope.searchedQUestionVal = $rootScope.searchedQuestion;
        });


        //Get Question Feed
        $scope.getQuestionsFeed = function() {
            $('.grid').hide();
            $('.spinner-cont').show();

            questionService.getRealFeed().
                then(function onSuccess(resp) {
                    $scope.overAllQuestionSet = resp.result.rows;
                    $scope.triggerQuestionSearchToGetData();
                    var pinnedQuestionID = JSON.parse($attrs.json).pinnedQuestionID || '';

                    var httpsUsed = false;
                    if (location.protocol == 'https:') {
                        httpsUsed = true;
                    }

                    if (pinnedQuestionID != '' || httpsUsed) {
                        var allQuestionsWithoutPinned = [];
                        var pinnedQuestion = [];
                        var currentQuestionElement;

                        for (i = 0; i < $scope.overAllQuestionSet.length; i++) {
                            currentQuestionElement = $scope.overAllQuestionSet[i];

                            //adjust the scheme of the profile image URL if needed
                            if (httpsUsed) {
                                currentQuestionElement.user[0].avatar_url = currentQuestionElement.user[0].avatar_url.replace('http:', 'https:');
                            }

                            if (currentQuestionElement.questionId == pinnedQuestionID) {
                                pinnedQuestion.push(currentQuestionElement);
                                $scope.pinnedQuestionPosition = i;
                            } else {
                                allQuestionsWithoutPinned.push(currentQuestionElement);
                            }
                        }

                        //if pinned question is found within the returned collection, put it first
                        if (pinnedQuestion.length > 0) {
                            $scope.overAllQuestionSet = pinnedQuestion.concat(allQuestionsWithoutPinned);
                            $scope.pinnedQuestionStatus = true;
                        }
                    }
                    $scope.questions = $scope.overAllQuestionSet;
                    $('.grid').show();
                    $('.spinner-cont').hide();
                    $timeout(function() {
                        appyrtk.masonry.masonry('reloadItems');
                        appyrtk.masonry.masonry();
                        $('.questionfeed .icon.plus-red-icon').on('click', function(event) {
                            event.preventDefault();
                        });
                    }, 100);
                    if ($scope.selectedQuestionID) {
                        for (var i = 0; i < $scope.questions.length; i++) {
                            if ($scope.questions[i].questionId === $scope.selectedQuestionID) {
                                $scope.openModal(i);
                                break;
                            }
                        }
                    }
                    //if there are no questions returned, show the 'no results found' message
                    if ($scope.questions.length == 0) {
                        $scope.errorMsg = $scope.noResultsText;
                        $scope.showError = true;
                    }

                }, function onError(resp) {
                    console.log("Error", resp.status);
                    $scope.errorMsg = $scope.genericErrorMsg;
                    $scope.showError = true;
                });
        } //Get Question Feed Ends Here

        $scope.triggerQuestionSearchToGetData = function(e) {
            $rootScope.$emit("DataAvailable", e);
        }

        //Type Filter
        $scope.filterByType = function(e) {
            $scope.selectedQuestionType;
            $scope.typeFiltered = [];
            switch (e) {
                case 'Text':
                    $scope.selectedQuestionType = 'Text';
                    $scope.typeFilterSwitch = true;
                    break;
                case 'Video':
                    $scope.selectedQuestionType = 'Video';
                    $scope.typeFilterSwitch = true;
                    break;
                case 'Image':
                    $scope.selectedQuestionType = 'Image';
                    $scope.typeFilterSwitch = true;
                    break;
                case 'All':
                    $scope.typeFilterSwitch = false;
                    break;
            }
            if ($scope.typeFilterSwitch) {
                for (var f = 0; f < $scope.overAllQuestionSet.length; f++) {
                    if ($scope.selectedQuestionType == $scope.overAllQuestionSet[f].type) {
                        $scope.typeFiltered.push($scope.overAllQuestionSet[f]);
                    }
                }
                $scope.questions = $scope.typeFiltered;
                $scope.catfilteredQuestions = [];
                if ($scope.categoryFilterArray.length != 0) {
                    for (var j = 0; j < $scope.typeFiltered.length; j++) {
                        for (var i = 0; i < $scope.categoryFilterArray.length; i++) {
                            $scope.selectedFilter = $scope.categoryFilterArray[i];
                            if ($scope.selectedFilter == $scope.typeFiltered[j].categoryName) {
                                $scope.catfilteredQuestions.push($scope.typeFiltered[j]);
                            }
                        }
                    }
                    $scope.questions = $scope.catfilteredQuestions;
                }
            } else {
                $scope.questions = $scope.overAllQuestionSet;
                $scope.catfilteredQuestions = [];
                if ($scope.categoryFilterArray.length != 0) {
                    for (var j = 0; j < $scope.overAllQuestionSet.length; j++) {
                        for (var i = 0; i < $scope.categoryFilterArray.length; i++) {
                            $scope.selectedFilter = $scope.categoryFilterArray[i];
                            if ($scope.selectedFilter == $scope.overAllQuestionSet[j].categoryName) {
                                $scope.catfilteredQuestions.push($scope.overAllQuestionSet[j]);
                            }
                        }
                    }
                    $scope.questions = $scope.catfilteredQuestions;
                }
            }
            $scope.limit = JSON.parse($attrs.json).chunkSize || 20;
            if ($scope.categoryFilterArray.length == 0) {
                $(".masonry-loader .btn").removeClass("disabled");
            }
            $timeout(function() {
                appyrtk.masonry.masonry('reloadItems');
                appyrtk.masonry.masonry();
            }, 100);
        }
        //Type Filter Ends Here ---->


        //Category Filter
        $scope.categoryFilter = function(e, n) {
            if (n == 1) {
                $scope.categoryFilterArray.push(e);
            } else if (n == 0) {
                var indexofFilter = $scope.categoryFilterArray.indexOf(e);
                if (indexofFilter > -1) {
                    $scope.categoryFilterArray.splice(indexofFilter, 1);
                    $scope.catfilteredQuestions = [];
                }
            }
        }
        $scope.applyFilter = function() {
            if ($scope.categoryFilterArray.length == 0) {

                $scope.limit = JSON.parse($attrs.json).chunkSize;
                $(".masonry-loader .btn").removeClass("disabled");
                $timeout(function() {
                    appyrtk.masonry.masonry();
                }, 100);
            } else {
                $scope.catfilteredQuestions = [];

                for (var j = 0; j < $scope.overAllQuestionSet.length; j++) {
                    for (var i = 0; i < $scope.categoryFilterArray.length; i++) {
                        $scope.selectedFilter = $scope.categoryFilterArray[i];
                        if ($scope.selectedFilter == $scope.overAllQuestionSet[j].categoryName) {
                            $scope.catfilteredQuestions.push($scope.overAllQuestionSet[j]);
                        }
                    }
                }
                $scope.questions = $scope.catfilteredQuestions;
                $scope.typeFiltered = [];
                if ($scope.typeFilterSwitch) {
                    for (var j = 0; j < $scope.catfilteredQuestions.length; j++) {
                        if ($scope.selectedQuestionType == $scope.catfilteredQuestions[j].type) {
                            $scope.typeFiltered.push($scope.catfilteredQuestions[j]);
                        }
                    }
                    $scope.questions = $scope.typeFiltered;
                }
                $scope.limit = $scope.catfilteredQuestions.length;
                $timeout(function() {
                    appyrtk.masonry.masonry('reloadItems');
                    appyrtk.masonry.masonry();
                }, 100);

                $(".filter-drop .after-drop").slideUp();
                $(".filter-drop").removeClass("active mob-drop-on");
                $(".masonry-loader .btn").addClass("disabled");
            }
        }
        $scope.resetCategoryFilter = function() {
            $scope.questions = $scope.overAllQuestionSet;
            $scope.categoryFilterArray = [];
            $scope.typeFiltered = [];
            if ($scope.typeFilterSwitch) {
                for (var f = 0; f < $scope.overAllQuestionSet.length; f++) {
                    if ($scope.selectedQuestionType == $scope.overAllQuestionSet[f].type) {
                        $scope.typeFiltered.push($scope.overAllQuestionSet[f]);
                    }
                }
                $scope.questions = $scope.typeFiltered;
            }

            $scope.selectedFilter = [];
            $scope.limit = JSON.parse($attrs.json).chunkSize;
            $(".masonry-loader .btn").removeClass("disabled");
            $timeout(function() {
                appyrtk.masonry.masonry('reloadItems');
                appyrtk.masonry.masonry();
            }, 100);
            $(".filter-drop .after-drop").slideUp();
            $(".filter-drop").removeClass("active mob-drop-on");
        }
        //Category Filter Ends Here ---->




        //Dynamic Modal Window Data
        $scope.openModal = function(q) {
            $scope.modalNumber = q;
            $scope.colorSeq;
            $scope.modalQuestion = $scope.questions[$scope.modalNumber].question;
            $scope.modalUsername = $scope.questions[$scope.modalNumber].user[0].name;
            $scope.modalUserAvatar = $scope.questions[$scope.modalNumber].user[0].avatar_url;
            $scope.modalAnswer = $scope.questions[$scope.modalNumber].answer;
            $scope.modalCategory = $scope.questions[$scope.modalNumber].categoryName;
            $scope.modalAlternateTitle = $scope.questions[$scope.modalNumber].alternateTitle;
            $scope.modalImage = $scope.questions[$scope.modalNumber].heroimage;
            $scope.modalVideo = $scope.questions[$scope.modalNumber].videourl;
            $scope.modalHotness = $scope.questions[$scope.modalNumber].stats[0].hotness;
            $scope.modalType = $scope.questions[$scope.modalNumber].type;

            //Color Pattern for Modal Window
            $scope.colorPattern = $scope.modalNumber % 4;
            if ($scope.colorPattern == 0) {
                $scope.colorSeq = 4;
            } else if ($scope.colorPattern == 1) {
                $scope.colorSeq = 1;
            } else if ($scope.colorPattern == 2) {
                $scope.colorSeq = 2;
            } else if ($scope.colorPattern == 3) {
                $scope.colorSeq = 3;
            }

            //get twitter share URL
            $scope.twitterShareURL = 'https://twitter.com/intent/tweet?url=' +
                encodeURIComponent($scope.questions[$scope.modalNumber].alternateUrl) +
                '&text=' + encodeURIComponent($scope.questions[$scope.modalNumber].question);
            $scope.facebookShareURL = 'http://www.facebook.com/sharer.php?u=' +
                encodeURIComponent($scope.questions[$scope.modalNumber].alternateUrl) +
                '&quote=' + encodeURIComponent($scope.questions[$scope.modalNumber].question);

            //SEO Accommodation
            $scope.alternateUrl = $scope.questions[$scope.modalNumber].alternateUrl;

            $scope.newURLPath = $scope.alternateUrl.substring($scope.alternateUrl.length, $scope.alternateUrl.indexOf("/", 8));


            $scope.newMetaTitle = $scope.questions[$scope.modalNumber].alternateTitle || $scope.modalQuestion.substring(0, 50);
            $scope.newMetaDescription = $scope.questions[$scope.modalNumber].alternateDescription || $scope.modalAnswer.substring(0, 155);
            //$scope.newMetaDescription = $($scope.newMetaDescription).text();
            $scope.newMetaKeywordsArray = $scope.questions[$scope.modalNumber].tags;
            $scope.newMetaKeywords = $scope.newMetaKeywordsArray.join(", ");
            $scope.questionID = $scope.questions[$scope.modalNumber].questionId;

            //Assigning Values through DOM Manipulation
            $('.grid-item .plus-red-icon').attr("href", '');
            $("meta[name='description']").attr("content", $scope.newMetaDescription);
            $("meta[property='og:description']").attr("content", $scope.newMetaDescription);
            $("meta[property='og:title']").attr("content", $scope.newMetaTitle);
            $("meta[name='keywords']").attr("content", $scope.newMetaKeywords);
            $("title").text($scope.newMetaTitle);
            $scope.newURL = window.location.origin + $scope.newURLPath;
            $scope.popupState = {
                'stateid': Math.random().toString() + '~' + $scope.questionID
            };
            if ($scope.alternateUrl.indexOf("/", 8) != -1) {
                window.history.pushState($scope.popupState, '', $scope.newURL);
            }
            appyrtk.overlay(".ovr-view-question");
            event.preventDefault();
        }
        //Dynamic Modal Window Data Ends Here

        //Reset Modal Window  - SEO DATA
        $scope.resetPopupClose = function() {
            //Assigning Values through DOM Manipulation
            $("meta[name='description']").attr("content", $scope.originalMetaDescription);
            $("meta[property='og:description']").attr("content", $scope.originalMetaDescription);
            $("meta[property='og:title']").attr("content", $scope.originalTitle);
            $("meta[name='keywords']").attr("content", $scope.originalMetaKeywords);
            $("title").text($scope.originalTitle);
            $scope.popupState = {
                'stateid': Math.random().toString() + '~' + $scope.questionID
            };
            window.history.pushState($scope.popupState, '', $scope.originalURL);

        }
        //Reset Modal Window  - SEO DATA --ENDS HERE


        //TRIGGER MODAL FUNCTION
        $rootScope.$on("CallParentMethod", function(evt, e) {
            if ($scope.pinnedQuestionStatus) {
                if (e < $scope.pinnedQuestionPosition) {
                    e = e + 1;
                } else if (e == $scope.pinnedQuestionPosition) {
                    e = 0;
                }
            }
            $scope.openModal(e);
        });
        //TRIGGER MODAL FUNCTION Ends Here


        //VIEW ALL TRIGGER INITIALIZED
        $rootScope.$on("ViewAll", function(evt) {
            $scope.viewAllQuestions();
        });



        //LOAD MORE
        $scope.loadMoreQuestions = function() {
            if ($scope.categoryFilterArray.length == 0) {
                $scope.limit = $scope.limit + 20;
                $timeout(function() {
                    appyrtk.masonry.masonry('reloadItems');
                    appyrtk.masonry.masonry();
                    $('.questionfeed .icon.plus-red-icon').on('click', function(event) {
                        event.preventDefault();
                    });
                }, 100);
                if ($scope.limit >= $scope.questions.length) {
                    $(".masonry-loader .btn").addClass("disabled");
                }
            }
        }
        //LOAD MORE ENDS HERE



        //VIEW ALL
        $scope.viewAllQuestions = function() {
            $timeout(function() {
                appyrtk.masonry.masonry('reloadItems');
                appyrtk.masonry.masonry();
            }, 100);
            var scrollTop = 800 // For example
            $('html, body').animate({
                scrollTop: scrollTop
            }, 'slow');
            $('.after-search').fadeOut();
        }
        //VIEW ALL ENDS HERE


        //social sharing - Twitter
        $scope.socialShareTwitter = function(questionItem) {
            var url = 'https://twitter.com/intent/tweet?url=' +
                encodeURIComponent(questionItem.alternateUrl) +
                '&text=' + encodeURIComponent(questionItem.question);
            if ($scope.twitterUsername) {
                url = url + '&via=' + $scope.twitterUsername;
            }
            if ($scope.twitterHashtags) {
                url = url + '&hashtags=' + $scope.twitterHashtags;
            }
            window.open(url, 'twitter', 'menubar=no,toolbar=no,resizable=yes,scrollbars=yes,height=600,width=600');
        }

        $scope.socialShareTwitterModal = function() {
            var url = $scope.twitterShareURL;
            if ($scope.twitterUsername) {
                url = url + '&via=' + $scope.twitterUsername;
            }
            if ($scope.twitterHashtags) {
                url = url + '&hashtags=' + $scope.twitterHashtags;
            }
            window.open(url, 'twitter', 'menubar=no,toolbar=no,resizable=yes,scrollbars=yes,height=600,width=600');
        }

        //social share - Facebook
        $scope.socialShareFacebook = function(questionItem) {
            var url = 'http://www.facebook.com/sharer.php?u=' +
                encodeURIComponent(questionItem.alternateUrl) +
                '&quote=' + encodeURIComponent(questionItem.question);
            window.open(url, 'facebook', 'menubar=no,toolbar=no,resizable=yes,scrollbars=yes,height=600,width=600');
        }

        $scope.socialShareFacebookModal = function() {
            var url = $scope.facebookShareURL;
            window.open(url, 'facebook', 'menubar=no,toolbar=no,resizable=yes,scrollbars=yes,height=600,width=600');
        }


        //Initialization of QuestionFeed
        $scope.getQuestionsFeed();

    }
]);