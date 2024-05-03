INSERT INTO users (id, name, email, password, created, modified, last_login, token, is_active, role) VALUES
    ('123e4567-e89b-12d3-a456-426614174000', 'Geovanny Mendoza', 'geovanny.mendoza@gmail.com', '$2b$10$pjFpBi8RPzF9ZsLLCTsX4OVNnlY2zdZxEQyT69dhlKmhVp/qhgA7C', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJnZW92YW5ueS5tZW5kb3phQGdtYWlsLmNvbSIsIm5hbWUiOiJHZW92YW5ueSBNZW5kb3phIiwiaWF0IjoxNTE2MjM5MDIyfQ.mb82GyDctVPHZYfFndocRrfvpLLFmcPxw-DSyssyUnuijqpwIYqXWLGPqaDpkYnd3gLA1Xh0U7hj_3EyaG4UBw', TRUE, 'ADMIN');

INSERT INTO phones (id, user_id, number, city_code, country_code) VALUES
    ('49a537ea-9ab0-413e-8d7c-f5f7749368fc','123e4567-e89b-12d3-a456-426614174000', '1234567890', '1', '1');