package view.bot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class parseReuMoy {

//    public static org.jsoup.nodes.Document getPage() throws IOException {
//        String url = "http://www.rea.perm.ru/?page_id=1036";
//        Document page;
//        page = Jsoup.parse(new URL(url), 30000);
//        return page;
//    }
//    public static Elements mainTable() throws IOException {
//        Document page = getPage();
//        Elements timeTable = page.select("div[id = main]");
//        return timeTable;
//    }
//
//    public static Elements primaryTable() throws IOException {
//        Elements mainTable = mainTable();
//        Elements timeTable = mainTable.select("div[id = primary]");
//        return timeTable;
//    }
//
//    public static Elements leftTable() throws IOException {
//        Elements leftTable = primaryTable();
//        Elements timeTable = leftTable.select("div[class = rasp_left_all]");
//        return timeTable;
//    }
//    public static String todayLink() throws IOException {
//        Elements todayLink = leftTable();
//        Element link = todayLink.select("a[href]").first();
//        String almostFinalLink = link.attr("href");
//        //link.select("href");
//        String finalLink = "http://www.rea.perm.ru/" + almostFinalLink;
//        return finalLink;
//    }
//
//    public static Document getTimetable() throws IOException {
//        String timetableUrl = todayLink();
//        Document page;
//        page = Jsoup.parse(new URL(timetableUrl), 30000);
//        return page;
//    }
//
//    public static Elements wrap() throws IOException {
//        Document page = getTimetable();
//        Elements timetable = page.select("div[id = wrap]");
//        return timetable;
//    }
//    public static Elements timetablePage() throws IOException {
//        Elements timetablePage = wrap();
//        Elements timetable = timetablePage.select("div[id = page]");
//        return timetable;
//    }
//
//    public static Elements timetableMain() throws IOException {
//        Elements timetableMain = timetablePage();
//        Elements timetable = timetableMain.select("div[id = main]");
//        return timetable;
//    }
//
//    public static Elements timetablePrimary() throws IOException {
//        Elements timetablePrimary = timetableMain();
//        Elements timetable = timetablePrimary.select("div[id = primary]");
//        return timetable;
//    }
//
//    public static Elements timetableRasp() throws IOException {
//        Elements timetableRasp = timetablePrimary();
//        Elements timetable = timetableRasp.select("div[class = rasp]");
//        Elements rasp = timetable.select("div[class = rasp]");
//        Elements hopeP = rasp.select("p");
//        return hopeP;
//    }
//
//    public static Elements tagOfGroups() throws IOException {
//        Elements tagOfGroups = timetableRasp();
//        Elements html = tagOfGroups.select("<p><p>");
//        return html;
//    }
//
//    public static String groupLink() throws IOException {
//        Elements groupLink = tagOfGroups();
//        Element html = groupLink.select("a[href]").first();
//        String linkOfGroup = html.attr("href");
//        String finalLink = "http://www.rea.perm.ru/" + linkOfGroup;
//        return finalLink;
//    }
//
//    public static Document parseGroupLink() throws IOException {
//        String url = groupLink();
//        Document page;
//        page = Jsoup.parse(new URL(url), 30000);
//        return page;
//    }

    public static String getTime() throws IOException {
        String url = "http://www.rea.perm.ru/?page_id=1036&id=Timetable/rs_PKo-21";
        ArrayList<String> time = new ArrayList<>();
        ArrayList<String> couple = new ArrayList<>();
        String name1 = Jsoup.connect(url)
                .get()
                .body()
                .select("table")
                .first()
                .select("tr[class = fon]")
                .select("td")
                .select("a[name = 1]")
                .first()
                .text();
        Elements values = Jsoup.connect(url)
                .get()
                .body()
                .select("table")
                .first()
                .select("tr[valign = top]");
        for(Element value : values){
            String timeOf = value.select("td").first().text();
            String couples = value.select("td[colspan = 6]").first().select("b").text();
            time.add(timeOf + "\n");
            couple.add(couples + "\n");
        }
        return  String.valueOf(time.get(0)  + couple.get(0)
                + time.get(1) + couple.get(1)
                + time.get(2) + couple.get(2)
                + time.get(3) + couple.get(3)
                + time.get(4) + couple.get(4)
                + time.get(5) + couple.get(5));
    }
    public static String getCouples() throws IOException {
        String url = "http://www.rea.perm.ru/?page_id=1036&id=Timetable/rs_PKo-21";
        ArrayList<String> couple = new ArrayList<>();
        Elements values = Jsoup.connect(url)
                .get()
                .body()
                .select("table")
                .first()
                .select("tr[valign = top]");
        for(Element value : values){
            String couples = value.select("td[colspan = 6]").first().select("b").text();
            couple.add(couples + "\n");
        }
        return String.valueOf(couple);
    }

//    private static boolean trProcessing(Element element){
//        return element.hasAttr("valign") || element.hasClass("fon");
//    }
//
//    public static void parse(){
//        try {
//            ArrayList<ArrayList<String>> list = new ArrayList<>();
//            Jsoup.connect("http://www.rea.perm.ru/?page_id=1036&id=Timetable/rs_PKo-21")
//                    .get()
//                    .body()
//                    .select("table")
//                    .first()
//                    .select("tr")
//                    .forEach(tr ->{
//                        if(trProcessing(tr))
//                            list.add((ArrayList<String>) tr.select("td")
//                                    .stream()
//                                    .map(Element::text)
//                                    .collect(Collectors.toList()));
//                    });
//            System.out.println(list.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //return null;
//    }

    public static void main(String[] args) {
        try {
            System.out.println(getTime());
            //System.out.println(getCouples());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
