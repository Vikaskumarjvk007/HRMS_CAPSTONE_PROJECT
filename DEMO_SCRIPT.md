# HRMS Portal – 20-Minute Panel Presentation Script

---

## 🎤 **Opening (0:00 - 2:00)**

Good morning everyone... uh, good afternoon, sir... ma'am. 

So, today I'm going to walk you through my final project, which is... uh, the **HRMS Portal – Employee Management System**. 

It's basically a backend system built entirely in **Java**, no frameworks like Spring Boot or anything fancy like that. Just pure Java with JDBC connecting to MySQL database. So yeah, let me just... let me show you what I've built here.

*[Open VS Code, show the project structure in the sidebar]*

So, this is the complete project structure. As you can see, I have all my packages organized here — we have the `model` package, `dao` package, `service` package, and `controllers`. Everything is written from scratch. No auto-generated code or anything.

The main idea behind this project was to create a system where... you know, an HR department or admin can manage employees, track their attendance, generate payroll, handle leave requests — basically everything an HR system would need to do in a real company.

Let me quickly tell you what problems this solves, and then we'll dive into the code.

---

## 📋 **Problem Statement (2:00 - 3:30)**

So... in most companies, right, they have employees working in different departments — like IT, HR, Finance, Sales — and managing all of this manually becomes really difficult. 

You need to:
- Store employee details like name, salary, department, phone number, email
- Track who came to office, who was absent, who took leave
- Generate monthly payroll — calculate basic salary, HRA, allowances, deductions, net salary
- Handle leave requests and approve or reject them

So this system does all of that. It's connected to a MySQL database where everything is stored — employees, departments, attendance records, payroll data, leave requests — everything.

And... uh, I've also added optional DynamoDB support for scalability, in case the company grows and needs cloud storage. But for this demo, we're using MySQL locally.

Alright, let me show you the database first.

---

## 🗄️ **Database Overview (3:30 - 5:00)**

*[Open terminal, run MySQL command]*

```bash
mysql -u root -proot hrms_project -e "SHOW TABLES;"
```

Okay, so as you can see, we have **6 tables** here:
- `admins` — stores admin login credentials
- `departments` — stores department names like HR, IT, Finance, Sales
- `employees` — this is the main table with employee details
- `attendance` — tracks daily attendance — Present, Absent, Leave
- `leave_requests` — stores leave applications with status (Pending, Approved, Rejected)
- `payrolls` — stores monthly salary breakup for each employee

Let me quickly show you some sample data...

```bash
mysql -u root -proot hrms_project -e "SELECT id, name, department_id, salary FROM employees LIMIT 5;"
```

So here we have some employees — let's say... John, Sarah, Michael, Emily — these are test employees I added. Each employee has an ID, name, department ID, and salary. This salary is the annual salary, and from this, we calculate the monthly payroll.

Okay, cool. Now let's jump into the code.

---

## 💻 **Code Walkthrough - Part 1: Database Connection (5:00 - 7:00)**

*[Open `DBConnection.java` in VS Code]*

Alright, so the first and most important file is `DBConnection.java`. This is where... uh, this is where we establish the connection with MySQL database.

```java
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/hrms_project";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
```

So basically, what's happening here is — we have the database URL, which is `localhost:3306`, the database name is `hrms_project`, username is `root`, password is `root`. Pretty straightforward.

And then we have this `getConnection()` method, which returns a Connection object. Every time any DAO class needs to interact with the database, they call this method. So this acts as a central point for all database connections.

Now let's see how we actually use this connection in the DAO layer.

---

## 💻 **Code Walkthrough - Part 2: DAO Classes (7:00 - 11:00)**

*[Open `EmployeeDAO.java`]*

Okay, so now we're in the `EmployeeDAO` class. DAO stands for **Data Access Object** — basically, this layer handles all the database operations for employees.

Let me show you a few key methods here...

### **1. Add Employee**

```java
public void addEmployee(Employee emp) {
    String query = "INSERT INTO employees (name, department_id, salary, email, phone_number, date_of_joining, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        
        stmt.setString(1, emp.getName());
        stmt.setInt(2, emp.getDepartmentId());
        stmt.setDouble(3, emp.getSalary());
        // ... and so on
        
        stmt.executeUpdate();
        System.out.println("Employee added successfully!");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
```

So what's happening here is — we write the SQL INSERT query with placeholders (`?`), then we use `PreparedStatement` to set the values, and finally we execute it. If the insertion is successful, it prints "Employee added successfully!"

Same way, we have methods for:
- `getEmployeeById()` — fetch a specific employee
- `getAllEmployees()` — fetch all employees
- `updateEmployee()` — update employee details
- `deleteEmployee()` — delete an employee

So basically, all CRUD operations are here.

*[Scroll down to show other methods briefly]*

