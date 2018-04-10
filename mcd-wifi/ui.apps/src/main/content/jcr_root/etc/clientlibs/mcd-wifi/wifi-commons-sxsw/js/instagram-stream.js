/*
 * File Name: instagram-stream.js
 * Author: mcd-wifi
 * Description: This file is used for instagram live tile component.
*/

/*Random Tile View for the Instagram Component*/
function getRandomOptionsTwo() {
	var doIt = Math.floor(Math.random() * 1003 * 5) % 2 === 0;
	return {
		index: "next",
		delay: 5000,
		animationDirection: doIt ? 'forward' : 'backward',
		direction: doIt ? 'vertical' : 'horizontal'
	};
}

/*Function to get mentions(@) for the particular instagram*/
function getMentionsInstagram(mentions, newString){
	var finalString ="";
	if(mentions){
		var allmentions = mentions.trim().split(" ");
		for(var i=0;i<allmentions.length;i++){
			if(finalString){
				finalString = finalString.replace("@"+allmentions[i], '<a target="_blank" href="https://www.instagram.com/'+allmentions[i]+'">@'+allmentions[i]+'</a>');
			}
			else{
				finalString = newString.replace("@"+allmentions[i], '<a target="_blank" href="https://www.instagram.com/'+allmentions[i]+'">@'+allmentions[i]+'</a>');
			}
			
		}
	}
	else{
		finalString = newString;
	}
	return finalString;
}
	
/*Function to get hashtag string(#) for the particular instagram*/
function hasTagsStringInstagram(hastags, newString){
	var finalString ="";
	if(hastags){
		var allHasTags = hastags.trim().split(" ");
		for(var i=0;i<allHasTags.length;i++){
			if(finalString){
				finalString = finalString.replace("#"+allHasTags[i]+"", '<a target="_blank" href="https://www.instagram.com/explore/tags/'+allHasTags[i]+'">#'+allHasTags[i]+'</a>');
			}
			else{
				finalString = newString.replace("#"+allHasTags[i]+"", '<a target="_blank" href="https://www.instagram.com/explore/tags/'+allHasTags[i]+'">#'+allHasTags[i]+'</a>');
			}
			
		}
	}
	else{
		finalString = newString;
	}
	return finalString;
}

/*Copy the HTML of clicked div in the POPUP for the instagram*/
function copyHtmlInstagram(popupHtml) {
    $("#socialpopupInstagram .modal-body").find('iframe').remove();
    $("#socialpopupInstagram .modal-body .content").html($(popupHtml).siblings('.inst-urlText').html() + $(popupHtml).html());       
    $("#socialpopupInstagram .modal-content .popUpTitle").html($(popupHtml).parent().siblings('.grid-item-caption').html());
    $("#socialpopupInstagram .modal-body .tweetModal").html($(popupHtml).siblings('.overlay').find(".details .tweetText").html());
    $("#socialpopupInstagram .modal-footer").html($(popupHtml).siblings('.socialIcons ').html());
    $("#socialpopupInstagram.modal video").attr("controls", "controls");
}

/*Function to be performed when modal popup is open*/
function openPopupInstagram(popupClass) {
	
	$(".socialTile-instagram").liveTile('pause');/*pause instagram live tile*/
	
    $(popupClass).each(function() {
        var value = $(this).find('video source').attr('src');
        if (value === "" || value === null || value === undefined) {
            $(this).find('video').hide();
            $(this).find('.modal-body').find('img').show();
            $(this).siblings('.overlay').addClass('twitterOverlay');
            $(this).siblings('.socialParent').addClass('gradientBG');
        } else {
            $(this).find('video').show();
            $(this).find('video').get(0).play();
            $(this).find('.modal-body').find('img').hide();
            _satellite.track("videoStart");
        }
		/*default image for the modal popup*/
        if (!$(this).find('img').attr('src')) {
            $(this).find('img').attr('src', "https://pbs.twimg.com/media/CggQGOFUkAEc2h0.jpg");
        }

    });
}

$(window).on('load', function() {
	/*Live Tile for Instagram*/
    $(".socialTile-instagram").liveTile({
        animationComplete: function() {
            $(this).liveTile("goto", getRandomOptionsTwo());
        }, pauseOnHover: true
    }).liveTile("goto", getRandomOptionsTwo());
	
	/*Function to open popup for the instagram*/
	$("#socialpopupInstagram.modal").on('show.bs.modal', function() {
		openPopupInstagram('#socialpopupInstagram');
		$('.inst-urlTextData').removeClass('hidden');
    });
	
	$("#socialpopupInstagram.modal").on('hidden.bs.modal', function(){
		if(!$('.cont-play-pause-buttons button').hasClass('paused')){
			$(".socialTile-instagram").liveTile('play'); 
    	}
		if ($('.instagramModal').find('video').length > 0) {
			 $(".instagramModal").find('video').get(0).currentTime = 0;
			 $(".instagramModal").find('video').get(0).pause();
		 }
		/*$(".socialTile-instagram").focus();*/
	});
	
	$(".socialTile-instagram").focusin(function(){
    	$(this).find('.slide').css('display','none');
    	$(this).find('.slide.active').css('display','block');
    	$(".socialTile-instagram").liveTile('pause');
    }).focusout(function(){
    	$(this).find('.slide').css('display','block');
    	if(!$('.cont-play-pause-buttons button').hasClass('paused')){
        	$(".socialTile-instagram").liveTile('play');
    	}
    });
	
	/*Function for instagram to copy the popup html*/
	$('.open-popup-instagram').on('click', function() {
        copyHtmlInstagram(this);
    });
	
	/* function to decode the html*/
	function decHTMLifEnc(str){
		if(isEncHTML(str)){
			return str.replace(/&amp;/g, '&').replace(/&lt;/g, '<').replace(/&gt;/g, '>');
		}
		return str;
	}

	/*function to check the string contains link or not*/
	function urlify(text) {
		var urlRegex = /(\b(https?|ftp|file):\/\/[-A-Z0-9+&@#\/%?=~_|!:,.;]*[-A-Z0-9+&@#\/%=~_|])/ig;
		return text.replace(urlRegex, function(url) {
			return '<a class="linkText" href="' + url + '">' + url + '</a>';
		});
	}
	
	/*Create Link for the URL present in the instagram images*/
	$('.inst-urlTextData').each(function() {
		var text = $(this).html();
		var hastags = $(this).siblings('.inst-hastags').html().trim();
		var mentions = $(this).siblings('.inst-mentions').html().trim();
		var newString = decHTMLifEnc(urlify(text));
		var finalString = hasTagsStringInstagram(hastags, newString);
		finalString = getMentionsInstagram(mentions, finalString);
		$(this).html(finalString);        
	});
	
	
	
});