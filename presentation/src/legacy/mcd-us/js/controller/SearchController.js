/* global angular, config */
/* eslint no-use-before-define: ["error", { "functions": false }] */
/* eslint func-names: ["error", "never"] */

(function () {

  angular
    .module('usr')
    .controller('searchController', SearchController);

  SearchController.$inject = ['$scope', '$http'];
  function SearchController($scope, $http) {
    $scope.searchresults = {};
    const params = $('#search-params');
    const q = decodeURIComponent(params.data('q'));
    let limit;
    $scope.totalHits = 0;
    $scope.pageCount = 0;
    $scope.hits = [];
    $scope.searchInputVal = params[0].getAttribute('data-q');
    $scope.searchSuggestion = false;
    $scope.searchInputCloseBtn = false;
    $scope.widget = $('[data-widget="accessible-autocomplete"]');
    $scope.input = $scope.widget.find('#search');
    $scope.pageType = undefined;
    $scope.isSearchInputFocus = false;
    $scope.filters = [
      {
        type: 'All'
      },
      {
        type: 'Product'
      },
      {
        type: 'Menu'
      },
      {
        type: 'Deals'
      }
    ];
    const defaultImagePath = $('.defaultImagePath').text();
    const hiddenPagePath = $('.hiddenPagePath').text();
    const newsSearchText = $('.newsSearchText').text();
    const resultText = $('.resultText').text();
    let descriptionCutOff = $('.description_cutoff').text();
    if (descriptionCutOff == null) {
      descriptionCutOff = 150;
    }
    $.more.setDefaults({ length: parseInt(descriptionCutOff, 10), moreText: '[+]', lessText: '[-]' });
    $scope.init = function () {
      $scope.loadSearchResults(true);
      angular.element('.removeMore').more();
      $scope.showNewsSearchText();
      $scope.pageType = 'All';
    };

    $scope.searchInputFocus = function (focus) {
      $scope.isSearchInputFocus = focus;
    };

    $scope.matchItems = function (input) {
      const reg = new RegExp(input.split('').join('\\w*').replace(/\W/, ''), 'i');
      const items = ['Chicken McNuggets',
        'Artisan Grilled Chicken Sandwich',
        'Buttermilk Crispy Chicken Sandwich',
        'Chicken Tenders',
        'McChicken'];
      return items.filter(item => {
        if (item.match(reg)) {
          return item;
        }
        return '';
      });
    };

    $scope.clearSelected = function () {
      const results = $scope.widget.find('#results');
      $scope.input.attr('aria-activedescendant', '');
      results.find('[aria-selected="true"]').attr('aria-selected', 'false').attr('id', '');
    };

    $scope.checkTabPress = function () {
      $scope.searchSuggestion = false;
      $scope.searchInputVal = $scope.searchInputBackVal.length > 0 ? $scope.searchInputBackVal : $scope.searchInputVal;
      $scope.searchInputBackVal = '';
    };

    $scope.changeInput = function (event) {
      if (event.keyCode === 27) {
        $scope.searchSuggestion = false;
        return;
      }
      if (event.keyCode === 13) {
        $scope.searchSuggestion = false;
        $scope.loadSearchRes($scope.searchInputVal);
        return;
      }
      if ([38, 40].indexOf(event.keyCode) !== -1) {
        const $listItems = $('.mcd-search__suggestion--section li');
        const $selected = $listItems.filter('.selected');
        let currentElm;
        $listItems.removeClass('selected');
        if (event.keyCode === 40) {
          if (!$selected.length || $selected.is(':last-child')) {
            currentElm = $listItems.eq(0);
          }
          else {
            currentElm = $selected.next();
          }
        }
        else if (event.keyCode === 38) {
          if (!$selected.length || $selected.is(':first-child')) {
            currentElm = $listItems.last();
          }
          else {
            currentElm = $selected.prev();
          }
        }
        if (currentElm && currentElm.length > 0) {
          $scope.clearSelected();
          currentElm.addClass('selected');
          currentElm.attr('aria-selected', 'true').attr('id', 'selectedOption');
          $scope.input.attr('aria-activedescendant', 'selectedOption');
          $scope.input.attr('aria-expanded', 'true');
          $scope.searchInputVal = currentElm[0].innerText;
          $scope.searchInputBackVal = '';
        }
        return;
      }
      if (event.target.value.length > 0) {
        $scope.autoCompleteResult = $scope.matchItems(event.target.value);
        if ($scope.autoCompleteResult.length > 0) {
          $scope.searchSuggestion = true;
          const [backgroundInputData] = $scope.autoCompleteResult;
          $scope.searchInputBackVal = event.target.value + backgroundInputData.substring(event.target.value.length);
        }
        else {
          $scope.searchInputBackVal = '';
        }
        $scope.searchInputCloseBtn = true;
      }
      else {
        $scope.searchInputBackVal = '';
        $scope.searchInputCloseBtn = false;
        $scope.searchSuggestion = false;
      }
    };

    $scope.searchInputCloseBtnCick = function () {
      $scope.searchInputVal = '';
      $scope.searchInputBackVal = '';
      $scope.noItemMsg = false;
      $scope.searchInputCloseBtn = false;
      $scope.searchSuggestion = false;
      $scope.input.attr('aria-expanded', 'false');
    };

    $scope.selectPredictedText = function (event) {
      $scope.searchInputVal = event.target.innerText;
      $scope.searchInputBackVal = '';
      $scope.searchSuggestion = false;
      $scope.input.attr('aria-expanded', 'true');
    };

    $scope.loadSearchRes = function (val) {
      if (val && val.length) {
        const url = window.location.toString();
        if (url.indexOf('q') !== -1) {
          window.location = url.replace(`${url.substring(url.indexOf('q'))}`, `q=${val}`);
        }
        else {
          window.location = `${url}?q=${val}`;
        }
      }
    };

    $scope.setupCarousel = function () {
      const filter = {
        itemCount: $('.mcd-filter__carousel__item').length,
        itemWidth: $('.mcd-filter__carousel__item').first().width()
      };
      for (let i = 0; i < filter.itemCount; i += 1) {
        $('.mcd-filter__carousel__item')[i].style.width = `calc(100% / ${filter.itemCount})`;
      }
      filter.itemWidth = $('.mcd-filter__carousel__item').first().width();
      $('.mcd-filter__horizontal-line').css('width', `${(filter.itemWidth * filter.itemCount) / 4}rem`);
    };

    $scope.carousalItemClick = function (event) {
      $('.mcd-filter__horizontal-line--selected').css('transform', `translateX(${(parseInt(event.target.getAttribute('key'), 10) *
        $('.mcd-filter__carousel__item').width()) / 16}rem)`);
      $scope.loadMoreButton = false;
      if (event.target.innerText.indexOf('(') !== -1) {
        $scope.pageType = event.target.innerText.substring(0, event.target.innerText.indexOf('(') - 1);
      }
      else {
        $scope.pageType = event.target.innerText;
      }
      $scope.loadSearchResults(true);
    };

    $scope.loadSearchResults = function (isPageInit) {

      if (!(isPageInit)) {
        $scope.pageCount += 1;
      }
      else {
        $scope.hits = [];
        $scope.pageCount = 0;
      }

      limit = params.data('limit');
      if (q !== '' && q !== 'undefined') {
        $http.get(`/services/mcd/searchResults.json?country=${config.get('country')}&language=${config.get('lang')}&q=${q}&limit=${limit}&page=${$scope.pageCount}&pageType=${$scope.pageType}&hidePage=${hiddenPagePath}`)
          .then(response => {
            $scope.searchresults = response.data;
            if ($scope.searchresults && $scope.searchresults.length !== 0) {
              $scope.totalHits = $scope.searchresults.totalHits;
              if ($scope.searchresults.hits.length > 0) {
                $scope.setupCarousel();
                $scope.loadMoreButton = true;
                for (let count = 0; count < $scope.searchresults.hits.length; count += 1) {
                  $scope.hits.push($scope.searchresults.hits[count]);
                }
                $('.searchtresulttitle').show();
                $('.nosearchtext').hide();
                if ($scope.searchresults.totalHits <= limit) {
                  $scope.disableLoadMore();
                }
                else {
                  $scope.enableLoadMore();
                }
              }
              else {
                // if no result found for the search query entered, show no search result error Msg.
                $('.noMoreResults').show();
                $scope.disableLoadMore();
                $scope.loadMoreButton = false;
              }
            }
            else {
              $scope.totalHits = 0;
              $('.noMoreResults').hide();
              $scope.disableLoadMore();
            }
            $scope.searchResultText();
            if ((isPageInit)) {
              $('.finalResultText').focus();
            }
          });
      }
      else {
        // If no search query entered, show no search query error Msg.
        $('.searchtresulttitle').hide();
        $('.nosearchtext').show();
        $('.noMoreResults').hide();
        $scope.disableLoadMore();

      }
      angular.element('.removeMore').more();
    };

    $scope.showThumbnail = function (element) {
      if (element) {
        if (element.thumbnail) {
          return element.thumbnail;
        }
        return defaultImagePath;

      }
      return defaultImagePath;


    };

    $scope.enableLoadMore = function () {
      $('.noMoreResults').hide();
      $('.load-more-btn').removeAttr('disabled');
      $('.load-more-btn').removeClass('search_deactivate');
      $('.fa-chevron-down').removeClass('search_deactivate');
      $('.load-more-btn').show();
      $('.fa-chevron-down').show();
    };

    $scope.disableLoadMore = function () {
      $('.load-more-btn').attr('disabled', 'disabled');
      $('.load-more-btn').addClass('search_deactivate');
      $('.fa-chevron-down').addClass('search_deactivate');
      $('.load-more-btn').hide();
      $('.fa-chevron-down').hide();
    };

    $scope.showNewsSearchText = function () {

      if (newsSearchText !== undefined && newsSearchText !== '') {
        const searchKeyword = $('.searchKeyword').text();
        const start = newsSearchText.indexOf('{');
        const end = newsSearchText.indexOf('}');
        const finalnewsSearchMessage = `${newsSearchText.substring(0, start)}<b>${searchKeyword}</b>${newsSearchText.substring(end + 1, newsSearchText.length)}`;
        const finalnewsSearchMessageForAda = newsSearchText.substring(0, start) + searchKeyword + newsSearchText.substring(end + 1, newsSearchText.length);
        $('.newsSearchFinalText').html(finalnewsSearchMessage);
        const newsUrl = $('.newsUrl').text();
        const searchNewsHref = `${newsUrl}/corporate/site-search?searchtext=${searchKeyword}&searchmode=anyword`;
        $('a.newsLink').attr('href', searchNewsHref);
        /* $("a.newsLink").attr("title", finalnewsSearchMessageForAda); */
        $('a.newsLink').attr('aria-label', finalnewsSearchMessageForAda);
      }

    };

    $scope.searchResultText = function () {
      if (resultText !== undefined && resultText !== '') {
        const totalHitsText = `<span class='bold'>${$scope.totalHits}</span>`;
        const queryTextToAppend = `"${q}"`;
        const finalResultText = resultText.replace('{0}', totalHitsText).replace('{1}', queryTextToAppend);
        $('.finalResultText').html(finalResultText);
      }
    };

    $scope.$on('ngRepeatFinished', () => {
      angular.element('.removeMore').more();
      setTimeout(() => {
        const scrollTop = $(window).scrollTop();
        const elementOffset = $('.first-of-set').last().offset().top;
        const distance = (elementOffset - scrollTop);

        if (distance < 183) {
          $(window).scrollTop(scrollTop - 183);
        }
        if ($scope.pageCount > 0) {
          $('.first-of-set').last().find(':tabbable').first()
            .focus();
        }

      }, 400);
    });
  }
}());

(function () {

  angular
    .module('usr')
    .directive('onFinishRender', OnFinishRender);

  OnFinishRender.$inject = ['$timeout'];
  function OnFinishRender($timeout) {
    const directive = {
      link,
      restrict: 'A',
    };
    return directive;

    function link(scope) {
      if (scope.$last === true) {
        $timeout(() => {
          scope.$emit('ngRepeatFinished');
        });
      }
    }
  }
}());
