package ru.codepinkglitch;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class User {
	private Subscription sub;
	private Weather weather;
	private String chatId;
	private int count;
	
	public User(Subscription sub, Weather weather, String chatId, int count) {
		this.sub = sub;
		this.weather = weather;
		this.chatId = chatId;
		this.count = count;
	}
	public Weather getWeather() {
		return weather;
	}
	public Subscription getSubscription() {
		return sub;
	}
	public void incrementCount() {
		count++;
	}
	public int getCount() {
		return count;
	}
	public String getChatId() {
		return chatId;
	}
	public void setChatId(String chatId) {
		this.chatId = chatId;
	}
	
	
	public void insertIntoDB() {
		try (var conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/botdb", 
				"Codepink_glitch", "c#%Hvf|S5YDCFgdg0#yf")) {
			var statement = conn.createStatement();
			String st;
			st = "INSERT INTO user_table VALUES (" + "\'" + getChatId() + "\'" + ", " 
					+ getCount() + ", " + weather.getLatitude() + ", " + weather.getLongitude() 
					+ ", " + sub.getStatus() + ", " + sub.getHours() + ", " + sub.getMinutes()
					+ ") ON DUPLICATE KEY UPDATE " + "chat_id=" + "\'" + getChatId() + "\'" 
					+ ", count=" + getCount() + ", latitude=" + weather.getLatitude() 
					+ ", longitude=" + weather.getLongitude() + ", is_active=" + sub.getStatus()
					+ ", hours=" + sub.getHours() + ", minutes=" + sub.getMinutes() + ";";
			statement.executeUpdate(st);
			conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public static ConcurrentHashMap<String, User> getDataFromDB(){
		List<String> ids = new ArrayList<>();
		ConcurrentHashMap<String, User> map = new ConcurrentHashMap<>();
		try(var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/botdb", 
				"Codepink_glitch", "c#%Hvf|S5YDCFgdg0#yf")) {
			var statement = conn.createStatement();
			var resultSet = statement.executeQuery("SELECT chat_id FROM user_table;");
			while(resultSet.next()) {
				ids.add(resultSet.getString("chat_id"));
			}
			for(int i = 0; i < ids.size(); i++) {
				User user = null;
				resultSet = statement.executeQuery("SELECT * FROM user_table WHERE chat_id=" 
						+ "\'" + ids.get(i) + "\'" + ";");
				while(resultSet.next()) {
					user = new User(new Subscription(resultSet.getBoolean("is_active"), 
							resultSet.getInt("hours"), resultSet.getInt("minutes")), 
							new Weather(resultSet.getDouble("latitude"), resultSet.getDouble("longitude")),
							ids.get(i), resultSet.getInt("count"));
				}
				map.put(ids.get(i), user);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
}