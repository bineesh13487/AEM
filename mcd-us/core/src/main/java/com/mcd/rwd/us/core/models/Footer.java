package com.mcd.rwd.us.core.models;

import com.mcd.rwd.us.core.bean.Link;
import com.mcd.rwd.us.core.bean.SocialChannel;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Created by sandeepc on 21/06/17.
 */

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public interface Footer {

    @Inject @Named("appbadgetitle")
    String getAppbadgetitle();

    @Inject @Named("socialchanneltitle")
    String getSocialchanneltitle();

    @Inject @Named("showappbadge")
    Boolean getShowappbadge();

    @Inject @Named("socialchannels")
    String[] getSocialchannels();

    @Inject @Named("channels")
    String[] getChannels();

    @Inject @Named("copyright")
    String getCopyright();

    @Inject @Named("aria")
    String getAria();

}
