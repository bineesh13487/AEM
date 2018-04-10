package com.mcd.rwd.global.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Rakesh.Balaiah on 10-05-2016.
 */
@Model(adaptables = Resource.class)
public class Bumper {

	@Inject @Optional @Default(booleanValues = false)
	private boolean enabled;

	@Inject @Optional
	private String title;

	@Inject @Optional
	private String text;

	@Inject @Optional @Default(values = "OK")
	private String btnOk;

	@Inject @Optional @Default(values = "CANCEL")
	private String btnCancel;

	@Inject @Optional
	private List<String> ignore;

	public boolean isEnabled() {
		return enabled;
	}

	public String getTitle() {
		return title;
	}

	public String getText() {
		return text;
	}

	public String getBtnOk() {
		return btnOk;
	}

	public String getBtnCancel() {
		return btnCancel;
	}

	public List<String> getIgnore() {
		return ignore;
	}
}
