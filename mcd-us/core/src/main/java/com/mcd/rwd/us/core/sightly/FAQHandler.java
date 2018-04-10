package com.mcd.rwd.us.core.sightly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Listener;
import com.citytechinc.cq.component.annotations.Tab;
import com.citytechinc.cq.component.annotations.widgets.MultiField;
import com.citytechinc.cq.component.annotations.widgets.NumberField;
import com.citytechinc.cq.component.annotations.widgets.PathField;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.icfolson.aem.library.api.page.PageDecorator;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.mcd.rwd.global.core.constants.ApplicationConstants;
import com.mcd.rwd.us.core.bean.Faq;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Handler class for get the properties from dialog and set into the component.
 * Created by prahlad.d on 5/11/2016.
 */
@Component(name = "faq",value = "FAQ Component", actions = { "text: FAQ Component", "-", "editannotate", "copymove", "delete" },
		disableTargeting = true, group = "GWS-Global" , path="/content")
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FAQHandler{

	private static final Logger LOG = LoggerFactory.getLogger(FAQHandler.class);

	@Inject
	private Resource resource;

	@Inject
	private PageDecorator currentPage;

	private static final String PN_FAQ = "faq";

	private static final String PN_QUESTION = "question";

	private static final String PN_ANSWER = "answer";

	@DialogField(fieldLabel = "Title", required = true , fieldDescription = "Please provide the title for FAQ.")
	@TextField
	@Inject
	String title;

	@DialogField(fieldLabel = "Description", required = true , fieldDescription = "Please provide the description for FAQ.")
	@TextField
	@Inject
	String description;

	@DialogField(fieldLabel = "FAQ page paths", fieldDescription = "Please choose or enter any URL for the FAQ pages.")
	@MultiField
	@PathField(rootPath = "/content")
	@Inject
	List<String> paths;

	private Map<String, Map<String, List<Faq>>> faqDataMap;

	@PostConstruct
		public void activate() throws Exception {
		PageManager pageManager = currentPage.getPageManager();

		faqDataMap = new TreeMap<String, Map<String, List<Faq>>>();

		if (null != paths && paths.size() > 0) {
			for (String path : paths) {
				LOG.debug("FAQ Paths Configured {}", path);
				populateQuestions(pageManager.getPage(path));
			}
		}
	}

	private void populateQuestions(Page page) {
		if (null != page && null != page.getContentResource(PN_FAQ)) {
			LOG.debug("Setting questions from the page {}", page.getPath());
			List<Faq> faqList = new ArrayList<Faq>();
			Map<String, List<Faq>> faqData = new TreeMap<String, List<Faq>>();
			Iterator<Resource> childResource = page.getContentResource(PN_FAQ).listChildren();
			while (childResource.hasNext()) {
				Resource qnaResource = childResource.next();
				if (qnaResource.isResourceType("mcd-us/components/content/qna")) {
					Faq question = qnaResource.adaptTo(Faq.class);
					faqList.add(question);
				}
			}

			if (!faqList.isEmpty()) {
				faqData.put(page.getTitle(), faqList);
				this.faqDataMap.put(page.getName(), faqData);
			}
		}
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Map<String, Map<String, List<Faq>>> getFaqDataMap() {
		return faqDataMap;
	}

}
