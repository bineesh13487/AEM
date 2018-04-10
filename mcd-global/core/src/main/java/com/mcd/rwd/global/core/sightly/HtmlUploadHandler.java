package com.mcd.rwd.global.core.sightly;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Tab;
import com.citytechinc.cq.component.annotations.widgets.Html5SmartFile;
import com.citytechinc.cq.component.annotations.widgets.TextArea;
import com.day.cq.wcm.foundation.Download;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;

@Component(name = "htmlupload", value = "HTML Upload", group = " GWS-Global", disableTargeting = true,
		actions = { "text:  HTML Upload", "-", "editannotate", "copymove", "delete" },
		path = "content/",
		tabs = {
				@Tab(title = "Upload HTML" , touchUINodeName = "upload_html")
		})
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HtmlUploadHandler {

	@DialogField( fieldLabel = "Upload html file")
	@Html5SmartFile(fileNameParameter = "./fileName", fileReferenceParameter = "./fileReference", mimeTypes = "text/*")
	private String file;

	@DialogField( fieldDescription="Please provide HTML content by uploading html file or by using Text Area(Uploading html file will take precedence over the HTML content)."
			,fieldLabel="HTML Content")
	@TextArea
	@Inject
	String source;

	@Inject
	Resource currentResource;



	private static final Logger LOGGER = LoggerFactory.getLogger(HtmlUploadHandler.class);

	@PostConstruct
	public void activate() throws Exception {
		source = currentResource.getValueMap().get("source", String.class);
		getUploadedContents();
	}

	/**
	 * Returns the HTML source of the component.
	 *
	 * @return
	 */
	public String getSource() {
		return this.source;
	}

	/**
	 * Get the source from the HTML file uploaded through the component.
	 *
	 * @throws IOException
	 */
	private void getUploadedContents() throws IOException {
		Download uploadedFile = new Download(currentResource);
		InputStream inputStream = null;
		// check if the uploaded file has content.
		if (uploadedFile.hasContent()) {
			try {
				inputStream = uploadedFile.getData().getBinary().getStream();
				if (null != inputStream) {
					// read the stream as String.
					source = IOUtils.toString(inputStream, "UTF-8");
				}
			} catch (IOException ioe) {
				LOGGER.error("IO Exception reading uploaded file's content.", ioe);
			} catch (Exception e) {
				LOGGER.error("Unable to read file's content", e);
			} finally {
				try {
					if (null != inputStream) {
						inputStream.close();
					}
				} catch (IOException ioException) {
					LOGGER.error("Error closing Input Stream.", ioException);
				}
			}
		}
	}
}