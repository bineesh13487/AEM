$( document).ready(function() {
     // alert("in ready javascript function");
        var categoryId=$('#pagecategoryId').val();
        var defaultImagePath=$('#defaultProductImagePath').val();
      //  alert("Category id is"+categoryId);
        if(!(categoryId==0)){
    $( "#loadingDiv").show();
       var url="/bin/categoryDetails";
         $.ajax({
             url: url,
             data:{categoryID:categoryId,
                   showLiveData:"true",
                   country:$('#country').val(),
                   language:$('#language').val()},
             dataType:"json",
             method:"GET",
             error:function(data){
                   var demoStr="";
 				   demoStr="<h3>Error while reading response from Server.</h3>";
                   $('#productsDetail').html(demoStr);
             },
             success: function(data){
        		//alert(data.category.id);
            var array=data.category.items;
            var demoStr="";
            if(array==undefined){
                demoStr="<h3>No more Data Retrieved from Server</h3>";
            }else{
                demoStr=demoStr+"<table width='100%'><tr>";
                for(var i=0;i<2;i++){
 					demoStr=demoStr+"<td width='50%'>";
 					if ((typeof array[i].imagePath !== "undefined") && (array[i].imagePath.length > 0)) {
                       demoStr=demoStr+"<img src='"+array[i].imagePath+"'/></br>";
                    }else{
                    demoStr=demoStr+"<img src='"+defaultImagePath+"'/></br>";
                    }
					demoStr=demoStr+"<h2>"+array[i].marketingName+"</h2>";
               	    demoStr=demoStr+"<a href='"+array[i].path+"'>Learn More</a>";
				    demoStr=demoStr+"</td>";
                }
            demoStr=demoStr+"</tr></table><table><tr>";
            var count=0;
            for(var i=2;i<array.length;i++){
				if(count==3){
						count=0;
						demoStr=demoStr+"</tr><tr>";
						}
						demoStr=demoStr+"<td>";
                    if ((typeof array[i].imagePath !== "undefined") && (array[i].imagePath.length > 0)) {
                       demoStr=demoStr+"<img src='"+array[i].imagePath+"'/></br>";
                    }else{
                    demoStr=demoStr+"<img src='"+defaultImagePath+"'/></br>";
                    }
				demoStr=demoStr+"<h3>"+array[i].marketingName+"</h3>";
                demoStr=demoStr+"<a href='"+array[i].path+"'>Learn More</a>";
				demoStr=demoStr+"</td>";
				count=count+1;
				}
			demoStr=demoStr+"</tr></table>";
            }
        $('#productsDetail').html(demoStr);
       	}}).done(function(){
   			 	$("#loadingDiv").hide();
  			});
}
    });
    $( window ).load(function() {
     //alert("window loaded");

    });