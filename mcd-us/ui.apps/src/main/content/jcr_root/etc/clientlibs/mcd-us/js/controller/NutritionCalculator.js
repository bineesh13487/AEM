app.controller('nutritionCalculatorController',['$scope','$q','$http','$cookies','getCoop','$timeout', function($scope, $q, $http, $cookies, getCoop, $timeout) {
    $scope.isWindows = window.navigator.platform == 'Win32' ? true : false;
    $scope.catName = "";
    $scope.catId = "";
    $scope.allIds = "";
    $scope.maxCount = $(".nutritionData").data("nclimit");
    var catDetailsUrl = $(".nutritionDnaUrl").data("catdetails");
    var itemDetailsUrl = $(".nutritionDnaUrl").data("itemdetails");
    var itemListUrl = $(".nutritionDnaUrl").data("itemlist");
    var viewType = $(".selectedView").data("viewType");
    var dataCloseItem = $(".buttonText").data("closeItem");
    var dataExploreItem = $(".buttonText").data("exploreItem");
    var dataCloseIngredients = $(".buttonText").data("closeIngredients");
    var dataCustomizeIngredients = $(".buttonText").data("customizeIngredients");

    $scope.sizeChanged = false;
    $scope.isArray = angular.isArray;
    $scope.isUndefined = angular.isUndefined;
    $scope.equals = angular.equals;
    $scope.categoryLoading = false;
    $scope.mealLoading = false;

    $scope.itemsJsonResponse = [];
    $scope.catItems = [];
    var coopurl = "";
    var coop=getCoop.getCoopId();
    if (coop) {
    	coopurl = "&coopId=" + coop;
    }



    $scope.updateCatName = function(e) {

    	$scope.catName = angular.element(e.currentTarget).find(".name").text();

    };

    $scope.addItem = function(id,prevent,e,itemName) { /* Start: GWSL-1516 - Added itemName parameter to this function */ /* End */

    		if (prevent) {
    			e.preventDefault();

          /* Start: GWSL-1516 */
          /* Screen reader to alert/read when same items added more than one time in NC. Also see script.js line 1933 - for adding first time */
          var addedText = $('.controls .status').first().text();
  				var alertElem = $( "<div role='alert' class='visuallyhidden nc-added-alert'>"+itemName+addedText+"</div>" ).appendTo( "body" );
          $(window).scrollTop($(".meal-item").eq(0));
  				setTimeout(function() {alertElem.remove();}, 3000);
          /* End */
    		}

    		if ($scope.itemsJsonResponse.length>=$scope.maxCount) {
    			$('#ncLimitModal').modal('show');
    		}
    		else {
    			$scope.mealLoading = true;
    			$( ".meal-item .meal-row .controls button" ).each(function() {
    				if (!$(this).hasClass("collapsed")) {
    					$(this).trigger("click");
    				}
    			});
    			// $(window).scrollTop($('#level-details'));
    			$http.get(itemDetailsUrl+"&item=" + id).then(function(response) {
            		var itemObj = {};
            		itemObj.id = response.data.item.item_id;
            		var itemTime = new Date().getTime();
            		itemObj.time = itemTime;
            		itemObj.products = $scope.getProducts(response.data);
            		itemObj.itemStatements = $scope.itemlevelIngradients(response.data);
            		itemObj.imgName = $scope.getItemImage(response.data.item);
            		itemObj.transpImgName = $scope.getTransparentImage(response.data.item);
            		itemObj.serveSize = $scope.getServingSize(response.data);
            		itemObj.has_components = response.data.item.has_components;
            		itemObj.itemName=angular.equals(response.data.item.item_marketing_name,{}) ? response.data.item.item_name : response.data.item.item_marketing_name;
            		$scope.itemsJsonResponse.unshift(itemObj);
                $scope.updateItemsAriaLabel();
            		$scope.mealLoading = false;
            		$timeout(function(){
            			 $scope.updateNutritionValues(false);

                   /* Start: GWSL-1516 */
            			 setTimeout(function(){
                     $(window).scrollTop($(".meal-item").eq(0));
                     $(".meal-item").eq(0).focus();
                   }, (itemObj.itemName.length + 15) < 50 ? 2000 : 4000);
                   /* End */
            			 $(".row.meal-row .select").each(function() {
            				 if ($(this).hasClass("open")) {
            					 $(this).removeClass("open");
            					 $(this).find("button").attr('aria-expanded', false);
            				 }
            			 });

               	    });

                });
    		}
    };

    $scope.updateItemsAriaLabel = function() {
      var tempObj = {};

      /* getting number of duplicate occurance first.
        `setsize` is total duplicate item. (like aria-setsize)
        `posinset` is position in set. (like aria-posinset)
      */
      $scope.itemsJsonResponse.map(function(item, index) {
        if(item.id in tempObj) {
          tempObj[item.id].setsize++;
          tempObj[item.id].posinset.push(tempObj[item.id].setsize);
        } else {
          tempObj[item.id] = {setsize: 1, posinset: [1]}
        }
      });

      // Updating aria-label text here
      $scope.itemsJsonResponse = $scope.itemsJsonResponse.map(function(item, index) {
        //item.ariaLabelSuffix = '(' + tempObj[item.id].posinset.shift() + ' of ' + tempObj[item.id].setsize + ')';
        item.ariaPosinset = tempObj[item.id].posinset.shift();
        item.ariaSetsize = tempObj[item.id].setsize;
        return item;
      });

      tempObj = {}; // clearing the data
    }


    $scope.openButtonheading = function(hasComponentFlag,isNutritionInfoAtProductLevel) {
    	if (isNutritionInfoAtProductLevel!='true') {
    		return dataCloseItem;
    	}
    	if(hasComponentFlag === 'No'){
    		return dataCloseItem;
    	}
    	return dataCloseIngredients;
    }

    $scope.closeButtonheading = function(hasComponentFlag,isNutritionInfoAtProductLevel) {
    	if (isNutritionInfoAtProductLevel!='true') {
    		return dataExploreItem;
    	}
    	if(hasComponentFlag === 'No'){
    		return dataExploreItem;
    	}
    	return dataCustomizeIngredients;
    }

    $scope.updateItems = function(id,time,e) {
    	e.preventDefault();
    	//$(window).scrollTop($('#level-details'));
    	$http.get(itemDetailsUrl+"&item=" + id).then(function(response) {
    		var dp = response.data;
    		$scope.sizeChanged = true;
    		for (var i = 0;i<$scope.itemsJsonResponse.length;i++) {

        		if ($scope.itemsJsonResponse[i].time==time) {
        			$scope.itemsJsonResponse[i].products = $scope.getProducts(dp);

        			if ($scope.sizeChanged) {
        				if ($scope.itemsJsonResponse[i].has_components=='No') {
                  /* Start: GWSL-1520 - changed to false */
                  $scope.updateNutritionValues(false);
                  /* End */
        					$scope.sizeChanged = false;
        				}
        	    }
              /* Start: GWSL-1520 */
              $('#'+$scope.itemsJsonResponse[i].time).find('.select-dropdown').focus();
              /* End */
        			return false;
        		}
        	}
        });
    }

    $scope.removeItem = function(id,time,e) {
    	e.preventDefault();
    	//$scope.hideDeleteModal();
    	for (var i = 0;i<$scope.itemsJsonResponse.length;i++) {

    		if ($scope.itemsJsonResponse[i].time==time) {
    			$scope.itemsJsonResponse.splice(i,1);
    		}
    	}
      $scope.updateItemsAriaLabel();
    	//$(window).scrollTop($('#level-details'));
    	$(window).scrollTop($('.view-basket .view-btn.add-meal'));
   		$(".view-basket .view-btn.add-meal").focus();
   		$timeout(function(){
   			$(".view-basket .view-btn.add-meal").focus();
   			$scope.updateNutritionValues(false);
    	});


    };

    $scope.animateNc = function() {

      $scope.deleteAllConfirmFlag = false; //used to show show/hide #deleteAllModal elements

      $( ".my-meal .totals.animated li" ).each(function( index ) {
            var nutrAnimItem = $(this);
            if(nutrAnimItem.hasClass('update')){
              nutrAnimItem.removeClass('update');
            }
            setTimeout(function() {
              nutrAnimItem.addClass('update');
            }, 50*index);
          });

    	$( ".my-meal .totals.animated .item.desktop" ).each(function( index ) {
            var nutrAnimItem = $(this);
            if(nutrAnimItem.hasClass('update')){
              nutrAnimItem.removeClass('update');
            }
            setTimeout(function() {
              nutrAnimItem.addClass('update');
            }, 50*index);
          });

    };

    $scope.removeAllItems = function() {
    	$scope.allIds = "";
    	var ids = [];
    	for (var i=0;i<$scope.itemsJsonResponse.length;i++) {
    		ids.push($scope.itemsJsonResponse[i].id);
    	}
    	$scope.allIds = ids.toString()
    	//$scope.hideDeleteAllModal();
    	$scope.myMealCounter = 0;
    	$scope.itemsJsonResponse = [];
    	$scope.allNutritionValues = {};

      $scope.deleteAllConfirmFlag = true;

      /*$(window).scrollTop($('.view-basket .view-btn.add-meal'));


    	setTimeout(function(){ $(".view-basket .view-btn.add-meal").focus();$scope.animateNc();$scope.updateTable(); }, 500);
      */

      $(window).scrollTop($('.view-basket .view-btn.add-meal'));
      $('.nc-added-alert').remove(); //Fix: for NVDA screen reader. These are hidden elements [role="alert"] created when adding new items. These elements are not necessary after all items are deleted.
      setTimeout(function () {
        $('#deleteAllModal').modal('hide');
        $(".view-basket .view-btn.add-meal").focus();
        $scope.animateNc();
        $scope.updateTable();
      }, 3000);

    };

    /*$scope.hideDeleteModal = function() {
    	$('#deleteModal').modal('hide');
    }

    $scope.hideDeleteAllModal = function() {
    	$('#deleteAllModal').modal('hide');
    }

    $scope.hideExitModal = function() {
    	$('#ncExitModal').modal('hide');
    }

    $scope.hideLimitModal = function() {
    	$('#ncLimitModal').modal('hide');
    }*/

    $scope.isComponentChecked = function(id) {
        if (id) {
            if (id.toString().startsWith("2")) {
                return false;
            } else {
                return true;
            }
        }
        else {
        	return false;
        }
    };

    $scope.isItem = function(id) {
        if (id) {
            if (id.toString().startsWith("2")) {
                return true;
            } else {
                return false;
            }
        }
        else {
        	return false;
        }
    };

    $scope.isItemAdded = function(id) {

    	if ($scope.itemsJsonResponse) {
    		var itemsArray = $scope.itemsJsonResponse;
    		if (itemsArray.length>0) {
    			for (var i=0;i<itemsArray.length;i++) {
    				if (itemsArray[i].id==id) {
    					return true;
    				}
    			}
    		}
    	}
    	return false;
    }

    $scope.getItemImage = function(elem) {
    	if(elem) {
    		if (JSON.stringify(elem).trim() != "{}") {
    			if (elem.attach_item_thumbnail_image) {
    				if (JSON.stringify(elem.attach_item_thumbnail_image).trim() != "{}") {
    					if (elem.attach_item_thumbnail_image.image_name) {
    						if (JSON.stringify(elem.attach_item_thumbnail_image.image_name).trim() != "{}") {
    							return elem.attach_item_thumbnail_image.image_name;
    						}
    					}
    				}
    			}
    		}
    	}
    	return "default.png";
    };


    $scope.getTransparentImage = function(elem) {
    	if(elem) {
    		if (JSON.stringify(elem).trim() != "{}") {
    			if (elem.attach_transparent_icon_image) {
    				if (JSON.stringify(elem.attach_transparent_icon_image).trim() != "{}") {
    					if (elem.attach_transparent_icon_image.image_name) {
    						if (JSON.stringify(elem.attach_transparent_icon_image.image_name).trim() != "{}") {
    							return elem.attach_transparent_icon_image.image_name;
    						}
    					}
    				}
    			}
    		}
    	}
    	return "default.jpg";
    };

    $scope.getComponentImage = function(elem) {
    	if(elem) {
    		if (JSON.stringify(elem).trim() != "{}") {
    			if (elem.attach_product_thumbnail_image) {
    				if (JSON.stringify(elem.attach_product_thumbnail_image).trim() != "{}") {
    					if (elem.attach_product_thumbnail_image.image_name) {
    						if (JSON.stringify(elem.attach_product_thumbnail_image.image_name).trim() != "{}") {
    							return elem.attach_product_thumbnail_image.image_name;
    						}
    					}
    				}
    			}
    		}
    	}
    	return "default.png";
    };

    $scope.showCustomizeButton = function(x) {
    	if (x) {
	        if (JSON.stringify(x) != "{}") {
	        	if (JSON.stringify(x).trim() != "{}") {
	        		if (x.has_components=="Yes") {
	        			if(x.products.length>0) {
	        				return true;
	        			}
	        		}
	        	}
	        }
    	}
    	return false;
    };

    $scope.sanitizeElement = function(x) {
    	if (x) {
	        if (JSON.stringify(x) != "{}") {
	            return x;
	        } else {
	            return "";
	        }
    	}
    	else {
    	    return "";
    	}
    };

    $scope.isEmpty = function(x) {
    	if (!angular.isUndefined(x)) {
	        if (JSON.stringify(x) != "{}") {
	        	if (JSON.stringify(x).trim() != "{}") {
	        		if (typeof x != 'string' || x!="") {
	        			return false;
	        		}
	        	}
	        }
    	}
    	return true;
    };


    $scope.isDefaultCheck = function(x) {
    	if (x) {
    		return true;
    	}
    	return false;
    };

    $scope.getCatDetails = function() {
        $scope.categoryLoading = true;
        $(window).scrollTop($('#level-category'));
        $scope.catItems = [];
        $http.get(catDetailsUrl+"&categoryId=" + $scope.catId+coopurl).then(function(response) {
            $scope.categoryLoading = false;
            //$scope.catItems = data.category.items.item;
            var allItems = response.data.category.items.item;
            if (allItems) {
            	if (Array.isArray(allItems)) {
            		for (var i=0;i<allItems.length;i++) {
            			if ($scope.displayItem(allItems[i])) {
            				var itemObj = {};
                    		itemObj.id = allItems[i].item_id;
                    		itemObj.name = allItems[i].item_marketing_name;
                    		if ($scope.isEmpty(itemObj.name)) {
                    			itemObj.name = allItems[i].item_name;
                    		}
                    		itemObj.img = $scope.getItemImage(allItems[i]);
                    		itemObj.transImgName = $scope.getTransparentImage(allItems[i]);
                    		itemObj.type = "normal";
                    		$scope.catItems.push(itemObj);
            			}
            		}
            	}
            }
            if ($scope.catItems.length>0 && $scope.catItems.length%4 != 0) {
            	var dummyCount = 4 - $scope.catItems.length%4;
            	for (var k=0;k<dummyCount;k++) {
            		var itemObj = {};
            		itemObj.type = "dummy";
            		$scope.catItems.push(itemObj);
            	}
            }
            $(".nc-catname").focus();
        })
    };
    $scope.getServingSize = function(b) {
        var innerList = [];
        if (b.item.relation_types) {
            if (b.item.relation_types.relation_type) {
                if (Array.isArray(b.item.relation_types.relation_type)) {
                    var m = b.item.relation_types.relation_type;
                    for (var i = 0; i < m.length; i++) {
                        var elem = m[i];
                        if (elem && elem.type != "master child") {
                            var elem1 = elem.related_items.related_item;
                            if (elem1) {
                                if (Array.isArray(elem1)) {
                                    for (var j = 0; j < elem1.length; j++) {
                                        innerList.push(elem1[j]);
                                        if (elem1[j].id==b.item.id) {
                                            innerList.first = elem1[j].label;
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (b.item.relation_types.relation_type.type != "master child") {
                        var elem = b.item.relation_types.relation_type;
                        if (Array.isArray(elem.related_items.related_item)) {
                            for (var i = 0; i < elem.related_items.related_item.length; i++) {
                                var elem1 = elem.related_items.related_item[i];
                                innerList.push(elem1);
                                if (elem1.id==b.item.id) {
                                    innerList.first = elem1.label;
                                }
                            }
                        }
                    }
                }
            }
        }
        return innerList;
    };
    $scope.getComponents = function(b) {
        var innerList = [];
        if (b.item.components) {
            if (b.item.components.component) {
                if (Array.isArray(b.item.components.component)) {
                    var m = b.item.components.component;
                    for (var i = 0; i < m.length; i++) {
                        var elem = m[i];
                        if (elem) {
                            elem.radioName = m[i].id;
                            if (m[i].id.toString().startsWith("2")) {
                            	 elem.type = "optional-component";
                            }
                            else {
                            	elem.type = "component";
                            }
                            innerList.push(elem);
                        }
                    }
                }
                else {
                	 var m = b.item.components.component;
                     var elem = m;
                     if (elem) {
                    	 elem.radioName = m.id;
                    	 elem.type = "component";
                         innerList.push(elem);
                     }
                }
            }
        }

        return innerList;
    };
    $scope.getMutex = function(b) {
        var innerList = [];
        if (b.item.mutex_groups) {
            if (Array.isArray(b.item.mutex_groups.mutex_group)) {


                var m = b.item.mutex_groups.mutex_group;

                for (var i = 0; i < m.length; i++) {
                    if (Array.isArray(m[i].constituents.constituent)) {
                    	 var byDispOrderm = m[i].constituents.constituent.slice(0);
                         byDispOrderm.sort(function(a,b) {
                             return a.display_order - b.display_order;
                         });
                         var nn = {};
                         nn.display_order = m[i].display_order;
                         nn.id = m[i].id;
                         nn.type = "mutex";
                         nn.constituents = byDispOrderm;
                         innerList.push(nn);
                    }
                }
            } else {
                if (b.item.mutex_groups.mutex_group) {
                	var m = b.item.mutex_groups.mutex_group;
                	if (m.constituents) {
                		if ( m.constituents.constituent) {
                			var byDispOrderm = m.constituents.constituent.slice(0);
                            byDispOrderm.sort(function(a,b) {
                                return a.display_order - b.display_order;
                            });

                			var nn = {};
                            nn.display_order = m.display_order;
                            nn.id = m.id;
                            nn.type = "mutex";
                            nn.constituents = byDispOrderm;
                            innerList.push(nn);
                		}
                	}
                }
            }
        }

        return innerList;
    };

    $scope.getProducts = function(b) {
    	 var components = $scope.getComponents(b);
    	 var mutex = $scope.getMutex(b);
    	 var c = components.concat(mutex);
    	 var byDispOrderm = c.slice(0);
         byDispOrderm.sort(function(a,b) {
             return a.display_order - b.display_order;
         });
         return byDispOrderm;
    }

    $scope.itemlevelIngradients = function(b) {
	    var itemIngredients = {};
		if (JSON.stringify(b.item.item_ingredient_statement).trim() != "{}") {
			itemIngredients.item_ingredient_statement = b.item.item_ingredient_statement;
	    }
	    if (JSON.stringify(b.item.item_additional_allergen).trim() != "{}") {
	    	itemIngredients.additionalAllergen = b.item.item_additional_allergen;
	    }
	    if (JSON.stringify(b.item.item_allergen).trim() != "{}") {
	    	itemIngredients.allergen = b.item.item_allergen;
	    }
	    if (JSON.stringify(b.item.item_additional_text_ingredient_statement).trim() != "{}") {
	    	itemIngredients.additionalStatement = b.item.item_additional_text_ingredient_statement;
	    }
	    return itemIngredients;
    }


    $scope.displayItem = function(x) {
    var itemVisiblity=false;
    	//donotshow core check was here
        if (JSON.stringify(x.do_not_show).trim()!=='{}' && x.do_not_show !="Do not Show") {
            if (JSON.stringify(x.relation_types) === '{}') {
                return true;
            }
            if (!(x.relation_types.relation_type instanceof Array)) {

                    if (x.relation_types.relation_type.related_items.related_item instanceof Array) {
                        for (var i = 0; i < x.relation_types.relation_type.related_items.related_item.length; i++) {
                            if (x.relation_types.relation_type.related_items.related_item[i].is_default) {
                                if (x.relation_types.relation_type.related_items.related_item[i].id == x.id) {
                                    itemVisiblity= true;
                                }
                            }
                        }
                    } else {
                        if (x.relation_types.relation_type.related_items.related_item.is_default) {
                            if (x.relation_types.relation_type.related_items.related_item.id == x.id) {
                                itemVisiblity= true;
                            }
                        }
                    }
              return itemVisiblity;
            }
            if (x.relation_types.relation_type instanceof Array) {
                for (var i = 0; i < x.relation_types.relation_type.length; i++) {
                                itemVisiblity=false;
                        if (x.relation_types.relation_type[i].related_items.related_item instanceof Array) {
                            for (var j = 0; j < x.relation_types.relation_type[i].related_items.related_item.length; j++) {
                                if (x.relation_types.relation_type[i].related_items.related_item[j].is_default) {
                                    if (x.relation_types.relation_type[i].related_items.related_item[j].id == x.id) {
                                        itemVisiblity= true;
                                    }
                                }
                            }
                        } else {
                            if (x.relation_types.relation_type[i].related_items.related_item.is_default) {
                                if (x.relation_types.relation_type[i].related_items.related_item.id == x.id) {
                                     itemVisiblity= true;
                                }
                            }
                        }
                             if(!itemVisiblity){
                                break;
                             }

                }
                       return itemVisiblity;
            }
           // return true;
        }
        else {
        	return false;
        }
    };

    $scope.getUom = function(x) {
    	if (JSON.stringify(x.uom).trim() != "{}" && x.uom !="") {
    			return x.uom;
    	}
    	return "";
    };

    $scope.getUomDescription = function(x) {

    	if (x) {
    		if (JSON.stringify(x.uom_description).trim() != "{}" && x.uom_description !="") {
    			return x.uom_description;
    		}
    	}
    	return "";
    };

    $scope.getDv = function(x) {
    	var dvtext = $(".nutritionData").data("dvtext").toString();
    	if (JSON.stringify(x.adult_dv).trim() != "{}") {
    			if (typeof x != 'string') {
    				return "(" + x.adult_dv + "% "+dvtext+")";
    			}
    		}
    	return "";
    };

    $scope.addBrakets = function(x) {
    	if (x && x != "") {
			return "(" + x + ")";
		}
    	return "";
    }

    $scope.removeBrakets = function(x) {
    	if (x && x != "") {
    		var parts = x.split("")
    		var returnString = "";
    		for (var count =0; count < parts.length; count++){
    			if(count != 0 && count != parts.length -1){
    				returnString = returnString + parts[count];
    			}
    		}
			return returnString;
		}
    	return "";
    }

    $scope.setDeleteVal = function(name,id,time) {
    	$scope.deleteName=name;
    	$scope.deleteId=id;
    	$scope.deleteTime=time;
    };

 $scope.updateTable = function() {
	 	var totalsHtml = "";
	    var totalsLength = $('div.totals .item.desktop').length;
	    $(".totals table").remove();
	    totalsHtml = '<table class="table"><tr>';
	    $('div.totals .item.desktop').each(function(x,y){
	      var tableItem = $(y).clone().removeClass('desktop');
	      var tableItemStr = tableItem.wrap('<div>').parent().html();

	      if(totalsLength == 7){
	        if(x < 4){
	          totalsHtml += '<td class="col-xs-3">' + tableItemStr + '</td>';
	        }
	        else{
	          totalsHtml += '<td class="col-xs-4">' + tableItemStr + '</td>';
	        }
	      }
	      else if(totalsLength == 6){
	        if(x < 4){
	          totalsHtml += '<td class="col-xs-4">' + tableItemStr + '</td>';
	        }
	        else{
	          totalsHtml += '<td class="col-xs-4">' + tableItemStr + '</td>';
	        }
	      }
	      else if(totalsLength == 5){
	        if(x < 3){
	          totalsHtml += '<td class="col-xs-4">' + tableItemStr + '</td>';
	        }
	        else{
	          totalsHtml += '<td class="col-xs-6">' + tableItemStr + '</td>';
	        }
	      }
	      else{
	        totalsHtml += '<td class="col-xs-3">' + tableItemStr + '</td>';
	      }

	      if(totalsLength == 7 && x == 3){
	        totalsHtml += '</tr></table><table class="table"><tr>';
	      }
	      else if(totalsLength == 6 && x == 2){
	        totalsHtml += '</tr></table><table class="table"><tr>';
	      }
	      else if(totalsLength == 5 && x == 2){
	        totalsHtml += '</tr></table><table class="table"><tr>';
	      }

	    });
	    totalsHtml += '</tr></table>';
	    $('.totals').append(totalsHtml);


	    $( ".my-meal table .item" ).each(function( index ) {
            var nutrAnimItem = $(this);
            if(nutrAnimItem.hasClass('update')){
              nutrAnimItem.removeClass('update');
            }
            setTimeout(function() {
              nutrAnimItem.addClass('update');
            }, 50*index);
          });

    };


    $scope.nutritionValues = function(x,focusToSummary) {

    	$scope.allNutritionValues = {};
    	if (focusToSummary) {
    		$(window).scrollTop($('#level-details'));
    	}

    	for (var i = 0;i<x.length; i++) {
    		var n = {};
    		n.id = x[i].id;
    		n.value = $scope.isEmpty(x[i].value) ? "" : x[i].value;
    		n.uom = $scope.getUom(x[i]);
    		n.uomDescription = $scope.getUomDescription(x[i]);
    		n.adult_dv = $scope.getDv(x[i]);
    		$scope.allNutritionValues[x[i].id] = n;
    	}

    	setTimeout(function() {
    		if (focusToSummary) {
    			$(".nc-summary").focus();
    		}
    			$scope.animateNc();
    			$scope.updateTable();
    		}, 500);
    };


    $scope.updateNutritionValues = function(scrollToSummary) {

    	var str = "";
    	$scope.myMealCounter = angular.element(".meal-item .meal-row > input:checked").length;
    	  angular.forEach(angular.element(".meal-item .meal-row > input:checked"), function(value, key){
    		  var a = angular.element(value);
    		  if (a.parent().find(".hasComponents").length > 0 ) {
    			  if (a.parent().find(".ingredients input:checked").length > 0) {
    			  str=str+a.parent().find(".itemid.hidden").text()+"("
        		  var str1="";

        		  angular.forEach(a.parent().find(".ingredients input:checked"), function(value1, key1){
                	var b = angular.element(value1);
                        str1=str1+b.data("productid")+"|";
                    });
                	str1 = str1.replace(/[|]\s*$/, "");
                	str = str + str1 + ")-";
    			  }
    		  }
    		  else {
    			  str=str+a.parent().find(".itemid.hidden").text()+"()-"
    		  }

    	  	});

    	  if (str!="") {
    		  $http.get(itemListUrl+"&item=" + str).then(function(response) {
    			  $scope.myMealValues = response.data.items.collective_nutrition.nutrient_facts.nutrient;
    			  $scope.nutritionValues($scope.myMealValues,scrollToSummary);
    		  });
    	  }
    	else {
    		$scope.allNutritionValues = {};

    		setTimeout(function(){ $scope.animateNc();$scope.updateTable(); }, 500);
    	}
    };

    $scope.calcSectionOut = function(elm) {
        if(!$(elm).hasClass('out')){
          $(elm).addClass('out');
        }
      };

    if ($cookies.get("MyMealItems")) {

    	if ($cookies.get("MyMealItems") != null && $cookies.get("MyMealItems") != "") {
            var array = $cookies.get("MyMealItems").split(',');
            var responses = [];
            var promises = [];
            $scope.mealLoading = true;
           // $(window).scrollTop($('#level-details'));
            $('.cont-hero .view-meal').hide();
        	$('.cont-hero .add-meal').show();
        	$('#level-all').fadeOut(50);
        	$('#level-category').fadeOut(50);
            $scope.calcSectionOut('#level-all');
            $('#level-category').removeClass('out');

            if ($('.cont-hero .detailed').hasClass('isActive')){
            	$('.cont-hero .detailed').slideUp(300);
            	$('.cont-hero .detailed').removeClass('isActive');
            }

            for (var i = 0 ; i < array.length ; i++) {
        		var promise = $http.get(itemDetailsUrl+"&item=" + array[i]).then(function(response) {
	        		var itemObj = {};
	        		itemObj.id = response.data.item.item_id;
	        		var itemTime = new Date().getTime();
	        		itemObj.time = itemTime;
	        		itemObj.products = $scope.getProducts(response.data);
	        		itemObj.itemStatements = $scope.itemlevelIngradients(response.data);
	        		itemObj.imgName = $scope.getItemImage(response.data.item);
	        		itemObj.transpImgName = $scope.getTransparentImage(response.data.item);
	        		itemObj.serveSize = $scope.getServingSize(response.data);
	        		itemObj.has_components = response.data.item.has_components;
	        		itemObj.itemName=angular.equals(response.data.item.item_marketing_name,{}) ? response.data.item.item_name : response.data.item.item_marketing_name;
	        		responses.push(itemObj);
        		});
        		promises.push(promise);
            }
            $q.all(promises).then(function(data) {
            	for (var j = 0 ; j < array.length ; j++) {
            		for (var k = 0 ; k < responses.length ; k++) {
            			if (array[j]==responses[k].id) {
            				responses[k].order = j+1;
            			}
            		}
            	}
            	var byDispOrderm = responses;
                byDispOrderm.sort(function(a,b) {
                    return a.order - b.order;
                });
                $scope.itemsJsonResponse = byDispOrderm;
                $scope.updateItemsAriaLabel();

                $cookies.remove("MyMealItems", {path: "/"});
                $scope.mealLoading = false;
                $timeout(function(){
                	  $(window).scrollTop($(".meal-item").eq(0));
                	  $(".meal-item").eq(0).focus();
             		  $scope.updateNutritionValues(false);
             	});
            });
        }
    }
    else {
    	$('.init-hide').fadeOut(0);
    }

    $scope.$on('ngRepeatFinishedCategory', function(ngRepeatFinishedEvent) {

    	if (window.innerWidth < 768) {
	    	if ($(".nc-dummy-item").eq(1)) {
	    		$(".nc-dummy-item").eq(1).hide();
	    	}
	    	if ($(".nc-dummy-item").eq(2)) {
	    		$(".nc-dummy-item").eq(2).hide();
	    	}
    	}
	});

    $scope.$on('ngRepeatFinishedNc', function(ngRepeatFinishedEvent) {

    	if ($cookies.get("MyMealItemsPDP")) {
    		if ($cookies.get("MyMealItemsPDP") != null && $cookies.get("MyMealItemsPDP") != "") {
    			if ($(".controls .btn.btn-red.collapsed").length>0) {
    				$(".controls .btn.btn-red.collapsed").trigger("click");
    			}
    		}
    	}
    	$cookies.remove("MyMealItemsPDP", {path: "/"});
	});

    $scope.$on('ngRepeatFinishedNcProducts', function(ngRepeatFinishedEvent) {
    	if ($scope.sizeChanged) {
        /* Start: GWSL-1520 - changed to false */
    		$scope.updateNutritionValues(false);
        /* End */
    		$scope.sizeChanged = false;
    	}
	});

    $scope.$watch('$viewContentLoaded', function(){
    	$timeout(function(){
    		$scope.updateTable();
    	});
    });


}]);


app.directive('onFinishRenderCategory', ['$timeout',function ($timeout) {
    return {
        restrict: 'A',
        link: function (scope, element, attr) {
            if (scope.$last === true) {
                $timeout(function () {
                    scope.$emit('ngRepeatFinishedCategory');
                });
            }
        }
    };
}]);


app.directive('onFinishRenderNc', ['$timeout',function ($timeout) {
    return {
        restrict: 'A',
        link: function (scope, element, attr) {
            if (scope.$last === true) {
                $timeout(function () {
                    scope.$emit('ngRepeatFinishedNc');
                });
            }
        }
    };
}]);

app.directive('onFinishRenderNcProducts', ['$timeout',function ($timeout) {
    return {
        restrict: 'A',
        link: function (scope, element, attr) {
            if (scope.$last === true) {
                $timeout(function () {
                    scope.$emit('ngRepeatFinishedNcProducts');
                });
            }
        }
    };
}]);
