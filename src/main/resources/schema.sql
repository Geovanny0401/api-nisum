DROP TABLE IF EXISTS phone;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
                      id UUID PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      email VARCHAR(255) UNIQUE,
                      password VARCHAR(255) NOT NULL,
                      created TIMESTAMP NOT NULL,
                      modified TIMESTAMP,
                      last_login TIMESTAMP NOT NULL,
                      token VARCHAR(255),
                      is_active BOOLEAN
);

CREATE TABLE phones (
                       id UUID PRIMARY KEY,
                       user_id UUID,
                       number VARCHAR(255),
                       city_code VARCHAR(10),
                       country_code VARCHAR(10),
                       CONSTRAINT FK_PHONE_USER_ID FOREIGN KEY (user_id) REFERENCES users (id)
);
