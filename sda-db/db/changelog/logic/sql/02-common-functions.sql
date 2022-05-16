---------------------------------------------------------------
------- functions by update                           ---------
---------------------------------------------------------------

create function model.insert_or_update_schedule(pi_schedule model.schedule, out po_result_msg text, out po_action text)
    language plpgsql
as
$$
declare
    schedule_id   int  = -1;
    schedule_hash int  = -1;
    result_test   text = '';
    action        text = '';
begin

    select sch.id, hash
    into schedule_id, schedule_hash
    from model.schedules sch
             join model.group_names gn on gn.id = sch.name_id
    where date = pi_schedule.date
      and name = pi_schedule.name;

    if FOUND then
        RAISE NOTICE 'hash select = % and pi = %, bool = %', schedule_hash, pi_schedule.hash, schedule_hash != pi_schedule.hash;
        if schedule_hash != pi_schedule.hash then
            action := 'update';
            select upd.po_result_msg into result_test from model.update_schedule(pi_schedule) upd;
        else
            action := 'didnt changed';
        end if;
    else
        action := 'insert';
        select ins.po_result_msg
        into result_test
        from model.insert_schedule(pi_schedule) ins;
    end if;

    po_action := action;

    po_result_msg = action || ', ' || result_test || ', ok, '
                        || pi_schedule.date || ', ' ||
                    pi_schedule.name;
exception
    when others
        then
            declare
                exception_diag text;
            begin
                get stacked diagnostics exception_diag = pg_exception_context;
                po_result_msg := 'ERROR:' || result_test || ', status: ' || SQLSTATE || ', ' || SQLERRM || ' CTX:' ||
                                 exception_diag;
                return;
            end;
end;
$$;

create or replace function model.update_schedule(pi_schedule model.schedule,
                                                 out po_result_msg text)
    language plpgsql
as
$$
declare
    p_less         model.lessons_json;
    p_is_empty     bool;
    p_is_single    bool;
    p_hash         int;
    p_lesson_ref   int;
    p_r_les_to_sch model.lessons_to_schedules%rowtype;
    p_schedule_id  int;
    p_id_name      int;
