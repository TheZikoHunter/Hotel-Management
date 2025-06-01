-- Create database (run this separately if needed)
-- CREATE DATABASE hotel;

-- Use the database
-- USE hotel;

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

-- Insert default admin user (username: admin, password: admin123 - hashed)
INSERT IGNORE INTO employees (username, password, full_name, role)
VALUES ('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'Administrator', 'admin');

-- Insert some sample employees (passwords are hashed)
INSERT IGNORE INTO employees (username, password, full_name, role)
VALUES 
    ('john', 'b4b597c714a8f49103da4dab0266af0ee0ae4f8575250a84855c3d76941cd422', 'John Smith', 'staff'),
    ('mary', '6120ac744907caa62f236bd2695b50478c7fdf17fffb928ac84507ad330b2be8', 'Mary Johnson', 'staff');

-- Insert some sample reservations
INSERT IGNORE INTO reservations (guest_name, guest_email, guest_phone, room_number, check_in_date, check_out_date, status, created_by, notes)
VALUES 
    ('Alice Brown', 'alice@example.com', '555-1234', 101, '2023-06-15', '2023-06-20', 'Confirmed', 1, 'VIP guest'),
    ('Bob Wilson', 'bob@example.com', '555-5678', 202, '2023-06-18', '2023-06-25', 'Confirmed', 2, 'Late check-in'),
    ('Charlie Davis', 'charlie@example.com', '555-9012', 303, '2023-06-10', '2023-06-12', 'Checked-out', 2, ''),
    ('Diana Evans', 'diana@example.com', '555-3456', 404, '2023-06-20', '2023-06-30', 'Pending', 1, 'Requested extra bed');