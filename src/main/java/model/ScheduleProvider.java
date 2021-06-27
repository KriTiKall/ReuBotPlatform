package model;

import model.dto.Schedule;

import java.util.Date;
import java.util.List;

public interface ScheduleProvider {

    Schedule getOne(Schedule schedule);

    List<Schedule> getListByDate(String date);

    List<Schedule> getListByName(String name);

    Schedule getSchedule(String groupName, Date date);
}