--drop table requests, bookings,  comments, items, users;

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar NOT NULL,
    email varchar NOT NULL,
    CONSTRAINT UNIQUE_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    description varchar NOT NULL,
    created TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS items (
    item_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar NOT NULL,
    description varchar NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    available bool NOT NULL,
    request_id BIGINT REFERENCES requests (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    item_id BIGINT NOT NULL REFERENCES items (item_id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    status varchar
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text varchar NOT NULL,
    item_id BIGINT NOT NULL REFERENCES items (item_id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    created timestamp NOT NULL
);
