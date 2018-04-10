 use(function () {
 var imageMaps = {};
imageMaps.timestamp = (new Date()).getTime();
imageMaps.name= "map-" + imageMaps.timestamp ;
imageMaps.hash= "#" + imageMaps.name,
imageMaps.maps = [];
granite.resource.resolve(granite.resource.path + "/image").then(function (imageResource) {
  var info = imageResource.properties["imageMap"];
    if(info)
    {
        var mapInfoRegex = /\[([^(]*)\(([^)]*)\)([^|]*)\|([^|]*)\|([^\]]*)\]/g;
         while (match = mapInfoRegex.exec(info)) {
              obj=  {
                        type: match[1],
                        coords: match[2],
                        href: match[3].replace(/\"([^\"]*)\"/g, "$1"),
                        target: match[4].replace(/\"([^\"]*)\"/g, "$1"),
                        text: match[5].replace(/\"([^\"]*)\"/g, "$1")
                	}
              imageMaps.maps.push(obj);
            }
    }

});

        return imageMaps;
	});