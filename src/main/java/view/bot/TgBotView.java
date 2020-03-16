package view.bot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import view.BotView;
import view.refacs.TgRefacs;

import java.io.IOException;

public class TgBotView extends TelegramLongPollingBot implements BotView{

    private Message msg;


    private void sendMessage(Message msg, String text){
        System.out.println("sendMessage is working");
        SendMessage sm = new SendMessage();
        //возможность разметки
        sm.enableMarkdown(true);
        //В какой чат отправить
        sm.setChatId(msg.getChatId().toString());
        //на какое сообшение отвечает
        sm.setReplyToMessageId(msg.getMessageId());
        //текст
        sm.setText(text);
        try{
            execute(new SendMessage()
                    .setChatId(msg.getChatId())
                    .setText(text)
            );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update) {
        msg = update.getMessage();

        if (update.hasMessage()) {
            if(msg.isCommand()){
                if(msg.getText().equals("/timetable")){
                    sendMessage(msg, String.valueOf(ParserReu.parse()));
                }
                if(msg.getText().equals("/timetablem")){
                    try {
                        sendMessage(msg, String.valueOf(parseReuMoy.getTime()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(msg.getText().equals("/time")){
                    sendTimetable(writtingInArray());
                }
            }
        }
    }

    public static final String[] template = {
            "Расписание на понедельник (%s)\n",
            "---\n",
            "1) %s\n",
            "2) %s\n",
            "3) %s\n",
            "4) %s\n",
            "5) %s\n",
            "6) %s\n",
            "7) %s\n",
            "8) %s\n"
    };

    public String[][] writtingInArray(){
        final int columns = 2;
        String [][] arr = new String[template.length][columns];
        for (int i = 0; i < template.length; i++) {
            arr[i][0] = template[i];
        }
        for (int i = 0; i < arr.length; i++) {
            return arr;
        }
        return null;
    }

    //user Name of BOT
    @Override
    public String getBotUsername() {
        return TgRefacs.BOT_NAME;
    }
    //BOT's token
    @Override
    public String getBotToken() {
        return TgRefacs.BOT_TOKEN;
    }

    @Override
    public void init() {
        ApiContextInitializer.init();
        TelegramBotsApi bot = new TelegramBotsApi();
        try {
            bot.registerBot(new TgBotView());
        }catch(TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    public static void start() {
        ApiContextInitializer.init();
        TelegramBotsApi bot = new TelegramBotsApi();
        try {
            bot.registerBot(new TgBotView());
        }catch(TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendTimetable(String[][] timetable) {
        String s = " ";
        for (int i = 0; i < timetable.length; i++) {
            s +=timetable[i][0];
            //sendMessage(msg, String.valueOf(timetable[i][0]));
        }
        sendMessage(msg, s);
    }
}
