(function($) {

    $(document).ready(function() {
        mcdApp.video();
    });

    var mql = window.matchMedia('only screen and (min-width: 320px) and (max-width: 767px)'),
    min600 = window.matchMedia('only screen and (min-device-width: 600px) and (max-device-height: 1024px)'),
    mcdApp = {
        video: function() {
            $(window).on('resize load', function() {
                $("div.video iframe:visible").each(function() {
                    $(this).height($(this).width() * (0.5625));
                });
                if(min600.matches && Modernizr.touch) {
                    $('.video-bg .videoBox video').hide();
                    $('.video-bg .videoBox .video-bg-poster').show();
                }
            });

            if (mql.matches) {
                $(".videoBox").css("display","block");
            } else {
                $(".fallback").css("display","none");
                $(".videoBox").css("display","block");

                $(window).on('resize load', function() {
                    $.each($('div.videoBox .thumbsWrapper'), function() {
                        /*if($(this).find('.thumb-box:not(.v-align)').length) {
                            var width = $(this).width();
                            $(this).find('.thumbs-video').width(width * 0.35);
                            $(this).find('.videoText').width(width* 0.54);
                            if ((width * 0.35) < 300) {
                                $(this).find('.thumbs-video .thumb-box').width('auto');
                            }
                        }*/
                    });
                });

                $("a.yt-thumb-vid").off().fancybox({
                    type: 'iframe',
                    fitToView: true,
                    width: 640,
                    height: 400,
                    overflow: 'hidden',
                    helpers: {
                        overlay: {
                            locked: false
                        }
                    }
                });

                $("a.thumb-vid").off().fancybox({
                    type: 'inline',
                    fitToView: true,
                	overflow: 'hidden',
                	closeBtn: true,
                	autoSize: true,
                    beforeShow : function () {
                    	$.fancybox.update();
                    	$.fancybox.reposition();
                	},
                	helpers: {
                   		overlay: {
                        	locked: false
                    	}
                	}
                });

                $("body").on('click', 'video', function() {
                    $this = $(this)[0];
                    if($this.paused) {
                        $this.play();
                    } else {
                        $this.pause();
                    }
                });

                $('.shareButton').on('click', function(){
                    $('.shareVia').slideToggle(500);
                });

                $('.shareVideo .shareVia').on('click', 'a.share', function() {
                    window.open($(this).data('href') + '=' + window.location.href);
                });
            }
        }
    };
}(jQuery));