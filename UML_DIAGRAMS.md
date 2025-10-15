# HRMS Portal - UML Diagrams

## 📊 1. Class Diagram

```mermaid
classDiagram
    %% Model Classes
    class Employee {
        -int id
        -String name
        -int departmentId
        -double salary
        -String email
        -String phoneNumber
        -LocalDate dateOfJoining
        -String password
        +getId() int
        +setId(int)
        +getName() String
        +setName(String)
        +getDepartmentId() int
        +setDepartmentId(int)
        +getSalary() double
        +setSalary(double)
        +getEmail() String
        +setEmail(String)
        +getPhoneNumber() String
        +setPhoneNumber(String)
        +getDateOfJoining() LocalDate
        +setDateOfJoining(LocalDate)
        +getPassword() String
        +setPassword(String)
    }

    class Department {
        -int id
        -String name
        +getId() int
        +setId(int)
        +getName() String
        +setName(String)
    }

    class Admin {
        -int id
        -String username
        -String password
        +getId() int
        +setId(int)
        +getUsername() String
        +setUsername(String)
        +getPassword() String
        +setPassword(String)
    }

    class Attendance {
        -int id
        -int employeeId
        -LocalDate date
        -String status
        +getId() int
        +setId(int)
        +getEmployeeId() int
        +setEmployeeId(int)
        +getDate() LocalDate
        +setDate(LocalDate)
        +getStatus() String
        +setStatus(String)
    }

    class LeaveRequest {
        -int id
        -int employeeId
        -LocalDate startDate
        -LocalDate endDate
        -String reason
        -String status
        +getId() int
        +setId(int)
        +getEmployeeId() int
        +setEmployeeId(int)
        +getStartDate() LocalDate
        +setStartDate(LocalDate)
        +getEndDate() LocalDate
        +setEndDate(LocalDate)
        +getReason() String
        +setReason(String)
        +getStatus() String
        +setStatus(String)
    }

    class Payroll {
        -int id
        -int employeeId
        -double basic
        -double hra
        -double allowances
        -double deductions
        -double netSalary
        -LocalDate payDate
        +getId() int
        +setId(int)
        +getEmployeeId() int
        +setEmployeeId(int)
        +getBasic() double
        +setBasic(double)
        +getHra() double
        +setHra(double)
        +getAllowances() double
        +setAllowances(double)
        +getDeductions() double
        +setDeductions(double)
        +getNetSalary() double
        +setNetSalary(double)
        +getPayDate() LocalDate
        +setPayDate(LocalDate)
    }

    %% DAO Classes
    class DBConnection {
        -static final String URL
        -static final String USER
        -static final String PASSWORD
        +static getConnection() Connection
    }

    class EmployeeDAO {
        +addEmployee(Employee) void
        +getEmployeeById(int) Employee
        +getAllEmployees() List~Employee~
        +updateEmployee(Employee) void
        +deleteEmployee(int) void
        +validateLogin(int, String) boolean
    }

    class DepartmentDAO {
        +addDepartment(Department) void
        +getDepartmentById(int) Department
        +getAllDepartments() List~Department~
        +updateDepartment(Department) void
        +deleteDepartment(int) void
    }

    class AdminDAO {
        +validateAdmin(String, String) boolean
        +getAdminByUsername(String) Admin
    }

    class AttendanceDAO {
        +markAttendance(int, LocalDate, String) void
        +getAttendanceByEmployee(int) List~Attendance~
        +getAllAttendance() List~Attendance~
        +getAttendanceByDate(LocalDate) List~Attendance~
    }

    class LeaveRequestDAO {
        +addLeaveRequest(LeaveRequest) void
        +getLeaveRequestById(int) LeaveRequest
        +getAllLeaveRequests() List~LeaveRequest~
        +getAllLeaveRequestsDetailed() List~Map~
        +updateLeaveStatus(int, String) void
        +getLeaveRequestsByEmployee(int) List~LeaveRequest~
    }

    class PayrollDAO {
        +addPayroll(Payroll) void
        +getPayrollById(int) Payroll
        +getAllPayrolls() List~Payroll~
        +getPayrollByEmployee(int) List~Payroll~
        +deletePayroll(int) void
    }

    class DynamoDBService {
        -DynamoDbClient dynamoDbClient
        -boolean isAvailable
        +addLeaveRequest(LeaveRequest) void
        +getLeaveRequestsByEmployee(int) List~LeaveRequest~
        +getAllLeaveRequests() List~LeaveRequest~
        +updateLeaveStatus(int, String) void
    }

    %% Service Classes
    class PayrollService {
        -PayrollDAO payrollDAO
        -EmployeeDAO employeeDAO
        +generatePayroll(int) void
        +generatePayrollForAll() void
    }

    class PayrollJobs {
        +main(String[]) void
    }

    %% Application Classes
    class AdminApp {
        -Scanner scanner
        -AdminDAO adminDAO
        -EmployeeDAO employeeDAO
        -DepartmentDAO departmentDAO
        -AttendanceDAO attendanceDAO
        -LeaveRequestDAO leaveRequestDAO
        -PayrollService payrollService
        +main(String[]) void
        -login() boolean
        -showMenu() void
        -addEmployee() void
        -viewAllEmployees() void
        -updateEmployee() void
        -deleteEmployee() void
        -markAttendance() void
        -generatePayroll() void
        -viewLeaveRequests() void
        -approveRejectLeave() void
    }

    class EmployeeApp {
        -Scanner scanner
        -EmployeeDAO employeeDAO
        -AttendanceDAO attendanceDAO
        -LeaveRequestDAO leaveRequestDAO
        -PayrollDAO payrollDAO
        +main(String[]) void
        -login() Employee
        -showMenu(Employee) void
        -viewProfile(Employee) void
        -viewAttendance(Employee) void
        -applyLeave(Employee) void
        -viewPayroll(Employee) void
    }

    %% Controller Classes
    class AdminController {
        +main(String[]) void
    }

    class EmployeeController {
        +main(String[]) void
    }

    %% Relationships
    Employee "1" --> "1" Department : belongs to
    Attendance "n" --> "1" Employee : tracked for
    LeaveRequest "n" --> "1" Employee : requested by
    Payroll "n" --> "1" Employee : generated for

    EmployeeDAO ..> DBConnection : uses
    DepartmentDAO ..> DBConnection : uses
    AdminDAO ..> DBConnection : uses
    AttendanceDAO ..> DBConnection : uses
    LeaveRequestDAO ..> DBConnection : uses
    PayrollDAO ..> DBConnection : uses

    EmployeeDAO ..> Employee : manages
    DepartmentDAO ..> Department : manages
    AdminDAO ..> Admin : manages
    AttendanceDAO ..> Attendance : manages
    LeaveRequestDAO ..> LeaveRequest : manages
    PayrollDAO ..> Payroll : manages

    PayrollService ..> PayrollDAO : uses
    PayrollService ..> EmployeeDAO : uses
    PayrollService ..> Payroll : creates

    AdminApp ..> AdminDAO : uses
    AdminApp ..> EmployeeDAO : uses
    AdminApp ..> DepartmentDAO : uses
    AdminApp ..> AttendanceDAO : uses
    AdminApp ..> LeaveRequestDAO : uses
    AdminApp ..> PayrollService : uses

    EmployeeApp ..> EmployeeDAO : uses
    EmployeeApp ..> AttendanceDAO : uses
    EmployeeApp ..> LeaveRequestDAO : uses
    EmployeeApp ..> PayrollDAO : uses

    AdminController ..> AdminApp : launches
    EmployeeController ..> EmployeeApp : launches

    DynamoDBService ..> LeaveRequest : manages
```

