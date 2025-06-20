-- Очистка таблиц
TRUNCATE TABLE time_slots CASCADE;
TRUNCATE TABLE user_role CASCADE;
TRUNCATE TABLE users_images CASCADE;
TRUNCATE TABLE employees CASCADE;
TRUNCATE TABLE users CASCADE;
TRUNCATE TABLE haircut_categories CASCADE;

-- Добавление сотрудников
INSERT INTO users (first_name, last_name, email, password, phone, created_at, state) VALUES
('Александр', 'Петров', 'petrov@barbershop.com', '$2a$10$YourHashedPassword', '+7(999)111-22-33', CURRENT_TIMESTAMP, 'CONFIRMED'),
('Михаил', 'Иванов', 'ivanov@barbershop.com', '$2a$10$YourHashedPassword', '+7(999)222-33-44', CURRENT_TIMESTAMP, 'CONFIRMED'),
('Дмитрий', 'Сидоров', 'sidorov@barbershop.com', '$2a$10$YourHashedPassword', '+7(999)333-44-55', CURRENT_TIMESTAMP, 'CONFIRMED'),
('Артем', 'Козлов', 'kozlov@barbershop.com', '$2a$10$YourHashedPassword', '+7(999)444-55-66', CURRENT_TIMESTAMP, 'CONFIRMED'),
('Владимир', 'Морозов', 'morozov@barbershop.com', '$2a$10$YourHashedPassword', '+7(999)555-66-77', CURRENT_TIMESTAMP, 'CONFIRMED'),
('Сергей', 'Волков', 'volkov@barbershop.com', '$2a$10$YourHashedPassword', '+7(999)666-77-88', CURRENT_TIMESTAMP, 'CONFIRMED');

-- Добавление ролей сотрудникам (используем подзапрос для получения id)
INSERT INTO user_role (user_id, role)
SELECT id, 'EMPLOYEE' FROM users WHERE email LIKE '%@barbershop.com'
UNION ALL
SELECT id, 'BARBER' FROM users WHERE email LIKE '%@barbershop.com';

-- Добавление записей в таблицу employees
INSERT INTO employees (id, experience_years, salary)
SELECT id, 
       CASE 
           WHEN email = 'petrov@barbershop.com' THEN 5
           WHEN email = 'ivanov@barbershop.com' THEN 7
           WHEN email = 'sidorov@barbershop.com' THEN 3
           WHEN email = 'kozlov@barbershop.com' THEN 8
           WHEN email = 'morozov@barbershop.com' THEN 4
           WHEN email = 'volkov@barbershop.com' THEN 6
       END,
       CASE 
           WHEN email = 'petrov@barbershop.com' THEN 80000.00
           WHEN email = 'ivanov@barbershop.com' THEN 90000.00
           WHEN email = 'sidorov@barbershop.com' THEN 70000.00
           WHEN email = 'kozlov@barbershop.com' THEN 95000.00
           WHEN email = 'morozov@barbershop.com' THEN 75000.00
           WHEN email = 'volkov@barbershop.com' THEN 85000.00
       END
FROM users 
WHERE email LIKE '%@barbershop.com';

-- Добавление фотографий сотрудников
INSERT INTO users_images (storage_file_name, size, content_type, user_id)
SELECT 'Frame_67.png', 1024000, 'image/png', id
FROM users 
WHERE email LIKE '%@barbershop.com';

-- Добавление временных слотов для каждого барбера
INSERT INTO time_slots (employee_id, start_time, end_time, booked)
SELECT 
    e.id,
    gs + INTERVAL '9 hours',
    gs + INTERVAL '10 hours',
    false
FROM employees e
CROSS JOIN generate_series(CURRENT_DATE, CURRENT_DATE + INTERVAL '7 days', INTERVAL '1 hour') gs
WHERE EXTRACT(HOUR FROM gs) BETWEEN 9 AND 19
AND EXTRACT(DOW FROM gs) NOT IN (0, 6); -- Исключаем воскресенье (0) и субботу (6)

-- Добавление категорий стрижек
INSERT INTO haircut_categories (name, price) VALUES
('Мужская стрижка', 1200.00),
('Детская стрижка', 900.00),
('Стрижка машинкой', 800.00),
('Бритьё головы', 700.00),
('Королевское бритьё', 1500.00),
('Окантовка', 500.00),
('Стрижка + мытьё', 1400.00),
('Модельная стрижка', 1600.00),
('Камуфляж седины', 1800.00),
('Стрижка бороды', 1000.00),
('Оформление усов', 600.00),
('Комплекс: стрижка + борода', 2000.00);
