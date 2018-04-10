package com.mcd.rwd.global.core.service;

/**
 * Created by Rakesh.Balaiah on 22-08-2015.
 */
public interface CacheService {

	Object getFromCache(String key);

	boolean putToCache(String key, Object object);

	boolean removeFromCache(String key);

	boolean isCached(String key);
}