---

## 🔄 2. Sequence Diagram - Admin Login and Add Employee

```mermaid
sequenceDiagram
    actor Admin
    participant AdminApp
    participant AdminDAO
    participant DBConnection
    participant Database
    participant EmployeeDAO

    Admin->>AdminApp: Start Application
    AdminApp->>Admin: Display Login Screen
    Admin->>AdminApp: Enter username & password
    AdminApp->>AdminDAO: validateAdmin(username, password)
    AdminDAO->>DBConnection: getConnection()
    DBConnection->>Database: Establish Connection
    Database-->>DBConnection: Connection Object
    DBConnection-->>AdminDAO: Connection
    AdminDAO->>Database: SELECT * FROM admins WHERE username=?
    Database-->>AdminDAO: Admin Record
    AdminDAO-->>AdminApp: true/false
    
    alt Login Successful
        AdminApp->>Admin: Show Main Menu
        Admin->>AdminApp: Select "Add Employee"
        AdminApp->>Admin: Request Employee Details
        Admin->>AdminApp: Enter name, dept, salary, etc.
        AdminApp->>EmployeeDAO: addEmployee(employee)
        EmployeeDAO->>DBConnection: getConnection()
        DBConnection-->>EmployeeDAO: Connection
        EmployeeDAO->>Database: INSERT INTO employees VALUES(...)
        Database-->>EmployeeDAO: Success
        EmployeeDAO-->>AdminApp: Employee Added
        AdminApp->>Admin: Display Success Message
    else Login Failed
        AdminApp->>Admin: Display Error Message
        AdminApp->>Admin: Exit Application
    end
```

