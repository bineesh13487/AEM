package com.mcd.rwd.us.core.bean;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Option;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = { Resource.class ,SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SecondaryNutrientListPOJO {

    private static final String FLAG_TRUE = "true";
    private static final String FLAG_FALSE = "false";

    private static final String BOTH_UNIT = "bothUnit";
    private static final String MEASUREMENT_UNIT = "measurementUnit";
    private static final String PERCENTAGE_UNIT = "percentageUnit";

    @DialogField( fieldLabel="Secondary Nutrient", required = true)
    @Selection(optionsProvider = "$PATH.nutrientsList.json?comp=nutrition-calculator", type = Selection.SELECT, dataSource = "mcd-us/dataSource/nutrients")
    @Inject
    private String secondaryNutrientList;

    @DialogField(fieldLabel="Display Name")
    @TextField
    @Inject
    private String marketingName;

    @DialogField(fieldLabel="Please select the measurement unit to be displayed" ,  value="bothUnit")
    @Selection(options = {
            @Option(text = "Value and Adult DV Unit", value = BOTH_UNIT),
            @Option(text = "Value Unit", value = MEASUREMENT_UNIT),
            @Option(text = "Adult DV Unit", value = PERCENTAGE_UNIT)
    }, type = Selection.SELECT)
    @Inject
    private String unitFlag;

    private String id;

    private String percentageUnitFlag = FLAG_FALSE;

    private String measurementUnitFlag = FLAG_FALSE;

    @PostConstruct
    public void postConstruct() {
        if (secondaryNutrientList != null) {
            String[] parts = secondaryNutrientList.split("_");
            id = parts[0];
        }
        if (unitFlag != null) {
            switch (unitFlag) {
                case BOTH_UNIT:
                    percentageUnitFlag = FLAG_TRUE;
                    measurementUnitFlag = FLAG_TRUE;
                    break;
                case PERCENTAGE_UNIT:
                    percentageUnitFlag = FLAG_TRUE;
                    break;
                case MEASUREMENT_UNIT:
                    measurementUnitFlag = FLAG_TRUE;
            }
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
        return measurementUnitFlag;
    }

    public String getId() {
        return id;
    }

}
