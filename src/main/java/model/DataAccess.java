package model;

import model.dto.Schedule;

import java.util.List;

public interface DataAccess {

    default void saveAll(List<Schedule> scheduleList) {
        scheduleList.forEach(this::save);
    }

    void save(Schedule schedule);

    Schedule getOne(Schedule schedule);

    List<Schedule> getListByDate(String date);

    List<Schedule> getListByName(String name);
}