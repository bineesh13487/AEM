package com.mcd.rwd.us.core.bean;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by prahlad.d on 5/16/2016.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public interface Faq {

    @Inject @Named("question")
    String getQuestion();

    @Inject @Named("answer")
    String getAnswer();

    @Inject @Named("topicId")
    String getTopicId();

}
