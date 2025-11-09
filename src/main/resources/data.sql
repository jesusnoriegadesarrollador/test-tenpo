CREATE TABLE IF NOT EXISTS history (
    id SERIAL PRIMARY KEY,
    date TIMESTAMP NOT NULL,
    endpoint TEXT,
    params TEXT,
    response TEXT,
    error TEXT
);
