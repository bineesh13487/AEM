/* coke Widget Renderer listener*/
function cokeWidgetRenderer(event, value) {
  var filtersJSON = ' ';
  var currentForm = event.findParentByType('form');
  var currentPagePath = CQ.WCM.getPagePath();
  $.ajax({
    url: '/services/coke-filters.json?pagePath=' + currentPagePath,
    dataType: 'json ',
    type: 'GET',
    async: true,
    success: function(successData) {
      console.log(successData);
      filtersJSON = successData.data.bvgcat;
    },
	error: function(){
		alert('The Coke API Service is temporarily downn. Please try after some time.');
	}
  });
}


/* coke Brand Renderer listener*/
function cokeBrandRenderer(event, value) {
  var filtersJSON = ' ';
  var categoryJSON = [];
  var currentForm = event.findParentByType('form');
  var currentPagePath = CQ.WCM.getPagePath();
  //console.log('currentForm',currentForm);
  event.addClass('brandCount');
  var componentId = event.id;
  var curentCount;
  console.log('Brand listener Renderer');
  $(".brandCount").each(function(index) {
    if ($(this).attr('id') == componentId) {
      curentCount = index;
      console.log(curentCount);
    }
  });
  var selectedCategory = '';
  setTimeout(function() {
    selectedCategory = currentForm.find('itemId', 'categoryVal')[curentCount].getValue();
    console.log('selectedCategory', selectedCategory);
    $.ajax({
      url: currentPagePath + '/jcr:content/cokedata/cokeFilters.json',
      dataType: 'json ',
      type: 'GET',
      async: false,
      success: function(successData) {
        filtersJSON = successData.data.bvgcat[selectedCategory]['brand'];
      }
    }); //Parsinf Local Json  

    $.each(filtersJSON, function(key, item) { // key: the name of the object key    // index: the ordinal position of the key within the object 
      var obj = {
        value: item.code,
        text: item.name
      }
      categoryJSON.push(obj);
    });

    console.log('Brands - Listener - Render', categoryJSON);
    var valueField_Brand = currentForm.find('itemId', 'brandCode')[curentCount];
    valueField_Brand.setOptions(categoryJSON);
    var valueField_Flav = currentForm.find('itemId', 'flavorId')[curentCount];
    valueField_Brand.addClass('brandCount');
  }, 300);


}




/* coke Brand Selection Change listener*/
function cokeBrandSelectionChange(event, value) {
  var filtersJSON = ' ';
  var flavorJSON = [];
  var currentForm = event.findParentByType('form');
  var componentId = event.id;
  var currentPagePath = CQ.WCM.getPagePath();
  var curentCount;
  $(".brandCount").each(function(index) {
    if ($(this).attr('id') == componentId) {
      curentCount = index;
      console.log(curentCount);
    }
  });

  var selectedCategory = currentForm.find('itemId', 'categoryVal')[curentCount].getValue();
  console.log('Category and Brand :', selectedCategory, value);
  $.ajax({
    url: currentPagePath + '/jcr:content/cokedata/cokeFilters.json',
    dataType: 'json ',
    type: 'GET',
    async: false,
    success: function(successData) {
      filtersJSON = successData.data.bvgcat[selectedCategory]['brand'][value]['flavor'];
      var brandOptions = successData.data.bvgcat[selectedCategory]['brand'];

      $.each(filtersJSON, function(key, item) { // key: the name of the object key    // index: the ordinal position of the key within the object 
        var obj = {
          value: item.code,
          text: item.name
        }
        flavorJSON.push(obj);
      });


      console.log('Brands - Listener', flavorJSON);
      var valueField_NAME = currentForm.find('itemId', 'flavorId')[curentCount];
      valueField_NAME.setOptions(flavorJSON);
      valueField_NAME.setValue('');
      valueField_NAME.addClass('flavCount');
	  var valueField_Flav = currentForm.find('itemId', 'flavorVal')[curentCount];
	  valueField_Flav.setValue("");
      //var brandOptions = currentForm.find('itemId', 'brandCode')[curentCount].options;
      $.each(brandOptions, function(key, item) {
        if (item.code == value) {
          var valueField_Cat = currentForm.find('itemId', 'brandVal')[curentCount];
          valueField_Cat.setValue(value);
        }
      });
    }
  }); //Parsinf Local Json  
  console.log("Brand Listener-Selection changed");

}


