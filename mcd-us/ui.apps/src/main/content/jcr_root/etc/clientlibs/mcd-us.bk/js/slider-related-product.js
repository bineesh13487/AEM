

//dynamic flex slider
function dynamicFlexSlider(obj, animType, controls, autoPlay,rtlflow,rtlDir, width, margin, ctrlWrapper, cstmDirNav, min, max){
    obj.css("direction","ltr")
    obj.flexslider({
        animation: animType,
        controlNav: controls,
        slideshow: autoPlay,
        itemWidth: width,
        itemMargin: margin,
        controlsContainer: ctrlWrapper,
        customDirectionNav: cstmDirNav,
        minItems: min,
        maxItems: max,
        rtl:rtlflow,
        start: function(slider) {
            slider.css("direction",rtlDir)
        },
    });
}

function getDeviceSize() {
    if (window.innerWidth < 768) {
        return 'mobile';
    } else {
        return 'desktop';
    }
}

var _sliderMap = {};

// destroys existing related flexsliders
function reinitializeSliders(viewSize) {
    var theContainer;
    $('.related-flexslider.no-init').each(function(index) {
        var key = $(this).data("id");
        var count = (viewSize == 'mobile') ? 3 : $(this).data("count");
        if (_sliderMap[key]) {
            theContainer = $(this).parent();
            $(this).remove();
            $(theContainer).html(_sliderMap[key]);
            var elem = $(theContainer).find('.related-flexslider');
  var rtlFlow=false,dir="ltr";

     if (window.location.href.indexOf("ar-") > -1){
        rtlFlow=true;
        dir="rtl";
    };
            dynamicFlexSlider($(elem), 'slide', false, false,rtlFlow,dir, 40, 0, $(elem).find('.custom-controls-container'),
                            $(elem).find('.custom-navigation a'), count, count);
        }
    });
}

function initNutritionRelatedFlexSliders(elem) {
    viewSize = getDeviceSize();
    var count = (viewSize == 'mobile') ? 3 : $(elem).data("count");
     var rtlFlow=false,dir="ltr";

     if (window.location.href.indexOf("ar-") > -1){
        rtlFlow=true;
        dir="rtl";
    };
    dynamicFlexSlider($(elem), 'slide', false, false,rtlFlow,dir, 40, 0, $(elem).find('.custom-controls-container'),
        $(elem).find('.custom-navigation a'), count, count);
    (function(elem){
        setTimeout(function(){
            _sliderMap[elem.data("id")] = '<div class="related-flexslider flexslider no-init" data-id="' + elem.data("id") + '" data-count="'
                + elem.data("count") + '">' + elem.html() + '</div>';
        }, 2000);
    })($(elem));
}

$(window).on('resize', function() {
    reinitializeSliders(getDeviceSize());
});
