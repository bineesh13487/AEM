
(function ($, $document) {
    "use strict";

    $document.on("dialog-ready", function() {
        console.log("here");
        if ($('input[type="checkbox"][name="./enablenewssearch"]').length > 0) {
            searchNewsfields()
        }

        //Wifi Tile component, hide daypart field on default configuration, as its required only within multifield.
        $('select[name="./daypartType"]').closest('.foundation-field-editable').remove();

        //Feature callout component remove time fields for Static tabs
        $('.feature-static coral-datepicker').closest('.foundation-field-editable').remove();

        //Product Callout Handler Component hide/show call out tabs
        if ($('.product-callout-number').length > 0) {
            showHideTab($('.product-callout-number'));
        }

        //Feature Callout Handler Component hide/show call out tabs
        if ($('.feature-callout-number').length > 0 && $('.feature-callout-type').length > 0) {
            showHideFeatureTabs();
        }

        //WIFI Main Slider Component hide/show fields
        if ($('.wifi_slider_background_element').length > 0) {
            showHideWifiSliderFields();
        }


        /*$(document).on("selected", ".daypartType", function(e) {
            var $select = $(this).data('select');
            var tileList = [];
            var flag = true;
            $('.js-coral-Multicompositefield-input').each(function() {
                var daypartType = $(this).find('.daypartType').data('select');
                var value = daypartType.getValue();
                if (tileList.includes(value)) {
                    flag = false;
                    var elem = $select.$element.find('select');
                    elem.setCustomValidity("Daypart should be unique.");
                    elem.checkValidity();
                    elem.updateErrorUI();
                    break;
                }
                else {
                    tileList.push(value);
                }
            });
            if (flag) {
                $input.setCustomValidity(null);
                $input.checkValidity();
                $input.validationMessage();
                $input.updateErrorUI();
            }
        });*/




    });

    $(document).on("foundation-contentloaded", function (e) {

        personalInfoHideFields();
       perosonalInforemoveLi();

        $.validator.register({
            selector: ".tile-imagePath input",
            validate: function(el) {
                var value = el.val();
                if (value && !/\/content\/(.*)?(?:jpe?g|gif|png)$/g.test(value)) {
                    return "Please provide an image with a valid extension i.e : jpeg|gif|png";
                }
            }
        });
        $.validator.register({
            selector: ".col-cntrl",
            validate: function(el) {
                var count = 0;
                $(el[0]).find('.coral-Multifield-list .col-cntrl').each(function() {
                    count += parseInt("" != $(this).data("select").getValue() ? $(this).data("select").getValue() : 0);
                });
                if (count > 12) {
                    return "Cannot add columns with width more than 100%."
                }
                else {
                    el.setCustomValidity(null);
                    el.updateErrorUI();
                }
            }
        });
        $.validator.register({
            selector: ".col-cntrl.coral-Select select",
            validate: function(el) {
                var count = 0;
                var parentField = $('.col-cntrl');
                $('.coral-Multifield-list .col-cntrl').each(function() {
                    count += parseInt("" != $(this).data("select").getValue() ? $(this).data("select").getValue() : 0);
                });
                if (count > 12) {
                    parentField.setCustomValidity("Cannot add columns with width more than 100%.");
                }
                else {
                    parentField.setCustomValidity(null);
                }
                parentField.updateErrorUI();
            }
        });

        try{
            $.validator.register({
                selector: "button.cq-dialog-submit",
                validate: function(el) {
                    if($('coral-dialog-content').find('input[type="hidden"][name="./sling:resourceType"]').val()=="mcd-rwd-global/components/content/publication"){
                         debugger;
                         var imageLink = $('.publicationadvancedimage').find('input[type="text"][name="./imageLink"]').val();
                         var thumb = $('.publicationadvancedimage').find('input[type="checkbox"][name="./createThumbnail"]').prop('checked');
                         var uploadLink = $('.publicationadvancedimage .cq-FileUpload-thumbnail-img').find('p');
                         if(thumb == ''){
                            thumb = false;
                         }
                         if($('.publicationimagetab .cq-FileUpload-thumbnail-img').find('p').length > 0 && $('.publicationadvancedimage').find('input[type="text"][name="./externalImagePath"]').val().length > 0){
                             var content = "Please provide Image either from Image Tab or using HTTP Path of Image path.";
                             alert(content, 'Error Dialog');
                             return " ";
                         }
                         else if((thumb && uploadLink.length>0 && imageLink.length>0) || (thumb && uploadLink.length>0) || (thumb && imageLink.length>0) || (uploadLink.length>0 && imageLink.length>0)){
                            var content = "Please provide Only Thumbnail Or Image Link Or Upload a file in Advanced Image Tab.";
                            alert(content, 'Error Dialog');
                            return " ";
                         }
                    }
                }
            });
        }catch(err){
            console.log("publication component error block");
        }finally{
            if($('.coral-Dialog-wrapper').find('.coral3-Tooltip--error').hasClass('is-open')){
                $('.coral-Dialog-wrapper').find('.coral3-Tooltip--error').css('display','none');
            }
        }
    });

    $document.on("selected", ".product-callout-number", function(e) {
        showHideTab($(this));
    });

    $document.on("selected", ".feature-callout-number, .feature-callout-type", function(e) {
        showHideFeatureTabs();
                    if ($(this).hasClass('global')) {
                        var type = $('.feature-callout-type').data("select").getValue();
                        if (type === "random") {
                            $('coral-datepicker').closest('.foundation-field-editable').hide();
                        }
                        else if (type === "time") {
                            $('coral-datepicker').closest('.foundation-field-editable').show();
                        }
                    }
    });

    $document.on("selected", ".wifi_slider_background_element", function(e) {
            var value =  $(this).data('select').getValue();
            if(value == "image"){
                $('.wifi_slider_background_image').parent().show();
                 $('.wifi_slider_background_image_alt_text').parent().show();
                 $('.wifi_slider_background_video').parent().hide();
                 $('.wifi_slider_background_video_type').parent().hide();
                 $('.wifi_slider_background_light_box').parent().hide();
                 $('.wifi_slider_background_youtube').parent().hide();
            }
            else if(value == "video"){

                var videoType = $('.wifi_slider_background_video_type .coral-Select-button .coral-Select-button-text').text();
                if(videoType == "Standard"){
                    $('.wifi_slider_background_light_box').parent().show();
                    $('.wifi_slider_background_youtube').parent().hide();
                }
                $('.wifi_slider_background_image').parent().hide();
                $('.wifi_slider_background_image_alt_text').parent().hide();
                $('.wifi_slider_background_video').parent().show();
                $('.wifi_slider_background_video_type').parent().show();
            }
        });

         $document.on("selected", ".wifi_slider_background_video_type", function(e) {
               var videoType = $(this).data('select').getValue();
                if(videoType == "Standard"){
                     $('.wifi_slider_background_light_box').parent().show();
                     $('.wifi_slider_background_youtube').parent().hide();
                }
                else if(videoType == "Youtube"){
                    $('.wifi_slider_background_light_box').parent().hide();
                    $('.wifi_slider_background_youtube').parent().show();
                }
         });

    $document.on('click', '.feature-callout .js-coral-Multicompositefield-add', function(e) {
        e.preventDefault();
        e.stopPropagation();
        var widget = $(this).prev().find('.coral-FileUpload:last').data('fileUpload');
        if (!widget) {
            return;
        }
        new Granite.FileUploadField(widget);

        var type = $('.feature-callout-type').data("select").getValue();
        if (type === "random") {
            $(this).prev().find('coral-datepicker').closest('.foundation-field-editable').hide();
        }

    });

    $document.on('click','.personalinfoarabia ol.coral-TagList li .coral-MinimalButton',function(event){
        var removeField = $(event.currentTarget).parent().find('input[type="hidden"]').val();
        //console.log("remove field "+xyz);
        $('section[data-itemid="'+removeField+'"]').css('display', 'none');

    });

    //Initailizing image upload for image flyout component
     $document.on('click', '.image-details .js-coral-Multicompositefield-add', function(e) {
                e.preventDefault();
                e.stopPropagation();
                var widget = $(this).prev().find('.coral-FileUpload:last').data('fileUpload');
                if (!widget) {
                    return;
                }
                new Granite.FileUploadField(widget);
     });

    $document.on('click', '.wifi-main-slider .js-coral-Multicompositefield-add', function(e) {
            console.log("here");
            e.preventDefault();
            e.stopPropagation();
            showHideWifiSliderFields();

    });

    $document.on('click', '.image-gallery .js-coral-Multicompositefield-add', function(e) {
        e.preventDefault();
        e.stopPropagation();
        var len = $(this).prev().find('.coral-FileUpload').length;
        //alert("this is length of widget");
        for(var i=0; i < len; i++){
            var widget=null;
            //alert("entered into for loop "+i);
            if($($(this).prev().find('.coral-FileUpload')[i]).find(".cq-FileUpload-thumbnail").length > 0){
				widget=null;
            }else{
				widget = $($(this).prev().find('.coral-FileUpload')[i]).data('fileUpload');
            }
            //var widget2 = $(this).prev().find('.coral-FileUpload:last').data('fileUpload');
            //alert("var widget value "+widget);
            if (!widget && i>=len) {
                return;
            }

            if(widget)
            new Granite.FileUploadField(widget);
        //new Granite.FileUploadField(widget2);
        //  alert("after granite fileupload creation");
        }

    });

    $document.on('click', 'input[name="./enablenewssearch"]', function() {
        searchNewsfields();
    });

        $document.on('click', 'ul.coral-SelectList li', function() {
        //alert("clicked dropdown "+ $(this).attr('data-value'));
            perosonalInfoLiClick($(this).attr('data-value'));
    });

    $document.on("dialog-success", function() {
            window.location.reload();
    });

    function searchNewsfields() {
        var enablenewssearch = $('input[type="checkbox"][name="./enablenewssearch"]').prop('checked');
        if (enablenewssearch) {
            $('input[data-itemid="newssearchtext"]').closest('.foundation-field-editable').show();
            $('input[name="./newsurl"]').closest('.foundation-field-editable').show();
        }
        else {
            $('input[data-itemid="newssearchtext"]').closest('.foundation-field-editable').hide();
            $('input[name="./newsurl"]').closest('.foundation-field-editable').hide();
        }
    }

    function showHideTab(el) {
        var widget =  el.data("select");
        if (widget) {
            var value = widget.getValue();
            if (value == 4) {
                $(el.closest('.cq-dialog-content').find('.coral-TabPanel-tab')[4]).show();
                //$(el.closest('.cq-dialog-content').find('.coral-TabPanel-pane')[4]).attr('disabled', false)
            }
            else {
                $(el.closest('.cq-dialog-content').find('.coral-TabPanel-tab')[4]).hide();
                //$(el.closest('.cq-dialog-content').find('.coral-TabPanel-pane')[4]).attr('disabled', true)
            }
        }
    }

    function showHideFeatureTabs() {
        var numberElement = $('.feature-callout-number');
        var global = numberElement.hasClass('global');
        var typeElement = $('.feature-callout-type');
        var numberWidget = numberElement.data("select");
        var typeWidget = typeElement.data("select");
        var number = numberWidget.getValue();0
        var type = typeWidget.getValue();
        numberElement.closest('.cq-dialog-content').find('.coral-TabPanel-tab').slice(1).hide();
        var length = 0;
        var i = 0;
        switch (type) {
            case "static" : length = number == 3 ? 8 : 9;
                            i = 5;
                            break;
            case "random" : if (global) {
                                length = number == 3 ? 4 : 5;
                                i = 1;
                                break;
                            }
                            length = number == 3 ? 12 : 13;
                            i = 9
                            break;
            case "time"   : length = number == 3 ? 4 : 5;
                            i = 1;
                            break;
        }
        for (;i < length; i++) {
            $(numberElement.closest('.cq-dialog-content').find('.coral-TabPanel-tab')[i]).show();
        }
    }

    function showHideWifiSliderFields(){
             if ($('.wifi_slider_background_element').length > 0) {
                            var value =  $('.wifi_slider_background_element .coral-Select-select option').val();
                                    if(value == "image"){
                                       $('.wifi_slider_background_video').parent().hide();
                                       $('.wifi_slider_background_video_type').parent().hide();
                                       $('.wifi_slider_background_light_box').parent().hide();
                                       $('.wifi_slider_background_youtube').parent().hide();
                                     }
                        }
    }

    function personalInfoHideFields(){
        $('section[data-itemid="ctitle"]').css('display', 'none');
		$('section[data-itemid="mname"]').css('display', 'none');
        $('section[data-itemid="ccntrycode"]').css('display', 'none');
		$('section[data-itemid="careacode"]').css('display', 'none');
        $('section[data-itemid="cnumber"]').css('display', 'none');
		$('section[data-itemid="cgender"]').css('display', 'none');
        $('section[data-itemid="cnationality"]').css('display', 'none');
		$('section[data-itemid="cmarstatus"]').css('display', 'none');
        $('section[data-itemid="cprefcontact"]').css('display', 'none');
		$('section[data-itemid="chearus"]').css('display', 'none');
		$('section[data-itemid="cdob"]').css('display', 'none');
        var listItems = $(".personalinfoarabia ol.coral-TagList li");
        listItems.each(function(idx, li) {
    		var product = $(li).find('input').attr('value');
            $('section[data-itemid="'+product+'"]').css('display', 'block');
		});
    }

    function perosonalInfoLiClick(dataitem){
		$('section[data-itemid="'+dataitem+'"]').css('display', 'block');
    }

        function perosonalInforemoveLi(){
    		$('.personalinfoarabia ol.coral-TagList li button').on('click',function(event){
                var liValue = $(event.currentTarget).parent().find('input[type="hidden"]').val();
                $('section[data-itemid="'+liValue+'"]').css('display', 'none');
        	});
        }

       function renderDialog(id, title, contentHtml, footerHtml, variant, cmd) {
            var dialog = new Coral.Dialog().set({
                id: id,
                variant: variant,
                header: {
                    innerHTML: title
                },
                content: {
                    innerHTML: contentHtml
                },
                footer: {
                    innerHTML: footerHtml
                }
            });
            document.body.appendChild(dialog);
            return dialog;
        }

        function showDialog(id) {
            var dialog = document.querySelector(id);
            dialog.show();
        }

        function alert(content, type) {
            var footer = '<button is="coral-button" variant="primary" coral-close>Ok</button>'
            $('#errorDialog').remove();
            renderDialog('errorDialog', type, content,
                footer, 'error', '');
            showDialog('#errorDialog');
        }

})($, $(document));