Now let me quickly show you the `AttendanceDAO`...

*[Open `AttendanceDAO.java`]*

### **2. Mark Attendance**

```java
public void markAttendance(int empId, LocalDate date, String status) {
    String query = "INSERT INTO attendance (employee_id, date, status) VALUES (?, ?, ?)";
    
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        
        stmt.setInt(1, empId);
        stmt.setDate(2, Date.valueOf(date));
        stmt.setString(3, status);
        
        stmt.executeUpdate();
        System.out.println("Attendance marked: " + status);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
```

So here, we're inserting attendance for an employee — we pass the employee ID, date, and status (Present, Absent, or Leave). Pretty simple, right?

And then we have `PayrollDAO` for salary calculations...

*[Open `PayrollDAO.java` briefly]*

### **3. Payroll Generation**

This one is a bit more complex because we're calculating salary components:
- **Basic salary** = Annual salary ÷ 12
- **HRA** = 20% of basic
- **Allowances** = 10% of basic
- **Deductions** = 5% of basic
- **Net salary** = Basic + HRA + Allowances - Deductions

All of this is calculated in `PayrollService.java` and then stored using `PayrollDAO`.

Okay, so that's the DAO layer. Let me now show you the service layer, where the actual business logic happens.

---

## 💻 **Code Walkthrough - Part 3: Service Layer (11:00 - 13:30)**

*[Open `PayrollService.java`]*

Alright, so this is the `PayrollService` class. This is where we generate payroll for employees.

```java
public void generatePayroll(int empId) {
    EmployeeDAO empDAO = new EmployeeDAO();
    Employee emp = empDAO.getEmployeeById(empId);
    
    if (emp == null) {
        System.out.println("Employee not found!");
        return;
    }
    
    double annualSalary = emp.getSalary();
    double monthlyBasic = annualSalary / 12;
    double hra = monthlyBasic * 0.20;
    double allowances = monthlyBasic * 0.10;
    double deductions = monthlyBasic * 0.05;
    double netSalary = monthlyBasic + hra + allowances - deductions;
    
    Payroll payroll = new Payroll();
    payroll.setEmployeeId(empId);
    payroll.setBasic(monthlyBasic);
    payroll.setHra(hra);
    payroll.setAllowances(allowances);
    payroll.setDeductions(deductions);
    payroll.setNetSalary(netSalary);
    payroll.setPayDate(LocalDate.now());
    
    PayrollDAO payrollDAO = new PayrollDAO();
    payrollDAO.addPayroll(payroll);
    
    System.out.println("Payroll generated for Employee ID: " + empId);
}
```

So basically, we fetch the employee, calculate all the salary components, create a `Payroll` object, and save it to the database. Simple and clean.

And if we want to generate payroll for **all employees** at once, we have another method called `generatePayrollForAll()`, which loops through all employees and calls this method.

Now let me show you the main application...

---

## 💻 **Code Walkthrough - Part 4: Admin & Employee Apps (13:30 - 16:00)**

*[Open `AdminApp.java`]*

Okay, so this is the `AdminApp`. This is basically the interface for the admin or HR person to interact with the system.

When the admin logs in, they get a menu like this:

```
=== HRMS Admin Panel ===
1. Add Employee
2. View All Employees
3. Update Employee
4. Delete Employee
5. Mark Attendance
6. Generate Payroll
7. View Leave Requests
8. Approve/Reject Leave
9. Logout
```

So the admin can do all these operations. Let me show you how login works...

```java
public boolean login(String username, String password) {
    AdminDAO adminDAO = new AdminDAO();
    return adminDAO.validateAdmin(username, password);
}
```

We have an `AdminDAO` which checks the credentials from the `admins` table. If it's valid, they can access the system.

Similarly, we have `EmployeeApp.java` where employees can:
- View their own profile
- View their attendance history
- Apply for leave
- View their payroll slips

So yeah, two separate interfaces — one for admin, one for employees.

Now let me actually run the application and show you a live demo...

---

## 🎬 **Live Demo (16:00 - 18:30)**

*[Open terminal, navigate to project directory]*

Alright, so let me run the admin application...

```bash
java -cp target/hrms_project-1.0-SNAPSHOT-shaded.jar com.hrms.model.service.AdminApp
```

*[Run the application]*

Okay, so it's asking for login credentials. The username is `admin` and password is `admin@123`.

*[Type credentials]*

```
Username: admin
Password: admin@123
Login successful!
```

Perfect! Now we're inside the admin panel. Let me... uh, let me mark attendance for an employee.

*[Select option 5 - Mark Attendance]*

```
Enter Employee ID: 1
Enter Date (YYYY-MM-DD): 2025-10-15
Enter Status (Present/Absent/Leave): Present
Attendance marked: Present
```

