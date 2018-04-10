package com.mcd.rwd.us.core.bean.search;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Property;
import com.citytechinc.cq.component.annotations.widgets.CheckBox;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Seema Pandey on 26-05-2016.
 */

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public final class SearchFilter {

	@DialogField(fieldLabel = "Filter (Page Type)", name = "type")
	@Selection(type = Selection.SELECT, optionsProvider = "$PATH.pagetype.json", optionsUrl = "$PATH.pagetype.json",
			dataSource = "mcd-us/datasource/search")
	@Inject
	@Named("type")
	private String type;

	@DialogField(fieldLabel = "Link (Optional, to be provided if need to redirect to external URL)", name = "path",
			additionalProperties = @Property(name = "width", value = "400"))
	@PathField
	@Inject @Named("path")
	private String path;

	@DialogField(fieldLabel = "Open in New Window?", fieldDescription = "Check this if the URL is provided," +
			" to load page in a new window", name = "target", additionalProperties =
			{@Property(name = "width", value = "100"), @Property(name = "value", value = "true")})
	@CheckBox(text = "Open in New Window?")
	@Inject @Named("target")
	private String target;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
}
