/* global angular */
// dynamic flex slider
function dynamicFlexSlider(obj, animType, controls, autoPlay, rtlflow, rtlDir, width, margin, min, max) {
  obj.css('direction', 'ltr');
  obj.flexslider({
    animation: animType,
    controlNav: controls,
    slideshow: autoPlay,
    itemWidth: width,
    itemMargin: margin,
    minItems: min,
    maxItems: max,
    rtl: rtlflow,
    start(slider) {
      slider.css('direction', rtlDir);
    },
  });
}

function getGridSize() {
  return ((window.innerWidth || document.documentElement.clientWidth) < 500) ? 2 : ((window.innerWidth || document.documentElement.clientWidth) < 890) ? 2 : ((window.innerWidth || document.documentElement.clientWidth) < 1056) ? 3 : ((window.innerWidth || document.documentElement.clientWidth) < 1920) ? 5 : 5;
}

function initNutritionRelatedFlexSliders(elem) {
  const count = getGridSize();
  let rtlFlow = false;
  let dir = 'ltr';

  // sliderMap[elem.data('id')] = `${$(elem).parent().html()}`;
  if (window.location.href.indexOf('ar-') > -1) {
    rtlFlow = true;
    dir = 'rtl';
  }
  dynamicFlexSlider($(elem), 'slide', true, false, rtlFlow, dir, 40, 0, count, count);

  $('.related-flexslider ul.flex-direction-nav').css('display', 'none');

  $('.related-flex-container .flex-nav-cust .flex-prev').click(e => {
    e.preventDefault();
    $('.related-flexslider').flexslider('prev');
  });
  $('.related-flex-container .flex-nav-cust .flex-next').click(e => {
    e.preventDefault();
    $('.related-flexslider').flexslider('next');
  });
}

$(window).on('resize', () => {
  const gridSize = getGridSize();
  if ($('.related-flexslider').data('flexslider') !== undefined) {
    $('.related-flexslider').data('flexslider').vars.minItems = gridSize;
    $('.related-flexslider').data('flexslider').vars.maxItems = gridSize;
  }
  if (gridSize === 2) {
    $('.related-flex-container .flex-nav-prev').css('right', '59.5%');
    $('.related-flex-container .flex-nav-next').css('right', '34.2%');
  }
  else if (gridSize === 3) {
    $('.related-flex-container .flex-nav-prev').css('right', '57.5%');
    $('.related-flex-container .flex-nav-next').css('right', '36.2%');
  }
  else if (gridSize === 5) {
    $('.related-flex-container .flex-nav-prev').css('right', '52.5%');
    $('.related-flex-container .flex-nav-next').css('right', '43.2%');
  }
});

