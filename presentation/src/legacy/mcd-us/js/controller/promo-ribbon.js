function PromoRibbonHeight() {	
	var promo_ribbon_Height = $('.ribbon-inner:eq(2) img').height();	
	$('.ribbon-inner img').height(promo_ribbon_Height);	
}

function createDynamicIdPromoRibbon(){
    if($('.promoRibbon').length > 1){                
    
        $('.promoRibbon .text-wrapper .ribbon-text-large').each(function(index){
                $(this).attr('id', 'promo-ribbon' + (index+1) );                
        })
        $('.promoRibbon .ribbon-relative > a.img-link').each(function(index){
                $(this).attr('aria-describedby', 'promo-ribbon' + (index+1) );
        })
        $('.promoRibbon .text-wrapper > a.btn').each(function(index){
                $(this).attr('aria-describedby', 'promo-ribbon' + (index+1) );
        })
        
    }                
}

$(window).on('resize',function(){
	if(window.innerWidth > 768){
		PromoRibbonHeight();
	}		
});

$(window).load(function() {
   if(window.innerWidth > 768){
		PromoRibbonHeight();
   }
   createDynamicIdPromoRibbon();
});

