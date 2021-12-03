package model;

import model.entity.Schedule;

import java.util.List;

public interface DAO {

    default void saveAll(List<Schedule> scheduleList) {
        scheduleList.forEach(this::save);
    }

    void save(Schedule schedule);

    void update(Schedule schedule);

    void delete(Schedule schedule);

    Schedule readByHash(int hashCode);

    Schedule read(Schedule schedule);
}
