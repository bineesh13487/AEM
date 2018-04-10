/*
 * File Name: main.js
 * Author: mcd-wifi
 * Description: This file is used for global functions and main slider component.
 */
var winHeight = window.innerHeight
var winWidth = window.innerWidth;
var $gallery = $('.main-gallery');

$(window).on('load', function() {
    $gallery.find('div.is-selected').find('div.tile').mouseover();

    /* Global function used to clear focus */
    $('*').on('mouseup mousedown', function() {
        $(this).blur();
    });

    $('.secondaryControls .fa.fa-pause, .secondaryControls .fa.fa-play').keypress(function(e) {
        var key = e.which;
        if (key === 13 || key === 32) {
            $(this).click();
            e.preventDefault();
        }
    });

    $('.tile-img').keypress(function(e) {
        var key = e.which;
        if (key === 13) {
            $(this).parents().find('.tile').trigger('click');
        }
    });

    $('*').mousedown(function() {
        $(this).addClass('noOutline');
        $('.modal').addClass('noOutline');
    });

    $('body').keypress(function() {
        $(this).find('.noOutline').removeClass('noOutline');
    });

    $.ctrlShift = function(key, callback, args) {
        $(document).keydown(function(e) {
            if (!args) {
                args = []; // IE barks when args is null 
            }
            if (e.keyCode === key && e.ctrlKey && e.shiftKey) {
                callback.apply(this, args);
                return false;
            }
        });
    };

    $.ctrlShift(13, function() {
        $('#slider .flex-play').trigger('click');
        $('#slider .flex-play').focus();
    });

    /*Global function, if modal popup is opened*/
    $('.modal').on('show.bs.modal', function() {
        $('body').css('overflow', 'hidden');
    });

    /*Function to be performed when modal popup is closed*/
    $('.modal').on('hidden.bs.modal', function() {
        $('.focused').focus();
        $('.focused').removeClass('focused');
        $('body').css('overflow', '');
    });
});

