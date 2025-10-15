# Quick DynamoDB Local Setup (Windows)

## Option 1: Using Docker (Fastest)

```powershell
# Pull and run DynamoDB Local
docker pull amazon/dynamodb-local
docker run -d -p 8000:8000 --name dynamodb-local amazon/dynamodb-local

# Verify it's running
Invoke-WebRequest http://localhost:8000 -UseBasicParsing

# Stop when done
docker stop dynamodb-local

# Start again later
docker start dynamodb-local
```

## Option 2: Using JAR File

### Download DynamoDB Local

```powershell
# Create directory
New-Item -ItemType Directory -Force -Path "C:\DynamoDBLocal"
cd C:\DynamoDBLocal

# Download from AWS (use browser or wget)
# URL: https://d1ni2b6xgvw0s0.cloudfront.net/v2.x/dynamodb_local_latest.tar.gz

# Extract
tar -xzf dynamodb_local_latest.tar.gz

# Run DynamoDB Local
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb -port 8000
```

### Keep it running in background:

```powershell
# Run in new window
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd C:\DynamoDBLocal; java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb"
```

## Install NoSQL Workbench

1. Download: https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/workbench.html
2. Install: Run `NoSQLWorkbench-win-x.x.x.exe`
3. Launch from Start Menu

## Connect NoSQL Workbench

1. Open NoSQL Workbench
2. Click "Operation builder"
3. Click "Add connection" → "DynamoDB local"
4. Settings:
   - Name: HRMS Local
   - Host: localhost
   - Port: 8000
5. Click "Connect"

## Test Connection

```powershell
# Using AWS CLI (if installed)
aws dynamodb list-tables --endpoint-url http://localhost:8000

# Using PowerShell
Invoke-RestMethod -Uri "http://localhost:8000" -Method Get
```

## Run HRMS with DynamoDB

```powershell
# Make sure DynamoDB Local is running first!

# Run your application
java -jar target/hrms_project-1.0-SNAPSHOT-shaded.jar

# You should see: "✅ DynamoDB Table already exists: LeaveRequests"
```

## Troubleshooting

### Port 8000 already in use?
```powershell
# Find what's using port 8000
netstat -ano | findstr :8000

# Kill that process (use PID from above)
taskkill /PID <PID> /F

# Or use different port
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb -port 8001
```

### Can't connect from Java?
Check firewall settings:
```powershell
# Allow port 8000
New-NetFirewallRule -DisplayName "DynamoDB Local" -Direction Inbound -LocalPort 8000 -Protocol TCP -Action Allow
```

## Quick Start Checklist

- [ ] DynamoDB Local running on port 8000
- [ ] Can access http://localhost:8000 in browser
- [ ] NoSQL Workbench installed
- [ ] NoSQL Workbench connected to localhost:8000
- [ ] Run HRMS app - see "✅ DynamoDB Table already exists"
- [ ] View LeaveRequests table in NoSQL Workbench

**You're ready! See DYNAMODB_DEMO_GUIDE.md for full demonstration instructions.**
