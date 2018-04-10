 /*!
 * ProjectName
 * McDonalds Dot Com
 * http://www.reachcreative.com
 * @author Reach Creative
 * @version 1.0.5
 * Copyright 2017. MIT licensed.
 */

(function($, window, document, undefined) {
    'use strict';

    /**
     * Breakpoint object to reference same breakpoints as sass.
     * @type {{xxs: number, xs: number, sm: number, md: number, lg: number, xl: number}}
     */
    var breakpoints = {
        xxs: 320,
        xs: 375,
        sm: 768,
        md: 992,
        lg: 1200,
        xl: 1500
    };

      var rtlFlow=false,dir="ltr";
     if (window.location.href.indexOf("ar-") > -1){
        rtlFlow=true;
        dir="rtl";
    };

    /**
     * Copyright 2012, Digital Fusion
     * Licensed under the MIT license.
     * http://teamdf.com/jquery-plugins/license/
     *
     * @author Sam Sehnert
     * @desc A small plugin that checks whether elements are within
     *       the user visible viewport of a web browser.
     *       only accounts for vertical position, not horizontal.
     */
    var $w = $(window);
    var adaGenericText;
    $.fn.visible = function(partial, hidden, direction) {

        if (this.length < 1) {
            return;
        }

        var $t = this.length > 1 ? this.eq(0) : this,
            t = $t.get(0),
            vpWidth = $w.width(),
            vpHeight = $w.height(),
            direction = (direction) ? direction : 'both',
            clientSize = hidden === true ? t.offsetWidth * t.offsetHeight : true;

        if (typeof t.getBoundingClientRect === 'function') {

            // Use this native browser method, if available.
            var rec = t.getBoundingClientRect(),
                tViz = rec.top >= 0 && rec.top < vpHeight,
                bViz = rec.bottom > 0 && rec.bottom <= vpHeight,
                lViz = rec.left >= 0 && rec.left < vpWidth,
                rViz = rec.right > 0 && rec.right <= vpWidth,
                vVisible = partial ? tViz || bViz : tViz && bViz,
                hVisible = partial ? lViz || rViz : lViz && rViz;

            if (direction === 'both') {
                return clientSize && vVisible && hVisible;
            } else if (direction === 'vertical') {
                return clientSize && vVisible;
            } else if (direction === 'horizontal') {
                return clientSize && hVisible;
            }
        } else {

            var viewTop = $w.scrollTop(),
                viewBottom = viewTop + vpHeight,
                viewLeft = $w.scrollLeft(),
                viewRight = viewLeft + vpWidth,
                offset = $t.offset(),
                _top = offset.top,
                _bottom = _top + $t.height(),
                _left = offset.left,
                _right = _left + $t.width(),
                compareTop = partial === true ? _bottom : _top,
                compareBottom = partial === true ? _top : _bottom,
                compareLeft = partial === true ? _right : _left,
                compareRight = partial === true ? _left : _right;

            if (direction === 'both') {
                return !!clientSize && ((compareBottom <= viewBottom) && (compareTop >= viewTop)) && ((compareRight <= viewRight) && (compareLeft >= viewLeft));
            } else if (direction === 'vertical') {
                return !!clientSize && ((compareBottom <= viewBottom) && (compareTop >= viewTop));
            } else if (direction === 'horizontal') {
                return !!clientSize && ((compareRight <= viewRight) && (compareLeft >= viewLeft));
            }
        }
    };

    /**
     * Scroll to element methd.
     * @link http://lions-mark.com/jquery/scrollTo/
     * @param target
     * @param options
     * @param callback
     * @returns {*}
     */
    $.fn.scrollTo = function(target, options, callback) {
        if (typeof options == 'function' && arguments.length == 2) {
            callback = options;
            options = target;
        }
        var settings = $.extend({
            scrollTarget: target,
            offsetTop: 50,
            duration: 500,
            easing: 'swing'
        }, options);
        return this.each(function() {
            var scrollPane = $(this);
            var scrollTarget = (typeof settings.scrollTarget == 'number') ? settings.scrollTarget : $(settings.scrollTarget);
            var scrollY = (typeof scrollTarget == 'number') ? scrollTarget : scrollTarget.offset().top + scrollPane.scrollTop() - parseInt(settings.offsetTop);
            scrollPane.animate({
                scrollTop: scrollY
            }, parseInt(settings.duration), settings.easing, function() {
                if (typeof callback == 'function') {
                    callback.call(this);
                }
            });
        });
    };

    function initAdaGenericText() {
    	if($('#adaTextJson') && $('#adaTextJson').attr('data-ada-json'))	{
    		adaGenericText = JSON.parse($('#adaTextJson').attr('data-ada-json'));
    	}
    }

    /**
     * Initiates zoom animation based on a trigger element, the target, and the animation.
     *
     * @method initZoomAnim
     * @param {Object} triggerElem - the element that is hovered over
     * @param {Object} targetElem - the element to be zoomed
     * @param {string} animClass - the zoom animation class
     * @param {Object} parentElem - the parent of the animated element
     */
    function initZoomAnim(triggerElem, targetElem, animClass, parentElem) {

        function zoomInBg() {
            if (parentElem !== undefined) {
                $(this).closest(parentElem).find(targetElem).addClass(animClass);
            } else {
                $(targetElem).addClass(animClass);
            }
        }

        function zoomOutBg() {
            if (parentElem !== undefined) {
                $(this).closest(parentElem).find(targetElem).removeClass(animClass);
            } else {
                $(targetElem).removeClass(animClass);
            }
        }

        $(triggerElem).unbind('mouseenter mouseleave');
        $(triggerElem).hover(zoomInBg, zoomOutBg);
    }

    // tiny helper function to add breakpoints
    function getGridSize() {
        if (window.innerWidth < 768) {
            return 1;
        }
        return 3;
    }

    function playSlider(clickElem, slider) {
        /*$(clickElem).on('click', function(){
            if(!$(this).hasClass('paused')){
                // go to next slide immediately
                $(slider).flexslider('next');
                $(slider).flexslider('play');
                $(this).attr('aria-label','Play slider');
                //$(this).find('.visuallyhidden').html("click to pause");
                $( "<div role='alert' class='visuallyhidden'>slider playing</div>" ).appendTo( "body" );
            }
            else{
                $(slider).flexslider('pause');
                $(this).attr('aria-label','Pause slider');
                //$(this).find('.visuallyhidden').html("click to play");
                $( "<div role='alert' class='visuallyhidden'>slider paused</div>" ).appendTo( "body" );
            }
            $(this).toggleClass('paused');
        });*/

        $('.flex-play').click(function(e){
          if(!$(this).hasClass('paused')){
              // go to next slide immediately
              $(slider).flexslider('next');
              $(slider).flexslider('play');
              $(this).attr('aria-label',adaGenericText.clicktopausetext);
              //$(this).find('.visuallyhidden').html("click to pause");
              $( "<div role='alert' class='visuallyhidden'>"+adaGenericText.sliderplayingtext+"</div>" ).appendTo( "body" );
          }
          else{
              $(slider).flexslider('pause');
              $(this).attr('aria-label',adaGenericText.clicktoplaytext);
              //$(this).find('.visuallyhidden').html("click to play");
              $( "<div role='alert' class='visuallyhidden'>"+adaGenericText.sliderpausedtext+"</div>" ).appendTo( "body" );
          }
          $(this).toggleClass('paused');
        });
    }

    function createSliderButtons(sliderContainer){
        var htmlContent = '';

        //htmlContent += '<div class="cont-play-pause-buttons">';
        //htmlContent +=  '<button type="button" class="flex-play paused keep-outline"><i class="fa fa-play-circle fa-lg"><span class="visuallyhidden">click to pause</span></i></button>';
        htmlContent +=  '<button type="button" aria-label="'+adaGenericText.clicktopausetext+'" class="flex-play paused keep-outline"><i class="fa fa-play-circle fa-lg"></i></button>';
        //htmlContent += '</div>';

        //$(sliderContainer).append(htmlContent);
        $('.cont-play-pause-buttons').html(htmlContent);

        // create click events
        playSlider(sliderContainer + ' .flex-play', sliderContainer);
    }

    function initFlexsliders() {
        // flexsliders
       var rtlFlow=false,dir="ltr";
             if (window.location.href.indexOf("ar-") > -1){
                rtlFlow=true;
                dir="rtl";
            };
        $('.home-flexslider').css('background-color', '#000000');
        $('.home-flexslider').flexslider({
            animation: 'fade',
             rtl: rtlFlow,
            before: function(){
              $('.home-flexslider .flex-control-nav a').attr('aria-selected','false');
              $('.home-flexslider').find('.slides li').attr("aria-hidden", "false");
              $('.home-flexslider').find('.slides li').attr("tabindex", "0");
            },
            after: function(){
              $('.home-flexslider .flex-control-nav .flex-active').attr('aria-selected','true');
              $('.home-flexslider').find('.slides li').not('.flex-active-slide').attr("aria-hidden", "true");
              $('.home-flexslider').find('.slides li:not(.flex-active-slide)').attr("tabindex", "-1");
              //$('.home-flexslider .slides .flex-active-slide').focus();
              $('.home-flexslider .flex-control-nav > li').each(function(index){
                var theclickabledot = $(this).find('a');
                var slideContent = $('.home-flexslider .slides li').eq(index).find('.caption .large-headline-text .visuallyhidden').html();
                //console.log(slideContent);
                if(theclickabledot.hasClass('flex-active')){
                  //$(this).find('a').html("<span class='visuallyhidden'>slide number "+(index+1)+" active</span>");
                  $(this).find('a').html("<span class='visuallyhidden'>"+adaGenericText.activeslidetext+ "-" +slideContent+"</span>");
                }else{
                  //$(this).find('a').html("<span class='visuallyhidden'>go to slide number "+(index+1)+" </span>");
                  $(this).find('a').html("<span class='visuallyhidden'>"+adaGenericText.gotoslidetext+ "-" +slideContent+" </span>");
                }

              });

              $('.custom-paging li a').removeClass('flex-active');
              var customNavLiNum = $('.custom-paging li').eq($('.home-flexslider').data('flexslider').currentSlide);
              //var activeAdjusted = $('.custom-paging li').eq($('.home-flexslider').data('flexslider').currentSlide)+1;
              $('.home-flexslider .custom-paging li a').each(function(index){
                var slideContent = $('.home-flexslider .slides li').eq(index).find('.caption .large-headline-text .visuallyhidden').html();
                //$(this).html('<span class="visuallyhidden">go to slide '+(index+1)+'</span>')
                $(this).html("<span class='visuallyhidden'>"+adaGenericText.gotoslidetext+ "-" +slideContent+" </span>");
              });

              //$('.custom-paging li').eq($('.home-flexslider').data('flexslider').currentSlide);
              $(customNavLiNum).find('a').addClass('flex-active');
              //$(customNavLiNum).find('a').html('<span class="visuallyhidden">slide '+($('.home-flexslider').data('flexslider').currentSlide+1)+' active</span>');
              var activeSlideContent = $('.home-flexslider .slides li').eq($('.home-flexslider').data('flexslider').currentSlide).find('.caption h2 .visuallyhidden').html();
              $(customNavLiNum).find('a').html('<span class="visuallyhidden">'+adaGenericText.activeslidetext+ '-' +activeSlideContent+'</span>');

            },
            start: function(slider) {
                $('.home-flexslider').resize();
                $('.home-flexslider .flex-control-nav').css('z-index', '2');
                $('.home-flexslider .flex-control-nav li a').attr('tabindex','0');
                $('.home-flexslider .cont-play-pause-buttons').css('z-index', '12');
                    slider.css("direction",dir);

                $('.flex-control-nav > li').each(function(index){
                  var theclickabledot = $(this).find('a');
                  var slideContent = $('.home-flexslider .slides li').eq(index).find('.caption .large-headline-text .visuallyhidden').html();
                  //console.log(slideContent);
                  if(theclickabledot.hasClass('flex-active')){
                    //$(this).find('a').html("<span class='visuallyhidden'>slide number "+(index+1)+" active</span>");
                    $(this).find('a').html("<span class='visuallyhidden'>"+adaGenericText.activeslidetext+ "-" +slideContent+"</span>");
                  }else{
                    //$(this).find('a').html("<span class='visuallyhidden'>go to slide number "+(index+1)+" </span>");
                    $(this).find('a').html("<span class='visuallyhidden'>"+adaGenericText.gotoslidetext+ "-" +slideContent+" </span>");
                  }

                });

                var customNavLiNum = $('.home-flexslider .custom-paging li').eq($('.home-flexslider').data('flexslider').currentSlide);
                //$('.custom-paging li').eq($('.home-flexslider').data('flexslider').currentSlide);
                $(customNavLiNum).find('a').addClass('flex-active');
                //$(customNavLiNum).find('a').html('<span class="visuallyhidden">slide '+($('.home-flexslider').data('flexslider').currentSlide+1)+' active</span>');
                var activeSlideContent = $('.home-flexslider .slides li').eq($('.home-flexslider').data('flexslider').currentSlide).find('.caption h2 .visuallyhidden').html();
                $(customNavLiNum).find('a').html('<span class="visuallyhidden">'+adaGenericText.gotoslidetext+ '-' +activeSlideContent+'</span>');
            }
        });
        createSliderButtons('.home-flexslider');
        $('.home-flexslider .flex-prev').attr('title','visit previous slide');
        $('.home-flexslider .flex-next').attr('title','visit next slide');
        $('.flex-prev').attr('tabindex','0').addClass('keep-outline');
        $('.flex-next').attr('tabindex','0').addClass('keep-outline');

        $('.home-flexslider .flex-control-nav a').on('focus', function(e){
          if($(this).hasClass('flex-active')){
            $(this).attr('aria-selected','true');
          }
          else{
            $(this).attr('aria-selected','false');
          }
        });


        $('.social-flexslider').flexslider({
            slideshow: false,
            animation: 'slide',
            itemWidth: 10,
            itemMargin: 15,
            minItems: getGridSize(),
            maxItems: getGridSize()
        });

        /*
        if (Modernizr.mq('(max-width: 767px)')) {
            initZoomAnim('.slides .cont-btn a', '.bg-mobile', 'bglarge', '.flex-active-slide');
        } else {
            initZoomAnim('.slides .cont-btn a', '.flex-active-slide', 'bglarge');
        }*/

        $(window).on('resize', function() {

            // remove from dom and add back since flexslider does not have a destroy method
            var gridSize = getGridSize();
            $('.social-flexslider').remove();
            $('.social-flex-container').html(socialFlexContents);

            $('.social-flexslider').flexslider({
                slideshow: false,
                animation: 'slide',
                itemWidth: 10,
                itemMargin: 15,
                minItems: gridSize,
                maxItems: gridSize
            });

            /*
            if (Modernizr.mq('(max-width: 767px)')) {
                initZoomAnim('.slides .cont-btn a', '.bg-mobile', 'bglarge', '.flex-active-slide');
            } else {
                initZoomAnim('.slides .cont-btn a', '.flex-active-slide', 'bglarge');
            }*/
        });

    }

    function initComponentCarouselBackground() {

        $('.component-carousel .slides li').each(function() {
            var _dataMobile = $(this).attr('data-mobile');
            var _dataDesktop = $(this).attr('data-desktop');
            if (Modernizr.mq('(max-width: 767px)')) {
                $(this).css('background', _dataMobile);
            } else {
                $(this).css('background', _dataDesktop);
            }
            $(this).css('background-size', 'cover');
            $(this).css('background-position', '50% 0%');

        });

    }

    function initFancybox() {
        // home carousel video
        $('.trigger-carousel-video').fancybox({
            closeClick: false,
            topRatio: 0.5,
            leftRatio: 0.5,
            padding: [0, 15, 0, 15],
            width: 650,
            wrapCSS: 'carousel-video',
            tpl: {
                closeBtn: '<a aria-label="Close" role="button" class="fancybox-item fancybox-close keep-outline" href="javascript:;"><span class="visuallyhidden">close</span><span class="fa-stack fa-lg" aria-hidden="true"><i class="fa fa-circle fa-stack-2x"></i><i class="fa fa-times fa-stack-1x fa-inverse"></i></span></a>'
            },
            helpers: {
                title: null,
                overlay: {
                    closeClick: true,
                    locked: true
                }
            },
            afterShow: function() {
                setTimeout(function(){
                  $('.fancybox-inner').attr('tabindex',0).focus();
                  if(getDeviceSize() === 'desktop' && player.playVideo !== undefined  && $('#homeCarouselComponent').length > 0){
                	  player.playVideo();
                  }
                }, 6000);
              },
              afterClose: function() {
                 // Return focus to the element used to open the fancybox
                 $(this.element).focus();
                 if(player.playVideo !== undefined ){
                     player.stopVideo();
                 }
            }
        });
    }

    function initInputPlaceholders() {
        $('[placeholder]').focus(function() {
            var input = $(this);
            if (input.val() == input.attr('placeholder')) {
                input.val('');
                input.removeClass('placeholder');
            }
        }).blur(function() {
            var input = $(this);
            if (input.val() == '' || input.val() == input.attr('placeholder')) {
                input.addClass('placeholder');
                input.val(input.attr('placeholder'));
            }
        }).blur().parents('form').submit(function() {
            $(this).find('[placeholder]').each(function() {
                var input = $(this);
                if (input.val() == input.attr('placeholder')) {
                    input.val('');
                }
            });
        });
    }

    function initFadeinAnimation(showImmediately) {
        if (showImmediately === true) {
            $('.fadeinnow').each(function() {
                $(this).fadeTo(500, 1);
            });
        } else {
            $('.fadein').each(function() {
                var objMiddle = $(this).outerHeight() / 2;
                var bottomOfObject = $(this).offset().top + objMiddle;
                var bottomOfWindow = $(window).scrollTop() + $(window).height();

                if (bottomOfWindow > bottomOfObject) {
                    $(this).fadeTo(500, 1);
                }
            });
        }
    }

    function removeNavAbs() {
        if (! Modernizr.mq('(max-width: 767px)') && $('.navbar').hasClass('position-abs')) {
            $('.navbar').removeClass('position-abs');
            $('.navbar .navbar-header-wrapper').removeClass('position-abs');
        }
    }

    function initNavDropdown() {

    	$('.navbar a').on('click', function(event) {
            var $trigger = $('#bs-example-navbar-collapse-1');
            if ($trigger !== event.target && !$trigger.has(event.target).length) {
                $('#bs-example-navbar-collapse-1').collapse('hide');
            }

        });

        $('.megamenu ul.dropdown-menu').on('click', function(e) {

            var _elem = $(e.target);


            if (_elem.hasClass('dropdown-toggle') || _elem.hasClass('fa')) {
                _elem.closest('.input-group-btn').toggleClass('open');
            }

            if(_elem.hasClass('simple-white')){

              var _targetFade = _elem.closest('.parent-collapse').find('.collapse');

              _targetFade.fadeToggle('fast', function() {
                var textShow = _elem.attr('data-text-show');
                var textHide = _elem.attr('data-text-hide');
				_elem.toggleClass('opened');

                if(_targetFade.css('display') == 'block') {
                    _elem.html(textHide);
                }
                else {
                    _elem.html(textShow);
                }

                setTimeout(function(){ _elem.focus(); }, 200);

              });


            }


            if (_elem.hasClass('fa-close') || _elem.hasClass('fa-times')) {
                // do nothing
            } else {
                e.stopPropagation();
            }

        });


        $('.megamenu ul.dropdown-menu .component-locate a:not(.dropdown-toggle)').on('click', function() {
            var root = $(this).closest('.input-group-btn');
            root.removeClass('open');
        });



        var mobileOpenParent;
        var mobileOpenParentLink;

        $('.navbar .top-nav-collapse').on('show.bs.collapse', function(e) {
            commonNavScrollTop(e);
            $('.nav-mobile-right .hamburger').addClass('is-active');

        });

        $('.navbar .top-nav-collapse').on('shown.bs.collapse',function(e){
          //console.log('open');
          //var target = $(this);//console.log(target);
          //target.attr('tabindex','0').focus().removeAttr('tabindex');
          //target.find('ul.megamenu .nearest-mobile').attr('tabindex','0').focus();
          commonNavScrollTop(e);

        });

        $('.navbar .top-nav-collapse').on('hide.bs.collapse', function(e) {
            $('.navbar').removeClass('position-abs');
            $('.nav-mobile-right .hamburger').removeClass('is-active');
        });


        $('.navbar .megamenu .dropdown').on('shown.bs.dropdown', function(e) {

            customNavEventLocator(e);

            if (Modernizr.mq('(max-width: 767px)')) {
                $(this).find('.dropdown-toggle').addClass('arrow_box');
            }
        });

        $('.navbar .megamenu .dropdown').on('hide.bs.dropdown', function() {
            $(this).find('.dropdown-toggle').removeClass('arrow_box');
        });



        $('.navbar .dropdown-mobile').on('show.bs.dropdown', function(e) {
            customNavEventLocator(e);
            commonNavScrollTop(e);
            mobileOpenParent = $(e.target);
            mobileOpenParentLink = $(e.target).find('.dropdown-toggle');
            console.log("show");
        });

        $('.navbar .dropdown-mobile').on('hide.bs.dropdown', function(e) {
            $('.navbar').removeClass('position-abs');
            $('.navbar .navbar-header-wrapper').removeClass('position-abs');
            //$(this).find('.dropdown-toggle').focus();

        });




        $('.megamenu .dropdown .dropdown-toggle').click(function(e) {
          e.preventDefault;
        });

        /*$('.megamenu .menu-parent > .dropdown-toggle').focus(function(e) {
          console.log("auto close");
          $('.megamenu .menu-parent.open .dropdown-toggle').not($(this)).click();
        });*/

        /*$('*').not('.dropdown-menu *, .menu-parent.open').on('click focus', function(e) {
          if(event.type === 'focus') { // 1
            console.log(e.target);
            console.log("auto close");
            $('.megamenu .menu-parent.open .dropdown-toggle').not('.select .dropdown-toggle').dropdown('toggle');
          }

        });*/


        var menudisabledHandle;
        var menuhiddenHandle;
        var menutabHandle;
        var megamenuParent;

        $('.megamenu .dropdown').on('shown.bs.dropdown', function(e) {
          //megamenuParent = $(e.target);
          megamenuParent = $(this).find('.dropdown-toggle');

          $(this).find('.dropdown-close').click(function(e) {
            e.preventDefault();
            megamenuParent.dropdown('toggle');
            setTimeout(function(){ megamenuParent.focus(); }, 100);
          });

          $('.end-dropdown').focus(function() {
            setTimeout(function(){ megamenuParent.focus(); }, 100);
          });


        });

        $('.megamenu .dropdown').on('hidden.bs.dropdown', function (e) {
          //menudisabledHandle.disengage();
          //menuhiddenHandle.disengage();
        });


        var menuMobiledisabledHandle;
        var menuMobilehiddenHandle;
        var menuMobiletabHandle;

        $('.navbar-header .dropdown-mobile').on('show.bs.dropdown', function(e) {
          setContainment($(e.target));
          console.log("show mobile menu dropdown");

          });

        $('.navbar-header .dropdown-mobile').on('hide.bs.dropdown', function(e) {
         removeContainment();


        });

		 $('.dropdown-menu').click(function(e) {
          if ($(e.target).is('[data-toggle=modal]')) {
            e.stopPropagation();
            $($(e.target).data('target')).modal();
		  }
        });

        $('#bs-example-navbar-collapse-1').on('show.bs.collapse', function(e) {
     setContainment($('.mobile-nav-area'));

          $('.end-menu').focus(function() {
            setTimeout(function(){ $('#last-target-hamburger').focus(); }, 100);
          });


        });

        $('#bs-example-navbar-collapse-1').on('hidden.bs.collapse', function(e) {
			removeContainment();

        });


        $('.navbar .dropdown-mobile .dropdown-close').click(function(e) {
          e.preventDefault();
          mobileOpenParent.click();
          console.log('click drop');
          setTimeout(function(){
            console.log($(mobileOpenParentLink));
            mobileOpenParentLink.focus();
            console.log("active: " + $(document.activeElement));
          }, 400);
        });



    }

    function customNavEventLocator(e){
        // dont open on Locate tab on restaurant locator page
        if($('.restaurantLocator').length && $(e.relatedTarget).hasClass('not-on-locate1')){
            e.preventDefault();
            $('html, body').animate({
                scrollTop: 0
            }, 400, 'swing', function() {

            });
            return false;
        }
    }

    function commonNavScrollTop(e){
        //if(! $(e.target).hasClass('dontscroll')){
            $('html, body').animate({
                scrollTop: 0
            }, 400, 'swing', function() {
                // navbar sticks to top
                $('.navbar').addClass('position-abs');

                //console.log($(e.target));

                if($(e.target).hasClass('dontscroll')){
                    $('.navbar .navbar-header-wrapper').addClass('position-abs');
                }
            });
        //}
    }


    function getDeviceSize() {
        if (window.innerWidth < 768) {
            return 'mobile';
        } else {
            return 'desktop';
        }
    }

    function scrollFocus(elem){
      var y = $(window).scrollTop();
      setTimeout(function(){
        elem.focus();
        $(window).scrollTop(y);
      }, 400);

    }

    function _mealSliderHideFlexsliderArrows(thisSlider) {
        if ($('.page-has-meal-sliders').length) {
            if (Modernizr.mq('(max-width: 767px)')) {
                // do nothing, keep arrows in mobile
            } else {
				if($('.page-has-meal-sliders.mealslide_with_param').length){
				    if ($(thisSlider).find('.slides li').length <= 4) {
                    $(thisSlider).find('.custom-navigation').hide();
                    }
				}else{
                if ($(thisSlider).find('.slides li').length <= 3) {
                    $(thisSlider).find('.custom-navigation').hide();
                }
            }

            }
        }
    }

    // tiny helper function to add breakpoints
     function getGalleryIngredientGridSize(e) {
        if (window.innerWidth < 768) {
            if ($('.page-has-meal-sliders').length) {
                return 1;
            } else {
                return 3;
            }

        }
         if ($(e).hasClass("imageGallery")) {
            return $(e).attr("data-count");
         } else if ($('.component-full-menu').length) {
            return 4;
        } else if ($('.page-has-meal-sliders').length) {
            // added to make happy meal slider configurable to 3 or 4 depending on the class added
            if($('.page-has-meal-sliders.mealslide_with_param').length){
              return 4;
            }
            else {
            return 3;
            }

        } else {
            return 6;
        }

    }

    function getIngredientGridSize() {
        if (window.innerWidth < 768) {
            if ($('.page-has-meal-sliders').length) {
                return 1;
            } else {
                return 3;
            }

        }

        if ($('.component-full-menu').length) {
            return 4;
        } else if ($('.page-has-meal-sliders').length) {
            return 3;
        } else {
            return 6;
        }

    }

    // tiny helper function to add breakpoints
    function getIngredientGridWidth() {
        if (window.innerWidth < 768) {
            return 256;
        } else if (window.innerWidth < 992) {
            return 125;
        } else if (window.innerWidth < 1200) {
            return 161;
        } else {
            return 195;
        }
    }

    function initProductCategory(){
        if (window.innerWidth < 768) {
          $('.cont-featured .img-responsive.cont-100.zoom-anim-target').removeClass('zoom-anim-target');
        }
        else{
          if(! $('.cont-featured .img-responsive.cont-100').hasClass('zoom-anim-target')){
            $('.cont-featured .img-responsive.cont-100').addClass('zoom-anim-target');
          }
        }
      }

    function initProductDetail() {

      // remove item if does not have ingredient-text
      $('.ingredient-text').each(function(){
        if($(this).html().length < 1){
          $(this).closest('li').remove();
        }
      });

	  // hide the slider if category is McCafe
      var isMcCafe = false;
      if($('.component-related-items').has('.loadingDiv_100006').length > 0){
		isMcCafe = true;
	  };

      var ingredientsSlideItems = $('.ingredients-flex-container .slides li');
      if(ingredientsSlideItems.length <= 2){
        // if there are 2 or less items in the list, hide "ingredients-flex-container"
        // and dont init the rest of the function
        $('.ingredients-flex-container').hide();
        return;
      }
      else if(ingredientsSlideItems.length < 6 && ingredientsSlideItems.length > 2){
        //if there are less than 6 and more than 2, the ingredients are centered on desktop
        $('.ingredients-flex-container .slides').addClass('centerItems');
      }


      var ingredientViewSize;

      if (window.innerWidth < 768) {
        ingredientViewSize = 'mobile';
      } else {
        ingredientViewSize = 'desktop';
      }

      //var ingredientsFlexContents = '<div class="ingredients-flexslider flexslider">' + $('.ingredients-flexslider').html() + '</div>';
      var ingredientsFlexContents = '<div class="ingredients-flex-container">' + $('.ingredients-flex-container').html() + '</div>';
      $('.ingredients-flexslider').css("direction","ltr");
      $('.ingredients-flexslider').flexslider({
        animation: 'slide',
        controlNav: false,
        slideshow: false,
        itemWidth: 40,
        itemMargin: 20,
        rtl: rtlFlow,
        minItems: getIngredientGridSize(), // use function to pull in initial value
        maxItems: getIngredientGridSize(), // use function to pull in initial value
        start: function(slider) {
          $('.ingredients-flexslider').resize();

          $('.ingredients-flexslider li').removeClass('active-slides');
          $('.ingredients-flexslider li').attr('aria-hidden', 'true');
          $('.ingredients-flexslider li  .visuallyhidden').removeAttr('tabindex');
          var startli = (slider.move * (slider.currentSlide));
          var endli = (slider.move * (slider.currentSlide + 1));
          slider.css("direction",dir);
          for (var i = startli + 1; i <= endli; i++) {
            $('.ingredients-flexslider li:nth-child(' + i + ')').addClass('active-slides');
            $('.ingredients-flexslider li:nth-child(' + i + ')').attr('aria-hidden', 'false');
            $('.ingredients-flexslider li:nth-child(' + i + ') .visuallyhidden').attr('tabindex', '0');
          }



        },
        after: function(slider){

          $('.ingredients-flexslider li').removeClass('active-slides');
          $('.ingredients-flexslider li').attr('aria-hidden', 'true');
          $('.ingredients-flexslider li .visuallyhidden').removeAttr('tabindex');
          var startli = (slider.move * (slider.currentSlide));
          var endli = (slider.move * (slider.currentSlide + 1));
          for (var i = startli + 1; i <= endli; i++) {
            $('.ingredients-flexslider li:nth-child(' + i + ')').addClass('active-slides');
            $('.ingredients-flexslider li:nth-child(' + i + ')').attr('aria-hidden', 'false');
            $('.ingredients-flexslider li:nth-child(' + i + ') .visuallyhidden').attr('tabindex', '0');
          }

          $('.ingredients-flexslider .slides .active-slides').first().find('.visuallyhidden').focus();


        }

      });

      //$('.ingredients-flexslider .flex-direction-nav li a').attr("role", "button");

      $('.ingredients-flexslider ul.flex-direction-nav').css("display", "none");

      $('.ingredients-flex-container .flex-nav-cust .flex-prev').click(function(e){
        e.preventDefault();
        $('.ingredients-flexslider').flexslider("prev");
      });
      $('.ingredients-flex-container .flex-nav-cust .flex-next').click(function(e){
        e.preventDefault();
        $('.ingredients-flexslider').flexslider("next");
      });


      $(window).on('resize', function() {

        if (getDeviceSize() != ingredientViewSize) {

          //$('.ingredients-flexslider').remove();
          //$('.ingredients-flex-container').html(ingredientsFlexContents);
          $('.ingredients-flexslider-container').remove();
          $('.ingredients-wrapper').html(ingredientsFlexContents);
          $('.ingredients-flexslider').css("direction","ltr");
          $('.ingredients-flexslider').flexslider({
            animation: 'slide',
            controlNav: false,
            slideshow: false,
            itemWidth: 40,
            itemMargin: 20,
            rtl: rtlFlow,
            minItems: getIngredientGridSize(),
            maxItems: getIngredientGridSize(),
            start: function(slider) {
              $('.ingredients-flexslider').resize();
              $('.ingredients-flexslider li').removeClass('active-slides');
              $('.ingredients-flexslider li').attr('aria-hidden', 'true');
              $('.ingredients-flexslider li .visuallyhidden').removeAttr('tabindex');
              var startli = (slider.move * (slider.currentSlide));
              var endli = (slider.move * (slider.currentSlide + 1));
              slider.css("direction",dir);
              for (var i = startli + 1; i <= endli; i++) {
                $('.ingredients-flexslider li:nth-child(' + i + ')').addClass('active-slides');
                $('.ingredients-flexslider li:nth-child(' + i + ')').attr('aria-hidden', 'false');
                $('.ingredients-flexslider li:nth-child(' + i + ')').attr('tabindex', '0');
              }


            },
            after: function(slider) {

              $('.ingredients-flexslider li').removeClass('active-slides');
              $('.ingredients-flexslider li').attr('aria-hidden', 'true');
              $('.ingredients-flexslider li .visuallyhidden').removeAttr('tabindex');
              var startli = (slider.move * (slider.currentSlide));
              var endli = (slider.move * (slider.currentSlide + 1));
              for (var i = startli + 1; i <= endli; i++) {
                $('.ingredients-flexslider li:nth-child(' + i + ')').addClass('active-slides');
                $('.ingredients-flexslider li:nth-child(' + i + ')').attr('aria-hidden', 'false');
                $('.ingredients-flexslider li:nth-child(' + i + ') .visuallyhidden').attr('tabindex', '0');
              }

              $('.ingredients-flexslider .slides .active-slides').first().find('.visuallyhidden').focus();

            }
          });
          $('.ingredients-flexslider ul.flex-direction-nav').css("display", "none");
          ingredientViewSize = getDeviceSize();
        }

      });
    }

    // global var to keep track of living flexslider items
    var _relatedFlexContainerArrayCont = new Array();

    // destroys existing related flexsliders
    function destroyRelatedFlexSliders() {
        var theContainer;

        $('.related-flexslider').each(function(index) {
            theContainer = $(this).parent();
            $(this).remove();
            $(theContainer).html(_relatedFlexContainerArrayCont[index]);
        });
    }

    function flexsliderADA(currSlider){

        currSlider.find('ul.slides').on('keydown', function(e){

          // down or right
          if(e.type == 'keydown' && (e.keyCode == 40 || e.keyCode == 39)){
            e.preventDefault();

            // reset all to unfocusable
            currSlider.find('li a').attr('tabindex','-1');
            currSlider.find('li.focus-active').removeClass('focus-active');

            var firstFocus = currSlider.find('li.first-focus');
            var currFocus = currSlider.find('li.current-focus');

            if(currFocus.length > 0){
              // we're iterating, get next list element
              currFocus.removeClass('current-focus');

              // if on the last item, go back to first
              if(currFocus == currSlider.find('li:last-child')){
                firstFocus.addClass('current-focus');
            }
            else{
              currFocus.next().addClass('current-focus');
            }
          }
          else{
            // first time we focused in the list, so get first element
            firstFocus.addClass('current-focus');
          }

            // shift slider so element is in view
            // must happen before focus
            if(! currSlider.find('li.current-focus').hasClass('active-slides')){
              currSlider.flexslider('next');
            }

            currSlider.find('li.current-focus').addClass('focus-active');
            currSlider.find('li.current-focus a').attr('tabindex','0').focus();

          }

          if(e.type == 'keydown' && (e.keyCode == 37 || e.keyCode == 38)){
            e.preventDefault();

            // reset all to unfocusable
            currSlider.find('li a').attr('tabindex','-1');
            currSlider.find('li.focus-active').removeClass('focus-active');

            var firstFocus = currSlider.find('li.first-focus');
            var currFocus = currSlider.find('li.current-focus');

            if(currFocus.length > 0){
              // we're iterating, get next list element
              currFocus.removeClass('current-focus');

              // if on the first item, go back to last
              if(currFocus == firstFocus){
                currSlider.find('li:last-child').addClass('current-focus');
            }
            else{
              currFocus.prev().addClass('current-focus');
            }
          }
          else{
            // first time we focused in the list, so get last element
            currSlider.find('li:last-child').addClass('current-focus');
          }

            // shift slider so element is in view
            // must happen before focus
            if(! currSlider.find('li.current-focus').hasClass('active-slides')){
              currSlider.flexslider('prev');
            }

            currSlider.find('li.current-focus').addClass('focus-active');
            currSlider.find('li.current-focus a').attr('tabindex','0').focus();

          }
        });


    }

    function flexsliderActiveSlidesADA(slider){
      // adds a class to visible items
      slider.find('li').removeClass('active-slides');
      var startli = (slider.move * (slider.currentSlide));
      var endli = (slider.move * (slider.currentSlide + 1));
      for (var i = startli + 1; i <= endli; i++) {
        slider.find('li:nth-child(' + i + ')').addClass('active-slides');
      }
      slider.find('.slides > li').attr("aria-hidden", "false");
        //slider.find('.slides > li a').attr("tabindex", "0");
      slider.find('.slides > li').not('.active-slides').attr("aria-hidden", "true");
      //slider.find('.slides li').not('.active-slides').attr("tabindex", "-1");
        //slider.find('.slides > li:not(.active-slides) a').attr("tabindex", "-1");
        //slider.find('.slides > li.active-slides a').first().focus();
        //console.log("huh");
      }

      function flexSliderAdaFocus(slider){
        slider.find('.slides > li.active-slides a').first().focus();
    }


    function flexsliderSurveyActiveSlidesADA(slider){
      slider.find('.slides > section').attr("aria-hidden", "false");
      slider.find('.slides > section').removeAttr("tabindex");
      slider.find('.slides > section').not('.flex-active-slide').attr("aria-hidden", "true");
      slider.find('.slides > section:not(.flex-active-slide)').attr("tabindex", "-1");
    }

  // inits related flexsliders and handles browser resize destroy-inits
    function initRelatedFlexSliders() {
        var viewSize;

        if (window.innerWidth < 768) {
            viewSize = 'mobile';
        } else {
            viewSize = 'desktop';
        }

        var sliderCont;

        $('.related-flexslider').each(function(index) {

            sliderCont = '<div class="related-flexslider flexslider">' + $(this).html() + '</div>';
            _relatedFlexContainerArrayCont.push(sliderCont);
            $(this).flexslider({
                animation: 'slide',
                controlNav: false,
                slideshow: false,
                itemWidth: 40,
                itemMargin: 0,
                //controlsContainer: $(this).find('.custom-controls-container'),
                //customDirectionNav: $(this).find('.custom-navigation a'),
                minItems: getGalleryIngredientGridSize(this),
                maxItems: getGalleryIngredientGridSize(this),
                  rtl: rtlFlow,
                start: function(slider) {
                    $(this).resize();
                    _mcpickSliderStart(slider, slider.currentSlide);
                    _mealSliderHideFlexsliderArrows(slider);
                           slider.css("direction",dir)

                    flexsliderActiveSlidesADA(slider);
                },
                after: function(slider) {
                    _mcpickSliderAfter(slider, slider.currentSlide);

                    flexsliderActiveSlidesADA(slider);
                }
            });

            flexsliderADA($(this));


            $(this).find('.custom-navigation a').removeClass("keep-outline");
            $(this).find('.flex-custom-nav a').removeClass("keep-outline");
            $(this).find('.custom-navigation a').on("mousedown", "*", function(e) {
                $(this).addClass("click-before-outline");
        });

            $(this).find('.custom-navigation a.flex-next').click(function(e) {
              $(this).closest(".flexslider").flexslider('next');
            });
            $(this).find('.custom-navigation a.flex-prev').click(function(e) {
              $(this).closest(".flexslider").flexslider('prev');
            });

           /* $(this).find('.flex-custom-nav a.flex-next').click(function(e) {
                $(this).closest(".flexslider").flexslider('next');
              });
               $(this).find('.flex-custom-nav a.flex-prev').click(function(e) {
                $(this).closest(".flexslider").flexslider('prev');
              });*/


            $(this).find('.custom-navigation a').keypress(function(event){
              var keycode = (event.keyCode ? event.keyCode : event.which);
              if (keycode == 13) {
                $(this).click();
              }
            });
        });



        $(window).on('resize', function() {
            var theContainer;

            if (getDeviceSize() != viewSize) {

                 //destroyRelatedFlexSliders();
                  $('.related-flexslider').css("direction","ltr")
                 $('.related-flexslider').not('.no-init').each(function(index) {

                    $(this).flexslider({
                        animation: 'slide',
                        controlNav: false,
                        slideshow: false,
                        itemWidth: 40,
                        itemMargin: 0,
                         rtl: rtlFlow,
                        controlsContainer: $(this).find('.custom-controls-container'),
                        customDirectionNav: $(this).find('.custom-navigation a'),
                        minItems: getGalleryIngredientGridSize(this), // use function to pull in initial value
                        maxItems: getGalleryIngredientGridSize(this),// use function to pull in initial value
                        start: function(slider) {
                            $(this).resize();
                            _mcpickSliderStart(slider, slider.currentSlide);
                            _mealSliderHideFlexsliderArrows(slider);
                              slider.css("direction",dir)
                            flexsliderActiveSlidesADA(slider);
                        },
                        after: function(slider) {
                            _mcpickSliderAfter(slider, slider.currentSlide);

                           flexsliderActiveSlidesADA(slider);
                        }
                    });

                     flexsliderADA($(this));

                    $(this).find('.custom-navigation a').removeClass("keep-outline");
                    $(this).find('.flex-custom-nav a').removeClass("keep-outline");
                    $(this).find('.custom-navigation a').on("mousedown", "*", function(e) {
                        $(this).addClass("click-before-outline");
                    });

                    $(this).find('.custom-navigation a.flex-next').click(function(e) {
                      $(this).closest(".flexslider").flexslider('next');
                    });
                    $(this).find('.custom-navigation a.flex-prev').click(function(e) {
                      $(this).closest(".flexslider").flexslider('prev');
                    });
                    $(this).find('.flex-custom-nav a.flex-next').click(function(e) {
                        $(this).closest(".flexslider").flexslider('next');
                      });
                      $(this).find('.flex-custom-nav a.flex-prev').click(function(e) {
                        $(this).closest(".flexslider").flexslider('prev');
                      });

                    $(this).find('.custom-navigation a').keypress(function(event){
                      var keycode = (event.keyCode ? event.keyCode : event.which);
                      if (keycode == 13) {
                        $(this).click();
                        }
                    });

                });

                viewSize = getDeviceSize();
            }

        });
        $('.popup-link').magnificPopup({
                  type: 'image',
                  gallery:{enabled:true}
         });
    }

    function initNavMobileSmallTabs(){
        $('.cont-half > ul.dropdown-menu').on('click', function(e) {
            var _elem = $(e.target);
            //console.log(_elem);

            if (_elem.hasClass('fa-close') || _elem.hasClass('fa-times')) {
                // do nothing
            } else {
                e.stopPropagation();
            }
        });
    }

    function addDistanceADA(){

      $('.dropdown-menu .distance .bootstrap-select').on('click', function(event) {
        if($(this).find('.dropdown-toggle').attr('aria-expanded') != "true"){
          $(this).find('.dropdown-toggle').attr('aria-expanded', "true");
          $(this).find('.dropdown-menu').attr('aria-expanded', true);
        }else{
          $(this).find('.dropdown-toggle').attr('aria-expanded', "false");
          $(this).find('.dropdown-menu').attr('aria-expanded', false);
        }
      });


    }
    function initNavLocateFilters() {

        $('.component-locate ul.dropdown-menu').on('click', function(e) {
            var _elem = $(e.target);
            //console.log(_elem);

            if (_elem.hasClass('fa-close')) {
                // do nothing
            } else {
                e.stopPropagation();
            }

        });


        $('.not-on-locate').click(function() {
          //addDistanceADA();
        });

        $('.component-locate .locate-filters-show').on('click keydown', function(event) {


          if(event.type == 'click' || (event.type == 'keydown' && (event.which == 13 || event.which == 32))){
            event.preventDefault();
            $('.component-locate li.afterfold').toggleClass('hidden');

            var dataMore = $(this).attr('data-more');
            var dataLess = $(this).attr('data-less');

            if ($(this).find('.fa').hasClass('fa-chevron-down')) {
                $(this).html(dataLess);
                $(this).attr('aria-expanded', 'true');
                //$( "<div role='alert' class='visuallyhidden'>"+adaGenericText.expandedtext+"</div>" ).appendTo( "body" );
                // set focus to new list items when menu is expanded
                //var firstElem = $('.component-locate li.first-focus');//.first();
                //firstElem.find('input').focus();

            } else {
                $(this).html(dataMore);
                $(this).attr('aria-expanded', 'false');
                //$( "<div role='alert' class='visuallyhidden'>"+adaGenericText.collapsedtext+"</div>" ).appendTo( "body" );
            }

            $(this).focus();
          }

        });

		/*$('.component-locate .locate-filters-show').keydown(function (e) {
          if($(this).attr('aria-expanded') == 'true'){
            if (e.which == 9 || e.which == 39) {
              setTimeout(function(){
                var firstElem = $('.component-locate li.first-focus');
                firstElem.find('input').focus();
              }, 0);
            }
          }

        });*/
    }

    function initRestaurantLocations() {
        //console.log('initLocations()');
        if($('.restaurant-location__hours-toggle > a').length){
          var theCollapsibles = $('.restaurant-location__hours-toggle > a');
          $.each(theCollapsibles, function(index,value){
            var oneCollapsible = $(value).attr('data-target');
            //console.log(oneCollapsible);
            $(oneCollapsible).on('hide.bs.collapse', function () {
              $(value).attr('aria-expanded','false');
            });
            $(oneCollapsible).on('show.bs.collapse', function () {
              $(value).attr('aria-expanded','true');
            });
          });



          /*theCollapsibles.on('hidden.bs.collapse', function () {
            $(this).attr('aria-expanded','false');
          });
          theCollapsibles.on('shown.bs.collapse', function () {
            $(this).attr('aria-expanded','true');
          });

          $('.restaurant-location__hours-toggle > a').on('click keydown',function(e){
            console.log($(this).attr('aria-expanded'));
            if($(this).attr('aria-expanded') == 'false'){
              $(this).attr('aria-expanded','true');
            }
            else{
              $(this).attr('aria-expanded','false');
            }
          });*/
        }

        $('.locations-list .location').each(function(index, el) {
            var $el = $(el);
            var $detailsToggle = $el.find('.details-toggle');
            var $detailPlus = $detailsToggle.find('.plus');
            var $detailMinus = $detailsToggle.find('.minus');
            var $details = $el.find('.secondary-details-mobile');

            //console.log(index, el, $details);

            if (index > 0) {
                $details.hide();
            } else {
                $el.addClass('details-open');
            }

            function update() {
                if ($el.hasClass('details-open')) {
                    $details.slideDown();
                    $detailMinus.slideDown('slow');
                    $detailPlus.slideUp('slow');
                } else {
                    $details.slideUp();
                    $detailMinus.slideUp('slow');
                    $detailPlus.slideDown('slow');
                }
            }

            function onToggleClick(e) {
                e.preventDefault();
                //console.log(e);

                var isOpen = $el.hasClass('details-open');
                $el.toggleClass('details-open');

                update();

                isOpen = !isOpen;

                if (isOpen) {
                    // Scroll to it...
                    var elOffset = $el.offset();
                    // add height of top nav, roughly, with padding
                    var processedOffset = elOffset.top - (86 + 12);
                    $('body').scrollTo(processedOffset);
                }
            }

            $detailsToggle.click(onToggleClick);
            update();
        });
    }

    function initRestarauntFinderTemplate() {
        if (!$('main .restaurantLocator').length) {
            return;
        }

        //console.log('we\' looking at the restaurent component...');

        // Disable locate click
        $('header').find('.locate-toggle').each(function(index, el) {
            //console.log(index, el);
            var $el = $(el);
            $el.attr('data-toggle', '__nope__');
            $(el).click(function(e) {
                e.preventDefault();
                //console.log(e);
            });
        });

        //ada accessible maps

    }

    // tiny helper function to add breakpoints

    function getMealAdditionGridSize() {
        if (window.innerWidth < 768) {
            return 3;
        } else {
            return 7;
        }
    }

    function initNutritionCalculateTab() {

        function fixMealTab(e) {
            var mnavBarHeight = $('.navbar').height();
            var mmealtabHeight = $('.cont-meal-tab').height()
            var mscrollToOffset = mnavBarHeight + mmealtabHeight;

            var banner = $('#smartbanner');
            var bannerHeight = 0;
            if(banner.length) {
                bannerHeight = $(banner).outerHeight();
            }

            var distanceY = window.pageYOffset || document.documentElement.scrollTop,
            shrinkOn = $('.cont-hero').height() - mscrollToOffset + bannerHeight - 5;


            if (distanceY > shrinkOn) {
                $('.cont-meal-tab').addClass('fixed');
                if(banner.length) {
                    if ($(banner).is(':visible') && e.type !== 'bannerClosed') {
                        $('.cont-meal-tab').css({'top': bannerHeight + $('.navbar').height()});
                    } else if (e.type === 'bannerClosed') {
                        $('.cont-meal-tab').css({'top': $('.navbar').height()});
                    }
                }
            } else {
                $('.cont-meal-tab').removeClass('fixed');
                $('.cont-meal-tab').css('top', '');
            }
        }

        if ($('.nutrition-calculator').length) {

        	//moved to nc controler

            $('.scroll-btn').click(function (){
                var navBarHeight = $('.navbar').height();
                var mealtabHeight = $('.cont-meal-tab').height()
                var scrollToOffset = navBarHeight + mealtabHeight;
                $('html, body').animate({
                    scrollTop: $('.cont-hero').height() - scrollToOffset - 5
                }, 200);
            });

            //init meal bug
            window.addEventListener('scroll', fixMealTab);
            $(window).on('bannerClosed', fixMealTab);
        }

        var viewSize;
        var sliderCont;

        if (window.innerWidth < 768) {
            viewSize = 'mobile';
        } else {
            viewSize = 'desktop';
        }

        //Collapsable toggle arrow icons

        function toggleChevron(e) {

            $(e.target).prev('.panel-heading').find('a.details-toggle').toggleClass('open');
            //added for nc
            var showtext=$(".ncshowtext").text();
            var hidetext=$(".nchidetext").text();
            if($(e.target).prev('.panel-heading').find('a.details-toggle').html()==showtext){
                $(e.target).prev('.panel-heading').find('a.details-toggle').html(hidetext);
            }else{
                $(e.target).prev('.panel-heading').find('a.details-toggle').html(showtext);
            }

            //$(e.target).find('.customize-flexslider').data('flexslider').resize();

            var theOptSlides = $(e.target).find('.customize-flexslider ul.slides li').length;

            sliderCont = '<div class="customize-flexslider flexslider">' + $(e.target).find('.customize-flexslider').html() + '</div>';

            if (getDeviceSize() == 'mobile') {

                //always slide for mobile
                if (theOptSlides > 0) {
                    $(e.target).find('.customize-flexslider').flexslider({
                        animation: 'slide',
                        controlNav: false,
                        slideshow: false,
                        itemWidth: 40,
                        itemMargin: 10,
                        controlsContainer: $(e.target).find('.custom-controls-container'),
                        customDirectionNav: $(e.target).find('.custom-flex-navigation a'),
                        minItems: 2, // use function to pull in initial value
                        maxItems: getMealAdditionGridSize(), // use function to pull in initial value
                        start: function(slider) {
                            //$('.customize-flexslider').resize();
                        }
                    });

                } else {
                    //no slide
                    $(e.target).find('.slider').addClass('no-slide');
                }

            } else {

                $(e.target).find('.customize-flexslider').flexslider({
                    animation: 'slide',
                    controlNav: false,
                    slideshow: false,
                    itemWidth: 40,
                    itemMargin: 20,
                    controlsContainer: $(e.target).find('.custom-controls-container'),
                    customDirectionNav: $(e.target).find('.custom-flex-navigation a'),
                    minItems: 4, // use function to pull in initial value
                    maxItems: getMealAdditionGridSize(), // use function to pull in initial value
                    start: function(slider) {
                        //$('.customize-flexslider').resize();
                    }
                });

                if (theOptSlides > 7) {

                } else {
                    //no slide
                    //$(e.target).find('.slider').addClass('no-slide');
                    $(e.target).find('.slider').addClass('min-num');
                }
            }

            $(window).on('resize', function() {

                var theContainer;

                if (getDeviceSize() != viewSize) {

                    $('.customize-flexslider').each(function(index) {
                        $(this).remove();
                    });

                    theContainer = $(e.target).find('.flex-container');

                    $(theContainer).html(sliderCont);

                    if (getDeviceSize() == 'mobile') {

                        //always slide for mobile
                        if (theOptSlides > 0) {

                            $(e.target).find('.customize-flexslider').flexslider({
                                animation: 'slide',
                                controlNav: false,
                                slideshow: false,
                                itemWidth: 40,
                                itemMargin: 10,
                                controlsContainer: $(e.target).find('.custom-controls-container'),
                                customDirectionNav: $(e.target).find('.custom-flex-navigation a'),
                                minItems: 2, // use function to pull in initial value
                                maxItems: getMealAdditionGridSize(), // use function to pull in initial value
                                start: function(slider) {
                                    //$('.customize-flexslider').resize();
                                }
                            });

                        } else {
                            //no slide
                            $(e.target).find('.slider').addClass('no-slide');
                        }

                    } else {

                        $(e.target).find('.customize-flexslider').flexslider({
                            animation: 'slide',
                            controlNav: false,
                            slideshow: false,
                            itemWidth: 40,
                            itemMargin: 20,
                            controlsContainer: $(e.target).find('.custom-controls-container'),
                            customDirectionNav: $(e.target).find('.custom-flex-navigation a'),
                            minItems: 4, // use function to pull in initial value
                            maxItems: getMealAdditionGridSize(), // use function to pull in initial value
                            start: function(slider) {
                                //$('.customize-flexslider').resize();
                            }
                        });

                        if (theOptSlides > 7) {
                //do nothing
                        } else {
                            //no slide
                            //$(e.target).find('.slider').addClass('no-slide');
                            $(e.target).find('.slider').addClass('min-num');
                        }
                    }
                    viewSize = getDeviceSize();
                }
            });
        }

        $('#nutrition-accordion').on('hidden.bs.collapse', toggleChevron);
        $('#nutrition-accordion').on('shown.bs.collapse', toggleChevron);

        $('.details-toggle').click(function() {
            $('.panel-collapse.in').collapse('hide');
        });
        //added for nutrition calculator as this section is generated after page load
        $(".nutrition-calculator").on('click', '.details-toggle', function() {
            $('.panel-collapse.in').collapse('hide');
        });


        function calcSectionOut(elm){
            if(!$(elm).hasClass('out')){
              $(elm).addClass('out');
            }
          }

          function showCalcStart(){
            $('.cont-hero .view-meal').show();
            $('.cont-hero .add-meal').hide();
            $('#level-category').fadeOut(50);
            calcSectionOut('#level-category');
            $('#level-details').fadeOut(50);
            calcSectionOut('#level-details');
            $('#level-all').removeClass('out');
            $('#level-all').fadeIn(100);
            scrollFocus($('#level-all'));
            if($('.cont-hero .detailed').hasClass('isActive')){
              $('.cont-hero .detailed').slideUp(300);
              $('.cont-hero .detailed').removeClass('isActive');
            }
            window.scrollTo(0, 0);
          }
          function showCalcCategory(){
            $('#level-all').fadeOut(50);
            /* Start: GWSL-1611 */
            $('#level-all').find('.no-ring').removeClass('no-ring');
            //$('#level-all').find('.click-before-outline').removeClass('click-before-outline').css('outline', 'initial');
            /* END */
            calcSectionOut('#level-all');
            $('#level-category').removeClass('out');
            $('#level-category').fadeIn(100);
            scrollFocus($('#level-category'));

            if($('.cont-hero .detailed').hasClass('isActive')){
              $('.cont-hero .detailed').slideUp(300);
              $('.cont-hero .detailed').removeClass('isActive');
            }

        /*setTimeout(function(){
          $('#level-category').focus();
        }, 200); */

          }
          function showCalcDetail(){
            $('.cont-hero .view-meal').hide();
            $('.cont-hero .add-meal').show();
            $('#level-category').fadeOut(50);
            calcSectionOut('#level-category');
            $('#level-all').fadeOut(50);
            calcSectionOut('#level-all');
            $('#level-details').removeClass('out');
            $('#level-details').fadeIn(100);
            //scrollFocus($('#level-details'));

            if(!$('.cont-hero .detailed').hasClass('isActive')){
              $('.cont-hero .detailed').slideDown(300);
              $('.cont-hero .detailed').addClass('isActive');
              $('.cont-hero .detailed').attr('aria-hidden', 'false');
            }

          }

          setTimeout(function(){
              $('#level-details .customize-start').focus();
            }, 320);

          //new nutrition calculator stuff
          if ($('.nutrition-calculator-alter').length) {

        	  $('.btn-legal-display-toggle').on('click', function(e){
                  e.preventDefault();
                  $('.cont-legal').toggleClass('short');

                  if($(this).attr('data-status') == 'short'){
                    $(this).html($(this).attr('data-less'));
                    $(this).attr('data-status','long');
                  }
                  else{
                    $(this).html($(this).attr('data-more'));
                    $(this).attr('data-status','short');
                  }
                  $(this).attr('aria-label', $(this).find('span').text());
                });

                /*var totalsLength = $('div.totals .item').length;
                var totalsHtml = '<table class="table"><tr>';
                $('div.totals .item').each(function(x,y){
                  var tableItem = $(y).clone().removeClass('desktop');
                  var tableItemStr = tableItem.wrap('<div>').parent().html();

                  if(totalsLength == 7){
                    if(x < 4){
                      totalsHtml += '<td class="col-xs-3">' + tableItemStr + '</td>';
                    }
                    else{
                      totalsHtml += '<td class="col-xs-4">' + tableItemStr + '</td>';
                    }
                  }
                  else if(totalsLength == 6){
                    if(x < 4){
                      totalsHtml += '<td class="col-xs-4">' + tableItemStr + '</td>';
                    }
                    else{
                      totalsHtml += '<td class="col-xs-4">' + tableItemStr + '</td>';
                    }
                  }
                  else if(totalsLength == 5){
                    if(x < 3){
                      totalsHtml += '<td class="col-xs-4">' + tableItemStr + '</td>';
                    }
                    else{
                      totalsHtml += '<td class="col-xs-6">' + tableItemStr + '</td>';
                    }
                  }
                  else{
                    totalsHtml += '<td class="col-xs-3">' + tableItemStr + '</td>';
                  }

                  if(totalsLength == 7 && x == 3){
                    totalsHtml += '</tr></table><table class="table"><tr>';
                  }
                  else if(totalsLength == 6 && x == 2){
                    totalsHtml += '</tr></table><table class="table"><tr>';
                  }
                  else if(totalsLength == 5 && x == 2){
                    totalsHtml += '</tr></table><table class="table"><tr>';
                  }

                });
                totalsHtml += '</tr></table>';
                $('.totals').append(totalsHtml);
*/

            $('#level-all .btn-category-nav').click(function(e) {
              e.preventDefault();
                showCalcCategory();
            });

            $('#level-all .btn-category-nav').mousedown(function() {
                $('#level-category').addClass('no-ring');
            });

            $('#level-category .btn-back').mousedown(function(e) {
                e.preventDefault();
                  showCalcStart();
              });

           $( "#level-category .btn-back" ).focus(function() {
        	   var y = $(window).scrollTop($("#level-category"));
        	   $(window).scrollTop(y);
           });

           $(".nutrition-calculator-alter").on('focus', '#level-category .menu-item .activate-item',  function(e){
               var scrollTop     = $(window).scrollTop(),
                   elementOffset = $(this).offset().top,
                   distance      = (elementOffset - scrollTop);

               if(distance < 173){
                 $(window).scrollTop(scrollTop - 173);
               }
             });

           $(".nutrition-calculator-alter").on('focus', '#level-details .meal-item label, #level-details .meal-item input, #level-details .meal-item button',  function(e){
               var scrollTop     = $(window).scrollTop(),
                   elementOffset = $(this).offset().top,
                   distance      = (elementOffset - scrollTop);

               if(distance < 183){
                 $(window).scrollTop(scrollTop - 183);
               }
             });

           $('#level-category .activate-item').keypress(function(event){
               var keycode = (event.keyCode ? event.keyCode : event.which);
               if (keycode == 13) {
                 $(this).click();
               }
             });

           $('.nutrition-calculator-alter').on('keypress', '#level-details .activate-check', function(event) {

               var keycode = (event.keyCode ? event.keyCode : event.which);
               if (keycode == 13) {
                 $(this).click();
               }
             });

           	 $('.nutrition-calculator-alter').on('change', '#level-details .activate-check', function(e) {
                 $(this).toggleClass('checked');
                 //var theItemName = $(this).attr('aria-label');
                 var hideText = $(this).parent().find("label").eq(0).find(".open").text();
                 var showText = $(this).parent().find("label").eq(0).find(".closed").text();
                 if($(this).hasClass('checked')){
                   $(this).next().attr('aria-label', hideText);
                   var alertElem = $( "<div role='alert' class='visuallyhidden nc-customize-alert'>"+hideText+"</div>" ).appendTo( "body" );
                 }else{
                   $(this).next().attr('aria-label', showText);
                   var alertElem = $( "<div role='alert' class='visuallyhidden nc-customize-alert'>"+showText+"</div>" ).appendTo( "body" );
                 }

                 /* Start: GWSL-2225 */
                 /* Remove role="alert" elements one by one after a period of given milliseconds*/
                 setTimeout(function() {alertElem.remove();}, 3000);
                 /* End */
             });

           	 $('body').on('click', '#level-category .nc.menu-item', function(e) {

                if(!$('.cont-hero .basket').hasClass('isActive')){
                  $('.cont-hero .basket').slideDown(200);
                  $('.cont-hero .basket').addClass('isActive');
                  $('.cont-hero .basket').attr('aria-hidden', 'false');
                }
                showCalcDetail();

            });

            //$('#level-category .activate-item.nc').keypress(function(event){
            $('.nutrition-calculator-alter').on('keypress', '#level-category .activate-item.nc', function(event) {
                var keycode = (event.keyCode ? event.keyCode : event.which);
                if (keycode == 13) {
                  $(this).click();
                }
              });

            $('.nutrition-calculator-alter').on('keypress', '.nc-select .dropdown-menu.inner li', function(event) {
                var keycode = (event.keyCode ? event.keyCode : event.which);
                if (keycode == 13) {
                  $(this).click();
                }
              });

            /*$('.nutrition-calculator-alter').on('click', '.nc-select .dropdown-menu.inner li', function(event) {
            	$(this).parents(".nc-select").find("button").focus();
            });*/
            //  $('#level-category .activate-item.nc').change(function(e) {
           /* $('.nutrition-calculator-alter').on('change', '#level-category .activate-item.nc', function(e) {
                  var clickedEl = $(this);
                  var theAnnounce = $(this).parent().find('.status');
                  theAnnounce.attr("role", "alert");

                  if(window.innerWidth < 768){
                    scrollFocus(clickedEl);
                    console.log(document.activeElement);
                  }
              });
            */

            $('.nutrition-calculator-alter').on('change', '#level-category .activate-item.nc', function(e) {
                var clickedEl = $(this);
                /* Commented: blank condition
                if(window.innerWidth < 768){
                  setTimeout(function() {
                    //clickedEl.parent().find('.buttons button').focus();
                  }, 500);
                }
                */
                var itemName1 = $(this).parent().find('.name').text();
                var itemName = $(this).parent().find('.status').text();
                var alertElem = $( "<div role='alert' class='visuallyhidden nc-added-alert'>"+itemName1+itemName+"</div>" ).appendTo( "body" );

                /* Start: GWSL-2225 */
                /* Remove role="alert" elements one by one after a period of given milliseconds*/
                setTimeout(function() {alertElem.remove();}, 3000);
                /* End */
            });


           /* $('.modal.nutrition-delete').on('hidden.bs.modal', function (e) {
                setTimeout(function() {
                  var tabbableItems = $('.nutrition-meal-details').find(":tabbable");
                  var $firsttabbableElmt = $(tabbableItems[0]);
                  $firsttabbableElmt.focus();
                }, 300);

              });*/


            $('.cont-hero .view-meal').click(function(e) {
             // e.preventDefault();
                showCalcDetail();
            });

            $('.add-meal').click(function(e) {
              e.preventDefault();
                showCalcStart();
            });

           // $('.init-hide').fadeOut(0);

            window.addEventListener('scroll', function(e){
                var distanceY = window.pageYOffset || document.documentElement.scrollTop,
                shrinkOn = $('.cont-hero').height();

                if (distanceY > shrinkOn) {
                  if(!$('.fixed-totals').hasClass('shown')){
                    $('.fixed-totals').addClass('shown');
                  }

                } else {
                  if($('.fixed-totals').hasClass('shown')){
                    $('.fixed-totals').removeClass('shown');
                  }

                }

            });

          }

    }

    /**
     * Initiate Contact Us template, as well as components that originated there.
     */
    function initContactUs() {

      if($('#ontactUs-ownersOrg').length && $('#ontactUs-ownersOrg-no').length){

        // ADA for radio group
        $('#ontactUs-ownersOrg').on('keydown', function(e){
          // up,down,left,right keys
          if(e.keyCode >= 37 && e.keyCode <= 40){
            $('#ontactUs-ownersOrg-no').focus();
            $('#ontactUs-ownersOrg-no').prop('checked', true);
            // hide fields
            $('#return-contact-feedback').collapse('hide');
          }
        });
        $('#ontactUs-ownersOrg-no').on('keydown', function(e){
          // up,down,left,right keys
          if(e.keyCode >= 37 && e.keyCode <= 40){
            $('#ontactUs-ownersOrg').focus();
            $('#ontactUs-ownersOrg').prop('checked', true);
            // display fields
            $('#return-contact-feedback').collapse('show');
          }
        });

        // ADA for radio group
        $('#contactUs-restaurantFeedback-locationSpecific-yes').on('keydown', function(e){
          // up,down,left,right keys
          if(e.keyCode >= 37 && e.keyCode <= 40){
            $('#contactUs-restaurantFeedback-locationSpecific-no').focus();
            $('#contactUs-restaurantFeedback-locationSpecific-no').prop('checked', true);
          }
        });
        $('#contactUs-restaurantFeedback-locationSpecific-no').on('keydown', function(e){
          // up,down,left,right keys
          if(e.keyCode >= 37 && e.keyCode <= 40){
            $('#contactUs-restaurantFeedback-locationSpecific-yes').focus();
            $('#contactUs-restaurantFeedback-locationSpecific-yes').prop('checked', true);
        }
        });

        // show/hide fields if selected (this only works for clicks)
        $('input[type=radio][name=ontactUs-ownersOrg]').on('change', function(e){
          //console.log($(this).val());
          if($(this).val() == 'Yes'){
            $('#return-contact-feedback').collapse('show');
          }
          else{
            $('#return-contact-feedback').collapse('hide');
        }
        });


    }
    }


    function initMcPick() {
        if ($('.page-has-meal-sliders').length) {
            _mcpickSetActiveSlide();

            // need to re-init flexsliders on tab toggle
            $('.nav-tabs a[data-toggle="tab"]').on('shown.bs.tab', function() {
               destroyRelatedFlexSliders();
               initRelatedFlexSliders();
                _mcpickSetActiveSlide();
            });

            // need to re-init flexsliders on browser resize
            //Fix for happy meal issue number 653
            /*$(window).on('resize', function() {
                destroyRelatedFlexSliders();
                initRelatedFlexSliders();
                _mcpickSetActiveSlide();
            });*/
        }
    }

    // mcpick: this sets the active class on the current slide
    function _mcpickSetActiveSlide() {
        $('.cont-slides .slides li a').on('click keydown',function(e) {
            if(e.type == 'click' || (e.type == 'keydown' && (e.keyCode == 13 || e.keyCode == 32))){
            e.preventDefault();
            var hmaria1 = $(this).parents(".related-flex-container").find(".hidden").attr("hmaria1");
            var hmaria2 = $(this).parents(".related-flex-container").find(".hidden").attr("hmaria2");
            $(this).closest('.slides').find('li a').removeClass('active');
            //$(this).closest('.slides').find('li a').removeAttr('aria-label');
            $(this).closest('.slides').find('li').each(function( index ) {
                $(this).find('a').first().attr("aria-label", hmaria1  + $(this).find('.caption').html() + hmaria2);
            });
            $(this).addClass('active');
            $(this).attr('aria-label',adaGenericText.selectedtext);
              //var theitem = $(this).parent().find('.caption').html();
              //$(this).attr('aria-label',theitem+' selected');
              //$(this).closest('li').attr("role", "alert");
              //$( "<div role='alert' class='visuallyhidden'>"+theitem+" selected</div>" ).appendTo( "body" );
            }
        });
        $('.cont-slides .slides li').on('click keydown',function(e) {
            if(e.type == 'click' || (e.type == 'keydown' && (e.keyCode == 13 || e.keyCode == 32))){
              e.preventDefault();
              var hmaria1 = $(this).parents(".related-flex-container").find(".hidden").attr("hmaria1");
              var hmaria2 = $(this).parents(".related-flex-container").find(".hidden").attr("hmaria2");
              $(this).closest('.slides').find('li a').removeClass('active');
              //$(this).closest('.slides').find('li a').removeAttr('aria-label');
              $(this).closest('.slides').find('li').each(function( index ) {
                  $(this).find('a').first().attr("aria-label", hmaria1 + $(this).find('.caption').html() + hmaria2);
              });
              $(this).find('a').addClass('active');
              $(this).find('a').attr('aria-label',adaGenericText.selectedtext);
              var theitem = $(this).find('.caption').html();
              //$(this).find('a').attr('aria-label',theitem+' selected');
              //$(this).attr("role", "alert");
              $( "<div role='alert' class='visuallyhidden'>"+theitem+" "+adaGenericText.selectedtext+"</div>" ).appendTo( "body" );

            }
        });
    }

    // mcpick: this handles the red bar/active slide when flexslider is initialized
    function _mcpickSliderStart(slider, currentSlide) {
        if ($('.page-has-meal-sliders').length) {
            var _middleIndex = currentSlide + 1;
            if (slider.count==1) {
            	_middleIndex = 0;
            }
            if (Modernizr.mq('(max-width: 767px)')) {
                _middleIndex = currentSlide;
            }
            var _getActiveSlide = $(slider).find('.slides li').get(_middleIndex);
            $(_getActiveSlide).find('a').addClass('active');
            $(_getActiveSlide).find('a').attr('aria-label',adaGenericText.selectedtext);
        }
    }

    // mcpick: this handles the red bar/active slide when flexslider is changing slides
    function _mcpickSliderAfter(slider, currentSlide) {
        if ($('.page-has-meal-sliders').length) {
            if (Modernizr.mq('(max-width: 767px)')) {
                var _middleIndex = currentSlide;
                var _getActiveSlide = $(slider).find('.slides li').get(_middleIndex);
                $(slider).find('.slides').find('li a').removeClass('active');
                $(slider).find('.slides').find('li a').removeAttr('aria-label');
                $(_getActiveSlide).find('a').addClass('active');
                $(_getActiveSlide).find('a').attr('aria-label',adaGenericText.selectedtext);
                var theitem = $(_getActiveSlide).find('.caption').html();
                $( "<div role='alert' class='visuallyhidden'>"+theitem+" "+adaGenericText.selectedtext+"</div>" ).appendTo( "body" );
            }
        }
    }


    function initSurvey() {

        var _currSlide = 0;

        function disableLinkToggle(){
            $('.survey-wrapper > a').attr('disabled', 'disabled');
        }

        function enableLinkToggle(){
            $('.survey-wrapper > a').removeAttr('disabled');
        }

        function checkIfLinkTogglable(){
            if($('.survey-wrapper > a[data-toggle]').is('[disabled]') && $('.survey-wrapper > a').attr('disabled') == 'disabled'){
                return false;
            }
            return true;
        }

        /*$('.launch-survey').click(function(){
          setTimeout(function(){
            $('#surveyModal .survey-start').focus();
          }, 500);
        });*/

        function toggleSurvey(e) {

            disableLinkToggle();

            $(e.target).prev('.panel-heading').find('a.details-toggle').toggleClass('open');

            var theOptSlides = $(e.target).find('.survey-flexslider ul.slides li').length;

            $(e.target).find('.survey-flexslider').flexslider({
                selector: ".slides > section",
                animation: 'fade',
                slideshow: false,
                keyboard: false,
				animationLoop: false,
                animationSpeed: 100,
                start: function(slider) {
                    $('.survey-flexslider').resize();
                    enableLinkToggle();
                    flexsliderSurveyActiveSlidesADA(slider);
                    //setTimeout(function(){
                        //$('.survey-flexslider .flex-active-slide h4').focus();
                    //}, 200);
                },
                after: function(slider) {
                    _currSlide = slider.currentSlide;
                    flexsliderSurveyActiveSlidesADA(slider);
                    //$('.survey-flexslider').focus();
                    //setTimeout(function(){
                        //$('.survey-flexslider .flex-active-slide h4').focus();
                    //}, 200);
                }
            });

            $('.survey-flexslider .flex-control-nav li a').each(function(index){
              //$(this).html('<span class="visuallyhidden">go to step number</span>'+$(this).html());

              $('.custom-paging').append('<li><a href="javaScript:void(0);"><span class="visuallyhidden">go to step number '+$(this).html()+'</span><span aria-hidden="true">'+$(this).html()+'</span></a></li>');

            });

            $('.survey-flexslider .custom-paging li a').each(function(index){
              $(this).click(function(e){
                e.preventDefault;
                $('.survey-flexslider').flexslider(index);
                setTimeout(function(){
                    $('.survey-flexslider .flex-active-slide .focus-start').focus();
                }, 200);
              });

            });

            $('.survey-flexslider .flex-control-nav').css('display', 'none');


            /*$('.survey-flexslider .flex-control-nav li a').click(function(index){
              setTimeout(function(){
                  $('.survey-flexslider .flex-active-slide .focus-start').focus();
              }, 200);
            });*/

            $('.next-question').click(function() {
                $('.survey-flexslider').flexslider(_currSlide + 1);
                setTimeout(function(){
                    //$('.survey-flexslider .flex-active-slide h4').focus();
                    $('.survey-flexslider .flex-active-slide .focus-start').attr("tabindex", "-1");
                    $('.survey-flexslider .flex-active-slide .focus-start').focus();
                }, 200);
            });

            $('.prev-question').click(function() {
                $('.survey-flexslider').flexslider(_currSlide - 1);
                setTimeout(function(){
                    //$('.survey-flexslider .flex-active-slide h4').focus();
                    $('.survey-flexslider .flex-active-slide .focus-start').attr("tabindex", "-1");
                    $('.survey-flexslider .flex-active-slide .focus-start').focus();
                }, 200);

            });

            $('.finish-survey').click(function(e) {
              e.preventDefault();
              closeSurvey();
            });

            $('.survey-wrapper .close-survey').click(function(e) {
                if(checkIfLinkTogglable()){
                    closeSurvey();
                }
                else{
                    e.preventDefault();
                }
            });

			$('.survey-wrapper input').focusin(function(e) {

              //console.log($(this));

              var theParentli = $(this).closest('li');
              if(!theParentli.hasClass('clone')){
                if(!theParentli.hasClass('flex-active-slide')){
                  var temp = $('.survey-flexslider section p:first').text().slice(0,18);

                  var toSlide = theParentli.index();
                    if('In the last month,'!==temp)
                        $('.survey-flexslider').flexslider(toSlide);
                }
              }

              //var theParentli = $(this).closest('li');

            });
        }

        function closeSurvey(){
            disableLinkToggle();
            //$('.survey .survey-wrapper .close-survey').fadeTo('fast', 0, function(){
                //$('.survey .survey-wrapper .survey-flexslider .flex-control-paging').fadeTo('fast', 0, function(){
                $('.survey .survey-wrapper .survey-flexslider .custom-paging').fadeTo('fast', 0, function(){
                    $('.survey-flexslider').fadeTo('fast' , 0, function(){
                        //$('.survey-wrapper .collapse').collapse('hide');
                        // only enable toggle when all animations are complete
                        enableLinkToggle();
                        $('.survey-title').fadeTo('fast', 0);
                        $('.survey-outro').show().fadeTo('fast', 1);
                        $('.survey-outro .focus-start').focus();
                    }).css('visibility','hidden');
                });
            //});
        }

        function toggleSurveyOpen(e){
            if(checkIfLinkTogglable()){
              $.when(toggleSurvey(e)).done(function(a1) {
                $('.survey-start').on('click',function(e){
                  e.preventDefault();
                  $(this).closest('.survey-intro').fadeTo('fast', 0, function(){
                    $('.survey-title').fadeTo('fast', 1);
                    $('.survey-flexslider').attr('aria-hidden', 'false');
                    $('.survey-flexslider').css('visibility','visible').fadeTo('fast' , 1, function(){
                      setTimeout(function(){
                          //$('.survey .survey-wrapper .survey-flexslider .flex-control-paging').fadeTo('slow', 1);
                          $('.survey .survey-wrapper .survey-flexslider .custom-paging').fadeTo('fast', 1);
                          $('.survey .survey-wrapper .close-survey').fadeTo('fast', 1, function(){
                              // only enable toggle when all animations are complete
                              enableLinkToggle();
                          });
                          $('.survey-flexslider .flex-active-slide .focus-start').attr("tabindex", "-1");
                          $('.survey-flexslider .flex-active-slide .focus-start').focus();
                      }, 100);
                    });
                  }).hide();
                });
              });
            }
            else{
                e.preventDefault();
            }
        }

        function checkToggleSurvey(e){
            if(checkIfLinkTogglable()){
                if($(e.target).attr('aria-expanded')=='true'){
                    closeSurvey();
                    return false;
                }
            }
            else{
                e.preventDefault();
            }

        }

        if ($('#collapseSurvey').length) {
            $('.survey-wrapper > a').on('click', checkToggleSurvey);
            $('#collapseSurvey').on('show.bs.collapse', toggleSurveyOpen);

            $('.details-toggle').click(function() {
                $('.panel-collapse.in').collapse('hide');
            });

            $('#collapseSurvey').collapse('show');
        }
    }

    function initBootstrapAccordionWithoutPanelFix() {
        var accordionGroups = [
            '#contactUsFAQ'
        ];

        $.each(accordionGroups, function(index, value) {
            var collapseTargets = value + ' .collapse';
            $(collapseTargets).on('show.bs.collapse', function() {
                //console.log("something is happening...");
                var actives = $(value).find('.in, .collapsing');
                actives.each(function(index, element) {
                    $(element).collapse('hide');
                });
            });
        });

    }

    function initMyDeals(){
      if($('#redirectRestaurantLocator').length){
        $('#redirectRestaurantLocator').modal('show');
      }
    }

    function initStars(){
      if($('.star-detail').length){
        $('.star-detail').on('click keydown', function(e){
          if(e.type == 'click' || (e.type == 'keydown' && (e.keyCode == 13 || e.keyCode == 32))){
            e.preventDefault();
            var targetElemStr = $(this).attr('href');
            $(targetElemStr).attr('tabindex','-1').focus();
            $( "<div role='alert' class='visuallyhidden'>"+$(targetElemStr).find('.visuallyhidden').html()+"</div>" ).appendTo( "body" );
          }
        });
      }
    }

    function initOffers() {
        if ($('.page-offers').length) {
            $('#collapseOne').on('shown.bs.collapse', function() {
                $('.cont-form .cont-heading').hide();

                $('html, body').animate({
                    scrollTop: $(document).height()
                }, 500);
            });

            $('#collapseOne').on('hidden.bs.collapse', function() {
                $('.cont-form .cont-heading').show();

                $('html, body').animate({
                    scrollTop: $(document).height()
                }, 500);
            });
        }
    }


    /*
     *
     * CUSTOM VIDEO PLAYER FUNCTIONS
     *
     */


    // This function creates an <iframe> (and YouTube player)
    // after the API code downloads.
    var player;
    var player_title;
    var player_videos; //['cjZEV1W6P_A','lAPtjaeaD7w','eWuQFeSdBNs','AZq5DKjSvcs','B9__89vfwMI'];
    var player_video_data = [];

    window.onYouTubeIframeAPIReady = function() {
        if($('.component-video').length){
	        if (player_videos) {
	            if(player_videos.length < 1){
	                parseVideoList();
	            }

	            player = new YT.Player('player', {
	                height: '390',
	                width: '640',
	                videoId: player_videos[0],
	                events: {
	                    'onReady': onPlayerReady,
	                    'onStateChange': onPlayerStateChange
	                }
	            });
	        }
        }
    };

    // The API will call this function when the video player is ready.
    function onPlayerReady(event) {
        player_title = player.getVideoData().title;

        // set title of current video
        $('.component-video .video-title').html(player_title);


        //event.target.playVideo();
    }

    //    The API calls this function when the player's state changes.
    //    The function indicates that when playing a video (state=1),
    //    the player should play for six seconds and then stop.
    var done = false;
    function onPlayerStateChange(event) {
        if (event.data == YT.PlayerState.PLAYING && !done) {
          //setTimeout(stopVideo, 6000);
          done = true;
        }
    }

    function playVideo() {
        player.playVideo();
    }

    function pauseVideo() {
        player.pauseVideo();
    }

    function stopVideo() {
        player.stopVideo();
    }

    function loadDifferentVideo(newVideoId){
        player.loadVideoById(newVideoId);

        // set title of current video
        $('.component-video .video-title').html(player_title);
    }

    // get video view stats using API
    function getYoutubeData(videoId) {
        // This only a temporary API key. It will need to be replaced by McDonalds'.
        var apiKey = 'AIzaSyDeKiNNLgttxVX4Pw2jnHM0D7WWPjt7AAA';
        var fetchurl = 'https://www.googleapis.com/youtube/v3/videos?part=snippet,statistics&id=' + videoId;
        fetchurl = fetchurl + '&key=' + apiKey;

        $.ajax({
            url: fetchurl,
            dataType: 'jsonp',
            success: function (data) {
                _handleYoutubeAjaxData(data);
            }
        });

    }
    function _handleYoutubeAjaxData(data){
        var videoObj = data.items;
        $(videoObj).each(function(index,element){
            var videoDetails = {
                viewcount : element.statistics.viewCount,
                title: element.snippet.title,
                thumbnail: element.snippet.thumbnails["default"].url,
                videoid: element.id
            };
            player_video_data.push(videoDetails);
        });
        videoModalPopulate(player_video_data);
    }

    function videoModalPopulate(videoData){
        var content = '';
        if(videoData.length < 2){
          $('.component-video .cont-playlist .list-unstyled').hide();
          $('.component-video .cont-playlist').css('width','650px');
        }
        $(videoData).each(function(index, element){

            if(index == 0){
                content +=  '<li tabindex="0" aria-label="press the enter or space key, or click to play this video" data-videoid="'+element.videoid+'" class="active">';
            }
            else{
                content +=  '<li tabindex="0" aria-label="press the enter or space key, or click to play this video" data-videoid="'+element.videoid+'">';
            }
            content +=      '<div class="media">';
            content +=          '<div class="media-left media-middle">';
            content +=                 '<img class="media-object" src="'+element.thumbnail+'" alt="">';
            content +=          '</div>';
            content +=          '<div class="media-body media-middle">';
            content +=              '<h4 class="media-heading">'+element.title+'</h4>'+element.viewcount+' Views';
            content +=          '</div>';
            content +=      '</div>';
            content +=  '</li>';
        });
        if($('.component-video .cont-playlist .list-unstyled').length){
            $('.component-video .cont-playlist .list-unstyled').html(content);
            videoModalClicks();
        }
    }

    function videoModalClicks(){
        // handle video changes on clicking thumbnails
        $('.component-video .cont-playlist .list-unstyled li').on('click keydown', function(e){

          if(e.type == 'click' || (e.type == 'keydown' && (e.keyCode == 13 || e.keyCode == 32))){
            var curr = e.currentTarget;
            var clickedVideoId = $(curr).attr('data-videoid');
            //console.log(clickedVideoId);

            if($(curr).hasClass('active')){
                // do nothing if video is already playing
            }
            else{
                loadDifferentVideo(clickedVideoId);
                $('.component-video .cont-playlist .list-unstyled li.active').removeClass('active');
                $(curr).addClass('active');
            }
          }

        });
    }

    function parseVideoList(){
        if($('.component-video').length){
            // get list and set up videos
            var videoArr = $('.component-video #data-videos').attr('data-videos');
            getYoutubeData(videoArr);
            player_videos = videoArr.split(',');
        }
    }

    function initHomeYoutubeAPICalls(){
        if($('.component-video').length){

            // This code loads the IFrame Player API code asynchronously.
            var tag = document.createElement('script');

            tag.src = 'https://www.youtube.com/iframe_api';
            var firstScriptTag = document.getElementsByTagName('script')[0];
            firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

            parseVideoList();

            // stop videos on closing modal
            $('#videoModal').on('hidden.bs.modal', function () {
                pauseVideo();
            });

        }
    }


    /*
     *
     * END CUSTOM VIDEO PLAYER FUNCTIONS
     *
     */





    function initRestaurantLocatorModal(){
      var $mapModal = $('.modal#restaurantLocatorMapModal');
      var $formModal = $('.modal#restaurantLocatorFormModal');
      var $forms = $('#restaurantLocatorFormContainer');
      var myOptions,map,marker;

      $('.restaurantLocatorModalSlider').flexslider();

      function toggleFormCollapse(selectedRestaurant){
        if(selectedRestaurant){
          $mapModal.modal('hide');
          $forms.find('.restaurantLocatorForm').collapse('show');
        } else {
          $forms.find('.restaurantLocatorOpenModalBtn').collapse('show');
        }
      }

      function enableFormInputs(){
            $forms.find('.restaurantLocatorForm input[disabled]').each(function(index, value){
                $(this).removeAttr('disabled');
            });

            $forms.find('.restaurantLocatorForm button[disabled]').each(function(index, value){
                $(this).removeAttr('disabled');
            });

            $forms.find('.restaurantLocatorForm select[disabled]').each(function(index, value){
                $(this).removeAttr('disabled');
            });

            $forms.find('.restaurantLocatorForm .disabled').each(function(index, value){
                $(this).removeClass('disabled');
            });

        }

      function disableFormInputs(){
            $forms.find('.restaurantLocatorForm input[data-disable]').each(function(index, value){
                $(this).attr('disabled', 'disabled');
            });

            $forms.find('.restaurantLocatorForm button[data-disable]').each(function(index, value){
                $(this).attr('disabled', 'disabled');
            });

            $forms.find('.restaurantLocatorForm select[disabled]').each(function(index, value){
                $(this).attr('disabled', 'disabled');
            });

            $forms.find('.restaurantLocatorForm .disabled').each(function(index, value){
                $(this).addClass('disabled');
            });
        }

      $mapModal.each(function(index, el){
        var $this = $(this);
        var $locations = $this.find('.restaurant-location');

        $locations.each(function(index, el){
          var $this = $(this);
          var $select = $this.find('.select-restaurant-location');
          $select.click(function(e){
            // selecting a a location will show the form and disable the inputs
            e.preventDefault();
            toggleFormCollapse(true);
            initDatepicker();
            disableFormInputs();
          });
        });
      });

      $('#restaurantLocatorFormContainer').each(function(index, el){
        var $el = $(this);
      });


      // modal: clicking cant find will close the modal and enable the inputs
      $('.modal#restaurantLocatorMapModal .cant-find').on('click', function(e){
        e.preventDefault();
        toggleFormCollapse(true);
        initDatepicker();
        enableFormInputs();
      });

      // form: clicking cant find will enable the inputs
      $('.restaurantLocatorOpenModalBtn .cant-find').on('click', function(e){
        initDatepicker();
        enableFormInputs();
      });

      // clicking GO displays list of locations
      $('.modal#restaurantLocatorMapModal .cont-enter-location .btn-red').on('click', function(e){
        e.preventDefault();
        if($('.modal#restaurantLocatorMapModal .cont-restaurant-list').hasClass('col-xs-0') && $("#zip-city-state").val() != ""){
          $('.modal#restaurantLocatorMapModal .cont-restaurant-list').attr('aria-hidden', 'false');
            $('.modal#restaurantLocatorMapModal .cont-restaurant-list').removeClass('col-xs-0').addClass('col-xs-12 col-sm-5');
            $('.modal#restaurantLocatorMapModal .cont-map-display').removeClass('col-sm-12').addClass('col-sm-7');
            google.maps.event.trigger(map, 'resize');
            setTimeout(function(){
              $('.modal#restaurantLocatorMapModal .restaurant-location-modal-list').focus();
            }, 800);
        }
        if($(".modal#restaurantLocatorMapModal .results-num").length > 0){
        	setTimeout(function(){
                $('.modal#restaurantLocatorMapModal .results-num').focus();
        	}, 1500);
        }
      });

      $.fn.selectRange = function(start, end) {
        return this.each(function() {
          if (this.setSelectionRange) {
              this.focus();
              this.setSelectionRange(start, end);
          } else if (this.createTextRange) {
              var range = this.createTextRange();
              range.collapse(true);
              range.moveEnd('character', end);
              range.moveStart('character', start);
              range.select();
          }
        });
      };



      $('.modal#restaurantLocatorMapModal').on('shown.bs.modal', function () {
        setTimeout(function(){
          $('.modal#restaurantLocatorMapModal #zip-city-state').selectRange(0,0);
        }, 200);
      });

    }

    function initOnlyOneModal(){
      var modalUniqueClass = '.modal';
      $('.modal').on('show.bs.modal', function() {
        var $element = $(this);
        var $uniques = $(modalUniqueClass + ':visible').not($(this));
        if ($uniques.length) {
          $uniques.modal('hide');
          $uniques.one('hidden.bs.modal', function() {
            $element.modal('show');
          });
          return false;
        }
      });
    }

    function initDatepicker(){
        if($('input.datepicker').length > 0){

            $('input.datepicker').each(function(index, value){

                if(! $(this).hasClass('initialized-date-picker')){

                    var picker = $(this).datepicker({
                        format: 'mm-dd-yyyy'
                    }).on('changeDate', function(ev) {
                        picker.hide();
                    }).data('datepicker');

                    $(this).addClass('initialized-date-picker');
                }

            });

            // make calendar icon clickable
            $('.input-icon-right .fa-calendar').on('click', function(e){
                $(e.target).closest('.form-group').find('.datepicker').datepicker('show');
            });

        }
    }


    var globalDisabledHandle;
    var globalHiddenHandle;
    var containmentElm = null;
    var containmentPrevElm = null;

    function setContainment(elm){
      console.log("set containment");

      //remove all containment
      try{
        globalDisabledHandle.disengage();
        globalHiddenHandle.disengage();
      }catch(err){
        console.log("nothing to disengage");
      }

      //if there was previous containment
      if(containmentElm != null){
        containmentPrevElm = containmentElm;
      }

      containmentElm = elm;
      globalDisabledHandle = ally.maintain.disabled({
      	filter: containmentElm,
      });
      globalHiddenHandle = ally.maintain.hidden({
      	filter: containmentElm,
      });
    }

    function removeContainment(){
      //remove all containment
      console.log("remove containment");
      try{
        globalDisabledHandle.disengage();
        globalHiddenHandle.disengage();
        console.log("disengage handles")
      }catch(err){
        console.log("nothing to disengage");
      }

      //if containment needs to return to previous
      if(containmentPrevElm != null){
        console.log("returning to prev contain");
        containmentElm = containmentPrevElm;
        containmentPrevElm = null;
        globalDisabledHandle = ally.maintain.disabled({
        	filter: containmentElm,
        });
        globalHiddenHandle = ally.maintain.hidden({
        	filter: containmentElm,
        });
      }else{
        containmentElm = null;
      }

    }

    function getContainmentState(){
      return containmentElm;
    }




    function initAccessibilityModules(){
      // Accessibility Extensions
      // ===============================

    	 $(".nav-skip-to-content").click(function(event){

    	        var skipTo="#"+this.href.split('#')[1];

    	        $(skipTo).attr('tabindex', -1).on('blur focusout', function () {
    	          $(this).removeAttr('tabindex');
    	        }).focus();

    	 });


      // GENERAL UTILITY FUNCTIONS
      // ===============================

      var uniqueId = function(prefix) {
          return (prefix || 'ui-id') + '-' + Math.floor((Math.random()*1000)+1)
      }


      var removeMultiValAttributes = function (el, attr, val) {
       var describedby = (el.attr( attr ) || "").split( /\s+/ )
          , index = $.inArray(val, describedby)
       if ( index !== -1 ) {
         describedby.splice( index, 1 )
       }
       describedby = $.trim( describedby.join( " " ) )
       if (describedby ) {
         el.attr( attr, describedby )
       } else {
        el.removeAttr( attr )
       }
      }

      var focusable = function ( element, isTabIndexNotNaN ) {
        var map, mapName, img,
        nodeName = element.nodeName.toLowerCase();
        if ( "area" === nodeName ) {
        map = element.parentNode;
        mapName = map.name;
        if ( !element.href || !mapName || map.nodeName.toLowerCase() !== "map" ) {
        return false;
        }
        img = $( "img[usemap='#" + mapName + "']" )[ 0 ];
        return !!img && visible( img );
        }
        return ( /input|select|textarea|button|object/.test( nodeName ) ?
        !element.disabled :
        "a" === nodeName ?
        element.href || isTabIndexNotNaN :isTabIndexNotNaN) && visible( element ); // the element and all of its ancestors must be visible
      }
      var visible = function ( element ) {
        return $.expr.filters.visible( element ) &&
          !$( element ).parents().addBack().filter(function() {
            return $.css( this, "visibility" ) === "hidden";
          }).length;
      }

      $.extend( $.expr[ ":" ], {
        data: $.expr.createPseudo ?
          $.expr.createPseudo(function( dataName ) {
            return function( elem ) {
              return !!$.data( elem, dataName );
            };
          }) :
          // support: jQuery <1.8
          function( elem, i, match ) {
            return !!$.data( elem, match[ 3 ] );
          },

        focusable: function( element ) {
          return focusable( element, !isNaN( $.attr( element, "tabindex" ) ) );
        },

        tabbable: function( element ) {
          var tabIndex = $.attr( element, "tabindex" ),
            isTabIndexNaN = isNaN( tabIndex );
          return ( isTabIndexNaN || tabIndex >= 0 ) && focusable( element, !isTabIndexNaN );
        }
      });



      // Modal Extension
      // ===============================

      $('.modal-dialog').attr( {'role' : 'document'})
        var modalhide =   $.fn.modal.Constructor.prototype.hide
        $.fn.modal.Constructor.prototype.hide = function(){
           modalhide.apply(this, arguments)
           $(document).off('keydown.bs.modal')
        }

        var modalfocus =   $.fn.modal.Constructor.prototype.enforceFocus
        $.fn.modal.Constructor.prototype.enforceFocus = function(){
          var $content = this.$element.find(".modal-content")
          var focEls = $content.find(":tabbable")
          , $lastEl = $(focEls[focEls.length-1])
          , $firstEl = $(focEls[0])
          $lastEl.on('keydown.bs.modal', $.proxy(function (ev) {
            if(ev.keyCode === 9 && !(ev.shiftKey | ev.ctrlKey | ev.metaKey | ev.altKey)) { // TAB pressed
              ev.preventDefault();
              $firstEl.focus();
            }
          }, this))
          $firstEl.on('keydown.bs.modal', $.proxy(function (ev) {
              if(ev.keyCode === 9 && ev.shiftKey) { // SHIFT-TAB pressed
                ev.preventDefault();
                $lastEl.focus();
              }
          }, this))
          modalfocus.apply(this, arguments)
        }


      var disabledHandle;
      var hiddenHandle;
      var tabHandle;

      $('.modal:not(#restaurantLocatorMapModal)').on('shown.bs.modal', function (e) {

        setTimeout(function(){
          if($(e.target).find('.modal-dialog .modal-focus-first').length){
            $(e.target).find('.modal-dialog .modal-focus-first').focus();
            console.log("first");
          }else{
            console.log("no first");
            var firstTabbableinModal = ally.query.firstTabbable({
              context: e.target,
              defaultToContext: true,
              strategy: 'quick',
            });
            firstTabbableinModal.focus();
          }

        }, 200);

        setContainment($(this));

        // create or show the dialog
        $(this).hidden = false;
      });

      $('.modal:not(#restaurantLocatorMapModal)').on('hide.bs.modal', function (e) {
        // undo disabling elements outside of the dialog
        //disabledHandle.disengage();
        //hiddenHandle.disengage();
        removeContainment();
        // hide or remove the dialog
        $(this).hidden = true;

      });


      // End Modal Extensions

      // Dropdown extentions

      var disabledHandleDropdown;
      var hiddenHandleDropdown;
      var tabHandleDropdown;

      $('.select').on('shown.bs.select', function (e) {
    	  var theParentToggle = $(this);
        var theParentToggleButton = $(this).find("button").first();

    	  var firstElm = ally.query.firstTabbable({
	          context: theParentToggle,
	          defaultToContext: true,
	          strategy: 'quick',
    	  });

        setContainment($(this));
    	  });


      $('.select').on('hidden.bs.select', function (e) {
        var contextClass = $(this).attr("class").toString();

        //disabledHandleDropdown.disengage();
        //hiddenHandleDropdown.disengage();

        var distractor = $("main");
        var theItem = $(this).find('button').first();

        removeContainment();

        theItem.focus();
        ally.element.focus(theItem);


      });



      // End dropdown extentions

      // Tab Extension
      // ===============================

      var $tablist = $('.nav-tabs, .nav-pills')
              , $lis = $tablist.children('li')
              , $tabs = $tablist.find('[data-toggle="tab"], [data-toggle="pill"]')

          if($tabs){
            $tablist.attr('role', 'tablist')
            //$lis.attr('role', 'presentation')
            //$tabs.attr('role', 'tab')
            $lis.attr('role', 'tab')
            $tabs.removeAttr('role')
          }

          $tabs.each(function( index ) {
            var tabpanel = $($(this).attr('href'))
              , tab = $(this)
              , tabid = tab.attr('id') || uniqueId('ui-tab')

              tab.attr('id', tabid)

            if(tab.parent().hasClass('active')){
              tab.attr( { 'tabIndex' : '0', 'aria-selected' : 'true', 'aria-controls': tab.attr('href').substr(1) } )
              tabpanel.attr({ 'role' : 'tabpanel', 'tabIndex' : '0', 'aria-hidden' : 'false', 'aria-labelledby':tabid })
              //tabpanel.attr({ 'role' : 'tabpanel', 'aria-hidden' : 'false', 'aria-labelledby':tabid })
            }else{
              tab.attr( { 'tabIndex' : '-1', 'aria-selected' : 'false', 'aria-controls': tab.attr('href').substr(1) } )
              tabpanel.attr( { 'role' : 'tabpanel', 'tabIndex' : '-1', 'aria-hidden' : 'true', 'aria-labelledby':tabid } )
            }
          })

          $.fn.tab.Constructor.prototype.keydown = function (e) {
            var $this = $(this)
            , $items
            , $ul = $this.closest('ul[role=tablist] ')
            , index
            , k = e.which || e.keyCode
            , $currtab

            $this = $(this)
            //if (!/(37|38|39|40)/.test(k)) return
            if (!/(37|39|40)/.test(k)) return

//            $items = $ul.find('[role=tab]:visible')
            $items = $ul.find('[role=tab]:visible a')
            index = $items.index($items.filter(':focus'))

            $currtab = $items.eq(index)
            //console.log($currtab)

            if (k == 38 || k == 37) index--                         // up & left
            if (k == 39 || k == 40) index++                        // down & right
            //if (k == 37) index--                         // up & left
            //if (k == 39) index++                        // down & right

            if(index < 0) index = $items.length -1
            if(index == $items.length) index = 0

            var nextTab = $items.eq(index)
//            if(nextTab.attr('role') ==='tab'){
            if(nextTab.parent().attr('role') ==='tab'){

              //nextTab.tab('show')      //Comment this line for dynamically loaded tabPabels, to save Ajax requests on arrow key navigation
              //.focus()

              setTimeout(function(){ nextTab.focus(); }, 50);
            }
            // nextTab.focus()
            /*if(k == 40){
              console.log($currtab.data('target'))
              setTimeout(function(){ $($currtab.data('target')).focus() }, 100);
            }*/

            e.preventDefault()
            e.stopPropagation()
          }

          $(document).on('keydown.tab.data-api','[data-toggle="tab"], [data-toggle="pill"]' , $.fn.tab.Constructor.prototype.keydown)

         var tabactivate =    $.fn.tab.Constructor.prototype.activate;
         $.fn.tab.Constructor.prototype.activate = function (element, container, callback) {
            var $active = container.find('> .active')
            $active.find('[data-toggle=tab], [data-toggle=pill]').attr({ 'tabIndex' : '-1','aria-selected' : false })
            $active.filter('.tab-pane').attr({ 'aria-hidden' : true,'tabIndex' : '-1' })

            tabactivate.apply(this, arguments)

            element.addClass('active')
            element.find('[data-toggle=tab], [data-toggle=pill]').attr({ 'tabIndex' : '0','aria-selected' : true })
            element.filter('.tab-pane').attr({ 'aria-hidden' : false,'tabIndex' : '0' })
         }

         /*$('.tab-pane.active').on('keydown', function(e) {
           if(e.type == 'keydown' && e.keyCode == 38){
             if($(document.activeElement).hasClass('tab-pane')){
               var theparenttab = $(this).attr('aria-labelledby');
               setTimeout(function(){
                 $('#'+theparenttab).focus();
               }, 200);
             }

           }
         });*/


         // DROPDOWN Extension

      // End Tab Extension



      // Collapse Extension
      // ===============================

      var $colltabs =  $('[data-toggle="collapse"]')
            $colltabs.each(function( index ) {
              var colltab = $(this)
              , collpanel = (colltab.attr('data-target')) ? $(colltab.attr('data-target')) : $(colltab.attr('href'))
              , parent  = colltab.attr('data-parent')
              , collparent = parent && $(parent)
              , collid = colltab.attr('id') || uniqueId('ui-collapse')

                colltab.attr('id', collid)

                if(collparent){
				   colltab.attr({ 'role':'tab', 'aria-selected':'false', 'aria-expanded':'false' })
                   //$(collparent).find('div:not(.collapse,.panel-body), h4').attr('role','presentation')
                   collparent.attr({ 'role' : 'tablist', 'aria-multiselectable' : 'true' })

                  if(collpanel.hasClass('in')){
                    colltab.attr({ 'aria-controls': collpanel.attr('id'), 'aria-selected':'true', 'aria-expanded':'true', 'tabindex':'0' })
                    collpanel.attr({ 'role':'tabpanel', 'tabindex':'0', 'aria-labelledby':collid, 'aria-hidden':'false' })
                    collpanel.removeAttr('tabIndex');
                  }else{
                    colltab.attr({'aria-controls' : collpanel.attr('id'), 'tabindex':'0' }) // -1
                    collpanel.attr({ 'role':'tabpanel', 'tabindex':'0', 'aria-labelledby':collid, 'aria-hidden':'true' })// -1
                    collpanel.removeAttr('tabIndex');
                  }
                }
            })

      var collToggle = $.fn.collapse.Constructor.prototype.toggle
      $.fn.collapse.Constructor.prototype.toggle = function(){
              var prevTab = this.$parent && this.$parent.find('[aria-expanded="true"]') , href

              if(prevTab){
                var prevPanel = prevTab.attr('data-target') || (href = prevTab.attr('href')) && href.replace(/.*(?=#[^\s]+$)/, '')
                , $prevPanel = $(prevPanel)
                , $curPanel = this.$element
                , par = this.$parent
                , curTab

              if (this.$parent) curTab = this.$parent.find('[data-toggle=collapse][href="#' + this.$element.attr('id') + '"]')

              collToggle.apply(this, arguments)

              if ($.support.transition) {
                this.$element.one($.support.transition.end, function(){

                    prevTab.attr({ 'aria-selected':'false','aria-expanded':'false', 'tabIndex':'0' }) // -1
                    $prevPanel.attr({ 'aria-hidden' : 'true','tabIndex' : '0'}) // -1

                    curTab.attr({ 'aria-selected':'true','aria-expanded':'true', 'tabIndex':'0' })

                    prevTab.removeAttr('tabIndex');
                    $prevPanel.removeAttr('tabIndex');
                    curTab.removeAttr('tabIndex');

                    if($curPanel.hasClass('in')){
                      $curPanel.attr({ 'aria-hidden' : 'false','tabIndex' : '0' })
                      $curPanel.removeAttr('tabIndex');
                    }else{
                      curTab.attr({ 'aria-selected':'false','aria-expanded':'false'})
                      $curPanel.attr({ 'aria-hidden' : 'true','tabIndex' : '0' }) // -1
                    }
                })
              }
            }else{
              collToggle.apply(this, arguments)
            }
          }

      $.fn.collapse.Constructor.prototype.keydown = function (e) {
            var $this = $(this)
            , $items
            , $tablist = $this.closest('div[role=tablist] ')
            , index
            , k = e.which || e.keyCode

            $this = $(this)
            if (!/(32|37|38|39|40)/.test(k)) return
            if(k==32) $this.click()

            $items = $tablist.find('[role=tab]')
            index = $items.index($items.filter(':focus'))

            if (k == 38 || k == 37) index--                                        // up & left
            if (k == 39 || k == 40) index++                        // down & right
            if(index < 0) index = $items.length -1
            if(index == $items.length) index = 0

            $items.eq(index).focus()

            e.preventDefault()
            e.stopPropagation()

          }

      $(document).on('keydown.collapse.data-api','[data-toggle="collapse"]' ,  $.fn.collapse.Constructor.prototype.keydown);


      $('.restaurant-location [data-toggle="collapse"]').click(function(){
        try{
          if($(this).hasClass("collapsed")){
            $( "<div role='alert' class='visuallyhidden'>"+adaGenericText.expandedtext+"</div>" ).appendTo( "body" );
          }else{
            $( "<div role='alert' class='visuallyhidden'>"+adaGenericText.collapsedtext+"</div>" ).appendTo( "body" );
          }
        } catch(ex) {}

      });


      // End Collapse Extension




      // Dropdown Extensions
      // ===============================




      // On dropdown open
      $(document).on('shown.bs.dropdown', function(event) {
        var dropdown = $(event.target);

        // Set aria-expanded to true
        dropdown.find('.dropdown-menu').first().attr('aria-expanded', true);
      });
      // On dropdown close
      $(document).on('hidden.bs.dropdown', function(event) {
       var dropdown = $(event.target);

       // Set aria-expanded to false
       dropdown.find('.dropdown-menu').first().attr('aria-expanded', false);


      });

      /*$(document).on('shown.bs.modal', function(event) {
        event.stopPropagation();
        setTimeout(function(){
          $('#test123').focus();
        }, 1000);

        console.log("test");
      });

      $( "#test123" ).focus(function(e) {
        e.stopPropagation();
      });

      */

      //for disjointed dropdowns
      $('.navbar .dropdown').mousedown(function() {
        //console.log('mousedown');
        $(this).find('ul.dropdown-menu').addClass('no-ring');
      });
      $('.navbar .dropdown').keydown(function() {
        $(this).find('ul.dropdown-menu').removeClass('no-ring');
      });

      $('.navbar .dropdown').on('shown.bs.dropdown',function(e){
        var target = $(this);
        setTimeout(function() {
          try{
        	  //target.attr('tabindex','0').focus().removeAttr('tabindex');
              //target.find('ul.dropdown-menu').attr('tabindex','0').focus();
            //target.find('ul.dropdown-menu').focus();
          } catch(ex) {}
        }, 200);
      });

      //for disjointed dropdowns
      $('.navbar .dropdown').keydown(function(e){
        if(e.keyCode == 27){
          var parentTargetName = $(this).attr('id');
          //setTimeout(function() {
          //}, 200);
          try{
            $("button[data-target='#" + parentTargetName +"']").focus();
          } catch(ex) {}
        }

      });


      $('.star-detail').click(function(){
        setTimeout(function() {
          $('#star-detail').focus();
        }, 200);
      });


      /*$(':checkbox').click(function() {
        $(this).trigger("change");
        console.log("change");
      });*/

      $( ":checkbox" ).not('.sso-checkbox').keydown(function(e) {
        console.log("line 3187:checkbox keydown" + e.keyCode );

        if(e.keyCode == 32 || e.keyCode == 13){
          e.preventDefault();
          $(this).trigger("change");
        }

      });

      /*$( ":radio" ).keydown(function(e) {
        if(e.keyCode == 32 || e.keyCode == 13){
          e.preventDefault();
          $(this).trigger("change");
        }
      });*/


      $(':checkbox').not('.sso-checkbox').change(function(e) {
        console.log("change");
        if($(this).attr("aria-checked") == "false"){
          $(this).attr("aria-checked", "true");
          $(this).prop("checked", true);
        }else{
          $(this).attr("aria-checked", "false");
          $(this).prop("checked", false);
        }



      });

      $('.navbar .top-components .dropdown .checkbox label').click(function(e){
        $(this).parent().find(':checkbox').trigger("change");
      });

      /*$('.radio label').click(function(e){
        $(this).parent().find(':radio').trigger("change");
      });*/


      if($('.contact-faq').length){
        $('.contact-faq li').on('shown.bs.collapse', function () {
          var linkcopy = $(this).find('.q-a__question a').html();
          var headingcopy = $('.contact-faq h3').html();
          $( "<div role='alert' class='visuallyhidden'>"+headingcopy +", "+ linkcopy+" expanded</div>" ).appendTo( "body" );
        })
      }


      if($('.search-results-cont').length){
        $('.load-more-btn').click(function (e) {

          //console.log($('.search-results-cont .first-of-set').last().html());
          setTimeout(function(){
            var scrollTop     = $(window).scrollTop(),
                elementOffset = $('.search-results-cont .first-of-set').last().offset().top,
                distance      = (elementOffset - scrollTop);

            if(distance < 183){
              $(window).scrollTop(scrollTop - 183);
            }
            $('.search-results-cont .first-of-set').last().find(':tabbable').first().focus();
          }, 400);

        });
      }


      // End Dropdown Extensions

      //offers form error refocus
      $('.focusError').click(function(e){
        e.preventDefault();
        var clickedFormLink = $(this);

        setTimeout(function(){
          //var firstError = $('.messageError').filter(function() { return $(this).attr('style') == ''; });
          var firstErrorField = $('label.hasErrors').next('input');
          var firstError = $('label.hasErrors').first();

          $(firstErrorField).focus();


        }, 400);
      });


    } //end accessibility extentions


  /*    function cookieMessageClose(){
      if($('.cookie-message.shown').length){

        var cbannerHeight = $(".cookie-message").outerHeight();
        $('body').addClass("cookie-banner");
        $('body').css("margin-top", "0");

        $('.cookie-message-close').click(function(e) {
          e.preventDefault();
          $('body').removeClass("cookie-banner");
          $('body').css("margin-top", "0");
          $('.cookie-message').removeClass( 'shown' );
          $('.cookie-message').slideUp( 200 );
          $('header a:not(.not-first)').first().focus();

        });

      }
    }*/

    // COOKIE & SMARTAPP BANNER EVENTS
    var cookieBoxHeight;
    var smartBannerHeight;
    var cookieNav;
    var headerHeightMobile = 92;
    var headerHeightDesktop = 110;

    function cookieSmart() {
        if( $("#cookieMessage").is(':visible') && $("#smartbanner").is(':visible') ){
            cookieBoxHeight = $("#cookieMessage").outerHeight();
            smartBannerHeight = $("#smartbanner").outerHeight();
            cookieNav = $('.header .navbar .navbar-header-wrapper').outerHeight();
            if (window.innerWidth < 768) {
                if($('.restaurantLocator').length > 0){
                    $('.restaurantLocator').css("padding-top", cookieBoxHeight + smartBannerHeight + headerHeightMobile);
                    $("#maincontent").css("margin-top", 0);
        }else{
                    $("#maincontent").css("margin-top", cookieNav);
                }
            }
            else {
                $("#maincontent").css("margin-top", cookieNav);
                if ($('.navbar.navbar-fixed-top').hasClass('smaller')) {
                    $("#component-nearest-location-desktop .dropdown-menu").css("top", cookieBoxHeight + smartBannerHeight + 80);
                }
                else {
                    $("#component-nearest-location-desktop .dropdown-menu").css("top", cookieBoxHeight + smartBannerHeight + headerHeightDesktop);
                }
        }
    }
        else if ($("#cookieMessage").is(':visible')) {
            cookieBoxHeight = $("#cookieMessage").outerHeight();
            if (window.innerWidth < 768) {
                if($('.restaurantLocator').length > 0){
                    $('.restaurantLocator').css("padding-top", cookieBoxHeight+headerHeightMobile);
                    $("#maincontent").css("margin-top", 0);
        }else{
                    $("#maincontent").css("margin-top", headerHeightMobile);
                }

        }
            else {
                $("#maincontent").css("margin-top", 0);
                if($('.restaurantLocator').length > 0){
                    $('.restaurantLocator').css("padding-top", cookieBoxHeight+headerHeightDesktop);
                }
                if ($('.navbar.navbar-fixed-top').hasClass('smaller')) {
                    $("#component-nearest-location-desktop .dropdown-menu").css("top", cookieBoxHeight + 80);
                }
                else {
                    $("#component-nearest-location-desktop .dropdown-menu").css("top", cookieBoxHeight + headerHeightDesktop);
                }
    }
        }
        else if ($("#smartbanner").is(':visible')) {
            smartBannerHeight = $("#smartbanner").outerHeight();
            if (window.innerWidth < 768) {
                $("#maincontent").css("margin-top", smartBannerHeight);
            }
            else {
                $("#maincontent").css("margin-top", smartBannerHeight);
                if ($('.navbar.navbar-fixed-top').hasClass('smaller')) {
                    $("#component-nearest-location-desktop .dropdown-menu").css("top", smartBannerHeight + 80);
                }
                else {
                    $("#component-nearest-location-desktop .dropdown-menu").css("top", smartBannerHeight + headerHeightDesktop);
                }
            }
        }
        else {
            $("#maincontent").css("margin-top", 0);
            $("#component-nearest-location-desktop .dropdown-menu").css('top', '');
        }
    }

    function cookieMessageClose() {
        $('#cookieMessage').hide();
        if($('.restaurantLocator').length > 0){
           $('.restaurantLocator').removeAttr("style");
        }
       cookieSmart();
    }

    function smartBannerClose() {
        $('#smartbanner').hide();
        if($('.restaurantLocator').length > 0){
           $('.restaurantLocator').removeAttr("style");
        }
        cookieSmart();
    }

    $('#cookieMessage .cookie-message-close').on('click', function(){
        cookieMessageClose();
    });

    $('#smartbanner .sb-close').on('click', function(){
        smartBannerClose();
    });



    $(function() {
        //$('a[data-toggle="tooltip"]').tooltip();
        $('a[data-toggle="popover"]').popover();

        initFancybox();
        //videosList.initFlexsliders();		/*This is not required for home carousel video */
        initComponentCarouselBackground();
        //initInputPlaceholders();      /*This is commented as assigning the value to placeholder is not required.(issue in pref center)*/
        initZoomAnim('.zoom-anim', '.zoom-anim-target', 'bglarge', '.zoom-anim-parent');
        initNavDropdown();
        initNavLocateFilters();
        initRestaurantLocations();
        initRestarauntFinderTemplate();
        initNutritionCalculateTab();
        initMcPick();
        initContactUs();
        initSurvey();
        initBootstrapAccordionWithoutPanelFix();
        initOffers();
        videosList.initHomeYoutubeAPICalls();
        initRestaurantLocatorModal();
        initOnlyOneModal();
        initNavMobileSmallTabs();
        initDatepicker();
        //initPortraitLandscapeCheck();
        initStars();
        initMyDeals();
        initAdaGenericText();
        initAccessibilityModules();
       // cookieMessageClose();
	   cookieSmart();

        setTimeout(function() {
            initFadeinAnimation(true);
        }, 1000);

        $('a[data-toggle="popover"]').popover({
          trigger: 'manual'
        });
        $('a[data-toggle="popover"]').click(function(e){
          e.preventDefault();
          var thePopElm = e.currentTarget;
          $(this).popover('toggle');
          setTimeout(function(){
            $(thePopElm).focus();
            ally.element.focus($(thePopElm));
          }, 200);
        });
        $('a[data-toggle="popover"]').keydown(function(e) {
          if(e.keyCode == 9 || e.keyCode == 39){
            $(this).popover('hide');
          }
        });
        $('a[data-toggle="popover"]').blur(function(e){
          $(this).popover('hide');
        });
        $('a[data-toggle="popover"]').on('shown.bs.popover', function () {
          $( "<div role='alert' class='visuallyhidden'>"+$(this).next('.popover').html()+"</div>" ).appendTo( "body" );
        });



        if ($('.component-product-detail').length) {
            initProductDetail();
        }

        if($('.component-category-details').length){
          initProductCategory();
        }

        if ($('.related-flexslider').length) {
          initRelatedFlexSliders();
        }

        $(window).on('resize', function() {
            initComponentCarouselBackground();
            removeNavAbs();
            //initPortraitLandscapeCheck();
            cookieSmart();
        });

		var first = true;
		$('.nearest-mobile').click(function() {
		  //console.log('click');
		  //google.maps.event.trigger(navMap,'resize');
		  if(first){
			init_nav_map();
		  }
		  first = false;
		});

	       //remove outline for click buttons but keep it for keyboard for ada
        $('button').mousedown(function() {
            $(this).addClass('no-ring');
        });
        $('button').keydown(function() {
            $(this).removeClass('no-ring');
        });
        $('.tab-pane').mousedown(function() {
            $(this).addClass('no-ring');
        });
        $('.main').mousedown(function() {
            $(this).addClass('no-ring');
        });
        $('#level-all').mousedown(function() {
            $(this).addClass('no-ring');
        });
        $('#level-details').mousedown(function() {
            $(this).addClass('no-ring');
        });
        $('#level-category').mousedown(function() {
            $(this).addClass('no-ring');
        });


		//custom radio button accordion switcher
        $('.contact-radio-control input:radio').change(function(e) {
          var accordParent = $(this).data('parent');
          $(accordParent).find('.collapse').removeClass('in');

          if($(this).is(':checked')) {
            var accordTarget = $(this).data('target');
            $(accordTarget).addClass('in');
          }
        });

        // On dropdown open
        $(document).on('shown.bs.dropdown', function(event) {
          var dropdown = $(event.target);

          // Set aria-expanded to true
          dropdown.find('.dropdown-menu').attr('aria-expanded', true);
        });
        // On dropdown close
        $(document).on('hidden.bs.dropdown', function(event) {
         var dropdown = $(event.target);

         // Set aria-expanded to false
         dropdown.find('.dropdown-menu').attr('aria-expanded', false);

        });

        //hide focus outline for mouse users but keep for keyboard users
        $("body").on("mousedown", "*", function(e) {
            if (($(this).is(":focus") || $(this).is(e.target)) && $(this).css("outline-style") == "none") {

                if(!$(this).is('div') && !$(this).hasClass('keep-outline')){
                  /* Start: GWSL-1611 -  commented out these lines because it's causing outline to disappear when using <TAB>key */
                  /*
                    $(this).css("outline", "none").on("blur", function() {
                      // $(this).off("blur").css("outline", "");
                    });
                  */
                  /* END */

  				        $(this).addClass("click-before-outline");
                }
            }
        });

        // SCROLL EVENTS
        $(window).on('scroll', function() {

            // minify fixed nav
            var distanceY = window.pageYOffset || document.documentElement.scrollTop,
                shrinkOn = 100,
                header = $('.navbar');

            if (distanceY > shrinkOn) {
                header.addClass('smaller');
            } else {
                if (header.hasClass('smaller')) {
                    header.removeClass('smaller');
                }
            }
            cookieSmart();
            // FADE IN EVENTS
            initFadeinAnimation();

        });

        function isDropdown(node){
            return ['BUTTON'].indexOf(node.nodeName) !== -1;
        }

        function isTextInput(node) {
            return ['INPUT', 'TEXTAREA'].indexOf(node.nodeName) !== -1;
        }

        document.addEventListener('touchstart', function(e) {
            //iOS close keyboard entry
            if (!isTextInput(e.target) && isTextInput(document.activeElement)) {
                document.activeElement.blur();
            }

            //console.log(e.target);


            if (!$(e.target).hasClass('dropdown-toggle')) {
                  if($(e.target).closest('.select').hasClass('open')){
                    //its in the dropdown do nothing
                    return;
                  }else{
                    //iOS close dropdown

                    if($(document.activeElement).closest('.dropdown-toggle').parent().hasClass('open') && $(document.activeElement).closest('.dropdown-toggle').parent().hasClass('select')){
                    	setTimeout(function(){
                          $(document.activeElement).closest('.dropdown-toggle').parent().removeClass('open');
                        }, 200);
                    }

                    if(!$(e.target).hasClass('filter-option')){
                        //iOS close select dropdown
                        if($(document.activeElement).closest('.bootstrap-select').hasClass('open') && !$(document.activeElement).closest('.bootstrap-select').hasClass('nc-select')){
                        	$(document.activeElement).closest('.bootstrap-select').removeClass('open');
                        }
                    }
                  }


            }

            var clickover = $(e.target);
            var _opened = $(".navbar-collapse").hasClass("in");
            if (_opened === true && !clickover.closest(".navbar-collapse").hasClass("in")) {
                //$("button.navbar-toggle").click();
            }else{
              //do nothing
            }

            var _openedDrop = $(".megamenu .dropdown").hasClass("open");
            if (_openedDrop === true && !clickover.closest(".dropdown").hasClass("open")) {
                //$(".dropdown.open a").click();
                //console.log("yep");
                    } else {
              //console.log("nope");
                    }

        }, false);




        // Bootstrap Select and SelectPicker Extensions
        // ===============================

        $('select').selectpicker({
            style: 'select-dropdown',
            dropupAuto : false
        });

        $('select').on('loaded.bs.select', function (e) {
            //console.log(e);
            var currAriaLabel = $(e.currentTarget).attr('aria-label');
            if(currAriaLabel){
              $(e.currentTarget.parentElement.firstChild).attr('aria-label', currAriaLabel)
            }

          //remove title attribute only on initial load
          if(!$(this).hasClass('allow-title')){ //Don't remove `title` attribute for restaurant locator dropdown
            $(this).siblings(".select-dropdown").removeAttr("title");
          }

          $(this).siblings(".select-dropdown").removeAttr("aria-labelledby");

          $(this).siblings(".dropdown-menu").attr("aria-expanded", "false");
          var controlId = $(this).siblings(".select-dropdown").attr("id");
          $(this).siblings(".dropdown-menu").attr("id", controlId+"__subject");
          $(this).siblings(".select-dropdown").attr("aria-controls", controlId+"__subject");
          $(this).siblings(".select-dropdown").attr("aria-owns", controlId+"__subject");

            // dont remove outline on focus
            $(e.currentTarget.parentElement.firstChild).addClass('keep-outline');
          });

		$('.dropdown-menu .bootstrap-select').on('click', function(event) {
        	var target = $(event.target);
			event.stopPropagation();
			$(this).toggleClass('open');
        });

		setTimeout(function(){
			if(adaGenericText) {
				$(".related-flexslider").append('<ul class="flex-direction-nav"><li class="` active-slides"><a class="flex-prev" href="#">'+adaGenericText.prevtext+'</a></li><li class="flex-nav-next active-slides"><a class="flex-next" href="#">'+adaGenericText.nexttext+'</a></li></ul>');
			}
			$("#homeCarouselComponent").find(".flex-direction-nav").css("display","none");
				$(".related-flexslider:first").find(".custom-navigation").find("a").removeClass( "flex-disabled");
					$(".related-flexslider:first").find(".custom-navigation").find("a").attr('tabindex',"0");
				 $('.custom-navigation a.flex-prev').click(function(e) {
                     	$(".related-flexslider:first").find(".custom-navigation").find("a").removeClass( "flex-disabled");
					$(".related-flexslider:first").find(".custom-navigation").find("a").attr('tabindex',"0");
                    });

		}, 3000);




    });


})(jQuery, window, document);


// This function creates an <iframe> (and YouTube player)
    // after the API code downloads.
	var adaGenericText = $('#adaTextJson').attr('data-ada-json') ? JSON.parse($('#adaTextJson').attr('data-ada-json')) : "";
    var player;
    var player_title;
    var player_videos; //['cjZEV1W6P_A','lAPtjaeaD7w','eWuQFeSdBNs','AZq5DKjSvcs','B9__89vfwMI'];
    var player_video_data = [];
	var done = false;
	var youTubePlayerID;
	var videoArray = [];

    /*window.onYouTubeIframeAPIReady = function() {


        if($('.component-video').length){
    	 if (player_videos) {
            if(player_videos.length < 1){
                videosList.parseVideoList();
            }
        }
     }
    };*/

	//youtube api update

  /*backlog update start (02-Jan-2017)*/

	/*$("ul.slides").on('click', '.cont-btn-video a', function(){
		youTubePlayerID = $(this).parent().siblings('.component-video').find('#data-videos').siblings().attr('id');
		//console.log(youTubePlayerID);
		videoArray = $(this).parent().siblings('.component-video').find('#data-videos').attr('data-videos').split(',');
		createPlayer(youTubePlayerID, videoArray );
	});*/

	$("body").on('click', '.caption-wrapper a', function(){
		youTubePlayerID = $(this).find('#data-videos').siblings().attr('id');
		videoArray = $(this).find('#data-videos').attr('data-videos').split(',');
		createPlayer(youTubePlayerID, videoArray );
	});

	$("body").on('click', '.trigger-carousel-video-nc', function(){
		youTubePlayerID = $("body").find('.data-videos-nc').attr("id");

		videoArray = $("body").find(".data-videos-id-nc").attr("data-videos").split(',');
		createPlayer(youTubePlayerID, videoArray );
	});

	/*backlog update end (02-Jan-2017)*/

	function createPlayer(playerID, videoArray ){
	player = new YT.Player(playerID, {
                height: '390',
                width: '640',
                videoId: videoArray[0],
                events: {
                    'onReady': videosList.onPlayerReady,
                    'onStateChange': videosList.onPlayerStateChange
                }
        });
	};


    function playVideo() {
        player.playVideo();
    }

    function pauseVideo() {
        player.pauseVideo();
    }

    function stopVideo() {
        player.stopVideo();
    }


var videosList = {

	initHomeYoutubeAPICalls: function (){
        if($('.component-video').length){

            // This code loads the IFrame Player API code asynchronously.
            var tag = document.createElement('script');

            tag.src = 'https://www.youtube.com/iframe_api';
            var firstScriptTag = document.getElementsByTagName('script')[0];
            firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

			videosList.parseVideoList(youTubePlayerID, videoArray);

            // stop videos on closing modal
            /*$('.component-video').on('hidden.bs.modal', function () {
                pauseVideo();
            });*/


            /*$('#videoModal').on('shown.bs.modal', function (e) {
                playVideo();
            });*/
        }

    },

	parseVideoList:  function (){
        if($('.component-video').length){
            //console.log('videoids '+$('.component-video #data-videos').length);

            var a = $('.component-video #data-videos').length;
            $('[id^="videoModal_"] #data-videos').each(function(){
                var parentDiv = $(this).closest('.component-video').attr('id');
                var videoArr = $(this).attr('data-videos');
                videosList.getYoutubeData(videoArr, parentDiv);
                player_videos = videoArr.split(',');
            });
        }
    },
	// get video view stats using API
    getYoutubeData: function (videoId, parentDiv) {
        // This only a temporary API key. It will need to be replaced by McDonalds'.
        var apiKey = 'AIzaSyDeKiNNLgttxVX4Pw2jnHM0D7WWPjt7AAA';
        var fetchurl = 'https://www.googleapis.com/youtube/v3/videos?part=snippet,statistics&id=' + videoId;
        fetchurl = fetchurl + '&key=' + apiKey;


        $.ajax({
            url: fetchurl,
            dataType: 'jsonp',
            success: function (data) {
                //console.log('ajax call '+data);
                videosList._handleYoutubeAjaxData(data, parentDiv);
            }
        });

    },

    _handleYoutubeAjaxData: function (data, parentDiv){
        var videoObj = data.items;
        player_video_data = [];
        $(videoObj).each(function(index,element){
            var videoDetails = {
                viewcount : element.statistics.viewCount,
            	title : element.snippet.title,
                thumbnail : element.snippet.thumbnails["default"].url,
                videoid: element.id
            };
            player_video_data.push(videoDetails);
        });
        videosList.videoModalPopulate(player_video_data,parentDiv);
    },
	videoModalPopulate : function (videoData, parentDiv){
        var content = '';
        $(videoData).each(function(index, element){
            if(index == 0){
                content +=  '<li data-videoid="'+element.videoid+'" class="active">';
            }
            else{
                content +=  '<li data-videoid="'+element.videoid+'">';
            }

            content +=      '<div class="media">';
            content +=          '<div class="media-left media-middle">';
            content +=                 '<img class="media-object" src="'+element.thumbnail+'" alt="'+element.title+'">';
            content +=          '</div>';
            content +=          '<div class="media-body media-middle">';
            content +=              '<h4 class="media-heading">'+element.title+'</h4>'+element.viewcount+' Views';
            content +=          '</div>';
            content +=      '</div>';
            content +=  '</li>';

            if($('#'+parentDiv+'.component-video .cont-playlist .list-unstyled').length){
                $('#'+parentDiv+'.component-video .cont-playlist .list-unstyled').html(content);

             videosList.videoModalClicks();
            }

        });

        //playlist hide
        if(videoData.length < 2) {
        	$('#'+parentDiv+'.component-video .cont-playlist .list-unstyled').hide();
        	$('#'+parentDiv+'.component-video .cont-playlist').css('width','650px');
        }
    },

	  videoModalClicks : function(){
        // handle video changes on clicking thumbnails
        $('.component-video .cont-playlist .list-unstyled li').on('click', function(e){
            var curr = e.currentTarget;
            var clickedVideoId = $(curr).attr('data-videoid');
            //console.log(clickedVideoId);

            if($(curr).hasClass('active')){
                // do nothing if video is already playing

            }
            else{
                videosList.loadDifferentVideo(clickedVideoId);
                $('.component-video .cont-playlist .list-unstyled li.active').removeClass('active');
                $(curr).addClass('active');

                /*backlog update (18-jan-2017)*/
                //player_title = $('.component-video .cont-playlist .list-unstyled li.active h4.media-heading').text();
                // set title of current video
                //$('.component-video .video-title').html(player_title);
                /*backlog update (18-jan-2017)*/
            }
        });
    },


	loadDifferentVideo : function (newVideoId){
	        player.loadVideoById(newVideoId);
    },

	// The API will call this function when the video player is ready.
    onPlayerReady : function (event) {
    	/*backlog update (18-jan-2017)*/
        //player_title = player.getVideoData().title;

        // set title of current video
        //$('.component-video .video-title').html(player_title);
        /*backlog update (18-jan-2017)*/
		//event.target.playVideo();
    },

	//    The API calls this function when the player's state changes.
    //    The function indicates that when playing a video (state=1),
    //    the player should play for six seconds and then stop.
    onPlayerStateChange : function (event) {
        if (event.data == YT.PlayerState.PLAYING && !done) {
          //setTimeout(stopVideo, 6000);
          done = true;
        }
    },

	initFlexsliders : function () {
        // flexsliders
           var rtlFlow=false,dir="ltr";
             if (window.location.href.indexOf("ar-") > -1){
                rtlFlow=true;
                dir="rtl";
        };
        var fxAnimation = $('.home-flexslider').data('animation');
        var fxSlideshow = $('.home-flexslider').data('slideshow');
        var fxSlideshowSpeed = $('.home-flexslider').data('slide-speed');
        var globalSlideSize = parseInt($('.home-flexslider').attr('data-slideSize'));

        $('.home-flexslider').flexslider({
        animation: fxAnimation,//String: Select your animation type, "fade" or "slide"
        slideshow: fxSlideshow, //Boolean: Animate slider automatically
        slideshowSpeed: fxSlideshowSpeed, //Integer: Set the speed of the slideshow cycling, in milliseconds
         rtl: rtlFlow,
        //pauseOnHover: true, //Boolean: Pause the slideshow when hovering over slider, then resume when no longer hovering
        start: function(slider) {
            $('.home-flexslider').resize();
            slider.css("direction",dir);
            },
			  after: function() {
				  var slideContent;
                 $('.flex-control-nav > li').each(function(index){
                var theclickabledot = $(this).find('a');
                if(index==2){
                    slideContent= $('.home-flexslider .slides  > :nth-child(3)').find('.caption h2 .visuallyhidden').html();
                 }
                 else{
                   slideContent = $('.home-flexslider .slides li').eq(index).find('.caption h2 .visuallyhidden').html();
                 }
                //console.log(slideContent);
                if(theclickabledot.hasClass('flex-active')){
                  //$(this).find('a').html("<span class='visuallyhidden'>slide number "+(index+1)+" active</span>");
                  $(this).find('a').html("<span class='visuallyhidden'>"+adaGenericText.activeslidetext+ "-" +slideContent+"</span>");
                }else{
                  //$(this).find('a').html("<span class='visuallyhidden'>go to slide number "+(index+1)+" </span>");
                  $(this).find('a').html("<span class='visuallyhidden'>"+adaGenericText.gotoslidetext+ "-" +slideContent+" </span>");
                }

              });
			               $('.home-flexslider .custom-paging li a').removeClass('flex-active');
              var customNavLiNum = $('.custom-paging li').eq($('.home-flexslider').data('flexslider').currentSlide);
              //var activeAdjusted = $('.custom-paging li').eq($('.home-flexslider').data('flexslider').currentSlide)+1;
              $('.home-flexslider .custom-paging li a').each(function(index){
                var slideContent = $('.home-flexslider .slides li').eq(index).find('.caption h2 .visuallyhidden').html();
                //$(this).html('<span class="visuallyhidden">go to slide '+(index+1)+'</span>')
                $(this).html("<span class='visuallyhidden'>"+adaGenericText.gotoslidetext+ "-" +slideContent+" </span>");
              });

              //$('.custom-paging li').eq($('.home-flexslider').data('flexslider').currentSlide);
              $(customNavLiNum).find('a').addClass('flex-active');
              //$(customNavLiNum).find('a').html('<span class="visuallyhidden">slide '+($('.home-flexslider').data('flexslider').currentSlide+1)+' active</span>');
              var activeSlideContent = $('.home-flexslider .slides li').eq($('.home-flexslider').data('flexslider').currentSlide).find('.caption h2 .visuallyhidden').html();
              $(customNavLiNum).find('a').html('<span class="visuallyhidden">'+adaGenericText.activeslidetext+ '-' +activeSlideContent+'</span>');
                $('.home-flexslider').find('.slides li').attr("aria-hidden", "false");
                $('.home-flexslider').find('.slides li').attr("tabindex", "0");
                $('.home-flexslider').find('.slides li').not('.flex-active-slide').attr("aria-hidden", "true");
                $('.home-flexslider').find('.slides li:not(.flex-active-slide)').attr("tabindex", "-1");
            }
        });
        $('.home-flexslider').find('.slides li').attr("aria-hidden", "false");
        $('.home-flexslider').find('.slides li').attr("tabindex", "0");
        $('.home-flexslider').find('.slides li').not('.flex-active-slide').attr("aria-hidden", "true");
        $('.home-flexslider').find('.slides li:not(.flex-active-slide)').attr("tabindex", "-1");

        $('.home-flexslider .flex-control-nav li a').each(function(index){
          var slideContent = $('.home-flexslider .slides li').eq(index).find('.caption h2 .visuallyhidden').html();
          //$('.home-flexslider .custom-paging').append('<li><a href="javaScript:void(0);" tabindex="0"><span class="visuallyhidden">go to slide '+$(this).html()+'</span></a></li>');
          /* Start: DEPS-24357 */
          var flexActiveClass = !index ? 'flex-active' : '';
          $('.home-flexslider .custom-paging').append('<li><a href="javaScript:void(0);" tabindex="0" class="'+flexActiveClass+'"><span class="visuallyhidden">'+adaGenericText.gotoslidetext+' - '+slideContent+'</span></a></li>');
          /* End */
        });

        $('.home-flexslider .custom-paging li a').each(function(index){
          $(this).click(function(e){
            e.preventDefault;
            $('.home-flexslider').flexslider(index);
            if($('.flex-play').attr("aria-label")==adaGenericText.clicktopausetext){
              //$('.flex-play').click();
              $('.flex-play').attr('aria-label',adaGenericText.clicktoplaytext);
              $('.flex-play').toggleClass("paused");
            }
            //if(!$('.home-flexslider').data('flexslider').stopped){};


            //$('.home-flexslider').flexslider("pause");
            setTimeout(function(){
                $('.home-flexslider .flex-active-slide .focus-start').focus();
            }, 400);
          });

        });

        //create custom nav arrows
        $('.home-flexslider .custom-direction-nav .flex-nav-prev a').click(function(e){
          $('.home-flexslider').flexslider("prev");
        });
        $('.home-flexslider .custom-direction-nav .flex-nav-next a').click(function(e){
          $('.home-flexslider').flexslider("next");
        });

        $('.home-flexslider .flex-control-nav').css('display', 'none');
        $('.home-flexslider .flex-direction-nav').css('display', 'none');
       if(globalSlideSize >= 1 || globalSlideSize > 1) {
    	   videosList.createSliderButtons('.home-flexslider');
       }

		$('.flex-prev').attr('aria-label','previous slide');
        $('.flex-next').attr('aria-label','next slide');

        var socialFlexContents = '<div class="social-flexslider flexslider">' + $('.social-flexslider').html() + '</div>';

        $('.social-flexslider').flexslider({
            slideshow: false,
            animation: 'slide',
            itemWidth: 10,
            itemMargin: 15,
            minItems: videosList.getGridSize(),
            maxItems: videosList.getGridSize()
        });

        if(globalSlideSize >= 1 || globalSlideSize > 1 ) {
        	//videosList.createSliderButtons('.social-flexslider');
        }
		var thisSlider= $("#homeCarouselComponent");
            var customNavContent = thisSlider.find('.custom-paging');
           $(customNavContent).insertAfter(thisSlider.find('.flex-viewport'));
           var customPagingContent = thisSlider.find('.custom-direction-nav');
           $(customPagingContent).insertAfter(thisSlider.find('.custom-paging'));

        /*
        if (Modernizr.mq('(max-width: 767px)')) {
            initZoomAnim('.slides .cont-btn a', '.bg-mobile', 'bglarge', '.flex-active-slide');
        } else {
            initZoomAnim('.slides .cont-btn a', '.flex-active-slide', 'bglarge');
        }*/

        $(window).on('resize', function() {

            // remove from dom and add back since flexslider does not have a destroy method
            var gridSize = videosList.getGridSize();
            $('.social-flexslider').remove();
            $('.social-flex-container').html(socialFlexContents);

            $('.social-flexslider').flexslider({
                slideshow: false,
                animation: 'slide',
                itemWidth: 10,
                itemMargin: 15,
                minItems: gridSize,
                maxItems: gridSize
            });

            if(globalSlideSize >= 1 || globalSlideSize > 1 ) {
            //	videosList.createSliderButtons('.social-flexslider');
            }

            /*
            if (Modernizr.mq('(max-width: 767px)')) {
                initZoomAnim('.slides .cont-btn a', '.bg-mobile', 'bglarge', '.flex-active-slide');
            } else {
                initZoomAnim('.slides .cont-btn a', '.flex-active-slide', 'bglarge');
            }*/
        });

    },
    /* start GWSL-1595*/
    getPlayPauseStatus : function (){
      /* getPlayPauseStatus checking function*/
      var autoplayStateValue = getCookie('home-carousel-autoplay-state');
      return ( autoplayStateValue != "") ? autoplayStateValue : "";
    },
    /* end */
    createSliderButtons : function (sliderContainer){
        var htmlContent = '';        
        /* start GWSL-1595*/
        var getPlayPauseStatus = this.getPlayPauseStatus();
        var autoplayStateValue = (getPlayPauseStatus == 'play' || getPlayPauseStatus == '') ? "paused" : "";
        /* end */
        htmlContent += '<li class="cont-play-pause-buttons">';        
        /* start GWSL-1595*/
        htmlContent += '<button type="button" class="flex-play ' + autoplayStateValue+'" aria-label="Pause slider">';
        /* end */
        htmlContent +=  '<i class="fa fa-play-circle fa-lg"></i></button>';
        htmlContent += '</li>';

        $(sliderContainer).append(htmlContent);
        // create click events
        videosList.playSlider(sliderContainer + ' .flex-play', sliderContainer);
        $("#homeCarouselComponent .cont-play-pause-buttons").appendTo("#homeCarouselComponent .custom-paging");
    },


	playSlider : function (clickElem, slider) {
        var play="click to pause";
        var pause="click to play";
        /* start GWSL-1595*/
        var getPlayPauseStatus = this.getPlayPauseStatus();
        if(getPlayPauseStatus == 'pause'){
          $(slider).flexslider('pause');
        }
        /* end */
        $(clickElem).on('click', function(){
            if(! $(this).hasClass('paused')){
                // go to next slide immediately
                $(slider).flexslider('next');
                $(slider).flexslider('play');
                /* start GWSL-1595*/
                setCookie('home-carousel-autoplay-state', 'play');
                /* end */
                if($(".soundPlayText").text().length > 0) play=$(".soundPlayText").text();

                $(this).attr('aria-label',play);
                $( "<div role='alert' class='visuallyhidden'>slider playing</div>" ).appendTo( "body" );
            }
            else{
                $(slider).flexslider('pause');
                /* start GWSL-1595*/
                setCookie('home-carousel-autoplay-state', 'pause');
                /* end */
                if($(".soundPauseText").text().length > 0) pause=$(".soundPauseText").text();
                $(this).attr('aria-label',pause);
                $( "<div role='alert' class='visuallyhidden'>slider paused</div>" ).appendTo( "body" );
            }
            $(this).toggleClass('paused');
        });
    },

	// tiny helper function to add breakpoints
    getGridSize : function () {
        if (window.innerWidth < 768) {
            return 1;
        }
        return 3;
    }
};


/*!
 * Bootstrap-select v1.10.0 (http://silviomoreto.github.io/bootstrap-select)
 *
 * Copyright 2013-2016 bootstrap-select
 * Licensed under MIT (https://github.com/silviomoreto/bootstrap-select/blob/master/LICENSE)
 */

(function (root, factory) {
  if (typeof define === 'function' && define.amd) {
    // AMD. Register as an anonymous module unless amdModuleId is set
    define(["jquery"], function (a0) {
      return (factory(a0));
    });
  } else if (typeof exports === 'object') {
    // Node. Does not work with strict CommonJS, but
    // only CommonJS-like environments that support module.exports,
    // like Node.
    module.exports = factory(require("jquery"));
  } else {
    factory(jQuery);
  }
}(this, function (jQuery) {

(function ($) {
  'use strict';

  //<editor-fold desc="Shims">
  if (!String.prototype.includes) {
    (function () {
      'use strict'; // needed to support `apply`/`call` with `undefined`/`null`
      var toString = {}.toString;
      var defineProperty = (function () {
        // IE 8 only supports `Object.defineProperty` on DOM elements
        try {
          var object = {};
          var $defineProperty = Object.defineProperty;
          var result = $defineProperty(object, object, object) && $defineProperty;
        } catch (error) {
        }
        return result;
      }());
      var indexOf = ''.indexOf;
      var includes = function (search) {
        if (this == null) {
          throw new TypeError();
        }
        var string = String(this);
        if (search && toString.call(search) == '[object RegExp]') {
          throw new TypeError();
        }
        var stringLength = string.length;
        var searchString = String(search);
        var searchLength = searchString.length;
        var position = arguments.length > 1 ? arguments[1] : undefined;
        // `ToInteger`
        var pos = position ? Number(position) : 0;
        if (pos != pos) { // better `isNaN`
          pos = 0;
        }
        var start = Math.min(Math.max(pos, 0), stringLength);
        // Avoid the `indexOf` call if no match is possible
        if (searchLength + start > stringLength) {
          return false;
        }
        return indexOf.call(string, searchString, pos) != -1;
      };
      if (defineProperty) {
        defineProperty(String.prototype, 'includes', {
          'value': includes,
          'configurable': true,
          'writable': true
        });
      } else {
        String.prototype.includes = includes;
      }
    }());
  }

  if (!String.prototype.startsWith) {
    (function () {
      'use strict'; // needed to support `apply`/`call` with `undefined`/`null`
      var defineProperty = (function () {
        // IE 8 only supports `Object.defineProperty` on DOM elements
        try {
          var object = {};
          var $defineProperty = Object.defineProperty;
          var result = $defineProperty(object, object, object) && $defineProperty;
        } catch (error) {
        }
        return result;
      }());
      var toString = {}.toString;
      var startsWith = function (search) {
        if (this == null) {
          throw new TypeError();
        }
        var string = String(this);
        if (search && toString.call(search) == '[object RegExp]') {
          throw new TypeError();
        }
        var stringLength = string.length;
        var searchString = String(search);
        var searchLength = searchString.length;
        var position = arguments.length > 1 ? arguments[1] : undefined;
        // `ToInteger`
        var pos = position ? Number(position) : 0;
        if (pos != pos) { // better `isNaN`
          pos = 0;
        }
        var start = Math.min(Math.max(pos, 0), stringLength);
        // Avoid the `indexOf` call if no match is possible
        if (searchLength + start > stringLength) {
          return false;
        }
        var index = -1;
        while (++index < searchLength) {
          if (string.charCodeAt(start + index) != searchString.charCodeAt(index)) {
            return false;
          }
        }
        return true;
      };
      if (defineProperty) {
        defineProperty(String.prototype, 'startsWith', {
          'value': startsWith,
          'configurable': true,
          'writable': true
        });
      } else {
        String.prototype.startsWith = startsWith;
      }
    }());
  }

  if (!Object.keys) {
    Object.keys = function (
      o, // object
      k, // key
      r  // result array
      ){
      // initialize object and result
      r=[];
      // iterate over object keys
      for (k in o)
          // fill result array with non-prototypical keys
        r.hasOwnProperty.call(o, k) && r.push(k);
      // return result
      return r;
    };
  }

  $.fn.triggerNative = function (eventName) {
    var el = this[0],
        event;

    if (el.dispatchEvent) {
      if (typeof Event === 'function') {
        // For modern browsers
        event = new Event(eventName, {
          bubbles: true
        });
      } else {
        // For IE since it doesn't support Event constructor
        event = document.createEvent('Event');
        event.initEvent(eventName, true, false);
      }

      el.dispatchEvent(event);
    } else {
      if (el.fireEvent) {
        event = document.createEventObject();
        event.eventType = eventName;
        el.fireEvent('on' + eventName, event);
      }

      this.trigger(eventName);
    }
  };
  //</editor-fold>

  // Case insensitive contains search
  $.expr[':'].icontains = function (obj, index, meta) {
    var $obj = $(obj);
    var haystack = ($obj.data('tokens') || $obj.text()).toUpperCase();
    return haystack.includes(meta[3].toUpperCase());
  };

  // Case insensitive begins search
  $.expr[':'].ibegins = function (obj, index, meta) {
    var $obj = $(obj);
    var haystack = ($obj.data('tokens') || $obj.text()).toUpperCase();
    return haystack.startsWith(meta[3].toUpperCase());
  };

  // Case and accent insensitive contains search
  $.expr[':'].aicontains = function (obj, index, meta) {
    var $obj = $(obj);
    var haystack = ($obj.data('tokens') || $obj.data('normalizedText') || $obj.text()).toUpperCase();
    return haystack.includes(meta[3].toUpperCase());
  };

  // Case and accent insensitive begins search
  $.expr[':'].aibegins = function (obj, index, meta) {
    var $obj = $(obj);
    var haystack = ($obj.data('tokens') || $obj.data('normalizedText') || $obj.text()).toUpperCase();
    return haystack.startsWith(meta[3].toUpperCase());
  };

  /**
   * Remove all diatrics from the given text.
   * @access private
   * @param {String} text
   * @returns {String}
   */
  function normalizeToBase(text) {
    var rExps = [
      {re: /[\xC0-\xC6]/g, ch: "A"},
      {re: /[\xE0-\xE6]/g, ch: "a"},
      {re: /[\xC8-\xCB]/g, ch: "E"},
      {re: /[\xE8-\xEB]/g, ch: "e"},
      {re: /[\xCC-\xCF]/g, ch: "I"},
      {re: /[\xEC-\xEF]/g, ch: "i"},
      {re: /[\xD2-\xD6]/g, ch: "O"},
      {re: /[\xF2-\xF6]/g, ch: "o"},
      {re: /[\xD9-\xDC]/g, ch: "U"},
      {re: /[\xF9-\xFC]/g, ch: "u"},
      {re: /[\xC7-\xE7]/g, ch: "c"},
      {re: /[\xD1]/g, ch: "N"},
      {re: /[\xF1]/g, ch: "n"}
    ];
    $.each(rExps, function () {
      text = text.replace(this.re, this.ch);
    });
    return text;
  }


  function htmlEscape(html) {
    var escapeMap = {
      '&': '&amp;',
      '<': '&lt;',
      '>': '&gt;',
      '"': '&quot;',
      "'": '&#x27;',
      '`': '&#x60;'
    };
    var source = '(?:' + Object.keys(escapeMap).join('|') + ')',
        testRegexp = new RegExp(source),
        replaceRegexp = new RegExp(source, 'g'),
        string = html == null ? '' : '' + html;
    return testRegexp.test(string) ? string.replace(replaceRegexp, function (match) {
      return escapeMap[match];
    }) : string;
  }

  var Selectpicker = function (element, options, e) {
    if (e) {
      e.stopPropagation();
      e.preventDefault();
    }

    this.$element = $(element);
    this.$newElement = null;
    this.$button = null;
    this.$menu = null;
    this.$lis = null;
    this.options = options;

    // If we have no title yet, try to pull it from the html title attribute (jQuery doesnt' pick it up as it's not a
    // data-attribute)
    if (this.options.title === null) {
      this.options.title = this.$element.attr('title');
    }

    //Expose public methods
    this.val = Selectpicker.prototype.val;
    this.render = Selectpicker.prototype.render;
    this.refresh = Selectpicker.prototype.refresh;
    this.setStyle = Selectpicker.prototype.setStyle;
    this.selectAll = Selectpicker.prototype.selectAll;
    this.deselectAll = Selectpicker.prototype.deselectAll;
    this.destroy = Selectpicker.prototype.destroy;
    this.remove = Selectpicker.prototype.remove;
    this.show = Selectpicker.prototype.show;
    this.hide = Selectpicker.prototype.hide;

    this.init();
  };

  Selectpicker.VERSION = '1.10.0';

  // part of this is duplicated in i18n/defaults-en_US.js. Make sure to update both.
  Selectpicker.DEFAULTS = {
    noneSelectedText: 'Nothing selected',
    noneResultsText: 'No results matched {0}',
    countSelectedText: function (numSelected, numTotal) {
      return (numSelected == 1) ? "{0} item selected" : "{0} items selected";
    },
    maxOptionsText: function (numAll, numGroup) {
      return [
        (numAll == 1) ? 'Limit reached ({n} item max)' : 'Limit reached ({n} items max)',
        (numGroup == 1) ? 'Group limit reached ({n} item max)' : 'Group limit reached ({n} items max)'
      ];
    },
    selectAllText: 'Select All',
    deselectAllText: 'Deselect All',
    doneButton: false,
    doneButtonText: 'Close',
    multipleSeparator: ', ',
    styleBase: 'btn',
    style: 'btn-default',
    size: 'auto',
    title: null,
    selectedTextFormat: 'values',
    width: false,
    container: false,
    hideDisabled: false,
    showSubtext: false,
    showIcon: true,
    showContent: true,
    dropupAuto: true,
    header: false,
    liveSearch: false,
    liveSearchPlaceholder: null,
    liveSearchNormalize: false,
    liveSearchStyle: 'contains',
    actionsBox: false,
    iconBase: 'glyphicon',
    tickIcon: 'glyphicon-ok',
    showTick: false,
    template: {
      caret: '<span class="caret"></span>'
    },
    maxOptions: false,
    mobile: false,
    selectOnTab: false,
    dropdownAlignRight: false
  };

  Selectpicker.prototype = {

    constructor: Selectpicker,

    init: function () {
      var that = this,
          id = this.$element.attr('id');

      this.$element.addClass('bs-select-hidden');

      // store originalIndex (key) and newIndex (value) in this.liObj for fast accessibility
      // allows us to do this.$lis.eq(that.liObj[index]) instead of this.$lis.filter('[data-original-index="' + index + '"]')
      this.liObj = {};
      this.multiple = this.$element.prop('multiple');
      this.autofocus = this.$element.prop('autofocus');
      this.$newElement = this.createView();
      this.$element
        .after(this.$newElement)
        .appendTo(this.$newElement);
      this.$button = this.$newElement.children('button');
      this.$menu = this.$newElement.children('.dropdown-menu');
      this.$menuInner = this.$menu.children('.inner');
      this.$searchbox = this.$menu.find('input');

      this.$element.removeClass('bs-select-hidden');

      if (this.options.dropdownAlignRight)
        this.$menu.addClass('dropdown-menu-right');

      if (typeof id !== 'undefined') {
        this.$element.attr('id', id+'orig');
        this.$button.attr('id', id);
        this.$button.attr('data-id', id);
        this.$button.attr('aria-labelledby', id+'-label');
        $('label[for="' + id + '"]').click(function (e) {
          e.preventDefault();
          that.$button.focus();
        });
      }

      this.checkDisabled();
      this.clickListener();
      if (this.options.liveSearch) this.liveSearchListener();
      this.render();
      this.setStyle();
      this.setWidth();
      if (this.options.container) this.selectPosition();
      this.$menu.data('this', this);
      this.$newElement.data('this', this);
      if (this.options.mobile) this.mobile();

      this.$newElement.on({
        'hide.bs.dropdown': function (e) {
          that.$element.trigger('hide.bs.select', e);
        },
        'hidden.bs.dropdown': function (e) {
          that.$element.trigger('hidden.bs.select', e);
        },
        'show.bs.dropdown': function (e) {
          that.$element.trigger('show.bs.select', e);
        },
        'shown.bs.dropdown': function (e) {
          that.$element.trigger('shown.bs.select', e);
        }
      });

      if (that.$element[0].hasAttribute('required')) {
        this.$element.on('invalid', function () {
          that.$button
            .addClass('bs-invalid')
            .focus();

          that.$element.on({
            'focus.bs.select': function () {
              that.$button.focus();
              that.$element.off('focus.bs.select');
            },
            'shown.bs.select': function () {
              that.$element
                .val(that.$element.val()) // set the value to hide the validation message in Chrome when menu is opened
                .off('shown.bs.select');
            },
            'rendered.bs.select': function () {
              // if select is no longer invalid, remove the bs-invalid class
              if (this.validity.valid) that.$button.removeClass('bs-invalid');
              that.$element.off('rendered.bs.select');
            }
          });

        });
      }

      setTimeout(function () {
        that.$element.trigger('loaded.bs.select');
      });
    },

    createDropdown: function () {
      // Options
      // If we are multiple or showTick option is set, then add the show-tick class
      var showTick = (this.multiple || this.options.showTick) ? ' show-tick' : '',
          inputGroup = this.$element.parent().hasClass('input-group') ? ' input-group-btn' : '',
          autofocus = this.autofocus ? ' autofocus' : '';
      // Elements
      var header = this.options.header ? '<div class="popover-title"><button type="button" class="close" aria-hidden="true">&times;</button>' + this.options.header + '</div>' : '';
      var searchbox = this.options.liveSearch ?
      '<div class="bs-searchbox">' +
      '<input type="text" class="form-control" autocomplete="off"' +
      (null === this.options.liveSearchPlaceholder ? '' : ' placeholder="' + htmlEscape(this.options.liveSearchPlaceholder) + '"') + '>' +
      '</div>'
          : '';
      var actionsbox = this.multiple && this.options.actionsBox ?
      '<div class="bs-actionsbox">' +
      '<div class="btn-group btn-group-sm btn-block">' +
      '<button type="button" class="actions-btn bs-select-all btn btn-default">' +
      this.options.selectAllText +
      '</button>' +
      '<button type="button" class="actions-btn bs-deselect-all btn btn-default">' +
      this.options.deselectAllText +
      '</button>' +
      '</div>' +
      '</div>'
          : '';
      var donebutton = this.multiple && this.options.doneButton ?
      '<div class="bs-donebutton">' +
      '<div class="btn-group btn-block">' +
      '<button type="button" class="btn btn-sm btn-default">' +
      this.options.doneButtonText +
      '</button>' +
      '</div>' +
      '</div>'
          : '';
      var drop =
          '<div class="btn-group bootstrap-select' + showTick + inputGroup + '">' +
          '<button type="button" aria-expanded="false" class="' + this.options.styleBase + ' dropdown-toggle" data-toggle="dropdown"' + autofocus + '>' +
          '<span class="filter-option pull-left"></span>&nbsp;' +
          '<span class="bs-caret">' +
          this.options.template.caret +
          '</span>' +
          '</button>' +
          '<div class="dropdown-menu open">' +
          header +
          searchbox +
          actionsbox +
          '<ul class="dropdown-menu inner" role="menu">' +
          '</ul>' +
          donebutton +
          '</div>' +
          '</div>';

      return $(drop);
    },

    createView: function () {
      var $drop = this.createDropdown(),
          li = this.createLi();

      $drop.find('ul')[0].innerHTML = li;
      return $drop;
    },

    reloadLi: function () {
      //Remove all children.
      this.destroyLi();
      //Re build
      var li = this.createLi();
      this.$menuInner[0].innerHTML = li;
    },

    destroyLi: function () {
      this.$menu.find('li').remove();
    },

    createLi: function () {
      var that = this,
          _li = [],
          optID = 0,
          titleOption = document.createElement('option'),
          liIndex = -1; // increment liIndex whenever a new <li> element is created to ensure liObj is correct

      // Helper functions
      /**
       * @param content
       * @param [index]
       * @param [classes]
       * @param [optgroup]
       * @returns {string}
       */
      var generateLI = function (content, index, classes, optgroup) {
        return '<li' +
            ((typeof classes !== 'undefined' & '' !== classes) ? ' class="' + classes + '"' : '') +
            ((typeof index !== 'undefined' & null !== index) ? ' data-original-index="' + index + '"' : '') +
            ((typeof optgroup !== 'undefined' & null !== optgroup) ? 'data-optgroup="' + optgroup + '"' : '') +
            '>' + content + '</li>';
      };

      /**
       * @param text
       * @param [classes]
       * @param [inline]
       * @param [tokens]
       * @returns {string}
       */
      var generateA = function (text, classes, inline, tokens) {
        return '<a tabindex="0"' +
            (typeof classes !== 'undefined' ? ' class="' + classes + '"' : '') +
            (typeof inline !== 'undefined' ? ' style="' + inline + '"' : '') +
            (that.options.liveSearchNormalize ? ' data-normalized-text="' + normalizeToBase(htmlEscape(text)) + '"' : '') +
            (typeof tokens !== 'undefined' || tokens !== null ? ' data-tokens="' + tokens + '"' : '') +
            '>' + text +
            '<span class="' + that.options.iconBase + ' ' + that.options.tickIcon + ' check-mark"></span>' +
            '</a>';
      };

      if (this.options.title && !this.multiple) {
        // this option doesn't create a new <li> element, but does add a new option, so liIndex is decreased
        // since liObj is recalculated on every refresh, liIndex needs to be decreased even if the titleOption is already appended
        liIndex--;

        if (!this.$element.find('.bs-title-option').length) {
          // Use native JS to prepend option (faster)
          var element = this.$element[0];
          titleOption.className = 'bs-title-option';
          titleOption.appendChild(document.createTextNode(this.options.title));
          titleOption.value = '';
          element.insertBefore(titleOption, element.firstChild);
          // Check if selected attribute is already set on an option. If not, select the titleOption option.
          if ($(element.options[element.selectedIndex]).attr('selected') === undefined) titleOption.selected = true;
        }
      }

      this.$element.find('option').each(function (index) {
        var $this = $(this);

        liIndex++;

        if ($this.hasClass('bs-title-option')) return;

        // Get the class and text for the option
        var optionClass = this.className || '',
            inline = this.style.cssText,
            text = $this.data('content') ? $this.data('content') : $this.html(),
            tokens = $this.data('tokens') ? $this.data('tokens') : null,
            subtext = typeof $this.data('subtext') !== 'undefined' ? '<small class="text-muted">' + $this.data('subtext') + '</small>' : '',
            icon = typeof $this.data('icon') !== 'undefined' ? '<span class="' + that.options.iconBase + ' ' + $this.data('icon') + '"></span> ' : '',
            isOptgroup = this.parentNode.tagName === 'OPTGROUP',
            isDisabled = this.disabled || (isOptgroup && this.parentNode.disabled);

        if (icon !== '' && isDisabled) {
          icon = '<span>' + icon + '</span>';
        }

        if (that.options.hideDisabled && isDisabled && !isOptgroup) {
          liIndex--;
          return;
        }

        if (!$this.data('content')) {
          // Prepend any icon and append any subtext to the main text.
          text = icon + '<span class="text">' + text + subtext + '</span>';
        }

        if (isOptgroup && $this.data('divider') !== true) {
          var optGroupClass = ' ' + this.parentNode.className || '';

          if ($this.index() === 0) { // Is it the first option of the optgroup?
            optID += 1;

            // Get the opt group label
            var label = this.parentNode.label,
                labelSubtext = typeof $this.parent().data('subtext') !== 'undefined' ? '<small class="text-muted">' + $this.parent().data('subtext') + '</small>' : '',
                labelIcon = $this.parent().data('icon') ? '<span class="' + that.options.iconBase + ' ' + $this.parent().data('icon') + '"></span> ' : '';

            label = labelIcon + '<span class="text">' + label + labelSubtext + '</span>';

            if (index !== 0 && _li.length > 0) { // Is it NOT the first option of the select && are there elements in the dropdown?
              liIndex++;
              _li.push(generateLI('', null, 'divider', optID + 'div'));
            }
            liIndex++;
            _li.push(generateLI(label, null, 'dropdown-header' + optGroupClass, optID));
          }

          if (that.options.hideDisabled && isDisabled) {
            liIndex--;
            return;
          }

          _li.push(generateLI(generateA(text, 'opt ' + optionClass + optGroupClass, inline, tokens), index, '', optID));
        } else if ($this.data('divider') === true) {
          _li.push(generateLI('', index, 'divider'));
        } else if ($this.data('hidden') === true) {
          _li.push(generateLI(generateA(text, optionClass, inline, tokens), index, 'hidden is-hidden'));
        } else {
          if (this.previousElementSibling && this.previousElementSibling.tagName === 'OPTGROUP') {
            liIndex++;
            _li.push(generateLI('', null, 'divider', optID + 'div'));
          }
          _li.push(generateLI(generateA(text, optionClass, inline, tokens), index));
        }

        that.liObj[index] = liIndex;
      });

      //If we are not multiple, we don't have a selected item, and we don't have a title, select the first element so something is set in the button
      if (!this.multiple && this.$element.find('option:selected').length === 0 && !this.options.title) {
        this.$element.find('option').eq(0).prop('selected', true).attr('selected', 'selected');
      }

      return _li.join('');
    },

    findLis: function () {
      if (this.$lis == null) this.$lis = this.$menu.find('li');
      return this.$lis;
    },

    /**
     * @param [updateLi] defaults to true
     */
    render: function (updateLi) {
      var that = this,
          notDisabled;

      //Update the LI to match the SELECT
      if (updateLi !== false) {
        this.$element.find('option').each(function (index) {
          var $lis = that.findLis().eq(that.liObj[index]);

          that.setDisabled(index, this.disabled || this.parentNode.tagName === 'OPTGROUP' && this.parentNode.disabled, $lis);
          that.setSelected(index, this.selected, $lis);
        });
      }

      this.tabIndex();

      var selectedItems = this.$element.find('option').map(function () {
        if (this.selected) {
          if (that.options.hideDisabled && (this.disabled || this.parentNode.tagName === 'OPTGROUP' && this.parentNode.disabled)) return;

          var $this = $(this),
              icon = $this.data('icon') && that.options.showIcon ? '<i class="' + that.options.iconBase + ' ' + $this.data('icon') + '"></i> ' : '',
              subtext;

          if (that.options.showSubtext && $this.data('subtext') && !that.multiple) {
            subtext = ' <small class="text-muted">' + $this.data('subtext') + '</small>';
          } else {
            subtext = '';
          }
          if (typeof $this.attr('title') !== 'undefined') {
            return $this.attr('title');
          } else if ($this.data('content') && that.options.showContent) {
            return $this.data('content');
          } else {
            return icon + $this.html() + subtext;
          }
        }
      }).toArray();

      //Fixes issue in IE10 occurring when no default option is selected and at least one option is disabled
      //Convert all the values into a comma delimited string
      var title = !this.multiple ? selectedItems[0] : selectedItems.join(this.options.multipleSeparator);

      //If this is multi select, and the selectText type is count, the show 1 of 2 selected etc..
      if (this.multiple && this.options.selectedTextFormat.indexOf('count') > -1) {
        var max = this.options.selectedTextFormat.split('>');
        if ((max.length > 1 && selectedItems.length > max[1]) || (max.length == 1 && selectedItems.length >= 2)) {
          notDisabled = this.options.hideDisabled ? ', [disabled]' : '';
          var totalCount = this.$element.find('option').not('[data-divider="true"], [data-hidden="true"]' + notDisabled).length,
              tr8nText = (typeof this.options.countSelectedText === 'function') ? this.options.countSelectedText(selectedItems.length, totalCount) : this.options.countSelectedText;
          title = tr8nText.replace('{0}', selectedItems.length.toString()).replace('{1}', totalCount.toString());
        }
      }

      if (this.options.title == undefined) {
        this.options.title = this.$element.attr('title');
      }

      if (this.options.selectedTextFormat == 'static') {
        title = this.options.title;
      }

      //If we dont have a title, then use the default, or if nothing is set at all, use the not selected text
      if (!title) {
        title = typeof this.options.title !== 'undefined' ? this.options.title : this.options.noneSelectedText;
      }

      //strip all html-tags and trim the result
      this.$button.attr('title', $.trim(title.replace(/<[^>]*>?/g, '')));
      this.$button.children('.filter-option').html(title);

      this.$element.trigger('rendered.bs.select');
    },

    /**
     * @param [style]
     * @param [status]
     */
    setStyle: function (style, status) {
      if (this.$element.attr('class')) {
        this.$newElement.addClass(this.$element.attr('class').replace(/selectpicker|mobile-device|bs-select-hidden|validate\[.*\]/gi, ''));
      }

      var buttonClass = style ? style : this.options.style;

      if (status == 'add') {
        this.$button.addClass(buttonClass);
      } else if (status == 'remove') {
        this.$button.removeClass(buttonClass);
      } else {
        this.$button.removeClass(this.options.style);
        this.$button.addClass(buttonClass);
      }
    },

    liHeight: function (refresh) {
      if (!refresh && (this.options.size === false || this.sizeInfo)) return;

      var newElement = document.createElement('div'),
          menu = document.createElement('div'),
          menuInner = document.createElement('ul'),
          divider = document.createElement('li'),
          li = document.createElement('li'),
          a = document.createElement('a'),
          text = document.createElement('span'),
          header = this.options.header && this.$menu.find('.popover-title').length > 0 ? this.$menu.find('.popover-title')[0].cloneNode(true) : null,
          search = this.options.liveSearch ? document.createElement('div') : null,
          actions = this.options.actionsBox && this.multiple && this.$menu.find('.bs-actionsbox').length > 0 ? this.$menu.find('.bs-actionsbox')[0].cloneNode(true) : null,
          doneButton = this.options.doneButton && this.multiple && this.$menu.find('.bs-donebutton').length > 0 ? this.$menu.find('.bs-donebutton')[0].cloneNode(true) : null;

      text.className = 'text';
      newElement.className = this.$menu[0].parentNode.className + ' open';
      menu.className = 'dropdown-menu open';
      menuInner.className = 'dropdown-menu inner';
      divider.className = 'divider';

      text.appendChild(document.createTextNode('Inner text'));
      a.appendChild(text);
      li.appendChild(a);
      menuInner.appendChild(li);
      menuInner.appendChild(divider);
      if (header) menu.appendChild(header);
      if (search) {
        // create a span instead of input as creating an input element is slower
        var input = document.createElement('span');
        search.className = 'bs-searchbox';
        input.className = 'form-control';
        search.appendChild(input);
        menu.appendChild(search);
      }
      if (actions) menu.appendChild(actions);
      menu.appendChild(menuInner);
      if (doneButton) menu.appendChild(doneButton);
      newElement.appendChild(menu);

      document.body.appendChild(newElement);

      var liHeight = a.offsetHeight,
          headerHeight = header ? header.offsetHeight : 0,
          searchHeight = search ? search.offsetHeight : 0,
          actionsHeight = actions ? actions.offsetHeight : 0,
          doneButtonHeight = doneButton ? doneButton.offsetHeight : 0,
          dividerHeight = $(divider).outerHeight(true),
          // fall back to jQuery if getComputedStyle is not supported
          menuStyle = typeof getComputedStyle === 'function' ? getComputedStyle(menu) : false,
          $menu = menuStyle ? null : $(menu),
          menuPadding = parseInt(menuStyle ? menuStyle.paddingTop : $menu.css('paddingTop')) +
                        parseInt(menuStyle ? menuStyle.paddingBottom : $menu.css('paddingBottom')) +
                        parseInt(menuStyle ? menuStyle.borderTopWidth : $menu.css('borderTopWidth')) +
                        parseInt(menuStyle ? menuStyle.borderBottomWidth : $menu.css('borderBottomWidth')),
          menuExtras =  menuPadding +
                        parseInt(menuStyle ? menuStyle.marginTop : $menu.css('marginTop')) +
                        parseInt(menuStyle ? menuStyle.marginBottom : $menu.css('marginBottom')) + 2;

      document.body.removeChild(newElement);

      this.sizeInfo = {
        liHeight: liHeight,
        headerHeight: headerHeight,
        searchHeight: searchHeight,
        actionsHeight: actionsHeight,
        doneButtonHeight: doneButtonHeight,
        dividerHeight: dividerHeight,
        menuPadding: menuPadding,
        menuExtras: menuExtras
      };
    },

    setSize: function () {
      this.findLis();
      this.liHeight();

      if (this.options.header) this.$menu.css('padding-top', 0);
      if (this.options.size === false) return;

      var that = this,
          $menu = this.$menu,
          $menuInner = this.$menuInner,
          $window = $(window),
          selectHeight = this.$newElement[0].offsetHeight,
          liHeight = this.sizeInfo['liHeight'],
          headerHeight = this.sizeInfo['headerHeight'],
          searchHeight = this.sizeInfo['searchHeight'],
          actionsHeight = this.sizeInfo['actionsHeight'],
          doneButtonHeight = this.sizeInfo['doneButtonHeight'],
          divHeight = this.sizeInfo['dividerHeight'],
          menuPadding = this.sizeInfo['menuPadding'],
          menuExtras = this.sizeInfo['menuExtras'],
          notDisabled = this.options.hideDisabled ? '.disabled' : '',
          menuHeight,
          getHeight,
          selectOffsetTop,
          selectOffsetBot,
          posVert = function () {
            selectOffsetTop = that.$newElement.offset().top - $window.scrollTop();
            selectOffsetBot = $window.height() - selectOffsetTop - selectHeight;
          };

      posVert();

      if (this.options.size === 'auto') {
        var getSize = function () {
          var minHeight,
              hasClass = function (className, include) {
                return function (element) {
                    if (include) {
                        return (element.classList ? element.classList.contains(className) : $(element).hasClass(className));
                    } else {
                        return !(element.classList ? element.classList.contains(className) : $(element).hasClass(className));
                    }
                };
              },
              lis = that.$menuInner[0].getElementsByTagName('li'),
              lisVisible = Array.prototype.filter ? Array.prototype.filter.call(lis, hasClass('hidden', false)) : that.$lis.not('.hidden'),
              optGroup = Array.prototype.filter ? Array.prototype.filter.call(lisVisible, hasClass('dropdown-header', true)) : lisVisible.filter('.dropdown-header');

          posVert();
          menuHeight = selectOffsetBot - menuExtras;

          if (that.options.container) {
            if (!$menu.data('height')) $menu.data('height', $menu.height());
            getHeight = $menu.data('height');
          } else {
            getHeight = $menu.height();
          }

          if (that.options.dropupAuto) {
            that.$newElement.toggleClass('dropup', selectOffsetTop > selectOffsetBot && (menuHeight - menuExtras) < getHeight);
          }
          if (that.$newElement.hasClass('dropup')) {
            menuHeight = selectOffsetTop - menuExtras;
          }

          if ((lisVisible.length + optGroup.length) > 3) {
            minHeight = liHeight * 3 + menuExtras - 2;
          } else {
            minHeight = 0;
          }

          $menu.css({
            'max-height': menuHeight + 'px',
            'overflow': 'hidden',
            'min-height': minHeight + headerHeight + searchHeight + actionsHeight + doneButtonHeight + 'px'
          });
          $menuInner.css({
            'max-height': menuHeight - headerHeight - searchHeight - actionsHeight - doneButtonHeight - menuPadding + 'px',
            'overflow-y': 'auto',
            'min-height': Math.max(minHeight - menuPadding, 0) + 'px'
          });
        };
        getSize();
        this.$searchbox.off('input.getSize propertychange.getSize').on('input.getSize propertychange.getSize', getSize);
        $window.off('resize.getSize scroll.getSize').on('resize.getSize scroll.getSize', getSize);
      } else if (this.options.size && this.options.size != 'auto' && this.$lis.not(notDisabled).length > this.options.size) {
        var optIndex = this.$lis.not('.divider').not(notDisabled).children().slice(0, this.options.size).last().parent().index(),
            divLength = this.$lis.slice(0, optIndex + 1).filter('.divider').length;
        menuHeight = liHeight * this.options.size + divLength * divHeight + menuPadding;

        if (that.options.container) {
          if (!$menu.data('height')) $menu.data('height', $menu.height());
          getHeight = $menu.data('height');
        } else {
          getHeight = $menu.height();
        }

        if (that.options.dropupAuto) {
          //noinspection JSUnusedAssignment
          this.$newElement.toggleClass('dropup', selectOffsetTop > selectOffsetBot && (menuHeight - menuExtras) < getHeight);
        }
        $menu.css({
          'max-height': menuHeight + headerHeight + searchHeight + actionsHeight + doneButtonHeight + 'px',
          'overflow': 'hidden',
          'min-height': ''
        });
        $menuInner.css({
          'max-height': menuHeight - menuPadding + 'px',
          'overflow-y': 'auto',
          'min-height': ''
        });
      }
    },

    setWidth: function () {
      if (this.options.width === 'auto') {
        this.$menu.css('min-width', '0');

        // Get correct width if element is hidden
        var $selectClone = this.$menu.parent().clone().appendTo('body'),
            $selectClone2 = this.options.container ? this.$newElement.clone().appendTo('body') : $selectClone,
            ulWidth = $selectClone.children('.dropdown-menu').outerWidth(),
            btnWidth = $selectClone2.css('width', 'auto').children('button').outerWidth();

        $selectClone.remove();
        $selectClone2.remove();

        // Set width to whatever's larger, button title or longest option
        this.$newElement.css('width', Math.max(ulWidth, btnWidth) + 'px');
      } else if (this.options.width === 'fit') {
        // Remove inline min-width so width can be changed from 'auto'
        this.$menu.css('min-width', '');
        this.$newElement.css('width', '').addClass('fit-width');
      } else if (this.options.width) {
        // Remove inline min-width so width can be changed from 'auto'
        this.$menu.css('min-width', '');
        this.$newElement.css('width', this.options.width);
      } else {
        // Remove inline min-width/width so width can be changed
        this.$menu.css('min-width', '');
        this.$newElement.css('width', '');
      }
      // Remove fit-width class if width is changed programmatically
      if (this.$newElement.hasClass('fit-width') && this.options.width !== 'fit') {
        this.$newElement.removeClass('fit-width');
      }
    },

    selectPosition: function () {
      this.$bsContainer = $('<div class="bs-container" />');

      var that = this,
          pos,
          actualHeight,
          getPlacement = function ($element) {
            that.$bsContainer.addClass($element.attr('class').replace(/form-control|fit-width/gi, '')).toggleClass('dropup', $element.hasClass('dropup'));
            pos = $element.offset();
            actualHeight = $element.hasClass('dropup') ? 0 : $element[0].offsetHeight;
            that.$bsContainer.css({
              'top': pos.top + actualHeight,
              'left': pos.left,
              'width': $element[0].offsetWidth
            });
          };

      this.$button.on('click', function () {
        var $this = $(this);

        if (that.isDisabled()) {
          return;
        }

        getPlacement(that.$newElement);

        that.$bsContainer
          .appendTo(that.options.container)
          .toggleClass('open', !$this.hasClass('open'))
          .append(that.$menu);
      });

      $(window).on('resize scroll', function () {
        getPlacement(that.$newElement);
      });

      this.$element.on('hide.bs.select', function () {
        that.$menu.data('height', that.$menu.height());
        that.$bsContainer.detach();
      });
    },

    setSelected: function (index, selected, $lis) {
      if (!$lis) {
        $lis = this.findLis().eq(this.liObj[index]);
      }

      $lis.toggleClass('selected', selected);
    },

    setDisabled: function (index, disabled, $lis) {
      if (!$lis) {
        $lis = this.findLis().eq(this.liObj[index]);
      }

      if (disabled) {
        $lis.addClass('disabled').children('a').attr('href', '#').attr('tabindex', -1);
      } else {
        $lis.removeClass('disabled').children('a').removeAttr('href').attr('tabindex', 0).attr('aria-disabled', 'false').attr('role', 'option');
      }
    },

    isDisabled: function () {
      return this.$element[0].disabled;
    },

    checkDisabled: function () {
      var that = this;

      if (this.isDisabled()) {
        this.$newElement.addClass('disabled');
        //this.$button.addClass('disabled').attr('tabindex', -1);
      } else {
        if (this.$button.hasClass('disabled')) {
          this.$newElement.removeClass('disabled');
          this.$button.removeClass('disabled');
        }

        if (this.$button.attr('tabindex') == -1 && !this.$element.data('tabindex')) {
          this.$button.removeAttr('tabindex');
        }
      }

      this.$button.click(function () {
        return !that.isDisabled();
      });
    },

    tabIndex: function () {
      if (this.$element.data('tabindex') !== this.$element.attr('tabindex') &&
        (this.$element.attr('tabindex') !== -98 && this.$element.attr('tabindex') !== '-98')) {
        this.$element.data('tabindex', this.$element.attr('tabindex'));
        this.$button.attr('tabindex', this.$element.data('tabindex'));
      }

      this.$element.attr('tabindex', -98);
	  this.$element.attr('aria-hidden', "true");
    },

    clickListener: function () {
      var that = this,
          $document = $(document);

      this.$newElement.on('touchstart.dropdown', '.dropdown-menu', function (e) {
        e.stopPropagation();
      });

      $document.data('spaceSelect', false);

      this.$button.on('keyup', function (e) {
        if (/(32)/.test(e.keyCode.toString(10)) && $document.data('spaceSelect')) {
            e.preventDefault();
            $document.data('spaceSelect', false);
        }
      });

      this.$button.on('click', function () {
        that.setSize();
      });

      this.$element.on('shown.bs.select', function () {
        if (!that.options.liveSearch && !that.multiple) {
          that.$menuInner.find('.selected a').focus();
        } else if (!that.multiple) {
          var selectedIndex = that.liObj[that.$element[0].selectedIndex];

          if (typeof selectedIndex !== 'number' || that.options.size === false) return;

          // scroll to selected option
          var offset = that.$lis.eq(selectedIndex)[0].offsetTop - that.$menuInner[0].offsetTop;
          offset = offset - that.$menuInner[0].offsetHeight/2 + that.sizeInfo.liHeight/2;
          that.$menuInner[0].scrollTop = offset;
        }
      });

      this.$menuInner.on('click', 'li a', function (e) {
        var $this = $(this),
            clickedIndex = $this.parent().data('originalIndex'),
            prevValue = that.$element.val(),
            prevIndex = that.$element.prop('selectedIndex');

        // Don't close on multi choice menu
        if (that.multiple) {
          e.stopPropagation();
        }

        e.preventDefault();

        //Don't run if we have been disabled
        if (!that.isDisabled() && !$this.parent().hasClass('disabled')) {
          var $options = that.$element.find('option'),
              $option = $options.eq(clickedIndex),
              state = $option.prop('selected'),
              $optgroup = $option.parent('optgroup'),
              maxOptions = that.options.maxOptions,
              maxOptionsGrp = $optgroup.data('maxOptions') || false;

          if (!that.multiple) { // Deselect all others if not multi select box
            $options.prop('selected', false);
            $option.prop('selected', true);
            that.$menuInner.find('.selected').removeClass('selected');
            that.setSelected(clickedIndex, true);
          } else { // Toggle the one we have chosen if we are multi select.
            $option.prop('selected', !state);
            that.setSelected(clickedIndex, !state);
            $this.blur();

            if (maxOptions !== false || maxOptionsGrp !== false) {
              var maxReached = maxOptions < $options.filter(':selected').length,
                  maxReachedGrp = maxOptionsGrp < $optgroup.find('option:selected').length;

              if ((maxOptions && maxReached) || (maxOptionsGrp && maxReachedGrp)) {
                if (maxOptions && maxOptions == 1) {
                  $options.prop('selected', false);
                  $option.prop('selected', true);
                  that.$menuInner.find('.selected').removeClass('selected');
                  that.setSelected(clickedIndex, true);
                } else if (maxOptionsGrp && maxOptionsGrp == 1) {
                  $optgroup.find('option:selected').prop('selected', false);
                  $option.prop('selected', true);
                  var optgroupID = $this.parent().data('optgroup');
                  that.$menuInner.find('[data-optgroup="' + optgroupID + '"]').removeClass('selected');
                  that.setSelected(clickedIndex, true);
                } else {
                  var maxOptionsArr = (typeof that.options.maxOptionsText === 'function') ?
                          that.options.maxOptionsText(maxOptions, maxOptionsGrp) : that.options.maxOptionsText,
                      maxTxt = maxOptionsArr[0].replace('{n}', maxOptions),
                      maxTxtGrp = maxOptionsArr[1].replace('{n}', maxOptionsGrp),
                      $notify = $('<div class="notify"></div>');
                  // If {var} is set in array, replace it
                  /** @deprecated */
                  if (maxOptionsArr[2]) {
                    maxTxt = maxTxt.replace('{var}', maxOptionsArr[2][maxOptions > 1 ? 0 : 1]);
                    maxTxtGrp = maxTxtGrp.replace('{var}', maxOptionsArr[2][maxOptionsGrp > 1 ? 0 : 1]);
                  }

                  $option.prop('selected', false);

                  that.$menu.append($notify);

                  if (maxOptions && maxReached) {
                    $notify.append($('<div>' + maxTxt + '</div>'));
                    that.$element.trigger('maxReached.bs.select');
                  }

                  if (maxOptionsGrp && maxReachedGrp) {
                    $notify.append($('<div>' + maxTxtGrp + '</div>'));
                    that.$element.trigger('maxReachedGrp.bs.select');
                  }

                  setTimeout(function () {
                    that.setSelected(clickedIndex, false);
                  }, 10);

                  $notify.delay(750).fadeOut(300, function () {
                    $(this).remove();
                  });
                }
              }
            }
          }

          if (!that.multiple) {
            that.$button.focus();
            setTimeout(function(){ that.$button.focus(); }, 100);
          } else if (that.options.liveSearch) {
            that.$searchbox.focus();
          }

          // Trigger select 'change'
          if ((prevValue != that.$element.val() && that.multiple) || (prevIndex != that.$element.prop('selectedIndex') && !that.multiple)) {
            // $option.prop('selected') is current option state (selected/unselected). state is previous option state.
            that.$element
              .trigger('changed.bs.select', [clickedIndex, $option.prop('selected'), state])
              .triggerNative('change');
          }
        }
      });

      this.$menu.on('click', 'li.disabled a, .popover-title, .popover-title :not(.close)', function (e) {
        if (e.currentTarget == this) {
          e.preventDefault();
          e.stopPropagation();
          if (that.options.liveSearch && !$(e.target).hasClass('close')) {
            that.$searchbox.focus();
          } else {
            that.$button.focus();
          }
        }
      });

      this.$menuInner.on('click', '.divider, .dropdown-header', function (e) {
        e.preventDefault();
        e.stopPropagation();
        if (that.options.liveSearch) {
          that.$searchbox.focus();
        } else {
          that.$button.focus();
        }
      });

      this.$menu.on('click', '.popover-title .close', function () {
        that.$button.click();
      });

      this.$searchbox.on('click', function (e) {
        e.stopPropagation();
      });

      this.$menu.on('click', '.actions-btn', function (e) {
        if (that.options.liveSearch) {
          that.$searchbox.focus();
        } else {
          that.$button.focus();
        }

        e.preventDefault();
        e.stopPropagation();

        if ($(this).hasClass('bs-select-all')) {
          that.selectAll();
        } else {
          that.deselectAll();
        }
      });

      this.$element.change(function () {
        that.render(false);
      });
    },

    liveSearchListener: function () {
      var that = this,
          $no_results = $('<li class="no-results"></li>');

      this.$button.on('click.dropdown.data-api touchstart.dropdown.data-api', function () {
        that.$menuInner.find('.active').removeClass('active');
        if (!!that.$searchbox.val()) {
          that.$searchbox.val('');
          that.$lis.not('.is-hidden').removeClass('hidden');
          if (!!$no_results.parent().length) $no_results.remove();
        }
        if (!that.multiple) that.$menuInner.find('.selected').addClass('active');
        setTimeout(function () {
          that.$searchbox.focus();
        }, 10);
      });

      this.$searchbox.on('click.dropdown.data-api focus.dropdown.data-api touchend.dropdown.data-api', function (e) {
        e.stopPropagation();
      });

      this.$searchbox.on('input propertychange', function () {
        if (that.$searchbox.val()) {
          var $searchBase = that.$lis.not('.is-hidden').removeClass('hidden').children('a');
          if (that.options.liveSearchNormalize) {
            $searchBase = $searchBase.not(':a' + that._searchStyle() + '("' + normalizeToBase(that.$searchbox.val()) + '")');
          } else {
            $searchBase = $searchBase.not(':' + that._searchStyle() + '("' + that.$searchbox.val() + '")');
          }
          $searchBase.parent().addClass('hidden');

          that.$lis.filter('.dropdown-header').each(function () {
            var $this = $(this),
                optgroup = $this.data('optgroup');

            if (that.$lis.filter('[data-optgroup=' + optgroup + ']').not($this).not('.hidden').length === 0) {
              $this.addClass('hidden');
              that.$lis.filter('[data-optgroup=' + optgroup + 'div]').addClass('hidden');
            }
          });

          var $lisVisible = that.$lis.not('.hidden');

          // hide divider if first or last visible, or if followed by another divider
          $lisVisible.each(function (index) {
            var $this = $(this);

            if ($this.hasClass('divider') && (
              $this.index() === $lisVisible.first().index() ||
              $this.index() === $lisVisible.last().index() ||
              $lisVisible.eq(index + 1).hasClass('divider'))) {
              $this.addClass('hidden');
            }
          });

          if (!that.$lis.not('.hidden, .no-results').length) {
            if (!!$no_results.parent().length) {
              $no_results.remove();
            }
            $no_results.html(that.options.noneResultsText.replace('{0}', '"' + htmlEscape(that.$searchbox.val()) + '"')).show();
            that.$menuInner.append($no_results);
          } else if (!!$no_results.parent().length) {
            $no_results.remove();
          }
        } else {
          that.$lis.not('.is-hidden').removeClass('hidden');
          if (!!$no_results.parent().length) {
            $no_results.remove();
          }
        }

        that.$lis.filter('.active').removeClass('active');
        if (that.$searchbox.val()) that.$lis.not('.hidden, .divider, .dropdown-header').eq(0).addClass('active').children('a').focus();
        $(this).focus();
      });
    },

    _searchStyle: function () {
      var styles = {
        begins: 'ibegins',
        startsWith: 'ibegins'
      };

      return styles[this.options.liveSearchStyle] || 'icontains';
    },

    val: function (value) {
      if (typeof value !== 'undefined') {
        this.$element.val(value);
        this.render();

        return this.$element;
      } else {
        return this.$element.val();
      }
    },

    changeAll: function (status) {
      if (typeof status === 'undefined') status = true;

      this.findLis();

      var $options = this.$element.find('option'),
          $lisVisible = this.$lis.not('.divider, .dropdown-header, .disabled, .hidden').toggleClass('selected', status),
          lisVisLen = $lisVisible.length,
          selectedOptions = [];

      for (var i = 0; i < lisVisLen; i++) {
        var origIndex = $lisVisible[i].getAttribute('data-original-index');
        selectedOptions[selectedOptions.length] = $options.eq(origIndex)[0];
      }

      $(selectedOptions).prop('selected', status);

      this.render(false);

      this.$element
        .trigger('changed.bs.select')
        .triggerNative('change');
    },

    selectAll: function () {
      return this.changeAll(true);
    },

    deselectAll: function () {
      return this.changeAll(false);
    },

    toggle: function (e) {
      e = e || window.event;

      if (e) e.stopPropagation();

      this.$button.trigger('click');
    },

    keydown: function (e) {
      var $this = $(this),
          $parent = $this.is('input') ? $this.parent().parent() : $this.parent(),
          $items,
          that = $parent.data('this'),
          index,
          next,
          first,
          last,
          prev,
          nextPrev,
          prevIndex,
          isActive,
          selector = ':not(.disabled, .hidden, .dropdown-header, .divider)',
          keyCodeMap = {
            32: ' ',
            48: '0',
            49: '1',
            50: '2',
            51: '3',
            52: '4',
            53: '5',
            54: '6',
            55: '7',
            56: '8',
            57: '9',
            59: ';',
            65: 'a',
            66: 'b',
            67: 'c',
            68: 'd',
            69: 'e',
            70: 'f',
            71: 'g',
            72: 'h',
            73: 'i',
            74: 'j',
            75: 'k',
            76: 'l',
            77: 'm',
            78: 'n',
            79: 'o',
            80: 'p',
            81: 'q',
            82: 'r',
            83: 's',
            84: 't',
            85: 'u',
            86: 'v',
            87: 'w',
            88: 'x',
            89: 'y',
            90: 'z',
            96: '0',
            97: '1',
            98: '2',
            99: '3',
            100: '4',
            101: '5',
            102: '6',
            103: '7',
            104: '8',
            105: '9'
          };


          if (that) {

      if (that.options.liveSearch) $parent = $this.parent().parent();

      if (that.options.container) $parent = that.$menu;

      $items = $('[role=menu] li', $parent);

      isActive = that.$newElement.hasClass('open');

      if (!isActive && (e.keyCode >= 48 && e.keyCode <= 57 || e.keyCode >= 96 && e.keyCode <= 105 || e.keyCode >= 65 && e.keyCode <= 90)) {
        if (!that.options.container) {
          that.setSize();
          that.$menu.parent().addClass('open');
          isActive = true;
        } else {
          that.$button.trigger('click');
        }
        that.$searchbox.focus();
      }

      if (that.options.liveSearch) {
        if (/(^9$|27)/.test(e.keyCode.toString(10)) && isActive && that.$menu.find('.active').length === 0) {
          e.preventDefault();
          that.$menu.parent().removeClass('open');
          if (that.options.container) that.$newElement.removeClass('open');
          that.$button.focus();
        }
        // $items contains li elements when liveSearch is enabled
        $items = $('[role=menu] li' + selector, $parent);
        if (!$this.val() && !/(38|40)/.test(e.keyCode.toString(10))) {
          if ($items.filter('.active').length === 0) {
            $items = that.$menuInner.find('li');
            if (that.options.liveSearchNormalize) {
              $items = $items.filter(':a' + that._searchStyle() + '(' + normalizeToBase(keyCodeMap[e.keyCode]) + ')');
            } else {
              $items = $items.filter(':' + that._searchStyle() + '(' + keyCodeMap[e.keyCode] + ')');
            }
          }
        }
      }

      if (!$items.length) return;

      if (/(38|40)/.test(e.keyCode.toString(10))) {
        index = $items.index($items.find('a').filter(':focus').parent());
        first = $items.filter(selector).first().index();
        last = $items.filter(selector).last().index();
        next = $items.eq(index).nextAll(selector).eq(0).index();
        prev = $items.eq(index).prevAll(selector).eq(0).index();
        nextPrev = $items.eq(next).prevAll(selector).eq(0).index();

        if (that.options.liveSearch) {
          $items.each(function (i) {
            if (!$(this).hasClass('disabled')) {
              $(this).data('index', i);
            }
          });
          index = $items.index($items.filter('.active'));
          first = $items.first().data('index');
          last = $items.last().data('index');
          next = $items.eq(index).nextAll().eq(0).data('index');
          prev = $items.eq(index).prevAll().eq(0).data('index');
          nextPrev = $items.eq(next).prevAll().eq(0).data('index');
        }

        prevIndex = $this.data('prevIndex');

        if (e.keyCode == 38) {
          if (that.options.liveSearch) index--;
          if (index != nextPrev && index > prev) index = prev;
          if (index < first) index = first;
          if (index == prevIndex) index = last;
        } else if (e.keyCode == 40) {
          if (that.options.liveSearch) index++;
          if (index == -1) index = 0;
          if (index != nextPrev && index < next) index = next;
          if (index > last) index = last;
          if (index == prevIndex) index = first;
        }

        $this.data('prevIndex', index);

        if (!that.options.liveSearch) {
          $items.eq(index).children('a').focus();
        } else {
          e.preventDefault();
          if (!$this.hasClass('dropdown-toggle')) {
            $items.removeClass('active').eq(index).addClass('active').children('a').focus();
            $this.focus();
          }
        }

      } else if (!$this.is('input')) {
        var keyIndex = [],
            count,
            prevKey;

        $items.each(function () {
          if (!$(this).hasClass('disabled')) {
            if ($.trim($(this).children('a').text().toLowerCase()).substring(0, 1) == keyCodeMap[e.keyCode]) {
              keyIndex.push($(this).index());
            }
          }
        });

        count = $(document).data('keycount');
        count++;
        $(document).data('keycount', count);

        prevKey = $.trim($(':focus').text().toLowerCase()).substring(0, 1);

        if (prevKey != keyCodeMap[e.keyCode]) {
          count = 1;
          $(document).data('keycount', count);
        } else if (count >= keyIndex.length) {
          $(document).data('keycount', 0);
          if (count > keyIndex.length) count = 1;
        }

        $items.eq(keyIndex[count - 1]).children('a').focus();
      }

      // Select focused option if "Enter", "Spacebar" or "Tab" (when selectOnTab is true) are pressed inside the menu.
      if ((/(13|32)/.test(e.keyCode.toString(10)) || (/(^9$)/.test(e.keyCode.toString(10)) && that.options.selectOnTab)) && isActive) {
        if (!/(32)/.test(e.keyCode.toString(10))) e.preventDefault();
        if (!that.options.liveSearch) {
          var elem = $(':focus');
          elem.click();
          // Bring back focus for multiselects
          elem.focus();
          // Prevent screen from scrolling if the user hit the spacebar
          e.preventDefault();
          // Fixes spacebar selection of dropdown items in FF & IE
          $(document).data('spaceSelect', true);
        } else if (!/(32)/.test(e.keyCode.toString(10))) {
          that.$menuInner.find('.active a').click();
          $this.focus();
        }
        $(document).data('keycount', 0);
      }

      if ((/(^9$|27)/.test(e.keyCode.toString(10)) && isActive && (that.multiple || that.options.liveSearch)) || (/(27)/.test(e.keyCode.toString(10)) && !isActive)) {
        that.$menu.parent().removeClass('open');
        if (that.options.container) that.$newElement.removeClass('open');
        that.$button.focus();
      }
    }
    },

    mobile: function () {
      this.$element.addClass('mobile-device');
    },

    refresh: function () {
      this.$lis = null;
      this.liObj = {};
      this.reloadLi();
      this.render();
      this.checkDisabled();
      this.liHeight(true);
      this.setStyle();
      this.setWidth();
      if (this.$lis) this.$searchbox.trigger('propertychange');

      this.$element.trigger('refreshed.bs.select');
    },

    hide: function () {
      this.$newElement.hide();
    },

    show: function () {
      this.$newElement.show();
    },

    remove: function () {
      this.$newElement.remove();
      this.$element.remove();
    },

    destroy: function () {
        this.$newElement.before(this.$element).remove();

        if (this.$bsContainer) {
            this.$bsContainer.remove();
        } else {
            this.$menu.remove();
        }

        this.$element
          .off('.bs.select')
          .removeData('selectpicker')
          .removeClass('bs-select-hidden selectpicker');
    }
  };

  // SELECTPICKER PLUGIN DEFINITION
  // ==============================
  function Plugin(option, event) {
    // get the args of the outer function..
    var args = arguments;
    // The arguments of the function are explicitly re-defined from the argument list, because the shift causes them
    // to get lost/corrupted in android 2.3 and IE9 #715 #775
    var _option = option,
        _event = event;
    [].shift.apply(args);

    var value;
    var chain = this.each(function () {
      var $this = $(this);
      if ($this.is('select')) {
        var data = $this.data('selectpicker'),
            options = typeof _option == 'object' && _option;

        if (!data) {
          var config = $.extend({}, Selectpicker.DEFAULTS, $.fn.selectpicker.defaults || {}, $this.data(), options);
          config.template = $.extend({}, Selectpicker.DEFAULTS.template, ($.fn.selectpicker.defaults ? $.fn.selectpicker.defaults.template : {}), $this.data().template, options.template);
          $this.data('selectpicker', (data = new Selectpicker(this, config, _event)));
        } else if (options) {
          for (var i in options) {
            if (options.hasOwnProperty(i)) {
              data.options[i] = options[i];
            }
          }
        }

        if (typeof _option == 'string') {
          if (data[_option] instanceof Function) {
            value = data[_option].apply(data, args);
          } else {
            value = data.options[_option];
          }
        }
      }
    });

    if (typeof value !== 'undefined') {
      //noinspection JSUnusedAssignment
      return value;
    } else {
      return chain;
    }
  }

  var old = $.fn.selectpicker;
  $.fn.selectpicker = Plugin;
  $.fn.selectpicker.Constructor = Selectpicker;

  // SELECTPICKER NO CONFLICT
  // ========================
  $.fn.selectpicker.noConflict = function () {
    $.fn.selectpicker = old;
    return this;
  };

  $(".zoom-anim-target").css('transition', 'transform 1s cubic-bezier(0.19, 1, 0.22, 1) 0s, -webkit-transform 1s cubic-bezier(0.19, 1, 0.22, 1) 0s')

  $('.flexslider img').css('transition', 'transform 1s cubic-bezier(0.19, 1, 0.22, 1) 0s, -webkit-transform 1s cubic-bezier(0.19, 1, 0.22, 1) 0s')

  $('.nutrition-calculator-alter img').css('transition', 'transform 1s cubic-bezier(0.19, 1, 0.22, 1) 0s, -webkit-transform 1s cubic-bezier(0.19, 1, 0.22, 1) 0s')


  $(document)
      .data('keycount', 0)
      .on('keydown.bs.select', '.bootstrap-select [data-toggle=dropdown], .bootstrap-select [role="menu"], .bs-searchbox input', Selectpicker.prototype.keydown)
      .on('focusin.modal', '.bootstrap-select [data-toggle=dropdown], .bootstrap-select [role="menu"], .bs-searchbox input', function (e) {
        e.stopPropagation();
      });

  // SELECTPICKER DATA-API
  // =====================
  $(window).on('load.bs.select.data-api', function () {
    $('.selectpicker').each(function () {
      var $selectpicker = $(this);
      Plugin.call($selectpicker, $selectpicker.data());
    })
  });
})(jQuery);


}));


/*!
 *  End Bootstrap-select v1.10.0
 */



/*!
*
* Tabbale and focus trap
*
*/





/*!
 *  End Tabbale and focus trap
 */
