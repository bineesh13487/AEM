
/* Fix for tab component to handle authoring mode */
function handleAuthoring(elem) {
    if(CQ && CQ.WCM) {
        $(elem).each(function(){
            var _this = this;
            if(CQ.WCM.areEditablesReady()) {
				toggleParagraphs(_this);
            } else {
				CQ.WCM.on("editablesready", function(){
					toggleParagraphs(_this);
                });
            }
        });
    }
}

/* Hides the paragraphs of the hidden tabs and shows only the active content */
function toggleParagraphs(comp) {
    var par = CQ.WCM.getEditable($(comp).data('editable-path'));

    if(par) {
    	if($(comp).hasClass('active')) {
        	par.show();
        } else {
    		par.hide();
    	}
    }
}