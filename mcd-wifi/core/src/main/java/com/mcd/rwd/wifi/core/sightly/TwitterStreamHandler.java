package com.mcd.rwd.wifi.core.sightly;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.widgets.CheckBox;
import com.citytechinc.cq.component.annotations.widgets.DialogFieldSet;
import com.citytechinc.cq.component.annotations.widgets.NumberField;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.mcd.rwd.global.core.components.common.Image;
import com.mcd.rwd.wifi.core.constants.ApplicationConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;

/**
 * Created by Vamsi Jetty on 04-19-2016.
 *
 */
@Component(name = "twitter-stream", value = "Twitter Stream",
		tabs = {
				@Tab( touchUINodeName = "twitter" , title = "Twitter" ),
				@Tab( touchUINodeName = "aria" , title = "Accessibility")
		},
		resourceSuperType = "foundation/components/parbase",
		actions = { "text: Twitter Stream", "-", "editannotate", "copymove", "delete" },
		group = "McD-Wifi", path = "content",
		listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE")})

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TwitterStreamHandler {

	@DialogField(fieldLabel = "Default Image")
	@DialogFieldSet(title = "Default Image", namePrefix = "twitter/")
	@ChildResource(name = "twitter")
	@Inject
	private Image image;

	@DialogField(
			fieldLabel = "Default Image Alt Text", required = true,
			name = "./defaultAltText",
			fieldDescription = "Enter image alt text",
			additionalProperties = @Property(name = "key", value = "defaultAltText")
	)
	@TextField
	@Inject @Named("defaultAltText")
	@Default(values = StringUtils.EMPTY)
	private String imagealttext;

	@DialogField(
			fieldLabel = "Username", required = true,
			name = "./username",
			fieldDescription = "This Twitter username whose tweets will be displayed.."
	)
	@TextField
	@Inject @Named("username")
	@Default(values = StringUtils.EMPTY)
	private String userName;

	@DialogField(
			fieldLabel = "Limit", required = true, name = "./limit",
			additionalProperties = {@Property(name = "boxMaxWidth", value = "{Long}30"),
			@Property(name = "maxValue", value = "10"), @Property(name = "minValue", value = "1")},
			fieldDescription = "Number of tweets to be displayed. Maximum limit for twitter feed is 10. "
	)
	@NumberField(max = "10" , min = "1") @Named("limit")
	@Inject @Default(values = StringUtils.EMPTY)
	private String limitVal;

	@DialogField(
			fieldLabel = "Reply Button Title", required = true,
			fieldDescription = "Twitter reply button title tool-tip. "
	)
	@TextField
	@Inject
	@Default(values = StringUtils.EMPTY)
	private String replyTitle;

	@DialogField(
			fieldLabel = "Re-tweet Button Title", required = true,
			fieldDescription = "Twitter re-tweet button title tool-tip. "
	)
	@TextField
	@Inject @Default(values = StringUtils.EMPTY)
	private String retweetTitle;

	@DialogField(
			fieldLabel = "Like Button Title", required = true,
			fieldDescription = "Twitter like/favorite button title tool-tip. "
	)
	@TextField
	@Inject @Default(values = StringUtils.EMPTY)
	private String likeTitle;

	@DialogField(fieldDescription = "Check the box to open in a new tabWhen checked the page containing " +
			"this component is replicated when the feed is updated.", value = "on",
			fieldLabel = "Replicate component?")
	@CheckBox(text = "Replicate component?")
	@Inject
	String replicate;

	@DialogField(
			fieldLabel = "Time-Stamp (Aria-Label) ", required = true, tab = 2,
			fieldDescription = "Provide description for time-stamp on Twitter post. (Ex: Twitter Posted )"
	)
	@TextField
	@Inject @Default(values = StringUtils.EMPTY)
	private String timeStamp;
	private static final Logger log = LoggerFactory.getLogger(TwitterStreamHandler.class);

	@Inject
	private Resource resource;

	private String username = StringUtils.EMPTY;
	private String jsonTweets;
	private String twitterUrl;
	private String tweetUrl;
	private String retweetUrl;
	private String favoriteUrl;
	private String imagePath = StringUtils.EMPTY;
	private String altText = StringUtils.EMPTY;
	private JSONArray tweetJsonArr = null;


	public String getJsonTweets() {
		return jsonTweets;
	}

	public String getUsername() {
		return username;
	}

	@PostConstruct
	public void activate() throws RepositoryException, JSONException {
		Node currentNode = resource.adaptTo(Node.class);
		if(currentNode != null){
			int limit = 0;
			if(StringUtils.isNotEmpty(limitVal)){
				limit = Integer.parseInt(limitVal);
			}
			if(null!=image)
			imagePath = image.getImagePath();
			altText = this.imagealttext;
			username = this.userName;

			if(currentNode.hasProperty(ApplicationConstants.TWEETS_JSON)){
				tweetJsonArr = new JSONArray();
				if(currentNode.getProperty(ApplicationConstants.TWEETS_JSON).isMultiple()){
					Value[] tweetValues = currentNode.getProperty(ApplicationConstants.TWEETS_JSON).getValues();
					updateTwitterJson(tweetValues, limit);
				}else{
					String tweet = currentNode.getProperty(ApplicationConstants.TWEETS_JSON).getString();
					JSONObject tweetJsonObj = new JSONObject(tweet);
					tweetJsonArr.put(tweetJsonObj);
				}
				log.info("tweet json array:::"+tweetJsonArr.length());
				jsonTweets = tweetJsonArr.toString();
				setTwitterUrl(ApplicationConstants.TWITTER_URL);
				setTweetUrl(ApplicationConstants.TWITTER_TWEET_URL);
				setRetweetUrl(ApplicationConstants.TWITTER_RETWEET_URL);
				setFavoriteUrl(ApplicationConstants.TWITTER_FAVORITE_URL);
			}
		}
	}

	private void updateTwitterJson(Value[] tweetValues, int limit){
		try {
			for(int index=0; index<tweetValues.length; index++){
				if(index<limit){
					String tweet = tweetValues[index].getString();
					JSONObject tweetJsonObj = new JSONObject(tweet);
					tweetJsonArr.put(tweetJsonObj);
				}
				else{
					break;
				}
			}
		} catch (JSONException e) {
			log.error("JSONException in updateTwitterJson method :: "+e);
		} catch (ValueFormatException e) {
			log.error("ValueFormatException in updateTwitterJson method :: "+e);
		} catch (IllegalStateException e) {
			log.error("IllegalStateException in updateTwitterJson method :: "+e);
		} catch (RepositoryException e) {
			log.error("RepositoryException in updateTwitterJson method :: "+e);
		}
	}
	public String getTwitterUrl() {
		return twitterUrl;
	}


	public void setTwitterUrl(String twitterUrl) {
		this.twitterUrl = twitterUrl;
	}


	public String getTweetUrl() {
		return tweetUrl;
	}


	public void setTweetUrl(String tweetUrl) {
		this.tweetUrl = tweetUrl;
	}


	public String getRetweetUrl() {
		return retweetUrl;
	}


	public void setRetweetUrl(String retweetUrl) {
		this.retweetUrl = retweetUrl;
	}


	public String getFavoriteUrl() {
		return favoriteUrl;
	}


	public void setFavoriteUrl(String favoriteUrl) {
		this.favoriteUrl = favoriteUrl;
	}


	public String getImagePath() {
		return imagePath;
	}

	public String getAltText() {
		return altText;
	}

	public String getTimeStamp() {
		return timeStamp;
	}
}
