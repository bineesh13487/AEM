package com.mcd.rwd.us.core.bean;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

@Model(adaptables = { Resource.class ,SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public interface CategoryListPOJO {

    @DialogField( fieldLabel="Category")
    @Selection(optionsProvider = "$PATH.categorylist.json?comp=nutrition-calculator", dataSource = "mcd-us/dataSource/nutrition-calculator", type = Selection.SELECT)
    @Inject
    String getCategoryList();

    @DialogField(fieldLabel="Display Name")
    @TextField
    @Inject
    String getCategoryMarketingName();

    @DialogField(fieldLabel="Category Image path" , required = true)
    @PathField(rootPath="/content/dam")
    @Inject
    String getCategoryImagePath();


}
