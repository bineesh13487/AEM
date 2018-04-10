package com.mcd.rwd.wifi.core.service.twitter.impl;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.PropertyUnbounded;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;
import twitter4j.URLEntity;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.mcd.rwd.wifi.core.service.twitter.TwitterFeedUpdater;
import com.mcd.rwd.wifi.core.utils.ConfigurationUtils;

@Component(immediate = true, label = "MCD Wifi - Twitter Feed Update Service",
    metatype = true, description = "Service to update Twitter Stream components.")
@Service
public final class TwitterFeedUpdaterImpl implements TwitterFeedUpdater {

    private static final Logger log = LoggerFactory.getLogger(TwitterFeedUpdaterImpl.class);
    
    @Reference
    private Replicator replicator;

    @Property(value = "mcd-wifi/components/content/twitter-stream", unbounded = PropertyUnbounded.ARRAY,
            label = "Twitter Feed component paths", description = "Component paths for Twitter Feed components.")
    private static final String TWITTER_COMPONENT_PATHS = "twitter.component.paths";

    private String[] twitterComponentPaths = null;

    protected void activate(ComponentContext ctx) {
        final Dictionary<?, ?> props = ctx.getProperties();

        twitterComponentPaths = PropertiesUtil.toStringArray(props.get(TWITTER_COMPONENT_PATHS));
        
    }

    @Override
    public void updateTwitterFeedComponents(ResourceResolver resourceResolver) {
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        List<Resource> twitterResources = findTwitterResources(resourceResolver);
        for (Resource twitterResource : twitterResources) {
            Page page = pageManager.getContainingPage(twitterResource);            
            if (page != null) {
                Twitter client = page.adaptTo(Twitter.class);                
                if (client != null) {
                	twitterAccount(client, twitterResource);
                } else {
                    log.warn("Twitter component found on {}, but page cannot be adapted to Twitter API. Check Cloud SErvice configuration", page.getPath());
                }
            }

        }
    	ConfigurationUtils.closedResourceResolver(resourceResolver); // Closing resource resolver.
    }

    private List<Resource> findTwitterResources(ResourceResolver resourceResolver) {
        List<Resource> twitterResources = new ArrayList<Resource>();

        Map<String, String> predicateMap = new HashMap<String, String>();
        predicateMap.put("path", "/content");
        predicateMap.put("property", "sling:resourceType");

        int i = 1;
        for (String path : twitterComponentPaths) {
            predicateMap.put("property." + (i++) + "_value", path);

        }

        predicateMap.put("p.limit", "-1");

        QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
        Session session = resourceResolver.adaptTo(Session.class);
        Query query = queryBuilder.createQuery(PredicateGroup.create(predicateMap), session);

        SearchResult result = query.getResult();
        Iterator<Resource> resources = result.getResources();
        while (resources.hasNext()) {
            twitterResources.add(resources.next());
        }
        return twitterResources;
    }
    
    private void twitterAccount(Twitter client, Resource twitterResource){
    	try {
    	    ValueMap properties = twitterResource.getValueMap();
    	    String username = properties.get("username", String.class);
    	    int limit = Integer.parseInt(properties.get("limit", String.class));
    	    if (!StringUtils.isEmpty(username)) {
    	        log.info("Loading Twitter timeline for user {} for component {}.", username, twitterResource.getPath());
    	        List < Status > statuses;
    	        List < String > tweetsList = new ArrayList <String> ();
	            List < String > jsonList = new ArrayList < String > (); 
	            int count = 0;
    	        for(int index=1; index<=15; index++){
    	        	Paging paging = new Paging(index, 200);	
    	        	statuses = client.getUserTimeline(username, paging);
    	        	updateTweetList(statuses, count, limit, tweetsList, jsonList);
    	        }                           
                log.info("jsonList Size::::::::::::::::::::" +jsonList.size());                           
                log.info("tweetList Size:::::::::::::::::::::" +tweetsList.size());
                
                readTweets(jsonList, tweetsList, twitterResource);
    	    }
        } catch (TwitterException e) {
            log.error("Exception while loading twitter feed on resource:" + twitterResource.getPath(), e);
        } catch (Exception e){
        	log.error("Exception in reading tweets::",e);
        }
    }
    
    private void updateTweetList(List<Status> statuses, int count, int limit, List<String> tweetsList, List<String> jsonList){
    	for (Status status : statuses) { 
    		if(count < limit){
    			if(status.getInReplyToUserId() == -1){
    				tweetsList.add(processTweet(status));
    				jsonList.add(TwitterObjectFactory.getRawJSON(status));
    			}
    		}else{
    			break;
    		}
    	}
    }
    
    private void readTweets(List < String > jsonList, List < String > tweetsList, Resource twitterResource ){
    	try{
    		ModifiableValueMap map = twitterResource.adaptTo(ModifiableValueMap.class);
            map.put("tweets", tweetsList.toArray(new String[tweetsList.size()]));
            map.put("tweetsJson", jsonList.toArray(new String[jsonList.size()]));
            twitterResource.getResourceResolver().commit();

            handleReplication(twitterResource);
            
    	}catch (PersistenceException e) {
            log.error("Exception while updating twitter feed on resource:" + twitterResource.getPath(), e);
        } catch (ReplicationException e) {
            log.error("Exception while replicating twitter feed on resource:" + twitterResource.getPath(), e);
        }catch (Exception e){
        	log.error("Exception in reading tweets::", e);
        }
    }
    private String processTweet(Status status) {
        String tweet = status.getText();

        for (URLEntity entity : status.getURLEntities()) {
            String url = String.format("<a target=\"_blank\" href=\"%s\">%s</a>", entity.getURL(), entity.getURL());
            tweet = tweet.replace(entity.getURL(), url);
        }
        return tweet;
    }
    /**
     * Twitter Resource replication. When ever the resource is updated the replication will happen. 
     * Note: Make sure the system user/service user is having replicate permission to the path containing the twitter resource. 
     */
    private void handleReplication(Resource twitterResource) throws ReplicationException {
        if (isReplicationEnabled(twitterResource)) {
            Session session = twitterResource.getResourceResolver().adaptTo(Session.class);
            replicator.replicate(session, ReplicationActionType.ACTIVATE, twitterResource.getPath());
        }

    }

    private boolean isReplicationEnabled(Resource twitterResource) {
        ValueMap properties = twitterResource.getValueMap();
        return properties.get("replicate", false);
    }

}