---

## 🔄 3. Sequence Diagram - Generate Payroll

```mermaid
sequenceDiagram
    actor Admin
    participant AdminApp
    participant PayrollService
    participant EmployeeDAO
    participant PayrollDAO
    participant DBConnection
    participant Database

    Admin->>AdminApp: Select "Generate Payroll"
    AdminApp->>Admin: Request Employee ID
    Admin->>AdminApp: Enter Employee ID
    AdminApp->>PayrollService: generatePayroll(empId)
    
    PayrollService->>EmployeeDAO: getEmployeeById(empId)
    EmployeeDAO->>DBConnection: getConnection()
    DBConnection->>Database: Establish Connection
    Database-->>DBConnection: Connection
    EmployeeDAO->>Database: SELECT * FROM employees WHERE id=?
    Database-->>EmployeeDAO: Employee Record
    EmployeeDAO-->>PayrollService: Employee Object
    
    alt Employee Found
        PayrollService->>PayrollService: Calculate Salary Components
        Note over PayrollService: monthlyBasic = salary / 12<br/>hra = basic * 0.20<br/>allowances = basic * 0.10<br/>deductions = basic * 0.05<br/>netSalary = basic + hra + allowances - deductions
        
        PayrollService->>PayrollService: Create Payroll Object
        PayrollService->>PayrollDAO: addPayroll(payroll)
        PayrollDAO->>DBConnection: getConnection()
        DBConnection-->>PayrollDAO: Connection
        PayrollDAO->>Database: INSERT INTO payrolls VALUES(...)
        Database-->>PayrollDAO: Success
        PayrollDAO-->>PayrollService: Payroll Saved
        PayrollService-->>AdminApp: Success
        AdminApp->>Admin: Display Payslip
    else Employee Not Found
        PayrollService-->>AdminApp: Error: Employee Not Found
        AdminApp->>Admin: Display Error Message
    end
```

---

## 🔄 4. Sequence Diagram - Employee Apply for Leave

```mermaid
sequenceDiagram
    actor Employee
    participant EmployeeApp
    participant LeaveRequestDAO
    participant DynamoDBService
    participant DBConnection
    participant Database
    participant DynamoDB

    Employee->>EmployeeApp: Login with ID & Password
    EmployeeApp->>Employee: Show Menu
    Employee->>EmployeeApp: Select "Apply for Leave"
    EmployeeApp->>Employee: Request Leave Details
    Employee->>EmployeeApp: Enter start date, end date, reason
    
    EmployeeApp->>LeaveRequestDAO: addLeaveRequest(leaveRequest)
    LeaveRequestDAO->>DBConnection: getConnection()
    DBConnection->>Database: Establish Connection
    Database-->>DBConnection: Connection
    LeaveRequestDAO->>Database: INSERT INTO leave_requests VALUES(...)
    Database-->>LeaveRequestDAO: Success
    
    par Optional DynamoDB Sync
        LeaveRequestDAO->>DynamoDBService: addLeaveRequest(leaveRequest)
        alt DynamoDB Available
            DynamoDBService->>DynamoDB: PutItem Request
            DynamoDB-->>DynamoDBService: Success
        else DynamoDB Unavailable
            DynamoDBService-->>LeaveRequestDAO: Gracefully Skip
        end
    end
    
    LeaveRequestDAO-->>EmployeeApp: Leave Request Submitted
    EmployeeApp->>Employee: Display Success Message
```

