create or replace function model.get_schedules(pi_group_name varchar(10), pi_is_actual bool,
                                               out po_json jsonb, out po_result_msg text)
    language plpgsql
as
$$
declare
    p_dates date[];
    p_date  date;
begin
    po_json := '[]'::jsonb;
    p_dates := ARRAY(select sch.date
                     from model.schedules sch
                              join model.group_names g on sch.name_id = g.id
                     where g.name = pi_group_name)::date[];

    foreach p_date in array p_dates
        loop
            select jsonb_set(
                           po_json::jsonb,
                           '{999}'::text[],
                           model.get_schedule(
                                   pi_group_name,
                                   p_date,
                                   pi_is_actual
                               )::jsonb)
            into po_json;
        end loop;

exception
    when others
        then
            declare
                exception_diag text;
            begin
                get stacked diagnostics exception_diag = pg_exception_context;
                po_result_msg := 'status: ' || SQLSTATE || ', ' || SQLERRM || ' CTX:' ||
                                 exception_diag;
                return;
            end;
end;
$$;

create or replace function model.get_schedule(pi_group_name varchar(10), pi_date date, pi_is_actual bool,
                                              out po_json jsonb, out po_is_exists bool, out po_result_msg text)
    language plpgsql
as
$$
declare
    p_lesson_ref  int  = NULL;
    p_is_single   bool = NULL;
    p_schedule_id int  = NULL;
begin
    po_is_exists := false;
    po_json := '{
      "groupName": "",
      "date": "",
      "lessons": []
    }'::jsonb;

    select sch.id
    into p_schedule_id
    from model.schedules sch
             join model.group_names g on sch.name_id = g.id
    where g.name = pi_group_name
      and sch.date = pi_date;

    if FOUND then
        select jsonb_set(
                       po_json::jsonb,
                       '{groupName}'::text[],
                       ('"' || pi_group_name || '"')::jsonb)
        into po_json;

        select jsonb_set(
                       po_json::jsonb,
                       '{date}'::text[],
                       ('"' || to_char(pi_date, 'DD.MM.YY') || '"')::jsonb)
        into po_json;

        RAISE NOTICE 'before loop %', po_json;

        for pos in 1..8
            loop
                select lts.lesson_ref_id, lts.is_single
                into p_lesson_ref, p_is_single
                from model.lessons_to_schedules lts
                         join model.schedules sch on lts.schedule_id = sch.id
                         join model.group_names gn on sch.name_id = gn.id
                where lts.position = pos
                  and gn.name = pi_group_name
                  and sch.date = pi_date
                  and lts.is_actual = pi_is_actual;

                RAISE NOTICE 'in loop ref = %, single = %', p_lesson_ref, p_is_single;

                select jsonb_set(
                               po_json::jsonb,
                               '{lessons, 9}'::text[],
                               (model.get_lesson(
                                       p_lesson_ref,
                                       p_is_single
                                   ))::jsonb)
                into po_json;
                RAISE NOTICE 'after iteration %', po_json;
            end loop;
        RAISE NOTICE 'after loop %', po_json;

    end if;

exception
    when others
        then
            declare
                exception_diag text;
            begin
                get stacked diagnostics exception_diag = pg_exception_context;
                po_result_msg := po_result_msg || ' ,status: ' || SQLSTATE || ', ' || SQLERRM || ' CTX:' ||
                                 exception_diag;
                return;
            end;
end ;
$$;

create table model.position_to_period
(
    position   int  not null,
    start_time time not null,
    end_time   time not null
);

insert into model.position_to_period
values (0, '8:30', '10:09:59'),
       (1, '10:10', '12:09:59'),
       (2, '12:10', '14:09:59'),
       (3, '14:00', '15:39:59'),
       (4, '15:40', '17:29:59'),
       (5, '17:30', '19:09:59'),
       (6, '19:10', '20:44:59'),
       (7, '20:45', '23:59:59'),
       (7, '00:00', '08:29:59');

create or replace function model.get_position_by_time(pi_time time, out po_position int)
    language plpgsql
as
$$
begin
    select position
    into po_position
    from model.position_to_period
    where pi_time between start_time and end_time;
end;
$$;


create or replace function model.get_next_lesson(pi_group_name varchar(10),
                                                 out po_json jsonb, out po_is_exist_today bool, out po_result_msg text)
    language plpgsql
as
$$
declare
    p_lesson_ref  int  = NULL;
    p_is_single   bool = NULL;
    p_schedule_id int  = NULL;
    p_position    int  = model.get_position_by_time(current_time::time);
begin
    p_position := (p_position + 1) % 8;
    po_is_exist_today := false;


    select sch.id
    into p_schedule_id
    from model.schedules sch
             join model.group_names g on sch.name_id = g.id
             join model.lessons_to_schedules lts on sch.id = lts.schedule_id
    where g.name = pi_group_name
      and sch.date >= current_date
      and lts.is_actual = true
      and lts.position > model.get_position_by_time(current_time::time);

    --     if FOUND then
