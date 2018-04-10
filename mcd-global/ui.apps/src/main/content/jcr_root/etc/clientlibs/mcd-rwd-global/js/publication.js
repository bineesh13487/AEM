//Image and div width redefine for left and right alignment

/*
var PublicationImageWidth = $('.publication .EverythingComponenttextandImage .image-wrapper-box > img').width();
$('.publication .EverythingComponenttextandImage .image-wrapper-boximage-wrapper-box').css("width", PublicationImageWidth);
$('.publication .EverythingComponenttextandImage').css("width", PublicationImageWidth);


var Owidth = $('.pubImage img').prop('naturalWidth');
$('.publicationImageleft > div').width(Owidth);
$('.publicationImageright > div').width(Owidth);
$('.publicationImagecenter').width(Owidth).css("margin", "auto");*/

//add event-comp class on dom ready
$('.event-text').parents('#everythingcontainer').addClass('event-comp');
$('.event-text').parents('.columncontrol').addClass('event-component');
$('.groupTitle').parents('.columncontrol').addClass('event-component');


