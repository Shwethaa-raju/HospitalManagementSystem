CREATE TABLE IF NOT EXISTS doctor (
                                      doctor_id UUID PRIMARY KEY,
                                      name VARCHAR(100) NOT NULL,
    age INT CHECK (age > 21),
    phone_no VARCHAR(15) UNIQUE NOT NULL,
    degrees VARCHAR(200),
    speciality VARCHAR(100) NOT NULL,
    experience INT CHECK (experience >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

INSERT INTO doctor (doctor_id, name, age, phone_no, degrees, speciality, experience)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'Dr. Arun Kumar', 45, '9876543210', 'MBBS, MD', 'Cardiology', 18),
    ('22222222-2222-2222-2222-222222222222', 'Dr. Meera Iyer', 38, '9876543211', 'MBBS, DGO', 'Gynecology', 12),
    ('33333333-3333-3333-3333-333333333333', 'Dr. Rajesh Patel', 50, '9876543212', 'MBBS, MS', 'Orthopedics', 22),
    ('44444444-4444-4444-4444-444444444444', 'Dr. Ananya Sharma', 34, '9876543213', 'MBBS, MD', 'Dermatology', 8);


CREATE TABLE IF NOT EXISTS doctor_schedule (
                                               schedule_id UUID PRIMARY KEY,
                                               doctor_id UUID NOT NULL,
                                               day_of_week INT NOT NULL CHECK (day_of_week BETWEEN 0 AND 6),
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    slot_duration INT NOT NULL CHECK (slot_duration > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_doctor_schedule_doctor
    FOREIGN KEY (doctor_id)
    REFERENCES doctor(doctor_id)
    ON DELETE CASCADE,

    CONSTRAINT chk_time_range
    CHECK (start_time < end_time)
    );


INSERT INTO doctor_schedule (
    schedule_id, doctor_id, day_of_week, start_time, end_time, slot_duration
)
VALUES
-- Dr. Arun Kumar (Cardiology)
('aaaaaaa1-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111', 1, '09:00', '13:00', 30),
('aaaaaaa2-2222-2222-2222-222222222222', '11111111-1111-1111-1111-111111111111', 3, '14:00', '18:00', 30),

-- Dr. Meera Iyer (Gynecology)
('bbbbbbb1-1111-1111-1111-111111111111', '22222222-2222-2222-2222-222222222222', 2, '10:00', '14:00', 20),
('bbbbbbb2-2222-2222-2222-222222222222', '22222222-2222-2222-2222-222222222222', 4, '10:00', '14:00', 20),

-- Dr. Rajesh Patel (Orthopedics)
('ccccccc1-1111-1111-1111-111111111111', '33333333-3333-3333-3333-333333333333', 1, '09:00', '12:00', 30),
('ccccccc2-2222-2222-2222-222222222222', '33333333-3333-3333-3333-333333333333', 5, '15:00', '18:00', 30),

-- Dr. Ananya Sharma (Dermatology)
('ddddddd1-1111-1111-1111-111111111111', '44444444-4444-4444-4444-444444444444', 0, '11:00', '15:00', 15),
('ddddddd2-2222-2222-2222-222222222222', '44444444-4444-4444-4444-444444444444', 6, '11:00', '15:00', 15);
