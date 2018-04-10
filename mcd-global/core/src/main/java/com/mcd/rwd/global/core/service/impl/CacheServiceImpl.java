package com.mcd.rwd.global.core.service.impl;

import com.mcd.rwd.global.core.service.CacheService;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Dictionary;

/**
 * Created by Rakesh.Balaiah on 22-08-2015.
 */

@Component(immediate = true, metatype = true, label = "EHCache Service",
		description = "Exposes the EHCache as a service which can be consumed by other services.")
@Service public class CacheServiceImpl implements CacheService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CacheServiceImpl.class);

	private static final String CACHE_NAME = "rwd";

	private static final int SECONDS_OR_MINS = 60;

	private static final int HOURS = 24;

	private static final int DEFAULT_MAX_ELEMENTS = 5000;

	@Property(label = "Max entries", intValue = 5000,
			description = "Maximum number of entries that can be allowed in the local heap")
	private static final String PROP_MAX_ENTIRES_LOCAL_HEAP = "maxEntriesLocalHeap";

	@Property(label = "Eternal?", boolValue = false,
			description = "When set to 'true', overrides Time to Live and Time to Idle so that no expiration can take place")
	private static final String PROP_ETERNAL = "eternal";

	@Property(label = "Time to idle", intValue = 3600,
			description = "The maximum number of seconds an element can exist in the cache without being accessed.")
	private static final String PROP_TIME_TO_IDLE = "timeToIdleSeconds";

	@Property(label = "Time to live", intValue = 3600,
			description = "The maximum number of seconds an element can exist in the cache regardless of use.")
	private static final String PROP_TIME_TO_LIVE = "timeToLiveSeconds";

	@Property(label = "Disk expiry thread interval", intValue = 120,
			description = "The interval in seconds between runs of the disk expiry thread.")
	private static final String PROP_DISK_EXPIRY_THREAD_INTERVAL = "diskExpiryThreadIntervalSeconds";

	@Property(label = "Memory store eviction policy",
			name = "memoryStoreEvictionPolicy", value = "LRU", propertyPrivate = true)
	private static final String PROP_MEMORY_STORE_EVICTION_POLICY = "memoryStoreEvictionPolicy";

	private CacheManager manager;

	@SuppressWarnings("rawtypes") @Activate
	protected final void activate(final ComponentContext context) {

		Dictionary properties = context.getProperties();

		CacheConfiguration cacheConfig = new CacheConfiguration();
		cacheConfig.setName(CacheServiceImpl.CACHE_NAME);
		cacheConfig.setMemoryStoreEvictionPolicy(
				PropertiesUtil.toString(properties.get(PROP_MEMORY_STORE_EVICTION_POLICY), "LRU"));
		cacheConfig.setTimeToIdleSeconds(PropertiesUtil
				.toInteger(properties.get(PROP_TIME_TO_IDLE), SECONDS_OR_MINS * SECONDS_OR_MINS * HOURS));
		cacheConfig.setTimeToLiveSeconds(PropertiesUtil
				.toInteger(properties.get(PROP_TIME_TO_LIVE), SECONDS_OR_MINS * SECONDS_OR_MINS * HOURS));
		cacheConfig.setEternal(PropertiesUtil.toBoolean(properties.get(PROP_ETERNAL), false));
		cacheConfig.setMaxEntriesLocalHeap(
				PropertiesUtil.toInteger(properties.get(PROP_MAX_ENTIRES_LOCAL_HEAP), DEFAULT_MAX_ELEMENTS));
		cacheConfig.setDiskExpiryThreadIntervalSeconds(
				PropertiesUtil.toInteger(properties.get(PROP_DISK_EXPIRY_THREAD_INTERVAL), 120));
		cacheConfig.addPersistence(
				new PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.NONE));

		Cache cache = new Cache(cacheConfig);

		manager = CacheManager.getInstance();
		manager.addCache(cache);
	}

	@Override
	public final Object getFromCache(final String key) {
		Object obj = null;
		try {
			if (manager != null && StringUtils.isNotBlank(key)) {
				Cache cache = manager.getCache(CacheServiceImpl.CACHE_NAME);
				Element element = cache.get(key);

				if (element != null) {
					obj = element.getObjectValue();
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error while getting from Cache ", e);
		}

		return obj;
	}

	@Override
	public final boolean putToCache(final String key, final Object object) {
		boolean status = false;

		try {
			if (manager != null && StringUtils.isNotBlank(key)) {
				Cache cache = manager.getCache(CacheServiceImpl.CACHE_NAME);
				Element element = new Element(key, object);
				cache.put(element);
				status = true;
			}
		} catch (Exception e) {
			LOGGER.error("Error while setting to Cache ", e);
		}
		return status;
	}

	@Override
	public final boolean removeFromCache(final String key) {

		try {
			if (manager != null && StringUtils.isNotBlank(key)) {
				Cache cache = manager.getCache(CacheServiceImpl.CACHE_NAME);

				if (cache.isKeyInCache(key)) {
					return cache.remove(key);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error while removing from cache ", e);
		}
		return false;
	}

	@Override
	public final boolean isCached(final String key) {
		try {
			if (manager != null && StringUtils.isNotBlank(key)) {
				Cache cache = manager.getCache(CacheServiceImpl.CACHE_NAME);
				return cache.isKeyInCache(key);
			}
		} catch (Exception e) {
			LOGGER.error("Error while checking cache ", e);
		}
		return false;
	}

	@Deactivate
	protected final void deactivate() {
		LOGGER.info("Deactivating Cache ..");
		if (manager != null) {
			manager.removeCache(CACHE_NAME);
			manager.shutdown();
			LOGGER.info("Shutting Down Cache Manager ..");
		}
	}
}
