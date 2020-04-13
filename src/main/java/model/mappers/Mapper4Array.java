package model.mappers;


import java.text.SimpleDateFormat;
import java.util.*;

public class Mapper4Array extends Mapper<ArrayList<ArrayList<String>>,  String[][]> {

    private List<List<String>> list = fillArrayOfArray();


    public void returning(String[][] timetable){
        for (int i = 0; i < timetable[0].length; i++) {
            System.out.println(timetable[0][i]);
        }
    }

    public String removeWord(String string, String word)
    {

        if (string.contains(word)) {

            String tempWord = word + " ";
            string = string.replaceAll(tempWord, "");

            tempWord = " " + word;
            string = string.replaceAll(tempWord, "");
        }

        return string;
    }

    public List<List<String>> fillArrayOfArray(){
        List<List<String>> columns = new ArrayList<>();

        columns.add(Arrays.asList(
                "номер пары",
                "СР 13.04.20",
                "ЧТ 14.04.20"
        ));

        columns.add(Arrays.asList(
                "I пара 8:30 - 10:00",
                "Выполнение работ по профессии Оператор электронно-вычислительных и вычислительных машин Гегин А.С. практика, ауд. Moodle",
                "Информационные технологии преподаватель, Тарутин А.В. практика, ауд. Moodle"
        ));

        columns.add(Arrays.asList(
                "II пара 10:10 - 11:40",
                "Выполнение работ по профессии Оператор электронно-вычислительных и вычислительных машнин Гегин А.С. практика, ауд. Moodle",
                "Информационные технологии преподаватель, Тарутин А.В. практика, ауд. Moodle"
        ));

        columns.add(Arrays.asList(
                "III пара 12:10 - 13:40",
                "Выполнение работ по профессии Оператор электронно-вычислительных и вычислительных машин Гегин А.С. практика, ауд. Moodle",
                "Иностранный язык Карушева А.А. практика, ауд. Moodle"
        ));

        columns.add(Arrays.asList(
                "IV пара 13:50 - 15:20",
                "",
                ""
        ));

        columns.add(Arrays.asList(
                "V пара 15:30 - 17:00",
                "",
                ""
        ));

        columns.add(Arrays.asList(
                "VI пара 17:30 - 19:00",
                "",
                ""
        ));

        columns.add(Arrays.asList(
                "VII пара 19:10 - 20:40",
                "",
                ""
        ));

        columns.add(Arrays.asList(
                "VIII пара 20:45 - 22:15",
                "",
                ""
        ));

        return columns;
    }


    public String getTomorrow() {
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

    @Override
    protected String[][] mapImp(ArrayList<ArrayList<String>> item) {

        int index = 0;
        String [][] newArr = new String[1][9];
        for (int i = 0; i < item.get(0).size(); i++) {
            if (item.get(0).get(i).contains(getTomorrow())) {
                index = i;
            }
        }
        for (int i = 0; i < item.size(); i++) {
            newArr[0][i] = item.get(i).get(0) + "\n" + item.get(i).get(index) +"\n";
        }
        return newArr;
    }
}