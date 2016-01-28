package org.lurie.redis;

/**
 * 
 * @author lurie
 *
 */
public interface ReidsCacheHandler {

	void onCacheEvent(int eventID, boolean flag, RedisCacheTask task);

}
