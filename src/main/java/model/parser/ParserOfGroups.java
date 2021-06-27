package model.parser;

import model.dto.Lesson;
import model.dto.Schedule;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ParserOfGroups implements Parser {

    public ParserOfGroups() {
        Jsoup.connect("http://www.rea.perm.ru");
    }
    //todo add a method of processing not normal td to normal
    @Override
    public Schedule[] parse(String url, String name) throws IOException {
        Elements[] trArray;
        int size;
        Lesson[][] disciplines;
        Schedule[] schedules;
        // Parsing and filtering row of schedule
        Elements trs = Jsoup.connect(url)
                .get()
                .body()
                .select("table")
                .first()
                .select("tr[class=\"fon\"], tr[valign=\"top\"]");

        trArray = multiLayerToOneLayer(trs); // getting array with nine elements containing tds list
        size = trArray[0].size(); // getting general size
        disciplines = new Lesson[size][8]; // width of matrix is count of day in schedule. eight is number of pair of schedule max
        schedules = new Schedule[size]; // length of array is number of day in schedule.

        for (int i = 0; i < disciplines.length; i++) { // get a lessons
            for (int j = 0; j < disciplines[0].length; j++) {
                disciplines[i][j] = elementToDiscipline(trArray[j + 1].get(i));
            }
        }

        for (int i = 0; i < trArray[0].size(); i++) // creating schedule object
            schedules[i] = new Schedule(
                    disciplines[i],
                    name,
                    trArray[0].get(i).text()
            );

        return schedules;
    }
    // getting cells from row and normalize their
    private static Elements[] multiLayerToOneLayer(Elements trs) {
        Elements[] elements = new Elements[9]; // array of td lists. Nine because we are have eight pair max and one row with date or group name
        Elements temp;  // tds storage

        for (int i = 0; i < elements.length; i++)
            elements[i] = new Elements(); // Array filling

        for (int i = 0; i < trs.size(); i++) {
            temp = trs.get(i).select("td"); // getting tds from tr item
            temp.remove(0); // remove first element because it keep useless data
//            if(temp)  todo will add processing of colspan=3 // we have processing colspan=6
            trToNormalTds(trs.get(i)); // doing nothing
            elements[i % 9].addAll(temp); // adding normal tds list to schedule pair
        }
        return elements;
    }

    private static Elements trToNormalTds(Element tr) { // start of method by colspan=3 processing/ it don't work
        Elements tds;

        tds = tr.select("td");
        tds.remove(0);

        for (int i = 0; i < tds.size(); i++) {
             if (tds.get(i).html().matches("colspan=\"3\"")) {
                 System.out.println("333333333333333");
             }
        }

        return tds;
    }

    private Lesson elementToDiscipline(Element td) {  // mapping td element to Discipline object
        String[] strings = td.html().split("<br>");
        if (strings.length == 3) {
            strings[0] = strings[0].substring(3, strings[0].length() - 4).trim();
            return new Lesson(strings[0], strings[1], strings[2]);
        } else
            return null;
    }
}
