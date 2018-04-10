package com.mcd.rwd.us.core.sightly;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.mcd.rwd.global.core.sightly.McDUse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by deepti_b on 13-02-2017.
 */
public class RedirectHandler extends McDUse {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedirectHandler.class);

    private String dealsPageUrl;

    private String coopInUrl;

    private Set tagSet= new HashSet();;

    private String coopOutUrl;

    @Override public void activate() throws Exception {
        Page currentPage = getCurrentPage();
        dealsPageUrl = addLinktoUrl(getProperties().get("dealPageUrl", String.class));
        coopInUrl = addLinktoUrl(getProperties().get("coopInUrl", String.class));
        coopOutUrl = addLinktoUrl(getProperties().get("coopOutUrl", String.class));
        String [] tags = getProperties().get("coops", String[].class);
        addTagSet(tagSet,tags);
      //  addPageTags(tagSet,currentPage.getTags());
    }

    private String addLinktoUrl(String redirectLink){
        if (null != redirectLink){
            redirectLink = redirectLink.contains("/content") ? redirectLink + ".html" : redirectLink;
        }
        return redirectLink;
    }

    private void addPageTags(Set tagSet, Tag[] tags) {
        if(tags.length > 0) {
            for (int k = 0; k < tags.length; k++) {
                tagSet.add(tags[k].getName());
            }
        }
    }
    private void addTagSet(Set tagSet, String[] tags) {
        TagManager tm = getResource().getResourceResolver().adaptTo(TagManager.class);
        if (tags!=null) {
            for (int k=0; k<tags.length; k++) {
                Tag t = tm.resolve(tags[k]);
                if (t!=null) {
                    tagSet.add(t.getName());
                }
            }
        }
        LOGGER.info("-----Coop Configured in redirect page are-----"+tagSet.size());
    }
    public String getDealsPageUrl() {
        return dealsPageUrl;
    }

    public String getCoopInUrl() {
        return coopInUrl;
    }

    public String getCoopOutUrl() {
        return coopOutUrl;
    }

    public Set getTagSet() {
        return tagSet;
    }
}
