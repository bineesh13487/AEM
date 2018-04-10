app.controller('categoryController', ['$scope', '$http','getCoop', function($scope, $http,getCoop) {
      var categoryId;
      var coop;
      $scope.init = function(id) {
          var iconicDesktop = $(".iconicDesktopFolder").data("folder-id");
          var showLiveData=$(".showLiveData").data("showlive-value");
		  var largeRendition = $(".iconicLargeRendition").data("folder-id");
		  var mediumRendition = $(".iconicMediumRendition").data("folder-id");
		  var smallRendition = $(".iconicSmallRendition").data("folder-id");
		  var xsmallRendition = $(".iconicXsmallRendition").data("folder-id");
		  var serverUrl = $(".iconicDynamicServerUrl").data("folder-id");
		  var productButtonText = $(".iconicbuttonText").data("folder-id");
		  if(angular.isUndefined(largeRendition)){
			  largeRendition='';
		  }
		  if(angular.isUndefined(mediumRendition)){
			  mediumRendition='';
		  }
		  if(angular.isUndefined(smallRendition)){
			  smallRendition='';
		  }
		  if(angular.isUndefined(xsmallRendition)){
			  xsmallRendition='';
		  }
		  if(angular.isUndefined(serverUrl)){
			  serverUrl='';
		  }
		  if(angular.isUndefined(productButtonText)){
			  productButtonText='';
		  }
          categoryId = id;
          //coop value to be obtained from cookies
		  coop=getCoop.getCoopId();
          if (categoryId) {
          if(coop){
          // $http.get("/services/mcd/categoryDetails.json?country=" + config.get("country") + "&language=" + config.get("lang") + "&categoryID=" + categoryId + "&showLiveData="+showLiveData+"&cops=" + coop + "&showNationalCoop=false")
          //order for selectors are country,language,categoryID,showLiveData,showNationalCoop,coop
          var selectorString="."+config.get("country")+"."+config.get("lang")+"."+categoryId+"."+showLiveData+"."+"false"+"."+coop+".json";
          $http.get("/services/mcd/categoryDetails"+selectorString)
                  .then(function(response) {
                      if(response.data.category){
                      var itemList = response.data.category.items;
                      if(itemList){
                      $.each(itemList, function(index, value) {
                      if(value.itemVisibility){
                         var productArray=[];
                         var isIconicProduct=false;
                          var promoCount=$('div[id^=promo]').length;
                           $(".iconic_count_div>div>div>div").each(function() {
								 productArray.push($(this).attr("id"));
                            });
                          productArray =  productArray.slice(0, [productArray.length] - promoCount);
                            console.log(productArray);
                           for(var i=0;i<productArray.length-1;i++){
                                if (parseInt(value.displayOrder,10) < parseInt(productArray[0],10)){
                                     var str = $scope.makeIconicDivStr(value, iconicDesktop,largeRendition,mediumRendition,smallRendition,xsmallRendition,serverUrl,productButtonText);
                                     $(str).insertBefore($("div#" + productArray[i]));
                                     break;
                                }
                                else if (parseInt(value.displayOrder,10) < parseInt(productArray[i],10)) {
                                    var str = $scope.makeIconicDivStr(value, iconicDesktop,largeRendition,mediumRendition,smallRendition,xsmallRendition,serverUrl,productButtonText);
                                     $(str).insertBefore($("div#" + productArray[i]));
                                     break;
                                   
                                }
                                else if(parseInt(value.displayOrder,10) > parseInt(productArray[productArray.length-1],10)){
                                //item id is gretaer tahn last elemt in cont items so inserting after that
                                     var str = $scope.makeIconicDivStr(value, iconicDesktop,largeRendition,mediumRendition,smallRendition,xsmallRendition,serverUrl,productButtonText);
                                     $(str).insertAfter($("div#" + productArray[productArray.length-1]));
                                     break;
                                   
                           }
                           }
                          }
                      });
                  }
                      }
                      $scope.displayPromo();


                  }).then(function() {
                      $(".loadingDiv").hide();
                      $(".cont-featured").show();
                      $scope.initZoomAnim('.zoom-anim', '.zoom-anim-target', 'bglarge', '.zoom-anim-parent');
                  });
                  }
                  else{

                   $scope.displayPromo();
                   $(".loadingDiv").hide();
                   $(".cont-featured").show();

                  }
          }
          $(function () {
              $(".iconic_count:nth-of-type(1) .making-iconic-header, .iconic_count:nth-of-type(2) .making-iconic-header").replaceWith(function () {
                  return "<h3>" + $(this).text() + "</h3>";
              });
      }); 
      };

      $scope.makeIconicDivStr = function(value, iconicDesktop,largeRendition,mediumRendition,smallRendition,xsmallRendition,serverUrl,productButtonText) {
          var desktopImage = iconicDesktop + '/' + value.imagePath;
		  var renditionLargeUrl = desktopImage;
		  var renditionMediumUrl= serverUrl+desktopImage+mediumRendition;
		  var renditionSmallUrl= serverUrl+desktopImage+smallRendition;
		  var renditionxsmallUrl = serverUrl+desktopImage+xsmallRendition;
		  var wrapperClass = 'no-class';
		  var structure = '';
		  
		  if (value.specializationText1 && value.specializationText2 && value.specializationText1 != '' && value.specializationText2 != '') {
				wrapperClass = "tag-limited-and-market";
				structure= '<div class="tag-text-wrapper-top"><table class="table"><tr>'+
					'</td><td class="tag-border-left"><div class="border"></div></td><td class="tag-text">'+value.specializationText1+
					'<td class="tag-border-right"><div class="border"></div></td></tr></table></div>'+
					'<div class="tag-text-wrapper-bottom"> <table class="table"><tr>'+
					'<td class="tag-border-left"><div class="border"></div></td><td class="tag-text">'+value.specializationText2+
					'</td><td class="tag-border-right"><div class="border"></div></td></tr></table></div>'
			} else if (value.specializationText1 && value.specializationText1 != '') {
				wrapperClass= "tag-limited";
				structure = '<div class="tag-text-wrapper-top"><table class="table"><tr><td class="tag-border-left"><div class="border"></div></td>'+
					'<td class="tag-text">'+value.specializationText1+'</td><td class="tag-border-right"><div class="border"></div></td></tr></table></div>'
			} else if (value.specializationText2 && value.specializationText2 != '') {
				wrapperClass = "tag-market";
				structure =  '<div class="tag-text-wrapper-bottom"> <table class="table"><tr><td class="tag-border-left"><div class="border"></div></td>'+
				  '<td class="tag-text">'+value.specializationText2+'</td><td class="tag-border-right"><div class="border"></div></td></tr></table></div>'
			} else {
				wrapperClass = "no-class";
				structure =  '';
			}
		  
          var str = "<div class='iconic_count col-sm-6 zoom-anim-parent "+ wrapperClass + "' id='" + value.displayOrder + "'>"+"<a href='" + value.path +
              "' class='zoom-anim'" + "title='"+productButtonText+" about " + value.marketingName + "'><picture >" +
			  "<source srcset='"+renditionxsmallUrl+"' media='(max-width: 432px)'>" +
			  "<source srcset='"+renditionSmallUrl+"' media='(max-width: 767px)'>" +
			  "<source srcset='"+renditionMediumUrl+"' media='(max-width: 1024px)'>" +
			  "<source srcset='"+renditionLargeUrl+"' media='(max-width: 1920px)'>" +
			  "<img class='zoom-anim-target img-responsive' srcset='"+desktopImage+"' aria-hidden='true' alt=' '/>"+
			  "</picture>"+
              "<div class='absolute-content' aria-hidden='true'> " +
              "<div class='absolute-content-wrapper'> " +
              "<div class='text-wrapper'> " +
              "<h4 class='making-iconic-header'>" + value.marketingName + "</h4> <div class='btn btn-red small'>"+productButtonText+"</div></div>" +
              "</div> " +
              "</div> " +structure+
              "</a></div> ";
          return str;
      }
      
	   
      $scope.displayPromo = function() {
          var count = $(".iconic_count").length;
          var mod = (count % 2);
		  var promoloop = 1;
		  if(mod==0){
			 promoloop = 2; 
		  }
          for (var i = 1; i <= promoloop; i++) {
              $("div#promo_" + i).show();
          }

      }
      
      
      $scope.specialIzationTextCheck = function(str) {
          if (str) {
        	  if (str!="") {
        		  return true;
        	  }
          }
          return false;
      }

      

          $scope.initZoomAnim=function(triggerElem, targetElem, animClass, parentElem) {

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
		$(".zoom-anim-target").css('transition', 'transform 1s cubic-bezier(0.19, 1, 0.22, 1) 0s, -webkit-transform 1s cubic-bezier(0.19, 1, 0.22, 1) 0s');
    }

  }]);