package view.telegram.bot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import view.telegram.commands.TelegramListener;

//савелий залупа хахахахах

public class BotViewTelegram {
    public BotViewTelegram(){
        ApiContextInitializer.init();
        TelegramBotsApi bot = new TelegramBotsApi();
        try {
            bot.registerBot(new TelegramListener());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}