---

## 📊 5. Use Case Diagram

```mermaid
graph TB
    Admin((Admin))
    Employee((Employee))
    System[HRMS Portal]
    
    Admin -->|Login| UC1[Authenticate Admin]
    Admin -->|Manage| UC2[Add Employee]
    Admin -->|Manage| UC3[Update Employee]
    Admin -->|Manage| UC4[Delete Employee]
    Admin -->|Manage| UC5[View All Employees]
    Admin -->|Track| UC6[Mark Attendance]
    Admin -->|Track| UC7[View Attendance Records]
    Admin -->|Process| UC8[Generate Payroll]
    Admin -->|Process| UC9[View All Payrolls]
    Admin -->|Handle| UC10[View Leave Requests]
    Admin -->|Handle| UC11[Approve/Reject Leave]
    Admin -->|Manage| UC12[Add Department]
    
    Employee -->|Login| UC13[Authenticate Employee]
    Employee -->|View| UC14[View Own Profile]
    Employee -->|View| UC15[View Attendance History]
    Employee -->|Apply| UC16[Submit Leave Request]
    Employee -->|View| UC17[View Leave Status]
    Employee -->|View| UC18[View Payroll Slips]
    
    UC1 -.-> System
    UC2 -.-> System
    UC3 -.-> System
    UC4 -.-> System
    UC5 -.-> System
    UC6 -.-> System
    UC7 -.-> System
    UC8 -.-> System
    UC9 -.-> System
    UC10 -.-> System
    UC11 -.-> System
    UC12 -.-> System
    UC13 -.-> System
    UC14 -.-> System
    UC15 -.-> System
    UC16 -.-> System
    UC17 -.-> System
    UC18 -.-> System
    
    style Admin fill:#ff9999
    style Employee fill:#99ccff
    style System fill:#99ff99
```

---

## 🏗️ 6. Architecture Diagram (Layered Architecture)

```
┌─────────────────────────────────────────────────────────────────┐
│                     PRESENTATION LAYER                          │
│  ┌──────────────────┐              ┌──────────────────┐        │
│  │   AdminApp.java  │              │ EmployeeApp.java │        │
│  │  (Console UI)    │              │  (Console UI)    │        │
│  └──────────────────┘              └──────────────────┘        │
└─────────────────────────────────────────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                     CONTROLLER LAYER                            │
│  ┌──────────────────────┐      ┌────────────────────────┐      │
│  │ AdminController.java │      │ EmployeeController.java│      │
│  └──────────────────────┘      └────────────────────────┘      │
└─────────────────────────────────────────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                      SERVICE LAYER                              │
│  ┌─────────────────────┐       ┌──────────────────┐            │
│  │ PayrollService.java │       │ PayrollJobs.java │            │
│  │ (Business Logic)    │       │ (Batch Job)      │            │
│  └─────────────────────┘       └──────────────────┘            │
└─────────────────────────────────────────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                       DAO LAYER                                 │
│  ┌────────────┐  ┌──────────────┐  ┌───────────────┐          │
│  │EmployeeDAO │  │DepartmentDAO │  │  AdminDAO     │          │
│  └────────────┘  └──────────────┘  └───────────────┘          │
│  ┌────────────┐  ┌──────────────┐  ┌───────────────┐          │
│  │AttendanceDA│  │LeaveRequestDA│  │  PayrollDAO   │          │
│  └────────────┘  └──────────────┘  └───────────────┘          │
│  ┌────────────────────────────────────────────────┐            │
│  │         DynamoDBService (Optional)             │            │
│  └────────────────────────────────────────────────┘            │
└─────────────────────────────────────────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                   DATABASE CONNECTION                           │
│  ┌──────────────────────────────────────────────┐              │
│  │           DBConnection.java                   │              │
│  │         (JDBC Connection Manager)             │              │
│  └──────────────────────────────────────────────┘              │
└─────────────────────────────────────────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                      DATA LAYER                                 │
│  ┌──────────────┐                  ┌──────────────┐            │
│  │    MySQL     │                  │  DynamoDB    │            │
│  │  hrms_project│                  │  (Optional)  │            │
│  │              │                  │              │            │
│  │ - admins     │                  │- leave_req   │            │
│  │ - departments│                  │              │            │
│  │ - employees  │                  │              │            │
│  │ - attendance │                  │              │            │
│  │ - leave_req  │                  │              │            │
│  │ - payrolls   │                  │              │            │
│  └──────────────┘                  └──────────────┘            │
└─────────────────────────────────────────────────────────────────┘
```

