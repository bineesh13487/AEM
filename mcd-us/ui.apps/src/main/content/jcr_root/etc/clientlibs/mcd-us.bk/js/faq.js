$('.faqBox-0.question-panel').hide();
$( "#topicList a" ).on( "click", function() {
	var d = $(this).text();
	var c = $(this).attr("class").split("-")[1];
	$('.question-panel').hide();
	$('.faqBox-'+c+'.question-panel').show();
    if(d==="All"){
		$('.question-panel').show();
    }
});