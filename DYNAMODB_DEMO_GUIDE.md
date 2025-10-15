# DynamoDB Setup & Demonstration Guide

## 🚀 Setting Up DynamoDB Local & NoSQL Workbench

This guide shows you how to set up and demonstrate the optional DynamoDB integration in your HRMS project.

---

## 📋 Prerequisites

1. **Java 21** (Already installed ✓)
2. **AWS CLI** (Optional, for cloud DynamoDB)
3. **NoSQL Workbench** (We'll install this)
4. **DynamoDB Local** (For local testing)

---

## 🔧 Part 1: Install DynamoDB Local

### Option A: Using Docker (Recommended)

```bash
# Pull DynamoDB Local image
docker pull amazon/dynamodb-local

# Run DynamoDB Local on port 8000
docker run -p 8000:8000 amazon/dynamodb-local
```

### Option B: Download JAR File

1. **Download DynamoDB Local:**
   - Visit: https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.DownloadingAndRunning.html
   - Download the `.tar.gz` or `.zip` file

2. **Extract and Run:**
```bash
# Extract the archive
tar -xvf dynamodb_local_latest.tar.gz

# Run DynamoDB Local
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb
```

3. **Verify it's running:**
   - Open browser: http://localhost:8000/shell/
   - You should see the DynamoDB JavaScript shell

---

## 🔧 Part 2: Install NoSQL Workbench

### Step 1: Download NoSQL Workbench

1. Visit: https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/workbench.html
2. Download for Windows
3. Run the installer: `NoSQLWorkbench-win-3.x.x.exe`
4. Follow installation wizard

### Step 2: Launch NoSQL Workbench

```powershell
# Or launch from Start Menu
Start-Process "NoSQL Workbench"
```

---

## 🔗 Part 3: Connect NoSQL Workbench to DynamoDB Local

### Step 1: Add Connection

1. Open **NoSQL Workbench**
2. Click **"Operation builder"** (left sidebar)
3. Click **"Add connection"** (top right)
4. Select **"DynamoDB local"**

### Step 2: Configure Connection

**Connection Settings:**
```
Connection name: HRMS Local
Connection type: DynamoDB local
Host: localhost
Port: 8000
```

Click **"Connect"**

### Step 3: Verify Connection

- You should see "Connected" status
- Left panel shows "No tables" (we'll create them next)

---

## 🗄️ Part 4: Create DynamoDB Tables for HRMS

### Option A: Using NoSQL Workbench GUI

#### 1. Create `leave_requests` Table

1. In NoSQL Workbench, click **"Data modeler"** (left sidebar)
2. Click **"Create data model"**
3. **Model name:** HRMS_Leave_Management
4. Click **"Add table"**

**Table Settings:**
```
Table name: leave_requests
Partition key: id (Number)
Billing mode: On-demand (PAY_PER_REQUEST)
```

**Attributes:**
- id (Number) - Partition Key
- employee_id (Number)
- start_date (String)
- end_date (String)
- reason (String)
- status (String)

5. Click **"Commit to DynamoDB Local"**

---

### Option B: Using AWS CLI (Automated)

**PowerShell Script:**

```powershell
# Set endpoint to local DynamoDB
$endpoint = "http://localhost:8000"

# Create leave_requests table
aws dynamodb create-table `
    --table-name leave_requests `
    --attribute-definitions `
        AttributeName=id,AttributeType=N `
        AttributeName=employee_id,AttributeType=N `
    --key-schema `
        AttributeName=id,KeyType=HASH `
    --global-secondary-indexes `
        "[{
            `"IndexName`": `"employee_id-index`",
            `"KeySchema`": [{`"AttributeName`":`"employee_id`",`"KeyType`":`"HASH`"}],
            `"Projection`": {`"ProjectionType`":`"ALL`"},
            `"ProvisionedThroughput`": {`"ReadCapacityUnits`": 5, `"WriteCapacityUnits`": 5}
        }]" `
    --billing-mode PAY_PER_REQUEST `
    --endpoint-url $endpoint

Write-Host "✅ Table created successfully!"
```

---

### Option C: Using Java Code (Project Method)

**Create this file:** `src/main/java/com/hrms/model/dao/DynamoDBSetup.java`

```java
package com.hrms.model.dao;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.net.URI;

public class DynamoDBSetup {
    
    public static void main(String[] args) {
        // Connect to local DynamoDB
        DynamoDbClient dynamoDb = DynamoDbClient.builder()
                .endpointOverride(URI.create("http://localhost:8000"))
                .region(Region.US_EAST_1)
                .build();
        
        createLeaveRequestsTable(dynamoDb);
        System.out.println("✅ DynamoDB tables created successfully!");
    }
    
    public static void createLeaveRequestsTable(DynamoDbClient dynamoDb) {
        try {
            CreateTableRequest request = CreateTableRequest.builder()
                    .tableName("leave_requests")
                    .attributeDefinitions(
                            AttributeDefinition.builder()
                                    .attributeName("id")
                                    .attributeType(ScalarAttributeType.N)
                                    .build(),
                            AttributeDefinition.builder()
                                    .attributeName("employee_id")
                                    .attributeType(ScalarAttributeType.N)
                                    .build()
                    )
                    .keySchema(
                            KeySchemaElement.builder()
                                    .attributeName("id")
                                    .keyType(KeyType.HASH)
                                    .build()
                    )
                    .globalSecondaryIndexes(
                            GlobalSecondaryIndex.builder()
                                    .indexName("employee_id-index")
                                    .keySchema(
                                            KeySchemaElement.builder()
                                                    .attributeName("employee_id")
                                                    .keyType(KeyType.HASH)
                                                    .build()
                                    )
                                    .projection(Projection.builder()
                                            .projectionType(ProjectionType.ALL)
                                            .build())
                                    .build()
                    )
                    .billingMode(BillingMode.PAY_PER_REQUEST)
                    .build();
            
            dynamoDb.createTable(request);
            System.out.println("✅ Table 'leave_requests' created!");
            
        } catch (ResourceInUseException e) {
            System.out.println("ℹ️ Table already exists, skipping...");
        } catch (Exception e) {
            System.err.println("❌ Error creating table: " + e.getMessage());
        }
    }
}
```

**Run it:**
```bash
# Compile and run
mvn clean compile
java -cp target/classes com.hrms.model.dao.DynamoDBSetup
```

---

## 🧪 Part 5: Test DynamoDB Integration

### Step 1: Update DynamoDBService Configuration

Check `src/main/java/com/hrms/model/dao/DynamoDBService.java`:

```java
public DynamoDBService() {
    try {
        this.dynamoDbClient = DynamoDbClient.builder()
                .endpointOverride(URI.create("http://localhost:8000"))  // Local DynamoDB
                .region(Region.US_EAST_1)
                .build();
        this.isAvailable = true;
        System.out.println("✅ DynamoDB Local connected!");
    } catch (Exception e) {
        this.isAvailable = false;
        System.out.println("⚠️ DynamoDB not available, using MySQL only");
    }
}
```

### Step 2: Test Adding Leave Request

**Create test file:** `TestDynamoDB.java`

```java
package com.hrms.model.test;

import com.hrms.model.LeaveRequest;
import com.hrms.model.dao.DynamoDBService;

import java.time.LocalDate;

public class TestDynamoDB {
    public static void main(String[] args) {
        DynamoDBService dynamoDB = new DynamoDBService();
        
        // Create test leave request
        LeaveRequest leave = new LeaveRequest();
        leave.setId(1);
        leave.setEmployeeId(1);
        leave.setStartDate(LocalDate.now().plusDays(7));
        leave.setEndDate(LocalDate.now().plusDays(9));
        leave.setReason("Testing DynamoDB integration");
        leave.setStatus("Pending");
        
        // Add to DynamoDB
        dynamoDB.addLeaveRequest(leave);
        System.out.println("✅ Leave request added to DynamoDB!");
        
        // Retrieve from DynamoDB
        var leaves = dynamoDB.getLeaveRequestsByEmployee(1);
        System.out.println("📋 Found " + leaves.size() + " leave requests");
        
        for (LeaveRequest l : leaves) {
            System.out.println("  - " + l.getReason() + " (" + l.getStatus() + ")");
        }
    }
}
```

**Run:**
```bash
mvn clean compile
java -cp target/classes:target/dependency/* com.hrms.model.test.TestDynamoDB
```

---

## 📊 Part 6: View Data in NoSQL Workbench

### Step 1: Switch to Operation Builder

1. Click **"Operation builder"** in left sidebar
2. Select your connection: **HRMS Local**
3. You should see the `leave_requests` table

### Step 2: View Table Data

1. Click on **`leave_requests`** table
2. Click **"Scan"** button
3. You'll see all records in a table view

### Step 3: Query Data

**Example: Find all leave requests for employee 1**

1. Click **"Query"** tab
2. Select index: **employee_id-index**
3. Enter: `employee_id = 1`
4. Click **"Run"**

### Step 4: Add Data Manually

1. Click **"Add item"** button
2. Fill in attributes:
```json
{
  "id": 2,
  "employee_id": 1,
  "start_date": "2025-10-20",
  "end_date": "2025-10-22",
  "reason": "Family function",
  "status": "Pending"
}
```
3. Click **"Save"**

---

## 🎤 Part 7: Demonstration Script for Panel

### **During Your Presentation:**

**1. Show MySQL Database (Primary Storage)**
```sql
mysql -u root -proot hrms_project
SELECT * FROM leave_requests;
```
*"This is our primary database using MySQL where all core data is stored."*

---

**2. Show DynamoDB Local Running**
```powershell
# Open browser
Start-Process "http://localhost:8000/shell/"
```
*"For scalability, we've integrated DynamoDB as an optional NoSQL database for leave requests."*

---

**3. Show NoSQL Workbench Connection**
- Open NoSQL Workbench
- Show connected to localhost:8000
- Display `leave_requests` table

*"Using AWS NoSQL Workbench, we can visualize and manage our DynamoDB data."*

---

**4. Demonstrate Sync Between MySQL and DynamoDB**

**Run EmployeeApp:**
```bash
java -jar target/hrms_project-1.0-SNAPSHOT-shaded.jar
```

**Apply for leave as employee:**
- Login as employee 1
- Apply for leave
- Check both databases:

**MySQL:**
```sql
SELECT * FROM leave_requests WHERE employee_id=1;
```

**DynamoDB (NoSQL Workbench):**
- Refresh table view
- Show same data synced

*"As you can see, the leave request is stored in both MySQL and DynamoDB, demonstrating our hybrid database approach."*

---

**5. Show Graceful Degradation**

**Stop DynamoDB:**
```powershell
# Stop Docker container or kill process
docker stop <container_id>
```

**Run application again:**
```bash
java -jar target/hrms_project-1.0-SNAPSHOT-shaded.jar
```

*"Notice the application still works perfectly with MySQL only. DynamoDB is optional and the system gracefully degrades if it's unavailable."*

---

## 🎯 Key Points for Your Presentation

### Why DynamoDB?

✅ **Scalability:** NoSQL can handle millions of records
✅ **Performance:** Low-latency reads/writes
✅ **Flexibility:** Schema-less design for evolving requirements
✅ **Cloud-Ready:** Easy to migrate from local to AWS cloud

### Hybrid Approach Benefits:

✅ **MySQL:** Structured data (employees, departments, payroll)
✅ **DynamoDB:** Semi-structured data (leave requests, logs)
✅ **Best of both worlds:** Relational + NoSQL

### Demonstration Flow:

1. Show MySQL tables (core data)
2. Show DynamoDB Local running
3. Show NoSQL Workbench interface
4. Apply for leave (syncs to both)
5. Query from both databases
6. Stop DynamoDB → show graceful degradation

---

## 📁 Quick Reference Commands

### Start DynamoDB Local (Docker):
```bash
docker run -p 8000:8000 amazon/dynamodb-local
```

### Start DynamoDB Local (JAR):
```bash
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb
```

### Check if DynamoDB is running:
```powershell
Invoke-WebRequest http://localhost:8000 -UseBasicParsing
```

### List all tables:
```bash
aws dynamodb list-tables --endpoint-url http://localhost:8000
```

### Scan table:
```bash
aws dynamodb scan --table-name leave_requests --endpoint-url http://localhost:8000
```

---

## 🆘 Troubleshooting

### Issue: DynamoDB not connecting

**Solution:**
```java
// Add this to DynamoDBService constructor:
System.setProperty("aws.accessKeyId", "dummy");
System.setProperty("aws.secretAccessKey", "dummy");
```

### Issue: NoSQL Workbench can't connect

**Check:**
1. DynamoDB Local is running (http://localhost:8000)
2. Port 8000 is not blocked by firewall
3. Connection settings: localhost:8000

### Issue: Table already exists error

**Solution:**
```bash
# Delete table first
aws dynamodb delete-table --table-name leave_requests --endpoint-url http://localhost:8000

# Then recreate
```

---

## 📚 Additional Resources

- **DynamoDB Local Docs:** https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.html
- **NoSQL Workbench:** https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/workbench.html
- **AWS DynamoDB SDK for Java:** https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/examples-dynamodb.html

---

## ✅ Checklist for Presentation

- [ ] DynamoDB Local installed and running
- [ ] NoSQL Workbench installed and connected
- [ ] `leave_requests` table created in DynamoDB
- [ ] Test data added to both MySQL and DynamoDB
- [ ] Application configured to use local endpoint
- [ ] Tested leave request flow (MySQL + DynamoDB sync)
- [ ] Tested graceful degradation (stop DynamoDB)
- [ ] Screenshots of NoSQL Workbench ready

---

**You're now ready to demonstrate the NoSQL integration! 🚀**

**Pro tip:** Practice the demo flow 2-3 times before the actual presentation to ensure smooth execution.