begin

    p_less = pi_schedule.lessons;

    select sch.id, gn.id
    into p_schedule_id, p_id_name
    from model.schedules sch
             join model.group_names gn on gn.id = sch.name_id
    where gn.name = pi_schedule.name
      and sch.date = pi_schedule.date;

    RAISE NOTICE 'update';

    update model.schedules sch
    set hash = pi_schedule.hash
    where sch.name_id = p_id_name
      and sch.date = pi_schedule.date;

    for i in 1..8
        loop
            p_hash := (p_less[i] #>> '{hash}')::int; -- save hash

            select po_lesson_ref, po_is_empty, po_is_single
            into p_lesson_ref, p_is_empty, p_is_single
            from model.get_lesson_ref(p_less[i]); -- array in postgrs start from 1

            select *
            into p_r_les_to_sch
            from model.lessons_to_schedules
            where schedule_id = p_schedule_id
              and position = i
              and is_actual = 'true';

            RAISE NOTICE 'Вызов функции(%) hash = %, lesson = %, | %', i, p_hash, p_r_les_to_sch.hash, p_hash != coalesce(p_r_les_to_sch.hash, 1987375157);
-- 1987375157 = empty lesson

            if FOUND or not p_is_empty then
                if FOUND and not p_is_empty then
                    if p_hash != p_r_les_to_sch.hash then
                        call model.delete_lesson(p_schedule_id, i);

                        RAISE NOTICE 'hash and';

                        update model.lessons_to_schedules
                        set is_actual = 'false'
                        where schedule_id = p_schedule_id
                          and position = i
                          and is_actual = 'true';

                        RAISE NOTICE 'hash ref = %, empty = %, single = % , json = %', p_lesson_ref, p_is_empty, p_is_single, p_less[i];

                        insert into model.lessons_to_schedules(schedule_id, position, lesson_ref_id, hash, is_single)
                        values (p_schedule_id, i, p_lesson_ref, p_hash, p_is_single);

                        --                     if p_is_empty = 'false' then
--                         insert into model.lessons_to_schedules(schedule_id, position, lesson_ref_id, hash, is_single)
--                         values (p_schedule_id, i, p_lesson_ref, p_hash, p_is_single);
--                     end if;
                    end if;
                end if;
            else
                call model.delete_lesson(p_schedule_id, i);

                RAISE NOTICE 'hash or ';

                update model.lessons_to_schedules
                set is_actual = 'false'
                where schedule_id = p_schedule_id
                  and position = i
                  and is_actual = 'true';

                if p_is_empty = 'false' then
                    insert into model.lessons_to_schedules(schedule_id, position, lesson_ref_id, hash, is_single)
                    values (p_schedule_id, i, p_lesson_ref, p_hash, p_is_single);
                end if;
            end if;
        end loop;

    po_result_msg := 'update_schedule: ok';

exception
    when others
        then
            declare
                exception_diag text;
            begin
                get stacked diagnostics exception_diag = pg_exception_context;
                po_result_msg := 'update_schedule: status: ' || SQLSTATE || ', ' || SQLERRM || ' CTX:' ||
                                 exception_diag;
                return;
            end;
end;
$$;

create procedure model.delete_lesson(pi_schedule_id int, pi_position int)
    language plpgsql
as
$$
declare
    p_is_single  bool;
    p_lesson_ref int;
    p_first_id   int;
    p_second_id  int;
begin
    select is_single, lesson_ref_id
    into p_is_single, p_lesson_ref
    from model.lessons_to_schedules
    where schedule_id = pi_schedule_id
      and position = pi_position
      and is_actual = 'false';

    delete
    from model.lessons_to_schedules
    where schedule_id = pi_schedule_id
      and position = pi_position
      and is_actual = 'false';

    if p_is_single = 'true' then
        delete
        from model.lessons
        where id = p_lesson_ref;
    else
        select first_lesson_id, second_lesson_id
        into p_first_id, p_second_id
        from model.pair_lesson
        where id = p_lesson_ref;

        delete
        from model.pair_lesson
        where id = p_lesson_ref;

        delete
        from model.lessons
        where id = p_first_id;

        delete
        from model.lessons
        where id = p_second_id;
    end if;

    delete
    from model.lessons_to_schedules
    where schedule_id = pi_schedule_id
      and position = pi_position
      and is_actual = 'false';
end;
$$;

create function model.insert_schedule(pi_schedule model.schedule,
                                      out po_result_msg text)
    language plpgsql
as
$$
declare
    p_less          model.lessons_json;
    p_is_empty      bool;
    p_is_single     bool;
    p_hash          int;
    p_lesson_ref    int;
    p_group_name_id int;
    p_schedule_id   int;
begin

    p_less = pi_schedule.lessons;

    select id into p_group_name_id from model.group_names where name = pi_schedule.name;
    if not FOUND then
        insert into model.group_names(name) values (pi_schedule.name);
        select currval(pg_get_serial_sequence('model.group_names', 'id')) into p_group_name_id;
    end if;

    insert into model.schedules(name_id, date, hash) values (p_group_name_id, pi_schedule.date, pi_schedule.hash);
    select currval(pg_get_serial_sequence('model.schedules', 'id')) into p_schedule_id;

    for i in 0..7
        loop
            p_hash := (p_less[i] #>> '{hash}')::int; -- save hash

            select po_lesson_ref, po_is_empty, po_is_single
            into p_lesson_ref, p_is_empty, p_is_single
            from model.get_lesson_ref(p_less[i]);

            if p_is_empty = 'false' then
                insert into model.lessons_to_schedules(schedule_id, position, lesson_ref_id, hash, is_single)
                values (p_schedule_id, i, p_lesson_ref, p_hash, p_is_single);
            end if;
        end loop;

    po_result_msg := 'insert_schedule: ok';

exception
    when others
        then
            declare
                exception_diag text;
            begin
                get stacked diagnostics exception_diag = pg_exception_context;
                po_result_msg := 'insert_schedule: status: ' || SQLSTATE || ', ' || SQLERRM || ' CTX:' ||
                                 exception_diag;
                return;
            end;
end;
$$;

-- create new lesson returning lessons_id or pair_lesson_id
create function model.get_lesson_ref(pi_lesson jsonb, out po_lesson_ref int,
                                     out po_is_empty bool, out po_is_single bool)
    language plpgsql
as
$$
declare
    p_first_id  int;
    p_second_id int;
begin
    po_is_empty := 'true';

    if pi_lesson #>> '{type}' = 'PairLesson' then
        --              is single flag (false)
        po_is_single := 'false';

        if pi_lesson #>> '{pair,first,type}' != 'Empty' then
            select model.insert_lesson(pi_lesson, 'pair,first') into p_first_id;
            po_is_empty := 'false';
        else
            p_first_id := null;
        end if;

        if pi_lesson #>> '{pair,second,type}' != 'Empty' then
            select model.insert_lesson(pi_lesson, 'pair,second') into p_second_id;
            po_is_empty := 'false';
        else
            p_second_id := null;
        end if;

        if po_is_empty != true then
            insert into model.pair_lesson(first_lesson_id, second_lesson_id) values (p_first_id, p_second_id);
            select currval(pg_get_serial_sequence('model.pair_lesson', 'id')) into po_lesson_ref;
        end if;
    else
--               is single flag (true)
        po_is_single = 'true';

        if pi_lesson #>> '{lesson,lesson,type}' != 'Empty' then
            select model.insert_lesson(pi_lesson, 'lesson,lesson') into po_lesson_ref;
            po_is_empty := 'false';
        end if;
    end if;
end;
$$;

create function model.insert_lesson(pi_lesson jsonb, pi_path text, out po_id int)
    language plpgsql
as
$$
declare
    p_teacher_id      int;
    p_teacher_name    varchar(80);
--     p_teacher_post_id int;
--     p_teacher_post    text;
    p_discipline_id   int;
    p_discipline_name text;
begin

    -- is exists discipline
    p_discipline_name = pi_lesson #>> ('{' || pi_path || ',name}')::text[];
    select id into p_discipline_id from model.disciplines where name = p_discipline_name;
    if not FOUND then
        insert into model.disciplines(name) values (p_discipline_name);
        select currval(pg_get_serial_sequence('model.disciplines', 'id')) into p_discipline_id;
    end if;

    -- will implement in next release
    -- if not exist create new
    -- is exists teacher post
--     p_teacher_post = pi_lesson #>> ('{' || pi_path || ',teacherPost}')::text[];
--     select id into p_teacher_post_id from model.teacher_posts where name = p_teacher_post;
--     if not FOUND then
--         insert into model.teacher_posts(name) values (p_teacher_name);
--         select currval(pg_get_serial_sequence('model.teacher_posts', 'id')) into p_teacher_post_id;
--     end if;

    -- is exists teacher
    p_teacher_name = pi_lesson #>> ('{' || pi_path || ',teacherName}')::text[];
    select id into p_teacher_id from model.teachers where name = p_teacher_name;
    if not FOUND then
        insert into model.teachers(name) values (p_teacher_name);
        select currval(pg_get_serial_sequence('model.teachers', 'id')) into p_teacher_id;
    end if;

    insert into model.lessons(discipline_id, teacher_id, type, auditorium)
    values (p_discipline_id, p_teacher_id, pi_lesson #>> ('{' || pi_path || ',lessonType}')::text[],
            pi_lesson #>> ('{' || pi_path || ',auditorium}')::text[]);

    select currval(pg_get_serial_sequence('model.lessons', 'id')) into po_id;
end;
$$;