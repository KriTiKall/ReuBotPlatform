package model;

import model.entity.Lesson;
import model.entity.Schedule;

import java.util.Date;

public interface ScheduleOperations {

    Schedule getCurrentSchedule(String groupName);

    Schedule[] getSchedules(String groupName);

    Lesson getNextLesson(String groupName);

    Schedule getSchedule(String groupName, Date date);
}
