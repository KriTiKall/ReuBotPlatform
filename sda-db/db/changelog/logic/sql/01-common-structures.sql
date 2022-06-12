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
    hash    int  not null,
    unique (name_id, date)
);

comment on table model.schedules is 'Basic schedule info';
comment on column model.schedules.id is 'ID';
comment on column model.schedules.name_id is 'Id on group name table';
comment on column model.schedules.date is 'Schedule date';
comment on column model.schedules.hash is 'Group name';

create table if not exists model.group_names
(
    id   serial primary key,
    name varchar(10) not null
);

create table if not exists model.lessons_to_schedules
(
    schedule_id   int         not null,
    position      int         not null check ( position < 9 and position >= 1),
    lesson_ref_id int         not null,
    hash          int         not null,
    update_time   timestamptz not null default now(),
    is_single     bool        not null,
    is_actual     bool        not null default true,
    unique (schedule_id, position, is_actual)
);

set timezone = '+5';

create table if not exists model.pair_lesson
(
    id               serial primary key,
    first_lesson_id  int,
    second_lesson_id int
);

create table if not exists model.lessons
(
    id            serial primary key,
    discipline_id int         not null,
    teacher_id    int         not null,
    type          varchar(50) not null,
    auditorium    varchar(30) not null
);

create table if not exists model.disciplines
(
    id   serial primary key,
    name text
);
-- dont use
create table if not exists model.teacher_posts
(
    id   serial primary key,
    name text
);

create table if not exists model.teachers
(
    id      serial primary key,
    post_id int default null,
    name    varchar(80)
);