package ilt.hackathon2017;

import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramWebhookBot;

public class ILTBot extends TelegramWebhookBot {

	@Override
	public String getBotPath() {
		return null;
	}

	@Override
	public String getBotUsername() {
		return "DigitalLegalAssitantBot";
	}

	@Override
	public BotApiMethod onWebhookUpdateReceived(Update update) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBotToken() {
		return "448560174:AAGQB_MdSfaIPFG67QTuE0kSArvJd_ybkL8";
	}
	
}
