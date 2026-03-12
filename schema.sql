CREATE TABLE IF NOT EXISTS short_urls (
    id SERIAL PRIMARY KEY,
    full_url TEXT NOT NULL,
    short_code VARCHAR(10) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
);
