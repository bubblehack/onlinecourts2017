package ilt.hackathon2017;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import ilt.playground.Case;
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
		try {
			dict.update(new FileInputStream("/private/tmp/clauses.rtf"));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		dict.update(ILTBot.class.getResourceAsStream("/ilt/playground/clauses.text"));
		
		engine.template = new DocumentTemplate(dict.generateList(), dict.generateHelp());
	}

	@Override
	public String getBotUsername() {
		//return "DigitalLegalAssitantBot";
		return "DigitalLegalAssitantBot2";
	}

	@Override
	public String getBotToken() {
		return "417433914:AAGoXm0gxCwYl6YHhcOFkrxbKtQcVGtkZ5Q";
		//return "448560174:AAGQB_MdSfaIPFG67QTuE0kSArvJd_ybkL8";
	}

	Variable question = null;
	boolean skipMode = false;

	@Override
	public void onUpdateReceived(Update update) {
		String error = null;
		String text = update.getMessage().getText();
		if (question != null || text.startsWith("/")) {

			if (text.startsWith("/")) {
				if (text.startsWith("/list")) {
					SendMessage s = new SendMessage();
					s.setText("Saved cases:\n" + String.join("\n", Case.list()));
					s.setChatId(update.getMessage().getChatId());
					try {
						sendMessage(s);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
					return;
				} else if (text.startsWith("/print")) {
					engine.print("/Users/mcw/poc.html");
				} else if (text.startsWith("/skip")) {
					skipAll();
				}
			}

			if (question instanceof MultiVariable) {
				List<String> options = ((MultiVariable) question).options;
				if (!options.contains(text)) {
					error = "Sorry, you need to use one of the provided answers ";
				} else {
					engine.acceptAnswer(question, text);
				}
			} else {
				engine.acceptAnswer(question, text);
			}
		}

		SendMessage s = new SendMessage();
		question = engine.getNextQuestion();
		if (question == null) {
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		if (error != null) {
			sb.append(error + "\n");
		}
		sb.append(question.finalMessage || engine.preamble == null ? "" : (engine.preamble + "\n"));
		if (question instanceof MultiVariable) {
			ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
			markup.setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(true);

			sb.append(question.questionText.text);
			sb.append("\n");
			List<KeyboardRow> rows = new ArrayList<>();
			for (String opt : ((MultiVariable) question).options) {
				KeyboardRow row = new KeyboardRow();
				row.add(opt);
				rows.add(row);
			}
			markup.setKeyboard(rows);

			s.setReplyMarkup(markup);
		} else {
			sb.append(question.questionText.text);
		}
		s.setText(sb.toString());
		s.setParseMode("Markdown");
		s.setChatId(update.getMessage().getChatId());
		try {
			sendMessage(s);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
	
	int idx = 0;
	
	void skipAll() {
		while (true) {
			Variable v = engine.getNextQuestion();
			if (v == null || v.finalMessage)
				break;;
			if (v instanceof MultiVariable) {
				engine.acceptAnswer(v, ((MultiVariable) v).options.get(0));
			} else {
				engine.acceptAnswer(v.canonical(), "generic answer " + idx++);
			}
		}
	}

}
