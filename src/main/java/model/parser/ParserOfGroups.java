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

        //        Elements trs = Jsoup.parse(html)
//                .body()
//                .select("table")
//                .first()
//                .select("tr[class=\"fon\"], tr[valign=\"top\"]");

        Elements trs = Jsoup.connect(url)
                .get()
                .body()
                .select("table")
                .first()
                .select("tr[class=\"fon\"], tr[valign=\"top\"]");

        trArray = multiLayerToOneLayer(trs);
        size = trArray[0].size();
        disciplines = new Lesson[size][8];
        schedules = new Schedule[size];

        for (int i = 0; i < disciplines.length; i++) { // get a lessons
            for (int j = 0; j < disciplines[0].length; j++) {
                disciplines[i][j] = elementToDiscipline(trArray[j + 1].get(i));
            }
        }

        for (int i = 0; i < trArray[0].size(); i++)
            schedules[i] = new Schedule(
                    disciplines[i],
                    name,
                    trArray[0].get(i).text()
            );

        return schedules;
    }

    private static Elements[] multiLayerToOneLayer(Elements trs) {
        Elements[] elements = new Elements[9];
        Elements temp;

        for (int i = 0; i < elements.length; i++)
            elements[i] = new Elements();

        for (int i = 0; i < trs.size(); i++) {
            temp = trs.get(i).select("td");
            temp.remove(0);
//            if(temp)  todo add processing of colspan=3
            trToNormalTds(trs.get(i));
            elements[i % 9].addAll(temp);
        }
        return elements;
    }

    private static Elements trToNormalTds(Element tr) {
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

    private Lesson elementToDiscipline(Element td) {
        String[] strings = td.html().split("<br>");
        if (strings.length == 3) {
            strings[0] = strings[0].substring(3, strings[0].length() - 4).trim();
            return new Lesson(strings[0], strings[1], strings[2]);
        } else
            return null;
    }
}
