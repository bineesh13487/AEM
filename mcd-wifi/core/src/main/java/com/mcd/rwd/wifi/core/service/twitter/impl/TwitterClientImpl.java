package com.mcd.rwd.wifi.core.service.twitter.impl;

import twitter4j.Twitter;

import com.mcd.rwd.wifi.core.service.twitter.TwitterClient;
import com.day.cq.wcm.webservicesupport.Configuration;

public final class TwitterClientImpl implements TwitterClient {

    private final Twitter twitter;

    private final com.day.cq.wcm.webservicesupport.Configuration serviceConfiguration;

    public TwitterClientImpl(Twitter impl, com.day.cq.wcm.webservicesupport.Configuration configuration) {
        this.twitter = impl;
        this.serviceConfiguration = configuration;
    }

    @Override
    public Configuration getServiceConfiguration() {
        return serviceConfiguration;
    }

    @Override
    public Twitter getTwitter() {
        return twitter;
    }


}
