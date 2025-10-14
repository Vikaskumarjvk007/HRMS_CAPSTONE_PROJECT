-- Complete HRMS Database Schema

-- Create departments table
CREATE TABLE IF NOT EXISTS departments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    location VARCHAR(200)
);

-- Create employees table
CREATE TABLE IF NOT EXISTS employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department_id INT,
    salary DECIMAL(12,2) NOT NULL,
    email VARCHAR(100),
    phone_number VARCHAR(20),
    date_of_joining DATE,
    password VARCHAR(255),
    FOREIGN KEY (department_id) REFERENCES departments(id)
);

-- Create admins table
CREATE TABLE IF NOT EXISTS admins (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Create attendance table
CREATE TABLE IF NOT EXISTS attendance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    date DATE NOT NULL,
    status VARCHAR(20),
    FOREIGN KEY (employee_id) REFERENCES employees(id),
    UNIQUE KEY unique_attendance (employee_id, date)
);

-- Create leave_requests table
CREATE TABLE IF NOT EXISTS leave_requests (
    request_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    reason TEXT,
    status VARCHAR(20) DEFAULT 'Pending',
    FOREIGN KEY (employee_id) REFERENCES employees(id)
);

-- Create payrolls table
CREATE TABLE IF NOT EXISTS payrolls (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    basic DECIMAL(12,2) NOT NULL,
    hra DECIMAL(12,2) NOT NULL,
    allowances DECIMAL(12,2) NOT NULL,
    deductions DECIMAL(12,2) NOT NULL,
    net_salary DECIMAL(12,2) NOT NULL,
    pay_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employees(id)
);

-- Insert default admin (username: admin, password: admin@123)
INSERT IGNORE INTO admins (username, password) VALUES ('admin', 'admin@123');

-- Insert sample departments
INSERT IGNORE INTO departments (name, location) VALUES 
    ('HR', 'Building A'),
    ('Finance', 'Building B'),
    ('IT', 'Building C'),
    ('Sales', 'Building D');