---

## 📊 7. Entity Relationship Diagram (ERD)

```mermaid
erDiagram
    ADMINS {
        int id PK
        varchar username UK
        varchar password
    }
    
    DEPARTMENTS {
        int id PK
        varchar name UK
    }
    
    EMPLOYEES {
        int id PK
        varchar name
        int department_id FK
        double salary
        varchar email
        varchar phone_number
        date date_of_joining
        varchar password
    }
    
    ATTENDANCE {
        int id PK
        int employee_id FK
        date date
        varchar status
    }
    
    LEAVE_REQUESTS {
        int id PK
        int employee_id FK
        date start_date
        date end_date
        varchar reason
        varchar status
    }
    
    PAYROLLS {
        int id PK
        int employee_id FK
        double basic
        double hra
        double allowances
        double deductions
        double net_salary
        date pay_date
    }
    
    DEPARTMENTS ||--o{ EMPLOYEES : "has"
    EMPLOYEES ||--o{ ATTENDANCE : "has"
    EMPLOYEES ||--o{ LEAVE_REQUESTS : "submits"
    EMPLOYEES ||--o{ PAYROLLS : "receives"
```

---

## 🔄 8. Activity Diagram - Payroll Generation Process

```mermaid
flowchart TD
    Start([Admin Starts Payroll Generation])
    Login{Admin<br/>Login Valid?}
    SelectMenu[Select Generate Payroll Option]
    InputEmpId[Enter Employee ID]
    FetchEmp[Fetch Employee from Database]
    EmpExists{Employee<br/>Exists?}
    
    CalcBasic[Calculate Monthly Basic = Annual Salary / 12]
    CalcHRA[Calculate HRA = Basic × 0.20]
    CalcAllow[Calculate Allowances = Basic × 0.10]
    CalcDeduct[Calculate Deductions = Basic × 0.05]
    CalcNet[Calculate Net Salary = Basic + HRA + Allowances - Deductions]
    
    CreatePayroll[Create Payroll Object]
    SaveDB[Save Payroll to Database]
    DisplaySlip[Display Payslip to Admin]
    ErrorMsg[Display Error Message]
    End([End])
    
    Start --> Login
    Login -->|Yes| SelectMenu
    Login -->|No| ErrorMsg
    SelectMenu --> InputEmpId
    InputEmpId --> FetchEmp
    FetchEmp --> EmpExists
    
    EmpExists -->|Yes| CalcBasic
    EmpExists -->|No| ErrorMsg
    
    CalcBasic --> CalcHRA
    CalcHRA --> CalcAllow
    CalcAllow --> CalcDeduct
    CalcDeduct --> CalcNet
    CalcNet --> CreatePayroll
    CreatePayroll --> SaveDB
    SaveDB --> DisplaySlip
    DisplaySlip --> End
    ErrorMsg --> End
    
    style Start fill:#90EE90
    style End fill:#FFB6C1
    style Login fill:#FFE4B5
    style EmpExists fill:#FFE4B5
    style CalcBasic fill:#87CEEB
    style CalcHRA fill:#87CEEB
    style CalcAllow fill:#87CEEB
    style CalcDeduct fill:#87CEEB
    style CalcNet fill:#87CEEB
    style SaveDB fill:#DDA0DD
```

---

## 🔄 9. State Diagram - Leave Request Lifecycle

