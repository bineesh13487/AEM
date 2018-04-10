/*
 * File Name: twitter-stream.js
 * Author: mcd-wifi
 * Description: This file is used for twitter live tile component.
*/

/*Random Tile View for the Twitter Component*/
function getRandomOptions() {
	var doIt = Math.floor(Math.random() * 1001) % 2 === 0;
	return {
		index: "next",
		delay: 7000,
		animationDirection: doIt ? 'forward' : 'backward',
		direction: doIt ? 'vertical' : 'horizontal'
	};
}	
	
/*Function to get mentions(@) for the particular tweet*/
function getMentions(mentions, newString){
	var finalString ="";
	if(mentions){
		var allmentions = mentions.trim().split(" ");
		for(var i=0;i<allmentions.length;i++){
			if(finalString){
				finalString = finalString.replace("@"+allmentions[i], '<a target="_blank" href="https://twitter.com/'+allmentions[i]+'">@'+allmentions[i]+'</a>');
			}
			else{
				finalString = newString.replace("@"+allmentions[i], '<a target="_blank" href="https://twitter.com/'+allmentions[i]+'">@'+allmentions[i]+'</a>');
			}
			
		}
	}
	else{
		finalString = newString;
	}
	return finalString;
}
	
/*Function to get hashtag string(#) for the particular tweet*/
function hasTagsString(hastags, newString){
	var finalString ="";
	if(hastags){
		var allHasTags = hastags.trim().split(" ");
		for(var i=0;i<allHasTags.length;i++){
			if(finalString){
				finalString = finalString.replace("#"+allHasTags[i]+"", '<a target="_blank" href="https://twitter.com/hashtag/'+allHasTags[i]+'?src=hash">#'+allHasTags[i]+'</a>');
			}
			else{
				finalString = newString.replace("#"+allHasTags[i]+"", '<a target="_blank" href="https://twitter.com/hashtag/'+allHasTags[i]+'?src=hash">#'+allHasTags[i]+'</a>');
			}
			
		}
	}
	else{
		finalString = newString;
	}
	return finalString;
}

/* function to encode the html*/
function isEncHTML(str) {
	var flag;
	if(str.search(/&amp;/g) !== -1 || str.search(/&lt;/g) !== -1 || str.search(/&gt;/g) !== -1){
		flag = true;
	}
	else{
		flag = false;
	}
	return flag;
}

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

/*Copy the HTML of clicked div in the POPUP for the twitter*/
function copyHtmlTwitter(popupHtml) {
	$("#socialpopupTwitter .modal-body").find('iframe').remove();
    $("#socialpopupTwitter .modal-header .tweetHeader").html($(popupHtml).parent().siblings('.tweetContent').find(".tweetHeader").html());        
    $("#socialpopupTwitter .modal-body .content").html($(popupHtml).parent().siblings('.tweetContent').find(".urlText").html() + $(popupHtml).html());
    $("#socialpopupTwitter .modal-content .modal-header .mainLogo").html($(popupHtml).parent().siblings('.tweetContent').find('.tweetAvatar').html());
    $("#socialpopupTwitter .modal-footer").html($(popupHtml).parent().siblings('.tweetContent').find('.twitterIcons').html());
    $("#socialpopupTwitter.modal video").attr("controls", "controls");
}

/*Function to be performed when modal popup is open*/
function openPopupTwitter(popupClass) {
	
	$(".socialTile-twitter").liveTile('pause');/*pause twitter live tile*/
	
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
	
	/*default image for twitter*/
	$('.media').find('img').each(function () {
		if (this.src.length === 0) {
			this.src='https://pbs.twimg.com/media/CggQGOFUkAEc2h0.jpg';
		}
	});
	
	/*Live Tile for Twitter*/
	$(".socialTile-twitter").liveTile({
		animationComplete: function() {
			$(this).liveTile("goto", getRandomOptions());
		}, pauseOnHover: true
	}).liveTile("goto", getRandomOptions());

	/*Create Link for the URL present in the twitter tweet*/
	$('.urlTextData').each(function() {
		var text = $(this).html();
		var hastags = $(this).siblings('.hastags').html().trim();
		var mentions = $(this).siblings('.mentions').html().trim();
		var newString = decHTMLifEnc(urlify(text));
		var finalString = hasTagsString(hastags, newString);
		finalString = getMentions(mentions, finalString);
		$(this).html(finalString);        
	});
	
	/*Function to open popup for the twitter*/
	$("#socialpopupTwitter.modal").on('show.bs.modal', function() {
          $(".socialTile-twitter").liveTile('pause');
		  openPopupTwitter('#socialpopupTwitter');
	});
	
	$("#socialpopupTwitter.modal").on('hidden.bs.modal', function(){
        if(!$('.cont-play-pause-buttons button').hasClass('paused')){
    		$(".socialTile-twitter").liveTile('play');
    	}
        if ($('.twitterModal').find('video').length > 0) {
            $(".twitterModal").find('video').get(0).currentTime = 0;
            $(".twitterModal").find('video').get(0).pause();
        }
        /*$('.wifi-tile').focusin();*/
	});


	/*on tab press twitter event fire*/
    $(".socialTile-twitter").keydown(function (e) {
        if (e.which === 9){
            $(".socialTile-twitter").focusin(function(){
                $(this).find('.slide').css('display','none');
                $(this).find('.slide.active').css('display','block');
                $(".socialTile-twitter").liveTile('pause');
            }).focusout(function(){
                $(this).find('.slide').css('display','block');
                if(!$('.cont-play-pause-buttons button').hasClass('paused')){
                    $(".socialTile-twitter").liveTile('play');
                }
            });
          	$("#socialpopupTwitter.modal").focusin( function() {
          		$(".socialTile-twitter").liveTile('pause');
			});
        }
	});
	
	/*Function for twitter to copy the popup html*/
	$('.open-popup-twitter').on('click', function() {
		copyHtmlTwitter(this);
    }); 
});		