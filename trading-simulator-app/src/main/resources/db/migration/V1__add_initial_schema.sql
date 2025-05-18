-- Enable pgcrypto for UUID v4 generation
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Enums
CREATE TYPE instrument_type AS ENUM ('STOCK', 'ETF', 'BOND', 'CRYPTO', 'CURRENCY');
CREATE TYPE order_type AS ENUM ('BUY', 'SELL');
CREATE TYPE order_status AS ENUM ('NEW', 'PARTIALLY_FILLED', 'FILLED', 'CANCELLED');

-- Instrument table
CREATE TABLE instrument (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ticker TEXT UNIQUE NOT NULL,
    name TEXT NOT NULL,
    exchange TEXT,
    currency TEXT,
    sector TEXT,
    type instrument_type NOT NULL,
    created_at BIGINT NOT NULL,
    updated_at BIGINT NOT NULL
);

-- Orders table
CREATE TABLE orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    instrument_id UUID NOT NULL REFERENCES instrument(id),
    quantity INTEGER NOT NULL,
    price DECIMAL(19,4) NOT NULL,
    type order_type NOT NULL,
    status order_status NOT NULL,
    created_at BIGINT NOT NULL,
    updated_at BIGINT NOT NULL
);

-- Trade table
CREATE TABLE trade (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    buy_order_id UUID NOT NULL REFERENCES orders(id),
    sell_order_id UUID NOT NULL REFERENCES orders(id),
    instrument_id UUID NOT NULL REFERENCES instrument(id),
    quantity INTEGER NOT NULL,
    price DECIMAL(19,4) NOT NULL,
    timestamp BIGINT NOT NULL,
    created_at BIGINT NOT NULL,
    updated_at BIGINT NOT NULL
);
