package ru.codepinkglitch;

import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class BotRunnable implements Runnable{
	private static ConcurrentHashMap<String, User> map = new ConcurrentHashMap<String, User>();
	private static Bot bot;
	private Update update;
	
	public BotRunnable() {}
	
	public static void setBot(Bot bot) {
		BotRunnable.bot = bot;
	}

	public BotRunnable(Update update) {
		this.update = update;
	}
	
	public static void setMap(ConcurrentHashMap<String, User> map) {
		BotRunnable.map = map;
	}
	
	public static ConcurrentHashMap<String, User> getMap(){
		return map;
	}
	
	public void updateTimers() {
		for(String key: map.keySet()) {
			if(map.get(key).getSubscription().getStatus()) 
				map.get(key).getSubscription().setScheduledMessage(scheduleTask(key));
		}
	}
	
	@Override
	public void run() {
		if(update.hasMessage()) {
			var message = new SendMessage();
			var chatId = update.getMessage().getChatId().toString();
			message.setChatId(chatId);
			message.setText("Sorry, i don't reconize human speech. Only buttons or time");
			if(map.get(chatId) == null) {
			var newUser = new User(new Subscription(), new Weather(), 0);
			newUser.insertIntoDB(chatId);
			map.put(chatId, newUser);
		}
			if(map.get(chatId).getCount() == 0) {
				message.setText("Hello, i need you to share location." 
						+ " Press the button below."
						+ " You can always change your location by pressing button below.");
				var buttonCreator = new ButtonCreator();
				message.setReplyMarkup(buttonCreator.createLocationButton());
				var noLocationUser = map.get(chatId);
				noLocationUser.incrementCount();
				noLocationUser.insertIntoDB(chatId);
				map.put(chatId, noLocationUser);
			}
			if(update.getMessage().hasLocation()) {
				var locationUser = map.get(chatId);
				locationUser.getWeather().setCoords(update.getMessage().getLocation().getLatitude(),
						update.getMessage().getLocation().getLongitude());
				locationUser.insertIntoDB(chatId);
				map.put(chatId, locationUser);
				message.setText("Location parsed. What you want to do?");
				var buttonCreator1 = new ButtonCreator();
				message.setReplyMarkup(buttonCreator1.createMenu());
			} else if(update.getMessage().getText().matches("^[0-2][0-9]:[0-5][0-9]$")
					&& map.get(chatId).getSubscription().getStatus()) {
				message.setText("I will send you forecast everyday at: " + update.getMessage().getText());
				var timeUser = map.get(chatId);
				var time = update.getMessage().getText().split(":");
				timeUser.getSubscription().setTime(Integer.parseInt(time[0]),
													Integer.parseInt(time[1]));
				timeUser.getSubscription().setScheduledMessage(scheduleTask(chatId));
				timeUser.insertIntoDB(chatId);
				map.put(chatId, timeUser);
			}
			
			try {
				bot.execute(message);
			} catch(TelegramApiException e) {
				e.printStackTrace();
			}
		} else if(update.hasCallbackQuery()) {
			var chatId = update.getCallbackQuery().getMessage().getChatId().toString();
			var message = new SendMessage();
			message.setChatId(chatId);
			var user = map.get(chatId);
			
			switch(update.getCallbackQuery().getData()) {
				case("Forecast"):
					message.setText(user.getWeather().getForecast());
					break;
				case("Sub"):
					if(!user.getSubscription().getStatus()) {
						user.getSubscription().setStatus(true);
						message.setText("Subscription activated. Thank you. "
				 				+ "Please indicate the time at which you  will receive a forecast. "
				 				+ "Time format 24h : 00:00. "
				 				+ "In case you want change the time, just type new value anytime.");
						user.insertIntoDB(chatId);
						map.put(chatId, user);
					} else {
						message.setText("Choose new time to recieve a forecast. "
								+ "Time format 24h : 00:00");
					}
					break;
				case("Cancel sub"):
					if(user.getSubscription().getStatus()) {
						user.getSubscription().setStatus(false);
						message.setText("Subscription cancelled");
						user.getSubscription().removeScheduledMessages();
						user.insertIntoDB(chatId);
						map.put(chatId, user);
					} else {
						message.setText("You don't have a subscription.");
					}
					break;
			}
			try {
				bot.execute(message);
			} catch(TelegramApiException e) {
				e.printStackTrace();
			}
		}
		
	}
	public TimerTask scheduleTask(String chatId) {
		var task = new TimerTask() {
			@Override
			public void run() {
				var scheduledMessage = new SendMessage();
				scheduledMessage.setChatId(chatId);
				scheduledMessage.setText(map.get(chatId).getWeather().getForecast());
				try {
					bot.execute(scheduledMessage);
				} catch(TelegramApiException e) {
					e.printStackTrace();
				}
			}
		};
		return task;
	}
}
