package model;

import model.dto.Lesson;
import model.dto.Schedule;

import java.util.Date;

public interface ScheduleOperations {

    Schedule getCurrentSchedule(String groupName);

    Schedule[] getSchedules(String groupName);

    Lesson getNextLesson(String groupName);

    Schedule getSchedule(String groupName, Date date);
}
