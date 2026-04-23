-- ============================================
-- Script de creación de base de datos
-- ms-user-registration - H2 Database
-- ============================================

CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    modified_at TIMESTAMP NOT NULL,
    last_login TIMESTAMP NOT NULL,
    token VARCHAR(512),
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX IF NOT EXISTS idx_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_token ON users(token);

CREATE TABLE IF NOT EXISTS user_phones (
    user_id UUID NOT NULL,
    number VARCHAR(15) NOT NULL,
    city_code VARCHAR(5) NOT NULL,
    country_code VARCHAR(4) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_user_phones_user_id ON user_phones(user_id);