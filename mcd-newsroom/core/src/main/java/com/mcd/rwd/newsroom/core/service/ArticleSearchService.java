package com.mcd.rwd.newsroom.core.service;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.mcd.rwd.newsroom.core.models.ArticleDetailBean;
import com.mcd.rwd.global.core.utils.ImageUtil;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.util.ISO8601;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.day.cq.search.QueryBuilder;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import com.mcd.rwd.us.core.service.McdFactoryConfig;
import com.mcd.rwd.us.core.service.McdFactoryConfigConsumer;

/**
 * Created by deepti_b on 10-07-2017.
 */

@Component(immediate = true, metatype = true, label = "Article Search")
@Service(ArticleSearchService.class)
public class ArticleSearchService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleSearchService.class);

	@Reference
	private QueryBuilder queryBuilder;

	@Reference
	private ResourceResolverFactory resolverFactory;

	@Reference
	McdFactoryConfigConsumer configConsumer;

	private Session session;

	McdFactoryConfig mcdFactoryConfig;

	public Map<String, String> getLocalQueryMap(String newsFolderPath, String siteLang, String limit) {
		Map<String, String> map = new TreeMap<String, String>();
		String tagVal = "";

		if (null != mcdFactoryConfig) {
			tagVal = mcdFactoryConfig.getLocalTagValue();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		String currentDate = null;
		if (cal != null) {
			currentDate = ISO8601.format(cal);
		}

		map.put("path", newsFolderPath);
		map.put("property", "jcr:content/sling:resourceType");
		map.put("property.value", "mcd-newsroom/components/page/articlecontentpage");
		map.put("1_property", "jcr:content/cq:tags");
		map.put("1_property.value", tagVal);
		map.put("group.1_group.property", "jcr:content/archiveDate");
		map.put("group.1_group.property.operation", "exists");
		map.put("group.1_group.property.value", "false");
		map.put("group.1_group.1_daterange.property", "jcr:content/publishedDate");
		map.put("group.1_group.1_daterange.upperBound", currentDate);
		map.put("group.1_group.p.and", "true");
		map.put("group.2_group.1_daterange.property", "jcr:content/archiveDate");
		map.put("group.2_group.1_daterange.lowerBound", currentDate);
		map.put("group.2_group.2_daterange.property", "jcr:content/publishedDate");
		map.put("group.2_group.2_daterange.upperBound", currentDate);
		map.put("group.2_group.p.and", "true");
		map.put("group.p.or", "true");
		map.put("orderby", "@jcr:content/publishedDate");
		map.put("orderby.sort", "desc");
		map.put("p.limit", limit);

		return map;
	}

	public Map<String, String> getRegionalQueryMap(String newsFolderPath, String siteLang, String limit) {
		Map<String, String> map = new TreeMap<String, String>();
		String tagVal = "";

		if (null != mcdFactoryConfig) {
			tagVal = mcdFactoryConfig.getLocalTagValue();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		String currentDate = null;
		if (cal != null) {
			currentDate = ISO8601.format(cal);
		}

		map.put("path", newsFolderPath);
		map.put("property", "jcr:content/sling:resourceType");
		map.put("property.value", "mcd-newsroom/components/page/articlecontentpage");
		map.put("group.1_group.1_property", "jcr:content/cq:tags");
		map.put("group.1_group.1_property.operation", "equals");
		map.put("group.1_group.1_property.value", tagVal);
		map.put("group.p.not", "true");
		map.put("4_group.1_group.property", "jcr:content/archiveDate");
		map.put("4_group.1_group.property.operation", "exists");
		map.put("4_group.1_group.property.value", "false");
		map.put("4_group.1_group.1_daterange.property", "jcr:content/publishedDate");
		map.put("4_group.1_group.1_daterange.upperBound", currentDate);
		map.put("4_group.1_group.p.and", "true");
		map.put("4_group.2_group.1_daterange.property", "jcr:content/archiveDate");
		map.put("4_group.2_group.1_daterange.lowerBound", currentDate);
		map.put("4_group.2_group.2_daterange.property", "jcr:content/publishedDate");
		map.put("4_group.2_group.2_daterange.upperBound", currentDate);
		map.put("4_group.2_group.p.and", "true");
		map.put("4_group.p.or", "true");
		map.put("orderby", "@jcr:content/publishedDate");
		map.put("orderby.sort", "desc");
		map.put("p.limit", limit);
		
		return map;
	}

	public Map<String, String> getRecentQueryMap(String newsFolderPath, String siteLang, String limit) {

		Map<String, String> map = new TreeMap<String, String>();
		String tagVal = "";

		if (null != mcdFactoryConfig) {
			tagVal = mcdFactoryConfig.getLocalTagValue();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		String currentDate = null;
		if (cal != null) {
			currentDate = ISO8601.format(cal);
		}

		map.put("path", newsFolderPath);
		map.put("property", "jcr:content/sling:resourceType");
		map.put("property.value", "mcd-newsroom/components/page/articlecontentpage");
		map.put("1_property", "jcr:content/cq:tags");
		map.put("1_property.value", tagVal);
		map.put("group.1_group.property", "jcr:content/archiveDate");
		map.put("group.1_group.property.operation", "exists");
		map.put("group.1_group.property.value", "false");
		map.put("group.1_group.1_daterange.property", "jcr:content/publishedDate");
		map.put("group.1_group.1_daterange.upperBound", currentDate);
		map.put("group.1_group.p.and", "true");
		map.put("group.2_group.1_daterange.property", "jcr:content/archiveDate");
		map.put("group.2_group.1_daterange.lowerBound", currentDate);
		map.put("group.2_group.2_daterange.property", "jcr:content/publishedDate");
		map.put("group.2_group.2_daterange.upperBound", currentDate);
		map.put("group.2_group.p.and", "true");
		map.put("group.p.or", "true");
		map.put("orderby", "@jcr:content/publishedDate");
		map.put("orderby.sort", "desc");
		map.put("p.limit", limit);
		
		return map;

	}

	public Map<String, String> getTrendingQueryMap(String newsFolderPath, String siteLang, String limit) {
		Map<String, String> map = new TreeMap<String, String>();
		String tagVal = "";

		if (null != mcdFactoryConfig) {
			tagVal = mcdFactoryConfig.getLocalTagValue();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		String currentDate = null;
		if (cal != null) {
			currentDate = ISO8601.format(cal);
		}

		map.put("path", newsFolderPath);
		map.put("property", "jcr:content/sling:resourceType");
		map.put("property.value", "mcd-newsroom/components/page/articlecontentpage");
		map.put("1_property", "jcr:content/cq:tags");
		map.put("1_property.value", tagVal);
		map.put("group.1_group.property", "jcr:content/archiveDate");
		map.put("group.1_group.property.operation", "exists");
		map.put("group.1_group.property.value", "false");
		map.put("group.1_group.1_daterange.property", "jcr:content/publishedDate");
		map.put("group.1_group.1_daterange.upperBound", currentDate);
		map.put("group.1_group.p.and", "true");
		map.put("group.2_group.1_daterange.property", "jcr:content/archiveDate");
		map.put("group.2_group.1_daterange.lowerBound", currentDate);
		map.put("group.2_group.2_daterange.property", "jcr:content/publishedDate");
		map.put("group.2_group.2_daterange.upperBound", currentDate);
		map.put("group.2_group.p.and", "true");
		map.put("group.p.or", "true");
		map.put("boolproperty", "jcr:content/isTrendingNews");
		map.put("boolproperty.value", "true");
		map.put("orderby", "@jcr:content/publishedDate");
		map.put("orderby.sort", "desc");
		map.put("p.limit", limit);
		
		return map;
	}

	public List<ArticleDetailBean> getRegionArticleBeanList(SearchResult result) {
		List<ArticleDetailBean> articleDetailBeans = new ArrayList<ArticleDetailBean>();

		try {
			
			// Iterate over the Hits if you need special information
			for (final Hit hit : result.getHits()) {
				ArticleDetailBean bean = new ArticleDetailBean();
				// Returns the path of the hit result
				String path = hit.getPath();
				Resource resource = hit.getResource();
				ValueMap properties = hit.getProperties();
				Resource imageResource = null;
				Resource descResource = null;
				ValueMap properties1 = null;
				String articleDescription = "";
				String pattern = "MMMMM d, yyyy";
				String aemPattern = "yyyy-MM-dd";
				// System.out.println(aemDate.substring(0,
				// aemDate.indexOf("T")));

				SimpleDateFormat format = new SimpleDateFormat(aemPattern);
				SimpleDateFormat aemFormat = new SimpleDateFormat(pattern);

					try {
						Resource jcrcon = resource.getChild("jcr:content");
						imageResource = jcrcon.getChild("articleImage");
					//	descResource = jcrcon.getChild("articleDesc");

						// res is the Resource
					/*	if (descResource != null) {
							properties1 = descResource.adaptTo(ValueMap.class);
							articleDescription = properties1.get("articleDescription", String.class);
						}*/
						articleDescription = properties.get("articleDescription", String.class);
						if (imageResource != null) {
							bean.setArticleImagePath(ImageUtil.getImagePath(imageResource));
						} else {
							bean.setArticleImagePath("");
						}
					} catch (Exception e) {
						LOGGER.info("Exception in ArticleSEarchService");
					}
					String articleDetailPage = ""; // pick from
													// configuration
					if (mcdFactoryConfig != null) {
						articleDetailPage = mcdFactoryConfig.getArticleDetailPagePath();
					}
					String aemPublishDate = properties.get("publishedDate", String.class);

					try {
						Date date = format.parse(aemPublishDate.substring(0, aemPublishDate.indexOf("T")));
						String publishDate = aemFormat.format(date);
						bean.setPublishDate(publishDate);
					} catch (ParseException e) {
						e.printStackTrace();
					}

					bean.setArticleTitle(properties.get("articleTitle", String.class));
					bean.setArticleDescription(articleDescription);
					bean.setArticlePagePath(articleDetailPage + (path.substring(path.lastIndexOf('/') + 1)) + ".html");
					articleDetailBeans.add(bean);
				
			}

		} catch (RepositoryException e) {
			LOGGER.info("Exception in ArticleSEarchService");
		}

		return articleDetailBeans;
	}

	public final List<ArticleDetailBean> getArticleResults(String newsType, String newsFolderPath, String country,
			String language, String limit) throws IOException {

		List<ArticleDetailBean> articleDetailBeans = new ArrayList<ArticleDetailBean>();

		mcdFactoryConfig = configConsumer.getMcdFactoryConfig(country, language);

		Map<String, Object> param = new HashMap<String, Object>();
		param.put(ResourceResolverFactory.SUBSERVICE, "readService");
		ResourceResolver resolver = null;
		try {
			resolver = resolverFactory.getServiceResourceResolver(param);
			session = resolver.adaptTo(Session.class);

			String flag = newsType;
			// invoking method as per newsType
			// String methodName = "get" + newsType + "QueryMap";
			// Method method = this.getClass().getMethod(methodName);
			// Map<String, String> map = (Map<String, String>)
			// method.invoke(newsFolderPath, siteLang);

			Map<String, String> map = new HashMap<String, String>();
			// final Map<String, String> map = new HashMap<String, String>();
			if (newsType.equalsIgnoreCase("Local")) {
				map = getLocalQueryMap(newsFolderPath, country, limit);
				Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);
				SearchResult result = query.getResult();
				articleDetailBeans = getArticleBeanList(result);
			} else if (newsType.equalsIgnoreCase("Regional")) {
				map = getRegionalQueryMap(newsFolderPath, country, limit);
				Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);
				SearchResult result = query.getResult();
				articleDetailBeans = getArticleBeanList(result);
			} else if (newsType.equalsIgnoreCase("Recent")) {
				map = getRecentQueryMap(newsFolderPath, country, limit);
				Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);
				SearchResult result = query.getResult();
				articleDetailBeans = getArticleBeanList(result);
			} else if (newsType.equalsIgnoreCase("Trending")) {
				map = getTrendingQueryMap(newsFolderPath, country, limit);
				Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);
				SearchResult result = query.getResult();
				articleDetailBeans = getArticleBeanList(result);
			}

		} catch (Exception e) {
			LOGGER.info("Exception in ArticleSEarchService");
		}
		return articleDetailBeans;
	}

	public List<ArticleDetailBean> getArticleBeanList(SearchResult result) {
		List<ArticleDetailBean> articleDetailBeans = new ArrayList<ArticleDetailBean>();

		try {

			String pattern = "MMMMM d, yyyy";
			String aemPattern = "yyyy-MM-dd";
			// System.out.println(aemDate.substring(0, aemDate.indexOf("T")));
			long totalMatches = result.getTotalMatches();
			SimpleDateFormat format = new SimpleDateFormat(aemPattern);
			SimpleDateFormat aemFormat = new SimpleDateFormat(pattern);
			// Iterate over the Hits if you need special information
			for (final Hit hit : result.getHits()) {
				ArticleDetailBean bean = new ArticleDetailBean();
				// Returns the path of the hit result
				String path = hit.getPath();
				Resource resource = hit.getResource();
				ValueMap properties = hit.getProperties();
				Resource imageResource = null;
				Resource descResource = null;
				ValueMap properties1 = null;
				String articleDescription = "";
				bean.setTotalMatches(totalMatches);
				try {
					Resource jcrcon = resource.getChild("jcr:content");
					imageResource = jcrcon.getChild("articleImage");
					//descResource = jcrcon.getChild("articleDesc");

					// res is the Resource
				/*	if (descResource != null) {
						properties1 = descResource.adaptTo(ValueMap.class);
						articleDescription = properties1.get("articleDescription", String.class);
					}*/
					articleDescription = properties.get("articleDescription", String.class);
					if (imageResource != null) {
						bean.setArticleImagePath(ImageUtil.getImagePath(imageResource));
					} else {
						bean.setArticleImagePath("");
					}
				} catch (Exception e) {
					LOGGER.info("Exception in ArticleSEarchService");
				}
				String articleDetailPage = ""; // pick from
												// configuration
				if (mcdFactoryConfig != null) {
					articleDetailPage = mcdFactoryConfig.getArticleDetailPagePath();
				}
				String aemPublishDate = properties.get("publishedDate", String.class);

				try {
					Date date = format.parse(aemPublishDate.substring(0, aemPublishDate.indexOf("T")));
					String publishDate = aemFormat.format(date);
					bean.setPublishDate(publishDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				bean.setArticleTitle(properties.get("articleTitle", String.class));
				bean.setArticleDescription(articleDescription);
				bean.setArticlePagePath(articleDetailPage + (path.substring(path.lastIndexOf('/') + 1)) + ".html");
				articleDetailBeans.add(bean);

			}

		} catch (RepositoryException e) {
			LOGGER.info("Exception in ArticleSEarchService");
		}

		return articleDetailBeans;
	}

	public Map<String, String> getArticleDetailQueryMap(String newsFolderPath, String nodeName) {
		Map<String, String> map = new HashMap<String, String>();

		String pagePath = newsFolderPath + "%" + nodeName;

		map.put("path", newsFolderPath);
		map.put("property", "jcr:content/sling:resourceType");
		map.put("property.value", "mcd-newsroom/components/page/articlecontentpage");
		map.put("1_property", "jcr:content/jcr:path");
		map.put("1_property.operation", "like");
		map.put("1_property.value", pagePath);

		return map;
	}

	public ArticleDetailBean getArticleDetails(String newsFolderPath, String nodeName) throws IOException {

		ArticleDetailBean articleDetailBean = new ArticleDetailBean();

		// mcdFactoryConfig = configConsumer.getMcdFactoryConfig(country,
		// language);

		Map<String, Object> param = new HashMap<String, Object>();
		param.put(ResourceResolverFactory.SUBSERVICE, "readService");
		ResourceResolver resolver = null;
		try {
			resolver = resolverFactory.getServiceResourceResolver(param);
			session = resolver.adaptTo(Session.class);

			Map<String, String> map = new HashMap<String, String>();
			// final Map<String, String> map = new HashMap<String, String>();

			map = getArticleDetailQueryMap(newsFolderPath, nodeName);
			Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);
			SearchResult result = query.getResult();

			articleDetailBean = getArticleDetailBean(result);

		} catch (Exception e) {
			LOGGER.info("Exception in ArticleSEarchService");
		}
		return articleDetailBean;

	}

	public ArticleDetailBean getArticleDetailBean(SearchResult result) {
		ArticleDetailBean bean = new ArticleDetailBean();
		try {

			String pattern = "MMMMM d, yyyy";
			String aemPattern = "yyyy-MM-dd";
			boolean isResourceExists = false;

			SimpleDateFormat format = new SimpleDateFormat(aemPattern);
			SimpleDateFormat aemFormat = new SimpleDateFormat(pattern);
			// Iterate over the Hits if you need special information
			for (final Hit hit : result.getHits()) {
				// Returns the path of the hit result
				String path = hit.getPath();
				Resource resource = hit.getResource();
				ValueMap properties = hit.getProperties();
				Resource articleImageComponentResource = null;
				Resource articleVideoComponentResource = null;
				Resource descResource = null;
				ValueMap properties1 = null;
				String articleDescription = "";

				try {
					Resource jcrcon = resource.getChild("jcr:content");
					properties1 = jcrcon.adaptTo(ValueMap.class);
				//	descResource = jcrcon.getChild("articleDesc");
					articleImageComponentResource = jcrcon.getChild("articleHeroImage");
					articleVideoComponentResource = jcrcon.getChild("articleVideoComponent");
					String articleType = properties1.get("articleType", String.class);
					if (articleType.equalsIgnoreCase("image")) {
						if (articleImageComponentResource != null) {
							isResourceExists = true;
							bean.setArticleComponentPath(articleImageComponentResource.getPath());
						}
					}
					if (articleType.equalsIgnoreCase("video")) {
						if (articleVideoComponentResource != null){
							isResourceExists = true;
							bean.setArticleComponentPath(articleVideoComponentResource.getPath());
						}
					}

				/*	if (articleImageComponentResource != null) {
						properties1 = jcrcon.adaptTo(ValueMap.class);
						String articleType = properties1.get("articleType", String.class);
						if (articleType.equalsIgnoreCase("image")) {
							isResourceExists = true;
							bean.setArticleComponentPath(articleImageComponentResource.getPath());
						}
					} else if (articleVideoComponentResource != null) {
						properties1 = jcrcon.adaptTo(ValueMap.class);
						String articleType = properties1.get("articleType", String.class);
						if (articleType.equalsIgnoreCase("video")) {
							isResourceExists = true;
							bean.setArticleComponentPath(articleVideoComponentResource.getPath());
						}
					}*/
					// res is the Resource
				/*	if (descResource != null) {
						properties1 = descResource.adaptTo(ValueMap.class);
						articleDescription = properties1.get("articleDescription", String.class);
					}*/
					Resource articleParResource = null;
					articleParResource = jcrcon.getChild("articlePar");
					bean.setArticlePar(false);
					if (articleParResource != null) {
						bean.setArticlePar(true);						
						bean.setArticleParPath(articleParResource.getPath());
					}

				} catch (Exception e) {
					LOGGER.info("Exception in ArticleSEarchService");
				}

				String aemPublishDate = properties.get("publishedDate", String.class);
				articleDescription = properties.get("articleDescription", String.class);
				try {
					Date date = format.parse(aemPublishDate.substring(0, aemPublishDate.indexOf("T")));
					String publishDate = aemFormat.format(date);
					bean.setPublishDate(publishDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				bean.setArticleTitle(properties.get("articleTitle", String.class));
				bean.setArticleDescription(articleDescription);
				bean.setResourceExists(isResourceExists);

			}

		} catch (RepositoryException e) {
			LOGGER.info("Exception in ArticleSEarchService");
		}

		return bean;
	}

	public final List<ArticleDetailBean> getAllArticleList(String newsFolderPath, String country, String language) throws IOException {
		List<ArticleDetailBean> articlesList = new ArrayList<ArticleDetailBean>();

		mcdFactoryConfig = configConsumer.getMcdFactoryConfig(country, language);

		Map<String, Object> param = new HashMap<String, Object>();
		param.put(ResourceResolverFactory.SUBSERVICE, "readService");
		ResourceResolver resolver = null;
		try {
			resolver = resolverFactory.getServiceResourceResolver(param);
			session = resolver.adaptTo(Session.class);

			Map<String, String> map = null;
			map = getArticlesQueryMap(newsFolderPath);
			Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);
			SearchResult result = query.getResult();
			articlesList = getArticlesList(result);

		} catch (Exception e) {
			LOGGER.info("Exception in ArticleSearchService - getAllArticleList method");
		}

		return articlesList;
	}

	public Map<String, String> getArticlesQueryMap(String newsFolderPath) {

		Map<String, String> map = new TreeMap<String, String>();

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		String currentDate = null;
		if (cal != null) {
			currentDate = ISO8601.format(cal);
		}
		map.put("path", newsFolderPath);
		map.put("property", "jcr:content/sling:resourceType");
		map.put("property.value", "mcd-newsroom/components/page/articlecontentpage");
		map.put("group.1_group.property", "jcr:content/archiveDate");
		map.put("group.1_group.property.operation", "exists");
		map.put("group.1_group.property.value", "false");
		map.put("group.1_group.1_daterange.property", "jcr:content/publishedDate");
		map.put("group.1_group.1_daterange.upperBound", currentDate);
		map.put("group.1_group.p.and", "true");
		map.put("group.2_group.1_daterange.property", "jcr:content/archiveDate");
		map.put("group.2_group.1_daterange.lowerBound", currentDate);
		map.put("group.2_group.2_daterange.property", "jcr:content/publishedDate");
		map.put("group.2_group.2_daterange.upperBound", currentDate);
		map.put("group.2_group.p.and", "true");
		map.put("group.p.or", "true");
		map.put("p.limit", -1+"");

		return map;
	}

	public List<ArticleDetailBean> getArticlesList(SearchResult result) {
		List<ArticleDetailBean> articleDetailBeans = new ArrayList<ArticleDetailBean>();

		try {
			// Iterate over the Hits if you need special information
			for (final Hit hit : result.getHits()) {
				ArticleDetailBean bean = new ArticleDetailBean();
				// Returns the path of the hit result
				String path = hit.getPath();
				//bean.setTotalMatches(totalMatches);
				String articleDetailPage = ""; // pick from
				// configuration
				if (mcdFactoryConfig != null) {
					articleDetailPage = mcdFactoryConfig.getArticleDetailPagePath();
				}

				bean.setArticlePagePath(articleDetailPage + (path.substring(path.lastIndexOf('/') + 1)) + ".html");
				articleDetailBeans.add(bean);
			}

		} catch (RepositoryException e) {
			LOGGER.info("Exception in ArticleSearchService - getArticlesList method");
		}
		return articleDetailBeans;
	}
}