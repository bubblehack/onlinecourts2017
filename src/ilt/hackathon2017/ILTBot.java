package ilt.hackathon2017;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class ILTBot extends TelegramLongPollingBot {


	@Override
	public String getBotUsername() {
		return "DigitalLegalAssitantBot";
	}

	@Override
	public String getBotToken() {
		return "448560174:AAGQB_MdSfaIPFG67QTuE0kSArvJd_ybkL8";
	}

	@Override
	public void onUpdateReceived(Update update) {
		System.out.println(update);
		SendMessage s = new SendMessage();
		s.setText("hello, you said: " + update.getMessage().getText());
		s.setChatId(update.getMessage().getChatId());
		try {
			sendMessage(s);
		} catch (TelegramApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
