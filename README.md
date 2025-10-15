# HRMS - Human Resource Management System

[![CI Build](https://github.com/Vikaskumarjvk007/HRMS_CAPSTONE_PROJECT/actions/workflows/ci-build-test.yml/badge.svg)](https://github.com/Vikaskumarjvk007/HRMS_CAPSTONE_PROJECT/actions)
[![Payroll](https://github.com/Vikaskumarjvk007/HRMS_CAPSTONE_PROJECT/actions/workflows/Payroll.yml/badge.svg)](https://github.com/Vikaskumarjvk007/HRMS_CAPSTONE_PROJECT/actions)

## About This Project

This is my capstone project for the training program. I've built a Human Resource Management System using Java that helps manage employee data, track attendance, handle leave requests, and process payroll.

The project uses both MySQL and AWS DynamoDB for data storage. MySQL handles the main employee and payroll data, while DynamoDB is used for leave requests (wanted to try out AWS services).

## What It Does

The system has two main interfaces - one for admins and one for employees:

**Admin Features:**
- Add/update/delete employee records
- View and approve leave requests
- Generate monthly payroll
- Track employee attendance
- Manage departments

**Employee Features:**
- View their own details
- Submit leave requests
- Check payroll information
- View attendance history

## Technologies Used

- Java (using JDK 17)
- MySQL database
- AWS DynamoDB
- JDBC for database connectivity
- Maven for build management
- JUnit for testing
- GitHub Actions for CI/CD

## How I Structured the Code

I tried to follow a layered architecture approach:

```
src/main/java/com/hrms/model/
├── controllers/     - handles user input (AdminController, EmployeeController)
├── service/         - business logic (PayrollService, AdminApp, EmployeeApp)
├── dao/             - database operations (EmployeeDAO, PayrollDAO, etc.)
├── helper/          - utility classes (LeaveCalendar)
└── [Model classes]  - Employee, Payroll, Attendance, LeaveRequest, etc.
```

The flow basically goes: User Input -> Controller -> Service -> DAO -> Database

## Setup Instructions

1. Clone the repository
2. Make sure you have Java 17+ and MySQL installed
3. Create a database named `hrms_db`
4. Run the schema file from `db/schema.sql` to create tables
5. Update database credentials in `DBConnection.java`
6. For DynamoDB, you'll need AWS credentials configured
7. Build the project: `mvn clean install`
8. Run: `mvn exec:java -Dexec.mainClass="com.hrms.model.service.AdminApp"`

## Sample Usage

When you run the admin app, you'll see something like:

```
=====================================
    HRMS - Admin Dashboard
=====================================
1. Manage Employees
2. Manage Leave Requests
3. Generate Payroll
4. View Attendance
5. Logout
-------------------------------------
Enter your choice:
```

For payroll generation, the system automatically runs once per month and makes sure not to generate duplicate payrolls for the same month.

## Testing

I've added some JUnit tests for the payroll service. You can run them with:
```
mvn test
```

There's also a GitHub Actions workflow that runs tests automatically on push.

## Known Issues

- Still working on improving the error handling in some places
- The UI is command-line based, might add a GUI later
- Need to add more test coverage

## What I Learned

This project helped me understand:
- Working with databases using JDBC
- Integrating multiple databases (MySQL + DynamoDB)
- Implementing DAO pattern
- Writing automated tests
- Setting up CI/CD pipelines
- Using Maven for dependency management

## Future Improvements

If I get time, I'd like to add:
- A proper web interface
- Email notifications for leave approvals
- Better reporting features
- Performance optimization for large datasets

---

Developed by Vikas Kumar  
GitHub: [@Vikaskumarjvk007](https://github.com/Vikaskumarjvk007)
