(function ($) {
    (function (window, document, $, undefined) {
        var appyrtk = {
            WINDOW_HEIGHT: $(window).height(),
            slider: null,
            WINDOW_WIDTH: $(window).width(),
            isMobile: false,
            isTouch: false,
            isTablet: false,
            resizeTimeoutID: null,
            $body: $("body"),
            masonry: null,
            filterApplied: false,
            counter: 0,
            detectDevice: function () {
                (function (a) {
                    if (/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino/i.test(a) || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0, 4))) {
                        appyrtk.isMobile = true;
                    }
                })(navigator.userAgent || navigator.vendor || window.opera);
                if (navigator.userAgent.match(/Android/i)
                    || navigator.userAgent.match(/webOS/i)
                    || navigator.userAgent.match(/iPhone/i)
                    || navigator.userAgent.match(/iPad/i)
                    || navigator.userAgent.match(/iPod/i)
                    || navigator.userAgent.match(/BlackBerry/i)
                    || navigator.userAgent.match(/Windows Phone/i)
                ) {
                    appyrtk.isTouch = true;
                    appyrtk.$body.addClass("touch");
                }
                else {
                    appyrtk.$body.addClass("no-touch");
                }
                appyrtk.isTablet = (!appyrtk.isMobile && appyrtk.isTouch);
            },
            _windowResize: function () {
            },
            resizeListener: function () {
                if (!appyrtk.isTouch) {
                    $(window).resize(function () {
                        clearTimeout(appyrtk.resizeTimeoutID);
                        appyrtk.resizeTimeoutID = setTimeout(appyrtk._windowResize, 500);
                    });
                }
                else {
                    window.addEventListener('orientationchange', function () {
                        setTimeout(function () {
                            //       appyrtk.masonryGridSystem();
                            //     appyrtk.sliderEventOnlyMobile();
                        }, 500);
                    });
                }
            },
            msIeVersion: function () {
                var ua = window.navigator.userAgent,
                    msie = ua.indexOf("MSIE ");
                if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./)) {
                    $("body").addClass("ie");
                }
                return false;
            },
            eventListeners: function () {
                var search = $('.search-with-drop');
                search.keydown(function () {
                    //$(this).find('.after-search').fadeIn(500);
                });
                //search.on("focusout", function () {
                //    $(this).find('.after-search').fadeOut(500);
                //});
                $('.after-search').click(function (event) {
                    $(this).show();
                    event.stopImmediatePropagation();
                });
                $(document).click(function () {
                    $('.after-search').fadeOut(500);
                    $(".filter-drop .after-drop").slideUp();
                    $(".filter-drop").removeClass("active mob-drop-on");

                });
                $('header .bars').click(function () {
                    $('header.mobile').toggleClass('active');
                    $('header.mobile .top-menu').fadeToggle();
                    $('body,html').toggleClass('no-ver-scroll')
                });
                $(window).scroll(function () {
                    if ($(window).scrollTop() >= $("header.mobile").outerHeight()) {
                        $('header').addClass('after-scroll');
                    }
                    else {
                        $('header').removeClass('after-scroll');
                    }
                });
                $('.filter-drop .selected-drop').click(function (e) {
                    $(".after-search").fadeOut();
                    $('.after-drop').slideToggle();
                    $(this).parent().toggleClass('active');
                    if ($(this).parent(".filter-drop").hasClass("mob-drop-on")) {
                        $(this).parent(".filter-drop").removeClass("mob-drop-on");
                    }
                    e.stopImmediatePropagation();
                });
                $(".after-drop").click(function (e) {
                    $(this).show()
                    e.stopImmediatePropagation();
                })
                $(".filter .fl-on-icon").click(function (e) {
                    var filter = $('.filter-drop');
                    filter.addClass('mob-drop-on');
                    // $('.filter-drop .selected-drop').trigger('click');
                    $('.after-drop').slideToggle();
                    filter.toggleClass('active');
                    e.stopImmediatePropagation();
                });
                $('.search-on-icon').click(function () {
                    $(this).parent().toggleClass('active');
                    $('.search-box .search .search-with-drop').fadeToggle();
                });
                $(".ovr-canvas .close-red-icon").click(function () {
                    var iframe = $(this).parents(".ovr-canvas").find("iframe");
                    if (iframe.length === 1) {
                        var src = iframe.attr("src");
                        iframe.attr("src", "");
                        iframe.attr("src", src);
                    }
                    $(".ovrlay,.ovr-canvas").fadeOut();
                    $('body,html').removeClass('no-ver-scroll');
                     $('body,html').removeClass('overlay-no-vertical');
                    $('.ovrlay').removeClass('ovr-white-bg');
                    appyrtk.masonry.masonry('reloadItems');
                    appyrtk.masonry.masonry();
                });
                $(document).on('click', '.masonry-box .m-text-box .plus-red-icon', function () {
                   // var html = $(this).parents(".grid-container").clone();
                   // $(".ovr-view-question .masonry-box").html(html);
                    $(".ovrlay").addClass('ovr-white-bg').fadeIn();
                    $(".ovr-view-question").fadeIn();
                    $('body').addClass('no-ver-scroll');
                    appyrtk.masonry.masonry('reloadItems');
                    appyrtk.masonry.masonry();
                });
                $(".ovrlay").click(function () {
                    $(".ovr-canvas:visible").find(".close-red-icon").trigger("click");
                });
                $(document).keyup(function (e) {
                    if (e.keyCode == 27) { // escape key maps to keycode `27`
                        $(".ovr-canvas:visible").find(".close-red-icon").trigger("click");
                        $('.after-search').fadeOut(500);
                        $(".filter-drop .after-drop").slideUp();
                        $(".filter-drop").removeClass("active");
                    }
                });
                $('.masonry-loader .move-to-top-icon').on('click', function () {
                    $('html,body').animate({
                        scrollTop: 0
                    }, 700);
                });
            },
            masanory: function () {
                appyrtk.masonry = $(".questionfeed .grid");
                appyrtk.masonry.imagesLoaded(function () {
                    // images have loaded
                    $(".spinner-cont").fadeOut();
                    $(".masonry-box .grid-item").show();
                    appyrtk.loadMore();
                    //  setTimeout(function () {
                    appyrtk.masonry.masonry({
                        itemSelector: '.grid-item',
                        columnWidth: '.grid-sizer'
                        // set the selectorType to use <ul> instead of inputs
                        // selectorType: 'list'
                    });
                    setTimeout(function () {
                        appyrtk.masonry.masonry('reloadItems');
                        appyrtk.masonry.masonry();
                    }, 500);
                    //  console.log("masanory timer")
                    //}, 300);
                    //console.log("masanory load")
                });
//                $("#apply-filter").click(function () {
//                    if ($(".drop-check input:checked").length === 0) {
//                        $(".masonry-box .grid-item").show();
//                        appyrtk.filterApplied = false;
//                        //appyrtk.masonry.masonry('reloadItems');
//                        //appyrtk.masonry.masonry();
//                        if (appyrtk.counter < 3) {
//                            $(".masonry-loader .btn").removeClass("disabled");
//                        }
//                    }
//                    else {
//                        $(".masonry-box .grid-item").hide();
//                        appyrtk.filterApplied = true;
//                        $(".masonry-loader .btn").addClass("disabled")
//                    }
//                    $(".drop-check input").each(function () {
//                        if ($(this).is(':checked')) {
//                            //console.log($(this).val());
//                            $(".masonry-box .grid-item." + $(this).val()).show();
//                            // appyrtk.masonry.masonry('reload')
//                            //       appyrtk.masonry.masonry();
//                        }
//                    });
//                    //console.log("reloaded");
//                    $(".filter-drop .after-drop").slideUp();
//                    $(".filter-drop").removeClass("active mob-drop-on");
//                    appyrtk.masonry.masonry('layout');
//                    appyrtk.masonry.masonry();
//                });
                $("#reset").click(function () {
                    $(".drop-check input").prop('checked', false);
                    $("#apply-filter").trigger("click");
                    //  appyrtk.masonry.masonry();
                });
            },
            overlay: function (elem) {
                $(".ovrlay").fadeIn();
                $(elem).fadeIn();
                $('body,html').addClass('no-ver-scroll');
                if(elem == '.ovr-ask-a-question'){
                    $('body,html').addClass('overlay-no-vertical');
                }
            },
            openRating: function () {
                $(".ovr-thank-you").hide();
                $(".ovr-rating").fadeIn();
            },
            loadMore: function () {
//                var clonedHtml = $(".masonry-box .grid-item").clone();
//                $('.masonry-loader .btn').click(function () {
//                    $('.masonry-loader .btn').addClass("disabled");
//                    clearTimeout(appyrtk.resizeTimeoutID);
//                    appyrtk.resizeTimeoutID = setTimeout(function () {
//                            if (appyrtk.counter < 3) {
//                                $(".masonry-box").append(clonedHtml);
//                                appyrtk.masonry.masonry('reloadItems');
//                                appyrtk.masonry.masonry();
//                                $('.masonry-loader .btn').removeClass("disabled");
//                                appyrtk.counter += 1;
//                            }
//                            if (appyrtk.counter === 3) {
//                                $('.masonry-loader .btn').addClass("disabled");
//                            }
//                        }
//                        , 100);
//                });
            },
            validation: function () {
                //     $('.ovr-ask-a-question').xfield_validator();
//                var form = $("#question-form").xfield_validator({
//                    validateOnId: 'submit',
//                    showErrorMessages: false,
//                    enableLastFieldFix: false,
//                    complete: function (formResponse) {
//                        if(formResponse.formStatus === false){
//
//                            $(".ovr-canvas.ovr-ask-a-question .validation-text").show();
//                        }
//
//                    }
//                });
//                form.submit(function (e) {
//
//                    if(grecaptcha.getResponse() === ""){
//                        $(".ovr-canvas.ovr-ask-a-question .validation-text").show();
//                        return false
//                    }
//                    $(".ovr-ask-a-question").hide();
//                    $(".ovr-thank-you").fadeIn();
//                    $(".ovr-canvas.ovr-ask-a-question .validation-text").hide();
//                    e.preventDefault();
//                });
            },
            androidInputFix: function () {
                if (/Android/.test(navigator.appVersion)) {
                    window.addEventListener("resize", function () {
                        if (document.activeElement.tagName == "INPUT" || document.activeElement.tagName == "TEXTAREA") {
                            if ($("body").hasClass("no-ver-scroll")) {
                                document.activeElement.scrollIntoView();
                            }
                        }
                    })
                }
            },
            init: function () {
                appyrtk.detectDevice();
                appyrtk.resizeListener();
                appyrtk.msIeVersion();
                appyrtk.eventListeners();
                appyrtk.validation();
                appyrtk.masanory();
                appyrtk.androidInputFix();
              // if this is a re-direct link from twitter --> show the thank you modal
              var twitterSuccessUrl=window.location.href;
              if (twitterSuccessUrl.indexOf('cnt=1&t=')!==-1) {
                var sPageURL = window.location.search.substring(1);
                var sURLVariables = sPageURL.split('&');
                var tStamp;
                for (var i = 0; i < sURLVariables.length; i++) {
                  var sParameterName = sURLVariables[i].split('=');
                  if (sParameterName[0] === 't') {
                    tStamp = parseInt(sParameterName[1]);
                  }
                }
                var currentTStamp = new Date().getTime();
                var lastFiveMin = 5 * 60 * 1000;
                // if last 5 min
                if (Math.abs(currentTStamp - tStamp) < (lastFiveMin)) {
                  $(".ovr-thank-you").show();
                }

              }
            }
        };
        window.appyrtk = appyrtk;
    })(window, document, jQuery);
    $(document).ready(function() {
        if ($('.questionsearch').length > 0) {
            $('body').addClass('yrtpApplication');
            $('main').css('margin-top', '0');
            $('footer').css('position', 'relative');


            $('.questionfeed .filter a').click(function(){
                $(this).siblings().removeClass('active');
				$(this).addClass('active');
            });
            $('.ovr-thank-you .ovr-btn-box input').click(function(){
                $('.ovrlay').hide();
                $('body').removeClass('no-ver-scroll');
                 $('body,html').removeClass('overlay-no-vertical');
            });


        }
        appyrtk.init();
    });
})(jQuery);




