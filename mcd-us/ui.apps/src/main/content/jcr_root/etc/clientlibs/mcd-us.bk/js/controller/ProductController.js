app.controller('productController', ['$scope', '$cookies', '$http', function($scope, $cookies, $http) {

    var itemDetailsUrl = $(".nutritionDnaUrl").data("itemdetails");
    var viewType = $(".selectedView").data("viewType");
    $scope.isArray = angular.isArray;
    $scope.equals = angular.equals;
    $scope.itemsJsonResponse = [];
    $scope.serveSize = [];
    $scope.pcPrimaryNutrition = [];
    $scope.pcSecondaryNutrition = [];
    $scope.pcIngredients = [];
    $scope.isUndefined = angular.isUndefined;
    $scope.customizationLabelText = "";
    $scope.customizationLabelButtonText = "";
    $scope.serves = "";
    $scope.itemIngredients = {};


    $scope.itemId = $(".nutritionData").data("item-id").toString();
    var dvtext = $(".nutritionData").data("dvtext").toString();

    
    $scope.updateItems = function(id,updateSize,e) {
    	
    	$scope.itemId = id;
    	if(!updateSize) {
    		e.preventDefault();
    	}
    	
    	 $http.get(itemDetailsUrl + "&item=" + id).then(function(response) {
    		 	if (updateSize) {
    		 		$scope.getServingSize(response.data);
    		 	}
    	        $scope.getCustomizationLabel(response.data);
    	        $scope.getCustomizationButtonLabel(response.data);
    	        if(response.data.item != undefined){
    	        	$scope.hasComponent= response.data.item.has_components;
   	        		$scope.getIngredients(response.data);
   	        		$scope.getItemLevelIngredients(response.data);
    	        }
    	        $scope.myMealValues = response.data.item.nutrient_facts.nutrient;
    	        $scope.serves = $scope.getServes(response.data);
  			  	$scope.nutritionValues($scope.myMealValues);
    	    });
    	
    	
    }
    
    $scope.getServes = function(elem) {
    	if(elem.item) {
    		if (JSON.stringify(elem.item).trim() != "{}") {
    			if (elem.item.serves) {
    				if (JSON.stringify(elem.item.serves).trim() != "{}") {
    					return elem.item.serves;
    				}
    			}
    		}
    	}
    	return "";
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
    	if (JSON.stringify(x.adult_dv).trim() != "{}" && x.adult_dv !="") {
    		return "(" + x.adult_dv + "% "+dvtext+")";
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
    
    $scope.nutritionValues = function(x) {
    	
    	$scope.allNutritionValues = {};
    	
    	for (var i = 0;i<x.length; i++) {
    		var n = {};
    		n.id = x[i].id;
    		n.value = $scope.isEmpty(x[i].value) ? "" : x[i].value;
    		n.uom = $scope.getUom(x[i]);
    		n.uomDescription = $scope.getUomDescription(x[i]);
    		n.adult_dv = $scope.getDv(x[i]);
    		$scope.allNutritionValues[x[i].id] = n;
    	}
    	
    };
    
    

    
    $scope.getCustomizationLabel = function(b) {
    	
    	if (b) {
    		if (b.item.customization_label) {
    			if (JSON.stringify(b.item.customization_label).trim() != "{}" && b.item.customization_label.trim() != "") {
    				$scope.customizationLabelText = b.item.customization_label;
    			}
    		}
    	}
    }
    
    $scope.getCustomizationButtonLabel = function(b) {
    	
    	if (b) {
    		if (b.item.customization_button) {
    			if (JSON.stringify(b.item.customization_button).trim() != "{}" && b.item.customization_button.trim() != "") {
    				$scope.customizationLabelButtonText = b.item.customization_button;
    			}
    		}
    	}
    }

    $scope.updateItems($scope.itemId,true,"event");


    $scope.getServingSize = function(b) {
        var innerList = [];
        if (b.item.relation_types) {
            if (b.item.relation_types.relation_type != undefined) {
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
                                        if (elem1[j].id == b.item.id) {
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
                                if (elem1.id == b.item.id) {
                                    innerList.first = elem1.label;
                                }
                            }
                        }
                    }
                }
            }
        }
        $scope.serveSize = innerList;
    };


    $scope.getIngredients = function(b) {
        var obj = {};
        var groupObjList = [];
        obj.id = b.item.id;
        $scope.pcIngredients = [];

        if (b.item.mutex_groups) {
            if (Array.isArray(b.item.mutex_groups.mutex_group)) {
                var m = b.item.mutex_groups.mutex_group;
                for (var i = 0; i < m.length; i++) {
                    if (Array.isArray(m[i].constituents.constituent)) {
                        var groupObj = {};
                        for (var j = 0; j < m[i].constituents.constituent.length; j++) {
                            var elem = m[i].constituents.constituent[j];
                            if (elem) {


                                if (elem.is_default) {
                                	if (elem.id.toString().startsWith("2")) {
                                		groupObj.display_order = m[i].display_order;
                                        groupObj.id = m[i].id;
                                        if (JSON.stringify(elem.item_marketing_name).trim() != "{}") {
                                            groupObj.name = elem.item_marketing_name;
                                        } else {
                                            groupObj.name = elem.item_name;
                                        }
                                        if (JSON.stringify(elem.item_ingredient_statement).trim() != "{}") {
                                            groupObj.ingStatement = elem.item_ingredient_statement;
                                        }
                                        if (JSON.stringify(elem.item_allergen).trim() != "{}") {
                                            groupObj.allergen = elem.item_allergen;
                                        }
                                        if (JSON.stringify(elem.item_additional_text_ingredient_statement).trim() != "{}") {
                                            groupObj.additionalStatement = elem.item_additional_text_ingredient_statement;
                                        }
                                        if (JSON.stringify(elem.item_additional_allergen).trim() != "{}") {
                                            groupObj.additionalAllergen = elem.item_additional_allergen;
                                        }
                                        groupObjList.push(groupObj);
                                	}
                                	else {
                                		groupObj.display_order = m[i].display_order;
                                        groupObj.id = m[i].id;
                                        if (JSON.stringify(elem.product_marketing_name).trim() != "{}") {
                                            groupObj.name = elem.product_marketing_name;
                                        } else {
                                            groupObj.name = elem.product_name;
                                        }
                                        if (JSON.stringify(elem.ingredient_statement).trim() != "{}") {
                                            groupObj.ingStatement = elem.ingredient_statement;
                                        }
                                        if (JSON.stringify(elem.product_allergen).trim() != "{}") {
                                            groupObj.allergen = elem.product_allergen;
                                        }
                                        if (JSON.stringify(elem.product_additional_text_ingredient_statement).trim() != "{}") {
                                            groupObj.additionalStatement = elem.product_additional_text_ingredient_statement;
                                        }
                                        if (JSON.stringify(elem.product_additional_allergen).trim() != "{}") {
                                            groupObj.additionalAllergen = elem.product_additional_allergen;
                                        }
                                        groupObjList.push(groupObj);
                                	}
                                    
                                }
                            }
                        }

                    }
                }
            } else if (b.item.mutex_groups.mutex_group) {
                var m = b.item.mutex_groups.mutex_group;
                var groupObj = {};


                var compIdList = [];
                if (Array.isArray(m.constituents.constituent)) {
                for (var i = 0; i < m.constituents.constituent.length; i++) {
                    var elem = m.constituents.constituent[i];
                    if (elem) {

                        if (elem.is_default) {
                        	if (elem.id.toString().startsWith("2")) {
                        		groupObj.display_order = m.display_order;
                                groupObj.id = m.id;
                                if (JSON.stringify(elem.item_marketing_name).trim() != "{}") {
                                    groupObj.name = elem.item_marketing_name;
                                } else {
                                    groupObj.name = elem.item_name;
                                }
                                if (JSON.stringify(elem.item_ingredient_statement).trim() != "{}") {
                                    groupObj.ingStatement = elem.item_ingredient_statement;
                                }
                                if (JSON.stringify(elem.item_allergen).trim() != "{}") {
                                    groupObj.allergen = elem.item_allergen;
                                }
                                if (JSON.stringify(elem.item_additional_text_ingredient_statement).trim() != "{}") {
                                    groupObj.additionalStatement = elem.item_additional_text_ingredient_statement;
                                }
                                if (JSON.stringify(elem.item_additional_allergen).trim() != "{}") {
                                    groupObj.additionalAllergen = elem.item_additional_allergen;
                                }
                                groupObjList.push(groupObj);
                        	}
                        	else {
                        		groupObj.display_order = m.display_order;
                                groupObj.id = m.id;
                                if (JSON.stringify(elem.product_marketing_name).trim() != "{}") {
                                    groupObj.name = elem.product_marketing_name;
                                } else {
                                    groupObj.name = elem.product_name;
                                }
                                if (JSON.stringify(elem.ingredient_statement).trim() != "{}") {
                                    groupObj.ingStatement = elem.ingredient_statement;
                                }
                                if (JSON.stringify(elem.product_allergen).trim() != "{}") {
                                    groupObj.allergen = elem.product_allergen;
                                }
                                if (JSON.stringify(elem.product_additional_text_ingredient_statement).trim() != "{}") {
                                    groupObj.additionalStatement = elem.product_additional_text_ingredient_statement;
                                }
                                if (JSON.stringify(elem.product_additional_allergen).trim() != "{}") {
                                    groupObj.additionalAllergen = elem.product_additional_allergen;
                                }
                                groupObjList.push(groupObj);
                        	}
                            
                        }

                    }
                }
                }

            }
        }
        if (b.item.components) {
            if (b.item.components.component) {
                if (Array.isArray(b.item.components.component)) {
                    var m = b.item.components.component;



                    for (var i = 0; i < m.length; i++) {
                        var elem = m[i];
                        if (elem) {
                            var groupObj = {};
                            if (!elem.id.toString().startsWith("2")) {
                                groupObj.id = elem.id;
                                groupObj.display_order = m[i].display_order;
                                if (JSON.stringify(m[i].product_marketing_name).trim() != "{}") {
                                    groupObj.name = m[i].product_marketing_name;
                                } else {
                                    groupObj.name = m[i].product_name;
                                }
                                if (JSON.stringify(m[i].ingredient_statement).trim() != "{}") {
                                    groupObj.ingStatement = m[i].ingredient_statement;
                                }
                                if (JSON.stringify(m[i].product_allergen).trim() != "{}") {
                                    groupObj.allergen = m[i].product_allergen;
                                }
                                if (JSON.stringify(m[i].product_additional_text_ingredient_statement).trim() != "{}") {
                                    groupObj.additionalStatement = m[i].product_additional_text_ingredient_statement;
                                }
								if (JSON.stringify(m[i].product_additional_allergen).trim() != "{}") {
                                    groupObj.additionalAllergen = m[i].product_additional_allergen;
                                }
                                groupObjList.push(groupObj);
                            }
                        }
                    }

                } else if (b.item.components.component) {
                    var m = b.item.components.component;



                    var elem = m;
                    if (elem) {
                        var groupObj = {};
                        if (!elem.id.toString().startsWith("2")) {
                            groupObj.id = elem.id;
                            groupObj.display_order = m.display_order;
                            if (JSON.stringify(m.product_marketing_name).trim() != "{}") {
                                groupObj.name = m.product_marketing_name;
                            } else {
                                groupObj.name = m.product_name;
                            }
                            if (JSON.stringify(m.ingredient_statement).trim() != "{}") {
                                groupObj.ingStatement = m.ingredient_statement;
                            }
                            if (JSON.stringify(m.product_allergen).trim() != "{}") {
                                groupObj.allergen = m.product_allergen;
                            }
                            if (JSON.stringify(m.product_additional_text_ingredient_statement).trim() != "{}") {
                                groupObj.additionalStatement = m.product_additional_text_ingredient_statement;
                            }
							if (JSON.stringify(m.product_additional_allergen).trim() != "{}") {
                                groupObj.additionalAllergen = m.product_additional_allergen;
                            }
                            groupObjList.push(groupObj);

                        }

                    }
                }
            }
            
            else
        	{
            	var groupObj = {};
               
                    groupObj.id = b.item.id;
                    //groupObj.display_order = m.display_order;
                    if (JSON.stringify(b.item.item_marketing_name).trim() != "{}") {
                        groupObj.name = b.item.item_marketing_name;
                    } else {
                        groupObj.name =  b.item.item_name;
                    }
                    if (JSON.stringify(b.item.item_ingredient_statement).trim() != "{}") {
                        groupObj.ingStatement = b.item.item_ingredient_statement;
                    }
                    if (JSON.stringify(b.item.item_allergen).trim() != "{}") {
                        groupObj.allergen = b.item.item_allergen;
                    }
                    if (JSON.stringify(b.item.item_additional_text_ingredient_statement).trim() != "{}") {
                        groupObj.additionalStatement = b.item.item_additional_text_ingredient_statement;
                    }
					if (JSON.stringify(b.item.item_additional_text_ingredient_statement).trim() != "{}") {
                        groupObj.additionalAllergen = b.item.item_additional_allergen;
        }
                    groupObjList.push(groupObj);

               
        	}
        }
        

        var byDispOrder = groupObjList.slice(0);
        byDispOrder.sort(function(a, b) {
            return a.display_order - b.display_order;
        });
        $scope.pcIngredients = byDispOrder;

    };




    $scope.addToNc1 = function() {
    	var ids = $scope.itemId;
        if (ids != null && ids != "") {
            
        	
            $cookies.put('MyMealItems', ids,{ path: '/' });
            $cookies.put('MyMealItemsPDP', ids,{ path: '/' });
            console.log($cookies.get("MyMealItems"));
        }
    };
    
    $scope.getItemLevelIngredients = function(data){
    	if (data.item.item_ingredient_statement) {
    		if (JSON.stringify(data.item.item_ingredient_statement).trim() != "{}") {
    			$scope.itemIngredients.item_ingredient_statement = data.item.item_ingredient_statement;
    		}
    	}
    	if (data.item.item_additional_allergen) {
    		if (JSON.stringify(data.item.item_additional_allergen).trim() != "{}") {
        		$scope.itemIngredients.additionalAllergen = data.item.item_additional_allergen;
        	}
    	}
    	if (data.item.item_allergen) {
    		if (JSON.stringify(data.item.item_allergen).trim() != "{}") {
    			$scope.itemIngredients.allergen = data.item.item_allergen;
    		}
    	}
    	if (data.item.item_additional_text_ingredient_statement) {
    		if (JSON.stringify(data.item.item_additional_text_ingredient_statement).trim() != "{}") {
        		$scope.itemIngredients.additionalStatement = data.item.item_additional_text_ingredient_statement;
        	}
    	}
	}
}]);