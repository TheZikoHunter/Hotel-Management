-- Create database (run this separately if needed)
-- CREATE DATABASE hotel_db;

-- Connect to the database
-- \c hotel_db

-- Create employees table
CREATE TABLE IF NOT EXISTS employees (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create reservations table
CREATE TABLE IF NOT EXISTS reservations (
    id SERIAL PRIMARY KEY,
    guest_name VARCHAR(100) NOT NULL,
    guest_email VARCHAR(100) NOT NULL,
    guest_phone VARCHAR(20) NOT NULL,
    room_number INTEGER NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_by INTEGER NOT NULL REFERENCES employees(id),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default admin user (username: admin, password: admin123)
INSERT INTO employees (username, password, full_name, role)
VALUES ('admin', 'admin123', 'Administrator', 'Admin')
ON CONFLICT (username) DO NOTHING;

-- Insert some sample employees
INSERT INTO employees (username, password, full_name, role)
VALUES 
    ('john', 'john123', 'John Smith', 'Receptionist'),
    ('mary', 'mary123', 'Mary Johnson', 'Manager')
ON CONFLICT (username) DO NOTHING;

-- Insert some sample reservations
INSERT INTO reservations (guest_name, guest_email, guest_phone, room_number, check_in_date, check_out_date, status, created_by, notes)
VALUES 
    ('Alice Brown', 'alice@example.com', '555-1234', 101, '2023-06-15', '2023-06-20', 'Confirmed', 1, 'VIP guest'),
    ('Bob Wilson', 'bob@example.com', '555-5678', 202, '2023-06-18', '2023-06-25', 'Confirmed', 2, 'Late check-in'),
    ('Charlie Davis', 'charlie@example.com', '555-9012', 303, '2023-06-10', '2023-06-12', 'Checked-out', 2, ''),
    ('Diana Evans', 'diana@example.com', '555-3456', 404, '2023-06-20', '2023-06-30', 'Pending', 1, 'Requested extra bed');