var selectedValue=[];
/* coke Flavor Selection Change listener*/
function cokeFlavorSelectionChange(event, value, checked) {

  var flavorJSON = '';
  var selectedFlavorCodev=[];

  var currentForm = event.findParentByType('form');
  var componentId = event.id;
  var currentPagePath = CQ.WCM.getPagePath();
  var curentCount;
  $(".flavCount").each(function(index) {
    if ($(this).attr('id') == componentId) {
      curentCount = index;
      console.log(curentCount);
    }
  });
  var selectedCategory = currentForm.find('itemId', 'categoryVal')[curentCount].getValue();
  var selectedBrand = currentForm.find('itemId', 'brandVal')[curentCount].getValue(); 
  selectedFlavorCodev= currentForm.find('itemId', 'flavorVal')[curentCount].getValue();
  //console.log('Category and Brand in Flavor Listeneer Changed:', selectedCategory, selectedBrand);
  $.ajax({
    url: currentPagePath + '/jcr:content/cokedata/cokeFilters.json',
    dataType: 'json ',
    type: 'GET',
    async: false,
    success: function(successData) {
      flavorJSON = successData.data.bvgcat[selectedCategory]['brand'][selectedBrand]['flavor'];
      var valueField_Flav = currentForm.find('itemId', 'flavorVal')[curentCount];
      var name = '';
      console.log('Checked Value', value);  
      $.each(flavorJSON, function(key, item) { 
        if (item.code == value) { 
			if(checked){ 
						//if(selectedFlavorCodev ==
						if(selectedFlavorCodev === ""){
							name = item.name;
							valueField_Flav.setValue(name);
						}else{
						 name=selectedFlavorCodev +"," + item.name;
						  valueField_Flav.setValue(name);
						}
					}
				else{
					if(selectedFlavorCodev.length > 0){
						  var splitSelectedFlav=selectedFlavorCodev.split(','); 
						   
						   for(var i=0;i<splitSelectedFlav.length;i++){
							   if(splitSelectedFlav[i] == item.name){
								splitSelectedFlav.splice(i,1);
							   }
							}
					}
					valueField_Flav.setValue(splitSelectedFlav); 
				}
        }
    });
	 
	  var currentVal = valueField_Flav.getValue();
      console.log('currentVal', currentVal);
    }
  });
  console.log("Flavor Listener-Selection changed");

}




/* coke Flavor Renderer listener*/
function cokeFlavorRenderer(event, value) {
  console.log('Inside Flavor listener Render:');
  var filtersJSON = ' ';
  var flavorJSON = [];
  var currentForm = event.findParentByType('form');
  var currentPagePath = CQ.WCM.getPagePath();
  event.addClass('flavCount');
  var componentId = event.id;
  var curentCount;
  $(".flavCount").each(function(index) {
    if ($(this).attr('id') == componentId) {
      curentCount = index;
      console.log(curentCount);
    }
  });

  //var fieldLength = currentForm.find('itemId', 'categoryVal').length;
  setTimeout(function() {
    var selectedCategory = currentForm.find('itemId', 'categoryVal')[curentCount].getValue();
    var selectedBrand = currentForm.find('itemId', 'brandVal')[curentCount].getValue();
    console.log('Category and Brand in Filteers Render:', selectedCategory, selectedBrand);
    $.ajax({
      url: currentPagePath + '/jcr:content/cokedata/cokeFilters.json',
      dataType: 'json ',
      type: 'GET',
      async: false,
      success: function(successData) {
        filtersJSON = successData.data.bvgcat[selectedCategory]['brand'][selectedBrand]['flavor'];
        $.each(filtersJSON, function(key, item) { // key: the name of the object key    // index: the ordinal position of the key within the object 
          var obj = {
            value: item.code,
            text: item.name
          }
          flavorJSON.push(obj);
        });

        console.log('Brands - Listener', flavorJSON);
        var valueField_NAME = currentForm.find('itemId', 'flavorId')[curentCount];
        valueField_NAME.setOptions(flavorJSON);
        valueField_NAME.addClass('flavCount');
        var selectedValues = valueField_NAME.value //Selected Values
        var allcheckboxes = valueField_NAME.optionItems.items;
		 var selectedFlav = currentForm.find('itemId', 'flavorVal')[curentCount].getValue().split(",");

			 $('.flavCount').eq(curentCount).each(function(index) {
          for (var i = 0; i < allcheckboxes.length; i++) {
            var currentInput = $(this).find("input[type='checkbox']").eq(i)
            for (var j = 0; j < selectedValues.length; j++) {
              if (allcheckboxes[i].inputValue == selectedValues[j]) {
                currentInput.prop("checked", '' );
				
				event.findById(currentInput.attr('id')).checked = true;
				//console.log('cheback', '#'+currentInput.attr('id'), event.findById(currentInput.attr('id')));
				currentInput[0].checked = "checked";
				//currentInput[0].setAttribute('checked', 'checked');
				console.log('checkedbox', currentInput);
				currentInput.attr("class","checkFlavor");
              }
            }
          }

        });
			
		


      
        //valueField_NAME.setValue('');
      }
    });
  }, 200);

}


