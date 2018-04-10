package com.mcd.rwd.newsroom.core.sightly;

import com.mcd.rwd.global.core.sightly.McDUse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by deepti_b on 09-07-2017.
 */
public class ArticleContentPageHandler extends McDUse {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleContentPageHandler.class);


    private String articleType;

    @Override
    public final void activate() {
        articleType=getProperties().get("articleType",String.class);
    }



    public String getArticleType() {
        return articleType;
    }
}
