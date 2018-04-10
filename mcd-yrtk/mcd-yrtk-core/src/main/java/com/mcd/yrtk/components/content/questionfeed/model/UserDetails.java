package com.mcd.yrtk.components.content.questionfeed.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class UserDetails {

    @JsonProperty(value = "avatar_url")
    private String avatarUrl;
    @JsonProperty(value = "profile_url")
    private String profileUrl;
    private String location;
    private String name;
    @JsonProperty(value = "hasAvtar")
    private boolean hasAvatar;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(final String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(final String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public boolean isHasAvatar() {
        return hasAvatar;
    }

    public void setHasAvatar(final boolean hasAvatar) {
        this.hasAvatar = hasAvatar;
    }
}
