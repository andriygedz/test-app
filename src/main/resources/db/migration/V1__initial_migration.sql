CREATE TABLE IF NOT EXISTS epaper
(
    id             serial PRIMARY KEY,
    newspaper_name VARCHAR(255) not null,
    width          int          not null,
    height         int          not null,
    dpi            int          not null,
    created        TIMESTAMP    not null default now(),
    file_name      VARCHAR(255) not null
)
