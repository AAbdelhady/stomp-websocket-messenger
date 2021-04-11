create table if not exists conversations
(
    id       bigint primary key       not null,
    created  timestamp with time zone not null,
    modified timestamp with time zone not null
);
create sequence if not exists conversations_seq increment by 50;

create table if not exists conversation_participants
(
    conversation_id bigint                   not null,
    user_id         bigint                   not null,
    created         timestamp with time zone not null default current_timestamp,
    constraint pk_conversations_participants primary key (conversation_id, user_id),
    constraint fk_conversations_participants_conversation_id foreign key (conversation_id) references conversations (id) on update cascade on delete cascade,
    constraint fk_conversations_participants_user_id foreign key (user_id) references users (id) on update cascade on delete cascade
);

create table if not exists messages
(
    id              bigint primary key       not null,
    conversation_id bigint                   not null,
    sender_id       bigint                   not null,
    text            text                     not null,
    sent            timestamp with time zone not null,
    constraint fk_messages_conversation_id foreign key (conversation_id) references conversations (id) on update cascade on delete cascade,
    constraint fk_messages_sender_id foreign key (sender_id) references users (id) on update cascade on delete cascade
);
create sequence if not exists messages_seq increment by 50;
create index if not exists idx_messages_sender_id on messages (sender_id);
create index if not exists idx_messages_receiver_id on messages (conversation_id);
