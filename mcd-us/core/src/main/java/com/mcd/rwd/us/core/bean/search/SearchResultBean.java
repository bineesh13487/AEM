package com.mcd.rwd.us.core.bean.search;

import java.util.Map;

/**
 * Created by Seema Pandey on 26-05-2016.
 */
public final class SearchResultBean {

	private ResponseHeader responseHeader;

	private Response response;

	private Map<String, Match> highlighting;

	public ResponseHeader getResponseHeader() {
		return responseHeader;
	}

	public void setResponseHeader(final ResponseHeader responseHeader) {
		this.responseHeader = responseHeader;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(final Response response) {
		this.response = response;
	}

	public Map<String, Match> getHighlighting() {
		return highlighting;
	}

	public void setHighlighting(final Map<String, Match> highlighting) {
		this.highlighting = highlighting;
	}

}
