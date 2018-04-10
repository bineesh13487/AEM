function processTime(time) {
    var timeDetails = time.split(' ');
    var timeValue = timeDetails[0];
    var timeOfDay = timeDetails[1];
    var timeFractions = timeValue.split(':');
    var hours = parseInt(timeFractions[0]);
    var minutes = parseInt(timeFractions[1]);
    if( timeOfDay == 'PM'){
        hours += 12;
    }
    var obj = {
        'hour': hours,
        'mins': minutes
    };
    return obj;
}

/* Functions used to compare two time objects*/
function compareTime(obj1, obj2) {
    if((obj1.hour > obj2.hour) || (obj1.hour == obj2.hour && obj1.mins > obj2.mins)) {
        return 1;
    } else if (obj1.hour == obj2.hour && obj1.mins == obj2.mins) {
        return 0;
    }
    return -1;
}

function matchesTimeInterval(start, end) {
    var currentTime = new Date(Date.now());
    var currentObj = {
        'hour': currentTime.getHours(),
        'mins': currentTime.getMinutes()
    };

    var startObj = processTime(start);
    var endObj = processTime(end);
    var startResult = compareTime(startObj, currentObj);

    if(startResult == -1) {
        var endResult = compareTime(endObj, currentObj);
        if(endResult == 1) {
            return true;
        }
    }

    return false;
}