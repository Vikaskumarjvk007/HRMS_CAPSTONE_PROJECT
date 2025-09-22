package com.hrms.model.dao;

import com.hrms.model.LeaveRequest;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.*;

public class DynamoDBService {
    private final DynamoDbClient dynamoDb;
    private final String tableName = "LeaveRequests";

    public DynamoDBService() {
        dynamoDb = DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create("dummyKey", "dummySecret")
                        )
                )
                .endpointOverride(URI.create("http://localhost:8000")) // Local DynamoDB
                .build();

        ensureTableExists();
    }

    // ✅ Ensure table exists
    private void ensureTableExists() {
        try {
            dynamoDb.describeTable(DescribeTableRequest.builder()
                    .tableName(tableName)
                    .build());
            System.out.println("✅ DynamoDB Table already exists: " + tableName);
        } catch (ResourceNotFoundException e) {
            System.out.println("⚠️ Table not found. Creating: " + tableName);

            CreateTableRequest request = CreateTableRequest.builder()
                    .tableName(tableName)
                    .keySchema(KeySchemaElement.builder()
                            .attributeName("requestId")
                            .keyType(KeyType.HASH) // Partition key
                            .build()
                    )
                    .attributeDefinitions(AttributeDefinition.builder()
                            .attributeName("requestId")
                            .attributeType(ScalarAttributeType.S)
                            .build()
                    )
                    .provisionedThroughput(ProvisionedThroughput.builder()
                            .readCapacityUnits(5L)
                            .writeCapacityUnits(5L)
                            .build()
                    )
                    .build();

            dynamoDb.createTable(request);
            System.out.println("✅ Table created successfully!");
        }
    }

    // ➕ Add Leave Request (use requestId from object)
    public void addLeaveRequest(LeaveRequest lr) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("requestId", AttributeValue.builder().s(lr.getRequestId()).build()); // use requestId from object
        item.put("employeeId", AttributeValue.builder().n(String.valueOf(lr.getEmployeeId())).build());
        item.put("startDate", AttributeValue.builder().s(lr.getStartDate().toString()).build());
        item.put("endDate", AttributeValue.builder().s(lr.getEndDate().toString()).build());
        item.put("reason", AttributeValue.builder().s(lr.getReason()).build());
        item.put("status", AttributeValue.builder().s(lr.getStatus()).build());
        if (lr.getEmployeeName() != null) {
            item.put("employeeName", AttributeValue.builder().s(lr.getEmployeeName()).build());
        }

        dynamoDb.putItem(PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build());

        System.out.println("✅ Leave request added with Request ID " + lr.getRequestId());
    }

    // 📋 Get All Leave Requests
    public List<LeaveRequest> getAllLeaveRequests() {
        List<LeaveRequest> list = new ArrayList<>();
        ScanResponse response = dynamoDb.scan(ScanRequest.builder()
                .tableName(tableName)
                .build());

        for (Map<String, AttributeValue> item : response.items()) {
            list.add(mapToLeaveRequest(item));
        }
        return list;
    }

    // 📋 Get Leave Requests by Employee ID
    public List<LeaveRequest> getLeaveRequestsByEmployee(int employeeId) {
        List<LeaveRequest> list = new ArrayList<>();
        ScanResponse response = dynamoDb.scan(ScanRequest.builder()
                .tableName(tableName)
                .build());

        for (Map<String, AttributeValue> item : response.items()) {
            LeaveRequest lr = mapToLeaveRequest(item);
            if (lr.getEmployeeId() == employeeId) {
                list.add(lr);
            }
        }
        return list;
    }

    // 📋 Get All Pending Requests (for Admin)
    public List<LeaveRequest> getAllPendingLeaveRequests() {
        List<LeaveRequest> pending = new ArrayList<>();
        ScanResponse response = dynamoDb.scan(ScanRequest.builder()
                .tableName(tableName)
                .build());

        for (Map<String, AttributeValue> item : response.items()) {
            LeaveRequest lr = mapToLeaveRequest(item);
            if ("PENDING".equalsIgnoreCase(lr.getStatus())) {
                pending.add(lr);
            }
        }
        return pending;
    }

    // ✏️ Update Leave Status by employeeId + startDate
    public void updateLeaveStatus(int employeeId, LocalDate startDate, String status) {
        String requestId = getRequestIdForLeave(employeeId, startDate);

        if (requestId == null) {
            System.out.println("❌ Could not find leave request for Employee ID " + employeeId + " on " + startDate);
            return;
        }

        dynamoDb.updateItem(UpdateItemRequest.builder()
                .tableName(tableName)
                .key(Map.of("requestId", AttributeValue.builder().s(requestId).build()))
                .updateExpression("SET #s = :status")
                .expressionAttributeNames(Map.of("#s", "status"))
                .expressionAttributeValues(Map.of(":status", AttributeValue.builder().s(status).build()))
                .build());

        System.out.println("✅ Leave status updated to " + status + " for Employee ID " + employeeId + " (Start: " + startDate + ")");
    }

    // ❌ Delete Leave Request by requestId
    public void deleteLeaveRequest(String requestId) {
        dynamoDb.deleteItem(DeleteItemRequest.builder()
                .tableName(tableName)
                .key(Map.of("requestId", AttributeValue.builder().s(requestId).build()))
                .build());

        System.out.println("✅ Leave request deleted with Request ID " + requestId);
    }

    // 📋 Get Leave Requests by Status
    public List<LeaveRequest> getLeaveRequestsByStatus(String status) {
        List<LeaveRequest> list = new ArrayList<>();
        ScanResponse response = dynamoDb.scan(ScanRequest.builder()
                .tableName(tableName)
                .build());

        for (Map<String, AttributeValue> item : response.items()) {
            LeaveRequest lr = mapToLeaveRequest(item);
            if (lr.getStatus().equalsIgnoreCase(status)) {
                list.add(lr);
            }
        }
        return list;
    }

    // 🔄 Helper: Convert DynamoDB Item → LeaveRequest Object
    private LeaveRequest mapToLeaveRequest(Map<String, AttributeValue> item) {
        LeaveRequest lr = new LeaveRequest(
                item.get("requestId").s(), // ✅ use requestId from DB
                Integer.parseInt(item.get("employeeId").n()),
                LocalDate.parse(item.get("startDate").s()),
                LocalDate.parse(item.get("endDate").s()),
                item.get("reason").s(),
                item.get("status").s()
        );

        if (item.containsKey("employeeName")) {
            lr.setEmployeeName(item.get("employeeName").s());
        }

        return lr;
    }

    // 🔑 Helper: Find requestId from employeeId + startDate
    private String getRequestIdForLeave(int employeeId, LocalDate startDate) {
        ScanResponse response = dynamoDb.scan(ScanRequest.builder()
                .tableName(tableName)
                .build());

        for (Map<String, AttributeValue> item : response.items()) {
            int empId = Integer.parseInt(item.get("employeeId").n());
            LocalDate sDate = LocalDate.parse(item.get("startDate").s());

            if (empId == employeeId && sDate.equals(startDate)) {
                return item.get("requestId").s();
            }
        }
        return null;
    }
}
