CREATE TABLE IF NOT EXISTS "user" (
    id             BIGSERIAL PRIMARY KEY,
    username       VARCHAR(100) NOT NULL UNIQUE,
    email          VARCHAR(255) NOT NULL UNIQUE,
    password_hash  VARCHAR(255) NOT NULL,
    created_at     TIMESTAMP WITH TIME ZONE DEFAULT now()
);
CREATE TABLE IF NOT EXISTS "transaction" (
    id                  BIGSERIAL PRIMARY KEY,
    user_id             BIGINT NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    purchase_id         BIGINT,
    tx_points           INTEGER NOT NULL,
    available_points    INTEGER NOT NULL DEFAULT 0,
    expired             BOOLEAN NOT NULL DEFAULT FALSE,
    created_at          TIMESTAMP WITH TIME ZONE DEFAULT now()
);


CREATE INDEX IF NOT EXISTS idx_transaction_user ON "transaction"(user_id);
CREATE UNIQUE INDEX IF NOT EXISTS uq_transaction_purchase_id ON "transaction"(purchase_id);

INSERT INTO "user" (username, email, password_hash, created_at) VALUES
  ('alice','alice@example.com','pwdhash1', CURRENT_TIMESTAMP),
  ('bob','bob@example.com','pwdhash2', CURRENT_TIMESTAMP),
  ('carol','carol@example.com','pwdhash3', CURRENT_TIMESTAMP),
  ('dave','dave@example.com','pwdhash4', CURRENT_TIMESTAMP),
  ('eve','eve@example.com','pwdhash5', CURRENT_TIMESTAMP);

INSERT INTO "transaction" (user_id, purchase_id, tx_points, available_points, expired, created_at, transaction_type)
VALUES
  ((SELECT id FROM "user" WHERE username='alice'), 1, 100, 100, FALSE, CURRENT_TIMESTAMP, 'EARNING'),
  ((SELECT id FROM "user" WHERE username='alice'), 2, 25, 25, FALSE, CURRENT_TIMESTAMP, 'EARNING'),
  ((SELECT id FROM "user" WHERE username='bob'), 3, 150, 150, FALSE, CURRENT_TIMESTAMP, 'EARNING'),
  ((SELECT id FROM "user" WHERE username='bob'), 4, 50, 50, FALSE, CURRENT_TIMESTAMP, 'EARNING'),
  ((SELECT id FROM "user" WHERE username='carol'), 5, 200, 200, FALSE, CURRENT_TIMESTAMP, 'EARNING'),
  ((SELECT id FROM "user" WHERE username='carol'), 6, 75, 75, FALSE, CURRENT_TIMESTAMP, 'EARNING'),
  ((SELECT id FROM "user" WHERE username='dave'), 7, 50, 50, FALSE, CURRENT_TIMESTAMP, 'EARNING'),
  ((SELECT id FROM "user" WHERE username='dave'), 8, 10, 10, FALSE, CURRENT_TIMESTAMP, 'EARNING'),
  ((SELECT id FROM "user" WHERE username='eve'), 9, 300, 300, FALSE, CURRENT_TIMESTAMP, 'EARNING'),
  ((SELECT id FROM "user" WHERE username='eve'), 10, 120, 120, FALSE, CURRENT_TIMESTAMP, 'EARNING');

