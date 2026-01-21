#drop database immunizer; 
create database immunizer;
use immunizer;
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    date_of_birth DATE,
    gender VARCHAR(50),
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    password VARCHAR(255) NOT NULL
);

CREATE TABLE vaccines (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    disease VARCHAR(255),
    total_doses INT,
    gap_between_doses INT,
    booster_required BOOLEAN,
    booster_after_days INT
);

CREATE TABLE schedule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    period INT NOT NULL UNIQUE
);

CREATE TABLE vaccine_dose (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    vaccine_id BIGINT NOT NULL,

    dose_number INT NOT NULL,          
    min_gap_days INT,                 

    CONSTRAINT fk_vd_vaccine
        FOREIGN KEY (vaccine_id)
        REFERENCES vaccines(id)
        ON DELETE CASCADE,

    UNIQUE (vaccine_id, dose_number)
);

CREATE TABLE vaccine_schedule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    vaccine_id BIGINT NOT NULL,

    start_period_id BIGINT NOT NULL,
    end_period_id   BIGINT NOT NULL,

    mandatory BOOLEAN NOT NULL,

    CONSTRAINT fk_vs_vaccine
        FOREIGN KEY (vaccine_id)
        REFERENCES vaccines(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_vs_start_period
        FOREIGN KEY (start_period_id)
        REFERENCES schedule(id),

    CONSTRAINT fk_vs_end_period
        FOREIGN KEY (end_period_id)
        REFERENCES schedule(id),

    CHECK (start_period_id <= end_period_id)
);

CREATE TABLE user_vaccines (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    user_id BIGINT NOT NULL,
    vaccine_id BIGINT NOT NULL,

    dose_number INT,
    administered_date DATE,

    next_dose_date DATE,
    completed BOOLEAN,
    overdue BOOLEAN,

    administered_by VARCHAR(255),
    notes TEXT,

    CONSTRAINT fk_user_vaccines_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_vaccines_vaccine
        FOREIGN KEY (vaccine_id) REFERENCES vaccines(id)
        ON DELETE CASCADE
);

INSERT INTO schedule (period) VALUES
(0),    -- At birth
(1),    -- 1 month
(2),    -- 2 months
(3),    -- 3 months
(6),    -- 6 months
(9),    -- 9 months
(12),   -- 1 year
(15),   -- 15 months
(18),   -- 18 months
(24),   -- 2 years
(36),   -- 3 years
(48),   -- 4 years
(60),   -- 5 years
(120);  -- 10 years

INSERT INTO vaccines (name, disease, total_doses, booster_required, booster_after_days) VALUES
('BCG', 'Tuberculosis', 1, false, NULL),
('Hepatitis B', 'Hepatitis B', 3, false, NULL),
('OPV', 'Poliomyelitis', 4, true, 1825),   -- booster after ~5 years
('IPV', 'Poliomyelitis', 3, true, 1825),
('DTP', 'Diphtheria, Tetanus, Pertussis', 5, true, 1825),
('Hib', 'Haemophilus influenzae type b', 3, false, NULL),
('Rotavirus', 'Rotavirus', 3, false, NULL),
('PCV', 'Pneumococcal disease', 3, true, 1825),
('MMR', 'Measles, Mumps, Rubella', 2, false, NULL),
('Varicella', 'Varicella', 2, false, NULL),
('JE', 'Japanese Encephalitis', 2, true, 3650), -- booster after ~10 years
('HPV', 'Human Papillomavirus', 2, false, NULL),
('Influenza', 'Influenza', 1, true, 365); -- annual booster

INSERT INTO vaccine_dose (vaccine_id, dose_number, min_gap_days) VALUES (1, 1, NULL);
INSERT INTO vaccine_dose (vaccine_id, dose_number, min_gap_days) VALUES
(2, 1, NULL),
(2, 2, 30),
(2, 3, 180);

INSERT INTO vaccine_dose (vaccine_id, dose_number, min_gap_days)
VALUES
(3, 1, NULL),
(3, 2, 30),
(3, 3, 30),
(3, 4, 180);

