package com.mcd.rwd.us.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Rakesh.Balaiah on 11-04-2016.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public interface FooterNav {

	public static final String AUTO = "auto";

	@Inject @Default(values = AUTO) String getListType();

	@Inject String getRootPath();

	@Inject List<String> getCustomList();

}