```mermaid
stateDiagram-v2
    [*] --> Draft : Employee Creates Request
    Draft --> Submitted : Employee Submits
    Submitted --> UnderReview : Admin Opens Request
    UnderReview --> Approved : Admin Approves
    UnderReview --> Rejected : Admin Rejects
    Approved --> [*] : Leave Processed
    Rejected --> [*] : Request Closed
    
    note right of Draft
        Leave request created
        with start date, end date,
        and reason
    end note
    
    note right of Submitted
        Status: Pending
        Waiting for admin review
    end note
    
    note right of Approved
        Status: Approved
        Employee can take leave
    end note
    
    note right of Rejected
        Status: Rejected
        Leave request denied
    end note
```

---

## 📊 10. Component Diagram

```mermaid
graph TB
    subgraph "Presentation Layer"
        AdminUI[Admin Console UI]
        EmpUI[Employee Console UI]
    end
    
    subgraph "Controller Layer"
        AdminCtrl[AdminController]
        EmpCtrl[EmployeeController]
    end
    
    subgraph "Service Layer"
        PayrollSvc[PayrollService]
        PayrollJob[PayrollJobs]
    end
    
    subgraph "Data Access Layer"
        EmployeeDAO[EmployeeDAO]
        DeptDAO[DepartmentDAO]
        AdminDAO[AdminDAO]
        AttendDAO[AttendanceDAO]
        LeaveDAO[LeaveRequestDAO]
        PayrollDAO[PayrollDAO]
        DynamoDB[DynamoDBService]
    end
    
    subgraph "Database Layer"
        DBConn[DBConnection]
        MySQL[(MySQL Database)]
        DynamoDBCloud[(AWS DynamoDB)]
    end
    
    AdminUI --> AdminCtrl
    EmpUI --> EmpCtrl
    
    AdminCtrl --> EmployeeDAO
    AdminCtrl --> DeptDAO
    AdminCtrl --> AdminDAO
    AdminCtrl --> AttendDAO
    AdminCtrl --> LeaveDAO
    AdminCtrl --> PayrollSvc
    
    EmpCtrl --> EmployeeDAO
    EmpCtrl --> AttendDAO
    EmpCtrl --> LeaveDAO
    EmpCtrl --> PayrollDAO
    
    PayrollSvc --> EmployeeDAO
    PayrollSvc --> PayrollDAO
    PayrollJob --> PayrollSvc
    
    EmployeeDAO --> DBConn
    DeptDAO --> DBConn
    AdminDAO --> DBConn
    AttendDAO --> DBConn
    LeaveDAO --> DBConn
    PayrollDAO --> DBConn
    
    DBConn --> MySQL
    DynamoDB --> DynamoDBCloud
    
    LeaveDAO -.-> DynamoDB
    
    style AdminUI fill:#FFE4E1
    style EmpUI fill:#E0FFFF
    style PayrollSvc fill:#F0E68C
    style MySQL fill:#98FB98
    style DynamoDBCloud fill:#DDA0DD
```

---

## 📝 Key Observations

### **Design Patterns Used:**

1. **DAO Pattern (Data Access Object)**
   - Separates business logic from database operations
   - Each entity has its own DAO class

2. **Singleton Pattern**
   - `DBConnection` ensures only one database connection instance

3. **Service Layer Pattern**
   - `PayrollService` encapsulates business logic for payroll calculations

4. **MVC-like Architecture**
   - Model: Entity classes (Employee, Department, etc.)
   - Controller: AdminController, EmployeeController
   - View: Console-based UI in AdminApp and EmployeeApp

### **Key Features:**

- ✅ Pure Java backend (no frameworks)
- ✅ JDBC for MySQL connectivity
- ✅ Optional DynamoDB integration for scalability
- ✅ Modular layered architecture
- ✅ Separation of concerns (DAO, Service, Controller layers)
- ✅ Role-based access (Admin vs Employee)

---

## 🔗 How to Use These Diagrams

1. **For Presentation:** Copy the Mermaid code and paste it into:
   - GitHub README (renders automatically)
   - Mermaid Live Editor: https://mermaid.live/
   - VS Code with Mermaid extension

2. **For Documentation:** Include these diagrams in your project report or thesis

3. **For Interview:** Use these to explain your project architecture to the panel

---

**Note:** These diagrams are generated using Mermaid syntax. You can render them in any Mermaid-compatible viewer or documentation tool.
