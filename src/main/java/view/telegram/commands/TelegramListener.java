package view.telegram.commands;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import view.BotView;
import view.telegram.refac.RefacTelegram;

public class TelegramListener  extends TelegramLongPollingBot implements BotView {

    private Message msg;

    @Override
    public void onUpdateReceived(Update update) {
        msg = update.getMessage();

        if (update.hasMessage()) {
            if (msg.getText().equalsIgnoreCase("/time"))
                sendTimetableForSubs(RefacTelegram.tt);
        }
    }

    private void sendMessage(Message msg, String text) {
        try {
            execute(new SendMessage()
                    .setChatId(msg.getChatId())
                    .setText(text)
            );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String[][] writingInArray() {
        String[][] arr = new String[RefacTelegram.template.length][2];

        for (int i = 0; i < RefacTelegram.template.length; i++)
            arr[i][0] = RefacTelegram.template[i];

        return arr;
    }

    @Override
    public void sendTimetableForSubs(String[][] timetable) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < timetable.length; i++) {
            for (int j = 0; j < timetable[i].length; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                stringBuilder
                        .append(timetable[i][j])
                        .append("\n").toString();
            }
            stringBuilder
                    .append("\n").toString();
        }
        sendMessage(msg, stringBuilder.toString());
    }

    //user Name of BOT
    @Override
    public String getBotUsername() {
        return RefacTelegram.BOT_NAME;
    }

    //BOT's token
    @Override
    public String getBotToken() {
        return RefacTelegram.BOT_TOKEN;
    }
}
