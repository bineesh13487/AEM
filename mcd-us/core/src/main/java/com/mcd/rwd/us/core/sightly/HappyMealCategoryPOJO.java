package com.mcd.rwd.us.core.sightly;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

/**
 * Created by sandeepc on 29/06/17.
 */

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HappyMealCategoryPOJO {

    @DialogField(fieldLabel = "Category", required = true, additionalProperties = {
            @Property(name = "comp", value = "happy-meal")
    })
    @Selection(type = Selection.SELECT, dataSource = "mcd-us/dataSource/happyMeal",
            optionsProvider = "$PATH.categorylist.json?comp=happy-meal")
    @Inject
    private String categoryList;

    @DialogField(fieldLabel = "Display Name")
    @TextField
    @Inject
    private String categoryMarketingName;

    public String getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(String categoryList) {
        this.categoryList = categoryList;
    }

    public String getCategoryMarketingName() {
        return categoryMarketingName;
    }

    public void setCategoryMarketingName(String categoryMarketingName) {
        this.categoryMarketingName = categoryMarketingName;
    }
}
