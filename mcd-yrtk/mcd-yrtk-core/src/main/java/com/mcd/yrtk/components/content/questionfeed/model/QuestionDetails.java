package com.mcd.yrtk.components.content.questionfeed.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class QuestionDetails {

    private boolean hasPicture;
    private String heroimage;
    private String alternateTitle;
    private long questionId;
    private long categoryId;
    private String answer;
    private String type;
    private String country;
    private String categoryName;
    private String videourl;
    private boolean hasVideo;
    private String alternateUrl;
    private String shortUrl;
    private String answerId;
    private String question;
    private String language;
    private List<UserDetails> user;
    private String thumbnailimage;

    public boolean isHasPicture() {
        return hasPicture;
    }

    public void setHasPicture(final boolean hasPicture) {
        this.hasPicture = hasPicture;
    }

    public String getHeroimage() {
        return heroimage;
    }

    public void setHeroimage(final String heroimage) {
        this.heroimage = heroimage;
    }

    public String getAlternateTitle() {
        return alternateTitle;
    }

    public void setAlternateTitle(final String alternateTitle) {
        this.alternateTitle = alternateTitle;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(final long questionId) {
        this.questionId = questionId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(final long categoryId) {
        this.categoryId = categoryId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(final String answer) {
        this.answer = answer;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(final String categoryName) {
        this.categoryName = categoryName;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(final String videourl) {
        this.videourl = videourl;
    }

    public boolean isHasVideo() {
        return hasVideo;
    }

    public void setHasVideo(final boolean hasVideo) {
        this.hasVideo = hasVideo;
    }

    public String getAlternateUrl() {
        return alternateUrl;
    }

    public void setAlternateUrl(final String alternateUrl) {
        this.alternateUrl = alternateUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(final String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(final String answerId) {
        this.answerId = answerId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(final String question) {
        this.question = question;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    public List<UserDetails> getUser() {
        return user;
    }

    public void setUser(final List<UserDetails> user) {
        this.user = user;
    }

    public String getThumbnailimage() {
        return thumbnailimage;
    }

    public void setThumbnailimage(final String thumbnailimage) {
        this.thumbnailimage = thumbnailimage;
    }
}
