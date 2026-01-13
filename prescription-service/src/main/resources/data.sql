CREATE TABLE IF NOT EXISTS medicine (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    dosage VARCHAR(50) NOT NULL,
    price NUMERIC(10, 2) NOT NULL CHECK (price >= 0)
);

CREATE TABLE IF NOT EXISTS lab_test (
    id UUID PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    price NUMERIC(10, 2) NOT NULL CHECK (price >= 0)
);

INSERT INTO medicine (id, name, dosage, price)
SELECT
    'a1f1c2a0-1111-4b2e-8c1a-111111111111',
    'Paracetamol',
    '500 mg',
    20.00
    WHERE NOT EXISTS (
  SELECT 1 FROM medicine WHERE id = 'a1f1c2a0-1111-4b2e-8c1a-111111111111'
);

INSERT INTO medicine (id, name, dosage, price)
SELECT
    'a1f1c2a0-2222-4b2e-8c1a-222222222222',
    'Paracetamol',
    '650 mg',
    30.00
    WHERE NOT EXISTS (
  SELECT 1 FROM medicine WHERE id = 'a1f1c2a0-2222-4b2e-8c1a-222222222222'
);

INSERT INTO medicine (id, name, dosage, price)
SELECT
    'a1f1c2a0-3333-4b2e-8c1a-333333333333',
    'Amoxicillin',
    '250 mg',
    60.00
    WHERE NOT EXISTS (
  SELECT 1 FROM medicine WHERE id = 'a1f1c2a0-3333-4b2e-8c1a-333333333333'
);

INSERT INTO medicine (id, name, dosage, price)
SELECT
    'a1f1c2a0-4444-4b2e-8c1a-444444444444',
    'Amoxicillin',
    '500 mg',
    110.00
    WHERE NOT EXISTS (
  SELECT 1 FROM medicine WHERE id = 'a1f1c2a0-4444-4b2e-8c1a-444444444444'
);

INSERT INTO medicine (id, name, dosage, price)
SELECT
    'a1f1c2a0-5555-4b2e-8c1a-555555555555',
    'Azithromycin',
    '500 mg',
    220.00
    WHERE NOT EXISTS (
  SELECT 1 FROM medicine WHERE id = 'a1f1c2a0-5555-4b2e-8c1a-555555555555'
);


INSERT INTO lab_test (id, name, price)
SELECT
    'b2e2d3b0-1111-4d9c-9e2b-111111111111',
    'Complete Blood Count (CBC)',
    300.00
    WHERE NOT EXISTS (
  SELECT 1 FROM lab_test WHERE id = 'b2e2d3b0-1111-4d9c-9e2b-111111111111'
);

INSERT INTO lab_test (id, name, price)
SELECT
    'b2e2d3b0-2222-4d9c-9e2b-222222222222',
    'Blood Sugar (Fasting)',
    120.00
    WHERE NOT EXISTS (
  SELECT 1 FROM lab_test WHERE id = 'b2e2d3b0-2222-4d9c-9e2b-222222222222'
);

INSERT INTO lab_test (id, name, price)
SELECT
    'b2e2d3b0-3333-4d9c-9e2b-333333333333',
    'HbA1c',
    450.00
    WHERE NOT EXISTS (
  SELECT 1 FROM lab_test WHERE id = 'b2e2d3b0-3333-4d9c-9e2b-333333333333'
);

INSERT INTO lab_test (id, name, price)
SELECT
    'b2e2d3b0-4444-4d9c-9e2b-444444444444',
    'Lipid Profile',
    700.00
    WHERE NOT EXISTS (
  SELECT 1 FROM lab_test WHERE id = 'b2e2d3b0-4444-4d9c-9e2b-444444444444'
);

INSERT INTO lab_test (id, name, price)
SELECT
    'b2e2d3b0-5555-4d9c-9e2b-555555555555',
    'Thyroid Profile (T3, T4, TSH)',
    500.00
    WHERE NOT EXISTS (
  SELECT 1 FROM lab_test WHERE id = 'b2e2d3b0-5555-4d9c-9e2b-555555555555'
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_medicine_name_dosage ON medicine (LOWER(name), LOWER(dosage));
CREATE UNIQUE INDEX IF NOT EXISTS ux_lab_test_name ON lab_test (LOWER(name));
