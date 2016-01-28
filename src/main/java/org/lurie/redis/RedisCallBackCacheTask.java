package org.lurie.redis;

public class RedisCallBackCacheTask {
	private int eventId;
	private boolean status;
	private RedisCacheTask data;

	public RedisCallBackCacheTask(int eventId, boolean status, RedisCacheTask data) {
		super();
		this.eventId = eventId;
		this.status = status;
		this.data = data;
	}

	public int getEventId() {
		return eventId;
	}

	public boolean isStatus() {
		return status;
	}

	public RedisCacheTask getData() {
		return data;
	}

}
