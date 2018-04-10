package com.mcd.rwd.us.core.bean.search;

import java.util.List;

/**
 * Created by Seema Pandey on 26-05-2016.
 */
public final class SearchResults {

	private List<Hit> hits;

	private boolean isPaginated;

	private long totalHits;

	public List<Hit> getHits() {
		return hits;
	}

	public void setHits(List<Hit> hits) {
		this.hits = hits;
	}

	public boolean isPaginated() {
		return isPaginated;
	}

	public void setPaginated(boolean paginated) {
		isPaginated = paginated;
	}

	public long getTotalHits() {
		return totalHits;
	}

	public void setTotalHits(long totalHits) {
		this.totalHits = totalHits;
	}
}
