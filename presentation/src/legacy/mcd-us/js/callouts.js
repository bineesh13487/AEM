/* Function used to shuffle array of callouts.*/
function shuffleArray(arr) {
	var length = arr.length;
    var j=length;
    while(j--){
        var temp = parseInt(Math.random()*length);
        var t = arr[j];
        arr[j] = arr[temp];
        arr[temp] = t;
    }
}

/* Functions used to compare two time objects*/
function compareTime(obj1, obj2) {
    if((obj1.hour>obj2.hour) || (obj1.hour==obj2.hour && obj1.mins>obj2.mins)) {
        return 1;
    } else if (obj1.hour==obj2.hour && obj1.mins==obj2.mins) {
        return 0;
    } else {
        return -1;
    }
}

/* Function used to retrieve the callout applicable to the current time window */
function getCurrentCallout(arr){
    //Create current time object
	var currentTime = new Date(Date.now());
    var currentObj = {
        'hour': currentTime.getHours(),
        'mins': currentTime.getMinutes()
    };
    var callout = arr[0];
    for(var i=0; i<arr.length; i++) {
        var obj = arr[i];
        //Compare start and end time present for the callout
        var startTime = obj.startTime.trim();
        if(startTime){
        	var startObj = processTime(startTime);
            var endObj = processTime(obj.endTime);
            var startResult = compareTime(startObj, currentObj);
            if(startResult == -1){
                var endResult = compareTime(endObj, currentObj);
                if(endResult == 1) {
                    callout = arr[i];
                    break;
                }
            }
        }
    }
    return callout;
}

/* Function used to convert time String (2:00 PM) to time object ({hour:14,mins:0}). */
function processTime(time) {
	var timeDetails = time.split(' ');
    var timeValue = timeDetails[0];
    var timeOfDay = timeDetails[1];
    var timeFractions = timeValue.split(':');
    var hours = parseInt(timeFractions[0]);
    var minutes = parseInt(timeFractions[1]);
    if(timeOfDay=='PM'){
        hours += 12;
    }
    var obj = {
        'hour': hours,
        'mins': minutes
    };
    return obj;
}


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


$(document).ready(function(){
    $(".promo-content .promo").each(function(i) {
        var data = eval('('+$(this).attr("data-attr")+')');
        var type = $(this).attr("data-type");
        var id = $(this).attr("id");
        //Shuffle the array
		if(type != 'static' && data!==undefined) {
			var element = null;
			if(type=='random') {
				shuffleArray(data);
				element = data[0];
			} else {
				element = getCurrentCallout(data);
			}

			if(element.imageAlt!=" "){
            $(this).find('img.hidden-xs').attr({
                "src": element.imagePath,
                "alt": element.imageAlt
            });
			$(this).find('img.visible-xs-block').attr({
                "src": element.mobileImagePath,
                "alt": element.imageAlt
            });
            }
            else{
            $(this).find('img.hidden-xs').attr({
                            "src": element.imagePath,
                            "alt": element.imageAlt

                        });
			$(this).find('img.visible-xs-block').attr({
                            "src": element.mobileImagePath,
                            "alt": element.imageAlt

                        });

            }

            $(this).find('img.img-top').attr({
                "src": element.mcdlogo,
                "title": logoAlt
            });

			$(this).find('p.top').text(element.eyetitle).css("text-align", element.titlePosition).css("color", "#"+element.titleColor);
			$(this).find('p.middle').text(element.title).css("text-align", element.titlePosition).css("color", "#"+element.titleColor);
			$(this).find('p.bottom').text(element.description).css("text-align", element.titlePosition).css("color", "#"+element.titleColor);

			$(this).find('a').attr({
			    "href": element.link.href,
			    "target": element.link.target,
			    "class": "zoom-anim btn btn-red btn--xsm " + element.link.linkClass,
			    "title": element.ctaAlt
			});
			$(this).find('a').text(element.link.text).css("float", element.ctaPosition);
		}
    });

	initZoomAnim('.zoom-anim', '.zoom-anim-target', 'bglarge', '.zoom-anim-parent');
});