package org.lurie.redis;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.Tuple;

/**
 * 
 * 缓存处理processor类
 * 
 * @author lurie
 *
 */
public class RedisCacheTaskProcessor implements Runnable {

	private JedisCluster jedis;

	public void run() {
		while (!RedisCacheManager.getInstance().isShutdown() || RedisCacheManager.getInstance().size() > 0) {
			RedisCacheTask task = null;
			boolean ret = false;
			try {
				connectRedis();
				task = RedisCacheManager.getInstance().getTask();
				if (task != null) {
					ret = task.process(jedis);
				}
			} catch (Exception ex) {
				System.err.println("缓存任务处理错误  ");
				ex.printStackTrace();
			}
			if (task != null) {
				task.onCacheEvent(ret);
			}
		}
	}

	private void connectRedis() throws Exception {
		String server = RedisCacheManager.getInstance().getServer();
		int port = RedisCacheManager.getInstance().getPort();
		try {
			if (jedis == null) {
				Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
				jedisClusterNodes.add(new HostAndPort(server, port));
				jedis = new JedisCluster(jedisClusterNodes);
			}
		} catch (Exception ex) {
			jedis = null;
			System.err.println("jedis cache server connect error : " + server + ":" + port);
			throw ex;
		}
	}

	public JedisCluster getJedis() {
		return jedis;
	}

	public static void main(String[] args) {
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		jedisClusterNodes.add(new HostAndPort("*.*.*.*", 7003));
		JedisCluster jedis = new JedisCluster(jedisClusterNodes);
		jedis.zadd("ss", 1, "java");
		jedis.zadd("ss", 1, "java");
		jedis.zadd("ss", 1, "java");
		Set<Tuple> set = jedis.zrangeByScoreWithScores("ss", 0, Integer.MAX_VALUE, 0, 10);
		for (Tuple tuple : set) {
			System.out.println(tuple.getElement());
		}
	}
}
