drop function model.get_schedules(varchar(10), bool, out jsonb, out text);
drop function model.get_schedule(varchar(10), date, bool, out  jsonb, out bool, out text);

drop table model.position_to_period;

drop function model.get_position_by_time(time, out int);

drop function model.get_next_lesson(varchar(10), out jsonb, out bool, out text);

drop function model.get_lesson( int, bool, out jsonb);

drop function model.get_lesson_content( text, text, text, text, out json);