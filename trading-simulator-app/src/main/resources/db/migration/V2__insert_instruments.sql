INSERT INTO instrument (id, ticker, name, exchange, currency, sector, type, created_at, updated_at)
VALUES
  (gen_random_uuid(), 'AAPL', 'Apple Inc.', 'NASDAQ', 'USD', 'Technology', 'STOCK', EXTRACT(EPOCH FROM now()) * 1000, EXTRACT(EPOCH FROM now()) * 1000),
  (gen_random_uuid(), 'TSLA', 'Tesla Inc.', 'NASDAQ', 'USD', 'Automotive', 'STOCK', EXTRACT(EPOCH FROM now()) * 1000, EXTRACT(EPOCH FROM now()) * 1000),
  (gen_random_uuid(), 'BTC', 'Bitcoin', 'BINANCE', 'USD', 'Crypto', 'CRYPTO', EXTRACT(EPOCH FROM now()) * 1000, EXTRACT(EPOCH FROM now()) * 1000),
  (gen_random_uuid(), 'VOO', 'Vanguard S&P 500 ETF', 'NYSEARCA', 'USD', 'ETF', 'ETF', EXTRACT(EPOCH FROM now()) * 1000, EXTRACT(EPOCH FROM now()) * 1000);