$(document).ready(function() {
    var mql768 = window.matchMedia('only screen and (min-width: 768px)');

    initFlickitySliders(); /*Initialization of Gallery slider*/
    createSliderButtons('.instagram-stream'); /*createSliderButtons('#slider');*/

    /*close modal popup for image and remove iframe*/
    $('#popup').on('hidden.bs.modal', function() {
        $('#popup .modal-body iframe').remove();
    });

    /*close modal popup for image and remove iframe*/
    $('#videoPopup').on('hidden.bs.modal', function() {
        $('#videoPopup .modal-body video').remove();
        $('#videoPopup .modal-body iframe').remove();

        if (mql768.matches && $('.billboard > div:visible').find('video').length > 0) {
            setTimeout(function() {
                if ($('.billboard > div:visible').find('video').length > 0 && $('.billboard > div:visible').find('video').get(0).paused) {
                    $('.billboard > div:visible').find('video').get(0).play();
                    $('.billboard > div:visible').find('video').get(0).currentTime = 0;
                }
            }, 150);
        }
    });

    /*Add attributes on load of video popup*/
    $('#videoPopup').on('show.bs.modal', function() {
        setTimeout(function() {
            if ($('#videoPopup').find('video').length > 0) {
                modalPositionCenter('video');
            } else {
                modalPositionCenter('iframe');
            }
        }, 500);

        $(window).on('resize', function() {
            if ($('#videoPopup').find('video').length > 0) {
                modalPositionCenter('video');
            } else {
                modalPositionCenter('iframe');
            }
        });


        if (mql768.matches && $('.billboard > div:visible').find('video').length > 0) {
            $('.billboard > div:visible').find('video').get(0).currentTime = 0;
            setTimeout(function() {
                if (mql768.matches && $('.billboard > div:visible').find('video').length > 0) {
                    $('.billboard > div:visible').find('video').get(0).pause();
                }
            }, 500);
        }
    });

    /*Call to a function when slider tile stops moving*/
    $gallery.on('settle', function() {
        $(this).find('img').removeClass('mouseOver');
        $(this).find('div.gallery-cell .tile-img').removeClass('border8');
        $(this).find('div.is-selected .tile-img').addClass('border8');
        $(this).find('div.is-selected .tile-img').addClass('mouseOver');
    });

    /*Call to a function when a particular slider tile is selected*/
    $gallery.on('cellSelect', function() {
        $(this).find('div.is-selected').find('div.tile').mouseover();
        $(this).find('div.gallery-cell .tile-img').removeClass('border8');
        $(this).find('div.gallery-cell .tile-img').removeClass('mouseOver');
        $(this).find('div.is-selected .tile-img').addClass('border8');
        $(this).find('div.is-selected .tile-img').addClass('mouseOver');
    });

    /*OnClick function for tile*/
    $('.tile').click(function() {
        openURL(this, '#popup');
    });

    /*OnClick function for footer*/
    $('.footer-popup').click(function(e) {
        e.preventDefault();
        openURL(this, '#tocBumper');
    });

    /*Opening the popup on click of the tile component*/
    $('.recruiter-tile > a').click(function(e) {
        e.preventDefault();
        openURL(this, '#popup');
    });

    $('.recruiter-tile > a').keydown(function() {
        $(this).addClass('focused');
    });

    $('.social-gradient > a > img').keydown(function() {
        $(this).parent('a').addClass('focused');
    });

    $('.media > a > img').keydown(function() {
        $(this).parent('a').addClass('focused');
    });



    /*setting the Target of the URL to open in the new window or self or in a popup*/
    function openURL(tile, popupID) {
        var videoURL = $(tile).data('video-url');
        var popupLink = $(tile).data('src');
        var videoSource = $(tile).data('source');
        var linkToOpen;
        if (!videoURL) {
            linkToOpen = popupLink;
        } else {
            linkToOpen = videoURL;
            popupID = "#videoPopup";
        }
        var dataLinkTarget = $(tile).data('link-target');
        if (dataLinkTarget === 'new') {
            window.open(linkToOpen, '_blank');
        } else if (dataLinkTarget === 'same') {
            window.open(linkToOpen, '_self');
        } else {
            if (linkToOpen !== "" && linkToOpen !== undefined) {
                if (videoSource === "standard") {
                    $(popupID).find('iframe').remove();
                    $('<video controls class="videoHeight" autoplay><source src="' + videoURL + '" type="video/mp4"></video>').appendTo('#videoPopup .modal-body');
                    $("#videoPopup video").load();
                    _satellite.track("videoStart");
                } else if (videoSource === "youtube") {
                    $(popupID).find('iframe').remove();
                    $('<iframe data-video-url="youtube" allowfullscreen width="420" height="400" src="' + videoURL + '"></iframe>').appendTo('#videoPopup .modal-body');
                } else {
                    $(popupID).find('iframe').remove();
                    $('<iframe allowfullscreen  width="1024" height="768" src="' + linkToOpen + '"></iframe>').appendTo('.modal-body');
                }
                $(popupID).modal('show');
            }
        }
    }

    function hideModal() {
        $('body').removeClass('modal-open');
        $('.main-loader').remove();
    }

    var width = $(window).width();

    if (width <= 668) {
        TweenMax.to('.main-loader', 0.4, {
            delay: 1,
            y: 200,
            opacity: 0,
            onComplete: hideModal
        });
        $('body').addClass('modal-open');
        $('.modal-close-btn').click(function() {
            event.preventDefault();
            var main = $('.tile-section-drawer').html();
            if (main.length !== 0) {
                $('.tile-section-drawer').contents(':not(.modal-close-btn)').empty();
                $('body').removeClass('modal-open');
                $('body').css('overflow', 'hidden');
                $('body').css('position', 'static');

            }
            $('a.promotional-content-btn').show();
        });
    } else if (width > 737) {

        $('.tile-video-wrapper').contents().unwrap();
        $('.tile-generic-wrapper').contents().unwrap();
        $('.tile-music-wrapper').contents().unwrap();

        /*tile mouseover and mouseout function*/
        $('.tile').on({
            mouseover: function() {
                var tileID = $(this).attr("id");
                var lastCharacter = tileID.substr(tileID.length - 1);
                var billboardID = '#billboard_content' + lastCharacter;
                var num = $(".billboard > div").length;
                for (var i = 1; i < num + 1; i++) {
                    $('#billboard_content' + i).css("display", "none");
                    if ($('#billboard_content' + i).find('video').length > 0) {
                        $('#billboard_content' + i).find('video').get(0).pause();
                        $('#billboard_content' + i).find('video').get(0).currentTime = 0;
                    }
                }
                $(billboardID).css("display", "block");
                if ($(billboardID).find('video').length > 0) {
                    $(billboardID).find('video').get(0).pause();
                    setTimeout(function() {
                        if ($(billboardID).find('video').get(0).paused) {
                            $(billboardID).find('video').get(0).play();
                        }
                    }, 150);

                }
                $(this).find('.tile-img').addClass('mouseOver');
                $(this).parent().siblings('div.is-selected').find('.tile-img').removeClass('border8');
                $(this).parent().siblings('div.is-selected').find('.tile-img').addClass('border0');
                $(this).parent().siblings('div.is-selected').find('.tile-img').removeClass('mouseOver');
            },
            mouseout: function() {
                var tileID = $(this).attr("id");
                var lastCharacter = tileID.substr(tileID.length - 1);
                var billboardID = '#billboard_content' + lastCharacter;
                $(this).find('img').removeClass('mouseOver');
                if ($(billboardID).find('video').length > 0) {
                    $(billboardID).find('video').get(0).currentTime = 0;
                    $(billboardID).find('video').get(0).pause();
                    setTimeout(function() {
                        if ($(billboardID).find('video').get(0).paused) {
                            $(billboardID).find('video').get(0).play();
                        }
                    }, 150);
                }

                $(this).find('.tile-img').removeClass('mouseOver');
                $(this).parent().siblings('div.is-selected').find('div.tile').mouseover();
                $(this).parent().siblings('div.is-selected').find('.tile-img').removeClass('border0');
                $(this).parent().siblings('div.is-selected').find('.tile-img').addClass('border8');
                $(this).parent().siblings('div.is-selected').find('.tile-img').addClass('mouseOver');

            }
        });

    }
});

