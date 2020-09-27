# --- !Ups

CREATE TABLE user_role (
                    id serial PRIMARY KEY,
                    role varchar(32) NOT NULL,
                    creation_ts TIMESTAMP default now()
);

insert into user_role (role)
values ('USER');
insert into user_role (role)
values ('ADMIN');

CREATE TABLE users (
                    id UUID PRIMARY KEY,
                    email varchar(512),
                    username varchar(256),
                    password varchar(512),
                    role_id bigint REFERENCES user_role(id) default(1),
                    is_active boolean default(true),
                    createdat DATE,
                    UNIQUE(email),
                    UNIQUE(username)
);

CREATE TABLE articles (
                        id UUID PRIMARY KEY,
                        location varchar(512),
                        author varchar(256) REFERENCES users(username),
                        sex boolean,
                        description text,
                        contact varchar(256),
                        image varchar(512),
                        category varchar(64),
                        is_approved boolean,
                        created_at timestamp,
                        updated_at timestamp
)
# --- !Downs

DROP TABLE IF EXISTS "user";
DROP TABLE IF EXISTS user_role;