package com.mcd.rwd.us.core.bean.search;

import java.util.List;

/**
 * Created by Seema Pandey on 26-05-2016.
 */
public final class Response {

	private long numFound;

	private long start;

	private double maxScore;

	private List<Document> docs;

	public long getNumFound() {
		return numFound;
	}

	public void setNumFound(final long numFound) {
		this.numFound = numFound;
	}

	public long getStart() {
		return start;
	}

	public void setStart(final long start) {
		this.start = start;
	}

	public double getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(final double maxScore) {
		this.maxScore = maxScore;
	}

	public List<Document> getDocs() {
		return docs;
	}

	public void setDocs(final List<Document> docs) {
		this.docs = docs;
	}
}