INSERT INTO vaccine_dose (vaccine_id, dose_number, min_gap_days)
VALUES
(4, 1, NULL),
(4, 2, 60),
(4, 3, 180);

INSERT INTO vaccine_dose (vaccine_id, dose_number, min_gap_days)
VALUES
(5, 1, NULL),
(5, 2, 30),
(5, 3, 30),
(5, 4, 365),
(5, 5, 365);

INSERT INTO vaccine_dose (vaccine_id, dose_number, min_gap_days)
VALUES
(6, 1, NULL),
(6, 2, 30),
(6, 3, 180);

INSERT INTO vaccine_dose (vaccine_id, dose_number, min_gap_days)
VALUES
(7, 1, NULL),
(7, 2, 28),
(7, 3, 28);

INSERT INTO vaccine_dose (vaccine_id, dose_number, min_gap_days)
VALUES
(8, 1, NULL),
(8, 2, 60),
(8, 3, 180);

INSERT INTO vaccine_dose (vaccine_id, dose_number, min_gap_days)
VALUES
(9, 1, NULL),
(9, 2, 180);

INSERT INTO vaccine_dose (vaccine_id, dose_number, min_gap_days)
VALUES
(10, 1, NULL),
(10, 2, 180);

INSERT INTO vaccine_dose (vaccine_id, dose_number, min_gap_days)
VALUES
(11, 1, NULL),
(11, 2, 365);

INSERT INTO vaccine_dose (vaccine_id, dose_number, min_gap_days)
VALUES
(12, 1, NULL),
(12, 2, 180);

INSERT INTO vaccine_dose (vaccine_id, dose_number, min_gap_days)
VALUES
(13, 1, NULL);

INSERT INTO vaccine_schedule (vaccine_id, start_period_id, end_period_id, mandatory)
VALUES
(1, 1, 1, true);

INSERT INTO vaccine_schedule (vaccine_id, start_period_id, end_period_id, mandatory)
VALUES
(2, 1, 5, true);

INSERT INTO vaccine_schedule (vaccine_id, start_period_id, end_period_id, mandatory)
VALUES
(3, 1, 13, true);

INSERT INTO vaccine_schedule (vaccine_id, start_period_id, end_period_id, mandatory)
VALUES
(4, 3, 13, true);

INSERT INTO vaccine_schedule (vaccine_id, start_period_id, end_period_id, mandatory)
VALUES
(5, 3, 13, true);

INSERT INTO vaccine_schedule (vaccine_id, start_period_id, end_period_id, mandatory)
VALUES
(6, 3, 9, true);

INSERT INTO vaccine_schedule (vaccine_id, start_period_id, end_period_id, mandatory)
VALUES
(7, 3, 5, true);

INSERT INTO vaccine_schedule (vaccine_id, start_period_id, end_period_id, mandatory)
VALUES
(8, 3, 9, true);

INSERT INTO vaccine_schedule (vaccine_id, start_period_id, end_period_id, mandatory)
VALUES
(9, 6, 8, true);

INSERT INTO vaccine_schedule (vaccine_id, start_period_id, end_period_id, mandatory)
VALUES
(10, 7, 8, true);

INSERT INTO vaccine_schedule (vaccine_id, start_period_id, end_period_id, mandatory)
VALUES
(11, 6, 10, true);

INSERT INTO vaccine_schedule (vaccine_id, start_period_id, end_period_id, mandatory)
VALUES
(12, 14, 14, true);

INSERT INTO vaccine_schedule (vaccine_id, start_period_id, end_period_id, mandatory)
VALUES
(13, 5, 14, false);


-- truncate table user_vaccines;

-- select * from vaccine_dose where vaccine_id=4;
-- select * from vaccines where id=4;
-- select * from vaccine_schedule where vaccine_id=4;
-- select * from schedule;
-- select * from user_vaccines where vaccine_id=4;