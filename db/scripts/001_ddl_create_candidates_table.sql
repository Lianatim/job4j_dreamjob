CREATE TABLE candidate
(
    id SERIAL PRIMARY KEY,
    name TEXT,
    description TEXT,
    photo BYTEA,
    city_id INTEGER,
    visible BOOLEAN,
    created TIMESTAMP
);