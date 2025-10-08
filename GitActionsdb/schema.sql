CREATE TABLE IF NOT EXISTS payrolls (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    basic DECIMAL(12,2) NOT NULL,
    hra DECIMAL(12,2) NOT NULL,
    allowances DECIMAL(12,2) NOT NULL,
    deductions DECIMAL(12,2) NOT NULL,
    net_salary DECIMAL(12,2) NOT NULL,
    pay_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
