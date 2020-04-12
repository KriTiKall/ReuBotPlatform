package view.vk.refac;

/**
 * Группа: ПКО-11
 * <p>
 * Расписание на понедельник (20.05)
 * Первое занятие с 12:10
 * <p>
 * 1) -
 * 2) -
 * 3) Математика (лекция, ауд. 313 общ.)
 * 4) Русский язык (практика, ауд. 302 общ.)
 * 5) Физическая культура (практика)
 * 6) -
 * 7) -
 * 8) -
 */

public class RefacVk {

    private boolean single = true;
    private long chatId;
    public static final long GROUP_ID = 7240343;
    public static final String ACCESS_TOKEN = "8218cc5aa1968d54c405fd46ea52f389a73c758d7a0539087bcf3651eaf0826ec4760ae806483b1ac5ac2";

    public static final String[] template = {
            "Расписание (%s)\n",
            "1) %s\n",
            "2) %s\n",
            "3) %s\n",
            "4) %s\n",
            "5) %s\n",
            "6) %s\n",
            "7) %s\n",
            "8) %s\n"
    };

    public void setChatId(long chatId) {
        if (single) {
            single = false;
            this.chatId = chatId;
        }
    }

    public long getChatId() {
        return chatId;
    }
}
