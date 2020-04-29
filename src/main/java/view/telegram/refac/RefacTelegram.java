package view.telegram.refac;

import view.GetProperties;

public class RefacTelegram {
    public static final String BOT_NAME = GetProperties.getInstanse().getProperty("tl.bot_name");
    public static final String BOT_TOKEN = GetProperties.getInstanse().getProperty("tl.bot_token");

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

    public static final String[] word = {" преподаватель техникума", " преподаватель" , " доцент",
            " старший преподаватель", " декан факультета", " профессор",
            " заведующий кафедрой", " ассистент"};

    public static final String[][] tt = {
            {"номер пары", "ВТ 24.03.20"},
            {"I пара 8:30 - 10:00", "Основы анализа бухгалтерской отчетности преподаватель, Коретко Е.Э. практика, ауд. Moodle"},
            {"II пара 10:10 - 11:40", "Бизнес-планирование преподаватель техникума, Обухова Н.Ю. практика, ауд. Moodle"},
            {"III пара 12:00 - 13:40", "хуйня из под коня, Нагоева Х.У. лекция, ауд. 228"},
            {"IV пара 13:50 - 15:20", "Классный час, Лузина Н.О. практика, ауд. 1337"}
    };
}
