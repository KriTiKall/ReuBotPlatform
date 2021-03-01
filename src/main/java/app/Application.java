package app;

import model.dto.Schedule;
import model.parser.Parser;
import model.parser.ParserOfGroups;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {
        Parser parser = new ParserOfGroups();
        Schedule[] schedules = parser.parse("http://www.rea.perm.ru/?page_id=1036&id=Timetable/rasp_2021.02.10", "PKo-31");

        for (Schedule schedule : schedules) {
            System.out.println(schedule);
        }

    }
}