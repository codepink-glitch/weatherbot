package ru.codepinkglitch;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class ConcurrentManager {
	private static Bot bot;
	private static ConcurrentHashMap<String, User> map;

	public void setBot(Bot bot) {
		ConcurrentManager.bot = bot;
	}
	public  ConcurrentManager(Bot bot, ConcurrentHashMap<String, User> map) {
		ConcurrentManager.map = map;
		ConcurrentManager.bot = bot;
		BotRunnable.setBot(bot);
		BotRunnable.setMap(map);
		var botRunnable = new BotRunnable();
		botRunnable.updateTimers();
	}
	public ConcurrentManager(Bot bot) {
		ConcurrentManager.bot = bot;
	}
	
	public void start() {
		var pool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		Thread mainThread = null;
		Runnable r = () -> {
			while(true) {
				if(bot.getQueue().isEmpty()) {
					try {
						Thread.currentThread().sleep(500);
						continue;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					pool.execute(new BotRunnable(bot.getQueue().poll()));
				}
			}
		};
		mainThread = new Thread(r);
		mainThread.setPriority(Thread.MAX_PRIORITY);
		pool.execute(mainThread);
	}

}