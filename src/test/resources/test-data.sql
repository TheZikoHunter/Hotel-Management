-- Test database setup script
CREATE DATABASE IF NOT EXISTS hotel_test;
USE hotel_test;

-- Create employees table
CREATE TABLE IF NOT EXISTS employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create reservations table
CREATE TABLE IF NOT EXISTS reservations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    guest_name VARCHAR(100) NOT NULL,
    guest_email VARCHAR(100) NOT NULL,
    guest_phone VARCHAR(20) NOT NULL,
    room_number INT NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_by INT NOT NULL,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES employees(id)
);

-- Insert test employees (passwords are hashed using SHA-256: password123)
INSERT INTO employees (username, password, full_name, role) VALUES
('admin_test', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Admin Test', 'admin'),
('chef_test', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Chef Test', 'chef de réception'),
('receptionniste_test', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Réceptionniste Test', 'réceptionniste'),
('concierge_test', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Concierge Test', 'concierge');

-- Insert test reservations
INSERT INTO reservations (guest_name, guest_email, guest_phone, room_number, check_in_date, check_out_date, status, created_by, notes) VALUES
('Test Client 1', 'test1@example.com', '555-0001', 101, '2025-06-15', '2025-06-20', 'Confirmé', 3, 'Test reservation 1'),
('Test Client 2', 'test2@example.com', '555-0002', 202, '2025-06-18', '2025-06-25', 'En attente', 3, 'Test reservation 2');
