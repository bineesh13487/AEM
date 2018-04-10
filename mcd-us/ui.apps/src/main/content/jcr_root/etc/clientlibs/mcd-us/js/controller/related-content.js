function createDynamicRelatedContent(){
    if($('.relatedcontent').length > 1){                
    
        $('.relatedcontent .text-wrapper .headline-text-large').each(function(index){
                $(this).attr('id', 'related-content' + (index+1) );                
        })
        $('.relatedcontent .rc-box-inner > a.img-link').each(function(index){
                $(this).attr('aria-describedby', 'related-content' + (index+1) );
        })
        $('.relatedcontent .text-wrapper > a.btn').each(function(index){
                $(this).attr('aria-describedby', 'related-content' + (index+1) );
        })        
    }                
}

$(window).load(function() {
	createDynamicRelatedContent();
});
