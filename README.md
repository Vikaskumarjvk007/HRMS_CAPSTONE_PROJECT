**🏢 Human Resource Management System (HRMS)**

**1\. Introduction**

**Human Resource Management System (HRMS)** is a **Java-based application** that automates HR tasks such as **employee management, leave requests, payroll generation, and attendance tracking**.

**Built with:**

- ☕ **Java OOP principles**
- 🗄 **JDBC + MySQL**
- ☁ **AWS DynamoDB**
- ⚙ **Maven + GitHub Actions (CI/CD)**

**2\. Objectives**

- ✔ Lightweight HRMS solution using **Java + JDBC**
- ✔ Dual database integration (**MySQL + DynamoDB**)
- ✔ **Automated Payroll** (once per month, duplicate-protected)
- ✔ Fully integrated **CI/CD Pipeline**

**3\. Features**

- 👨‍💼 Employee Management (CRUD)
- 📅 Leave Requests (stored in **DynamoDB**)
- 💰 Payroll Automation (monthly)
- 📝 Attendance Tracking
- 🔒 Secure **Admin Login**

**4\. Technology Stack**

- **Language** → Java 17+
- **Databases** → MySQL + DynamoDB Local
- **Build Tool** → Maven
- **Testing** → JUnit
- **Version Control** → Git & GitHub
- **CI/CD** → GitHub Actions + Qodana

**5\. Architecture**

- **Controllers Layer** → CLI input/output
- **Service Layer** → Business logic
- **DAO Layer** → MySQL (JDBC) + DynamoDB (AWS SDK)
- **Models** → Employee, Payroll, LeaveRequest
- **Helpers** → Utilities (LeaveCalendar)

**Flow**:  
Controllers → Services → DAO → MySQL/DynamoDB

**6\. Project Structure**

hrms_project/

├── controllers/ # AdminController, EmployeeController

├── dao/ # EmployeeDAO, PayrollDAO, LeaveRequestDAO

├── service/ # PayrollService, LeaveDynamoService

├── helper/ # LeaveCalendar

├── model/ # Employee, Payroll, LeaveRequest

├── test/ # PayrollServiceTest

├── pom.xml # Maven dependencies

└── README.md # Documentation

**7\. Admin Menu (CLI Demo)**

\=====================================

HRMS - Admin Dashboard

\=====================================

1\. Manage Employees

2\. Manage Leave Requests

3\. Generate Payroll

4\. View Attendance

5\. Logout

\-------------------------------------

Enter your choice: _

**8\. Sample CLI Outputs**

**🔑 Admin Login**

Welcome to HRMS Admin Portal

Enter Username: admin

Enter Password: \*\*\*\*

✅ Login Successful!

**👨‍💼 Add Employee**

\--- Employee Management ---

Enter Employee Name: John Doe

Enter Email: <john.doe@example.com>

Enter Role: Developer

Enter Salary: 60000

✅ Employee added successfully!

**📝 Leave Request**

\--- Leave Request ---

Enter Employee ID: 101

Enter Start Date (YYYY-MM-DD): 2025-10-15

Enter End Date (YYYY-MM-DD): 2025-10-17

✅ Leave request submitted to DynamoDB!

**💰 Payroll Generation**

\--- Payroll Job Running ---

Fetching employees...

Calculating salary components...

Generating payroll for October 2025...

✅ Payroll generated successfully for 5 employees.

⚠ Skipped: Payroll already exists for
