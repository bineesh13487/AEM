package com.mcd.rwd.us.core.bean.search;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Seema Pandey on 26-05-2016.
 */
public class ResponseHeader {

	private int status;

	@SerializedName("QTime")
	private int qTime;

	private Params params;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getQTime() {
		return qTime;
	}

	public void setQTime(int qTime) {
		this.qTime = qTime;
	}

	public Params getParams() {
		return params;
	}

	public void setParams(Params params) {
		this.params = params;
	}
}
