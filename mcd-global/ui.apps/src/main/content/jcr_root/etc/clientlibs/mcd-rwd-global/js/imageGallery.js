function imageGallOnLoad(selection,value){
	//selection.findParentByType('tabpanel').manageTabs(selection.getValue(),true);
	var dialog = selection.findParentByType('dialog');
	var galleryType = dialog.getField('./galleryType').getValue();
	var thumbnailPosition = dialog.getField('./thumbPosition');
	var animationType = dialog.getField('./animation');
	var translation = dialog.getField('./transition');
	if(galleryType==='thumbnail') {
		thumbnailPosition.show();
	}else{
		thumbnailPosition.hide();
	}
	if(galleryType==='showcase'){
		animationType.hide();
		translation.show();
	}else{
		animationType.show();
		if(animationType.getValue()==='slide'){
			translation.show();
		}else{
			translation.hide();
		}
	}
}

function imageGallOnSelect(box,value){
	//box.findParentByType('tabpanel').manageTabs(value);
	var dialog = box.findParentByType('dialog');
	var thumbnailPosition = dialog.getField('./thumbPosition');
	var animationType = dialog.getField('./animation');
	var translation = dialog.getField('./transition');
	if(value==='thumbnail') {
		thumbnailPosition.show();
	}else{
		thumbnailPosition.hide();
	}
	if(value==='showcase'){
		animationType.hide();
		translation.show();
	}else{
		animationType.show();
		if(animationType.getValue()==='slide'){
			translation.show();
		}else{
			translation.hide();
		}
	}

}

function animateOnSelect(field,value){
    dlg = field.findParentByType('dialog');
    if(value=='fade'){
        dlg.getField('./transition').hide();
    }else{
        dlg.getField('./transition').show();
    }
}

function renderData(selection, value, itemVal) {
	selection.findParentByType('tabpanel').hideTabStripItem(itemVal);
}
