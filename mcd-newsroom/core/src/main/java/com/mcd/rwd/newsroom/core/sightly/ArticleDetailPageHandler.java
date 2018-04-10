package com.mcd.rwd.newsroom.core.sightly;

import com.mcd.rwd.newsroom.core.models.ArticleDetailBean;
import com.mcd.rwd.newsroom.core.service.ArticleSearchService;
import com.mcd.rwd.global.core.sightly.McDUse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.cq.wcm.api.Page;
import com.mcd.rwd.global.core.utils.PageUtil;

/**
 * Created by deepti_b on 12-07-2017.
 */
public class ArticleDetailPageHandler extends McDUse {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleDetailPageHandler.class);

    private String articleNodeName;

    private String country;
    private String language;
    private String pathFolder;
    private ArticleSearchService articleService;
    private ArticleDetailBean detailBean;
   // private String articleComponentPath;
    
    @Override
    public void activate() throws Exception {
    	LOGGER.debug("In Article Detail Page Handler Handler");
    	pathFolder = getPageProperties().get("newsFolderPath").toString();
    	articleService = getSlingScriptHelper().getService(ArticleSearchService.class);
    	Page currentPage = getCurrentPage();
		this.country = PageUtil.getCountry(currentPage);
		this.language = PageUtil.getLanguage(currentPage);
    	
        String[] selectorsArray = getRequest().getRequestPathInfo().getSelectors();
        if (selectorsArray != null && selectorsArray.length > 0) {
            articleNodeName = selectorsArray[0];
        }
        
        /*if(pathFolder==null||pathFolder.length()<=1)
        pathFolder =  "/content/newsroom/region/arabia/english";  // to get value from page properties of Article Detail page 
            */    
        detailBean = articleService.getArticleDetails(pathFolder, articleNodeName);
       
        //articleService.getArticleResults("LOCAL",pathFolder,country, language);
    }

    public String getArticleNodeName() {
        return articleNodeName;
    }

	public String getPathFolder() {
		return pathFolder;
	}
	
	public ArticleDetailBean getDetailBean() {
		return detailBean;
	}
    
    
}


