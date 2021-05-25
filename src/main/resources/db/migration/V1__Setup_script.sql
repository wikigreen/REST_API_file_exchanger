create table if not exists accounts(
   id bigint auto_increment,
   user_id bigint,
   email varchar(255) not null,
   nickname varchar(30) not null,
   account_status varchar(15) not null,

   constraint accounts_id primary key (id)
);

create table if not exists users(
    id bigint auto_increment,
    account_id bigint,
    first_name varchar(50),
    last_name varchar(50),

    constraint users_pk primary key(id),
    constraint users_account_id__fk
        foreign key(account_id) references accounts(id)
            on delete set null
);

alter table accounts
    add constraint accounts_user_id__fk
        foreign key (user_id) references users(id)
            on delete set null;

create table if not exists files(
    id bigint auto_increment,
    file_name varchar(255),
    file_size_in_bits bigint,
    file_status varchar(10),
    upload_date timestamp not null default current_timestamp,
    update_date timestamp not null default current_timestamp on update current_timestamp,
    user_id bigint,

    constraint files_pk primary key (id),
    constraint files_user__fk foreign key (user_id)
        references users(id)
        on delete set null
);

create table if not exists events(
     id bigint auto_increment,
     event_type varchar(15),
     event_date timestamp not null default current_timestamp,
     file_id bigint,

     constraint events_pk primary key (id),
     constraint events_file__fk foreign key (file_id)
         references files(id)
         on delete set null
);

create table if not exists users_events(
   user_id bigint not null,
   event_id bigint not null,

   constraint users_events_user_id__fk
       foreign key (user_id)
           references users(id)
           on delete cascade,

   constraint users_events_event_id__fk
       foreign key (event_id)
           references events(id)
           on delete cascade
);