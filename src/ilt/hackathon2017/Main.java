package ilt.hackathon2017;

import org.telegram.telegrambots.TelegramBotsApi;

public class Main {
	
	public static void main(String[] args) throws Exception {
		System.out.println("Hello");
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		telegramBotsApi.registerBot(new ILTBot());
		
	}

}
