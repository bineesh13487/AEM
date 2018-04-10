/**
 * 
 */
package com.mcd.rwd.wifi.core.sightly;

import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.widgets.CheckBox;
import com.citytechinc.cq.component.annotations.widgets.NumberField;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.mcd.rwd.wifi.core.constants.ApplicationConstants;
import com.mcd.rwd.wifi.core.service.instagram.impl.InstagramStreamImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Vamsi Jetty on 04-29-2016.
 *
 */

@Component(name = "instagram-stream", value = "Instagram Stream",
		tabs = {
				@Tab( touchUINodeName = "instagram" , title = "Instagram" ),
				@Tab( touchUINodeName = "aria" , title = "Accessibility")
		},
		resourceSuperType = "foundation/components/parbase",
		actions = { "text: Instagram Stream", "-", "editannotate", "copymove", "delete" },
		group = "McD-Wifi", path = "content",
		listeners = {@Listener(name = "afteredit", value = "REFRESH_PAGE")})

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class InstagramStreamHandler{

	@DialogField(
			fieldLabel = "Username", required = true,
			fieldDescription = "This Instagram username whose posts will be displayed."
	)
	@TextField
	@Inject @Default(values = StringUtils.EMPTY)
	private String userName;

	@DialogField(
			fieldLabel = "Limit", required = true,
			name = "./numberOfPosts", additionalProperties = {@Property(name = "boxMaxWidth", value = "{Long}30"),
				@Property(name = "maxValue", value = "20"), @Property(name = "minValue", value = "1")},
			fieldDescription = "Number of posts to be displayed. 'Recommended number is 4'"
	)
	@NumberField(max = "20" , min = "1")
	@Inject @Named("numberOfPosts")
	@Default(values = StringUtils.EMPTY)
	private String limit;

	@DialogField(
			fieldLabel = "Alt Text", required = true,
			fieldDescription = "Alt text for the instagram."
	)
	@TextField
	@Inject @Default(values = StringUtils.EMPTY)
	private String altText;

	@DialogField(
			fieldLabel = "Comment Alt Text", required = true,
			fieldDescription = "Alt text for Instagram comment icon."
	)
	@TextField
	@Inject @Default(values = StringUtils.EMPTY)
	private String altComment;

	@DialogField(
			fieldLabel = "Like Alt Text", required = true,
			fieldDescription = "Alt text for Instagram like icon."
	)
	@TextField
	@Inject @Default(values = StringUtils.EMPTY)
	private String altLike;

	@DialogField(
			fieldLabel = "Comments Link (Aria-Label)", required = true, tab = 2,
			fieldDescription = "Provide description for Comments Link. (Ex: Instagram Comments)"
	)
	@TextField
	@Inject @Default(values = StringUtils.EMPTY)
	private String commentLink;

	@DialogField(
			fieldLabel = "Likes Link (Aria-Label)", required = true,
			name = "./likesLink", tab = 2,
			fieldDescription = "Provide description for Likes Link. (Ex: Instagram Likes)"
	)
	@TextField
	@Inject @Named("likesLink")
	@Default(values = StringUtils.EMPTY)
	private String likeLink;

	@DialogField(
			fieldLabel = "Time-Stamp (Aria-Label) ", required = true, tab = 2,
			fieldDescription = "Provide description for time-stamp on Instagram post. (Ex: Instagram Posted )"
	)
	@TextField
	@Inject @Default(values = StringUtils.EMPTY)
	private String timeStamp;

	@DialogField(
			fieldLabel = "Modal Close (Aria-Label)", required = true, tab = 2,
			fieldDescription = "Please provide aria-label for close button in modal. (Ex: Close)"
	)
	@TextField
	@Inject @Default(values = StringUtils.EMPTY)
	private String modalClose;

	@DialogField(fieldDescription = "When checked the page containing this component is" +
			" replicated when the feed is updated.", value = "on",
			fieldLabel = "Replicate Page?",
		additionalProperties = @Property(name = "deleteHint", value = "{Boolean}true"))
	@CheckBox(text = "Replicate Page?")
	@Inject
	String replicate;

	@OSGiService
	InstagramStreamImpl instagramStream;

/*	@Inject
	SlingScriptHelper slingScriptHelper;*/

	private static final Logger log = LoggerFactory.getLogger(InstagramStreamHandler.class);
	private String instagramPosts;
	private String instagramUrl;
	private String numberOfPosts = StringUtils.EMPTY;
	private String likesLink = StringUtils.EMPTY;


	public String getInstagramPosts() {
		return instagramPosts;
	}

	@PostConstruct
	public void init() {
		/*Node currentNode = getResource().adaptTo(Node.class);
		String userName = "";
		if(currentNode != null && currentNode.hasProperty("userName")){
			userName = currentNode.getProperty("userName").getString();
		}*/
		numberOfPosts = this.limit;
		likesLink = this.likeLink;
		instagramUrl = ApplicationConstants.INSTAGRAM_URL +userName/*+ ApplicationConstants.INSTAGRAM_MEDIA*/;
		//InstagramStreamImpl instagramStream = slingScriptHelper.getService(InstagramStreamImpl.class);
		if(null!=instagramStream)
		instagramPosts = instagramStream.getJsonResponse(instagramUrl);
		if(instagramPosts != null){
			log.info("No of instagram posts recieved::" +instagramPosts.length());
		} else {
			log.info("Please check the username \""+userName+"\" is valid.");
		}
		setInstagramUrl(ApplicationConstants.INSTAGRAM_URL);
	}

	public String getInstagramUrl() {
		return instagramUrl;
	}
	public void setInstagramUrl(String instagramUrl) {
		this.instagramUrl = instagramUrl;
	}

	public String getUserName() {
		return userName;
	}

	public String getAltText() {
		return altText;
	}

	public String getAltComment() {
		return altComment;
	}

	public String getAltLike() {
		return altLike;
	}

	public String getCommentLink() {
		return commentLink;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public String getModalClose() {
		return modalClose;
	}

	public String getNumberOfPosts() {
		return numberOfPosts;
	}

	public String getLikesLink() {
		return likesLink;
	}
}
