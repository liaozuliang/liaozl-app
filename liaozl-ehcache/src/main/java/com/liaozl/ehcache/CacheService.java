package com.liaozl.ehcache;


public interface CacheService {
	
	public Object get(String cacheName, String key);
	
	public void put(String cacheName, String key, Object value);
	
	public void update(String cacheName,  String key, Object value);
	
	public void remove(String cacheName,  String key);
	
}
