package org.lurie.redis;

import redis.clients.jedis.JedisCluster;

/**
 * 缓存处理任务，根据需求可以扩展
 * 
 * @author lurie
 *
 */
public abstract class RedisCacheTask {

	private int eventID;

	private ReidsCacheHandler handler;

	public RedisCacheTask(int eventID, ReidsCacheHandler handler) {
		this.eventID = eventID;
		this.handler = handler;
	}

	public abstract Object getRet();

	public int getEventID() {
		return eventID;
	}

	public ReidsCacheHandler getCacheHandler() {
		return handler;
	}

	public abstract boolean process(JedisCluster jedis);

	public void onCacheEvent(boolean ret) {
		try {
			if (handler != null) {
				handler.onCacheEvent(getEventID(), ret, this);
			}
		} catch (Exception e) {
			System.err.println("处理回掉失败");
		}
	}
}
