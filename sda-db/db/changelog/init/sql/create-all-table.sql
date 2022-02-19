create schema if not exists model;

create domain model.lessons_json as jsonb[8] check (array_ndims(value) = 1 AND array_length(value, 1) = 8);

comment on domain model.lessons_json is 'Jsonb array with constrain on size';

create type model.schedule as
(
    name    varchar(10),
    date    date,
    hash    int,
    lessons model.lessons_json
);

comment on type model.schedule is 'Store object data from application server';

create table if not exists model.schedules
(
    id   serial primary key,
    name varchar(10) not null,
    date date        not null,
    hash int         not null
);

comment on table model.schedules is 'Basic schedule info';
comment on column model.schedules.id is 'ID';
comment on column model.schedules.name is 'Group name';
comment on column model.schedules.date is 'Schedule date';
comment on column model.schedules.hash is 'Group name';

create type model.entity_lesson as
(
    first_id  int,
    second_id int,
    is_single bool
);

create table if not exists model.lessons_to_schedule
(
    schedule_id int                 not null,
    position    int                 not null check ( value < 8 and value >= 0),
    hash        int                 not null,
    lesson_ids  model.entity_lesson not null
);

create table if not exists model.disciplines
(
    id   serial primary key,
    name text
);

create table if not exists model.teacher_posts
(
    id   serial primary key,
    name text
);

create table if not exists model.teachers
(
    id      serial primary key,
    post_id int,
    name    varchar(80)
);

create table if not exists model.lessons
(
    id            serial primary key,
    discipline_id int         not null,
    teacher_id    int         not null,
    auditorium    varchar(20) not null
);

-- In development
-- create table if not exists model.schedule_updates
-- (
--     id          serial primary key,
--     schedule_id int       not null,
--     uploaded    timestamp not null,
--     last_update timestamp
-- );


create function model.insert_or_update_schedule(pi_schedule model.schedule, out po_result_msg text)
    language plpgsql
as
$$
declare
    schedule_id   int;
    schedule_hash int;
    result_test   text;
begin

    select into schedule_id id, schedule_hash hash
    from model.schedules
    where date = pi_schedule.date
      and name = pi_schedule.name;

    if FOUND then
        if schedule_hash != pi_schedule.hash then

        end if;
    else
        select model.insert_schedule(pi_schedule) into result_test;
    end if;


    po_result_msg := result_test || ', ok';

exception
    when others
        then
            declare
                exception_diag text;
            begin
                get stacked diagnostics exception_diag = pg_exception_context;
                po_result_msg := result_test || ', status: ' || SQLSTATE || ', ' || SQLERRM || ' CTX:' || exception_diag;
                return;
            end;
end;
$$;

create function model.insert_schedule(pi_schedule model.schedule,
                                      out po_result_msg text)
    language plpgsql
as
$$
declare
    les_pos   model.entity_lesson;
    less      model.lessons_json;
    first_id  int;
    second_id int;
    sch_id    int;
begin

    less = pi_schedule.lessons;

    insert into model.schedules values (pi_schedule.name, pi_schedule.date, pi_schedule.hash);
    select currval(pg_get_serial_sequence('model.schedules', 'id')) into sch_id;

    for i in 0..7
        loop
            if less[i] ->> 'type' = 'model.entity.PairLesson' then
--              is single flag (false)
                les_pos.is_single = 'false';

                if less[i] #>> '{pair,first,type}' != 'Empty' then
                    select model.insert_lesson(less[i], 'pair,first') into first_id;
                    les_pos.first_id := first_id;
                else
                    les_pos.first_id := null;
                end if;

                if less[i] #>> '{pair,second,type}' != 'Empty' then
                    select model.insert_lesson(less[i], 'pair,second') into second_id;
                    les_pos.second_id := second_id;
                else
                    les_pos.second_id := null;
                end if;
            else
--               is single flag (true)
                les_pos.is_single = 'true';

                if less[i] #>> '{lesson,type}' != 'Empty' then
                    select model.insert_lesson(less[i], 'lesson') into first_id;
                    les_pos.first_id := first_id;
                else
                    les_pos.first_id := null;
                end if;
            end if;

            insert into model.lessons_to_schedule(schedule_id, position, hash, lesson_ids)
            values (sch_id, i, pi_schedule.hash, les_pos);
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

create function model.insert_lesson(pi_lesson jsonb, pi_path text, out po_id int)
    language plpgsql
as
$$
declare
    p_teacher_id      int;
    p_teacher_name    varchar(80);
    p_teacher_post_id int;
    p_teacher_post text;
    p_discipline_id   int;
    p_discipline_name text;
begin

    -- is exists teacher
    p_teacher_post = pi_lesson #>> '{' || pi_path || ',teacherPost}';

    select id into p_teacher_post_id from model.teacher_posts where name = p_teacher_post;
    if not FOUND then
        insert into model.teacher_posts(name) values (p_teacher_name);
        select currval(pg_get_serial_sequence('model.teacher_posts', 'id')) into p_teacher_post_id;
    end if;

    -- is exists teacher
    p_teacher_name = pi_lesson #>> '{' || pi_path || ',teacherName}';

    select id into p_teacher_id from model.teachers where name = p_teacher_name;
    if not FOUND then
        insert into model.teachers(post_id, name) values (p_teacher_post_id, p_teacher_name);
        select currval(pg_get_serial_sequence('model.teachers', 'id')) into p_teacher_id;
    end if;

    -- is exists discipline
    p_discipline_name = pi_lesson #>> '{' || pi_path || ',name}';

    select id into p_discipline_id from model.disciplines where name = p_discipline_id;
    if not FOUND then
        insert into model.disciplines values (p_discipline_name);
        select currval(pg_get_serial_sequence('model.disciplines', 'id')) into p_discipline_id;
    end if;

    insert into model.lessons(discipline_id, teacher_id, auditorium)
    values (p_discipline_id, p_teacher_id, pi_lesson #>> '{' || pi_path || ',auditorium}');

    select currval(pg_get_serial_sequence('model.lessons', 'id')) into po_id;
end;
$$;