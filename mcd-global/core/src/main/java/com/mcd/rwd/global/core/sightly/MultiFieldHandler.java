package com.mcd.rwd.global.core.sightly;

import com.adobe.cq.sightly.WCMUsePojo;
import com.mcd.rwd.global.core.utils.MultiFieldPanelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 *
 */
public final class MultiFieldHandler extends WCMUsePojo {
	private static final Logger log = LoggerFactory.getLogger(MultiFieldHandler.class);

	private List<Map<String, String>> results;

	@Override public void activate() throws Exception {
		String name = get("name", String.class);
		log.error("Name Received {}", name);
		setResults(MultiFieldPanelUtil.getMultiFieldPanelValues(getResource(), name));
	}

	public List<Map<String, String>> getResults() {
		return results;
	}

	public void setResults(List<Map<String, String>> results) {
		this.results = results;
	}
}