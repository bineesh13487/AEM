app.controller("PeopleStoriesFormController", ['$scope', '$http', 'Scopes', '$timeout','$q', function($scope, $http, Scopes, $timeout, $q) {
    //Scope variables 
    $scope.stateData = ["state1","state2","state3","state4"];
    $scope.positionData = ["position1","position2","position3"];
    $scope.genderData = ["Male","Female","Other"];
    $scope.submitted = false;
    $scope.globalErrorMessage = false;
    $scope.phoneNumbr = /^\+?\d{2}[- ]?\d{3}[- ]?\d{5}$/;
    
    //Private Methods
    var init;
    /**  Initialization method. Called on load.**/
    init = function() { 
    
    };

    /**Scope Methods**/
    
    //Category and Flavors Drop-down selection
    $scope.peopleStoryFormSubmit = function(e,formIsValid) { 
        //event.preventDefault();
        $scope.submitted=true;  
        if(formIsValid){
            $scope.globalErrorMessage=false; 
            var jsonData = {};  
            var formId=$("#people-stories-input-form");
            var formName=formId.attr('name');
            var formArray = formId.serializeArray();  
            $.each(formArray, function() { 
                if (jsonData[this.name]) {
                    if(!jsonData[this.name].push) {
                        jsonData[this.name] = [jsonData[this.name]];
                    }
                    if(this.value != "") {
                        jsonData[this.name].push(this.value || '');
                    } 
                } else {
                    if(this.value != "") {
                        jsonData[this.name] = this.value || '';
                    }
                }
            });
           
            var url='http://www1.staging.mcdonalds.com/cokeintegration/cokestore/locations'; 
            //console.log($("form#people-stories-input-form").find('#upload-photo').val()); 
            //console.log(JSON.stringify(jQuery('#frm').serializeArray()); // store json string)
            //console.log(jsonData);  
            $scope.postPeoplestoriesformData(jsonData,url,formName).then(function(data) { 
                //Should Redirect to other page
                $timeout(function(){
                    window.location.href="/content/us/en-us/success.html" 
                });
            },function (data) {

            });
        } else {
            $scope.globalErrorMessage=true; 
            $('html,body').animate({scrollTop: $('.scrollOnError').offset().top},'medium');
             
        }
    }
    
    $scope.postPeoplestoriesformData = function (inputData,apiUrl,formName) { 
        // var fileimg=new FormData($('#upload-photo'));
        var formData = new FormData($("#people-stories-input-form")); 
        var importFiles = $('#upload-photo')[0].files;  
        
        // console.log(JSON.stringify(inputData))
        var deferred = $q.defer();
        $http({
            method: 'POST',
            url: apiUrl,
            processData: false,
            contentType: false,
            dataType: 'json', 
            cache: false,
            data: {inputdata:inputData,formnaame:formName}
        }).then(function(success) {
            deferred.resolve(success.data);
        }, function(error) {
            deferred.reject(error.data);
        });
        return deferred.promise;
         
    }
    $scope.displayArchways = function(selectedValue) {
        selectedValue ?  $(".archways-section").slideDown(300): $(".archways-section").slideUp(500);    
    };
    
    $('#upload-photo').on('change', function() {
        var file = this.files[0];  
        var fname =  $('#upload-photo').val();
       // fname!=""?$('.filename-display').html(fname):'';
        var imgclean =  $('#upload-photo'); 
        data = new FormData();
        data.append('file', $('#upload-photo')[0].files[0]); 
        var imgname  =  $('input[type=file]').val();
        var size  =  $('#upload-photo')[0].files[0].size; 
        var ext =  imgname.substr( (imgname.lastIndexOf('.') +1) );
        if(ext=='jpg' || ext=='jpeg' || ext=='png' || ext=='gif' || ext=='PNG' || ext=='JPG' || ext=='JPEG')
        {
            if(size<=1000000)
            {
                $.ajax({
                url: "http://www1.staging.mcdonalds.com/cokeintegration/cokestore/locations",
                type: "POST",
                data: data,
                enctype: 'multipart/form-data',
                processData: false,  // tell jQuery not to process the data
                contentType: false   // tell jQuery not to set contentType
                }).done(function(data) {
                    if(data!='FILE_SIZE_ERROR' || data!='FILE_TYPE_ERROR' )
                    {
                        
                    }
                    else
                    {
                        imgclean.replaceWith( imgclean = imgclean.clone( true ) );
                        alert('SORRY SIZE AND TYPE ISSUE');
                    }
            
                });
                return false;
            }//end size
        
        }//end FILETYPE 
    });     


    //Init method call
    init();
    }
]);
