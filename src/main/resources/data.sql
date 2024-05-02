INSERT INTO users (id, name, email, password, created, modified, last_login, token, is_active) VALUES
    ('123e4567-e89b-12d3-a456-426614174000', 'Geovanny Mendoza', 'geovanny.mendoza@gmail.com', 'Password123!', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '', TRUE);

INSERT INTO phones (id, user_id, number, city_code, country_code) VALUES
    ('49a537ea-9ab0-413e-8d7c-f5f7749368fc','123e4567-e89b-12d3-a456-426614174000', '1234567890', '1', '1');