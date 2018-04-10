package com.mcd.yrtk.components.content.testcomponent

import com.citytechinc.cq.component.annotations.DialogField
import com.citytechinc.cq.component.annotations.widgets.TextField
import com.icfolson.aem.library.core.components.AbstractComponent
import com.mcd.yrtk.annotations.AEMComponent

import javax.inject.Inject

@AEMComponent("Test Component")
class TestComponent extends AbstractComponent {

	@DialogField(fieldLabel = "Test Title", fieldDescription = "This is the test title.")
	@TextField
    @Inject
    String title
}
