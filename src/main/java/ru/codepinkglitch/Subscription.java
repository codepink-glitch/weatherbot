package ru.codepinkglitch;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.time.LocalDate;
import java.time.ZoneId;

public class Subscription {
	public boolean isActive;
	private int hours;
	private int minutes;
	private Timer timer;
	private Date date;
	
	public Subscription() {
		isActive = false;
		timer = new Timer();
		hours = 25;
		minutes = 0;
	}
	public Subscription(boolean isActive, int hours, int minutes) {
		this.isActive = isActive;
		this.hours = hours;
		this.minutes = minutes;
		timer = new Timer();
		defineDate();
	}
	public void setTime(int hours, int minutes) {
		this.hours = hours;
		this.minutes = minutes;
		defineDate();
	}
	public void setStatus(boolean isActive) {
		this.isActive = isActive;
	}
	public boolean getStatus() {
		return isActive;
	}
	public int getHours() {
		return hours;
	}
	public int getMinutes() {
		return minutes;
	}
	public Timer getTimer() {
		return timer;
	}
	public void setScheduledMessage(TimerTask task) {
		timer.schedule(task, date, 86400000);
	}
	public void removeScheduledMessages() {
		timer.cancel();
	}
	public void defineDate() {
		date = new Date();
		date.setTime(hours * 3600000 +  minutes * 60000 
				+ LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000);
		if(date.compareTo(new Date()) < 0)
			date.setTime(date.getTime() + 86400000);
	}
}
