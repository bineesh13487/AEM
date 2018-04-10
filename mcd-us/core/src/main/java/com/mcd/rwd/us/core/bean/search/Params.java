package com.mcd.rwd.us.core.bean.search;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Seema Pandey on 26-05-2016.
 */
public final class Params {

	@SerializedName("hl.fragsize") private String hlFragmentSize;

	private String enableElevation;

	private String exclusive;

	@SerializedName("hl.simple.pre") private String hlSimplePrefix;

	@SerializedName("hl.fl") private List<String> hlFl;

	private String wt;

	private String hl;

	private String rows;

	private String fl;

	@SerializedName("hl.snippets") private String hlSnippets;

	private String start;

	@SerializedName("hl.simple.post") private String hlSimplePostfix;

	public String getHlFragmentSize() {
		return hlFragmentSize;
	}

	public void setHlFragmentSize(final String hlFragmentSize) {
		this.hlFragmentSize = hlFragmentSize;
	}

	public String getEnableElevation() {
		return enableElevation;
	}

	public void setEnableElevation(final String enableElevation) {
		this.enableElevation = enableElevation;
	}

	public String getExclusive() {
		return exclusive;
	}

	public void setExclusive(final String exclusive) {
		this.exclusive = exclusive;
	}

	public String getHlSimplePrefix() {
		return hlSimplePrefix;
	}

	public void setHlSimplePrefix(final String hlSimplePrefix) {
		this.hlSimplePrefix = hlSimplePrefix;
	}

	public List<String> getHlFl() {
		return hlFl;
	}

	public void setHlFl(final List<String> hlFl) {
		this.hlFl = hlFl;
	}

	public String getWt() {
		return wt;
	}

	public void setWt(final String wt) {
		this.wt = wt;
	}

	public String getHl() {
		return hl;
	}

	public void setHl(final String hl) {
		this.hl = hl;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(final String rows) {
		this.rows = rows;
	}

	public String getFl() {
		return fl;
	}

	public void setFl(final String fl) {
		this.fl = fl;
	}

	public String getHlSnippets() {
		return hlSnippets;
	}

	public void setHlSnippets(final String hlSnippets) {
		this.hlSnippets = hlSnippets;
	}

	public String getStart() {
		return start;
	}

	public void setStart(final String start) {
		this.start = start;
	}

	public String getHlSimplePostfix() {
		return hlSimplePostfix;
	}

	public void setHlSimplePostfix(final String hlSimplePostfix) {
		this.hlSimplePostfix = hlSimplePostfix;
	}
}
