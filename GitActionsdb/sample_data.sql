-- Sample Data for HRMS Testing

-- Insert sample employees (Department IDs: 1=HR, 2=Finance, 3=IT, 4=Sales)
INSERT INTO employees (name, department_id, salary, email, phone_number, date_of_joining, password) VALUES
('John Smith', 1, 55000.00, 'john.smith@company.com', '555-0101', '2023-01-15', 'pass123'),
('Sarah Johnson', 2, 62000.00, 'sarah.j@company.com', '555-0102', '2023-02-20', 'pass123'),
('Michael Brown', 3, 300000.00, 'michael.b@company.com', '555-0103', '2023-03-10', 'pass123'),
('Emily Davis', 3, 72000.00, 'emily.d@company.com', '555-0104', '2023-04-05', 'pass123'),
('David Wilson', 4, 58000.00, 'david.w@company.com', '555-0105', '2023-05-12', 'pass123'),
('Jessica Martinez', 1, 51000.00, 'jessica.m@company.com', '555-0106', '2023-06-18', 'pass123'),
('Robert Taylor', 2, 68000.00, 'robert.t@company.com', '555-0107', '2023-07-22', 'pass123'),
('Linda Anderson', 4, 61000.00, 'linda.a@company.com', '555-0108', '2023-08-30', 'pass123');

-- Insert sample attendance records for the last 5 days
INSERT INTO attendance (employee_id, date, status) VALUES
-- Day 1 (5 days ago)
(1, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'Present'),
(2, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'Present'),
(3, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'Present'),
(4, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'Absent'),
(5, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'Present'),
(6, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'Present'),
(7, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'Present'),
(8, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'Present'),

-- Day 2 (4 days ago)
(1, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 'Present'),
(2, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 'Present'),
(3, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 'Leave'),
(4, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 'Present'),
(5, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 'Present'),
(6, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 'Present'),
(7, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 'Absent'),
(8, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 'Present'),

-- Day 3 (3 days ago)
(1, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 'Present'),
(2, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 'Present'),
(3, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 'Leave'),
(4, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 'Present'),
(5, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 'Present'),
(6, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 'Present'),
(7, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 'Present'),
(8, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 'Present'),

-- Day 4 (2 days ago)
(1, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'Present'),
(2, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'Present'),
(3, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'Present'),
(4, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'Present'),
(5, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'Present'),
(6, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'Leave'),
(7, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'Present'),
(8, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'Present'),

-- Day 5 (yesterday)
(1, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'Present'),
(2, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'Present'),
(3, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'Present'),
(4, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'Present'),
(5, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'Absent'),
(6, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'Present'),
(7, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'Present'),
(8, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'Present');

-- Insert sample leave requests
INSERT INTO leave_requests (employee_id, start_date, end_date, reason, status) VALUES
(3, DATE_SUB(CURDATE(), INTERVAL 4 DAY), DATE_SUB(CURDATE(), INTERVAL 3 DAY), 'Medical appointment', 'Approved'),
(6, DATE_SUB(CURDATE(), INTERVAL 2 DAY), DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'Personal matter', 'Approved'),
(1, DATE_ADD(CURDATE(), INTERVAL 5 DAY), DATE_ADD(CURDATE(), INTERVAL 7 DAY), 'Family vacation', 'Pending'),
(5, DATE_ADD(CURDATE(), INTERVAL 10 DAY), DATE_ADD(CURDATE(), INTERVAL 12 DAY), 'Wedding attendance', 'Pending');

-- Insert sample payroll records for last month
INSERT INTO payrolls (employee_id, basic, hra, allowances, deductions, net_salary, pay_date) VALUES
(1, 45000.00, 9000.00, 1000.00, 5000.00, 50000.00, DATE_SUB(CURDATE(), INTERVAL 1 MONTH)),
(2, 50000.00, 10000.00, 2000.00, 6000.00, 56000.00, DATE_SUB(CURDATE(), INTERVAL 1 MONTH)),
(3, 60000.00, 12000.00, 3000.00, 7500.00, 67500.00, DATE_SUB(CURDATE(), INTERVAL 1 MONTH)),
(4, 58000.00, 11600.00, 2400.00, 7200.00, 64800.00, DATE_SUB(CURDATE(), INTERVAL 1 MONTH)),
(5, 47000.00, 9400.00, 1600.00, 5800.00, 52200.00, DATE_SUB(CURDATE(), INTERVAL 1 MONTH)),
(6, 42000.00, 8400.00, 900.00, 5100.00, 46200.00, DATE_SUB(CURDATE(), INTERVAL 1 MONTH)),
(7, 55000.00, 11000.00, 2000.00, 6800.00, 61200.00, DATE_SUB(CURDATE(), INTERVAL 1 MONTH)),
(8, 49000.00, 9800.00, 1400.00, 6100.00, 54100.00, DATE_SUB(CURDATE(), INTERVAL 1 MONTH));
