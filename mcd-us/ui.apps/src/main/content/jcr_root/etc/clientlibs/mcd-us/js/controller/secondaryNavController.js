app.controller('secondaryNavController', [
		'$scope', '$window', '$cookies', '$location'
        , function ($scope, $window, $cookies, $location) {
        if (localStorage.getItem("navActiveIndex") == null) {
            $scope.microNavActive = localStorage.setItem("navActiveIndex", 0);
        }
        if (localStorage.getItem("mobNavActiveIndex") == null) {
            $scope.mobMicroNavActive = localStorage.setItem("mobNavActiveIndex", 0);
        }
        if ($('.header-microsite').length > 0) {
            $('header > nav.navbar').addClass('header-nav-relative');
        }
        $scope.microNavActive = localStorage.getItem("navActiveIndex");
        $scope.mobMicroNavActive = localStorage.getItem("mobNavActiveIndex");
        // Method used to redirect the page from the cookie			
        $scope.init = function () {
            if ($cookies.get("microSiteDefaultlanguage")) {
                var actualPageUrl;
                var pageUrl = $location.path().split('.')[0];
                if (pageUrl.indexOf('/content/') >= 0) {
                    actualPageUrl = pageUrl;
                }
                else {
                    actualPageUrl = "/content" + pageUrl
                }
                var selectedLanguagecookie = $cookies.get("microSiteDefaultlanguage").replace(new RegExp('%2F', 'g'), "/");
                var cookievaluearr = selectedLanguagecookie.split("/");
                var pageurlarr = actualPageUrl.split("/");
                var micrositepath = ""
                if (actualPageUrl != selectedLanguagecookie && cookievaluearr.length == pageurlarr.length) {
                    for (i = 1; i < cookievaluearr.length - 1; i++) {
                        micrositepath = micrositepath + "/" + cookievaluearr[i];
                    }
                    if (actualPageUrl.indexOf(micrositepath) >= 0) {
                        $window.open(selectedLanguagecookie + '.html', '_self');
                    }
                }
            }
        };
        // Method used to redirect microsite page
        $scope.redirectToLink = function (link, $event) {
            var index = $event.target.id
            var newTabSelf = $event.target.classList.contains('_self');
            var newTabBlank = $event.target.classList.contains('_blank');
            if (newTabBlank) {
                $window.open(link, '_blank');
            }
            else {
                localStorage.setItem("navActiveIndex", index);
                $window.open(link, '_self');
            }
        };
        $scope.mredirectToLink = function (link, $event) {
            var index = $event.target.id
            var newTabSelf = $event.target.classList.contains('_self');
            var newTabBlank = $event.target.classList.contains('_blank');
            if (newTabBlank) {
                $window.open(link, '_blank');
            }
            else {
                localStorage.setItem("mobNavActiveIndex", index);
                $window.open(link, '_self');
            }
        };
        // Method used set to set the cookie of microsite selected language.
        $scope.setMicrositeredirectToLink = function (cookievalue, link, $event) {
            if (cookievalue.length > 0) {
                var res = cookievalue.split("/");
                var pagePath = $location.path();
                var cookiePath = "";
                if (pagePath.includes("/content/")) {
                    for (var i = 1; i < res.length - 1; i++) {
                        cookiePath = cookiePath + "/" + res[i];
                    }
                }
                else {
                    for (var j = 2; j < res.length - 1; j++) {
                        cookiePath = cookiePath + "/" + res[j];
                    }
                }
                //var encodeCookieValue =  encodeURIComponent(cookievalue);
                $cookies.put("microSiteDefaultlanguage", cookievalue, {
                    path: cookiePath
                });
            }
            var newTab = $event.target.classList.contains('true');
            var index = $event.target.id;
            localStorage.setItem("navActiveIndex", index);
            if (newTab) {
                $window.open(link, '_blank');
            }
            else $window.open(link, '_self');
        };
        $scope.setMicrositeDefaultLanguageCookie = function (cookievalue) {
                if (cookievalue.length > 0) {
                    var res = cookievalue.split("/");
                    var cookiePath = "";
                    for (var i = 1; i < res.length; i++) {
                        cookiePath = cookiePath + "/" + res[i];
                    }
                    $cookies.put("getMicroSiteDefaultlanguage", cookievalue, {
                        path: cookiePath
                    });
                }
            }
            //function when secondary navigation dropdown close
        function closeSecondaryNavigationDD() {
            //$("#mSecondaryNavigation").modal("hide");
            $(".mobile-nav").removeClass("open");
            $(".mobile-nav > button").attr("aria-expanded", "false");
            $(".mobile-nav .mobile-nav-items").removeClass("in");
            $(".mobile-nav .mobile-nav-items").attr("display", "none");
            $(".mobile-nav .mobile-nav-items").attr("aria-expanded", "false");
        }
        //function on drop click first list active
        function setFocusDropDownAction() {
            $('#msdlanguageBox').on('click', function () {
                setTimeout(function () {
                    $('#msdlanguage1').focus();
                }, 10);
            });
            $('#msmlanguageBox').on('click', function () {
                setTimeout(function () {
                    if (!$('#msmlanguageBox').hasClass('collapsed')) {
                        $('#msmlangauge1').focus();
                    }
                }, 10);
            });
        }

        function mobileLanguageToggleCloseAction() {
            $('#cmhLanguageToggler').removeClass('in');
            $('#msmlanguageBox').addClass('collapsed');
        }

        function desktopLanguageToggleCloseAction() {
            $('.language-toggle').removeClass("open");
            $('#msdlanguageBox').attr("aria-expanded", "false");
            $('#languageItems').attr("aria-expanded", "false");
        }
        //ADA for dropdown    
        $('.mobile-nav').find(".dropdown-menu li:last-child a").keydown(function (e) {
            setTimeout(function () {
                $('#mSecondaryNavigation #0').focus();
            }, 10);
        });
        //function click action on secondary Navigation
        function secondaryDropdownClickAction() {
            $('#msmSecondNav').on('click', function () {
                //$('.modal-backdrop ').hide();
                setTimeout(function () {
                    $('#mSecondaryNavigation #0').focus();
                }, 10);
                //$('#mSecondaryNavigation').modal('show');
                if (!$('#msmlanguageBox').hasClass('collapsed')) {
                    $('#cmhLanguageToggler').removeClass('in');
                    $('#msmlanguageBox').addClass('collapsed');
                }
            });
            $(".mobile-nav.dropdown, .herocomponent").focusin(function () {
                mobileLanguageToggleCloseAction();
                desktopLanguageToggleCloseAction()
            });
            $('.mobile-nav').on('click', '#mSecondaryNavigation li a', function () {
                closeSecondaryNavigationDD();
            });
            $('.mobile-nav').on('click', function () {
                console.log("arun test");
                $("main").children("div").attr("aria-hidden", "true");
                $('.mobile-nav').parents('div').removeAttr("aria-hidden");
                $('header, footer, .header-title, .mobile-lang-toggler').attr("aria-hidden", "true");
            });
            $('.mobile-nav-items li a').on('click', function () {
                $("main").children("div").removeAttr("aria-hidden", "true");
                $('.mobile-nav').parents('div').removeAttr("aria-hidden");
                $('head, header, footer, .header-title, .mobile-lang-toggler').removeAttr("aria-hidden", "true");
                closeSecondaryNavigationDD();
            })
        }
        // function convert string first charcter in capital letter
        function upperFirstAll(str) {
            str = str.toLowerCase().replace(/^[\u00C0-\u1FFF\u2C00-\uD7FF\w]|\s[\u00C0-\u1FFF\u2C00-\uD7FF\w]/g, function (letter) {
                return letter.toUpperCase();
            });
            return str;
        }
        // function add aria label in active link
        function addAriaLabelForActiveLink() {
            $('#dSecondaryNavigation li, #mSecondaryNavigation li').each(function (index) {
                var current_elm = $(this).find('a');
                var current_elm_text = $(this).find('a').text();
                var current_elm_uText = upperFirstAll(current_elm_text);
                if (current_elm.hasClass('active')) {
                    current_elm.attr('aria-label', current_elm_uText + ' Selected');
                }
            });
        }
        //function for set active text on dropdown button
        function activeDropDownListText() {
            var mActiveNavText = $("#mSecondaryNavigation li a.active").eq(0).text();
            $('#msmSecondNav .button-text').text(mActiveNavText);
        }
        //function to check smartapp banner and cookebox and set top margin in main box
        var snCookieBoxHeight;
        var snSmartBannerHeight;
        var snTopmenuHeight;
        var scrollHeight;

        function snCookieSmart() {
            if ($("#cookieMessage").is(':visible') && $("#smartbanner").is(':visible')) {
                snCookieBoxHeight = $("#cookieMessage").outerHeight();
                snSmartBannerHeight = $("#smartbanner").outerHeight();
                snTopmenuHeight = $(".navbar-header-wrapper .navbar-header").outerHeight();
                $("#maincontent").css("margin-top", ((snSmartBannerHeight + snCookieBoxHeight) - 24) + 'px');
                scrollHeight = snCookieBoxHeight + snSmartBannerHeight + snTopmenuHeight;
            }
            else if ($("#cookieMessage").is(':visible')) {
                snCookieBoxHeight = $("#cookieMessage").outerHeight();
                snTopmenuHeight = $(".navbar-header-wrapper .navbar-header").outerHeight();
                $("#maincontent").css("margin-top", (snCookieBoxHeight - 24) + 'px');
                scrollHeight = snCookieBoxHeight + snTopmenuHeight;
            }
            else if ($("#smartbanner").is(':visible')) {
                snSmartBannerHeight = $("#smartbanner").outerHeight();
                snTopmenuHeight = $(".navbar-header-wrapper .navbar-header").outerHeight();
                $("#maincontent").css("margin-top", snSmartBannerHeight + 'px');
                scrollHeight = snSmartBannerHeight + snTopmenuHeight;
            }
            else {
                $("#maincontent").css("margin-top", 0);
                scrollHeight = snTopmenuHeight;
            }
            $("body").css("margin-top", 0);
        }

        function snCookieMessageClose() {
            $('#cookieMessage').hide();
            snCookieSmart();
        }

        function snSmartBannerClose() {
            $('#smartbanner').hide();
            snCookieSmart();
        }
        $('#cookieMessage .cookie-message-close').on('click', function () {
            snCookieMessageClose();
        });
        $('#smartbanner .sb-close').on('click', function () {
            snSmartBannerClose();
        });
        
        //on escape key press close dropdown 
        $(document).keyup(function (e) {
            if (e.keyCode == 27) {
                closeSecondaryNavigationDD();
                $("main").children("div").removeAttr("aria-hidden", "true");
                $('.mobile-nav').parents('div').removeAttr("aria-hidden");
                $('head, header, footer, .header-title, .mobile-lang-toggler').removeAttr("aria-hidden", "true");
            }
        });
        
        $('body').click(function (evt) {
            if (evt.target.id == "msmSecondNav") {
                return;
            }
            if ($(evt.target).closest('#msmSecondNav').length) {
                return;
            }
            closeSecondaryNavigationDD();
        });
        
        $(window).scroll(function () {
            var scroll = $(window).scrollTop();
            var microsite_header_height = $('.component-microsite-header:visible').height();
            $('#component-microsite-header-dummy').height(microsite_header_height);
            if (scroll >= scrollHeight) {
                $(".component-microsite-header").addClass("headerFixed");
                $('#component-microsite-header-dummy').removeClass('hide');
            }
            else {
                $(".component-microsite-header").removeClass("headerFixed");
                $('#component-microsite-header-dummy').addClass('hide');
            }
        });
        $(window).load(function () {
            activeDropDownListText();
            addAriaLabelForActiveLink();
            if (($('.secondaryNavigation').length > 0) && ($(window).width() < 767)) {
                snCookieSmart();
            }
            else {
                $("body").css("margin-top", 0);
            }
        });
        // Call function on dom ready
        setFocusDropDownAction();
        secondaryDropdownClickAction();
        
}]);