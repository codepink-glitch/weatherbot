package ru.codepinkglitch;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class ButtonCreator {
	ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
	List<KeyboardRow> keyboardRowList = new ArrayList<>();
	KeyboardRow keyboardRow = new KeyboardRow();
	KeyboardButton locationButton = new KeyboardButton();
	
	ReplyKeyboardMarkup createLocationButton() {
		keyboardMarkup.setResizeKeyboard(true);
	    keyboardMarkup.setSelective(true);
	    locationButton.setText("location parse");
	    locationButton.setRequestLocation(true);
	    keyboardRow.add(locationButton);
	    keyboardRowList.add(keyboardRow);
	    keyboardMarkup.setKeyboard(keyboardRowList);	 
	    return keyboardMarkup; 
	}
	
	InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
	InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
	InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
	List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
	List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
	InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
	
	InlineKeyboardMarkup createMenu() {
		inlineKeyboardButton1.setText("Forecast");
		inlineKeyboardButton1.setCallbackData("Forecast");
		inlineKeyboardButton2.setText("Subscription");
		inlineKeyboardButton2.setCallbackData("Sub");
		inlineKeyboardButton3.setText("Cancel sub");
		inlineKeyboardButton3.setCallbackData("Cancel sub");
		keyboardButtonsRow.add(inlineKeyboardButton1);
		keyboardButtonsRow.add(inlineKeyboardButton2);
		keyboardButtonsRow.add(inlineKeyboardButton3);
		rowList.add(keyboardButtonsRow);
		inlineKeyboardMarkup.setKeyboard(rowList);
		return inlineKeyboardMarkup;
	}

}
