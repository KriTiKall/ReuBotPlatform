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
    id      serial primary key,
    name_id int  not null,
    date    date not null,
    hash    int  not null
);

comment on table model.schedules is 'Basic schedule info';
comment on column model.schedules.id is 'ID';
comment on column model.schedules.name_id is 'Id on group name table';
comment on column model.schedules.date is 'Schedule date';
comment on column model.schedules.hash is 'Group name';

create table if not exists model.group_name
(
    id   serial primary key,
    name varchar(10) not null
);

create table if not exists model.lessons_to_schedule
(
    schedule_id int                 not null,
    position    int                 not null check ( value < 8 and value >= 0),
    hash        int                 not null,
    lesson_ids  model.entity_lesson not null
);

create type model.entity_lesson as
(
    first_id  int,
    second_id int,
    is_single bool
);

create table if not exists model.lessons
(
    id            serial primary key,
    discipline_id int         not null,
    teacher_id    int         not null,
    auditorium    varchar(20) not null
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
    from model.schedules sch join model.group_name gn on gn.id = sch.name_id
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
                po_result_msg := result_test || ', status: ' || SQLSTATE || ', ' || SQLERRM || ' CTX:' ||
                                 exception_diag;
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
    p_les_pos       model.entity_lesson;
    p_less          model.lessons_json;
    p_group_name_id int;
    p_first_id      int;
    p_second_id     int;
    p_schedule_id   int;
begin

    p_less = pi_schedule.lessons;

    select id into p_group_name_id from model.teacher_posts where name = pi_schedule.name;
    if not FOUND then
        insert into model.group_name(name) values (pi_schedule.name);
        select currval(pg_get_serial_sequence('model.group_name', 'id')) into p_group_name_id;
    end if;

    insert into model.schedules values (pi_schedule.name, pi_schedule.date, pi_schedule.hash);
    select currval(pg_get_serial_sequence('model.schedules', 'id')) into p_schedule_id;

    for i in 0..7
        loop
            if p_less[i] ->> 'type' = 'model.entity.PairLesson' then
--              is single flag (false)
                p_les_pos.is_single = 'false';

                if p_less[i] #>> '{pair,first,type}' != 'Empty' then
                    select model.insert_lesson(p_less[i], 'pair,first') into p_first_id;
                    p_les_pos.first_id := p_first_id;
                else
                    p_les_pos.first_id := null;
                end if;

                if p_less[i] #>> '{pair,second,type}' != 'Empty' then
                    select model.insert_lesson(p_less[i], 'pair,second') into p_second_id;
                    p_les_pos.second_id := p_second_id;
                else
                    p_les_pos.second_id := null;
                end if;
            else
--               is single flag (true)
                p_les_pos.is_single = 'true';

                if p_less[i] #>> '{lesson,type}' != 'Empty' then
                    select model.insert_lesson(p_less[i], 'lesson') into p_first_id;
                    p_les_pos.first_id := p_first_id;
                else
                    p_les_pos.first_id := null;
                end if;
            end if;

            insert into model.lessons_to_schedule(schedule_id, position, hash, lesson_ids)
            values (p_schedule_id, i, pi_schedule.hash, p_les_pos);
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
    p_teacher_post    text;
    p_discipline_id   int;
    p_discipline_name text;
begin
    -- if not exist create new
    -- is exists teacher post
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