package com.mcd.rwd.us.core.bean;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.Switch;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Created by apple on 29/06/17.
 */
@Model(adaptables = { Resource.class ,SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NutritionListPOJO {

    private static final String MEASUREMENT_UNIT_FLAG = "false";

    @DialogField( fieldLabel="Primary Nutrient" , required = true)
    @Selection(optionsProvider = "$PATH.nutrientsList.json?comp=nutrition-calculator", dataSource = "mcd-us/dataSource/nutrients", type = Selection.SELECT)
    @Inject
    private String nutrientList;

    @DialogField(fieldLabel="Display Name", name = "./nutrientMarketingName")
    @TextField
    @Inject
    private String marketingName;

    @DialogField(fieldLabel="Please check the check-box to hide Adult DV Unit" , defaultValue="true", value="true")
    @Switch
    @Inject
    private String percentageUnitFlag;

    private String id;

    @PostConstruct
    public void postConstruct() {
        if (nutrientList != null) {
            final String[] parts = nutrientList.split("_");
            id = parts[0];
        }
    }

    public String getMarketingName() {
        return marketingName;
    }

    public void setMarketingName(String marketingName) {
        this.marketingName = marketingName;
    }

    public String getPercentageUnitFlag() {
        return percentageUnitFlag;
    }

    public String getMeasurementUnitFlag() {
        return MEASUREMENT_UNIT_FLAG;
    }

    public String getId() {
        return id;
    }
}