/* coke Category Renderer listener*/
function cokeCategoryRenderer(event, value) {

  var filtersJSON = ' ';
  var categoryJSON = [];
  event.addClass('catCount');
  console.log("From Category listener-Renderer");
  console.log('Categories - Option Provider', categoryJSON);
}


/* coke Category Selection Change listener*/
function cokeCategorySelectionChange(event, value) {
  var filtersJSON = ' ';
  var categoryJSON = [];
  var previousBrandValues = new Array();
  var previousCategoryValues = new Array();
  var checkBrandValues = new Array();
  var currentForm = event.findParentByType('form');
  var fieldLength = currentForm.find('itemId', 'brandCode').length;
  var componentId = event.id;
  var currentPagePath = CQ.WCM.getPagePath();
  var curentCount;
  $(".catCount").each(function(index) {
    if ($(this).attr('id') == componentId) {
      curentCount = index;
      console.log(curentCount);
    }
  });

  //Push the Brands for respective categories fieldlength >2
  var currCatVal = event.findParentByType('form').find('itemId', 'category')[curentCount].getValue();
  event.findParentByType('form').find('itemId', 'brandCode')[curentCount].setValue('');
  console.log('currCatVal', currCatVal);
  if (fieldLength >= 2) {
    for (var index = 0; index < fieldLength; index++) {
      var previousBrandName = currentForm.find('itemId', 'brandCode')[index].getValue();
      previousBrandValues.push(previousBrandName);
      var previousCategoryName = event.findParentByType('form').find('itemId', 'category')[index].getValue();
      previousCategoryValues.push(previousCategoryName);
    }
  }

  for (var i = 0; i < fieldLength; i++) {
    if (currCatVal == previousCategoryValues[i]) {
      checkBrandValues.push(previousBrandValues[i]);
    }
  }
  console.log('checkBrandValues', checkBrandValues);

  $.ajax({
    url: currentPagePath + '/jcr:content/cokedata/cokeFilters.json',
    dataType: 'json ',
    type: 'GET',
    async: false,
    success: function(successData) {
      filtersJSON = successData.data.bvgcat[value]['brand'];
    }
  });

  //Parse info Local Json  
  $.each(filtersJSON, function(key, item) { // key: the name of the object key    // index: the ordinal position of the key within the object 
    var flag = false;
    if (checkBrandValues.length >= 1) {
      for (var indx = 0; indx < checkBrandValues.length; indx++) {
        var brandVal = checkBrandValues[indx];
        if (brandVal == item.name) {
          flag = true;
        }
      }
    }
    if (flag == false) {
      var obj = {
        value: item.code,
        text: item.name
      }
      categoryJSON.push(obj);
    }
  });

  console.log('Categories - Listener', categoryJSON);
  var valueField_Brand = currentForm.find('itemId', 'brandCode')[curentCount];
  valueField_Brand.setOptions(categoryJSON);
  valueField_Brand.setValue('');
  var valueField_Flav = currentForm.find('itemId', 'flavorId')[curentCount];
  //valueField_Flav.setOptions(null);
  valueField_Flav.setValue('');
  valueField_Brand.addClass('brandCount');
  var categoryOptions = currentForm.find('itemId', 'category')[curentCount].options;
  $.each(categoryOptions, function(key, item) {
    if (item.value == value) {
      var valueField_Cat = currentForm.find('itemId', 'categoryVal')[curentCount];
      valueField_Cat.setValue(value);
    }
  });
  console.log("Category Listener-Selection changed");


}