Great! So attendance has been recorded. Now let me generate payroll for this employee...

*[Select option 6 - Generate Payroll]*

```
Enter Employee ID: 1
Payroll generated for Employee ID: 1
Payslip:
--------------------------
Employee ID: 1
Basic Salary: ₹45,833.33
HRA: ₹9,166.67
Allowances: ₹4,583.33
Deductions: ₹2,291.67
Net Salary: ₹57,291.66
Pay Date: 2025-10-15
--------------------------
```

Perfect! So the payroll has been calculated and stored in the database. 

Let me quickly verify this by checking the database...

```bash
mysql -u root -proot hrms_project -e "SELECT * FROM payrolls WHERE employee_id=1 ORDER BY id DESC LIMIT 1;"
```

*[Show the output]*

Yep, it's there! So everything is working as expected.

Now let me exit and show you the employee application too...

*[Run EmployeeApp.java]*

```bash
java -cp target/hrms_project-1.0-SNAPSHOT-shaded.jar com.hrms.model.service.EmployeeApp
```

Okay, so employees log in using their employee ID and password. Let me use employee ID `1` and password `pass123`.

*[Login]*

```
Enter Employee ID: 1
Enter Password: pass123
Login successful! Welcome, John Smith
```

Nice! So now the employee can see their menu:

```
1. View My Profile
2. View My Attendance
3. Apply for Leave
4. View My Payroll
5. Logout
```

Let me check the attendance history...

*[Select option 2]*

```
Attendance History:
2025-10-15 - Present
2025-10-14 - Present
2025-10-13 - Present
2025-10-12 - Absent
```

Cool! So all the data is coming from the database correctly.

Alright, let me exit this now and wrap up.

---

## 🏁 **Conclusion & Challenges (18:30 - 20:00)**

*[Close applications, show VS Code]*

So yeah, that's the complete demo of my HRMS Portal project. 

To summarize what we saw:
- **Database**: MySQL with 6 tables — admins, departments, employees, attendance, leave_requests, payrolls
- **Java Backend**: Pure Java with JDBC — no frameworks
- **DAO Layer**: Handles all database operations
- **Service Layer**: Contains business logic like payroll calculation
- **Applications**: AdminApp for HR, EmployeeApp for employees

Some challenges I faced while building this:
1. **Database design** — deciding how to structure the tables and relationships
2. **Payroll calculation** — making sure the math was correct for salary components
3. **Connection management** — avoiding memory leaks by properly closing connections
4. **Date handling** — using `LocalDate` and converting to SQL date format

And... uh, I also learned a lot about:
- JDBC and prepared statements to prevent SQL injection
- Exception handling with try-catch blocks
- Organizing code into layers (DAO, Service, Controller)

Future improvements I'd like to make:
- Add **REST API** so it can be accessed from a web or mobile app
- Implement **role-based access control** for different admin levels
- Add **email notifications** for leave approvals and payroll generation
- Integrate **DynamoDB** fully for cloud scalability

So yeah, that's it from my side. Thank you so much for your time and attention. I hope I was able to explain everything clearly. If you have any questions, I'm happy to answer!

---

## ❓ **Potential Panel Questions (For Your Preparation)**

**Q1: Why didn't you use Spring Boot or Hibernate?**  
A: I wanted to understand the core concepts first — how JDBC works, how to write raw SQL queries, how to manage connections manually. Once I'm comfortable with the basics, I plan to learn frameworks.

**Q2: How do you handle SQL injection?**  
A: I'm using `PreparedStatement` instead of `Statement`, which automatically escapes special characters and prevents SQL injection attacks.

**Q3: What if two admins try to generate payroll for the same employee at the same time?**  
A: Good question! Right now, my system doesn't handle concurrency. In a real-world scenario, I'd implement database transactions or use locking mechanisms to prevent duplicate entries.

**Q4: Can you show me the test cases?**  
A: Yes! I have JUnit tests in the `test` folder. I've written tests for PayrollService to verify that salary calculations are correct.

**Q5: How do you deploy this application?**  
A: Currently, it's running locally. To deploy, I'd package it as a JAR file, set up a MySQL database on a cloud server like AWS RDS, update the connection details, and run it on an EC2 instance or similar.

**Q6: What about security for passwords?**  
A: Right now, passwords are stored in plain text, which is not secure. In a production system, I'd use password hashing with BCrypt or similar algorithms before storing them in the database.

---

## 💡 **Quick Tips for Delivery**

1. **Speak slowly and clearly** — don't rush through the code
2. **Use hand gestures** when explaining concepts
3. **Make eye contact** with panel members
4. **Pause after major sections** to check if they have questions
5. **Stay calm** even if you forget something — just say "let me check that again"
6. **Show confidence** — you built this, you know it!

---

Good luck with your presentation! You've got this! 🚀
