package ilt.hackathon2017;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import ilt.playground.Data;
import ilt.playground.DocumentEngine;
import ilt.playground.Variable;

public class ILTBot extends TelegramLongPollingBot {
	
	DocumentEngine engine;

	public ILTBot() {
		engine = new DocumentEngine();
		engine.template = Data.doc();
	}

	@Override
	public String getBotUsername() {
		return "DigitalLegalAssitantBot";
	}

	@Override
	public String getBotToken() {
		return "448560174:AAGQB_MdSfaIPFG67QTuE0kSArvJd_ybkL8";
	}

	Variable question = null;
	
	@Override
	public void onUpdateReceived(Update update) {
		String text = update.getMessage().getText();
		if (question != null) {
			engine.acceptAnswer(question, text);
		}
		
		SendMessage s = new SendMessage();
		question = engine.getNextQuestion();
		s.setText(question.questionText.text);
		s.setChatId(update.getMessage().getChatId());
		try {
			sendMessage(s);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
	
}
