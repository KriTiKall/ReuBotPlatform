package app;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import view.bot.TgBotView;

public class Main {
    public static void main(String[] args){
       TgBotView.start();
    }
}