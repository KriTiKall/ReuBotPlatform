package model.performers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Performer {

    private static final String[] words = {
            " преподаватель техникума", " преподаватель", " доцент",
            " старший преподаватель", " декан факультета", " профессор",
            " заведующий кафедрой", " ассистент"
    };

    public static String[][] removeTeacher(String[][] timetable) {
        for (int i = 0 ; i < timetable.length; i++) {
            for (int j = 0; j < words.length; j++) {
                if (timetable[i][1].contains(words[j])) {
                    timetable[i][1] = timetable[i][1].replaceAll(words[j], "");
                }
            }
        }
        return timetable;
    }

    public static String getTomorrow() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d.MM.yy");
        Calendar calendar = new GregorianCalendar();

        calendar.add(Calendar.DATE, +1);
        if (Calendar.DAY_OF_WEEK == 5)
            calendar.add(Calendar.DATE, +2);
        else if (Calendar.DAY_OF_WEEK == 6)
            calendar.add(Calendar.DATE, +1);
        calendar.getTime();
        return (dateFormat.format(calendar.getTime())).toString();
    }
}
