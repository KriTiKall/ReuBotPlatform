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

public class TgListener extends TelegramLongPollingBot implements BotView {

    private Message msg;


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


    public void onUpdateReceived(Update update) {
        msg = update.getMessage();
        ParserReu pr = new ParserReu();

        if (update.hasMessage()) {
            if (msg.isCommand()) {
                if (msg.getText().equals("/timetable")) {
                    sendMessage(msg, String.valueOf(pr.parse()));
                }
                if (msg.getText().equals("/timetableMoy")) {
                    try {
                        sendMessage(msg, String.valueOf(parseReuMoy.getTime()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (msg.getText().equals("/time")) {
                    sendTimetable(tt);
                    //sendTimetable(writingInArray());
                }
            }
        }
    }

    private final String[] words = {" преподаватель техникума", " преподаватель", " доцент", " старший преподаватель", " декан факультета", " профессор", " заведующий кафедрой", " ассистент", "практика"};

    public String perform(Message msg) {
        ParserReu p = new ParserReu();
        String s = p.parse().toString();
        for (int i = 0; i < words.length; i++) {
            if (s.contains(words[i])) {
                String temp = words[i] + " ";
                s = s.replace(temp, "");
                temp = " " + words[i];
                s = s.replace(temp, "");
            }
        }
        return s;
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

    public String[][] writingInArray() {
        final int columns = 2;
        String[][] arr = new String[template.length][columns];
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


    public static void start() {
        ApiContextInitializer.init();
        TelegramBotsApi bot = new TelegramBotsApi();
        try {
            bot.registerBot(new TgListener());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    String[][] tt = {
            {"номер пары", "ВТ 24.03.20"},
            {"I пара 8:30 - 10:00", "Основы анализа бухгалтерской отчетности преподаватель, Коретко Е.Э. практика, ауд. Moodle"},
            {"II пара 10:10 - 11:40", "Бизнес-планирование преподаватель техникума, Обухова Н.Ю. практика, ауд. Moodle"},
            {"III пара 12:00 - 13:40", "хуйня из под коня, Нагоева Х.У. лекция, ауд. 228"},
            {"IV пара 13:50 - 15:20", "Классный час, Лузина Н.О. практика, ауд. 1337"}
    };
 /* public static String removeWord(String string, String word)
    {

        // Check if the word is present in string
        // If found, remove it using removeAll()
        if (string.contains(word)) {

            // To cover the case
            // if the word is at the
            // beginning of the string
            // or anywhere in the middle
            String tempWord = word + " ";
            string = string.replaceAll(tempWord, "");

            // To cover the edge case
            // if the word is at the
            // end of the string
            tempWord = " " + word;
            string = string.replaceAll(tempWord, "");
        }

        // Return the resultant string
        return string;
    }*/

    @Override
    public void sendTimetable(String[][] timetable) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < timetable.length; i++) {
            for (int j = 0; j < timetable[i].length; j++) {
                if(i == 0 && j == 0){
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
        System.out.println(stringBuilder.toString());
    }
}
