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

-- Note: Sample login credentials for testing:
-- Admin: admin / admin123
-- Chef de réception: chef_reception / password123  
-- Réceptionniste: receptionniste1 / password123 or receptionniste2 / password123
-- All other sample users: password123

-- Insert some sample employees with hotel roles (passwords are hashed u-- Insert some sample employees with hotel roles (passwords are hashed using SHA-256)
INSERT IGNORE INTO employees (username, password, full_name, role)
VALUES 
    ('chef_reception', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Pierre Dubois', 'chef de réception'),
    ('receptionniste1', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Marie Leclerc', 'réceptionniste'),
    ('receptionniste2', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Jean Martin', 'réceptionniste'),
    ('concierge1', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Sophie Moreau', 'concierge'),
    ('voiturier1', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Luc Bernard', 'voiturier'),
    ('agent_securite', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Marc Rousseau', 'agent de sécurité'),
    ('femme_chambre1', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Claire Petit', 'femme de chambre'),
    ('gouvernante1', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Isabelle Thomas', 'gouvernante');

-- Insert some sample reservations
INSERT IGNORE INTO reservations (guest_name, guest_email, guest_phone, room_number, check_in_date, check_out_date, status, created_by, notes)
VALUES 
    ('Alice Brown', 'alice@example.com', '555-1234', 101, '2025-06-15', '2025-06-20', 'Confirmé', 3, 'Client VIP'),
    ('Bob Wilson', 'bob@example.com', '555-5678', 202, '2025-06-18', '2025-06-25', 'Confirmé', 4, 'Arrivée tardive'),
    ('Charlie Davis', 'charlie@example.com', '555-9012', 303, '2025-06-01', '2025-06-03', 'Parti', 3, ''),
    ('Diana Evans', 'diana@example.com', '555-3456', 404, '2025-06-20', '2025-06-30', 'En attente', 3, 'Lit supplémentaire demandé');