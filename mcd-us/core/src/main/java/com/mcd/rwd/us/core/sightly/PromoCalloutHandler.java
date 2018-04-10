package com.mcd.rwd.us.core.sightly;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Listener;
import com.citytechinc.cq.component.annotations.Tab;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.mcd.rwd.us.core.constants.PromoConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Handler for get the promo configuration and set into the promo component.
 * Created by prahlad.d on 6/4/2016.
 */
@Component(name = "promocallout", value = "Promo Component",
		description = "Display the promotion.",
		actions = { "text: Promo Component", "-", "editannotate", "copymove", "delete" },
		disableTargeting = true,
		resourceSuperType = "foundation/components/parbase",
		tabs = {@Tab( touchUINodeName = "fieldConfig" , title = "Promo Settings" )},
		group = " GWS-Global", path = "content",
		listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE"),
				@Listener(name = "afterinsert", value = "REFRESH_PAGE")})

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PromoCalloutHandler {

	@DialogField(name = "./path", fieldLabel = "Path")
	@PathField
	@Inject @Named("path")
	@Default(values = "")
	private String link;

	@Inject
	Resource resource;

	private static final Logger LOG = LoggerFactory.getLogger(PromoCalloutHandler.class);

	private String resourcePath;

	@PostConstruct
	public void init() throws Exception {
		//ValueMap properties = getProperties();
		//String link = properties.get(ApplicationConstants.PN_PATH, String.class);
		LOG.debug("Link configured for Promo {}", link);
		if (StringUtils.isNotBlank(link) && (resource.getResourceResolver().getResource(link + PromoConstants.JCR_CONTENT_PROMO) != null)) {
			this.resourcePath = link + PromoConstants.JCR_CONTENT_PROMO;
			LOG.debug("Promo resource included {}", this.resourcePath);
		}
	}

	public String getResourcePath() {
		return resourcePath;
	}
}
