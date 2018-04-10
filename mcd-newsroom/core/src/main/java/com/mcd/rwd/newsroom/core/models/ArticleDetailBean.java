package com.mcd.rwd.newsroom.core.models;

public class ArticleDetailBean {

	private String articleTitle;
	private String articleMinTitle;
	private String publishDate;
	private String articleDescription;
	private String articleImagePath;
	private String articlePagePath;	
	private String articleComponentPath;
    private long totalMatches;
	private boolean isResourceExists;
	private boolean isArticlePar;
	private String articleParPath;
	
	public String getArticleTitle() {
		return articleTitle;
	}
	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	public String getArticleDescription() {
		return articleDescription;
	}
	public void setArticleDescription(String articleDescription) {
		this.articleDescription = articleDescription;
	}
	public String getArticleImagePath() {
		return articleImagePath;
	}
	public void setArticleImagePath(String articleImagePath) {
		this.articleImagePath = articleImagePath;
	}
	public String getArticlePagePath() {
		return articlePagePath;
	}
	public void setArticlePagePath(String articlePagePath) {
		this.articlePagePath = articlePagePath;
	}
	public String getArticleComponentPath() {
		return articleComponentPath;
	}
	public void setArticleComponentPath(String articleComponentPath) {
		this.articleComponentPath = articleComponentPath;
	}

	public boolean isResourceExists() {
		return isResourceExists;
	}

	public String getArticleMinTitle() {
		return articleMinTitle;
	}

	public void setArticleMinTitle(String articleMinTitle) {
		this.articleMinTitle = articleMinTitle;
	}

	public void setResourceExists(boolean resourceExists) {
		isResourceExists = resourceExists;
	}
	public long getTotalMatches() {
		return totalMatches;
	}
	public void setTotalMatches(long totalMatches) {
		this.totalMatches = totalMatches;
	}
	public boolean isArticlePar() {
		return isArticlePar;
	}
	public void setArticlePar(boolean isArticlePar) {
		this.isArticlePar = isArticlePar;
	}
	public String getArticleParPath() {
		return articleParPath;
	}
	public void setArticleParPath(String articleParPath) {
		this.articleParPath = articleParPath;
	}

}
