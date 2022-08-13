DROP table IF EXISTS users, items, bookings;

CREATE TABLE IF NOT EXISTS users
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_name varchar(320) NOT NULL,
    email     varchar(320) UNIQUE
);

CREATE TABLE IF NOT EXISTS items
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    item_name   VARCHAR NOT NULL,
    description VARCHAR,
    available   BOOLEAN,
    owner_id    BIGINT REFERENCES users (id),
    request     BIGINT
);

CREATE TABLE IF NOT EXISTS bookings
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    start_date BIGINT,
    end_date   BIGINT,
    item_id    BIGINT REFERENCES items (id),
    booker_id  BIGINT REFERENCES users (id),
    status     VARCHAR
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description  VARCHAR,
    requester_id BIGINT
);

CREATE TABLE IF NOT EXISTS comments
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    comment_text VARCHAR,
    item_id      BIGINT,
    author_id    BIGINT
);