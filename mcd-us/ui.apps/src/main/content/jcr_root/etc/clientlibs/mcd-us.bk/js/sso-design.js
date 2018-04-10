/*!
 * ProjectName
 * Project Title
 * http://www.reachcreative.com
 * @author Reach Creative
 * @version 1.0.5
 * Copyright 2016. MIT licensed.
 */
function toggleRadio(event) {

  var node = event.currentTarget; //console.log(node);
  var state = node.getAttribute('aria-checked').toLowerCase();

  if (event.type === 'click' || (event.type === 'keydown' && event.keyCode === 32) ) {

    if (state === 'false') {
      var tmpNodes = node.parentNode.parentNode.parentNode.getElementsByClassName('sso-radio-box-checked');
      for(var i=0; i<tmpNodes.length; i++){
        if(tmpNodes[i].getAttribute('aria-checked') == 'true'){
          tmpNodes[i].setAttribute('aria-checked', 'false');
          //tmpNodes[i].removeAttribute('checked');
        }
      }

      tmpNodes = node.parentNode.parentNode.parentNode.getElementsByClassName('sso-radio');
      for(var i=0; i<tmpNodes.length; i++){
        if(tmpNodes[i].getAttribute('aria-checked') == 'true'){
          tmpNodes[i].setAttribute('aria-checked', 'false');
          tmpNodes[i].removeAttribute('checked');
        }
      }

      node.setAttribute('aria-checked', 'true');
      node.setAttribute('checked', 'checked');
      node.parentNode.children[0].children[0].setAttribute('aria-checked', 'true');

    }

  }

}
function focusRadio(event) {
  event.currentTarget.className += ' focus';
}
function blurRadio(event) {
  event.currentTarget.className = event.currentTarget.className.replace(' focus','')
}


function toggleCollapsible(event) {
  var node = event.currentTarget;
  var state = node.getAttribute('aria-expanded').toLowerCase();

  if (event.type === 'click' || (event.type === 'keydown' && event.keyCode === 32) ) {

    if (state === 'true') {
      node.setAttribute('aria-expanded', 'false');
    }
    else {
      node.setAttribute('aria-expanded', 'true');
    }
  }
}

/*
* @function toggleCheckBox
*
* @desc Toogles the state of a checkbox and updates image indicating state based on aria-checked values
*
* @param   {Object}  event  -  Standard W3C event object
*
*/

function toggleCheckbox(event) {

  var node = event.currentTarget;
  var image = node.getElementsByTagName('img')[0];

  var state = node.getAttribute('aria-checked').toLowerCase();

  if (event.type === 'click' || (event.type === 'keydown' && event.keyCode === 32) ) {

    //console.log(node.parentNode.children[0].children[0]);
    //console.log(node.getAttribute("data-ios-checkbox"));

    if (state === 'true') {
      node.setAttribute('aria-checked', 'false');
      if(node.getAttribute("data-ios-checkbox") === 'true'){
        node.parentNode.children[1].setAttribute('aria-checked', 'false');
      }
      else{
        node.parentNode.children[0].children[0].setAttribute('aria-checked', 'false');
      }

    }
    else {
      node.setAttribute('aria-checked', 'true');
      if(node.getAttribute("data-ios-checkbox") === 'true'){
        node.parentNode.children[1].setAttribute('aria-checked', 'true');
      }
      else{
        node.parentNode.children[0].children[0].setAttribute('aria-checked', 'true');
      }

    }

    //event.preventDefault()
    //event.stopPropagation()
  }

}

/*
* @function focusCheckBox
*
* @desc Adds focus to the class name of the checkbox
*
* @param   {Object}  event  -  Standard W3C event object
*/

function focusCheckbox(event) {
  event.currentTarget.className += ' focus';
}

/*
* @function blurCheckBox
*
* @desc Adds focus to the class name of the checkbox
*
* @param   {Object}  event  -  Standard W3C event object
*/

function blurCheckbox(event) {
  event.currentTarget.className = event.currentTarget.className.replace(' focus','')
}


/*!
* ProjectName
* Project Title
* http://www.reachcreative.com
* @author Reach Creative
* @version 1.0.5
* Copyright 2016. MIT licensed.
*/
(function ($, window, document, undefined) {

  'use strict';

  $(function () {

    // expand tooltip on click
    function ssoTooltipInit(){
      $('.sso-trigger-tooltip').on('touch click', function(e){
        e.preventDefault();
        var targetElem = $(this).parent().find('.sso-tooltip-content');
        targetElem.toggleClass('sso-tooltip-active');
      });
    }

    // flexslider
    // function ssoFlexsliderInit(){
    //   $('.sso-slideShowSlider').each(function() {
    //     var $this = $(this),
    //     mydata = $this.data(),
    //     controlsContainer = $this.find(".flex-container");

    //     $this.flexslider({
    //       animation: mydata.animationtype,
    //       slideshow: mydata.autoplay,
    //       slideshowSpeed:  mydata.transitionspeed,
    //       direction: mydata.transitiontype,
    //       useCSS: true,
    //       controlsContainer: controlsContainer,
    //       pauseOnHover: true,

    //       start: function(slider) {
    //         TweenMax.from($('.left,.right'), 1, {
    //           opacity: '1',
    //           delay: 4
    //         });

    //         controlsContainer.find('.right').click(function(event) {
    //           slider.flexAnimate(slider.getTarget("next"));
    //         });

    //         controlsContainer.find('.left').click(function(event) {
    //           slider.flexAnimate(slider.getTarget("prev"));
    //         });
    //       }
    //     });
    //   });
    // }

    function ssoAdaCheckbox(){
      $('.sso-fac-checkbox .sso-checkbox-box-checked').on('keydown', function(e){
        if(e.keyCode === 32 || e.keyCode === 13){
          var tmp = $(this).parent().next('.sso-checkbox'); //console.log(tmp);
          tmp.trigger('click');
        }
      });

      $('.sso-ios .sso-ios-checkbox-toggle').on('keydown', function(e){
        if(e.keyCode === 32 || e.keyCode === 13){
          var tmp = $(this).parent().find('.sso-checkbox');
          tmp.trigger('click');
        }
      });
    }

    function ssoAdaRadio(){
      $('.sso-fac-radio .sso-radio-box-checked').on('keydown', function(e){
        if(e.keyCode === 32 || e.keyCode === 13){
          var tmp = $(this).parent().parent().find('.sso-radio-short-label');// console.log(tmp);
          tmp.trigger('click');
        }
      });
    }

    function ssoNoRing(){
      //remove outline for click buttons but keep it for keyboard for ada
      $('.sso-ios-checkbox-toggle').mousedown(function() {
        $(this).addClass('sso-no-ring');
      });
    }

    // function ssoNotificationClose(){
    //   $('.sso-notification-close').click(function() {
    //     $('.sso-notification').slideUp( 200 );
    //   });
    // }

    //ssoNotificationClose();
    ssoNoRing();
    ssoAdaCheckbox();
    ssoAdaRadio();
    ssoTooltipInit();
    //ssoFlexsliderInit();
  });

})(jQuery, window, document);