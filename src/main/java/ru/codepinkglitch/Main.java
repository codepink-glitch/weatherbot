package ru.codepinkglitch;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
	public static void main(String[] args) {
		
		var bot = new Bot();
		var manager = new ConcurrentManager(bot, User.getDataFromDB());
		try {
			var botsApi = new TelegramBotsApi(DefaultBotSession.class);
			botsApi.registerBot(bot);
		} catch(TelegramApiException e) {
			e.printStackTrace();
		}
		manager.start();
		
		/* ConcurrentHashMap<String, User> map = User.getDataFromDB();
		for(String key: map.keySet()) {
			System.out.println("Key: " + key + ", " + map.get(key).getSubscription().getStatus() +
					 ", " + map.get(key).getSubscription().getHours() + map.get(key).getSubscription().getMinutes());	
		}
		*/
	}
}