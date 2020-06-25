package data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ParserPK_21 implements Parser {

    @Override
    public List<List<String>> parse(){
        List<List<String>> list = new ArrayList<>();
        try {
            Jsoup.connect("http://www.rea.perm.ru/?page_id=1036&id=Timetable/rs_PKo-21")
                    .get()
                    .body()
                    .select("table")
                    .first()
                    .select("tr")
                    .forEach(tr ->{
                        if(trProcessing(tr))
                            list.add((ArrayList<String>) tr.select("td")
                                    .stream()
                                    .map(Element::text)
                                    .collect(Collectors.toList()));
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private boolean trProcessing(Element element){
        return element.hasAttr("valign") || element.hasClass("fon");
    }
}
