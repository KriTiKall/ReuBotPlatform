drop function model.insert_or_update_schedule(model.schedule, out text);

drop function model.update_schedule(model.schedule, out text);

drop procedure model.delete_lesson(int, int);

drop function model.insert_schedule(model.schedule, out text);

drop function model.get_lesson_ref(model.lessons_json, text, out int, out bool);

drop function model.insert_lesson(jsonb, text, out int)