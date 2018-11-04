CREATE TABLE product
(
   	id  SERIAL PRIMARY KEY      NOT NULL,
    creation_date timestamp  not null,
    name text,
    price real
)

CREATE TABLE users
(
    id SERIAL PRIMARY KEY  NOT NULL,
    login text,
    password text,
    role text
)