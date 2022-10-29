CREATE TABLE IF NOT EXISTS accounts(
    account_id serial PRIMARY KEY,
    customer_id INT NOT NULL
);

CREATE TABLE IF NOT EXISTS balances(
    balance_id serial PRIMARY KEY,
    account_id INT NOT NULL,
    currency VARCHAR (3) NOT NULL,
    amount DECIMAL NOT NULL
);

CREATE TABLE IF NOT EXISTS transactions(
    transaction_id serial PRIMARY KEY,
    balance_id INT NOT NULL,
    amount DECIMAL NOT NULL,
    direction VARCHAR (3) NOT NULL,
    description VARCHAR (250) NOT NULL
);