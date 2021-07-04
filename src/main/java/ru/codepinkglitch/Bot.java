package ru.codepinkglitch;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


public class Bot extends TelegramLongPollingBot {
	private static final String BOT_NAME = "NewbieWeatherBot";
	private static final String BOT_TOKEN = "1803845882:AAHYoassvebeKkyDaW5XxkXyvoxcDNRQ35s";
	private final Queue<Update> queue = new ConcurrentLinkedQueue<>();
	
	public Bot() {
		super();
		}

	@Override
	public void onUpdateReceived(Update update) {
			queue.add(update);
	}

	@Override
	public String getBotUsername() {
		return BOT_NAME;
	}

	@Override
	public String getBotToken() {
		return BOT_TOKEN;
	}
	public Queue<Update> getQueue(){
		return queue;
	}
}
