package model.dto;

import java.util.Arrays;

public class Schedule {

    private Lesson[] lessons;
    private String groupName;
    private String date;

    public Schedule(Lesson[] lessons, String groupName, String date) {
        if (lessons.length != 8)
            throw new RuntimeException("Array of lesson have not 8 elements");
        this.lessons = lessons;
        this.groupName = groupName;
        this.date = date;
    }

    public Schedule() {}

    public Lesson[] getLessons() {
        return lessons;
    }

    public void setLessons(Lesson[] lessons) {
        this.lessons = lessons;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Schedule schedule = (Schedule) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(lessons, schedule.lessons)) return false;
        if (!groupName.equals(schedule.groupName)) return false;
        return date.equals(schedule.date);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(lessons);
        result = 31 * result + groupName.hashCode();
        result = 31 * result + date.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "lessons=" + Arrays.toString(lessons) +
                ", groupName='" + groupName + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}