--         select jsonb_set(
--                        po_json::jsonb,
--                        '{groupName}'::text[],
--                        ('"' || pi_group_name || '"')::jsonb)
--         into po_json;
--
--         select jsonb_set(
--                        po_json::jsonb,
--                        '{date}'::text[],
--                        ('"' || pi_date || '"')::jsonb)
--         into po_json;
--
--         for pos in 0..7
--             loop
--                 select lts.lesson_ref_id, lts.is_actual
--                 into p_lesson_ref, p_is_single
--                 from model.lessons_to_schedules lts
--                          join model.schedules sch on lts.schedule_id = sch.id
--                          join model.group_names gn on sch.name_id = gn.id
--                 where lts.position = pos
--                   and gn.name = pi_group_name
--                   and sch.date = pi_date
--                   and lts.is_actual = pi_is_actual;
--
--                 select jsonb_set(
--                                po_json::jsonb,
--                                '{lessons, 9}'::text[],
--                                (model.get_lesson(
--                                        p_lesson_ref,
--                                        p_is_single
--                                    ))::jsonb)
--                 into po_json;
--             end loop;
--     end if;

exception
    when others
        then
            declare
                exception_diag text;
            begin
                get stacked diagnostics exception_diag = pg_exception_context;
                po_result_msg := po_result_msg || ' ,status: ' || SQLSTATE || ', ' || SQLERRM || ' CTX:' ||
                                 exception_diag;
                return;
            end;
end ;
$$;


create or replace function model.get_lesson(pi_lesson_ref int, pi_is_single bool,
                                            out po_json jsonb)
    language plpgsql
as
$$
declare
    p_less_rec  record;
    p_first_id  int;
    p_second_id int;

begin
    if pi_lesson_ref is not null then
        if pi_is_single = 'false' then
            po_json := ('{"type": "PairLesson","pair": {"first": {},"second": {}}}')::jsonb;

            select pl.first_lesson_id, pl.second_lesson_id
            into p_first_id, p_second_id
            from model.pair_lesson pl
            where pl.id = pi_lesson_ref;

            -- first json
            if p_first_id is not null then
                select ds.name lesson_name, ts.name teacher_name, ls.type, ls.auditorium
                into p_less_rec
                from model.lessons ls
                         join model.disciplines ds on ls.discipline_id = ds.id
                         join model.teachers ts on ls.teacher_id = ts.id
                where ls.id = p_first_id;

                select jsonb_set(
                               po_json::jsonb,
                               '{pair, first}'::text[],
                               (model.get_lesson_content(
                                       p_less_rec.lesson_name,
                                       p_less_rec.teacher_name,
                                       p_less_rec.type,
                                       p_less_rec.auditorium
                                   ))::jsonb)
                into po_json;
            else
                select jsonb_set(
                               po_json::jsonb,
                               '{pair, first}'::text[],
                               '{"type":"Empty"}'::jsonb)
                into po_json;
            end if;

            -- second json
            if p_second_id is not null then
                select ds.name lesson_name, ts.name teacher_name, ls.type, ls.auditorium
                into p_less_rec
                from model.lessons ls
                         join model.disciplines ds on ls.discipline_id = ds.id
                         join model.teachers ts on ls.teacher_id = ts.id
                where ls.id = p_second_id;

                select jsonb_set(
                               po_json::jsonb,
                               '{pair, second}'::text[],
                               (model.get_lesson_content(
                                       p_less_rec.lesson_name,
                                       p_less_rec.teacher_name,
                                       p_less_rec.type,
                                       p_less_rec.auditorium
                                   ))::jsonb)
                into po_json;
            else
                select jsonb_set(
                               po_json::jsonb,
                               '{pair, second}'::text[],
                               '{"type":"Empty"}'::jsonb)
                into po_json;
            end if;


        elseif pi_is_single = 'true' then

            select ds.name lesson_name, ts.name teacher_name, ls.type, ls.auditorium
            into p_less_rec
            from model.lessons ls
                     join model.disciplines ds on ls.discipline_id = ds.id
                     join model.teachers ts on ls.teacher_id = ts.id
            where ls.id = pi_lesson_ref;

            select jsonb_set(
                           ('{"type": "SingleLesson","lesson": {}}')::jsonb,
                           '{lesson}'::text[],
                           (model.get_lesson_content(
                                   p_less_rec.lesson_name,
                                   p_less_rec.teacher_name,
                                   p_less_rec.type,
                                   p_less_rec.auditorium
                               ))::jsonb)
            into po_json;

        end if;
    else
        po_json := '{
          "type": "SingleLesson",
          "lesson": {
            "type": "Empty"
          }
        }';
    end if;
end;
$$;

create or replace function model.get_lesson_content(pi_name text, pi_teacher_name text, pi_type text,
                                                    pi_auditorium text,
                                                    out po_json json)
    language plpgsql
as
$$
declare
begin
    po_json := '{
      "type": "Lesson",
      "name": "' || pi_name || '",
      "teacherName": "' || pi_teacher_name || '",
      "lessonType": "' || pi_type || '",
      "auditorium": "' || pi_auditorium || '"
    }';
end;
$$;