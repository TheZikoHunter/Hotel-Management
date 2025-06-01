-- Database encoding repair script
-- This script fixes character encoding issues in the hotel database

-- First, let's check the current character set of the database and tables
-- You should run these commands to see current encoding:
-- SHOW CREATE DATABASE hotel;
-- SHOW CREATE TABLE employees;

-- Set the database to use UTF-8
ALTER DATABASE hotel CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Update the employees table to use UTF-8
ALTER TABLE employees CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Update the reservations table to use UTF-8
ALTER TABLE reservations CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Fix the corrupted role data (if "chef de rÃ©ception" exists, replace it with correct UTF-8)
UPDATE employees 
SET role = 'chef de réception' 
WHERE role = 'chef de rÃ©ception' OR role LIKE '%rÃ©ception%';

-- Fix any other potential encoding issues in roles
UPDATE employees 
SET role = 'réceptionniste' 
WHERE role = 'rÃ©ceptionniste' OR role LIKE '%rÃ©ceptionniste%';

-- Verify the fix
SELECT id, username, full_name, role FROM employees WHERE role LIKE '%réception%' OR role LIKE '%réceptionniste%';

-- If you see any remaining corrupted data, you may need to update specific records manually
-- Example:
-- UPDATE employees SET role = 'chef de réception' WHERE id = [specific_id];
