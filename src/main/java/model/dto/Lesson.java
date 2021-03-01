package model.dto;

public class Lesson {

    private int lessonNumber;
    private String name;
    private String teacherName;
    private String information;

    public Lesson(String name, String teacherName, String information) {
        this.name = name;
        this.teacherName = teacherName;
        this.information = information;
    }

    public Lesson(int lessonNumber, String name, String teacherName, String information) {
        this.lessonNumber = lessonNumber;
        this.name = name;
        this.teacherName = teacherName;
        this.information = information;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public int getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(int lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lesson that = (Lesson) o;

        if (lessonNumber != that.lessonNumber) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (teacherName != null ? !teacherName.equals(that.teacherName) : that.teacherName != null) return false;
        return information != null ? information.equals(that.information) : that.information == null;
    }

    @Override
    public int hashCode() {
        int result = lessonNumber;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (teacherName != null ? teacherName.hashCode() : 0);
        result = 31 * result + (information != null ? information.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Discipline{" +
                "lessonNumber=" + lessonNumber +
                ", name='" + name + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", information='" + information + '\'' +
                '}';
    }
}
