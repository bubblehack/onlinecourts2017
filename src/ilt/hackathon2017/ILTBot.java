package ilt.hackathon2017;

import java.util.List;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import ilt.playground.ClauseDictionary;
import ilt.playground.DocumentEngine;
import ilt.playground.DocumentTemplate;
import ilt.playground.MultiVariable;
import ilt.playground.Variable;

public class ILTBot extends TelegramLongPollingBot {
	
	DocumentEngine engine;

	public ILTBot() {
		engine = new DocumentEngine();
		ClauseDictionary dict = new ClauseDictionary();
		dict.update(ILTBot.class.getResourceAsStream("/ilt/playground/clauses.text"));
		
		engine.template = new DocumentTemplate(dict.generateList());
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
		String error = null;
		String text = update.getMessage().getText();
		if (question != null || text.startsWith("/")) {
			if (question instanceof MultiVariable) {
				List<String> options = ((MultiVariable)question).options;
				try {
					Integer idx = Integer.parseInt(text) - 1;

					engine.acceptAnswer(question, options.get(idx));
				} catch (Exception e) {
					error = "Sorry, you need to give me a number between 1 and " + options.size();
				}
			} else {
				engine.acceptAnswer(question, text);
			}
		}
		
		SendMessage s = new SendMessage();
		question = engine.getNextQuestion();
		StringBuilder sb = new StringBuilder();
		if (error != null) {
			sb.append(error + "\n");
		}
		if (question instanceof MultiVariable) {
			sb.append(question.questionText.text);
			sb.append("\n");
			int i = 0;
			for (String opt : ((MultiVariable)question).options) {
				sb.append(++i + ": " + opt + "\n");
			}
		} else {
			sb.append(question.questionText.text);
		}
		s.setText(sb.toString());
		s.setChatId(update.getMessage().getChatId());
		try {
			sendMessage(s);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
	
}