/*Function to Initialize flickity slider*/
function initFlickitySliders() {
    if (winWidth > 767) {
        $gallery.flickity({
            initialIndex: 2,
            accessibility: true,
            cellAlign: 'center',
            wrapAround: true
        });

    } else {
        $gallery.flickity({
            initialIndex: 0,
            accessibility: true,
            cellAlign: 'center',
            wrapAround: true
        });
    }
}

/*Function for Slider play pause button */
function playSlider(clickElem) {
    $(clickElem).on('click', function() {
        if (!$(this).hasClass('paused')) {
            $(this).attr('aria-label', 'Play');
            $(".socialTile-twitter").liveTile('pause'); /*For Pause the twitter live tile on click*/
            $(".socialTile-instagram").liveTile('pause'); /*For Pause the twitter live tile on click*/
        } else {
            $(this).attr('aria-label', 'Pause');
            $(".socialTile-twitter").trigger('focusout');
            $(".socialTile-instagram").trigger('focusout');
            $(".socialTile-twitter").liveTile('play'); /*For Pause the twitter live tile on click*/
            $(".socialTile-instagram").liveTile('play'); /*For Pause the twitter live tile on click*/
        }
        $(this).toggleClass('paused');
    });
}

/*Function to create slider play pause button*/
function createSliderButtons(sliderContainer) {
    var htmlContent = '';
    htmlContent += '<div class="cont-play-pause-buttons secondaryControls" >';
    htmlContent += '<button type="button" class="flex-play" aria-label="Pause Button"><i  class="fa fa-pause"></i></button>';
    htmlContent += '</div>';

    $(sliderContainer).prepend(htmlContent);
    playSlider(sliderContainer + ' .flex-play'); /*create click events*/
}

/*Functions to align video model position to center*/
function modalPositionCenter(videoType) {
    var winHeight = window.innerHeight;
    var videoModalHeight = $('#videoPopup .modal-dialog ' + videoType).height();
    var modalMarginTop = (winHeight - videoModalHeight) / 2;
    $('#videoPopup .modal-dialog').css({
        "padding": "0",
        "margin-top": modalMarginTop
    });
}

/*Function to set slider height in landscape mode*/
function checkOrientation() {
    var winWidth = window.innerWidth;
    if (winWidth < 768) {
        $('.main-gallery .flickity-viewport').css('min-height', winWidth);
        $('.wifi-survey').css('height', winWidth);
        $('.wifi-survey .grid-item .special-tile-mobile').css('height', winWidth);
        $('.instagram-stream').css('height', winWidth);
        $('.instagram-stream .socialTile-instagram ').css('height', winWidth);
        $('.twitter-stream').css('height', winWidth);
        $('.twitter-stream .socialTile-twitter').css('height', winWidth);
    } else {
        $('.main-gallery .flickity-viewport').removeAttr('style');
        $('.wifi-survey').removeAttr('style');
        $('.wifi-survey .grid-item .special-tile-mobile').removeAttr('style');
        $('.instagram-stream').removeAttr('style');
        $('.instagram-stream .socialTile-instagram ').removeAttr('style');
        $('.twitter-stream').removeAttr('style');
        $('.twitter-stream .socialTile-twitter').removeAttr('style');
    }
}

$(window).on('load', function() {
    checkOrientation();
});

$(window).on('resize', function() {
    checkOrientation();
    $gallery.flickity('destroy'); /*Destroy flickity slider*/
    initFlickitySliders(); /*Initialize flickity slider*/
});