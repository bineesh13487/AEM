package com.mcd.rwd.global.core.models;

import org.apache.commons.lang.StringUtils;
import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Option;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.widgets.CheckBox;
import com.citytechinc.cq.component.annotations.widgets.MultiField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rakesh.Balaiah on 07-04-2016.
 */

@Component(
		name = "columncontrol",
		path = "content",
		value = "Column Control",
		description = "Provides the ability to divide a paragraph into multiple paragraphs.",
		actions = {"text: Column Control","-","edit","copymove","delete"},
		isContainer = true,
		disableTargeting = true,
		resourceSuperType = "foundation/components/parbase",
		allowedParents = {"*/parsys"},
		group = " GWS-Global"
)
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ColumnControl {

	@DialogField(fieldLabel = "Column Widths", fieldDescription = "Choose the width of each column from the dropdown." +
			" Add and remove any number of columns using '+' and '-' button respectively.", additionalProperties = {
			@Property(name = "emptyOption", value = "true"), @Property(name = "class", value = "col-cntrl")})
	@MultiField
	@Selection(type = Selection.SELECT, options = { @Option(text = "~8%", value = "1"),
			@Option(text = "~17%", value = "2"), @Option(text = "~25%", value = "3"),
			@Option(text = "~33%", value = "4"), @Option(text = "~42%", value = "5"),
			@Option(text = "~50%", value = "6"), @Option(text = "~58%", value = "7"),
			@Option(text = "~67%", value = "8"), @Option(text = "~75%", value = "9"),
			@Option(text = "~83%", value = "10"), @Option(text = "~92%", value = "11"),
			@Option(text = "100%", value = "12")
	})
	@Inject
	private int[] columns;

	@DialogField(fieldDescription = "Includes gutter space between the columns.",
			additionalProperties = @Property(name = "value", value = "true"))
	@CheckBox(text = "Enable Gutter Space")
	@Inject @Default(booleanValues = false)
	private boolean enableGutters;

	@DialogField(fieldDescription = "Enable left right margin for columns. Do not use this option " +
			"in case this column control is being nested within another column control.",
			additionalProperties = @Property(name = "value", value = "true"))
	@CheckBox(text = "Enable Margin Space")
	@Inject @Default(booleanValues = false)
	private boolean enableMargin;

	private List<String> classes;

	@Inject @Optional
	private String gutterSpaceValue = StringUtils.EMPTY;

	@PostConstruct
	protected void init() {
		if (null != columns && columns.length > 0) {
			classes = new ArrayList<String>();
			for (int i = 0; i < columns.length; i++) {
				classes.add("col-sm-" + columns[i] + " col-md-" + columns[i]);
			}
		}
	}

	public boolean isEnableGutters() {
		return this.enableGutters;
	}

	public boolean isEnableMargin() {
		return enableMargin;
	}

	public List<String> getClasses() {
		return this.classes;
	}

	public String getGutterSpaceValue() {
		return (gutterSpaceValue+"px");
	}

}
