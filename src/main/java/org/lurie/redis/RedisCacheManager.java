package org.lurie.redis;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * redis缓存管理类，多线程操作
 * 
 * @author lurie
 *
 */
public class RedisCacheManager {

	private static RedisCacheManager instance = new RedisCacheManager();

	private List<Thread> threads = new ArrayList<Thread>();

	private LinkedList<RedisCacheTask> tasks = new LinkedList<RedisCacheTask>();

	private String server;
	private int port;
	private int threadCount = 1;

	private boolean shutdown = false;

	private RedisCacheManager() {
	}

	public static RedisCacheManager getInstance() {
		return instance;
	}

	private void start() {
		Thread thread = null;
		for (int i = 0; i < threadCount; i++) {
			thread = new Thread(new RedisCacheTaskProcessor());
			thread.setName("CacheManager-" + i);
			thread.setDaemon(true);
			thread.start();
			threads.add(thread);
		}
	}

	public void addTask(RedisCacheTask ct) {
		synchronized (tasks) {
			tasks.add(ct);
			tasks.notifyAll();
		}
	}

	protected RedisCacheTask waitingForTask() throws InterruptedException {
		synchronized (tasks) {
			RedisCacheTask task = tasks.poll();
			return task;
		}
	}

	public int size() {
		return tasks.size();
	}

	public String getServer() {
		return server;
	}

	public int getPort() {
		return port;
	}

	public boolean isShutdown() {
		return shutdown;
	}

	public static void shutdown() {
		getInstance().shutdown = true;
		while (getInstance().size() > 0) {
			try {
				System.err.println("waiting for shutdown CacheManager ");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
