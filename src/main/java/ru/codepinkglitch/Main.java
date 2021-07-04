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
		
	}
}